package com.example.helpfastmobile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatChamadoActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";
    private static final String TAG = "HelpFastDebug";
    private static final long POLLING_INTERVAL_MS = 10000; // 10 segundos

    private EditText editMensagem;
    private ImageButton btnSend;
    private Button buttonVoltar;
    private RecyclerView recyclerViewMensagens;

    private ChamadoViewModel chamadoViewModel;
    private MensagemAdapter mensagemAdapter;
    private SessionManager sessionManager;
    private int chamadoId;

    private final Handler pollingHandler = new Handler(Looper.getMainLooper());
    private Runnable pollingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_chat_chamado);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        editMensagem = findViewById(R.id.edit_mensagem);
        btnSend = findViewById(R.id.btn_send);
        buttonVoltar = findViewById(R.id.button_voltar);
        recyclerViewMensagens = findViewById(R.id.recycler_view_mensagens);

        chamadoId = getIntent().getIntExtra(EXTRA_CHAMADO_ID, -1);

        if (chamadoId == -1) {
            Toast.makeText(this, "Erro: ID do chamado não fornecido.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupRecyclerView();
        setupObservers();
        setupPolling();

        buttonVoltar.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> handleEnviarMensagem());
    }

    @Override
    protected void onResume() {
        super.onResume();
        pollingHandler.post(pollingRunnable);
        Log.d(TAG, "Polling de chat iniciado.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollingHandler.removeCallbacks(pollingRunnable);
        Log.d(TAG, "Polling de chat pausado.");
    }

    private void setupRecyclerView() {
        int usuarioLogadoId = sessionManager.getUserId();
        mensagemAdapter = new MensagemAdapter(usuarioLogadoId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Faz a lista começar de baixo
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setAdapter(mensagemAdapter);
    }

    private void setupPolling() {
        pollingRunnable = () -> {
            Log.d(TAG, "Verificando novas mensagens...");
            chamadoViewModel.getChat(chamadoId);
            pollingHandler.postDelayed(pollingRunnable, POLLING_INTERVAL_MS);
        };
    }

    private void setupObservers() {
        chamadoViewModel.getCreateChatResult().observe(this, chat -> {
            if (chat != null) {
                editMensagem.setText("");
                // Recarrega o chat imediatamente para mostrar a mensagem enviada
                chamadoViewModel.getChat(chamadoId);
            }
        });

        // Lógica para atualizar a UI do chat com a nova lista de mensagens
        chamadoViewModel.getChatResult().observe(this, chatMessages -> {
            if (chatMessages != null) {
                Log.d(TAG, "Chat atualizado com " + chatMessages.size() + " mensagens.");
                mensagemAdapter.setMensagens(chatMessages);
                recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
            }
        });

        chamadoViewModel.getN8nSendSuccess().observe(this, aVoid -> {
            Log.i(TAG, "Mensagem enviada para o n8n com sucesso!");
        });

        chamadoViewModel.getCreateChatError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Falha ao enviar: " + error, Toast.LENGTH_SHORT).show();
        });
        chamadoViewModel.getN8nSendError().observe(this, error -> {
            if (error != null) Log.e(TAG, "Falha ao enviar para o n8n: " + error);
        });
    }

    private void handleEnviarMensagem() {
        String mensagem = editMensagem.getText().toString().trim();
        if (mensagem.isEmpty()) {
            return;
        }

        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setChamadoId(chamadoId);
        createChatDto.setMotivo(mensagem);
        chamadoViewModel.createChat(createChatDto);

        chamadoViewModel.sendQuestionToN8n(mensagem);
    }
}
