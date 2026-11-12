package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.List;
import java.util.Set;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
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
    for (Language language : Language.values()) {
      generator.addProvider(event.includeClient(), new CubitLanguageProvider(output, language));
    }
    generator.addProvider(event.includeClient(), new CubitItemModelProvider(output, existingFiles));
    generator.addProvider(event.includeClient(), new CubitSoundProvider(output, existingFiles));
    generator.addProvider(
        event.includeServer(),
        new CubitEntityTagsProvider(output, event.getLookupProvider(), existingFiles));
    generator.addProvider(
        event.includeServer(), new CubitRecipeProvider(output, event.getLookupProvider()));
    generator.addProvider(
        event.includeServer(), new CubitDamageTypeProvider(output, event.getLookupProvider()));
    generator.addProvider(
        event.includeClient(), new CubitBlockStateProvider(output, existingFiles));
    generator.addProvider(
        event.includeServer(),
        new CubitBlockTagsProvider(output, event.getLookupProvider(), existingFiles));
    addLoot(event, generator, output);
  }

  private static void addLoot(GatherDataEvent event, DataGenerator generator, PackOutput output) {
    generator.addProvider(
        event.includeServer(),
        new LootTableProvider(
            output,
            Set.of(),
            List.of(
                new LootTableProvider.SubProviderEntry(
                    CubitBlockLootProvider::new, LootContextParamSets.BLOCK)),
            event.getLookupProvider()));
  }
}
