package org.flycraft.dropchanger.content.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;

public class DCTextField extends GuiTextField {

	public DCTextField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
		super(par1FontRenderer, par2, par3, par4, par5);
		setMaxStringLength(4);
	}
	
	@Override
    public boolean textboxKeyTyped(char par1, int par2)
    {
        if (isFocused())
        {
            switch (par1)
            {
                case 1:
                    setCursorPositionEnd();
                    setSelectionPos(0);
                    return true;
                case 3:
                    GuiScreen.setClipboardString(getSelectedtext());
                    return true;
                case 22:
                    writeText(GuiScreen.getClipboardString());
                    return true;
                case 24:
                    GuiScreen.setClipboardString(getSelectedtext());
                    writeText("");
                    return true;
                default:
                    switch (par2)
                    {
                        case 14:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                deleteWords(-1);
                            }
                            else
                            {
                                deleteFromCursor(-1);
                            }

                            return true;
                        case 199:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                setSelectionPos(0);
                            }
                            else
                            {
                                setCursorPositionZero();
                            }

                            return true;
                        case 203:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
                                }
                                else
                                {
                                    setSelectionPos(getSelectionEnd() - 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                setCursorPosition(getNthWordFromCursor(-1));
                            }
                            else
                            {
                                moveCursorBy(-1);
                            }

                            return true;
                        case 205:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
                                }
                                else
                                {
                                    setSelectionPos(getSelectionEnd() + 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                setCursorPosition(getNthWordFromCursor(1));
                            }
                            else
                            {
                                moveCursorBy(1);
                            }

                            return true;
                        case 207:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                setSelectionPos(getText().length());
                            }
                            else
                            {
                                setCursorPositionEnd();
                            }

                            return true;
                        case 211:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                deleteWords(1);
                            }
                            else
                            {
                                deleteFromCursor(1);
                            }
                            
                            return true;
                        default:
                            if (ChatAllowedCharacters.isAllowedCharacter(par1) && Character.isDigit(par1))
                            {
                                writeText(Character.toString(par1));
                                
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                    }
            }
        }
        else
        {
            return false;
        }
    }

}
