package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Screens that the server opens on the client. */
public final class CubitMenus {
  private static final String ROBOT_ID = "robot";
  private static final DeferredRegister<MenuType<?>> MENUS =
      DeferredRegister.create(Registries.MENU, CubitMod.MOD_ID);

  public static final Supplier<MenuType<RobotMenu>> ROBOT =
      MENUS.register(ROBOT_ID, () -> IMenuTypeExtension.create(RobotMenu::onClient));

  public static final Supplier<MenuType<InductionDockMenu>> INDUCTION_DOCK =
      MENUS.register(
          CubitBlocks.INDUCTION_DOCK_ID,
          () -> IMenuTypeExtension.create(InductionDockMenu::onClient));

  private CubitMenus() {}

  public static void register(IEventBus modBus) {
    MENUS.register(modBus);
  }
}
