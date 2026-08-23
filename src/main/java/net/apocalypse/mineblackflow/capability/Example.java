package net.apocalypse.mineblackflow.capability;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Example {
    public static double global_session_example = 0;
    public static com.google.gson.JsonArray jsonarray_example = new com.google.gson.JsonArray();
    public static com.google.gson.JsonObject jsonobject_example = new com.google.gson.JsonObject();
}
