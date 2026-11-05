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
| `pre-commit` | Histórico | Arquivo `.md` em `_docs/` alterado sem linha nova na tabela de histórico de modificações. Os `.md` fora de `_docs/` não têm essa tabela |
| `pre-commit` | Formatação | Arquivo `.java` fora do padrão, com um `.java` no commit |
| `pre-commit` | gitleaks | Segredo nas alterações preparadas para o commit |
| `commit-msg` | Mensagem | Título fora do Conventional Commits |
| `commit-msg` | Autoria | Coautoria ou atribuição a uma IA. Citar uma ferramenta de IA como assunto da mudança é permitido |
| `pre-push` | Build | Compilação, testes, arquitetura, cobertura, formatação ou análise estática reprovados, por meio de `./gradlew check` |
| `pre-push` | GameTests | GameTest reprovada, ou mod que não carrega no servidor, como com uma classe de cliente, por meio de `scripts/development/run-gametests.sh` |
| `pre-push` | Datagen | `src/generated` diferente do que a geração produz, por meio de `scripts/development/check-generated.sh` |

Os hooks rodam só na máquina local. Eles não bloqueiam merges feitos pelo GitHub nem um push direto de outra máquina, e o plano atual do GitHub não permite proteger branches em repositório privado.

## gitleaks

- **O que faz:** procura senhas, tokens e chaves nas alterações antes do commit.
- **Por que foi escolhido:** funciona como um único binário, sem depender de Java ou Gradle, que ainda não existiam no projeto quando ele foi adotado.
- **Versão:** fixada em `scripts/development/setup-dev.sh`, com o checksum oficial.

## Quando um gate reprova

- **Branch:** crie a issue e a branch dela, e faça o commit lá. As alterações não commitadas acompanham a troca de branch.
- **Formatação:** rode `make format` e adicione os arquivos ao commit de novo.
- **Histórico:** adicione uma linha no topo da tabela do arquivo, com o nome da pessoa, a data e a hora (`AAAA-MM-DD HH:MM`) e uma frase direta do que foi feito.
- **gitleaks:** remova o segredo do arquivo e troque-o, se ele já tiver sido exposto. O relatório oculta o valor. Em caso de falso positivo, consulte o desenvolvedor antes de criar qualquer exceção.
- **Mensagem:** reescreva o título no formato `tipo(escopo opcional): descrição`, em português.
- **Autoria:** remova a atribuição.
- **gitleaks não encontrado:** rode `scripts/development/setup-dev.sh`.
- **Build:** veja [`ferramentas-de-build.md`](ferramentas-de-build.md#quando-algo-reprova).
- **GameTests:** veja [`ferramentas-de-build.md`](ferramentas-de-build.md#quando-algo-reprova). Se o log citar `invalid dist`, código comum referenciou uma classe de cliente: mova a referência para `client/`.
- **Datagen:** se o `datagen/` mudou, rode `make data` e adicione `src/generated` ao commit. Se um arquivo gerado foi editado à mão, desfaça com `git restore src/generated` e leve a mudança para o `datagen/`, porque o cache da geração não sobrescreve a edição.

Nunca use `--no-verify`.

## Por que o `runServer` não é um gate

Ele exigiria aceitar o EULA em cada clone. As GameTests usam o mesmo alvo de servidor dedicado (`forgeserverdev`) e reprovam igualmente uma classe de cliente carregada no servidor (pergunta 111).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 00:12 | Corrige o motivo da escolha do gitleaks. |
| Marcelo Fabiano | 2026-11-04 23:45 | Limita o gate do histórico aos arquivos de `_docs/`. |
| Marcelo Fabiano | 2026-11-04 19:56 | Ativa os gates de GameTests e datagen no push. |
| Marcelo Fabiano | 2026-11-04 18:02 | Usa o Makefile na correção da formatação. |
| Marcelo Fabiano | 2026-11-04 17:57 | Adiciona o gate de formatação. |
| Marcelo Fabiano | 2026-11-04 17:44 | Adiciona o gate de push. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Adiciona o gate do histórico de modificações. |
| Marcelo Fabiano | 2026-11-04 16:57 | Registra o limite da proteção de branches. |
| Marcelo Fabiano | 2026-11-04 15:58 | Adiciona gates de commit com gitleaks. |
