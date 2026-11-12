package io.github.marcelofabianov.cubit.menu;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;

/**
 * What travels once, when the robot screen opens, because it does not change while it is open: the
 * size of the inventory, the numbers in the descriptions of the switches and the tags of the
 * scanner filter, which the client names in its own language. The filter only changes by a click on
 * the robot, which the open screen does not allow.
 */
public record RobotScreenOpening(
    int slots,
    int lampOnBelow,
    long lampCost,
    int oreRadius,
    int mobRadius,
    long scanCost,
    List<String> filterTags,
    String filterIconTag,
    DefenseOpening defense) {
  private static final int MAX_FILTER = 64;

  public RobotScreenOpening {
    filterTags = List.copyOf(filterTags);
  }

  public void write(FriendlyByteBuf data) {
    data.writeVarInt(slots);
    data.writeVarInt(lampOnBelow);
    data.writeVarLong(lampCost);
    data.writeVarInt(oreRadius);
    data.writeVarInt(mobRadius);
    data.writeVarLong(scanCost);
    data.writeCollection(filterTags, FriendlyByteBuf::writeUtf);
    data.writeUtf(filterIconTag);
    defense.write(data);
  }

  /** A filter longer than any tier allows is cut, so a bad packet cannot grow the screen. */
  static RobotScreenOpening read(FriendlyByteBuf data) {
    int slots = data.readVarInt();
    int lampOnBelow = data.readVarInt();
    long lampCost = data.readVarLong();
    int oreRadius = data.readVarInt();
    int mobRadius = data.readVarInt();
    long scanCost = data.readVarLong();
    List<String> names = data.readList(FriendlyByteBuf::readUtf);
    List<String> kept = names.subList(0, Math.min(names.size(), MAX_FILTER));
    String iconTag = data.readUtf();
    return new RobotScreenOpening(
        slots,
        lampOnBelow,
        lampCost,
        oreRadius,
        mobRadius,
        scanCost,
        kept,
        iconTag,
        DefenseOpening.read(data));
  }

  public boolean hasFilter() {
    return !filterTags.isEmpty();
  }
}
