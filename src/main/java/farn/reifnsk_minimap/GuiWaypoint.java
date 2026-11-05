package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

class GuiWaypoint extends GuiButton {
	private static final int[] COLOR1 = new int[]{-1, -65536};
	private static final int[] COLOR2 = new int[]{-4144960, -4194304};
	private static final int COLOR_SIZE = 9;
	private static final int BUTTON_SIZE = 30;
	private static final int ADD_SPACE = 2;
	static final int SIZE = 45;
	private GuiWaypointScreen gws;
	private Waypoint waypoint;
	private int number;
	private String name;
	private int top;
	private int bottom;
	private int left;
	private int right;
	private int ctop;
	private int cbottom;
	private int cleft;
	private int cright;
	private int btop;
	private int bbottom;
	private int bleft;
	private int bright;
	private long clickTime = System.nanoTime();

	GuiWaypoint(int id, GuiWaypointScreen screen) {
		super(id, 0, 0, 0, 0, null);
		this.gws = screen;
	}

	void setWaypoint(int id, Waypoint point) {
		this.number = id;
		this.waypoint = point;
		this.name = null;
	}

	public void drawButton(Minecraft mc, int x, int z) {
		if(this.waypoint != null) {
			FontRenderer textRenderer = mc.fontRenderer;
			if(this.name == null) {
                this.name = this.number + ") " + this.waypoint.name;
                while (textRenderer.getStringWidth(this.name) > 160) {
                    this.name = this.name.substring(0, this.name.length() - 1);
                }
            }

			boolean mouseIn = this.mouseIn(x, z);
			this.drawString(textRenderer, this.name, this.xPosition + 1, this.yPosition + 1, (mouseIn ? COLOR1 : COLOR2)[this.waypoint.type]);
			boolean mouseOver = mouseIn && x < this.cleft;
			int r = (int)(this.waypoint.red * 255.0F) & 255;
			int g = (int)(this.waypoint.green * 255.0F) & 255;
			int b = (int)(this.waypoint.blue * 255.0F) & 255;
			int rgb = 0xFF000000 | r << 16 | g << 8 | b;
			this.drawRect(this.cleft, this.ctop, this.cright, this.cbottom, rgb);
			String waypointStatus = this.gws.getRemoveMode() ? (this.gws.isRemove(this.waypoint) ? "X" : "KEEP") : (this.waypoint.enable ? "ON" : "OFF");
			int color = mouseIn ? -2130706433 : (waypointStatus.equals("X") ? -1593901056 : (waypointStatus.equals("KEEP") ? -1610547456 : (this.waypoint.enable ? -1610547456 : -1593901056)));
			this.drawRect(this.bleft, this.btop, this.bright, this.bbottom, color);
			this.drawCenteredString(mc.fontRenderer, waypointStatus, this.bleft + this.bright >> 1, this.btop + 1, -1);
			if(mouseOver) {
				String formatted = String.format("X:%d, Y:%d, Z:%d", this.waypoint.x, this.waypoint.y, this.waypoint.z);
				int tWidth = textRenderer.getStringWidth(formatted);
				int xStart = x - tWidth / 2 - 1;
				int xEnd = xStart + tWidth + 2;
				this.drawRect(xStart, z - 11, xEnd, z - 1, -1610612736);
				this.drawCenteredString(textRenderer, formatted, x, z - 10, -1);
			}

		}
	}

	public boolean mousePressed(Minecraft mc, int x, int z) {
		if(this.waypoint == null) {
			return false;
		} else {
			if(this.mouseIn(x, z)) {
				if(this.colorIn(x, z)) {
					this.waypoint.red = (float)Math.random();
					this.waypoint.green = (float)Math.random();
					this.waypoint.blue = (float)Math.random();
					this.gws.updateWaypoint(this.waypoint);
					return true;
				}

				if(this.buttonIn(x, z)) {
					if(this.gws.getRemoveMode()) {
						this.gws.removeWaypoint(this.waypoint);
					} else {
						this.waypoint.enable = !this.waypoint.enable;
						this.gws.updateWaypoint(this.waypoint);
					}

					return true;
				}

				long time = System.nanoTime();
				if(!this.gws.getRemoveMode() && time < this.clickTime + 300000000L) {
					mc.displayGuiScreen(new GuiWaypointEditorScreen(this.gws, this.waypoint));
					return true;
				}

				this.clickTime = time;
			}

			return false;
		}
	}

	@SuppressWarnings("SameParameterValue")
	void bounds(int x, int z, int width, int height) {
		this.xPosition = x;
		this.yPosition = z;
		this.width = width;
		this.height = height;
		this.top = z;
		this.bottom = z + height;
		this.left = x;
		this.right = x + width;
		this.ctop = this.top;
		this.cbottom = this.bottom;
		this.cright = this.right - 2 - 30 - 2;
		this.cleft = this.cright - 9;
		this.btop = this.top;
		this.bbottom = this.bottom;
		this.bright = this.right - 2;
		this.bleft = this.bright - 30;
	}

	private boolean mouseIn(int x, int z) {
		return z >= this.top && z < this.bottom && x >= this.left && x < this.right;
	}

	private boolean colorIn(int x, int z) {
		return z >= this.ctop && z < this.cbottom && x >= this.cleft && x < this.cright;
	}

	private boolean buttonIn(int x, int z) {
		return z >= this.btop && z < this.bbottom && x >= this.bleft && x < this.bright;
	}
}
