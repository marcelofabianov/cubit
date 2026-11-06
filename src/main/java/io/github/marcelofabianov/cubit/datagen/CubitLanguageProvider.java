package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.player.ResetCommand;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import io.github.marcelofabianov.cubit.registry.CubitCreativeTabs;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
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
  private static final String RESET_CLEARED = "Follow link cleared.";
  private static final String RESET_NOBODY = "No Cubit is following you.";

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
    add(ResetCommand.CLEARED_KEY, RESET_CLEARED);
    add(ResetCommand.NOBODY_KEY, RESET_NOBODY);
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
}
