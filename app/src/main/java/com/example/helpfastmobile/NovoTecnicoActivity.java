package com.example.helpfastmobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class NovoTecnicoActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editTelefone, editSenha;
    private Button buttonCancelar, buttonSalvar;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_tecnico);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        editNome = findViewById(R.id.edit_nome_tecnico);
        editEmail = findViewById(R.id.edit_email_tecnico);
        editTelefone = findViewById(R.id.edit_telefone_tecnico);
        editSenha = findViewById(R.id.edit_senha_tecnico);
        buttonCancelar = findViewById(R.id.button_cancelar);
        buttonSalvar = findViewById(R.id.button_salvar);

        setupObservers();

        buttonCancelar.setOnClickListener(v -> finish());
        buttonSalvar.setOnClickListener(v -> handleSalvarTecnico());
    }

    private void setupObservers() {
        registerViewModel.getRegistrationResult().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "Técnico " + user.getNome() + " criado com sucesso!", Toast.LENGTH_LONG).show();
                finish(); // Fecha a tela após o sucesso
            }
        });

        registerViewModel.getRegistrationError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Erro ao criar o técnico: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSalvarTecnico() {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String telefone = editTelefone.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define o cargo do novo usuário como Técnico (ID = 2)
        final int CARGO_TECNICO = 2;

        registerViewModel.registerUser(nome, email, senha, telefone, CARGO_TECNICO);
    }
}
