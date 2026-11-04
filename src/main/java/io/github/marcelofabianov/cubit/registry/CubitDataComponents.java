package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.item.RobotStateComponent;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Item data. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitDataComponents {
  private static final String ROBOT_STATE_ID = "robot_state";
  private static final DeferredRegister.DataComponents COMPONENTS =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CubitMod.MOD_ID);

  public static final Supplier<DataComponentType<RobotState>> ROBOT_STATE =
      COMPONENTS.registerComponentType(
          ROBOT_STATE_ID,
          builder ->
              builder
                  .persistent(RobotStateComponent.CODEC)
                  .networkSynchronized(RobotStateComponent.STREAM_CODEC));

  private CubitDataComponents() {}

  public static void register(IEventBus modBus) {
    COMPONENTS.register(modBus);
  }
}
