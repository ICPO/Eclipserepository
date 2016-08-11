package org.flycraft.dropchanger.content;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import org.flycraft.dropchanger.dataclasses.MobData;
import org.flycraft.dropchanger.managers.PacketManager;

/**
 * Ждет пакет с данными о дропе от сервера, как дождется - отсылает пакет с запросом на открывание гуи сервером.
 * Хранит в себе игрока, использовавшего информатор, интити,
 * по которому кликнул игрок(если не кликнул, clieckedEntity = null), по прибытию пакета с данными хранит еще и данные.
 * Завершается в момент открывания GUI, отдавая хранимые данные GUI'шке.
 * 
 * @author Alexandr
 *
 */

public class Waiter extends Thread {
	
	private EntityPlayer player;
	public volatile HashMap<String, MobData> waitIt;
	public volatile boolean isRunning = true;
	public EntityLivingBase clieckedEntity;
	
	public Waiter(EntityPlayer player, EntityLivingBase clieckedEntity) {
		this.player = player;
		this.clieckedEntity = clieckedEntity;
	}
	
    @Override
    public void run() {
    	synchronized (this) {
    		int attempts = 0;
	    	while(isRunning) {
	    		try {
					wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		if(waitIt != null) {
	    			PacketManager.sendSimplePacketToMod(0);
	    			break;
	    		}
	    		if(attempts > 5) {
	    			player.addChatMessage(I18n.getString("dropchanger.packet.missing"));;
	    			break;
	    		}
	    		attempts++;
	    	}
    	}
    }
    
    public void kill() {
    	isRunning = false;
    }
	
    public void stopWithPermissionsError() {
    	kill();
    	player.addChatMessage(I18n.getString("warining.playerDontHavePermissions"));
    }
}
