package com.Apothic0n.Inversia.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndGatewayFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EndGatewayFeature.class)
public class EndGatewayFeatureMixin extends Feature<EndGatewayConfiguration> {

    public EndGatewayFeatureMixin(Codec<EndGatewayConfiguration> config) {
        super(config);
    }

    /**
     * @author Apothicon
     * @reason Prevents being stranded in the end after going through a gateway, by generating the gateway with a platform under it.
     */
    @Overwrite
    public boolean place(FeaturePlaceContext<EndGatewayConfiguration> p_159715_) {
        BlockPos blockpos = p_159715_.origin();
        WorldGenLevel worldgenlevel = p_159715_.level();
        EndGatewayConfiguration endgatewayconfiguration = p_159715_.config();

        for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-1, -2, -1), blockpos.offset(1, 2, 1))) {
            boolean flag = blockpos1.getX() == blockpos.getX();
            boolean flag1 = blockpos1.getY() == blockpos.getY();
            boolean flag2 = blockpos1.getZ() == blockpos.getZ();
            boolean flag3 = Math.abs(blockpos1.getY() - blockpos.getY()) == 2;
            if (flag && flag1 && flag2) {
                BlockPos blockpos2 = blockpos1.immutable();
                this.setBlock(worldgenlevel, blockpos2, Blocks.END_GATEWAY.defaultBlockState());
                makeSquare(worldgenlevel, blockpos2.below(3), Blocks.AIR.defaultBlockState());
                makeSquare(worldgenlevel, blockpos2.below(4), Blocks.AIR.defaultBlockState());
                this.setBlock(worldgenlevel, blockpos2.below(4), Blocks.ENDER_CHEST.defaultBlockState());
                makeSquare(worldgenlevel, blockpos2.below(5), Blocks.OBSIDIAN.defaultBlockState());
                endgatewayconfiguration.getExit().ifPresent((p_65699_) -> {
                    BlockEntity blockentity = worldgenlevel.getBlockEntity(blockpos2);
                    if (blockentity instanceof TheEndGatewayBlockEntity theendgatewayblockentity) {
                        theendgatewayblockentity.setExitPosition(p_65699_, endgatewayconfiguration.isExitExact());
                        blockentity.setChanged();
                    }

                });
            } else if (flag1) {
                this.setBlock(worldgenlevel, blockpos1, Blocks.AIR.defaultBlockState());
            } else if (flag3 && flag && flag2) {
                this.setBlock(worldgenlevel, blockpos1, Blocks.BEDROCK.defaultBlockState());
            } else if ((flag || flag2) && !flag3) {
                this.setBlock(worldgenlevel, blockpos1, Blocks.BEDROCK.defaultBlockState());
            } else {
                this.setBlock(worldgenlevel, blockpos1, Blocks.AIR.defaultBlockState());
            }
        }

        return true;
    }

    private void makeSquare(WorldGenLevel worldgenlevel, BlockPos blockpos2, BlockState blockState) {
        this.setBlock(worldgenlevel, blockpos2, blockState);
        this.setBlock(worldgenlevel, blockpos2.north(), blockState);
        this.setBlock(worldgenlevel, blockpos2.east(), blockState);
        this.setBlock(worldgenlevel, blockpos2.south(), blockState);
        this.setBlock(worldgenlevel, blockpos2.west(), blockState);
        this.setBlock(worldgenlevel, blockpos2.north().east(), blockState);
        this.setBlock(worldgenlevel, blockpos2.south().east(), blockState);
        this.setBlock(worldgenlevel, blockpos2.south().west(), blockState);
        this.setBlock(worldgenlevel, blockpos2.north().west(), blockState);
    }
}
