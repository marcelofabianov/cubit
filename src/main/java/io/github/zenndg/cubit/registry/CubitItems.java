package io.github.zenndg.cubit.registry;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.DamagedRobotItem;
import io.github.zenndg.cubit.item.EngineeringDeskItem;
import io.github.zenndg.cubit.item.JobCardItem;
import io.github.zenndg.cubit.item.ModuleItem;
import io.github.zenndg.cubit.item.PowerBankItem;
import io.github.zenndg.cubit.item.RechargeSplashItem;
import io.github.zenndg.cubit.item.RobotItem;
import io.github.zenndg.cubit.item.TabletItem;
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
  private static final int MODULE_STACK_SIZE = 64;
  private static final String MODULE_SUFFIX = "_module";
  private static final String POWER_BANK_SUFFIX = "_power_bank";
  private static final int POWER_BANK_STACK_SIZE = 1;
  private static final String DAMAGED_PREFIX = "damaged_";
  private static final String TOOLBOX_ID = "toolbox";
  private static final String TABLET_ID = "tablet";
  private static final int TABLET_STACK_SIZE = 1;
  private static final String FISHER_CARD_ID = "ml_fisher";
  private static final String FARMER_CARD_ID = "ml_farmer";
  private static final String ORGANIZER_CARD_ID = "ml_organizer";
  private static final String COOK_CARD_ID = "ml_cook";
  private static final String CASING_SUFFIX = "_casing";
  private static final String VISOR_SUFFIX = "_visor";
  private static final String TREADS_SUFFIX = "_treads";
  private static final String CORE_SUFFIX = "_core";
  private static final String POWER_BANK_CASING_ID = "power_bank_casing";
  private static final String DOCK_SOLAR_PANEL_ID = "dock_solar_panel";
  private static final String POWER_CELL_SUFFIX = "_power_cell";
  private static final int PART_STACK_SIZE = 64;

  /** The item and the thrown flask share the ID (answer 1400). */
  public static final String RECHARGE_SPLASH_ID = "recharge_splash";

  /** As the snowballs and the eggs, also thrown (answer 1410). */
  private static final int RECHARGE_SPLASH_STACK_SIZE = 16;

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

  /** The card of the Fisher, only by /give in the base of the jobs (answer 1161). */
  public static final Supplier<Item> FISHER_CARD =
      ITEMS.register(FISHER_CARD_ID, () -> new JobCardItem(new Item.Properties(), RobotJob.FISHER));

  public static final Supplier<Item> FARMER_CARD =
      ITEMS.register(FARMER_CARD_ID, () -> new JobCardItem(new Item.Properties(), RobotJob.FARMER));

  public static final Supplier<Item> ORGANIZER_CARD =
      ITEMS.register(
          ORGANIZER_CARD_ID, () -> new JobCardItem(new Item.Properties(), RobotJob.ORGANIZER));

  public static final Supplier<Item> COOK_CARD =
      ITEMS.register(COOK_CARD_ID, () -> new JobCardItem(new Item.Properties(), RobotJob.COOK));

  public static final Supplier<Item> TOOLBOX =
      ITEMS.register(
          TOOLBOX_ID, () -> new Item(new Item.Properties().stacksTo(TOOLBOX_STACK_SIZE)));

  /** Replaces the Cubit Remote, which was only planned, so no save holds it (answer 1026). */
  public static final Supplier<Item> TABLET =
      ITEMS.register(
          TABLET_ID, () -> new TabletItem(new Item.Properties().stacksTo(TABLET_STACK_SIZE)));

  public static final Supplier<Item> INDUCTION_DOCK =
      ITEMS.register(
          CubitBlocks.INDUCTION_DOCK_ID,
          () ->
              new BlockItem(
                  CubitBlocks.INDUCTION_DOCK.get(),
                  new Item.Properties().stacksTo(DOCK_STACK_SIZE)));

  /** The crafting desk of the mod, which keeps its state in the item (answers 1432 and 1481). */
  public static final Supplier<Item> ENGINEERING_DESK =
      ITEMS.register(
          CubitBlocks.ENGINEERING_DESK_ID,
          () ->
              new EngineeringDeskItem(
                  CubitBlocks.ENGINEERING_DESK.get(),
                  new Item.Properties().stacksTo(DOCK_STACK_SIZE)));

  /** The dock with the solar panel, from the panel and a dock (answers 1384 and 1386). */
  public static final Supplier<Item> SOLAR_INDUCTION_DOCK =
      ITEMS.register(
          CubitBlocks.SOLAR_INDUCTION_DOCK_ID,
          () ->
              new BlockItem(
                  CubitBlocks.SOLAR_INDUCTION_DOCK.get(),
                  new Item.Properties().stacksTo(DOCK_STACK_SIZE)));

  /** The panel that goes on a dock, only for the crafting grid (answer 1386). */
  public static final Supplier<Item> DOCK_SOLAR_PANEL = part(DOCK_SOLAR_PANEL_ID);

  public static final Supplier<Item> RECHARGE_SPLASH =
      ITEMS.register(
          RECHARGE_SPLASH_ID,
          () -> new RechargeSplashItem(new Item.Properties().stacksTo(RECHARGE_SPLASH_STACK_SIZE)));

  public static final Supplier<Item> ADVANCED_MODULE = module(RobotTier.ADVANCED);
  public static final Supplier<Item> ELITE_MODULE = module(RobotTier.ELITE);
  public static final Supplier<Item> ULTIMATE_MODULE = module(RobotTier.ULTIMATE);

  public static final Supplier<Item> BASE_POWER_BANK = powerBank(RobotTier.BASE);
  public static final Supplier<Item> ADVANCED_POWER_BANK = powerBank(RobotTier.ADVANCED);
  public static final Supplier<Item> ELITE_POWER_BANK = powerBank(RobotTier.ELITE);
  public static final Supplier<Item> ULTIMATE_POWER_BANK = powerBank(RobotTier.ULTIMATE);

  /** The parts the Cubit is made of, only for the crafting grid (answers 1340 and 1349). */
  public static final Supplier<Item> GENERAL_ROBOT_CASING =
      part(CubitConfig.GENERAL_ROBOT.id() + CASING_SUFFIX);

  public static final Supplier<Item> GENERAL_ROBOT_VISOR =
      part(CubitConfig.GENERAL_ROBOT.id() + VISOR_SUFFIX);
  public static final Supplier<Item> GENERAL_ROBOT_TREADS =
      part(CubitConfig.GENERAL_ROBOT.id() + TREADS_SUFFIX);
  public static final Supplier<Item> GENERAL_ROBOT_CORE =
      part(CubitConfig.GENERAL_ROBOT.id() + CORE_SUFFIX);

  /** The parts the Power Banks are made of: one casing and a cell per tier (answer 1347). */
  public static final Supplier<Item> POWER_BANK_CASING = part(POWER_BANK_CASING_ID);

  public static final Supplier<Item> BASE_POWER_CELL = powerCell(RobotTier.BASE);
  public static final Supplier<Item> ADVANCED_POWER_CELL = powerCell(RobotTier.ADVANCED);
  public static final Supplier<Item> ELITE_POWER_CELL = powerCell(RobotTier.ELITE);
  public static final Supplier<Item> ULTIMATE_POWER_CELL = powerCell(RobotTier.ULTIMATE);

  private CubitItems() {}

  /** The ID is the tier and the suffix, like base_power_bank, from _docs/itens.md. */
  private static Supplier<Item> powerBank(RobotTier tier) {
    return ITEMS.register(
        tier.key() + POWER_BANK_SUFFIX,
        () -> new PowerBankItem(new Item.Properties().stacksTo(POWER_BANK_STACK_SIZE), tier));
  }

  /** The ID is the tier and the suffix, like base_power_cell, from _docs/itens.md. */
  private static Supplier<Item> powerCell(RobotTier tier) {
    return part(tier.key() + POWER_CELL_SUFFIX);
  }

  /** A part has no use outside the crafting grid and stacks like a Module (answer 1352). */
  private static Supplier<Item> part(String id) {
    return ITEMS.register(id, () -> new Item(new Item.Properties().stacksTo(PART_STACK_SIZE)));
  }

  /** The ID is the tier and the suffix, like advanced_module, from _docs/itens.md. */
  private static Supplier<Item> module(RobotTier tier) {
    return ITEMS.register(
        tier.key() + MODULE_SUFFIX,
        () -> new ModuleItem(new Item.Properties().stacksTo(MODULE_STACK_SIZE), tier));
  }

  public static void register(IEventBus modBus) {
    ITEMS.register(modBus);
  }
}
