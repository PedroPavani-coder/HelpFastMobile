package com.example.helpfastmobile.data.repository;

import android.util.Log;

import com.example.helpfastmobile.data.model.AbrirChamadoDto;
import com.example.helpfastmobile.data.model.Chamado;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.data.model.ChatApiResponse;
import com.example.helpfastmobile.data.model.CreateChatDto;
import com.example.helpfastmobile.data.model.DocumentAssistantResponse;
import com.example.helpfastmobile.data.model.DocumentQuestionRequest;
import com.example.helpfastmobile.data.model.N8nPayload;
import com.example.helpfastmobile.data.model.UpdateStatusDto;
import com.example.helpfastmobile.data.network.ApiClient;
import com.example.helpfastmobile.data.network.ApiService;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChamadoRepository {

    private static final String TAG = "HelpFastDebug";
    private static volatile ChamadoRepository instance;
    private final ApiService apiService;

    private ChamadoRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public static ChamadoRepository getInstance() {
        if (instance == null) {
            synchronized (ChamadoRepository.class) {
                if (instance == null) {
                    instance = new ChamadoRepository();
                }
            }
        }
        return instance;
    }

    public void perguntarDocumentAssistant(String pergunta, Integer usuarioId, DataSourceCallback<DocumentAssistantResponse> callback) {
        DocumentQuestionRequest request = new DocumentQuestionRequest();
        request.setPergunta(pergunta);
        request.setUsuarioId(usuarioId);

        apiService.perguntarDocumentAssistant(request).enqueue(new Callback<DocumentAssistantResponse>() {
            @Override
            public void onResponse(Call<DocumentAssistantResponse> call, Response<DocumentAssistantResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao conectar com a IA.");
                }
            }

            @Override
            public void onFailure(Call<DocumentAssistantResponse> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void getTodosChamados(DataSourceCallback<List<Chamado>> callback) {
        apiService.getTodosChamados().enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao buscar todos os chamados.");
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void getMeusChamados(int clienteId, DataSourceCallback<List<Chamado>> callback) {
        apiService.getMeusChamados(clienteId).enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao buscar meus chamados.");
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void abrirChamado(int clienteId, String motivo, DataSourceCallback<Chamado> callback) {
        apiService.abrirChamado(new AbrirChamadoDto(clienteId, motivo)).enqueue(new Callback<Chamado>() {
            @Override
            public void onResponse(Call<Chamado> call, Response<Chamado> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao abrir chamado.");
                }
            }

            @Override
            public void onFailure(Call<Chamado> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void updateStatusChamado(int chamadoId, String novoStatus, Integer tecnicoId, DataSourceCallback<Void> callback) {
        apiService.updateStatusChamado(chamadoId, new UpdateStatusDto(novoStatus, tecnicoId)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSucesso(null);
                } else {
                    callback.onErro("Falha ao atualizar status.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }

    public void getChat(int chamadoId, DataSourceCallback<List<Chat>> callback) {
        apiService.getChat().enqueue(new Callback<ChatApiResponse>() {
            @Override
            public void onResponse(Call<ChatApiResponse> call, Response<ChatApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse apiResponse = response.body();
                    List<Chat> todosOsChats = apiResponse.getChats();

                    if (todosOsChats != null) {
                        Log.d(TAG, "Recebidos " + todosOsChats.size() + " chats no total da API.");
                        for (int i = 0; i < Math.min(todosOsChats.size(), 3); i++) {
                            Chat chat = todosOsChats.get(i);
                            // CORREÇÃO: Usando o nome correto do método do modelo Chat
                            Log.d(TAG, "Chat Exemplo " + i + ": ID=" + chat.getId() + ", ChamadoID=" + chat.getChamadoId() + ", Mensagem=" + chat.getMensagem());
                        }

                        List<Chat> chatsFiltrados = todosOsChats.stream()
                                .filter(c -> c.getChamadoId() != null && c.getChamadoId() == chamadoId)
                                .collect(Collectors.toList());
                        
                        Log.d(TAG, "Chats filtrados para o chamado " + chamadoId + ": " + chatsFiltrados.size() + " mensagens.");
                        callback.onSucesso(chatsFiltrados);
                    } else {
                         callback.onErro("A lista de chats retornada pela API está nula.");
                    }

                } else {
                    Log.e(TAG, "Falha ao buscar chat. Código: " + response.code() + ", Mensagem: " + response.message());
                    callback.onErro("Falha ao buscar chat.");
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse> call, Throwable t) {
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    public void createChat(CreateChatDto createChatDto, DataSourceCallback<Chat> callback) {
        apiService.createChat(createChatDto).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao criar chat.");
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                callback.onErro(t.getMessage());
            }
        });
    }
}
