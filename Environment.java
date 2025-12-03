package reifnsk.minimap;

import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Chunk;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

class Environment {
    private static final ConcurrentLinkedQueue<Environment> queue = new ConcurrentLinkedQueue<>();
    private static final int foliageColorPine = ColorizerFoliage.getFoliageColorPine();
    private static final int foliageColorBirch = ColorizerFoliage.getFoliageColorBirch();
    private static Environment[] envCache = new Environment[262144];
    private static World world;
    private static WorldChunkManager worldChunkManager;
    private int x;
    private int z;
    private int grassColor;
    private int foliageColor;
    private float temperature;
    private float humidity;
    private boolean valid;
    private int biomeColor;

    static {
        for(int var0 = 0; var0 < envCache.length; ++var0) {
            envCache[var0] = new Environment();
        }

    }

    private boolean isLocation(int var1, int var2) {
        return this.x == var1 && this.z == var2;
    }

    private void set(int var1, int var2, float var3, float var4, BiomeGenBase var5) {
        this.x = var1;
        this.z = var2;
        this.grassColor = ColorizerGrass.getGrassColor(var3, var4);
        this.foliageColor = ColorizerFoliage.getFoliageColor(var3, var4);
        this.temperature = var3;
        this.humidity = var4;
        this.biomeColor = var5.color;
    }

    public int getGrassColor() {
        return this.grassColor;
    }

    public int getFoliageColor() {
        return this.foliageColor;
    }

    public static int getFoliageColorPine() {
        return foliageColorPine;
    }

    public static int getFoliageColorBirch() {
        return foliageColorBirch;
    }

    public int getBiomeColor() {
        return biomeColor;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public float getHumidity() {
        return this.humidity;
    }

    static void calcEnvironment() {
        if(Thread.currentThread() == ReiMinimap.instance.mcThread) {
            while(true) {
                Environment var0 = queue.poll();
                if(var0 == null) {
                    return;
                }

                calcEnvironment(var0);
            }
        }
    }

    public static Environment getEnvironment(int var0, int var1, Thread var2) {
        int var3 = (var0 & 511) << 9 | var1 & 511;
        Environment var4 = envCache[var3];
        if(!var4.isLocation(var0, var1)) {
            var4.valid = false;
            if(var2 == ReiMinimap.instance.mcThread) {
                calcEnvironment(var0, var1, var4);
            } else {
                var4.set(var0, var1, 0.5F, 1.0F, BiomeGenBase.plains);
                queue.offer(var4);
            }
        }

        return var4;
    }

    public static Environment getEnvironment(Chunk var0, int var1, int var2, Thread var3) {
        return getEnvironment(var0.xPosition * 16 + var1, var0.zPosition * 16 + var2, var3);
    }

    private static void calcEnvironment(Environment var0) {
        calcEnvironment(var0.x, var0.z, var0);
    }

    private static void calcEnvironment(int x, int z, Environment environment) {
        if(!environment.valid) {
            worldChunkManager.func_4069_a(x, z, 1, 1);
            float temperature = (float)worldChunkManager.temperature[0];
            float humidity = (float)worldChunkManager.humidity[0];
            BiomeGenBase base = world.getWorldChunkManager().getBiomeGenAt(x, z);
            if(base == null) {
                base = BiomeGenBase.getBiome(temperature, humidity);
            }
            environment.set(x, z, temperature, humidity, base);
            environment.valid = true;
        }
    }

    public static void setWorld(World var0) {
        world = var0;
        worldChunkManager = var0.getWorldChunkManager();
        queue.clear();

        for(int var1 = 0; var1 < envCache.length; ++var1) {
            envCache[var1].x = Integer.MIN_VALUE;
            envCache[var1].z = Integer.MIN_VALUE;
        }

    }
}
