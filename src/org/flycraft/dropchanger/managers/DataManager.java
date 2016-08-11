package org.flycraft.dropchanger.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.flycraft.dropchanger.DCInfo;
import org.flycraft.dropchanger.DropChanger;
import org.flycraft.dropchanger.dataclasses.MobData;
import org.flycraft.dropchanger.dataclasses.MobSqlData;
import org.flycraft.dropchanger.dataclasses.Settings;
import org.flycraft.dropchanger.dataclasses.SettingsDB;
import org.flycraft.dropchanger.thingsdoers.Sender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Менеджер данных. Выполняет сериализация и десериализация из/в Json-записи, хранит в себе ссылки на экзепляры классов настройки.
 * Пишет данные в файлы.
 * @author Alexandr
 *
 */

public class DataManager {
	public static Gson json = new GsonBuilder().setPrettyPrinting().create();
	
	private Settings settings;
	private SettingsDB settingDb;
	
	private final File sFile;
	private final File sDbFile;
	
	public DataManager(File falseFile) {
		sFile = new File(falseFile.getParentFile(), DCInfo.MOD_NAME + ".json");
		sDbFile = new File(falseFile.getParentFile(), DCInfo.MOD_NAME + "DB" + ".json");
	}
	
	public void init() {
		try {
			if(!sFile.exists()) {
				Sender.send("Creating new config-file...");
				
				sFile.createNewFile();
					FileWriter writer = new FileWriter(sFile);
					writer.write(json.toJson(new Settings()));
					writer.close();
				
			}
			
			if(!sDbFile.exists() && FMLCommonHandler.instance().getSide().isServer()) {
				Sender.send("Creating new DBconfig-file...");
				
				sDbFile.createNewFile();
					FileWriter writer = new FileWriter(sDbFile);
					writer.write(json.toJson(new SettingsDB()));
					writer.close();
				
			}
			
			BufferedReader readenSettingsFile = new BufferedReader(new FileReader(sFile));
			settings = json.fromJson(readenSettingsFile, Settings.class);
			
			if(FMLCommonHandler.instance().getSide().isServer()) {
				BufferedReader readenSettingsDBFile = new BufferedReader(new FileReader(sDbFile));
				settingDb = json.fromJson(readenSettingsDBFile, SettingsDB.class);
			}
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void saveData(HashMap<String, MobData> mobs) {
		settings.mobs = mobs;
		FileWriter writer;
		try {
			writer = new FileWriter(sFile);
			writer.write(json.toJson(settings));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DropChanger.eventCatcher.updateEntities();
	}
	
	public String[] getMySqlInfo() {
		return new String[] {settingDb.MySQLurl, settingDb.MySQLuser,  settingDb.MySQLpassword};
	}
	
	public boolean isSqlCustomQuery() {
		if(FMLCommonHandler.instance().getSide().isServer()) {
			return settingDb.MySQLCustomQuery;
		}
		return false;
	}
	
	public String getLang() {
		return settings.lang;
	}
	
	public boolean isDebug() {
		return settings.moreInfo;
	}
	
	public int getEntityCheckerID() {
		return settings.entityCheckerID;
	}
	
	public HashMap<String, MobData> getEntities() {
		return settings.mobs;
	}
	
	public int getEntitiesLeght() {
		return settings.mobs.size();
	}

	public HashMap<String, MobSqlData> getSqlData() {
		return settingDb.MySQLMobsQueries;
	}
	
}
