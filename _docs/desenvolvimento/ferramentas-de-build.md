# Ferramentas de build

Todas as versões ficam em `gradle.properties`, exceto o Gradle, fixado em `gradle/wrapper/gradle-wrapper.properties`, com o `distributionSha256Sum` publicado em `services.gradle.org`, para o wrapper recusar uma distribuição alterada. Ao atualizar o Gradle, troque os dois juntos.

## Comandos

Os comandos ficam no `Makefile`. Rode `make` para ver a lista.

| Comando | O que faz |
|---|---|
| `make setup` | Instala as ferramentas de desenvolvimento e ativa os hooks |
| `make check` | Roda todos os gates: build, testes, arquitetura, cobertura, formatação e análise estática |
| `make test` | Roda os testes |
| `make coverage` | Confere a cobertura de `domain/` e mostra onde está o relatório |
| `make format` | Formata o código Java |
| `make format-check` | Confere a formatação sem alterar arquivos |
| `make build` | Roda os gates e gera o jar em `build/libs/` |
| `make clean` | Remove a saída do build |
| `make client` | Abre o jogo com o mod |
| `make server` | Inicia um servidor dedicado com o mod |
| `make data` | Roda a geração de dados |
| `make check-generated` | Roda a geração de dados e reprova se `src/generated` mudar |
| `make gametest` | Roda os GameTests num servidor sem interface |
| `make gametest-coverage` | Roda os GameTests e gera o relatório do que eles cobrem fora do domínio, sem reprovar nada |
| `make bundle` | Baixa o jar da última release com as dependências num `.zip` em `build/dist/`. Com `VERSION=0.1.0`, baixa a release escolhida |
| `make stop` | Para os daemons do Gradle |

**`make gametest`:** o jogo termina com sucesso mesmo quando o mod falha ao carregar, então `scripts/development/run-gametests.sh` só aprova se o log trouxer `All N required tests passed`. Foi conferido que o comando falha com uma GameTest reprovada e com uma classe de cliente no código comum. O `pre-push` também o roda, e o log fica em `build/gametest.log`. O servidor roda em `run/gametest/`, separado do `run/` do `make client`, para um config editado à mão não mudar o resultado. Num diretório novo, o log mostra um erro por falta do `server.properties`, que o jogo cria em seguida.

**GameTests:** ficam no pacote `gametest`, e cobrem o que está fora de `domain/` (regra 34 do `AGENTS.md`). Os testes usam as estruturas `empty.nbt` e `arena.nbt`, de `data/cubit/structure/`. O framework cerca cada estrutura com paredes e teto de barreira invisível, então a `arena`, com 24 blocos de comprimento e 8 de altura, limita o robô. Nela, o piso de pedra fica em `y=1`, porque o `y=0` relativo está uma camada abaixo da estrutura, e `helper.getEntities` não enxerga nada fora dela. Não compare posições com `helper.relativePos`, que devolve X e Z negativos: use `helper.absolutePos`.

**`make gametest-coverage`:** liga o agente do JaCoCo só nesse comando, pela propriedade `gameTestCoverage`, para os GameTests do `pre-push` rodarem sem ele. O relatório fica em `build/reports/jacoco/gametest/index.html`, sem `domain/`, que tem gate próprio, e sem `client/`, que não roda numa GameTest (perguntas 527 e 538). Se o mod não carregar, o relatório sai vazio, então leia a contagem de testes no log.

**Jar publicado:** a tarefa `jar` exclui os pacotes `gametest` e `datagen` e as estruturas de `data/cubit/structure/`, que só servem ao desenvolvimento. As execuções de `make gametest`, `make data`, `make client` e `make server` leem as classes direto, sem passar pelo jar, então um problema só no jar aparece nos testes de instância, no fim da Fase 3.

**LambDynamicLights:** é dependência obrigatória só do cliente, então o `neoforge.mods.toml` exige `lambdynlights_runtime` com `side = "CLIENT"`. Esse é o ID presente tanto no artefato de desenvolvimento quanto no jar publicado, que o traz embutido. O código compila contra a API, e o jar não leva a biblioteca. Ela só entra em `make client` e `make data`, pelo conjunto de fontes vazio `clientRun`, porque o jogo procura mods no classpath da execução e o `additionalRuntimeClasspath` não basta. O `make data` a exige porque também inicia como cliente. `make server` e `make gametest` não a carregam, o que prova que um servidor dedicado abre sem ela.

**Jade:** é dependência opcional, então o `neoforge.mods.toml` declara `jade` com `type = "optional"`, e o mod abre sem ele (regra 24 do `AGENTS.md`). O código compila contra o Jade (`compileOnly`), e o jar não o leva. Ele só entra em `make client` e `make data`, pelo conjunto de fontes `clientRun`. `make server` e `make gametest` não o carregam, e a GameTest `theRobotWorksWithoutJade` reprova se ele estiver presente. Só o pacote `compat.jade` importa o Jade, conferido pelo ArchUnit, e o próprio Jade acha o plugin pela anotação `@WailaPlugin` (perguntas 664, 665 e 667).

