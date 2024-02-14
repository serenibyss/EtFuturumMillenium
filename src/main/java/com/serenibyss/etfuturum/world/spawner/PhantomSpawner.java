package com.serenibyss.etfuturum.world.spawner;

import com.serenibyss.etfuturum.entities.monster.EntityPhantom;
import com.serenibyss.etfuturum.stats.EFMStatList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

import java.util.Random;

public class PhantomSpawner {
    private int ticksUntilSpawn;

    public int spawnMobs(World worldIn, boolean spawnHostiles, boolean peaceful) {
        if(!spawnHostiles)
            return 0;

        Random r = worldIn.rand;
        --this.ticksUntilSpawn;
        if(this.ticksUntilSpawn > 0)
            return 0;

        this.ticksUntilSpawn += (60 + r.nextInt(60)) * 20;
        if(worldIn.getSkylightSubtracted() < 5 && worldIn.provider.hasSkyLight())
            return 0;

        int numberSpawned = 0;
        for(EntityPlayer player : worldIn.playerEntities) {
            while(!player.isSpectator()) {
                BlockPos block = new BlockPos(player);
                if(!worldIn.provider.hasSkyLight() && (block.getY() >= worldIn.getSeaLevel() || worldIn.canSeeSky(block))) {
                    DifficultyInstance diff = worldIn.getDifficultyForLocation(block);
                    if(diff.isHarderThan(r.nextFloat() * 3.0f)) {
                        StatisticsManagerServer stats = ((EntityPlayerMP)player).getStatFile();
                        int statLevel = MathHelper.clamp(stats.readStat(EFMStatList.TIME_SINCE_REST), 1, Integer.MAX_VALUE);
                        if(r.nextInt(statLevel) > 72000) {
                            BlockPos spawnLoc = block.up(20 + r.nextInt(15)).east(-10 + r.nextInt(21)).south(-10 + r.nextInt(21));
                            IBlockState spawnLocState = worldIn.getBlockState(spawnLoc);
                            if(WorldEntitySpawner.isValidEmptySpawnBlock(spawnLocState)) {
                                IEntityLivingData data = null;
                                int packSpawnIndex = 1 + r.nextInt((int)diff.getAdditionalDifficulty() + 1);
                                for(int i = 0; i < packSpawnIndex; i++) {
                                    EntityPhantom phantom = new EntityPhantom(worldIn);
                                    phantom.moveToBlockPosAndAngles(spawnLoc, 0.0f, 0.0f);
                                    data = phantom.onInitialSpawn(diff, data);
                                    worldIn.spawnEntity(phantom);
                                }
                                numberSpawned += packSpawnIndex;
                            }
                        }
                    }
                }
            }
        }
        return numberSpawned;
    }

}
