package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The languages of the mod. English is the base, and every other language has the same keys, with
 * its own texts (answers 712, 732 and 735).
 */
enum Language {
  EN_US(
      Locales.ENGLISH,
      List.of(
          EnglishGameTexts::addTo,
          ScreenTexts::addTo,
          JadeTexts::addTo,
          UpgradeTexts::addTo,
          DefenseTexts::addTo),
      ChatPhrases::of),
  PT_BR(
      Locales.PORTUGUESE,
      List.of(
          PortugueseGameTexts::addTo,
          PortugueseScreenTexts::addTo,
          PortugueseJadeTexts::addTo,
          PortugueseUpgradeTexts::addTo,
          PortugueseDefenseTexts::addTo),
      PortugueseChatPhrases::of);

  private final String locale;
  private final List<Consumer<BiConsumer<String, String>>> texts;
  private final Function<ChatTopic, List<String>> phrases;

  Language(
      String locale,
      List<Consumer<BiConsumer<String, String>>> texts,
      Function<ChatTopic, List<String>> phrases) {
    this.locale = locale;
    this.texts = texts;
    this.phrases = phrases;
  }

  String locale() {
    return locale;
  }

  void addTexts(BiConsumer<String, String> add) {
    texts.forEach(group -> group.accept(add));
  }

  List<String> phrases(ChatTopic topic) {
    return phrases.apply(topic);
  }

  /** The codes of the game for each language. */
  private static final class Locales {
    private static final String ENGLISH = "en_us";
    private static final String PORTUGUESE = "pt_br";
  }
}
