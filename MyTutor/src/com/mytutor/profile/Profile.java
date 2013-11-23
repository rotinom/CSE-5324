package com.mytutor.profile;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;



public class Profile {
    private Bitmap picture_;
    private String firstName_;
    private String lastName_;
    private String[] address_;
    private String city_;
    private String state_;
    private String zipCode_;
    private String[] subjects_;
    
    public Bitmap getPicture() {
        return picture_;
    }

    public void setPicture(Bitmap picture) {
        picture_ = picture;
    }

    public String getFirstName() {
        return firstName_;
    }

    public void setFirstName(String firstName) {
        firstName_ = firstName;
    }

    public String getLastName() {
        return lastName_;
    }

    public void setLastName(String lastName) {
        lastName_ = lastName;
    }

    public String[] getAddress() {
        return address_;
    }

    public void setAddress(String[] address) {
        address_ = address;
    }

    public String getCity() {
        return city_;
    }

    public void setCity(String city) {
        city_ = city;
    }

    public String getState() {
        return state_;
    }

    public void setState(String state) {
        state_ = state;
    }

    public String getZipCode() {
        return zipCode_;
    }

    public void setZipCode(String zipCode) {
        zipCode_ = zipCode;
    }

    public String[] getSubjects() {
        return subjects_;
    }

    public void setSubjects(String[] subjects) {
        subjects_ = subjects;
    }

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
    public void deserialize(String data) {
        
        JSONArray jArray;
        try {
            jArray = new JSONArray(data);

        
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                //ret.put(json_data.getString("name"), json_data.getString("categoryId"));
            } 
        } 
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
}
