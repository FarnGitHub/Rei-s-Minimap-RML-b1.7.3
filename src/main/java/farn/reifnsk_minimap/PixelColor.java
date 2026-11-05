package farn.reifnsk_minimap;

public class PixelColor {
	static final float d = 0.003921569F;
	public final boolean alphaComposite;
	public float red;
	public float green;
	public float blue;
	public float alpha;

	public PixelColor() {
		this(true);
	}

	public PixelColor(boolean z1) {
		this.alphaComposite = z1;
	}

	public void clear() {
		this.red = this.green = this.blue = this.alpha = 0.0F;
	}

	public void composite(int rgba) {
		this.composite(rgba, 1.0F);
	}

	public void composite(int argb, float f2) {
		if(this.alphaComposite) {
			float a = (float)(argb >> 24 & 255) * 0.003921569F;
			float r = (float)(argb >> 16 & 255) * 0.003921569F * f2;
			float g = (float)(argb >> 8 & 255) * 0.003921569F * f2;
			float b = (float)(argb & 255) * 0.003921569F * f2;
			this.red += (r - this.red) * a;
			this.green += (g - this.green) * a;
			this.blue += (b - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = (float)(argb >> 24 & 255) * 0.003921569F;
			this.red = (float)(argb >> 16 & 255) * 0.003921569F * f2;
			this.green = (float)(argb >> 8 & 255) * 0.003921569F * f2;
			this.blue = (float)(argb & 255) * 0.003921569F * f2;
		}

	}

	public void composite(float alpha, int rgb, float brightness) {
		if(this.alphaComposite) {
			float r = (float)(rgb >> 16 & 255) * 0.003921569F * brightness;
			float g = (float)(rgb >> 8 & 255) * 0.003921569F * brightness;
			float b = (float)(rgb & 255) * 0.003921569F * brightness;
			this.red += (r - this.red) * alpha;
			this.green += (g - this.green) * alpha;
			this.blue += (b - this.blue) * alpha;
			this.alpha += (1.0F - this.alpha) * alpha;
		} else {
			this.alpha = (float)(rgb >> 24 & 255) * 0.003921569F;
			this.red = (float)(rgb >> 16 & 255) * 0.003921569F * brightness;
			this.green = (float)(rgb >> 8 & 255) * 0.003921569F * brightness;
			this.blue = (float)(rgb & 255) * 0.003921569F * brightness;
		}

	}

	public void composite(float alpha, int rgb, float bR, float bG, float bB) {
		if(this.alphaComposite) {
			float r = (float)(rgb >> 16 & 255) * 0.003921569F * bR;
			float g = (float)(rgb >> 8 & 255) * 0.003921569F * bG;
			float b = (float)(rgb & 255) * 0.003921569F * bB;
			this.red += (r - this.red) * alpha;
			this.green += (g - this.green) * alpha;
			this.blue += (b - this.blue) * alpha;
			this.alpha += (1.0F - this.alpha) * alpha;
		} else {
			this.alpha = (float)(rgb >> 24 & 255) * 0.003921569F;
			this.red = (float)(rgb >> 16 & 255) * 0.003921569F * bR;
			this.green = (float)(rgb >> 8 & 255) * 0.003921569F * bG;
			this.blue = (float)(rgb & 255) * 0.003921569F * bB;
		}

	}

	public void composite(float a, float r, float g, float b) {
		if(this.alphaComposite) {
			this.red += (r - this.red) * a;
			this.green += (g - this.green) * a;
			this.blue += (b - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = a;
			this.red = r;
			this.green = g;
			this.blue = b;
		}

	}

	public void composite(float a, float r, float g, float b, float l) {
		if(this.alphaComposite) {
			this.red += (r * l - this.red) * a;
			this.green += (g * l - this.green) * a;
			this.blue += (b * l - this.blue) * a;
			this.alpha += (1.0F - this.alpha) * a;
		} else {
			this.alpha = a;
			this.red = r * l;
			this.green = g * l;
			this.blue = b * l;
		}

	}
}
