//package com.guayand0.mixin.client;
//
//import net.minecraft.entity.LivingEntity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(LivingEntity.class)
//public class ExampleClientMixin {
//	@Inject(
//			method = "dropLoot",
//			at = @At("TAIL")
//	)
//	private void init(CallbackInfo info) {
//		// This code is injected into the start of Minecraft.run()V
//	}
//}