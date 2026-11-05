# Roteiro manual

O que está fora de `domain/` se cobre com GameTests e com este roteiro (regra 34 do `AGENTS.md`). Ele fica com o que uma GameTest não enxerga, como o visual e os cliques de verdade.

Os números citados são os valores padrão de [`tiers.md`](../tiers.md), que prevalece (pergunta 150).

## Preparar

1. Rode `make client`.
2. Crie um mundo novo, no modo criativo e com comandos ativos.

## Colocar e recolher

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba **Cubit** do inventário criativo | A aba aparece com o ícone do robô, e o item se chama `Cubit` |
| 2 | Olhe o item na hotbar | Barra verde com cerca de um quarto do tamanho (carga de 25%) |
| 3 | Clique com o botão direito num bloco | O robô aparece em cima do bloco, de frente para você, e o item some da mão |
| 4 | Olhe o robô, de frente | Carcaça clara e arredondada, tela escura com moldura e dois olhos ciano separados, barra e botão laranja embaixo, à direita de quem olha, colar cinza, dois braços com garras e duas esteiras |
| 4a | Olhe a lateral direita do robô | Bobina de cobre em cima, atrás do ombro, e esteira com três rodas |
| 4b | Olhe a lateral esquerda do robô | Painel de ventilação em cima, atrás do ombro, e esteira com três rodas |
| 4c | Olhe o robô, por trás | Painel com a bateria verde e duas linhas de ventilação abaixo |
| 4d | Olhe de cima, voando | Painel solar azul e duas antenas com ponta laranja, inclinadas para fora e para a frente |
| 4e | Olhe por baixo | Bobina de cobre entre as duas esteiras |
| 4f | Olhe o item na hotbar | Ícone do Cubit de frente, com a tela e os olhos |
| 4g | Abra a tela de Mods e selecione o Cubit | O mesmo desenho do ícone do item aparece menor que a área do logo, sem borrão |
| 5 | Clique com o botão direito num bloco sem espaço acima, com outro item do Cubit | Nada acontece, e o item continua na mão |
| 6 | Shift + botão direito no robô | O robô some, e o item volta com a mesma barra |
| 7 | Coloque esse item de novo | O robô volta |

## Vários robôs

Para conferir o estado, use `/data get entity @e[type=cubit:general_robot,limit=1,sort=nearest] PowerState`.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque um primeiro robô | `PowerState` é `ACTIVE` |
| 2 | Coloque um segundo robô e confira perto dele | `PowerState` é `INACTIVE_SWITCHED_OFF` |
| 3 | Botão direito no segundo robô e confira os dois | O segundo fica `ACTIVE`, e o primeiro, `INACTIVE_SWITCHED_OFF` |
| 4 | Saia e entre de novo no mundo, e confira os dois | Os estados continuam os mesmos |

## Movimento

Coloque um robô, que segue você, e use blocos para montar o terreno.

| # | Faça | Espere |
|---|---|---|
| 1 | Ande em linha reta | O robô vem atrás e para a cerca de 3 blocos, sem empurrar você |
| 2 | Corra para longe, em modo sobrevivência, ou voe para longe | A cerca de 20 blocos, ele aparece perto de você |
| 3 | Suba um degrau de um bloco | O robô sobe sem pular |
| 4 | Fique atrás de um muro de dois blocos, a menos de 20 blocos | O robô não passa e não pula, mas contorna se houver caminho |
| 5 | Fique do outro lado de uma poça, com um caminho em volta | O robô contorna a água |
| 6 | Coloque o robô dentro de uma piscina funda e fique por perto | Ele afunda, anda no fundo e não perde vida |
| 7 | Deixe o robô num penhasco e pule, para ele cair ao seguir | Ele não perde vida |
| 7a | Fique do outro lado de uma vala de lava, sem caminho em volta | Ele não entra na lava |
| 8 | Vá para o Nether e volte | Ele não vai junto. Ao voltar, retoma quando o chunk dele estiver carregado |
| 9 | Coloque um segundo robô, longe do primeiro, e ande | O segundo fica parado |

## Em chamas

Use um isqueiro no robô, com `/give @s minecraft:flint_and_steel`. O fogo dura poucos segundos, então repita se precisar.

