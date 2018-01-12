package com.pets.app.initialsetup;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pets.app.R;
import com.pets.app.common.ApplicationsConstants;
import com.pets.app.common.Constants;
import com.pets.app.model.object.LoginDetails;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class InstagramActivity extends BaseActivity {

    //Used to specify the API version which we are going to use.
    public static final String APIURL = "https://api.instagram.com/v1";
    //Used for Authentication.
    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    //Used for getting token and User details.
    private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    //The callback url that we have used while registering the application.
    private static String CALLBACK_URL = "http://www.sphinx-solution.com";
    private Activity mActivity;
    private String authURLString;
    private String tokenURLString;
    private String request_token;
    private String accessTokenString;
    private String client_id = "39720825ac6a4f9088f1d5a5a7c8bc71";
    private String client_secret = "d3e5e80b7ab04f0a96f6f609558cea8b";
    private LoginDetails mLoginDetails;

    /**
     * Method that returns String from the InputStream given by p_is
     *
     * @param p_is The given InputStream
     * @return The String from the InputStream
     */
    private static String streamToString(InputStream p_is) {
        try {
            BufferedReader m_br;
            StringBuffer m_outString = new StringBuffer();
            m_br = new BufferedReader(new InputStreamReader(p_is));
            String m_read = m_br.readLine();
            while (m_read != null) {
                m_outString.append(m_read);
                m_read = m_br.readLine();
            }
            return m_outString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = InstagramActivity.this;

        initializeToolbar(mActivity.getString(R.string.instagram));
        setInstagramUrl();
        initializeView();
    }

    /**
     * Initialize View
     */
    private void initializeView() {

        WebView webView = findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authURLString);
    }

    /**
     * Setting Instagram Url
     */
    private void setInstagramUrl() {
        authURLString = AUTHURL + "?client_id=" + client_id + "&redirect_uri=" + CALLBACK_URL + "&response_type=code&display=touch&scope=likes+comments+relationships+follower_list";
        tokenURLString = TOKENURL + "?client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=" + CALLBACK_URL + "&grant_type=authorization_code";
    }

    private void callServiceForInstagram() {

        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(tokenURLString);
                    HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("POST");
                    Log.d("TOKEN", request_token);

                    //httpsURLConnection.setDoInput(true);
                    //httpsURLConnection.setDoOutput(true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                    outputStreamWriter.write("client_id=" + client_id +
                            "&client_secret=" + client_secret +
                            "&grant_type=authorization_code" +
                            "&redirect_uri=" + CALLBACK_URL +
                            "&code=" + request_token);
                    outputStreamWriter.flush();
                    String response = null;
                    if (httpsURLConnection.getResponseCode() == 200) {
                        Log.d("CODE", "SUCESS");
                        response = streamToString(httpsURLConnection.getInputStream());
                    }

                    return response;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                JSONObject jsonObject;
                hideProgressDialogue();
                if (response != null) {
                    try {
                        jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                        accessTokenString = jsonObject.getString("access_token"); //Here is your ACCESS TOKEN
                        String userId = jsonObject.getJSONObject("user").getString("id");
                        String userName = jsonObject.getJSONObject("user").getString("username");

                        mLoginDetails = new LoginDetails();
                        mLoginDetails.setSocial_id(userId);
                        mLoginDetails.setSocial_type(Constants.INSTAGRAM);
                        mLoginDetails.setEmail_id("");
                        mLoginDetails.setName(userName);

                        Intent mIntent = new Intent();
                        mIntent.putExtra(ApplicationsConstants.USER_OBJECT, mLoginDetails);
                        setResult(RESULT_OK, mIntent);
                        mActivity.finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute("");
    }

    private class AuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(CALLBACK_URL)) {
                System.out.println("URL : " + url);
                Log.d("URL: ", url);
                String parts[] = url.split("=");
                request_token = parts[1];  //This is your request token.
                callServiceForInstagram();
                return true;
            }
            return false;
        }
    }
}
