package com.example.retrofitdemo2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class POJOVIEW {
    @SerializedName("sucess")
    @Expose
    int sucess;

    @SerializedName("userlist")
    @Expose
    List<userlist> userlists;

    @SerializedName("message")
    @Expose
    String message;

    public int getSucess() {
        return sucess;
    }

    public List<userlist> getUserlists() {
        return userlists;
    }

    public String getMessage() {
        return message;
    }

    class userlist
    {
        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("profile")
        @Expose
        String profile;

        @SerializedName("email")
        @Expose
        String email;

        @SerializedName("address")
        @Expose
        String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
