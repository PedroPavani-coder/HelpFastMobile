package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

public class CreateChatDto {

    @SerializedName("chamadoId")
    private Integer chamadoId;

    @SerializedName("motivo")
    private String motivo;

    @SerializedName("chatId")
    private Integer chatId;

    // Getters and Setters
    public Integer getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(Integer chamadoId) {
        this.chamadoId = chamadoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
