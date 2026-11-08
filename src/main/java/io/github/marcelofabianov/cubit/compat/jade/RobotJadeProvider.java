package io.github.marcelofabianov.cubit.compat.jade;

import io.github.marcelofabianov.cubit.domain.energy.EnergyReading;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotReadout;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Tier, battery and state of a Cubit in Jade (answer 660). The battery is only on the server, so
 * Jade asks for it while someone looks at the robot (answer 661). Bad data shows nothing.
 */
enum RobotJadeProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
  INSTANCE;

  private static final String CHARGE = "Charge";
  private static final String CAPACITY = "Capacity";
  private static final String TIER = "Tier";
  private static final String STATE = "State";

  @Override
  public void appendServerData(CompoundTag data, EntityAccessor accessor) {
    if (accessor.getEntity() instanceof RobotEntity robot) {
      EnergyReading battery = RobotReadout.battery(robot);
      data.putLong(CHARGE, battery.charge().amount());
      data.putLong(CAPACITY, battery.capacity().amount());
      data.putString(TIER, robot.state().tier().name());
      data.putString(STATE, robot.powerState().name());
    }
  }

  @Override
  public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
    CompoundTag data = accessor.getServerData();
    enumOf(RobotTier.class, data.getString(TIER)).ifPresent(tier -> tooltip.add(tierLine(tier)));
    if (data.contains(CHARGE) && data.contains(CAPACITY)) {
      tooltip.add(
          JadeEnergyText.battery(
              EnergyReading.capped(data.getLong(CHARGE), data.getLong(CAPACITY))));
    }
    enumOf(PowerState.class, data.getString(STATE))
        .ifPresent(state -> tooltip.add(Component.translatable(JadeKeys.state(state))));
  }

  @Override
  public ResourceLocation getUid() {
    return JadeKeys.ROBOT_UID;
  }

  /** In the color of the collar of each tier. */
  private static Component tierLine(RobotTier tier) {
    ChatFormatting color =
        switch (tier) {
          case BASE -> ChatFormatting.GRAY;
          case ADVANCED -> ChatFormatting.BLUE;
          case ELITE -> ChatFormatting.LIGHT_PURPLE;
          case ULTIMATE -> ChatFormatting.GOLD;
        };
    Component name = Component.translatable(JadeKeys.tierName(tier)).withStyle(color);
    return Component.translatable(JadeKeys.TIER, name);
  }

  private static <E extends Enum<E>> Optional<E> enumOf(Class<E> type, String name) {
    return Arrays.stream(type.getEnumConstants())
        .filter(constant -> constant.name().equals(name))
        .findFirst();
  }
}
