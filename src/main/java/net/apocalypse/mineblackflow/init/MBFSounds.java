package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MBFSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MineBlackFlow.MODID);

    public static final RegistryObject<SoundEvent> ANIMAL = register("animal", true);
    public static final RegistryObject<SoundEvent> MANIA_BREAK = register("mania_break", false);
    public static final RegistryObject<SoundEvent> DOG_AMBIENT = register("dog_ambient", false);
    public static final RegistryObject<SoundEvent> DOG_HURT = register("dog_hurt", false);
    public static final RegistryObject<SoundEvent> DOG_DIE = register("dog_die", false);
    public static final RegistryObject<SoundEvent> ELEPHANT_AMBIENT = register("elephant_ambient", false);
    public static final RegistryObject<SoundEvent> ELEPHANT_HURT = register("elephant_hurt", false);
    public static final RegistryObject<SoundEvent> ELEPHANT_DIE = register("elephant_die", false);
    public static final RegistryObject<SoundEvent> SHEEP_AMBIENT = register("sheep_ambient", false);
    public static final RegistryObject<SoundEvent> SHEEP_HURT = register("sheep_hurt", false);
    public static final RegistryObject<SoundEvent> SHEEP_DIE = register("sheep_die", false);
    public static final RegistryObject<SoundEvent> SHEEP_ATTACK_PRE = register("sheep_attack_pre", false);
    public static final RegistryObject<SoundEvent> SHEEP_ATTACK_HIT = register("sheep_attack_done", false);
    public static final RegistryObject<SoundEvent> MOUSE_AMBIENT = register("mouse_ambient", false);
    public static final RegistryObject<SoundEvent> MOUSE_HURT = register("mouse_hurt", false);
    public static final RegistryObject<SoundEvent> MOUSE_DIE = register("mouse_die", false);
    public static final RegistryObject<SoundEvent> MOUSE_ATTACK_PRE = register("mouse_attack_pre", false);
    public static final RegistryObject<SoundEvent> MOUSE_ATTACK_HIT = register("mouse_attack_done", false);
    public static final RegistryObject<SoundEvent> TP_START = register("tp_start", false);
    public static final RegistryObject<SoundEvent> TP_DONE = register("tp_done", false);

    public static RegistryObject<SoundEvent> register(String name, boolean fixed){
        return fixed ? REGISTRY.register(name, ()->SoundEvent.createFixedRangeEvent(MineBlackFlow.modLoc(name), 1))
                : REGISTRY.register(name, ()->SoundEvent.createVariableRangeEvent(MineBlackFlow.modLoc(name)));
    }
}
