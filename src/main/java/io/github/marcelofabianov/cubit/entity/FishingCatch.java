package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.JunkFilter;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/**
 * One catch from the fishing table of the game, with the Luck of the Sea of the tier, through a
 * virtual bobber, and with the junk left out when Keep junk is off (answers 1183, 1191 and 1207).
 */
public final class FishingCatch {
  private static final String JUNK_PATH = "fishing_junk";

  /** The junk Keep junk leaves out, which a pack may edit (answer 1207). */
  public static final TagKey<Item> JUNK =
      TagKey.create(
          Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, JUNK_PATH));

  private FishingCatch() {}

  public static List<ItemStack> roll(
      ServerLevel level, Vec3 water, boolean open, int luck, boolean keepJunk) {
    LootParams params =
        new LootParams.Builder(level)
            .withParameter(LootContextParams.ORIGIN, water)
            .withParameter(LootContextParams.TOOL, new ItemStack(Items.FISHING_ROD))
            .withParameter(LootContextParams.THIS_ENTITY, new VirtualFishingHook(level, open))
            .withLuck(luck)
            .create(LootContextParamSets.FISHING);
    return level
        .getServer()
        .reloadableRegistries()
        .getLootTable(BuiltInLootTables.FISHING)
        .getRandomItems(params)
        .stream()
        .filter(stack -> JunkFilter.keeps(keepJunk, stack.is(JUNK), stack.isEnchanted()))
        .toList();
  }
}
