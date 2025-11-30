package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

public class GuiSimpleButton extends GuiButton {
	public GuiSimpleButton(int i1, int i2, int i3, int i4, int i5, String string6) {
		super(i1, i2, i3, i4, i5, string6);
	}

	public void drawButton(Minecraft minecraft1, int i2, int i3) {
		if(this.enabled2) {
			FontRenderer fontRenderer4 = minecraft1.fontRenderer;
			boolean z5 = i2 >= this.xPosition && i3 >= this.yPosition && i2 < this.xPosition + this.width && i3 < this.yPosition + this.height;
			int i6 = z5 && this.enabled ? -932813210 : -1610612736;
			this.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, i6);
			this.drawCenteredString(fontRenderer4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.enabled ? -1 : -8355712);
		}
	}
}
