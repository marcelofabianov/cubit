package io.github.zenndg.cubit.domain.behavior;

/**
 * What the Fisher keeps of a catch: everything with Keep junk on, and otherwise all but the junk.
 * Junk is what the tag of junk holds, and an enchanted item never is, so the rod and the bow of the
 * treasure stay (answers 1191 and 1207).
 */
public final class JunkFilter {
  private JunkFilter() {}

  public static boolean keeps(boolean keepJunk, boolean inJunkTag, boolean enchanted) {
    return keepJunk || !inJunkTag || enchanted;
  }
}
