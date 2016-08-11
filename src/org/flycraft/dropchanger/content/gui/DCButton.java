package org.flycraft.dropchanger.content.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.flycraft.dropchanger.DCInfo;
import org.lwjgl.opengl.GL11;

public class DCButton extends GuiButton {

	protected static final ResourceLocation dcButtonTextures = new ResourceLocation(DCInfo.MOD_ID, "textures/gui/widgets.png");
	
	private int yOnTextureMap;
	
	FontRenderer fontRenderer;
	
	public DCButton(int par1, int par2, int par3, String par4Str, int w, int h, int yOnTextureMap, FontRenderer customFontRenderer) {
		super(par1, par2, par3, w, h, par4Str);
		this.yOnTextureMap = yOnTextureMap;
		fontRenderer = customFontRenderer;
	}

	@Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (drawButton) {
            par1Minecraft.getTextureManager().bindTexture(dcButtonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
            int l = 14737632;
            int yOnTextureMap = this.yOnTextureMap;
            
            if (flag)
            {
                yOnTextureMap += height;
            }
            
            if (!enabled)
            {
                l = -6250336;
            }
            else if (field_82253_i)
            {
                l = 16777120;
            }

            drawTexturedModalRect(xPosition, yPosition, 0, yOnTextureMap, width, height);
            drawCenteredString(fontRenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, l);
        }
    }

}
