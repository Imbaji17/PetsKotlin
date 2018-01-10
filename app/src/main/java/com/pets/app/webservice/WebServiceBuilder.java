package com.pets.app.webservice;

import com.pets.app.model.FindHostelResponse;
import com.pets.app.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class WebServiceBuilder {

    public interface ApiClient {

        //*****************************************************************************************************************************//
        //*********************************************** PETS APP REGISTRATION ***********************************************************//
        //*****************************************************************************************************************************//

        @FormUrlEncoded
        @POST("login/")
        Call<LoginResponse> login(@Field("email_id") String email_id, @Field("password") String password, @Field("language_code") String language_code, @Field("device_type") String device_type, @Field("device_token") String device_token, @Field("timestamp") String timestamp, @Field("key") String key);

//        @FormUrlEncoded
//        @POST("sociallogin/")
//        Call<LoginResponse> socialLogin(@Field("timestamp") String timestamp, @Field("key") String key, @Field("social_id") String social_id, @Field("social_type") String social_type, @Field("email") String email, @Field("device_id") String device_id, @Field("device_type") String device_type, @Field("type") String type);
//
//        @FormUrlEncoded
//        @POST("forget_password/")
//        Call<NormalResponse> forgetPassword(@Field("mobile") String mobile, @Field("timestamp") String timestamp, @Field("key") String key);
//
//        @POST("signup/")
//        Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);
//
//        @POST("update_user/")
//        Call<SignUpResponse> updateUser(@Body SignUpRequest signUpRequest);
//
//        @GET("getExperianceTitleList")
//        Call<ExperienceTitleResponse> getExperienceTitleList(@Query("user_id") String user_id, @Query("timestamp") String timestamp, @Query("key") String key);

        //http://34.199.202.75/pets/api/PetsApi/hostel_list?key=6b88b734ebb2c9f02ffe2cfdc1f40020&keyword=&language_code=EN&lat=0.000000&lng=0.000000&next_offset=0&timestamp=1515495550497.3&user_id=10
        @GET("hostel_list")
        Call<FindHostelResponse> hostelList(@Query("key") String key, @Query("keyword") String keyword, @Query("language_code") String languageCode, @Query("lat") String lat,
                                            @Query("lng") String lng, @Query("next_offset") int next_offset, @Query("timestamp") String timestamp, @Query("user_id") String user_id);

    }
}
