package com.common;

import java.net.Socket;

public interface ConnectionListenerInterface {
    void connectionCreated(ConnectionInterface connection);
    void connectionClosed(ConnectionInterface connection);
    void connectionException(ConnectionInterface connection, Exception e);
    void receivedContent(ConnectionInterface connection, MessageInterface message);
}
