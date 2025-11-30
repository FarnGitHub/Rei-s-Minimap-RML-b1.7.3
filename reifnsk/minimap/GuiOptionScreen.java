package reifnsk.minimap;

import java.util.ArrayList;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiOptionScreen extends GuiScreen implements GuiScreenInterface {
	private static final int LIGHTING_VERSION = 16844800;
	private static final int SUNRISE_DIRECTION = 16844931;
	public static final int minimapMenu = 0;
	public static final int optionMinimap = 1;
	public static final int optionSurfaceMap = 2;
	public static final int optionEntitiesRadar = 3;
	public static final int optionMarker = 4;
	public static final int aboutMinimap = 5;
	private static final String[] TITLE_STRING = new String[]{"Rei\'s Minimap " + ReiMinimap.version, "Minimap Options", "SurfaceMap Options", "Entities Radar Options", "Marker Options", "About Rei\'s Minimap"};
	private int page;
	private ArrayList buttonList = new ArrayList();
	private GuiSimpleButton exitMenu;
	private GuiSimpleButton waypoint;
	private GuiSimpleButton keyconfig;
	private int top;
	private int left;
	private int right;
	private int bottom;
	private int centerX;
	private int centerY;

	public GuiOptionScreen() {
	}

	GuiOptionScreen(int i1) {
		this.page = i1;
	}

	public void initGui() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;
		this.controlList.clear();
		this.buttonList.clear();
		EnumOption[] enumOption4;
		int i3 = (enumOption4 = EnumOption.values()).length;

		for(int i2 = 0; i2 < i3; ++i2) {
			EnumOption enumOption1 = enumOption4[i2];
			if(enumOption1.getPage() == this.page && (!this.mc.theWorld.multiplayerWorld || enumOption1 != EnumOption.ENTITIES_RADAR_OPTION || ReiMinimap.instance.getAllowEntitiesRadar()) && enumOption1 != EnumOption.DIRECTION_TYPE) {
				GuiOptionButton guiOptionButton5 = new GuiOptionButton(this.mc.fontRenderer, enumOption1);
				guiOptionButton5.setValue(ReiMinimap.instance.getOption(enumOption1));
				this.controlList.add(guiOptionButton5);
				this.buttonList.add(guiOptionButton5);
			}
		}

		this.left = this.width - GuiOptionButton.getWidth() >> 1;
		this.top = this.height - this.buttonList.size() * 10 >> 1;
		this.right = this.width + GuiOptionButton.getWidth() >> 1;
		this.bottom = this.height + this.buttonList.size() * 10 >> 1;

		for(int i6 = 0; i6 < this.buttonList.size(); ++i6) {
			GuiOptionButton guiOptionButton7 = (GuiOptionButton)this.buttonList.get(i6);
			guiOptionButton7.xPosition = this.left;
			guiOptionButton7.yPosition = this.top + i6 * 10;
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
