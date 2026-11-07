package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** The phrases of the chatter, the same as in _docs/conceito/falas.md. */
final class ChatPhrases {
  private static final String DAWN_0 = "The sun is coming up!";
  private static final String DAWN_1 = "Good morning!";
  private static final String DAWN_2 = "Rise and shine!";
  private static final String DAWN_3 = "A new day! Systems ready.";
  private static final String DAWN_4 = "Solar panel, wake up!";
  private static final String DAWN_5 = "Morning already? Beep!";
  private static final String DAWN_6 = "First light detected.";
  private static final String DAWN_7 = "Let's get to work!";
  private static final String NICE_DAY_0 = "What a beautiful day!";
  private static final String NICE_DAY_1 = "Nice weather today!";
  private static final String NICE_DAY_2 = "The sun feels great on my panel.";
  private static final String NICE_DAY_3 = "Perfect day for an adventure!";
  private static final String NICE_DAY_4 = "Blue skies ahead!";
  private static final String NICE_DAY_5 = "Sunshine: my favorite snack.";
  private static final String NICE_DAY_6 = "I could get used to this.";
  private static final String NICE_DAY_7 = "All systems happy!";
  private static final String DUSK_0 = "It's getting dark...";
  private static final String DUSK_1 = "The sun is going down.";
  private static final String DUSK_2 = "Time to turn on the lights?";
  private static final String DUSK_3 = "Night is coming. Stay close!";
  private static final String DUSK_4 = "Monsters come out soon...";
  private static final String DUSK_5 = "Solar panel going to sleep.";
  private static final String DUSK_6 = "Should we head back?";
  private static final String DUSK_7 = "It's getting spooky out here.";
  private static final String RAIN_0 = "Looks like rain.";
  private static final String RAIN_1 = "My circuits don't like water.";
  private static final String RAIN_2 = "Rain detected. Hello, rust!";
  private static final String RAIN_3 = "Where did I leave my umbrella?";
  private static final String RAIN_4 = "I hope I'm waterproof.";
  private static final String RAIN_5 = "Drip, drop, beep!";
  private static final String RAIN_6 = "No sun for my panel today.";
  private static final String RAIN_7 = "Mind the puddles!";
  private static final String STORM_0 = "What a storm!";
  private static final String STORM_1 = "That thunder is loud!";
  private static final String STORM_2 = "Lightning! Stay away from me!";
  private static final String STORM_3 = "I'm made of metal, you know!";
  private static final String STORM_4 = "This storm is scary...";
  private static final String STORM_5 = "Thunder detected. Very loud!";
  private static final String STORM_6 = "Can we go inside now?";
  private static final String STORM_7 = "Please don't strike me!";
  private static final String COLD_0 = "Brrr, it's cold!";
  private static final String COLD_1 = "My battery hates this cold.";
  private static final String COLD_2 = "My circuits are freezing!";
  private static final String COLD_3 = "I should have brought a scarf.";
  private static final String COLD_4 = "Snow! Is it safe for tracks?";
  private static final String COLD_5 = "Beep... b-b-beep...";
  private static final String COLD_6 = "Is it cold, or is it just me?";
  private static final String COLD_7 = "Ice on my antennas!";
  private static final String ACHIEVEMENT_0 = "Congratulations!";
  private static final String ACHIEVEMENT_1 = "Well done!";
  private static final String ACHIEVEMENT_2 = "You did it!";
  private static final String ACHIEVEMENT_3 = "I knew you could do it!";
  private static final String ACHIEVEMENT_4 = "Achievement logged. Beep!";
  private static final String ACHIEVEMENT_5 = "Great job, partner!";
  private static final String ACHIEVEMENT_6 = "Adding that to my memory!";
  private static final String ACHIEVEMENT_7 = "That's my human!";
  private static final String CHALLENGE_0 = "WOW! You're amazing!";
  private static final String CHALLENGE_1 = "Incredible! Beep beep beep!";
  private static final String CHALLENGE_2 = "My circuits are overloading with joy!";
  private static final String CHALLENGE_3 = "That was legendary!";
  private static final String CHALLENGE_4 = "I'm so proud of you!";
  private static final String CHALLENGE_5 = "Best. Human. Ever!";
  private static final String LEFT_BEHIND_0 = "Wait for me!";
  private static final String LEFT_BEHIND_1 = "You walk too fast!";
  private static final String LEFT_BEHIND_2 = "Hey! Slow down!";
  private static final String LEFT_BEHIND_3 = "My tracks can't keep up!";
  private static final String LEFT_BEHIND_4 = "Don't leave me behind!";
  private static final String LEFT_BEHIND_5 = "I only have tiny tracks!";
  private static final String LEFT_BEHIND_6 = "Is this a race?";
  private static final String LEFT_BEHIND_7 = "You know I run on batteries, right?";
  private static final String LEFT_BEHIND_8 = "Wait! Wait! Wait!";
  private static final String LEFT_BEHIND_9 = "Beep beep! Coming through!";
  private static final String LEFT_BEHIND_10 = "Maximum speed reached!";
  private static final String LEFT_BEHIND_11 = "So far away...";
  private static final String LEFT_BEHIND_12 = "Target moving too fast!";
  private static final String LEFT_BEHIND_13 = "Hold on! Almost there!";
  private static final String LEFT_BEHIND_14 = "Why are we running?!";
  private static final String LEFT_BEHIND_15 = "Activating emergency teleport!";
  private static final String BIOME_FOREST_0 = "So many trees! Scanning... too many trees.";
  private static final String BIOME_FOREST_1 = "Leaves on my sensors again.";
  private static final String BIOME_FOREST_2 = "It smells green in here. I think.";
  private static final String BIOME_FOREST_3 = "Do trees have batteries?";
  private static final String BIOME_JUNGLE_0 = "It's so humid! My circuits!";
  private static final String BIOME_JUNGLE_1 = "Vines keep grabbing my antennas!";
  private static final String BIOME_JUNGLE_2 = "Something is watching us. Beep.";
  private static final String BIOME_JUNGLE_3 = "My tracks were not made for this.";
  private static final String BIOME_DESERT_0 = "Sand in my tracks. Again.";
  private static final String BIOME_DESERT_1 = "So hot! Fans at full speed!";
  private static final String BIOME_DESERT_2 = "My solar panel loves it here!";
  private static final String BIOME_DESERT_3 = "Warning: too much sand detected.";
  private static final String BIOME_SWAMP_0 = "Ew. My tracks are all muddy.";
  private static final String BIOME_SWAMP_1 = "This water looks suspicious.";
  private static final String BIOME_SWAMP_2 = "Do I want to know what that smell is?";
  private static final String BIOME_SWAMP_3 = "Careful where you step!";
  private static final String BIOME_OCEAN_0 = "So much water! I can't swim, you know.";
  private static final String BIOME_OCEAN_1 = "I'll wait right here, on dry land.";
  private static final String BIOME_OCEAN_2 = "Salt is terrible for circuits!";
  private static final String BIOME_OCEAN_3 = "Look at the waves! Beep!";
  private static final String BIOME_MOUNTAIN_0 = "Up we go! One block at a time.";
  private static final String BIOME_MOUNTAIN_1 = "What a view! Saving to my memory.";
  private static final String BIOME_MOUNTAIN_2 = "Great signal up here!";
  private static final String BIOME_MOUNTAIN_3 = "Please don't make me climb that.";
  private static final String BIOME_PLAINS_0 = "Finally, flat ground!";
  private static final String BIOME_PLAINS_1 = "Perfect place for my tracks!";
  private static final String BIOME_PLAINS_2 = "So much open space!";
  private static final String BIOME_PLAINS_3 = "Nothing to bump into. I like it.";
  private static final String BIOME_TAIGA_0 = "Pine trees! They smell nice, probably.";
  private static final String BIOME_TAIGA_1 = "It's getting chilly in here.";
  private static final String BIOME_TAIGA_2 = "This place looks cozy.";
  private static final String BIOME_TAIGA_3 = "So many pine cones on my sensors.";
  private static final String BIOME_BADLANDS_0 = "So many colors in these rocks!";
  private static final String BIOME_BADLANDS_1 = "Scanning for gold... maybe?";
  private static final String BIOME_BADLANDS_2 = "This place is dry. Very dry.";
  private static final String BIOME_BADLANDS_3 = "Who painted all these rocks?";
  private static final String BIOME_MUSHROOM_0 = "Giant mushrooms?! Data not found.";
  private static final String BIOME_MUSHROOM_1 = "Why is the cow a mushroom?";
  private static final String BIOME_MUSHROOM_2 = "No hostile mobs here. I like it!";
  private static final String BIOME_MUSHROOM_3 = "My scanner is very confused.";
  private static final String BIOME_NETHER_0 = "Too hot! Way too hot!";
  private static final String BIOME_NETHER_1 = "No sun here for my panel.";
  private static final String BIOME_NETHER_2 = "I don't like this place at all.";
  private static final String BIOME_NETHER_3 = "Please, can we go home?";
  private static final String BIOME_END_0 = "Where is everything?";
  private static final String BIOME_END_1 = "So quiet. Too quiet.";
  private static final String BIOME_END_2 = "My compass makes no sense here.";
  private static final String BIOME_END_3 = "Is this the end? Of what?";
  private static final String ANIMAL_PIG_0 = "This piggy will make great bacon!";
  private static final String ANIMAL_PIG_1 = "Bacon detected! Hehe.";
  private static final String ANIMAL_PIG_2 = "Sorry, little piggy...";
  private static final String ANIMAL_PIG_3 = "Too bad I can't eat bacon.";
  private static final String ANIMAL_PIG_4 = "Oink... offline.";
  private static final String ANIMAL_COW_0 = "Steak for dinner? Nice!";
  private static final String ANIMAL_COW_1 = "No more milk from that one.";
  private static final String ANIMAL_COW_2 = "Moo... system shutdown.";
  private static final String ANIMAL_COW_3 = "Beef! And leather for later!";
  private static final String ANIMAL_COW_4 = "I'd like mine well done. Just kidding.";
  private static final String ANIMAL_CHICKEN_0 = "Chicken dinner! Winner!";
  private static final String ANIMAL_CHICKEN_1 = "Who will lay the eggs now?";
  private static final String ANIMAL_CHICKEN_2 = "Feathers everywhere! My sensors!";
  private static final String ANIMAL_CHICKEN_3 = "Cluck... end of transmission.";
  private static final String ANIMAL_SHEEP_0 = "Baa... baa... bye.";
  private static final String ANIMAL_SHEEP_1 = "Wool! Time for a new bed?";
  private static final String ANIMAL_SHEEP_2 = "You could have just sheared it...";
  private static final String ANIMAL_SHEEP_3 = "Mutton logged. Beep!";
  private static final String ANIMAL_RABBIT_0 = "Hop... stop.";
  private static final String ANIMAL_RABBIT_1 = "Lucky foot? Let me check!";
  private static final String ANIMAL_RABBIT_2 = "It was so fast. You were faster!";
  private static final String ANIMAL_RABBIT_3 = "Rabbit stew loading...";
  private static final String ANIMAL_OTHER_0 = "Food supply increased!";
  private static final String ANIMAL_OTHER_1 = "Good hunt, partner!";
  private static final String ANIMAL_OTHER_2 = "That smells like lunch.";
  private static final String ANIMAL_OTHER_3 = "I'll add that to the menu.";
  private static final String REDSTONE_0 = "I love redstone!";
  private static final String REDSTONE_1 = "Redstone! My favorite snack!";
  private static final String REDSTONE_2 = "Redstone runs in my circuits!";
  private static final Map<ChatTopic, List<String>> PHRASES = new EnumMap<>(ChatTopic.class);

