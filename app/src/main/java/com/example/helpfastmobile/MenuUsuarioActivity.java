package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuUsuarioActivity extends AppCompatActivity {

    private static final String TAG = "HelpFastDebug";

    private TextView textBoasVindas;
    private Button buttonSair;

    // Menus
    private LinearLayout menuCliente, menuTecnico, menuAdmin;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        sessionManager = new SessionManager(getApplicationContext());

        bindViews();
        setupMenuPorCargo();
        setupClickListeners();
    }

    private void bindViews() {
        textBoasVindas = findViewById(R.id.text_boas_vindas);
        buttonSair = findViewById(R.id.button_sair);

        // Encontra os containers de menu
        menuCliente = findViewById(R.id.menu_cliente);
        menuTecnico = findViewById(R.id.menu_tecnico);
        menuAdmin = findViewById(R.id.menu_admin);
    }

    private void setupClickListeners() {
        buttonSair.setOnClickListener(v -> {
            sessionManager.clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Configura os cliques para os botões dentro dos menus
        findViewById(R.id.button_novo_chamado).setOnClickListener(v -> startActivity(new Intent(this, NovoChamadoActivity.class)));
        findViewById(R.id.button_meus_chamados).setOnClickListener(v -> startActivity(new Intent(this, MeusChamadosActivity.class)));
        findViewById(R.id.button_consultar_chamados).setOnClickListener(v -> startActivity(new Intent(this, MeusChamadosActivity.class)));
        findViewById(R.id.button_visualizar_chamados).setOnClickListener(v -> {
            Intent intent = new Intent(this, MeusChamadosActivity.class);
            intent.putExtra("modo_admin", true);
            startActivity(intent);
        });
        findViewById(R.id.button_gerenciar_usuarios).setOnClickListener(v -> startActivity(new Intent(this, GerenciarUsuariosActivity.class)));
        findViewById(R.id.button_extrair_relatorios).setOnClickListener(v -> Toast.makeText(this, "Tela de Relatórios a ser implementada.", Toast.LENGTH_SHORT).show());
    }

    private void setupMenuPorCargo() {
        int cargoId = sessionManager.getCargoId();
        String userName = sessionManager.getUserName();

        Log.d(TAG, "Configurando menu para o Cargo ID: " + cargoId);

        // Define a mensagem de boas-vindas com o nome do usuário
        if (userName != null && !userName.isEmpty()) {
            textBoasVindas.setText("Olá, " + userName);
        } else {
            textBoasVindas.setText("Olá!");
        }

        // Oculta todos os menus por padrão
        menuCliente.setVisibility(View.GONE);
        menuTecnico.setVisibility(View.GONE);
        menuAdmin.setVisibility(View.GONE);

        // Mostra o menu correto com base no cargoId
        switch (cargoId) {
            case 1: // Administrador
                menuAdmin.setVisibility(View.VISIBLE);
                break;
            case 2: // Técnico
                menuTecnico.setVisibility(View.VISIBLE);
                break;
            case 3: // Cliente
                menuCliente.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this, "Cargo de usuário desconhecido!", Toast.LENGTH_SHORT).show();
                buttonSair.performClick(); // Força o logout
                break;
        }
    }
}
