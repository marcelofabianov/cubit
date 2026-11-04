# Cubit

Mod de Minecraft que adiciona um robô companheiro fofo, movido a bateria, feito para o modpack [Iron Meridian](https://github.com/marcelofabianov/iron-meridian): singleplayer, de tecnologia e construção, sem magia.

**Comece por:** [`robo-companheiro.md`](robo-companheiro.md), a visão geral com as fases e o mapa da documentação em `_docs/`. Os documentos são a fonte da verdade do projeto.

## Estado atual

- **Etapa atual:** Fase 1, conceito. O trabalho é definir persona, features e demais informações em arquivos `.md`. Não crie código nem projeto Gradle nesta etapa.
- **Próximo passo:** a Fase 2 (versão mínima), que só começa quando o desenvolvedor encerrar o conceito e aprovar as [decisões](_docs/decisions.md) e a [estrutura de pacotes](#estrutura-de-pacotes-proposta).
- **Limite:** não avance para as fases seguintes sem pedido explícito.

## Documentação

- **Números:** ficam só em `_docs/tiers.md`. As outras páginas apontam para lá.
- **Decisões:** ficam em `_docs/decisions.md`. Recomendações marcadas como não aprovadas não valem como decididas.
- **Imagens:** ficam em `_docs/assets/`.
- **Rascunhos:** conversas e ideias vão para `tmp/`, que o Git ignora.

## Regras do mod

1. **Só inglês no mod:** nome, itens, legendas, telas, mensagens, IDs, comandos e config. Existe só o `en_us.json`, sem `pt_br.json`.
2. **Sem magia:** nem nos nomes, nem nas mecânicas.
3. **Números em config:** bateria, consumo, raios, intervalos, dano e slots por tier nunca ficam fixos na lógica.
4. **Receitas:** só com itens do jogo puro, em datapack JSON, para o pack poder sobrescrever com KubeJS.
5. **Energia:** FE, pelas capabilities do NeoForge, para aceitar energia de qualquer mod do pack (Mekanism, Powah, Solar Flux).
6. **Luz:** API do LambDynamicLights, como dependência opcional. Nada de blocos de luz invisíveis no mundo.
7. **Scanner de minérios:** baseado nas tags `c:ores`, nunca numa lista fixa de blocos.
8. **Estado do robô como item:** em data components do 1.21.1.
9. **Design próprio:** o modelo final não pode copiar um robô existente, para o mod poder ser publicado.
10. **Nome a evitar:** "Robit", que é o robô companheiro do Mekanism, presente no pack.

## Stack

| Item | Versão |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.x (o pack usa a 21.1.265) |
| Java | 21 |
| Build | MDK oficial do NeoForge para 1.21.1 (ModDevGradle) |
| GeckoLib | 4.9.3 (versão do pack) |
| LambDynamicLights | 4.8.11+1.21.1 (versão do pack, opcional) |

Versões conferidas em 2026-11-04. Confira de novo no `mods/` do Iron Meridian antes de fixar ou atualizar uma dependência. O Gradle não está instalado na máquina: use o wrapper do MDK.

## Como trabalhar

O desenvolvedor é sênior em TypeScript, Go e PHP, e Java é secundário para ele. Valoriza SOLID, DRY, KISS, Clean Code, TDD e DDD.

- **Decisão em aberto:** pergunte em vez de decidir sozinho.
- **Modding:** explique as decisões específicas de Minecraft quando não forem óbvias.
- **Domínio:** regras de tier, bateria, consumo, filtros e decisão de comportamento ficam separadas das classes do Minecraft e são testadas com JUnit, sem subir o jogo.
- **Mundo:** use GameTests do NeoForge para os comportamentos que dependem do mundo, quando fizer sentido.
- **Código:** prefira classes pequenas, nomes claros e composição em vez de herança profunda.
- **Idioma:** converse e documente em português. O que aparece no mod fica em inglês.

## Git

- **Autorização:** nunca faça commit, push, merge, tag, release ou PR sem autorização explícita para aquela ação. Antes, mostre os arquivos, o resumo e a mensagem proposta.
- **Autoria:** nunca atribua autoria ou coautoria a uma IA, nem em commits, PRs, tags, releases ou comentários.
- **Assinatura:** os commits são assinados com SSH. Não desligue a assinatura.
- **Remote:** `git@github.com-personal:marcelofabianov/cubit.git`. Não troque por HTTPS nem por `git@github.com:`, que caem na conta profissional.
- **Conta do `gh`:** esta é a conta pessoal. Se o `gh api user --jq .login` mostrar outra, prefixe o comando com `GH_TOKEN=$(gh auth token --user marcelofabianov)`.

## Estrutura de pacotes proposta

**Aguardando aprovação.** Vale para a Fase 2.

```text
io.github.marcelofabianov.cubit
├── CubitMod                // ponto de entrada: só conecta os registros
├── domain/                 // Java puro, testado com JUnit
│   ├── tier/               // RobotTier, TierSpec, TierCatalog (interface)
│   ├── energy/             // Battery, PowerState, regras de consumo e religamento
│   ├── behavior/           // FollowDecision: seguir, parar ou teleportar
│   └── ownership/          // ActiveRobotPolicy
├── config/                 // ModConfigSpec → implementa TierCatalog
├── registry/               // DeferredRegisters
├── entity/                 // RobotEntity, goals de IA
├── item/                   // RobotItem, RobotStateComponent
├── block/                  // ChargingStationBlock, BlockEntity
├── menu/                   // RobotMenu
├── player/                 // attachment com o UUID do robô ativo
├── client/                 // renderer, modelo GeckoLib, tela
└── datagen/                // receitas, loot, traduções
```

Os testes ficam em `src/test` (JUnit) e os GameTests em um pacote `gametest`.

## Notas de modding

- **Limite do domínio:** um teste com ArchUnit deve falhar se `domain` importar `net.minecraft` ou `net.neoforged`. É mais simples que um subprojeto Gradle.
- **Cliente e comum:** `Menu` roda nos dois lados, e `Screen` só no cliente. Código comum não pode carregar classes de `client`, senão um servidor dedicado quebra.
- **Rede:** o menu e o `SynchedEntityData` bastam na versão mínima. Pacotes próprios só entram com o controle remoto.
- **Config `COMMON`:** fica em `config/`, onde o packwiz consegue distribuí-lo. O `SERVER` ficaria dentro de cada mundo.
- **Atributos:** são registrados antes de o config carregar, então vida e velocidade são aplicadas quando a entidade nasce.
- **Receitas com datagen:** geram JSONs normais em `data/`, que o KubeJS pode sobrescrever.
- **Distribuição:** o pack usa packwiz e não guarda `.jar`, então o mod precisa ser publicado no Modrinth ou no CurseForge para entrar nele.
