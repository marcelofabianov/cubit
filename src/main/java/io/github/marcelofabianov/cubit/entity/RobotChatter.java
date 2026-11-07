package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.BiomeWatch;
import io.github.marcelofabianov.cubit.domain.behavior.ChatRules;
import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import io.github.marcelofabianov.cubit.domain.behavior.ChatWatch;
import io.github.marcelofabianov.cubit.domain.behavior.FollowDecision;
import io.github.marcelofabianov.cubit.domain.behavior.LeftBehindWatch;
import io.github.marcelofabianov.cubit.domain.behavior.NoticeRepeat;
import io.github.marcelofabianov.cubit.domain.behavior.ScanRules;
import io.github.marcelofabianov.cubit.domain.behavior.WeatherReading;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The comments of the robot that follows, on the weather and on what its owner achieves. The rules
 * live in the domain. Only the switch is saved, because the rest is recalculated.
 */
public final class RobotChatter implements RobotSwitch {
  private static final String KEY = "notice.cubit.chat.%s.%d";

  private final RobotEntity robot;
  private boolean switchedOn = true;
  private ChatWatch watch;
  private NoticeRepeat repeat = NoticeRepeat.READY;
  private LeftBehindWatch leftBehind = LeftBehindWatch.READY;
  private BiomeWatch biome = BiomeWatch.UNKNOWN;
  private NoticeRepeat animalRepeat = NoticeRepeat.READY;
  private NoticeRepeat redstoneRepeat = NoticeRepeat.READY;
  private NoticeRepeat biomeRepeat = NoticeRepeat.READY;
  private NoticeRepeat leftBehindRepeat = NoticeRepeat.READY;

  RobotChatter(RobotEntity robot) {
    this.robot = robot;
  }

  public static String keyOf(ChatTopic topic, int phrase) {
    return KEY.formatted(topic.name().toLowerCase(Locale.ROOT), phrase);
  }

  /** The robot comments on what its owner killed. The draw is here, so a test can fix it. */
  public static void onOwnerKilled(ServerPlayer owner, Animal animal) {
    RobotEntity.follower(owner)
        .map(id -> owner.serverLevel().getEntity(id))
        .filter(RobotEntity.class::isInstance)
        .map(RobotEntity.class::cast)
        .ifPresent(
            robot -> robot.chatter().remarkOn(animal, robot.getRandom().nextInt(Percentage.MAX)));
  }

  /** The robot loves redstone, so it comments when its owner mines it (answer 619). */
  public static void onOwnerMinedRedstone(ServerPlayer owner) {
    RobotEntity.follower(owner)
        .map(id -> owner.serverLevel().getEntity(id))
        .filter(RobotEntity.class::isInstance)
        .map(RobotEntity.class::cast)
        .ifPresent(robot -> robot.chatter().remarkOnRedstone());
  }

  /** The robot may be loaded in another dimension, where it would not be following. */
  public static void congratulate(ServerPlayer owner, boolean challenge) {
    RobotEntity.follower(owner)
        .map(id -> owner.serverLevel().getEntity(id))
        .filter(RobotEntity.class::isInstance)
        .map(RobotEntity.class::cast)
        .ifPresent(robot -> robot.chatter().congratulate(challenge));
  }

  void setSwitchedOn(boolean switchedOn) {
    this.switchedOn = switchedOn;
  }

