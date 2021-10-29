package com.ksusha.coopybook.adapter;

import java.io.Serializable;

//Serializable, чтобы Intent понимал, что он передает сразу весь класс
public class ListItem implements Serializable {
    private String title;
    private String description;
    private String linkToPhoto = "empty";
    private int id = 0;

    public String getTitle() { //для получения данных
        return title;
    }

    public void setTitle(String title) { //для записи данных
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkToPhoto() {
        return linkToPhoto;
    }

    public void setLinkToPhoto(String linkToPhoto) {
        this.linkToPhoto = linkToPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
