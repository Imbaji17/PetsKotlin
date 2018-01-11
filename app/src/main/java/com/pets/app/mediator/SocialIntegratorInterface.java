package com.pets.app.mediator;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pets.app.interfaces.SimpleItemClickListener;
import com.pets.app.model.object.LoginDetails;

import java.util.Arrays;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public abstract class SocialIntegratorInterface {

    private final String[] PERMISSIONS = new String[]{"email", "public_profile"};
    public CallbackManager facebookCallbackManager;
    protected LoginDetails loginDetails;

    //CONSTRUCTOR FOR FACEBOOK INITIALIZATION
    protected SocialIntegratorInterface(Activity activityContext, FacebookCallback<LoginResult> facebookCallBack) {
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(activityContext
                , Arrays.asList(PERMISSIONS));
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
                facebookCallBack);
    }

    public abstract void getProfile(Object object, SimpleItemClickListener simpleItemClickListener);

    public abstract void logOut();
}
