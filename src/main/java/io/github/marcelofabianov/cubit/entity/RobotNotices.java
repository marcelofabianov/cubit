package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.NoticeRepeat;
import io.github.marcelofabianov.cubit.domain.behavior.NoticeRules;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Sends short texts to the client, only to the owner and only from the robot that follows. It stays
 * out of the saves, so a robot loaded with a low battery warns again.
 */
public final class RobotNotices {
  public static final String FORMAT_KEY = "notice.cubit.format";
  private static final String KEY = "notice.cubit.%s";

  private final RobotEntity robot;
  private final Map<RobotNotice, NoticeRepeat> repeats = new EnumMap<>(RobotNotice.class);

  RobotNotices(RobotEntity robot) {
    this.robot = robot;
    for (RobotNotice notice : RobotNotice.values()) {
      repeats.put(notice, NoticeRepeat.READY);
    }
  }

  public static String keyOf(RobotNotice notice) {
    return KEY.formatted(notice.name().toLowerCase(Locale.ROOT));
  }

  /** Whether the robot showed this text less than an interval ago. */
  public static boolean isHolding(RobotEntity robot, RobotNotice notice) {
    return !robot.notices().repeats.get(notice).canShow();
  }

  void tick() {
    repeats.replaceAll((notice, repeat) -> repeat.tick());
    if (robot.battery().hasAtMost(CubitMod.CONFIG.criticalCharge())) {
      show(RobotNotice.CRITICAL_BATTERY);
    }
  }

  void show(RobotNotice notice) {
    Player owner = RobotInteractions.followedPlayer(robot);
    if (!(owner instanceof ServerPlayer player)
        || owner.level() != robot.level()
        || !NoticeRules.speaks(robot.powerState())
        || !repeats.get(notice).canShow()) {
      return;
    }
    CubitNetwork.sendNotice(
        player, Component.translatable(FORMAT_KEY, Component.translatable(keyOf(notice))));
    repeats.put(notice, repeats.get(notice).shown(CubitMod.CONFIG.noticeRepeatTicks()));
  }
}
