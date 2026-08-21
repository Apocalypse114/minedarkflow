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
    public static LivingSoundSet DOG = LivingSoundSet.create("dog");
    public static LivingSoundSet ELEPHANT = LivingSoundSet.create("elephant");
    public static LivingSoundSet SHEEP = LivingSoundSet.create("sheep");
    public static final RegistryObject<SoundEvent> SHEEP_ATTACK_PRE = register("sheep_attack_pre", false);
    public static final RegistryObject<SoundEvent> SHEEP_ATTACK_HIT = register("sheep_attack_done", false);
    public static LivingSoundSet MOUSE = LivingSoundSet.create("mouse");
    public static final RegistryObject<SoundEvent> MOUSE_ATTACK_PRE = register("mouse_attack_pre", false);
    public static final RegistryObject<SoundEvent> MOUSE_ATTACK_HIT = register("mouse_attack_done", false);
    public static final RegistryObject<SoundEvent> TP_START = register("tp_start", false);
    public static final RegistryObject<SoundEvent> TP_DONE = register("tp_done", false);
    public static LivingSoundSet DOG_PROTO = LivingSoundSet.create("dog_proto");
    public static final RegistryObject<SoundEvent> DOG_PROTO_ATTACK = register("dog_proto_attack", false);
    public static final RegistryObject<SoundEvent> DOG_PROTO_SKILL = register("dog_proto_skill", false);
    public static final RegistryObject<SoundEvent> DOG_PROTO_BITE = register("dog_proto_bite", false);

    public static RegistryObject<SoundEvent> register(String name, boolean fixed){
        return fixed ? REGISTRY.register(name, ()->SoundEvent.createFixedRangeEvent(MineBlackFlow.modLoc(name), 1))
                : REGISTRY.register(name, ()->SoundEvent.createVariableRangeEvent(MineBlackFlow.modLoc(name)));
    }
    public record LivingSoundSet(RegistryObject<SoundEvent> ambient,
                                 RegistryObject<SoundEvent> hurt,
                                 RegistryObject<SoundEvent> die){
        public static LivingSoundSet create(String name){
            RegistryObject<SoundEvent> a = register(name+"_ambient", false);
            RegistryObject<SoundEvent> h = register(name+"_hurt", false);
            RegistryObject<SoundEvent> d = register(name+"_die", false);
            return new LivingSoundSet(a, h, d);
        }
    }
}
