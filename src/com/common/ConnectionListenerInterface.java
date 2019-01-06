package com.common;

public interface ConnectionListenerInterface {
    void connectionCreated(ConnectionInterface connection);
    void connectionClosed(ConnectionInterface connection);
    void connectionException(ConnectionInterface connection, Exception e);
    void receivedContent(MessageInterface message);
}
