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

	public CDRecord() {
	}

	public CDRecord(final String dId, final String artist, final String dTitle, final String category,
			final String genre, final String cdExtra, final short year, final List<String> tracks) {
		this.dId = dId;
		this.artist = artist;
		this.dTitle = dTitle;
		this.category = category;
		this.genre = genre;
		this.cdExtra = cdExtra;
		this.year = year;
		this.tracks = tracks;
	}

	public String getArtist() {
		return artist;
	}

	public String getCategory() {
		return category;
	}

	public String getCdExtra() {
		return cdExtra;
	}

	public String getdId() {
		return dId;
	}

	public String getdTitle() {
		return dTitle;
	}

	public String getGenre() {
		return genre;
	}

	public List<String> getTracks() {
		return tracks;
	}

	public Short getYear() {
		return year;
	}

	public void setArtist(final String artist) {
		this.artist = artist;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public void setCdExtra(final String cdExtra) {
		this.cdExtra = cdExtra;
	}

	public void setdId(final String dId) {
		this.dId = dId;
	}

	public void setdTitle(final String dTitle) {
		this.dTitle = dTitle;
	}

	public void setGenre(final String genre) {
		this.genre = genre;
	}

	public void setTracks(final List<String> tracks) {
		this.tracks = tracks;
	}

	public void setYear(final short year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return dId;
	}
}
