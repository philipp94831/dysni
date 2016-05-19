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
	private Short year;
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

	public CDRecord() {
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setdTitle(String dTitle) {
		this.dTitle = dTitle;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setCdExtra(String cdExtra) {
		this.cdExtra = cdExtra;
	}

	public void setYear(short year) {
		this.year = year;
	}

	public void setTracks(List<String> tracks) {
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

	public Short getYear() {
		return year;
	}

	public List<String> getTracks() {
		return tracks;
	}
	
	@Override
	public String toString() {
		return dId;
	}
}
