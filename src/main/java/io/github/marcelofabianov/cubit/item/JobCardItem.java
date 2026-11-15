package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A card of Machine Learning, which gives the Cubit its job, one per card (answers 191, 216 and
 * 1161). The cards stack, because the item keeps no data, and the slot takes only the tag.
 */
public final class JobCardItem extends Item {
  private static final String CARDS_PATH = "ml_cards";

  /** The cards the card slot takes, so a pack can add or remove one (answer 1162). */
  public static final TagKey<Item> CARDS =
      TagKey.create(
          Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, CARDS_PATH));

  private final RobotJob job;

  public JobCardItem(Properties properties, RobotJob job) {
    super(properties);
    this.job = job;
  }

  public RobotJob job() {
    return job;
  }

  /** The job of a card in the tag, or none for anything else. */
  public static Optional<RobotJob> jobOf(ItemStack stack) {
    return stack.is(CARDS) && stack.getItem() instanceof JobCardItem card
        ? Optional.of(card.job())
        : Optional.empty();
  }
}
