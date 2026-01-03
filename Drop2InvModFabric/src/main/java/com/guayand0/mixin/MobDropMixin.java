package com.guayand0.mixin;

import com.guayand0.logic.MobDropLogic;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MobDropMixin {

	@Inject(
			method = "dropLoot",
			at = @At("TAIL")
	)
	private void onDropStacks(ServerWorld world, DamageSource damageSource, boolean causedByPlayer, CallbackInfo ci) {
		LivingEntity mob = (LivingEntity)(Object)this;

		if (!(mob.getAttacker() instanceof ServerPlayerEntity player)) return;

		world.getEntitiesByClass(
				ItemEntity.class,
				mob.getBoundingBox().expand(1.5),
				item -> true
		).forEach(item -> MobDropLogic.give(player, item));
	}
}
