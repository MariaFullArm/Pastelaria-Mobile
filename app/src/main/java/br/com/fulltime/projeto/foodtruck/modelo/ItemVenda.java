package br.com.fulltime.projeto.foodtruck.modelo;

import java.io.Serializable;

public class ItemVenda implements Serializable {

    private int id;
    private Produto produto;
    private Venda venda;
    private int quantidade;

    public ItemVenda(Produto produto) {
        this.produto = produto;
        quantidade = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}