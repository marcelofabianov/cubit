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

/** The names, subtitles and notices of the game, in Portuguese (answers 712 and 733). */
final class PortugueseGameTexts {
  private static final String ROBOT_NAME = "Cubit";
  private static final String DAMAGED_ROBOT_NAME = "Cubit Danificado";
  private static final String TOOLBOX_NAME = "Caixa de Ferramentas";
  private static final String DOCK_NAME = "Base de Indução";
  private static final String TRACKS_SUBTITLE = "Esteiras do Cubit rodam";
  private static final String HOSTILE_MOB_ALERT_SUBTITLE = "Cubit avisa de um inimigo";
  private static final String RARE_ORE_ALERT_SUBTITLE = "Cubit encontra um minério raro";
  private static final String EXPRESSION_BEEP_SUBTITLE = "Cubit apita";
  private static final String SHORT_CIRCUIT_SUBTITLE = "Cubit entra em curto";
  private static final String POWER_ON_SUBTITLE = "Cubit liga";
  private static final String POWER_OFF_SUBTITLE = "Cubit desliga";
  private static final String RECHARGING_SUBTITLE = "Cubit recarrega";
  private static final String PLACED_SUBTITLE = "Cubit colocado";
  private static final String RESET_CLEARED = "Vínculo de seguir removido.";
  private static final String RESET_NOBODY = "Nenhum Cubit está seguindo você.";
  private static final String NOTICE_FORMAT = "Cubit: %s";
  private static final String ENEMY_NOTICE = "Cuidado, %s!";
  private static final String ORE_NOTICE = "%s!!!";
  private static final String ORE_FOUND_NOTICE = "Minério encontrado";
  private static final String LOOKING_NOTICE = "Procurando %s";
  private static final String FILTER_CLEARED_NOTICE = "Filtro limpo";
  private static final String CRITICAL_BATTERY_NOTICE = "Bateria crítica";
  private static final String NEED_RECHARGE_NOTICE = "Preciso recarregar";
  private static final String HURT_NOTICE = "Isso dói!";
  private static final String OWNER_HURT_NOTICE = "Cuidado!";

  private static final String MOD_DESCRIPTION =
      Description.PART_1
          + Description.PART_2
          + Description.PART_3
          + Description.PART_4
          + Description.PART_5
          + Description.PART_6
          + Description.PART_7;

  private PortugueseGameTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitLanguageProvider.MOD_DESCRIPTION_KEY, MOD_DESCRIPTION);
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
    addNotices(add);
    addSubtitles(add);
  }

  private static void addNotices(BiConsumer<String, String> add) {
    add.accept(RobotNotices.keyOf(RobotNotice.CRITICAL_BATTERY), CRITICAL_BATTERY_NOTICE);
    add.accept(RobotNotices.keyOf(RobotNotice.HURT), HURT_NOTICE);
    add.accept(RobotNotices.keyOf(RobotNotice.OWNER_HURT), OWNER_HURT_NOTICE);
    add.accept(RobotNotices.keyOf(RobotNotice.NEED_RECHARGE), NEED_RECHARGE_NOTICE);
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

  /** The mod list description, in parts short enough to keep each literal on its own line. */
  private static final class Description {
    private static final String PART_1 = "Cubit é um robô companheiro fofo, movido a bateria, ";
    private static final String PART_2 = "que segue você, carrega seus itens e ilumina o caminho. ";
    private static final String PART_3 = "Ele procura minérios e mobs hostis, conversa com bipes ";
    private static final String PART_4 = "e textos curtos e recarrega na Base de Indução com FE ";
    private static final String PART_5 = "de qualquer mod, com redstone ou com o painel solar. ";
    private static final String PART_6 = "Recolha-o como item quando quiser, com tudo o que ele ";
    private static final String PART_7 = "guarda a salvo.";
  }
}
