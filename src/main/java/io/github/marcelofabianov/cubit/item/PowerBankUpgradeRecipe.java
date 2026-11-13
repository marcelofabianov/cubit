package io.github.marcelofabianov.cubit.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerBankUpgrade;
import io.github.marcelofabianov.cubit.registry.CubitRecipes;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

/**
 * A shaped recipe that takes the Power Bank of the grid to the next tier with its charge (answer
 * 902). A charge that does not fit the new bank refuses the recipe, so no FE is lost (answer 907).
 */
public final class PowerBankUpgradeRecipe extends ShapedRecipe {
  private static final String GROUP_FIELD = "group";
  private static final String CATEGORY_FIELD = "category";
  private static final String RESULT_FIELD = "result";
  private static final String EMPTY_GROUP = "";

  public static final MapCodec<PowerBankUpgradeRecipe> CODEC =
      RecordCodecBuilder.mapCodec(instance ->
          instance
              .group(
                  Codec.STRING
                      .optionalFieldOf(GROUP_FIELD, EMPTY_GROUP)
                      .forGetter(ShapedRecipe::getGroup),
                  CraftingBookCategory.CODEC
                      .fieldOf(CATEGORY_FIELD)
                      .orElse(CraftingBookCategory.MISC)
                      .forGetter(ShapedRecipe::category),
                  ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.shape),
                  ItemStack.STRICT_CODEC.fieldOf(RESULT_FIELD).forGetter(recipe -> recipe.output))
              .apply(instance, PowerBankUpgradeRecipe::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, PowerBankUpgradeRecipe> STREAM_CODEC =
      StreamCodec.of(PowerBankUpgradeRecipe::write, PowerBankUpgradeRecipe::read);

  private final ShapedRecipePattern shape;
  private final ItemStack output;

  public PowerBankUpgradeRecipe(
      String group, CraftingBookCategory category, ShapedRecipePattern shape, ItemStack output) {
    super(group, category, shape, output);
    this.shape = shape;
    this.output = output;
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    return super.matches(input, level) && carried(input).isPresent();
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    ItemStack next = super.assemble(input, registries);
    carried(input).ifPresent(battery -> PowerBankItem.store(next, battery));
    return next;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CubitRecipes.POWER_BANK_UPGRADE.get();
  }

  private Optional<Battery> carried(CraftingInput input) {
    if (!(output.getItem() instanceof PowerBankItem next)) {
      return Optional.empty();
    }
    return input.items().stream()
        .filter(PowerBankItem::is)
        .findFirst()
        .flatMap(previous ->
            PowerBankUpgrade.carry(
                PowerBankItem.batteryOf(previous), PowerBankItem.capacityOf(next.tier())));
  }

  private static void write(RegistryFriendlyByteBuf buffer, PowerBankUpgradeRecipe recipe) {
    buffer.writeUtf(recipe.getGroup());
    buffer.writeEnum(recipe.category());
    ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.shape);
    ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
  }

  private static PowerBankUpgradeRecipe read(RegistryFriendlyByteBuf buffer) {
    return new PowerBankUpgradeRecipe(
        buffer.readUtf(),
        buffer.readEnum(CraftingBookCategory.class),
        ShapedRecipePattern.STREAM_CODEC.decode(buffer),
        ItemStack.STREAM_CODEC.decode(buffer));
  }
}
