package org.flycraft.dropchanger.content.gui.scroll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.flycraft.dropchanger.DCInfo;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Scroll from custom NPCs
 *
 */

public class GuiCustomScroll extends GuiScreen {

   private final ResourceLocation resource;
   private List list;
   public int id;
   public int guiLeft;
   public int guiTop;
   private int xSize;
   private int ySize;
   public int selected;
   private HashSet selectedList;
   private int hover;
   private int listHeight;
   private int scrollY;
   private int maxScrollY;
   private int scrollHeight;
   private boolean isScrolling;
   private boolean multipleSelection;
   private GuiCustomScrollActionListener listener;
   private boolean isSorted;
   
   private boolean isScroll;
   private int lastSlected;
   private FontRenderer fontRenderer = super.fontRenderer;
   private boolean isFocused;
   private boolean isItemScroll;

   public GuiCustomScroll(GuiScreen parent, int id, boolean isItem) {
      resource = new ResourceLocation(DCInfo.MOD_ID, "textures/gui/misc.png");
      guiLeft = 0;
      guiTop = 0;
      multipleSelection = false;
      isSorted = true;
      super.width = 176;
      super.height = 166;
      xSize = 176;
      ySize = 159;
      selected = -1;
      hover = -1;
      selectedList = new HashSet();
      listHeight = 0;
      scrollY = 0;
      scrollHeight = 0;
      isScrolling = false;
      isFocused = false;
      if(parent instanceof GuiCustomScrollActionListener) {
         listener = (GuiCustomScrollActionListener)parent;
      }

      list = new ArrayList();
      this.id = id;
      this.isItemScroll = isItem;
   }

   public void setSize(int x, int y) {
      ySize = y;
      xSize = x;
      listHeight = 14 * list.size();
      scrollHeight = (int)((double)(ySize - 8) / (double)listHeight * (double)(ySize - 8));
      maxScrollY = listHeight - (ySize - 8) - 1;
   }

