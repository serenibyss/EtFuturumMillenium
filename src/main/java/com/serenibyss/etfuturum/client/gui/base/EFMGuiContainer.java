package com.serenibyss.etfuturum.client.gui.base;

import com.serenibyss.etfuturum.containers.base.EFMContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public abstract class EFMGuiContainer<T extends EFMContainer> extends GuiContainer {

    public EFMGuiContainer(T inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @SuppressWarnings("unchecked")
    public T getContainer() {
        return (T) inventorySlots;
    }

    public ITextComponent getTitle() {
        return getContainer().getTitle();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheelMovement = Mouse.getEventDWheel();
        if (wheelMovement != 0) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            mouseScrolled(mouseX, mouseY, wheelMovement);
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, int wheelMovement) {}
}
