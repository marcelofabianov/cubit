# Pescador

Conceito do trabalho de Pescador, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento base está decidido na [pergunta 248](../../perguntas/fase-5-trabalhos.md). O tier mínimo, o Avançado, está decidido na pergunta 1145. A evolução por tier é proposta, e os detalhes em aberto são respondidos no refinamento do card (perguntas 1153 e 1154).

## A ideia

Um Cubit que pesca sozinho numa área com água aberta, sem vara e sem boia, e guarda o que pegar. É o trabalho mais simples da lista e um dos dois primeiros da ordem de entrega, junto do Fazendeiro.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Fisher | `cubit:ml_fisher` | Card de Aprendizado de Máquina do trabalho de Pescador |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Avançado.

## Como ele trabalha

Definido na pergunta 248:

- **Onde:** na área marcada com água aberta.
- **Como:** pesca pela tabela de pesca do jogo, em intervalos do config, gastando FE, sem vara e sem boia.
- **Inventário cheio:** descarrega no baú marcado. Sem onde descarregar, hiberna.

## Evolução por tier (proposta)

O tier melhora o que o card faz (pergunta 192). Proposta no mesmo meio-termo das opções do Minerador (pergunta 202):

| Recurso | Avançado | Elite | Ultimate |
|---|---|---|---|
| Intervalo entre pescas | Base do config | Menor | Menor ainda |
| Isca (pesca mais rápido) | — | II | III |
| Sorte do Mar (mais tesouro) | — | II | III |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **O que é água aberta.** Recomendação: a mesma regra do jogo para a pesca de tesouro, num bloco de água com água ao redor. Fora dela, ele pesca só a tabela comum, como no jogo.
2. **Onde ele fica.** Recomendação: na borda da água, dentro da área. Se não houver água na área, avisa e hiberna.
3. **Opções no meio-termo.** Recomendação: Isca e Sorte do Mar como na tabela acima, com os valores no config.
4. **Vários Pescadores na mesma água.** Recomendação: cada um reserva um ponto da borda, até o limite de Cubits por área.
5. **Filtro.** Recomendação: um filtro na aba Trabalho para descartar lixo, como botas e varas velhas, que ficam no chão ou nem são guardadas.
6. **Clima.** Recomendação: a chuva acelera a pesca, como no jogo.
7. **Visual.** Recomendação: um chapéu de pescador ou uma rede na garra, pela personalização por card da Fase 5.2.
8. **Números para `tiers.md`.** O intervalo do Pescador já está em "A definir". Entram também o custo por pesca e os níveis de Isca e Sorte do Mar.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Pescador, com o comportamento da pergunta 248 e a proposta de evolução por tier. |