| # | Faça | Espere |
|---|---|---|
| 1 | Ponha fogo no robô, sem água por perto, e afaste-se | Ele anda ao acaso, sem seguir você e sem teleportar |
| 2 | Ponha fogo no robô com uma poça a menos de 8 blocos | Ele vai até a água e se apaga |
| 2a | Ponha fogo no robô com água a menos de 8 blocos, subindo ou descendo degraus | Ele sobe ou desce os degraus até a água e se apaga |
| 2b | Ponha fogo no robô com água atrás de uma parede, sem caminho até ela | Ele anda ao acaso, em vez de ficar parado |
| 3 | Espere o fogo apagar | Ele volta a seguir você na hora |
| 4 | Ponha fogo perto de uma vala de lava | Ele não entra na lava |
| 5 | Ponha fogo num segundo robô, desligado | Ele queima parado |

## Blocos que ferem

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque um bloco de magma embaixo do robô ativo | Ele sai para o bloco seguro mais próximo, sem pegar fogo, e volta a seguir |
| 2 | Pare perto de uma área de magma, com o robô seguindo | Ele não fica parado em cima do magma |
| 3 | Encoste um cacto no robô, com areia embaixo do cacto | Ele se afasta, e a vida para de cair |
| 4 | Plante uma rosa do wither no bloco do robô | Ele sai de cima da rosa |
| 5 | Coloque um magma ao lado do robô, sem encostar | Ele não reage |
| 6 | Coloque um magma embaixo de um robô desligado | Ele fica parado e leva dano |

## Lava

Use um balde de lava, com `/give @s minecraft:lava_bucket`. Ele tem 25 de vida e morre em cerca de 3 s na lava. Se morrer, vira um `Damaged Cubit`, que não queima.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque lava no bloco do robô ativo | Ele se move devagar rumo à borda, e pode morrer antes de sair |
| 2 | Coloque lava no bloco de um robô desligado | Ele fica parado e morre |

## Inventário

| # | Faça | Espere |
|---|---|---|
| 1 | Botão direito no robô ativo | Abre uma tela de baú com o nome do robô e 27 slots |
| 2 | Guarde itens em slots variados e feche | Os itens continuam nos mesmos slots ao abrir de novo |
| 3 | Tente guardar um Cubit, um `Damaged Cubit` e uma shulker box, com clique e Shift + clique | Nenhum deles entra |
| 4 | Shift + botão direito para recolher, e coloque de novo | Os itens estão nos mesmos slots |
| 5 | Mate o robô, conserte e coloque. Ele fica sem carga: botão direito com redstone na mão para ele religar | Os itens continuam lá |
| 6 | Saia e entre no mundo | Os itens continuam lá |
| 7 | Com a tela aberta, afaste-se com comando ou mate o robô com outro comando | A tela fecha, e nenhum item é duplicado |
| 8 | Botão direito num robô desligado com carga | Ele liga, sem abrir a tela |

## Energia

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque um robô e abra a tela | Painel à esquerda, com o slot de energia e uma barra verde de cerca de um quarto |
| 2 | Ande por um tempo com o robô seguindo | A barra da tela cai devagar e a do item, ao recolher, também |
| 3 | Guarde redstone e bloco de redstone no slot, com clique e Shift + clique, e tente outro item | Só a redstone entra, e o item some aos poucos, com a barra subindo |
| 4 | Clique no painel com um item na mão, fora do slot | O item não cai no chão |
| 5 | Tire tudo do slot e aguarde a barra parar de subir. Rode `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Energy:0L}` e afaste-se | Ele para, sem abrir a tela nem ligar com um clique |
| 5a | Com carga baixa, olhe as barras da tela e do item | As duas mostram ao menos um risco enquanto houver carga, e só ficam vazias com 0 FE |
| 6 | Clique com redstone na mão no robô sem bateria | A pilha entra no slot e, com 5%, ele volta a seguir você |
| 7 | Com a recarga em andamento, recolha e coloque de novo | O slot e a carga continuam, sem perder redstone |

## Consumo por situação

Para conferir a carga, use `/data get entity @e[type=cubit:general_robot,limit=1,sort=nearest] Energy`. Os valores são os do tier Base, com o slot de energia vazio.

