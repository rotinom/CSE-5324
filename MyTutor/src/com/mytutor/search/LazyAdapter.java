package com.mytutor.search;
 
import java.util.ArrayList;
import java.util.HashMap;
 
import com.mytutor.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.mytutor.search.ImageLoader;
 
public class LazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader();
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.result_item, null);
        
        Log.i("SearchResults", "getView for position " + position);
        
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        TextView distance = (TextView)vi.findViewById(R.id.distance);
        TextView username = (TextView)vi.findViewById(R.id.username);
        TextView rate = (TextView)vi.findViewById(R.id.rate);
        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.rating);
        TextView email = (TextView)vi.findViewById(R.id.email);
        TextView premium = (TextView)vi.findViewById(R.id.premium);
 
        HashMap<String, String> tutor = new HashMap<String, String>();
        tutor = data.get(position);
 
        // Setting all values in listview
        distance.setText(tutor.get("distance"));
        username.setText(tutor.get("username"));
        rate.setText(tutor.get("rate"));
        ratingBar.setRating(Float.parseFloat(tutor.get("rating")));
        email.setText(tutor.get("email"));
        imageLoader.fetchDrawableOnThread("http://omega.uta.edu/~jwe0053/picture.php?email="+tutor.get("email"), image);
        
        if(Integer.parseInt(tutor.get("premium")) != 1)
        	premium.setVisibility(View.GONE);
        return vi;
    }
}