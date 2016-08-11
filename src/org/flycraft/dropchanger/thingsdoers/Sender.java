package org.flycraft.dropchanger.thingsdoers;

import org.flycraft.dropchanger.DCInfo;
import org.flycraft.dropchanger.DropChanger;
import org.flycraft.dropchanger.managers.LocalManager;

public class Sender {
	
	/**
	 * Написать сообщение в консоль
	 * @param massage
	 */
	
	public static void send(String massage) {
		System.out.println("[" + DCInfo.MOD_NAME + "] " + massage);
	}
	
	public static void sendDebug(String massage) {
		if(DropChanger.dataMng.isDebug()) {
			System.out.println("[" + DCInfo.MOD_NAME + " | DEBUG" + "] " + massage);
		}
	}
	
	/**
	 * Написать сообщение в консоль через мультиязычную систему (sendWitchMultiLang)
	 * @param key
	 */
	
	// Стандартная
	public static void sendWML(String key) {
		send(LocalManager.getString(key));
	}
	
	public static void sendWMLWWithCustomPrefixAndPostfix(String prefix, String key, String postfix) {
		send(prefix + LocalManager.getString(key) + postfix);
	}
	
	public static void sendWMLWWithCustomPrefix(String prefix, String key) {
		sendWMLWWithCustomPrefixAndPostfix(prefix, key, "");
	}
	
	public static void sendWMLWWithCustomPostfix(String key, String postfix) {
		sendWMLWWithCustomPrefixAndPostfix("", key, postfix);
	}
	
	// Отладка
	public static void sendDebugWML(String key) {
		sendDebug(LocalManager.getString(key));
	}
	
	public static void sendDebugWMLWWithCustomPrefixAndPostfix(String prefix, String key, String postfix) {
		sendDebug(prefix + LocalManager.getString(key) + postfix);
	}
	
	public static void sendDebugWMLWWithCustomPrefix(String prefix, String key) {
		sendDebugWMLWWithCustomPrefixAndPostfix(prefix, key, "");
	}
	
	public static void sendDebugWMLWWithCustomPostfix(String key, String postfix) {
		sendDebugWMLWWithCustomPrefixAndPostfix("", key, postfix);
	}
	
}
