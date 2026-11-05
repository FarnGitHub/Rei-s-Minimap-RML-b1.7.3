package farn.reifnsk_minimap;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiOptionScreen extends GuiScreen implements GuiScreenInterface {
	private static final String[] TITLE_STRING = new String[]{"Rei's Minimap " + ReiMinimap.version, "Minimap Options", "SurfaceMap Options", "Entities Radar Options", "Marker Options", "About Rei's Minimap"};
	private int page;
	private GuiSimpleButton exitMenu;
	private GuiSimpleButton waypoint;
	private GuiSimpleButton keyconfig;
	private int top;
	private int left;
	private int right;
	private int bottom;
	private int centerX;

	public GuiOptionScreen() {
	}

	GuiOptionScreen(int i1) {
		this.page = i1;
	}

	public void initGui() {
		this.centerX = this.width / 2;
		this.controlList.clear();
		List<GuiOptionButton> minimapButtons = new ArrayList<>();

        for (EnumOption option : EnumOption.values()) {
            if (option.getPage() == this.page && ((this.mc.theWorld != null && !this.mc.theWorld.multiplayerWorld) || option != EnumOption.ENTITIES_RADAR_OPTION || ReiMinimap.instance.getAllowEntitiesRadar()) && option != EnumOption.DIRECTION_TYPE) {
                GuiOptionButton guiOptionButton5 = new GuiOptionButton(this.mc.fontRenderer, option);
                guiOptionButton5.setValue(ReiMinimap.instance.getOption(option));
                this.controlList.add(guiOptionButton5);
                minimapButtons.add(guiOptionButton5);
            }
        }

		this.left = this.width - GuiOptionButton.getWidth() >> 1;
		this.top = this.height - minimapButtons.size() * 10 >> 1;
		this.right = this.width + GuiOptionButton.getWidth() >> 1;
		this.bottom = this.height + minimapButtons.size() * 10 >> 1;

		for(int index = 0; index < minimapButtons.size(); ++index) {
			GuiOptionButton theButton = minimapButtons.get(index);
			theButton.xPosition = this.left;
			theButton.yPosition = this.top + index * 10;
		}

		if(this.page == 0) {
			this.exitMenu = new GuiSimpleButton(0, this.centerX - 95, this.bottom + 7, 60, 14, "Exit Menu");
			this.controlList.add(this.exitMenu);
			this.waypoint = new GuiSimpleButton(1, this.centerX - 30, this.bottom + 7, 60, 14, "Waypoints");
			this.controlList.add(this.waypoint);
			this.keyconfig = new GuiSimpleButton(2, this.centerX + 35, this.bottom + 7, 60, 14, "Keyconfig");
			this.controlList.add(this.keyconfig);
		} else {
			this.exitMenu = new GuiSimpleButton(0, this.centerX - 30, this.bottom + 7, 60, 14, "Back");
			this.controlList.add(this.exitMenu);
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		if(this.mc.theWorld == null)
			this.drawDefaultBackground();
		String string4 = TITLE_STRING[this.page];
		int i5 = this.fontRenderer.getStringWidth(string4);
		int i6 = this.width - i5 >> 1;
		int i7 = this.width + i5 >> 1;
		this.drawRect(i6 - 2, this.top - 22, i7 + 2, this.top - 8, -1610612736);
		this.drawCenteredString(this.fontRenderer, string4, this.centerX, this.top - 19, -1);
		this.drawRect(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.drawScreen(i1, i2, f3);
	}

	protected void actionPerformed(GuiButton guiButton1) {
		if(guiButton1 instanceof GuiOptionButton) {
			GuiOptionButton guiOptionButton2 = (GuiOptionButton)guiButton1;
			ReiMinimap.instance.setOption(guiOptionButton2.getOption(), guiOptionButton2.getValue());
			ReiMinimap.instance.saveOptions();
		}

		if(guiButton1 instanceof GuiSimpleButton) {
			if(guiButton1 == this.exitMenu) {
				this.mc.displayGuiScreen(this.page == 0 ? null : new GuiOptionScreen(0));
			}

			if(guiButton1 == this.waypoint) {
				this.mc.displayGuiScreen(new GuiWaypointScreen(this));
			}

			if(guiButton1 == this.keyconfig) {
				this.mc.displayGuiScreen(new GuiKeyConfigScreen());
			}
		}

	}
}
