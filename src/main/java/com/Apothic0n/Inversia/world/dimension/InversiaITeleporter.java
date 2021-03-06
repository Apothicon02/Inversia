package com.Apothic0n.Inversia.world.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ITeleporter;

import java.util.ArrayList;

public class InversiaITeleporter implements ITeleporter {
    public static ArrayList<Object> fallToNetherRoof(ServerPlayer player) {
        ServerLevel newLevel = player.getServer().getLevel(InversiaDimensions.InversiaDim);
        double pX = player.getX();
        double pY = 255;
        double pZ = player.getZ();
        BlockPos destinationPos = new BlockPos(pX, pY, pZ);
        if (ForgeHooks.onTravelToDimension(player, newLevel.dimension())) {
            int tries = 0;
            BlockPos tempPos = destinationPos;
            while ((newLevel.getBlockState(tempPos).getMaterial() != Material.AIR) &&
                    !newLevel.getBlockState(tempPos).canBeReplaced(Fluids.WATER) &&
                    newLevel.getBlockState(tempPos.above()).getMaterial() != Material.AIR &&
                    !newLevel.getBlockState(tempPos.above()).canBeReplaced(Fluids.WATER) && tries < 128) {
                tempPos = tempPos.below();
                tries++;
            }
            if (tempPos.getY() >= 128) {
                pY = destinationPos.getY();
                destinationPos = new BlockPos(pX, pY, pZ);
            }
        }
        ArrayList<Object> array = new ArrayList<Object>();
        array.add(player);
        array.add(destinationPos);
        return array;
    }

    public static ArrayList<Object> acsendFromNetherRoof(ServerPlayer player) {
        ServerLevel newLevel = player.getServer().getLevel(InversiaDimensions.InversiaDim);
        double pX = player.getX();
        double pY = 0;
        double pZ = player.getZ();
        BlockPos destinationPos = new BlockPos(pX, pY, pZ);
        if (ForgeHooks.onTravelToDimension(player, newLevel.dimension())) {
            int tries = 0;
            BlockPos tempPos = destinationPos;
            while (newLevel.getBlockState(tempPos).getMaterial() != Material.AIR &&
                    !newLevel.getBlockState(tempPos).canBeReplaced(Fluids.WATER) &&
                    newLevel.getBlockState(tempPos.above()).getMaterial() != Material.AIR &&
                    !newLevel.getBlockState(tempPos.above()).canBeReplaced(Fluids.WATER) &&
                    newLevel.getBlockState(tempPos.above().above()).getMaterial() != Material.AIR &&
                    !newLevel.getBlockState(tempPos.above().above()).canBeReplaced(Fluids.WATER) &&
                    newLevel.getBlockState(tempPos.above().above()).getMaterial().isSolid() && tries < 253) {
                tempPos = tempPos.above();
                tries++;
            }
            if (tempPos.getY() < 255) {
                pY = destinationPos.getY();
                destinationPos = new BlockPos(pX, pY, pZ);
            }
        }
        ArrayList<Object> array = new ArrayList<Object>();
        array.add(player);
        array.add(destinationPos);
        return array;
    }
}
