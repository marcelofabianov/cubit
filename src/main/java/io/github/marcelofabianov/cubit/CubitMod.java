package io.github.marcelofabianov.cubit;

import io.github.marcelofabianov.cubit.config.CubitConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(CubitMod.MOD_ID)
public final class CubitMod {
  public static final String MOD_ID = "cubit";
  public static final CubitConfig CONFIG = new CubitConfig();

  public CubitMod(ModContainer container) {
    container.registerConfig(ModConfig.Type.COMMON, CONFIG.spec());
  }
}
