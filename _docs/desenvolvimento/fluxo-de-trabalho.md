# Fluxo de trabalho

Este documento mostra, passo a passo, como uma tarefa vai da issue ao merge e como uma versão é publicada. As regras ficam no [`AGENTS.md`](../../AGENTS.md). Em caso de divergência, vale o `AGENTS.md`.

## Preparar o ambiente

Rode uma vez em cada clone:

```bash
make setup
```

O script ativa os gates de commit, descritos em [`hooks-do-git.md`](hooks-do-git.md).

O `gh` precisa usar a conta pessoal:

```bash
gh api user --jq .login
```

Se mostrar outra conta, prefixe os comandos com `GH_TOKEN=$(gh auth token --user marcelofabianov)`.

## Caminho de uma tarefa

```text
Issue → Branch → Trabalho → Validação → Plano de commits → Commits → PR → Merge → Encerramento
```

### 1. Issue

Toda tarefa começa por uma issue, atribuída ao desenvolvedor e no milestone da fase, com o que entra e o que fica fora do escopo. Issues e PRs dividem a mesma numeração, então confirme o número antes de nomear a branch.

### 2. Branch

Atualize a `develop` e crie a branch a partir dela, com o nome em inglês e em `kebab-case`:

```bash
git fetch origin develop:develop
git switch -c feature/{issue}-{slug} --no-track develop
```

### 3. Trabalho

