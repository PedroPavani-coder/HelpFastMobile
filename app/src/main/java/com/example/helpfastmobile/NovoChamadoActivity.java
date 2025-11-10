package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class NovoChamadoActivity extends AppCompatActivity {

    private static final String TAG = "HelpFastDebug";

    private EditText editAssunto;
    private EditText editMotivo;
    private Button buttonVoltar;
    private Button buttonAbrir;

    private ChamadoViewModel chamadoViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_chamado);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        editAssunto = findViewById(R.id.edit_assunto);
        editMotivo = findViewById(R.id.edit_motivo);
        buttonVoltar = findViewById(R.id.button_voltar);
        buttonAbrir = findViewById(R.id.button_abrir);

        setupObservers();

        buttonVoltar.setOnClickListener(v -> finish());
        buttonAbrir.setOnClickListener(v -> handleAbrirChamado());
    }

    private void setupObservers() {
        chamadoViewModel.getAbrirChamadoResult().observe(this, success -> {
            Toast.makeText(this, "Chamado aberto com sucesso!", Toast.LENGTH_LONG).show();
            // CORREÇÃO: Navega para a TelaFaqActivity
            Intent intent = new Intent(NovoChamadoActivity.this, TelaFaqActivity.class);
            startActivity(intent);
            finish(); // Fecha a tela atual
        });

        chamadoViewModel.getAbrirChamadoError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Falha ao abrir o chamado: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAbrirChamado() {
        // O campo 'motivo' é o que a API espera. O campo 'assunto' não é utilizado na API.
        String motivo = editMotivo.getText().toString().trim();

        if (motivo.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha o motivo.", Toast.LENGTH_SHORT).show();
            return;
        }

        int clienteId = sessionManager.getUserId();

        if (clienteId == -1) {
            Toast.makeText(this, "Sessão inválida. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "Abrindo chamado para o cliente ID " + clienteId + " com o motivo: " + motivo);

        // Apenas chama o método da ViewModel. A observação cuidará do resultado.
        chamadoViewModel.abrirChamado(clienteId, motivo);
    }
}
