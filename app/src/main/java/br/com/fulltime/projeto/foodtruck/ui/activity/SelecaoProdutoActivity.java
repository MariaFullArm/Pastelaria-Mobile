package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.ProdutoDAO;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.SelecaoProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class SelecaoProdutoActivity extends AppCompatActivity {

    private SelecaoProdutoAdapter adapter;
    private ArrayList<ItemVenda> itemVendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_produto);

        configuraAdapter();

        Spinner spinner = findViewById(R.id.selecao_produto_spinner);
        List<String> tipos = new ArrayList<String>();
        tipos.add("Comida");
        tipos.add("Bebida");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tipos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if("Comida" == parent.getItemAtPosition(position).toString()){

                }
                if("Bebida" == parent.getItemAtPosition(position).toString()){

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configuraAdapter() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirmar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_formulario_ok:

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
