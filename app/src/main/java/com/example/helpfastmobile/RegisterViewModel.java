package com.example.helpfastmobile;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private static final String TAG = "HelpFastDebug";
    private final UserRepository userRepository;
    private final MutableLiveData<User> registrationResult = new MutableLiveData<>();
    private final MutableLiveData<String> registrationError = new MutableLiveData<>();


    public RegisterViewModel() {
        userRepository = UserRepository.getInstance();
    }

    public LiveData<User> getRegistrationResult() {
        return registrationResult;
    }

    public LiveData<String> getRegistrationError() {
        return registrationError;
    }

    public void registerUser(String nome, String email, String senha, String telefone, int cargoId) {
        Log.d(TAG, "ViewModel: registerUser chamado com Cargo ID: " + cargoId);
        userRepository.register(nome, email, senha, telefone, cargoId, new DataSourceCallback<User>() {
            @Override
            public void onSucesso(User data) {
                registrationResult.postValue(data);
            }

            @Override
            public void onErro(String errorMessage) {
                registrationError.postValue(errorMessage);
            }
        });
    }
}
