package farn.reifnsk_minimap;

import java.util.Arrays;

import net.minecraft.src.Chunk;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.World;

public class ChunkCache {
	private static final int threshold = 128;
	private final int shift;
    private final int mask;
	private final Chunk[] cache;
	private final int[] count;
	private final boolean[] slime;

	public ChunkCache(int shift) {
		this.shift = shift;
        int size = 1 << this.shift;
		this.mask = size - 1;
		this.cache = new Chunk[size * size];
		this.count = new int[size * size];
		this.slime = new boolean[size * size];
	}

	public Chunk get(World world, int x, int z) {
		int index = x & this.mask | (z & this.mask) << this.shift;
		Chunk chunk = this.cache[index];
		if(chunk == null || chunk.worldObj != world || !chunk.isAtLocation(x, z) || --this.count[index] < 0) {
			if(world.blockExists(x << 4, 0, z << 4)) {
				this.cache[index] = chunk = world.getChunkFromChunkCoords(x, z);
				this.count[index] = threshold;
			} else if(chunk instanceof EmptyChunk && chunk.isAtLocation(x, z)) {
				this.count[index] = 8;
			} else {
				this.cache[index] = chunk = new EmptyChunk(world, x, z);
				this.count[index] = 8;
			}

			this.slime[index] = chunk.func_997_a(987234911L).nextInt(10) == 0;
		}

		return chunk;
	}

	public boolean isSlimeSpawn(int x, int z) {
		return this.slime[x & this.mask | (z & this.mask) << this.shift];
	}

	public void clear() {
		Arrays.fill(this.cache, null);
		Arrays.fill(this.count, 0);
	}
}
