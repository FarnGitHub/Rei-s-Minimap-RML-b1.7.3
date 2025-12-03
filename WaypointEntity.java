package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;

public class WaypointEntity extends Entity {
	private final Minecraft mc;

	public WaypointEntity(Minecraft minecraft1) {
		super(minecraft1.theWorld);
		this.mc = minecraft1;
		this.ignoreFrustumCheck = true;
		this.onUpdate();
	}

	public void onUpdate() {
		this.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
	}

	protected void entityInit() {
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
	}

	public boolean isInRangeToRenderDist(double var1) {
		return true;
	}
}
