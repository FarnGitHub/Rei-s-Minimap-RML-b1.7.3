package farn.reifnsk_minimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Render;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

public class WaypointEntityRender extends Render {
	static final ReiMinimap rm = ReiMinimap.instance;
	final Minecraft mc;
	double far = 1.0D;
	double _d = 1.0D;
	public static boolean isFrontView = false;

	public WaypointEntityRender(Minecraft mc) {
		this.mc = mc;
	}

	public void doRender(Entity entity1, double x, double y, double z, float pitch, float yaw) {
		this.far = (double)(512 >> this.mc.gameSettings.renderDistance) * 0.9D;
		this._d = 1.0D / (double)(256 >> this.mc.gameSettings.renderDistance);
		double scale = rm.getVisibleDimensionScale();
		ArrayList<ViewWaypoint> wPoints = new ArrayList<>();
		if(rm.getMarker()) {

            for (Waypoint waypoint : rm.getWaypoints()) {
                if (waypoint.enable) {
                    wPoints.add(new ViewWaypoint(waypoint, scale));
                }
            }

			if(!wPoints.isEmpty()) {
				wPoints.sort(ViewWaypoint::compareTo);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_FOG);

                for (ViewWaypoint viewPoint : wPoints) {
                    this.draw(viewPoint);
                }

				GL11.glEnable(GL11.GL_FOG);
				GL11.glEnable(GL11.GL_LIGHTING);
				this.shadowSize = 0.0F;
			}
		}
	}

	void draw(ViewWaypoint view) {
		float viewF = (float)Math.max(0.0D, 1.0D - view.distance * this._d);
		FontRenderer textRenderer = this.getFontRendererFromRenderManager();
		GL11.glPushMatrix();
		StringBuilder strBuilder = new StringBuilder();
		if(rm.getMarkerLabel() && view.name != null) {
			strBuilder.append(view.name);
		}

		if(rm.getMarkerDistance()) {
			if(strBuilder.length() != 0) {
				strBuilder.append(" ");
			}

			strBuilder.append(String.format("[%1.2fm]", view.distance));
		}

		String text = strBuilder.toString();
		double scale = (view.dl * 0.1D + 1.0D) * 0.02666666666666667D;
		int y = rm.getMarkerIcon() ? -16 : 0;
		GL11.glTranslated(view.dx, view.dy, view.dz);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(isFrontView ? -this.renderManager.playerViewX : this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScaled(-scale, -scale, scale);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tess = Tessellator.instance;
		if(rm.getMarkerIcon()) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			Waypoint.FILE[view.type].bind();
			tess.startDrawingQuads();
			tess.setColorRGBA_F(view.red, view.green, view.blue, 0.4F);
			tess.addVertexWithUV(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tess.addVertexWithUV(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tess.addVertexWithUV(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tess.addVertexWithUV(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tess.draw();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			tess.startDrawingQuads();
			tess.setColorRGBA_F(view.red, view.green, view.blue, viewF);
			tess.addVertexWithUV(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tess.addVertexWithUV(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tess.addVertexWithUV(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tess.addVertexWithUV(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tess.draw();
		}

		int x = textRenderer.getStringWidth(text) >> 1;
		if(x != 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			tess.startDrawingQuads();
			tess.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.6275F);
			tess.addVertex(-x - 1, y - 1, 0.0D);
			tess.addVertex(-x - 1, y + 8, 0.0D);
			tess.addVertex(x + 1, y + 8, 0.0D);
			tess.addVertex(x + 1, y - 1, 0.0D);
			tess.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			textRenderer.drawString(text, -x, y, view.type == 0 ? 1627389951 : 1627324416);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			int dis = (int)(255.0F * viewF);
			if(dis > 5) {
				textRenderer.drawString(text, -x, y, (view.type == 0 ? 0xFFFFFF : 16711680) | dis << 24);
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	 class ViewWaypoint extends Waypoint implements Comparable<ViewWaypoint> {
		double dx;
		double dy;
		double dz;
		double dl;
		double distance;

		ViewWaypoint(Waypoint point, double dis) {
			super(point);
			this.dx = (double)point.x * dis - RenderManager.renderPosX + 0.5D;
			this.dy = (double)point.y - RenderManager.renderPosY + 0.5D;
			this.dz = (double)point.z * dis - RenderManager.renderPosZ + 0.5D;
			this.dl = this.distance = Math.sqrt(this.dx * this.dx + this.dy * this.dy + this.dz * this.dz);
			if(this.dl > WaypointEntityRender.this.far) {
				double d5 = WaypointEntityRender.this.far / this.dl;
				this.dx *= d5;
				this.dy *= d5;
				this.dz *= d5;
				this.dl = WaypointEntityRender.this.far;
			}

		}

		public int compareTo(ViewWaypoint view) {
			return Double.compare(view.distance, this.distance);
		}
	}
}
