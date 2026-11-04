# Cubit — Visão geral

Mod próprio do **Iron Meridian** (Minecraft 1.21.1, NeoForge). Adiciona o **Cubit**, um robô companheiro craftado com recursos do jogo puro, que ajuda o player em tarefas simples. Ele evolui por tiers, de Base a Ultimate.

**Idioma:** o que entra no jar, aparece no jogo ou acompanha o download do mod fica em inglês, com um único arquivo de idioma, o `en_us.json`. Arquivos de programação também ficam em inglês. Arquivos de conteúdo, como os `.md`, ficam em português.

> Status: **Fase 2, versão mínima**. Os números são o ponto de partida para balancear nos testes.

## Conceito

- Companheiro **tecnológico, sem magia**, alinhado à proposta do pack.
- Tarefas simples: seguir, carregar itens, iluminar, avisar sobre perigos e minérios, coletar, organizar.
- Tem **personalidade**: a tela do rosto muda de expressão, ele faz barulho de esteira e se comunica com bipes e poucos textos curtos.
- A **bateria** é um recurso a gerenciar: o robô precisa voltar para carregar.

## Onde está cada coisa

| Assunto | Arquivo |
|---|---|
| Personalidade | [`_docs/conceito/persona.md`](_docs/conceito/persona.md) |
| Visual, expressões e sons | [`_docs/conceito/visual.md`](_docs/conceito/visual.md) |
| Imagens de conceito | [`_docs/imagens/conceito/`](_docs/imagens/conceito/) |
| Bateria, Induction Dock, Power Bank, redstone, painel solar e retorno automático | [`_docs/funcionalidades/energia.md`](_docs/funcionalidades/energia.md) |
| Movimento, robô que segue, vida, morte e local de morte | [`_docs/funcionalidades/movimento.md`](_docs/funcionalidades/movimento.md) |
| Inventário, ímã, filtros, baús e slots exclusivos | [`_docs/funcionalidades/inventario.md`](_docs/funcionalidades/inventario.md) |
| Scanners, alertas e lanterna | [`_docs/funcionalidades/sensores.md`](_docs/funcionalidades/sensores.md) |
| Choque e modos de defesa | [`_docs/funcionalidades/combate.md`](_docs/funcionalidades/combate.md) |
| Controle remoto e modo área | [`_docs/funcionalidades/controle-remoto.md`](_docs/funcionalidades/controle-remoto.md) |
| Itens, blocos, nomes e receitas | [`_docs/itens.md`](_docs/itens.md) |
| Números por tier | [`_docs/tiers.md`](_docs/tiers.md) |
| Resumo das decisões | [`_docs/decisoes.md`](_docs/decisoes.md) |
| Perguntas e respostas por fase | [`_docs/perguntas/`](_docs/perguntas/README.md) |
| Termos e nomes oficiais | [`_docs/glossario.md`](_docs/glossario.md) |
| Fluxo de trabalho | [`_docs/desenvolvimento/fluxo-de-trabalho.md`](_docs/desenvolvimento/fluxo-de-trabalho.md) |
| Ferramentas de build e comandos | [`_docs/desenvolvimento/ferramentas-de-build.md`](_docs/desenvolvimento/ferramentas-de-build.md) |
| Git hooks | [`_docs/desenvolvimento/hooks-do-git.md`](_docs/desenvolvimento/hooks-do-git.md) |

Os números ficam em `_docs/tiers.md`. As outras páginas apontam para lá, e o `README.md` traz só um resumo.

## Fases

1. **Conceito** (encerrada): definir persona, features e demais informações em arquivos `.md`.
2. **Versão mínima** (atual): crafting com itens do jogo puro, seguir o player, inventário, bateria, vários Cubits com um seguindo, Induction Dock com hibernação, recarga com redstone e virar item.
3. **Sentidos**: painel solar, lanterna, scanners, bipes e textos curtos, expressões na tela, nome, cor e Jade.
4. **Upgrades e autonomia**: tiers, Power Bank, ímã, filtros, demais slots exclusivos, choque, modos de defesa, controle remoto, modo área, descarregar em baú, retorno automático e local de morte.

## Futuro

O robô atual é de uso geral e o primeiro do mod. No futuro, o mod pode ter vários robôs especializados, para atividades diversas. Essa ideia está fora das fases atuais e ainda não tem definição, mas nada no projeto pode ficar fixo de forma que comprometa essa flexibilidade.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 17:44 | Marca a Fase 2 como atual. |
| Marcelo Fabiano | 2026-11-04 17:26 | Atualiza a fase 2 e a regra dos números. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra a flexibilidade para robôs especializados. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra a ideia futura de robôs especializados. |
| Marcelo Fabiano | 2026-11-04 17:15 | Define a regra de idioma e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Atualiza a fase 2 e a regra do robô que segue. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza o mapa dos documentos e as fases. |
| Marcelo Fabiano | 2026-11-04 16:07 | Documenta o fluxo de trabalho. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona a visão geral, o README e as regras do projeto. |
