package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.dao.VendedorDAO;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioVendedorActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaVendedorAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerVendedor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_DE_REQUISICAO_LISTA_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_POSICAO_INVALIDA;

public class ListaVendedoresFragment extends MainFragment {

    private ListaVendedorAdapter adapter;
    private List<Vendedor> vendedores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_vendedor, container, false);

        ((MainActivity) getActivity()).setToolbarTiltle("Lista de Vendedores");
        configuraBotaoAdicionaVendedor(view);
        vendedores = new ArrayList<>();
        configuraRecyclerView(view);

        Call<List<Vendedor>> call = new RetrofitConfig().getVendedorService().lista();
        call.enqueue(new Callback<List<Vendedor>>() {
            @Override
            public void onResponse(Call<List<Vendedor>> call, Response<List<Vendedor>> response) {
                vendedores = response.body();
                if (vendedores != null) {
                    adapter.substituiLista(vendedores);
                }
            }

            @Override
            public void onFailure(Call<List<Vendedor>> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });

        return view;
    }

    private void configuraRecyclerView(View view) {
        RecyclerView listaVendedor = view.findViewById(R.id.lista_vendedor_recyclerview);
        configuraAdapter(listaVendedor);
    }

    private void configuraAdapter(RecyclerView listaVendedor) {
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

            @Override
            public void onItemClickDeletar(Vendedor vendedor, final int posicao) {
                Call<String> call = new RetrofitConfig().getVendedorService().deleta(10);
                call.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("onResponde", " (2) " + response.raw().code());
                        if (response.isSuccessful()) {
                            adapter.remove(posicao);
                            Toast.makeText(getContext(),
                                    "Vendedor excluido com sucesso", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
            }
        });
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
                final Vendedor vendedor = (Vendedor) data.getSerializableExtra(CHAVE_VENDEDOR);

                Call<Void> call = new RetrofitConfig().getVendedorService().insere(vendedor);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(),
                                "Vendedor cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        adapter.adiciona(vendedor);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }

            if (ehRequisicaoParaAlterarVendedorComResultado(requestCode, data)) {
                Vendedor vendedor = (Vendedor) data.getSerializableExtra(CHAVE_VENDEDOR);
                final int posicao = data.getIntExtra(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);

                if (posicao > CODIGO_POSICAO_INVALIDA) {
                    Log.i("teste", " +" + vendedor.getId());
                    Call<Vendedor> call = new RetrofitConfig()
                            .getVendedorService().altera(vendedor.getId(), vendedor);
                    call.enqueue(new Callback<Vendedor>() {
                        @Override
                        public void onResponse(Call<Vendedor> call, Response<Vendedor> response) {
                            Vendedor vendedorAPI = response.body();
                            Toast.makeText(getContext(),
                                    "Vendedor alterado com sucesso", Toast.LENGTH_SHORT).show();
                            adapter.altera(vendedorAPI, posicao);
                        }

                        @Override
                        public void onFailure(Call<Vendedor> call, Throwable t) {

                        }
                    });
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
