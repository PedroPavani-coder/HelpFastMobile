package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

public class AbrirChamadoDto {

    @SerializedName("clienteId")
    private int clienteId;

    @SerializedName("motivo")
    private String motivo;

    public AbrirChamadoDto(int clienteId, String motivo) {
        this.clienteId = clienteId;
        this.motivo = motivo;
    }

    // Getters and Setters
    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
