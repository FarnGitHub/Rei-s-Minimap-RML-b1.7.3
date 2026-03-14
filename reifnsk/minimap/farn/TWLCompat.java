package reifnsk.minimap.farn;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.model.SimpleButtonModel;
import net.minecraft.src.*;
import reifnsk.minimap.*;

public class TWLCompat {
    static WidgetBoolean widgetBool = new WidgetBoolean(new SettingToggle("enabled"), "Enabled");
    static OpenMinimapWidget menuKey = new OpenMinimapWidget("Open Map Settings");

    public static void init() {
        ReiMinimap.instance.theMinecraft = ModLoader.getMinecraftInstance();
        ModSettingScreen screen = new ModSettingScreen("reiminimap", "Rei's Minimap");
        screen.append(widgetBool);
        screen.append(menuKey);
    }

    static class SettingToggle extends SettingBoolean {

        public SettingToggle(String var1) {
            super(var1, true);
        }

        public void set(Boolean var1, String var2) {
            super.set(var1, var2);
            ReiMinimap.instance.setOption(EnumOption.MINIMAP, var1 ? EnumOptionValue.ENABLE : EnumOptionValue.DISABLE);
        }

        public Boolean get(String var1) {
            return ReiMinimap.instance.enable;
        }
    }

    static class OpenMinimapWidget extends WidgetSetting implements Runnable {
        public Button buttonNew;

        public OpenMinimapWidget(String var1) {
            super(var1);
            this.setTheme("");
            SimpleButtonModel var5 = new SimpleButtonModel();
            this.buttonNew = new Button(var5);
            var5.addActionCallback(this);
            this.add(this.buttonNew);
            this.update();
        }

        @Override
        public void addCallback(Runnable var1) {
            this.buttonNew.getModel().addActionCallback(var1);
        }

        @Override
        public void removeCallback(Runnable var1) {
            this.buttonNew.getModel().removeActionCallback(var1);
        }

        @Override
        public void update() {
            this.buttonNew.setText(this.userString());
        }

        @Override
        public void run() {
            ModLoader.getMinecraftInstance().displayGuiScreen(new GuiOptionScreen());
            this.update();
            GuiModScreen.clicksound();
        }

        @Override
        public String userString() {
            return "Minimap's Settings";
        }
    }
}
