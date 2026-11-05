package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/** Generates the files in src/generated, which are never edited by hand. */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class CubitDataGenerators {
  private CubitDataGenerators() {}

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput output = generator.getPackOutput();
    ExistingFileHelper existingFiles = event.getExistingFileHelper();
    generator.addProvider(event.includeClient(), new CubitLanguageProvider(output));
    generator.addProvider(event.includeClient(), new CubitItemModelProvider(output, existingFiles));
    generator.addProvider(
        event.includeServer(),
        new CubitEntityTagsProvider(output, event.getLookupProvider(), existingFiles));
    generator.addProvider(
        event.includeServer(), new CubitRecipeProvider(output, event.getLookupProvider()));
  }
}
