package de.hpi.idd.dysni.records;

import java.util.List;

/**
 * Created by dennis on 08.05.16.
 */
public class CDRecord {
    private String dId;
    private String artist;
    private String dTitle;
    private String category;
    private String genre;
    private String cdExtra;
    private short year;
    private List<String> tracks;

    public CDRecord(String dId, String artist, String dTitle, String category, String genre, String cdExtra, short year, List<String> tracks) {
        this.dId = dId;
        this.artist = artist;
        this.dTitle = dTitle;
        this.category = category;
        this.genre = genre;
        this.cdExtra = cdExtra;
        this.year = year;
        this.tracks = tracks;
    }

    public String getdId() {
        return dId;
    }

    public String getArtist() {
        return artist;
    }

    public String getdTitle() {
        return dTitle;
    }

    public String getCategory() {
        return category;
    }

    public String getGenre() {
        return genre;
    }

    public String getCdExtra() {
        return cdExtra;
    }

    public short getYear() {
        return year;
    }

    public List<String> getTracks() {
        return tracks;
    }
}
