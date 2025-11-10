package com.example.helpfastmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder> {

    private final List<Chamado> chamados = new ArrayList<>();
    private final int cargoId;
    private final OnChamadoInteractionListener listener;

    public interface OnChamadoInteractionListener {
        void onVisualizarClick(Chamado chamado);
        void onConcluirClick(Chamado chamado);
        void onCancelarClick(Chamado chamado);
    }

    public ChamadoAdapter(int cargoId, OnChamadoInteractionListener listener) {
        this.cargoId = cargoId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado chamado = chamados.get(position);
        holder.tvProtocolo.setText("ID: #" + chamado.getId());
        holder.tvAssunto.setText(chamado.getMotivo());

        String status = chamado.getStatus();
        holder.tvStatus.setText("Status: " + (status != null ? status : "N/A"));

        // --- LÓGICA DE VISIBILIDADE DOS BOTÕES ---

        // O botão Visualizar está sempre ativo
        holder.btnVisualizar.setOnClickListener(v -> listener.onVisualizarClick(chamado));

        boolean isActionable = status != null && status.equalsIgnoreCase("Aberto");
        boolean isTecnicoOrAdmin = (cargoId == 1 || cargoId == 2);

        if (isTecnicoOrAdmin && isActionable) {
            holder.btnConcluir.setVisibility(View.VISIBLE);
            holder.btnCancelar.setVisibility(View.VISIBLE);
            holder.btnConcluir.setOnClickListener(v -> listener.onConcluirClick(chamado));
            holder.btnCancelar.setOnClickListener(v -> listener.onCancelarClick(chamado));
        } else {
            holder.btnConcluir.setVisibility(View.GONE);
            holder.btnCancelar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chamados.size();
    }

    public void setChamados(List<Chamado> novosChamados) {
        this.chamados.clear();
        if (novosChamados != null) {
            this.chamados.addAll(novosChamados);
        }
        notifyDataSetChanged();
    }

    static class ChamadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvProtocolo, tvAssunto, tvStatus;
        ImageButton btnVisualizar, btnConcluir, btnCancelar;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            // CORREÇÃO: Usando os IDs corretos do XML
            tvProtocolo = itemView.findViewById(R.id.tv_chamado_id);
            tvAssunto = itemView.findViewById(R.id.tv_chamado_assunto);
            tvStatus = itemView.findViewById(R.id.tv_chamado_status);
            btnVisualizar = itemView.findViewById(R.id.iv_view_details);
            btnConcluir = itemView.findViewById(R.id.iv_concluir_chamado);
            btnCancelar = itemView.findViewById(R.id.iv_cancelar_chamado);
        }
    }
}
