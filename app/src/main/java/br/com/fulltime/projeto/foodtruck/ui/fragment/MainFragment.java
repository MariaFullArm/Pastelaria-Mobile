package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioVendedorActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;

import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.exibirProgressMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeRefreshingMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeStatusMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.*;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.*;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ((MainActivity)getActivity()).setToolbarTiltle("Pastelaria");
        configuraBotoes(view);

        setSwipeStatusMain(false);
        exibirProgressMain(false);

        return view;
    }

    private void configuraBotoes(View view) {
        Button botaoListaVendedor = view.findViewById(R.id.botao_lista_vendedor_main);
        abreListaDeVendedor(botaoListaVendedor);

        FloatingActionButton botaoAdicionaVendedor = view.findViewById(R.id.botao_adiciona_vendedor_main);
        abreFormularioVendedor(botaoAdicionaVendedor);

        FloatingActionButton botaoAdicionaProduto = view.findViewById(R.id.botao_adiciona_produto_main);
        abreFormularioProduto(botaoAdicionaProduto);

        Button botaoListaProduto = view.findViewById(R.id.botao_lista_produto_main);
        abreListaDeProduto(botaoListaProduto);

        Button botaoHistorico = view.findViewById(R.id.botao_historico_main);
        abreHistoricoDeVenda(botaoHistorico);

        Button botaoCadastroVenda = view.findViewById(R.id.botao_cadastro_venda_main);
        abreCadastroDeVenda(botaoCadastroVenda);
    }

    private void abreCadastroDeVenda(Button botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empilhaFragment(new FormularioVendaFragment());
            }
        });
    }

    private void abreHistoricoDeVenda(Button botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empilhaFragment(new HistoricoVendaFragment());
            }
        });
    }

    private void abreListaDeProduto(Button botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empilhaFragment(new ListaProdutoFragment());
            }
        });
    }

    private void abreFormularioProduto(FloatingActionButton botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFormularioProduto = new Intent(getContext(), FormularioProdutoActivity.class);
                startActivityForResult(intentFormularioProduto, CODIGO_DE_REQUISICAO_PRODUTO_MAIN);
            }
        });
    }

    private void abreFormularioVendedor(FloatingActionButton botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFormularioVendedor = new Intent(getContext(), FormularioVendedorActivity.class);
                startActivityForResult(intentFormularioVendedor, CODIGO_DE_REQUISICAO_VENDEDOR_MAIN);
            }
        });
    }

    private void abreListaDeVendedor(Button botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empilhaFragment(new ListaVendedoresFragment());
            }
        });
    }

    protected void empilhaFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();

        tx.replace(R.id.frame_main, fragment);
        tx.addToBackStack(null);

        tx.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CODIGO_DE_REQUISICAO_VENDEDOR_MAIN){
                empilhaFragment(new ListaVendedoresFragment());
            }

            if(requestCode == CODIGO_DE_REQUISICAO_PRODUTO_MAIN){
                empilhaFragment(new ListaProdutoFragment());
            }
        }
    }
}
