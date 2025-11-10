package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

public class LoginDbo {

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String senha;

    public LoginDbo(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
