package com.example.helpfastmobile;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "HelpFastDebug";
    private final UserRepository userRepository;
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    public LoginViewModel() {
        this.userRepository = UserRepository.getInstance();
    }

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    public void login(String email, String password) {
        userRepository.login(email, password, new DataSourceCallback<User>() {
            @Override
            public void onSucesso(User data) {
                loginResult.postValue(data);
            }

            @Override
            public void onErro(String errorMessage) {
                loginError.postValue(errorMessage);
            }
        });
    }
}
