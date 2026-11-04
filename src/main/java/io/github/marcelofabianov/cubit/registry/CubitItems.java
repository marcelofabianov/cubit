package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.item.RobotItem;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitItems {
  private static final int ROBOT_STACK_SIZE = 1;
  private static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(Registries.ITEM, CubitMod.MOD_ID);

  public static final Supplier<Item> GENERAL_ROBOT =
      ITEMS.register(
          CubitConfig.GENERAL_ROBOT.id(),
          () ->
              new RobotItem(
                  new Item.Properties().stacksTo(ROBOT_STACK_SIZE),
                  CubitConfig.GENERAL_ROBOT,
                  CubitEntities.GENERAL_ROBOT));

  private CubitItems() {}

  public static void register(IEventBus modBus) {
    ITEMS.register(modBus);
  }
}
