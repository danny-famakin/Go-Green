package com.codepath.gogreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by anyazhang on 7/14/17.
 */
@ParseClassName("Action")
public class Action extends ParseObject {
    long uid;
    String actionType;
    String subType;
    double magnitude;
    double points;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
        this.put("subType", subType);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
        this.put("uid", uid);
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
        this.put("actionType", actionType);
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
        this.put("magnitude", magnitude);
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
        this.put("points", points);
    }
}
