package br.com.fulltime.projeto.foodtruck.request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.service.VendedorService;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaVendedorAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.exibirProgressMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeRefreshingMain;

public class RequestVendedor {

    private Context context;
    private VendedorService service;

    public RequestVendedor(Context context) {
        this.context = context;
        service = new RetrofitConfig().getVendedorService();
    }

    public void inserirVendedor(Vendedor vendedor, final RequestComunicador fecharActivity) {
        Call<ResponseBody> call = service.insere(vendedor);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Vendedor cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    fecharActivity.comunica();
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure", t.toString());
            }
        });
    }

    public void alterarVendedor(Vendedor vendedor, final RequestComunicador fecharActivity) {
        Call<ResponseBody> call = service.altera(vendedor.getId(), vendedor);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Vendedor alterado com sucesso", Toast.LENGTH_SHORT).show();
                    fecharActivity.comunica();
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void atualizaListaDeVendedores(final ListaVendedorAdapter adapter) {
        Call<List<Vendedor>> call = service.lista();
        call.enqueue(new Callback<List<Vendedor>>() {
            @Override
            public void onResponse(Call<List<Vendedor>> call, Response<List<Vendedor>> response) {
                if (response.isSuccessful()) {
                    List<Vendedor> vendedores = response.body();
                    if (vendedores != null) {
                        adapter.substituiLista(vendedores);
                    }
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                setSwipeRefreshingMain(false);
                exibirProgressMain(false);
            }

            @Override
            public void onFailure(Call<List<Vendedor>> call, Throwable t) {
                setSwipeRefreshingMain(false);
                exibirProgressMain(false);
            }
        });
    }
}