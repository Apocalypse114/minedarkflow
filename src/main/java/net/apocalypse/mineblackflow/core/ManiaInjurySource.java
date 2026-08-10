package net.apocalypse.mineblackflow.core;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record ManiaInjurySource<T>(T cause, T direct, @Nullable Vec3 pos, FactorType type) {

    public static ManiaInjurySource<DamageSource> fromDamageSource(DamageSource source) {
        return new ManiaInjurySource<>(source, source, source.getSourcePosition(), FactorType.DAMAGE);
    }
    public static ManiaInjurySource<Entity> fromEntity(Entity causing, Entity direct) {
        return new ManiaInjurySource<>(causing, direct, causing.position(),
                causing == direct ? FactorType.ENTITY_DIRECT : FactorType.ENTITY_INDIRECT);
    }
    public static ManiaInjurySource<Entity> fromEntity(Entity cause){
        return fromEntity(cause, cause);
    }
    public static ManiaInjurySource<BlockState> fromBlock(BlockPos pos, BlockState state) {
        return new ManiaInjurySource<>(state, state, pos.getCenter(), FactorType.BLOCK);
    }
    public static ManiaInjurySource<ItemStack> fromItemStack(ItemStack stack) {
        return new ManiaInjurySource<>(stack, stack, null, FactorType.ITEM);
    }
    public static ManiaInjurySource<MobEffect> fromEffect(MobEffect effect){
        return new ManiaInjurySource<>(effect, effect, null,FactorType.EFFECT);
    }
    public static ManiaInjurySource<?> fromNothing() {
        return new ManiaInjurySource<>(null, null, null, FactorType.VOID);
    }
    public static ManiaInjurySource<CommandSourceStack> fromCommand(CommandSourceStack source){
        return new ManiaInjurySource<>(source, source, source.getPosition(), FactorType.COMMAND);
    }

    //用于快速简单判断来源类型
    public enum FactorType {
        DAMAGE, ENTITY_DIRECT, ENTITY_INDIRECT, BLOCK, ITEM, VOID, COMMAND, EFFECT, OTHER;
    }
}