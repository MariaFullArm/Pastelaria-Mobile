package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.ItemVendaDAO;
import br.com.fulltime.projeto.foodtruck.dao.ProdutoDAO;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.SelecaoProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemVendaSelectedListener;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class SelecaoProdutoActivity extends AppCompatActivity {

    private SelecaoProdutoAdapter adapter;
    private List<ItemVenda> itemVendasDeExibicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_produto);

        configuraRecyclerView();
        configuraSpinner();
    }

    private void configuraSpinner() {
        Spinner spinner = findViewById(R.id.selecao_produto_spinner);
        List<String> tipos = new ArrayList<>();
        tipos.add("Todos");
        tipos.add("Pastel");
        tipos.add("Bebida");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ordenarLista("");
                }
                if (position == 1) {
                    ordenarLista("Pastel");
                }
                if (position == 2) {
                    ordenarLista("Bebida");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ordenarLista(String tipoParaOrdenar) {
        adapter.ordenaTipo(tipoParaOrdenar);
    }


    private void configuraRecyclerView() {
        MoedaUtil conversor = new MoedaUtil();
        ProdutoDAO dao = new ProdutoDAO();
        criaProdutos(conversor, dao);

        itemVendasDeExibicao = new ItemVendaDAO().converteProdutos(dao.todos());

        RecyclerView listaProduto = findViewById(R.id.formulario_venda_recyclerview);
        configuraAdapter(listaProduto);
    }

    private void configuraAdapter(RecyclerView listaProduto) {
        adapter = new SelecaoProdutoAdapter(this, itemVendasDeExibicao);
        listaProduto.setAdapter(adapter);
        adapter.setOnItemVendaSelectedListener(new OnItemVendaSelectedListener() {
            @Override
            public void onItemVendaSelected(ItemVenda item, int posicao, int quantidade) {
                if (quantidade >0){
                    itemVendasDeExibicao.get(posicao).setQuantidade(quantidade);
                }
            }
        });
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
                List<ItemVenda> itensVenda = new ArrayList<>();
                for (ItemVenda i :itemVendasDeExibicao) {
                    if(i.getQuantidade()>0)
                        itensVenda.add(i);
                }

                Intent intent = new Intent();
                intent.putExtra("LISTA_ITEM_VENDA", (Serializable) itensVenda);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void criaProdutos(MoedaUtil conversor, ProdutoDAO dao) {
        dao.insere(
                new Produto(),
                new Produto(),
                new Produto(),
                new Produto(),
                new Produto(),
                new Produto(),
                new Produto(),
                new Produto());

    }
}
