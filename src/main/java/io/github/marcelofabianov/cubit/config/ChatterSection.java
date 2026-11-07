package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the chatter. The names are a contract with the pack. */
final class ChatterSection {
  private static final String REPEAT_TICKS = "chatter_repeat_ticks";
  private static final int REPEAT_TICKS_DEFAULT = 6000;
  private static final int REPEAT_TICKS_MIN = 1;
  private static final int REPEAT_TICKS_MAX = 72000;
  private static final String CHECK_TICKS = "chatter_check_ticks";
  private static final int CHECK_TICKS_DEFAULT = 20;
  private static final int CHECK_TICKS_MIN = 1;
  private static final int CHECK_TICKS_MAX = 1200;

  private static final String BIOME_REPEAT = "chatter_biome_repeat_ticks";
  private static final int BIOME_REPEAT_DEFAULT = 2400;
  private static final String ANIMAL_REPEAT = "chatter_animal_repeat_ticks";
  private static final int ANIMAL_REPEAT_DEFAULT = 1200;
  private static final String ANIMAL_CHANCE = "chatter_animal_chance_percent";
  private static final int ANIMAL_CHANCE_DEFAULT = 33;
  private static final int ANIMAL_CHANCE_MIN = 0;
  private static final int ANIMAL_CHANCE_MAX = 100;
  private static final String LEFT_BEHIND_BLOCKS = "chatter_left_behind_blocks";
  private static final int LEFT_BEHIND_BLOCKS_DEFAULT = 16;
  private static final int LEFT_BEHIND_BLOCKS_MIN = 1;
  private static final int LEFT_BEHIND_BLOCKS_MAX = 128;
  private static final String LEFT_BEHIND_REPEAT = "chatter_left_behind_repeat_ticks";
  private static final int LEFT_BEHIND_REPEAT_DEFAULT = 1200;

  final ModConfigSpec.IntValue repeatTicks;
  final ModConfigSpec.IntValue biomeRepeatTicks;
  final ModConfigSpec.IntValue animalRepeatTicks;
  final ModConfigSpec.IntValue animalChancePercent;
  final ModConfigSpec.IntValue leftBehindBlocks;
  final ModConfigSpec.IntValue leftBehindRepeatTicks;
  final ModConfigSpec.IntValue checkTicks;

  ChatterSection(ModConfigSpec.Builder builder) {
    repeatTicks =
        builder.defineInRange(
            REPEAT_TICKS, REPEAT_TICKS_DEFAULT, REPEAT_TICKS_MIN, REPEAT_TICKS_MAX);
    biomeRepeatTicks =
        builder.defineInRange(
            BIOME_REPEAT, BIOME_REPEAT_DEFAULT, REPEAT_TICKS_MIN, REPEAT_TICKS_MAX);
    animalRepeatTicks =
        builder.defineInRange(
            ANIMAL_REPEAT, ANIMAL_REPEAT_DEFAULT, REPEAT_TICKS_MIN, REPEAT_TICKS_MAX);
    animalChancePercent =
        builder.defineInRange(
            ANIMAL_CHANCE, ANIMAL_CHANCE_DEFAULT, ANIMAL_CHANCE_MIN, ANIMAL_CHANCE_MAX);
    leftBehindBlocks =
        builder.defineInRange(
            LEFT_BEHIND_BLOCKS,
            LEFT_BEHIND_BLOCKS_DEFAULT,
            LEFT_BEHIND_BLOCKS_MIN,
            LEFT_BEHIND_BLOCKS_MAX);
    leftBehindRepeatTicks =
        builder.defineInRange(
            LEFT_BEHIND_REPEAT, LEFT_BEHIND_REPEAT_DEFAULT, REPEAT_TICKS_MIN, REPEAT_TICKS_MAX);
    checkTicks =
        builder.defineInRange(CHECK_TICKS, CHECK_TICKS_DEFAULT, CHECK_TICKS_MIN, CHECK_TICKS_MAX);
  }
}
