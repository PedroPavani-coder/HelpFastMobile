package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Representa a estrutura completa da resposta da API para o endpoint /api/Chat/all.
 * A API retorna um objeto que contém o total de mensagens e a lista de chats.
 */
public class ChatApiResponse {

    @SerializedName("total")
    private int total;

    @SerializedName("chats")
    private List<Chat> chats;

    // Getters

    public int getTotal() {
        return total;
    }

    public List<Chat> getChats() {
        return chats;
    }
}
