package com.serenibyss.etfuturum.mixin.phantom;

import com.serenibyss.etfuturum.world.spawner.PhantomSpawner;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldServer.class)
abstract class WorldServerMixin extends World implements IThreadListener {

    @Unique
    private PhantomSpawner phantomSpawner = new PhantomSpawner();

    protected WorldServerMixin(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    @Inject(method="tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldEntitySpawner;findChunksForSpawning(Lnet/minecraft/world/WorldServer;ZZZ)I"))
    public void etfuturum$tickPhantomSpawner(CallbackInfo ci) {
        phantomSpawner.spawnMobs(this, spawnHostileMobs, spawnPeacefulMobs);
    }
}
