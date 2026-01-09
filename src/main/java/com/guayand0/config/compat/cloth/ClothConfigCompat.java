package com.guayand0.config.compat.cloth;

import com.guayand0.config.Drop2InvConfig;
import com.guayand0.config.Drop2InvConfigManager;
import com.guayand0.mobs.MobCategory;
import com.guayand0.mobs.config.MobConfigManager;
import com.guayand0.mobs.utils.MobUtils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class ClothConfigCompat {

    private static final String CONFIG_VALUE = "drop2inv.category.";
    private record MobEntry(String mobId, String displayName) {}

    public static Screen create(Screen parent) {
        Drop2InvClothConfig clothConfig = new Drop2InvClothConfig();
        Drop2InvConfig config = Drop2InvConfigManager.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Drop2Inv Config"))
                .setSavingRunnable(clothConfig::saveToManager);

        ConfigEntryBuilder entry = builder.entryBuilder();




        // GENERAL
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable(CONFIG_VALUE + "general"));

        general.addEntry(
                entry.startBooleanToggle(
                        Text.translatable(CONFIG_VALUE + "general.enabled"), config.enabled)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "general.enabled.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.enabled)
                        .setSaveConsumer(v -> {
                            config.enabled = v;        // modifica config real
                            clothConfig.enabled = v;   // sincroniza ClothConfig
                        }).build()
        );




        // BLOQUES
        ConfigCategory blocks = builder.getOrCreateCategory(Text.translatable(CONFIG_VALUE + "blocks"));

        blocks.addEntry(
                entry.startBooleanToggle(
                        Text.translatable(CONFIG_VALUE + "blocks.blocks_to_inv"), config.blocks.blocks_to_inv)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "blocks.blocks_to_inv.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.blocks.blocks_to_inv)
                        .setSaveConsumer(v -> config.blocks.blocks_to_inv = v).build()
        );




        SubCategoryBuilder blocksSpecial = entry.startSubCategory(Text.translatable(CONFIG_VALUE + "blocks.special"));

        blocksSpecial.add(
                entry.startBooleanToggle(
                                Text.translatable(CONFIG_VALUE + "blocks.special.break_tree_logs"), config.blocks.break_tree_logs)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "blocks.special.break_tree_logs.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.blocks.break_tree_logs)
                        .setSaveConsumer(v -> config.blocks.break_tree_logs = v).build()
        );
        blocksSpecial.add(
                entry.startBooleanToggle(
                                Text.translatable(CONFIG_VALUE + "blocks.special.break_tree_leaf"), config.blocks.break_tree_leaf)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "blocks.special.break_tree_leaf.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.blocks.break_tree_leaf)
                        .setSaveConsumer(v -> config.blocks.break_tree_leaf = v).build()
        );
        /*blocksSpecial.add(
                entry.startBooleanToggle(
                                Text.translatable(CONFIG_VALUE + "blocks.special.break_crops"), config.blocks.break_crops)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "blocks.special.break_crops.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.blocks.break_crops)
                        .setSaveConsumer(v -> config.blocks.break_crops = v).build()
        );*/
        blocksSpecial.add(
                entry.startBooleanToggle(
                                Text.translatable(CONFIG_VALUE + "blocks.special.break_vertical"), config.blocks.break_vertical)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "blocks.special.break_vertical.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.blocks.break_vertical)
                        .setSaveConsumer(v -> config.blocks.break_vertical = v).build()
        );
        blocksSpecial.add(
                entry.startBooleanToggle(
                                Text.translatable(CONFIG_VALUE + "blocks.special.break_chorus"), config.blocks.break_chorus)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "blocks.special.break_chorus.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.blocks.break_chorus)
                        .setSaveConsumer(v -> config.blocks.break_chorus = v).build()
        );
        blocks.addEntry(blocksSpecial.build());




		//MOBS
        ConfigCategory mobs = builder.getOrCreateCategory(Text.translatable(CONFIG_VALUE + "mobs"));

        mobs.addEntry(
                entry.startBooleanToggle(
                        Text.translatable(CONFIG_VALUE + "mobs.mobs_to_inv"), config.mobs.mobs_to_inv)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "mobs.mobs_to_inv.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.mobs.mobs_to_inv)
                        .setSaveConsumer(v -> config.mobs.mobs_to_inv = v).build()
        );




        SubCategoryBuilder mobsSpecial = entry.startSubCategory(Text.translatable(CONFIG_VALUE + "mobs.special"));

        mobsSpecial.add(
                entry.startBooleanToggle(
                                Text.translatable(CONFIG_VALUE + "mobs.special.sheep_shear"), config.mobs.sheep_shear)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "mobs.special.sheep_shear.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.mobs.sheep_shear)
                        .setSaveConsumer(v -> config.mobs.sheep_shear = v).build()
        );
        mobs.addEntry(mobsSpecial.build());




        SubCategoryBuilder mobsCategory = entry.startSubCategory(Text.translatable(CONFIG_VALUE + "mobs.category"));

        mobsCategory.add(
                entry.startBooleanToggle(
                        Text.translatable(CONFIG_VALUE + "mobs.category.hostile"), config.mobs.hostile)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "mobs.category.hostile.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.mobs.hostile)
                        .setSaveConsumer(v -> config.mobs.hostile = v).build()
        );
        mobsCategory.add(
                entry.startBooleanToggle(
                        Text.translatable(CONFIG_VALUE + "mobs.category.neutral"), config.mobs.neutral)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "mobs.category.neutral.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.mobs.neutral)
                        .setSaveConsumer(v -> config.mobs.neutral = v).build()
        );
        mobsCategory.add(
                entry.startBooleanToggle(
                        Text.translatable(CONFIG_VALUE + "mobs.category.passive"), config.mobs.passive)
                        .setTooltip(Text.translatable(CONFIG_VALUE + "mobs.category.passive.@Tooltip"))
                        .setDefaultValue(Drop2InvConfig.DEFAULTS.mobs.passive)
                        .setSaveConsumer(v -> config.mobs.passive = v).build()
        );
        mobs.addEntry(mobsCategory.build());




        SubCategoryBuilder perMobCategory = entry.startSubCategory(Text.translatable(CONFIG_VALUE + "mobs.per_mob_category"));

        MobConfigManager mobConfig = MobConfigManager.get();

        Set<String> allMobs = new HashSet<>();
        allMobs.addAll(mobConfig.getPassive());
        allMobs.addAll(mobConfig.getNeutral());
        allMobs.addAll(mobConfig.getHostile());

        List<MobEntry> mobsSorted = allMobs.stream()
                .map(mobId -> {
                    Identifier id = Identifier.tryParse(mobId);
                    if (id == null) return null;
                    if (!Registries.ENTITY_TYPE.containsId(id)) return null;

                    String key = "entity." + id.getNamespace() + "." + id.getPath();
                    String name = Text.translatable(key).getString().replace("_", " ").toUpperCase();

                    return new MobEntry(mobId, name);
                }).filter(Objects::nonNull).sorted(Comparator.comparing(MobEntry::displayName)).toList();

        for (MobEntry entryMob : mobsSorted) {

            String mobId = entryMob.mobId();
            String displayName = entryMob.displayName();
            Identifier id = Identifier.tryParse(mobId);

            MobCategory defaultCategory = MobUtils.getDefaultCategory(mobId, mobConfig);
            MobCategory currentCategory = config.mobs.individual_category.getOrDefault(mobId, defaultCategory);

            String tooltipMobId = id.getPath().toUpperCase();
            String tooltipText = Text.translatable(CONFIG_VALUE + "mobs.per_mob_category.individual.@Tooltip", tooltipMobId).getString();

            perMobCategory.add(
                    entry.startEnumSelector(
                            Text.literal(displayName), MobCategory.class, currentCategory)
                            .setTooltip(Text.literal(tooltipText))
                            .setDefaultValue(defaultCategory)
                            .setSaveConsumer(v -> config.mobs.individual_category.put(mobId, v)).build()
            );
        }
        mobs.addEntry(perMobCategory.build());

        return builder.build();
    }
}
