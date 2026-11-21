package io.github.zenndg.cubit.registry;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.DockCapabilities;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.entity.RechargeSplashProjectile;
import io.github.zenndg.cubit.entity.RobotDockedView;
import io.github.zenndg.cubit.entity.RobotEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Entity types. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitEntities {
  /** As the mobs of the game, so it passes an open door, which leaves 13/16 free (1372). */
  private static final float ROBOT_WIDTH = 0.6f;

  private static final float ROBOT_HEIGHT = 1f;

  /** The size, range and update interval of the splash potion of the game. */
  private static final float FLASK_SIZE = 0.25f;

  private static final int FLASK_TRACKING_RANGE = 4;
  private static final int FLASK_UPDATE_INTERVAL = 10;
  private static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(Registries.ENTITY_TYPE, CubitMod.MOD_ID);

  // MISC, because a robot is not a mob that counts toward the natural spawn limits.
  public static final Supplier<EntityType<RobotEntity>> GENERAL_ROBOT =
      ENTITIES.register(
          CubitConfig.GENERAL_ROBOT.id(),
          () ->
              EntityType.Builder.<RobotEntity>of(
                      (type, level) ->
                          new RobotEntity(
                              type,
                              level,
                              CubitConfig.GENERAL_ROBOT,
                              CubitItems.GENERAL_ROBOT,
                              CubitItems.DAMAGED_GENERAL_ROBOT),
                      MobCategory.MISC)
                  .sized(ROBOT_WIDTH, ROBOT_HEIGHT)
                  .build(CubitConfig.GENERAL_ROBOT.id()));

  public static final Supplier<EntityType<RechargeSplashProjectile>> RECHARGE_SPLASH =
      ENTITIES.register(
          CubitItems.RECHARGE_SPLASH_ID,
          () ->
              EntityType.Builder.<RechargeSplashProjectile>of(
                      RechargeSplashProjectile::new, MobCategory.MISC)
                  .sized(FLASK_SIZE, FLASK_SIZE)
                  .clientTrackingRange(FLASK_TRACKING_RANGE)
                  .updateInterval(FLASK_UPDATE_INTERVAL)
                  .build(CubitItems.RECHARGE_SPLASH_ID));

  private CubitEntities() {}

  public static void register(IEventBus modBus) {
    ENTITIES.register(modBus);
    modBus.addListener(CubitEntities::createAttributes);
    modBus.addListener(CubitEntities::registerCapabilities);
  }

  private static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerEntity(
        DockCapabilities.DOCKED_ROBOT,
        GENERAL_ROBOT.get(),
        (robot, context) -> new RobotDockedView(robot));
  }

  private static void createAttributes(EntityAttributeCreationEvent event) {
    event.put(GENERAL_ROBOT.get(), RobotEntity.createAttributes().build());
  }
}
