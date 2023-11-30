package com.poluectov.criticine.webapp.model.data;

public class UserCinemaReview {
    int id;
    int userId;
    User user;
    int cinemaId;
    Cinema cinema;
    int rating;
    String review;

    public UserCinemaReview(int id, int userId, int cinemaId, int rating, String review) {
        this.id = id;
        this.userId = userId;
        this.cinemaId = cinemaId;
        this.rating = rating;
        this.review = review;
    }

    public UserCinemaReview(int userId, int cinemaId, int rating, String review) {
        this.userId = userId;
        this.cinemaId = cinemaId;
        this.rating = rating;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }
}