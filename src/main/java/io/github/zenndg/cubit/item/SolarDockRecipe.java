package io.github.zenndg.cubit.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zenndg.cubit.registry.CubitRecipes;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * The Dock Solar Panel and an Induction Dock, in any position of the grid, make the Solar Dock,
 * which keeps the tier, the energy and the storage of the dock (answers 1384 and 1386).
 */
public final class SolarDockRecipe extends CustomRecipe {
  private static final String PANEL_FIELD = "panel";
  private static final String DOCK_FIELD = "dock";
  private static final String RESULT_FIELD = "result";
  private static final int INGREDIENTS = 2;
  private static final int ONE = 1;

  public static final MapCodec<SolarDockRecipe> CODEC =
      RecordCodecBuilder.mapCodec(instance ->
          instance
              .group(
                  Ingredient.CODEC_NONEMPTY.fieldOf(PANEL_FIELD).forGetter(recipe -> recipe.panel),
                  Ingredient.CODEC_NONEMPTY.fieldOf(DOCK_FIELD).forGetter(recipe -> recipe.dock),
                  BuiltInRegistries.ITEM
                      .byNameCodec()
                      .fieldOf(RESULT_FIELD)
                      .forGetter(recipe -> recipe.result))
              .apply(instance, SolarDockRecipe::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, SolarDockRecipe> STREAM_CODEC =
      StreamCodec.composite(
          Ingredient.CONTENTS_STREAM_CODEC,
          recipe -> recipe.panel,
          Ingredient.CONTENTS_STREAM_CODEC,
          recipe -> recipe.dock,
          ByteBufCodecs.registry(Registries.ITEM),
          recipe -> recipe.result,
          SolarDockRecipe::new);

  private final Ingredient panel;
  private final Ingredient dock;
  private final Item result;

  public SolarDockRecipe(Ingredient panel, Ingredient dock, Item result) {
    super(CraftingBookCategory.MISC);
    this.panel = panel;
    this.dock = dock;
    this.result = result;
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    return dockIn(input).isPresent();
  }

  /** The components of the dock pass to the Solar Dock: tier, energy, storage and name. */
  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    return dockIn(input).map(stack -> stack.transmuteCopy(result, ONE)).orElse(ItemStack.EMPTY);
  }

  /** Shown in the recipe book, which hides special recipes. */
  @Override
  public boolean isSpecial() {
    return false;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return NonNullList.of(Ingredient.EMPTY, panel, dock);
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider registries) {
    return new ItemStack(result);
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= INGREDIENTS;
  }

  @Override
  public RecipeSerializer<SolarDockRecipe> getSerializer() {
    return CubitRecipes.SOLAR_DOCK_ASSEMBLY.get();
  }

  /** The dock, when the grid has only the panel and one dock. */
  private Optional<ItemStack> dockIn(CraftingInput input) {
    ItemStack found = ItemStack.EMPTY;
    boolean hasPanel = false;
    int filled = 0;
    for (int slot = 0; slot < input.size(); slot++) {
      ItemStack stack = input.getItem(slot);
      if (stack.isEmpty()) {
        continue;
      }
      filled++;
      if (panel.test(stack)) {
        hasPanel = true;
      } else if (dock.test(stack)) {
        found = stack;
      }
    }
    return filled == INGREDIENTS && hasPanel && !found.isEmpty()
        ? Optional.of(found)
        : Optional.empty();
  }
}
