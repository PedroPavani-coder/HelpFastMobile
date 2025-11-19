package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

public class DocumentQuestionRequest {

    @SerializedName("pergunta")
    private String pergunta;

    @SerializedName("usuarioId")
    private Integer usuarioId;

    // Getters and Setters
    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
