package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuUsuarioActivity extends AppCompatActivity {

    private TextView textBoasVindas;
    private Button buttonNovoChamado;
    private Button buttonMeusChamados;
    private Button buttonSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        // Inicialização dos componentes da UI
        textBoasVindas = findViewById(R.id.text_boas_vindas);
        buttonNovoChamado = findViewById(R.id.button_novo_chamado);
        buttonMeusChamados = findViewById(R.id.button_meus_chamados);
        buttonSair = findViewById(R.id.button_sair);

        // Simplesmente exibimos uma mensagem genérica
        textBoasVindas.setText("Seja bem vindo!");

        // Ação do botão "Solicitar novo Chamado"
        buttonNovoChamado.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUsuarioActivity.this, NovoChamadoActivity.class);
            startActivity(intent);
        });

        // Ação do botão "Meus Chamados"
        buttonMeusChamados.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUsuarioActivity.this, MeusChamadosActivity.class);
            startActivity(intent);
        });

        // Ação do botão "Sair"
        buttonSair.setOnClickListener(v -> {
            // Navega de volta para a tela de Login, limpando a pilha de activities
            Intent intent = new Intent(MenuUsuarioActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}