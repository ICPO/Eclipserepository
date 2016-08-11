package org.flycraft.dropchanger.content.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.flycraft.dropchanger.content.DropChangerContainer;
import org.flycraft.dropchanger.content.gui.scroll.GuiCustomScroll;
import org.flycraft.dropchanger.dataclasses.ItemMobData;
import org.flycraft.dropchanger.dataclasses.MobData;
import org.flycraft.dropchanger.managers.PacketManager;

import com.google.gson.Gson;

public class GuiBuffer {
	
	private DropChangerGui gui;
	
	public String xpFromText;
	public String xpToText;
	public String xpChanceText;
    public String itemIdText;
    public String itemMetaText;
    public String itemCountFromText;
    public String itemCountToText;
    public String itemChanceText;
	
    public String selectedMob;
    public String selectedItem;
	public final GuiCustomScroll mobScroll;
	public final GuiCustomScroll itemScroll;
	public final HashMap<String, MobData> mobs;
	public final DropChangerContainer slots;
	
    public boolean drawMessage;
    public String drawingMassageText;
    public int messageType;
    public int messageTimer;
    
    public EntityLivingBase clieckedEntity;


	public GuiBuffer(DropChangerGui gui, EntityLivingBase clieckedEntity) {
		mobScroll = gui.mobScroll;
		itemScroll = gui.itemScroll;
		mobs = gui.mobs;
		slots = gui.slots;
		this.gui = gui;
		
		xpFromText = "10";
		xpToText = "20";
		xpChanceText = "100";
		itemIdText = "1";
		itemMetaText = "0";
		itemCountFromText = "1";
		itemCountToText = "2";
		itemChanceText = "100";
		
		drawMessage = false;
		drawingMassageText = "none";
		messageType = 0;
		messageTimer = 0;
		
		this.clieckedEntity = clieckedEntity;
	}
	
    void loadElements() {
    	mobScroll.setUnsortedList(new ArrayList<String>(mobs.keySet()));
    	if(selectedMob != null) {
    		mobScroll.setSelected(selectedMob);
        	if(itemScroll.getList().isEmpty()) {
        		itemScroll.setUnsortedList(new ArrayList<String>(mobs.get(selectedMob).getItemsData().keySet()));
        	}
    	}
    	
    	if(selectedItem != null) {
    		itemScroll.setSelected(selectedItem);
    	}
    	
		if(selectedMob == null && !mobScroll.getList().isEmpty()) {
			String toSelect = mobScroll.getList().get(0).toString();
			selectMob(toSelect);
		}
		
		if(selectedMob != null && selectedItem == null && !itemScroll.getList().isEmpty()) {
			String toSelect = itemScroll.getList().get(0).toString();
			selectItem(toSelect);
			itemScroll.setSelected(toSelect);
		}
		
		if(itemScroll.getList().isEmpty()) {
			itemIdText = "0";
			itemChanceText = "0";
			itemCountFromText = "0";
			itemCountToText = "0";
			itemIdText = "0";
			itemMetaText = "0";
		}
		if(mobScroll.getList().isEmpty()) {
			xpChanceText = "0";
			xpFromText = "0";
			xpToText = "0";
		}
		
    }
    
    void selectClickedMob() {
		if(clieckedEntity != null) {
			System.out.println("Выбираем кликнутого моба");
			addMob(EntityList.getEntityID(clieckedEntity));
			clieckedEntity = null;
		}
    }
	
	String getGuiMobName() {
		if(selectedMob != null) {
			return selectedMob;
		} else {
			return I18n.getString("dropchanger.gui.entity.empty");
		}
	}
	
	String getGuiItemName() {
		if(selectedItem != null) {
			return "" + StatCollector.translateToLocal(selectedItem == null ? "" : StatCollector.translateToLocal(selectedItem) + ".name").trim();
		} else {
			return I18n.getString("dropchanger.gui.entity.item.empty");
		}
	}
	
	HashMap<String, ItemMobData> convertToModObject(ArrayList<EntityItem> defualtDrop) {
		
		HashMap<String, ItemMobData> converted = new HashMap<String, ItemMobData>();
		
		//for(int i = 0; i < defualt.size(); i++) {
		//	ItemStack item = defualt.get(i).getEntityItem();
			ItemMobData itemToAdd = new ItemMobData(332, 1, 2, 0, 100);
			converted.put(itemToAdd.getName(), itemToAdd);
		//}
		return converted;
	}
	
