package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.ProdutoDAO;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.SelecaoProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class SelecaoProdutoActivity extends AppCompatActivity {

    private SelecaoProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_produto);

        MoedaUtil conversor = new MoedaUtil();

        ProdutoDAO dao = new ProdutoDAO();
        dao.insere(
                new Produto("Comida", "Pastel de Frango", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de Carne e quejio", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de queijo", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de presunto", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de calabresa", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de presunto e queijo", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de palmito", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de pastel", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de nada", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de vento", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de maionese", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de x tudo", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de mostadela", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de provolone", conversor.validaMoeda("12.50"), "Bom"),
                new Produto("Comida", "Pastel de Carne", conversor.validaMoeda("12.50"), "Bom"));

        RecyclerView listaProduto = findViewById(R.id.formulario_venda_recyclerview);
        adapter = new SelecaoProdutoAdapter(this, dao.todos());
        listaProduto.setAdapter(adapter);
    }
}
