package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

public class GuiKeyConfigButton extends GuiButton {
	private final GuiKeyConfigScreen parent;
	private final KeyInput keyInput;
	private final String labelText;
	private final String buttonText;
	private int buttonWidth;

	public GuiKeyConfigButton(GuiKeyConfigScreen screen, int id, int x, int z, int labelWidth, int buttonWidth, KeyInput input) {
		super(id, x, z, labelWidth + 12 + buttonWidth, 9, "");
		this.parent = screen;
		this.keyInput = input;
		this.buttonWidth = buttonWidth;
		this.labelText = this.keyInput.label();
		this.buttonText = this.keyInput.getKeyName();
	}

	public void drawButton(Minecraft mc, int x, int z) {
		if(this.keyInput != null) {
			boolean mouseOver = x >= this.xPosition && x < this.xPosition + this.width && z >= this.yPosition && z < this.yPosition + this.height;
			this.drawString(mc.fontRenderer, this.labelText, this.xPosition, this.yPosition + 1, mouseOver ? -1 : -4144960);
			String text = this.buttonText;
			if(this == this.parent.getEditKeyConfig()) {
				text = ">" + text + "<";
			}

			mouseOver = x >= this.xPosition + this.width - this.buttonWidth && x < this.xPosition + this.width && z >= this.yPosition && z < this.yPosition + this.height;
			int color = mouseOver ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
			this.drawRect(this.xPosition + this.width - this.buttonWidth, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
			this.drawCenteredString(mc.fontRenderer, text, this.xPosition + this.width - this.buttonWidth / 2, this.yPosition + 1, -1);
		}
	}

	public boolean mousePressed(Minecraft mc, int x, int z) {
		return x >= this.xPosition + this.width - this.buttonWidth && x < this.xPosition + this.width && z >= this.yPosition && z < this.yPosition + this.height;
	}

	@SuppressWarnings("unused")
	void setBounds(int i1, int i2, int i3, int i4) {
		this.xPosition = i1;
		this.yPosition = i2;
		this.buttonWidth = i4;
		this.width = i3 + i4 + 2;
	}

	KeyInput getKeyInput() {
		return this.keyInput;
	}
}
