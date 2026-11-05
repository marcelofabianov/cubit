package io.github.marcelofabianov.cubit.block;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;

/** How a dock sees a robot, so it works with any robot type (AGENTS.md, rule 42). */
public interface DockableRobot {
  Optional<UUID> ownerId();

  boolean answersDockCall();

  int dockCallRadius();

  void callToDock(BlockPos dock);

  void releaseFromDock();
}
