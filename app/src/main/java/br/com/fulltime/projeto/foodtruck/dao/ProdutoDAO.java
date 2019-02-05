package br.com.fulltime.projeto.foodtruck.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Produto;

public class ProdutoDAO {

    private final static ArrayList<Produto> produtos = new ArrayList<>();

    public List<Produto> todos() {
        return (List<Produto>) produtos.clone();
    }

    public void insere(Produto... produtos) {
        ProdutoDAO.produtos.addAll(Arrays.asList(produtos));
    }

    public void insereTudo(List<Produto> lista) {
        removeTodos();
        for (Produto produto :lista) {
            insere(produto);
        }
    }

    public void altera(Produto produto, int posicao) {
        produtos.set(posicao, produto);
    }

    public void remove(int posicao) {
        produtos.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(produtos, posicaoInicio, posicaoFim);
    }

    public void removeTodos() {
        produtos.clear();
    }
}
