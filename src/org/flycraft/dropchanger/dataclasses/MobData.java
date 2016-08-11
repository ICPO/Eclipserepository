package org.flycraft.dropchanger.dataclasses;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;

/**
 * Используется для хранения информации о мобе, в отличае от класса Mob используется для общения сервера и клиента.
 * Хранит: имя, дропающий опыт, предметы
 * @author Alexandr
 */

public class MobData {
	
	protected int xpFrom;
	protected int xpTo;
	protected int xpDropChance;
	protected HashMap<String, ItemMobData> items = new HashMap<String, ItemMobData>();
	
	public MobData(int xpFrom, int xpTo, int xpDropChance, HashMap<String, ItemMobData> items) {
		this.xpFrom = xpFrom;
		this.xpTo = xpTo;
		this.xpDropChance = xpDropChance;
		this.items = items;
	}
	
	public HashMap<String, ItemMobData> getItemsData() {
		return items;
	}
	
	public int getXpDropFrom() {
		return xpFrom;
	}
	
	public int getXpDropTo() {
		return xpTo;
	}
	
	public void setXpDropFrom(int count) {
		xpFrom = count;
	}
	
	public void setXpDropTo(int count) {
		xpTo = count;
	}
	
	public void addItem(String name, ItemMobData item) {
		items.put(name, item);
	}
	
	public void setXpDropChance(int chace) {
		xpDropChance = chace;
	}

	public int getXpDropChance() {
		return xpDropChance;
	}
	
}
