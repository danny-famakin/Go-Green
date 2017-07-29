package com.codepath.gogreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by melissaperez on 7/27/17.
 */

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public String uid;
    public String body;
    public String aid;

    public String getUid(){
        return this.getString("uid");
    }

    public void setUid(String uid){
        this.uid = uid;
        this.put("uid", uid);
    }

    public String getBody(){
        return this.getString("body");
    }

    public void setBody(String body){
        this.body = body;
        this.put("body", body);
    }

    public String getAid(){
        return this.getString("aid");
    }

    public void setAid(String aid){
        this.aid = aid;
        this.put("aid", aid);
    }
}
