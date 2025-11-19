package com.example.helpfastmobile.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpfastmobile.viewmodel.ChamadoViewModel;
import com.example.helpfastmobile.R;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.data.model.CreateChatDto;
import com.example.helpfastmobile.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChatChamadoActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";
    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";
    private static final String TAG = "HelpFastDebug";
    private static final int MAX_RESPOSTAS_IA = 3; // Máximo de 3 respostas da I.A.

    private EditText editMensagem;
    private ImageButton btnSend;
    private Button buttonVoltar;
    private RecyclerView recyclerViewMensagens;

    private ChamadoViewModel chamadoViewModel;
    private MensagemAdapter mensagemAdapter;
    private SessionManager sessionManager;
    private int chamadoId;
    private List<Chat> mensagensLocais = new ArrayList<>();
    private int contadorRespostasIa = 0; // Contador de respostas da I.A.
    private boolean transferidoParaTecnico = false; // Flag para controlar transferência

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

        String mensagemPreenchida = getIntent().getStringExtra(EXTRA_PREENCHER_MENSAGEM);
        if (mensagemPreenchida != null && !mensagemPreenchida.trim().isEmpty()) {
            editMensagem.setText(mensagemPreenchida);
        }

        setupRecyclerView();
        setupObservers();
        
        // Se for técnico, carrega o histórico completo do chat
        int cargoId = sessionManager.getCargoId();
        if (cargoId == 2) { // É técnico (cargoId 2)
            carregarHistoricoChat();
        }

        buttonVoltar.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> handleEnviarMensagem());
    }
    
    private void carregarHistoricoChat() {
        chamadoViewModel.getChat(chamadoId);
        chamadoViewModel.getChatResult().observe(this, chats -> {
            if (chats != null && !chats.isEmpty()) {
                mensagensLocais.clear();
                mensagensLocais.addAll(chats);
                mensagemAdapter.setMensagens(mensagensLocais);
                if (mensagemAdapter.getItemCount() > 0) {
                    recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
                }
                
                // Conta quantas respostas da I.A. já existem no histórico
                for (Chat chat : chats) {
                    if (chat.getUsuarioId() == null) { // Mensagens da I.A. têm usuarioId null
                        contadorRespostasIa++;
                    }
                }
            }
        });
        
        chamadoViewModel.getChatError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Erro ao carregar histórico do chat: " + error);
            }
        });
    }


    private void setupRecyclerView() {
        int usuarioLogadoId = sessionManager.getUserId();
        mensagemAdapter = new MensagemAdapter(usuarioLogadoId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setAdapter(mensagemAdapter);
    }

    private void adicionarMensagemAoChat(String texto, Integer usuarioId) {
        Chat novaMensagem = new Chat();
        novaMensagem.setMotivo(texto);
        novaMensagem.setUsuarioId(usuarioId);
        novaMensagem.setChamadoId(chamadoId);
        mensagensLocais.add(novaMensagem);
        mensagemAdapter.setMensagens(mensagensLocais);
        if (mensagemAdapter.getItemCount() > 0) {
            recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
        }
        
        // Salva a mensagem no servidor se for do cliente ou técnico
        if (usuarioId != null) {
            salvarMensagemNoServidor(texto, usuarioId);
        }
    }
    
    private void salvarMensagemNoServidor(String texto, Integer usuarioId) {
        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setPergunta(texto);
        chamadoViewModel.createChat(createChatDto);
    }

    private void setupObservers() {
        chamadoViewModel.getDocumentAssistantSuccess().observe(this, resposta -> {
            if (resposta != null && !transferidoParaTecnico) {
                Log.i(TAG, "Resposta recebida do DocumentAssistant. Resposta: " + resposta.getResposta() + 
                      ", Escalar para humano: " + resposta.isEscalarParaHumano());
                
                // Incrementa o contador de respostas da I.A.
                contadorRespostasIa++;
                
                // Adiciona a resposta da IA ao chat (sem usuarioId para aparecer como mensagem recebida)
                adicionarMensagemIaAoChat(resposta.getResposta());
                
                // Verifica se deve transferir para técnico (3 respostas ou escalarParaHumano)
                boolean deveTransferir = resposta.isEscalarParaHumano() || contadorRespostasIa >= MAX_RESPOSTAS_IA;
                
                if (deveTransferir && !transferidoParaTecnico) {
                    transferidoParaTecnico = true;
                    Log.w(TAG, "Transferindo chat para técnico. Respostas da I.A.: " + contadorRespostasIa);
                    
                    // Notifica o cliente sobre a transferência
                    String mensagemTransferencia = "Você será atendido pelo técnico, aguarde um instante.";
                    adicionarMensagemIaAoChat(mensagemTransferencia);
                }
            }
        });

        chamadoViewModel.getDocumentAssistantError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Falha ao enviar pergunta para DocumentAssistant: " + error);
                Toast.makeText(this, "Erro ao processar pergunta: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Observadores para salvar mensagens no servidor
        chamadoViewModel.getCreateChatResult().observe(this, chat -> {
            if (chat != null) {
                Log.d(TAG, "Mensagem salva no servidor com sucesso.");
            }
        });
        
        chamadoViewModel.getCreateChatError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Erro ao salvar mensagem no servidor: " + error);
            }
        });
    }
    
    private void adicionarMensagemIaAoChat(String texto) {
        Chat novaMensagem = new Chat();
        novaMensagem.setMotivo(texto);
        novaMensagem.setUsuarioId(null); // null indica mensagem da I.A.
        novaMensagem.setChamadoId(chamadoId);
        mensagensLocais.add(novaMensagem);
        mensagemAdapter.setMensagens(mensagensLocais);
        if (mensagemAdapter.getItemCount() > 0) {
            recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
        }
        
        // Salva a mensagem da I.A. no servidor
        salvarMensagemIaNoServidor(texto);
    }
    
    private void salvarMensagemIaNoServidor(String texto) {
        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setPergunta(texto);
        chamadoViewModel.createChat(createChatDto);
    }

    private void handleEnviarMensagem() {
        String mensagem = editMensagem.getText().toString().trim();
        if (mensagem.isEmpty()) {
            return;
        }
        
        // Se já foi transferido para técnico, não permite mais envio de mensagens para I.A.
        if (transferidoParaTecnico) {
            Toast.makeText(this, "O chat foi transferido para o técnico. Aguarde o atendimento.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Se já atingiu o limite de 3 respostas da I.A., transfere para técnico
        if (contadorRespostasIa >= MAX_RESPOSTAS_IA) {
            if (!transferidoParaTecnico) {
                transferidoParaTecnico = true;
                String mensagemTransferencia = "Você será atendido pelo técnico, aguarde um instante.";
                adicionarMensagemIaAoChat(mensagemTransferencia);
            }
            return;
        }

        // Adiciona a mensagem do usuário ao chat localmente
        Integer usuarioId = sessionManager.getUserId();
        adicionarMensagemAoChat(mensagem, usuarioId);
        editMensagem.setText("");

        // Envia para a IA através do endpoint api/DocumentAssistant/perguntar apenas se não foi transferido
        if (!transferidoParaTecnico) {
            chamadoViewModel.perguntarDocumentAssistant(mensagem);
        }
    }
}
