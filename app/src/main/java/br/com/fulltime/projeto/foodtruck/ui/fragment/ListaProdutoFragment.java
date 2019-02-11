package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.request.RequestProduto;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaProdutoAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerProduto;

import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.exibirProgressMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeStatusMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.swipeMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_PRODUTO;

public class ListaProdutoFragment extends Fragment {

    private ListaProdutoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_produto, container, false);

        ((MainActivity) getActivity()).setToolbarTiltle("Lista de Produtos");
        configuraBotaoAdicionarProduto(view);
        configuraRecyclerView(view);

        setSwipeStatusMain(true);
        swipeMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizaLista();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        exibirProgressMain(true);
        atualizaLista();
    }

    private void atualizaLista() {
        new RequestProduto(getContext()).atualizaListaDeProdutos(adapter);
    }

    private void configuraRecyclerView(View view) {
        RecyclerView listaProduto = view.findViewById(R.id.lista_produto_recyclerview);
        configuraAdapter(listaProduto);
    }

    private void configuraAdapter(RecyclerView listaProduto) {
        adapter = new ListaProdutoAdapter(getContext());
        listaProduto.setAdapter(adapter);
        adapter.setOnItemClickListenerProduto(new OnItemClickListenerProduto() {
            @Override
            public void onItemClick(final Produto produto, final int posicao) {
                abreFormularioParaAlterarProduto(produto);
            }

            @Override
            public void onItemClickDeletar(final Produto produto, final int posicao) {
                new RequestProduto(getContext()).deletaProduto(produto);
                atualizaLista();
            }
        });
    }

    private void configuraBotaoAdicionarProduto(View view) {
        FloatingActionButton adicionaProduto = view.findViewById(R.id.lista_produto_botao_adiciona);
        adicionaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreFormularioParaAdicionarProduto();
            }
        });
    }

    private void abreFormularioParaAdicionarProduto() {
        Intent intent = new Intent(getContext(), FormularioProdutoActivity.class);
        startActivity(intent);
    }

    private void abreFormularioParaAlterarProduto(Produto produto) {
        Intent intent = new Intent(getContext(), FormularioProdutoActivity.class);
        intent.putExtra(CHAVE_PRODUTO, produto);
        startActivity(intent);
    }
}