package com.example.jihye.newsfeed;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.widget.Button;

public class ListViewItem {
    private Drawable musicalImage;
    private String musicalTitle;
    private String musicalDate;

    public void setImage(Drawable icon) {
        musicalImage = icon;
    }

    public void setTitle(String title) {
        musicalTitle = title;
    }

    public void setDate(String date) {
        musicalDate = date;
    }

    public Drawable getImage() {
        return this.musicalImage;
    }

    public String getTitle() {
        return this.musicalTitle;
    }

    public String getDate() {
        return this.musicalDate;
    }

}
