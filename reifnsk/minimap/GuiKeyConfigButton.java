package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

public class GuiKeyConfigButton extends GuiButton {
	private GuiKeyConfigScreen parrent;
	private KeyInput keyInput;
	private String labelText;
	private String buttonText;
	private int labelWidth;
	private int buttonWidth;

	public GuiKeyConfigButton(GuiKeyConfigScreen guiKeyConfigScreen1, int i2, int i3, int i4, int i5, int i6, KeyInput keyInput7) {
		super(i2, i3, i4, i5 + 12 + i6, 9, "");
		this.parrent = guiKeyConfigScreen1;
		this.keyInput = keyInput7;
		this.labelWidth = i5;
		this.buttonWidth = i6;
		this.labelText = this.keyInput.label();
		this.buttonText = this.keyInput.getKeyName();
	}

	public void drawButton(Minecraft minecraft1, int i2, int i3) {
		if(this.keyInput != null) {
			boolean z4 = i2 >= this.xPosition && i2 < this.xPosition + this.width && i3 >= this.yPosition && i3 < this.yPosition + this.height;
			this.drawString(minecraft1.fontRenderer, this.labelText, this.xPosition, this.yPosition + 1, z4 ? -1 : -4144960);
			String string5 = this.buttonText;
			if(this == this.parrent.getEditKeyConfig()) {
				string5 = ">" + string5 + "<";
			}

			z4 = i2 >= this.xPosition + this.width - this.buttonWidth && i2 < this.xPosition + this.width && i3 >= this.yPosition && i3 < this.yPosition + this.height;
			int i6 = z4 ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
			this.drawRect(this.xPosition + this.width - this.buttonWidth, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, i6);
			this.drawCenteredString(minecraft1.fontRenderer, string5, this.xPosition + this.width - this.buttonWidth / 2, this.yPosition + 1, -1);
		}
	}

	public boolean mousePressed(Minecraft minecraft1, int i2, int i3) {
		return i2 >= this.xPosition + this.width - this.buttonWidth && i2 < this.xPosition + this.width && i3 >= this.yPosition && i3 < this.yPosition + this.height;
	}

	void setBounds(int i1, int i2, int i3, int i4) {
		this.xPosition = i1;
		this.yPosition = i2;
		this.labelWidth = i3;
		this.buttonWidth = i4;
		this.width = i3 + i4 + 2;
	}

	KeyInput getKeyInput() {
		return this.keyInput;
	}
}
