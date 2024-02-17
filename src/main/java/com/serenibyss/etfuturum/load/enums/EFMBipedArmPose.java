package com.serenibyss.etfuturum.load.enums;

import com.serenibyss.etfuturum.load.feature.Feature;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.serenibyss.etfuturum.load.feature.Features.*;

@SideOnly(Side.CLIENT)
public class EFMBipedArmPose {

    public static ModelBiped.ArmPose THROW_SPEAR;

    public static void init() {
        THROW_SPEAR = addArmPose(MC13.trident, "throw_spear");
    }

    private static ModelBiped.ArmPose addArmPose(Feature feature, String name) {
        if (!feature.isEnabled()) {
            return null;
        }
        return EnumHelper.addEnum(ModelBiped.ArmPose.class, name, new Class[0]);
    }
}
