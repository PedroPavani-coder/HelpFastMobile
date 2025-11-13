package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

public class DocumentQuestionRequest {

    @SerializedName("pergunta")
    private String pergunta;

    // Getters and Setters
    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }
}
