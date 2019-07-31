package com.stimednp.dtsmywisata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rivaldy on 7/29/2019.
 */

public class Wisatas implements Parcelable {
    private int id;
    private String title;
    private String coor_latitude;
    private String coor_longitude;
    private String desc;
    private String date;
    private String url_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoor_latitude() {
        return coor_latitude;
    }

    public void setCoor_latitude(String coor_latitude) {
        this.coor_latitude = coor_latitude;
    }

    public String getCoor_longitude() {
        return coor_longitude;
    }

    public void setCoor_longitude(String coor_longitude) {
        this.coor_longitude = coor_longitude;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.coor_latitude);
        dest.writeString(this.coor_longitude);
        dest.writeString(this.desc);
        dest.writeString(this.date);
        dest.writeString(this.url_image);
    }

    public Wisatas() {
    }

    protected Wisatas(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.coor_latitude = in.readString();
        this.coor_longitude = in.readString();
        this.desc = in.readString();
        this.date = in.readString();
        this.url_image = in.readString();
    }

    public static final Creator<Wisatas> CREATOR = new Creator<Wisatas>() {
        @Override
        public Wisatas createFromParcel(Parcel source) {
            return new Wisatas(source);
        }

        @Override
        public Wisatas[] newArray(int size) {
            return new Wisatas[size];
        }
    };
}
