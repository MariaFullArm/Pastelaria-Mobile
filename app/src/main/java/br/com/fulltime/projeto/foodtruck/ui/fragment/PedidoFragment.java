package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVendidoAPI;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.modelo.Venda;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.PedidoAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoFragment extends Fragment {

    private Venda venda;
    private EditText observacoes;
    private TextView data;
    private TextView nomeVendedor;
    private Button botao;
    List<Vendedor> vendedores = new ArrayList<>();
    private List<ItemVenda> itensVenda = new ArrayList<>();
    private PedidoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);

        ((MainActivity) getActivity()).setToolbarTiltle("Historico de Venda");

        observacoes = view.findViewById(R.id.pedido_observacoes);
        nomeVendedor = view.findViewById(R.id.pedido_vendedor);
        data = view.findViewById(R.id.pedido_data);
        botao = view.findViewById(R.id.pedido_botao_finalizar);

        recebeVenda();
        configuraBotao();
        inicializaRecyclerView(view);
        return view;
    }

    private void configuraBotao() {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Venda copiaDaVenda = new Venda();
                if (venda.isStatus() == false)
                    copiaDaVenda.setStatus(true);
                else
                    copiaDaVenda.setStatus(false);

                Call<ResponseBody> call = new RetrofitConfig().getVendaService().finalizaVenda(venda.getId(), copiaDaVenda);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (venda.isStatus() == false)
                                venda.setStatus(true);
                            else
                                venda.setStatus(false);
                            Toast.makeText(getContext(), "Status da venda alterada", Toast.LENGTH_SHORT).show();
                            atualizaTextoDoBotao();
                        } else try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void atualizaTextoDoBotao() {
        if (venda.isStatus() == false)
            botao.setText("Finalizar");
        else
            botao.setText("Desfazer");
    }

    private void recebeVenda() {
        Bundle arguments = getArguments();

        if (arguments != null) {
            if (arguments.getSerializable("venda") != null) {
                venda = (Venda) arguments.getSerializable("venda");

                data.setText(venda.getData_venda().toString());
                atualizaTextoDoBotao();
                preencheNomeVendedor();

                if (venda.getObservacoes() != null) {
                    observacoes.setText(venda.getObservacoes());
                }
                Call<List<ItemVendidoAPI>> call = new RetrofitConfig().getItemService().listaDeItem(venda.getId());
                call.enqueue(new Callback<List<ItemVendidoAPI>>() {
                    @Override
                    public void onResponse(Call<List<ItemVendidoAPI>> call, Response<List<ItemVendidoAPI>> response) {
                        if (response.isSuccessful()) {
                            List<ItemVendidoAPI> vendidoAPIS = response.body();
                            populaItemVenda(vendidoAPIS);

                        } else try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ItemVendidoAPI>> call, Throwable t) {

                    }
                });
            }
        }
    }

    private void preencheNomeVendedor() {
        Call<List<Vendedor>> call = new RetrofitConfig().getVendedorService().lista();
        call.enqueue(new Callback<List<Vendedor>>() {
            @Override
            public void onResponse(Call<List<Vendedor>> call, Response<List<Vendedor>> response) {
                if (response.isSuccessful()) {
                    vendedores = new ArrayList<>(response.body());
                    if (vendedores != null) {
                        if (!vendedores.isEmpty())
                            for (Vendedor vendedor : vendedores) {
                                if (venda.getId_vendedor() == vendedor.getId())
                                    nomeVendedor.setText(vendedor.getNome());
                            }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Vendedor>> call, Throwable t) {

            }
        });
    }

    private void populaItemVenda(final List<ItemVendidoAPI> lista) {
        Call<List<Produto>> call = new RetrofitConfig().getProdutoService().lista();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful()) {
                    List<Produto> produtos = response.body();
                    for (int i = 0; i < lista.size(); i++) {
                        for (int j = 0; j < produtos.size(); j++) {
                            if (lista.get(i).getId_produto() == produtos.get(j).getId()) {
                                ItemVenda itemVenda = new ItemVenda(produtos.get(j));
                                itemVenda.setQuantidade(lista.get(i).getQuantidade());
                                itensVenda.add(itemVenda);
                            }
                        }
                    }
                    adapter.carregaLista(itensVenda);

                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
    }

    private void inicializaRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.pedido_lista_produtos);
        adapter = new PedidoAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }
}
