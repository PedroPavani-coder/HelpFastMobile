package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialização dos componentes da UI
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // Ação do botão de Login
        btnLogin.setOnClickListener(v -> {
            tentarLogin();
        });

        // Ação do botão de Cadastro
        btnRegister.setOnClickListener(v -> {
            // Navega para a tela de Cadastro
            Intent intentCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intentCadastro);
        });
    }

    private void tentarLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etPassword.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulação de login bem-sucedido
        if (email.equals("teste@helpfast.com") && senha.equals("12345")) {
            // Navega para a tela de Menu do Usuário após o login
            Intent intentMenu = new Intent(LoginActivity.this, MenuUsuarioActivity.class);
            startActivity(intentMenu);
            finish(); // Fecha a tela de login
        } else {
            Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
        }
    }
}