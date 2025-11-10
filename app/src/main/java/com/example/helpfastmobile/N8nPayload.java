package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

/**
 * Representa o payload (corpo de dados) a ser enviado para o webhook do n8n.
 */
public class N8nPayload {

    @SerializedName("text")
    private String text;

    public N8nPayload(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
