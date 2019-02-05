package br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener;

import br.com.fulltime.projeto.foodtruck.modelo.Produto;

public interface OnItemClickListenerProduto {
    void onItemClick(Produto produto, int posicao);

    void onItemClickDeletar(Produto produto, int posicao);
}
