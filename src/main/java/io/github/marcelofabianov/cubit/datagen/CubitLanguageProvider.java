package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import io.github.marcelofabianov.cubit.entity.RobotChatter;
import java.util.List;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** One language file of the mod. English is the base, and every language has the same keys. */
final class CubitLanguageProvider extends LanguageProvider {
  /** The mod list reads this key before the description of the neoforge.mods.toml (answer 759). */
  static final String MOD_DESCRIPTION_KEY = "fml.menu.mods.info.description." + CubitMod.MOD_ID;

  private final Language language;

  CubitLanguageProvider(PackOutput output, Language language) {
    super(output, CubitMod.MOD_ID, language.locale());
    this.language = language;
  }

  @Override
  protected void addTranslations() {
    language.addTexts(this::add);
    for (ChatTopic topic : ChatTopic.values()) {
      List<String> phrases = language.phrases(topic);
      for (int phrase = 0; phrase < phrases.size(); phrase++) {
        add(RobotChatter.keyOf(topic, phrase), phrases.get(phrase));
      }
    }
  }
}
