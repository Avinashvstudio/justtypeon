package com.example.justtypeon.Model;


public class GroupChat {
    private String senderId; // Sender's user ID
    private String senderName; // Sender's name
    private String groupName; // Name of the group
    private String message;
    private boolean isSeen;

    public GroupChat(String senderId, String senderName, String groupName, String message, boolean isSeen) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.groupName = groupName;
        this.message = message;
        this.isSeen = isSeen;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
