package br.com.fulltime.projeto.foodtruck.modelo;

public class ItemVendidoAPI {

    private int id;
    private int Id_produto;
    private int id_venda;
    private int quantidade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_produto() {
        return Id_produto;
    }

    public void setId_produto(int id_produto) {
        Id_produto = id_produto;
    }

    public int getId_venda() {
        return id_venda;
    }

    public void setId_venda(int id_venda) {
        this.id_venda = id_venda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
