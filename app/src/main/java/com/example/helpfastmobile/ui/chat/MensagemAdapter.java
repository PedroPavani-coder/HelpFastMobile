package com.example.helpfastmobile.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpfastmobile.R;
import com.example.helpfastmobile.data.model.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder> {

    private static final int VIEW_TYPE_ENVIADA = 1; // Direita
    private static final int VIEW_TYPE_RECEBIDA = 2; // Esquerda

    private final List<Chat> mensagens = new ArrayList<>();
    private final int idUsuarioLogado;

    public MensagemAdapter(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
    }

    @Override
    public int getItemViewType(int position) {
        Chat mensagem = mensagens.get(position);
        String tipo = mensagem.getTipo();

        // REGRA 1: Se a mensagem é da IA, é sempre RECEBIDA (esquerda).
        if (tipo != null && (tipo.toLowerCase().contains("ia") || tipo.equalsIgnoreCase("assistente"))) {
            return VIEW_TYPE_RECEBIDA;
        }

        // REGRA 2: Se não for da IA, compara o ID do remetente com o do usuário logado.
        if (Objects.equals(mensagem.getRemetenteId(), idUsuarioLogado)) {
            return VIEW_TYPE_ENVIADA;
        } else {
            return VIEW_TYPE_RECEBIDA;
        }
    }

    @NonNull
    @Override
    public MensagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ENVIADA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_enviada, parent, false);
        } else { // VIEW_TYPE_RECEBIDA
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_recebida, parent, false);
        }
        return new MensagemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensagemViewHolder holder, int position) {
        Chat mensagem = mensagens.get(position);
        holder.bind(mensagem, getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    public void setMensagens(List<Chat> novasMensagens) {
        this.mensagens.clear();
        if (novasMensagens != null) {
            this.mensagens.addAll(novasMensagens);
        }
        notifyDataSetChanged();
    }

    class MensagemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeRemetente;
        private final TextView tvTextoMensagem;

        public MensagemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeRemetente = itemView.findViewById(R.id.tv_nome_remetente);
            tvTextoMensagem = itemView.findViewById(R.id.tv_texto_mensagem);
        }

        void bind(Chat mensagem, int viewType) {
            tvTextoMensagem.setText(mensagem.getMensagem());

            // Se a mensagem foi ENVIADA pelo usuário logado, o nome é sempre "Você".
            if (viewType == VIEW_TYPE_ENVIADA) {
                tvNomeRemetente.setText("Você");
                return;
            }

            // Se a mensagem foi RECEBIDA, identifica o remetente pelo campo "tipo" da API.
            String tipo = mensagem.getTipo();
            if (tipo != null) {
                String tipoLowerCase = tipo.toLowerCase();
                if (tipoLowerCase.contains("ia") || tipoLowerCase.contains("assistente")) {
                    tvNomeRemetente.setText("HelpFast IA");
                } else if (tipoLowerCase.equals("tecnico")) {
                    tvNomeRemetente.setText("Técnico");
                } else { // O padrão para "Usuario" e outros tipos será "Cliente".
                    tvNomeRemetente.setText("Cliente");
                }
            } else {
                // Fallback caso o campo "tipo" seja nulo.
                tvNomeRemetente.setText("Suporte");
            }
        }
    }
}
