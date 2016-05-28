package de.hpi.idd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hpi.idd.cd.CDKeyHandler;
import de.hpi.idd.cd.CDKeyHandler2;
import de.hpi.idd.dysni.key.KeyHandler;

public class KeyHandlerManager {

	public static List<KeyHandler<Map<String, String>, String>> getKeyHandlers(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return Arrays.asList(new CDKeyHandler(), new CDKeyHandler2());
		default:
			return Collections.emptyList();
		}
	}
}
