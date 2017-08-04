package com.codepath.gogreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
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
    public JSONObject resourceData;
    public JSONArray comments;


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

    public void setResourceData(JSONObject resourceData){
        this.resourceData = resourceData;
        this.put("resourceData", resourceData);
    }

    public JSONObject getResourceData(){
        return this.getJSONObject("resourceData");
    }

    public JSONArray getFavorited(){
        return this.getJSONArray("favorited");
    }

    public void setComments(JSONArray commentArray){
        this.comments = commentArray;
        this.put("comments", commentArray);
    }

    public JSONArray getComments() {
        return this.getJSONArray("comments");
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("actionType", getActionType());
        jsonObject.put("subType", getSubType());
        jsonObject.put("uid", getUid());
        jsonObject.put("magnitude", getMagnitude());
        jsonObject.put("points", getPoints());
        jsonObject.put("favorited", getFavorited());
        jsonObject.put("resourceData", getResourceData());
        jsonObject.put("comments", getComments());

        return jsonObject;

    }

    public static Action fromJSON(JSONObject jsonObject) throws JSONException {
        Action action = new Action();
        action.setActionType(jsonObject.getString("actionType"));
        if (jsonObject.has("subType")) {
            action.setSubType(jsonObject.getString("subType"));
        }
        action.setUid(jsonObject.getString("uid"));
        action.setMagnitude(jsonObject.getDouble("magnitude"));
        action.setPoints(jsonObject.getDouble("points"));
        if (jsonObject.has("favorited")) {
            action.setFavorited(jsonObject.getJSONArray("favorited"));
        }
        action.setResourceData(jsonObject.getJSONObject("resourceData"));
        if (jsonObject.has("comments")) {
            action.setComments(jsonObject.getJSONArray("comments"));
        }
        return action;
    }

}
