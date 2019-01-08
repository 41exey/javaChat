package com.common;

import java.net.Socket;

public interface ConnectionInterface {
    int PORT = 8000;
    String IP = "127.0.0.1";
    void send(MessageInterface message);
    void close();
    Socket getSocket();
    void setNick(String nick);
    String getNick();
}
