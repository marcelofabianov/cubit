package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the notices. The names are a contract with the pack. */
final class NoticeSection {
  private static final String REPEAT_TICKS = "notice_repeat_ticks";
  private static final int REPEAT_TICKS_DEFAULT = 500;
  private static final int REPEAT_TICKS_MIN = 1;
  private static final int REPEAT_TICKS_MAX = 72000;

  final ModConfigSpec.IntValue repeatTicks;

  NoticeSection(ModConfigSpec.Builder builder) {
    repeatTicks =
        builder.defineInRange(
            REPEAT_TICKS, REPEAT_TICKS_DEFAULT, REPEAT_TICKS_MIN, REPEAT_TICKS_MAX);
  }
}
