package br.com.fulltime.projeto.foodtruck.service;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VendedorService {

    @POST("vendedor")
    Call<ResponseBody> insere(@Body Vendedor vendedor);

    @GET("vendedor")
    Call<List<Vendedor>> lista();

    @DELETE("vendedor/{id}")
    Call<ResponseBody> deleta(@Path("id") int id);

    @PUT("vendedor/{id}")
    Call<ResponseBody> altera(@Path("id") int id, @Body Vendedor vendedor);
}
