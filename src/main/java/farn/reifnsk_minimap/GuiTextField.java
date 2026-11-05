package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

import org.lwjgl.input.Keyboard;

public class GuiTextField extends GuiButton {
	private static GuiTextField active;
	private int inputType;
	private GuiTextField prev;
	private GuiTextField next;
	private int norm = 0;

	public GuiTextField(String text) {
		super(0, 0, 0, 0, 0, text);
	}

	public GuiTextField() {
		super(0, 0, 0, 0, 0, "");
	}

	public void drawButton(Minecraft mc, int x, int z) {
		int color = active == this ? -2134851392 : -2141167520;
		this.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
		if(this.inputType == 0) {
			this.drawCenteredString(mc.fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + 1, -1);
		} else {
			int sWidth = mc.fontRenderer.getStringWidth(this.displayString);
			this.drawString(mc.fontRenderer, this.displayString, this.xPosition + this.width - sWidth - 1, this.yPosition + 1, -1);
		}

	}

	public boolean mousePressed(Minecraft mc, int x, int z) {
		if(x >= this.xPosition && x < this.xPosition + this.width && z >= this.yPosition && z < this.yPosition + this.height) {
			this.active();
		}

		return false;
	}

	public void active() {
		if(active != null) {
			active.norm();
		}

		active = this;
	}

	static void keyTypedActive(Minecraft mc, char character, int keyCode) {
		if(active != null) {
			active.keyTyped(mc, character, keyCode);
		}

	}

	private void keyTyped(Minecraft mc, char character, int keyCode) {
		if(this.inputType == 0 && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && keyCode == 47) {
			String clipBoarded = GuiScreen.getClipboardString();
			if(clipBoarded == null) {
				return;
			}

			for(int charIndex = 0; charIndex < clipBoarded.length(); ++charIndex) {
				char charAt = clipBoarded.charAt(charIndex);
				if(charAt != 13 && charAt != 10) {
					if(charAt == 58) {
						charAt = 59;
					}

					String str = this.displayString + charAt;
					if(mc.fontRenderer.getStringWidth(str) >= this.width - 2) {
						break;
					}

					this.displayString = str;
				}
			}
		}

		if(keyCode != 14 && keyCode != 211) {
			if(keyCode == 15) {
				if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					next();
				} else {
					prev();
				}
			}

			if(keyCode == 28) {
				next();
			}

			if(this.checkInput(character)) {
				String string4 = this.displayString + character;
				if(mc.fontRenderer.getStringWidth(string4) < this.width - 2) {
					try {
						if(this.inputType == 1) {
							int i5 = Integer.parseInt(string4);
							string4 = i5 < -32000000 ? "-32000000" : (i5 >= 32000000 ? "31999999" : Integer.toString(i5));
						}

						if(this.inputType == 2) {
							int i5 = Integer.parseInt(string4);
							string4 = i5 < 0 ? "0" : (i5 > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(i5));
						}
					} catch (NumberFormatException ignored) {
					}

					this.displayString = string4;
				}
			}

		} else {
			if(!this.displayString.isEmpty()) {
				this.displayString = this.displayString.substring(0, this.displayString.length() - 1);
			}

		}
	}

	boolean checkInput(char c1) {
		switch(this.inputType) {
		case 0:
            return " !\"#$%&'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»".indexOf(c1) != -1;
        case 1:
            return (this.displayString.isEmpty() ? "-0123456789" : "0123456789").indexOf(c1) != -1;
        case 2:
            return "0123456789".indexOf(c1) != -1;
        default:
			return false;
		}
	}

	void norm() {
		String input = this.displayString;

		try {
			if(this.inputType == 1) {
				int parsed = Integer.parseInt(input);
				input = parsed < -32000000 ? "-32000000" : (parsed >= 32000000 ? "31999999" : Integer.toString(parsed));
			}

			if(this.inputType == 2) {
				int parsed = Integer.parseInt(input);
				input = parsed < 0 ? "0" : (parsed > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(parsed));
			}
		} catch (NumberFormatException numberFormatException3) {
			input = Integer.toString(this.norm);
		}

		this.displayString = input;
	}

	void setInputType(int i1) {
		this.inputType = i1;
	}

	void setPosition(int i1, int i2) {
		this.xPosition = i1;
		this.yPosition = i2;
	}

	void setSize(int i1, int i2) {
		this.width = i1;
		this.height = i2;
	}

	void setBounds(int i1, int i2, int i3, int i4) {
		this.xPosition = i1;
		this.yPosition = i2;
		this.width = i3;
		this.height = i4;
	}

	void setNext(GuiTextField guiTextField1) {
		this.next = guiTextField1;
	}

	void setPrev(GuiTextField guiTextField1) {
		this.prev = guiTextField1;
	}

	static void next() {
		if(active != null) {
			active.norm();
			active = active.next;
		}

	}

	static void prev() {
		if(active != null) {
			active.norm();
			active = active.prev;
		}

	}

	static GuiTextField getActive() {
		return active;
	}

	void setNorm(int i1) {
		this.norm = i1;
	}
}
