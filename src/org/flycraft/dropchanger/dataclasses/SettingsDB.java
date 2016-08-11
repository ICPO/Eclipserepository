package org.flycraft.dropchanger.dataclasses;

import java.util.HashMap;

/**
 * Хранит настройки соеденения с базой данных.
 * @author Alexandr
 *
 */

public class SettingsDB {
	
	private final String[] INFO = new String[] {
			"# Если MySQLCustomStatements равен true, то во время включения сервера включаеться мэнэджер, который",
			"# будет выполнять произвольный SQL-запрос при убийстве определенного интити, если вы предварительно указали его",
			"# ниже(\"MySQLMobsQueries\").",
			"# ",
			"# Тег: имя игрока - (%p%);",
			"# ",
			"# ",
			"# If MySQLCustomQuery is true, that with server launching manager SQL-statements. It is will be send",
			"# your SQL-query when entity killed by player. You can prints SQL-query below \"MySQLMobsQueries\" ",
			"# ",
			"# Tag: player name - (%p%);",
			"# Example of setting/Пример настроек: http://pastebin.com/ad2JdjhB"
	};
	
	public boolean MySQLCustomQuery = false;
	public String MySQLurl = "mysql://localhost/site";
	public String MySQLuser = "root";
	public String MySQLpassword = "password";
	
	public HashMap<String, MobSqlData> MySQLMobsQueries = new HashMap<String, MobSqlData>();
	
}
