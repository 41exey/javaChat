package com.common;

import java.io.Serializable;

public interface MessageInterface extends Serializable {
    int CLOSE_TYPE = 0;
    int CONTENT_TYPE = 1;
    int GET_NICK_TYPE = 2;
    int GET_HISTORY = 3;
    int END_HISTORY = 4;
    int MESSAGES_HISTORY_COUNT = 50;
    String getNick();
    String getContent();
    int getType();
    int getId();
    void setId(int id);
    String toString();
}
