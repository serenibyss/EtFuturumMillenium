package com.serenibyss.etfuturum.client.particle;

import com.serenibyss.etfuturum.EFMTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleNautilus extends Particle {

    private final double coordX;
    private final double coordY;
    private final double coordZ;

    public ParticleNautilus(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.coordX = xCoord;
        this.coordY = yCoord;
        this.coordZ = zCoord;
        this.prevPosX = xCoord + xSpeed;
        this.prevPosY = yCoord + ySpeed;
        this.prevPosZ = zCoord + zSpeed;
        this.posX = this.prevPosX;
        this.posY = this.prevPosY;
        this.posZ = this.prevPosZ;
        this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleRed = 0.9F * f;
        this.particleGreen = 0.9F * f;
        this.particleBlue = f;
        this.canCollide = false;
        this.particleMaxAge = (int) (Math.random() * 10.0D) + 30;
        this.setParticleTexture(EFMParticleHandler.NAUTILUS);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        int i = super.getBrightnessForRender(partialTick);
        float f = (float) this.particleAge / (float) this.particleMaxAge;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int) (f * 15.0F * 16.0F);

        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float) this.particleAge / (float) this.particleMaxAge;
        f = 1.0F - f;
        float f1 = 1.0F - f;
        f1 = f1 * f1;
        f1 = f1 * f1;
        this.posX = this.coordX + this.motionX * (double) f;
        this.posY = this.coordY + this.motionY * (double) f - (double) (f1 * 1.2F);
        this.posZ = this.coordZ + this.motionZ * (double) f;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
    }
}
