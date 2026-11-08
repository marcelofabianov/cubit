package io.github.marcelofabianov.cubit.datagen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/** Every language of the mod has the same keys as English, the base (answers 712 and 735). */
class LanguageFilesTest {
  private static final Path LANG = Path.of("src/generated/resources/assets/cubit/lang");
  private static final String ENGLISH = "en_us.json";
  private static final String PORTUGUESE = "pt_br.json";
  private static final Pattern KEY = Pattern.compile("^\\s*\"([^\"]+)\"\\s*:", Pattern.MULTILINE);
  private static final String SAME_KEYS = "The Portuguese file must have the same keys as English";
  private static final String EMPTY = "The English file must have keys";

  private static Set<String> keysOf(String file) throws IOException {
    Matcher matcher = KEY.matcher(Files.readString(LANG.resolve(file)));
    Set<String> keys = new TreeSet<>();
    while (matcher.find()) {
      keys.add(matcher.group(1));
    }
    return keys;
  }

  @Test
  void portugueseHasTheSameKeysAsEnglish() throws IOException {
    Set<String> english = keysOf(ENGLISH);
    assertFalse(english.isEmpty(), EMPTY);
    assertEquals(english, keysOf(PORTUGUESE), SAME_KEYS);
  }
}
