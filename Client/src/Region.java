

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Contains methods to setup and render the current region
 * 	and the objects it contains.
 * @author Allen
 */
@SuppressWarnings("all")
final class Region {

	public int underlay_floor_texture;
	public int underlay_floor_map_color;
    public Region(byte abyte0[][][], int ai[][][])
    {
        highestPlane = 99;
        regionSizeX = 104;
        regionSizeY = 104;
        heightMap = ai;
        tileSettings = abyte0;
        underLay = new byte[4][regionSizeX][regionSizeY];
        overLay = new byte[4][regionSizeX][regionSizeY];
        overlayClippingPaths = new byte[4][regionSizeX][regionSizeY];
        overlayClippingPathRotations = new byte[4][regionSizeX][regionSizeY];
        tileCullingBitsets = new int[4][regionSizeX + 1][regionSizeY + 1];
        tileShadowIntensity = new byte[4][regionSizeX + 1][regionSizeY + 1];
        tileShadowArray = new int[regionSizeX + 1][regionSizeY + 1];
        blendedHue = new int[regionSizeY];
        blendedSaturation = new int[regionSizeY];
        blendedLightness = new int[regionSizeY];
        blendedHighAdvisor = new int[regionSizeY];
        blendedDirectionTracker = new int[regionSizeY];
    }

