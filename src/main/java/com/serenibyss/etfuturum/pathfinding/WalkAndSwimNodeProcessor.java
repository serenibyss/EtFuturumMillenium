package com.serenibyss.etfuturum.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;

import java.util.EnumSet;

public class WalkAndSwimNodeProcessor extends WalkNodeProcessor {
    private float walkPriority;
    private float waterEdgePriority;


    @Override
    public void init(IBlockAccess sourceIn, EntityLiving mob) {
        super.init(sourceIn, mob);
        mob.setPathPriority(PathNodeType.WATER, 0.0f);
        this.walkPriority = mob.getPathPriority(PathNodeType.WALKABLE);
        mob.setPathPriority(PathNodeType.WALKABLE, 6.0f);
        this.waterEdgePriority = mob.getPathPriority(EFMPathNodeType.WATER_BORDER);
        mob.setPathPriority(EFMPathNodeType.WATER_BORDER, 4.0F);
    }

    @Override
    public void postProcess() {
        this.entity.setPathPriority(PathNodeType.WALKABLE, this.walkPriority);
        this.entity.setPathPriority(EFMPathNodeType.WATER_BORDER, this.waterEdgePriority);
        super.postProcess();
    }

    @Override
    public PathPoint getStart() {
        return this.openPoint(MathHelper.floor(this.entity.getEntityBoundingBox().minX), MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5), MathHelper.floor(this.entity.getEntityBoundingBox().minZ))
    }

    @Override
    public PathPoint getPathPointToCoords(double x, double y, double z) {
        return this.openPoint(MathHelper.floor(x), MathHelper.floor(y + 0.5), MathHelper.floor(z));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int i = 0;
        int j = 0;
        BlockPos blockPos = new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z);
        double d0 = this.getYLevel(blockPos);
        PathPoint pp = this.getPathPoint(currentPoint.x, currentPoint.y, currentPoint.z + 1, 1, d0);
        PathPoint pp2 = this.getPathPoint(currentPoint.x - 1, currentPoint.y, currentPoint.z, 1, d0);
        PathPoint pp3 = this.getPathPoint(currentPoint.x + 1, currentPoint.y, currentPoint.z, 1, d0);
        PathPoint pp4 = this.getPathPoint(currentPoint.x, currentPoint.y, currentPoint.z - 1, 1, d0);
        PathPoint pp5 = this.getPathPoint(currentPoint.x, currentPoint.y + 1, currentPoint.z - 1, 1, d0);
        PathPoint pp6 = this.getPathPoint(currentPoint.x, currentPoint.y - 1, currentPoint.z - 1, 1, d0);
        if(pp != null && !pp.visited && pp.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pp;
        }
        if(pp2 != null && !pp2.visited && pp2.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pp2;
        }
        if(pp3 != null && !pp3.visited && pp3.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pp3;
        }
        if(pp4 != null && !pp4.visited && pp4.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pp4;
        }
        if(pp5 != null && !pp5.visited && pp5.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pp5;
        }
        if(pp6 != null && !pp6.visited && pp6.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pp6;
        }

        boolean flag = pp4 == null || pp4.nodeType == PathNodeType.OPEN || pp4.costMalus != 0.0F;
        boolean flag1 = pp == null || pp.nodeType == PathNodeType.OPEN || pp.costMalus != 0.0F;
        boolean flag2 = pp3 == null || pp3.nodeType == PathNodeType.OPEN || pp3.costMalus != 0.0F;
        boolean flag3 = pp2 == null || pp2.nodeType == PathNodeType.OPEN || pp2.costMalus != 0.0F;
        if(flag && flag3) {
            PathPoint pp7 = this.getPathPoint(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1, 1, d0);
            if(pp7 != null && !pp7.visited && pp7.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pp7;
            }
        }
        if(flag && flag2) {
            PathPoint pp8 = this.getPathPoint(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1, 1, d0);
            if(pp8 != null && !pp8.visited && pp8.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pp8;
            }
        }
        if(flag1 && flag3) {
            PathPoint pp9 = this.getPathPoint(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1, 1, d0);
            if(pp9 != null && !pp9.visited && pp9.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pp9;
            }
        }
        if(flag1 && flag2) {
            PathPoint pp10 = this.getPathPoint(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1, 1, d0);
            if(pp10 != null && !pp10.visited && pp10.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pp10;
            }
        }
        return i;
    }

    private double getYLevel(BlockPos pos) {
        if(!this.entity.isInWater()) {
            BlockPos blok = pos.down();
            AxisAlignedBB aabb = this.blockaccess.getBlockState(blok).getCollisionBoundingBox(this.blockaccess, blok);
            return (double) blok.getY() + (aabb == null ? 0 : aabb.maxY); //check back to see if this collision is correct
        } else {
            return (double) pos.getY() + 0.5;
        }
    }

    private PathPoint getPathPoint(int x, int y, int z, int a, double b) {
        PathPoint pathpoint = null;
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = this.getYLevel(blockpos);
        if(d0 - b > 1.125D) {
            return null;
        } else {
            PathNodeType pathnodetype = this.getPathNodeType(this.blockaccess, x, y, z, this.entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, false, false);
            float f = this.entity.getPathPriority(pathnodetype);
            double d1 = (double)this.entity.width / 2.0;
            if(f >= 0.0F) {
                pathpoint = this.openPoint(x, y, z);
                pathpoint.nodeType = pathnodetype;
                pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            }

            if(pathnodetype != PathNodeType.WATER && pathnodetype != PathNodeType.WALKABLE) {
                if(pathpoint == null && a > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.TRAPDOOR) {
                    pathpoint = this.getPathPoint(x, y + 1, z, a - 1, b);
                }

                if(pathnodetype == PathNodeType.OPEN) {
                    AxisAlignedBB aabb = new AxisAlignedBB((double)x - d1 + 0.5, (double)y + 0.001, (double)z - d1 + 0.5, (double)x + d1 + 0.5, (double)((float)y + this.entity.height), (double)z + d1 + 0.5);
                    if(!this.entity.world.checkNoEntityCollision(aabb, null)) {
                        return null;
                    }

                    PathNodeType pathnodetype1 = this.getPathNodeType(this.blockaccess, x, y - 1, z, this.entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, false, false);
                    if(pathnodetype1 == PathNodeType.BLOCKED) {
                        pathpoint = this.openPoint(x, y, z);
                        pathpoint.nodeType = PathNodeType.WALKABLE;
                        pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                        return pathpoint;
                    }

                    if(pathnodetype1 == PathNodeType.WATER) {
                        pathpoint = this.openPoint(x, y, z);
                        pathpoint.nodeType = PathNodeType.WATER;
                        pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                        return pathpoint;
                    }

                    int i = 0;

                    while(y > 0 && pathnodetype == PathNodeType.OPEN) {
                        --y;
                        if(i++ > this.entity.getMaxFallHeight()) {
                            return null;
                        }

                        pathnodetype = this.getPathNodeType(this.blockaccess, x, y, z, this.entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, false, false);
                        f = this.entity.getPathPriority(pathnodetype);
                        if(pathnodetype != PathNodeType.OPEN && f >= 0.0F) {
                            pathpoint = this.openPoint(x, y, z);
                            pathpoint.nodeType = pathnodetype;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            break;
                        }

                        if(f < 0.0f) {
                            return null;
                        }
                    }
                }

                return pathpoint;
            }
            else {
                if(y < this.entity.world.getSeaLevel() - 10 && pathpoint != null) {
                    ++pathpoint.costMalus;
                }

                return pathpoint;
            }
        }
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess blockaccess, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<PathNodeType> pathNodeTypes, PathNodeType pathNodeType, BlockPos blockPos) {
        for(int i = 0; i < xSize; ++i) {
            for (int j = 0; j < ySize; ++j) {
                for (int k = 0; k < zSize; ++k) {
                    int l = i + x;
                    int m = j + y;
                    int n = k + z;
                    PathNodeType pathnodetype = this.getPathNodeType(blockaccess, l, m, n);
                    if(pathnodetype == PathNodeType.DOOR_WOOD_CLOSED && canOpenDoorsIn && canEnterDoorsIn) {
                        pathnodetype = PathNodeType.WALKABLE;
                    }

                    if(pathnodetype == PathNodeType.DOOR_OPEN && !canEnterDoorsIn) {
                        pathnodetype = PathNodeType.BLOCKED;
                    }

                    if(pathnodetype == PathNodeType.RAIL && !(blockaccess.getBlockState(blockPos).getBlock() instanceof BlockRailBase) && !(blockaccess.getBlockState(blockPos.down()).getBlock() instanceof BlockRailBase)) {
                        pathnodetype = PathNodeType.FENCE;
                    }

                    if(i == 0 && j == 0 && k == 0) {
                        pathNodeType = pathnodetype;
                    }

                    pathNodeTypes.add(pathnodetype);
                }
            }
        }
        return pathNodeType;
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
        PathNodeType pathnodetype = this.getPathNodeType(blockaccessIn, x, y, z);
        if (pathnodetype == PathNodeType.WATER) {
            for(EnumFacing enumFacing : EnumFacing.values()) {
                PathNodeType pathnodetype2 = this.getPathNodeTypeRaw(blockaccessIn, x + enumFacing.getXOffset(), y + enumFacing.getYOffset(), z + enumFacing.getZOffset());
                if(pathnodetype2 == PathNodeType.BLOCKED) {
                    return EFMPathNodeType.WATER_BORDER;
                }
            }

            return PathNodeType.WATER;
        } else {
            if (pathnodetype == PathNodeType.OPEN && y >= 1) {
                Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
                PathNodeType pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
                pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER && pathnodetype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA) {
                    pathnodetype = PathNodeType.DAMAGE_FIRE;
                }

                if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }
                if (pathnodetype1 == PathNodeType.DAMAGE_OTHER) pathnodetype = PathNodeType.DAMAGE_OTHER;
            }
            pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
            return pathnodetype;
        }
    }
}
