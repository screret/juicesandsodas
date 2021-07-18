package io.screret.github.juicesandsodas.containers;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.screret.github.juicesandsodas.Base;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BlenderBlockScreen extends ContainerScreen<BlenderBlockContainer> {

    public static final int PLAYER_INVENTORY_YPOS = 125;

    private ResourceLocation GUI = new ResourceLocation(Base.MODID, "textures/gui/blender_gui.png");

    public BlenderBlockScreen(BlenderBlockContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.width = 174;
        this.height = 164;
    }

    final static int COOK_BAR_XPOS = 49;
    final static  int COOK_BAR_YPOS = 60;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 80;
    final static  int COOK_BAR_HEIGHT = 17;

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        // draw the label for the top of the screen
        final int LABEL_XPOS = 5;
        final int LABEL_YPOS = 5;
        this.font.draw(matrixStack, this.title.getString(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());     ///    this.font.drawString

        // draw the label for the player inventory slots
        this.font.draw(matrixStack, this.minecraft.player.inventory.getDisplayName().getString(),                  ///    this.font.drawString
                4, 74, Color.darkGray.getRGB());
    }

    public void tick() {
        super.tick();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.getXSize(), this.getYSize());


        int l = this.menu.getCookProgressionScaled();
        this.blit(matrixStack, this.leftPos + 81, this.topPos + 33, 176, 14, l + 1, 16);
    }
}
