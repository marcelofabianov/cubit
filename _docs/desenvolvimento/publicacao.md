# Publicação

Como publicar uma versão do Cubit no Modrinth e no CurseForge, com a mesma versão nos dois (pergunta 714). O envio é manual, pelo site, sem plugin do Gradle e sem token na máquina (pergunta 744). O desenvolvedor cria as contas e os projetos e envia o jar. O agente prepara os textos, o changelog e a lista do que preencher (pergunta 748).

As regras das plataformas foram conferidas em 2026-11-08, nas [fontes](#fontes) no fim da página. Elas mudam com o tempo, então confira de novo antes de cada publicação. O que as fontes não confirmam está marcado como **não confirmado**: confira no próprio site, na hora.

## Antes de enviar

1. **Checklist:** os itens 4 a 8 da versão no [`checklist.md`](../../checklist.md) estão feitos, inclusive os testes de instância e os termos das ferramentas de IA dos sons.
2. **Versão:** a `release/X.Y.Z` foi mesclada na `main`, com a tag `vX.Y.Z` e o [`CHANGELOG.md`](../../CHANGELOG.md) com a data, como em [Publicar uma versão](fluxo-de-trabalho.md#publicar-uma-versão).
3. **Jar:** gerado da `main`, na tag:

   ```bash
   git switch main
   git pull --ff-only
   make build
   ```

   O arquivo fica em `build/libs/cubit-X.Y.Z.jar`, sem os pacotes `gametest` e `datagen`, como descrito em [`ferramentas-de-build.md`](ferramentas-de-build.md).
4. **Repositório público (só na primeira vez):** o desenvolvedor confere que nada privado está no repositório, nem no histórico, e o torna público, em *Settings → General → Danger Zone → Change visibility*. Os links de código e de issues das páginas e do changelog dependem disso, e as issues do GitHub são o canal de bugs (pergunta 751).

## O que preencher

Os mesmos dados valem para as duas plataformas.

| Campo | Valor |
|---|---|
| Nome | `Cubit`. O Modrinth pede que o título seja só o nome do projeto |
| Slug | `cubit`, livre no Modrinth em 2026-11-08. Se o CurseForge recusar, use `cubit-robot` nas duas, para ficarem iguais (pergunta 750) |
| Resumo | Uma frase em inglês, sem repetir o nome, em [`descricao-das-plataformas.md`](descricao-das-plataformas.md#resumo) |
| Descrição longa | Em inglês, citando a tradução para o português (pergunta 745), em [`descricao-das-plataformas.md`](descricao-das-plataformas.md#descrição-longa) (pergunta 758) |
| Ícone | O ícone do mod, ampliado sem suavizar, como em [Ícone](#ícone) |
| Galeria | Só capturas de tela feitas no jogo, no teste de instância. Nada de pranchas de conceito nem de imagem feita com IA (pergunta 747) |
| Licença | MIT |
| Código | `https://github.com/marcelofabianov/cubit` |
| Issues | `https://github.com/marcelofabianov/cubit/issues` |
| Categorias | Escolhidas no site. As que descrevem o Cubit são tecnologia e utilidade, se houver categorias com esses nomes |
| Lados | Cliente e servidor: o mod precisa estar nos dois |
| Versão do jogo | `1.21.1` |
| Loader | NeoForge |
| Tipo da versão | Release (pergunta 743) |
| Número da versão | O do `gradle.properties`, como `0.1.0` |
| Changelog | A seção da versão no `CHANGELOG.md`, em Markdown |

### Dependências

Iguais às do `neoforge.mods.toml`:

| Mod | Tipo | Observação |
|---|---|---|
| GeckoLib | Obrigatória | No cliente e no servidor |
| LambDynamicLights | Obrigatória | Só no cliente. O servidor dedicado abre sem ela |
| Jade | Opcional | O mod abre sem ele |

### Ícone

O `mod_logo.png` tem 25×25 pixels, e o CurseForge pede um PNG de pelo menos 400×400. Como 400 é 16 vezes 25, a ampliação por vizinho mais próximo mantém os pixels nítidos:

```bash
mkdir -p tmp/publicacao
magick src/main/resources/assets/cubit/textures/gui/mod_logo.png -filter point -resize 400x400 tmp/publicacao/icon.png
```

O mesmo arquivo serve ao Modrinth, que aceita PNG.

## Modrinth

1. **Projeto:** em `modrinth.com`, crie um projeto do tipo mod, com o nome, o slug e o resumo da tabela.
2. **Página:** preencha a descrição, o ícone, a galeria, os links, a licença, as categorias e os lados. As regras pedem que a descrição diga o que o mod adiciona, por que baixá-lo e o que for crítico, e que todos os metadados estejam corretos.
3. **Avisos:** na aba *Disclosures* das configurações do projeto, ligue `Contains AI-generated content` para os assets e diga que são os sons (pergunta 746). O aviso fica nesse campo, não na descrição, como recomenda a plataforma.
4. **Versão:** crie a versão com o número, o nome `Cubit X.Y.Z`, o canal release, o loader NeoForge, a versão `1.21.1`, as dependências da tabela, o jar e o changelog.
5. **Revisão:** envie o projeto para a revisão. A meta da plataforma é de 24 a 48 horas, mas ela avisa que costuma levar de duas a quatro semanas. Um projeto reenviado depois de uma correção volta para o fim da fila.

## CurseForge

1. **Projeto:** em `authors.curseforge.com`, escolha Minecraft e preencha o nome, o resumo, a descrição, a licença, a classe de mods, a categoria principal, até 4 categorias extras e o logo. O nome precisa ser único, e o título e a descrição ficam em inglês.
2. **Links:** depois de criar, na área de gestão do projeto, acrescente os links do código e das issues.
3. **Arquivo:** envie o jar. É o último passo antes de o projeto ir para a moderação. Preencha o tipo release, a versão `1.21.1`, o loader NeoForge, o changelog e as dependências da tabela, em *Related Projects*. Sem a marcação do loader, o arquivo fica como incompatível.
4. **Moderação:** o projeto e os arquivos passam por verificações automáticas e manuais. A plataforma não informa o prazo.

**Não confirmado:** as regras do slug, o nome exato com que o NeoForge aparece no formulário e se há campo de versão do Java ou de lado. O artigo de envio diz que os arquivos precisam ser `.zip` na maioria dos jogos, mas não fala do jar do Minecraft: confira no formulário.

## Depois da aprovação

1. **Conferência:** abra as duas páginas e confira os links, as dependências e o arquivo.
2. **Iron Meridian:** o pack adiciona o Cubit pelo passo "Adicionar um mod novo" do `docs/MANUTENCAO.md` dele, pelo Modrinth, de onde vêm o GeckoLib, o LambDynamicLights e o Jade. Ali, o comando é `packwiz modrinth add --project-id <id> --version-id <id> -y`, e o `git status` mostra se ele mexeu em outros `.pw.toml`. A exceção do Cubit à regra do pack contra funções repetidas está na issue `marcelofabianov/iron-meridian#2`.

## Versões seguintes

1. Siga o mesmo caminho: o `CHANGELOG.md` atualizado na `release/X.Y.Z` (pergunta 749), a tag e o jar da `main`.
2. No Modrinth, crie uma versão nova no projeto, como no passo 4.
3. No CurseForge, envie um arquivo novo, como no passo 3.
4. Use o mesmo número nas duas plataformas e o tipo combinado para a versão.

## Fontes

Conferidas em 2026-11-08:

| Plataforma | Fonte | O que confirma |
|---|---|---|
| Modrinth | [Content Rules](https://modrinth.com/legal/rules) | Descrição em inglês e o que ela deve dizer, metadados corretos, regras de IA e imagens |
| Modrinth | [AI policy and disclosures](https://modrinth.com/news/article/ai-policy-and-disclosures/) | A aba *Disclosures* e o aviso de IA para os assets |
| Modrinth | [Content disclosures](https://support.modrinth.com/en/articles/16567675-content-disclosures) | O aviso fica no campo próprio, não na descrição |
| Modrinth | [Project review times](https://support.modrinth.com/en/articles/8793355-project-review-times) | Prazos da revisão e a fila no reenvio |
| Modrinth | [API: create project](https://docs.modrinth.com/api/operations/createproject/) e [create version](https://docs.modrinth.com/api/operations/createversion/) | Campos do projeto e da versão, os canais e os tipos de dependência |
| Modrinth | API pública, `GET /v2/project/cubit` | Resposta 404: o slug `cubit` estava livre |
| CurseForge | [Creating and submitting a project](https://support.curseforge.com/support/solutions/articles/9000197241-creating-and-submitting-a-project) | Campos do projeto e o arquivo antes da moderação |
| CurseForge | [Project submission guide](https://support.curseforge.com/support/solutions/articles/9000199552-project-submission-guide-and-tips) | Logo em PNG de 400×400 |
| CurseForge | [File project types](https://support.curseforge.com/support/solutions/articles/9000197242-file-project-types-and-additional-fields) | Campos do arquivo e os tipos de projeto relacionado |
| CurseForge | [Moderation policies](https://support.curseforge.com/en/support/solutions/articles/9000197279-project-and-modpack-moderation-policies) | Moderação automática e manual, e o aviso de IA só em imagens de vitrine |
| CurseForge | [Minecraft modloaders](https://support.curseforge.com/support/solutions/articles/9000202116-minecraft-modloaders) | A marcação do loader no arquivo |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-08 01:05 | Aponta o resumo e a descrição longa para a descrição das plataformas. |
| Marcelo Fabiano | 2026-11-08 00:33 | Cria o guia de publicação no Modrinth e no CurseForge. |
