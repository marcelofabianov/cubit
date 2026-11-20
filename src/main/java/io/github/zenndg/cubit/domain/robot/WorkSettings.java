package io.github.zenndg.cubit.domain.robot;

/**
 * The settings of the Work tab: Keep junk of the Fisher, on at first, the unload interval of a
 * Cubit in its area, which follows the config until someone chooses, and the mode of the Organizer,
 * organizing at first (answers 1191, 1254 and 1290). The Farmer has a fertilizer slot instead of a
 * switch (answer 1246).
 */
public record WorkSettings(
    boolean keepJunk, UnloadInterval unloadInterval, OrganizerMode organizerMode) {
  public static final WorkSettings INITIAL =
      new WorkSettings(true, UnloadInterval.CONFIG_DEFAULT, OrganizerMode.INITIAL);

  /** The mode of the Organizer starts organizing, also for what was saved before it (1301). */
  public WorkSettings(boolean keepJunk, UnloadInterval unloadInterval) {
    this(keepJunk, unloadInterval, OrganizerMode.INITIAL);
  }

  public WorkSettings withKeepJunk(boolean on) {
    return new WorkSettings(on, unloadInterval, organizerMode);
  }

  public WorkSettings withUnloadInterval(UnloadInterval interval) {
    return new WorkSettings(keepJunk, interval, organizerMode);
  }

  public WorkSettings withOrganizerMode(OrganizerMode mode) {
    return new WorkSettings(keepJunk, unloadInterval, mode);
  }
}
