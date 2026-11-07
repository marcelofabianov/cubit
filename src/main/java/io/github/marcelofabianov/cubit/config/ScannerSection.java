package io.github.marcelofabianov.cubit.config;

import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys shared by all the scanners. The names are a contract with the pack. */
final class ScannerSection {
  private static final String RARE_TAGS = "rare_ore_tags";
  private static final String NETHERITE_SCRAP = "c:ores/netherite_scrap";
  private static final String EMERALD = "c:ores/emerald";
  private static final String DIAMOND = "c:ores/diamond";
  private static final String GOLD = "c:ores/gold";
  private static final String LAPIS = "c:ores/lapis";
  private static final String REDSTONE = "c:ores/redstone";
  private static final String IRON = "c:ores/iron";
  private static final List<String> RARE_TAGS_DEFAULT =
      List.of(NETHERITE_SCRAP, EMERALD, DIAMOND, GOLD, LAPIS, REDSTONE, IRON);
  private static final String EMPTY_TAG = "";
  private static final String SURPRISED_TICKS = "surprised_expression_ticks";
  private static final int SURPRISED_TICKS_DEFAULT = 60;
  private static final int SURPRISED_TICKS_MIN = 1;
  private static final int SURPRISED_TICKS_MAX = 1200;
  private static final String BLOCKS_PER_TICK = "scan_blocks_per_tick";
  private static final int BLOCKS_PER_TICK_DEFAULT = 2000;
  private static final int BLOCKS_PER_TICK_MIN = 1;
  private static final int BLOCKS_PER_TICK_MAX = 2_146_689;

  private static final String MARK_TICKS = "scan_mark_ticks";
  private static final int MARK_TICKS_DEFAULT = 200;
  private static final int MARK_TICKS_MIN = 1;
  private static final int MARK_TICKS_MAX = 1200;

  final ModConfigSpec.ConfigValue<List<? extends String>> rareTags;
  final ModConfigSpec.IntValue surprisedTicks;
  final ModConfigSpec.IntValue markTicks;
  final ModConfigSpec.IntValue blocksPerTick;

  ScannerSection(ModConfigSpec.Builder builder) {
    rareTags =
        builder.defineListAllowEmpty(
            RARE_TAGS, RARE_TAGS_DEFAULT, () -> EMPTY_TAG, String.class::isInstance);
    surprisedTicks =
        builder.defineInRange(
            SURPRISED_TICKS, SURPRISED_TICKS_DEFAULT, SURPRISED_TICKS_MIN, SURPRISED_TICKS_MAX);
    markTicks =
        builder.defineInRange(MARK_TICKS, MARK_TICKS_DEFAULT, MARK_TICKS_MIN, MARK_TICKS_MAX);
    blocksPerTick =
        builder.defineInRange(
            BLOCKS_PER_TICK, BLOCKS_PER_TICK_DEFAULT, BLOCKS_PER_TICK_MIN, BLOCKS_PER_TICK_MAX);
  }
}
