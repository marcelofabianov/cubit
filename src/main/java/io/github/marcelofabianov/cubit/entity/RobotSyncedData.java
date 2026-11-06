package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

/**
 * The data a robot sends to the client. The game sizes this data from what is defined when the
 * entity is created, so {@link RobotEntity} loads this class first. Defining it later fails the
 * first robots created. None of it is saved, because it is recalculated.
 */
final class RobotSyncedData {
  private static final byte NOTHING_PLAYING = 0;

  static final EntityDataAccessor<Byte> PLAYING_SOUNDS =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);
  static final EntityDataAccessor<Byte> EXPRESSION =
      SynchedEntityData.defineId(RobotEntity.class, EntityDataSerializers.BYTE);

  private RobotSyncedData() {}

  /** Called while the class loads, only to make sure it runs before any robot exists. */
  static void load() {}

  static void define(SynchedEntityData.Builder builder) {
    builder.define(PLAYING_SOUNDS, NOTHING_PLAYING);
    builder.define(EXPRESSION, (byte) RobotExpression.OFF.ordinal());
  }
}
