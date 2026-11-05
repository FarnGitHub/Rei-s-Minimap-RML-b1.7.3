package farn.reifnsk_minimap;

public class Waypoint {
	private static final int MAX_TYPE_VALUE = 1;
	public static final int NORMAL = 0;
	public static final int DEATH_POINT = 1;
	static final GLTexture[] FILE = new GLTexture[]{GLTexture.WAYPOINT1, GLTexture.WAYPOINT2};
	static final GLTexture[] MARKER = new GLTexture[]{GLTexture.MARKER1, GLTexture.MARKER2};
	public String name;
	public int x;
	public int y;
	public int z;
	public boolean enable;
	public float red;
	public float green;
	public float blue;
	public int type;

	Waypoint(String name, int x, int y, int z, boolean enable, float red, float green, float blue) {
		this.name = name == null ? "" : name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.enable = enable;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	Waypoint(String name, int x, int y, int z, boolean enable, float red, float green, float blue, int type) {
		this.name = name == null ? "" : name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.enable = enable;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.type = type % 2;
	}

	Waypoint(Waypoint waypoint) {
		this.set(waypoint);
	}

	void set(Waypoint waypoint) {
		this.name = waypoint.name;
		this.x = waypoint.x;
		this.y = waypoint.y;
		this.z = waypoint.z;
		this.enable = waypoint.enable;
		this.red = waypoint.red;
		this.green = waypoint.green;
		this.blue = waypoint.blue;
		this.type = waypoint.type % 2;
	}

	static Waypoint load(String input) {
		try {
			String[] splited = input.split(":");
			String name = splited[0];
			int x = Integer.parseInt(splited[1]);
			int y = Integer.parseInt(splited[2]);
			int z = Integer.parseInt(splited[3]);
			boolean enable = Boolean.parseBoolean(splited[4]);
			int rgb = Integer.parseInt(splited[5], 16);
			float r = (float)(rgb >> 16 & 255) / 255.0F;
			float g = (float)(rgb >> 8 & 255) / 255.0F;
			float b = (float)(rgb & 255) / 255.0F;
			int type = splited.length >= 7 ? Integer.parseInt(splited[6]) : 0;
			return new Waypoint(name, x, y, z, enable, r, g, b, type);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String toString() {
		int r = (int)(this.red * 255.0F) & 255;
		int g = (int)(this.green * 255.0F) & 255;
		int b = (int)(this.blue * 255.0F) & 255;
		int rgb = r << 16 | g << 8 | b;
		return String.format(this.type == 0 ? "%s:%d:%d:%d:%s:%06X" : "%s:%d:%d:%d:%s:%06X:%d", new Object[]{this.name, this.x, this.y, this.z, this.enable, rgb, this.type});
	}
}
