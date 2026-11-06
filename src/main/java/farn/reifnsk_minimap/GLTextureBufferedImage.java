package farn.reifnsk_minimap;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.src.GLAllocation;

import org.lwjglx.opengl.GL11;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class GLTextureBufferedImage extends BufferedImage {
	private static final ByteBuffer buffer = GLAllocation.createDirectByteBuffer(262144);
	private static final HashMap<Integer, GLTextureBufferedImage> registerImage = new HashMap<>();
	private static final Lock lock = new ReentrantLock();
	public byte[] data;
	private int register;
	private boolean magFiltering;
	private boolean minFiltering;
	private boolean clampTexture;

	private GLTextureBufferedImage(ColorModel colorModel, WritableRaster raster, boolean isRasterPremultiplied, Hashtable properties) {
		super(colorModel, raster, isRasterPremultiplied, properties);
		this.data = ((DataBufferByte)raster.getDataBuffer()).getData();
	}

	public static GLTextureBufferedImage create(int width, int height) {
		ColorSpace colorSpace = ColorSpace.getInstance(1000);
		int[] bits = new int[]{8, 8, 8, 8};
		int[] band = new int[]{0, 1, 2, 3};
		ComponentColorModel component = new ComponentColorModel(colorSpace, bits, true, false, 3, 0);
		WritableRaster raster = Raster.createInterleavedRaster(0, width, height, width * 4, 4, band, (Point)null);
		return new GLTextureBufferedImage(component, raster, false, null);
	}

	public static GLTextureBufferedImage create(BufferedImage delegate) {
		GLTextureBufferedImage image = create(delegate.getWidth(), delegate.getHeight());
		Graphics graphics2 = image.getGraphics();
		graphics2.drawImage(delegate, 0, 0, null);
		graphics2.dispose();
		return image;
	}

	public int register() {
		lock.lock();

		try {
			int param = this.clampTexture ? 10496 : 10497;
			if(this.register == 0) {
				this.register = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, param);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, param);
				buffer.clear();
				buffer.put(this.data);
				buffer.flip();
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.getWidth(), this.getHeight(), 0, 6408, 5121, buffer);
				registerImage.put(this.register, this);
				return this.register;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, param);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, param);
			buffer.clear();
			buffer.put(this.data);
			buffer.flip();
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, this.getWidth(), this.getHeight(), 6408, 5121, buffer);
			return this.register;
		} finally {
			lock.unlock();
		}
	}

	public boolean bind() {
		lock.lock();

		try {
			if(this.register == 0) {
				return false;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
		} finally {
			lock.unlock();
		}

		return true;
	}

	public void unregister() {
		lock.lock();

		try {
			if(this.register != 0) {
				GL11.glDeleteTextures(this.register);
				this.register = 0;
				registerImage.remove(this.register);
			}
		} finally {
			lock.unlock();
		}

	}

	public static void unregister(int registerId) {
		lock.lock();

		try {
			GLTextureBufferedImage image = registerImage.get(registerId);
			if(image != null) {
				image.unregister();
			}
		} finally {
			lock.unlock();
		}

	}

	public void setMagFilter(boolean flag) {
		this.magFiltering = flag;
	}

	public void setMinFilter(boolean flag) {
		this.minFiltering = flag;
	}

	public int getId() {
		return this.register;
	}

	public boolean getMagFilter() {
		return this.magFiltering;
	}

	public boolean getMinFilter() {
		return this.minFiltering;
	}

	public void setClampTexture(boolean flag) {
		this.clampTexture = flag;
	}

	public boolean isClampTexture() {
		return this.clampTexture;
	}

	public void setRGBA(int x, int y, byte r, byte g, byte b, byte a) {
		int index = (y * this.getWidth() + x) * 4;
		this.data[index++] = r;
		this.data[index++] = g;
		this.data[index++] = b;
		this.data[index] = a;
	}

	public void setRGB(int x, int y, byte r, byte g, byte b) {
		int index = (y * this.getWidth() + x) * 4;
		this.data[index++] = r;
		this.data[index++] = g;
		this.data[index++] = b;
		this.data[index] = -1;
	}

	public void setRGB(int x, int y, int rgba) {
		int index = (y * this.getWidth() + x) * 4;
		this.data[index++] = (byte)(rgba >> 16);
		this.data[index++] = (byte)(rgba >> 8);
		this.data[index++] = (byte)(rgba);
		this.data[index] = (byte)(rgba >> 24);
	}

	public static void createTexture(int[] datas, int width, int height, int id, boolean linear, boolean clamp) {
		byte[] bytesData = new byte[width * height * 4];
		int colorIndex = 0;
		int i8 = datas.length;

		for(int bIndex = 0; colorIndex < i8; ++colorIndex) {
			int color = datas[colorIndex];
			bytesData[bIndex++] = (byte)(color >> 16);
			bytesData[bIndex++] = (byte)(color >> 8);
			bytesData[bIndex++] = (byte)(color);
			bytesData[bIndex++] = (byte)(color >> 24);
		}

		createTexture(bytesData, width, height, id, linear, clamp);
	}

	public static void createTexture(byte[] datas, int width, int height, int id, boolean linear, boolean clamp) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, linear ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, linear ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT);
		buffer.clear();
		buffer.put(datas);
		buffer.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}
}
