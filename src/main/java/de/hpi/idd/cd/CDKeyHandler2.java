package de.hpi.idd.cd;

import java.util.Map;

import de.hpi.idd.dysni.key.KeyComparator;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.key.LevenshteinComparator;

public class CDKeyHandler2 implements KeyHandler<Map<String, String>, String> {

	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.8);

	@Override
	public String computeKey(final Map<String, String> obj) {
		final String title = obj.get("dtitle");
		final String artist = obj.get("artist");
		return artist.substring(0, Math.min(3, artist.length())) + title.substring(0, Math.min(3, title.length()));
	}

	@Override
	public KeyComparator<String> getComparator() {
		return CDKeyHandler2.COMPARATOR;
	}
}