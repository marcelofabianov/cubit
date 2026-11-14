# Cozinheiro

Conceito do trabalho de Cozinheiro, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento base está decidido na [pergunta 247](../../perguntas/fase-5-trabalhos.md). O tier mínimo, o Elite, está decidido na pergunta 1145. A evolução por tier é proposta, e os detalhes em aberto são respondidos no refinamento do card (perguntas 1153 e 1154).

## A ideia

Um Cubit que mantém a cozinha da base funcionando: leva a comida crua aos fumeiros e fornalhas, abastece o combustível e guarda a comida pronta. Usa só blocos do jogo. O Alquimista segue o mesmo molde, com uma cadeia de várias etapas.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Cook | `cubit:ml_cook` | Card de Aprendizado de Máquina do trabalho de Cozinheiro |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Elite.

## Blocos que ele usa

- **Baús de entrada marcados:** de onde tira os alimentos crus.
- **Fumeiros e fornalhas marcados:** onde cozinha, mexendo pela capability de itens do NeoForge.
- **Baú de saída marcado:** onde guarda a comida pronta.

Marcar os fumeiros e as fornalhas precisa de um app do Tablet, que o primeiro card que precisar dele decide (pergunta 1155).

## Como ele trabalha

Definido na pergunta 247:

1. Leva os alimentos crus dos baús marcados aos fumeiros e fornalhas marcados.
2. Abastece o combustível com o que tem no inventário.
3. Leva a comida pronta a um baú marcado.

## Evolução por tier (proposta)

O tier melhora o que o card faz (pergunta 192).

| Recurso | Elite | Ultimate |
|---|---|---|
| Cozinhar em fumeiros e fornalhas | ✔ | ✔ |
| Fogueiras marcadas, sem combustível | — | ✔ |
| Fornos marcados por Cozinheiro | Base do config | O dobro |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **O que é alimento cru.** Recomendação: qualquer item com receita de fumeiro, porque o fumeiro só aceita comida. Na fornalha, só os mesmos itens, para ele não fundir minério.
2. **Preferência de forno.** Recomendação: usa primeiro os fumeiros, que cozinham na metade do tempo, e depois as fornalhas.
3. **Combustível.** Recomendação: do inventário, mas também de um baú de combustível marcado, com um filtro na aba Trabalho para o player escolher o que pode queimar.
4. **Itens deixados pelo player no forno.** Recomendação: só usa forno vazio, ou com o que ele mesmo colocou, como no Alquimista.
5. **Experiência.** Recomendação: a experiência dos fornos fica guardada neles, como no jogo, para o player recolher.
6. **Fogueiras.** Recomendação: no Ultimate, cozinha também em fogueiras marcadas, que não gastam combustível.
7. **Vários Cozinheiros.** Recomendação: cada um reserva o forno antes de usar.
8. **Visual.** Recomendação: um chapéu de cozinheiro, pela personalização por card da Fase 5.2.
9. **Números para `tiers.md`.** Entram em "A definir": o custo por ação e o número de fornos por tier.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Cozinheiro, com o comportamento da pergunta 247 e a proposta de evolução por tier. |
