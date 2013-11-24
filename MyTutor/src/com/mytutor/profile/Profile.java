package com.mytutor.profile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;



public class Profile {
    
    private Bitmap picture_;
    
    private ArrayList<Integer> subCategory_;
    private String firstName_;
    private String lastName_;
    private double lat_;
    private double lon_;
    private String zipCode_;
    private boolean premium_;
    private String schedule_;
    private double rate_;
    private double rating_;
    private String profile_;
    private String email_;

    
    private final String PICTURE_KEY = "picture";
    private Map<String, String> attributeMap_;
    
    JSONArray json_;
    
    public Profile() {        
        // Default 200x200 bitmap
        picture_ = Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
        json_ = new JSONArray();
        
        subCategory_ = new ArrayList<Integer>();
    }
//    
//    public Bitmap getPicture() {
//        return attributeMap_.get(PICTURE_KEY);
//        return picture_;
//    }
//d
//    public void setPicture(Bitmap value) {
//        attributeMap_.put(PICTURE_KEY,  value);
//    }
//
//    public String getFirstName() {
//        return firstName_;
//    }
//
//    public void setFirstName(String firstName) {
//        firstName_ = firstName;
//    }
//
//    public String getLastName() {
//        return lastName_;
//    }
//
//    public void setLastName(String lastName) {
//        lastName_ = lastName;
//    }
//
//    public String[] getAddress() {
//        return address_;
//    }
//
//    public void setAddress(String[] address) {
//        address_ = address;
//    }
//
//    public String getCity() {
//        return city_;
//    }
//
//    public void setCity(String city) {
//        city_ = city;
//    }
//
//    public String getState() {
//        return state_;
//    }
//
//    public void setState(String state) {
//        state_ = state;
//    }
//
//    public String getZipCode() {
//        return zipCode_;
//    }
//
//    public void setZipCode(String zipCode) {
//        zipCode_ = zipCode;
//    }
//
//    public String[] getSubjects() {
//        return subjects_;
//    }
//
//    public void setSubjects(String[] subjects) {
//        subjects_ = subjects;
//    }

    public String serialize() {
        String ret = new String();
        
        

        
        
        // Encode the bitmap into Base64
        ByteArrayOutputStream stream = new ByteArrayOutputStream();  
        picture_.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ret += Base64.encodeToString(image, Base64.DEFAULT);
        
        
        return ret;
    }
    
    /**
     * Pull data out of a JSON encoded string
     * @param data
     */
    public void deserialize(String email, String data) {
        email_ = email;
        
        
        try {
            json_ = new JSONArray(data);
            
            for(int i = 0; i < json_.length(); i++) {
                JSONObject json_data = json_.getJSONObject(i);
                
                
                firstName_ = json_data.getString("firstName");
                lastName_ = json_data.getString("lastName");
                lat_ = json_data.getDouble("lat");
                lon_ = json_data.getDouble("lon");
                zipCode_ = json_data.getString("zipcode");
                premium_ = (0 != json_data.getInt("premium"));
                schedule_ = json_data.getString("schedule");
                rating_ = json_data.getDouble("rating");
                profile_ = json_data.getString("profile");
                subCategory_.add(json_data.getInt("subCategory"));
                

                String s = json_data.toString(i);
                Log.d("Profile", "Deserialized: " + s);
                
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
