package com.example.helpfastmobile;

// Interface genérica para callbacks de fonte de dados.
public interface DataSourceCallback<T> {
    void onSucesso(T data);
    void onErro(String mensagem);
}
