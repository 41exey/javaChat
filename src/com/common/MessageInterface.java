package com.common;

import java.io.Serializable;

public interface MessageInterface extends Serializable {
    int CLOSE_TYPE = 0;
    int CONTENT_TYPE = 1;
    String getNick();
    String getContent();
    int getType();
}
