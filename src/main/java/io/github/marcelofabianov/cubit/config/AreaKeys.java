package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The area of one tier, with 0 for a tier without it. The key names are a contract (1050). */
final class AreaKeys {
  private static final String SIZE = "area_size_blocks";
  private static final String HEIGHT = "area_height_blocks";
  private static final int MIN_ZERO = 0;
  private static final int MAX_SIZE = 128;
  private static final int MAX_HEIGHT = 64;

  private final ModConfigSpec.IntValue size;
  private final ModConfigSpec.IntValue height;

  AreaKeys(ModConfigSpec.Builder builder, AreaSpec defaults) {
    size = builder.defineInRange(SIZE, defaults.sizeBlocks(), MIN_ZERO, MAX_SIZE);
    height = builder.defineInRange(HEIGHT, defaults.heightBlocks(), MIN_ZERO, MAX_HEIGHT);
  }

  AreaSpec read() {
    return new AreaSpec(size.get(), height.get());
  }
}
