package com.ventustium.MyLnList;

public class IdTitleModel {
    int id, idLN;
    String title;
    String description;

    public IdTitleModel(int id, int idLN, String title, String description) {
        this.id = id;
        this.idLN = idLN;
        this.title = title;
        this.description = description;
    }

    public IdTitleModel(String title, int id, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdTitleModel() {

    }

    public int getIdLN() {
        return idLN;
    }

    public void setIdLN(int idLN) {
        this.idLN = idLN;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
