package com.pets.app.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import khandroid.ext.apache.http.HttpResponse;
import khandroid.ext.apache.http.client.HttpClient;
import khandroid.ext.apache.http.client.methods.HttpPost;
import khandroid.ext.apache.http.entity.mime.MultipartEntity;
import khandroid.ext.apache.http.impl.client.DefaultHttpClient;
import khandroid.ext.apache.http.protocol.BasicHttpContext;
import khandroid.ext.apache.http.protocol.HttpContext;

/**
 * Created by BAJIRAO on 03-Oct-16.
 */
public class UploadImage {

    public static String uploadImage(String url, MultipartEntity multipartEntity) throws IOException {

        url = url.replaceAll(" ", "%20");
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(multipartEntity);
        HttpResponse response = httpClient.execute(httpPost, localContext);
        InputStream is = response.getEntity().getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String strLine;
        String resp = "";
        while ((strLine = br.readLine()) != null) {
            resp += strLine;
        }
        return resp;
    }
}
