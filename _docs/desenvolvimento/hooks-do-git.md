# Git hooks

Os hooks ficam em `scripts/development/git-hooks/` e rodam em todo commit e todo push.

## Como ativar

Rode uma vez em cada clone:

```bash
scripts/development/setup-dev.sh
```

O script baixa o gitleaks para `.tools/`, confere o checksum e aponta o `core.hooksPath` do repositório para os hooks. Nada é instalado fora do projeto. Por enquanto, só funciona em Linux x86_64.

## O que cada hook faz

| Hook | Gate | O que reprova |
|---|---|---|
| `pre-commit` | Branch | Commit fora de `feature/{issue}-{slug}`, `hotfix/{issue}-{slug}` ou `release/X.Y.Z` |
| `pre-commit` | Histórico | Arquivo `.md` alterado sem linha nova na tabela de histórico de modificações |
| `pre-commit` | gitleaks | Segredo nas alterações preparadas para o commit |
| `commit-msg` | Mensagem | Título fora do Conventional Commits |
| `commit-msg` | Autoria | Coautoria ou atribuição a uma IA. Citar uma ferramenta de IA como assunto da mudança é permitido |
| `pre-push` | Build | Compilação, testes, arquitetura ou cobertura reprovados, por meio de `./gradlew check` |

Os hooks rodam só na máquina local. Eles não bloqueiam merges feitos pelo GitHub nem um push direto de outra máquina, e o plano atual do GitHub não permite proteger branches em repositório privado.

## gitleaks

- **O que faz:** procura senhas, tokens e chaves nas alterações antes do commit.
- **Por que foi escolhido:** funciona como um único binário, sem depender de Java ou Gradle, que ainda não existem no projeto.
- **Versão:** fixada em `scripts/development/setup-dev.sh`, com o checksum oficial.

## Quando um gate reprova

- **Branch:** crie a issue e a branch dela, e faça o commit lá. As alterações não commitadas acompanham a troca de branch.
- **Histórico:** adicione uma linha no topo da tabela do arquivo, com o nome da pessoa, a data e a hora (`AAAA-MM-DD HH:MM`) e uma frase direta do que foi feito.
- **gitleaks:** remova o segredo do arquivo e troque-o, se ele já tiver sido exposto. O relatório oculta o valor. Em caso de falso positivo, consulte o desenvolvedor antes de criar qualquer exceção.
- **Mensagem:** reescreva o título no formato `tipo(escopo opcional): descrição`, em português.
- **Autoria:** remova a atribuição.
- **gitleaks não encontrado:** rode `scripts/development/setup-dev.sh`.
- **Build:** veja [`ferramentas-de-build.md`](ferramentas-de-build.md#quando-algo-reprova).

Nunca use `--no-verify`.

## Gates previstos

- **Commit:** formatação, com o Spotless e o google-java-format.
- **Push:** datagen e `runServer`, quando houver conteúdo para eles.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 17:44 | Adiciona o gate de push. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Adiciona o gate do histórico de modificações. |
| Marcelo Fabiano | 2026-11-04 16:57 | Registra o limite da proteção de branches. |
| Marcelo Fabiano | 2026-11-04 15:58 | Adiciona gates de commit com gitleaks. |
