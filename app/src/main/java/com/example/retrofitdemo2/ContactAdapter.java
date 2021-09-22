package com.example.retrofitdemo2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    Context mContext;
    List<POJOVIEW.userlist> userlists;

    public ContactAdapter(Context mContext, List<POJOVIEW.userlist> userlists) {
        this.mContext = mContext;
        this.userlists = userlists;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(mContext).inflate(R.layout.customdesign,parent,false);


        return new ContactHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        POJOVIEW.userlist userlist=userlists.get(position);

        holder.nametxt.setText(userlist.getName());



        Glide.with(mContext)
                .load(ViewData.base_url+userlist.getProfile())
                .into(holder.circleImageView);

        holder.nametxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ViewSingleUser.class);
                intent.putExtra("nameeee",holder.nametxt.getText().toString());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userlists.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView nametxt;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.getprofile);
            nametxt=itemView.findViewById(R.id.getname);

        }
    }
}