	void saveChanges() {
		
		// Сущность
		mobs.get(selectedMob).setXpDropFrom(Integer.parseInt(xpFromText));
		mobs.get(selectedMob).setXpDropTo(Integer.parseInt(xpToText));
		mobs.get(selectedMob).setXpDropChance(Integer.parseInt(xpChanceText));
		
		// Предмет
		if(selectedItem != null) {
			
			if(itemIdText.isEmpty() || itemMetaText.isEmpty() || itemCountFromText.isEmpty() || itemCountToText.isEmpty() || itemChanceText.isEmpty()) {
				setMessage(I18n.getString("dropchanger.gui.emtyField"), 2);
				return;
			} else {
			
				if(Long.parseLong(itemIdText) > 3200) {
					setMessage(I18n.getString("dropchanger.gui.tooBigInt") + ":$" + cropString(itemIdText, 40), 2);
					itemIdText = "3200";
					return;
				}
				if(Long.parseLong(itemMetaText) > 1000) {
					setMessage(I18n.getString("dropchanger.gui.tooBigInt") + ":$" + itemMetaText, 2);
					itemMetaText = "1000";
					return;
				}
				if(Long.parseLong(itemCountFromText) > 10000) {
					setMessage(I18n.getString("dropchanger.gui.tooBigInt") + ":$" + itemCountFromText, 2);
					itemCountFromText = "10000";
					return;
				}
				if(Long.parseLong(itemCountToText) > 10000) {
					setMessage(I18n.getString("dropchanger.gui.tooBigInt") + ":$" + itemCountFromText, 2);
					itemCountToText = "10000";
					return;
				}
				if(Long.parseLong(itemChanceText) > 100) {
					setMessage(I18n.getString("dropchanger.gui.tooBigInt") + ":$" + itemChanceText, 2);
					itemChanceText = "100";
					return;
				}
				if(Long.parseLong(xpChanceText) > 100) {
					setMessage(I18n.getString("dropchanger.gui.tooBigInt") + ":$" + xpChanceText, 2);
					itemChanceText = "100";
					return;
				}
				
				ItemMobData itemObject = mobs.get(selectedMob).getItemsData().get(selectedItem);
				
				itemObject.setID(Integer.parseInt(itemIdText));
				itemObject.setMetaData(Integer.parseInt(itemMetaText));
				itemObject.setCountFrom(Integer.parseInt(itemCountFromText));
				itemObject.setCountTo(Integer.parseInt(itemCountToText));
				itemObject.setChance(Integer.parseInt(itemChanceText));
				
			}
		}
		
		HashMap<String, ItemMobData> selectedMobItems = mobs.get(selectedMob).getItemsData();
		selectedMobItems.put(selectedMobItems.get(selectedItem).getName(), selectedMobItems.remove(selectedItem));
		
		itemScroll.setUnsortedList(new ArrayList<String>(mobs.get(selectedMob).getItemsData().keySet()));
		
		PacketManager.sendPacketToMod(new Gson().toJson(mobs));
	}
	
	void addMob(int entityID) {
		if(!mobs.containsKey(EntityList.getStringFromID(entityID))) {
			EntityLiving informer = (EntityLiving) EntityList.createEntityByID(entityID, Minecraft.getMinecraft().theWorld);
			ArrayList<EntityItem> defualtDrop = informer.capturedDrops;
			mobs.put(EntityList.getStringFromID(entityID), new MobData(4, 6, 100, convertToModObject(defualtDrop)));
			loadElements();
			mobScroll.setSelected(EntityList.getStringFromID(entityID));
			selectMob(EntityList.getStringFromID(entityID));
		} else {
			selectMob(EntityList.getStringFromID(entityID));
			setMessage(I18n.getString("dropchanger.gui.entity.entity.alreadyAdded"), 1);
		}
	}
	
	void addItem(ItemStack item) {
		ItemMobData mobItem = new ItemMobData(item.itemID, item.stackSize, item.stackSize, item.getItemDamage(), 100);
		if(!mobs.get(selectedMob).getItemsData().containsKey(mobItem.getName())) {
			mobs.get(selectedMob).addItem(mobItem.getName(), mobItem);
			loadElements();
			mobScroll.setSelected(selectedMob);
			selectMob(selectedMob);
			selectItem(mobItem.getName());
			itemScroll.setSelected(mobItem.getName());
		} else {
			selectItem(mobItem.getName());
			setMessage(I18n.getString("dropchanger.gui.entity.item.alreadyAdded"), 1);
		}
	}
	
	void selectMob(String mobName) {
		// Сущность
		selectedMob = mobName;
		MobData selectedMobObject = mobs.get(selectedMob);
		xpFromText = String.valueOf(selectedMobObject.getXpDropFrom());
		xpToText = String.valueOf(selectedMobObject.getXpDropTo());
		xpChanceText = String.valueOf(selectedMobObject.getXpDropChance());
		mobScroll.setSelected(mobName);
		
		// Предметы
		itemScroll.clear();
		itemScroll.setUnsortedList(new ArrayList<String>(selectedMobObject.getItemsData().keySet()));
		selectedItem = null;
		if(!itemScroll.getList().isEmpty()) {
			itemScroll.setSelected(itemScroll.getList().get(0).toString());
		}
		loadElements();
	}
	
	void selectItem(String itemName) {
		selectedItem = itemName;
		ItemMobData selectedItemObject = mobs.get(selectedMob).getItemsData().get(selectedItem);
		
		itemIdText = String.valueOf(selectedItemObject.getID());
		itemMetaText = String.valueOf(selectedItemObject.getMetaData());
		itemCountFromText = String.valueOf(selectedItemObject.getCountFrom());
		itemCountToText = String.valueOf(selectedItemObject.getCountTo());
		itemChanceText = String.valueOf(selectedItemObject.getDropChance());
	}
	
	void removeMob(String name) {
		mobs.remove(name);
		
		selectedMob = null;
		selectedItem = null;
		
		itemScroll.clear();
		
		//if(removed > 0) {
		//	selectMob(mobs.get(removed-1));
		//	itemScroll.setUnsortedList(selectedMob.getItemsNames());
		//}
		loadElements();
	}
	
	void removeItem(String name) {
		HashMap<String, ItemMobData> items = mobs.get(selectedMob).getItemsData();
		items.remove(name);
		
		selectedItem = null;
		itemScroll.clear();
		//if(removed > 0) {
		//	selectItem(mobs.get(selectedMob).getItemsData().get(removed-1).getDisplayName());
		//}
		loadElements();
	}
	
	String cropString(String toCrop, int stringLegth) {
		if(toCrop.length() > stringLegth) {
			return toCrop.substring(0, stringLegth-1) + "...";
		} else {
			return toCrop;
		}
	}
	
    public void setMessage(String text, int type) {
    	drawMessage = true;
    	drawingMassageText = text;
    	messageType = type;
    	messageTimer = 0;
    }
	
}
