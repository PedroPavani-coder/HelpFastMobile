package com.example.helpfastmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<User> usuarios = new ArrayList<>();
    private OnDeleteClickListener onDeleteClickListener;

    // Interface para comunicar o evento de clique para a Activity
    public interface OnDeleteClickListener {
        void onDeleteClick(User user);
    }

    public UsuarioAdapter(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        User currentUser = usuarios.get(position);
        holder.tvNomeUsuario.setText(currentUser.getNome());

        // Converte o ID do cargo para um texto descritivo
        String cargo;
        switch (currentUser.getCargoId()) {
            case 1:
                cargo = "Admin";
                break;
            case 2:
                cargo = "Técnico";
                break;
            case 3:
                cargo = "Cliente";
                break;
            default:
                cargo = "Desconhecido";
                break;
        }
        holder.tvCargoUsuario.setText(cargo);

        holder.ivDeleteUsuario.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(currentUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
        notifyDataSetChanged();
    }

    class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeUsuario;
        private final TextView tvCargoUsuario;
        private final ImageView ivDeleteUsuario;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeUsuario = itemView.findViewById(R.id.tv_nome_usuario);
            tvCargoUsuario = itemView.findViewById(R.id.tv_cargo_usuario);
            ivDeleteUsuario = itemView.findViewById(R.id.iv_delete_usuario);
        }
    }
}
