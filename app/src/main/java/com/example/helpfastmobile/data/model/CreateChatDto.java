package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * DTO (Data Transfer Object) para criar uma nova mensagem de chat.
 * Este objeto é enviado para a API.
 */
public class CreateChatDto {

    @SerializedName("chamadoId")
    private int chamadoId;

    // O campo "motivo" é usado para armazenar o texto da mensagem.
    @SerializedName("motivo")
    private String motivo;

    @SerializedName("usuarioId")
    private Integer usuarioId;

    // Getters e Setters

    public int getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(int chamadoId) {
        this.chamadoId = chamadoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
