package com.example.retrofitdemo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewData extends AppCompatActivity {
    RecyclerView recyclerView;
    public static String base_url="http://192.168.43.41/android/";
    ContactAdapter contactAdapter;
    List<POJOVIEW.userlist> userlists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);


        userlists=new ArrayList<>();
        recyclerView=findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        getUserProfiles();

    }
    void getUserProfiles()
    {
        Retrofit retrofit=null;

        if (retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        APIInterface apiView;

        apiView=retrofit.create(APIInterface.class);


        Call<POJOVIEW> call=apiView.viewAllusers();


        call.enqueue(new Callback<POJOVIEW>() {
            @Override
            public void onResponse(Call<POJOVIEW> call, Response<POJOVIEW> response) {
                if (response.body().getSucess()==1)
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewData.this));
                {


                    userlists.addAll(response.body().getUserlists());


                    contactAdapter=new ContactAdapter(ViewData.this,userlists);
                    recyclerView.setAdapter(contactAdapter);

                    contactAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<POJOVIEW> call, Throwable t) {
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.adddata,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adddata:
                Toast.makeText(getApplicationContext(), "Clicked on Add", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewData.this,MainActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}