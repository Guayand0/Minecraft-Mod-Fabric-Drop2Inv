package com.guayand0.mixin;

import com.guayand0.config.Drop2InvConfig;
import com.guayand0.mobs.MobCategory;
import com.guayand0.mobs.logic.MobDropLogic;
import com.guayand0.mobs.utils.MobUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MobDropMixin {

    @Inject(method = "dropLoot", at = @At("TAIL"))
    private void onDropStacks(ServerWorld world, DamageSource damageSource, boolean causedByPlayer, CallbackInfo ci) {

        Drop2InvConfig config = AutoConfig.getConfigHolder(Drop2InvConfig.class).getConfig();
        if (!config.enabled) return;
        if (!config.mobs.mobs_to_inv) return;

        LivingEntity mob = (LivingEntity) (Object) this;
        if (!(mob.getAttacker() instanceof ServerPlayerEntity player)) return;

        EntityType<?> mobType = mob.getType();
        MobCategory category = MobUtils.getCategory(mobType);

        world.getEntitiesByClass(
                ItemEntity.class,
                mob.getBoundingBox(),
                item -> item.age <= 1
        ).forEach(item -> MobDropLogic.give(player, item, category));
    }
}