   public void drawScreen(int i, int j, float f) {
      drawGradientRect(guiLeft, guiTop, xSize + guiLeft, ySize + guiTop, -1072689136, -804253680);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.renderEngine.bindTexture(resource);
      if(scrollHeight < ySize - 8) {
         drawScrollBar();
      }

      GL11.glPushMatrix();
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)guiLeft, (float)guiTop, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      hover = getMouseOver(i, j);
      drawItems();
      GL11.glPopMatrix();
      if(scrollHeight < ySize - 8) {
         i -= guiLeft;
         j -= guiTop;
         if(Mouse.isButtonDown(0)) {
            if(i >= xSize - 11 && i < xSize - 6 && j >= 4 && j < ySize) {
               isScrolling = true;
            }
         } else {
            isScrolling = false;
         }

         if(isScrolling) {
            scrollY = (j - 8) * listHeight / (ySize - 8) - scrollHeight;
            if(scrollY < 0) {
               scrollY = 0;
            }

            if(scrollY > maxScrollY) {
               scrollY = maxScrollY;
            }
         }

         if(isFocused) {
	         int k2 = Mouse.getDWheel();
	         if(k2 < 0) {
	            scrollY += 14;
	            if(scrollY > maxScrollY) {
	               scrollY = maxScrollY;
	            }
	         } else if(k2 > 0) {
	            scrollY -= 14;
	            if(scrollY < 0) {
	               scrollY = 0;
	            }
	         }
	         isFocused = false;
         }
      }

   }

   public boolean mouseInOption(int i, int j, int k) {
      byte l = 4;
      int i1 = 14 * k + 4 - scrollY;
      
      isFocused = true;
      
      return i >= l - 1 && i < l + xSize - 11 && j >= i1 - 1 && j < i1 + 8;
   }

   protected void drawItems() {
      for(int i = 0; i < list.size(); ++i) {
         byte j = 4;
         int k = 14 * i + 4 - scrollY;
         if(k >= 4 && k + 12 < ySize) {
            String text = (String)list.get(i);
            if((!multipleSelection || !selectedList.contains(text)) && (multipleSelection || selected != i)) {
               if(i == hover) {
            	   if(isItemScroll) {
            		   fontRenderer.drawString(cropString("" + StatCollector.translateToLocal(text == null ? "" : StatCollector.translateToLocal(text) + ".name").trim(), 22), j, k, '\uff00');
            	   } else {
            		   fontRenderer.drawString(cropString(text, 22), j, k, '\uff00');
            	   }
               } else {
            	   if(isItemScroll) {
            		   fontRenderer.drawString(cropString("" + StatCollector.translateToLocal(text == null ? "" : StatCollector.translateToLocal(text) + ".name").trim(), 22), j, k, 16777215);
            	   } else {
            		   fontRenderer.drawString(cropString(text, 22), j, k, 16777215);
            	   }
               }
            } else {
               drawVerticalLine(j - 2, k - 4, k + 10, -1);
               drawVerticalLine(j + xSize - 20, k - 4, k + 10, -1);
               drawHorizontalLine(j - 2, j + xSize - 20, k - 3, -1);
               drawHorizontalLine(j - 2, j + xSize - 20, k + 10, -1);
        	   if(isItemScroll) {
        		   fontRenderer.drawString(cropString("" + StatCollector.translateToLocal(text == null ? "" : StatCollector.translateToLocal(text) + ".name").trim(), 22), j, k, 16777215);
        	   } else {
        		   fontRenderer.drawString(cropString(text, 22), j, k, 16777215);
        	   }
            }
         }
      }

   }

   public String getSelected() {
      return selected != -1 && selected < list.size()?(String)list.get(selected):null;
   }

   private int getMouseOver(int i, int j) {
      i -= guiLeft;
      j -= guiTop;
      if(i >= 4 && i < xSize - 4 && j >= 4 && j < ySize) {
         for(int j1 = 0; j1 < list.size(); ++j1) {
            if(mouseInOption(i, j, j1)) {
               return j1;
            }
         }
      }

      return -1;
   }

   public void mouseClicked(int i, int j, int k) {
      if(k == 0 && hover >= 0) {
         if(multipleSelection) {
            if(selectedList.contains(list.get(hover))) {
               selectedList.remove(list.get(hover));
            } else {
               selectedList.add(list.get(hover));
            }
         } else {
            if(hover >= 0) {
               selected = hover;
            }

            hover = -1;
         }
         
         if(listener != null) {
            listener.customScrollClicked(i, j, k, this);
         }

      }
   }

   private void drawScrollBar() {
      int i = guiLeft + xSize - 10;
      int j = guiTop + (int)((double)scrollY / (double)listHeight * (double)(ySize - 8)) + 4;
      drawTexturedModalRect(i, j, xSize, 9, 5, 1);

      int k;
      for(k = j + 1; k < j + scrollHeight - 1; ++k) {
         drawTexturedModalRect(i, k, xSize, 10, 5, 1);
      }

      drawTexturedModalRect(i, k, xSize, 11, 5, 1);
   }

   public boolean hasSelected() {
      return selected >= 0;
   }

   public void setList(List list) {
      isSorted = true;
      Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
      this.list = list;
      setSize(xSize, ySize);
   }

   public void setUnsortedList(List list) {
      isSorted = false;
      this.list = list;
      setSize(xSize, ySize);
   }

   public void replace(String old, String name) {
      String select = getSelected();
      list.remove(old);
      list.add(name);
      if(isSorted) {
         Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
      }

      if(old.equals(select)) {
         select = name;
      }

      selected = list.indexOf(select);
      setSize(xSize, ySize);
   }

   public void setSelected(String name) {
	   selected = list.indexOf(name);
	   selectItemInPhysicalList();
	   lastSlected = selected;
   }
   
   private void selectItemInPhysicalList() {
	   if(scrollHeight < ySize - 8) {
		   int minSelectedPoint = scrollY;
		   int maxSelectedPoint = scrollY + 42;
		   int pointToSelect = selected * 14;
		   
		   if(pointToSelect >= maxSelectedPoint || pointToSelect <= minSelectedPoint) {
			   scrollY = pointToSelect;
		   }
           if(scrollY > maxScrollY) {
               scrollY = maxScrollY;
           }
           if(scrollY < 0) {
               scrollY = 0;
           }
	   }
   }
   
   public boolean isChanged() {
	   if(selected != lastSlected) {
		   return true;
	   }
	   return false;
   }

   public void clear() {
      list = new ArrayList();
      selected = -1;
      scrollY = 0;
      setSize(xSize, ySize);
   }

   public List getList() {
      return list;
   }

   public HashSet getSelectedList() {
      return selectedList;
   }

   public void setSelectedList(HashSet selectedList) {
	   this.selectedList = selectedList;
   }
   
	private String cropString(String toCrop, int stringLegth) {
		if(toCrop.length() > stringLegth) {
			return toCrop.substring(0, stringLegth-1) + "...";
		} else {
			return toCrop;
		}
	}

	public void setFontRenderer(FontRenderer customFontRenderer) {
		fontRenderer = customFontRenderer;
	}
}
