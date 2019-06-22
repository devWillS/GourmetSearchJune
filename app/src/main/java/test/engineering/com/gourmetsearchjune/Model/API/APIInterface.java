package test.engineering.com.gourmetsearchjune.Model.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import test.engineering.com.gourmetsearch.Model.Response.HotPepperObject;

public interface APIInterface {
    @GET("gourmet/v1")
    Call<HotPepperObject> getOptionsHotPepperObjectNew(
            @Query("key") String key,
            @Query("genre") String genre,
            @Query("format") String format,
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("count") int count
    );

    @GET("genre/v1")
    Call<HotPepperObject> getGenreMaster(
            @Query("key") String key,
            @Query("format") String format
    );
}
