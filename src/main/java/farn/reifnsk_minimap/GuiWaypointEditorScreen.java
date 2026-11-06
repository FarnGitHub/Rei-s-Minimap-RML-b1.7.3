package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.MathHelper;

import org.lwjglx.input.Keyboard;

public class GuiWaypointEditorScreen extends GuiScreen implements GuiScreenInterface {
	private GuiWaypointScreen parrent;
	private Waypoint waypoint;
	private Waypoint waypointBackup;
	private GuiTextField nameTextField;
	private GuiTextField xCoordTextField;
	private GuiTextField yCoordTextField;
	private GuiTextField zCoordTextField;
	private GuiScrollbar[] rgb;
	private GuiSimpleButton okButton;
	private GuiSimpleButton cancelButton;

	public GuiWaypointEditorScreen(Minecraft mc, Waypoint wayPoint) {
		this.waypoint = wayPoint;
		this.waypointBackup = wayPoint == null ? null : new Waypoint(wayPoint);
		String wName;
		int x;
		int y;
		int z;
		if(wayPoint == null) {
			wName = "";
			EntityPlayerSP entityPlayerSP7 = mc.thePlayer;
			x = MathHelper.floor_double(entityPlayerSP7.posX);
			y = MathHelper.floor_double(entityPlayerSP7.posY);
			z = MathHelper.floor_double(entityPlayerSP7.posZ);
		} else {
			wName = wayPoint.name;
			x = wayPoint.x;
			y = wayPoint.y;
			z = wayPoint.z;
		}

		this.nameTextField = new GuiTextField(wName);
		this.nameTextField.setInputType(0);
		this.nameTextField.active();
		this.xCoordTextField = new GuiTextField(Integer.toString(x));
		this.xCoordTextField.setInputType(1);
		this.yCoordTextField = new GuiTextField(Integer.toString(y));
		this.yCoordTextField.setInputType(2);
		this.zCoordTextField = new GuiTextField(Integer.toString(z));
		this.zCoordTextField.setInputType(1);
		this.nameTextField.setNext(this.xCoordTextField);
		this.nameTextField.setPrev(this.zCoordTextField);
		this.xCoordTextField.setNext(this.yCoordTextField);
		this.xCoordTextField.setPrev(this.nameTextField);
		this.yCoordTextField.setNext(this.zCoordTextField);
		this.yCoordTextField.setPrev(this.xCoordTextField);
		this.zCoordTextField.setNext(this.nameTextField);
		this.zCoordTextField.setPrev(this.yCoordTextField);
		this.rgb = new GuiScrollbar[3];

		for(int index = 0; index < 3; ++index) {
			GuiScrollbar scrollBar = new GuiScrollbar(0, 0, 0, 118, 10);
			scrollBar.setMinimum(0.0F);
			scrollBar.setMaximum(255.0F);
			scrollBar.setVisibleAmount(0.0F);
			scrollBar.setBlockIncrement(10.0F);
			scrollBar.orientation = 1;
			this.rgb[index] = scrollBar;
		}

		this.rgb[0].setValue((float)(wayPoint == null ? Math.random() : (double)wayPoint.red) * 255.0F);
		this.rgb[1].setValue((float)(wayPoint == null ? Math.random() : (double)wayPoint.green) * 255.0F);
		this.rgb[2].setValue((float)(wayPoint == null ? Math.random() : (double)wayPoint.blue) * 255.0F);
	}

