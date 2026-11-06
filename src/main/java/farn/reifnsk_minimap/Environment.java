package farn.reifnsk_minimap;

import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Chunk;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

class Environment {
    private static final ConcurrentLinkedQueue<Environment> queue = new ConcurrentLinkedQueue<>();
    private static final Environment[] envCache = new Environment[262144];
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
        for(int i = 0; i < envCache.length; i++)
            envCache[i] = new Environment();
    }

    private boolean isLocation(int x, int z) {
        return this.x == x && this.z == z;
    }

    private void set(int x, int z, float temp, float humid, BiomeGenBase biome) {
        this.x = x;
        this.z = z;
        this.grassColor = ColorizerGrass.getGrassColor(temp, humid);
        this.foliageColor = ColorizerFoliage.getFoliageColor(temp, humid);
        this.temperature = temp;
        this.humidity = humid;
        this.biomeColor = biome.color;
    }

    public int getGrassColor() {
        return this.grassColor;
    }

    public int getFoliageColor() {
        return this.foliageColor;
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
                Environment env = queue.poll();
                if(env == null) {
                    return;
                }

                calcEnvironment(env);
            }
        }
    }

    public static Environment getEnvironment(int x, int z, Thread var2) {
        int index = (x & 511) << 9 | z & 511;
        Environment env = envCache[index];
        if(!env.isLocation(x, z)) {
            env.valid = false;
            if(var2 == ReiMinimap.instance.mcThread) {
                calcEnvironment(x, z, env);
            } else {
                env.set(x, z, 0.5F, 1.0F, BiomeGenBase.plains);
                queue.offer(env);
            }
        }

        return env;
    }

    public static Environment getEnvironment(Chunk chunk, int x, int z, Thread thread) {
        return getEnvironment(chunk.xPosition * 16 + x, chunk.zPosition * 16 + z, thread);
    }

    private static void calcEnvironment(Environment env) {
        calcEnvironment(env.x, env.z, env);
    }

    private static void calcEnvironment(int x, int z, Environment env) {
        if(!env.valid) {
            worldChunkManager.func_4069_a(x, z, 1, 1);
            float temperature = (float)worldChunkManager.temperature[0];
            float humidity = (float)worldChunkManager.humidity[0];
            BiomeGenBase base = world.getWorldChunkManager().getBiomeGenAt(x, z);
            if(base == null) {
                base = BiomeGenBase.getBiome(temperature, humidity);
            }
            env.set(x, z, temperature, humidity, base);
            env.valid = true;
        }
    }

    public static void setWorld(World world) {
        Environment.world = world;
        worldChunkManager = world.getWorldChunkManager();
        queue.clear();

        for (Environment environment : envCache) {
            environment.x = Integer.MIN_VALUE;
            environment.z = Integer.MIN_VALUE;
        }

    }
}
