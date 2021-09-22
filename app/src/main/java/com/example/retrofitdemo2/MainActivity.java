package com.example.retrofitdemo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.HashMap;
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
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {

    EditText edtname,edtemai,estaddress;
    CircleImageView circleImageView;
    Button addbtn;
    String filepath="", postpath="";

    public static String Base_url="http://192.168.43.41/android/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtemai=findViewById(R.id.email);
        edtname=findViewById(R.id.name);
        estaddress=findViewById(R.id.phone);

        circleImageView=findViewById(R.id.cicularimg);
        addbtn=findViewById(R.id.add);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= CropImage.activity().setAspectRatio(100,100).getIntent(MainActivity.this);
                startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    public void addUser(){

        Retrofit retrofit=null;
        if (retrofit==null){
            retrofit=new Retrofit.Builder().
                    baseUrl(Base_url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        MultipartBody.Part img=null;
        if(!postpath.equalsIgnoreCase("")){

            File file=new File(postpath);
            RequestBody imageData=RequestBody.create(MediaType.parse("image/*"),file);

            img=MultipartBody.Part.createFormData("profileImage",file.getName(),imageData);

        }
        RequestBody name=RequestBody.create(MediaType.parse("plain/text"),edtname.getText().toString());
        RequestBody email=RequestBody.create(MediaType.parse("plain/text"),edtemai.getText().toString());
        RequestBody address=RequestBody.create(MediaType.parse("plain/text"),estaddress.getText().toString());


        APIInterface apiInterface=retrofit.create(APIInterface.class);
        Map<String,RequestBody> param=new HashMap<>();
        param.put("NAME",name);
        param.put("EMAIL",email);
        param.put("ADDRESS",address);

        Call<POJO> call=apiInterface.addProfile(param,img);
        call.enqueue(new Callback<POJO>() {
            @Override
            public void onResponse(Call<POJO> call, Response<POJO> response) {
                if (response.body().getSuccess()==1){
                    Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<POJO> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        Intent intent=new Intent(MainActivity.this,ViewData.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            Uri resultUri=result.getUri();


            filepath=getRealPath(resultUri);

            Glide.with(MainActivity.this)
                    .load(filepath)
                    .into(circleImageView);

            postpath=filepath;


        }
    }

    String getRealPath(Uri resultUri)
    {
        String result;

        Cursor cursor=getBaseContext().getContentResolver().query(resultUri,null,null,null,null);

        if(cursor==null)
        {
            result=resultUri.getPath();
        }
        else
        {
            cursor.moveToNext();

            int index=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            result=cursor.getString(index);
            cursor.close();
        }
        return result;
    }
}