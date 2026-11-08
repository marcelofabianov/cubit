package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.function.Supplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

/** Volume and range stay in this file's output, where a resource pack can change them. */
final class CubitSoundProvider extends SoundDefinitionsProvider {
  private static final String FOLDER = "general_robot/";
  private static final String SUBTITLE_PREFIX = "subtitles.cubit.general_robot.";
  private static final int RANGE_BLOCKS = 16;

  CubitSoundProvider(PackOutput output, ExistingFileHelper existingFiles) {
    super(output, CubitMod.MOD_ID, existingFiles);
  }

  static String subtitleKey(String name) {
    return SUBTITLE_PREFIX + name;
  }

  @Override
  public void registerSounds() {
    addSound(CubitSounds.TRACKS, CubitSounds.TRACKS_NAME);
    addSound(CubitSounds.HOSTILE_MOB_ALERT, CubitSounds.HOSTILE_MOB_ALERT_NAME);
    addSound(CubitSounds.RARE_ORE_ALERT, CubitSounds.RARE_ORE_ALERT_NAME);
    addSound(CubitSounds.EXPRESSION_BEEP, CubitSounds.EXPRESSION_BEEP_NAME);
    addSound(CubitSounds.SHORT_CIRCUIT, CubitSounds.SHORT_CIRCUIT_NAME);
    addSound(CubitSounds.POWER_ON, CubitSounds.POWER_ON_NAME);
    addSound(CubitSounds.POWER_OFF, CubitSounds.POWER_OFF_NAME);
    addSound(CubitSounds.RECHARGING, CubitSounds.RECHARGING_NAME);
    addSound(CubitSounds.PLACED, CubitSounds.PLACED_NAME);
  }

  private void addSound(Supplier<SoundEvent> event, String name) {
    ResourceLocation file = ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, FOLDER + name);
    add(
        event,
        definition()
            .subtitle(subtitleKey(name))
            .with(sound(file).attenuationDistance(RANGE_BLOCKS)));
  }
}
