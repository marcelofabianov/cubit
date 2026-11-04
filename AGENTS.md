# Cubit

Mod de Minecraft que adiciona um robô companheiro fofo, movido a bateria, feito para o modpack [Iron Meridian](https://github.com/marcelofabianov/iron-meridian): singleplayer, de tecnologia e construção, sem magia.

**Comece por:** [`robo-companheiro.md`](robo-companheiro.md), a visão geral com as fases e o mapa da documentação em `_docs/`. As respostas em `_docs/perguntas/` são a fonte da verdade e prevalecem sobre os outros documentos.

## Estado atual

- **Etapa atual:** Fase 1, conceito. O trabalho é definir persona, features e demais informações em arquivos `.md`. Não crie código nem projeto Gradle nesta etapa.
- **Próximo passo:** a Fase 2 (versão mínima), que só começa quando o desenvolvedor encerrar o conceito e não houver [perguntas](_docs/perguntas/README.md) pendentes para ela.
- **Limite:** não avance para as fases seguintes sem pedido explícito.

## Documentação

- **Números:** ficam só em `_docs/tiers.md`. As outras páginas apontam para lá.
- **Perguntas:** ficam em `_docs/perguntas/`, com um arquivo por fase. Recomendação de pergunta pendente não vale como decidida.
- **Decisões:** o resumo do que foi decidido fica em `_docs/decisoes.md`.
- **Histórico:** todo `.md` termina com uma tabela de histórico de modificações, e quem altera o arquivo adiciona uma linha no topo dela.
- **Imagens:** ficam em `_docs/imagens/`.
- **Rascunhos:** conversas e ideias vão para `tmp/`, que o Git ignora.
- **Fluxo de trabalho:** o passo a passo de uma tarefa, de uma versão e de um hotfix está em [`_docs/desenvolvimento/fluxo-de-trabalho.md`](_docs/desenvolvimento/fluxo-de-trabalho.md).

## Regras do mod

1. **Idioma:** o que entra no jar, aparece no jogo ou acompanha o download do mod fica em inglês: nome, itens, legendas, telas, mensagens, IDs, comandos, config e licença. Existe só o `en_us.json`, sem `pt_br.json`.
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

- **Modding:** explique as decisões específicas de Minecraft quando não forem óbvias.
- **Mudança neste arquivo:** justifique por que é necessária e espere a autorização do desenvolvedor.
- **Encerramento:** sessão que termina com trabalho pendente deixa um prompt de continuidade em `tmp/prompt/{AAAA-MM-DD_HHhMM}_continuidade_{assunto}.md`, com a leitura obrigatória, o que foi feito e o que se espera da próxima sessão.

### Decisões bloqueantes

É bloqueante qualquer ponto com mais de um caminho defensável em que a escolha altere arquitetura, dependências, processo, contrato, saves ou o comportamento entregue.

1. Identifique a decisão e pare antes de implementá-la.
2. Apresente as alternativas, com a sua recomendação e a opção de recusar todas.
3. Espere a confirmação explícita.
4. Implemente e registre a resposta em `_docs/perguntas/`, no arquivo da fase, e o resumo em `_docs/decisoes.md`.

Autorização para começar um trabalho não resolve os bloqueios dele, mesmo que a decisão pareça pequena ou reversível.

## Regras de desenvolvimento

### Prioridade

Quando duas regras puxarem para lados opostos, vence a que vem antes:

```text
Mundo do player > Fronteira do domínio > Contrato com pack e saves > Comportamento testado >
Design > Clareza > Simplicidade > Sem repetição
```

### Domínio

11. **Conceito antes do código:** uma feature só é implementada depois de documentada em `_docs/funcionalidades/` e com as decisões dela fechadas.
12. **Fronteira:** `domain/` não importa `net.minecraft` nem `net.neoforged`. Um teste com ArchUnit reprova a violação no build. Regras de tier, bateria, consumo, filtros e decisão de comportamento ficam nessa camada.
13. **Energia é sempre inteira**, num value object. Transferir nunca cria nem perde FE, seja na bateria, na Power Bank ou na estação.
14. **Value objects** são `record` que validam no construtor. Sem `get*`: `capacity()`, não `getCapacity()`.
15. **Interfaces:** só existem com um consumidor real e são curtas. Se um consumidor não usa todos os métodos, separe.
16. **README do domínio:** `domain/` tem um `README.md` com a linguagem do domínio, as regras e os porquês. Atualizá-lo faz parte da tarefa que muda o modelo.

### Minecraft

17. **O mod não derruba o jogo por dado inválido:** usa um valor seguro e registra no log. Só falha ao iniciar, em caso de erro de programação.
18. **Saves são contrato:** IDs, data components, NBT, chaves de config e tags não mudam sem migração.
19. **Autoridade do servidor:** toda regra roda no servidor. O cliente só exibe e pede ações.
20. **Dono do robô:** toda ação confere o dono. O servidor valida dono e distância de todo pacote recebido do cliente.
21. **Orçamento de tick:** nada pesado a cada tick. Varreduras rodam em intervalos e sem alocação desnecessária.
22. **Chunks:** nunca carregue um chunk à força nem leia um bloco sem confirmar que ele está carregado.
23. **Separação do cliente:** código comum nunca referencia classes de `client/`.
24. **Dependências opcionais:** o mod precisa abrir sem elas. LambDynamicLights e Jade ficam isolados.

### Flexibilidade para novos robôs

O robô atual é o primeiro, de uso geral. O mod pode ter robôs especializados no futuro, então nada pode prender o projeto a um único tipo de robô.

39. **Tipo de robô é dado:** regras, números e capacidades vêm do tipo e do tier do robô, nunca de um `if` ou de uma constante do robô geral.
40. **Capacidades por composição:** ímã, choque, scanner, inventário e demais recursos são peças que um tipo de robô combina, sem herança entre robôs.
41. **Config, IDs e dados por tipo:** chaves de config, IDs e data components já nascem com o tipo do robô, porque mudá-los depois exige migração (regra 18).
42. **Itens compartilhados:** Induction Dock, Cubit Remote e Power Bank reconhecem os robôs por tag ou interface, nunca por uma classe concreta.
43. **Sem antecipar robôs:** essa flexibilidade vale nas fronteiras acima. Nada de implementar tipos, telas ou capacidades de robôs que ainda não existem (regra 15).

### Forma do código

25. **Idioma:** arquivos de programação (como `.java` e `.sh`) ficam em inglês, incluindo comentários, logs e testes. Arquivos de conteúdo (como `.md`) ficam em português, inclusive o nome do arquivo e da pasta, assim como issues, PRs e mensagens de commit. `README.md`, `AGENTS.md`, `CLAUDE.md` e `LICENSE` mantêm o nome, porque as ferramentas dependem dele.
26. **Sem número mágico:** balanceamento vai no config, e constantes técnicas ficam nomeadas.
27. **Funções** têm no máximo 30 linhas e uma responsabilidade. Prefira classes pequenas e composição em vez de herança profunda.
28. **Nomes:** classes em `PascalCase`; JSONs e IDs em `snake_case`, no singular; scripts `.sh` em `kebab-case`. Adapters levam o que adaptam como prefixo (`NeoForgeTierCatalog`).
29. **Comentários** são exceção e explicam o porquê. Sem código comentado e sem `TODO` sem issue.
30. **Gerados:** `src/generated/` nunca é editado à mão. Um gate reprova se o `runData` gerar diferença.
31. **Versões** ficam em `gradle.properties`, alinhadas às do Iron Meridian.

### Testes

32. **TDD:** o teste vem antes ou junto do código. Use JUnit 5 no domínio e GameTests no que depende do mundo.
33. **Sem mock do jogo:** nunca faça mock de `Level` ou `Entity`. Comportamento de mundo se testa no mundo.
34. **Cobertura:** mínimo de 80% em `domain/`, medido com JaCoCo. O restante se cobre com GameTests e um roteiro manual.
35. **Teste no pack:** antes de ir para a `main`, teste numa instância do Iron Meridian.

### Gates

36. **Commit:** branch, histórico de modificações, mensagem e gitleaks já estão ativos. A formatação entra na Fase 2. Veja [`_docs/desenvolvimento/hooks-do-git.md`](_docs/desenvolvimento/hooks-do-git.md).
37. **Push (a partir da Fase 2):** build, testes, ArchUnit, datagen, cobertura e `runServer`, para detectar classe de cliente carregada no servidor.
38. **Ferramentas:** toda ferramenta ou hook entra com um documento em `_docs/desenvolvimento/`: o que faz, por que foi escolhido, a versão e o que fazer quando reprova.

## Git

- **Autorização:** nunca faça commit, push, merge, tag, release ou PR sem autorização explícita para aquela ação. Antes, mostre os arquivos, o resumo e a mensagem proposta.
- **Autoria:** nunca atribua autoria ou coautoria a uma IA, nem em commits, PRs, tags, releases ou comentários.
- **Assinatura:** os commits são assinados com SSH. Não desligue a assinatura.
- **Remote:** `git@github.com-personal:marcelofabianov/cubit.git`. Não troque por HTTPS nem por `git@github.com:`, que caem na conta profissional.
- **Conta do `gh`:** esta é a conta pessoal. Se o `gh api user --jq .login` mostrar outra, prefixe o comando com `GH_TOKEN=$(gh auth token --user marcelofabianov)`.

### Fluxo de branches

O projeto segue o Gitflow, com Semantic Versioning e Conventional Commits.

| Branch | Sai de | Volta para | Papel |
|---|---|---|---|
| `main` | — | — | Versões publicadas; cada merge vira uma tag |
| `release/X.Y.Z` | `homolog` | `main` e `develop` | Release fechada |
| `homolog` | `develop` | `release/X.Y.Z` | Release em teste |
| `develop` | `main` | `homolog` | Integração do trabalho em andamento |
| `feature/{issue}-{slug}` | `develop` | `develop` | Funcionalidade, melhoria, correção não urgente ou tarefa técnica |
| `hotfix/{issue}-{slug}` | `main` | `main`, `homolog` e `develop` | Correção urgente do que já foi publicado |

### Regras de Git

- **Proibido:** commit direto em `main`, `homolog` ou `develop`; usar `--no-verify`; misturar várias mudanças lógicas num commit.
- **Issue:** toda tarefa começa por uma issue, e a branch leva o número dela. O nome da branch fica em inglês e em `kebab-case`: `feature/12-power-bank`.
- **PR:** todo merge é feito por PR, com `Closes #n` na descrição. A branch padrão no GitHub é a `develop`.
- **Commits:** em Conventional Commits, em português, e atômicos: `feat: adiciona a Power Bank`.
- **Versão:** Semantic Versioning, com tag anotada `vX.Y.Z` na `main`. Na fase `0.x`, mudança incompatível sobe o MINOR, e o resto sobe o PATCH.
- **Backlog:** um milestone do GitHub por fase do projeto.

## Estrutura de pacotes

Aprovada. Vale a partir da Fase 2.

```text
io.github.marcelofabianov.cubit
├── CubitMod                // ponto de entrada: só conecta os registros
├── domain/                 // Java puro, testado com JUnit
│   ├── robot/              // RobotType, capacidades que cada tipo combina
│   ├── tier/               // RobotTier, TierSpec, TierCatalog (interface)
│   ├── energy/             // Battery, PowerState (ativo, inativo, hibernação), regras de consumo e religamento
│   ├── behavior/           // FollowDecision: seguir, parar ou teleportar
│   └── ownership/          // FollowingRobotPolicy
├── config/                 // ModConfigSpec → implementa TierCatalog
├── registry/               // DeferredRegisters
├── entity/                 // RobotEntity, goals de IA
├── item/                   // RobotItem, PowerBankItem, RobotStateComponent
├── block/                  // InductionDockBlock, BlockEntity
├── menu/                   // RobotMenu, InductionDockMenu
├── player/                 // attachment com o UUID do robô que segue
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

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 17:16 | Adiciona as regras de flexibilidade para novos robôs. |
| Marcelo Fabiano | 2026-11-04 17:15 | Define a regra de idioma e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as respostas como fonte da verdade e atualiza os pacotes e a regra do histórico. |
| Marcelo Fabiano | 2026-11-04 16:57 | Aponta para o diretório de perguntas, aprova a estrutura de pacotes e renomeia o bloco do Dock. |
| Marcelo Fabiano | 2026-11-04 16:08 | Aponta o AGENTS para o fluxo de trabalho. |
| Marcelo Fabiano | 2026-11-04 15:58 | Adota as regras de desenvolvimento e o fluxo de branches. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona a visão geral, o README e as regras do projeto. |
