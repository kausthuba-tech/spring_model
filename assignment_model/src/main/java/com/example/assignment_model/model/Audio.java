package com.example.assignment_model.model;


public class Audio {

    private String artistName;
    private String trackTitle;
    private String albumTitle;
    private Integer trackNumber;
    private Integer year;
    private Integer reviewCount;
    private Integer copiesSold;

    public Audio() {
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getCopiesSold() {
        return copiesSold;
    }

    public void setCopiesSold(Integer copiesSold) {
        this.copiesSold = copiesSold;
    }

    public Audio(String artistName, String trackTitle, String albumTitle, Integer trackNumber, Integer year, Integer reviewCount, Integer copiesSold) {
        this.artistName = artistName;
        this.trackTitle = trackTitle;
        this.albumTitle = albumTitle;
        this.trackNumber = trackNumber;
        this.year = year;
        this.reviewCount = reviewCount;
        this.copiesSold = copiesSold;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "artistName='" + artistName + '\'' +
                ", trackTitle='" + trackTitle + '\'' +
                ", albumTitle='" + albumTitle + '\'' +
                ", trackNumber=" + trackNumber +
                ", year=" + year +
                ", reviewCount=" + reviewCount +
                ", copiesSold=" + copiesSold +
                '}';
    }
}

