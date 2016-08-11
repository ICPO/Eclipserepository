package org.flycraft.dropchanger.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import org.flycraft.dropchanger.DCInfo;
import org.flycraft.dropchanger.DropChanger;
import org.flycraft.dropchanger.dataclasses.MobData;
import org.flycraft.dropchanger.thingsdoers.Sender;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * Получает и отслыает пакеты.
 * @author Alexandr
 *
 */

public class PacketManager implements IPacketHandler {
	
	private final Side side = FMLCommonHandler.instance().getEffectiveSide();
	
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
    	DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    	
    	if(packet.channel.equals(DCInfo.MOD_CHANNEL_TOMOD) || packet.channel.equals(DCInfo.MOD_CHANNEL_TOPLAYER)) {
	        StringBuffer inputLine = new StringBuffer();
	        try {
	            String tmp;
				while ((tmp = inputStream.readLine()) != null) {
				    inputLine.append(tmp);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
	        if(packet.channel.equals(DCInfo.MOD_CHANNEL_TOMOD)) {
	        	DropChanger.instance.dataMng.saveData((HashMap<String, MobData>)new Gson().fromJson(inputLine.toString().substring(2), new TypeToken<HashMap<String, MobData>>(){}.getType()));
	        	return;
	        }
	        if(packet.channel.equals(DCInfo.MOD_CHANNEL_TOPLAYER)) {
	        	DropChanger.guiHandler.waiter.waitIt = (HashMap<String, MobData>) new Gson().fromJson(inputLine.toString().substring(2), new TypeToken<HashMap<String, MobData>>(){}.getType());
	        	return;
	        }
    	}
    	
    	if(packet.channel.equals(DCInfo.MOD_CHANNEL_SIMPLECOMMANDS)) {
    		EntityPlayer rPlayer = (EntityPlayer) player;
	    	if(rPlayer.worldObj.isRemote) {
	        	try {
	        		
					switch(inputStream.readInt()) {
						case 0: DropChanger.guiHandler.doPermissionsWarningFromClient(); return;
						default: Sender.sendWML("packet.handling.wrongcode");
					}
				} catch (IOException e) { e.printStackTrace(); }
	    	} else {
	    		
	        	try {
					switch(inputStream.readInt()) {
						case 0: DropChanger.guiHandler.openDropChangerGuiFromServer(player); return;
						default: Sender.sendDebugWML("packet.handling.wrongcode");
					}
				} catch (IOException e) { e.printStackTrace(); }
	    	}
    	}
    }
    
    /**
     * Отправляет сообщение на сервер
     * @param player
     * @param message
     */
    
    public static void sendPacketToMod(String message) {
    	PacketDispatcher.sendPacketToServer(stringToPacket(message, DCInfo.MOD_CHANNEL_TOMOD));
    }
    
    /**
     * Отправляет простую команду(int) серверу для дальнейшей обработки
     * @param code
     */
    
    public static void sendSimplePacketToMod(int code) {
    	PacketDispatcher.sendPacketToServer(intToPacket(code, DCInfo.MOD_CHANNEL_SIMPLECOMMANDS));
    }
    
    public static void sendSimplePacketToPlayer(int code, Player player) {
    	PacketDispatcher.sendPacketToPlayer(intToPacket(code, DCInfo.MOD_CHANNEL_SIMPLECOMMANDS), player);
    }
    
    /**
     * Отправляет пакет на клиент(игроку)
     * @param player
     * @param message
     */
    
    public static void sendPacketToPlayer(EntityPlayer player, String message) {
    	PacketDispatcher.sendPacketToPlayer(stringToPacket(message, DCInfo.MOD_CHANNEL_TOPLAYER), (Player) player);
    }
    
    
    private static Packet250CustomPayload intToPacket(int code, String channel) {
    	
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
        	outputStream.writeInt(code);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bos.toByteArray();
        packet.length = bos.size();
    	
    	
		return packet;
    }
    private static Packet250CustomPayload stringToPacket(String message, String channel) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
        	outputStream.writeUTF(message);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        
        try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return packet;
        
    }
    
}