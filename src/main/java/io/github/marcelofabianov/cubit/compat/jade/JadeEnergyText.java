package io.github.marcelofabianov.cubit.compat.jade;

import io.github.marcelofabianov.cubit.domain.energy.EnergyReading;
import java.util.Locale;
import net.minecraft.network.chat.Component;

/** Energy as the player reads it: 74% (222,000 / 300,000 FE), with grouped digits. */
final class JadeEnergyText {
  private static final String GROUPED = "%,d";

  private JadeEnergyText() {}

  static Component battery(EnergyReading reading) {
    return Component.translatable(
        JadeKeys.BATTERY,
        reading.percent(),
        grouped(reading.charge().amount()),
        grouped(reading.capacity().amount()));
  }

  static Component stock(EnergyReading reading) {
    return Component.translatable(
        JadeKeys.DOCK_ENERGY,
        grouped(reading.charge().amount()),
        grouped(reading.capacity().amount()));
  }

  private static String grouped(long amount) {
    return String.format(Locale.ROOT, GROUPED, amount);
  }
}
