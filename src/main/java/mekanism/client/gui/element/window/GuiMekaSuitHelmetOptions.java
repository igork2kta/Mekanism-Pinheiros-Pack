package mekanism.client.gui.element.window;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiColorPickerSlot;
import mekanism.client.gui.element.GuiScreenSwitch;
import mekanism.client.gui.element.GuiSlider;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.HUDElement.HUDColor;
import mekanism.common.inventory.container.SelectedWindowData.WindowType;
import net.minecraft.client.gui.GuiGraphics;

public class GuiMekaSuitHelmetOptions extends GuiWindow {

    public GuiMekaSuitHelmetOptions(IGuiWrapper gui, int x, int y) {
        super(gui, x, y, 140, 140, WindowType.MEKA_SUIT_HELMET);
        interactionStrategy = InteractionStrategy.NONE;
        addChild(new GuiColorPickerSlot(gui, relativeX + 12, relativeY + 32, false, HUDColor.REGULAR::getColor, color -> {
            MekanismConfig.client.hudColor.set(color.rgb());
            MekanismConfig.client.save();
        }));
        addChild(new GuiColorPickerSlot(gui, relativeX + 61, relativeY + 32, false, HUDColor.WARNING::getColor, color -> {
            MekanismConfig.client.hudWarningColor.set(color.rgb());
            MekanismConfig.client.save();
        }));
        addChild(new GuiColorPickerSlot(gui, relativeX + 110, relativeY + 32, false, HUDColor.DANGER::getColor, color -> {
            MekanismConfig.client.hudDangerColor.set(color.rgb());
            MekanismConfig.client.save();
        }));

        GuiSlider opacitySlider = addChild(new GuiSlider(gui, relativeX + 10, relativeY + 62, 120, value -> {
            MekanismConfig.client.hudOpacity.set(value);
            MekanismConfig.client.save();
        }));
        opacitySlider.setValue(MekanismConfig.client.hudOpacity.get());

        GuiSlider jitterSlider = addChild(new GuiSlider(gui, relativeX + 10, relativeY + 87, 120, value -> {
            //Jitter is on a scale of [1, 100]
            // so we need to multiply the [0, 1] value by 99 to get it to [0, 99]
            // and then add 1 to shift it to [1, 100]
            MekanismConfig.client.hudJitter.set(99 * value + 1);
            MekanismConfig.client.save();
        }));
        //Jitter is on a scale of [1, 100]
        // so we need to subtract 1 to get it to [0, 99]
        // and then divide by 99 to get it to [0, 1]
        jitterSlider.setValue((MekanismConfig.client.hudJitter.get() - 1) / 99);

        addChild(new GuiScreenSwitch(gui, relativeX + 7, relativeY + 112, 126, MekanismLang.COMPASS.translate(), MekanismConfig.client.hudCompassEnabled,
              (element, mouseX, mouseY) -> {
                  MekanismConfig.client.hudCompassEnabled.set(!MekanismConfig.client.hudCompassEnabled.get());
                  MekanismConfig.client.save();
                  return true;
              }));
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderForeground(guiGraphics, mouseX, mouseY);

        drawTitleText(guiGraphics, MekanismLang.HELMET_OPTIONS.translate(), 6);
        drawTextExact(guiGraphics, MekanismLang.HUD_OVERLAY.translate(), relativeX + 7, relativeY + 20, headingTextColor());

        drawScaledCenteredText(guiGraphics, MekanismLang.DEFAULT.translate(), relativeX + 21, relativeY + 52, subheadingTextColor(), 0.8F);
        drawScaledCenteredText(guiGraphics, MekanismLang.WARNING.translate(), relativeX + 70, relativeY + 52, subheadingTextColor(), 0.8F);
        drawScaledCenteredText(guiGraphics, MekanismLang.DANGER.translate(), relativeX + 119, relativeY + 52, subheadingTextColor(), 0.8F);

        drawScaledCenteredText(guiGraphics, MekanismLang.OPACITY.translate(Math.round(MekanismConfig.client.hudOpacity.get() * 100)), relativeX + 70, relativeY + 75, subheadingTextColor(), 0.8F);
        drawScaledCenteredText(guiGraphics, MekanismLang.JITTER.translate((int) MekanismConfig.client.hudJitter.get()), relativeX + 70, relativeY + 100, subheadingTextColor(), 0.8F);
    }
}
