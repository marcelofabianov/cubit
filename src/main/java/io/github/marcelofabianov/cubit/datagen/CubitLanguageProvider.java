package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.entity.RobotChatter;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.entity.RobotScanners;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.player.ResetCommand;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import io.github.marcelofabianov.cubit.registry.CubitCreativeTabs;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.List;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** The only language file of the mod: everything the player reads is in English. */
final class CubitLanguageProvider extends LanguageProvider {
  private static final String LOCALE = "en_us";
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
  private static final String LAMP_ON = "Lamp: On";
  private static final String LAMP_OFF = "Lamp: Off";
  private static final String RESET_CLEARED = "Follow link cleared.";
  private static final String RESET_NOBODY = "No Cubit is following you.";
  private static final String NOTICE_FORMAT = "Cubit: %s";
  private static final String CHATTER_ON = "Chatter: On";
  private static final String CHATTER_OFF = "Chatter: Off";
  private static final String SCANNERS_ON = "Scanners: On";
  private static final String SCANNERS_OFF = "Scanners: Off";
  private static final String ENEMY_NOTICE = "Careful, %s!";
  private static final String ORE_NOTICE = "%s!!!";
  private static final String ORE_FOUND_NOTICE = "Ore found";
  private static final String LOOKING_NOTICE = "Looking for %s";
  private static final String FILTER_CLEARED_NOTICE = "Filter cleared";
  private static final String CRITICAL_BATTERY_NOTICE = "Critical battery";
  private static final String HURT_NOTICE = "That hurts!";
  private static final String OWNER_HURT_NOTICE = "Careful!";

  CubitLanguageProvider(PackOutput output) {
    super(output, CubitMod.MOD_ID, LOCALE);
  }

  @Override
  protected void addTranslations() {
    add(CubitItems.GENERAL_ROBOT.get(), ROBOT_NAME);
    add(CubitEntities.GENERAL_ROBOT.get(), ROBOT_NAME);
    add(CubitCreativeTabs.MAIN_TITLE_KEY, ROBOT_NAME);
    add(CubitItems.DAMAGED_GENERAL_ROBOT.get(), DAMAGED_ROBOT_NAME);
    add(CubitItems.TOOLBOX.get(), TOOLBOX_NAME);
    add(CubitBlocks.INDUCTION_DOCK.get(), DOCK_NAME);
    add(RobotMenu.LAMP_ON_KEY, LAMP_ON);
    add(RobotMenu.LAMP_OFF_KEY, LAMP_OFF);
    add(RobotMenu.CHATTER_ON_KEY, CHATTER_ON);
    add(RobotMenu.CHATTER_OFF_KEY, CHATTER_OFF);
    addChatPhrases();
    add(RobotMenu.SCANNERS_ON_KEY, SCANNERS_ON);
    add(RobotMenu.SCANNERS_OFF_KEY, SCANNERS_OFF);
    add(ResetCommand.CLEARED_KEY, RESET_CLEARED);
    add(ResetCommand.NOBODY_KEY, RESET_NOBODY);
    add(RobotNotices.FORMAT_KEY, NOTICE_FORMAT);
    add(RobotScanners.ENEMY_KEY, ENEMY_NOTICE);
    add(RobotScanners.ORE_KEY, ORE_NOTICE);
    add(RobotScanners.ORE_FOUND_KEY, ORE_FOUND_NOTICE);
    add(RobotScanners.LOOKING_KEY, LOOKING_NOTICE);
    add(RobotScanners.CLEARED_KEY, FILTER_CLEARED_NOTICE);
    add(RobotNotices.keyOf(RobotNotice.CRITICAL_BATTERY), CRITICAL_BATTERY_NOTICE);
    add(RobotNotices.keyOf(RobotNotice.HURT), HURT_NOTICE);
    add(RobotNotices.keyOf(RobotNotice.OWNER_HURT), OWNER_HURT_NOTICE);
    addSubtitles();
  }

  private void addSubtitles() {
    add(CubitSoundProvider.subtitleKey(CubitSounds.TRACKS_NAME), TRACKS_SUBTITLE);
    add(
        CubitSoundProvider.subtitleKey(CubitSounds.HOSTILE_MOB_ALERT_NAME),
        HOSTILE_MOB_ALERT_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.RARE_ORE_ALERT_NAME), RARE_ORE_ALERT_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.EXPRESSION_BEEP_NAME), EXPRESSION_BEEP_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.SHORT_CIRCUIT_NAME), SHORT_CIRCUIT_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.POWER_ON_NAME), POWER_ON_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.POWER_OFF_NAME), POWER_OFF_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.RECHARGING_NAME), RECHARGING_SUBTITLE);
    add(CubitSoundProvider.subtitleKey(CubitSounds.PLACED_NAME), PLACED_SUBTITLE);
  }

  private void addChatPhrases() {
    for (ChatTopic topic : ChatTopic.values()) {
      List<String> phrases = ChatPhrases.of(topic);
      for (int phrase = 0; phrase < phrases.size(); phrase++) {
        add(RobotChatter.keyOf(topic, phrase), phrases.get(phrase));
      }
    }
  }
}
