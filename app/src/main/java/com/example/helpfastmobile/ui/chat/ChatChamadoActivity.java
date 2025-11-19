package com.example.helpfastmobile.ui.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpfastmobile.R;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.data.model.CreateChatDto;
import com.example.helpfastmobile.util.SessionManager;
import com.example.helpfastmobile.viewmodel.ChamadoViewModel;

public class ChatChamadoActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";
    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";
    private static final String TAG = "HelpFastDebug";
    private static final long POLLING_INTERVAL_MS = 10000; // 10 segundos
    private static final int MAX_AI_RESPONSES = 3;

    private EditText editMensagem;
    private ImageButton btnSend, btnCancelar, btnConcluir;
    private Button buttonVoltar;
    private RecyclerView recyclerViewMensagens;
    private LinearLayout containerBotoesAcao;

    private ChamadoViewModel chamadoViewModel;
    private MensagemAdapter mensagemAdapter;
    private SessionManager sessionManager;
    private int chamadoId;
    private int aiMessageCount = 0; // Contador para as respostas da IA
    private boolean isTransferred = false; // Flag para controlar se já foi transferido

    private final Handler pollingHandler = new Handler(Looper.getMainLooper());
    private Runnable pollingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_chat_chamado);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        bindViews();

        chamadoId = getIntent().getIntExtra(EXTRA_CHAMADO_ID, -1);
        if (chamadoId == -1) {
            Toast.makeText(this, "Erro: ID do chamado não fornecido.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupRecyclerView();
        setupObservers();
        setupPolling();
        setupActionButtons();

        String mensagemPreenchida = getIntent().getStringExtra(EXTRA_PREENCHER_MENSAGEM);
        if (mensagemPreenchida != null && !mensagemPreenchida.trim().isEmpty()) {
            handlePrimeiraMensagem(mensagemPreenchida);
        }

        buttonVoltar.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> handleEnviarMensagem());
    }

    private void bindViews() {
        editMensagem = findViewById(R.id.edit_mensagem);
        btnSend = findViewById(R.id.btn_send);
        buttonVoltar = findViewById(R.id.button_voltar);
        recyclerViewMensagens = findViewById(R.id.recycler_view_mensagens);
        containerBotoesAcao = findViewById(R.id.container_botoes_acao);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnConcluir = findViewById(R.id.btn_concluir);
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
        mensagemAdapter = new MensagemAdapter(sessionManager.getUserId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setAdapter(mensagemAdapter);
    }

    private void setupPolling() {
        pollingRunnable = () -> {
            Log.d(TAG, "Verificando histórico de mensagens da API...");
            chamadoViewModel.getChat(chamadoId);
            pollingHandler.postDelayed(pollingRunnable, POLLING_INTERVAL_MS);
        };
    }

    private void setupObservers() {
        chamadoViewModel.getChatResult().observe(this, chatMessages -> {
            if (chatMessages != null) {
                Log.d(TAG, "Histórico da API recebido com " + chatMessages.size() + " mensagens.");
                aiMessageCount = (int) chatMessages.stream().filter(m -> m.getUsuarioId() == null).count();
                // CORREÇÃO: Usa getMensagem() para verificar a mensagem de transbordo
                isTransferred = chatMessages.stream().anyMatch(m -> "Você será atendido por um técnico, aguarde um instante.".equals(m.getMensagem()));

                mensagemAdapter.setMensagens(chatMessages);
                if (mensagemAdapter.getItemCount() > 0) {
                    recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
                }
            }
        });

        chamadoViewModel.getCreateChatResult().observe(this, chat -> {
            if (chat != null) {
                Log.d(TAG, "Mensagem (ID: " + chat.getId() + ") foi salva na API. Atualizando histórico.");
                chamadoViewModel.getChat(chamadoId);
            }
        });

        chamadoViewModel.getDocumentAssistantSuccess().observe(this, resposta -> {
            if (resposta != null) {
                Log.i(TAG, "Resposta recebida da IA: " + resposta.getResposta());
                salvarMensagem(resposta.getResposta(), null);

                if ((aiMessageCount + 1) >= MAX_AI_RESPONSES || resposta.isEscalarParaHumano()) {
                    if (!isTransferred) {
                         Log.w(TAG, "Limite da IA atingido ou transbordo solicitado. Avisando o cliente.");
                        salvarMensagem("Você será atendido por um técnico, aguarde um instante.", null);
                        isTransferred = true;
                    }
                }
            }
        });

        chamadoViewModel.getUpdateStatusResult().observe(this, success -> {
            Toast.makeText(this, "Status do chamado atualizado!", Toast.LENGTH_SHORT).show();
            finish();
        });

        chamadoViewModel.getChatError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Erro ao carregar chat: " + error, Toast.LENGTH_LONG).show();
        });
        chamadoViewModel.getCreateChatError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Falha ao salvar mensagem: " + error, Toast.LENGTH_SHORT).show();
        });
        chamadoViewModel.getDocumentAssistantError().observe(this, error -> {
            if (error != null) Log.e(TAG, "Falha ao conectar com a IA: " + error);
        });
        chamadoViewModel.getUpdateStatusError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Falha ao atualizar status: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupActionButtons() {
        int cargoId = sessionManager.getCargoId();
        if (cargoId == 1 || cargoId == 2) { // Admin ou Técnico
            containerBotoesAcao.setVisibility(View.VISIBLE);
            int tecnicoId = sessionManager.getUserId();
            btnConcluir.setOnClickListener(v -> chamadoViewModel.updateStatusChamado(chamadoId, "Finalizado", tecnicoId));
            btnCancelar.setOnClickListener(v -> chamadoViewModel.updateStatusChamado(chamadoId, "Cancelado", tecnicoId));
        } else {
            containerBotoesAcao.setVisibility(View.GONE);
        }
    }

    private void handlePrimeiraMensagem(String texto) {
        editMensagem.setText("");
        salvarMensagem(texto, sessionManager.getUserId());

        if (aiMessageCount < MAX_AI_RESPONSES && !isTransferred) {
            Log.d(TAG, "Enviando primeira pergunta para a IA.");
            chamadoViewModel.perguntarDocumentAssistant(texto, sessionManager.getUserId());
        }
    }

    private void handleEnviarMensagem() {
        String textoMensagem = editMensagem.getText().toString().trim();
        if (textoMensagem.isEmpty()) {
            return;
        }

        editMensagem.setText("");
        salvarMensagem(textoMensagem, sessionManager.getUserId());

        boolean isCliente = sessionManager.getCargoId() == 3;
        if (isCliente && aiMessageCount < MAX_AI_RESPONSES && !isTransferred) {
            Log.d(TAG, "Cliente enviando pergunta para a IA. Contagem: " + aiMessageCount);
            chamadoViewModel.perguntarDocumentAssistant(textoMensagem, sessionManager.getUserId());
        } else if (isCliente) {
            Log.w(TAG, "Limite de mensagens da IA atingido ou já transferido. Mensagem apenas salva no histórico.");
        } else {
            Log.d(TAG, "Mensagem de técnico/admin enviada. Apenas salvando no histórico.");
        }
    }

    private void salvarMensagem(String texto, Integer usuarioId) {
        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setChamadoId(chamadoId);
        createChatDto.setMotivo(texto); // O backend espera "motivo" para o texto da mensagem.
        if (usuarioId != null) {
            createChatDto.setUsuarioId(usuarioId);
        }
        chamadoViewModel.createChat(createChatDto);
    }
}
