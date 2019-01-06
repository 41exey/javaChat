package com.common;

import com.client.Client;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Connection implements ConnectionInterface, Runnable {
    private Socket socket;
    private ConnectionListenerInterface connectionListener;
    private boolean needToRun = true;
    private OutputStream out;
    private InputStream in;

    public Connection(Socket socket, ConnectionListenerInterface connectionListener) {
        try {
            this.socket = socket;
            this.connectionListener = connectionListener;
            out = socket.getOutputStream();
            in = socket.getInputStream();
            Thread t = new Thread(this);
            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void send(MessageInterface message) {
        ObjectOutputStream objectOut = null;
        try {
            objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        needToRun = false;
    }

    @Override
    public void run() {
        while (needToRun) {
            try {
                int amount = in.available();
                if (amount != 0) {
                    ObjectInputStream objectIn = new ObjectInputStream(in);
                    MessageInterface message = (MessageInterface) objectIn.readObject();
                    connectionListener.receivedContent(message);
                } else {
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
