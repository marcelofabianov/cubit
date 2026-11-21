package io.github.zenndg.cubit.block;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import org.slf4j.Logger;

/** The name of the owner of a dock: online, or from the cache of profiles of the server. */
final class DockOwnerName {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String UNKNOWN_OWNER_MESSAGE = "The dock owner at {} has no known name.";

  private DockOwnerName() {}

  static Optional<String> of(MinecraftServer server, UUID owner, BlockPos pos) {
    Optional<String> name =
        Optional.ofNullable(server.getPlayerList().getPlayer(owner))
            .map(player -> player.getGameProfile().getName())
            .or(() -> cached(server, owner));
    if (name.isEmpty()) {
      LOGGER.warn(UNKNOWN_OWNER_MESSAGE, pos);
    }
    return name;
  }

  private static Optional<String> cached(MinecraftServer server, UUID owner) {
    GameProfileCache cache = server.getProfileCache();
    return cache == null ? Optional.empty() : cache.get(owner).map(GameProfile::getName);
  }
}
