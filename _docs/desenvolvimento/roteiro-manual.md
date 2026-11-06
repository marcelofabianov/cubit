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

## Comando `/cubit reset`

| # | Faça | Espere |
|---|---|---|
| 1 | Digite `/cubit ` no chat | O jogo sugere `reset` |
| 2 | Coloque um robô, que segue você, e rode `/cubit reset` | A mensagem `Follow link cleared.` aparece, e o robô para. `PowerState` é `INACTIVE_SWITCHED_OFF`, e `Energy` não muda |
| 3 | Rode `/cubit reset` de novo | A mensagem `No Cubit is following you.` aparece |
| 4 | Botão direito no robô | Ele liga e volta a seguir você |
| 5 | Deixe o robô seguindo, afaste-se com comando até o chunk dele descarregar, por exemplo com `/tp @s ~2000 ~ ~`, e rode `/cubit reset`. Volte com `/tp @s ~-2000 ~ ~` | Ao carregar de novo, ele fica parado, com `INACTIVE_SWITCHED_OFF`. Um clique o liga |
| 6 | Rode o comando num servidor, com um player que não é operador | Funciona da mesma forma, só no vínculo dele |

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

## Receitas

Rode num mundo novo, em modo sobrevivência, com `/gamemode survival` e `/give @s minecraft:crafting_table`. Antes do passo 1, confira que as receitas do Cubit e do Dock não aparecem no livro.

| # | Faça | Espere |
|---|---|---|
| 1 | Pegue um diamante, com `/give @s minecraft:diamond` | A receita do `Cubit` aparece no livro. A do `Induction Dock` ainda não |
| 2 | Dê os materiais: `/give @s minecraft:redstone 8`, `/give @s minecraft:redstone_lamp`, `/give @s minecraft:iron_ingot 8`, `/give @s minecraft:obsidian 2` e `/give @s minecraft:chest 2` | Os itens chegam |
| 3 | Na mesa, monte redstone, lâmpada e redstone; ferro, diamante e ferro; obsidiana, vazio e obsidiana | Sai um `Cubit` |
| 4 | Olhe o livro, com o `Cubit` no inventário | A receita do `Induction Dock` aparece |
| 5 | Coloque o `Cubit` | Ele segue você, com a barra de 25% na tela |
| 6 | Na mesa, monte o L: redstone na coluna 1 da linha 1; ferro na coluna 1 da linha 2; baú, redstone e ferro na linha 3 | Sai um `Induction Dock` |
| 7 | Monte o L espelhado, trocando as colunas 1 e 3 | Também sai um `Induction Dock` |
| 8 | Coloque o `Dock` e abra a tela, com Shift + botão direito | A barra de energia está vazia, e a tela do bloco, apagada |

## Sons

Com o volume geral e o de "Criaturas amigáveis" ligados nas opções do jogo. Para ver as legendas, ligue "Mostrar legendas".

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque um robô | Toca o som de colocar, uma vez. Não toca o de ligar |
| 2 | Ande para ele seguir | A esteira toca em loop enquanto ele anda, sem cortes, e para quando ele para |
| 3 | Ocupe todos os slots do inventário e ande | A esteira fica um pouco mais grave, porque ele anda mais devagar |
| 4 | Coloque um segundo robô e clique nele | No segundo, o de ligar. No primeiro, o de desligar |
| 5 | Rode `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Energy:0L}` num robô seguindo, com o slot vazio | Toca o de desligar, e o robô para |
| 6 | Clique nele com redstone na mão | Toca a recarga, em loop, enquanto a barra sobe. Ao chegar em 5%, toca o de ligar |
| 7 | Espere a bateria encher, ou tire a redstone do slot | A recarga para |
| 8 | Ponha fogo no robô, com isqueiro, parado e andando | Toca o curto elétrico enquanto queima, junto da esteira se ele andar |
| 9 | Ponha fogo num robô desligado | O curto elétrico toca, sem a esteira |
| 10 | Com o Dock com energia, coloque um Cubit com pouca carga | Toca a recarga, sem o som de desligar. Ao sair, toca o de ligar |
| 11 | Com o Dock sem energia, coloque um Cubit com pouca carga | Não toca a recarga |
| 12 | Afaste-se cerca de 16 blocos de um robô fazendo som | O volume cai até sumir |
| 13 | Abaixe "Criaturas amigáveis" nas opções | Os sons do Cubit abaixam |
| 14 | Rode `make server` | O servidor abre sem erro de classe de cliente |

