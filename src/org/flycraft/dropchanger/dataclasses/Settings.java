package org.flycraft.dropchanger.dataclasses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Хранит основные настройки мода, служит объектом сериализации и десериализации.
 * @author Alexandr
 *
 */

public class Settings {
	
	private final String[] INFO = new String[] {
			"# Рекомендованная подсветка синтаксиса: JavaScripts",
			"# ",
			"# Приятного использования.",
			"# ",
			"# Recommended syntax highlighting: JavaScripts",
			"# ",
			"# Enjoy using."
			};
	
	public String lang = "en";
	public boolean moreInfo = false;
	public int entityCheckerID = 1000;
	
	public HashMap<String, MobData> mobs = new HashMap<String, MobData>();
}
