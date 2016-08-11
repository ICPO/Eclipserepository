package org.flycraft.dropchanger.content.gui.scroll;

import java.util.List;

import org.flycraft.dropchanger.content.gui.DropChangerGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Реализация скролл-меню предметов.
 * 
 * В отличает от класса {@link GuiCustomScroll} локализует преюмет относительно его кодового имени
 * 
 * @author Alexandr
 *
 */

public class GuiItemScroll extends GuiCustomScroll {

	private final FontRenderer customFontRenderer;
	
	private final Minecraft mc = Minecraft.getMinecraft();
	
	public GuiItemScroll(DropChangerGui parent, int id) {
		super(parent, id);
		customFontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, true);
	}

	@Override
	protected void drawItems() { // Вшиваем локализацию "на ходу"
		for (int i = 0; i < list.size(); ++i) {
			byte j = 4;
			int k = 14 * i + 4 - scrollY;
			if (k >= 4 && k + 12 < ySize) {
				String text = (String) list.get(i);
				if ((!multipleSelection || !selectedList.contains(text))
						&& (multipleSelection || selected != i)) {
					if (i == hover) {
						fontRenderer.drawString(cropString(getDisplayItemName(text), 22), j, k, '\uff00');
					} else {
						fontRenderer.drawString(cropString(getDisplayItemName(text), 22), j, k, 16777215);
					}
				} else {
					drawVerticalLine(j - 2, k - 4, k + 10, -1);
					drawVerticalLine(j + xSize - 20, k - 4, k + 10, -1);
					drawHorizontalLine(j - 2, j + xSize - 20, k - 3, -1);
					drawHorizontalLine(j - 2, j + xSize - 20, k + 10, -1);
					fontRenderer.drawString(cropString(getDisplayItemName(text), 22), j, k, 16777215);
				}
			}
		}
	}
	
	private String getDisplayItemName(String itemUnlName) {
		return "" + StatCollector.translateToLocal(itemUnlName == null ? "" : StatCollector.translateToLocal(itemUnlName) + ".name").trim();
	}

}