    private static int calculateNoise(int x, int seed)
    {
        int k = x + seed * 57;
        k = k << 13 ^ k;
        int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return l >> 19 & 0xff;
    }
    public final void createRegionScene(CollisionDetection collisionMaps[], WorldController worldController) {
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 104; x++) {
				for (int y = 0; y < 104; y++)
					if ((tileSettings[plane][x][y] & 1) == 1) {
						int originalPlane = plane;
						if ((tileSettings[1][x][y] & 2) == 2)
							originalPlane--;
						if (originalPlane >= 0)
							collisionMaps[originalPlane].appendSolidFlag(y, x);
					}


			}


		}
		hueRandomizer += (int) (Math.random() * 5D) - 2;
		if (hueRandomizer < -8)
			hueRandomizer = -8;
		if (hueRandomizer > 8)
			hueRandomizer = 8;
		offsetLightning += (int) (Math.random() * 5D) - 2;
		if (offsetLightning < -16)
			offsetLightning = -16;
		if (offsetLightning > 16)
			offsetLightning = 16;
		for (int plane = 0; plane < 4; plane++) {
			byte shadowIntensity[][] = tileShadowIntensity[plane];
			byte directionalLightInitialIntensity = 96;
			char specularDistributionFactor = /*768*/'\u0300';
			byte directionalLightX = -50;
			byte directionalLightY = -10;
			byte directionalLightZ = -50;
			int directionalLightLength = (int) Math.sqrt(directionalLightX * directionalLightX + directionalLightY * directionalLightY + directionalLightZ
					* directionalLightZ);
			int specularDistribution = specularDistributionFactor * directionalLightLength >> 8;
		for (int y = 1; y < regionSizeY - 1; y++) {
			for (int x = 1; x < regionSizeX - 1; x++) {
				int xHeightDifference = heightMap[plane][x + 1][y] - heightMap[plane][x - 1][y];
				int yHeightDifference = heightMap[plane][x][y + 1] - heightMap[plane][x][y - 1];
				int normalizedLength = (int) Math.sqrt(xHeightDifference * xHeightDifference + 0x10000 + yHeightDifference * yHeightDifference);
				int normalizedNormalX = (xHeightDifference << 8) / normalizedLength;
				int normalizedNormalY = 0x10000 / normalizedLength;
				int normalizedNormalZ = (yHeightDifference << 8) / normalizedLength;
				int directionalLightIntensity = directionalLightInitialIntensity + 
						(directionalLightX * normalizedNormalX + directionalLightY * normalizedNormalY + directionalLightZ * normalizedNormalZ) / specularDistribution;
				int weightedShadowIntensity = (shadowIntensity[x - 1][y] >> 2)
						+ (shadowIntensity[x + 1][y] >> 3)
						+ (shadowIntensity[x][y - 1] >> 2)
						+ (shadowIntensity[x][y + 1] >> 3) + (shadowIntensity[x][y] >> 1);
				tileShadowArray[x][y] = directionalLightIntensity - weightedShadowIntensity;
			}


		}


		for (int y = 0; y < regionSizeY; y++) {
			blendedHue[y] = 0;
			blendedSaturation[y] = 0;
			blendedLightness[y] = 0;
			blendedHighAdvisor[y] = 0;
			blendedDirectionTracker[y] = 0;
		}


		for (int x = -5; x < regionSizeX + 5; x++) {
			for (int y = 0; y < regionSizeY; y++) {
				int xPositiveOffset = x + 5;
				if (xPositiveOffset >= 0 && xPositiveOffset < regionSizeX) {
					int floorId = underLay[plane][xPositiveOffset][y] & 0xff;
					if (floorId > 0) {
						if(floorId >= Flo.cache.length)
							floorId = Flo.cache.length;
						Flo flo = Flo.cache[floorId - 1];
						blendedHue[y] += flo.hue2;
						blendedSaturation[y] += flo.saturation;
						blendedLightness[y] += flo.lightness;
						blendedHighAdvisor[y] += flo.hue_weight;
						blendedDirectionTracker[y]++;
					}
				}
				int xNegativeOffset = x - 5;
				if (xNegativeOffset >= 0 && xNegativeOffset < regionSizeX) {
					int floorId = underLay[plane][xNegativeOffset][y] & 0xff;
					if (floorId > 0) {
						if(floorId >= Flo.cache.length)
							floorId = Flo.cache.length;
						Flo flo_1 = Flo.cache[floorId - 1];
						blendedHue[y] -= flo_1.hue2;
						blendedSaturation[y] -= flo_1.saturation;
						blendedLightness[y] -= flo_1.lightness;
						blendedHighAdvisor[y] -= flo_1.hue_weight;
						blendedDirectionTracker[y]--;
					}
				}
			}


			if (x >= 1 && x < regionSizeX - 1) {
				int blended_hue = 0;
				int blended_saturation = 0;
				int blended_lightness = 0;
				int blended_high_advisor = 0;
				int blend_direction_tracker = 0;
				for (int y = -5; y < regionSizeY + 5; y++) {
					int yPositiveOffset = y + 5;
					if (yPositiveOffset >= 0 && yPositiveOffset < regionSizeY) {
						blended_hue += blendedHue[yPositiveOffset];
						blended_saturation += blendedSaturation[yPositiveOffset];
						blended_lightness += blendedLightness[yPositiveOffset];
						blended_high_advisor += blendedHighAdvisor[yPositiveOffset];
						blend_direction_tracker += blendedDirectionTracker[yPositiveOffset];
					}
					int yNegativeOffset = y - 5;
					if (yNegativeOffset >= 0 && yNegativeOffset < regionSizeY) {
						blended_hue -= blendedHue[yNegativeOffset];
						blended_saturation -= blendedSaturation[yNegativeOffset];
						blended_lightness -= blendedLightness[yNegativeOffset];
						blended_high_advisor -= blendedHighAdvisor[yNegativeOffset];
						blend_direction_tracker -= blendedDirectionTracker[yNegativeOffset];
					}
					if (y >= 1 && y < regionSizeY - 1 && (!lowMem
						|| (tileSettings[0][x][y] & 2) != 0 || (tileSettings[plane][x][y] & 0x10) == 0
						&& getVisibilityPlaneFor(y, plane, x) == onBuildTimePlane)) {
						if (plane < highestPlane)
							highestPlane = plane;
						int underlay_floor_id = underLay[plane][x][y] & 0xff;
						int overlay_floor_id = overLay[plane][x][y] & 0xff;
						if (underlay_floor_id > 0 || overlay_floor_id > 0) {
							int vertexSouthWest = heightMap[plane][x][y];
							int vertexSouthEast = heightMap[plane][x + 1][y];
							int vertexNorthEast = heightMap[plane][x + 1][y + 1];
							int vertexNorthWest = heightMap[plane][x][y + 1];
							int lightSouthWest = tileShadowArray[x][y];
							int lightSouthEast = tileShadowArray[x + 1][y];
							int lightNorthEast = tileShadowArray[x + 1][y + 1];
							int lightNorthWest = tileShadowArray[x][y + 1];
							int hsl_bitset_unmodified = -1;
							int hsl_bitset_randomized = -1;
							if (underlay_floor_id > 0) {
								int hue = (blended_hue * 256) / blended_high_advisor;
								int saturation = blended_saturation / blend_direction_tracker;
								int lightness = blended_lightness / blend_direction_tracker;
								hsl_bitset_unmodified = getHSLBitset(hue, saturation, lightness);
								hue = hue + hueRandomizer & 0xff;
								lightness += offsetLightning;
								if (lightness < 0)
									lightness = 0;
								else if (lightness > 255)
									lightness = 255;
								hsl_bitset_randomized = getHSLBitset(hue, saturation, lightness);
							}
							if(underlay_floor_id > 0  || overlay_floor_id != 0) {
								int hue = -1;
								int saturation = 0;
								int lightness = 0;
								if(underlay_floor_id == 0) 
								{
									hue = -1;
									saturation = 0;
									lightness = 0;
								} else
								if (underlay_floor_id > 0) {
									if(blended_high_advisor < 1)
										blended_high_advisor = 1;
										
									hue = (blended_hue << 8) / blended_high_advisor;
									saturation = blended_saturation / blend_direction_tracker;
									lightness = blended_lightness / blend_direction_tracker;
									hsl_bitset_unmodified = getHSLBitset(hue, saturation, lightness);
										hue = hue + hueRandomizer & 0xff;
										lightness += offsetLightning;
										if (lightness < 0)
											lightness = 0;
											
										else if (lightness > 255)
											lightness = 255;
									
								} else {
									hue = underlay_floor_id;
									saturation = 0;
									lightness = 0;
								}
								if(hue != -1 && hsl_bitset_randomized == -1)
									hsl_bitset_randomized = getHSLBitset(hue, saturation, lightness);
									
								if(hsl_bitset_unmodified == -1)
									hsl_bitset_unmodified = hsl_bitset_randomized;
								
							}
							if (plane > 0) {
								boolean hideOverlay = true;
								if (underlay_floor_id == 0 && overlayClippingPaths[plane][x][y] != 0)
									hideOverlay = false;
								if(overlay_floor_id >= OverLayFlo317.cache.length)
										overlay_floor_id = OverLayFlo317.cache.length-1;
								if (overlay_floor_id > 0 && !OverLayFlo317.cache[overlay_floor_id - 1].occludeOverlay)
									hideOverlay = false;
								if (hideOverlay && vertexSouthWest == vertexSouthEast && vertexSouthWest == vertexNorthEast && vertexSouthWest == vertexNorthWest)
									tileCullingBitsets[plane][x][y] |= 0x924;
							}
							int rgb_bitset_randomized = 0;
							if (hsl_bitset_unmodified != -1)
								rgb_bitset_randomized = Rasterizer.hsl2rgb[adjustHSLLightness(hsl_bitset_randomized,
										96)];
							if (overlay_floor_id == 0) {
								/**
								 * Draws shaped tile
								 */
								if(Client.getOption("hd_tex")) {

									if(underlay_floor_id-1 >= Flo.cache.length)
										underlay_floor_id = Flo.cache.length-1;
									Flo floor = Flo.cache[underlay_floor_id - 1];
									int underlay_texture_id = floor.texture;
									//System.out.println("Coord: " + x + ", " + y + " TEX ID:: " + floor.texture);
									if (underlay_texture_id != -1)
										underlay_texture_id = Client.instance.underlay_id; //632 593 504
									underlay_floor_texture = underlay_texture_id;
									underlay_floor_map_color = getOverlayShadow(hsl_bitset_unmodified, 96);
									int tile_opcode = overlayClippingPaths[plane][x][y] + 1;
									byte tile_orientation = overlayClippingPathRotations[plane][x][y];
									/**
									 * Adds underlay tile
									 */
									int overlay_hsl = getHSLBitset(floor.hue, floor.saturation, floor.lightness);
									worldController.addTile(plane, x, y, 
										tile_opcode, tile_orientation, underlay_texture_id, vertexSouthWest, vertexSouthEast, vertexNorthEast, vertexNorthWest, 
										adjustHSLLightness(hsl_bitset_unmodified, lightSouthWest), 
										adjustHSLLightness(hsl_bitset_unmodified, lightSouthEast),
										adjustHSLLightness(hsl_bitset_unmodified, lightNorthEast), 
										adjustHSLLightness(hsl_bitset_unmodified, lightNorthWest), 
										getOverlayShadow(overlay_hsl, lightSouthWest), 
										getOverlayShadow(overlay_hsl, lightSouthEast),
										getOverlayShadow(overlay_hsl, lightNorthEast), 
										getOverlayShadow(overlay_hsl, lightNorthWest),
										rgb_bitset_randomized, rgb_bitset_randomized, underlay_floor_map_color, 
										underlay_floor_texture, underlay_floor_map_color);
								} else {
									worldController.addTile(plane, x, y, 0, 0, -1, vertexSouthWest, vertexSouthEast, vertexNorthEast, vertexNorthWest, 
											adjustHSLLightness(hsl_bitset_unmodified, lightSouthWest), 
											adjustHSLLightness(hsl_bitset_unmodified,lightSouthEast), 
											adjustHSLLightness(hsl_bitset_unmodified, lightNorthEast),
											adjustHSLLightness(hsl_bitset_unmodified, lightNorthWest), 
											0, 
											0, 
											0, 
											0,
											rgb_bitset_randomized, rgb_bitset_randomized, -1, 0, 0);
								}
							} else {
								int overlay_map_color = 0;
								int overlay_floor_texture_color = -1;
								int tile_opcode = overlayClippingPaths[plane][x][y] + 1;
								byte tile_orientation = overlayClippingPathRotations[plane][x][y];
								if(overlay_floor_id-1 >= OverLayFlo317.cache.length)
									overlay_floor_id = OverLayFlo317.cache.length - 1;
								OverLayFlo317 over = OverLayFlo317.cache[overlay_floor_id - 1];
                                int overlay_texture_id = over.groundTextureOverlay;
                                //System.out.println("Coords: " + x + ", " + y + " TEXID: " + overlay_texture_id);
                                int overlay_hsl;
                                int overlay_rgb = 0;
								if (overlay_texture_id > 50 && !Client.getOption("hd_tex")) {
									overlay_texture_id = -1;
								}
								if (overlay_texture_id >= 0) {
									if(overlay_texture_id < 51 || !Client.getOption("hd_tex"))
										overlay_rgb = TextureLoader317.getAverageTextureColour(overlay_texture_id);
                                    overlay_hsl = -1;
                                } else if (over.groundColorOverlay == 0xff00ff) {
                                    overlay_rgb = 0;
                                    overlay_hsl = -2;
                                    overlay_texture_id = -1;
                                } else if (over.groundColorOverlay == 0x00171C) {
                                	over.groundColorOverlay = 0xA5A599;
                                	overlay_rgb = Rasterizer.hsl2rgb[getOverlayShadow(over.hslOverlayColor, 96)]; 
                                	overlay_hsl = -2;
                                } else if(over.groundColorOverlay == 0x333333) {
                                    overlay_rgb = Rasterizer.hsl2rgb[getOverlayShadow(over.hslOverlayColor, 96)];                                
                                    overlay_hsl = -2;
                                    overlay_texture_id = -1;
                                } else {
                                    overlay_hsl = getHSLBitset(over.groundHueOverlay, over.groundSaturationOverlay, over.groundLightnessOverlay);
                                    overlay_rgb = Rasterizer.hsl2rgb[getOverlayShadow(over.hslOverlayColor, 96)];
                                }
								if ((overlay_floor_id-1) == 54) {
				                    overlay_rgb = over.groundColorOverlay = 0x8B8B83;
				                    overlay_hsl = -2;
				                }
								if((overlay_floor_id-1) == 111){
									overlay_rgb = TextureLoader317.getAverageTextureColour(1);
									overlay_hsl = -1;//method177(150,100,100);
                                    overlay_texture_id = 1;
                                } else if (overlay_hsl == 6363) { //river bank (brown shit) 508
									overlay_rgb = 0x483B21;
									overlay_hsl = getHSLBitset(25,146,24);
                                } else if((overlay_floor_id-1) == 54){
									overlay_rgb = over.groundColorOverlay;
									overlay_hsl = -2;
                                    overlay_texture_id = -1;
                                }
								if(Client.getOption("hd_tex")) {
									if(over.detailedColor != -1) 
									{
										overlay_map_color = (Rasterizer.hsl2rgb[over.detailedColor] != 1) ? Rasterizer.hsl2rgb[over.detailedColor] : 0;
									}
									if ((!Client.getOption("hd_tex") ? (overlay_texture_id >= 0 && overlay_texture_id < 51) : (overlay_texture_id >= 0))) 
									{
										overlay_hsl = -1;
										if (over.groundColorOverlay != 0xff00ff) 
										{
											overlay_hsl = over.groundColorOverlay;
											if(overlay_texture_id > 50)
											overlay_rgb = (overlay_hsl != -1 ? Rasterizer.hsl2rgb[overlay_hsl] : 0);
											overlay_floor_texture_color = getOverlayShadow(over.groundColorOverlay, 96);
										} else 
										{
											if(overlay_texture_id > 50)
												overlay_rgb = over.detailedColor;
											overlay_hsl = -2;
											underlay_floor_map_color = -1;
											overlay_floor_texture_color = -1;
										}
									} else if (over.groundColorOverlay == -1) 
									{
										if(overlay_texture_id > 50)
										overlay_rgb = overlay_map_color;
										overlay_hsl = -2;
										//?
										if(plane > 0)
											underlay_floor_texture = -1;
											
										overlay_texture_id = -1;
									} else 
									{
										overlay_floor_texture_color = getOverlayShadow(over.groundColorOverlay, 96);
										overlay_hsl = over.groundColorOverlay;
										if(overlay_texture_id > 50)
											overlay_rgb = Rasterizer.hsl2rgb[overlay_floor_texture_color];
									}
								}
								if(Client.getOption("hd_tex")) {
									worldController.addTile(plane, x, y, 
										tile_opcode, tile_orientation, overlay_texture_id, vertexSouthWest, vertexSouthEast, vertexNorthEast, vertexNorthWest, 
										adjustHSLLightness(hsl_bitset_unmodified, lightSouthWest), adjustHSLLightness(hsl_bitset_unmodified, lightSouthEast),
										adjustHSLLightness(hsl_bitset_unmodified, lightNorthEast),adjustHSLLightness(hsl_bitset_unmodified, lightNorthWest),
										getOverlayShadow(overlay_hsl, lightSouthWest), getOverlayShadow(overlay_hsl, lightSouthEast),
										getOverlayShadow(overlay_hsl, lightNorthEast), getOverlayShadow(overlay_hsl, lightNorthWest),
										rgb_bitset_randomized, overlay_rgb, overlay_floor_texture_color, 
										underlay_floor_texture, underlay_floor_map_color);
								} else {
	                                worldController.addTile(plane, x, y, tile_opcode,
	                                        tile_orientation, overlay_texture_id, vertexSouthWest, vertexSouthEast, vertexNorthEast, vertexNorthWest,
	                                        adjustHSLLightness(hsl_bitset_unmodified, lightSouthWest), adjustHSLLightness(hsl_bitset_unmodified,
	                                                lightSouthEast), adjustHSLLightness(hsl_bitset_unmodified, lightNorthEast),
	                                                adjustHSLLightness(hsl_bitset_unmodified, lightNorthWest), getOverlayShadow(overlay_hsl,
	                                                        lightSouthWest), getOverlayShadow(overlay_hsl, lightSouthEast),
	                                                        getOverlayShadow(overlay_hsl, lightNorthEast), getOverlayShadow(overlay_hsl,
	                                                                lightNorthWest), rgb_bitset_randomized, overlay_rgb, -1, 0 ,0);
									}
	                            }
							}
						}
					}
				}
			}
			for (int y = 1; y < regionSizeY - 1; y++) {
				for (int x = 1; x < regionSizeX - 1; x++)
					worldController.setVisiblePlanesFor(plane, x, y, getVisibilityPlaneFor(y, plane, x));
			}
		}


		worldController.shadeModels(-10, -50, -50);
		for (int j1 = 0; j1 < regionSizeX; j1++) {
			for (int l1 = 0; l1 < regionSizeY; l1++)
				if ((tileSettings[1][j1][l1] & 2) == 2)
					worldController.applyBridgeMode(l1, j1);


		}


		int renderRule1 = 1;
		int renderRule2 = 2;
		int renderRule3 = 4;
		for (int currentPlane = 0; currentPlane < 4; currentPlane++) {
			if (currentPlane > 0) {
				renderRule1 <<= 3;
				renderRule2 <<= 3;
				renderRule3 <<= 3;
			}
			for (int plane = 0; plane <= currentPlane; plane++) {
				for (int y = 0; y <= regionSizeY; y++) {
					for (int x = 0; x <= regionSizeX; x++) {
						if ((tileCullingBitsets[plane][x][y] & renderRule1) != 0) {
							int lowestOcclussionY = y;
							int higestOcclussionY = y;
							int lowestOcclussionPlane = plane;
							int higestOcclussionPlane = plane;
							for (; lowestOcclussionY > 0 && (tileCullingBitsets[plane][x][lowestOcclussionY - 1] & renderRule1) != 0; lowestOcclussionY--);
							for (; higestOcclussionY < regionSizeY && (tileCullingBitsets[plane][x][higestOcclussionY + 1] & renderRule1) != 0; higestOcclussionY++);
							findLowestOcclussionPlane1: for (; lowestOcclussionPlane > 0; lowestOcclussionPlane--) {
								for (int occludedY = lowestOcclussionY; occludedY <= higestOcclussionY; occludedY++)
									if ((tileCullingBitsets[lowestOcclussionPlane - 1][x][occludedY] & renderRule1) == 0)
										break findLowestOcclussionPlane1;
							}
							findHighestOcclussionPlane1: for (; higestOcclussionPlane < currentPlane; higestOcclussionPlane++) {
								for (int occludedY = lowestOcclussionY; occludedY <= higestOcclussionY; occludedY++)
									if ((tileCullingBitsets[higestOcclussionPlane + 1][x][occludedY] & renderRule1) == 0)
										break findHighestOcclussionPlane1;
							}
							int occlussionSurface = ((higestOcclussionPlane + 1) - lowestOcclussionPlane) * ((higestOcclussionY - lowestOcclussionY) + 1);
							if (occlussionSurface >= 8) {
								char highestOcclussionVertexHeightOffset = '\360';
								int highestOcclussionVertexHeight = heightMap[higestOcclussionPlane][x][lowestOcclussionY]
								                                          - highestOcclussionVertexHeightOffset;
								int lowestOcclussionVertexHeight = heightMap[lowestOcclussionPlane][x][lowestOcclussionY];
								WorldController.createCullingCluster(currentPlane, x * 128, lowestOcclussionVertexHeight,
										x * 128, higestOcclussionY * 128 + 128, highestOcclussionVertexHeight,
										lowestOcclussionY * 128, 1);
								for (int occludedPlane = lowestOcclussionPlane; occludedPlane <= higestOcclussionPlane; occludedPlane++) {
									for (int occludedY = lowestOcclussionY; occludedY <= higestOcclussionY; occludedY++)
										tileCullingBitsets[occludedPlane][x][occludedY] &= ~renderRule1;
								}
							}
						}
						if ((tileCullingBitsets[plane][x][y] & renderRule2) != 0) {
							int lowestOcclusionX = x;
							int highestOcclusionX = x;
							int lowestOcclusionPlane = plane;
							int highestOcclusionPlane = plane;
							for (; lowestOcclusionX > 0 && (tileCullingBitsets[plane][lowestOcclusionX - 1][y] & renderRule2) != 0; lowestOcclusionX--);
							for (; highestOcclusionX < regionSizeX && (tileCullingBitsets[plane][highestOcclusionX + 1][y] & renderRule2) != 0; highestOcclusionX++);
							findLowestOcclussionPlane2: for (; lowestOcclusionPlane > 0; lowestOcclusionPlane--) {
								for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++)
									if ((tileCullingBitsets[lowestOcclusionPlane - 1][occludedX][y] & renderRule2) == 0)
										break findLowestOcclussionPlane2;
							}
							findHighestOcclussionPlane2: for (; highestOcclusionPlane < currentPlane; highestOcclusionPlane++) {
								for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++)
									if ((tileCullingBitsets[highestOcclusionPlane + 1][occludedX][y] & renderRule2) == 0)
										break findHighestOcclussionPlane2;
							}
							int occlussionSurface = ((highestOcclusionPlane + 1) - lowestOcclusionPlane) * ((highestOcclusionX - lowestOcclusionX) + 1);
							if (occlussionSurface >= 8) {
								char highestOcclussionVertexHeightOffset = '\360';
								int highestOcclussionVertexHeight = heightMap[highestOcclusionPlane][lowestOcclusionX][y] - highestOcclussionVertexHeightOffset;
								int lowestOcclussionVertexHeight = heightMap[lowestOcclusionPlane][lowestOcclusionX][y];
								WorldController.createCullingCluster(currentPlane, lowestOcclusionX * 128, lowestOcclussionVertexHeight,
										highestOcclusionX * 128 + 128, y * 128, highestOcclussionVertexHeight,
										y * 128, 2);
								for (int occludedPlane = lowestOcclusionPlane; occludedPlane <= highestOcclusionPlane; occludedPlane++) {
									for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++)
										tileCullingBitsets[occludedPlane][occludedX][y] &= ~renderRule2;


								}


							}
						}
						if ((tileCullingBitsets[plane][x][y] & renderRule3) != 0) {
							int lowestOcclusionX = x;
							int highestOcclusionX = x;
							int lowestOcclusionY = y;
							int highestOcclusionY = y;
							for (; lowestOcclusionY > 0 && (tileCullingBitsets[plane][x][lowestOcclusionY - 1] & renderRule3) != 0; lowestOcclusionY--);
							for (; highestOcclusionY < regionSizeY && (tileCullingBitsets[plane][x][highestOcclusionY + 1] & renderRule3) != 0; highestOcclusionY++);
							findLowestOcclusionX3: 
							for (; lowestOcclusionX > 0; lowestOcclusionX--) {
								for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++)
									if ((tileCullingBitsets[plane][lowestOcclusionX - 1][occludedY] & renderRule3) == 0)
										break findLowestOcclusionX3;
							}
							findHighestOcclusionX3: 
							for (; highestOcclusionX < regionSizeX; highestOcclusionX++) {
								for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++)
									if ((tileCullingBitsets[plane][highestOcclusionX + 1][occludedY] & renderRule3) == 0)
										break findHighestOcclusionX3;
							}
							if (((highestOcclusionX - lowestOcclusionX) + 1) * ((highestOcclusionY - lowestOcclusionY) + 1) >= 4) {
								int j12 = heightMap[plane][lowestOcclusionX][lowestOcclusionY];
								WorldController.createCullingCluster(currentPlane, lowestOcclusionX * 128, j12,
										highestOcclusionX * 128 + 128, highestOcclusionY * 128 + 128, j12,
										lowestOcclusionY * 128, 4);
								for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
									for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++)
										tileCullingBitsets[plane][occludedX][occludedY] &= ~renderRule3;
								}
							}
						}
					}
				}
			}
		}
	}
	

    private static int generateMapHeight(int x, int y)
    {
        int mapHeight = (getNoise(x + 45365, y + 0x16713, 4) - 128) + (getNoise(x + 10294, y + 37821, 2) - 128 >> 1) + (getNoise(x, y, 1) - 128 >> 2);
        mapHeight = (int)((double)mapHeight * 0.29999999999999999D) + 35;
        if(mapHeight < 10)
            mapHeight = 10;
        else
        if(mapHeight > 60)
            mapHeight = 60;
        return mapHeight;
    }

    public static void passiveRequestGameObjectModels(Stream stream, OnDemandFetcher fetcher)
    {
    	label0:
        {
            int gameObjectId = -1;
            do
            {
                int gameObjectIfOffset = stream.readUSmart2();
                if(gameObjectIfOffset == 0)
                    break label0;
                gameObjectId += gameObjectIfOffset;
                System.out.println(gameObjectId);
                ObjectDef class46 = ObjectDef.forID(gameObjectId);
                class46.passiveRequestModels(fetcher);
                do
                {
                    int terminate = stream.readSmart();
                    if(terminate == 0)
                        break;
                    stream.readUnsignedByte();
                } while(true);
            } while(true);
        }
    }

    public final void initiateVertexHeights(int yOffset, int yLength, int xLength, int xOffset)
    {
        for(int y = yOffset; y <= yOffset + yLength; y++)
        {
            for(int x = xOffset; x <= xOffset + xLength; x++)
                if(x >= 0 && x < regionSizeX && y >= 0 && y < regionSizeY)
                {
                    tileShadowIntensity[0][x][y] = 127;
                    if(x == xOffset && x > 0)
                        heightMap[0][x][y] = heightMap[0][x - 1][y];
                    if(x == xOffset + xLength && x < regionSizeX - 1)
                        heightMap[0][x][y] = heightMap[0][x + 1][y];
                    if(y == yOffset && y > 0)
                        heightMap[0][x][y] = heightMap[0][x][y - 1];
                    if(y == yOffset + yLength && y < regionSizeY - 1)
                        heightMap[0][x][y] = heightMap[0][x][y + 1];
                }

        }
    }
	
	public static void write(String text, String file) {
		BufferedWriter bw = null;
		try {
			FileWriter fileWriter = new FileWriter(file + ".txt", true);
			bw = new BufferedWriter(fileWriter);
			bw.write(text);
			bw.newLine();
			bw.flush();
			bw.close();
			fileWriter = null;
			bw = null;
		} catch (Exception exception) {
			System.out.println("Critical error while writing data: " + file);
			exception.printStackTrace();
		}
	}

    public void renderObject(int y, WorldController worldController, CollisionDetection collisionMap, int type, int z, int x, int objectId, int face) {
		try {
    	if (lowMem && (tileSettings[0][x][y] & 2) == 0) {
			if ((tileSettings[z][x][y] & 0x10) != 0)
				return;
			if (getVisibilityPlaneFor(y, z, x) != onBuildTimePlane)
				return;
		}
		if (z < highestPlane)
			highestPlane = z;
		int vertexHeight = heightMap[z][x][y];
		int vertexHeightRight = heightMap[z][x + 1][y];
		int vertexHeightTopRight = heightMap[z][x + 1][y + 1];
		int vertexHeightTop = heightMap[z][x][y + 1];
		int vertexMix = vertexHeight + vertexHeightRight + vertexHeightTopRight + vertexHeightTop >> 2;
		ObjectDef objectDef = ObjectDef.forID(objectId);
		int hash = x + (y << 7) + ((objectId > 0x7fff ? objectId & 0x7fff : objectId) << 14) + 0x40000000;
		if (!objectDef.hasActions)
			hash += 0x80000000;
		byte objectConfig = (byte) ((face << 6) + type);
		Client.instance.calcObjectScreenPos(x, z, y);
		Client.instance.newBoldFont.drawBasicString(String.valueOf(objectId), Client.instance.objectDrawX, Client.instance.objectDrawY);
		if (type == 22) {
			if (lowMem && !objectDef.hasActions && !objectDef.aBoolean736)
				return;
			Object obj;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				obj = objectDef.renderObject(22, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				obj = new ObjectOnTile(objectId, face, 22, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addGroundDecoration(z, vertexMix, y, ((Animable) (obj)), objectConfig, hash, x, objectId);
			if (objectDef.isUnwalkable && objectDef.hasActions && collisionMap != null)
				collisionMap.appendSolidFlag(y, x);
			return;
		}
		if (type == 10 || type == 11) {
			Object obj1;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				obj1 = objectDef.renderObject(10, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				obj1 = new ObjectOnTile(objectId, face, 10, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			if (obj1 != null) {
				int i5 = 0;
				if (type == 11)
					i5 += 256;
				int sizeX;
				int sizeY;
				if (face == 1 || face == 3) {
					sizeX = objectDef.sizeY;
					sizeY = objectDef.sizeX;
				} else {
					sizeX = objectDef.sizeX;
					sizeY = objectDef.sizeY;
				}
				if (worldController.addInteractableEntity(hash, objectConfig, vertexMix, sizeY, ((Animable) (obj1)), sizeX, z, i5, y, x, objectId) && objectDef.aBoolean779) {
					Model model;
					if (obj1 instanceof Model)
						model = (Model) obj1;
					else
						model = objectDef.renderObject(10, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
					if (model != null) {
						for (int sizeXCounter = 0; sizeXCounter <= sizeX; sizeXCounter++) {
							for (int sizeYCounter = 0; sizeYCounter <= sizeY; sizeYCounter++) {
								int shadowIntensity = model.shadowIntensity / 4;
								if (shadowIntensity > 30)
									shadowIntensity = 30;
								if (shadowIntensity > tileShadowIntensity[z][x + sizeXCounter][y + sizeYCounter])
									tileShadowIntensity[z][x + sizeXCounter][y + sizeYCounter] = (byte) shadowIntensity;
							}

						}

					}
				}
			}
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markSolidOccupant(objectDef.walkable, objectDef.sizeX, objectDef.sizeY, x, y, face);
			return;
		}
		if (type >= 12) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(type, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, face, type, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addInteractableEntity(hash, objectConfig, vertexMix, 1, ((Animable) (renderable)), 1, z, 0, y, x, objectId);
			if (type >= 12 && type <= 17 && type != 13 && z > 0)
				tileCullingBitsets[z][x][y] |= 0x924;
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markSolidOccupant(objectDef.walkable, objectDef.sizeX, objectDef.sizeY, x, y, face);
			return;
		}
		if (type == 0) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(0, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, face, 0, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallObject(WALL_ROOT_FLAGS[face], ((Animable) (renderable)), hash, y, objectConfig, x, null, vertexMix, 0, z, objectId);
			if (face == 0) {
				if (objectDef.aBoolean779) {
					tileShadowIntensity[z][x][y] = 50;
					tileShadowIntensity[z][x][y + 1] = 50;
				}
				if (objectDef.aBoolean764)
					tileCullingBitsets[z][x][y] |= 0x249;
			} else if (face == 1) {
				if (objectDef.aBoolean779) {
					tileShadowIntensity[z][x][y + 1] = 50;
					tileShadowIntensity[z][x + 1][y + 1] = 50;
				}
				if (objectDef.aBoolean764)
					tileCullingBitsets[z][x][y + 1] |= 0x492;
			} else if (face == 2) {
				if (objectDef.aBoolean779) {
					tileShadowIntensity[z][x + 1][y] = 50;
					tileShadowIntensity[z][x + 1][y + 1] = 50;
				}
				if (objectDef.aBoolean764)
					tileCullingBitsets[z][x + 1][y] |= 0x249;
			} else if (face == 3) {
				if (objectDef.aBoolean779) {
					tileShadowIntensity[z][x][y] = 50;
					tileShadowIntensity[z][x + 1][y] = 50;
				}
				if (objectDef.aBoolean764)
					tileCullingBitsets[z][x][y] |= 0x492;
			}
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markWall(y, face, x, type, objectDef.walkable);
			if (objectDef.anInt775 != 16)
				worldController.moveWallDec(y, objectDef.anInt775, x, z);
			return;
		}
		if (type == 1) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(1, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, face, 1, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallObject(WALL_EXT_FLAGS[face], ((Animable) (renderable)), hash, y, objectConfig, x, null, vertexMix, 0, z, objectId);
			if (objectDef.aBoolean779)
				if (face == 0)
					tileShadowIntensity[z][x][y + 1] = 50;
				else if (face == 1)
					tileShadowIntensity[z][x + 1][y + 1] = 50;
				else if (face == 2)
					tileShadowIntensity[z][x + 1][y] = 50;
				else if (face == 3)
					tileShadowIntensity[z][x][y] = 50;
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markWall(y, face, x, type, objectDef.walkable);
			return;
		}
		if (type == 2) {
			int i3 = face + 1 & 3;
			Object obj11;
			Object obj12;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null) {
				obj11 = objectDef.renderObject(2, 4 + face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
				obj12 = objectDef.renderObject(2, i3, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			} else {
				obj11 = new ObjectOnTile(objectId, 4 + face, 2, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
				obj12 = new ObjectOnTile(objectId, i3, 2, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			}
			worldController.addWallObject(WALL_ROOT_FLAGS[face], ((Animable) (obj11)), hash, y, objectConfig, x, ((Animable) (obj12)), vertexMix, WALL_ROOT_FLAGS[i3], z, objectId);
			if (objectDef.aBoolean764)
				if (face == 0) {
					tileCullingBitsets[z][x][y] |= 0x249;
					tileCullingBitsets[z][x][y + 1] |= 0x492;
				} else if (face == 1) {
					tileCullingBitsets[z][x][y + 1] |= 0x492;
					tileCullingBitsets[z][x + 1][y] |= 0x249;
				} else if (face == 2) {
					tileCullingBitsets[z][x + 1][y] |= 0x249;
					tileCullingBitsets[z][x][y] |= 0x492;
				} else if (face == 3) {
					tileCullingBitsets[z][x][y] |= 0x492;
					tileCullingBitsets[z][x][y] |= 0x249;
				}
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markWall(y, face, x, type, objectDef.walkable);
			if (objectDef.anInt775 != 16)
				worldController.moveWallDec(y, objectDef.anInt775, x, z);
			return;
		}
		if (type == 3) {
			Object obj5;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				obj5 = objectDef.renderObject(3, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				obj5 = new ObjectOnTile(objectId, face, 3, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallObject(WALL_EXT_FLAGS[face], ((Animable) (obj5)), hash, y, objectConfig, x, null, vertexMix, 0, z, objectId);
			if (objectDef.aBoolean779)
				if (face == 0)
					tileShadowIntensity[z][x][y + 1] = 50;
				else if (face == 1)
					tileShadowIntensity[z][x + 1][y + 1] = 50;
				else if (face == 2)
					tileShadowIntensity[z][x + 1][y] = 50;
				else if (face == 3)
					tileShadowIntensity[z][x][y] = 50;
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markWall(y, face, x, type, objectDef.walkable);
			return;
		}
		if (type == 9) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(type, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, face, type, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addInteractableEntity(hash, objectConfig, vertexMix, 1, ((Animable) (renderable)), 1, z, 0, y, x, objectId);
			if (objectDef.isUnwalkable && collisionMap != null)
				collisionMap.markSolidOccupant(objectDef.walkable, objectDef.sizeX, objectDef.sizeY, x, y, face);
			return;
		}
		if (objectDef.adjustToTerrain)
			if (face == 1) {
				int j3 = vertexHeightTop;
				vertexHeightTop = vertexHeightTopRight;
				vertexHeightTopRight = vertexHeightRight;
				vertexHeightRight = vertexHeight;
				vertexHeight = j3;
			} else if (face == 2) {
				int k3 = vertexHeightTop;
				vertexHeightTop = vertexHeightRight;
				vertexHeightRight = k3;
				k3 = vertexHeightTopRight;
				vertexHeightTopRight = vertexHeight;
				vertexHeight = k3;
			} else if (face == 3) {
				int l3 = vertexHeightTop;
				vertexHeightTop = vertexHeight;
				vertexHeight = vertexHeightRight;
				vertexHeightRight = vertexHeightTopRight;
				vertexHeightTopRight = l3;
			}
		if (type == 4) {
			Object obj7;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				obj7 = objectDef.renderObject(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				obj7 = new ObjectOnTile(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallDecoration(hash, y, face * 512, z, 0, vertexMix, ((Animable) (obj7)), x, objectConfig, 0, WALL_ROOT_FLAGS[face], objectId);
			return;
		}
		if (type == 5) {
			int offset = 16;
			int wallHash = worldController.getWallObjectUID(z, x, y);
			if (wallHash > 0)
				offset = ObjectDef.forID(wallHash >> 14 & 0x7fff).anInt775;
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallDecoration(hash, y, face * 512, z, ROTATE_X_DELTA[face] * offset, vertexMix, ((Animable) (renderable)), x, objectConfig, ROTOATE_Y_DELTA[face]  * offset, WALL_ROOT_FLAGS[face], objectId);
			return;
		}
		if (type == 6) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallDecoration(hash, y, face, z, 0, vertexMix, ((Animable) (renderable)), x, objectConfig, 0, 256, objectId);
			return;
		}
		if (type == 7) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallDecoration(hash, y, face, z, 0, vertexMix, ((Animable) (renderable)), x, objectConfig, 0, 512, objectId);
			return;
		}
		if (type == 8) {
			Object renderable;
			if (objectDef.animationID == -1 && objectDef.configObjectIDs == null)
				renderable = objectDef.renderObject(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1);
			else
				renderable = new ObjectOnTile(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, objectDef.animationID, true);
			worldController.addWallDecoration(hash, y, face, z, 0, vertexMix, ((Animable) (renderable)), x, objectConfig, 0, 768, objectId);
		}
		} catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Error placing object (out of map boundaries)");
			System.out.println("ObjectID: " + objectId + " X: " + x + " Y: " + y);
			e.printStackTrace();
		}
	}

    private static int getNoise(int i, int j, int k)
    {
        int l = i / k;
        int i1 = i & k - 1;
        int j1 = j / k;
        int k1 = j & k - 1;
        int l1 = getNoise2D(l, j1);
        int i2 = getNoise2D(l + 1, j1);
        int j2 = getNoise2D(l, j1 + 1);
        int k2 = getNoise2D(l + 1, j1 + 1);
        int l2 = method184(l1, i2, i1, k);
        int i3 = method184(j2, k2, i1, k);
        return method184(l2, i3, k1, k);
    }

    private int getHSLBitset(int i, int j, int k)
    {
        if(k > 179)
            j /= 2;
        if(k > 192)
            j /= 2;
        if(k > 217)
            j /= 2;
        if(k > 243)
            j /= 2;
        return (i / 4 << 10) + (j / 32 << 7) + k / 2;
    }

    public static boolean isObjectModelCached(int i, int j)
    {
        ObjectDef class46 = ObjectDef.forID(i);
        if(j == 11)
            j = 10;
        if(j >= 5 && j <= 8)
            j = 4;
        return class46.allModelsFetched(j);
    }

    public final void loadMapChunk(int reqPlane, int rotation, CollisionDetection clips[], int baseX, int baseTileXChunk, byte mapData[],
                                int baseTileYChunk, int myPlane, int baseY)
    {
    	/**
    	 * Apply clipping
    	 */
        for(int xTile = 0; xTile < 8; xTile++)
        {
            for(int yTile = 0; yTile < 8; yTile++)
                if(baseX + xTile > 0 && baseX + xTile < 103 && baseY + yTile > 0 && baseY + yTile < 103)
                    clips[myPlane].clipData[baseX + xTile][baseY + yTile] &= 0xfeffffff;

        }
        
        Stream stream = new Stream(mapData);
        for(int plane = 0; plane < 4; plane++)
        {
            for(int xTile = 0; xTile < 64; xTile++)
            {
                for(int yTile = 0; yTile < 64; yTile++)
                    if(plane == reqPlane && xTile >= baseTileXChunk && xTile < baseTileXChunk + 8 && yTile >= baseTileYChunk && yTile < baseTileYChunk + 8)
                        readTile(baseY + MapUtility.getRotatedMapChunkY(yTile & 7, rotation, xTile & 7), 0, stream, baseX + MapUtility.getRotatedMapChunkX(rotation, yTile & 7, xTile & 7), myPlane, rotation, 0);
                    else
                        readTile(-1, 0, stream, -1, 0, 0, 0);

            }

        }

    }

    public final void method180(byte abyte0[], int i, int j, int k, int l, CollisionDetection aclass11[])
    {
        for(int i1 = 0; i1 < 4; i1++)
        {
            for(int j1 = 0; j1 < 64; j1++)
            {
                for(int k1 = 0; k1 < 64; k1++)
                    if(j + j1 > 0 && j + j1 < 103 && i + k1 > 0 && i + k1 < 103)
                        aclass11[i1].clipData[j + j1][i + k1] &= 0xfeffffff;

            }

        }

        Stream stream = new Stream(abyte0);
        for(int l1 = 0; l1 < 4; l1++)
        {
            for(int i2 = 0; i2 < 64; i2++)
            {
                for(int j2 = 0; j2 < 64; j2++)
                    readTile(j2 + i, l, stream, i2 + j, l1, 0, k);

            }

        }
    }

    private void readTile(int tileY, int yOffset, Stream stream, int tileX, int tilePlane, int rotation, int xOffset)
    {
        if(tileX >= 0 && tileX < 104 && tileY >= 0 && tileY < 104)
        {
        	int absX = (xOffset + tileX);
            int absY = (yOffset + tileY);
            tileSettings[tilePlane][tileX][tileY] = 0;
            do
            {
                int tileType = stream.readUnsignedByte();
                if(tileType == 0)
                    if(tilePlane == 0)
                    {
                        heightMap[0][tileX][tileY] = -generateMapHeight(0xe3b7b + tileX + xOffset, 0x87cce + tileY + yOffset) * 8;
                        return;
                    } else
                    {
                        heightMap[tilePlane][tileX][tileY] = heightMap[tilePlane - 1][tileX][tileY] - 240;
                        return;
                    }
                if(tileType == 1)
                {
                    int j2 = stream.readUnsignedByte();
                    if(j2 == 1)
                        j2 = 0;
                    if(tilePlane == 0)
                    {
                        heightMap[0][tileX][tileY] = -j2 * 8;
                        return;
                    } else
                    {
                        heightMap[tilePlane][tileX][tileY] = heightMap[tilePlane - 1][tileX][tileY] - j2 * 8;
                        return;
                    }
                }
                if(tileType <= 49)
                {
                	byte tileId = stream.readSignedByte();
                	//if(absX == Client.instance.myPlayer.x && absY == Client.instance.myPlayer.y)
                	//Client.instance.currentTileId = tileId;
                	/*if(absX <= 3200)
                		System.out.println("TileID: " + tileId + " X: " + absX + " Y: " + absY);*/
                	if(tileId == 55 && Client.getOption("hd_tex")) // 54
                		tileId = 54;
                    overLay[tilePlane][tileX][tileY] = tileId;
                    overlayClippingPaths[tilePlane][tileX][tileY] = (byte)((tileType - 2) / 4);
                    overlayClippingPathRotations[tilePlane][tileX][tileY] = (byte)((tileType - 2) + rotation & 3);
                } else
                if(tileType <= 81)
                    tileSettings[tilePlane][tileX][tileY] = (byte)(tileType - 49);
                else {
                    underLay[tilePlane][tileX][tileY] = (byte)(tileType - 81);
                }
            } while(true);
        }
        do
        {
            int i2 = stream.readUnsignedByte();
            if(i2 == 0)
                break;
            if(i2 == 1)
            {
                stream.readUnsignedByte();
                return;
            }
            if(i2 <= 49)
                stream.readUnsignedByte();
        } while(true);
    }

    private int getVisibilityPlaneFor(int i, int j, int k)
    {
        if((tileSettings[j][k][i] & 8) != 0)
            return 0;
        if(j > 0 && (tileSettings[1][k][i] & 2) != 0)
            return j - 1;
        else
            return j;
    }

    public final void readObjectMap(CollisionDetection clippingPlanes[], WorldController worldController, int myPlane, int baseXTile, 
    		int baseYTileShifted, int reqPlane, byte mapData[], int baseXTileShifted, int rotation, int baseYTile)
    {
label0:
        {
            Stream stream = new Stream(mapData); //turns the .dat file into a stream.
            int foundObjectId = -1;
            do
            {
                int i2 = stream.readUSmart2(); //reads object id.
                if(i2 == 0)
                    break label0;
                foundObjectId += i2;
                int objectBits = 0;
                do
                {
                    int k2 = stream.readSmart(); //reads coordiantes x , y , z
                    if(k2 == 0)
                        break;
                    objectBits += k2 - 1;
                    int objectTileY = objectBits & 0x3f;
                    int objectTileX = objectBits >> 6 & 0x3f;
                    int objectPlane = objectBits >> 12;
                    int k3 = stream.readUnsignedByte(); //reads orienetation j & j1 
                    int type = k3 >> 2;
                    int objectRotation = k3 & 3;
                    if(objectPlane == myPlane && objectTileX >= baseXTileShifted && objectTileX < baseXTileShifted + 8 && objectTileY >= baseYTileShifted && objectTileY < baseYTileShifted + 8)
                    {
                        ObjectDef objectDef = ObjectDef.forID(foundObjectId);
                        int finalXTile = baseXTile + MapUtility.getRotatedLandscapeChunkX(rotation, (objectRotation == 0 || objectRotation == 2) ? objectDef.sizeY : objectDef.sizeX, objectTileX & 7, objectTileY & 7, (objectRotation == 0 || objectRotation == 2) ? objectDef.sizeX : objectDef.sizeY);
                        int finalYTile = baseYTile + MapUtility.getRotatedLandscapeChunkY(objectTileY & 7, (objectRotation == 0 || objectRotation == 2) ? objectDef.sizeY : objectDef.sizeX, rotation, (objectRotation == 0 || objectRotation == 2) ? objectDef.sizeX : objectDef.sizeY, objectTileX & 7);
                        if(finalXTile > 0 && finalYTile > 0 && finalXTile < 103 && finalYTile < 103)
                        {
                            int l4 = objectPlane;
                            if((tileSettings[1][finalXTile][finalYTile] & 2) == 2)
                                l4--;
                            CollisionDetection tileSetting = null;
                            if(l4 >= 0)
                                tileSetting = clippingPlanes[l4];
                            renderObject(finalYTile, worldController, tileSetting, type, reqPlane, finalXTile, foundObjectId, objectRotation + rotation & 3);
                        }
                    }
                } while(true);
            } while(true);
        }
    }

    private static int method184(int i, int j, int k, int l)
    {
        int i1 = 0x10000 - Rasterizer.COSINE[(k * 1024) / l] >> 1;
        return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
    }

    private int getOverlayShadow(int i, int j)
    {
        if(i == -2)
            return 0xbc614e;
        if(i == -1)
        {
            if(j < 0)
                j = 0;
            else
            if(j > 127)
                j = 127;
            j = 127 - j;
            return j;
        }
        j = (j * (i & 0x7f)) / 128;
        if(j < 2)
            j = 2;
        else
        if(j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }

    private static int getNoise2D(int i, int j)
    {
        int k = calculateNoise(i - 1, j - 1) + calculateNoise(i + 1, j - 1) + calculateNoise(i - 1, j + 1) + calculateNoise(i + 1, j + 1);
        int l = calculateNoise(i - 1, j) + calculateNoise(i + 1, j) + calculateNoise(i, j - 1) + calculateNoise(i, j + 1);
        int i1 = calculateNoise(i, j);
        return k / 16 + l / 8 + i1 / 4;
    }

    private static int adjustHSLLightness(int i, int j)
    {
        if(i == -1)
            return 0xbc614e;
        j = (j * (i & 0x7f)) / 128;
        if(j < 2)
            j = 2;
        else
        if(j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }

    public static void addObject(WorldController worldController, int i, int j, int k, int l, CollisionDetection class11, int ai[][][], int i1,
                                 int objectId, int k1)
    {
        int l1 = ai[l][i1][j];
        int i2 = ai[l][i1 + 1][j];
        int j2 = ai[l][i1 + 1][j + 1];
        int k2 = ai[l][i1][j + 1];
        int l2 = l1 + i2 + j2 + k2 >> 2;
        ObjectDef objectDef = ObjectDef.forID(objectId);
		int i3 = i1 + (j << 7) + ((objectId > 0x7fff ? objectId  & 0x7fff : objectId) << 14) + 0x40000000;
        if(!objectDef.hasActions)
            i3 += 0x80000000;
        byte byte1 = (byte)((i << 6) + k);
        if(k == 22)
        {
            Object obj;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj = objectDef.renderObject(22, i, l1, i2, j2, k2, -1);
            else
                obj = new ObjectOnTile(objectId, i, 22, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addGroundDecoration(k1, l2, j, ((Animable) (obj)), byte1, i3, i1, objectId);
            if(objectDef.isUnwalkable && objectDef.hasActions)
                class11.appendSolidFlag(j, i1);
            return;
        }
        if(k == 10 || k == 11)
        {
            Object obj1;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj1 = objectDef.renderObject(10, i, l1, i2, j2, k2, -1);
            else
                obj1 = new ObjectOnTile(objectId, i, 10, i2, j2, l1, k2, objectDef.animationID, true);
            if(obj1 != null)
            {
                int j5 = 0;
                if(k == 11)
                    j5 += 256;
                int sizeX;
                int sizeY;
                if(i == 1 || i == 3)
                {
                    sizeX = objectDef.sizeY;
                    sizeY = objectDef.sizeX;
                } else
                {
                    sizeX = objectDef.sizeX;
                    sizeY = objectDef.sizeY;
                }
                worldController.addInteractableEntity(i3, byte1, l2, sizeY, ((Animable) (obj1)), sizeX, k1, j5, j, i1, objectId);
            }
            if(objectDef.isUnwalkable)
                class11.markSolidOccupant(objectDef.walkable, objectDef.sizeX, objectDef.sizeY, i1, j, i);
            return;
        }
        if(k >= 12)
        {
            Object obj2;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj2 = objectDef.renderObject(k, i, l1, i2, j2, k2, -1);
            else
                obj2 = new ObjectOnTile(objectId, i, k, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addInteractableEntity(i3, byte1, l2, 1, ((Animable) (obj2)), 1, k1, 0, j, i1, objectId);
            if(objectDef.isUnwalkable)
                class11.markSolidOccupant(objectDef.walkable, objectDef.sizeX, objectDef.sizeY, i1, j, i);
            return;
        }
        if(k == 0)
        {
            Object obj3;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj3 = objectDef.renderObject(0, i, l1, i2, j2, k2, -1);
            else
                obj3 = new ObjectOnTile(objectId, i, 0, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallObject(WALL_ROOT_FLAGS[i], ((Animable) (obj3)), i3, j, byte1, i1, null, l2, 0, k1, objectId);
            if(objectDef.isUnwalkable)
                class11.markWall(j, i, i1, k, objectDef.walkable);
            return;
        }
        if(k == 1)
        {
            Object obj4;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj4 = objectDef.renderObject(1, i, l1, i2, j2, k2, -1);
            else
                obj4 = new ObjectOnTile(objectId, i, 1, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallObject(WALL_EXT_FLAGS[i], ((Animable) (obj4)), i3, j, byte1, i1, null, l2, 0, k1, objectId);
            if(objectDef.isUnwalkable)
                class11.markWall(j, i, i1, k, objectDef.walkable);
            return;
        }
        if(k == 2)
        {
            int j3 = i + 1 & 3;
            Object obj11;
            Object obj12;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
            {
                obj11 = objectDef.renderObject(2, 4 + i, l1, i2, j2, k2, -1);
                obj12 = objectDef.renderObject(2, j3, l1, i2, j2, k2, -1);
            } else
            {
                obj11 = new ObjectOnTile(objectId, 4 + i, 2, i2, j2, l1, k2, objectDef.animationID, true);
                obj12 = new ObjectOnTile(objectId, j3, 2, i2, j2, l1, k2, objectDef.animationID, true);
            }
            worldController.addWallObject(WALL_ROOT_FLAGS[i], ((Animable) (obj11)), i3, j, byte1, i1, ((Animable) (obj12)), l2, WALL_ROOT_FLAGS[j3], k1, objectId);
            if(objectDef.isUnwalkable)
                class11.markWall(j, i, i1, k, objectDef.walkable);
            return;
        }
        if(k == 3)
        {
            Object obj5;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj5 = objectDef.renderObject(3, i, l1, i2, j2, k2, -1);
            else
                obj5 = new ObjectOnTile(objectId, i, 3, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallObject(WALL_EXT_FLAGS[i], ((Animable) (obj5)), i3, j, byte1, i1, null, l2, 0, k1, objectId);
            if(objectDef.isUnwalkable)
                class11.markWall(j, i, i1, k, objectDef.walkable);
            return;
        }
        if(k == 9)
        {
            Object obj6;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj6 = objectDef.renderObject(k, i, l1, i2, j2, k2, -1);
            else
                obj6 = new ObjectOnTile(objectId, i, k, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addInteractableEntity(i3, byte1, l2, 1, ((Animable) (obj6)), 1, k1, 0, j, i1, objectId);
            if(objectDef.isUnwalkable)
                class11.markSolidOccupant(objectDef.walkable, objectDef.sizeX, objectDef.sizeY, i1, j, i);
            return;
        }
        if(objectDef.adjustToTerrain)
            if(i == 1)
            {
                int k3 = k2;
                k2 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k3;
            } else
            if(i == 2)
            {
                int l3 = k2;
                k2 = i2;
                i2 = l3;
                l3 = j2;
                j2 = l1;
                l1 = l3;
            } else
            if(i == 3)
            {
                int i4 = k2;
                k2 = l1;
                l1 = i2;
                i2 = j2;
                j2 = i4;
            }
        if(k == 4)
        {
            Object obj7;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj7 = objectDef.renderObject(4, 0, l1, i2, j2, k2, -1);
            else
                obj7 = new ObjectOnTile(objectId, 0, 4, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallDecoration(i3, j, i * 512, k1, 0, l2, ((Animable) (obj7)), i1, byte1, 0, WALL_ROOT_FLAGS[i], objectId);
            return;
        }
        if(k == 5)
        {
            int j4 = 16;
            int l4 = worldController.getWallObjectUID(k1, i1, j);
            if(l4 > 0)
                j4 = ObjectDef.forID(l4 >> 14 & 0x7fff).anInt775;
            Object obj13;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj13 = objectDef.renderObject(4, 0, l1, i2, j2, k2, -1);
            else
                obj13 = new ObjectOnTile(objectId, 0, 4, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallDecoration(i3, j, i * 512, k1, ROTATE_X_DELTA[i] * j4, l2, ((Animable) (obj13)), i1, byte1, ROTOATE_Y_DELTA[i] * j4, WALL_ROOT_FLAGS[i], objectId);
            return;
        }
        if(k == 6)
        {
            Object obj8;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj8 = objectDef.renderObject(4, 0, l1, i2, j2, k2, -1);
            else
                obj8 = new ObjectOnTile(objectId, 0, 4, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallDecoration(i3, j, i, k1, 0, l2, ((Animable) (obj8)), i1, byte1, 0, 256, objectId);
            return;
        }
        if(k == 7)
        {
            Object obj9;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj9 = objectDef.renderObject(4, 0, l1, i2, j2, k2, -1);
            else
                obj9 = new ObjectOnTile(objectId, 0, 4, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallDecoration(i3, j, i, k1, 0, l2, ((Animable) (obj9)), i1, byte1, 0, 512, objectId);
            return;
        }
        if(k == 8)
        {
            Object obj10;
            if(objectDef.animationID == -1 && objectDef.configObjectIDs == null)
                obj10 = objectDef.renderObject(4, 0, l1, i2, j2, k2, -1);
            else
                obj10 = new ObjectOnTile(objectId, 0, 4, i2, j2, l1, k2, objectDef.animationID, true);
            worldController.addWallDecoration(i3, j, i, k1, 0, l2, ((Animable) (obj10)), i1, byte1, 0, 768, objectId);
        }
    }

  public static boolean allObjectsLoaded(int i, byte[] is, int i_250_) {
    boolean bool = true;
    try {
	    Stream stream = new Stream(is);
	    int i_252_ = -1;
	    for (;;)
	      {
		int i_253_ = stream.readSmart ();
		if (i_253_ == 0)
		  break;
		i_252_ += i_253_;
		int i_254_ = 0;
		boolean bool_255_ = false;
		for (;;)
		  {
		    if (bool_255_)
		      {
			int i_256_ = stream.readSmart ();
			if (i_256_ == 0)
			  break;
			stream.readUnsignedByte();
		      }
		    else
		      {
			int i_257_ = stream.readSmart ();
			if (i_257_ == 0)
			  break;
			i_254_ += i_257_ - 1;
			int i_258_ = i_254_ & 0x3f;
			int i_259_ = i_254_ >> 6 & 0x3f;
			int i_260_ = stream.readUnsignedByte() >> 2;
			int i_261_ = i_259_ + i;
			int i_262_ = i_258_ + i_250_;
			if (i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103)
			  {
			    ObjectDef class46 = ObjectDef.forID (i_252_);
			    if (i_260_ != 22 || !lowMem || class46.hasActions
	                    || class46.aBoolean736)
			      {
				bool &= class46.isAllModelsFetched ();
				bool_255_ = true;
			      }
			  }
		      }
		  }
	      }
    } catch (Exception e) {
    	e.printStackTrace();
    }
    return bool;
  }

    public final void loadObjects(int i, CollisionDetection aclass11[], int j, WorldController worldController, byte abyte0[]) {
		label0: {
			ByteBuffer stream = new ByteBuffer(abyte0);
			int l = -1;
			do {
				int i1 = stream.method422();
				if (i1 == 0)
					break label0;
				l += i1;
				int j1 = 0;
				do {
					int k1 = stream.method422();
					if (k1 == 0)
						break;
					j1 += k1 - 1;
					int l1 = j1 & 0x3f;
					int i2 = j1 >> 6 & 0x3f;
					int j2 = j1 >> 12;
					int k2 = stream.readUnsignedByte();
					int l2 = k2 >> 2;
					int i3 = k2 & 3;
					int j3 = i2 + i;
					int k3 = l1 + j;
					if (j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103) {
						int l3 = j2;
						if ((tileSettings[1][j3][k3] & 2) == 2)
							l3--;
						CollisionDetection class11 = null;
						if (l3 >= 0 && l3 < 4)
							class11 = aclass11[l3];
						renderObject(k3, worldController, class11, l2, j2, j3, l, i3);
					}
				} while (true);
			} while (true);
		}
	}

    private static int hueRandomizer = (int)(Math.random() * 17D) - 8;
    private final int[] blendedHue;
    private final int[] blendedSaturation;
    private final int[] blendedLightness;
    private final int[] blendedHighAdvisor;
    private final int[] blendedDirectionTracker;
    private final int[][][] heightMap;
    public final byte[][][] overLay;
    static int onBuildTimePlane;
    private static int offsetLightning = (int)(Math.random() * 33D) - 16;
    private final byte[][][] tileShadowIntensity;
    private final int[][][] tileCullingBitsets;
    private final byte[][][] overlayClippingPaths;
    private static final int ROTATE_X_DELTA[] = {
        1, 0, -1, 0
    };
   // private static final int anInt138 = 323;
    private final int[][] tileShadowArray;
    private static final int WALL_EXT_FLAGS[] = {
        16, 32, 64, 128
    };
    public final byte[][][] underLay;
    private static final int ROTOATE_Y_DELTA[] = {
        0, -1, 0, 1
    };
    static int highestPlane = 99;
    private final int regionSizeX;
    private final int regionSizeY;
    private final byte[][][] overlayClippingPathRotations;
    private final byte[][][] tileSettings;
    static boolean lowMem = true;
    private static final int WALL_ROOT_FLAGS[] = {
        1, 2, 4, 8
    };

}
