package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.robot.RobotName;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * The small window of Rename, in the middle of the screen: the field with the current name, Confirm
 * and Cancel, and Enter confirms (answer 1035). It takes every key while open, so typing an E does
 * not close the screen behind it.
 */
final class TabletRenameDialog {
  private static final int WIDTH = 180;
  private static final int HEIGHT = 62;
  private static final int MARGIN = 8;
  private static final int TITLE_Y = 7;
  private static final int FIELD_Y = 19;
  private static final int FIELD_HEIGHT = 16;
  private static final int BUTTON_Y = 41;
  private static final int BUTTON_WIDTH = 60;
  private static final int BUTTON_HEIGHT = 14;
  private static final int BUTTON_GAP = 6;
  private static final int SHADOW = 0xAA000000;
  private static final float ABOVE_ITEMS = 400;

  private final Font font;
  private final Consumer<String> confirm;
  private final EditBox field;
  private final FlatButton confirmButton;
  private final FlatButton cancelButton;
  private final int left;
  private final int top;
  private boolean open;

  TabletRenameDialog(Font font, int screenWidth, int screenHeight, Consumer<String> confirm) {
    this.font = font;
    this.confirm = confirm;
    left = (screenWidth - WIDTH) / 2;
    top = (screenHeight - HEIGHT) / 2;
    Component title = Component.translatable(TabletKeys.RENAME_TITLE);
    field =
        new EditBox(font, left + MARGIN, top + FIELD_Y, WIDTH - 2 * MARGIN, FIELD_HEIGHT, title);
    field.setMaxLength(RobotName.MAX_LENGTH);
    int buttonsX = left + WIDTH - MARGIN - 2 * BUTTON_WIDTH - BUTTON_GAP;
    confirmButton =
        new FlatButton(
            buttonsX,
            top + BUTTON_Y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            Component.translatable(TabletKeys.CONFIRM).withStyle(ChatFormatting.BOLD),
            this::confirm);
    cancelButton =
        new FlatButton(
            buttonsX + BUTTON_WIDTH + BUTTON_GAP,
            top + BUTTON_Y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            Component.translatable(TabletKeys.CANCEL).withStyle(ChatFormatting.BOLD),
            this::close);
    cancelButton.setMuted(true);
  }

  boolean isOpen() {
    return open;
  }

  void open(String current) {
    field.setValue(current);
    field.setFocused(true);
    open = true;
  }

  void close() {
    field.setFocused(false);
    open = false;
  }

  private void confirm() {
    confirm.accept(field.getValue());
    close();
  }

  /** Above the slots and their items, with the screen behind darkened. */
  void render(GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float tick) {
    graphics.pose().pushPose();
    graphics.pose().translate(0, 0, ABOVE_ITEMS);
    graphics.fill(0, 0, width, height, SHADOW);
    ScreenPainter.window(graphics, left, top, WIDTH, HEIGHT);
    Component title =
        Component.translatable(TabletKeys.RENAME_TITLE).withStyle(ChatFormatting.BOLD);
    graphics.drawString(font, title, left + MARGIN, top + TITLE_Y, ScreenPainter.TEXT, false);
    for (AbstractWidget widget : List.of(field, confirmButton, cancelButton)) {
      widget.render(graphics, mouseX, mouseY, tick);
    }
    graphics.pose().popPose();
  }

  /** Escape cancels and Enter confirms. The field takes every other key. */
  boolean keyPressed(int key, int scanCode, int modifiers) {
    if (key == GLFW.GLFW_KEY_ESCAPE) {
      close();
    } else if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
      confirm();
    } else {
      field.keyPressed(key, scanCode, modifiers);
    }
    return true;
  }

  boolean charTyped(char typed, int modifiers) {
    return field.charTyped(typed, modifiers);
  }

  boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (AbstractWidget widget : List.of(field, confirmButton, cancelButton)) {
      if (widget.mouseClicked(mouseX, mouseY, button)) {
        return true;
      }
    }
    return true;
  }
}
