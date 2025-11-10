package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class ChatIaResult {

    @SerializedName("id")
    private int id;

    @SerializedName("chatId")
    private int chatId;

    @SerializedName("resultJson")
    private String resultJson;

    @SerializedName("createdAt")
    private Date createdAt;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
