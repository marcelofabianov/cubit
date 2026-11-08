package io.github.marcelofabianov.cubit.compat.jade;

import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * Found by Jade through its annotation, so nothing in the mod references this package and the mod
 * opens without Jade (AGENTS.md, rule 24, and answer 667). The server part runs on the server, and
 * the tooltip part only on the client.
 */
@WailaPlugin
public final class CubitJadePlugin implements IWailaPlugin {
  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerEntityDataProvider(RobotJadeProvider.INSTANCE, RobotEntity.class);
    registration.registerBlockDataProvider(
        DockJadeProvider.INSTANCE, InductionDockBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerEntityComponent(RobotJadeProvider.INSTANCE, RobotEntity.class);
    registration.registerBlockComponent(DockJadeProvider.INSTANCE, InductionDockBlock.class);
  }
}
