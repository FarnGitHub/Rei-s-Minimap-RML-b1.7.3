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

	public ChunkCache(int i1) {
		this.shift = i1;
        int size = 1 << this.shift;
		this.mask = size - 1;
		this.cache = new Chunk[size * size];
		this.count = new int[size * size];
		this.slime = new boolean[size * size];
	}

	public Chunk get(World world1, int i2, int i3) {
		int i4 = i2 & this.mask | (i3 & this.mask) << this.shift;
		Chunk chunk = this.cache[i4];
		if(chunk == null || chunk.worldObj != world1 || !chunk.isAtLocation(i2, i3) || --this.count[i4] < 0) {
			if(world1.blockExists(i2 << 4, 0, i3 << 4)) {
				this.cache[i4] = chunk = world1.getChunkFromChunkCoords(i2, i3);
				this.count[i4] = threshold;
			} else if(chunk instanceof EmptyChunk && chunk.isAtLocation(i2, i3)) {
				this.count[i4] = 8;
			} else {
				this.cache[i4] = chunk = new EmptyChunk(world1, i2, i3);
				this.count[i4] = 8;
			}

			this.slime[i4] = chunk.func_997_a(987234911L).nextInt(10) == 0;
		}

		return chunk;
	}

	public boolean isSlimeSpawn(int i1, int i2) {
		return this.slime[i1 & this.mask | (i2 & this.mask) << this.shift];
	}

	public void clear() {
		Arrays.fill(this.cache, null);
		Arrays.fill(this.count, 0);
	}
}
