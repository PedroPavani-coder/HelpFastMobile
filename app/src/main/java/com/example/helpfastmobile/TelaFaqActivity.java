package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaFaqActivity extends AppCompatActivity implements View.OnClickListener {

    // Chave para o "Extra" do Intent
    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";

    private Button btnImpressora, btnComputador, btnWifi, btnCelular, buttonIrChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_faq);

        // 1. Inicializa todos os botões
        btnImpressora = findViewById(R.id.btn_impressora);
        btnComputador = findViewById(R.id.btn_computador);
        btnWifi = findViewById(R.id.btn_wifi);
        btnCelular = findViewById(R.id.btn_celular);
        buttonIrChat = findViewById(R.id.button_ir_chat);

        // 2. Configura o listener de clique para todos os botões
        btnImpressora.setOnClickListener(this);
        btnComputador.setOnClickListener(this);
        btnWifi.setOnClickListener(this);
        btnCelular.setOnClickListener(this);
        buttonIrChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 3. Obtém o texto do botão clicado
        Button botaoClicado = (Button) v;
        String pergunta = botaoClicado.getText().toString();

        // Se for o botão "Ir para o Chat", não envia texto pré-definido
        if (v.getId() == R.id.button_ir_chat) {
            irParaChatComPergunta(null); // Passa nulo para não preencher nada
        } else {
            irParaChatComPergunta(pergunta);
        }
    }

    // 4. Método centralizado para iniciar o chat
    private void irParaChatComPergunta(String pergunta) {
        Intent intent = new Intent(this, ChatChamadoActivity.class);

        // Anexa a pergunta como um "Extra" se ela não for nula
        if (pergunta != null) {
            intent.putExtra(EXTRA_PREENCHER_MENSAGEM, pergunta);
        }

        startActivity(intent);
        finish(); // Fecha a tela de FAQ
    }
}
