package com.ugurckmkc.foursquareapivize;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by UgurCkmkc on 29/04/2017.
 */

public interface FoursquareService {
    // Yakında ki mekanlar
    @GET("search/recommendations?v=20161101&intent=coffee")
    Call<FoursquareJSON> searchCoffee(@Query("client_id") String clientID,
                                      @Query("client_secret") String clientSecret,
                                      @Query("ll") String ll,
                                      @Query("llAcc") double llAcc);
}
