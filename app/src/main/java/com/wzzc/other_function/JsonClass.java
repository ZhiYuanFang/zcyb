package com.wzzc.other_function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/22.
 *
 */

public class JsonClass {
    public static JSONArray jrrjrr (JSONArray jrr , int index) {
        try {
            return jrr.getJSONArray(index);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
    public static Long lj (JSONObject json,String key) {
        try {
            return json.getLong(key);
        } catch (JSONException e) {
            return 0L;
        }
    }
    public static String sj (JSONObject json , String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    public static boolean isHaveData(JSONObject jsonObj, String key){
        try{
            JSONObject json = jsonObj.getJSONObject(key);
            if(json == null || json.length() == 0){
                return false;
            }else{
                return true;
            }
        }catch(JSONException e){
            return false;
        }
    }

    public static int ij(JSONObject json , String key){
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    public static boolean boolj (JSONObject json , String key) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }
    }

    public static JSONObject jj(JSONObject json,String key){
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            return new JSONObject();
        }
   }

   public static JSONObject jjrr (JSONArray jrr , int index) {
       try {
           return jrr.getJSONObject(index);
       } catch (JSONException e) {
           return new JSONObject();
       }
   }

   public static String sjrr (JSONArray jrr , int index) {
       try {
           return jrr.getString(index);
       } catch (JSONException e) {
           return "";
       }
   }

   public static int ijrr (JSONArray jrr , int index) {
       try {
           return jrr.getInt(index);
       } catch (JSONException e) {
           return -1;
       }
   }

   public static String getElseMenuId(JSONArray jsonArray){
       String id = "";
       if(null == jsonArray || 0 == jsonArray.length()){
        return id;
       }
       for(int i = 0; i<jsonArray.length(); ++i){
           JSONObject jsonObj =  jsonArray.optJSONObject(i);
           int tempId = jsonObj.optInt("data_id", 0);
           id = id+tempId+"|";
       }
       return id;
   }

   public static JSONArray jrrj(JSONObject json , String key){
       try {
           return json.getJSONArray(key);
       } catch (JSONException e) {
           return new JSONArray();
       }
   }

   public static Object oj (JSONObject json,String key) {
       try {
           return json.get(key);
       } catch (JSONException e) {
           return null;
       }
   }

}
