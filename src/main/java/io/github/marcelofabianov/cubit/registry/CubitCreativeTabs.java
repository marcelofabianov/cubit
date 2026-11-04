package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** The creative tab of the mod. */
public final class CubitCreativeTabs {
  public static final String MAIN_TITLE_KEY = "itemGroup.cubit.main";
  private static final String MAIN_ID = "main";
  private static final DeferredRegister<CreativeModeTab> TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CubitMod.MOD_ID);

  public static final Supplier<CreativeModeTab> MAIN =
      TABS.register(
          MAIN_ID,
          () ->
              CreativeModeTab.builder()
                  .title(Component.translatable(MAIN_TITLE_KEY))
                  .icon(() -> new ItemStack(CubitItems.GENERAL_ROBOT.get()))
                  .displayItems(
                      (parameters, output) -> output.accept(CubitItems.GENERAL_ROBOT.get()))
                  .build());

  private CubitCreativeTabs() {}

  public static void register(IEventBus modBus) {
    TABS.register(modBus);
  }
}
