package br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener;

import br.com.fulltime.projeto.foodtruck.modelo.Venda;

public interface OnItemClickListenerHistorico {
    void onItemClick(Venda venda);

    void onItemClickDeletar(Venda venda);

    void onItemClickEditar(Venda venda);

    void onItemClickFinalizar(Venda venda);
}
