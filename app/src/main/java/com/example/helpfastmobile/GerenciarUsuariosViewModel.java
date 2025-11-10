package com.example.helpfastmobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class GerenciarUsuariosViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<List<User>> usuariosResult = new MutableLiveData<>();
    private final MutableLiveData<String> usuariosError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
    private final MutableLiveData<String> deleteError = new MutableLiveData<>();

    public GerenciarUsuariosViewModel() {
        this.userRepository = UserRepository.getInstance();
    }

    // LiveData getters
    public LiveData<List<User>> getUsuariosResult() { return usuariosResult; }
    public LiveData<String> getUsuariosError() { return usuariosError; }
    public LiveData<Boolean> getDeleteResult() { return deleteResult; }
    public LiveData<String> getDeleteError() { return deleteError; }

    public void fetchUsuarios() {
        userRepository.getUsuarios(new DataSourceCallback<List<User>>() {
            @Override
            public void onSucesso(List<User> data) { usuariosResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { usuariosError.postValue(errorMessage); }
        });
    }

    public void deleteUsuario(int usuarioId) {
        userRepository.deleteUsuario(usuarioId, new DataSourceCallback<Boolean>() {
            @Override
            public void onSucesso(Boolean data) { deleteResult.postValue(data); }
            @Override
            public void onErro(String errorMessage) { deleteError.postValue(errorMessage); }
        });
    }
}
