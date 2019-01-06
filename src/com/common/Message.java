package com.common;

public class Message implements MessageInterface {
    private final String nick;
    private final String content;
    private final int type;

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getType() {
        return type;
    }

    public Message(String nick, String content, int type) {
        this.nick = nick;
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nick='" + nick + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
