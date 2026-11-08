package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.item.DamagedRobotItem;
import io.github.marcelofabianov.cubit.item.RobotItem;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitItems {
  private static final int ROBOT_STACK_SIZE = 1;
  private static final int TOOLBOX_STACK_SIZE = 16;
  private static final int DOCK_STACK_SIZE = 1;
  private static final String DAMAGED_PREFIX = "damaged_";
  private static final String TOOLBOX_ID = "toolbox";
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

  public static final Supplier<Item> DAMAGED_GENERAL_ROBOT =
      ITEMS.register(
          DAMAGED_PREFIX + CubitConfig.GENERAL_ROBOT.id(),
          () ->
              new DamagedRobotItem(
                  new Item.Properties().stacksTo(ROBOT_STACK_SIZE).fireResistant()));

  public static final Supplier<Item> TOOLBOX =
      ITEMS.register(
          TOOLBOX_ID, () -> new Item(new Item.Properties().stacksTo(TOOLBOX_STACK_SIZE)));

  public static final Supplier<Item> INDUCTION_DOCK =
      ITEMS.register(
          CubitBlocks.INDUCTION_DOCK_ID,
          () ->
              new BlockItem(
                  CubitBlocks.INDUCTION_DOCK.get(),
                  new Item.Properties().stacksTo(DOCK_STACK_SIZE)));

  private CubitItems() {}

  public static void register(IEventBus modBus) {
    ITEMS.register(modBus);
  }
}