- **A partir da Fase 2:** a feature precisa estar documentada em `_docs/funcionalidades/`, com as decisões fechadas. O teste vem antes ou junto do código.
- **Decisão bloqueante:** pare e siga o [fluxo do `AGENTS.md`](../../AGENTS.md#decisões-bloqueantes). Registre a resposta em [`perguntas/`](../perguntas/README.md), no arquivo da fase.
- **Rascunhos:** ficam em `tmp/`.
- **Histórico de modificações:** todo `.md` em `_docs/` termina com uma tabela de histórico. Quem altera o arquivo adiciona uma linha no topo da tabela, com o nome da pessoa, a data e a hora e uma frase direta do que foi feito.

### 4. Validação

Compare a entrega com os documentos `.md`, que são os validadores. As respostas em [`perguntas/`](../perguntas/README.md) prevalecem sobre os demais. Passe também pelos checklists de [`pilares.md`](pilares.md) que se aplicam. Artefatos, como testes, logs, relatórios e capturas de tela, complementam essa conferência, mas não a substituem.

Se a entrega e um documento divergirem, corrija a entrega. Se o documento estiver errado, pergunte ao desenvolvedor antes de mudá-lo.

### 5. Plano de commits

O plano de commits é a relação dos commits a fazer, com os arquivos e a mensagem de cada um. O agente o apresenta e espera a autorização.

| # | Branch | Mensagem | Arquivos |
|---|---|---|---|
| 1 | `feature/12-power-bank` | `docs: descreve a Power Bank` | `_docs/funcionalidades/energia.md` |

Cada commit trata de uma mudança lógica e deixa o projeto num estado válido, sem link quebrado nem teste falhando.

### 6. Commits

```bash
git add {arquivos do commit}
git commit -m "tipo(escopo opcional): descrição em português"
```

Use `git add` com os arquivos do plano, nunca `git add .`. Se um gate reprovar, corrija a causa conforme [`hooks-do-git.md`](hooks-do-git.md).

### 7. Pull request

Antes do push, atualize a branch com a de origem. Depois, abra o PR para a `develop`:

```bash
git push -u origin feature/{issue}-{slug}
cp .github/pull_request_template.md tmp/pr.md
# Preencha tmp/pr.md antes de abrir o PR.
gh pr create --base develop --title "tipo: descrição" --body-file tmp/pr.md
```

A descrição do PR segue o template [`.github/pull_request_template.md`](../../.github/pull_request_template.md), que o GitHub também aplica aos PRs abertos pelo site. Ele tem:

- `Closes #n` na primeira linha;
- o que muda;
- a validação: os documentos conferidos e a evidência de cada um, os comandos, o teste no Iron Meridian e os checklists dos pilares;
- como revisar ou testar;
- o que ficou fora do escopo, quando houver.

### 8. Merge

O merge é feito no GitHub, pelo desenvolvedor ou pelo agente, se ele pedir para aquele PR.

### 9. Encerramento

1. Confira se a issue fechou. Se continuar aberta, feche-a à mão.
2. Atualize a `develop` local.
3. Apague a branch da tarefa, com autorização.

## O que precisa de autorização

A autorização vale só para aquela ação e não se estende às seguintes.

| O agente faz sem pedir | O agente só faz com autorização |
|---|---|
| Ler, investigar e apresentar opções | Commit, push, merge, tag, release e PR |
| Editar arquivos da tarefa aprovada, sem commit | Push forçado e qualquer reescrita de histórico |
| Rodar testes e gates | Apagar branches e arquivos |
| Criar a issue exigida por uma tarefa pedida | Mudar o `AGENTS.md` |
| Corrigir o que quebra um gate, sem mudar comportamento | Decisão bloqueante, mesmo com recomendação clara |

Todo item criado no GitHub é informado com o link, na mesma resposta.

## Publicar uma versão

```text
develop → homolog → release/X.Y.Z → main (tag vX.Y.Z)
                         └────────→ develop
```

1. **Homologação:** abra um PR da `develop` para a `homolog`.
2. **Teste:** teste a `homolog` numa instância do Iron Meridian.
3. **Correções:** o que aparecer no teste passa pelo caminho normal, de `feature/*` para a `develop`, e volta à `homolog` por PR.
4. **Release fechada:** com o teste aprovado, crie a `release/X.Y.Z` a partir da `homolog` e ajuste a versão. Só entram ajustes da própria release.
5. **Publicação:** abra um PR da `release/X.Y.Z` para a `main`.
6. **Tag:** depois do merge, crie a tag anotada na `main`, com autorização:

   ```bash
   git tag -a vX.Y.Z -m "vX.Y.Z"
   git push origin vX.Y.Z
   ```

7. **Volta:** abra um PR da `release/X.Y.Z` para a `develop`.

Na fase `0.x`, mudança incompatível sobe o MINOR, e o resto sobe o PATCH.

## Corrigir com urgência

Use só para um problema no que já foi publicado.

1. Crie a issue e a `hotfix/{issue}-{slug}` a partir da `main`.
2. Faça a correção e teste numa instância do Iron Meridian.
3. Abra um PR para a `main` e crie a tag do novo PATCH depois do merge.
4. Leve a correção para a `homolog` e para a `develop`, por PR.

## Reescrever o histórico

Só em último caso, numa branch sem merge e com autorização, como para tirar um dado que não podia ter sido enviado. Nunca em `main`, `homolog` ou `develop`.

```bash
git push --force-with-lease=feature/{issue}-{slug}:{hash esperado} origin feature/{issue}-{slug}
```

## Encerrar uma sessão

Se restar trabalho pendente, deixe um prompt em `tmp/prompt/{AAAA-MM-DD_HHhMM}_continuidade_{assunto}.md`:

```markdown
# Continuidade: {assunto}

## Leitura obrigatória
O que ler, em ordem, e o que entender de cada item.

## O que foi feito
Links, estado do repositório, o que não foi commitado e como cada autorização foi interpretada.

## O que se espera da próxima sessão
O primeiro passo, separando o que está decidido do que está pendente.
```

## Cuidados

- **Conteúdo público do projeto:** não cite projetos privados em arquivos, commits, issues ou PRs.
- **Autoria:** nada de coautoria ou atribuição a uma IA.
- **Saves:** antes de mudar um ID, um data component ou uma chave de config, leia a regra 18 do `AGENTS.md`.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 23:45 | Limita o histórico aos arquivos de `_docs/`. |
| Marcelo Fabiano | 2026-11-04 19:12 | Adota o template de pull request. |
| Marcelo Fabiano | 2026-11-04 18:02 | Usa o Makefile na preparação do ambiente. |
| Marcelo Fabiano | 2026-11-04 17:51 | Inclui os pilares na validação. |
| Marcelo Fabiano | 2026-11-04 17:44 | Adiciona a etapa de validação contra os documentos. |
| Marcelo Fabiano | 2026-11-04 17:26 | Esclarece o que é permitido na Fase 1. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 16:57 | Aponta as decisões para as perguntas por fase e adiciona o milestone e o histórico. |
| Marcelo Fabiano | 2026-11-04 16:07 | Documenta o fluxo de trabalho. |
