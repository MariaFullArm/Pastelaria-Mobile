package br.com.fulltime.projeto.foodtruck.ui.fragment;

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

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        configuraBotoes(view);

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

            }
        });
    }

    private void abreFormularioVendedor(FloatingActionButton botao) {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empilhaFragment(new FormularioVendedorFragment());
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

    private void empilhaFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();

        tx.replace(R.id.frame_main, fragment);
        tx.addToBackStack(null);

        tx.commit();
    }

    protected void trocaFragment(Fragment fragment){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();

        tx.replace(R.id.frame_main, fragment);
        tx.commit();
    }
}
