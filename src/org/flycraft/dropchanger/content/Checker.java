package org.flycraft.dropchanger.content;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.flycraft.dropchanger.DCInfo;
import org.flycraft.dropchanger.DropChanger;

public class Checker extends Item {
	
	public Checker() {
		super(DropChanger.instance.dataMng.getEntityCheckerID());
		this.setUnlocalizedName("informator");
		
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer player, EntityLivingBase clieckedEntity) {
		DropChanger.guiHandler.openInfromator(player, clieckedEntity);
        return true;
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
		DropChanger.guiHandler.openInfromator(player, null);
        return par1ItemStack;
    }
	
	@Override
    public String getUnlocalizedName(){
        return DCInfo.MOD_ID + ".item." + super.getUnlocalizedName().substring(5);
    }
	
	@Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return getUnlocalizedName();
    }
	
	@Override
    public void registerIcons(IconRegister par1IconRegister) {
        itemIcon = par1IconRegister.registerIcon(DCInfo.MOD_ID + ":" + getUnlocalizedName().substring(6 + DCInfo.MOD_ID.length()));
    }

}
