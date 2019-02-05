package br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener;

import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;

public interface OnItemVendaSelectedListener {

    void onItemVendaSelected(ItemVenda item, int posicao, int quantidade);

}