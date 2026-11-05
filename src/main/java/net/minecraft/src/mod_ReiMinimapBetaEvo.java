package net.minecraft.src;

import net.minecraft.client.Minecraft;
import farn.reifnsk_minimap.ReiMinimap;
import farn.reifnsk_minimap.WaypointEntity;
import farn.reifnsk_minimap.WaypointEntityRender;

import java.util.Map;

@SuppressWarnings("unused")
public class mod_ReiMinimapBetaEvo extends BaseMod{
    @Override
    public String Version() {
        return "3.0";
    }

    public mod_ReiMinimapBetaEvo() {
        ModLoader.SetInGameHook(this, true, false);
    }

    public boolean OnTickInGame(Minecraft mc) {
        ReiMinimap.instance.onTickInGame(mc);
        return true;
    }

    @SuppressWarnings({"unchecked"})
    public void AddRenderer(Map entityRendererMap) {
        entityRendererMap.put(WaypointEntity.class, new WaypointEntityRender(ModLoader.getMinecraftInstance()));
    }
}
