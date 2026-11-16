# Fazendeiro

Conceito do trabalho de Fazendeiro, da Fase 5.2, entregue no card #195. O documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área e a [base do trabalho](../trabalhos.md#base-do-trabalho) valem aqui.

**Situação:** decidido. O comportamento base vem da [pergunta 204](../../perguntas/fase-5-trabalhos.md), o tier mínimo, o Avançado, da pergunta 1145, e o refinamento do card, das perguntas 1215 a 1249. Os números ficam em [`tiers.md`](../../tiers.md#trabalhos).

## A ideia

Um Cubit que cuida de uma plantação: ara a terra perto da água, planta com as sementes do baú, colhe o que amadureceu e replanta (pergunta 1231).

## Imagem conceito

![O Cubit Fazendeiro](../../imagens/conceito/trabalhos/fazendeiro.png)

Aceita na pergunta 1238. No mod, a enxada é o item do jogo, e o raminho do chapéu é o graveto do jogo, sem a folha (pergunta 1242).

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Farmer | `cubit:ml_farmer` | Card de Aprendizado de Máquina do trabalho de Fazendeiro |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Avançado, e fica na aba do mod no criativo, como o do Pescador (pergunta 1213).

### Receita

Com forma, em JSON gerado por datagen, na mesma família do card do Pescador (pergunta 1228):

```text
P C P
R H R
P R P
```

| Letra | Item | Quantidade |
|---|---|---|
| `P` | Papel | 4 |
| `C` | Comparador | 1 |
| `R` | Redstone | 3 |
| `H` | Enxada de ferro | 1 |

## Como o player o põe para trabalhar

Como o Pescador, sem app novo: o app Área marca a plantação, e o app Descarga marca o baú.

## Como ele trabalha

### Quais plantas

Qualquer planta que cresce por idade, também de outros mods (perguntas 1218 e 1222):

- **Madura:** a planta com uma propriedade `age` na idade máxima. Isso inclui as `CropBlock` do jogo e de outros mods, a verruga do Nether e as plantas que só têm a `age`.
- **Tags:** a `cubit:farmer_crops` acrescenta plantas e começa vazia, e a `cubit:farmer_ignored` tira, e começa com as mudas, o bambu, os caules de abóbora e melancia e o arbusto de bagas doces. As duas podem ser editadas pelo pack (pergunta 1240).
- **Caules:** os de abóbora e melancia ficam de fora, porque dão fruto. O fruto é uma planta de colheita especial.

### Colher e replantar

- **Ordem:** ele varre a área em intervalos, com o limite de blocos lidos por tick, e vai à planta madura mais perto. Cada Fazendeiro reserva a planta antes de ir. Sem nada maduro, espera a próxima varredura parado, sem hibernar (pergunta 1224).
- **Colheita:** o que a planta solta, pela tabela de drops do jogo, vai direto para o inventário, com uma enxada virtual que tem a Fortuna do tier (pergunta 1221).
- **Replantar:** a mesma planta. Ele separa da colheita a semente desse bloco antes de guardar o resto e, sem ela, usa a do inventário. Cenoura e batata usam o próprio item. Sem semente nenhuma, deixa o bloco vazio e segue (perguntas 204 e 1220).
- **Terra arada:** o Cubit com o card do Fazendeiro nunca a pisoteia. Os outros seguem o jogo (pergunta 1223).
- **Custo:** um valor de FE por planta colhida, maior nos tiers altos, que também trabalham mais rápido, com uma pausa menor entre uma colheita e a próxima (perguntas 1225 e 1226).
- **Inventário cheio:** como no Pescador, espera sem colher até descarregar no baú marcado. Sem onde descarregar, hiberna pelo inventário cheio, como na base.
- **Na área:** ele não busca os itens do chão nem faz a ronda, como o Pescador (pergunta 1209).

### Arar e plantar

Desde o Avançado (perguntas 1231 a 1236), mudando a pergunta 204, que dizia que ele não arava:

- **Arar:** ele ara o que a enxada do jogo transforma em terra arada, como grama, terra e caminho de terra, também de outros mods, com uma enxada virtual. Só ara com ar em cima e só com água a até 4 blocos, para a terra não secar, e só mexe nos blocos que a enxada transforma, que não guardam dados. A tag de blocos protegidos fica para o primeiro card que puder quebrar blocos com dados (pergunta 1239).
- **Sementes do baú:** ele pega as sementes nos baús marcados com o app Descarga que estão dentro da área. Quando falta semente para a terra arada vazia, vai ao baú e leva até uma pilha. A sobra das sementes da colheita vai para o baú na descarga, e ele as busca de volta.
- **Que semente:** a mesma planta de um bloco vizinho, para formar canteiros. Sem vizinho plantado, usa a semente que tem em maior quantidade. Se faltar a semente do vizinho, usa outra que tiver. Vale qualquer semente, também de outros mods, e mais de um tipo na mesma área.
- **Sem semente:** sem semente no inventário nem no baú, deixa a terra vazia e segue, sem hibernar.
- **Custo:** arar um bloco custa o mesmo que colher uma planta no tier, e plantar não custa nada a mais. A pausa entre uma ação e a próxima é a mesma da colheita.

### Plantas de colheita especial

Do Elite em diante (pergunta 1219):

- **Cana e cacto:** colhe acima do bloco de baixo e deixa esse bloco.
- **Abóbora e melancia:** colhe o fruto e deixa o caule.
- Nenhuma delas precisa replantar.

### Fertilizante

Desde o Avançado, por um slot exclusivo, no lugar da chave `Use bone meal`, que saiu, e sem chave para ligar e desligar (perguntas 1243 a 1249). O que diferencia os tiers é a pausa entre ações (pergunta 1248):

- **Slot:** na fileira de slots exclusivos, depois do slot do card. Só aparece com o card do Fazendeiro e fica apagado, com o tier que o libera, num tier em que o config não libera o fertilizante. Fica no item do Cubit, também no Damaged Cubit, e no NBT.
- **Aceita:** qualquer item da tag comum `c:fertilizers`, que o NeoForge traz com a farinha de osso e onde outros mods põem os fertilizantes deles.
- **Efeito:** todo fertilizante age como a farinha de osso do jogo, uma vez em cada planta que ele acabou de plantar, e gasta um item. Um efeito especial de outro mod não acontece pelo Cubit. Não custa FE, só o item.
- **Abastecer:** com o slot vazio e nada a fazer, ele busca fertilizante num baú da Descarga dentro da área e enche o slot.

## Evolução por tier

Decidida nas perguntas 1221 e 1226, com os valores no config:

| Recurso | Avançado | Elite | Ultimate |
|---|---|---|---|
| Colher e replantar | ✔ | ✔ | ✔ |
| Arar e plantar com as sementes do baú | ✔ | ✔ | ✔ |
| Plantas de colheita especial | — | ✔ | ✔ |
| Slot de fertilizante | ✔ | ✔ | ✔ |
| Fortuna | — | — | II |
| Custo por planta colhida ou bloco arado | 50 FE | 75 FE | 100 FE |
| Pausa entre colheitas | 20 ticks | 12 ticks | 6 ticks |

## Visual

Só visual, sempre com o card, como o do Pescador (perguntas 1216 e 1229):

- **Mochila:** a mesma do Pescador, com a enxada de ferro do jogo no lugar da vara.
- **Enxada:** na hora de colher, ela passa para a garra direita, e ele ergue o braço, como na pesca.
- **Chapéu de palha:** cobre o painel solar, que continua carregando, e leva o graveto do jogo espetado, como na imagem conceito, sem a folha (pergunta 1242).
- **Ícone na tela:** um trigo pequeno no canto esquerdo da tela, como o peixe do Pescador.
- **Arte:** o modelo do chapéu de palha, a textura e o ícone são feitos por prompt, em `tmp/arte/fazendeiro/prompt.md`, com uma imagem conceito.

## Contrato

| Tipo | Nome | O que guarda |
|---|---|---|
| Item | `cubit:ml_farmer` | O card, na tag `cubit:ml_cards` |
| Receita | `cubit:ml_farmer` | A receita do card |
| Tag de bloco | `cubit:farmer_crops` | Plantas que o Fazendeiro colhe além das que crescem por idade |
| Tag de bloco | `cubit:farmer_ignored` | Plantas que ele nunca colhe, como as mudas e o bambu |
| Componente do item | `cubit:fertilizer_slot` | O fertilizante do slot, também no Damaged Cubit (pergunta 1247) |
| NBT da entidade | `FertilizerSlot` | O fertilizante do slot, como o `CardSlot` |
| Config, por tier | `farmer_action_cost` e `farmer_action_pause_ticks` | O custo por planta colhida ou bloco arado e a pausa entre ações |
| Config, por tier | `farmer_special_crops` e `farmer_bone_meal` | Se o tier colhe as plantas especiais e se libera o slot de fertilizante |
| Config, por tier | `farmer_fortune_level` | A Fortuna da enxada virtual, de 0 a 3 |
| Config, `general` | `farmer_scan_interval_ticks` | O intervalo da varredura da plantação, com 40 |

A reserva das plantas fica só na memória do servidor e não vai para o save.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-15 13:45 | Libera o fertilizante desde o Avançado e confirma que não há chave (perguntas 1248 e 1249). |
| Marcelo Fabiano | 2026-11-15 13:25 | Troca a chave `Use bone meal` pelo slot de fertilizante, que aceita a tag `c:fertilizers` e se abastece no baú (perguntas 1243 a 1247). |
| Marcelo Fabiano | 2026-11-15 12:45 | Acrescenta o graveto no chapéu de palha (pergunta 1242). |
| Marcelo Fabiano | 2026-11-15 12:30 | Registra as tags iniciais e a tag de blocos protegidos para depois (perguntas 1239 e 1240). |
| Marcelo Fabiano | 2026-11-15 12:05 | Acrescenta a imagem conceito (pergunta 1238). |
| Marcelo Fabiano | 2026-11-15 10:10 | Fecha o refinamento do card #195, com as perguntas 1215 a 1236: arar e plantar com as sementes do baú, as plantas, a colheita, a semente, as especiais, a farinha de osso, a evolução por tier, a receita, o visual e o contrato. |
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Fazendeiro, com o comportamento da pergunta 204 e a proposta de evolução por tier. |