## Expressões

Compare com as imagens em `_docs/imagens/conceito/png/`, com os nomes `expressao-*.png`. Para a carga, use `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Energy:20000L}`.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque um robô | Expressão feliz, com arcos e bochechas, por cerca de 3 s, e depois a normal, com bipe. Só o som de colocar no início |
| 2 | Olhe de frente, com luz normal | A tela fica centralizada na moldura, sem vazar para o corpo e sem imagem espelhada |
| 3 | Botão direito para abrir o inventário | Bipe e expressão feliz, e volta à normal com outro bipe |
| 4 | Vá para um lugar escuro, com `/time set midnight` | A tela continua brilhando, e o corpo escurece |
| 5 | Rode o comando de carga acima | Expressão de sono, com a bateria e os "Z" no lado certo, e bipe |
| 6 | Abra o inventário nesse estado | Continua no sono |
| 7 | Coloque um segundo robô | Tela apagada, sem brilho no escuro, e antenas com a ponta cinza |
| 8 | Clique nesse robô | Normal, sem bipe, com o som de ligar. O primeiro apaga |
| 9 | Coloque um Cubit no Dock com energia | Expressão carregando, com o raio |
| 10 | Veja o robô de longe, em volta | Sem falhas de desenho, e a tela só na frente |
| 11 | Fique parado perto de um robô, na expressão normal, por 15 s | Ele pisca a cada cerca de 5 s, rápido e sem bipe |
| 12 | Coloque um segundo robô ligado longe do primeiro, e compare | Eles não piscam ao mesmo tempo |
| 13 | Suba uma escada com ele seguindo | Os olhos olham para cima durante a subida e voltam ao centro cerca de 0,5 s depois |
| 14 | Desça a escada e pule de um degrau alto | Os olhos olham para baixo e voltam ao centro |
| 15 | Ande no plano | Os olhos ficam no centro, sem tremer |
| 16 | Em sobrevivência, com outro robô, ou pondo fogo nele, cause dano num robô ligado | Olho triste com lágrima por cerca de 5 s depois do último dano, com o curto elétrico no lugar do som de dano do jogo e sem bipe. Em chamas, só o loop, sem repetição a cada golpe |
| 17 | Rode `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Health:3f}` e cause um dano pequeno | Olhos em X vermelhos por cerca de 3 s, depois a lágrima até os 5 s, e depois a normal |
| 18 | Coloque esse robô na base | Carregando. Quando a vida subir de 10%, sai da expressão em X |
| 19 | Com a tela do inventário aberta, leve o robô a perder vida, com fogo por exemplo, e olhe o painel | A barra vermelha, à esquerda da verde, desce junto da vida, e a verde continua igual |
| 20 | Em modo sobrevivência, com o robô seguindo a menos de 16 blocos, leve dano, por queda ou de um mob | Olhos de preocupado, com a gota de suor, por cerca de 3 s, sem som e sem bipe |
| 21 | Afaste-se mais de 16 blocos do robô, sem ele seguir por perto, e leve dano | Ele não fica preocupado |

Alerta e surpreso só aparecem com os scanners.

## Animações

