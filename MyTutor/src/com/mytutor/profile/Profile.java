package com.mytutor.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mytutor.session.ServerSession;



public class Profile {

    private Map<String, ArrayList<String>> categories_;
    
    
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
        json_ = new JSONArray();
        setCategories(new HashMap<String, ArrayList<String>>());
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
        
//        
//
//        
//        
//        // Encode the bitmap into Base64
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();  
//        picture_.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] image = stream.toByteArray();
//        ret += Base64.encodeToString(image, Base64.DEFAULT);
//        
//        
        return ret;
    }
    
    /**
     * Pull data out of a JSON encoded string
     * @param data
     */
    public void deserialize(String email, String data) {
        setEmail(email);
        
        try {
            // Get a session
            ServerSession session = ServerSession.create();
            
            json_ = new JSONArray(data);
            
            for(int i = 0; i < json_.length(); i++) {
                JSONObject json_data = json_.getJSONObject(i);
                
                
                setFirstName(json_data.getString("firstName"));
                setLastName(json_data.getString("lastName"));
                lat_ = json_data.getDouble("lat");
                lon_ = json_data.getDouble("lon");
                setZipCode(json_data.getString("zipcode"));
                premium_ = (0 != json_data.getInt("premium"));
                schedule_ = json_data.getString("schedule");
                rating_ = json_data.getDouble("rating");
                setProfile(json_data.getString("profile"));
                rate_ = json_data.getDouble("rate");
                
                String subcat_id = json_data.getString("subCategory");
                String subcat    = new String();
                
                String category = new String();

                category = session.getCategory(subcat_id);
                subcat = session.getSubcategoryNameFromId(subcat_id);

                
                
                // Create a list in the map if necessary
                if(!getCategories().containsKey(category)) {
                    ArrayList<String> subcat_list = new ArrayList<String>();
                    getCategories().put(category, subcat_list);
                }
                
                // Append the subcategory to our main category list
                getCategories().get(category).add(subcat);
                
                String s = json_data.toString(i);
                Log.d("Profile", "Deserialized: " + s);  
            }
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("Profile", "Exception! " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Profile", "Exception! " + e.toString());
            e.printStackTrace();
        }
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

    public String getEmail() {
        return email_;
    }

    public void setEmail(String email) {
        email_ = email;
    }

    public String getProfile() {
        return profile_;
    }

    public void setProfile(String profile) {
        profile_ = profile;
    }

    public String getZipCode() {
        return zipCode_;
    }

    public void setZipCode(String zipCode) {
        zipCode_ = zipCode;
    }

    public Map<String, ArrayList<String>> getCategories() {
        return categories_;
    }

    public void setCategories(Map<String, ArrayList<String>> categories) {
        categories_ = categories;
    }
    
}
