package net.minecraft.src;

import net.minecraft.client.Minecraft;
import reifnsk.minimap.ReiMinimap;

public class mod_ReiMinimap extends BaseMod{
    @Override
    public String Version() {
        return "3.0";
    }

    public mod_ReiMinimap() {
        ModLoader.SetInGameHook(this, true, false);
    }

    public boolean OnTickInGame(Minecraft var1) {
        ReiMinimap.instance.onTickInGame(var1);
        return true;
    }
}
