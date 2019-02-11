package br.com.fulltime.projeto.foodtruck.retrofit;

import br.com.fulltime.projeto.foodtruck.service.ProdutoService;
import br.com.fulltime.projeto.foodtruck.service.VendedorService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.17.196.132/pastelaria/api.php/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ProdutoService getProdutoService() {
        return retrofit.create(ProdutoService.class);
    }

    public VendedorService getVendedorService() {
        return retrofit.create(VendedorService.class);
    }
}
