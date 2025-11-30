package reifnsk.minimap;

import java.util.Random;

import net.minecraft.src.*;

class Environment {
	private static final int foliageColorPine = ColorizerFoliage.getFoliageColorPine();
	private static final int foliageColorBirch = ColorizerFoliage.getFoliageColorBirch();
	private static long randomSeed;
	private static NoiseGeneratorOctaves2 temperatureGen;
	private static NoiseGeneratorOctaves2 humidityGen;
	private static NoiseGeneratorOctaves2 noiseGen;
	private static Environment[] envCache = new Environment[262144];
	private int x;
	private int z;
	private int grassColor;
	private int foliageColor;
	public double temperatureColor;
	public double humidityColor;
	public BiomeGenBase theBiome;
	private static World world;
	private int biomeSolidColor;
	private boolean valid;

	private boolean isLocation(int x, int z) {
		return this.x == x && this.z == z;
	}

	private void set(int x, int z, double temperature, double humidity, BiomeGenBase base) {
		this.x = x;
		this.z = z;
		this.grassColor = ColorizerGrass.getGrassColor(temperature, humidity);
		this.foliageColor = ColorizerFoliage.getFoliageColor(temperature, humidity);
		this.temperatureColor = temperature;
		this.humidityColor = humidity;
		this.theBiome = base;
		this.biomeSolidColor = theBiome.color;
	}

	public int getGrassColor() {
		return this.grassColor;
	}

	public int getSolidGrassColor() {
		return this.biomeSolidColor;
	}

	public int getFoliageColor() {
		return this.foliageColor;
	}

	public int getFoliageColorPine() {
		return foliageColorPine;
	}

	public int getFoliageColorBirch() {
		return foliageColorBirch;
	}

	public static Environment getEnvironment(int x, int z) {
		int ptr = (x & 511) << 9 | z & 511;
		Environment env = envCache[ptr];
		if(!env.isLocation(x, z)) {
			env.valid = false;
			calcEnvironment(x, z, env);
		}

		return env;
	}

	public static Environment getEnvironment(Chunk chunk, int x, int z) {
		return getEnvironment(chunk.xPosition * 16 + x, chunk.zPosition * 16 + z);
	}

	private static void calcEnvironment(int x, int z, Environment environment) {
		if(!environment.valid) {
			world.getWorldChunkManager().func_4069_a(x, z, 1, 1);
			double temperature = world.getWorldChunkManager().temperature[0];
			double humidity = world.getWorldChunkManager().humidity[0];
			BiomeGenBase base = world.getWorldChunkManager().getBiomeGenAt(x, z);
			if(base == null) {
				base = BiomeGenBase.getBiome((float)temperature, (float) humidity);
			}
			environment.set(x, z, temperature, humidity, base);
			environment.valid = true;
		}
	}

	public static void setWorld(World worldRaw) {
		if(world != worldRaw) {
			world = worldRaw;
			randomSeed = world.getRandomSeed();
			temperatureGen = new NoiseGeneratorOctaves2(new Random(randomSeed * 9871L), 4);
			humidityGen = new NoiseGeneratorOctaves2(new Random(randomSeed * 39811L), 4);
			noiseGen = new NoiseGeneratorOctaves2(new Random(randomSeed * 543321L), 2);

			for(int i = 0; i < envCache.length; ++i) {
				envCache[i].x = Integer.MIN_VALUE;
				envCache[i].z = Integer.MIN_VALUE;
			}
		}
	}

	static {
		for(int i = 0; i < envCache.length; ++i) {
			envCache[i] = new Environment();
		}

	}
}
