package com.example.radhika.demoapp;

/**
 * Created by Radhika on 03-05-2016.
 */
public class News {

    String title,content,publisher,date,url;

    public News(String title, String content, String publisher, String date,String url) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.date = date;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public News()
    {

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
