package br.com.fulltime.projeto.foodtruck.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;

public class VendedorDAO {

    private final static ArrayList<Vendedor> vendedores = new ArrayList<>();

    public List<Vendedor> todos() {
        return (List<Vendedor>) vendedores.clone();
    }

    public void insere(Vendedor... notas) {
        VendedorDAO.vendedores.addAll(Arrays.asList(notas));
    }

    public void altera(Vendedor nota, int posicao) {
        vendedores.set(posicao, nota);
    }

    public void remove(int posicao) {
        vendedores.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(vendedores, posicaoInicio, posicaoFim);
    }

    public void removeTodos() {
        vendedores.clear();
    }
}
