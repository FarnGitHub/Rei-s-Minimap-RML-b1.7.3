package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.Tessellator;

import org.lwjglx.opengl.GL11;

public class GuiScrollbar extends GuiButton {
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	private final long repeatStart = 500000000L;
	private final long repeatInterval = 40000000L;
	int orientation;
	private float value = 0.0F;
	private float extent = 0.0F;
	private float min = 0.0F;
	private float max = 0.0F;
	private float unitIncrement = 1.0F;
	private float blockIncrement = 9.0F;
	private int draggingPos;
	private float draggingValue;
	private int dragging;
	private long draggingTimer;
	private int minBarSize = 6;

	public GuiScrollbar(int i1, int i2, int i3, int i4, int i5) {
		super(i1, i2, i3, i4, i5, "");
	}

	public void drawButton(Minecraft minecraft1, int i2, int i3) {
		if(this.value > this.max - this.extent) {
			this.value = this.max - this.extent;
		}

		if(this.value < this.min) {
			this.value = this.min;
		}

		if(this.orientation == VERTICAL) {
			this.drawVertical(minecraft1, i2, i3);
		} else if(this.orientation == HORIZONTAL) {
			this.drawHorizontal(minecraft1, i2, i3);
		}

	}

	private void drawVertical(Minecraft minecraft1, int i2, int i3) {
		if(this.dragging != 0) {
			this.mouseDragged(minecraft1, i2, i3);
		}

		double d4 = (double)this.xPosition + (double)this.width * 0.5D;
		int i6 = this.yPosition;
		int i7 = this.yPosition + this.height;
		Tessellator tessellator8 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boolean z9 = (double)i2 >= d4 - 4.0D && (double)i2 <= d4 + 4.0D;
		if(!z9 || i3 < i6 || i3 > i6 + 8 || this.dragging != 0 && this.dragging != 1) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startDrawingQuads();
		tessellator8.addVertex(d4, i6, 0.0D);
		tessellator8.addVertex(d4, i6, 0.0D);
		tessellator8.addVertex(d4 - 4.0D, i6 + 8, 0.0D);
		tessellator8.addVertex(d4 + 4.0D, i6 + 8, 0.0D);
		tessellator8.draw();
		double d10;
		double d12;
		if(this.min < this.max - this.extent) {
			d10 = this.height - 20;
			d12 = this.extent / (this.max - this.min);
			if(d12 * d10 < (double)this.minBarSize) {
				d12 = (double)this.minBarSize / d10;
			}

			double d14 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d12);
			double d16 = d14 + d12;
			d14 = (double)i6 + d14 * d10 + 10.0D;
			d16 = (double)i6 + d16 * d10 + 10.0D;
			if(this.dragging != 5 && (!z9 || (double)i3 < d14 || (double)i3 > d16 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startDrawingQuads();
			tessellator8.addVertex(d4 + 4.0D, d14, 0.0D);
			tessellator8.addVertex(d4 - 4.0D, d14, 0.0D);
			tessellator8.addVertex(d4 - 4.0D, d16, 0.0D);
			tessellator8.addVertex(d4 + 4.0D, d16, 0.0D);
			tessellator8.draw();
		} else {
			d10 = i6 + 10;
			d12 = i7 - 10;
			if(this.dragging != 5 && (!z9 || (double)i3 < d10 || (double)i3 > d12 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startDrawingQuads();
			tessellator8.addVertex(d4 + 4.0D, d10, 0.0D);
			tessellator8.addVertex(d4 - 4.0D, d10, 0.0D);
			tessellator8.addVertex(d4 - 4.0D, d12, 0.0D);
			tessellator8.addVertex(d4 + 4.0D, d12, 0.0D);
			tessellator8.draw();
		}

		if(!z9 || i3 < i7 - 8 || i3 > i7 || this.dragging != 0 && this.dragging != 2) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startDrawingQuads();
		tessellator8.addVertex(d4, i7, 0.0D);
		tessellator8.addVertex(d4, i7, 0.0D);
		tessellator8.addVertex(d4 + 4.0D, i7 - 8, 0.0D);
		tessellator8.addVertex(d4 - 4.0D, i7 - 8, 0.0D);
		tessellator8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawHorizontal(Minecraft minecraft1, int i2, int i3) {
		if(this.dragging != 0) {
			this.mouseDragged(minecraft1, i2, i3);
		}

		double d4 = (double)this.yPosition + (double)this.height * 0.5D;
		int i6 = this.xPosition;
		int i7 = this.xPosition + this.width;
		Tessellator tessellator8 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		boolean z9 = (double)i3 >= d4 - 4.0D && (double)i3 <= d4 + 4.0D;
		if(!z9 || i2 < i6 || i2 > i6 + 8 || this.dragging != 0 && this.dragging != 1) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startDrawingQuads();
		tessellator8.addVertex(i6, d4, 0.0D);
		tessellator8.addVertex(i6, d4, 0.0D);
		tessellator8.addVertex(i6 + 8, d4 + 4.0D, 0.0D);
		tessellator8.addVertex(i6 + 8, d4 - 4.0D, 0.0D);
		tessellator8.draw();
		double d10;
		double d12;
		if(this.min < this.max - this.extent) {
			d10 = this.width - 20;
			d12 = this.extent / (this.max - this.min);
			if(d12 * d10 < (double)this.minBarSize) {
				d12 = (double)this.minBarSize / d10;
			}

			double d14 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d12);
			double d16 = d14 + d12;
			d14 = (double)i6 + d14 * d10 + 10.0D;
			d16 = (double)i6 + d16 * d10 + 10.0D;
			if(this.dragging != 6 && (!z9 || (double)i2 < d14 || (double)i2 > d16 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startDrawingQuads();
			tessellator8.addVertex(d14, d4 - 4.0D, 0.0D);
			tessellator8.addVertex(d14, d4 + 4.0D, 0.0D);
			tessellator8.addVertex(d16, d4 + 4.0D, 0.0D);
			tessellator8.addVertex(d16, d4 - 4.0D, 0.0D);
			tessellator8.draw();
		} else {
			d10 = i6 + 10;
			d12 = i7 - 10;
			if(this.dragging != 6 && (!z9 || (double)i2 < d10 || (double)i2 > d12 || this.dragging != 0)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
			}

			tessellator8.startDrawingQuads();
			tessellator8.addVertex(d10, d4 - 4.0D, 0.0D);
			tessellator8.addVertex(d10, d4 + 4.0D, 0.0D);
			tessellator8.addVertex(d12, d4 + 4.0D, 0.0D);
			tessellator8.addVertex(d12, d4 - 4.0D, 0.0D);
			tessellator8.draw();
		}

		if(!z9 || i2 < i7 - 8 || i2 > i7 || this.dragging != 0 && this.dragging != 2) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
		}

		tessellator8.startDrawingQuads();
		tessellator8.addVertex(i7, d4, 0.0D);
		tessellator8.addVertex(i7, d4, 0.0D);
		tessellator8.addVertex(i7 - 8, d4 - 4.0D, 0.0D);
		tessellator8.addVertex(i7 - 8, d4 + 4.0D, 0.0D);
		tessellator8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public boolean mousePressed(Minecraft mc, int x, int z) {
		return super.mousePressed(mc, x, z) && (this.orientation == VERTICAL && this.mousePressedVertical(mc, x, z) || this.orientation == HORIZONTAL && this.mousePressedHorizontal(mc, x, z));
	}

	private boolean mousePressedVertical(Minecraft mc, int x, int z) {
		double scrollX = (double)this.xPosition + (double)this.width * 0.5D;
		int scrollMinY = this.yPosition;
		int scrollMaxY = this.yPosition + this.height;
		if((double)x >= scrollX - 4.0D && (double)x <= scrollX + 4.0D) {
			if(this.max == this.min) {
				return true;
			} else {
				if(this.dragging == 0) {
					this.draggingTimer = System.nanoTime() + this.repeatStart;
				}

				if(z < scrollMinY || z > scrollMinY + 8 || this.dragging != 0 && this.dragging != 1) {
					if(z < scrollMaxY - 8 || z > scrollMaxY || this.dragging != 0 && this.dragging != 2) {
						double d8 = this.height - 20;
						double d10 = this.extent / (this.max - this.min);
						if(d10 * d8 < (double)this.minBarSize) {
							d10 = (double)this.minBarSize / d8;
						}

						double d12 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d10);
						double d14 = d12 + d10;
						d12 = (double)scrollMinY + d12 * d8 + 10.0D;
						d14 = (double)scrollMinY + d14 * d8 + 10.0D;
						if((double)z >= d12 || this.dragging != 0 && this.dragging != 3) {
							if((double)z > d14 && (this.dragging == 0 || this.dragging == 4)) {
								this.dragging = 4;
								this.blockIncrement();
								return true;
							} else {
								if(this.dragging == 0) {
									this.dragging = 5;
									this.draggingPos = z;
									this.draggingValue = this.value;
								}

								return true;
							}
						} else {
							this.dragging = 3;
							this.blockDecrement();
							return true;
						}
					} else {
						this.dragging = 2;
						this.unitIncrement();
						return true;
					}
				} else {
					this.dragging = 1;
					this.unitDecrement();
					return true;
				}
			}
		} else {
			return false;
		}
	}

	private boolean mousePressedHorizontal(Minecraft mc, int x, int z) {
		double scrollY = (double)this.yPosition + (double)this.height * 0.5D;
		int scrollMinX = this.xPosition;
		int scrollMaxX = this.xPosition + this.width;
		if((double)z >= scrollY - 4.0D && (double)z <= scrollY + 4.0D) {
			if(this.max == this.min) {
				return true;
			} else {
				if(this.dragging == 0) {
					this.draggingTimer = System.nanoTime() + this.repeatStart;
				}

				if(x < scrollMinX || x > scrollMinX + 8 || this.dragging != 0 && this.dragging != 1) {
					if(x < scrollMaxX - 8 || x > scrollMaxX || this.dragging != 0 && this.dragging != 2) {
						double d8 = this.width - 20;
						double d10 = this.extent / (this.max - this.min);
						if(d10 * d8 < (double)this.minBarSize) {
							d10 = (double)this.minBarSize / d8;
						}

						double d12 = (double)(this.value / (this.max - this.min - this.extent)) * (1.0D - d10);
						double d14 = d12 + d10;
						d12 = (double)scrollMinX + d12 * d8 + 10.0D;
						d14 = (double)scrollMinX + d14 * d8 + 10.0D;
						if((double)x >= d12 || this.dragging != 0 && this.dragging != 3) {
							if((double)x > d14 && (this.dragging == 0 || this.dragging == 4)) {
								this.dragging = 4;
								this.blockIncrement();
								return true;
							} else {
								if(this.dragging == 0) {
									this.dragging = 6;
									this.draggingPos = x;
									this.draggingValue = this.value;
								}

								return true;
							}
						} else {
							this.dragging = 3;
							this.blockDecrement();
							return true;
						}
					} else {
						this.dragging = 2;
						this.unitIncrement();
						return true;
					}
				} else {
					this.dragging = 1;
					this.unitDecrement();
					return true;
				}
			}
		} else {
			return false;
		}
	}

	protected void mouseDragged(Minecraft mc, int x, int z) {
		if(this.dragging == 5) {
			float dragHeight = (float)(this.height - 20);
			float dragExtent = this.extent / (this.max - this.min);
			if(dragExtent * dragHeight < (float)this.minBarSize) {
				dragExtent = (float)this.minBarSize / dragHeight;
			}

			float cValue = this.draggingValue + (this.max - this.min - this.extent) / (1.0F - dragExtent) * (float)(z - this.draggingPos) / dragHeight;
			this.value = Math.max(this.min, Math.min(this.max - this.extent, cValue));
		} else if(this.dragging == 6) {
			float dragHeight = (float)(this.width - 20);
			float dragExtent = this.extent / (this.max - this.min);
			if(dragExtent * dragHeight < (float)this.minBarSize) {
				dragExtent = (float)this.minBarSize / dragHeight;
			}

			float cValue = this.draggingValue + (this.max - this.min - this.extent) / (1.0F - dragExtent) * (float)(x - this.draggingPos) / dragHeight;
			this.value = Math.max(this.min, Math.min(this.max - this.extent, cValue));
		}

		long time = System.nanoTime();
		if(this.draggingTimer < time) {
			this.mousePressed(mc, x, z);
			this.draggingTimer = time + this.repeatInterval;
		}

	}

	public void mouseReleased(int x, int z) {
		this.dragging = 0;
	}

	public void setValue(float val) {
		if(val < this.min) {
			val = this.min;
		}

		if(val > this.max - this.extent) {
			val = this.max - this.extent;
		}

		this.value = val;
	}

	public float getValue() {
		return this.value;
	}

	public void setMaximum(float val) {
		if(this.min > val) {
			throw new IllegalArgumentException("min > max");
		} else {
			this.max = val;
			this.value = Math.min(this.value, this.max);
		}
	}

	public float getMaximum() {
		return this.max;
	}

	public void setMinimum(float val) {
		if(val > this.max) {
			throw new IllegalArgumentException("min > max");
		} else {
			this.min = val;
			this.value = Math.max(this.value, this.min);
		}
	}

	public float getMinimum() {
		return this.min;
	}

	public void setVisibleAmount(float f1) {
		if(this.max - this.min < f1) {
			throw new IllegalArgumentException("max - min < extent");
		} else {
			this.extent = Math.min(this.max - this.min, f1);
		}
	}

	public float getVisibleAmount() {
		return this.extent;
	}

	public void unitIncrement() {
		this.value = Math.min(this.max - this.extent, this.value + this.unitIncrement);
	}

	public void unitDecrement() {
		this.value = Math.max(this.min, this.value - this.unitIncrement);
	}

	public void blockIncrement() {
		this.value = Math.min(this.max - this.extent, this.value + this.blockIncrement);
	}

	public void blockDecrement() {
		this.value = Math.max(this.min, this.value - this.blockIncrement);
	}

	public void setMinimumBarSize(int i1) {
		this.minBarSize = i1;
	}

	public int getMinimumBarSize() {
		return this.minBarSize;
	}

	public void setUnitIncrement(float f1) {
		this.unitIncrement = f1;
	}

	public void setBlockIncrement(float f1) {
		this.blockIncrement = f1;
	}

	public float getUnitIncrement() {
		return this.unitIncrement;
	}

	public float getBlockIncrement() {
		return this.blockIncrement;
	}
}
