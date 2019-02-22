package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.SelecaoProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemVendaSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelecaoProdutoActivity extends AppCompatActivity {

    private SelecaoProdutoAdapter adapter;
    private List<ItemVenda> itemVendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_produto);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_centralizada);
        TextView tituloToolbar = findViewById(R.id.title_toolbar);
        tituloToolbar.setText("Seleção de Produtos");

        configuraRecyclerView();
        configuraSpinner();

        itemVendas = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra("LISTA_ITEM_VENDA")) {
            itemVendas = (List<ItemVenda>) intent.getSerializableExtra("LISTA_ITEM_VENDA");
        }
        atualizaLista();
    }

    private void configuraSpinner() {
        Spinner spinner = findViewById(R.id.selecao_produto_spinner);
        List<String> tipos = new ArrayList<>();
        tipos.add("Todos Produtos");
        tipos.add("Pastel");
        tipos.add("Bebida");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    filtra(null);
                }
                if (position == 1) {
                    filtra("Pastel");
                }
                if (position == 2) {
                    filtra("Bebida");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void filtra(String tipo) {
        adapter.filtraListaDeExibicao(tipo);
    }

    private void atualizaLista() {
        Call<List<Produto>> call = new RetrofitConfig().getProdutoService().lista();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful()) {
                    List<Produto> produtos = response.body();

                    if (produtos != null) {
                        adapter.substituiLista(produtos);
                        if (!itemVendas.isEmpty())
                            adapter.trocaItens(itemVendas);
                    }
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(SelecaoProdutoActivity.this, jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(SelecaoProdutoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
    }


    private void configuraRecyclerView() {
        RecyclerView listaProduto = findViewById(R.id.formulario_venda_recyclerview);
        configuraAdapter(listaProduto);
    }

    private void configuraAdapter(RecyclerView listaProduto) {
        adapter = new SelecaoProdutoAdapter(this);
        listaProduto.setAdapter(adapter);
        adapter.setOnItemVendaSelectedListener(new OnItemVendaSelectedListener() {
            @Override
            public void onItemVendaSelected(ItemVenda item, int quantidade) {
                boolean ehInsercao = true;
                for (int i = 0; i < itemVendas.size(); i++) {
                    if (item.getProduto().getId() == itemVendas.get(i).getProduto().getId()) {
                        itemVendas.set(i, item);
                        ehInsercao = false;
                    }
                }
                if (ehInsercao) {
                    itemVendas.add(item);
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
                if (itemVendas.size() > 0) {
                    List<ItemVenda> itensDeRetorno = new ArrayList<>();
                    for (ItemVenda i : itemVendas) {
                        if (i.getQuantidade() > 0)
                            itensDeRetorno.add(i);
                    }

                    Intent intent = new Intent();
                    intent.putExtra("LISTA_ITEM_VENDA", (Serializable) itensDeRetorno);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else
                    Toast.makeText(this, "Não foi selecionado nenhum item", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
