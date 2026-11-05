package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Entity types. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitEntities {
  private static final float ROBOT_WIDTH = 0.75f;
  private static final float ROBOT_HEIGHT = 1f;
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

  private CubitEntities() {}

  public static void register(IEventBus modBus) {
    ENTITIES.register(modBus);
    modBus.addListener(CubitEntities::createAttributes);
  }

  private static void createAttributes(EntityAttributeCreationEvent event) {
    event.put(GENERAL_ROBOT.get(), RobotEntity.createAttributes().build());
  }
}
