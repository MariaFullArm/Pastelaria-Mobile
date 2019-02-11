package br.com.fulltime.projeto.foodtruck.request;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.service.ProdutoService;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ListaProdutoAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.exibirProgressMain;
import static br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity.setSwipeRefreshingMain;

public class RequestProduto {

    private Context context;
    private ProdutoService service = new RetrofitConfig().getProdutoService();

    public RequestProduto(Context context) {
        this.context = context;
    }

    public void insereProduto(Produto produto, final RequestComunicador comunicador) {
        Call<ResponseBody> call = service.insere(produto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    comunicador.comunica();
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

    public void alteraProduto(Produto produto, final RequestComunicador comunicador) {
        Call<Produto> call = service.altera(produto.getId(), produto);
        call.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Produto alterado com sucesso", Toast.LENGTH_SHORT).show();
                    comunicador.comunica();
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Produto> call, Throwable t) {

            }
        });
    }

    public void deletaProduto(Produto produto) {
        Call<String> call = service.delete(produto.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, " Produto excluido com sucesso!", Toast.LENGTH_LONG).show();
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void atualizaListaDeProdutos(final ListaProdutoAdapter adapter) {
        Call<List<Produto>> call = service.lista();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful()) {
                    List<Produto> produtos = response.body();
                    if (produtos != null) {
                        adapter.substituiLista(produtos);
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
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                setSwipeRefreshingMain(false);
                exibirProgressMain(false);
            }
        });
    }
}
