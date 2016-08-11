package org.flycraft.dropchanger.content;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;

public class DropChangerContainer extends Container {
	
	private Slot mobSlot;
	private Slot mobItemSlot;
	
	public DropChangerContainer(InventoryPlayer par1InventoryPlayer) {
		
        byte b0 = 35;
        short short1 = 143;
		int i;
		
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, b0 + j * 18, short1 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(par1InventoryPlayer, i, b0 + i * 18, 58 + short1));
        }
        
        addSlotToContainer(mobSlot = new Slot(par1InventoryPlayer, inventorySlots.size(), 10, 143) {
        	@Override
            public boolean isItemValid(ItemStack par1ItemStack) {
        		if(par1ItemStack.getItem() instanceof ItemMonsterPlacer) {
        			return true;
        		}
                return false;
            }
        });
        addSlotToContainer(mobItemSlot = new Slot(par1InventoryPlayer, inventorySlots.size() + 1, 202, 143));
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	public Slot getMobSlot() {
		return mobSlot;
	}
	
	public Slot getItemSlot() {
		return mobItemSlot;
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0)
            {
                if (!mergeItemStack(itemstack1, 1, 37, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 >= 1 && par2 < 28)
            {
                if (!mergeItemStack(itemstack1, 28, 37, false))
                {
                    return null;
                }
            }
            else if (par2 >= 28 && par2 < 37)
            {
                if (!mergeItemStack(itemstack1, 1, 28, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 1, 37, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
