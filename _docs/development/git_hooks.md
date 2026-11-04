# Git hooks

Os hooks ficam em `scripts/development/git-hooks/` e rodam em todo commit.

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
| `pre-commit` | gitleaks | Segredo nas alterações preparadas para o commit |
| `commit-msg` | Mensagem | Título fora do Conventional Commits |
| `commit-msg` | Autoria | Coautoria ou atribuição a uma IA. Citar uma ferramenta de IA como assunto da mudança é permitido |

Os hooks não bloqueiam merges feitos pelo GitHub, porque rodam só na máquina local.

## gitleaks

- **O que faz:** procura senhas, tokens e chaves nas alterações antes do commit.
- **Por que foi escolhido:** funciona como um único binário, sem depender de Java ou Gradle, que ainda não existem no projeto.
- **Versão:** fixada em `scripts/development/setup-dev.sh`, com o checksum oficial.

## Quando um gate reprova

- **Branch:** crie a issue e a branch dela, e faça o commit lá. As alterações não commitadas acompanham a troca de branch.
- **gitleaks:** remova o segredo do arquivo e troque-o, se ele já tiver sido exposto. O relatório oculta o valor. Em caso de falso positivo, consulte o desenvolvedor antes de criar qualquer exceção.
- **Mensagem:** reescreva o título no formato `tipo(escopo opcional): descrição`, em português.
- **Autoria:** remova a atribuição.
- **gitleaks não encontrado:** rode `scripts/development/setup-dev.sh`.

Nunca use `--no-verify`.

## Gates previstos

Os gates de Java e Gradle entram na Fase 2, junto do código:

- **Commit:** formatação.
- **Push:** build, testes, ArchUnit, datagen, cobertura e `runServer`.
