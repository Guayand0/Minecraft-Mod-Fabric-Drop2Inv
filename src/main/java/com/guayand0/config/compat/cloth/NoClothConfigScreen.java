package com.guayand0.config.compat.cloth;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NoClothConfigScreen extends Screen {

    private final Screen parent;
    private static final String CONFIG_VALUE = "drop2inv.no_cloth_config.";

    public NoClothConfigScreen(Screen parent) {
        super(Text.literal("Drop2Inv Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int buttonY = this.height / 2 + 20;

        // Texto 1
        String msg1 = Text.translatable(CONFIG_VALUE + "L1").getString();
        int w1 = this.textRenderer.getWidth(msg1);

        TextFieldWidget text1 = new TextFieldWidget(this.textRenderer, centerX - w1 / 2- 4, buttonY - 50, w1 + 10, 18, Text.empty());
        text1.setMaxLength(msg1.length());
        text1.setText(msg1);
        text1.setEditable(false);
        text1.setDrawsBackground(false);
        this.addDrawableChild(text1);

        // Texto 2
        String msg2 = Text.translatable(CONFIG_VALUE + "L2").getString();
        int w2 = this.textRenderer.getWidth(msg2);

        TextFieldWidget text2 = new TextFieldWidget(this.textRenderer, centerX - w2 / 2 - 4, buttonY - 30, w2 + 10, 18, Text.empty());
        text2.setMaxLength(msg2.length());
        text2.setText(msg2);
        text2.setEditable(false);
        text2.setDrawsBackground(false);
        this.addDrawableChild(text2);

        // Botón volver
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Volver"), button ->
                        this.client.setScreen(parent)).dimensions(centerX - 50, buttonY, 100, 20).build()
        );
    }

}
