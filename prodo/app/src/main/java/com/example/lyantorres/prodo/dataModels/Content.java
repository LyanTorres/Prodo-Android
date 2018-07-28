package com.example.lyantorres.prodo.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Content implements Serializable{
    private String _id;
    private String mName;
    private String mContentLink;
    private String mThumbnailLink;

    public Content(JSONObject _contentObj){

        try {
            this._id = _contentObj.getString("_id");
            mName = _contentObj.getString("name");
            mContentLink = _contentObj.getString("contentLink");
            mThumbnailLink = _contentObj.getString("thumbnailLink");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String get_id() {
        return _id;
    }

    public String getmName() {
        return mName;
    }

    public String getmContentLink() {
        return mContentLink;
    }

    public String getmThumbnailLink() {
        return mThumbnailLink;
    }

    public void setmThumbnailLink(String mThumbnailLink) {
        this.mThumbnailLink = mThumbnailLink;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmContentLink(String mContentLink) {
        this.mContentLink = mContentLink;
    }
}
