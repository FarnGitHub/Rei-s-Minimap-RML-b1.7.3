package net.minecraft.src;

import net.minecraft.client.Minecraft;
import reifnsk.minimap.ReiMinimap;
import reifnsk.minimap.WaypointEntity;
import reifnsk.minimap.WaypointEntityRender;

import java.util.Map;

public class mod_ReiMinimap extends BaseMod{
    @Override
    public String Version() {
        return "3.0";
    }

    public mod_ReiMinimap() {
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