  boolean switchedOn() {
    return switchedOn;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  @Override
  public boolean isAvailable() {
    return robot.powerState() != PowerState.HIBERNATING;
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
  }

  /** A change the robot could not comment on is gone, so a storm is not announced after it ends. */
  void tick() {
    repeat = repeat.tick();
    leftBehindRepeat = leftBehindRepeat.tick();
    biomeRepeat = biomeRepeat.tick();
    animalRepeat = animalRepeat.tick();
    redstoneRepeat = redstoneRepeat.tick();
    if (!runs()) {
      watch = null;
      leftBehind = LeftBehindWatch.READY;
      biome = BiomeWatch.UNKNOWN;
      return;
    }
    checkLeftBehind();
    if (ScanRules.isDue(robot.tickCount, CubitMod.CONFIG.chatterCheckTicks())) {
      readTheWorld();
    }
  }

  /** Reads the world now, for what cannot wait the interval. It obeys the same rules. */
  public void readTheWorldNow() {
    if (runs()) {
      readTheWorld();
    }
  }

  /** Reading a distance is cheap, and a running owner would slip past a slower check. */
  private void checkLeftBehind() {
    FollowDecision follow = CubitMod.CONFIG.followDecision();
    LeftBehindWatch.Step step =
        leftBehind.next(
            robot.distanceTo(RobotInteractions.followedPlayer(robot)),
            follow.stopDistanceBlocks(),
            CubitMod.CONFIG.chatterLeftBehindBlocks(),
            follow.teleportDistanceBlocks());
    leftBehind = step.watch();
    if (step.speaks() && mayComplain() && say(ChatTopic.LEFT_BEHIND)) {
      robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
      leftBehindRepeat = leftBehindRepeat.shown(CubitMod.CONFIG.chatterLeftBehindRepeatTicks());
    }
  }

  /** On fire or on the way to a dock, the robot is not trying to follow. */
  private boolean mayComplain() {
    return leftBehindRepeat.canShow()
        && !robot.isOnFire()
        && !robot.docking().hasTrip()
        && ChatRules.isCalm(RobotFace.expression(robot));
  }

  private boolean runs() {
    Player owner = RobotInteractions.followedPlayer(robot);
    boolean follows = owner != null && owner.level() == robot.level();
    return ChatRules.runs(switchedOn, robot.powerState(), follows);
  }

  private void readTheWorld() {
    readTheBiome();
    readTheWeather();
  }

  private void readTheBiome() {
    BlockPos above = robot.blockPosition().above();
    BiomeWatch.Step step = biome.next(RobotBiomes.groupOf(robot.level().getBiome(above)));
    biome = step.watch();
    if (biomeRepeat.canShow() && ChatRules.isCalm(RobotFace.expression(robot))) {
      step.topic().ifPresent(this::commentOnTheBiome);
    }
  }

  private void commentOnTheBiome(ChatTopic topic) {
    if (say(topic)) {
      robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
      biomeRepeat = biomeRepeat.shown(CubitMod.CONFIG.chatterBiomeRepeatTicks());
    }
  }

  private void readTheWeather() {
    WeatherReading reading = read();
    if (watch == null) {
      watch = ChatWatch.first(reading);
      return;
    }
    ChatWatch.Step step = watch.next(reading);
    watch = step.watch();
    if (repeat.canShow() && ChatRules.isCalm(RobotFace.expression(robot))) {
      step.topic().ifPresent(this::commentOnTheWeather);
    }
  }

  private void commentOnTheWeather(ChatTopic topic) {
    if (say(topic)) {
      robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
      repeat = repeat.shown(CubitMod.CONFIG.chatterRepeatTicks());
    }
  }

  public void remarkOn(Animal animal, int roll) {
    boolean pet = animal instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null;
    boolean remarks =
        ChatRules.remarksOnAnimal(pet, roll, CubitMod.CONFIG.chatterAnimalChancePercent());
    if (remarks
        && runs()
        && animalRepeat.canShow()
        && ChatRules.isCalm(RobotFace.expression(robot))
        && say(RobotAnimals.topicOf(animal))) {
      robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
      animalRepeat = animalRepeat.shown(CubitMod.CONFIG.chatterAnimalRepeatTicks());
    }
  }

  /** Calm only, so it never covers the REDSTONE!!! of the scanner, whose surprise is not calm. */
  public void remarkOnRedstone() {
    if (runs()
        && redstoneRepeat.canShow()
        && ChatRules.isCalm(RobotFace.expression(robot))
        && say(ChatTopic.REDSTONE)) {
      robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
      redstoneRepeat = redstoneRepeat.shown(CubitMod.CONFIG.chatterRedstoneRepeatTicks());
    }
  }

  private void congratulate(boolean challenge) {
    if (runs() && say(ChatTopic.ofAchievement(challenge))) {
      if (ChatRules.needsItsOwnBeep(RobotFace.expression(robot))) {
        robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
      }
      robot.face().onInteraction();
    }
  }

  private boolean say(ChatTopic topic) {
    int phrase = robot.getRandom().nextInt(topic.phrases());
    return robot.notices().showNamed(topic.name(), Component.translatable(keyOf(topic, phrase)));
  }

  /** The rain is read where the robot is, so a desert or a snowy biome has no rain. */
  private WeatherReading read() {
    Level level = robot.level();
    BlockPos above = robot.blockPosition().above();
    boolean sky =
        level.dimensionType().hasSkyLight()
            && !level.dimensionType().hasCeiling()
            && level.canSeeSky(above);
    boolean raining = level.isRainingAt(above);
    return new WeatherReading(
        level.getDayTime(),
        sky,
        raining,
        raining && level.isThundering(),
        level.getBiome(above).value().coldEnoughToSnow(above));
  }
}
