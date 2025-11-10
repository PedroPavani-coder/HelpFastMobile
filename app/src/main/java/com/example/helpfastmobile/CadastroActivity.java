package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "HelpFastDebug";

    private EditText etNome, etEmail, etTelefone, etSenha, etConfirmarSenha;
    private Button btnCadastrar, btnLogin;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = findViewById(R.id.edit_nome);
        etEmail = findViewById(R.id.edit_email);
        etTelefone = findViewById(R.id.edit_telefone);
        etSenha = findViewById(R.id.edit_senha);
        etConfirmarSenha = findViewById(R.id.edit_confirmar_senha);
        btnCadastrar = findViewById(R.id.button_cadastrar);
        btnLogin = findViewById(R.id.button_login);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        setupObservers();

        btnCadastrar.setOnClickListener(v -> handleRegistration());

        btnLogin.setOnClickListener(v -> finish());
    }

    private void setupObservers() {
        registerViewModel.getRegistrationResult().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "Usuário " + user.getNome() + " registrado com sucesso!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        registerViewModel.getRegistrationError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Erro ao realizar o cadastro: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRegistration() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define que todo novo cadastro será um Cliente (ID = 3)
        final int CARGO_CLIENTE = 3;

        registerViewModel.registerUser(nome, email, senha, telefone, CARGO_CLIENTE);
    }
}
