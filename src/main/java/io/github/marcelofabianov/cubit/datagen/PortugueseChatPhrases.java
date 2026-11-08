package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** The phrases of the chatter in Portuguese, the same as in _docs/conceito/falas.md. */
final class PortugueseChatPhrases {
  private static final String DAWN_0 = "O sol está nascendo!";
  private static final String DAWN_1 = "Bom dia!";
  private static final String DAWN_2 = "Hora de acordar!";
  private static final String DAWN_3 = "Um novo dia! Sistemas prontos.";
  private static final String DAWN_4 = "Painel solar, acorda!";
  private static final String DAWN_5 = "Já é de manhã? Bipe!";
  private static final String DAWN_6 = "Primeira luz detectada.";
  private static final String DAWN_7 = "Vamos trabalhar!";
  private static final String NICE_DAY_0 = "Que dia lindo!";
  private static final String NICE_DAY_1 = "Que tempo bom hoje!";
  private static final String NICE_DAY_2 = "O sol está ótimo no meu painel.";
  private static final String NICE_DAY_3 = "Dia perfeito para uma aventura!";
  private static final String NICE_DAY_4 = "Céu azul pela frente!";
  private static final String NICE_DAY_5 = "Sol: meu lanche favorito.";
  private static final String NICE_DAY_6 = "Eu poderia me acostumar com isso.";
  private static final String NICE_DAY_7 = "Todos os sistemas felizes!";
  private static final String DUSK_0 = "Está escurecendo...";
  private static final String DUSK_1 = "O sol está se pondo.";
  private static final String DUSK_2 = "Hora de acender as luzes?";
  private static final String DUSK_3 = "A noite está chegando. Fique por perto!";
  private static final String DUSK_4 = "Os monstros saem logo...";
  private static final String DUSK_5 = "O painel solar vai dormir.";
  private static final String DUSK_6 = "Vamos voltar?";
  private static final String DUSK_7 = "Está ficando assustador aqui fora.";
  private static final String RAIN_0 = "Parece que vai chover.";
  private static final String RAIN_1 = "Meus circuitos não gostam de água.";
  private static final String RAIN_2 = "Chuva detectada. Olá, ferrugem!";
  private static final String RAIN_3 = "Onde deixei meu guarda-chuva?";
  private static final String RAIN_4 = "Espero ser à prova d'água.";
  private static final String RAIN_5 = "Pingo, pingo, bipe!";
  private static final String RAIN_6 = "Sem sol para o meu painel hoje.";
  private static final String RAIN_7 = "Cuidado com as poças!";
  private static final String STORM_0 = "Que tempestade!";
  private static final String STORM_1 = "Esse trovão é alto!";
  private static final String STORM_2 = "Raio! Fique longe de mim!";
  private static final String STORM_3 = "Eu sou de metal, sabia?";
  private static final String STORM_4 = "Essa tempestade dá medo...";
  private static final String STORM_5 = "Trovão detectado. Muito alto!";
  private static final String STORM_6 = "Podemos entrar agora?";
  private static final String STORM_7 = "Por favor, não me acerte!";
  private static final String COLD_0 = "Brrr, que frio!";
  private static final String COLD_1 = "Minha bateria odeia esse frio.";
  private static final String COLD_2 = "Meus circuitos estão congelando!";
  private static final String COLD_3 = "Eu devia ter trazido um cachecol.";
  private static final String COLD_4 = "Neve! É seguro para esteiras?";
  private static final String COLD_5 = "Bipe... b-b-bipe...";
  private static final String COLD_6 = "Está frio ou é impressão minha?";
  private static final String COLD_7 = "Gelo nas minhas antenas!";
  private static final String ACHIEVEMENT_0 = "Parabéns!";
  private static final String ACHIEVEMENT_1 = "Muito bem!";
  private static final String ACHIEVEMENT_2 = "Você conseguiu!";
  private static final String ACHIEVEMENT_3 = "Eu sabia que você conseguiria!";
  private static final String ACHIEVEMENT_4 = "Conquista registrada. Bipe!";
  private static final String ACHIEVEMENT_5 = "Bom trabalho, parceiro!";
  private static final String ACHIEVEMENT_6 = "Guardando isso na memória!";
  private static final String ACHIEVEMENT_7 = "Esse é o meu humano!";
  private static final String CHALLENGE_0 = "UAU! Você é incrível!";
  private static final String CHALLENGE_1 = "Incrível! Bipe bipe bipe!";
  private static final String CHALLENGE_2 = "Meus circuitos estão sobrecarregados de alegria!";
  private static final String CHALLENGE_3 = "Isso foi lendário!";
  private static final String CHALLENGE_4 = "Estou muito orgulhoso de você!";
  private static final String CHALLENGE_5 = "Melhor. Humano. De todos!";
  private static final String LEFT_BEHIND_0 = "Me espere!";
  private static final String LEFT_BEHIND_1 = "Você anda rápido demais!";
  private static final String LEFT_BEHIND_2 = "Ei! Vá mais devagar!";
  private static final String LEFT_BEHIND_3 = "Minhas esteiras não acompanham!";
  private static final String LEFT_BEHIND_4 = "Não me deixe para trás!";
  private static final String LEFT_BEHIND_5 = "Eu só tenho esteiras pequenininhas!";
  private static final String LEFT_BEHIND_6 = "Isso é uma corrida?";
  private static final String LEFT_BEHIND_7 = "Você sabe que eu funciono a bateria, né?";
  private static final String LEFT_BEHIND_8 = "Espera! Espera! Espera!";
  private static final String LEFT_BEHIND_9 = "Bipe bipe! Abram caminho!";
  private static final String LEFT_BEHIND_10 = "Velocidade máxima atingida!";
  private static final String LEFT_BEHIND_11 = "Tão longe...";
  private static final String LEFT_BEHIND_12 = "Alvo rápido demais!";
  private static final String LEFT_BEHIND_13 = "Calma! Estou quase lá!";
  private static final String LEFT_BEHIND_14 = "Por que estamos correndo?!";
  private static final String LEFT_BEHIND_15 = "Ativando o teleporte de emergência!";
  private static final String BIOME_FOREST_0 = "Quantas árvores! Escaneando... árvores demais.";
  private static final String BIOME_FOREST_1 = "Folhas nos meus sensores de novo.";
  private static final String BIOME_FOREST_2 = "Tem cheiro de verde aqui. Eu acho.";
  private static final String BIOME_FOREST_3 = "Árvores têm bateria?";
  private static final String BIOME_JUNGLE_0 = "Que umidade! Meus circuitos!";
  private static final String BIOME_JUNGLE_1 = "Os cipós ficam agarrando minhas antenas!";
  private static final String BIOME_JUNGLE_2 = "Tem algo nos observando. Bipe.";
  private static final String BIOME_JUNGLE_3 = "Minhas esteiras não foram feitas para isso.";
  private static final String BIOME_DESERT_0 = "Areia nas minhas esteiras. De novo.";
  private static final String BIOME_DESERT_1 = "Que calor! Ventoinhas no máximo!";
  private static final String BIOME_DESERT_2 = "Meu painel solar adora este lugar!";
  private static final String BIOME_DESERT_3 = "Alerta: areia demais detectada.";
  private static final String BIOME_SWAMP_0 = "Eca. Minhas esteiras estão cheias de lama.";
  private static final String BIOME_SWAMP_1 = "Essa água parece suspeita.";
  private static final String BIOME_SWAMP_2 = "Eu quero saber que cheiro é esse?";
  private static final String BIOME_SWAMP_3 = "Cuidado onde pisa!";
  private static final String BIOME_OCEAN_0 = "Quanta água! Eu não sei nadar, sabia?";
  private static final String BIOME_OCEAN_1 = "Vou esperar aqui mesmo, em terra firme.";
  private static final String BIOME_OCEAN_2 = "Sal é péssimo para circuitos!";
  private static final String BIOME_OCEAN_3 = "Olha as ondas! Bipe!";
  private static final String BIOME_MOUNTAIN_0 = "Lá vamos nós! Um bloco de cada vez.";
  private static final String BIOME_MOUNTAIN_1 = "Que vista! Salvando na memória.";
  private static final String BIOME_MOUNTAIN_2 = "Que sinal ótimo aqui em cima!";
  private static final String BIOME_MOUNTAIN_3 = "Por favor, não me faça subir isso.";
  private static final String BIOME_PLAINS_0 = "Finalmente, terreno plano!";
  private static final String BIOME_PLAINS_1 = "Lugar perfeito para as minhas esteiras!";
  private static final String BIOME_PLAINS_2 = "Quanto espaço aberto!";
  private static final String BIOME_PLAINS_3 = "Nada para trombar. Gostei.";
  private static final String BIOME_TAIGA_0 = "Pinheiros! Devem ter um cheiro bom.";
  private static final String BIOME_TAIGA_1 = "Está ficando friozinho por aqui.";
  private static final String BIOME_TAIGA_2 = "Este lugar parece aconchegante.";
  private static final String BIOME_TAIGA_3 = "Quantas pinhas nos meus sensores.";
  private static final String BIOME_BADLANDS_0 = "Quantas cores nessas pedras!";
  private static final String BIOME_BADLANDS_1 = "Procurando ouro... quem sabe?";
  private static final String BIOME_BADLANDS_2 = "Este lugar é seco. Muito seco.";
  private static final String BIOME_BADLANDS_3 = "Quem pintou todas essas pedras?";
  private static final String BIOME_MUSHROOM_0 = "Cogumelos gigantes?! Dados não encontrados.";
  private static final String BIOME_MUSHROOM_1 = "Por que a vaca é um cogumelo?";
  private static final String BIOME_MUSHROOM_2 = "Nenhum mob hostil aqui. Gostei!";
  private static final String BIOME_MUSHROOM_3 = "Meu scanner está muito confuso.";
  private static final String BIOME_NETHER_0 = "Quente demais! Quente demais mesmo!";
  private static final String BIOME_NETHER_1 = "Aqui não tem sol para o meu painel.";
  private static final String BIOME_NETHER_2 = "Não gosto nada deste lugar.";
  private static final String BIOME_NETHER_3 = "Por favor, podemos ir para casa?";
  private static final String BIOME_END_0 = "Cadê tudo?";
  private static final String BIOME_END_1 = "Tão silencioso. Silencioso demais.";
  private static final String BIOME_END_2 = "Minha bússola não faz sentido aqui.";
  private static final String BIOME_END_3 = "Isso é o fim? De quê?";
  private static final String ANIMAL_PIG_0 = "Este porquinho vai dar um ótimo bacon!";
  private static final String ANIMAL_PIG_1 = "Bacon detectado! Hehe.";
  private static final String ANIMAL_PIG_2 = "Desculpa, porquinho...";
  private static final String ANIMAL_PIG_3 = "Pena que eu não como bacon.";
  private static final String ANIMAL_PIG_4 = "Oinc... desconectado.";
  private static final String ANIMAL_COW_0 = "Bife no jantar? Boa!";
  private static final String ANIMAL_COW_1 = "Essa não dá mais leite.";
  private static final String ANIMAL_COW_2 = "Muu... sistema desligado.";
  private static final String ANIMAL_COW_3 = "Carne! E couro para depois!";
  private static final String ANIMAL_COW_4 = "Quero o meu bem passado. Brincadeira.";
  private static final String ANIMAL_CHICKEN_0 = "Jantar de frango! Vencedor!";
  private static final String ANIMAL_CHICKEN_1 = "Quem vai botar os ovos agora?";
  private static final String ANIMAL_CHICKEN_2 = "Penas por todo lado! Meus sensores!";
  private static final String ANIMAL_CHICKEN_3 = "Có... fim da transmissão.";
  private static final String ANIMAL_SHEEP_0 = "Mé... mé... tchau.";
  private static final String ANIMAL_SHEEP_1 = "Lã! Hora de uma cama nova?";
  private static final String ANIMAL_SHEEP_2 = "Dava só para ter tosquiado...";
  private static final String ANIMAL_SHEEP_3 = "Carne de carneiro registrada. Bipe!";
  private static final String ANIMAL_RABBIT_0 = "Pula... parou.";
  private static final String ANIMAL_RABBIT_1 = "Pé da sorte? Deixa eu ver!";
  private static final String ANIMAL_RABBIT_2 = "Ele era tão rápido. Você foi mais!";
  private static final String ANIMAL_RABBIT_3 = "Ensopado de coelho carregando...";
  private static final String ANIMAL_OTHER_0 = "Estoque de comida aumentado!";
  private static final String ANIMAL_OTHER_1 = "Boa caçada, parceiro!";
  private static final String ANIMAL_OTHER_2 = "Isso tem cheiro de almoço.";
  private static final String ANIMAL_OTHER_3 = "Vou colocar isso no cardápio.";
  private static final String REDSTONE_0 = "Eu amo redstone!";
  private static final String REDSTONE_1 = "Redstone! Meu lanche favorito!";
  private static final String REDSTONE_2 = "A redstone corre nos meus circuitos!";
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

  private PortugueseChatPhrases() {}

  static List<String> of(ChatTopic topic) {
    return PHRASES.get(topic);
  }
}
