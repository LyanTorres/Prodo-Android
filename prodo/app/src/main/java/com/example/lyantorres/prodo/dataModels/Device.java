package com.example.lyantorres.prodo.dataModels;

public class Device {
    private String _id;
    private String mName;
    private String mContentLinks;
    private String mThumbnailLink;

    public Device (String _id, String _name, String _contentLinks, String _thumbnailLink){
        this._id = _id;
        mName= _name;
        mContentLinks = _contentLinks;
        mThumbnailLink = _thumbnailLink;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String _name) {
        this.mName = _name;
    }

    public String getmContentLinks() {
        return mContentLinks;
    }

    public void setmContentLinks(String _contentLinks) {
        this.mContentLinks = _contentLinks;
    }

    public String getmThumbnailLink() {
        return mThumbnailLink;
    }

    public void setmThumbnailLink(String _thumbnailLink) {
        this.mThumbnailLink = _thumbnailLink;
    }
}
