package com.common;

public class Message implements MessageInterface {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
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

    public Message(int id, String nick, String content, int type) {
        this.id = id;
        this.nick = nick;
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return "( " + id + " ) " + nick + ": "
                + content;
    }
}
