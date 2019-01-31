package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.ProdutoDAO;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.listener.OnItemClickListenerProduto;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_DE_REQUISICAO_ALTERA_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_DE_REQUISICAO_LISTA_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_DE_REQUISICAO_PRODUTO_MAIN;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CODIGO_POSICAO_INVALIDA;

public class ListaProdutoFragment extends Fragment {

    private ListaProdutoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_produto, container, false);

        configuraBotaoAdicionarProduto(view);
        List<Produto> produtos = new ProdutoDAO().todos();
        configuraRecyclerView(view, produtos);

        return view;
    }

    private void configuraRecyclerView(View view, List<Produto> produtos) {
        RecyclerView listaProduto = view.findViewById(R.id.lista_produto_recyclerview);
        configuraAdapter(produtos, listaProduto);
    }

    private void configuraAdapter(List<Produto> produtos, RecyclerView listaProduto) {
        adapter = new ListaProdutoAdapter(getContext(), produtos);
        listaProduto.setAdapter(adapter);
        adapter.setOnItemClickListenerProduto(new OnItemClickListenerProduto() {
            @Override
            public void onItemClick(Produto produto, int posicao) {
                Intent intent = new Intent(getContext(), FormularioProdutoActivity.class);
                intent.putExtra(CHAVE_PRODUTO, produto);
                intent.putExtra(CHAVE_POSICAO, posicao);
                startActivityForResult(intent, CODIGO_DE_REQUISICAO_ALTERA_PRODUTO);
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
            if (ehRequisicaoParaAdicionarVendedorComResultado(requestCode, data)) {
                Produto produtoRecebido = (Produto) data.getSerializableExtra(CHAVE_PRODUTO);
                Toast.makeText(getContext(),
                        "produto " + produtoRecebido.getNome() + " Salvo", Toast.LENGTH_SHORT).show();

                new ProdutoDAO().insere(produtoRecebido);
                adapter.adiciona(produtoRecebido);
            }

            if (ehRequisicaoParaAlterarVendedorComResultado(requestCode, data)) {
                Produto produtoRecebido = (Produto) data.getSerializableExtra(CHAVE_PRODUTO);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);

                if (posicaoRecebida > CODIGO_POSICAO_INVALIDA) {
                    Toast.makeText(getContext(),
                            "produto " + produtoRecebido.getNome() + " foi alterado", Toast.LENGTH_SHORT).show();
                    new ProdutoDAO().altera(produtoRecebido, posicaoRecebida);
                    adapter.altera(produtoRecebido, posicaoRecebida);
                }
            }
        }
    }

    private boolean ehRequisicaoParaAlterarVendedorComResultado(int requestCode, Intent intent) {
        if (intent.hasExtra(CHAVE_PRODUTO)) {
            return requestCode == CODIGO_DE_REQUISICAO_ALTERA_PRODUTO;
        }
        return false;
    }

    private boolean ehRequisicaoParaAdicionarVendedorComResultado(int requestCode, Intent intent) {
        if (intent.hasExtra(CHAVE_PRODUTO)) {
            return requestCode == CODIGO_DE_REQUISICAO_LISTA_PRODUTO;
        }
        return false;
    }
}
