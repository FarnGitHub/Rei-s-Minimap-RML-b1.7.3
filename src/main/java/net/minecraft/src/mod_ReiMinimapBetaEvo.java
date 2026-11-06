package net.minecraft.src;

import net.minecraft.client.Minecraft;
import farn.reifnsk_minimap.ReiMinimap;
import farn.reifnsk_minimap.WaypointEntity;
import farn.reifnsk_minimap.WaypointEntityRender;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

@SuppressWarnings("unused")
public class mod_ReiMinimapBetaEvo extends BaseMod{
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
        try {
            ClassLoader loaderClassLoader = ModLoader.class.getClassLoader();
            if(shouldFixJar()) {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(ModLoader.class.getClassLoader(), getJarFile());
            }
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private URL getJarFile() throws IOException {
        URL classResource = this.getClass().getResource("/" + this.getClass().getName().replace('.', '/') + ".class");
        if (classResource == null) throw new RuntimeException("class resource is null");

        if ("jar".equals(classResource.getProtocol())) {
            JarURLConnection conn = (JarURLConnection) classResource.openConnection();
            return conn.getJarFileURL();
        }
        throw new RuntimeException("Invalid Jar File URL String");
    }

    private boolean shouldFixJar() {
        try {
            Class.forName("farn.reifnsk_minimap.ReiMinimap", false, mod_ReiMinimapBetaEvo.class.getClassLoader());
            return false;
        } catch (ClassNotFoundException ignored) {
            return true;
        }
    }
}