| # | Faça | Espere |
|---|---|---|
| 1 | Fique parado perto de um robô ligado | Ele sobe e desce de leve, e as antenas oscilam |
| 2 | Ande em volta dele, a até 3 blocos, para ele não sair do lugar | O corpo vira para você, com as esteiras paradas, até cerca de 45°. Tela, antenas e braços giram junto |
| 3 | Ande para ele seguir | O corpo balança de leve, e braços e antenas se movem. Ao parar, ele volta a ficar parado sem salto |
| 4 | Olhe as duas esteiras de lado, nas duas laterais, com ele andando para a frente | Em cima, os elos correm para a frente, e embaixo, para trás, nos dois lados. As rodas giram no mesmo sentido |
| 5 | Olhe as esteiras de frente e de trás | Os elos descem na frente e sobem atrás |
| 6 | Pare o robô | As esteiras param no quadro em que estavam, sem piscar |
| 7 | Coloque dois robôs andando em velocidades diferentes, com o inventário de um cheio | Cada um corre a esteira no próprio ritmo |
| 8 | Desligue um robô com `/cubit reset` ou coloque um segundo | Em cerca de 0,5 s, o corpo abaixa, e braços e antenas caem. Ele fica assim, sem oscilar e sem virar para você |
| 9 | Empurre esse robô desligado | Continua caído, e as esteiras podem correr |
| 10 | Coloque um robô no Dock | A mesma pose de desligado, sem atravessar a placa nem a torre |
| 11 | Religue o robô ou tire-o do Dock | Ele volta suave à pose de ligado |
| 12 | Leve dano num robô ligado, com a tela triste | A tela e o corpo continuam alinhados, sem vazar, inclusive com o corpo virado |
| 13 | Em chamas, ou com outra causa de dano, com o robô ligado e parado | Os braços sobem e ficam erguidos enquanto durar a expressão triste ou de olhos em X, cerca de 5 s depois do último dano, e descem |
| 14 | Cause dano com o robô andando | Os braços ficam erguidos, e o corpo e as antenas continuam balançando |
| 15 | Cause dano num robô desligado ou na base | Os braços não sobem |
| 16 | Na expressão normal, a até 3 blocos do robô no plano, pule | Os olhos sobem por cerca de 0,5 s e voltam ao centro, sem olhar para baixo ao pousar |
| 17 | Suba em blocos, de modo que seus pés fiquem 1 bloco ou mais acima dos dele, a até 8 blocos, e fique parado | Os olhos ficam para cima enquanto você estiver lá. Com o robô subindo até você, também para cima |
| 18 | Fique 1 bloco ou mais abaixo dele, num buraco, a até 8 blocos | Os olhos ficam para baixo enquanto você estiver lá |
| 19 | Pule de uma altura de 3 blocos ou mais, perto dele, voltando ao nível dele | Os olhos descem na queda e voltam ao centro depois de cerca de 0,5 s |
| 20 | Com um robô que sobe uma escada, e você abaixo dele | Os olhos olham para cima, porque o movimento dele vence, e depois para baixo, quando ele parar |
| 21 | Ande em terreno irregular com ele seguindo | Avalie se os olhos ficam agitados demais com o limite de 1 bloco |
| 22 | Pule perto de um robô desligado | Tela apagada, sem olhos |
| 23 | Suba 1 bloco ou mais acima dele, a até 8 blocos | O corpo inclina de leve para cima, cerca de 8°, sem salto, e volta ao centro quando você volta ao nível dele |
| 24 | Fique 1 bloco ou mais abaixo dele | O corpo inclina de leve para baixo |
| 25 | Pule no plano perto dele | Só os olhos se movem, e o corpo não inclina |
| 26 | Com ele inclinado, deixe-o desligar | A pose de desligado toma o lugar da inclinação |
| 27 | Rode `make server` | O servidor abre sem erro de classe de cliente |

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
| Marcelo Fabiano | 2026-11-06 01:06 | Adiciona as animações. |
| Marcelo Fabiano | 2026-11-06 00:28 | Define o tempo dos olhos em X e ordena os passos. |
| Marcelo Fabiano | 2026-11-06 00:22 | Adiciona a expressão de preocupado. |
| Marcelo Fabiano | 2026-11-06 00:20 | Adiciona a barra de vida. |
| Marcelo Fabiano | 2026-11-06 00:17 | Aumenta o tempo da expressão triste. |
| Marcelo Fabiano | 2026-11-06 00:12 | Define o som ao levar dano. |
| Marcelo Fabiano | 2026-11-06 00:10 | Adiciona as expressões triste e quase morto. |
| Marcelo Fabiano | 2026-11-05 23:58 | Adiciona a piscada e o olhar. |
| Marcelo Fabiano | 2026-11-05 23:36 | Adiciona as expressões. |
| Marcelo Fabiano | 2026-11-05 22:44 | Adiciona os sons. |
| Marcelo Fabiano | 2026-11-05 21:53 | Adiciona as receitas do Cubit e do Induction Dock. |
| Marcelo Fabiano | 2026-11-05 20:42 | Adiciona o comando `/cubit reset`. |
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
