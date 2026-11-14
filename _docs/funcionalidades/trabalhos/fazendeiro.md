# Fazendeiro

Conceito do trabalho de Fazendeiro, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento base está decidido na [pergunta 204](../../perguntas/fase-5-trabalhos.md). O tier mínimo, o Avançado, está decidido na pergunta 1145. A evolução por tier é proposta, e os detalhes em aberto são respondidos no refinamento do card (perguntas 1153 e 1154).

## A ideia

Um Cubit que cuida de uma plantação pronta: colhe o que amadureceu e replanta. Ele não cria a fazenda, só a mantém. É um dos dois primeiros da ordem de entrega, junto do Pescador.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Farmer | `cubit:ml_farmer` | Card de Aprendizado de Máquina do trabalho de Fazendeiro |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Avançado.

## Como ele trabalha

Definido na pergunta 204:

- **Colher:** colhe o que estiver maduro, pelas tags comuns de plantações.
- **Replantar:** usa as sementes do inventário.
- **Sem semente:** deixa o bloco vazio e segue.
- **Terra nova:** não ara.
- **Inventário cheio:** descarrega no baú marcado. Sem onde descarregar, hiberna.

## Evolução por tier (proposta)

O tier melhora o que o card faz (pergunta 192).

| Recurso | Avançado | Elite | Ultimate |
|---|---|---|---|
| Colher e replantar | ✔ | ✔ | ✔ |
| Plantas de colheita especial, como cana, cacto, abóbora e melancia | — | ✔ | ✔ |
| Farinha de osso do inventário nas plantas novas | — | — | ✔ |
| Fortuna nas colheitas | — | — | II |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **Quais plantas.** Recomendação: as tags comuns de plantações com idade, como trigo, cenoura, batata, beterraba e verruga do Nether. Outros mods entram se usarem as mesmas tags.
2. **Plantas de colheita especial.** Recomendação: no Elite, colhe cana e cacto deixando o bloco de baixo, e abóbora e melancia sem quebrar o caule.
3. **Semente de qual planta.** Recomendação: replanta a mesma planta que colheu. Ele separa do que colheu as sementes que vai precisar antes de descarregar.
4. **Farinha de osso.** Recomendação: só no Ultimate e só com uma chave na aba Trabalho, porque gasta material do player.
5. **Pisotear.** Recomendação: ele nunca pisa na terra arada, para não desfazê-la, como o jogo faz com mobs.
6. **Ordem da colheita.** Recomendação: percorre a área em faixas, como uma ceifadeira, e cada Fazendeiro reserva a faixa.
7. **Visual.** Recomendação: um chapéu de palha ou uma foice na garra, pela personalização por card da Fase 5.2.
8. **Números para `tiers.md`.** Entram em "A definir": o custo por colheita e o nível de Fortuna.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Fazendeiro, com o comportamento da pergunta 204 e a proposta de evolução por tier. |
