package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

public class UpdateStatusDto {

    @SerializedName("status")
    private String status;

    @SerializedName("tecnicoId")
    private Integer tecnicoId;

    public UpdateStatusDto(String status, Integer tecnicoId) {
        this.status = status;
        this.tecnicoId = tecnicoId;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Integer tecnicoId) {
        this.tecnicoId = tecnicoId;
    }
}
