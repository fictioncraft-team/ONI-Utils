package wintersteve25.oniutils.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wintersteve25.oniutils.common.blocks.base.ONIBaseContainer;
import wintersteve25.oniutils.common.network.ONINetworking;
import wintersteve25.oniutils.common.network.TERedstoneButtonPacket;

public abstract class ONIBaseGuiContainer<T extends ONIBaseContainer> extends ContainerScreen<T> {

    public static ResourceLocation bg = null;

    protected final ONIBaseGuiTabInfo infoTab;
    protected final ONIBaseGuiTabAlert alertTab;
    protected ONIBaseGuiTabRedstone redstoneOutputTab;
    protected InfoButton infoButton;
    protected AlertButton alertButton;
    protected RedstoneButton redstoneButton;
    protected RedstoneOutputButton redstoneOutputButton;

    public ONIBaseGuiContainer(T container, PlayerInventory inv, ITextComponent name, ResourceLocation resourceLocation) {
        super(container, inv, name);

        bg = resourceLocation;
        infoTab = new ONIBaseGuiTabInfo();
        alertTab = new ONIBaseGuiTabAlert();

        if (hasRedstoneOutputButton()) {
            redstoneOutputTab = new ONIBaseGuiTabRedstone();
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        infoTab.updateText();
        infoTab.render(matrixStack, mouseX, mouseY, partialTicks);
        alertTab.render(matrixStack, mouseX, mouseY, partialTicks);

        if (hasRedstoneOutputButton()) {
            redstoneOutputTab.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void tick() {
        if (hasRedstoneOutputButton()) {
            this.redstoneOutputTab.tick();
        }

        super.tick();
    }

    @Override
    public void onClose() {
        if (hasRedstoneOutputButton()) {
            this.redstoneOutputTab.removed();
        }

        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (hasRedstoneOutputButton()) {
            return !this.redstoneOutputTab.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        super.charTyped(codePoint, modifiers);
        if (hasRedstoneOutputButton()) {
            return this.redstoneOutputButton.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hasRedstoneOutputButton()) {
            if (this.redstoneButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            } else {
                return this.redstoneOutputTab.isVisible() || super.mouseClicked(mouseX, mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = this.infoTab.getGuiLeftTopPosition(this.width, this.xSize);
        this.guiTop = (this.height - this.getYSize()) / 2;

        this.infoTab.init(this.width, this.height, this.minecraft, this.container, this.container.tabTitle());
        this.infoTab.updateText();

        this.alertTab.init(this.width, this.height, this.minecraft, this.container, "oniutils.gui.titles.warning");

        if (hasRedstoneOutputButton()) {
            this.redstoneOutputTab.init(this.width, this.height, this.minecraft, this.container, "oniutils.gui.titles.redstoneOutput");
            this.redstoneOutputButton = new RedstoneOutputButton();
            addButton(redstoneOutputButton);
            this.children.add(this.redstoneOutputTab);
        }
        this.infoButton = new InfoButton();
        this.alertButton = new AlertButton();
        this.redstoneButton = new RedstoneButton();

        addButton(infoButton);
        addButton(alertButton);
        addButton(redstoneButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (bg != null) {
            RenderSystem.color4f(1F, 1F, 1F, 1F);
            this.minecraft.getTextureManager().bindTexture(bg);

            int j = (this.height - this.getYSize()) / 2;

            this.blit(matrixStack, this.guiLeft, j, 0, 0, this.getXSize(), this.getYSize() + 5);
        }
    }

    protected void powerToolTip(MatrixStack matrixStack, int power, int mouseX, int mouseY, int i, int j) {
        if (mouseX > i + 105 && mouseY > j + 24 && mouseX < i + 122 && mouseY < j + 73) {
            renderTooltip(matrixStack, new TranslationTextComponent("oniutils.gui.machines.power", power), mouseX, mouseY);
        }
    }

    protected void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int i, int j, int cord1, int cord1Y, int cord2, int cord2Y, ITextComponent text) {
        if (mouseX > i + cord1 && mouseY > j + cord1Y && mouseX < i + cord2 && mouseY < j + cord2Y) {
            renderTooltip(matrixStack, text, mouseX, mouseY);
        }
    }

    protected int getProgressScaled(int pixels) {
        int i = container.getProgress();
        int j = container.getTotalProgress();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class Button extends AbstractButton {
        protected Button(int x, int y) {
            super(x, y, 16, 16, StringTextComponent.EMPTY);
        }

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(ONIBaseGuiContainer.bg);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            int j = 0;

            if (this.isHovered()) {
                j += this.width;
            }

            this.blit(matrixStack, this.x, this.y, j, 240, this.width, this.height);
            this.blitOverlay(matrixStack);
        }

        public void setPosition(int xIn, int yIn) {
            this.x = xIn;
            this.y = yIn;
        }

        protected abstract void blitOverlay(MatrixStack matrixStack);
    }

    @OnlyIn(Dist.CLIENT)
    protected class InfoButton extends SpriteButton {
        public InfoButton() {
            super(ONIBaseGuiContainer.this.guiLeft+1, ONIBaseGuiContainer.this.guiTop-17, 32, 240);
        }

        public void onPress() {
            if (ONIBaseGuiContainer.this.alertTab.isVisible()) {
                ONIBaseGuiContainer.this.alertTab.toggleVisibility();
            }

            if (hasRedstoneOutputButton()) {
                if (ONIBaseGuiContainer.this.redstoneOutputTab.isVisible()) {
                    ONIBaseGuiContainer.this.redstoneOutputTab.toggleVisibility();
                }

                ONIBaseGuiContainer.this.redstoneOutputButton.setPosition(ONIBaseGuiContainer.this.guiLeft+52, ONIBaseGuiContainer.this.guiTop-17);
            }

            ONIBaseGuiContainer.this.infoTab.toggleVisibility();
            ONIBaseGuiContainer.this.guiLeft = ONIBaseGuiContainer.this.infoTab.getGuiLeftTopPosition(ONIBaseGuiContainer.this.width, ONIBaseGuiContainer.this.xSize);

            this.setPosition(ONIBaseGuiContainer.this.guiLeft+1, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.alertButton.setPosition(ONIBaseGuiContainer.this.guiLeft+18, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.redstoneButton.setPosition(ONIBaseGuiContainer.this.guiLeft+35, ONIBaseGuiContainer.this.guiTop-17);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected class AlertButton extends SpriteButton {
        public AlertButton() {
            super(ONIBaseGuiContainer.this.guiLeft+18, ONIBaseGuiContainer.this.guiTop-17, 49, 240);
        }

        public void onPress() {
            if (ONIBaseGuiContainer.this.infoTab.isVisible()) {
                ONIBaseGuiContainer.this.infoTab.toggleVisibility();
            }

            if (hasRedstoneOutputButton()) {
                if (ONIBaseGuiContainer.this.redstoneOutputTab.isVisible()) {
                    ONIBaseGuiContainer.this.redstoneOutputTab.toggleVisibility();
                }

                ONIBaseGuiContainer.this.redstoneOutputButton.setPosition(ONIBaseGuiContainer.this.guiLeft+52, ONIBaseGuiContainer.this.guiTop-17);
            }

            ONIBaseGuiContainer.this.alertTab.toggleVisibility();
            ONIBaseGuiContainer.this.guiLeft = ONIBaseGuiContainer.this.alertTab.getGuiLeftTopPosition(ONIBaseGuiContainer.this.width, ONIBaseGuiContainer.this.xSize);

            this.setPosition(ONIBaseGuiContainer.this.guiLeft+18, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.infoButton.setPosition(ONIBaseGuiContainer.this.guiLeft+1, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.redstoneButton.setPosition(ONIBaseGuiContainer.this.guiLeft+35, ONIBaseGuiContainer.this.guiTop-17);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected class RedstoneOutputButton extends SpriteButton {
        public RedstoneOutputButton() {
            super(ONIBaseGuiContainer.this.guiLeft+52, ONIBaseGuiContainer.this.guiTop-17, 66, 240);
        }

        public void onPress() {
            if (ONIBaseGuiContainer.this.infoTab.isVisible()) {
                ONIBaseGuiContainer.this.infoTab.toggleVisibility();
            }

            if (ONIBaseGuiContainer.this.alertTab.isVisible()) {
                ONIBaseGuiContainer.this.alertTab.toggleVisibility();
            }

            ONIBaseGuiContainer.this.redstoneOutputTab.toggleVisibility();
            ONIBaseGuiContainer.this.guiLeft = ONIBaseGuiContainer.this.redstoneOutputTab.getGuiLeftTopPosition(ONIBaseGuiContainer.this.width, ONIBaseGuiContainer.this.xSize);

            this.setPosition(ONIBaseGuiContainer.this.guiLeft+52, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.infoButton.setPosition(ONIBaseGuiContainer.this.guiLeft+1, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.alertButton.setPosition(ONIBaseGuiContainer.this.guiLeft+18, ONIBaseGuiContainer.this.guiTop-17);
            ONIBaseGuiContainer.this.redstoneButton.setPosition(ONIBaseGuiContainer.this.guiLeft+35, ONIBaseGuiContainer.this.guiTop-17);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected class RedstoneButton extends SpriteButton {
        public RedstoneButton() {
            super(ONIBaseGuiContainer.this.guiLeft+35, ONIBaseGuiContainer.this.guiTop-17, 83, 240);
        }

        boolean pressed = false;

        public void onPress() {
            if (!pressed) {
                setU(100);
                pressed = true;
            } else {
                setU(83);
                pressed = false;
            }
            ONINetworking.sendToServer(new TERedstoneButtonPacket(container.getTileEntity()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract static class SpriteButton extends Button {
        protected int u;
        protected int v;

        protected SpriteButton(int x, int y, int u, int v) {
            super(x, y);

            this.u = u;
            this.v = v;
        }

        protected void setU(int u) {
            this.u = u;
        }

        protected void setV(int v) {
            this.v = v;
        }

        protected void blitOverlay(MatrixStack matrixStack) {
            this.blit(matrixStack, this.x, this.y, this.u, this.v, 16, 16);
        }
    }

    protected abstract boolean hasRedstoneOutputButton();
}