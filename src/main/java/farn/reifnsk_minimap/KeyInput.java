package farn.reifnsk_minimap;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.lwjglx.input.Keyboard;

public enum KeyInput {
	MENU_KEY(50),
	TOGGLE_ENABLE(0),
	TOGGLE_RENDER_TYPE(0),
	TOGGLE_ZOOM(44),
	TOGGLE_LARGE_MAP(45),
	TOGGLE_LARGE_MAP_LABEL(0),
	TOGGLE_WAYPOINTS_VISIBLE(0),
	TOGGLE_WAYPOINTS_MARKER(0),
	TOGGLE_WAYPOINTS_DIMENSION(0),
	TOGGLE_ENTITIES_RADAR(0),
	SET_WAYPOINT(46),
	WAYPOINT_LIST(0),
	ZOOM_IN(0),
	ZOOM_OUT(0);

	private static final File configFile = new File(ReiMinimap.directory, "keyconfig.txt");
	private final int defaultKeyIndex;
	private final String label;
	private int keyIndex;
	private boolean keyDown;
	private boolean oldKeyDown;

	static {
		loadKeyConfig();
		saveKeyConfig();
	}

	KeyInput(int keyCode) {
		this.defaultKeyIndex = keyCode;
		this.keyIndex = keyCode;
		this.label = ReiMinimap.capitalize(this.name());
	}

	public void setKey(int keyCode) {
		if(keyCode == 1) {
			keyCode = 0;
		}

		if(keyCode != 0 || this != MENU_KEY) {
			if(keyCode != 0) {
				KeyInput[] keyInput5;
				int i4 = (keyInput5 = values()).length;

				for(int i3 = 0; i3 < i4; ++i3) {
					KeyInput keyInput2 = keyInput5[i3];
					if(keyInput2.keyIndex == keyCode) {
						if(keyInput2 == MENU_KEY && this.keyIndex == 0) {
							return;
						}

						keyInput2.keyIndex = this.keyIndex;
						keyInput2.keyDown = false;
						keyInput2.oldKeyDown = false;
						break;
					}
				}
			}

			this.keyIndex = keyCode;
			this.keyDown = false;
			this.oldKeyDown = false;
		}
	}

	public int getKey() {
		return this.keyIndex;
	}

	public String label() {
		return this.label;
	}

	public String getKeyName() {
		String keyName = Keyboard.getKeyName(this.keyIndex);
		return keyName == null ? String.format("#%02X", this.keyIndex) : ReiMinimap.capitalize(keyName);
	}

	public void setKey(String input) {
		int keyIndex = Keyboard.getKeyIndex(input);
		if(input.startsWith("#")) {
			try {
				keyIndex = Integer.parseInt(input.substring(1), 16);
			} catch (Exception ignored) {
			}
		}

		this.setKey(keyIndex);
	}

	public boolean isKeyDown() {
		return this.keyDown;
	}

	public boolean isKeyPush() {
		return this.keyDown && !this.oldKeyDown;
	}

	public boolean isKeyPushUp() {
		return !this.keyDown && this.oldKeyDown;
	}

	public static void update() {
        for (KeyInput input : values()) {
            input.oldKeyDown = input.keyDown;
            input.keyDown = input.keyIndex != 0 && Keyboard.isKeyDown(input.keyIndex);
        }

	}

	public static boolean saveKeyConfig() {

		try(PrintWriter writer = new PrintWriter(configFile)) {
            for (KeyInput input : values())
                writer.println(input);
			return true;
		} catch (Exception ignored) {
		}

		return false;
	}

	public static void loadKeyConfig() {
		try(Scanner scanner = new Scanner(configFile)) {
			while(scanner.hasNextLine()) {
				try {
					String[] splited = scanner.nextLine().split(":");
					valueOf(ReiMinimap.toUpperCase(splited[0].trim())).setKey(ReiMinimap.toUpperCase(splited[1].trim()));
				} catch (Exception ignored) {
				}
			}
		} catch (Exception ignored) {
		}

	}

	public void setDefault() {
		this.keyIndex = this.defaultKeyIndex;
	}

	public boolean isDefault() {
		return this.keyIndex == this.defaultKeyIndex;
	}

	public String toString() {
		return ReiMinimap.capitalize(this.name()) + ": " + this.getKeyName();
	}
}
