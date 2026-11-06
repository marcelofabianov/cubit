# Cubit

Mod de Minecraft que adiciona um robô companheiro fofo, movido a bateria, de tecnologia e construção. É feito para o player e funciona numa instância só com ele e suas dependências. O modpack [Iron Meridian](https://github.com/marcelofabianov/iron-meridian) é um dos lugares onde ele é testado.

**Comece por:** [`README.md`](README.md), a visão geral com as fases, e [`_docs/README.md`](_docs/README.md), o mapa da documentação. As respostas em `_docs/perguntas/` são a fonte da verdade e prevalecem sobre os outros documentos.

## Estado atual

- **Etapa atual:** Fase 3, sentidos, em andamento. A Fase 2 foi encerrada. As funcionalidades entram por issue, na ordem do `checklist.md`.
- **Já entrou:** colocar e recolher, seguir o player, vários robôs, fogo, lava, os arquivos de som, a revisão dos `.md`, o novo modelo, a morte com o item danificado, o inventário, a energia em uso, o consumo por situação, a reação a blocos que ferem, o Induction Dock, o `/cubit reset`, as receitas e, na Fase 3, os sons, as expressões, o som de dano, a barra de vida, as animações, o colar e a bateria e os textos curtos.
- **Próximos:** lanterna, painel solar, e os demais itens do `checklist.md`. Os testes de instância e a primeira versão, `0.1.0`, ficam para o fim da Fase 3.
- **Comandos:** estão em [`_docs/desenvolvimento/ferramentas-de-build.md`](_docs/desenvolvimento/ferramentas-de-build.md).
- **Limite:** não avance para as fases seguintes sem pedido explícito.

## Documentação

- **Números:** ficam em `_docs/tiers.md`. As outras páginas apontam para lá. O `README.md` traz só um resumo, e o roteiro manual cita os valores padrão como resultado esperado.
- **Perguntas:** ficam em `_docs/perguntas/`, com um arquivo por fase. Recomendação de pergunta pendente não vale como decidida.
- **Decisões:** o resumo do que foi decidido fica em `_docs/decisoes.md`.
- **Histórico:** todo `.md` em `_docs/` termina com uma tabela de histórico de modificações, e quem altera o arquivo adiciona uma linha no topo dela. Fora de `_docs/`, os `.md` não têm essa tabela.
- **Imagens:** ficam em `_docs/imagens/`.
- **Rascunhos:** conversas e ideias vão para `tmp/`, que o Git ignora. Rascunhos de PR ficam em `tmp/pr/`, arquivos de issue, em `tmp/issue/`, e logs, em `tmp/log/`.
- **Fluxo de trabalho:** o passo a passo de uma tarefa, de uma versão e de um hotfix está em [`_docs/desenvolvimento/fluxo-de-trabalho.md`](_docs/desenvolvimento/fluxo-de-trabalho.md).

## Regras do mod

1. **Idioma:** o que entra no jar, aparece no jogo ou acompanha o download do mod fica em inglês: nome, itens, legendas, telas, mensagens, IDs, comandos, config e licença. Existe só o `en_us.json`, sem `pt_br.json`.
2. **Sem magia:** nem nos nomes, nem nas mecânicas.
3. **Números em config:** bateria, consumo, raios, intervalos, dano e slots por tier nunca ficam fixos na lógica.
4. **Receitas:** os materiais são só do jogo puro, e uma receita pode combinar itens do próprio mod, como no conserto. Ficam em datapack JSON, para o pack poder sobrescrever com KubeJS.
5. **Energia:** FE, pelas capabilities do NeoForge, para aceitar energia de outros mods, quando houver. Tudo também aceita redstone, para funcionar sem outros mods.
6. **Luz:** API do LambDynamicLights, dependência obrigatória. Nada de blocos de luz invisíveis no mundo.
7. **Scanner de minérios:** baseado nas tags `c:ores`, nunca numa lista fixa de blocos.
8. **Estado do robô como item:** em data components do 1.21.1.
9. **Design próprio:** o modelo final não pode copiar um robô existente, para o mod poder ser publicado.
10. **Nome a evitar:** "Robit", que é o robô companheiro do Mekanism.

## Stack

| Item | Versão |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.x (o pack usa a 21.1.265) |
| Java | 21 |
| Build | MDK oficial do NeoForge para 1.21.1 (ModDevGradle) |
| GeckoLib | 4.9.3 (versão do pack) |
| LambDynamicLights | 4.8.11+1.21.1 (versão do pack, obrigatória, no cliente) |

Versões conferidas em 2026-11-04. Confira de novo no `mods/` do Iron Meridian antes de fixar ou atualizar uma dependência. O Gradle não está instalado na máquina: use o wrapper. O JDK 21 é baixado pelo próprio projeto.

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
Segurança > Defesa > Fronteira de módulo > Contrato externo > Comportamento testado >
Design sólido > Clareza > Simplicidade > Sem repetição > Sem excesso > Domínio > Regra do Escoteiro
```

O significado de cada pilar, os checklists e a matriz de decisão estão em [`_docs/desenvolvimento/pilares.md`](_docs/desenvolvimento/pilares.md). Segurança, aqui, é a proteção do mundo do player e da máquina dele.

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
22. **Chunks:** nunca carregue um chunk à força nem leia um bloco sem confirmar que ele está carregado. A exceção é o Servidor e as antenas da Fase 5, por ação do player, com limites no config.
23. **Separação do cliente:** código comum nunca referencia classes de `client/`.
24. **Dependências:** GeckoLib e LambDynamicLights são obrigatórias, e o mod precisa abrir sem Jade, que fica isolado. Dependência só de cliente não pode impedir um servidor dedicado de abrir.

### Forma do código

25. **Idioma:** arquivos de programação (como `.java` e `.sh`) ficam em inglês, incluindo comentários, logs e testes. Arquivos de conteúdo (como `.md`) ficam em português, inclusive o nome do arquivo e da pasta, assim como issues, PRs e mensagens de commit. `README.md`, `AGENTS.md`, `CLAUDE.md`, `LICENSE` e `pull_request_template.md` mantêm o nome, porque as ferramentas dependem dele.
26. **Constantes em vez de literais:** nada de número mágico nem string literal solta, inclusive nos testes. Balanceamento vai no config, e o resto vira constante nomeada. O Checkstyle reprova a violação.
27. **Funções** têm no máximo 30 linhas e uma responsabilidade. Prefira classes pequenas e composição em vez de herança profunda.
28. **Nomes:** classes em `PascalCase`; JSONs e IDs em `snake_case`, no singular; scripts `.sh` e arquivos `.md` em `kebab-case`. Adapters levam o que adaptam como prefixo (`NeoForgeTierCatalog`).
29. **Comentários** são exceção e explicam o porquê. Sem código comentado e sem `TODO` sem issue.
30. **Gerados:** `src/generated/` nunca é editado à mão. Um gate reprova se o `runData` gerar diferença.
31. **Versões** ficam em `gradle.properties`, compatíveis com as do Iron Meridian.

### Testes

32. **TDD:** o teste vem antes ou junto do código. Use JUnit 5 no domínio e GameTests no que depende do mundo.
33. **Sem mock do jogo:** nunca faça mock de `Level` ou `Entity`. Comportamento de mundo se testa no mundo.
34. **Cobertura:** mínimo de 85% em `domain/`, medido com JaCoCo. O restante se cobre com GameTests e um roteiro manual.
35. **Teste antes da `main`:** teste numa instância só com o mod e suas dependências e, depois, numa do Iron Meridian.

### Gates

36. **Commit:** branch, histórico de modificações, formatação, mensagem e gitleaks já estão ativos. Veja [`_docs/desenvolvimento/hooks-do-git.md`](_docs/desenvolvimento/hooks-do-git.md).
37. **Push:** build, testes, ArchUnit, cobertura, formatação, Checkstyle, GameTests e datagen já estão ativos. As GameTests carregam o mod como servidor dedicado, o que também detecta classe de cliente carregada no servidor.
38. **Ferramentas:** toda ferramenta ou hook entra com um documento em `_docs/desenvolvimento/`: o que faz, por que foi escolhido, a versão e o que fazer quando reprova.

### Robô único

O Cubit é o único robô do mod. O que muda entre um Cubit e outro é o tier e o card de Aprendizado de Máquina, que dá a ele um trabalho. `general_robot` e `RobotType` continuam nos IDs, no config e no código.

39. **Números por dado:** regras, números e capacidades vêm do tier e do trabalho, nunca de um `if` ou de uma constante fixa.
40. **Capacidades por composição:** ímã, choque, scanner, inventário e os trabalhos são peças que o Cubit combina, sem herança.
41. **Config, IDs e dados:** chaves de config, IDs e data components seguem o padrão atual, com `general_robot`, porque mudá-los depois exige migração (regra 18).
42. **Itens compartilhados:** Induction Dock, Cubit Remote e Power Bank reconhecem o robô por tag ou interface, nunca por uma classe concreta.
43. **Sem antecipar trabalhos:** nada de implementar trabalhos, telas ou capacidades que ainda não entraram (regra 15).

### Validação

44. **Os documentos validam a entrega:** toda entrega é conferida contra os documentos `.md`, e as respostas em `_docs/perguntas/` prevalecem sobre os demais. Artefatos, como testes, logs, relatórios e capturas de tela, são bem-vindos na validação, mas não substituem os documentos.
45. **Validação no PR:** a descrição do PR informa os documentos conferidos e a evidência de cada um, incluindo os checklists dos pilares que se aplicam.

### Comandos do projeto

46. **Makefile:** os comandos do projeto ficam no `Makefile`. Receita com mais de 3 linhas vira um script `.sh` em `scripts/`, e o `Makefile` só o chama.

### Perguntas

47. **Toda pergunta respondida fica registrada:** qualquer pergunta feita ao desenvolvedor e respondida por ele entra em `_docs/perguntas/`, no arquivo da fase e com o próximo número livre, antes de seguir para o próximo passo. Vale também para pergunta não bloqueante e para resposta que só confirma a recomendação. Resposta que muda outra aponta para ela. A exceção é a autorização de uma ação pontual, como commit, push ou apagar uma branch, que vale só para aquela vez.

### Mudança no que está documentado

48. **O documentado pode mudar:** se um impedimento técnico aparecer ao avançar, a decisão pode ser alterada ou desfeita. O impedimento é mostrado com evidência, o desenvolvedor decide, e a nova resposta aponta para a antiga. O que já foi publicado continua seguindo a regra 18.

### Independência

49. **Independência:** o mod prioriza funcionar numa instância de Minecraft só com ele e suas dependências. Nenhum recurso pode exigir outro mod ou modpack, e todo cenário é pensado primeiro assim. As exceções são GeckoLib e LambDynamicLights, obrigatórias, e Jade, opcional.

### Início de feature

50. **Alinhamento:** ao iniciar uma feature, o agente descreve em poucas linhas o que vamos fazer e pergunta ao desenvolvedor se tem algo mais para acrescentar, antes da rodada de decisões, da issue e da branch.

## Git

- **Autorização:** nunca faça commit, push, tag, release ou PR sem autorização explícita para aquela ação. Antes, mostre os arquivos, o resumo e a mensagem proposta.
- **Merge:** somente o desenvolvedor faz.
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
- **Checklist:** ao encerrar um PR depois do merge, avalie se algum item de [`checklist.md`](checklist.md) deve ser marcado. A marcação entra num novo commit, no PR seguinte, nunca direto na `develop`.

## Estrutura de pacotes

Aprovada. Vale a partir da Fase 2.

```text
io.github.marcelofabianov.cubit
├── CubitMod                // ponto de entrada: só conecta os registros
├── domain/                 // Java puro, testado com JUnit
│   ├── robot/              // RobotType, capacidades que o Cubit combina
│   ├── tier/               // RobotTier, TierSpec, TierCatalog (interface)
│   ├── energy/             // Battery, PowerState (ativo, inativo, hibernação), regras de consumo e religamento
│   ├── behavior/           // FollowDecision: seguir, parar ou teleportar; BurningAction: reagir a fogo e lava
│   ├── dock/               // DockStock, DockStatus, DockStay, DockQueue
│   └── ownership/          // FollowingRobotPolicy
├── config/                 // ModConfigSpec → implementa TierCatalog
├── registry/               // DeferredRegisters
├── entity/                 // RobotEntity, goals de IA
├── item/                   // RobotItem, PowerBankItem, RobotStateComponent
├── block/                  // InductionDockBlock, BlockEntity
├── menu/                   // RobotMenu, InductionDockMenu
├── network/                // pacotes próprios: NoticePayload e CubitNetwork
├── player/                 // attachment com o UUID do robô que segue, robôs mortos com o dono offline e /cubit reset
├── client/                 // renderer, modelo GeckoLib, tela
└── datagen/                // receitas, loot, traduções
```

Os testes ficam em `src/test` (JUnit) e os GameTests em um pacote `gametest`.

## Notas de modding

- **Limite do domínio:** um teste com ArchUnit deve falhar se `domain` importar `net.minecraft` ou `net.neoforged`. É mais simples que um subprojeto Gradle.
- **Cliente e comum:** `Menu` roda nos dois lados, e `Screen` só no cliente. Código comum não pode carregar classes de `client`, senão um servidor dedicado quebra.
- **Rede:** o menu e o `SynchedEntityData` bastam para o estado do robô. Os textos usam um pacote próprio, só do servidor para o cliente, e os pacotes do cliente para o servidor só entram com o controle remoto.
- **Config `COMMON`:** fica em `config/`, onde o packwiz consegue distribuí-lo. O `SERVER` ficaria dentro de cada mundo.
- **Atributos:** são registrados antes de o config carregar, então vida e velocidade são aplicadas quando a entidade nasce.
- **Receitas com datagen:** geram JSONs normais em `data/`, que o KubeJS pode sobrescrever.
- **Distribuição:** o pack usa packwiz e não guarda `.jar`, então o mod precisa ser publicado no Modrinth ou no CurseForge para entrar nele.