Receita com mais de 3 linhas vira um script `.sh` em `scripts/`, e o `Makefile` só o chama (regra 46 do `AGENTS.md`).

O primeiro build leva alguns minutos, porque baixa o JDK e prepara o Minecraft.

## Ferramentas

| Ferramenta | O que faz | Por que foi escolhida |
|---|---|---|
| Gradle (wrapper) | Executa o build, sem precisar instalar nada | Vem no MDK oficial do NeoForge |
| ModDevGradle | Integra o Gradle ao NeoForge | É o plugin do MDK oficial |
| JDK 21 Adoptium | Compila o mod e o Minecraft | O projeto o baixa sozinho, porque o Java do sistema pode não ter o pacote de desenvolvimento |
| JUnit | Roda os testes do domínio sem subir o jogo | É o padrão em Java e está na regra 32 do `AGENTS.md` |
| ArchUnit | Isola `domain/`, separa o código comum do cliente, separa `block/` de `entity/`, proíbe `get*` no domínio e deixa só o `compat.jade` importar o Jade | Garante as regras 12, 14, 23, 24 e 42 do `AGENTS.md` no build |
| JaCoCo | Mede a cobertura de `domain/`, com mínimo de 85% de instruções e de ramos | Garante a regra 34 do `AGENTS.md` |
| Spotless com google-java-format | Formata o código Java, com recuo de 2 espaços | É o padrão mais comum em Java, sem opções para discutir (pergunta 95) |
| Checkstyle | Confere as regras de forma do código | Reprova no build o que antes dependia da revisão (pergunta 97) |
| Jade `15.10.6+neoforge` | Mostra o tier, a bateria e o estado do Cubit e o estoque e o estado do Induction Dock ao mirar neles | É o mod de informações do Iron Meridian, na mesma versão, e opcional (pergunta 242). Vem do Maven do Modrinth, limitado ao grupo `maven.modrinth`, e o SHA-512 do jar foi conferido contra o `jade.pw.toml` do pack (pergunta 664) |
| LambDynamicLights `4.8.11+1.21.1` | Faz o Cubit iluminar o mundo, sem blocos de luz invisíveis | É a dependência obrigatória da regra 6 do `AGENTS.md`, na versão do Iron Meridian. Vem do repositório `maven.gegy.dev`, limitado ao grupo da biblioteca |

## Pacote de uma release

O `make bundle` chama o `scripts/development/bundle-release.sh` e monta um `.zip` com uma release pronta para instalar (perguntas 870 a 873).

- **O que faz:** pega a última release do GitHub, ou a de `VERSION`, e baixa o jar do Cubit dela. Lê as versões das dependências no `gradle.properties` da tag da release, e não da `develop`, e baixa do Modrinth o GeckoLib, o LambDynamicLights e o Jade.
- **Conferência:** o SHA-512 de cada dependência é conferido com o que o Modrinth publica, e o SHA-256 do jar do Cubit, com o que o GitHub publica. Um arquivo diferente reprova o comando.
- **Saída:** `build/dist/cubit-X.Y.Z-with-dependencies.zip`, com `mods/` para o Cubit, o GeckoLib e o LambDynamicLights, e `optional/` para o Jade.
- **Por que assim:** as dependências vêm do Modrinth, como no Iron Meridian e nas instâncias de teste, e as versões vêm da própria tag, para o pacote ser igual ao que a release foi testada.
- **Rede:** só as APIs públicas do GitHub e do Modrinth, sem login, com o user agent `cubit-bundle-script`. O JSON baixado é lido com `python3 -I`, como dado.
- **Precisa de:** `curl`, `python3`, `shasum` e `zip`, que o macOS já traz.
- **Quando reprova:** `no release vX.Y.Z on GitHub` quer dizer que a versão não existe. `Modrinth has no …` quer dizer que a versão da dependência na tag não está no Modrinth para o Minecraft e o NeoForge dela. `wrong SHA-…` quer dizer que o arquivo baixado não bate com o publicado: rode de novo e, se repetir, não use o pacote.

## Regras do Checkstyle

Ficam em `config/checkstyle/checkstyle.xml`.

| Regra | Limite | Origem |
|---|---|---|
| Tamanho de função | 30 linhas | Regra 27 do `AGENTS.md` |
| Complexidade ciclomática | 10 | Pilar Clareza |
| Tamanho de arquivo | 400 linhas | Pilar Clareza |
| Número mágico | Só -1, 0, 1 e 2 são livres, inclusive nos testes | Regra 26 do `AGENTS.md` |
| String literal | Só em constante `static final`, inclusive nos testes | Regra 26 do `AGENTS.md` |
| `TODO` | Só com issue: `TODO(#12)` | Regra 29 do `AGENTS.md` |
| Imports | Sem `*` e sem import não usado | Pilar Clareza |

