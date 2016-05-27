package de.hpi.idd.dysni.records;

import java.util.Arrays;
import java.util.Map;

class CDRecordParser {

	public static CDRecord parse(final Map<String, String> record) {
		final CDRecord cd = new CDRecord();
		cd.setdId(record.get("did"));
		cd.setArtist(record.get("artist"));
		cd.setdTitle(record.get("dtitle"));
		cd.setCategory(record.get("category"));
		final String year = record.get("year");
		if (!year.isEmpty()) {
			cd.setYear(Short.parseShort(year));
		}
		cd.setGenre(record.get("genre"));
		cd.setCdExtra(record.get("cdextra"));
		cd.setTracks(Arrays.asList(record.get("tracks").split("\\|")));
		return cd;
	}
}
