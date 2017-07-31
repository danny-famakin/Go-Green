package com.codepath.gogreen.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by melissaperez on 7/27/17.
 */

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public String uid;
    public String body;
    public Date date;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
        this.put("uid", uid);
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
        this.put("body", body);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("body",  body);
            jsonObject.put("date", sdf.format(date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Comment fromJSON(JSONObject jsonObject) throws JSONException {
        Comment comment = new Comment();
        try {
            comment.uid = jsonObject.getString("uid");
            comment.body = jsonObject.getString("body");

            String dateStr = jsonObject.getString("date");
            Date parseDate = sdf.parse(dateStr);
            Log.d("date", parseDate.toString());
            comment.date = parseDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return comment;
    }
}
