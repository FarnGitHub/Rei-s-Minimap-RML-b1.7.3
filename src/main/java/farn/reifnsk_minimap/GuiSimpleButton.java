package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

public class GuiSimpleButton extends GuiButton {
	public GuiSimpleButton(int id, int x, int y, int width, int height, String text) {
		super(id, x, y, width, height, text);
	}

	public void drawButton(Minecraft mc, int x, int y) {
		if(this.enabled2) {
			boolean mouseOver = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
			int color = mouseOver && this.enabled ? -932813210 : -1610612736;
			this.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, color);
			this.drawCenteredString(mc.fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.enabled ? -1 : -8355712);
		}
	}
}
