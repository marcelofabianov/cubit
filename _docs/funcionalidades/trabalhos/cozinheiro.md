# Cozinheiro

Conceito do trabalho de Cozinheiro, da Fase 5.2, entregue no card #197. O documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área e a [base do trabalho](../trabalhos.md#base-do-trabalho) valem aqui.

**Situação:** decidido. O comportamento base vem da [pergunta 247](../../perguntas/fase-5-trabalhos.md), o tier mínimo, o Elite, da pergunta 1145, e o refinamento do card, das perguntas 1314 a 1326. Os números ficam em [`tiers.md`](../../tiers.md#trabalhos).

## A ideia

Um Cubit que mantém a cozinha da base funcionando: leva a comida crua aos fumeiros e fornalhas, abastece o combustível e guarda a comida pronta. Usa só blocos do jogo.

## Imagem conceito

![O Cubit Cozinheiro](../../imagens/conceito/trabalhos/cozinheiro.png)

Aceita na pergunta 1328. No mod, a tigela é o item do jogo.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Cook | `cubit:ml_cook` | Card de Aprendizado de Máquina do trabalho de Cozinheiro |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Elite, e fica na aba do mod no criativo.

### Receita

Com forma, em JSON gerado por datagen, na mesma família dos outros cards (pergunta 1325):

```text
P C P
R S R
P R P
```

| Letra | Item | Quantidade |
|---|---|---|
| `P` | Papel | 4 |
| `C` | Comparador | 1 |
| `R` | Redstone | 3 |
| `S` | Fumeiro | 1 |

## Como o player o põe para trabalhar

1. O app Área marca a cozinha.
2. O app Descarga marca os baús, de onde ele tira o alimento cru e o combustível e onde guarda a comida pronta. O limite é o da Descarga do tier (pergunta 1315).
3. O app Marcar marca os fumeiros e as fornalhas, até `cook_ovens` do tier. Com o card do Cozinheiro, ele só aceita fumeiro e fornalha e avisa em outro bloco (perguntas 1316 e 1326).

Ele só usa os baús e os fornos que estão dentro da área, porque nunca sai dela.

## Como ele trabalha

- **Alimento cru:** qualquer item com receita de fumeiro, também de outros mods. Na fornalha, só os mesmos itens, para ele nunca fundir minério (pergunta 1317).
- **Fumeiro primeiro:** ele enche os fumeiros marcados antes das fornalhas, porque cozinham na metade do tempo (pergunta 1318).
- **Pôr o cru:** tira do baú da Descarga e põe pela face de cima do forno, só se a entrada estiver vazia ou com o mesmo item (pergunta 1320).
- **Combustível:** só os itens da tag `cubit:cook_fuels`, que começa com carvão, carvão vegetal e bloco de carvão e que o pack pode editar, tirados dos baús da Descarga e postos pela face do lado, só se o espaço estiver vazio ou com o mesmo combustível. Ele nunca queima madeira, ferramentas ou outros itens do player (perguntas 1319 e 1320).
- **Tirar a comida:** pela face de baixo, só o que é comida. O que o player deixou na entrada e no combustível nunca sai (pergunta 1320).
- **Guardar:** a comida pronta vai para os baús pela descarga comum, no primeiro com espaço, cheio ou pelo intervalo (pergunta 1315).
- **Experiência:** fica no forno, como no jogo, e o player a recolhe ao tirar um item com a mão (pergunta 1321).
- **Sem fogueiras:** a fogueira do jogo não tem inventário pela capability, então fica de fora (pergunta 1322).
- **Vários Cozinheiros:** cada um reserva o forno antes de ir (pergunta 1323).
- **Leitura:** varre os fornos marcados a cada `cook_scan_interval_ticks` do tier, e cada pilha movida custa `cook_action_cost`, com a pausa do tier entre uma ação e a próxima (pergunta 1326).
- **Fornalha de fundição:** fica de fora, porque não cozinha comida (pergunta 1329).
- **Faces:** o cru entra por cima, o combustível pelo lado e a comida sai por baixo, como os funis do jogo (pergunta 1329).
- **Sem nada a fazer:** espera a próxima varredura, sem hibernar (pergunta 1329). Com o inventário cheio e sem baú com espaço, hiberna, como os outros trabalhos.

## Proteção

Como o Organizador, o Cozinheiro é uma exceção à regra de que o Cubit nunca mexe em blocos com dados: só põe e tira nos fumeiros e fornalhas marcados e nos baús da Descarga, pela capability do NeoForge, sem quebrar o bloco. Cada transferência tira de um lado e põe no outro por completo (regra 13).

## Evolução por tier

Decidida na pergunta 1326, com os valores no config:

| Recurso | Elite | Ultimate |
|---|---|---|
| Cozinhar em fumeiros e fornalhas | ✔ | ✔ |
| Fornos marcados, `cook_ovens` | 4 | 8 |
| Custo por pilha movida, `cook_action_cost` | 50 FE | 50 FE |
| Pausa entre ações, `cook_action_pause_ticks` | 12 ticks | 6 ticks |
| Intervalo da varredura, `cook_scan_interval_ticks` | 100 ticks | 40 ticks |

## Visual

Só visual, sempre com o card, no padrão dos trabalhos (pergunta 1324):

- **Touca de chef:** branca e alta, cobrindo o painel solar, que continua carregando.
- **Mochila:** a mesma dos outros trabalhos, com uma concha de pau modelada saindo do topo, como o livro e pena do Organizador.
- **Tigela:** a tigela do jogo vai para a garra direita quando ele mexe num forno, e a concha some da mochila nessa hora.
- **Ícone na tela:** uma tigela pequena no canto esquerdo da tela.
- **Arte:** a imagem conceito, o ícone da tela e a textura da touca e da concha são feitos por prompt, em `tmp/arte/cozinheiro/prompt.md`.

## Contrato

| Tipo | Nome | O que guarda |
|---|---|---|
| Item | `cubit:ml_cook` | O card, na tag `cubit:ml_cards` |
| Receita | `cubit:ml_cook` | A receita do card |
| Tag de item | `cubit:cook_fuels` | Os combustíveis que ele usa, com carvão, carvão vegetal e bloco de carvão |
| NBT da entidade | `MarkedBlocks` | Os fornos marcados, na chave das marcas do app Marcar (pergunta 1327) |
| Config, por tier | `cook_ovens` | Os fornos que o tier marca |
| Config, por tier | `cook_action_cost`, `cook_action_pause_ticks` e `cook_scan_interval_ticks` | O custo por pilha movida, a pausa entre ações e o intervalo da varredura |

A reserva dos fornos fica só na memória do servidor e não vai para o save.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-19 23:04 | Fecha o conceito do Cozinheiro, com os baús da Descarga, o app Marcar, o combustível, a proteção, o visual e os números (perguntas 1314 a 1327). |
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Cozinheiro, com o comportamento da pergunta 247 e a proposta de evolução por tier. |
