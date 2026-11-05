package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

public class GuiSimpleButton extends GuiButton {
	public GuiSimpleButton(int i1, int i2, int i3, int i4, int i5, String string6) {
		super(i1, i2, i3, i4, i5, string6);
	}

	public void drawButton(Minecraft mc, int x, int z) {
		if(this.enabled2) {
			boolean mouseOver = x >= this.xPosition && z >= this.yPosition && x < this.xPosition + this.width && z < this.yPosition + this.height;
			int color = mouseOver && this.enabled ? -932813210 : -1610612736;
			this.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
			this.drawCenteredString(mc.fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.enabled ? -1 : -8355712);
		}
	}
}
