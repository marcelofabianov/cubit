# Organizador

Conceito do trabalho de Organizador, da Fase 5.2, entregue no card #196. O documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área e a [base do trabalho](../trabalhos.md#base-do-trabalho) valem aqui.

**Situação:** decidido. O comportamento base vem das [perguntas 208, 212, 215, 249 e 250](../../perguntas/fase-5-trabalhos.md), o tier mínimo, o Elite, da pergunta 1145, e o refinamento do card, das perguntas 1285 a 1300. Os números ficam em [`tiers.md`](../../tiers.md#trabalhos).

## A ideia

Um Cubit que mantém a base arrumada: recolhe o que as máquinas produzem e o que está no chão e guarda junto dos itens iguais. Também abastece lugares com quantidades fixas, o que antes seria o Fabricador.

## Imagem conceito

A imagem é gerada pelo prompt em `tmp/arte/organizador/prompt.md` e entra em `_docs/imagens/conceito/trabalhos/organizador.png` depois de aceita pelo desenvolvedor (pergunta 1298).

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Organizer | `cubit:ml_organizer` | Card de Aprendizado de Máquina do trabalho de Organizador |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Elite, e fica na aba do mod no criativo, como os outros cards.

### Receita

Com forma, em JSON gerado por datagen, na mesma família dos cards do Pescador e do Fazendeiro (pergunta 1299):

```text
P C P
R F R
P R P
```

| Letra | Item | Quantidade |
|---|---|---|
| `P` | Papel | 4 |
| `C` | Comparador | 1 |
| `R` | Redstone | 3 |
| `F` | Funil | 1 |

## Como o player o põe para trabalhar

1. O app Área marca a área, como nos outros trabalhos.
2. O app Descarga marca os baús onde ele guarda e de onde ele tira. Com o card, o limite de baús é o `organizer_chests` do tier, no lugar do `unload_chests` (pergunta 1287).
3. O app Marcar marca as máquinas, no modo organizar, ou os destinos, no modo abastecer (perguntas 1288 e 1297).
4. Na aba Trabalho, o player escolhe o modo e, no abastecer, monta os pedidos (perguntas 1290 e 1295).

Ele só usa os baús, as máquinas e os destinos que estão dentro da área, porque nunca sai dela.

## Modo

Um por vez, na linha `Mode` / `Modo` da aba Trabalho, entre `Organize` / `Organizar` e `Supply` / `Abastecer`. Começa em organizar e fica no item. Para fazer os dois, o player usa dois Cubits (pergunta 1290).

## Modo organizar

- **Recolher:** pega os itens do chão da área e tira o que as máquinas marcadas oferecem pela face de baixo, como um funil embaixo delas. No forno do jogo, isso é o resultado e o balde vazio. Vale para máquinas de outros mods que oferecem saída pela capability do NeoForge (perguntas 208, 215 e 1289).
- **Guardar:** põe cada item no baú marcado que tem mais do mesmo item. Mesmo item é o mesmo item com os mesmos dados, a regra do jogo para empilhar, então um item encantado, uma poção ou um item com nome não se misturam com o comum (perguntas 208 e 1293).
- **Sem baú com o item:** usa o primeiro baú com espaço, na ordem em que foi marcado. Para mudar a ordem, o player desmarca e marca de novo (perguntas 212 e 1291).
- **Sem lugar:** fica com o item no inventário, avisa uma vez com `Cubit: No room for some items` / `Cubit: Não há lugar para alguns itens` e continua com o que cabe. Com o inventário cheio, hiberna com `Hibernating · Inventory full` (pergunta 1292).
- **Leitura:** varre as máquinas e o chão a cada `organizer_scan_interval_ticks` do tier, com o limite de blocos lidos por tick (pergunta 215 e regra 21).

## Modo abastecer

- **Pedido:** cada destino marcado é um pedido, com o item, com os dados, e a quantidade, de 1 a 64. Na aba Trabalho, cada destino vira uma linha com a posição, um slot fantasma para o item e − e + para a quantidade, que andam de 1 em 1 e, com Shift, de 8 em 8 (perguntas 1294, 1295 e 1300).
- **Face:** o destino guarda a face em que o player clicou ao marcar, e o Cubit põe o item por ela, como o jogo faz ao apontar um funil. Na fornalha, a face de cima abastece a entrada, e a do lado, o combustível (pergunta 1296).
- **Atender:** a cada varredura, ele conta o item no destino pela face. Se faltar, tira de qualquer baú da Descarga que tenha o item e completa até a quantidade, sem passar dela (perguntas 249 e 1294).
- **Sem o item nos baús:** o pedido espera a próxima varredura, sem hibernar.
- **Sem item no pedido:** a linha fica vazia, e o destino não recebe nada.

## Proteção

Só o Organizador mexe em blocos com dados, e só assim: tira pela face de baixo das máquinas marcadas, põe e tira nos baús da Descarga e põe nos destinos pela face marcada, sempre pela capability do NeoForge. Ele nunca quebra o bloco nem mexe na entrada ou no combustível de uma máquina marcada para organizar (pergunta 1289). Cada transferência tira de um lado e põe no outro por completo, sem criar nem perder itens (regra 13).

## Evolução por tier

Decidida na pergunta 1300, com os valores no config:

| Recurso | Elite | Ultimate |
|---|---|---|
| Modo organizar e modo abastecer | ✔ | ✔ |
| Baús marcados, `organizer_chests` | 9 | 18 |
| Máquinas ou destinos marcados, `organizer_marks` | 8 | 16 |
| Custo por pilha movida, `organizer_action_cost` | 50 FE | 50 FE |
| Pausa entre ações, `organizer_action_pause_ticks` | 12 ticks | 6 ticks |
| Intervalo da varredura, `organizer_scan_interval_ticks` | 100 ticks | 40 ticks |

## Visual

Só visual, sempre com o card, como o do Pescador e o do Fazendeiro (pergunta 1298):

- **Caixa:** uma caixa de papelão nas costas, no lugar da mochila.
- **Prancheta:** na garra direita, que ele ergue ao tirar ou pôr um item, como a vara na pesca.
- **Ícone na tela:** uma caixa pequena no canto esquerdo da tela, como o peixe do Pescador.
- **Arte:** o modelo da caixa e da prancheta, a textura e o ícone são feitos por prompt, em `tmp/arte/organizador/prompt.md`, com a imagem conceito.

## Contrato

| Tipo | Nome | O que guarda |
|---|---|---|
| Item | `cubit:ml_organizer` | O card, na tag `cubit:ml_cards` |
| Receita | `cubit:ml_organizer` | A receita do card |
| Componente do item | campo `organizer_mode` do `cubit:robot_state` | O modo, `organize` ou `supply`, com `organize` quando falta |
| NBT da entidade | `OrganizerMode` | O modo |
| NBT da entidade | `OrganizerMarks` | As máquinas ou os destinos, em ordem: `Block`, a posição num `long`, `Dimension`, `Face` e, no destino, o item e a quantidade do pedido. Sem a chave, o Cubit não tem marcas |
| Pacote do servidor | `cubit:organizer_marks` | As marcas dos Cubits do player, para as setas, a cada segundo com o Tablet na mão |
| Config, por tier | `organizer_chests` e `organizer_marks` | Os baús e as marcas do tier |
| Config, por tier | `organizer_action_cost`, `organizer_action_pause_ticks` e `organizer_scan_interval_ticks` | O custo por pilha movida, a pausa entre ações e o intervalo da varredura |

As marcas não vão para o item: recolher o Cubit as apaga, como a área e os baús (perguntas 343 e 1297).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-19 21:34 | Fecha o conceito do Organizador, com o app Marcar, os pedidos, a exceção à proteção e os números (perguntas 1285 a 1300). |
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Organizador, com os modos das perguntas 208 a 250 e a proposta para fechar a pergunta 208. |
