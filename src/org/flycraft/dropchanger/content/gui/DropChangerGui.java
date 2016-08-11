package org.flycraft.dropchanger.content.gui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.flycraft.dropchanger.DCInfo;
import org.flycraft.dropchanger.content.DropChangerContainer;
import org.flycraft.dropchanger.content.gui.scroll.GuiCustomScroll;
import org.flycraft.dropchanger.content.gui.scroll.GuiCustomScrollActionListener;
import org.flycraft.dropchanger.dataclasses.MobData;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DropChangerGui extends GuiContainer implements GuiCustomScrollActionListener
{
    private static final ResourceLocation dropChangerGuiTextures = new ResourceLocation(DCInfo.MOD_ID, "textures/gui/container/dropchanger.png");
    public HashMap<String, MobData> mobs;
    
    public GuiCustomScroll mobScroll;
    public GuiCustomScroll itemScroll;
    
    public final DropChangerContainer slots;
    
    public DCTextField xpFromTextField;
    public DCTextField xpToTextField;
    public DCTextField xpChanceTextField;
    public DCTextField itemIdTextField;
    public DCTextField itemMetaTextField;
    public DCTextField itemCountFromTextField;
    public DCTextField itemCountToTextField;
    public DCTextField itemChanceTextField;
    
    private final Minecraft mc;
	private final GuiBuffer buffer;
	private final FontRenderer customFontRenderer;
	
	//Расположение
	private int itemElementsBarOffset = 95;

    public DropChangerGui(InventoryPlayer par1InventoryPlayer, HashMap<String, MobData> mobs, EntityLivingBase clieckedEntity) {
        super(new DropChangerContainer(par1InventoryPlayer));
        
        xSize = 256;
        ySize = 224;
        mc = Minecraft.getMinecraft();
        
        customFontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, true);
        
        initBasic();
        slots = (DropChangerContainer) super.inventorySlots;
        this.mobs = mobs;
        
        buffer = new GuiBuffer(this, clieckedEntity);
        }
    
    private void initBasic() {
    	 mobScroll = new GuiCustomScroll(this, 0, false);
    	 itemScroll = new GuiCustomScroll(this, 1, true);
    }
    
    @Override
    public void initGui() {
    	super.initGui();
    	
        mobScroll.setWorldAndResolution(super.mc, 350, 250);
        mobScroll.setFontRenderer(customFontRenderer);
        mobScroll.setSize(110, 49);
        mobScroll.guiLeft = this.guiLeft + 12;
        mobScroll.guiTop = this.guiTop + 28;
        
        itemScroll.setWorldAndResolution(super.mc, 350, 250);
        itemScroll.setFontRenderer(customFontRenderer);
        itemScroll.setSize(113, 49);
        itemScroll.guiLeft = this.guiLeft + 129;
        itemScroll.guiTop = this.guiTop + 28;
        
        xpFromTextField = new DCTextField(getFontRenderer(), this.guiLeft + 33, this.guiTop + 98, 20, 10);
        xpFromTextField.setMaxStringLength(3);
        xpToTextField = new DCTextField(getFontRenderer(), this.guiLeft + 75, this.guiTop + 98, 20, 10);
        xpToTextField.setMaxStringLength(3);
        xpChanceTextField = new DCTextField(getFontRenderer(), this.guiLeft + 99, this.guiTop + 98, 20, 10);
        xpChanceTextField.setMaxStringLength(3);
        
        itemIdTextField = new DCTextField(getFontRenderer(), this.guiLeft + 146, this.guiTop + itemElementsBarOffset, 24, 10);
        itemMetaTextField = new DCTextField(getFontRenderer(), this.guiLeft + 217, this.guiTop + itemElementsBarOffset, 24, 10);
        itemCountFromTextField = new DCTextField(getFontRenderer(), this.guiLeft + 149, this.guiTop + itemElementsBarOffset + 22, 24, 10);
        itemCountToTextField = new DCTextField(getFontRenderer(), this.guiLeft + 188, this.guiTop + itemElementsBarOffset + 22, 24, 10);
        itemChanceTextField = new DCTextField(getFontRenderer(), this.guiLeft + 217, this.guiTop + itemElementsBarOffset + 22, 24, 10);
        itemChanceTextField.setMaxStringLength(3);
        
        setButton(1, I18n.getString("dropchanger.gui.entity.remove"), 23, 77, 0);
        setButton(2, I18n.getString("dropchanger.gui.entity.item.remove"), 144, 77, 0);
        
        setButton(3, I18n.getString("dropchanger.gui.entity.save"), 22, 113, 1);
        setButton(4, "", 11, 164, 2);
        setButton(5, "", 203, 164, 2);
    	
        buffer.selectClickedMob();
    	buffer.loadElements();
    	
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    	String guiName = I18n.getString("dropchanger.item.informator.name");
        customFontRenderer.drawString(guiName, xSize / 2 - fontRenderer.getStringWidth(guiName) / 2, 6, 0xFFFFFF);
        
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.name")  + ": " + buffer.cropString(buffer.getGuiMobName(), 17), xSize / 16, 18, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.xp.count") + ": ", xSize / 16, 88, 0xFFFFFF);
        customFontRenderer.drawString("/", xSize / 16 + 72, 88, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.xp.count.chance") + ": ", xSize / 16 + 80, 88, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.xp.count.from")  + ": ", xSize / 16 + 5, 98, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.xp.count.to") + ": ", xSize / 16 + 45, 98, 0xFFFFFF);
        
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item") + ": " + buffer.cropString(buffer.getGuiItemName(), 17), xSize / 2 + 5, 18, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item.id") + ": ", xSize / 2 + 5, itemElementsBarOffset, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item.meta") + ": ", xSize / 2 + 49, itemElementsBarOffset, 0xFFFFFF);
        customFontRenderer.drawString("/", xSize / 2 + 60, itemElementsBarOffset + 12, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item.chance") + ": ", xSize / 2 + 80, itemElementsBarOffset + 12, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item.count") + ": ", xSize / 2 + 5, itemElementsBarOffset + 12, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item.count.from")  + ": ", xSize / 2 + 9, itemElementsBarOffset + 22, 0xFFFFFF);
        customFontRenderer.drawString(I18n.getString("dropchanger.gui.entity.item.count.to") + ": ", xSize / 2 + 47, itemElementsBarOffset + 22, 0xFFFFFF);
        
    }
    
    @Override
    public void drawScreen(int par1, int par2, float par3) {
    	super.drawScreen(par1, par2, par3);
    	
    	RenderHelper.disableStandardItemLighting();
    	
    	mobScroll.drawScreen(par1, par2, par3);
    	itemScroll.drawScreen(par1, par2, par3);
    	
    	xpFromTextField.drawTextBox();
    	xpToTextField.drawTextBox();
    	xpChanceTextField.drawTextBox();
    	itemIdTextField.drawTextBox();
    	itemMetaTextField.drawTextBox();
    	itemCountFromTextField.drawTextBox();
    	itemCountToTextField.drawTextBox();
    	itemChanceTextField.drawTextBox();
    	
    	if(buffer.drawMessage) {
    		drawMessage();
    	}
    	RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        
        xpFromTextField.mouseClicked(i, j, k);
        xpToTextField.mouseClicked(i, j, k);
        xpChanceTextField.mouseClicked(i, j, k);
        itemIdTextField.mouseClicked(i, j, k);
    	itemMetaTextField.mouseClicked(i, j, k);
    	itemCountFromTextField.mouseClicked(i, j, k);
    	itemCountToTextField.mouseClicked(i, j, k);
    	itemChanceTextField.mouseClicked(i, j, k);
        
        mobScroll.mouseClicked(i, j, k);
        itemScroll.mouseClicked(i, j, k);
     }
    
    @Override
    protected void keyTyped(char par1, int par2) {
    	if(par1 == KeyEvent.VK_ESCAPE) {
    		mc.thePlayer.closeScreen();
    		return;
    	}
    	
    	if (itemIdTextField.isFocused() && itemIdTextField.textboxKeyTyped(par1, par2)) {
    		buffer.itemIdText = itemIdTextField.getText();
    		return;
    	}
    	
    	if (xpFromTextField.isFocused() && xpFromTextField.textboxKeyTyped(par1, par2)) {
    		buffer.xpFromText = xpFromTextField.getText();
    		return;
    	}
    	
    	if (xpToTextField.isFocused() && xpToTextField.textboxKeyTyped(par1, par2)) {
    		buffer.xpToText = xpToTextField.getText();
    		return;
    	}
    	
    	if (xpChanceTextField.isFocused() && xpChanceTextField.textboxKeyTyped(par1, par2)) {
    		buffer.xpChanceText = xpChanceTextField.getText();
    		return;
    	}
    	
    	if(itemMetaTextField.isFocused() && itemMetaTextField.textboxKeyTyped(par1, par2)) {
    		buffer.itemMetaText = itemMetaTextField.getText();
    		return;
    	}
    	
    	if(itemCountFromTextField.isFocused() && itemCountFromTextField.textboxKeyTyped(par1, par2)) {
    		buffer.itemCountFromText = itemCountFromTextField.getText();
    		return;
    	}
    	
    	if(itemCountToTextField.isFocused() && itemCountToTextField.textboxKeyTyped(par1, par2)) {
    		buffer.itemCountToText = itemCountToTextField.getText();
    		return;
    	}
    	
    	if(itemChanceTextField.isFocused() && itemChanceTextField.textboxKeyTyped(par1, par2)) {
    		buffer.itemChanceText = itemChanceTextField.getText();
    		return;
    	}
	}
    
    @Override
    public void updateScreen() {
    	super.updateScreen();
    	
    	if(!xpFromTextField.getText().equals(buffer.xpFromText)) {
    		xpFromTextField.setText(buffer.xpFromText);
    	}
    	if(!xpToTextField.getText().equals(buffer.xpToText)) {
    		xpToTextField.setText(buffer.xpToText);
    	}
    	if(!xpChanceTextField.getText().equals(buffer.xpToText)) {
    		xpChanceTextField.setText(buffer.xpChanceText);
    	}
    	if(!itemIdTextField.getText().equals(buffer.itemIdText)) {
    		itemIdTextField.setText(buffer.itemIdText);
    	}
    	if(!itemMetaTextField.getText().equals(buffer.itemMetaText)) {
    		itemMetaTextField.setText(buffer.itemMetaText);
    	}
    	if(!itemCountFromTextField.getText().equals(buffer.itemCountFromText)) {
    		itemCountFromTextField.setText(buffer.itemCountFromText);
    	}
    	if(!itemCountToTextField.getText().equals(buffer.itemCountToText)) {
    		itemCountToTextField.setText(buffer.itemCountToText);
    	}
    	if(!itemChanceTextField.getText().equals(buffer.itemChanceText)) {
    		itemChanceTextField.setText(buffer.itemChanceText);
    	}
    	
    	if(buffer.drawMessage) {
			if(mc.theWorld.getTotalWorldTime() % 100 == 0) {
				buffer.messageTimer++;
			}
			if(buffer.messageTimer > 1) {
				buffer.drawMessage = false;
				buffer.messageTimer = 0;
			}
    	}
    	
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(dropChangerGuiTextures);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
    }
    
    protected FontRenderer getFontRenderer() {
    	return customFontRenderer;
    }
	
	@Override
	public void customScrollClicked(int var1, int var2, int var3, GuiCustomScroll var4) {
		
		if(mobScroll.isChanged()) {
			buffer.selectMob(mobScroll.getSelected());
			if(!itemScroll.getList().isEmpty()) {
				itemScroll.setSelected(itemScroll.getList().get(0).toString());
			}
		}
		
		if(!itemScroll.getList().isEmpty()) { // itemScroll.isChanged()
			buffer.selectItem(itemScroll.getSelected());
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		// Без мобов
		switch (par1GuiButton.id) {
    		case 4: {
    			if(slots.getMobSlot().getStack() != null) {
    				ItemMonsterPlacer mobPlacer = (ItemMonsterPlacer) slots.getMobSlot().getStack().getItem();
    				int entityID = mobPlacer.getDamage(slots.getMobSlot().getStack());
    				buffer.addMob(entityID); break;
    			} else {
    				buffer.setMessage(I18n.getString("dropchanger.gui.entity.entity.slotEmpty"), 1);
    			}
    		}
		}
		
		// С мобами
		if(mobScroll.getList().size() > 0) {
			if(par1GuiButton.id == 2) {
				buffer.removeItem(itemScroll.getSelected());
				if(itemScroll.getList().size() == 0) {
					buffer.setMessage(I18n.getString("dropchanger.gui.message.info.noitem"), 0);
				}
				return;
			}
			
	        switch (par1GuiButton.id) {
	        	case 1: buffer.removeMob(mobScroll.getSelected()); break;
	        	case 3: buffer.saveChanges(); break;
	        	case 5: {
	        		if(slots.getItemSlot().getStack() != null) {
	        			buffer.addItem(slots.getItemSlot().getStack()); break;
	        		} else {
	        			buffer.setMessage(I18n.getString("dropchanger.gui.entity.item.slotEmpty"), 1);
	        		}
	        	}
	        }
		} else {
			buffer.setMessage(I18n.getString("dropchanger.gui.message.info.nomob"), 0);
		}
	}
	
	/**
	 * @param text - текст
	 * @param type - тип(0 - информация, 1 - предупреждение, 2 - ошибка)
	 */
	
    private void drawMessage() {
    	
    	int x = guiLeft + 232;
    	int y = guiTop + 142;
    	
    	String textureName = "info";
    	String typeText = null;
    	
    	switch(buffer.messageType) {
    		case 0: {
	    		textureName = "info";
	    		typeText = EnumChatFormatting.BLUE + I18n.getString("dropchanger.gui.message.info");
	    	} break;
	    	case 1: {
	    		textureName = "warning";
	    		typeText = EnumChatFormatting.YELLOW + I18n.getString("dropchanger.gui.message.warning");
	    	} break;
	    	case 2: {
	    		textureName = "error";
	    		typeText = EnumChatFormatting.RED + I18n.getString("dropchanger.gui.message.error");
	    	} break;
	    	default: {
	    		textureName = "error";
	    		typeText = EnumChatFormatting.RED + I18n.getString("dropchanger.gui.message.unknown");
	    	}
    	}
    	
    	mc.getTextureManager().bindTexture(new ResourceLocation(DCInfo.MOD_ID, "textures/gui/container/messages/body.png"));
    	customDrawTexturedModalRect(x, y, 0, 0, 100, 80, 0.00992325F, 0.0125325F);
    	
		mc.getTextureManager().bindTexture(new ResourceLocation(DCInfo.MOD_ID, "textures/gui/container/messages/" + textureName + ".png"));
		customDrawTexturedModalRect(x + 4, y + 4, 0, 0, 14, 14, 0.09422325F, 0.1025325F);
		
		fontRenderer.drawString(typeText, x + 25, y + 7, 0xFFFFFF);
		
		int pathingY = 0;
		
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < buffer.drawingMassageText.length(); i++) {
			if(buffer.drawingMassageText.charAt(i) == '$') {
				fontRenderer.drawString(builder.toString(), x + 7, y + 22 + pathingY, 0xFFFFFF);
				builder = new StringBuilder();
				pathingY += 10;
			} else {
				builder.append(buffer.drawingMassageText.charAt(i));
			}
			if(buffer.drawingMassageText.length() == i + 1) {
				fontRenderer.drawString(builder.toString(), x + 8, y + 20 + pathingY, 0xFFFFFF);
			}
			
		}
    }

    
    public void customDrawTexturedModalRect(int par1, int par2, int par3, int par4, double d, double par6, float f, float f1) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + d), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + d) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + d), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + d) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }
	
    private void setButton(int id, String text, int x, int y, int type) {
    	int w = 0;
    	int h = 0;
    	int textureY = 0;
    	
    	switch(type) {
    	case 0: {
    		w = 87;
    		h = 10;
    		textureY = 86;
    	} break;
    	case 1: {
    		w = 92;
    		h = 18;
    		textureY = 22;
    	} break;
    	case 2: {
    		w = 14;
    		h = 14;
    		textureY = 58;
    	} break;
    	}
    	buttonList.add(new DCButton(id, guiLeft + x, guiTop + y, text, w, h, textureY, customFontRenderer));
    }
    
}
