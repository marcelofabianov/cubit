package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A kind of ore, by its tag: a subtag such as {@code c:ores/diamond} or, for an ore of another mod
 * that only has the parent tag, {@link #ANY}. Tags instead of a list of blocks, so the ores of
 * every mod count (AGENTS.md, rule 7).
 */
public record OreKind(String tag) {
  private static final String PARENT_TAG = "c:ores";
  private static final String SUBTAG_PREFIX = "c:ores/";
  private static final String INVALID_MESSAGE = "Not an ore tag: ";
  private static final String WORD_SEPARATORS = "[_/]";
  private static final String SPACE = " ";

  public static final OreKind ANY = new OreKind(PARENT_TAG);

  public OreKind {
    if (!isOreTag(tag)) {
      throw new IllegalArgumentException(INVALID_MESSAGE + tag);
    }
  }

  public static Optional<OreKind> parse(String tag) {
    return isOreTag(tag) ? Optional.of(new OreKind(tag)) : Optional.empty();
  }

  /** A block is in the parent tag and in its subtags, and only the subtags say what it is. */
  public static List<OreKind> kindsOf(Collection<String> tags) {
    List<OreKind> named =
        tags.stream()
            .filter(tag -> tag.startsWith(SUBTAG_PREFIX))
            .filter(OreKind::isOreTag)
            .sorted()
            .map(OreKind::new)
            .toList();
    if (!named.isEmpty()) {
      return named;
    }
    return tags.contains(PARENT_TAG) ? List.of(ANY) : List.of();
  }

  /** The name shown to the player, in capitals. The parent tag does not say which ore it is. */
  public Optional<String> name() {
    if (tag.equals(PARENT_TAG)) {
      return Optional.empty();
    }
    String words = tag.substring(SUBTAG_PREFIX.length()).replaceAll(WORD_SEPARATORS, SPACE);
    return Optional.of(words.toUpperCase(Locale.ROOT));
  }

  private static boolean isOreTag(String tag) {
    return tag.equals(PARENT_TAG)
        || tag.startsWith(SUBTAG_PREFIX) && tag.length() > SUBTAG_PREFIX.length();
  }
}
