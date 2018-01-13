package com.pets.app.webservice;

import com.pets.app.model.FindHostelResponse;
import com.pets.app.model.LoginResponse;
import com.pets.app.model.NormalResponse;
import com.pets.app.model.ReviewsResponse;
import com.pets.app.model.request.FavouriteHostel;
import com.pets.app.model.request.UpdateUserRequest;
import com.pets.app.model.request.WriteReview;

import retrofit2.Call;
import retrofit2.http.Body;
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

        @FormUrlEncoded
        @POST("registerUser/")
        Call<LoginResponse> signUp(@Field("name") String name, @Field("email_id") String email_id, @Field("password") String password, @Field("phone_code") String phone_code, @Field("phone_number") String phone_number, @Field("location") String location, @Field("lat") String lat, @Field("lng") String lng,
                                   @Field("language_code") String language_code, @Field("device_type") String device_type, @Field("device_token") String device_token, @Field("timestamp") String timestamp, @Field("key") String key);

        @POST("socialLogin/")
        Call<LoginResponse> socialLogin(@Body UpdateUserRequest userRequest);

        @POST("updateProfile/")
        Call<LoginResponse> updateUser(@Body UpdateUserRequest userRequest);

        @FormUrlEncoded
        @POST("forgot_password/")
        Call<NormalResponse> forgetPassword(@Field("email_id") String email_id, @Field("timestamp") String timestamp, @Field("key") String key);

        @FormUrlEncoded
        @POST("verify_otp/")
        Call<NormalResponse> verifyOTP(@Field("user_id") String userId, @Field("otp") String otp, @Field("timestamp") String timestamp, @Field("key") String key);

        @FormUrlEncoded
        @POST("resend_otp/")
        Call<NormalResponse> resendOTP(@Field("user_id") String userId, @Field("timestamp") String timestamp, @Field("key") String key);

        @FormUrlEncoded
        @POST("changePassword/")
        Call<NormalResponse> changePassword(@Field("user_id") String user_id, @Field("old") String old, @Field("new") String newP, @Field("language_code") String language_code, @Field("timestamp") String timestamp, @Field("key") String key);


//        @POST("update_user/")
//        Call<SignUpResponse> updateUser(@Body UpdateUserRequest signUpRequest);
//
//        @GET("getExperianceTitleList")
//        Call<ExperienceTitleResponse> getExperienceTitleList(@Query("user_id") String user_id, @Query("timestamp") String timestamp, @Query("key") String key);

        //http://34.199.202.75/pets/api/PetsApi/hostel_list?key=6b88b734ebb2c9f02ffe2cfdc1f40020&keyword=&language_code=EN&lat=0.000000&lng=0.000000&next_offset=0&timestamp=1515495550497.3&user_id=10
        @GET("hostel_list")
        Call<FindHostelResponse> hostelList(@Query("key") String key, @Query("keyword") String keyword, @Query("language_code") String languageCode, @Query("lat") String lat,
                                            @Query("lng") String lng, @Query("next_offset") int next_offset, @Query("timestamp") String timestamp, @Query("user_id") String user_id);

        @POST("favorite_unfavorite/")
        Call<NormalResponse> favourite(@Body FavouriteHostel favouriteHostel);

        @GET("hostel_detail_by_id")
        Call<FindHostelResponse> hostelDetailsById(@Query("hostel_id") String id, @Query("key") String key, @Query("language_code") String languageCode, @Query("lat") String lat,
                                                   @Query("lng") String lng, @Query("timestamp") String timestamp, @Query("user_id") String user_id);

        @GET("reviews_by_type")
        Call<ReviewsResponse> reviewsByType(@Query("key") String key, @Query("language_code") String languageCode, @Query("next_offset") int nextOffset,
                                            @Query("timestamp") String timeStamp, @Query("type") String type, @Query("type_id") String type_id, @Query("user_id") String user_id);

        @POST("write_review_by_type")
        Call<NormalResponse> writeReview(@Body WriteReview writeReview);

        @POST("delete_review_by_type")
        Call<NormalResponse> deleteReview(@Body WriteReview writeReview);

    }
}
