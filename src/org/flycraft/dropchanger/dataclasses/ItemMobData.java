package org.flycraft.dropchanger.dataclasses;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Используется для хранения информации о предмете.
 * Хранит: id, мета-дату, минимальное количество, максимальное количетов, шанс дропа.
 * @author Alexandr
 *
 */

public class ItemMobData {
	
	private int itemID;
	private int metaData;
	private int countFrom;
	private int countTo;
	private int chance;
	
	public ItemMobData(int itemID, int countFrom, int countTo, int metaData, int chance) {
		this.itemID = itemID;
		this.metaData = metaData;
		this.countFrom = countFrom;
		this.countTo = countTo;
		this.chance = chance;
	}
	
	// Геттеры
	
	public int getID() {
		return itemID;
	}
	
	public int getMetaData() {
		return metaData;
	}
	
	public String getName() {
		try {
			String item = new ItemStack(getID(), 1, getMetaData()).getUnlocalizedName();
			if(item != null) {
				return item;
			} else {
				return "Unknown";
			}
		} catch(java.lang.NullPointerException e) {
			return "Out of ID";
		}
	}
	
	public String getDisplayName() {
		try {
			String item = new ItemStack(getID(), 1, getMetaData()).getDisplayName();
			if(item != null && itemID < Item.itemsList.length) {
				return item;
			} else {
				return "Unknown";
			}
		} catch(java.lang.NullPointerException e) {
			return "Out of ID";
		}
	}
	
	public int getCountFrom() {
		return countFrom;
	}
	
	public int getCountTo() {
		return countTo;
	}
	
	public int getDropChance() {
		return chance;
	}
	
	// Сеттеры
	
	public void setID(int id) {
		itemID = id;
	}
	
	public void setMetaData(int meta) {
		metaData = meta;
	}
	
	public void setCountFrom(int from) {
		countFrom = from;
	}
	
	public void setCountTo(int to) {
		countTo = to;
	}
	
	public void setChance(int chance) {
		this.chance = chance;
	}
	
}
