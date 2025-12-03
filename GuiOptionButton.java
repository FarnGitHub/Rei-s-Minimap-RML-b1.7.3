package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

public class GuiOptionButton extends GuiButton {
	private static int NAME_WIDTH;
	private static int VALUE_WIDTH;
	private static int WIDTH;
	private EnumOption option;
	private EnumOptionValue value;

	public GuiOptionButton(FontRenderer fontRenderer1, EnumOption enumOption2) {
		super(0, 0, 0, 0, 10, "");
		this.option = enumOption2;
		this.value = this.option.getValue(0);

		for(int i3 = 0; i3 < enumOption2.getValueNum(); ++i3) {
			String string4 = enumOption2.getValue(i3).text();
			int i5 = fontRenderer1.getStringWidth(string4) + 4;
			VALUE_WIDTH = Math.max(VALUE_WIDTH, i5);
		}

		NAME_WIDTH = Math.max(NAME_WIDTH, fontRenderer1.getStringWidth(enumOption2.getText() + ": "));
		WIDTH = VALUE_WIDTH + 8 + NAME_WIDTH;
	}

	public void drawButton(Minecraft minecraft1, int i2, int i3) {
		if(this.enabled2) {
			this.value = ReiMinimap.instance.getOption(this.option);
			FontRenderer fontRenderer4 = minecraft1.fontRenderer;
			boolean z5 = i2 >= this.xPosition && i3 >= this.yPosition && i2 < this.xPosition + getWidth() && i3 < this.yPosition + getHeight();
			int i6 = z5 ? -1 : -4144960;
			int i7 = z5 ? 1728053247 : this.value.color;
			this.drawString(fontRenderer4, this.option.getText(), this.xPosition, this.yPosition + 1, i6);
			int i8 = this.xPosition + NAME_WIDTH + 8;
			int i9 = i8 + VALUE_WIDTH;
			this.drawRect(i8, this.yPosition, i9, this.yPosition + getHeight() - 1, i7);
			this.drawCenteredString(fontRenderer4, this.value.text(), i8 + VALUE_WIDTH / 2, this.yPosition + 1, -1);
		}
	}

	public boolean mousePressed(Minecraft minecraft1, int i2, int i3) {
		if(this.enabled && i2 >= this.xPosition && i3 >= this.yPosition && i2 < this.xPosition + getWidth() && i3 < this.yPosition + getHeight()) {
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

	public void setValue(EnumOptionValue enumOptionValue1) {
		if(this.option.getValue(enumOptionValue1) != -1) {
			this.value = enumOptionValue1;
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
