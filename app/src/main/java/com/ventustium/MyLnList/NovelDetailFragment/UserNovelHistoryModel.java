package com.ventustium.MyLnList.NovelDetailFragment;

public class UserNovelHistoryModel {
    int id, idLN, volume, chapter, score;
    String googleID, status;

    public UserNovelHistoryModel(int id, int idLN, int volume, int chapter, int score, String googleID, String status) {
        this.id = id;
        this.idLN = idLN;
        this.volume = volume;
        this.chapter = chapter;
        this.score = score;
        this.googleID = googleID;
        this.status = status;
    }

    public UserNovelHistoryModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLN() {
        return idLN;
    }

    public void setIdLN(int idLN) {
        this.idLN = idLN;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
