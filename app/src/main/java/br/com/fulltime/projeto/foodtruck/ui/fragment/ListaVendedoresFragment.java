package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.request.RequestVendedor;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioVendedorActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaVendedorAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerVendedor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.exibirProgressMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeRefreshingMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeStatusMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.swipeMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR;

public class ListaVendedoresFragment extends MainFragment {

    private ListaVendedorAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_vendedor, container, false);

        MainActivity activity = (MainActivity) getActivity();
        activity.setToolbarTiltle("Lista de Vendedores");

        configuraBotaoAdicionaVendedor(view);
        configuraRecyclerView(view);

        setSwipeStatusMain(true);
        swipeMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizaListaDeVendedores();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        exibirProgressMain(true);
        atualizaListaDeVendedores();
    }

    private void atualizaListaDeVendedores() {
        new RequestVendedor(getContext()).atualizaListaDeProdutos(adapter);
    }

    private void configuraRecyclerView(View view) {
        RecyclerView listaVendedor = view.findViewById(R.id.lista_vendedor_recyclerview);
        configuraAdapter(listaVendedor);
    }

    private void configuraAdapter(RecyclerView listaVendedor) {
        adapter = new ListaVendedorAdapter(getContext());
        listaVendedor.setAdapter(adapter);
        adapter.setOnItemClickListenerVendedor(new OnItemClickListenerVendedor() {
            @Override
            public void onItemClick(Vendedor vendedor, int posicao) {
                Intent intent = new Intent(getContext(), FormularioVendedorActivity.class);
                intent.putExtra("vendedor", vendedor);
                intent.putExtra("requisicao", CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR);
                startActivity(intent);
            }

            @Override
            public void onItemClickDeletar(Vendedor vendedor, final int posicao) {
                Call<ResponseBody> call = new RetrofitConfig().getVendedorService().deleta(vendedor.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            adapter.remove(posicao);
                            Toast.makeText(getContext(),
                                    "Vendedor excluido com sucesso", Toast.LENGTH_SHORT).show();
                        } else
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("sad", t.toString());
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
                Intent intentFormulario = new Intent(getContext(), FormularioVendedorActivity.class);
                startActivity(intentFormulario);
            }
        });
    }
}
