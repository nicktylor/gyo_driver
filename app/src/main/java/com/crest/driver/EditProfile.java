package com.crest.driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crest.driver.Multipart.MultipartRequest;
import com.crest.driver.Multipart.MultipartResponce;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.crest.driver.other.CircleTransform;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by brittany on 3/23/17.
 */

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private TextView actionbar_title, tv_rc_book_photo, tv_puc_photo, tv_vehicle_insurance_photo, tv_change_password;
    private Button btn_save;
    private Spinner spinner_vehicle_list;
    private EditText et_user_name, et_email, et_vehicle_number, et_mobile_number, et_password, et_confirm_pass;
    private ImageView iv_rc_book, iv_puc_photo, iv_vehicle_photo, img_profile, iv_edit;
    private String filePathInsurance = "";
    private String filePathPUC = "";
    private String filePathRCBook = "";
    private String filePathProfile = "";
    private Intent galleryIntent;
    private static int RESULT_PROFILE_IMG = 0;
    private static int RESULT_RC_BOOK_IMG = 1;
    private static int RESULT_PUC_IMG = 2;
    private static int RESULT_INSURANCE_IMG = 3;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    Uri selectedImage;
    Cursor cursor;
    private List<String> vehicleTypeList = new ArrayList<>();
    private ArrayAdapter<String> vehicleListAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);

        initUI();

        if (Constant.isOnline(getApplicationContext())) {
            getDriverProfileAPI();
        }
    }

    private void getDriverProfileAPI() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_DRIVER_GET_PROFILE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(EditProfile.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
//                        Toast.makeText(EditProfile.this, message, Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = response.getJSONObject("data");
                        et_user_name.setText(jsonObject.getString("v_name"));
                        et_email.setText(jsonObject.getString("v_email"));
                        et_vehicle_number.setText(jsonObject.getString("v_vehicle_number"));
                        et_mobile_number.setText(jsonObject.getString("v_phone"));
                        Glide.with(EditProfile.this).load(jsonObject.getString("v_image"))
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(EditProfile.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_profile);
//                        et_vehicle_number.setText(jsonObject.getString("v_phone"));
//                        et_mobile_number.setText(jsonObject.getString(""));
//                        if (jsonObject.getString("v_image").equals("")) ;
//                        Picasso.with(getApplicationContext()).load("" + jsonObject.getString("v_image").toString().replaceAll("\'", "")).error(R.drawable.add_image).placeholder(R.drawable.add_image).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_profile);
                    } else {
                        Toast.makeText(EditProfile.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    private void initUI() {
        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        btn_save = (Button) findViewById(R.id.btn_save);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_vehicle_number = (EditText) findViewById(R.id.et_vehicle_number);
        tv_rc_book_photo = (TextView) findViewById(R.id.tv_rc_book_photo);
        tv_puc_photo = (TextView) findViewById(R.id.tv_puc_photo);
        tv_vehicle_insurance_photo = (TextView) findViewById(R.id.tv_vehicle_insurance_photo);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        tv_change_password = (TextView) findViewById(R.id.tv_change_password);

        iv_rc_book = (ImageView) findViewById(R.id.iv_rc_book);
        iv_puc_photo = (ImageView) findViewById(R.id.iv_puc_photo);
        iv_vehicle_photo = (ImageView) findViewById(R.id.iv_vehicle_photo);
        spinner_vehicle_list = (Spinner) findViewById(R.id.spinner_vehicle_list);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);

        iv_edit.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        iv_vehicle_photo.setOnClickListener(this);
        iv_puc_photo.setOnClickListener(this);
        iv_rc_book.setOnClickListener(this);
        btn_save.setOnClickListener(this);



        vehicleListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, vehicleTypeList);
        vehicleListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehicle_list.setAdapter(vehicleListAdapter);
        if (Constant.isOnline(getApplicationContext())) {
            vehicleTypeAPI();
        }

        galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        actionbar_title.setText(R.string.actionbar_edit_profile);
    }


    private void vehicleTypeAPI() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_VEHICLE_TYPE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("city","");


        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(getApplicationContext(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                final String success = response.optString("status").toString();
                final String message = response.optString("message").toString();
                String value = String.valueOf(success);
                android.util.Log.e("value", "    " + value);
                if (value.equals("0")) {
//                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject data = response.getJSONObject("data");

                        for (int i = 1; i < data.length() + 1; i++) {

                            JSONObject objData = data.getJSONObject(String.valueOf(i));
                            /*android.util.Log.d("######", "iiiiiiii" + objData);
                            JSONObject charges = objData.getJSONObject("charges");*/

                            String id = objData.getString("id");
                            String name = objData.getString("v_name");
                            String type = objData.getString("v_type");

                            Log.d("#####", "id : " + id);

                            vehicleListAdapter.add(objData.getString("v_type"));


//                            countAmount(Integer.parseInt(charges.getString("min_charge")), Integer.parseInt(charges.getString("base_fare")), Integer.parseInt(charges.getString("upto_km")), Integer.parseInt(charges.getString("upto_km_charge")), Integer.parseInt(charges.getString("after_km_charge")), Integer.parseInt(charges.getString("ride_time_pick_charge")), Integer.parseInt(charges.getString("ride_time_wait_charge")), Float.parseFloat(charges.getString("service_tax")));



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }

            }
        }, false);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                Intent eIntent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(eIntent);
                break;

            case R.id.btn_save:
                Log.d("###########", ".........");
                if (validate()) {
                    if (Constant.isOnline(getApplicationContext()))
                        editProfile();
                }
                break;

            case R.id.iv_rc_book:
                pickRcBookFromGallery();
                break;

            case R.id.iv_puc_photo:
                pickPUCFromGallery();
                break;

            case R.id.iv_vehicle_photo:
                pickVehicleFromGallery();
                break;

            case R.id.img_profile:
                pickImageFromGallery();
                break;


        }
    }

    private void pickImageFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_PROFILE_IMG);
        }

    }

    private void pickVehicleFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_INSURANCE_IMG);
        }

    }

    private void pickPUCFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_PUC_IMG);
        }

    }

    private void pickRcBookFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_RC_BOOK_IMG);
        }


    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.permission_title_rationale));
            builder.setMessage(rationale);
            builder.setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(EditProfile.this, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton(getString(R.string.label_cancel), null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    private void editProfile() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = bos.toByteArray();
        HttpPost postRequest = new HttpPost(Constant.URL_EDIT_PROFIE);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            reqEntity.addPart("device", new StringBody("ANDROID"));
            reqEntity.addPart("lang", new StringBody("en"));
            reqEntity.addPart("login_id", new StringBody(Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID)));
            reqEntity.addPart("v_token", new StringBody(Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN)));
            reqEntity.addPart("v_name", new StringBody(et_user_name.getText().toString()));
            reqEntity.addPart("v_email", new StringBody(et_email.getText().toString()));
            reqEntity.addPart("v_phone", new StringBody(et_mobile_number.getText().toString()));
            reqEntity.addPart("v_vehicle_number", new StringBody(et_vehicle_number.getText().toString()));
            reqEntity.addPart("v_vehicle_type", new StringBody(spinner_vehicle_list.getSelectedItem().toString()));
            if (filePathProfile.equals("")) {

            } else {
                File user_image = new File(filePathProfile);
                reqEntity.addPart("v_image", new FileBody(user_image));
            }
            if (filePathRCBook.equals("")) {

            }else{
                File rc_book_file = new File(filePathRCBook);
                reqEntity.addPart("v_image_rc_book", new FileBody(rc_book_file));
            }

            if(filePathPUC.equals("")){

            }else{
                File puc_file = new File(filePathPUC);
                reqEntity.addPart("v_image_puc", new FileBody(puc_file));
            }

           if(filePathInsurance.equals("")){

           }else{
               File insurance_file = new File(filePathInsurance);
               reqEntity.addPart("v_image_insurance", new FileBody(insurance_file));
           }




            Log.d("######", "@@@@@@2" + reqEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MultipartRequest.setRequest(EditProfile.this, reqEntity, postRequest, new MultipartResponce() {
            @Override
            public void responce(final JSONObject jsonObject) {
                Log.d("########", "#####" + jsonObject);

                try {
                    if(jsonObject.getString("status").equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(),Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(),Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_INSURANCE_IMG && resultCode == RESULT_OK
                    && null != data) {
                selectedImage = data.getData();
                String file = FileUtils.getPath(this, selectedImage);
                Log.d("#######", "@@@@@@" + file.substring(file.lastIndexOf("/") + 1));
                tv_vehicle_insurance_photo.setText("" + file.substring(file.lastIndexOf("/") + 1));
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePathInsurance = cursor.getString(columnIndex);
                cursor.close();
            } else if (requestCode == RESULT_PUC_IMG && resultCode == RESULT_OK
                    && null != data) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                String file = FileUtils.getPath(this, selectedImage);
                Log.d("#######", "@@@@@@" + file.substring(file.lastIndexOf("/") + 1));
                tv_puc_photo.setText("" + file.substring(file.lastIndexOf("/") + 1));
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePathPUC = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == RESULT_RC_BOOK_IMG && resultCode == RESULT_OK
                    && null != data) {
                selectedImage = data.getData();
                String file = FileUtils.getPath(this, selectedImage);
                Log.d("#######", "@@@@@@" + file.substring(file.lastIndexOf("/") + 1));
                tv_rc_book_photo.setText("" + file.substring(file.lastIndexOf("/") + 1));
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePathRCBook = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == RESULT_PROFILE_IMG && resultCode == RESULT_OK
                    && null != data) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePathProfile = cursor.getString(columnIndex);
                cursor.close();
                img_profile.setImageBitmap(BitmapFactory
                        .decodeFile(filePathProfile));
            }

        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean validate() {
        if (et_user_name.getText().toString().trim().length() > 0) {
            if (et_email.getText().toString().trim().length() > 0) {
                if (et_email.getText().toString().trim().matches(Constant.emailPattern)) {
                    if (et_vehicle_number.getText().toString().trim().length() > 0) {
                        if (et_mobile_number.getText().toString().trim().length() > 0) {
                            return true;
                        } else et_mobile_number.setError("Please enter mobile number.");
                    } else et_vehicle_number.setError("Please enter vehicle number.");
                } else et_email.setError("Invalid email.");
            } else et_email.setError("Please enter email.");
        } else et_user_name.setError("Please enter full name.");
        return false;
    }
}