| # | Faça | Espere |
|---|---|---|
| 1 | Ande em terreno plano por 10 s e compare a carga | Cai cerca de 3.000 FE (15 FE/t) |
| 2 | Ocupe todos os slots do inventário e repita | Cai mais rápido, cerca de 20 FE/t, e o robô anda visivelmente mais devagar |
| 3 | Ponha o robô numa piscina e ande por perto | Cai cerca de 20 FE/t, ou 25 FE/t com o inventário cheio |
| 4 | Suba uma escada de 10 degraus com ele | Cai cerca de 150 FE a mais que no plano |
| 5 | Desça a mesma escada | Só o gasto de andar, sem devolver energia |
| 6 | Use `/effect give @e[type=cubit:general_robot,limit=1,sort=nearest] minecraft:poison 10` com ele parado | Cai cerca de 25 FE/t, mesmo parado |
| 7 | Ponha fogo nele, com o inventário cheio | Ele foge na velocidade normal de chamas, e a carga cai mais rápido |
| 8 | Avalie o peso, a água e a subida | Os efeitos são sentidos. Se não forem, ajuste os números |

## Induction Dock

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba **Cubit** e coloque um `Induction Dock`. Compare com a prancha `induction-dock-prancha.png`, nos seis lados | Torre com tela, com a frente virada para você, e placa em U na frente. Com o bloco de cima ocupado, ele não é colocado |
| 1a | Olhe a tela sem energia, depois dê redstone | Tela apagada. Com energia, círculo ciano e LEDs ciano. No escuro, os LEDs continuam acesos |
| 2 | Clique com redstone e com bloco de redstone na mão | A pilha entra no slot e vira energia, um item por vez |
| 3 | Shift + botão direito, com a mão vazia | Abre a tela com o nome do bloco, o slot, a barra de energia e o inventário |
| 4 | Guarde outro item no slot, com clique e Shift + clique | Só a redstone entra |
| 5 | Encha o estoque com redstone | A barra enche, e o item que não couber inteiro fica no slot |
| 6 | Coloque um funil em cima, apontando para o Dock, com redstone | A redstone entra no Dock |
| 7 | Em modo sobrevivência, quebre o Dock com energia, com outro item no slot | Cai o item do Dock, que devolve a energia ao ser colocado, e o item do slot |
| 8 | Explode um TNT ao lado | O Dock continua lá |
| 9 | Num servidor com outro player, tente usar e quebrar o Dock | Ele não abre a tela e não quebra. No modo criativo com permissão de operador, quebra |

## Cubit na base

Use `/data get entity @e[type=cubit:general_robot,limit=1,sort=nearest] PowerState` para conferir o estado.

| # | Faça | Espere |
|---|---|---|
| 1 | Com um Cubit sem carga na mão, clique no Dock | Ele aparece em cima da placa, de costas para a torre, com `HIBERNATING`. Na tela do Dock, carregando. Sem energia no estoque, sem energia |
| 2 | Dê redstone ao Dock | O Cubit carrega, e a tela mostra as barras verdes, e a fita da frente da placa fica verde |
| 3 | Clique no Cubit na base, depois Shift + clique | Abre o inventário. Depois, ele é recolhido |
| 4 | Coloque de novo, com vida baixa (`/data merge entity ... {Health:5f}`) | A vida sobe, cerca de 1 min até encher, mesmo sem energia |
| 5 | Com ele cheio | A tela mostra completa por cerca de 3 s, e ele sai e volta a seguir |
| 6 | Com um Cubit seguindo a até 15 blocos, clique no Dock | Ele anda até o Dock, sobe na placa e hiberna |
| 7 | Clique no Dock com o Cubit na base | Ele é liberado e volta a seguir |
| 8 | Coloque-o atrás de uma parede, sem rota, e clique no Dock | Depois de cerca de 5 s preso, ele aparece no Dock |
| 9 | Chame com o Dock ocupado | Ele para perto e aguarda. Quando o Dock libera, entra. Com outro Dock livre no raio, vai até ele |
| 10 | Com outro Cubit seguindo, deixe o da base sair | O que sai fica parado e desligado, na frente do Dock |
| 11 | Quebre o Dock com o Cubit na base | Ele é liberado, sem perder nada |
| 12 | Saia e entre no mundo com o Cubit na base | Ele continua hibernando |

## Morte e conserto

