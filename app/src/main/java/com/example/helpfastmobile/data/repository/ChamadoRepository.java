package com.example.helpfastmobile.data.repository;

import android.util.Log;

import com.example.helpfastmobile.data.model.AbrirChamadoDto;
import com.example.helpfastmobile.data.network.ApiClient;
import com.example.helpfastmobile.data.network.ApiService;
import com.example.helpfastmobile.data.model.DocumentQuestionRequest;
import com.example.helpfastmobile.data.model.DocumentAssistantResponse;
import com.example.helpfastmobile.data.model.N8nPayload;
import com.example.helpfastmobile.data.model.UpdateStatusDto;
import com.example.helpfastmobile.data.model.Chamado;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.data.model.CreateChatDto;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChamadoRepository {

    private static final String N8N_WEBHOOK_URL = "https://n8n.grupoopt.com.br/webhook/b4d0df62-cdc5-45ca-b874-4acb613408fb";
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

    // --- n8n Integration ---
    public void sendQuestionToN8n(String question, DataSourceCallback<Void> callback) {
        N8nPayload payload = new N8nPayload(question);
        apiService.sendToN8n(N8N_WEBHOOK_URL, payload).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSucesso(null);
                } else {
                    String errorMsg = "Falha ao enviar para o n8n. Código: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onErro(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Erro de conexão com o n8n: " + t.getMessage());
                callback.onErro("Erro de conexão com o n8n: " + t.getMessage());
            }
        });
    }

    // --- DocumentAssistant Integration (OpenAI sincrono) ---
    public void perguntarDocumentAssistant(String pergunta, DataSourceCallback<DocumentAssistantResponse> callback) {
        DocumentQuestionRequest request = new DocumentQuestionRequest();
        request.setPergunta(pergunta);
        
        apiService.perguntarDocumentAssistant(request).enqueue(new Callback<DocumentAssistantResponse>() {
            @Override
            public void onResponse(Call<DocumentAssistantResponse> call, Response<DocumentAssistantResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DocumentAssistantResponse resposta = response.body();
                    Log.d(TAG, "Resposta recebida do DocumentAssistant. Escalar para humano: " + resposta.isEscalarParaHumano());
                    callback.onSucesso(resposta);
                } else {
                    String errorMsg = "Falha ao enviar pergunta para DocumentAssistant. Código: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onErro(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<DocumentAssistantResponse> call, Throwable t) {
                Log.e(TAG, "Erro de conexão com DocumentAssistant: " + t.getMessage());
                callback.onErro("Erro de conexão com DocumentAssistant: " + t.getMessage());
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
                callback.onErro("Falha na conexão: " + t.getMessage());
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
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    public void getChamadoDetails(int chamadoId, DataSourceCallback<Chamado> callback) {
        apiService.getChamadoDetails(chamadoId).enqueue(new Callback<Chamado>() {
            @Override
            public void onResponse(Call<Chamado> call, Response<Chamado> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao buscar detalhes do chamado.");
                }
            }

            @Override
            public void onFailure(Call<Chamado> call, Throwable t) {
                callback.onErro("Falha na conexão: " + t.getMessage());
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
                callback.onErro("Falha na conexão: " + t.getMessage());
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
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    public void getChat(int chamadoId, DataSourceCallback<List<Chat>> callback) {
        apiService.getChat(chamadoId).enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao buscar chat.");
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
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
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }
}
