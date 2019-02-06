package br.com.fulltime.projeto.foodtruck.service;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProdutoService {

    @POST("produto")
    Call<Void> insere(@Body Produto produto);

    @GET("produto")
    Call<List<Produto>> lista();

    @DELETE("produto/{id}")
    Call<String> delete(@Path("id") int id);

    @PUT("produto/{id}")
    Call<Produto> altera(@Path("id") int id, @Body Produto produto);
}
