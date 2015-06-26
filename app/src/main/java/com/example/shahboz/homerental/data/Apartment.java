package com.example.shahboz.homerental.data;

import com.example.shahboz.homerental.MyApp;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by shahboz on 14/06/2015
 * in context of HomeRental.
 */
@ParseClassName("_Apartment")
public class Apartment extends ParseObject {
    public Apartment(){

    }
    public String getDescription(){
        return getString("description");
    }
    public void setDesription(String desc){
        put("description",desc);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(MyApp.LOCATION);
    }

    public void setLocation(ParseGeoPoint value) {
        put(MyApp.LOCATION, value);
    }
    public void setCost(int c){
        put(MyApp.COST,c);
    }
    public int getCost(){
        return getInt(MyApp.COST);
    }
    public void setSize(double s){
        put(MyApp.SIZE,s);
    }
    public double getSize(){
        return getDouble(MyApp.SIZE);
    }
    public List<ParseFile> getPhotos(){
        return getList(MyApp.PHOTOS);
    }
    public void setPhotos(List<ParseFile> photos){
        put(MyApp.PHOTOS,photos);
    }
    public void addPhoto(ParseFile photo){
        add(MyApp.PHOTOS, photo);
    }
    public void setTags(List<String> tags){
        put(MyApp.TAGS, tags);
    }
    public List<String> getTags(){
        return getList(MyApp.TAGS);
    }
    public void addTag(String tag){
        add(MyApp.TAGS,tag);
    }
    public void setCity(String s){
        put(MyApp.CITY, s);
    }
    public String getCity(){
        return getString(MyApp.CITY);
    }
    public String getStreetName(){
        return getString(MyApp.STREET_NAME);
    }
    public void setStreetName(String s){
        put(MyApp.STREET_NAME,s);
    }
    public int getHomeNumber(){
        return getInt(MyApp.HOME_NUMBER);
    }
    public void setHomeNumber(int num){
        put(MyApp.HOME_NUMBER,num);
    }
}

