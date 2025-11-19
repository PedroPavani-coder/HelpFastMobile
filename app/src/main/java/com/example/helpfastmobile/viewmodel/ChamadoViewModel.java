package com.example.helpfastmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.helpfastmobile.data.model.Chamado;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.data.model.CreateChatDto;
import com.example.helpfastmobile.data.model.DocumentAssistantResponse;
import com.example.helpfastmobile.data.repository.ChamadoRepository;
import com.example.helpfastmobile.data.repository.DataSourceCallback;

import java.util.List;

public class ChamadoViewModel extends ViewModel {

    private final ChamadoRepository chamadoRepository;

    private final MutableLiveData<List<Chamado>> todosChamadosResult = new MutableLiveData<>();
    private final MutableLiveData<String> todosChamadosError = new MutableLiveData<>();
    private final MutableLiveData<List<Chamado>> meusChamadosResult = new MutableLiveData<>();
    private final MutableLiveData<String> meusChamadosError = new MutableLiveData<>();
    private final MutableLiveData<Chamado> abrirChamadoResult = new MutableLiveData<>();
    private final MutableLiveData<String> abrirChamadoError = new MutableLiveData<>();
    private final MutableLiveData<Void> updateStatusResult = new MutableLiveData<>();
    private final MutableLiveData<String> updateStatusError = new MutableLiveData<>();
    private final MutableLiveData<List<Chat>> chatResult = new MutableLiveData<>();
    private final MutableLiveData<String> chatError = new MutableLiveData<>();
    private final MutableLiveData<Chat> createChatResult = new MutableLiveData<>();
    private final MutableLiveData<String> createChatError = new MutableLiveData<>();
    private final MutableLiveData<DocumentAssistantResponse> documentAssistantSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> documentAssistantError = new MutableLiveData<>();

    public ChamadoViewModel() {
        this.chamadoRepository = ChamadoRepository.getInstance();
    }

    // Getters
    public LiveData<List<Chamado>> getTodosChamadosResult() { return todosChamadosResult; }
    public LiveData<String> getTodosChamadosError() { return todosChamadosError; }
    public LiveData<List<Chamado>> getMeusChamadosResult() { return meusChamadosResult; }
    public LiveData<String> getMeusChamadosError() { return meusChamadosError; }
    public LiveData<Chamado> getAbrirChamadoResult() { return abrirChamadoResult; }
    public LiveData<String> getAbrirChamadoError() { return abrirChamadoError; }
    public LiveData<Void> getUpdateStatusResult() { return updateStatusResult; }
    public LiveData<String> getUpdateStatusError() { return updateStatusError; }
    public LiveData<List<Chat>> getChatResult() { return chatResult; }
    public LiveData<String> getChatError() { return chatError; }
    public LiveData<Chat> getCreateChatResult() { return createChatResult; }
    public LiveData<String> getCreateChatError() { return createChatError; }
    public LiveData<DocumentAssistantResponse> getDocumentAssistantSuccess() { return documentAssistantSuccess; }
    public LiveData<String> getDocumentAssistantError() { return documentAssistantError; }

    // --- Métodos do Repositório ---

    public void perguntarDocumentAssistant(String pergunta, Integer usuarioId) {
        chamadoRepository.perguntarDocumentAssistant(pergunta, usuarioId, new DataSourceCallback<DocumentAssistantResponse>() {
            @Override
            public void onSucesso(DocumentAssistantResponse data) {
                documentAssistantSuccess.postValue(data);
            }
            @Override
            public void onErro(String errorMessage) {
                documentAssistantError.postValue(errorMessage);
            }
        });
    }

    public void getTodosChamados() {
        chamadoRepository.getTodosChamados(new DataSourceCallback<List<Chamado>>() {
            @Override
            public void onSucesso(List<Chamado> data) { todosChamadosResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { todosChamadosError.postValue(errorMessage); }
        });
    }

    public void getMeusChamados(int clienteId) {
        chamadoRepository.getMeusChamados(clienteId, new DataSourceCallback<List<Chamado>>() {
            @Override
            public void onSucesso(List<Chamado> data) { meusChamadosResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { meusChamadosError.postValue(errorMessage); }
        });
    }

    public void abrirChamado(int clienteId, String motivo) {
        chamadoRepository.abrirChamado(clienteId, motivo, new DataSourceCallback<Chamado>() {
            @Override
            public void onSucesso(Chamado data) { abrirChamadoResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { abrirChamadoError.postValue(errorMessage); }
        });
    }

    public void updateStatusChamado(int chamadoId, String novoStatus, Integer tecnicoId) {
        chamadoRepository.updateStatusChamado(chamadoId, novoStatus, tecnicoId, new DataSourceCallback<Void>() {
            @Override
            public void onSucesso(Void data) { updateStatusResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { updateStatusError.postValue(errorMessage); }
        });
    }

    public void getChat(int chamadoId) {
        chamadoRepository.getChat(chamadoId, new DataSourceCallback<List<Chat>>() {
            @Override
            public void onSucesso(List<Chat> data) { chatResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { chatError.postValue(errorMessage); }
        });
    }

    public void createChat(CreateChatDto createChatDto) {
        chamadoRepository.createChat(createChatDto, new DataSourceCallback<Chat>() {
            @Override
            public void onSucesso(Chat data) { createChatResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { createChatError.postValue(errorMessage); }
        });
    }
}
