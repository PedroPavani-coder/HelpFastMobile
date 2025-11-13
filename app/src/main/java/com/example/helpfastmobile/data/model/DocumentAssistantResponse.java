package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

public class DocumentAssistantResponse {

    @SerializedName("resposta")
    private String resposta;

    @SerializedName("escalarParaHumano")
    private boolean escalarParaHumano;

    // Getters and Setters
    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isEscalarParaHumano() {
        return escalarParaHumano;
    }

    public void setEscalarParaHumano(boolean escalarParaHumano) {
        this.escalarParaHumano = escalarParaHumano;
    }
}

