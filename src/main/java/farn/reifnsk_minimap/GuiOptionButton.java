package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

public class GuiOptionButton extends GuiButton {
	private static int NAME_WIDTH;
	private static int VALUE_WIDTH;
	private static int WIDTH;
	private final EnumOption option;
	private EnumOptionValue value;

	public GuiOptionButton(FontRenderer fontRender, EnumOption option) {
		super(0, 0, 0, 0, 10, "");
		this.option = option;
		this.value = this.option.getValue(0);

		for(int index = 0; index < option.getValueNum(); ++index) {
			String text = option.getValue(index).text();
			int tWidth = fontRender.getStringWidth(text) + 4;
			VALUE_WIDTH = Math.max(VALUE_WIDTH, tWidth);
		}

		NAME_WIDTH = Math.max(NAME_WIDTH, fontRender.getStringWidth(option.getText() + ": "));
		WIDTH = VALUE_WIDTH + 8 + NAME_WIDTH;
	}

	public void drawButton(Minecraft mc, int x, int y) {
		if(this.enabled2) {
			this.value = ReiMinimap.instance.getOption(this.option);
			FontRenderer fontRenderer4 = mc.fontRenderer;
			boolean mouseOver = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + getWidth() && y < this.yPosition + getHeight();
			int textColor = mouseOver ? -1 : -4144960;
			int buttonColor = mouseOver ? 1728053247 : this.value.color;
			this.drawString(fontRenderer4, this.option.getText(), this.xPosition, this.yPosition + 1, textColor);
			int xStart = this.xPosition + NAME_WIDTH + 8;
			int xEnd = xStart + VALUE_WIDTH;
			this.drawRect(xStart, this.yPosition, xEnd, this.yPosition + getHeight() - 1, buttonColor);
			this.drawCenteredString(fontRenderer4, this.value.text(), xStart + VALUE_WIDTH / 2, this.yPosition + 1, -1);
		}
	}

	public boolean mousePressed(Minecraft mc, int x, int z) {
		if(this.enabled && x >= this.xPosition && z >= this.yPosition && x < this.xPosition + getWidth() && z < this.yPosition + getHeight()) {
			this.nextValue();
			return true;
		} else {
			return false;
		}
	}

	public EnumOption getOption() {
		return this.option;
	}

	public EnumOptionValue getValue() {
		return this.value;
	}

	public void setValue(EnumOptionValue optionValue) {
		if(this.option.getValue(optionValue) != -1) {
			this.value = optionValue;
		}

	}

	public void nextValue() {
		this.value = this.option.getValue((this.option.getValue(this.value) + 1) % this.option.getValueNum());
		if(!ReiMinimap.instance.getAllowCavemap() && this.option == EnumOption.RENDER_TYPE && this.value == EnumOptionValue.CAVE) {
			this.nextValue();
		}

	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return 10;
	}
}
