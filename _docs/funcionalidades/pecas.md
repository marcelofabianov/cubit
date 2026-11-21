# Peças de montagem

O Cubit e as Power Banks são montados a partir de peças, numa cascata de receitas (issue #221, perguntas 1338 a 1351). As grades estão em [`itens.md`](../itens.md#peças-de-montagem).

## Por quê

- **O craft conta como o Cubit é feito:** carcaça, visor, esteiras e o núcleo que pensa, como na prancha de conceito.
- **O livro de receitas e o JEI mostram a árvore:** o player vê as peças antes do Cubit.
- **Ponto de integração para os packs:** uma peça pode ser sobrescrita sozinha com KubeJS, sem mexer no resto.

## Cubit

- **Quatro peças:** Cubit Casing, Cubit Visor, Cubit Treads e Cubit Neural Core (perguntas 1340 e 1349).
- **Só Overworld:** a receita final e as quatro peças usam só materiais do Overworld, porque o Cubit é o companheiro do começo do jogo. A lâmpada de redstone, que leva glowstone do Nether, sai do Cubit (pergunta 1343).
- **Montagem:** as quatro peças, sem forma, em qualquer posição da grade (pergunta 1346). O Cubit sai como hoje: tier Base, com 25% de bateria e a vida cheia.
- **Núcleo:** é o cérebro que todo Cubit tem, sem cor de tier. Não dá trabalho nenhum: o trabalho vem do card de Aprendizado de Máquina (pergunta 1340).

## Power Bank

- **Carcaça e células:** uma Power Bank Casing, igual para todos os tiers, e uma Power Cell por tier, que leva a cor dele (pergunta 1347). As células são encadeadas: cada uma leva a anterior no centro, e a Base é a mais barata, só com materiais do Overworld (pergunta 1354).
- **Do zero:** a Power Bank Casing e a célula do tier, sem forma, em qualquer tier. A Power Bank sai vazia, como hoje (pergunta 1355).
- **Subir de tier:** a mesma cruz da célula do tier, com a Power Bank anterior no centro, com o tipo `cubit:power_bank_upgrade`. A célula de dentro sobe junto, e a carga passa inteira para a nova. Se não couber, a receita não aparece, como hoje (perguntas 907 e 1355).
- **Mesmo custo:** montar do zero e subir de tier custam o mesmo, e nada é pago duas vezes (pergunta 1355).
- **As células não guardam energia:** são só peças, sem carga e sem barra.

## Regras das peças

- **Pilha:** todas empilham até 64, como os Modules.
- **Criativo:** ficam na aba `cubit:main`.
- **Uso:** nenhuma peça tem uso fora da grade de craft. O clique direito com elas não faz nada.
- **Livro de receitas:** as quatro peças do Cubit aparecem com um diamante, e o Cubit, com o Neural Core. A Power Bank Casing e a Base Power Cell aparecem com um Cubit, e cada célula seguinte, com a Power Bank anterior (pergunta 1350). A Power Bank do zero aparece com a célula do tier.
- **O que não muda:** a Toolbox, o conserto do Damaged Cubit, a recarga pela grade, os Modules, o Dock e o Tablet.

## Visual

Texturas de item 16×16, no estilo das atuais, aceitas nas perguntas 1339 e 1342. Os prompts ficam em `tmp/arte/cascata/prompt.md`.

| Peça | Como é |
|---|---|
| Cubit Casing | O corpo branco com as antenas e os ombros, e o buraco do rosto aberto |
| Cubit Visor | A tela escura com os olhos ciano e a linha laranja |
| Cubit Treads | O par de esteiras escuras ligadas pelo eixo |
| Cubit Neural Core | Um chip escuro com pinos nos quatro lados e uma rede neural em ciano |
| Power Bank Casing | A Base Power Bank vazia, sem LEDs, sem faixa e com a alça cinza |
| Power Cells | Uma célula branca com a faixa na cor do tier. A Ultimate tem o corpo de netherite e a faixa dourada |

## Contrato

| Onde | Nome | Conteúdo |
|---|---|---|
| Item | `cubit:general_robot_casing`, `cubit:general_robot_visor`, `cubit:general_robot_treads` e `cubit:general_robot_core` | As peças do Cubit |
| Item | `cubit:power_bank_casing` | A carcaça das Power Banks |
| Item | `cubit:base_power_cell`, `cubit:advanced_power_cell`, `cubit:elite_power_cell` e `cubit:ultimate_power_cell` | As células |
| Receita | Os mesmos IDs das peças | A receita de cada peça |
| Receita | `cubit:general_robot` | Passa de receita com forma para sem forma, com o mesmo ID |
| Receita | `cubit:base_power_bank` | Passa de receita com forma para sem forma, com o mesmo ID |
| Receita | `cubit:advanced_power_bank`, `cubit:elite_power_bank` e `cubit:ultimate_power_bank` | O upgrade, no mesmo tipo `cubit:power_bank_upgrade`, com a cruz |
| Receita | `cubit:advanced_power_bank_from_casing`, `cubit:elite_power_bank_from_casing` e `cubit:ultimate_power_bank_from_casing` | A Power Bank do zero, sem forma |
| Tradução | `item.cubit.{id}` | Em inglês, como na tabela de IDs, e em português: Carcaça do Cubit, Visor do Cubit, Esteiras do Cubit, Núcleo Neural do Cubit, Carcaça da Power Bank e Célula de Energia Base, Avançada, Elite e Suprema |

Nenhum save muda: as peças não têm data components nem NBT.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-21 01:45 | Encadeia as células e troca o upgrade pela cruz (perguntas 1354 e 1355). |
| Marcelo Fabiano | 2026-11-21 00:55 | Cria o conceito das peças de montagem (perguntas 1338 a 1351). |
