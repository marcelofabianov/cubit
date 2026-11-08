package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import java.util.Locale;
import net.minecraft.util.StringRepresentable;

/** The screen of a dock as a block state. The lowercase names are part of the saves. */
public enum DockStatusProperty implements StringRepresentable {
  OFF(DockStatus.OFF),
  IDLE(DockStatus.IDLE),
  CHARGING(DockStatus.CHARGING),
  COMPLETE(DockStatus.COMPLETE),
  NO_ENERGY(DockStatus.NO_ENERGY);

  private final DockStatus status;

  DockStatusProperty(DockStatus status) {
    this.status = status;
  }

  static DockStatusProperty of(DockStatus status) {
    for (DockStatusProperty property : values()) {
      if (property.status == status) {
        return property;
      }
    }
    return OFF;
  }

  @Override
  public String getSerializedName() {
    return name().toLowerCase(Locale.ROOT);
  }
}
