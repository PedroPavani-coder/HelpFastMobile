package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

// Modelo para representar uma única mensagem no histórico do chat.
public class Mensagem {

    @SerializedName("id")
    private int id;

    @SerializedName("mensagem")
    private String texto;

    @SerializedName("enviadoPorCliente")
    private boolean enviadoPorCliente;

    @SerializedName("dataEnvio")
    private String dataEnvio;

    // Getters
    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEnviadoPorCliente() {
        return enviadoPorCliente;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }
}
