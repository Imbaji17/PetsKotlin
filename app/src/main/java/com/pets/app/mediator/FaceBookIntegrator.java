package com.pets.app.mediator;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pets.app.common.Constants;
import com.pets.app.interfaces.SimpleItemClickListener;
import com.pets.app.model.object.LoginDetails;

import org.json.JSONObject;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class FaceBookIntegrator extends SocialIntegratorInterface {

    public FaceBookIntegrator(Activity activityContext, FacebookCallback<LoginResult> facebookCallback) {
        super(activityContext, facebookCallback);
    }

    @Override
    public void getProfile(Object object, final SimpleItemClickListener simpleItemClickListener) {
        /***** Code to get response and handle received json object *****/
        LoginResult loginResult = (LoginResult) object;
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("------>>>", response.toString());
                        try {
                            final String email = object.getString("email");
                            final String first_name = object.getString("first_name");
                            final String last_name = object.getString("last_name");
                            final String name = first_name + " " + last_name;
                            final String result = first_name + " " + last_name + ", " + email;
                            final String social_id = object.getString("id");
                            final String imageURL = "https://graph.facebook.com/" + social_id + "/picture?type=large";
                            loginDetails = new LoginDetails();
                            loginDetails.setName((!TextUtils.isEmpty(first_name)) ? name : " ".concat((!TextUtils.isEmpty(last_name)) ? last_name : ""));
                            loginDetails.setEmail_id(email);
                            loginDetails.setSocial_type(Constants.FACEBOOK);
                            loginDetails.setSocial_id(social_id);
                            simpleItemClickListener.onItemClick(loginDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
//        return mUserInfoDetails;
    }

    @Override
    public void logOut() {
        // Logout from facebook
        if (LoginManager.getInstance() != null)
            LoginManager.getInstance().logOut();
    }
}
