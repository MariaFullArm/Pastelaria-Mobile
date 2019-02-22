package br.com.fulltime.projeto.foodtruck.retrofit;

import br.com.fulltime.projeto.foodtruck.service.ItemService;
import br.com.fulltime.projeto.foodtruck.service.ProdutoService;
import br.com.fulltime.projeto.foodtruck.service.VendaService;
import br.com.fulltime.projeto.foodtruck.service.VendedorService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    // add your other interceptors …

    // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.17.196.132/pastelaria/api.php/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public ProdutoService getProdutoService() {
        return retrofit.create(ProdutoService.class);
    }

    public VendedorService getVendedorService() {
        return retrofit.create(VendedorService.class);
    }

    public VendaService getVendaService() {
        return retrofit.create(VendaService.class);
    }

    public ItemService getItemService() {
        return retrofit.create(ItemService.class);
    }

}