	public GuiWaypointEditorScreen(GuiWaypointScreen guiWaypointScreen1, Waypoint waypoint2) {
		this(guiWaypointScreen1.getMinecraft(), waypoint2);
		this.parrent = guiWaypointScreen1;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		for(int index = 0; index < 3; ++index) {
			this.rgb[index].xPosition = this.width - 150 >> 1;
			this.rgb[index].yPosition = this.height / 2 + 20 + index * 10;
			this.controlList.add(this.rgb[index]);
		}

		this.nameTextField.setBounds(this.width - 150 >> 1, this.height / 2 - 40, 150, 9);
		this.xCoordTextField.setBounds(this.width - 150 >> 1, this.height / 2 - 20, 150, 9);
		this.yCoordTextField.setBounds(this.width - 150 >> 1, this.height / 2 - 10, 150, 9);
		this.zCoordTextField.setBounds(this.width - 150 >> 1, this.height / 2, 150, 9);
		this.controlList.add(this.nameTextField);
		this.controlList.add(this.xCoordTextField);
		this.controlList.add(this.yCoordTextField);
		this.controlList.add(this.zCoordTextField);
		this.okButton = new GuiSimpleButton(0, this.width / 2 - 65, this.height / 2 + 58, 60, 14, "OK");
		this.cancelButton = new GuiSimpleButton(1, this.width / 2 + 5, this.height / 2 + 58, 60, 14, "Cancel");
		this.controlList.add(this.okButton);
		this.controlList.add(this.cancelButton);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	public void drawScreen(int i1, int i2, float f3) {
		if(this.mc.theWorld == null)
			this.drawDefaultBackground();
		int x = MathHelper.floor_double(this.mc.thePlayer.posX);
		int y = MathHelper.floor_double(this.mc.thePlayer.posY);
		int z = MathHelper.floor_double(this.mc.thePlayer.posZ);
		this.xCoordTextField.setNorm(x);
		this.yCoordTextField.setNorm(y);
		this.zCoordTextField.setNorm(z);
		String wEdit = "Waypoint Edit";
		int wEditWidth = this.fontRenderer.getStringWidth(wEdit);
		int wEditXs = this.width - wEditWidth >> 1;
		int wEditXe = this.width + wEditWidth >> 1;
		this.drawRect(wEditXs - 2, this.height / 2 - 71, wEditXe + 2, this.height / 2 - 57, -1610612736);
		this.drawCenteredString(this.fontRenderer, wEdit, this.width / 2, this.height / 2 - 68, -1);
		String xDisplay = Integer.toString(x).equals(this.xCoordTextField.displayString) ? "xCoord: (Current)" : "xCoord:";
		this.drawString(this.fontRenderer, xDisplay, (this.width - 150) / 2 + 1, this.height / 2 - 19, -1);
		String yDisplay = Integer.toString(y).equals(this.yCoordTextField.displayString) ? "yCoord: (Current)" : "yCoord:";
		this.drawString(this.fontRenderer, yDisplay, (this.width - 150) / 2 + 1, this.height / 2 - 9, -1);
		String zDisplay = Integer.toString(z).equals(this.zCoordTextField.displayString) ? "zCoord: (Current)" : "zCoord:";
		this.drawString(this.fontRenderer, zDisplay, (this.width - 150) / 2 + 1, this.height / 2 + 1, -1);
		this.drawRect((this.width - 150) / 2 - 2, this.height / 2 - 50, (this.width + 150) / 2 + 2, this.height / 2 + 52, -1610612736);
		this.drawCenteredString(this.fontRenderer, "Waypoint Name", this.width >> 1, this.height / 2 - 49, -1);
		this.drawCenteredString(this.fontRenderer, "Coordinate", this.width >> 1, this.height / 2 - 29, -1);
		this.drawCenteredString(this.fontRenderer, "Color", this.width >> 1, this.height / 2 + 11, -1);
		if(this.waypoint != null) {
			this.waypoint.red = this.rgb[0].getValue() / 255.0F;
			this.waypoint.green = this.rgb[1].getValue() / 255.0F;
			this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
		}

		int r = (int)this.rgb[0].getValue() & 255;
		int g = (int)this.rgb[1].getValue() & 255;
		int b = (int)this.rgb[2].getValue() & 255;
		int rgb = 0xFF000000 | r << 16 | g << 8 | b;
		this.drawCenteredString(this.fontRenderer, String.format("R:%03d", r), this.width / 2 - 15, this.height / 2 + 21, -2139062144);
		this.drawCenteredString(this.fontRenderer, String.format("G:%03d", g), this.width / 2 - 15, this.height / 2 + 31, -2139062144);
		this.drawCenteredString(this.fontRenderer, String.format("B:%03d", b), this.width / 2 - 15, this.height / 2 + 41, -2139062144);
		this.drawRect(this.width + 90 >> 1, this.height / 2 + 20, this.width + 150 >> 1, this.height / 2 + 50, rgb);
		super.drawScreen(i1, i2, f3);
	}

	protected void keyTyped(char character, int keyCode) {
		if(keyCode == 1) {
			this.cancel();
		} else if(keyCode == 28 && GuiTextField.getActive() == this.zCoordTextField) {
			this.zCoordTextField.norm();
			this.accept();
		} else {
			GuiTextField.keyTypedActive(this.mc, character, keyCode);
		}
	}

	private void cancel() {
		if(this.waypoint != null) {
			this.waypoint.set(this.waypointBackup);
		}

		this.mc.displayGuiScreen(this.parrent);
	}

	private void accept() {
		if(this.waypoint != null) {
			this.waypoint.name = this.nameTextField.displayString;
			this.waypoint.x = parseInt(this.xCoordTextField.displayString);
			this.waypoint.y = parseInt(this.yCoordTextField.displayString);
			this.waypoint.z = parseInt(this.zCoordTextField.displayString);
			this.waypoint.red = this.rgb[0].getValue() / 255.0F;
			this.waypoint.green = this.rgb[1].getValue() / 255.0F;
			this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
			this.parrent.updateWaypoint(this.waypoint);
		} else {
			String display = this.nameTextField.displayString;
			int x = parseInt(this.xCoordTextField.displayString);
			int y = parseInt(this.yCoordTextField.displayString);
			int z = parseInt(this.zCoordTextField.displayString);
			float f5 = this.rgb[0].getValue() / 255.0F;
			float f6 = this.rgb[1].getValue() / 255.0F;
			float f7 = this.rgb[2].getValue() / 255.0F;
			this.waypoint = new Waypoint(display, x, y, z, true, f5, f6, f7);
			if(this.parrent == null) {
				ReiMinimap.instance.getWaypoints().add(this.waypoint);
				ReiMinimap.instance.saveWaypoints();
			} else {
				this.parrent.addWaypoint(this.waypoint);
			}
		}

		this.mc.displayGuiScreen(this.parrent);
	}

	private static int parseInt(String input) {
		try {
			return Integer.parseInt(input);
		} catch (Exception exception1) {
			return 0;
		}
	}

	protected void actionPerformed(GuiButton button) {
		if(button == this.okButton) {
			this.accept();
		} else if(button == this.cancelButton) {
			this.cancel();
		}
	}
}
