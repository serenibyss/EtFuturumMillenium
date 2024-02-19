package com.serenibyss.etfuturum.containers;

import com.serenibyss.etfuturum.api.StonecutterRegistry;
import com.serenibyss.etfuturum.blocks.EFMBlocks;
import com.serenibyss.etfuturum.containers.base.EFMContainer;
import com.serenibyss.etfuturum.containers.base.IntReferenceHolder;
import com.serenibyss.etfuturum.sounds.EFMSounds;
import com.serenibyss.etfuturum.util.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class ContainerStonecutter extends EFMContainer {

    private final IntReferenceHolder recipeIndex = IntReferenceHolder.standalone();

    private final World world;
    private final BlockPos pos;
    private long lastSoundTime;

    private List<ItemStack> recipeOutputs = Collections.emptyList();
    private ItemStack inputStack = ItemStack.EMPTY;
    private final Slot inputSlot;
    private final Slot outputSlot;

    private final InventoryCraftResult resultInventory = new InventoryCraftResult();
    private final IInventory inputInventory = new InventoryStonecutter();

    private Runnable inventoryUpdateListener = () -> {};

    public ContainerStonecutter(EntityPlayer player, World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
        this.inputSlot = this.addSlotToContainer(new Slot(inputInventory, 0, 20, 33));
        this.outputSlot = this.addSlotToContainer(new SlotStonecutter(resultInventory, 1, 143, 33));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(recipeIndex);
    }

    @Override
    public ITextComponent getTitle() {
        return new TextComponentTranslation("container.stonecutter");
    }

    @SideOnly(Side.CLIENT)
    public int getRecipeIndex() {
        return recipeIndex.get();
    }

    @SideOnly(Side.CLIENT)
    public List<ItemStack> getRecipeOutputs() {
        return this.recipeOutputs;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasInputItem() {
        return inputSlot.getHasStack() && !recipeOutputs.isEmpty();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return isWithinUsableDistance(world, pos, player, EFMBlocks.STONECUTTER.getBlock());
    }

    @Override
    public boolean clickMenuButton(EntityPlayer playerIn, int id) {
        if (isValidRecipeIndex(id)) {
            recipeIndex.set(id);
            setupResultSlot();
        }
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        ItemStack stack = inputSlot.getStack();
        if (!ItemStackUtils.areItemStacksEqualIgnoreCount(stack, inputStack)) {
            this.inputStack = stack.copy();
            updateAvailableRecipes();
        }
    }

    private void updateAvailableRecipes() {
        this.recipeOutputs = Collections.emptyList();
        this.recipeIndex.set(-1);
        this.outputSlot.putStack(ItemStack.EMPTY);
        if (!inputStack.isEmpty()) {
            this.recipeOutputs = StonecutterRegistry.instance().getResultsForInput(inputStack);
        }
    }

    private void setupResultSlot() {
        if (!this.recipeOutputs.isEmpty() && isValidRecipeIndex(recipeIndex.get())) {
            this.outputSlot.putStack(this.recipeOutputs.get(recipeIndex.get()).copy());
        } else {
            this.outputSlot.putStack(ItemStack.EMPTY);
        }
        this.detectAndSendChanges();
    }

    private boolean isValidRecipeIndex(int index) {
        return index >= 0 && index < this.recipeOutputs.size();
    }

    @SideOnly(Side.CLIENT)
    public void setInventoryUpdateListener(Runnable runnable) {
        this.inventoryUpdateListener = runnable;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            Item slotItem = slotStack.getItem();
            stack = slotStack.copy();
            if (index == 1) {
                slotItem.onCreated(slotStack, player.world, player);
                if (!this.mergeItemStack(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index == 0) {
                if (!this.mergeItemStack(slotStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!StonecutterRegistry.instance().getResultsForInput(slotStack).isEmpty()) {
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!this.mergeItemStack(slotStack, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.mergeItemStack(slotStack, 2, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }

            slot.onSlotChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            this.detectAndSendChanges();
        }
        return stack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.resultInventory.removeStackFromSlot(1);
        this.clearContainer(player, player.world, this.inputInventory);
    }

    private class InventoryStonecutter extends InventoryBasic {

        public InventoryStonecutter() {
            super(ContainerStonecutter.this.getTitle(), 1);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            ContainerStonecutter.this.onCraftMatrixChanged(this);
            ContainerStonecutter.this.inventoryUpdateListener.run();
        }
    }

    private class SlotStonecutter extends Slot {

        public SlotStonecutter(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack onTake(EntityPlayer player, ItemStack stack) {
            ItemStack remaining = ContainerStonecutter.this.inputSlot.decrStackSize(1);
            if (!remaining.isEmpty()) {
                ContainerStonecutter.this.setupResultSlot();
            }

            stack.getItem().onCreated(stack, player.world, player);
            long time = world.getTotalWorldTime();
            if (lastSoundTime != time) {
                world.playSound(null, pos, EFMSounds.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                lastSoundTime = time;
            }

            return super.onTake(player, stack);
        }
    }
}
