package farn.reifnsk_minimap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiWaypointScreen extends GuiScreen implements GuiScreenInterface {
	static final int MIN_STRING_WIDTH = 64;
	static final int MAX_STRING_WIDTH = 160;
	static final int LIST_SIZE = 9;
	private final ReiMinimap rmm = ReiMinimap.instance;
	private List<Waypoint> wayPts = this.rmm.getWaypoints();
	private final GuiWaypoint[] guiWaypoints = new GuiWaypoint[9];
	private final GuiScrollbar scrollbar = new GuiScrollbar(0, 0, 0, 12, 90);
	private GuiSimpleButton backButton;
	private GuiSimpleButton addButton;
	private GuiSimpleButton removeFlagButton;
	private GuiSimpleButton removeApplyButton;
	private GuiSimpleButton removeCancelButton;
	private GuiSimpleButton prevDimension;
	private GuiSimpleButton nextDimension;
	private final ConcurrentHashMap<Waypoint, Waypoint> deleteObject = new ConcurrentHashMap<>();
	private int scroll;
	private boolean removeMode;
	private int maxStringWidth;
	private final GuiScreen parent;

	public GuiWaypointScreen(GuiScreen guiScreen1) {
		this.parent = guiScreen1;

		for(int index = 0; index < LIST_SIZE; ++index) {
			this.guiWaypoints[index] = new GuiWaypoint(index, this);
		}

	}

	public void initGui() {
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
        this.controlList.addAll(Arrays.asList(this.guiWaypoints));
		this.controlList.add(this.scrollbar);
		this.updateWaypoints();
		int xPos = this.width / 2;
		int yPos = this.height + 90 >> 1;
		this.backButton = new GuiSimpleButton(0, xPos - 65, yPos + 7, 40, 14, this.parent == null ? "Close" : "Back");
		this.controlList.add(this.backButton);
		this.addButton = new GuiSimpleButton(0, xPos - 20, yPos + 7, 40, 14, "Add");
		this.controlList.add(this.addButton);
		this.removeFlagButton = new GuiSimpleButton(0, xPos + 25, yPos + 7, 40, 14, "Remove");
		this.controlList.add(this.removeFlagButton);
		this.removeApplyButton = new GuiSimpleButton(0, xPos - 65, yPos + 7, 60, 14, "Remove");
		this.controlList.add(this.removeApplyButton);
		this.removeCancelButton = new GuiSimpleButton(0, xPos + 5, yPos + 7, 60, 14, "Cancel");
		this.controlList.add(this.removeCancelButton);
		this.prevDimension = new GuiSimpleButton(0, 0, 0, 14, 14, "<");
		this.controlList.add(this.prevDimension);
		this.nextDimension = new GuiSimpleButton(0, 0, 0, 14, 14, ">");
		this.controlList.add(this.nextDimension);
		this.setRemoveMode(this.removeMode);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void drawScreen(int x, int z, float tick) {
		if(this.mc.theWorld == null)
			this.drawDefaultBackground();
		this.backButton.enabled = this.backButton.enabled2 = !this.removeMode;
		this.addButton.enabled = this.addButton.enabled2 = !this.removeMode;
		this.removeFlagButton.enabled = this.removeFlagButton.enabled2 = !this.removeMode;
		this.removeApplyButton.enabled = this.removeApplyButton.enabled2 = this.removeMode;
		this.removeCancelButton.enabled = this.removeCancelButton.enabled2 = this.removeMode;
		this.addButton.enabled = this.rmm.getCurrentDimension() == this.rmm.getWaypointDimension();
		int sWidth = Math.min(MAX_STRING_WIDTH, this.maxStringWidth) + 16;
		int yMin = this.height - 90 >> 1;
		int yMax = this.height + 90 >> 1;
		int xMin = this.width - sWidth - 45 - 10 >> 1;
		int xMax = this.width + sWidth + 45 + 10 >> 1;
		this.drawRect(xMin - 2, yMin - 2, xMax + 2, yMax + 2, -1610612736);
		String wPoint = String.format("Waypoints [%s]", ReiMinimap.instance.getDimensionName(ReiMinimap.instance.getWaypointDimension()));
		int wWidth = this.fontRenderer.getStringWidth(wPoint);
		int wXmin = this.width - wWidth >> 1;
		int wXmax = this.width + wWidth >> 1;
		this.prevDimension.xPosition = wXmin - 18;
		this.prevDimension.yPosition = yMin - 22;
		this.nextDimension.xPosition = wXmax + 4;
		this.nextDimension.yPosition = yMin - 22;
		super.drawScreen(x, z, tick);
		this.drawRect(wXmin - 2, yMin - 22, wXmax + 2, yMin - 8, -1610612736);
		this.drawCenteredString(this.fontRenderer, wPoint, this.width / 2, yMin - 19, -1);
	}

	public void updateScreen() {
		int scrollVal = (int)this.scrollbar.getValue();
		if(this.scroll != scrollVal) {
			this.scroll = scrollVal;
			this.setWaypoints();
		}

	}

	protected void keyTyped(char character, int keyCode) {
		super.keyTyped(character, keyCode);
		switch(keyCode) {
		case 199:
			this.scrollbar.setValue(this.scrollbar.getMinimum());
			break;
		case 200:
			this.scrollbar.unitDecrement();
			break;
		case 201:
			this.scrollbar.blockDecrement();
		case 202:
		case 203:
		case 204:
		case 205:
		case 206:
		default:
			break;
		case 207:
			this.scrollbar.setValue(this.scrollbar.getMaximum());
			break;
		case 208:
			this.scrollbar.unitIncrement();
			break;
		case 209:
			this.scrollbar.blockIncrement();
		}

	}

	public void handleMouseInput() {
		super.handleMouseInput();
		int dWheel = Mouse.getDWheel();
		if(dWheel != 0) {
			dWheel = dWheel < 0 ? 3 : -3;
			this.scrollbar.setValue(this.scrollbar.getValue() + (float)dWheel);
		}

	}

	protected void actionPerformed(GuiButton button) {
		if(button == this.backButton) {
			this.mc.displayGuiScreen(this.parent);
		}

		if(button == this.removeFlagButton) {
			this.setRemoveMode(true);
		}

		if(button == this.removeCancelButton) {
			this.setRemoveMode(false);
		}

		if(button == this.removeApplyButton) {
			boolean wRemove = false;

            for (Waypoint waypoint : this.deleteObject.keySet()) {
                wRemove |= this.wayPts.remove(waypoint);
            }

			if(wRemove) {
				this.rmm.saveWaypoints();
				this.updateWaypoints();
			}

			this.setRemoveMode(false);
		}

		if(button == this.addButton && this.rmm.getCurrentDimension() == this.rmm.getWaypointDimension()) {
			this.mc.displayGuiScreen(new GuiWaypointEditorScreen(this, null));
		}

		if(button == this.prevDimension) {
			this.setRemoveMode(false);
			this.rmm.prevDimension();
			this.wayPts = this.rmm.getWaypoints();
			this.updateWaypoints();
		}

		if(button == this.nextDimension) {
			this.setRemoveMode(false);
			this.rmm.nextDimension();
			this.wayPts = this.rmm.getWaypoints();
			this.updateWaypoints();
		}

	}

	void setRemoveMode(boolean removeMode) {
		this.removeMode = removeMode;
		this.deleteObject.clear();
	}

	boolean getRemoveMode() {
		return this.removeMode;
	}

	boolean isRemove(Waypoint waypoint) {
		return this.deleteObject.containsKey(waypoint);
	}

	void addWaypoint(Waypoint waypoint) {
		if(!this.wayPts.contains(waypoint)) {
			this.wayPts.add(waypoint);
			this.rmm.saveWaypoints();
			this.updateWaypoints();
			this.scrollbar.setValue(this.scrollbar.getMaximum());
		}

	}

	void removeWaypoint(Waypoint waypoint) {
		if(this.removeMode) {
			if(this.deleteObject.remove(waypoint) == null) {
				this.deleteObject.put(waypoint, waypoint);
			}
		} else if(this.wayPts.remove(waypoint)) {
			this.rmm.saveWaypoints();
			this.updateWaypoints();
		}

	}

	void updateWaypoint(Waypoint waypoint) {
		if(this.wayPts.contains(waypoint)) {
			this.rmm.saveWaypoints();
			this.updateWaypoints();
		}

	}

	private void updateWaypoints() {
		this.maxStringWidth = MIN_STRING_WIDTH;
		for(int index = 0; index < this.wayPts.size(); ++index) {
			Waypoint waypoint3 = this.wayPts.get(index);
			this.maxStringWidth = Math.max(this.maxStringWidth, this.fontRenderer.getStringWidth(index + 1 + ") " + waypoint3.name));
		}

		this.scrollbar.setMinimum(0.0F);
		this.scrollbar.setMaximum((float)this.wayPts.size());
		this.scrollbar.setVisibleAmount((float)Math.min(9, this.wayPts.size()));
		this.scroll = (int)this.scrollbar.getValue();
		this.updateGui();
		this.setWaypoints();
	}

	private void updateGui() {
		int sWidth = Math.min(MAX_STRING_WIDTH, this.maxStringWidth) + 16;
		int yPos = this.height - 90 - 4 >> 1;
		int xMin = this.width - sWidth - 45 - 12 >> 1;
		int xMax = this.width + sWidth + 45 + 12 >> 1;

		for(int index = 0; index < LIST_SIZE; ++index) {
			this.guiWaypoints[index].bounds(xMin + 2, yPos + 2 + 10 * index, sWidth + 45, 9);
		}

		this.scrollbar.xPosition = xMax - 12;
		this.scrollbar.yPosition = yPos + 2;
	}

	private void setWaypoints() {
		for(int index = 0; index < LIST_SIZE; ++index) {
			int scrollIndex = index + this.scroll;
			this.guiWaypoints[index].setWaypoint(scrollIndex + 1, scrollIndex < this.wayPts.size() ? this.wayPts.get(scrollIndex) : null);
		}

	}

	Minecraft getMinecraft() {
		return this.mc;
	}
}
