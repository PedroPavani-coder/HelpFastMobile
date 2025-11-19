package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de dados para uma mensagem de Chat.
 * Mapeia os campos retornados pela API para um objeto Java.
 */
public class Chat {

    @SerializedName("id")
    private int id;

    @SerializedName("chamadoId")
    private Integer chamadoId;

    @SerializedName("mensagem")
    private String mensagem;

    @SerializedName("remetenteId")
    private Integer remetenteId;

    @SerializedName("destinatarioId")
    private Integer destinatarioId;

    @SerializedName("tipo")
    private String tipo;

    // CORREÇÃO: O campo de data agora é uma String para evitar erros de parsing.
    @SerializedName("dataEnvio")
    private String dataEnvio;

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(Integer chamadoId) {
        this.chamadoId = chamadoId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    // Método adaptado para manter compatibilidade
    public String getMotivo() {
        return mensagem;
    }

    public Integer getRemetenteId() {
        return remetenteId;
    }

    public void setRemetenteId(Integer remetenteId) {
        this.remetenteId = remetenteId;
    }

    // Método adaptado para manter compatibilidade
    public Integer getUsuarioId() {
        return remetenteId;
    }

    public Integer getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Integer destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
