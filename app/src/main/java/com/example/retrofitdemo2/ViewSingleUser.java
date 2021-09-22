package com.example.retrofitdemo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewSingleUser extends AppCompatActivity {
    TextView txtname,txtmail,txtphone;
    CircleImageView circleImage;
    public String username = "";

    String filepath = "", postpath = "";

    List<POJOVIEW.userlist> userlits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_user);
        txtmail=findViewById(R.id.emailview);
        txtname=findViewById(R.id.nameview);
        txtphone=findViewById(R.id.phoneview);
        circleImage=findViewById(R.id.cicularview);

        username = getIntent().getStringExtra("nameeee");


        userlits = new ArrayList<>();

        getUser(username);

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
                    Toast.makeText(ViewSingleUser.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    userlits.addAll(response.body().getUserlists());

                    for(int i=0;i< userlits.size();i++)
                    {


                        POJOVIEW.userlist us=userlits.get(i);
                        txtname.setText(us.getName());
                        txtmail.setText(us.getEmail());
                        txtphone.setText(us.getPhone());

                        Glide.with(ViewSingleUser.this)
                                .load(MainActivity.Base_url+us.getProfile())
                                .into(circleImage);

                    }


                }
                else
                {
                    Toast.makeText(ViewSingleUser.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<POJOVIEW> call, Throwable t) {

                Toast.makeText(ViewSingleUser.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(getApplicationContext(), "Clicked on Edit", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ViewSingleUser.this, Updateuser.class);
                intent.putExtra("nameeee",username);
                startActivity(intent);
                break;
            case R.id.delete:
                Toast.makeText(getApplicationContext(), "Clicked on Delete", Toast.LENGTH_LONG).show();
                getUserDelete();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void getUserDelete(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View customview=getLayoutInflater().inflate(R.layout.deldialog,null);
        builder.setView(customview);
        builder.setTitle("Delete");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDelete();
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
    public void getDelete(){
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


        Map<String,String> param=new HashMap<>();

        param.put("name",username);

        Call<POJO> call=apiInterface.userDelete(param);


        call.enqueue(new Callback<POJO>() {
            @Override
            public void onResponse(Call<POJO> call, Response<POJO> response) {


                if (response.body().getSuccess()==1)
                {
                    Toast.makeText(ViewSingleUser.this,response.body().getMessage(),Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(ViewSingleUser.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<POJO> call, Throwable t) {

                Toast.makeText(ViewSingleUser.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        Intent intent=new Intent(ViewSingleUser.this,ViewData.class);
        startActivity(intent);


    }
}
