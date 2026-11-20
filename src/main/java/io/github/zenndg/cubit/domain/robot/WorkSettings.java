package io.github.zenndg.cubit.domain.robot;

/**
 * The settings of the Work tab: Keep junk of the Fisher, on at first, and the unload interval of a
 * Cubit in its area, which follows the config until someone chooses (answers 1191 and 1254). The
 * Farmer has a fertilizer slot instead of a switch (answer 1246).
 */
public record WorkSettings(boolean keepJunk, UnloadInterval unloadInterval) {
  public static final WorkSettings INITIAL = new WorkSettings(true, UnloadInterval.CONFIG_DEFAULT);

  public WorkSettings withKeepJunk(boolean on) {
    return new WorkSettings(on, unloadInterval);
  }

  public WorkSettings withUnloadInterval(UnloadInterval interval) {
    return new WorkSettings(keepJunk, interval);
  }
}
