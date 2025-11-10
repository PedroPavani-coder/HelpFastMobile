package com.example.helpfastmobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class FinalizacaoChamadoActivity extends AppCompatActivity {

    // Botão 'btnFinalizar' removido pois não existe no layout XML
    private Button btnVoltar;
    private ChamadoViewModel chamadoViewModel;
    private int chamadoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizacao_chamado);

        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);
        chamadoId = getIntent().getIntExtra("CHAMADO_ID", -1);

        // CORREÇÃO: Usando o ID correto do XML
        btnVoltar = findViewById(R.id.button_voltar_menu);

        // A lógica de finalização e seus observers foram removidos,
        // pois esta tela é apenas de confirmação visual.

        btnVoltar.setOnClickListener(v -> finish());
    }

    // Métodos 'setupObservers' e 'handleFinalizarChamado' removidos.
}
