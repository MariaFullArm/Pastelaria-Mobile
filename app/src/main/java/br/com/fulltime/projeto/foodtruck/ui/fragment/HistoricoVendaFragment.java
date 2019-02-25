package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.util.Dialog;
import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Venda;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.service.VendaService;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.HistoricoVendaAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerHistorico;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoVendaFragment extends Fragment {

    private HistoricoVendaAdapter adapter;
    private Spinner spinner;
    private int posicao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico_venda, container, false);

        ((MainActivity) getActivity()).setToolbarTiltle("Historico de Venda");

        configuraRecyclerView(view);

        Call<List<Venda>> call = new RetrofitConfig().getVendaService().lista();

        configuraSpinner(view);

        return view;
    }

    private void configuraSpinner(View view) {
        spinner = view.findViewById(R.id.historico_venda_spinner);

        List<String> tipos = new ArrayList<>();
        tipos.add("Todas as vendas");
        tipos.add("Vendas pendentes");
        tipos.add("Vendas finalizadas");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, tipos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicao = position;
                atualizaLista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void atualizaLista() {
        VendaService service = new RetrofitConfig().getVendaService();
        if (posicao == 0) {
            Call<List<Venda>> call = service.lista();
            call.enqueue(callBackLista());
        }
        if (posicao == 1) {
            Call<List<Venda>> call = service.listaDePendentes();
            call.enqueue(callBackLista());
        }
        if (posicao == 2) {
            Call<List<Venda>> call = service.listaDeFinalizados();
            call.enqueue(callBackLista());
        }
    }

    @NonNull
    private Callback<List<Venda>> callBackLista() {
        return new Callback<List<Venda>>() {
            @Override
            public void onResponse(Call<List<Venda>> call, Response<List<Venda>> response) {
                if (response.isSuccessful()) {
                    List<Venda> body = response.body();
                    if (body != null) {
                        adapter.subtituiLista(body);
                        if (body.size() == 0)
                            new Dialog(getContext()).alertaHistorico();
                    }
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Venda>> call, Throwable t) {

            }
        };
    }

    private void configuraRecyclerView(View view) {
        RecyclerView listaHistorico = view.findViewById(R.id.historico_venda_recycleview);
        adapter = new HistoricoVendaAdapter(getContext());
        listaHistorico.setAdapter(adapter);
        adapter.setOnClickListener(new OnItemClickListenerHistorico() {
            @Override
            public void onItemClick(Venda venda) {
                PedidoFragment fragment = new PedidoFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("venda", venda);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_main, fragment).addToBackStack(null).commit();
            }

            @Override
            public void onItemClickDeletar(Venda venda) {
                Call<ResponseBody> call = new RetrofitConfig().getVendaService().deleta(venda.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Venda Excluida com sucesso", Toast.LENGTH_SHORT).show();
                            atualizaLista();
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

            @Override
            public void onItemClickEditar(Venda venda) {
                FormularioVendaFragment fragment = new FormularioVendaFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("venda", venda);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_main, fragment).addToBackStack(null).commit();
            }

            @Override
            public void onItemClickFinalizar(final Venda venda) {
                if (venda.isStatus() == false)
                    venda.setStatus(true);
                else
                    venda.setStatus(false);

                Call<ResponseBody> call = new RetrofitConfig().getVendaService().finalizaVenda(venda.getId(), venda);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            atualizaLista();
                            Toast.makeText(getContext(), "Status da venda alterada", Toast.LENGTH_SHORT).show();
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
}