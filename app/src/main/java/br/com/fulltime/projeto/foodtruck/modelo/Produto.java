package br.com.fulltime.projeto.foodtruck.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Produto implements Serializable {

    private int id;
    private String tipo;
    private String nome;
    private BigDecimal valor;
    private String descricao;

    public Produto(String tipo, String nome, BigDecimal valor, String descricao) {
        this.tipo = tipo;
        this.nome = nome;
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
