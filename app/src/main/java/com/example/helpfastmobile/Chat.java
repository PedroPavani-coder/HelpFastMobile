package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Chat {

    @SerializedName("id")
    private int id;

    @SerializedName("chamadoId")
    private Integer chamadoId;

    // --- CAMPOS ADICIONADOS PARA UM CHAT FUNCIONAL ---

    @SerializedName(value="motivo", alternate={"texto", "mensagem"})
    private String motivo; // O texto da mensagem

    @SerializedName("usuarioId")
    private Integer usuarioId; // Quem enviou a mensagem

    @SerializedName(value="dataEnvio", alternate={"createdAt"})
    private Date dataEnvio; // Quando foi enviada

    // Getters and Setters
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

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
