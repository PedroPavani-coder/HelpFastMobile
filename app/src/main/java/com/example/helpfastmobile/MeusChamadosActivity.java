package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeusChamadosActivity extends AppCompatActivity implements ChamadoAdapter.OnChamadoInteractionListener {

    private static final String TAG = "HelpFastDebug";

    private RecyclerView recyclerView;
    private ChamadoAdapter chamadoAdapter;
    private ChamadoViewModel chamadoViewModel;
    private Button btnVoltar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_chamados);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        btnVoltar = findViewById(R.id.button_voltar_menu);

        setupRecyclerView();
        setupObservers();
        loadChamados();

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        int cargoId = sessionManager.getCargoId();
        chamadoAdapter = new ChamadoAdapter(cargoId, this);
        recyclerView = findViewById(R.id.recycler_view_chamados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chamadoAdapter);
    }

    private void setupObservers() {
        // Observador para a lista completa (Admins e Técnicos)
        chamadoViewModel.getTodosChamadosResult().observe(this, chamados -> {
            if (chamados != null) {
                int cargoId = sessionManager.getCargoId();
                if (cargoId == 2) { // É um Técnico, precisa filtrar
                    int userId = sessionManager.getUserId();
                    List<Chamado> chamadosDoTecnico = chamados.stream()
                            .filter(c -> c.getTecnicoId() != null && c.getTecnicoId() == userId)
                            .collect(Collectors.toList());
                    updateAdapter(chamadosDoTecnico);
                } else { // É um Admin, mostra tudo
                    updateAdapter(chamados);
                }
            } else {
                updateAdapter(new ArrayList<>());
            }
        });

        // Observador para a lista do cliente
        chamadoViewModel.getMeusChamadosResult().observe(this, chamados -> {
            // ADICIONADO LOG DE DIAGNÓSTICO
            if (chamados == null) {
                Log.d(TAG, "MeusChamadosActivity: A lista de chamados do cliente chegou NULA.");
            } else {
                Log.d(TAG, "MeusChamadosActivity: A lista de chamados do cliente chegou com " + chamados.size() + " itens.");
            }
            updateAdapter(chamados);
        });

        // Observadores de erro
        chamadoViewModel.getTodosChamadosError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Erro: " + error, Toast.LENGTH_SHORT).show();
        });
        chamadoViewModel.getMeusChamadosError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Erro: " + error, Toast.LENGTH_SHORT).show();
        });

        // Observadores de atualização de status
        chamadoViewModel.getUpdateStatusResult().observe(this, success -> {
            Toast.makeText(this, "Status do chamado atualizado!", Toast.LENGTH_SHORT).show();
            loadChamados();
        });
        chamadoViewModel.getUpdateStatusError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Falha ao atualizar status: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    // Método auxiliar para evitar repetição de código
    private void updateAdapter(List<Chamado> chamados) {
        if (chamados != null && !chamados.isEmpty()) {
            chamadoAdapter.setChamados(chamados);
        } else {
            chamadoAdapter.setChamados(new ArrayList<>());
            Toast.makeText(this, "Nenhum chamado encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadChamados() {
        boolean modoAdmin = getIntent().getBooleanExtra("modo_admin", false);
        int cargoId = sessionManager.getCargoId();

        if (modoAdmin || cargoId == 2) { // Admin e Técnico
            chamadoViewModel.getTodosChamados();
        } else { // Cliente
            int userId = sessionManager.getUserId();
            Log.d(TAG, "Buscando chamados para o cliente ID: " + userId);
            chamadoViewModel.getMeusChamados(userId);
        }
    }

    @Override
    public void onVisualizarClick(Chamado chamado) {
        Intent intent = new Intent(this, ChatChamadoActivity.class);
        intent.putExtra(ChatChamadoActivity.EXTRA_CHAMADO_ID, chamado.getId());
        startActivity(intent);
    }

    @Override
    public void onConcluirClick(Chamado chamado) {
        int tecnicoId = sessionManager.getUserId();
        chamadoViewModel.updateStatusChamado(chamado.getId(), "Finalizado", tecnicoId);
    }

    @Override
    public void onCancelarClick(Chamado chamado) {
        int tecnicoId = sessionManager.getUserId();
        chamadoViewModel.updateStatusChamado(chamado.getId(), "Cancelado", tecnicoId);
    }
}
