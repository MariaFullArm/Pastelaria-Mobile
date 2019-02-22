package br.com.fulltime.projeto.foodtruck.service;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVendidoAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItemService {

    @POST("itensvendidos")
    Call<ResponseBody> insere(@Body List<ItemVenda> itemVendas);

    @GET("itensvendidos/{id}")
    Call<List<ItemVendidoAPI>> listaDeItem(@Path("id") int idVenda);
}
