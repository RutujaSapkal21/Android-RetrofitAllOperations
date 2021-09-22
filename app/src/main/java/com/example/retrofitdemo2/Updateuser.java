package com.example.retrofitdemo2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Updateuser extends AppCompatActivity {
    CircleImageView circleImageView;
    EditText nameEdt, emailEdt, phoneEdt;

    public String username = "";

    String filepath = "", postpath = "";

    List<POJOVIEW.userlist> userlits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuser);

        nameEdt = findViewById(R.id.upname);
        emailEdt = findViewById(R.id.upemail);
        phoneEdt = findViewById(R.id.upphone);

        circleImageView = findViewById(R.id.upcicularimg);
        username = getIntent().getStringExtra("nameeee");


        userlits = new ArrayList<>();

        getUser(username);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = CropImage.activity()
                        .setAspectRatio(100, 100)
                        .getIntent(Updateuser.this);

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);


            }
        });
    }
    public void getUser(String name) {
        Retrofit retrofit=null;

        if (retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(MainActivity.Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        APIInterface apiInterface;

        apiInterface=retrofit.create(APIInterface.class);

        Map<String,String> map=new HashMap<>();
        map.put("name",name);




        Call<POJOVIEW> call=apiInterface.getSingleUser(map);

        call.enqueue(new Callback<POJOVIEW>() {
            @Override
            public void onResponse(Call<POJOVIEW> call, Response<POJOVIEW> response) {

                if (response.body().getSucess()==1)
                {
                    Toast.makeText(Updateuser.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    userlits.addAll(response.body().getUserlists());

                    for(int i=0;i< userlits.size();i++)
                    {


                        POJOVIEW.userlist us=userlits.get(i);
                        nameEdt.setText(us.getName());
                        emailEdt.setText(us.getEmail());
                        phoneEdt.setText(us.getPhone());
                        Glide.with(Updateuser.this)
                                .load(MainActivity.Base_url+us.getProfile())
                                .into(circleImageView);
                    }


                }
                else
                {
                    Toast.makeText(Updateuser.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<POJOVIEW> call, Throwable t) {

                Toast.makeText(Updateuser.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
    public void updateUser(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View customview=getLayoutInflater().inflate(R.layout.updatecust,null);
        builder.setView(customview);
        builder.setTitle("Update");
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getupdated();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void getupdated(){


        Retrofit retrofit = null;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MultipartBody.Part img = null;

        if (!postpath.equalsIgnoreCase("")) {
            File file = new File(postpath);

            RequestBody imageData = RequestBody.create(MediaType.parse("image/*"), file);

            img = MultipartBody.Part.createFormData("Profileimage", file.getName(), imageData);
        }

        RequestBody username1 = RequestBody.create(MediaType.parse("plain/text"), username);

        RequestBody name=RequestBody.create(MediaType.parse("plain/text"),nameEdt.getText().toString());
        RequestBody email=RequestBody.create(MediaType.parse("plain/text"),emailEdt.getText().toString());
        RequestBody address=RequestBody.create(MediaType.parse("plain/text"),phoneEdt.getText().toString());

       APIInterface apiInterface;

        apiInterface = retrofit.create(APIInterface.class);


        Map<String, RequestBody> param = new HashMap<>();
        param.put("name2", username1);
        param.put("name1", name);
        param.put("email", email);
        param.put("phone", address);

        Call<POJOVIEW> call = apiInterface.updateuser(param, img);

        call.enqueue(new Callback<POJOVIEW>() {
            @Override
            public void onResponse(Call<POJOVIEW> call, Response<POJOVIEW> response) {

                if (response.body().getSucess() == 1) {

                    Toast.makeText(Updateuser.this, response.body().getMessage(), Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(Updateuser.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<POJOVIEW> call, Throwable t) {

                Toast.makeText(Updateuser.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Intent intent= new Intent(Updateuser.this, ViewData.class);
        startActivity(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resultUri = result.getUri();


            filepath = getRealPath(resultUri);

            Glide.with(Updateuser.this)
                    .load(filepath)
                    .into(circleImageView);

            postpath = filepath;


        }
    }


    String getRealPath(Uri resultUri) {
        String result;

        Cursor cursor = getBaseContext().getContentResolver().query(resultUri, null, null, null, null);

        if (cursor == null) {
            result = resultUri.getPath();
        } else {
            cursor.moveToNext();

            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }
}