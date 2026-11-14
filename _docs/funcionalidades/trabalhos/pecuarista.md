# Pecuarista

Conceito do trabalho de Pecuarista, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento base está decidido na [pergunta 205](../../perguntas/fase-5-trabalhos.md). O tier mínimo, o Avançado, está decidido na pergunta 1145. A evolução por tier é proposta, e os detalhes em aberto são respondidos no refinamento do card (perguntas 1153 e 1154).

## A ideia

Um Cubit que cuida dos animais de um curral: alimenta, tosquia, recolhe ovos e controla o tamanho do rebanho. É o único card que pode ferir mobs pacíficos, e só dentro da área.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Rancher | `cubit:ml_rancher` | Card de Aprendizado de Máquina do trabalho de Pecuarista |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Avançado.

## Como ele trabalha

Definido na pergunta 205:

- **Alimentar:** usa os itens do inventário, o que faz os animais se reproduzirem.
- **Tosquiar:** tosquia as ovelhas.
- **Ovos:** recolhe os ovos do chão.
- **Abate:** abate só acima de um limite de animais por espécie, no config, poupando filhotes.
- **Exceção:** a regra de nunca atacar mobs pacíficos tem exceção só para este card e só na área.

## Evolução por tier (proposta)

O tier melhora o que o card faz (pergunta 192). Todas as funções da pergunta 205 ficam disponíveis desde o Avançado.

| Recurso | Avançado | Elite | Ultimate |
|---|---|---|---|
| Alimentar, tosquiar, ovos e abate | ✔ | ✔ | ✔ |
| Ordenhar vacas com baldes do inventário | — | ✔ | ✔ |
| Pilhagem no abate | — | — | II |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **Limite por espécie.** Recomendação: o config define o padrão, e a aba Trabalho permite um limite menor por espécie, nunca maior.
2. **Alimentar só abaixo do limite.** Recomendação: ele só alimenta para reproduzir quando a espécie está abaixo do limite, para não gastar comida à toa.
3. **Quais espécies.** Recomendação: as que o jogo permite reproduzir. Animais domesticados, com dono, como cães, gatos e cavalos, nunca são abatidos nem alimentados.
4. **Animais com nome.** Recomendação: um animal com etiqueta de nome nunca é abatido.
5. **Escolha do abate.** Recomendação: abate os adultos mais velhos primeiro, sem nunca baixar do limite.
6. **Drops.** Recomendação: guarda carne, couro e lã no inventário e descarrega no baú marcado.
7. **Ordenhar.** Recomendação: no Elite, com baldes vazios do inventário.
8. **Visual.** Recomendação: um chapéu de vaqueiro ou um balde na garra, pela personalização por card da Fase 5.2.
9. **Números para `tiers.md`.** O limite de animais já está em "A definir". Entram também o custo por ação e o nível de Pilhagem.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Pecuarista, com o comportamento da pergunta 205 e a proposta de evolução por tier. |
