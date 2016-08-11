package org.flycraft.dropchanger.thingsdoers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.flycraft.dropchanger.DropChanger;
import org.flycraft.dropchanger.dataclasses.MobData;
import org.flycraft.dropchanger.dataclasses.ItemMobData;
import org.flycraft.dropchanger.dataclasses.MobSqlData;

import cpw.mods.fml.common.FMLCommonHandler;

/**
 * Ловит события/реализует функции дропа самостоятельно.
 * @author Alexandr
 *
 */

public class DropCatcher {
	
	HashMap<String, MobData> mobsData;
	HashMap<String, MobSqlData> mobsSqlData;
	
	public DropCatcher() {
		mobsData = DropChanger.dataMng.getEntities();
		if(FMLCommonHandler.instance().getSide().isServer()) {
			mobsSqlData = DropChanger.dataMng.getSqlData();
		}
	}
	
	@ForgeSubscribe
	public void entityDropItems(LivingDropsEvent event) {
		
		if(!event.entity.worldObj.isRemote && !(event.entityLiving instanceof EntityPlayer)) {
			
			Entity eventEntity = event.entityLiving;
			String eventEntityName = EntityList.getEntityString(eventEntity);
			
			if(mobsData.containsKey(eventEntityName)) {
				event.setCanceled(true);
				
				MobData mobData = mobsData.get(eventEntityName);
				
				for(int i1 = 0; i1 < mobData.getItemsData().keySet().toArray().length; i1++) {
					ItemMobData item = mobData.getItemsData().get(mobData.getItemsData().keySet().toArray()[i1]);
					
					if(calculateChance(item.getDropChance())) {
						int count = MathHelper.getRandomIntegerInRange(new Random(), item.getCountFrom(), item.getCountTo());
						eventEntity.entityDropItem(new ItemStack(item.getID(), count, item.getMetaData()), 1.0F);
					}
				}
				
				disableXpDrop((EntityLivingBase) eventEntity);
				
				if(calculateChance(mobData.getXpDropChance())) {
					int count = MathHelper.getRandomIntegerInRange(new Random(), mobData.getXpDropFrom(), mobData.getXpDropTo());
					
	                while (count > 0)
	                {
	                    int j = EntityXPOrb.getXPSplit(count);
	                    count -= j;
	                    eventEntity.worldObj.spawnEntityInWorld(new EntityXPOrb(eventEntity.worldObj, eventEntity.posX, eventEntity.posY, eventEntity.posZ, count));
	                }
				}
			}
			if(DropChanger.dataMng.isSqlCustomQuery() && !mobsSqlData.isEmpty() && mobsSqlData.containsKey(eventEntityName)) {
				if(calculateChance(mobsSqlData.get(eventEntityName).getSqlQueryChance())) {
					DropChanger.sqlManager.addQuery(mobsSqlData.get(eventEntityName).getSqlQuery().replaceAll("%p%", event.source.getEntity().getEntityName()));
				}
			}
		}
	}
	
	public boolean calculateChance(int chance) {
		double random = Math.random();
		double randomFor = (double) chance / 100;
		
		if(random < randomFor) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Обновляет список мобов.
	 */
	
	public void updateEntities() {
		mobsData = DropChanger.dataMng.getEntities();
		if(FMLCommonHandler.instance().getSide().isServer()) {
			mobsSqlData = DropChanger.dataMng.getSqlData();
		}
	}
	
	private void disableXpDrop(EntityLivingBase entity) {
		try {
			Field field;
			try {
				field = getField(entity.getClass(), "field_70718_bc");
			} catch(NullPointerException e) {
				field = getField(entity.getClass(), "recentlyHit");
			}
			field.setAccessible(true);
			field.setInt(entity, 0);
		} catch (IllegalArgumentException e) { e.printStackTrace(); } catch (IllegalAccessException e) { e.printStackTrace(); }
	}
	
	private Field getField(Class clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class superClass = clazz.getSuperclass();
			return getField(superClass, fieldName);
		}
	}
	
}