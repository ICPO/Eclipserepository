package org.flycraft.dropchanger.managers;

import java.util.Locale;
import java.util.ResourceBundle;

import org.flycraft.dropchanger.DropChanger;

/**
 * Мэнеджер консольных языков. Отвечат за локализацию. Имеет static метод getString(key).
 * @author Alexandr
 *
 */

public class LocalManager {
	
	private static ResourceBundle currentLang;  
	
	public LocalManager() {
		
		currentLang = ResourceBundle.getBundle("org.flycraft.dropchanger.local.lang", new Locale(DropChanger.instance.dataMng.getLang()));
	}
	
	public static String getString(String key) {
		return currentLang.getString(key);
		
	}
	
}
