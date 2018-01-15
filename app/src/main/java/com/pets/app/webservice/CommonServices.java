package com.pets.app.webservice;

import android.content.Context;

import com.pets.app.common.AppPreferenceManager;
import com.pets.app.common.Constants;
import com.pets.app.common.Enums;
import com.pets.app.model.LoginResponse;
import com.pets.app.utilities.TimeStamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class CommonServices {

    public static void getUserDetail(final Context context) {

        String languageCode = Enums.Language.EN.name().toUpperCase();
        String timeStamp = TimeStamp.getTimeStamp();
        String key = TimeStamp.getMd5(timeStamp + AppPreferenceManager.getUserID() + Constants.TIME_STAMP_KEY);

        WebServiceBuilder.ApiClient apiClient = RestClient.createService(WebServiceBuilder.ApiClient.class);
        Call<LoginResponse> call = apiClient.getUserDetails(AppPreferenceManager.getUserID(), timeStamp, key, languageCode);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response != null && response.isSuccessful()) {
                    AppPreferenceManager.saveUser(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
            }
        });
    }
}
