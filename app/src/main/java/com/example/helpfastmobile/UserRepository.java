package com.example.helpfastmobile;

import android.util.Log;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static final String TAG = "HelpFastDebug";
    private static volatile UserRepository instance;
    private final ApiService apiService;

    private UserRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    // --- MÉTODOS REATORADOS PARA USAR O PADRÃO CALLBACK ---

    public void login(String email, String password, DataSourceCallback<User> callback) {
        apiService.login(new LoginDbo(email, password)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Credenciais inválidas ou resposta vazia.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Falha na chamada de login: " + t.getMessage());
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    public void register(String nome, String email, String senha, String telefone, int cargoId, DataSourceCallback<User> callback) {
        RegisterDbo registerDbo = new RegisterDbo();
        registerDbo.setNome(nome);
        registerDbo.setEmail(email);
        registerDbo.setSenha(senha);
        registerDbo.setTelefone(telefone);
        registerDbo.setCargoId(cargoId);
        apiService.register(registerDbo).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Erro no cadastro.");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    public void getUsuarios(DataSourceCallback<List<User>> callback) {
        apiService.getUsuarios().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesso(response.body());
                } else {
                    callback.onErro("Falha ao buscar usuários.");
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    public void deleteUsuario(int usuarioId, DataSourceCallback<Boolean> callback) {
        apiService.deleteUsuario(usuarioId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSucesso(true);
                } else {
                    callback.onErro("Falha ao deletar usuário.");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onErro("Falha na conexão: " + t.getMessage());
            }
        });
    }
}
