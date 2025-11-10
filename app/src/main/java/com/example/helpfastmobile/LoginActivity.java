package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "HelpFastDebug";

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnRegister;

    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        setupObservers();

        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        });
    }

    private void setupObservers() {
        loginViewModel.getLoginResult().observe(this, user -> {
            if (user != null) {
                sessionManager.saveUserId(user.getId());
                sessionManager.saveCargoId(user.getCargoId());
                sessionManager.saveUserName(user.getNome());

                Log.d(TAG, "Login bem-sucedido para: " + user.getNome());
                Toast.makeText(this, "Bem-vindo, " + user.getNome(), Toast.LENGTH_SHORT).show();

                // Revertido para o comportamento correto: navegar para a MenuUsuarioActivity
                Intent intent = new Intent(LoginActivity.this, MenuUsuarioActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginViewModel.getLoginError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Login falhou: " + error);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "LoginActivity: Chamando a loginViewModel.login().");
        loginViewModel.login(email, password);
    }
}
