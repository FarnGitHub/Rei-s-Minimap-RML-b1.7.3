package farn.reifnsk_minimap;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.lwjglx.opengl.GL11;

public class GLTexture {
	private static final String DEFAULT_PACK = "/farn/reifnsk_minimap/reitextures/";
	private static String pack = DEFAULT_PACK;
	private static final ArrayList<GLTexture> list = new ArrayList<>();
	private static final GLTexture missing = new GLTexture("missing.png", true, false);
	static final GLTexture TEMPERATURE = new GLTexture("temperature.png", true, true);
	static final GLTexture HUMIDITY = new GLTexture("humidity.png", true, true);
	static final GLTexture ROUND_MAP = new GLTexture("roundmap.png", true, true);
	static final GLTexture ROUND_MAP_MASK = new GLTexture("roundmap_mask.png", false, true);
	static final GLTexture SQUARE_MAP = new GLTexture("squaremap.png", true, true);
	static final GLTexture SQUARE_MAP_MASK = new GLTexture("squaremap_mask.png", false, true);
	static final GLTexture ENTITY = new GLTexture("entity.png", true, true);
	static final GLTexture ENTITY2 = new GLTexture("entity2.png", true, true);
	static final GLTexture LIGHTNING = new GLTexture("lightning.png", true, true);
	static final GLTexture N = new GLTexture("n.png", true, true);
	static final GLTexture E = new GLTexture("e.png", true, true);
	static final GLTexture W = new GLTexture("w.png", true, true);
	static final GLTexture S = new GLTexture("s.png", true, true);
	static final GLTexture MMARROW = new GLTexture("mmarrow.png", true, true);
	static final GLTexture WAYPOINT1 = new GLTexture("waypoint.png", true, true);
	static final GLTexture WAYPOINT2 = new GLTexture("waypoint2.png", true, true);
	static final GLTexture MARKER1 = new GLTexture("marker.png", true, true);
	static final GLTexture MARKER2 = new GLTexture("marker2.png", true, true);
	private final String fileName;
	private final boolean blur;
	private final boolean clamp;
	private int textureId;

	static void setPack(String pack) {
		if(!pack.equals(GLTexture.pack)) {

            for (GLTexture gLTexture : list) {
                gLTexture.release();
            }

			GLTexture.pack = pack;
		}
	}

	private GLTexture(String fileName, boolean blur, boolean clamp) {
		this.fileName = fileName;
		this.blur = blur;
		this.clamp = clamp;
		list.add(this);
	}

	int[] getData() {
		BufferedImage image = read(this.fileName);
		int width = image.getWidth();
		int height = image.getHeight();
		int[] data = new int[width * height];
		image.getRGB(0, 0, width, height, data, 0, width);
		return data;
	}

	void bind() {
		if(this.textureId == 0) {
			BufferedImage image = read(this.fileName);
			if(image == null) {
				this.textureId = this == missing ? -2 : -1;
			} else {
				this.textureId = GL11.glGenTextures();
				int width = image.getWidth();
				int height = image.getHeight();
				int[] data = new int[width * height];
				image.getRGB(0, 0, width, height, data, 0, width);
				GLTextureBufferedImage.createTexture(data, width, height, this.textureId, this.blur, this.clamp);
			}
		}

		if(this.textureId == -2) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		} else {
			if(this.textureId == -1) {
				missing.bind();
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
		}
	}

	void release() {
		if(this.textureId > 0) {
			GL11.glDeleteTextures(this.textureId);
		}

		this.textureId = 0;
	}

	private static BufferedImage read(String path) {
		BufferedImage bufferedImage1 = readImage(pack + path);
		return bufferedImage1 == null ? readImage(DEFAULT_PACK + path) : bufferedImage1;
	}

	private static BufferedImage readImage(String path) {
		InputStream resource = ReiMinimap.instance.theMinecraft.texturePackList.selectedTexturePack.getResourceAsStream(path);
        if (resource != null) {
            try {
                return ImageIO.read(resource);
            } catch (Exception ignored) {
            } finally {
                try {
                    resource.close();
                } catch (Exception ignored2) {
                }

            }

        }
        return null;
    }
}
