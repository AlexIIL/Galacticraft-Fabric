package io.github.teamgalacticraft.galacticraft.blocks.machines.refinery;

import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;

public enum RefineryStatus {

    /**
     * Generator is active and is generating energy.
     */
    REFINING(new TranslatableTextComponent("ui.galacticraft-rewoven.machinestatus.refining").setStyle(new Style().setColor(TextFormat.GREEN)).getFormattedText()),
    /**
     * Generator has fuel but buffer is full.
     */
    IDLE(new TranslatableTextComponent("ui.galacticraft-rewoven.machinestatus.idle").setStyle(new Style().setColor(TextFormat.GOLD)).getFormattedText()),
    /**
     * The generator has no fuel.
     */
    INACTIVE(new TranslatableTextComponent("ui.galacticraft-rewoven.machinestatus.inactive").setStyle(new Style().setColor(TextFormat.GRAY)).getFormattedText());

    private String name;

    RefineryStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

