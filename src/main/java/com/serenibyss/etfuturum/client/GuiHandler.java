package com.serenibyss.etfuturum.client;

import com.serenibyss.etfuturum.client.gui.GuiBarrel;
import com.serenibyss.etfuturum.containers.ContainerBarrel;
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
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile == null) return null;

        return switch (ID) {
            case GUI_BARREL -> new ContainerBarrel(player.inventory, (TileEntityBarrel) tile, player);
            default -> null;
        };
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile == null) return null;

        return switch (ID) {
            case GUI_BARREL -> new GuiBarrel(player.inventory, (TileEntityBarrel) tile, player);
            default -> null;
        };
    }
}
