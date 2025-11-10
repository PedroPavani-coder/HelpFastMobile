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

import java.util.List;

public class GerenciarUsuariosActivity extends AppCompatActivity implements UsuarioAdapter.OnDeleteClickListener {

    private static final String TAG = "HelpFastDebug"; // Tag para os nossos logs

    private RecyclerView recyclerViewUsuarios;
    private Button buttonVoltarMenu;
    private Button buttonCadastrarTecnico;

    private GerenciarUsuariosViewModel viewModel;
    private UsuarioAdapter usuarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_usuarios);
        Log.d(TAG, "GerenciarUsuariosActivity: onCreate");

        viewModel = new ViewModelProvider(this).get(GerenciarUsuariosViewModel.class);

        recyclerViewUsuarios = findViewById(R.id.recycler_view_usuarios);
        buttonVoltarMenu = findViewById(R.id.button_voltar_menu);
        buttonCadastrarTecnico = findViewById(R.id.button_cadastrar_tecnico);

        setupRecyclerView();

        buttonVoltarMenu.setOnClickListener(v -> finish());
        buttonCadastrarTecnico.setOnClickListener(v -> {
            startActivity(new Intent(this, NovoTecnicoActivity.class));
        });

        setupObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "GerenciarUsuariosActivity: onResume. Buscando usuários...");
        viewModel.fetchUsuarios();
    }

    private void setupRecyclerView() {
        usuarioAdapter = new UsuarioAdapter(this);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsuarios.setAdapter(usuarioAdapter);
    }

    private void setupObservers() {
        Log.d(TAG, "GerenciarUsuariosActivity: Configurando observadores.");
        viewModel.getUsuariosResult().observe(this, users -> {
            Log.d(TAG, "GerenciarUsuariosActivity: Observador da lista de usuários recebeu uma resposta.");
            if (users != null && !users.isEmpty()) {
                Log.d(TAG, "GerenciarUsuariosActivity: " + users.size() + " usuários recebidos. Atualizando adapter.");
                usuarioAdapter.setUsuarios(users);
            } else {
                Log.d(TAG, "GerenciarUsuariosActivity: Lista de usuários nula ou vazia.");
                Toast.makeText(this, "Nenhum usuário encontrado.", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUsuariosError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "GerenciarUsuariosActivity: Erro ao buscar usuários: " + error);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getDeleteResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Usuário deletado com sucesso!", Toast.LENGTH_SHORT).show();
                viewModel.fetchUsuarios();
            }
        });

        viewModel.getDeleteError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Falha ao deletar o usuário: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(User user) {
        Log.d(TAG, "GerenciarUsuariosActivity: Clique para deletar o usuário ID: " + user.getId());
        viewModel.deleteUsuario(user.getId());
    }
}
