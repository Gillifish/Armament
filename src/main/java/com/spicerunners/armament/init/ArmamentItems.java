package com.spicerunners.armament.init;

import com.spicerunners.armament.ArmamentMain;
import com.spicerunners.armament.item.GhastBlasterItem;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

public class ArmamentItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ArmamentMain.MODID);
    public static final RegistryObject<Item> GHAST_BLASTER = REGISTRY.register("ghast_blaster", () -> new GhastBlasterItem());
}
