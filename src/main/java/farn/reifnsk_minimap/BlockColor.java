package farn.reifnsk_minimap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public final class BlockColor {
	private static final ArrayList<BlockColor> list = new ArrayList<>();
	private static final BlockColor AIR_BLOCK = instance();
	private static final int BLOCK_NUM = Block.blocksList.length;
	private static final BlockColor[] defaultColor = new BlockColor[BLOCK_NUM * 16 + 1];
	private static final BlockColor[] textureColor = new BlockColor[BLOCK_NUM * 16 + 1];
	private static final BlockColor[] blockColor = new BlockColor[BLOCK_NUM * 16 + 1];
	private static final boolean[] opaqueList = new boolean[BLOCK_NUM];
	private static final boolean[] useMetadata = new boolean[BLOCK_NUM];
	private static boolean mcpatcher = false;
	private static boolean custom_lava = false;
	private static boolean custom_water = false;
	private final int argb;
	public final TintType tintType;
	public final float alpha;
	public final float red;
	public final float green;
	public final float blue;

	static {

		try {
			Class<?> mcpClass = Class.forName("com.pclewis.mcpatcher.MCPatcherUtils");
			Method getBool = mcpClass.getMethod("getBoolean", String.class, String.class, Boolean.TYPE);
			custom_lava = (boolean)getBool.invoke(null, "HD Textures", "customLava", true);
			custom_water = (boolean)getBool.invoke(null, "HD Textures", "customWater", true);
			mcpatcher = true;
		} catch (Exception ignored) {
		}
		System.out.println("[Rei's Minimap] MCPatcher HD Fix: " + (mcpatcher ? "Found" : "Not found"));
		Arrays.fill(defaultColor, AIR_BLOCK);
		setDefaultColor(1, 0, -9934744);
		setDefaultColor(2, 0, -12096451);
		setDefaultColor(3, 0, -8825542);
		setDefaultColor(4, 0, -6974059);
		setDefaultColor(5, 0, -4417438);
		setDefaultColor(6, 0, 1816358162);
		setDefaultColor(6, 1, 1412577569);
		setDefaultColor(6, 2, 1819645267);
		setDefaultColor(7, 0, -13421773);
		setDefaultColor(8, 0, -1960157441);
		setDefaultColor(9, 0, -1960157441);
		setDefaultColor(10, 0, -2530028);
		setDefaultColor(11, 0, -2530028);
		setDefaultColor(12, 0, -2238560);
		setDefaultColor(13, 0, -7766146);
		setDefaultColor(14, 0, -7304324);
		setDefaultColor(15, 0, -7830913);
		setDefaultColor(16, 0, -9145485);
		setDefaultColor(17, 0, -10006222);
		setDefaultColor(17, 1, -13358823);
		setDefaultColor(17, 2, -3620193);
		setDefaultColor(18, 0, -1708107227);
		setDefaultColor(18, 1, -1522906074);
		setDefaultColor(18, 2, -1707912909);
		setDefaultColor(18, 3, -1707912909);
		setDefaultColor(18, 4, -1708107227);
		setDefaultColor(18, 5, -1522906074);
		setDefaultColor(18, 6, -1707912909);
		setDefaultColor(18, 7, -1707912909);
		setDefaultColor(19, 0, -1710770);
		setDefaultColor(20, 0, 1090519039);
		setDefaultColor(21, 0, -9998201);
		setDefaultColor(22, 0, -14858330);
		setDefaultColor(23, 0, -10987432);
		setDefaultColor(24, 0, -2370913);
		setDefaultColor(25, 0, -10206158);
		setDefaultColor(26, 0, -6339259);
		setDefaultColor(26, 1, -6339259);
		setDefaultColor(26, 2, -6339259);
		setDefaultColor(26, 3, -6339259);
		setDefaultColor(26, 4, -6339259);
		setDefaultColor(26, 5, -6339259);
		setDefaultColor(26, 6, -6339259);
		setDefaultColor(26, 7, -6339259);
		setDefaultColor(26, 8, -6397599);
		setDefaultColor(26, 9, -6397599);
		setDefaultColor(26, 10, -6397599);
		setDefaultColor(26, 11, -6397599);
		setDefaultColor(26, 12, -6397599);
		setDefaultColor(26, 13, -6397599);
		setDefaultColor(26, 14, -6397599);
		setDefaultColor(26, 15, -6397599);
		setDefaultColor(27, 0, -528457632);
		setDefaultColor(27, 1, -528457632);
		setDefaultColor(27, 2, -528457632);
		setDefaultColor(27, 3, -528457632);
		setDefaultColor(27, 4, -528457632);
		setDefaultColor(27, 5, -528457632);
		setDefaultColor(27, 6, -528457632);
		setDefaultColor(27, 7, -528457632);
		setDefaultColor(27, 8, -523214752);
		setDefaultColor(27, 9, -523214752);
		setDefaultColor(27, 10, -523214752);
		setDefaultColor(27, 11, -523214752);
		setDefaultColor(27, 12, -523214752);
		setDefaultColor(27, 13, -523214752);
		setDefaultColor(27, 14, -523214752);
		setDefaultColor(27, 15, -523214752);
		setDefaultColor(28, 0, -8952744);
		setDefaultColor(29, 0, -9605779);
		setDefaultColor(29, 1, -7499421);
		setDefaultColor(29, 2, -9804194);
		setDefaultColor(29, 3, -9804194);
		setDefaultColor(29, 4, -9804194);
		setDefaultColor(29, 5, -9804194);
		setDefaultColor(29, 8, -9605779);
		setDefaultColor(29, 9, -7499421);
		setDefaultColor(29, 10, -9804194);
		setDefaultColor(29, 11, -9804194);
		setDefaultColor(29, 12, -9804194);
		setDefaultColor(29, 13, -9804194);
		setDefaultColor(30, 0, 1775884761);
		setDefaultColor(31, 0, 1383747097);
		setDefaultColor(31, 1, -1571782606);
		setDefaultColor(31, 2, 1330675762);
		setDefaultColor(32, 0, 1383747097);
		setDefaultColor(33, 0, -9605779);
		setDefaultColor(33, 1, -6717094);
		setDefaultColor(33, 2, -9804194);
		setDefaultColor(33, 3, -9804194);
		setDefaultColor(33, 4, -9804194);
		setDefaultColor(33, 5, -9804194);
		setDefaultColor(33, 8, -9605779);
		setDefaultColor(33, 9, -6717094);
		setDefaultColor(33, 10, -9804194);
		setDefaultColor(33, 11, -9804194);
		setDefaultColor(33, 12, -9804194);
		setDefaultColor(33, 13, -9804194);
		setDefaultColor(34, 0, -6717094);
		setDefaultColor(34, 1, -6717094);
		setDefaultColor(34, 2, -2137423526);
		setDefaultColor(34, 3, -2137423526);
		setDefaultColor(34, 4, -2137423526);
		setDefaultColor(34, 5, -2137423526);
		setDefaultColor(34, 8, -6717094);
		setDefaultColor(34, 9, -7499421);
		setDefaultColor(34, 10, -2137423526);
		setDefaultColor(34, 11, -2137423526);
		setDefaultColor(34, 12, -2137423526);
		setDefaultColor(34, 13, -2137423526);
		setDefaultColor(35, 0, -2236963);
		setDefaultColor(35, 1, -1475018);
		setDefaultColor(35, 2, -4370744);
		setDefaultColor(35, 3, -9991469);
		setDefaultColor(35, 4, -4082660);
		setDefaultColor(35, 5, -12928209);
		setDefaultColor(35, 6, -2588006);
		setDefaultColor(35, 7, -12434878);
		setDefaultColor(35, 8, -6445916);
		setDefaultColor(35, 9, -14191468);
		setDefaultColor(35, 10, -8374846);
		setDefaultColor(35, 11, -14273895);
		setDefaultColor(35, 12, -11193573);
		setDefaultColor(35, 13, -13153256);
		setDefaultColor(35, 14, -6083544);
		setDefaultColor(35, 15, -15067369);
		setDefaultColor(37, 0, -1057883902);
		setDefaultColor(38, 0, -1057552625);
		setDefaultColor(39, 0, -1064211115);
		setDefaultColor(40, 0, -1063643364);
		setDefaultColor(41, 0, -66723);
		setDefaultColor(42, 0, -1447447);
		setDefaultColor(43, 0, -5723992);
		setDefaultColor(43, 1, -1712721);
		setDefaultColor(43, 2, -7046838);
		setDefaultColor(43, 3, -8224126);
		setDefaultColor(43, 4, -6591135);
		setDefaultColor(43, 5, -8750470);
		setDefaultColor(44, 0, -5723992);
		setDefaultColor(44, 1, -1712721);
		setDefaultColor(44, 2, -7046838);
		setDefaultColor(44, 3, -8224126);
		setDefaultColor(44, 4, -6591135);
		setDefaultColor(44, 5, -8750470);
		setDefaultColor(45, 0, -6591135);
		setDefaultColor(46, 0, -2407398);
		setDefaultColor(47, 0, -4943782);
		setDefaultColor(48, 0, -14727393);
		setDefaultColor(49, 0, -15527395);
		setDefaultColor(50, 0, 1627379712);
		setDefaultColor(51, 0, -4171263);
		setDefaultColor(52, 0, -14262393);
		setDefaultColor(53, 0, -4417438);
		setDefaultColor(54, 0, -7378659);
		setDefaultColor(55, 0, 1827466476);
		setDefaultColor(56, 0, -8287089);
		setDefaultColor(57, 0, -10428192);
		setDefaultColor(58, 0, -8038091);
		setDefaultColor(59, 0, 302029071);
		setDefaultColor(59, 1, 957524751);
		setDefaultColor(59, 2, 1444710667);
		setDefaultColor(59, 3, -1708815608);
		setDefaultColor(59, 4, -835813369);
		setDefaultColor(59, 5, -532579833);
		setDefaultColor(59, 6, -531663353);
		setDefaultColor(59, 7, -531208953);
		setDefaultColor(60, 0, -9221331);
		setDefaultColor(60, 1, -9550295);
		setDefaultColor(60, 2, -9879003);
		setDefaultColor(60, 3, -10207967);
		setDefaultColor(60, 4, -10536675);
		setDefaultColor(60, 5, -10865383);
		setDefaultColor(60, 6, -11194347);
		setDefaultColor(60, 7, -11523055);
		setDefaultColor(60, 8, -11786226);
		setDefaultColor(61, 0, -9145228);
		setDefaultColor(62, 0, -8355712);
		setDefaultColor(63, 0, -1598779307);
		setDefaultColor(64, 0, -1064934094);
		setDefaultColor(65, 0, -2139595212);
		setDefaultColor(66, 0, -8951211);
		setDefaultColor(67, 0, -6381922);
		setDefaultColor(68, 0, -1598779307);
		setDefaultColor(69, 0, -1603709901);
		setDefaultColor(70, 0, -7368817);
		setDefaultColor(71, 0, -1061043775);
		setDefaultColor(72, 0, -4417438);
		setDefaultColor(73, 0, -6981535);
		setDefaultColor(74, 0, -6981535);
		setDefaultColor(75, 0, -2141709038);
		setDefaultColor(76, 0, -2136923117);
		setDefaultColor(77, 0, -2139851660);
		setDefaultColor(78, 0, -1314833);
		setDefaultColor(79, 0, -1619219203);
		setDefaultColor(80, 0, -986896);
		setDefaultColor(81, 0, -15695840);
		setDefaultColor(82, 0, -6380624);
		setDefaultColor(83, 0, -7094428);
		setDefaultColor(84, 0, -9811658);
		setDefaultColor(85, 0, -4417438);
		setDefaultColor(86, 0, -4229867);
		setDefaultColor(87, 0, -9751501);
		setDefaultColor(88, 0, -11255757);
		setDefaultColor(89, 0, -4157626);
		setDefaultColor(90, 0, -9231226);
		setDefaultColor(91, 0, -3893474);
		setDefaultColor(92, 0, -1848115);
		setDefaultColor(93, 0, -6843501);
		setDefaultColor(94, 0, -4156525);
		setDefaultColor(95, 0, -7378659);
		setDefaultColor(96, 0, -8495827);
		setDefaultColor(96, 1, -8495827);
		setDefaultColor(96, 2, -8495827);
		setDefaultColor(96, 3, -8495827);
		setDefaultColor(96, 4, 545152301);
		setDefaultColor(96, 5, 545152301);
		setDefaultColor(96, 6, 545152301);
		setDefaultColor(96, 7, 545152301);
		setDefaultColor(97, 0, -9934744);
		setDefaultColor(97, 1, -6974059);
		setDefaultColor(97, 2, -8750470);
		setDefaultColor(98, 0, -8750470);
		setDefaultColor(98, 1, -9275542);
		setDefaultColor(98, 2, -9013642);
		setDefaultColor(99, 0, -3495048);
		setDefaultColor(99, 1, -7509421);
		setDefaultColor(99, 2, -7509421);
		setDefaultColor(99, 3, -7509421);
		setDefaultColor(99, 4, -7509421);
		setDefaultColor(99, 5, -7509421);
		setDefaultColor(99, 6, -7509421);
		setDefaultColor(99, 7, -7509421);
		setDefaultColor(99, 8, -7509421);
		setDefaultColor(99, 9, -7509421);
		setDefaultColor(99, 10, -3495048);
		setDefaultColor(100, 0, -3495048);
		setDefaultColor(100, 1, -4840156);
		setDefaultColor(100, 2, -4840156);
		setDefaultColor(100, 3, -4840156);
		setDefaultColor(100, 4, -4840156);
		setDefaultColor(100, 5, -4840156);
		setDefaultColor(100, 6, -4840156);
		setDefaultColor(100, 7, -4840156);
		setDefaultColor(100, 8, -4840156);
		setDefaultColor(100, 9, -4840156);
		setDefaultColor(100, 10, -3495048);
		setDefaultColor(101, 0, -2140312470);
		setDefaultColor(102, 0, 1627389951);
		setDefaultColor(103, 0, -6842076);
		setDefaultColor(104, 0, 1073780992);
		setDefaultColor(104, 1, 1209242626);
		setDefaultColor(104, 2, 1344704516);
		setDefaultColor(104, 3, 1480166151);
		setDefaultColor(104, 4, 1615693321);
		setDefaultColor(104, 5, 1751154956);
		setDefaultColor(104, 6, 1886616590);
		setDefaultColor(104, 7, 2022144016);
		setDefaultColor(105, 0, 1073780992);
		setDefaultColor(105, 1, 1209242626);
		setDefaultColor(105, 2, 1344704516);
		setDefaultColor(105, 3, 1480166151);
		setDefaultColor(105, 4, 1615693321);
		setDefaultColor(105, 5, 1751154956);
		setDefaultColor(105, 6, 1886616590);
		setDefaultColor(105, 7, 2022144016);
		setDefaultColor(106, 0, -2145432054);
		setDefaultColor(107, 0, -1061382046);
		setDefaultColor(108, 0, -6591135);
		setDefaultColor(109, 0, -8750470);

		for(int bId = 0; bId < BLOCK_NUM; ++bId) {
			boolean opaque = true;

			for(int meta = 0; opaque && meta < 16; ++meta) {
				BlockColor color = defaultColor[pointer(bId, meta)];
				opaque = color == null || color.alpha != 0.0F && color.alpha != 1.0F;
			}

			opaqueList[bId] = opaque;
		}

	}

	@SuppressWarnings("SameParameterValue")
    private static void calcBlockColor(BlockColor[]... blockColors) {
		Arrays.fill(blockColor, AIR_BLOCK);
		Arrays.fill(useMetadata, false);

		for(int bId = 0; bId < BLOCK_NUM; ++bId) {
			BlockColor[] metaColors = null;
			BlockColor mainColor = null;

            for (BlockColor[] blockColor4 : blockColors) {
                if (blockColor4[bId << 4] != null) {
                    metaColors = blockColor4;
                    mainColor = blockColor4[bId << 4];
                    blockColor[bId << 4] = mainColor;
                    break;
                }
            }

			if(metaColors != null) {
				for(int i8 = 1; i8 < 16; ++i8) {
					int i5 = pointer(bId, i8);
					if(metaColors[i5] != AIR_BLOCK && metaColors[i5] != mainColor) {
						blockColor[i5] = metaColors[i5];
						useMetadata[bId] = true;
					} else {
						blockColor[i5] = mainColor;
					}
				}
			}
		}

	}

	public static void calcBlockColorTD() {
		calcBlockColor(textureColor, defaultColor);
	}

	public static boolean useMetadata(int i0) {
		return useMetadata[i0];
	}

	public static BlockColor getBlockColor(int i0, int i1) {
		return blockColor[pointer(i0, i1)];
	}

	private static BlockColor instance() {
		return instance(16711935, TintType.NONE);
	}

	private static BlockColor instance(int color, TintType tintType1) {
		BlockColor bColor = new BlockColor(color, tintType1);
		int indexInList = list.indexOf(bColor);
		if(indexInList == -1) {
			list.add(bColor);
			return bColor;
		} else {
			return list.get(indexInList);
		}
	}

	private static void setDefaultColor(int block, int meta, int color) {
		TintType tint = TintType.NONE;
		switch(block) {
		case 2:
		case 106:
			tint = TintType.GRASS;
			break;
		case 8:
		case 9:
		case 79:
			tint = TintType.WATER;
			break;
		case 18:
			int leaveMeta = meta & 3;
			if(leaveMeta == 0) {
				tint = TintType.FOLIAGE;
			}

			if(leaveMeta == 1) {
				tint = TintType.PINE;
			}

			if(leaveMeta == 2) {
				tint = TintType.BIRCH;
			}

			if(leaveMeta == 3) {
				tint = TintType.FOLIAGE;
			}
			break;
		case 20:
			tint = TintType.GLASS;
			break;
		case 31:
			if(meta == 1 || meta == 2) {
				tint = TintType.TALL_GRASS;
			}
		}

		defaultColor[pointer(block, meta)] = instance(color, tint);
	}

	public static void textureColorUpdate() {
		Minecraft mc = ReiMinimap.instance.theMinecraft;
		TexturePackList texturePackRepo = mc.texturePackList;
		TexturePackBase texturepack = texturePackRepo.selectedTexturePack;
		HashMap<String, BufferedImage[]> imageMap = new HashMap<>();
		BufferedImage[] vanillaTexture = splitImage(readImage(texturepack, "/terrain.png"));
		imageMap.put(null, vanillaTexture);

		try {
			Field overridesField = ModLoader.class.getDeclaredField("overrides");
			overridesField.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<Integer, Map<String, Integer>> overrides = (Map<Integer, Map<String, Integer>>) overridesField.get(null);
			if(overrides != null) {
				Map<String, Integer> map = overrides.get(0);
				if(map != null) {
                    for (Entry<String, Integer> entry : map.entrySet()) {
                        vanillaTexture[entry.getValue()] = readImage(texturepack, entry.getKey());
                    }
				}
			}
		} catch (Exception ignored) {
		}

		Arrays.fill(textureColor, AIR_BLOCK);
		TempBlockAccess acess = new TempBlockAccess();
		for(int blockId = 0; blockId < BLOCK_NUM; ++blockId) {
			Block block = Block.blocksList[blockId];
			if(block != null) {
				acess.blockId = blockId;
				String texName = getBlockTexture(block);
				BufferedImage[] images = imageMap.get(texName);
				if(images == null) {
					images = splitImage(readImage(texturepack, texName));
					imageMap.put(texName, images);
				}

				int renderType = block.getRenderType();

				for(int meta = 0; meta < 16; ++meta) {
					try {
						boolean redstoneTorch = block instanceof BlockRedstoneTorch;
						int tex = block.getBlockTextureFromSideAndMetadata(redstoneTorch ? 0 : 1, meta);
						if(blockId == 18) {
							tex &= -2;
						}

						acess.meta = meta;
						block.setBlockBoundsBasedOnState(acess, 0, 0, 0);
						double minX = block.minX;
						double minZ = block.minZ;
						double maxX = block.maxX;
						double maxZ = block.maxZ;
						switch(renderType) {
							case 1:
								int color = calcColorInt(images[tex], minX, minZ, maxX, maxZ);
								if((color & 0xFF000000) != 0) {
									int alpha = Math.max(color >>> 24, 48) << 24;
									setTextureColor(blockId, meta, color & 0xFFFFFF | alpha);
								}
								break;
							case 2:
								int color1 = calcColorInt(images[tex], 0.4375D, 0.4375D, 0.5625D, 0.5625D);
								int color2 = calcColorInt(images[tex], 0.375D, 0.375D, 0.625D, 0.625D);
								int alpha1 = color1 >> 24 & 255;
								int alpha2 = color2 >> 24 & 255;
								int combined = alpha1 + alpha2;
                                if(combined != 0) {
									int r = ((color1 >> 16 & 255) * alpha1 + (color2 >> 16 & 255) * alpha2) / combined;
									int g = ((color1 >> 8 & 255) * alpha1 + (color2 >> 8 & 255) * alpha2) / combined;
									int b = ((color1 & 255) * alpha1 + (color2 & 255) * alpha2) / combined;
									setTextureColor(blockId, meta, Integer.MIN_VALUE | r << 16 | g << 8 | b);
									break;
								} else {
									color1 = calcColorInt(images[tex], 0.25D, 0.25D, 0.75D, 0.75D);
									color2 = calcColorInt(images[tex], 0.0D, 0.0D, 1.0D, 1.0D);
									alpha1 = color1 >> 24 & 255;
									alpha2 = color2 >> 24 & 255;
									combined = alpha1 + alpha2;
									if(combined != 0) {
										int r = ((color1 >> 16 & 255) * alpha1 + (color2 >> 16 & 255) * alpha2) / combined;
										int g = ((color1 >> 8 & 255) * alpha1 + (color2 >> 8 & 255) * alpha2) / combined;
										int b = ((color1 & 255) * alpha1 + (color2 & 255) * alpha2) / combined;
										setTextureColor(blockId, meta, Integer.MIN_VALUE | r << 16 | g << 8 | b);
										break;
									}
								}
							case 3:
								setTextureColor(blockId, meta, calcColorInt(images[tex], minX, minZ, maxX, maxZ));
								break;
							case 4:
								String customTex = null;
								BufferedImage image = images[tex];
								if(mcpatcher) {
									if(texName == null) {
										if(custom_water && (blockId == 8 || blockId == 9)) {
											customTex = "/custom_water_still.png";
										}

										if(custom_lava && (blockId == 10 || blockId == 11)) {
											customTex = "/custom_lava_still.png";
										}
									}

									if(customTex != null) {
										InputStream inputStream57 = texturepack.getResourceAsStream(customTex);
										if(inputStream57 != null) {
											try {
												image = ImageIO.read(inputStream57);
											} catch (IOException ignored) {
											} finally {
												try {
													inputStream57.close();
												} catch (IOException ignored2) {
												}

											}
										}
									}

									if(image != images[tex]) {
										images[tex] = image;
									}
								} else {
									if(blockId == 8 || blockId == 9) {
										images[tex] = new BufferedImage(1, 1, 2);
										images[tex].setRGB(0, 0, -1960157441);
									}

									if(blockId == 10 || blockId == 11) {
										images[tex] = new BufferedImage(1, 1, 2);
										images[tex].setRGB(0, 0, -2530028);
									}
								}

								int waterRgb = calcColorInt(image, 0.0D, 0.0D, 1.0D, 1.0D);
								if(blockId == 8 || blockId == 10) {
									int r = waterRgb >> 30 & 255;
									int g = waterRgb >> 16 & 255;
									int b = waterRgb >> 8 & 255;
									int alpha = waterRgb & 255;
									g = (int)((double)g * 0.9D);
									b = (int)((double)b * 0.9D);
									alpha = (int)((double)alpha * 0.9D);
									waterRgb = r << 30 | g << 16 | b << 8 | alpha;
								}

								setTextureColor(blockId, meta, waterRgb);
								break;
							case 5:
								float redstoneStrength = (float)meta / 15.0F;
								int redstoneColor = calcColorInt(images[tex], minX, minZ, maxX, maxZ);
								if((redstoneColor & 0xFF000000) != 0) {
									int r = Math.max(redstoneColor >> 24 & 255, 108);
									int g = (int)((float)(redstoneColor >> 16 & 255) * Math.max(0.3F, redstoneStrength * 0.6F + 0.4F));
									int b = (int)((float)(redstoneColor >> 8 & 255) * Math.max(0.0F, redstoneStrength * redstoneStrength * 0.7F - 0.5F));
									setTextureColor(blockId, meta, r << 24 | g << 16 | b << 8);
								}
								break;
							case 6:
								int cropColor = calcColorInt(images[tex], minX, minZ, maxX, maxZ);
								if((cropColor & 0xFF000000) != 0) {
									int alpha = Math.max(cropColor >>> 24, 32) << 24;
									setTextureColor(blockId, meta, cropColor & 0xFFFFFF | alpha);
								}
								break;
							case 8:
								int ladderColor = calcColorInt(images[tex], minX, minZ, maxX, maxZ);
								if((ladderColor & 0xFF000000) != 0) {
									int alpha = Math.min(ladderColor >>> 24, 40) << 24;
									setTextureColor(blockId, meta, ladderColor & 0xFFFFFF | alpha);
								}
								break;
							case 11:
								int fenceColor = calcColorInt(images[tex], minX, minZ, maxX, maxZ);
								if((fenceColor & 0xFF000000) != 0) {
									int rgb = Math.min(fenceColor >>> 24, 96) << 24;
									setTextureColor(blockId, meta, fenceColor & 0xFFFFFF | rgb);
								}
								break;
							case 16:
								if(meta >= 10 && meta <= 13) {
									setTextureColor(blockId, meta, calcColorInt(images[tex], 0.0D, 0.25D, 1.0D, 1.0D));
									break;
								}

								setTextureColor(blockId, meta, calcColorInt(images[tex], minX, minZ, maxX, maxZ));
								break;
							case 17:
								if((meta & 7) != 0 && (meta & 7) != 1) {
									setTextureColor(blockId, meta, calcColorInt(images[tex], 0.0D, 0.0D, 1.0D, 0.25D));
									break;
								}

								setTextureColor(blockId, meta, calcColorInt(images[tex], minX, minZ, maxX, maxZ));
								break;
							default:
								setTextureColor(blockId, meta, calcColorInt(images[tex], minX, minZ, maxX, maxZ));
						}
					} catch (Exception ignored) {
					}
				}
			}
		}

		Arrays.fill(useMetadata, false);

		for(int i45 = 0; i45 < BLOCK_NUM; ++i45) {
			BlockColor blockColor47 = textureColor[pointer(i45, 0)];
			if(blockColor47 != null) {
				boolean z49 = false;

				for(int i51 = 1; !z49 && i51 < 16; ++i51) {
					z49 = !blockColor47.equals(textureColor[pointer(i45, i51)]);
				}

				useMetadata[i45] = z49;
			}
		}

	}

	private static void setTextureColor(int block, int meta, int opaquaness) {
		if(opaqueList[block]) {
			opaquaness |= 0xFF000000;
		}

		if((opaquaness & 0xFF000000) == 0) {
			textureColor[pointer(block, meta)] = AIR_BLOCK;
		} else {
			TintType tint = TintType.NONE;
			int i4;
			switch(block) {
			case 2:
			case 106:
				tint = TintType.GRASS;
				break;
			case 8:
			case 9:
			case 79:
				tint = TintType.WATER;
				break;
			case 18:
				i4 = meta & 3;
				if(i4 == 0) {
					tint = TintType.FOLIAGE;
				}

				if(i4 == 1) {
					tint = TintType.PINE;
				}

				if(i4 == 2) {
					tint = TintType.BIRCH;
				}

				if(i4 == 3) {
					tint = TintType.FOLIAGE;
				}
				break;
			case 20:
				tint = TintType.GLASS;
				break;
			case 31:
				if(meta == 1 || meta == 2) {
					tint = TintType.TALL_GRASS;
				}
			}

			if("ic2.common.BlockRubLeaves".equals(Block.blocksList[block].getClass().getCanonicalName())) {
				tint = TintType.BIRCH;
			}

			if("eloraam.world.BlockCustomLeaves".equals(Block.blocksList[block].getClass().getCanonicalName())) {
				i4 = meta & 3;
				if(i4 == 0) {
					tint = TintType.FOLIAGE;
				}

				if(i4 == 1) {
					tint = TintType.PINE;
				}

				if(i4 == 2) {
					tint = TintType.BIRCH;
				}

				if(i4 == 3) {
					tint = TintType.FOLIAGE;
				}
			}

			textureColor[pointer(block, meta)] = instance(opaquaness, tint);
		}
	}

	private static int pointer(int i0, int i1) {
		return i0 << 4 | i1;
	}

	private static BufferedImage readImage(TexturePackBase texturePackBase0, String string1) {
		InputStream inputStream2 = null;

		label87: {
			BufferedImage bufferedImage4;
			try {
				inputStream2 = texturePackBase0.getResourceAsStream(string1);
				if(inputStream2 == null) {
					break label87;
				}

				bufferedImage4 = ImageIO.read(inputStream2);
			} catch (IOException iOException12) {
				break label87;
			} finally {
				if(inputStream2 != null) {
					try {
						inputStream2.close();
					} catch (IOException ignored) {
					}
				}

			}

			return bufferedImage4;
		}

		BufferedImage bufferedImage3 = new BufferedImage(1, 1, 2);
		bufferedImage3.setRGB(0, 0, 16711935);
		return bufferedImage3;
	}

	private static BufferedImage[] splitImage(BufferedImage bufferedImage0) {
		if(bufferedImage0 == null) {
			bufferedImage0 = new BufferedImage(1, 1, 2);
			bufferedImage0.setRGB(0, 0, 16711935);
			BufferedImage[] bufferedImage9 = new BufferedImage[256];
			Arrays.fill(bufferedImage9, bufferedImage0);
			return bufferedImage9;
		} else {
			int i1 = Math.max(1, bufferedImage0.getWidth() >> 4);
			int i2 = Math.max(1, bufferedImage0.getHeight() >> 4);
			BufferedImage[] bufferedImage3 = new BufferedImage[256];

			for(int i4 = 0; i4 < 256; ++i4) {
				bufferedImage3[i4] = GLTextureBufferedImage.create(i1, i2);
				int i5 = (i4 & 15) * bufferedImage0.getWidth() >> 4;
				int i6 = (i4 >> 4) * bufferedImage0.getHeight() >> 4;

				for(int i7 = 0; i7 < i2; ++i7) {
					for(int i8 = 0; i8 < i1; ++i8) {
						bufferedImage3[i4].setRGB(i8, i7, bufferedImage0.getRGB(i8 + i5, i7 + i6));
					}
				}
			}

			return bufferedImage3;
		}
	}

	private static int calcColorInt(BufferedImage bufferedImage0, double d1, double d3, double d5, double d7) {
		int i9 = (int)Math.floor((double)bufferedImage0.getWidth() * d1);
		int i10 = (int)Math.floor((double)bufferedImage0.getHeight() * d3);
		int i11 = (int)Math.floor((double)bufferedImage0.getWidth() * d5);
		int i12 = (int)Math.floor((double)bufferedImage0.getHeight() * d7);
		long j13 = 0L;
		long j15 = 0L;
		long j17 = 0L;
		long j19 = 0L;

		int i21;
		for(i21 = i10; i21 < i12; ++i21) {
			for(int i22 = i9; i22 < i11; ++i22) {
				int i23 = bufferedImage0.getRGB(i22, i21);
				int i24 = i23 >> 24 & 255;
				j13 += i24;
				j15 += (i23 >> 16 & 255) * i24;
				j17 += (i23 >> 8 & 255) * i24;
				j19 += (i23 & 255) * i24;
			}
		}

		if(j13 == 0L) {
			return 16711935;
		} else {
			i21 = bufferedImage0.getWidth() * bufferedImage0.getHeight();
			double d25 = 1.0D / (double)j13;
			j13 /= i21;
			j15 = Math.min(255, Math.max(0, (int)((double)j15 * d25)));
			j17 = Math.min(255, Math.max(0, (int)((double)j17 * d25)));
			j19 = Math.min(255, Math.max(0, (int)((double)j19 * d25)));
			return (int)(j13 << 24 | j15 << 16 | j17 << 8 | j19);
		}
	}

	private BlockColor(int i1, TintType tintType2) {
		if(tintType2 == null) {
			tintType2 = TintType.NONE;
		}

		float f3 = (float)(i1 >> 24 & 255) * 0.003921569F;
		float f4 = (float)(i1 >> 16 & 255) * 0.003921569F;
		float f5 = (float)(i1 >> 8 & 255) * 0.003921569F;
		float f6 = (float)(i1 & 255) * 0.003921569F;
		this.alpha = f3;
		this.red = f4;
		this.green = f5;
		this.blue = f6;
		this.argb = i1;
		this.tintType = tintType2;
	}

	public String toString() {
		return String.format("%08X:%s", this.argb, this.tintType);
	}

	public int hashCode() {
		return this.argb;
	}

	public boolean equals(Object object1) {
		return object1 instanceof BlockColor && this.equals((BlockColor)object1);
	}

	boolean equals(BlockColor blockColor1) {
		return this.argb == blockColor1.argb && this.tintType == blockColor1.tintType;
	}

	private static String getBlockTexture(Block block0) {
		Method[] method4;
		int i3 = (method4 = block0.getClass().getMethods()).length;
		int i2 = 0;

		while(true) {
			if(i2 < i3) {
				Method method1 = method4[i2];
				if(method1.getReturnType() != String.class || method1.getParameterTypes().length != 0 || !method1.getName().equals("getTextureFile")) {
					++i2;
					continue;
				}

				try {
					return (String)method1.invoke(block0, new Object[0]);
				} catch (Exception ignored) {
				}
			}

			return null;
		}
	}

	static class TempBlockAccess implements IBlockAccess {
		private int blockId;
		private int meta;

		private TempBlockAccess() {
		}

		public int getBlockId(int i1, int i2, int i3) {
			return this.blockId;
		}

		public TileEntity getBlockTileEntity(int i1, int i2, int i3) {
			return null;
		}

		public float getBrightness(int i1, int i2, int i3, int i4) {
			return 1.0F;
		}

		public float getLightBrightness(int i1, int i2, int i3) {
			return 1.0F;
		}

		public int getBlockMetadata(int i1, int i2, int i3) {
			return this.meta;
		}

		public Material getBlockMaterial(int i1, int i2, int i3) {
			Block block = getBlock();
			return block != null ? block.blockMaterial : Material.air;
		}

		public boolean isBlockOpaqueCube(int i1, int i2, int i3) {
			Block block = getBlock();
			return block != null && block.isOpaqueCube();
		}

		public boolean isBlockNormalCube(int i1, int i2, int i3) {
			Block block = getBlock();
			return block != null && block.blockMaterial.getIsTranslucent() && block.renderAsNormalBlock();
		}

		public WorldChunkManager getWorldChunkManager() {
			return null;
		}

		Block getBlock() {
			return Block.blocksList[this.blockId];
		}
	}
}
