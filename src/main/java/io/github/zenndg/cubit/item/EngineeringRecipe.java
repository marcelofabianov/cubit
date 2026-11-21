package io.github.zenndg.cubit.item;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.zenndg.cubit.registry.CubitRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

/**
 * A recipe of the Engineering Desk, with or without a shape, in the same JSON as the recipes of the
 * game, under the type `cubit:engineering` (answer 1459). It runs only in the desk, never in the
 * crafting table, and the mirrored shape counts.
 */
public final class EngineeringRecipe implements CraftingRecipe {
  public static final MapCodec<EngineeringRecipe> CODEC =
      Codec.mapEither(
              RecipeSerializer.SHAPED_RECIPE.codec(), RecipeSerializer.SHAPELESS_RECIPE.codec())
          .xmap(EngineeringRecipe::new, EngineeringRecipe::either);

  public static final StreamCodec<RegistryFriendlyByteBuf, EngineeringRecipe> STREAM_CODEC =
      StreamCodec.of(EngineeringRecipe::write, EngineeringRecipe::read);

  private final CraftingRecipe shape;

  public EngineeringRecipe(ShapedRecipe shaped) {
    this.shape = shaped;
  }

  public EngineeringRecipe(ShapelessRecipe shapeless) {
    this.shape = shapeless;
  }

  private EngineeringRecipe(Either<ShapedRecipe, ShapelessRecipe> either) {
    this.shape = either.map(shaped -> shaped, shapeless -> shapeless);
  }

  /** The shaped recipe, which places its marks in the grid, when there is one. */
  public ShapedRecipe shaped() {
    return shape instanceof ShapedRecipe shaped ? shaped : null;
  }

  private Either<ShapedRecipe, ShapelessRecipe> either() {
    return shape instanceof ShapedRecipe shaped
        ? Either.left(shaped)
        : Either.right((ShapelessRecipe) shape);
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    return shape.matches(input, level);
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    return shape.assemble(input, registries);
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return shape.canCraftInDimensions(width, height);
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider registries) {
    return shape.getResultItem(registries);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return shape.getIngredients();
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    return shape.getRemainingItems(input);
  }

  @Override
  public CraftingBookCategory category() {
    return shape.category();
  }

  @Override
  public String getGroup() {
    return shape.getGroup();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CubitRecipes.ENGINEERING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return CubitRecipes.ENGINEERING_TYPE.get();
  }

  private static void write(RegistryFriendlyByteBuf buffer, EngineeringRecipe recipe) {
    ShapedRecipe shaped = recipe.shaped();
    buffer.writeBoolean(shaped != null);
    if (shaped != null) {
      RecipeSerializer.SHAPED_RECIPE.streamCodec().encode(buffer, shaped);
    } else {
      RecipeSerializer.SHAPELESS_RECIPE
          .streamCodec()
          .encode(buffer, (ShapelessRecipe) recipe.shape);
    }
  }

  private static EngineeringRecipe read(RegistryFriendlyByteBuf buffer) {
    return buffer.readBoolean()
        ? new EngineeringRecipe(RecipeSerializer.SHAPED_RECIPE.streamCodec().decode(buffer))
        : new EngineeringRecipe(RecipeSerializer.SHAPELESS_RECIPE.streamCodec().decode(buffer));
  }
}
