package io.github.zenndg.cubit.network;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The Cubits of the player for the selection screen of the Tablet, already in order (answers 1003
 * and 1004). Only from the server to the player who holds the Tablet, and the client only draws.
 */
public record RobotListPayload(List<Entry> entries) implements CustomPacketPayload {
  private static final String PATH = "robot_list";
  private static final int MAX_ENTRIES = 1024;
  private static final int MAX_TEXT = 256;

  public static final Type<RobotListPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, RobotListPayload> STREAM_CODEC =
      StreamCodec.of(RobotListPayload::write, RobotListPayload::read);

  /**
   * One Cubit: the enums travel as their order, the place as a block and the dimension by its id.
   * The age is how old the snapshot is, in ticks, and zero for a loaded Cubit.
   */
  public record Entry(
      UUID robot,
      String name,
      Look look,
      int status,
      Work work,
      int batteryPercent,
      BlockPos pos,
      String dimension,
      Where where) {}

  /** The job and the reason the state shows, each by its order plus one, with zero for none. */
  public record Work(int job, int reason) {
    public Optional<RobotJob> jobValue() {
      return ofIndex(RobotJob.values(), job);
    }

    public Optional<HibernationReason> reasonValue() {
      return ofIndex(HibernationReason.values(), reason);
    }

    private static <T> Optional<T> ofIndex(T[] all, int value) {
      return value <= 0 || value > all.length ? Optional.empty() : Optional.of(all[value - 1]);
    }
  }

  /** The looks of the portrait, by the order of their enums. */
  public record Look(int tier, int color, int expression) {}

  /** Whether the Cubit is loaded and follows the player, and how old a snapshot is. */
  public record Where(boolean loaded, boolean following, double distance, long ageTicks) {}

  @Override
  public Type<RobotListPayload> type() {
    return TYPE;
  }

  private static void write(RegistryFriendlyByteBuf buffer, RobotListPayload payload) {
    buffer.writeVarInt(payload.entries().size());
    for (Entry entry : payload.entries()) {
      UUIDUtil.STREAM_CODEC.encode(buffer, entry.robot());
      buffer.writeUtf(entry.name(), MAX_TEXT);
      buffer.writeVarInt(entry.look().tier());
      buffer.writeVarInt(entry.look().color());
      buffer.writeVarInt(entry.look().expression());
      buffer.writeVarInt(entry.status());
      buffer.writeVarInt(entry.work().job());
      buffer.writeVarInt(entry.work().reason());
      buffer.writeVarInt(entry.batteryPercent());
      buffer.writeBlockPos(entry.pos());
      buffer.writeUtf(entry.dimension(), MAX_TEXT);
      buffer.writeBoolean(entry.where().loaded());
      buffer.writeBoolean(entry.where().following());
      buffer.writeDouble(entry.where().distance());
      buffer.writeVarLong(entry.where().ageTicks());
    }
  }

  /** A list longer than any player would have is cut, so a bad packet never fills the memory. */
  private static RobotListPayload read(RegistryFriendlyByteBuf buffer) {
    int size = Math.min(buffer.readVarInt(), MAX_ENTRIES);
    List<Entry> entries = new ArrayList<>(size);
    for (int index = 0; index < size; index++) {
      UUID robot = UUIDUtil.STREAM_CODEC.decode(buffer);
      String name = buffer.readUtf(MAX_TEXT);
      Look look = new Look(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
      int status = buffer.readVarInt();
      Work work = new Work(buffer.readVarInt(), buffer.readVarInt());
      int battery = buffer.readVarInt();
      BlockPos pos = buffer.readBlockPos();
      String dimension = buffer.readUtf(MAX_TEXT);
      Where where =
          new Where(
              buffer.readBoolean(),
              buffer.readBoolean(),
              buffer.readDouble(),
              buffer.readVarLong());
      entries.add(new Entry(robot, name, look, status, work, battery, pos, dimension, where));
    }
    return new RobotListPayload(entries);
  }
}