Use `/kill @e[type=cubit:general_robot,limit=1,sort=nearest]` para matar o robô.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba **Cubit** do inventário criativo | Aparecem o `Damaged Cubit` e a `Toolbox`, cada um com o seu ícone |
| 2 | Mate um robô com o comando | Cai um `Damaged Cubit` no lugar dele, e um robô novo colocado depois passa a seguir você |
| 3 | Em modo sobrevivência, bata no próprio robô | Ele não perde vida nem recua |
| 4 | Clique com o `Damaged Cubit` num bloco | Nada é colocado, e o item continua na mão |
| 5 | Tente guardar o `Damaged Cubit` numa shulker box | Ele não entra |
| 6 | Jogue o `Damaged Cubit` na lava e no fogo | Ele não queima |
| 7 | Solte o `Damaged Cubit` e explode um TNT ao lado | Ele continua no chão |
| 8 | Faça a `Toolbox` na mesa de craft: colunas laterais com 3 ferros, e coluna central com ferro, redstone e obsidiana | Sai uma `Toolbox`, que empilha até 16 |
| 9 | Combine `Damaged Cubit` e `Toolbox` na grade do inventário, nas duas posições | Sai um `Cubit`, e a `Toolbox` é consumida |
| 10 | Repita na mesa de craft, em posições afastadas | Sai um `Cubit` da mesma forma |
| 11 | Coloque o `Cubit` consertado | Barra sem carga, com o robô desligado e com 25% da vida (`/data get entity @e[type=cubit:general_robot,limit=1,sort=nearest] Health`) |
| 12 | Mate um robô sobre um buraco até o vazio, para o item cair | O item volta para onde o robô esteve por último no chão |
| 13 | Saia e entre no mundo com um robô morto antes, e coloque outro | O novo segue você normalmente |

## Servidor dedicado

1. Rode `make server` e aceite o EULA em `run/eula.txt`, se ainda não tiver aceitado.
2. Espere a mensagem `Done`, e confirme que o servidor abriu sem erro de classe de cliente.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 17:35 | Tira o brilho verde do roteiro do Dock. |
| Marcelo Fabiano | 2026-11-05 17:15 | Adiciona o Cubit na base. |
| Marcelo Fabiano | 2026-11-05 16:57 | Adota a aparência da prancha no roteiro do Dock. |
| Marcelo Fabiano | 2026-11-05 16:22 | Adiciona o Induction Dock como máquina. |
| Marcelo Fabiano | 2026-11-05 15:32 | Adiciona os blocos que ferem. |
| Marcelo Fabiano | 2026-11-05 14:40 | Adiciona o consumo por situação. |
| Marcelo Fabiano | 2026-11-05 13:54 | Zera a bateria por comando, com o slot vazio, e adiciona o mínimo visível das barras. |
| Marcelo Fabiano | 2026-11-05 12:56 | Adiciona a energia e troca o comando temporário pela redstone. |
| Marcelo Fabiano | 2026-11-05 02:31 | Adiciona o comando para dar carga ao robô consertado. |
| Marcelo Fabiano | 2026-11-05 02:11 | Adiciona o inventário. |
| Marcelo Fabiano | 2026-11-05 01:23 | Adiciona a morte e o conserto. |
| Marcelo Fabiano | 2026-11-05 00:55 | Ajusta a conferência do ícone do mod. |
| Marcelo Fabiano | 2026-11-05 00:35 | Adiciona a conferência do ícone do mod. |
| Marcelo Fabiano | 2026-11-05 00:32 | Atualiza o visual esperado do robô com o novo modelo. |
| Marcelo Fabiano | 2026-11-05 00:12 | Avisa que os números citados são os padrões de tiers.md. |
| Marcelo Fabiano | 2026-11-04 23:27 | Adiciona a saída da lava. |
| Marcelo Fabiano | 2026-11-04 21:29 | Adiciona a água em outra altura e sem rota. |
| Marcelo Fabiano | 2026-11-04 21:02 | Adiciona o comportamento em chamas. |
| Marcelo Fabiano | 2026-11-04 20:31 | Adiciona a lava ao movimento. |
| Marcelo Fabiano | 2026-11-04 20:09 | Adiciona o movimento. |
| Marcelo Fabiano | 2026-11-04 19:31 | Cria o roteiro de colocar e recolher o Cubit. |
