package net.minecraft.src;

import net.minecraft.client.Minecraft;
import farn.reifnsk_minimap.ReiMinimap;
import farn.reifnsk_minimap.WaypointEntity;
import farn.reifnsk_minimap.WaypointEntityRender;

import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

@SuppressWarnings("unused")
public class mod_ReiMinimapBetaEvo extends BaseMod {
    @Override
    public String Version() {
        return "3.0";
    }

    public mod_ReiMinimapBetaEvo() {
        this.fixBetaEvoShittyClassLoading();
        ModLoader.SetInGameHook(this, true, false);
    }

    public boolean OnTickInGame(Minecraft mc) {
        ReiMinimap.instance.onTickInGame(mc);
        return true;
    }

    @SuppressWarnings("unchecked")
    public void AddRenderer(Map map) {
        map.put(WaypointEntity.class, new WaypointEntityRender(ModLoader.getMinecraftInstance()));
    }

    private void fixBetaEvoShittyClassLoading() {
        boolean shouldFixJar = false;
        try {
            Class.forName("farn.reifnsk_minimap.ReiMinimap", false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException ignored) {
            shouldFixJar = true;
        }

        if (shouldFixJar) {
            System.out.println("[Rei's Minimap] can't load farn.reifnsk_minimap.ReiMinimap class, try adding jar to classpath...");
            try {
                URL classResource = this.getClass().getResource("/" + this.getClass().getName().replace('.', '/') + ".class");
                if (classResource == null) throw new NullPointerException("class resource is null");
                JarURLConnection conn = (JarURLConnection) classResource.openConnection();
                URL jar = conn.getJarFileURL();
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(ModLoader.class.getClassLoader(), jar);
                System.out.println("[Rei's Minimap] successfully adding jar to classpath");
            } catch (Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}
