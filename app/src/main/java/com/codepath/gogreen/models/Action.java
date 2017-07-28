package com.codepath.gogreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by anyazhang on 7/14/17.
 */
@ParseClassName("Action")
public class Action extends ParseObject {
    public String uid;
    public String actionType;
    public String subType;
    public double magnitude;
    public double points;
    public JSONArray favorited;
<<<<<<< HEAD
<<<<<<< HEAD
    public Comment comments;
=======
    public ParseUser user;
=======
    public JSONObject resourceData;
>>>>>>> 7fdbb8189dce7e8aa2d51cac5fb9264f21d8acb0

>>>>>>> 63cfe2caac5bf62e319760dd3849797cf39d0be7

    public String getSubType() {
        return this.getString("subType");
    //        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
        this.put("subType", subType);
    }

    public String getUid() {
        return this.getString("uid");

    //        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        this.put("uid", uid);
    }

    public String getActionType() {
        return this.getString("actionType");

    //        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
        this.put("actionType", actionType);
    }

    public double getMagnitude() {
        return this.getDouble("magnitude");
    //        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        this.put("magnitude", magnitude);
    }

    public double getPoints() {

        return this.getDouble("points");
    //        return points;
    }

    public void setPoints(double points) {
        this.points = points;
        this.put("points", points);
    }

    public void setFavorited(JSONArray favorited) {
        this.favorited = favorited;
        this.put("favorited", favorited);

    }

    public JSONArray getFavorited(){
        return favorited;
    }


}
