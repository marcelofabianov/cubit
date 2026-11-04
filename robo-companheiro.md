# Cubit — Visão geral

Mod próprio do **Iron Meridian** (Minecraft 1.21.1, NeoForge). Adiciona o **Cubit**, um robô companheiro craftado com recursos do jogo puro, que ajuda o player em tarefas simples. Ele evolui por tiers, de Base a Ultimate.

**Idioma:** tudo que aparece no mod (nome, itens, legendas, telas, mensagens, IDs e comandos) fica só em inglês, com um único arquivo de idioma, o `en_us.json`. Os documentos seguem em português.

> Status: **Fase 1, conceito**. Os números são o ponto de partida para balancear nos testes.

## Conceito

- Companheiro **tecnológico, sem magia**, alinhado à proposta do pack.
- Tarefas simples: seguir, carregar itens, iluminar, avisar sobre perigos e minérios, coletar, organizar.
- Tem **personalidade**: a tela do rosto muda de expressão, ele faz barulho de esteira e emite sons de alerta.
- A **bateria** é um recurso a gerenciar: o robô precisa voltar para carregar.

## Onde está cada coisa

| Assunto | Arquivo |
|---|---|
| Personalidade | [`_docs/concept/persona.md`](_docs/concept/persona.md) |
| Visual, expressões e sons | [`_docs/concept/visual.md`](_docs/concept/visual.md) |
| Imagens de conceito | [`_docs/assets/concept/`](_docs/assets/concept/) |
| Bateria, solar, Power Bank, estação e retorno automático | [`_docs/features/energy.md`](_docs/features/energy.md) |
| Movimento, robô ativo e local de morte | [`_docs/features/following.md`](_docs/features/following.md) |
| Inventário, ímã, filtros, baús e slots exclusivos | [`_docs/features/inventory.md`](_docs/features/inventory.md) |
| Scanners, alertas e lanterna | [`_docs/features/sensors.md`](_docs/features/sensors.md) |
| Choque e modos de defesa | [`_docs/features/combat.md`](_docs/features/combat.md) |
| Controle remoto e modo área | [`_docs/features/remote.md`](_docs/features/remote.md) |
| Itens, blocos, nomes e receitas | [`_docs/items.md`](_docs/items.md) |
| Números por tier | [`_docs/tiers.md`](_docs/tiers.md) |
| Decisões tomadas e em aberto | [`_docs/decisions.md`](_docs/decisions.md) |
| Termos e nomes oficiais | [`_docs/glossary.md`](_docs/glossary.md) |
| Fluxo de trabalho | [`_docs/development/workflow.md`](_docs/development/workflow.md) |
| Git hooks | [`_docs/development/git_hooks.md`](_docs/development/git_hooks.md) |

Os números ficam só em `_docs/tiers.md`. As outras páginas apontam para lá.

## Fases

1. **Conceito** (atual): definir persona, features e demais informações em arquivos `.md`.
2. **Versão mínima**: crafting com itens do jogo puro, seguir o player, inventário, bateria, estação de carga e virar item.
3. **Sentidos**: lanterna, sons de inimigos e minérios, e expressões na tela.
4. **Upgrades e autonomia**: tiers, ímã, filtros, slots exclusivos, choque, modos de defesa, modo área, descarregar em baú, retorno automático e local de morte.
