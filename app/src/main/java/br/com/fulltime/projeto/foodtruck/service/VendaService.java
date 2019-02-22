package br.com.fulltime.projeto.foodtruck.service;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Venda;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VendaService {

    @POST("venda")
    Call<Venda> insere(@Body Venda venda);

    @GET("venda")
    Call<List<Venda>> lista();

    @GET("venda/finalizada")
    Call<List<Venda>> listaDeFinalizados();

    @GET("venda/pendente")
    Call<List<Venda>> listaDePendentes();

    @PUT("venda/{id}")
    Call<ResponseBody> altera(@Path("id") int id, @Body Venda venda);

    @PUT("status/{id}")
    Call<ResponseBody> finalizaVenda(@Path("id") int id, @Body Venda venda);

    @DELETE("venda/{id}")
    Call<ResponseBody> deleta(@Path("id") int id);
}
