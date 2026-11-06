package farn.reifnsk_minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;

public class WaypointEntity extends Entity {
	private final Minecraft mc = ModLoader.getMinecraftInstance();

	public WaypointEntity() {
		super(ModLoader.getMinecraftInstance().theWorld);
		this.ignoreFrustumCheck = true;
		this.onUpdate();
	}

	public void onUpdate() {
		this.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
	}

	protected void entityInit() {
	}

	protected void readEntityFromNBT(NBTTagCompound nbt) {
	}

	protected void writeEntityToNBT(NBTTagCompound nbt) {
	}

	public boolean isInRangeToRenderDist(double var1) {
		return true;
	}
}
