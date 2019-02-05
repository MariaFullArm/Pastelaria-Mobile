package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.VendedorDAO;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioVendedorActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaVendedorAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerVendedor;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_DE_REQUISICAO_LISTA_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_POSICAO_INVALIDA;

public class ListaVendedoresFragment extends MainFragment {

    private ListaVendedorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_vendedor, container, false);

        configuraBotaoAdicionaVendedor(view);
        List<Vendedor> vendedores = pegaVendedores();
        configuraRecyclerView(view, vendedores);

        return view;
    }

    private void configuraRecyclerView(View view, List<Vendedor> vendedores) {
        RecyclerView listaVendedor = view.findViewById(R.id.lista_vendedor_recyclerview);
        configuraAdapter(vendedores, listaVendedor);
    }

    private void configuraAdapter(List<Vendedor> vendedores, RecyclerView listaVendedor) {
        adapter = new ListaVendedorAdapter(getContext(), vendedores);
        listaVendedor.setAdapter(adapter);
        adapter.setOnItemClickListenerVendedor(new OnItemClickListenerVendedor() {
            @Override
            public void onItemClick(Vendedor vendedor, int posicao) {
                Intent intent = new Intent(getContext(), FormularioVendedorActivity.class);
                intent.putExtra("vendedor", vendedor);
                intent.putExtra("posicao", posicao);
                startActivityForResult(intent, CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR);
            }
        });
    }

    private List<Vendedor> pegaVendedores() {
        VendedorDAO dao = new VendedorDAO();
        List<Vendedor> vendedores = dao.todos();
        return vendedores;
    }

    private void configuraBotaoAdicionaVendedor(View view) {
        FloatingActionButton adicionaVendedor = view.findViewById(R.id.lista_vendedor_botao_adiciona);
        adicionaVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaFormularioVendedorEsperandoResultado(CODIGO_DE_REQUISICAO_LISTA_VENDEDOR);
            }
        });
    }

    private void iniciaFormularioVendedorEsperandoResultado(int codigoDeRequisicao) {
        Intent intentFormulario = new Intent(getContext(), FormularioVendedorActivity.class);
        startActivityForResult(intentFormulario, codigoDeRequisicao);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ehRequisicaoParaAdicionarVendedorComResultado(requestCode, data)) {
                Vendedor vendedorRecebido = (Vendedor) data.getSerializableExtra(CHAVE_VENDEDOR);
                Toast.makeText(getContext(),
                        "Vendedor " + vendedorRecebido.getNome() + " Salvo", Toast.LENGTH_SHORT).show();

                VendedorDAO dao = new VendedorDAO();
                dao.insere(vendedorRecebido);
                adapter.adiciona(vendedorRecebido);

            }

            if (ehRequisicaoParaAlterarVendedorComResultado(requestCode, data)) {
                Vendedor vendedorRecebido = (Vendedor) data.getSerializableExtra(CHAVE_VENDEDOR);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);

                if (posicaoRecebida > CODIGO_POSICAO_INVALIDA) {
                    Toast.makeText(getContext(),
                            "Vendedor " + vendedorRecebido.getNome() + " Foi Alterado", Toast.LENGTH_SHORT).show();
                    new VendedorDAO().altera(vendedorRecebido, posicaoRecebida);
                    adapter.altera(vendedorRecebido, posicaoRecebida);
                }
            }
        }
    }

    private boolean ehRequisicaoParaAlterarVendedorComResultado(int requestCode, Intent intent) {
        if (intent.hasExtra(CHAVE_VENDEDOR)) {
            return requestCode == CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR;
        }
        return false;
    }

    private boolean ehRequisicaoParaAdicionarVendedorComResultado(int requestCode, Intent intent) {
        if (intent.hasExtra(CHAVE_VENDEDOR)) {
            return requestCode == CODIGO_DE_REQUISICAO_LISTA_VENDEDOR;
        }
        return false;
    }
}
