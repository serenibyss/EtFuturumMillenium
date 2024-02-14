package com.serenibyss.etfuturum.client;

import com.serenibyss.etfuturum.client.gui.GuiBarrel;
import com.serenibyss.etfuturum.client.gui.GuiStonecutter;
import com.serenibyss.etfuturum.containers.ContainerBarrel;
import com.serenibyss.etfuturum.containers.ContainerStonecutter;
import com.serenibyss.etfuturum.tiles.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

import static com.serenibyss.etfuturum.client.EFMGuiIDs.*;

public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        return switch (ID) {
            case GUI_BARREL -> new ContainerBarrel(player.inventory, (TileEntityBarrel) tile, player);
            case GUI_STONECUTTER -> new ContainerStonecutter(player, world, pos);
            default -> null;
        };
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        return switch (ID) {
            case GUI_BARREL -> new GuiBarrel(player.inventory, (TileEntityBarrel) tile, player);
            case GUI_STONECUTTER -> new GuiStonecutter(player, world, pos);
            default -> null;
        };
    }
}
