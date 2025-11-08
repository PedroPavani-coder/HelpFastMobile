package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etTelefone, etSenha, etConfirmarSenha;
    private Button btnCadastrar, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicialização dos componentes da UI
        etNome = findViewById(R.id.edit_nome);
        etEmail = findViewById(R.id.edit_email);
        etTelefone = findViewById(R.id.edit_telefone);
        etSenha = findViewById(R.id.edit_senha);
        etConfirmarSenha = findViewById(R.id.edit_confirmar_senha);
        btnCadastrar = findViewById(R.id.button_cadastrar);
        btnLogin = findViewById(R.id.button_login);

        // Ação do botão "Cadastrar"
        btnCadastrar.setOnClickListener(v -> {
            handleRegistration();
        });

        // Ação do botão "Login" (para voltar)
        btnLogin.setOnClickListener(v -> {
            // Finaliza a activity de cadastro e volta para a de login
            finish();
        });
    }

    // Método para processar o cadastro
    private void handleRegistration() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // Validação dos campos
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lógica de cadastro (simulação)
        Toast.makeText(this, "Usuário " + nome + " registrado com sucesso!", Toast.LENGTH_LONG).show();

        // Volta para a tela de Login após o sucesso
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        // Flags para limpar a pilha de activities e garantir que o usuário não volte para a tela de cadastro
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}