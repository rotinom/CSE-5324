package com.mytutor.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;


public class SearchData 	
{	
    // Location information
    public List<Map> data = new ArrayList<Map>(); 
    
    static SearchData singleton_;
    	
    protected SearchData() {
        
    }
    
    public static SearchData getInstance() throws Exception{
    	if(null == singleton_){
    		throw new Exception("singleton_ not set.  Call create first!");
    	}
    	
    	return singleton_;
    }
    
    public static SearchData create(){
    	if(null == singleton_){
    		singleton_ = new SearchData();
    	}
    	
    	return singleton_;
    }
	
}