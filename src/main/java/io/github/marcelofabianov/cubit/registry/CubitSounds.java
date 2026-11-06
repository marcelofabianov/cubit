package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ContinuousSound;
import io.github.marcelofabianov.cubit.domain.behavior.PowerSound;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** The sounds, named by robot type, so another robot can have its own without a migration. */
public final class CubitSounds {
  public static final String TRACKS_NAME = "tracks";
  public static final String HOSTILE_MOB_ALERT_NAME = "hostile_mob_alert";
  public static final String RARE_ORE_ALERT_NAME = "rare_ore_alert";
  public static final String EXPRESSION_BEEP_NAME = "expression_beep";
  public static final String SHORT_CIRCUIT_NAME = "short_circuit";
  public static final String POWER_ON_NAME = "power_on";
  public static final String POWER_OFF_NAME = "power_off";
  public static final String RECHARGING_NAME = "recharging";
  public static final String PLACED_NAME = "placed";
  private static final String GENERAL_ROBOT_PREFIX = "general_robot.";
  private static final DeferredRegister<SoundEvent> SOUNDS =
      DeferredRegister.create(Registries.SOUND_EVENT, CubitMod.MOD_ID);

  public static final Supplier<SoundEvent> TRACKS = generalRobot(TRACKS_NAME);
  public static final Supplier<SoundEvent> HOSTILE_MOB_ALERT = generalRobot(HOSTILE_MOB_ALERT_NAME);
  public static final Supplier<SoundEvent> RARE_ORE_ALERT = generalRobot(RARE_ORE_ALERT_NAME);
  public static final Supplier<SoundEvent> EXPRESSION_BEEP = generalRobot(EXPRESSION_BEEP_NAME);
  public static final Supplier<SoundEvent> SHORT_CIRCUIT = generalRobot(SHORT_CIRCUIT_NAME);
  public static final Supplier<SoundEvent> POWER_ON = generalRobot(POWER_ON_NAME);
  public static final Supplier<SoundEvent> POWER_OFF = generalRobot(POWER_OFF_NAME);
  public static final Supplier<SoundEvent> RECHARGING = generalRobot(RECHARGING_NAME);
  public static final Supplier<SoundEvent> PLACED = generalRobot(PLACED_NAME);

  private CubitSounds() {}

  public static void register(IEventBus modBus) {
    SOUNDS.register(modBus);
  }

  public static SoundEvent of(PowerSound sound) {
    return switch (sound) {
      case POWER_ON -> POWER_ON.get();
      case POWER_OFF -> POWER_OFF.get();
    };
  }

  public static SoundEvent of(ContinuousSound sound) {
    return switch (sound) {
      case TRACKS -> TRACKS.get();
      case SHORT_CIRCUIT -> SHORT_CIRCUIT.get();
      case RECHARGING -> RECHARGING.get();
    };
  }

  private static Supplier<SoundEvent> generalRobot(String name) {
    String path = GENERAL_ROBOT_PREFIX + name;
    return SOUNDS.register(
        path,
        () ->
            SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, path)));
  }
}