  static {
    PHRASES.put(
        ChatTopic.DAWN, List.of(DAWN_0, DAWN_1, DAWN_2, DAWN_3, DAWN_4, DAWN_5, DAWN_6, DAWN_7));
    PHRASES.put(
        ChatTopic.NICE_DAY,
        List.of(
            NICE_DAY_0,
            NICE_DAY_1,
            NICE_DAY_2,
            NICE_DAY_3,
            NICE_DAY_4,
            NICE_DAY_5,
            NICE_DAY_6,
            NICE_DAY_7));
    PHRASES.put(
        ChatTopic.DUSK, List.of(DUSK_0, DUSK_1, DUSK_2, DUSK_3, DUSK_4, DUSK_5, DUSK_6, DUSK_7));
    PHRASES.put(
        ChatTopic.RAIN, List.of(RAIN_0, RAIN_1, RAIN_2, RAIN_3, RAIN_4, RAIN_5, RAIN_6, RAIN_7));
    PHRASES.put(
        ChatTopic.STORM,
        List.of(STORM_0, STORM_1, STORM_2, STORM_3, STORM_4, STORM_5, STORM_6, STORM_7));
    PHRASES.put(
        ChatTopic.COLD, List.of(COLD_0, COLD_1, COLD_2, COLD_3, COLD_4, COLD_5, COLD_6, COLD_7));
    PHRASES.put(
        ChatTopic.LEFT_BEHIND,
        List.of(
            LEFT_BEHIND_0,
            LEFT_BEHIND_1,
            LEFT_BEHIND_2,
            LEFT_BEHIND_3,
            LEFT_BEHIND_4,
            LEFT_BEHIND_5,
            LEFT_BEHIND_6,
            LEFT_BEHIND_7,
            LEFT_BEHIND_8,
            LEFT_BEHIND_9,
            LEFT_BEHIND_10,
            LEFT_BEHIND_11,
            LEFT_BEHIND_12,
            LEFT_BEHIND_13,
            LEFT_BEHIND_14,
            LEFT_BEHIND_15));
    PHRASES.put(
        ChatTopic.BIOME_FOREST,
        List.of(BIOME_FOREST_0, BIOME_FOREST_1, BIOME_FOREST_2, BIOME_FOREST_3));
    PHRASES.put(
        ChatTopic.BIOME_JUNGLE,
        List.of(BIOME_JUNGLE_0, BIOME_JUNGLE_1, BIOME_JUNGLE_2, BIOME_JUNGLE_3));
    PHRASES.put(
        ChatTopic.BIOME_DESERT,
        List.of(BIOME_DESERT_0, BIOME_DESERT_1, BIOME_DESERT_2, BIOME_DESERT_3));
    PHRASES.put(
        ChatTopic.BIOME_SWAMP, List.of(BIOME_SWAMP_0, BIOME_SWAMP_1, BIOME_SWAMP_2, BIOME_SWAMP_3));
    PHRASES.put(
        ChatTopic.BIOME_OCEAN, List.of(BIOME_OCEAN_0, BIOME_OCEAN_1, BIOME_OCEAN_2, BIOME_OCEAN_3));
    PHRASES.put(
        ChatTopic.BIOME_MOUNTAIN,
        List.of(BIOME_MOUNTAIN_0, BIOME_MOUNTAIN_1, BIOME_MOUNTAIN_2, BIOME_MOUNTAIN_3));
    PHRASES.put(
        ChatTopic.BIOME_PLAINS,
        List.of(BIOME_PLAINS_0, BIOME_PLAINS_1, BIOME_PLAINS_2, BIOME_PLAINS_3));
    PHRASES.put(
        ChatTopic.BIOME_TAIGA, List.of(BIOME_TAIGA_0, BIOME_TAIGA_1, BIOME_TAIGA_2, BIOME_TAIGA_3));
    PHRASES.put(
        ChatTopic.BIOME_BADLANDS,
        List.of(BIOME_BADLANDS_0, BIOME_BADLANDS_1, BIOME_BADLANDS_2, BIOME_BADLANDS_3));
    PHRASES.put(
        ChatTopic.BIOME_MUSHROOM,
        List.of(BIOME_MUSHROOM_0, BIOME_MUSHROOM_1, BIOME_MUSHROOM_2, BIOME_MUSHROOM_3));
    PHRASES.put(
        ChatTopic.BIOME_NETHER,
        List.of(BIOME_NETHER_0, BIOME_NETHER_1, BIOME_NETHER_2, BIOME_NETHER_3));
    PHRASES.put(ChatTopic.BIOME_END, List.of(BIOME_END_0, BIOME_END_1, BIOME_END_2, BIOME_END_3));
    PHRASES.put(
        ChatTopic.ANIMAL_PIG,
        List.of(ANIMAL_PIG_0, ANIMAL_PIG_1, ANIMAL_PIG_2, ANIMAL_PIG_3, ANIMAL_PIG_4));
    PHRASES.put(
        ChatTopic.ANIMAL_COW,
        List.of(ANIMAL_COW_0, ANIMAL_COW_1, ANIMAL_COW_2, ANIMAL_COW_3, ANIMAL_COW_4));
    PHRASES.put(
        ChatTopic.ANIMAL_CHICKEN,
        List.of(ANIMAL_CHICKEN_0, ANIMAL_CHICKEN_1, ANIMAL_CHICKEN_2, ANIMAL_CHICKEN_3));
    PHRASES.put(
        ChatTopic.ANIMAL_SHEEP,
        List.of(ANIMAL_SHEEP_0, ANIMAL_SHEEP_1, ANIMAL_SHEEP_2, ANIMAL_SHEEP_3));
    PHRASES.put(
        ChatTopic.ANIMAL_RABBIT,
        List.of(ANIMAL_RABBIT_0, ANIMAL_RABBIT_1, ANIMAL_RABBIT_2, ANIMAL_RABBIT_3));
    PHRASES.put(
        ChatTopic.ANIMAL_OTHER,
        List.of(ANIMAL_OTHER_0, ANIMAL_OTHER_1, ANIMAL_OTHER_2, ANIMAL_OTHER_3));
    PHRASES.put(ChatTopic.REDSTONE, List.of(REDSTONE_0, REDSTONE_1, REDSTONE_2));
    PHRASES.put(
        ChatTopic.ACHIEVEMENT,
        List.of(
            ACHIEVEMENT_0,
            ACHIEVEMENT_1,
            ACHIEVEMENT_2,
            ACHIEVEMENT_3,
            ACHIEVEMENT_4,
            ACHIEVEMENT_5,
            ACHIEVEMENT_6,
            ACHIEVEMENT_7));
    PHRASES.put(
        ChatTopic.CHALLENGE,
        List.of(CHALLENGE_0, CHALLENGE_1, CHALLENGE_2, CHALLENGE_3, CHALLENGE_4, CHALLENGE_5));
  }

  private ChatPhrases() {}

  static List<String> of(ChatTopic topic) {
    return PHRASES.get(topic);
  }
}
