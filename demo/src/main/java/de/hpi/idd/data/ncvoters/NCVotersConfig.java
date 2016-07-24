package de.hpi.idd.data.ncvoters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.window.FixedWindowBuilder;

public class NCVotersConfig {

	public static Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> config() {
		return Arrays.asList(new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String lastName = ((String) obj.get("last_name")).toLowerCase();
				String firstName = ((String) obj.get("first_name")).toLowerCase();
				return StringUtils.substring(firstName, 0, 7) + StringUtils.substring(lastName, 0, 7);
			}
		}, new FixedWindowBuilder<>(1)), new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String firstName = ((String) obj.get("first_name")).toLowerCase();
				String lastName = ((String) obj.get("last_name")).toLowerCase();
				return StringUtils.substring(lastName, 0, 7) + StringUtils.substring(firstName, 0, 7);
			}
		}, new FixedWindowBuilder<>(1)));
	}

}
