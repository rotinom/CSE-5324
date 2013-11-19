package com.mytutor.profile;
 
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mytutor.R;
 
public class ProfileCategoryAdapter extends BaseAdapter {
 
    private Activity activity_;
    private ArrayList<HashMap<String, String>> data_;
    private LayoutInflater inflater_; 
 
    public ProfileCategoryAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        activity_ = activity;
        data_     = data;
        inflater_ = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return data_.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null) {
            vi = inflater_.inflate(R.layout.category_item, null);
        }
        
        Log.i("ProfileCategoryAdapter", "getView for position " + position);
        

        TextView mainCat = (TextView)vi.findViewById(R.id.mainCatEditText);
        TextView subCat  = (TextView)vi.findViewById(R.id.subCatEditText);
 
        HashMap<String, String> category = data_.get(position);
        
        mainCat.setText(category.get("main"));
        subCat.setText(category.get("subcategory"));
        
        return vi;
    }
}