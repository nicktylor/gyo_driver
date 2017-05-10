package com.crest.driver.Multipart;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.crest.driver.R;
import com.crest.driver.SignUp;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.CustomDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import okhttp3.HttpUrl;

/**
 * Created by annie on 10/4/17.
 */

public class MultipartRequest {
    private static Context mContext;
    private static CustomDialog dialog;
    private static MultipartResponce multipartResponce;
    private static MultipartEntity multipartEntity;
    private static HttpPost httpPost;

    public static void setRequest(Context context, MultipartEntity reqEntity, HttpPost postRequest, MultipartResponce multipart) {
        mContext = context;
        multipartEntity = reqEntity;
        multipartResponce = multipart;
        httpPost = postRequest;
        new Prosess().execute();
    }

    private static class Prosess extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new CustomDialog(mContext);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... url) {
            try {
                Thread.sleep(2000);
                executeMultipartPost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.hide();
        }
    }

    private static void executeMultipartPost() throws Exception {
        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
//            //HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.BASE_URL + getResources().getString(R.string.set_profile)).newBuilder();
            httpPost.setEntity(multipartEntity);
            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            JSONObject jsonObject = new JSONObject(s.toString());
            multipartResponce.responce(jsonObject);
        } catch (Exception e) {
            Log.d("exception", "" + e);
            e.printStackTrace();
        }
    }
}
