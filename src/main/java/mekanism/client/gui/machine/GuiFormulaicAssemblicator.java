package mekanism.client.gui.machine;

import java.util.Optional;
import mekanism.api.text.ILangEntry;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.button.ToggleButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.MekanismLang;
import mekanism.common.attachments.FormulaAttachment;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.tile.FormulaicAssemblicatorContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.item.ItemCraftingFormula;
import mekanism.common.network.PacketUtils;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteraction;
import mekanism.common.registries.MekanismAttachmentTypes;
import mekanism.common.tile.machine.TileEntityFormulaicAssemblicator;
import mekanism.common.util.text.BooleanStateDisplay.OnOff;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiFormulaicAssemblicator extends GuiConfigurableTile<TileEntityFormulaicAssemblicator, FormulaicAssemblicatorContainer> {

    private MekanismButton encodeFormulaButton;
    private MekanismButton stockControlButton;
    private MekanismButton fillEmptyButton;
    private MekanismButton craftSingleButton;
    private MekanismButton craftAvailableButton;
    private MekanismButton autoModeButton;

    public GuiFormulaicAssemblicator(FormulaicAssemblicatorContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        imageHeight += 64;
        inventoryLabelY = imageHeight - 94;
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 159, 15)).warning(WarningType.NOT_ENOUGH_ENERGY, () -> {
            if (tile.getAutoMode() && tile.hasRecipe()) {
                MachineEnergyContainer<TileEntityFormulaicAssemblicator> energyContainer = tile.getEnergyContainer();
                return energyContainer.getEnergyPerTick().greaterThan(energyContainer.getEnergy());
            }
            return false;
        });
        //Overwrite the output slots with a "combined" slot
        addRenderableWidget(new GuiSlot(SlotType.OUTPUT_LARGE, this, 115, 16));
        addRenderableWidget(new GuiProgress(() -> tile.getOperatingTicks() / (double) tile.getTicksRequired(), ProgressType.TALL_RIGHT, this, 86, 43).recipeViewerCrafting());
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::usedEnergy));
        encodeFormulaButton = addRenderableWidget(new MekanismImageButton(this, 7, 45, 14, getButtonLocation("encode_formula"),
              (element, mouseX, mouseY) -> PacketUtils.sendToServer(new PacketGuiInteract(GuiInteraction.ENCODE_FORMULA, ((GuiFormulaicAssemblicator) element.gui()).tile)),
              (element, graphics, mouseX, mouseY) -> element.displayTooltips(graphics, mouseX, mouseY, MekanismLang.ENCODE_FORMULA.translate())));
        stockControlButton = addRenderableWidget(new MekanismImageButton(this, 26, 75, 16, getButtonLocation("stock_control"),
              (element, mouseX, mouseY) -> PacketUtils.sendToServer(new PacketGuiInteract(GuiInteraction.STOCK_CONTROL_BUTTON, ((GuiFormulaicAssemblicator) element.gui()).tile)),
              (element, graphics, mouseX, mouseY) -> element.displayTooltips(graphics, mouseX, mouseY, MekanismLang.STOCK_CONTROL.translate(OnOff.of(((GuiFormulaicAssemblicator) element.gui()).tile.getStockControl())))));
        fillEmptyButton = addRenderableWidget(new ToggleButton(this, 44, 75, 16, 16, getButtonLocation("empty"),
              getButtonLocation("fill"), () -> tile.formula == null, (element, mouseX, mouseY) -> {
            TileEntityFormulaicAssemblicator tile = ((GuiFormulaicAssemblicator) element.gui()).tile;
            GuiInteraction interaction = tile.formula == null ? GuiInteraction.EMPTY_GRID : GuiInteraction.FILL_GRID;
            return PacketUtils.sendToServer(new PacketGuiInteract(interaction, tile));
        }, (element, graphics, mouseX, mouseY) -> {
            ILangEntry langEntry = ((GuiFormulaicAssemblicator) element.gui()).tile.formula == null ? MekanismLang.EMPTY_ASSEMBLICATOR : MekanismLang.FILL_ASSEMBLICATOR;
            element.displayTooltips(graphics, mouseX, mouseY, langEntry.translate());
        }));
        craftSingleButton = addRenderableWidget(new MekanismImageButton(this, 71, 75, 16, getButtonLocation("craft_single"),
              (element, mouseX, mouseY) -> PacketUtils.sendToServer(new PacketGuiInteract(GuiInteraction.CRAFT_SINGLE, ((GuiFormulaicAssemblicator) element.gui()).tile)),
              (element, graphics, mouseX, mouseY) -> element.displayTooltips(graphics, mouseX, mouseY, MekanismLang.CRAFT_SINGLE.translate())));
        craftAvailableButton = addRenderableWidget(new MekanismImageButton(this, 89, 75, 16, getButtonLocation("craft_available"),
              (element, mouseX, mouseY) -> PacketUtils.sendToServer(new PacketGuiInteract(GuiInteraction.CRAFT_ALL, ((GuiFormulaicAssemblicator) element.gui()).tile)),
              (element, graphics, mouseX, mouseY) -> element.displayTooltips(graphics, mouseX, mouseY, MekanismLang.CRAFT_AVAILABLE.translate())));
        autoModeButton = addRenderableWidget(new MekanismImageButton(this, 107, 75, 16, getButtonLocation("auto_toggle"),
              (element, mouseX, mouseY) -> PacketUtils.sendToServer(new PacketGuiInteract(GuiInteraction.NEXT_MODE, ((GuiFormulaicAssemblicator) element.gui()).tile)),
              (element, graphics, mouseX, mouseY) -> element.displayTooltips(graphics, mouseX, mouseY,MekanismLang.AUTO_MODE.translate(OnOff.of(((GuiFormulaicAssemblicator) element.gui()).tile.getAutoMode())))));
        updateEnabledButtons();
    }

    @Override
    public void containerTick() {
        super.containerTick();
        updateEnabledButtons();
    }

    private void updateEnabledButtons() {
        encodeFormulaButton.active = !tile.getAutoMode() && tile.hasRecipe() && canEncode();
        stockControlButton.active = tile.hasValidFormula();
        fillEmptyButton.active = !tile.getAutoMode();
        craftSingleButton.active = !tile.getAutoMode() && tile.hasRecipe();
        craftAvailableButton.active = !tile.getAutoMode() && tile.hasRecipe();
        autoModeButton.active = tile.hasValidFormula();
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected ItemStack checkValidity(int slotIndex) {
        int i = slotIndex - 21;
        if (i >= 0 && tile.hasValidFormula()) {
            ItemStack stack = tile.formula.input.get(i);
            if (!stack.isEmpty()) {
                Slot slot = menu.slots.get(slotIndex);
                //Only render the "correct" item in the gui slot if we don't already have that item there
                if (slot.getItem().isEmpty() || !tile.formula.isIngredientInPos(tile.getLevel(), slot.getItem(), i)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        //TODO: Gui element
        SlotOverlay overlay = tile.hasRecipe() ? SlotOverlay.CHECK : SlotOverlay.X;
        guiGraphics.blit(overlay.getTexture(), leftPos + 88, topPos + 22, 0, 0, overlay.getWidth(), overlay.getHeight(),
              overlay.getWidth(), overlay.getHeight());
    }

    private boolean canEncode() {
        if (!tile.hasValidFormula()) {
            ItemStack stack = tile.getFormulaSlot().getStack();
            if (!stack.isEmpty() && stack.getItem() instanceof ItemCraftingFormula) {
                Optional<FormulaAttachment> existingFormula = stack.getExistingData(MekanismAttachmentTypes.FORMULA_HOLDER);
                if (existingFormula.isEmpty()) {
                    return true;
                }
                return existingFormula.filter(FormulaAttachment::isEmpty).isPresent();
            }
        }
        return false;
    }
}