package com.poluectov.criticine.webapp.model.data;

public class Cinema {
   int id;
    String name;
    float rating;
    int creationYear;
    String pictureFile;
    int cinemaTypeId;
    CinemaType cinemaType;

    public Cinema(int id, String name, float rating, int creationYear, String pictureFile, int cinemaTypeId) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.creationYear = creationYear;
        this.pictureFile = pictureFile;
        this.cinemaTypeId = cinemaTypeId;
    }

    public Cinema(String name, float rating, int creationYear, String pictureFile, int cinemaTypeId) {
        this.name = name;
        this.rating = rating;
        this.creationYear = creationYear;
        this.pictureFile = pictureFile;
        this.cinemaTypeId = cinemaTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(int creationYear) {
        this.creationYear = creationYear;
    }

    public String getPictureFile() {
        return pictureFile;
    }

    public void setPictureFile(String pictureFile) {
        this.pictureFile = pictureFile;
    }

    public CinemaType getCinemaType() {
        return cinemaType;
    }

    public void setCinemaType(CinemaType cinemaType) {
        this.cinemaType = cinemaType;
    }

    public int getCinemaTypeId() {
        return cinemaTypeId;
    }

    public void setCinemaTypeId(int cinemaTypeId) {
        this.cinemaTypeId = cinemaTypeId;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", creationYear=" + creationYear +
                ", pictureFile='" + pictureFile + '\'' +
                ", cinemaTypeId=" + cinemaTypeId +
                ", cinemaType=" + cinemaType +
                '}';
    }
}