As bibliotecas de teste vêm só do Maven Central, para o build não depender do Maven da NeoForge para elas.

## Quando algo reprova

- **`DomainBoundaryTest`:** o nome da regra que reprovou diz o motivo.
  - `theDomainOnlyDependsOnItselfAndJava`: uma classe de `domain/` importou o jogo ou outro pacote do mod. Mova a regra para fora do domínio ou inverta a dependência com uma interface.
  - `commonCodeDoesNotUseTheClient`: código comum citou uma classe de cliente. Mova a referência para `client/`.
  - `theDockDoesNotKnowTheEntity`: `block/` citou `entity/`. Use uma interface, como `DockableRobot`.
  - `theDomainHasNoGetters`: troque `getCapacity()` por `capacity()`.
- **`jacocoTestCoverageVerification`:** faltam testes no domínio. Abra `build/reports/jacoco/test/html/index.html` para ver o que falta.
- **`spotlessJavaCheck`:** o código não está formatado. Rode `make format`.
- **`checkstyleMain` ou `checkstyleTest`:** uma regra de forma do código foi violada. Corrija a causa, sem suprimir a regra.
- **`make gametest`:** uma GameTest reprovou, e o nome dela e o motivo aparecem em `build/gametest.log`, depois de `GAME TESTS COMPLETE`. Se esse trecho não existir, o mod não carregou. Com `invalid dist` no log, código comum referenciou uma classe de cliente.
- **`onlyThePluginUsesJade`:** uma classe fora de `compat.jade` importou o Jade. Mova a referência para o plugin, porque sem o Jade essa classe quebraria o mod.
- **`theRobotWorksWithoutJade`:** o Jade entrou nas GameTests. Confira se ele continua só em `clientRunRuntimeOnly`.
- **`lambdynlights_runtime is not installed`:** a execução não carregou a biblioteca. Confira se ela usa o conjunto de fontes `clientRun`. Em `make server` ou `make gametest`, esse erro significa que a dependência deixou de ser só do cliente.
- **`Verification of Gradle distribution failed`:** o arquivo baixado não bate com o `distributionSha256Sum`. Não apague o hash: confira a versão e o valor publicado, porque o download pode estar alterado.
- **`createMinecraftArtifacts`:** a preparação do Minecraft falhou. Rode com `--info` para ler o erro.
- **`release version 21 not supported`:** o Gradle usou um Java sem o pacote de desenvolvimento. Confira se `gradle/gradle-daemon-jvm.properties` existe e rode `make stop`.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-12 21:50 | Adiciona o `make bundle`. |
| Marcelo Fabiano | 2026-11-07 23:41 | Acrescenta a regra do Jade ao ArchUnit. |
| Marcelo Fabiano | 2026-11-07 21:40 | Adiciona o Jade. |
| Marcelo Fabiano | 2026-11-07 14:38 | Adiciona o relatório de cobertura dos GameTests. |
| Marcelo Fabiano | 2026-11-07 14:31 | Adiciona o jar publicado, as regras de ArchUnit e o gate de ramos. |
| Marcelo Fabiano | 2026-11-07 13:51 | Adiciona o checksum do wrapper. |
| Marcelo Fabiano | 2026-11-06 13:10 | Adiciona o LambDynamicLights. |
| Marcelo Fabiano | 2026-11-05 00:12 | Corrige o que o make data faz. |
| Marcelo Fabiano | 2026-11-04 21:49 | Registra as barreiras das GameTests. |
| Marcelo Fabiano | 2026-11-04 20:21 | Documenta a arena e as coordenadas das GameTests. |
| Marcelo Fabiano | 2026-11-04 19:56 | Adiciona o comando que confere os arquivos gerados. |
| Marcelo Fabiano | 2026-11-04 19:12 | Registra que uma GameTest reprovada faz o comando falhar e como as GameTests rodam. |
| Marcelo Fabiano | 2026-11-04 18:24 | Estende as regras de literais aos testes. |
| Marcelo Fabiano | 2026-11-04 18:20 | Adiciona a regra de string literal. |
| Marcelo Fabiano | 2026-11-04 18:12 | Registra o limite do comando de GameTests. |
| Marcelo Fabiano | 2026-11-04 18:02 | Passa os comandos para o Makefile. |
| Marcelo Fabiano | 2026-11-04 18:00 | Registra a exceção de número mágico nos testes. |
| Marcelo Fabiano | 2026-11-04 17:57 | Documenta o Spotless, o Checkstyle e as regras de forma do código. |
| Marcelo Fabiano | 2026-11-04 17:48 | Sobe a cobertura mínima para 85%. |
| Marcelo Fabiano | 2026-11-04 17:45 | Documenta as ferramentas de build e os comandos. |
