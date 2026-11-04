# Ferramentas de build

Todas as versões ficam em `gradle.properties`, exceto o Gradle, fixado em `gradle/wrapper/gradle-wrapper.properties`.

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
| `make data` | Roda a geração de dados e confirma que o mod carrega |
| `make gametest` | Roda os GameTests num servidor sem interface |
| `make stop` | Para os daemons do Gradle |

**Limite do `make gametest`:** sem GameTests, o servidor falha ao iniciar com `No test functions were given!`, mas o comando termina com sucesso. Ainda não foi provado que uma GameTest reprovada faz o comando falhar. Isso precisa ser conferido quando a primeira GameTest existir.

Receita com mais de 3 linhas vira um script `.sh` em `scripts/`, e o `Makefile` só o chama (regra 46 do `AGENTS.md`).

O primeiro build leva alguns minutos, porque baixa o JDK e prepara o Minecraft.

## Ferramentas

| Ferramenta | O que faz | Por que foi escolhida |
|---|---|---|
| Gradle (wrapper) | Executa o build, sem precisar instalar nada | Vem no MDK oficial do NeoForge |
| ModDevGradle | Integra o Gradle ao NeoForge | É o plugin do MDK oficial |
| JDK 21 Adoptium | Compila o mod e o Minecraft | O projeto o baixa sozinho, porque o Java do sistema pode não ter o pacote de desenvolvimento |
| JUnit | Roda os testes do domínio sem subir o jogo | É o padrão em Java e está na regra 32 do `AGENTS.md` |
| ArchUnit | Impede que `domain/` dependa do jogo | Garante a regra 12 do `AGENTS.md` no build |
| JaCoCo | Mede a cobertura de `domain/`, com mínimo de 85% | Garante a regra 34 do `AGENTS.md` |
| Spotless com google-java-format | Formata o código Java, com recuo de 2 espaços | É o padrão mais comum em Java, sem opções para discutir (pergunta 95) |
| Checkstyle | Confere as regras de forma do código | Reprova no build o que antes dependia da revisão (pergunta 97) |

## Regras do Checkstyle

Ficam em `config/checkstyle/checkstyle.xml`.

| Regra | Limite | Origem |
|---|---|---|
| Tamanho de função | 30 linhas | Regra 27 do `AGENTS.md` |
| Complexidade ciclomática | 10 | Pilar Clareza |
| Tamanho de arquivo | 400 linhas | Pilar Clareza |
| Número mágico | Só -1, 0, 1 e 2 são livres. Testes ficam de fora, porque os valores esperados vêm dos documentos | Regra 26 do `AGENTS.md` |
| `TODO` | Só com issue: `TODO(#12)` | Regra 29 do `AGENTS.md` |
| Imports | Sem `*` e sem import não usado | Pilar Clareza |

As bibliotecas de teste vêm só do Maven Central, para o build não depender do Maven da NeoForge para elas.

## Quando algo reprova

- **`DomainBoundaryTest`:** uma classe de `domain/` importou o jogo. Mova a regra para fora do domínio ou inverta a dependência com uma interface.
- **`jacocoTestCoverageVerification`:** faltam testes no domínio. Abra `build/reports/jacoco/test/html/index.html` para ver o que falta.
- **`spotlessJavaCheck`:** o código não está formatado. Rode `make format`.
- **`checkstyleMain` ou `checkstyleTest`:** uma regra de forma do código foi violada. Corrija a causa, sem suprimir a regra.
- **`createMinecraftArtifacts`:** a preparação do Minecraft falhou. Rode com `--info` para ler o erro.
- **`release version 21 not supported`:** o Gradle usou um Java sem o pacote de desenvolvimento. Confira se `gradle/gradle-daemon-jvm.properties` existe e rode `make stop`.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 18:12 | Registra o limite do comando de GameTests. |
| Marcelo Fabiano | 2026-11-04 18:02 | Passa os comandos para o Makefile. |
| Marcelo Fabiano | 2026-11-04 18:00 | Registra a exceção de número mágico nos testes. |
| Marcelo Fabiano | 2026-11-04 17:57 | Documenta o Spotless, o Checkstyle e as regras de forma do código. |
| Marcelo Fabiano | 2026-11-04 17:48 | Sobe a cobertura mínima para 85%. |
| Marcelo Fabiano | 2026-11-04 17:45 | Documenta as ferramentas de build e os comandos. |
