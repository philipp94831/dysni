package de.hpi.idd.data.cd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.window.FixedWindowBuilder;

public class CDConfig {

	public static Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> config() {
		return Arrays.asList(new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String title = ((String) obj.get("dtitle")).toLowerCase();
				String artist = ((String) obj.get("artist")).toLowerCase();
				return StringUtils.substring(artist, 0, 3) + StringUtils.substring(title, 0, 3);
			}
		}, new FixedWindowBuilder<>(19)), new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String title = ((String) obj.get("dtitle")).toLowerCase();
				String artist = ((String) obj.get("artist")).toLowerCase();
				return StringUtils.substring(title, 0, 3) + StringUtils.substring(artist, 0, 3);
			}
		}, new FixedWindowBuilder<>(19)));
	}
}