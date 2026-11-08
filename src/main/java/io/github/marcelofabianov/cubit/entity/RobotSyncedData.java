package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.GazeDirection;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.ScanSignal;
import io.github.marcelofabianov.cubit.domain.energy.BatteryIndicator;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

/**
 * The data a robot sends to the client. The game sizes this data from what is defined when the
 * entity is created, so {@link RobotEntity} loads this class first. Defining it later fails the
 * first robots created. None of it is saved as it is here: most is recalculated, and the color is
 * saved under its key, because an ordinal would change the color if the list ever changed.
 */
final class RobotSyncedData {
  private static final byte NOTHING_PLAYING = 0;
  private static final byte NO_LIGHT = 0;

  static final EntityDataAccessor<Byte> PLAYING_SOUNDS =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);
  static final EntityDataAccessor<Byte> EXPRESSION =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);
  static final EntityDataAccessor<Byte> OWNER_GAZE =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);
  static final EntityDataAccessor<Byte> OWNER_LEVEL =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);
  static final EntityDataAccessor<Boolean> ARMS_RAISED =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BOOLEAN);
  static final EntityDataAccessor<Byte> TIER =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);
  static final EntityDataAccessor<Byte> BATTERY_INDICATOR =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);

  static final EntityDataAccessor<Boolean> LAMP_LIT =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BOOLEAN);
  static final EntityDataAccessor<Byte> LIGHT_LEVEL =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);

  static final EntityDataAccessor<Boolean> SOLAR_CHARGING =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BOOLEAN);

  static final EntityDataAccessor<Byte> COLOR =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);

  static final EntityDataAccessor<Byte> SCAN_SIGNAL =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);

  private RobotSyncedData() {}

  /** Called while the class loads, only to make sure it runs before any robot exists. */
  static void load() {}

  static void define(SynchedEntityData.Builder builder) {
    builder.define(PLAYING_SOUNDS, NOTHING_PLAYING);
    builder.define(EXPRESSION, (byte) RobotExpression.OFF.ordinal());
    builder.define(OWNER_GAZE, (byte) GazeDirection.LEVEL.ordinal());
    builder.define(OWNER_LEVEL, (byte) GazeDirection.LEVEL.ordinal());
    builder.define(ARMS_RAISED, false);
    builder.define(TIER, (byte) RobotTier.BASE.ordinal());
    builder.define(BATTERY_INDICATOR, (byte) BatteryIndicator.EMPTY.ordinal());
    builder.define(LAMP_LIT, false);
    builder.define(SOLAR_CHARGING, false);
    builder.define(LIGHT_LEVEL, NO_LIGHT);
    builder.define(COLOR, (byte) RobotColor.WHITE.ordinal());
    builder.define(SCAN_SIGNAL, (byte) ScanSignal.NONE.ordinal());
  }
}
