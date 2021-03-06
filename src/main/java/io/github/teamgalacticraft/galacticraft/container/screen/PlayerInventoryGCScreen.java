package io.github.teamgalacticraft.galacticraft.container.screen;

import io.github.teamgalacticraft.galacticraft.Constants;
import io.github.teamgalacticraft.galacticraft.container.PlayerInventoryGCContainer;
import io.github.teamgalacticraft.galacticraft.items.GalacticraftItems;
import io.github.teamgalacticraft.galacticraft.items.OxygenTankItem;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.ingame.PlayerInventoryScreen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;

/**
 * @author <a href="https://github.com/teamgalacticraft">TeamGalacticraft</a>
 */
public class PlayerInventoryGCScreen extends ContainerScreen<PlayerInventoryGCContainer> {
    public static final Identifier BACKGROUND = new Identifier(Constants.MOD_ID, Constants.ScreenTextures.getRaw(Constants.ScreenTextures.PLAYER_INVENTORY_SCREEN));

    private float mouseX;
    private float mouseY;

    public PlayerInventoryGCScreen(PlayerEntity player) {
//        super(((GCPlayerAccessor) player).getGCContainer(), player.inventory, new TranslatableTextComponent(Constants.MOD_ID + ".player_inv_screen"));
        super(new PlayerInventoryGCContainer(player.inventory, !player.world.isClient, player), player.inventory, new TranslatableTextComponent(Constants.MOD_ID + ".player_inv_screen"));
    }

    public static boolean isCoordinateBetween(int coordinate, int min, int max) {
        int newMin = Math.min(min, max);
        int newMax = Math.max(min, max);
        return coordinate >= newMin && coordinate <= newMax;
    }

    @Override
    protected void drawMouseoverTooltip(int x, int y) {
        if (PlayerInventoryGCScreen.isCoordinateBetween(x, left + 138, left + 138 + 12)
                && PlayerInventoryGCScreen.isCoordinateBetween(y, top + 8, top + 8 + 40)) {
            ItemStack invStack = container.gearInventory.getInvStack(PlayerInventoryGCContainer.OXYGEN_TANK_1_SLOT);
            int storedOxy = invStack.isEmpty() ? 0 : OxygenTankItem.getOxygenCount(invStack);
            int maxOxy = invStack.isEmpty() ? 0 : OxygenTankItem.getMaxOxygen(invStack);
            this.renderTooltip("Tank 1 Oxygen: " + storedOxy + "/" + maxOxy, x, y);
        } else if (PlayerInventoryGCScreen.isCoordinateBetween(x, left + 156, left + 156 + 12)
                && PlayerInventoryGCScreen.isCoordinateBetween(y, top + 8, top + 8 + 40)) {
            ItemStack invStack = container.gearInventory.getInvStack(PlayerInventoryGCContainer.OXYGEN_TANK_2_SLOT);
            int storedOxy = invStack.isEmpty() ? 0 : OxygenTankItem.getOxygenCount(invStack);
            int maxOxy = invStack.isEmpty() ? 0 : OxygenTankItem.getMaxOxygen(invStack);
            this.renderTooltip("Tank 2 Oxygen: " + storedOxy + "/" + maxOxy, x, y);
        }
        super.drawMouseoverTooltip(x, y);
    }

    @Override
    public void render(int x, int y, float lastFrameDuration) {
        this.renderBackground();
        super.render(x, y, lastFrameDuration);
        this.drawMouseoverTooltip(x, y);

        this.mouseX = (float) x;
        this.mouseY = (float)/*y*/ minecraft.window.getScaledHeight() / 2;

        GuiLighting.enableForItems();
        this.itemRenderer.renderGuiItem(Items.CRAFTING_TABLE.getDefaultStack(), this.left + 6, this.top - 20);
        this.itemRenderer.renderGuiItem(GalacticraftItems.OXYGEN_MASK.getDefaultStack(), this.left + 35, this.top - 20);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        System.out.println("X: " + mouseX);
//        System.out.println("Y: " + mouseY);
//        System.out.println("b: " + button);
        boolean b = super.mouseClicked(mouseX, mouseY, button);

        if (PlayerInventoryGCScreen.isCoordinateBetween((int) Math.floor(mouseX), left, left + 29)
                && PlayerInventoryGCScreen.isCoordinateBetween((int) Math.floor(mouseY), top - 26, top)) {
            System.out.println("Clicked on vanilla tab!");
            minecraft.openScreen(new PlayerInventoryScreen(playerInventory.player));
        }

        return b;
    }

    @Override
    public void drawBackground(float v, int mouseX, int mouseY) {
//        this.drawTexturedReact(...)
        this.minecraft.getTextureManager().bindTexture(BACKGROUND);
        this.blit(this.left, this.top, 0, 0, this.containerWidth, this.containerHeight);

        int int_3 = this.left;
        int int_4 = this.top;
        PlayerInventoryScreen.drawEntity(int_3 + 51, int_4 + 75, 30, (float) (int_3 + 51) - this.mouseX, (float) (int_4 + 75 - 50) - this.mouseY, this.minecraft.player);

        this.minecraft.getTextureManager().bindTexture(BACKGROUND);

        //X,Y,blitOffset,u,v,width,height
        blit(this.left + 138, this.top + 8, 244, 0, 12, 40);
        blit(this.left + 156, this.top + 8, 244, 0, 12, 40);

        this.minecraft.getTextureManager().bindTexture(new Identifier(Constants.MOD_ID, Constants.ScreenTextures.getRaw(Constants.ScreenTextures.PLAYER_INVENTORY_TABS)));
        this.blit(this.left, this.top - 28, 0, 32, 57, 62);
    }
}