package com.example.shahboz.homerental.data;

import com.example.shahboz.homerental.MyApp;
import com.parse.ParseClassName;
import com.parse.ParseUser;



/**
 * Created by shahboz on 14/06/2015.
 */
@ParseClassName("_User")
public class MyUser extends ParseUser {

    public void setType(String type) {
        put(MyApp.USER_TYPE,type);
    }

    public String getType(){
        return getString(MyApp.USER_TYPE);
    }
}
