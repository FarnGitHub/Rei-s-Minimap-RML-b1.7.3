package farn.reifnsk_minimap;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiKeyConfigScreen extends GuiScreen implements GuiScreenInterface {
	private int top;
	private int bottom;
	private int left;
	private int right;
	private GuiSimpleButton okButton;
	private GuiSimpleButton cancelButton;
	private GuiSimpleButton defaultButton;
	private GuiKeyConfigButton edit;
	private final int[] currentKeyCode;

	GuiKeyConfigScreen() {
		KeyInput[] inputs = KeyInput.values();
		this.currentKeyCode = new int[inputs.length];

		for(int index = 0; index < this.currentKeyCode.length; ++index) {
			this.currentKeyCode[index] = inputs[index].getKey();
		}

	}

	public void initGui() {
		int lWidth = this.calcLabelWidth();
		int bWidth = this.calcButtonWidth();
		this.left = (this.width - lWidth - bWidth - 12) / 2;
		this.right = (this.width + lWidth + bWidth + 12) / 2;
		this.top = (this.height - KeyInput.values().length * 10) / 2;
		this.bottom = (this.height + KeyInput.values().length * 10) / 2;
		int yPos = this.top;

        for (KeyInput input : KeyInput.values()) {
            GuiKeyConfigButton button = new GuiKeyConfigButton(this, 0, this.left, yPos, lWidth, bWidth, input);
            this.controlList.add(button);
            yPos += 10;
        }

		int centerX = this.width / 2;
		this.okButton = new GuiSimpleButton(0, centerX - 74, this.bottom + 7, 46, 14, "OK");
		this.controlList.add(this.okButton);
		this.cancelButton = new GuiSimpleButton(0, centerX - 23, this.bottom + 7, 46, 14, "Cancel");
		this.controlList.add(this.cancelButton);
		this.defaultButton = new GuiSimpleButton(0, centerX + 28, this.bottom + 7, 46, 14, "Default");
		this.controlList.add(this.defaultButton);
	}

	private int calcLabelWidth() {
		int totalWidth = -1;
        for (KeyInput input : KeyInput.values()) {
            totalWidth = Math.max(
					totalWidth,
					this.mc.fontRenderer.getStringWidth(input.name())
			);
        }

		return totalWidth;
	}

	private int calcButtonWidth() {
		int totalWidth = 30;

        for (KeyInput input :KeyInput.values()) {
            totalWidth = Math.max(totalWidth, this.mc.fontRenderer.getStringWidth(">" + input.getKeyName() + "<"));
        }

		return totalWidth + 2;
	}

	public void drawScreen(int x, int z, float tick) {
		if(this.mc.theWorld == null)
			this.drawDefaultBackground();
		String keyConfig = "Key Config";
		int tWidth = this.fontRenderer.getStringWidth(keyConfig);
		int tX = this.width - tWidth >> 1;
		int tZ = this.width + tWidth >> 1;
		this.drawRect(tX - 2, this.top - 22, tZ + 2, this.top - 8, -1610612736);
		this.drawCenteredString(this.fontRenderer, keyConfig, this.width / 2, this.top - 19, -1);
		this.drawRect(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.drawScreen(x, z, tick);
	}

	GuiKeyConfigButton getEditKeyConfig() {
		return this.edit;
	}

	protected void actionPerformed(GuiButton button) {
		if(button instanceof GuiKeyConfigButton) {
			this.edit = (GuiKeyConfigButton)button;
		}

		if(button == this.okButton) {
			if(KeyInput.saveKeyConfig()) {
				this.mc.ingameGUI.addChatMessage("§E[Rei's Minimap] Keyconfig Saved.");
			} else {
				this.mc.ingameGUI.addChatMessage("§E[Rei's Minimap] Error Keyconfig Saving.");
			}

			this.mc.displayGuiScreen(new GuiOptionScreen());
		}

		if(button == this.defaultButton) {
            for (KeyInput input : KeyInput.values()) {
                input.setDefault();
            }

			this.controlList.clear();
			this.initGui();
		}

		if(button == this.cancelButton) {
			KeyInput[] inputs = KeyInput.values();

			for(int index = 0; index < this.currentKeyCode.length; ++index) {
				inputs[index].setKey(this.currentKeyCode[index]);
			}

			this.mc.displayGuiScreen(new GuiOptionScreen());
		}

	}

	protected void keyTyped(char character, int keyCode) {
		if(this.edit != null) {
			this.edit.getKeyInput().setKey(keyCode);
			this.edit = null;
			this.controlList.clear();
			this.initGui();
		} else if(keyCode == 1) {
			KeyInput[] inputs = KeyInput.values();

			for(int index = 0; index < this.currentKeyCode.length; ++index) {
				inputs[index].setKey(this.currentKeyCode[index]);
			}

			this.mc.displayGuiScreen(null);
		}

	}
}
