package br.com.washington.androidprojfiap.api;

import br.com.washington.androidprojfiap.domain.Login;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by washington on 14/09/2017.
 */

public interface IRetrofitApi {
    @GET("v2/58b9b1740f0000b614f09d2f")
    Call<Login> getDefaultAutentication();
}
