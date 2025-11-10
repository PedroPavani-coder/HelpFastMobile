package com.example.helpfastmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder> {

    private static final int VIEW_TYPE_ENVIADA = 1;
    private static final int VIEW_TYPE_RECEBIDA = 2;

    private final List<Chat> mensagens = new ArrayList<>();
    private final int idUsuarioLogado;

    public MensagemAdapter(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
    }

    @Override
    public int getItemViewType(int position) {
        Chat mensagem = mensagens.get(position);
        // Se o ID do usuário da mensagem for o mesmo do usuário logado, é uma mensagem enviada.
        if (mensagem.getUsuarioId() != null && mensagem.getUsuarioId() == idUsuarioLogado) {
            return VIEW_TYPE_ENVIADA;
        }
        return VIEW_TYPE_RECEBIDA;
    }

    @NonNull
    @Override
    public MensagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ENVIADA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_enviada, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_recebida, parent, false);
        }
        return new MensagemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensagemViewHolder holder, int position) {
        Chat mensagem = mensagens.get(position);
        holder.tvCorpo.setText(mensagem.getMotivo());
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

    static class MensagemViewHolder extends RecyclerView.ViewHolder {
        TextView tvCorpo;

        public MensagemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCorpo = itemView.findViewById(R.id.tv_mensagem_corpo);
        }
    }
}
