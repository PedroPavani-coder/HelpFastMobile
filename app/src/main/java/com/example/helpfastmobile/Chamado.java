package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;
import java.util.Date; // Date ainda pode ser usado para o getter, se necessário, mas o campo será String

public class Chamado {

    @SerializedName("id")
    private int id;

    @SerializedName("tecnicoId")
    private Integer tecnicoId;

    @SerializedName("motivo")
    private String motivo;

    @SerializedName("status")
    private String status;

    // CORREÇÃO: Alterado para String para evitar erros de parsing de data
    @SerializedName("dataAbertura")
    private String dataAbertura;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Integer tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
}
