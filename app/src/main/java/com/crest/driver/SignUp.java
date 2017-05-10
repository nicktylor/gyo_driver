package com.crest.driver;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.Multipart.MultipartRequest;
import com.crest.driver.Multipart.MultipartResponce;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.GPSTracker;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
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

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private TextView actionbar_title, tv_rc_book_photo, tv_puc_photo, tv_vehicle_insurance_photo;
    private Button bt_submit;
    private Spinner spinner_vehicle_list;
    private EditText et_user_name, et_email, et_vehicle_number, et_mobile_number, et_password, et_confirm_pass;
    private ImageView iv_rc_book, iv_puc_photo, iv_vehicle_photo, img_profile;
    private Intent intent;
    private static int RESULT_PROFILE_IMG = 0;
    private static int RESULT_RC_BOOK_IMG = 1;
    private static int RESULT_PUC_IMG = 2;
    private static int RESULT_INSURANCE_IMG = 3;
    private Intent galleryIntent;
    private String filePathInsurance = "";
    private String filePathPUC = "";
    private String filePathRCBook = "";
    private String filePathProfile = "";
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private List<String> vehicleTypeList = new ArrayList<>();
    private ArrayAdapter<String> vehicleListAdapter;
    GPSTracker gps;
    double latitude, longitude;
    Uri selectedImage;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);


        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        initUI();

        actionbar_title.setText(R.string.actionbar_signup);
        bt_submit.setOnClickListener(this);
        iv_rc_book.setOnClickListener(this);
        iv_puc_photo.setOnClickListener(this);
        iv_vehicle_photo.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        vehicleListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item, vehicleTypeList);
        vehicleListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehicle_list.setAdapter(vehicleListAdapter);

        if (Constant.isOnline(getApplicationContext())) {
            vehicleTypeAPI();
        }


    }


    private void initUI() {
        Constant.CHECK_GPS = true;

        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_vehicle_number = (EditText) findViewById(R.id.et_vehicle_number);
        tv_rc_book_photo = (TextView) findViewById(R.id.tv_rc_book_photo);
        tv_puc_photo = (TextView) findViewById(R.id.tv_puc_photo);
        tv_vehicle_insurance_photo = (TextView) findViewById(R.id.tv_vehicle_insurance_photo);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);
        iv_rc_book = (ImageView) findViewById(R.id.iv_rc_book);
        iv_puc_photo = (ImageView) findViewById(R.id.iv_puc_photo);
        iv_vehicle_photo = (ImageView) findViewById(R.id.iv_vehicle_photo);
        spinner_vehicle_list = (Spinner) findViewById(R.id.spinner_vehicle_list);
        img_profile = (ImageView) findViewById(R.id.img_profile);

        bt_submit.setOnClickListener(this);
        iv_rc_book.setOnClickListener(this);
        iv_puc_photo.setOnClickListener(this);
        iv_vehicle_photo.setOnClickListener(this);

        galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:

                gps = new GPSTracker(v.getContext(), SignUp.this);
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("######", "lat: " + latitude + "long :" + longitude);
                    if (validate(et_user_name, et_email, et_vehicle_number, tv_rc_book_photo, tv_puc_photo
                            , tv_vehicle_insurance_photo, et_mobile_number, et_password, et_confirm_pass)) {
                        if (Constant.isOnline(getApplicationContext()))
                            if (filePathProfile == "") {
                                Toast.makeText(getApplicationContext(), "Please select profile image.", Toast.LENGTH_SHORT).show();
                            } else {
                                //varifyMobileNo(et_user_name, et_email, et_vehicle_number, et_mobile_number, et_password);
                                registration(et_user_name, et_email, et_vehicle_number, et_mobile_number, et_password);
                            }

                    }
                } else {
                    gps.showSettingsAlert();
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
                    ActivityCompat.requestPermissions(SignUp.this, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton(getString(R.string.label_cancel), null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
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
                           /* android.util.Log.d("######", "iiiiiiii" + objData);
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

    private void varifyMobileNo(EditText et_user_name,
                                EditText et_email,
                                EditText et_vehicle_number,
                                EditText et_mobile_number,
                                EditText et_password){

        Intent intent = new Intent(SignUp.this,VeryfyActivity.class);

        intent.putExtra("et_user_name",et_user_name.getText().toString());
        intent.putExtra("et_email",et_email.getText().toString());
        intent.putExtra("et_mobile_number",et_mobile_number.getText().toString());
        intent.putExtra("et_password",et_password.getText().toString());
        intent.putExtra("device_token",FirebaseInstanceId.getInstance().getToken());
        intent.putExtra("et_vehicle_number",et_vehicle_number.getText().toString());
        intent.putExtra("vehicalType",spinner_vehicle_list.getSelectedItem().toString());
        intent.putExtra("filePathProfile",filePathProfile);
        intent.putExtra("filePathRCBook",filePathRCBook);
        intent.putExtra("filePathPUC",filePathPUC);
        intent.putExtra("filePathInsurance",filePathInsurance);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);

        startActivity(intent);

    }



    private void registration(EditText et_user_name,
                              EditText et_email,
                              EditText et_vehicle_number,
                              final EditText et_mobile_number,
                              EditText et_password) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = bos.toByteArray();
        HttpPost postRequest = new HttpPost(Constant.URL_SINGUP);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            reqEntity.addPart("device", new StringBody("ANDROID"));
            reqEntity.addPart("v_name", new StringBody(et_user_name.getText().toString()));
            reqEntity.addPart("v_email", new StringBody(et_email.getText().toString()));
            reqEntity.addPart("v_phone", new StringBody(et_mobile_number.getText().toString()));
            reqEntity.addPart("v_password", new StringBody(et_password.getText().toString()));
            reqEntity.addPart("v_device_token", new StringBody("" + FirebaseInstanceId.getInstance().getToken()));
            reqEntity.addPart("v_vehicle_number", new StringBody(et_vehicle_number.getText().toString()));
            reqEntity.addPart("v_vehicle_type", new StringBody(spinner_vehicle_list.getSelectedItem().toString()));
            if (filePathProfile.equals("")) {

            } else {
                File user_image = new File(filePathProfile);
                reqEntity.addPart("v_image", new FileBody(user_image));
            }

            if (filePathRCBook.equals("")) {


            } else {
                File rc_book_file = new File(filePathRCBook);
                reqEntity.addPart("v_image_rc_book", new FileBody(rc_book_file));
            }
            if (filePathPUC.equals("")) {

            } else {
                File puc_file = new File(filePathPUC);
                reqEntity.addPart("v_image_puc", new FileBody(puc_file));
            }

            if (filePathInsurance.equals("")) {

            }else{
                File insurance_file = new File(filePathInsurance);
                reqEntity.addPart("v_image_insurance", new FileBody(insurance_file));
            }


            reqEntity.addPart("l_latitude", new StringBody(""+latitude));
            reqEntity.addPart("l_longitude", new StringBody(""+longitude));

            Log.d("######", "@@@@@@2" + reqEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MultipartRequest.setRequest(SignUp.this, reqEntity, postRequest, new MultipartResponce() {
            @Override
            public void responce(final JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("1")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        /*Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);*/

                        Intent intent = new Intent(getApplicationContext(), VeryfyActivity.class);
                        intent.putExtra("et_mobile_number",et_mobile_number.getText().toString());
                        startActivity(intent);

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private boolean validate(EditText et_user_name, EditText et_email, EditText et_vehicle_number,
                             TextView tv_rc_book_photo, TextView tv_puc_photo,
                             TextView tv_vehicle_insurance_photo, EditText et_mobile_number,
                             EditText et_password, EditText et_confirm_pass) {

        if (et_user_name.getText().toString().trim().length() > 0) {
            if (et_email.getText().toString().trim().length() > 0) {
                if (et_email.getText().toString().trim().matches(Constant.emailPattern)) {
                    if (et_vehicle_number.getText().toString().trim().length() > 0) {
                        if (tv_rc_book_photo.getText().toString().trim().length() > 0) {
                            if (tv_puc_photo.getText().toString().trim().length() > 0) {
                                if (tv_vehicle_insurance_photo.getText().toString().trim().length() > 0) {
                                    if (et_mobile_number.getText().toString().trim().length() > 0) {
                                        if (et_mobile_number.getText().toString().trim().length() == 10) {
                                        if (et_password.getText().toString().trim().length() > 0) {
                                            if (et_password.getText().toString().trim().length() >= 6) {
                                                if (et_password.getText().toString().trim().equals(et_confirm_pass.getText().toString().trim())) {
                                                    return true;
                                                } else
                                                    et_confirm_pass.setError("Please enter same password.");
                                            } else
                                                et_password.setError("Password must be six to ten characters.");
                                        } else et_password.setError("Please enter password.");
                                    } else et_mobile_number.setError("Please enter valid mobile no.");
                                    } else et_mobile_number.setError("Please enter mobile number.");
                                } else
                                    Toast.makeText(this, "Please choose vehicle insurance photo.", Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(this, "Please choose puc book photo.", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(this, "Please choose rc book photo.", Toast.LENGTH_SHORT).show();

                    } else et_vehicle_number.setError("Please enter vehicle number.");
                } else et_email.setError("Invalid email.");
            } else et_email.setError("Please enter email.");
        } else et_user_name.setError("Please enter full name.");
        return false;
    }

}
