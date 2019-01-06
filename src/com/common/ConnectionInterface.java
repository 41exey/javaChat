package com.common;

public interface ConnectionInterface {
    int PORT = 8000;
    String IP = "127.0.0.1";
    void send(MessageInterface message);
    void close();
}
