package org.flycraft.dropchanger.content;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import org.flycraft.dropchanger.DropChanger;
import org.flycraft.dropchanger.content.gui.DropChangerGui;
import org.flycraft.dropchanger.dataclasses.MobData;
import org.flycraft.dropchanger.managers.PacketManager;
import org.flycraft.dropchanger.thingsdoers.Sender;

import com.google.gson.Gson;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DropChangerGuiHandler implements IGuiHandler {
	
	@SideOnly(Side.CLIENT)
	public Waiter waiter;
	
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,int x, int y, int z) {
		if(id == 1) {
			return new DropChangerContainer(player.inventory);
		}
		return null;
    }
	
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	if(id == 1) {
    		HashMap<String, MobData> mobs = waiter.waitIt;
    		EntityLivingBase clieckedEntity = waiter.clieckedEntity;
    		waiter.kill();
    		return new DropChangerGui(player.inventory, mobs, clieckedEntity);
    	}
    	return null;
    }
    
    /**
     * Вызывает Waiter, начинает ожидание пакета с информацией о мобах
     * @param player
     * @param clieckedEntity
     */
    
    @SideOnly(Side.CLIENT)
    public void waitForPacket(EntityPlayer player, EntityLivingBase clieckedEntity) {
		waiter = new Waiter(player, clieckedEntity);
		waiter.start();
    }
    
    public void openInfromator(EntityPlayer player, EntityLivingBase clieckedEntity) {
    	Side side = FMLCommonHandler.instance().getSide();
    	
    	boolean isClientWithLocalServer = false;
    	boolean isOp = false;
    	boolean isPerms = false;
    	
    	if(side.isClient() && MinecraftServer.getServer() != null) {
    		isClientWithLocalServer = true;
    	}
    	if(side.isServer()) {
    		isOp = MinecraftServer.getServer().getConfigurationManager().getOps().contains(player.username.toLowerCase().trim());
    		isPerms = false; //TODO: Работа с правами/плагин permission
    	}
		if(player.worldObj.isRemote) { // Клиент
			waitForPacket(player, clieckedEntity);
		}
    	
		if(isClientWithLocalServer || isOp || isPerms) { // Сервер
			if(!player.worldObj.isRemote) {
				PacketManager.sendPacketToPlayer(player, new Gson().toJson(DropChanger.instance.dataMng.getEntities()));
			}
		} else { // Если игрок без прав
			if(!player.worldObj.isRemote && (!isOp || !isPerms)) {
				Sender.sendDebugWMLWWithCustomPrefixAndPostfix("[SERVER PROTECTION WARNING] ", "warning.onUseWithoutPermission", " " + player.username);
				PacketManager.sendSimplePacketToPlayer(0, (Player) player); // Говорим клиенту произвести действие при отсутствие прав
			}
		}
    }
    
    @SideOnly(Side.CLIENT)
    public void doPermissionsWarningFromClient() {
    	waiter.stopWithPermissionsError();
    }
    
    /**
     * Открывает GUI инспектора(Checker) со стороны сервера
     * @param player
     */
    
    public void openDropChangerGuiFromServer(Player player) {
    	EntityPlayerMP mpPLayer = (EntityPlayerMP) player;
    	FMLNetworkHandler.openGui(mpPLayer, DropChanger.instance, 1, mpPLayer.worldObj, (int) mpPLayer.posX, (int) mpPLayer.posY, (int) mpPLayer.posZ);
    }
}