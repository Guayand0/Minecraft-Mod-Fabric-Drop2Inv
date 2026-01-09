package com.guayand0;

import com.guayand0.blocks.BlockBreakHandler;
import com.guayand0.blocks.DropCancelHandler;
import com.guayand0.config.Drop2InvConfigManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Drop2Inv implements ModInitializer {

	public static final String MOD_ID = "drop2inv";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		BlockBreakHandler.register();
		DropCancelHandler.register();

		Drop2InvConfigManager.load();
	}
}
