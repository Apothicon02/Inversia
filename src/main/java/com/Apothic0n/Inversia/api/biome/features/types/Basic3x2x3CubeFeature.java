package com.Apothic0n.Inversia.api.biome.features.types;

import com.Apothic0n.Inversia.core.objects.InversiaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

import static com.Apothic0n.Inversia.core.events.ModEvents.BRIGHTNESS_NOISE;
import static com.Apothic0n.Inversia.core.events.ModEvents.SATURATION_NOISE;

public class Basic3x2x3CubeFeature extends Feature<SimpleBlockConfiguration> {
    public Basic3x2x3CubeFeature(Codec<SimpleBlockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();
        RandomSource random = pContext.random();
        BlockState material = pContext.config().toPlace().getState(random, blockPos);
        if (worldGenLevel.isEmptyBlock(blockPos.offset(-1, -1, 0)) || worldGenLevel.isEmptyBlock(blockPos.offset(1, -1, 0)) ||
                worldGenLevel.isEmptyBlock(blockPos.offset(0, -1, -1)) || worldGenLevel.isEmptyBlock(blockPos.offset(0, -1, 1))) {
            return false;
        } else {
            generateCube(worldGenLevel, blockPos, material);
            if ((int)(Math.random()*(3)+1) < 2) {
                generateCube(worldGenLevel, blockPos.above(2), material);
                if ((int)(Math.random()*(3)+1) < 2) {
                    generateCube(worldGenLevel, blockPos.above(4), material);
                    if ((int)(Math.random()*(3)+1) < 2) {
                        generateCube(worldGenLevel, blockPos.above(6), material);
                        if ((int)(Math.random()*(4)+1) < 2) {
                            generateCube(worldGenLevel, blockPos.above(8), material);
                            if ((int)(Math.random()*(4)+1) < 2) {
                                generateCube(worldGenLevel, blockPos.above(10), material);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
    
    private static void generateCube(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState material) {
        generateLayer(worldGenLevel, blockPos, material);
        generateLayer(worldGenLevel, blockPos.above(), material);
    }

    private static void generateLayer(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState material) {
        setBlock(worldGenLevel, blockPos.offset(0, 0, 0), material);
        setBlock(worldGenLevel, blockPos.offset(1, 0, 0), material);
        setBlock(worldGenLevel, blockPos.offset(0, 0, 1), material);
        setBlock(worldGenLevel, blockPos.offset(-1, 0, 0), material);
        setBlock(worldGenLevel, blockPos.offset(0, 0, -1), material);
        setBlock(worldGenLevel, blockPos.offset(-1, 0, -1), material);
        setBlock(worldGenLevel, blockPos.offset(1, 0, 1), material);
        setBlock(worldGenLevel, blockPos.offset(1, 0, -1), material);
        setBlock(worldGenLevel, blockPos.offset(-1, 0, 1), material);
    }

    private static void setBlock(WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState material) {
        if (material.is(InversiaBlocks.JADE.get())) {
            int x = blockPos.getX();
            int z = blockPos.getZ();
            double brightness = (BRIGHTNESS_NOISE.getValue(x * 0.02, z * 0.02, false));
            double saturation = (SATURATION_NOISE.getValue(x * 0.02, z * 0.02, false));
            if ((brightness < 0.4 && brightness > 0) || (saturation < 0 && saturation > -0.4)) {
                material = InversiaBlocks.LIGHT_JADE.get().defaultBlockState();
            }
        }
        worldGenLevel.setBlock(blockPos, material, 2);
    }
}
