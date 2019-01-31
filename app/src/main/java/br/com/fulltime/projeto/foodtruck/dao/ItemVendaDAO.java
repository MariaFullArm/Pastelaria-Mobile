package br.com.fulltime.projeto.foodtruck.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;

public class ItemVendaDAO {

    private final static ArrayList<ItemVenda> itens = new ArrayList<>();

    public List<ItemVenda> todos() {
        return (List<ItemVenda>) itens.clone();
    }

    public List<ItemVenda> converteProdutos(List<Produto> produtos){
        ArrayList<ItemVenda> itemVendas = new ArrayList<>();
        for (Produto produto : produtos) {
            ItemVenda itemVenda = new ItemVenda(produto, 0);
            itemVendas.add(itemVenda);
        }
        return itemVendas;
    }

    public void insere(ItemVenda... item) {
        ItemVendaDAO.itens.addAll(Arrays.asList(item));
    }

    public void altera(ItemVenda item, int posicao) {
        itens.set(posicao, item);
    }

}
