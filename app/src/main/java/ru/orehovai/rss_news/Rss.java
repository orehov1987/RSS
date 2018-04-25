package ru.orehovai.rss_news;

/*
класс для структуры поля списка новостей
 */

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class Rss{

    private static final String TAG = "MyLog";

    private String title;
    private String description;
    private Date pubDate;
    private String link;
    private Bitmap bitmap;//картинка
    private String image;//ссылка на картинку
    private boolean isBookmarked;//закладка

    public Rss(String title, String description, Date pubDate, String link, String image ,Bitmap bitmap, boolean isBookmarked) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.image = image;
        this.bitmap = bitmap;
        this.isBookmarked = isBookmarked;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public String getImage() {
        return this.image;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getPubDate() {
        return this.pubDate;
    }

    public String getLink() {
        return this.link;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    /*@Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd - hh:mm:ss");

        String result = getTitle() + "  ( " + simpleDateFormat.format(this.getPubDate()) + " )";
        return result;
    }*/




}
