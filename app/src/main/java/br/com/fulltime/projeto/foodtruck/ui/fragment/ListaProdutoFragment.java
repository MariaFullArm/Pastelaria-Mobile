package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.ProdutoDAO;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerProduto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_DE_REQUISICAO_ALTERA_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_DE_REQUISICAO_LISTA_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_POSICAO_INVALIDA;

public class ListaProdutoFragment extends Fragment {

    private ListaProdutoAdapter adapter;
    private List<Produto> produtos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_produto, container, false);

        ((MainActivity)getActivity()).setToolbarTiltle("Lista de Produtos");
        configuraBotaoAdicionarProduto(view);
        produtos = new ArrayList<>();
        configuraRecyclerView(view);

        Call<List<Produto>> call = new RetrofitConfig().getProdutoService().lista();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                produtos = response.body();
                if (produtos != null)
                    adapter.substituiLista(produtos);
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });

        return view;
    }

    private void configuraRecyclerView(View view) {
        RecyclerView listaProduto = view.findViewById(R.id.lista_produto_recyclerview);
        configuraAdapter(listaProduto);
    }

    private void configuraAdapter(RecyclerView listaProduto) {
        adapter = new ListaProdutoAdapter(getContext(), produtos);
        listaProduto.setAdapter(adapter);
        adapter.setOnItemClickListenerProduto(new OnItemClickListenerProduto() {
            @Override
            public void onItemClick(final Produto produto, final int posicao) {
                Intent intent = new Intent(getContext(), FormularioProdutoActivity.class);
                intent.putExtra(CHAVE_PRODUTO, produto);
                intent.putExtra(CHAVE_POSICAO, posicao);
                startActivityForResult(intent, CODIGO_DE_REQUISICAO_ALTERA_PRODUTO);
            }

            @Override
            public void onItemClickDeletar(final Produto produto, final int posicao) {
                Call<String> call = new RetrofitConfig().getProdutoService().delete(produto.getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        adapter.remove(posicao);
                        Toast.makeText(getContext(), produto.getNome() + " foi excluido com sucesso!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void configuraBotaoAdicionarProduto(View view) {
        FloatingActionButton adicionaProduto = view.findViewById(R.id.lista_produto_botao_adiciona);
        adicionaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaFormularioEsperandoResultado(CODIGO_DE_REQUISICAO_LISTA_PRODUTO);
            }
        });
    }

    private void iniciaFormularioEsperandoResultado(int codigoRequisicao) {
        Intent intentFormulario = new Intent(getContext(), FormularioProdutoActivity.class);
        startActivityForResult(intentFormulario, codigoRequisicao);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ehRequisicaoParaAdicionarProdutoComResultado(requestCode, data)) {
                final Produto produtoRecebido = (Produto) data.getSerializableExtra(CHAVE_PRODUTO);

                Call<Void> insere = new RetrofitConfig().getProdutoService().insere(produtoRecebido);
                insere.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        adapter.adiciona(produtoRecebido);
                        Toast.makeText(getContext(),
                                "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Não foi possivel se conectar a API", Toast.LENGTH_LONG).show();

                    }
                });
            }

            if (ehRequisicaoParaAlterarProdutoComResultado(requestCode, data)) {
                Produto produtoRecebido = (Produto) data.getSerializableExtra(CHAVE_PRODUTO);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);

                if (posicaoRecebida > CODIGO_POSICAO_INVALIDA) {
                    Toast.makeText(getContext(),
                            "Produto alterado com sucesso", Toast.LENGTH_SHORT).show();
                    new ProdutoDAO().altera(produtoRecebido, posicaoRecebida);
                    adapter.altera(produtoRecebido, posicaoRecebida);
                }
            }
        }
    }

    private boolean ehRequisicaoParaAlterarProdutoComResultado(int requestCode, Intent intent) {
        if (intent.hasExtra(CHAVE_PRODUTO)) {
            return requestCode == CODIGO_DE_REQUISICAO_ALTERA_PRODUTO;
        }
        return false;
    }

    private boolean ehRequisicaoParaAdicionarProdutoComResultado(int requestCode, Intent intent) {
        if (intent.hasExtra(CHAVE_PRODUTO)) {
            return requestCode == CODIGO_DE_REQUISICAO_LISTA_PRODUTO;
        }
        return false;
    }
}

