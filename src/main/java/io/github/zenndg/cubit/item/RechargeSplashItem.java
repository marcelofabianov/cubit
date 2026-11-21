package io.github.zenndg.cubit.item;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.entity.RechargeSplashProjectile;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * The Recharge Splash, thrown like a splash potion of the game. Only the server spawns the flask,
 * and the impact decides who receives (answers 1400 to 1419).
 */
public final class RechargeSplashItem extends Item {
  public static final String TOOLTIP_KEY = "item.cubit.recharge_splash.tooltip";
  private static final int ONE = 1;
  private static final float VOLUME = 0.5f;
  private static final float PITCH = 0.4f;
  private static final float PITCH_BASE = 0.8f;
  private static final float THROW_TILT = -20f;
  private static final float THROW_SPEED = 0.5f;
  private static final float THROW_SPREAD = 1f;

  public RechargeSplashItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    float pitch = PITCH / (level.getRandom().nextFloat() * PITCH + PITCH_BASE);
    level.playSound(
        null,
        player.getX(),
        player.getY(),
        player.getZ(),
        SoundEvents.SPLASH_POTION_THROW,
        SoundSource.PLAYERS,
        VOLUME,
        pitch);
    if (!level.isClientSide()) {
      RechargeSplashProjectile flask = new RechargeSplashProjectile(level, player);
      flask.setItem(stack);
      flask.shootFromRotation(
          player, player.getXRot(), player.getYRot(), THROW_TILT, THROW_SPEED, THROW_SPREAD);
      level.addFreshEntity(flask);
    }
    player.awardStat(Stats.ITEM_USED.get(this));
    stack.consume(ONE, player);
    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
  }

  /** The seconds come from the config, so the text matches the notice. */
  @Override
  public void appendHoverText(
      ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
    int seconds = CubitMod.CONFIG.rechargeSplash().spec().durationSeconds();
    lines.add(Component.translatable(TOOLTIP_KEY, seconds).withStyle(ChatFormatting.GRAY));
  }
}
