package io.github.teamgalacticraft.galacticraft.energy;

import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;

/**
 * @author <a href="https://github.com/teamgalacticraft">TeamGalacticraft</a>
 */
public class OxygenEnergyType implements EnergyType {
    @Override
    public int getMaximumTransferSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public TextComponent getDisplayAmount(int amount) {
        return new StringTextComponent(String.valueOf(amount));
    }

    @Override
    public boolean isCompatibleWith(EnergyType type) {
        return type == this;
    }

    @Override
    public int convertFrom(EnergyType type, int amount) {
        return isCompatibleWith(type) ? amount : 0;
    }

    @Override
    public int convertTo(EnergyType type, int amount) {
        return isCompatibleWith(type) ? amount : 0;
    }
}