package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.entity.RobotScanners;
import io.github.marcelofabianov.cubit.player.ResetCommand;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import io.github.marcelofabianov.cubit.registry.CubitCreativeTabs;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.function.BiConsumer;

/** The names, subtitles and notices of the game, in English, the base language (answer 712). */
final class EnglishGameTexts {
  private static final String ROBOT_NAME = "Cubit";
  private static final String DAMAGED_ROBOT_NAME = "Damaged Cubit";
  private static final String TOOLBOX_NAME = "Toolbox";
  private static final String DOCK_NAME = "Induction Dock";
  private static final String TRACKS_SUBTITLE = "Cubit tracks rumble";
  private static final String HOSTILE_MOB_ALERT_SUBTITLE = "Cubit warns of an enemy";
  private static final String RARE_ORE_ALERT_SUBTITLE = "Cubit finds a rare ore";
  private static final String EXPRESSION_BEEP_SUBTITLE = "Cubit beeps";
  private static final String SHORT_CIRCUIT_SUBTITLE = "Cubit short circuits";
  private static final String POWER_ON_SUBTITLE = "Cubit powers on";
  private static final String POWER_OFF_SUBTITLE = "Cubit powers off";
  private static final String RECHARGING_SUBTITLE = "Cubit recharges";
  private static final String PLACED_SUBTITLE = "Cubit placed";
  private static final String RESET_CLEARED = "Follow link cleared.";
  private static final String RESET_NOBODY = "No Cubit is following you.";
  private static final String NOTICE_FORMAT = "Cubit: %s";
  private static final String ENEMY_NOTICE = "Careful, %s!";
  private static final String ORE_NOTICE = "%s!!!";
  private static final String ORE_FOUND_NOTICE = "Ore found";
  private static final String LOOKING_NOTICE = "Looking for %s";
  private static final String FILTER_CLEARED_NOTICE = "Filter cleared";
  private static final String CRITICAL_BATTERY_NOTICE = "Critical battery";
  private static final String HURT_NOTICE = "That hurts!";
  private static final String OWNER_HURT_NOTICE = "Careful!";

  private EnglishGameTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.GENERAL_ROBOT.get().getDescriptionId(), ROBOT_NAME);
    add.accept(CubitEntities.GENERAL_ROBOT.get().getDescriptionId(), ROBOT_NAME);
    add.accept(CubitCreativeTabs.MAIN_TITLE_KEY, ROBOT_NAME);
    add.accept(CubitItems.DAMAGED_GENERAL_ROBOT.get().getDescriptionId(), DAMAGED_ROBOT_NAME);
    add.accept(CubitItems.TOOLBOX.get().getDescriptionId(), TOOLBOX_NAME);
    add.accept(CubitBlocks.INDUCTION_DOCK.get().getDescriptionId(), DOCK_NAME);
    add.accept(ResetCommand.CLEARED_KEY, RESET_CLEARED);
    add.accept(ResetCommand.NOBODY_KEY, RESET_NOBODY);
    add.accept(RobotNotices.FORMAT_KEY, NOTICE_FORMAT);
    add.accept(RobotScanners.ENEMY_KEY, ENEMY_NOTICE);
    add.accept(RobotScanners.ORE_KEY, ORE_NOTICE);
    add.accept(RobotScanners.ORE_FOUND_KEY, ORE_FOUND_NOTICE);
    add.accept(RobotScanners.LOOKING_KEY, LOOKING_NOTICE);
    add.accept(RobotScanners.CLEARED_KEY, FILTER_CLEARED_NOTICE);
    add.accept(RobotNotices.keyOf(RobotNotice.CRITICAL_BATTERY), CRITICAL_BATTERY_NOTICE);
    add.accept(RobotNotices.keyOf(RobotNotice.HURT), HURT_NOTICE);
    add.accept(RobotNotices.keyOf(RobotNotice.OWNER_HURT), OWNER_HURT_NOTICE);
    addSubtitles(add);
  }

  private static void addSubtitles(BiConsumer<String, String> add) {
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.TRACKS_NAME), TRACKS_SUBTITLE);
    add.accept(
        CubitSoundProvider.subtitleKey(CubitSounds.HOSTILE_MOB_ALERT_NAME),
        HOSTILE_MOB_ALERT_SUBTITLE);
    add.accept(
        CubitSoundProvider.subtitleKey(CubitSounds.RARE_ORE_ALERT_NAME), RARE_ORE_ALERT_SUBTITLE);
    add.accept(
        CubitSoundProvider.subtitleKey(CubitSounds.EXPRESSION_BEEP_NAME), EXPRESSION_BEEP_SUBTITLE);
    add.accept(
        CubitSoundProvider.subtitleKey(CubitSounds.SHORT_CIRCUIT_NAME), SHORT_CIRCUIT_SUBTITLE);
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.POWER_ON_NAME), POWER_ON_SUBTITLE);
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.POWER_OFF_NAME), POWER_OFF_SUBTITLE);
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.RECHARGING_NAME), RECHARGING_SUBTITLE);
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.PLACED_NAME), PLACED_SUBTITLE);
  }
}
