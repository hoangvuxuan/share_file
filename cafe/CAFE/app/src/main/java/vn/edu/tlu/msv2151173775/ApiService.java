package vn.edu.tlu.msv2151173775;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("add_orders.php")
    @FormUrlEncoded
    Call<Void> addOrder(
            @Field("product_name") String productName,
            @Field("date_time") String dateTime
    );

    @GET("get_data.php")
    Call<ResponseModel> getProducts();
}
