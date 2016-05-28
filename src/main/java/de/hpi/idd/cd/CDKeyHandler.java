package de.hpi.idd.cd;

import java.util.Map;

import de.hpi.idd.dysni.key.KeyComparator;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.key.LevenshteinComparator;

public class CDKeyHandler implements KeyHandler<Map<String, String>, String> {

	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.6);

	@Override
	public String computeKey(final Map<String, String> obj) {
		final String title = obj.get("dtitle");
		final String artist = obj.get("artist");
		return title.substring(0, Math.min(3, title.length())) + artist.substring(0, Math.min(3, artist.length()));
	}

	@Override
	public KeyComparator<String> getComparator() {
		return CDKeyHandler.COMPARATOR;
	}
}