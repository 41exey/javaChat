package com.common;

public interface ConnectionInterface {
    int PORT = 8000;
    void send(MessageInterface message);
    void close();
}
