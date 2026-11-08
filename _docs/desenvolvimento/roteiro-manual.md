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
| 7b | Fique abaixo de um degrau de 5 blocos, a menos de 20 blocos | Ele desce do degrau e vem até você, sem perder vida |
| 7c | Fique abaixo de um degrau de 6 blocos, sem outro caminho, a menos de 20 blocos | Ele não desce e fica em cima |
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
| 1 | Coloque um robô e abra a tela | O slot `Redstone` à direita e a barra `Battery`, verde, com cerca de um quarto |
| 2 | Ande por um tempo com o robô seguindo | A barra da tela cai devagar e a do item, ao recolher, também |
| 3 | Guarde redstone e bloco de redstone no slot, com clique e Shift + clique, e tente outro item | Só a redstone entra, e o item some aos poucos, com a barra subindo |
| 4 | Clique na janela com um item na mão, fora dos slots, e nas abas à direita | O item não cai no chão |
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
| 10 | Olhando para o Dock, rode `/data remove block ~ ~ ~ Owner` com as coordenadas dele, e tente quebrá-lo em modo sobrevivência | Não quebra. Num mundo com comandos, em criativo e como operador, quebra |

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

Compare com as imagens em `_docs/imagens/conceito/png/`, com os nomes `expressao-*.png`. Para a carga, use `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Energy:10000L}`.

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
| 19 | Com a tela do inventário aberta, leve o robô a perder vida, com fogo por exemplo | A barra `Health`, vermelha, desce junto da vida, e a `Battery` continua igual |
| 20 | Em modo sobrevivência, com o robô seguindo a menos de 16 blocos, leve dano, por queda ou de um mob | Olhos de preocupado, com a gota de suor, por cerca de 3 s, sem som e sem bipe |
| 21 | Afaste-se mais de 16 blocos do robô, sem ele seguir por perto, e leve dano | Ele não fica preocupado |

Alerta e surpreso estão na seção Scanners.

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
| 13 | Em chamas, ou com outra causa de dano, com o robô ligado e parado | Os braços sobem, ficam erguidos por cerca de 2 s depois do último dano e descem, com a lágrima seguindo até cerca de 5 s |
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

## Colar e bateria

Compare com `tier-base.png`, `tier-avancado.png`, `tier-elite.png`, `tier-ultimate.png` e `vista-costas.png`, em `_docs/imagens/conceito/png/`. Só o tier Base existe até a Fase 4, então os outros são testados por comando, e o robô usa os números do Base. A carga vai de 0 a 300.000 FE.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque um robô e olhe o colar | Cinza, com a faixa de cima mais clara, no tronco central, sem vão entre as peças |
| 2 | Rode `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Tier:"advanced"}` | O colar fica azul, sem falhas em volta do pescoço |
| 3 | Repita com `{Tier:"elite"}` e `{Tier:"ultimate"}` | Roxo e dourado |
| 4 | Repita com `{Tier:"base"}` | Volta ao cinza |
| 5 | Olhe as costas com a carga de 25%, de um robô recém-colocado | Bateria com contorno arredondado e um nível verde, sem o desenho antigo aparecendo por baixo |
| 6 | Rode o comando com `{Energy:150000L}`, depois `{Energy:225000L}` e `{Energy:300000L}` | 2, 3 e 4 níveis verdes |
| 7 | Rode com `{Energy:15000L}` | 1 nível vermelho, junto da expressão de sono |
| 8 | Rode com `{Energy:0L}` | Sem preenchimento, com o contorno visível |
| 9 | Com `/time set midnight`, coloque um robô ligado, com carga, e olhe as costas no escuro | O preenchimento brilha, e o contorno e o corpo escurecem |
| 10 | Coloque um segundo robô, que fica desligado, e olhe as costas dele no escuro | Mostra a carga dele, sem brilho |
| 11 | Coloque um Cubit no Dock com energia, no escuro | O preenchimento brilha e sobe de nível enquanto ele carrega |
| 12 | Veja o robô de longe, em volta, e com ele virando para você | Colar e bateria acompanham o corpo e o pescoço, sem falhas de desenho |
| 13 | Olhe o item do Cubit e o `Damaged Cubit` | Os ícones continuam com o colar cinza |
| 14 | Rode `make server` | O servidor abre sem erro de classe de cliente |

## Texto de bateria crítica

Para a carga, use `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Energy:15000L}`, com o slot de energia vazio. O limite é de 5% (15.000 FE), e o intervalo é de cerca de 25 s, parado ou andando devagar para a carga não zerar.

| # | Faça | Espere |
|---|---|---|
| 1 | Com um robô seguindo, rode o comando | `Cubit: Critical battery` aparece menor que a mensagem do jogo, logo acima da hotbar, some em cerca de 3 s, e ele fica com a expressão de sono e o indicador vermelho |
| 1a | Em modo sobrevivência, com corações e armadura, repita | O texto fica acima da armadura, sem cobrir nada, e continua legível. Com `F1`, ele não aparece |
| 2 | Fique parado perto dele e espere cerca de 25 s | O texto aparece de novo, uma vez |
| 3 | Avalie o intervalo | Diga se 25 s está bom ou se deve mudar |
| 4 | Rode com `{Energy:16000L}` num robô novo | Nenhum texto, sem sono e com o indicador verde |
| 5 | Coloque um segundo robô, que fica desligado, e rode o comando perto dele | Nenhum texto |
| 6 | Coloque um robô com carga de 5% no Dock | Nenhum texto enquanto ele hiberna |
| 7 | Num servidor com outro player, se possível, repita o passo 1 | Só o dono vê o texto |
| 8 | Em modo sobrevivência, com um robô seguindo, ponha fogo nele | `Cubit: That hurts!` aparece uma vez, sem repetir a cada golpe, até o intervalo passar |
| 9 | Com um robô seguindo a menos de 16 blocos, leve dano, por queda ou de um mob | `Cubit: Careful!` e a expressão de preocupado |
| 10 | Longe de um robô, a mais de 16 blocos, sem ele por perto, leve dano | Nenhum texto |
| 11 | Cause dano num robô desligado | Nenhum texto |

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

## Lanterna

Os valores são os padrões de [`tiers.md`](../tiers.md). Só o tier Base tem números no config, então os outros tiers usam os dele até a Fase 4.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Em mundo plano, longe de luzes, digite `/time set noon` e coloque um Cubit | Sem lanterna: pontas laranja, e a bateria não cai parado (`/data get entity @e[type=cubit:general_robot,limit=1] Energy`) |
| 2 | Digite `/time set night` | Em até 1 s, as pontas ficam em branco quente com um halo que pulsa devagar, os olhos ganham um brilho azul em volta, e o chão clareia em volta do robô (luz 10) |
| 3 | Ande cerca de 10 blocos | A luz acompanha o Cubit, e a bateria cai andando |
| 4 | Pare perto do Cubit e meça a energia duas vezes, com 10 s entre elas | Cai cerca de 200 FE, ou 1 FE/t |
| 5 | Abra a aba Settings e clique na chave `Lamp`, em ON | A chave vai para OFF, e a lanterna, o halo e o brilho dos olhos apagam na hora. Resta só a claridade fraca da tela |
| 6 | Recolha com Shift + clique direito e coloque de novo | A lanterna continua desligada. Clique na chave para ligá-la |
| 7 | Coloque tochas em volta do Cubit, no limite da luz | Não pisca |
| 8 | Zere a bateria com `/data merge entity @e[type=cubit:general_robot,limit=1] {Energy:0L}` | A tela apaga, a lanterna apaga, as pontas ficam cinza, e não há luz |
| 9 | Recarregue e leve o Cubit a um Dock, no escuro | Lanterna apagada, pontas laranja e só a claridade da tela. No inventário, o botão fica desabilitado, mantendo o valor |
| 10 | Libere o Cubit do Dock | A lanterna volta a seguir a chave e o escuro |
| 11 | Durante o dia, a céu aberto, digite `/weather thunder` | Com a trovoada, a lanterna pode acender, porque o céu escurece |
| 12 | Com a opção de luz de entidades do LambDynamicLights desligada | Sem luz no chão e sem erro. As pontas continuam acendendo |
| 13 | Com Iris e um shader, se disponível | A luz e as pontas continuam funcionando |

## Painel solar e tier no corpo

Os valores são os padrões de [`tiers.md`](../tiers.md).

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Veja o topo do Cubit, de lado e de cima | Placa elevada, centralizada, com moldura escura e célula azul. As antenas ficam livres |
| 2 | Veja a cintura e os antebraços, com os quatro tiers por comando | A fita cobre mais o tronco, sem aumentar o Cubit, e as faixas têm a cor dela |
| 3 | De dia, a céu aberto, deixe o Cubit parado e meça `Energy` com 10 s entre as leituras | Sobe cerca de 1.000 FE, sem som de recarga, e a célula pulsa de leve |
| 4 | Coloque um bloco opaco acima dele | Em até 1 s, para de subir e de pulsar |
| 5 | Troque por vidro | Volta a subir e a pulsar |
| 6 | Digite `/weather thunder` | Continua carregando |
| 7 | Digite `/time set night`, com a lanterna desligada | Não sobe nem pulsa |
| 8 | Encha a bateria de dia | Para de pulsar |
| 9 | Leve ao Nether e a um Dock | Não carrega pelo painel e não pulsa |

## Cor e nome

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Com o Cubit ativo e sobrevivência, clique com um corante vermelho | A cor muda, gasta 1 corante, e ele fica feliz com o bipe. Tela, indicador, painel, garras, esteiras e pontas das antenas não mudam |
| 2 | Clique de novo com vermelho | Nada muda, nada é gasto, e o inventário não abre |
| 3 | Pinte com as 16 cores e compare com `cubit-personalizacao.png` | Cada cor lembra a imagem, e a fita e as faixas continuam na cor do tier |
| 4 | Repita com o branco | Volta ao visual original |
| 5 | No criativo, pinte | Não gasta o corante |
| 6 | Pinte o Cubit desligado e o Cubit no Dock | A cor muda. Desligado, a tela fica apagada. No Dock, continua carregando |
| 7 | Recolha o Cubit pintado | O ícone tem a cor dele e, ao colocar, a cor continua |
| 8 | Dê um nome na etiqueta, na bigorna, e use no Cubit | O nome fica sempre visível acima dele, gasta a etiqueta, e ele fica feliz. O inventário mostra o nome |
| 9 | Renomeie um Cubit como item na bigorna e coloque | O nome aparece |
| 10 | Mate o Cubit pintado e com nome | O Damaged Cubit mantém o nome, e o ícone não muda |
| 11 | Conserte com a Toolbox e coloque | A cor e o nome voltam |
| 12 | Saia e entre no mundo | A cor e o nome continuam |
| 13 | Com outro player no servidor, use corante e etiqueta no Cubit | Nada muda, e nada é gasto |

## Scanners

Os valores são os padrões de [`tiers.md`](../tiers.md). Mobs varrem a cada 10 s e minérios a cada 35 s, então espere esse tempo em cada passo. Para ver a carga, use `/data get entity @e[type=cubit:general_robot,limit=1,sort=nearest] Energy`.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Com um Cubit seguindo, invoque um zumbi a 3 blocos, com `/summon minecraft:zombie ~3 ~ ~ {NoAI:1b}` | Em até 10 s, toca o alerta, aparece `Cubit: Careful, Zombie!` e a tela fica em alerta |
| 2 | Continue perto do zumbi por 30 s | O texto e o som não se repetem, e o alerta continua na tela |
| 3 | Mate o zumbi com `/kill @e[type=minecraft:zombie]` | Em até 10 s, a tela volta ao normal, com o bipe |
| 4 | Repita com esqueleto e creeper, um de cada vez | O texto cita cada um, sem esperar os 25 s do outro |
| 5 | Invoque uma vaca ao lado | Nenhum alerta |
| 6 | Coloque minério de diamante a 3 blocos | Em até 35 s, toca o som do minério, aparece `Cubit: Diamond Ore!!!` e a tela fica surpresa por cerca de 3 s |
| 7 | Espere mais 40 s | Não avisa de novo sobre o mesmo bloco |
| 8 | Quebre o minério, espere uma varredura e coloque de novo | Avisa de novo |
| 9 | Coloque minério de carvão | Nenhum aviso |
| 10 | Clique no Cubit com minério de carvão na mão | Bipe e `Cubit: Looking for Coal Ore`, e o item continua na mão |
| 11 | Espere uma varredura | Aparece `Cubit: Coal Ore!!!`, sem som e sem surpresa |
| 12 | Clique de novo com minério de carvão | Bipe e `Cubit: Filter cleared` |
| 13 | Clique com minério de ferro num Cubit desligado | O filtro muda, com o bipe e o texto, e a tela continua apagada |
| 14 | Abra a aba Settings | A chave `Scanners` em ON |
| 15 | Clique na chave, com um zumbi por perto, e espere 15 s | A chave vai para OFF, e nada avisa nem gasta |
| 16 | Recolha o Cubit e coloque de novo | A chave e o filtro continuam |
| 17 | Mate o Cubit, conserte e coloque | A chave e o filtro continuam |
| 18 | Parado, coberto e com a lanterna desligada, meça a carga com 35 s entre as leituras | Cai de 200 a 250 FE: 3 ou 4 varreduras de mobs e 1 de minérios, a 50 FE cada |
| 19 | Leve o Cubit a um Dock | Não varre e não avisa, e o botão fica desabilitado |
| 20 | Rode `make server` | O servidor abre sem erro de classe de cliente |

## Caixas dos scanners

Use um mundo plano em criativo, com um Cubit seguindo. A caixa do minério dura cerca de 10 s, e minérios varrem a cada 35 s, então espere esse tempo em cada passo.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Coloque minério de diamante a 3 blocos do Cubit e cubra com pedra | Em até 35 s, junto do `Cubit: Diamond Ore!!!`, uma caixa azul clara, translúcida e pulsando, visível através da pedra |
| 2 | Espere 10 s | A caixa some |
| 3 | Espere a próxima varredura | Nem caixa nem texto: o minério já foi visto |
| 4 | Coloque minério de ferro e quebre-o logo depois de a caixa aparecer | A caixa some na hora |
| 5 | Coloque minério de carvão sem filtro e espere uma varredura | Nenhuma caixa |
| 6 | Ponha carvão no filtro e espere uma varredura | Caixa azul em volta do carvão, com `Cubit: Coal Ore!!!` |
| 7 | Coloque diamantes a 5 blocos à frente do Cubit e a 4 para o lado e 4 para cima | Só o da frente ganha caixa: o outro fica fora da esfera |
| 8 | Rode `/summon minecraft:zombie ~3 ~ ~` perto do Cubit | Em até 10 s, uma seta vermelha para baixo sobre a cabeça do zumbi, translúcida, pulsando e balançando de leve, que o acompanha através das paredes e fica virada para você |
| 9 | Afaste-se até o zumbi ficar a mais de 8 blocos do Cubit | A seta some |
| 10 | Desligue a chave `Scanners` | Em até 30 s, nenhuma caixa nova, e as que havia somem |
| 11 | Peça a outro player que olhe o mesmo minério | Ele não vê caixa. Esse passo fica para os testes de instância |
| 12 | Teste os passos 1 e 8 nos gráficos Rápido, Bonito e Fabuloso | A caixa aparece nos três |

## Cores dos scanners

Use um mundo plano, com um Cubit seguindo. Para ver de dia e de noite, use `/time set day` e `/time set night`.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | De dia, com a chave `Lamp` desligada, rode `/summon minecraft:zombie ~3 ~ ~ {NoAI:1b}` | Em até 10 s, as pontas e os halos das antenas ficam vermelhos, com um brilho vermelho suave atrás do corpo |
| 2 | Mate o zumbi com `/kill @e[type=minecraft:zombie]` | Em até 10 s, as antenas apagam e o brilho some |
| 3 | Coloque minério de diamante a 3 blocos e espere a varredura | Junto do `Cubit: Diamond Ore!!!`, antenas e brilho azuis por cerca de 3 s |
| 4 | Ponha carvão no filtro e coloque minério de carvão | Junto do `Cubit: Coal Ore!!!`, antenas e brilho azuis por cerca de 3 s |
| 5 | Com um zumbi no raio, coloque um diamante | As antenas continuam vermelhas |
| 6 | Com um zumbi no raio, leve dano, com `/damage @e[type=cubit:general_robot,limit=1,sort=nearest] 1` | A tela fica triste, e as antenas continuam vermelhas |
| 7 | De noite, com a lanterna acesa, repita o passo 1 | As antenas ficam vermelhas e, depois do zumbi, voltam ao branco da lanterna |
| 8 | Olhe o chão em volta do Cubit com a cor acesa | A luz no chão continua branca, ou nenhuma, sem a lanterna |
| 9 | Desligue a chave `Scanners` com um zumbi no raio | As cores apagam |
| 10 | Peça a outro player que olhe o Cubit no passo 1 | Ele também vê as cores. Esse passo fica para os testes de instância |

## Quando o dono renasce

Use modo sobrevivência, com `/gamemode survival`. Para morrer, use `/kill @s`. Para ir longe do spawn, use `/tp @s ~1000 ~ ~`, e espere o Cubit aparecer perto de você antes de cada morte.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Com um Cubit seguindo, longe do spawn, morra e renasça | Ele aparece com você no spawn, com a mesma carga e o mesmo inventário |
| 2 | Morra perto do spawn, a menos de 20 blocos de onde vai renascer | Ele vem andando até você, sem aparecer de repente |
| 3 | Com o Cubit seguindo no Nether, morra e renasça no mundo normal | Ele aparece com você no mundo normal |
| 4 | Passe por um portal do Nether, sem morrer | Ele não vai junto |
| 5 | Deixe um Cubit desligado longe, ligue outro para seguir, morra e renasça | O que seguia volta, e o desligado continua no lugar |
| 6 | Chame o Cubit a um Dock e morra antes de ele chegar, perto do Dock | Ele continua indo ao Dock |

## Falas

As frases estão em [`falas.md`](../conceito/falas.md). Fique a céu aberto, com um Cubit seguindo, e espere 5 minutos entre as falas sobre o tempo, ou reduza `chatter_repeat_ticks` no config.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Rode `/time set 22900` e espere | Ao chegar o amanhecer, em até 1 s, bipe e uma frase de amanhecer |
| 2 | Rode `/time set 2500` e espere | Bipe e uma frase de dia lindo |
| 3 | Rode `/time set 11900` e espere | Bipe e uma frase de entardecer |
| 4 | Rode `/weather rain` | Bipe e uma frase de chuva |
| 5 | De dia, rode `/weather clear` | Bipe e uma frase de dia lindo |
| 6 | Rode `/weather thunder` | Bipe e uma frase de chuva forte |
| 7 | Vá a um bioma com neve, com `/locate biome minecraft:snowy_plains` e `/tp` | Bipe e uma frase de frio |
| 8 | Repita qualquer passo logo depois de uma fala | Nada: o intervalo de 5 minutos não acabou |
| 9 | Entre numa caverna e rode `/weather rain` | Nenhuma fala de chuva |
| 10 | Com um zumbi no raio do scanner, troque o horário | Nenhuma fala |
| 11 | Ganhe uma conquista, por exemplo com `/advancement grant @s only minecraft:story/mine_stone` | Expressão feliz, bipe e uma frase de parabéns |
| 12 | Ganhe um desafio, com `/advancement grant @s only minecraft:adventure/kill_all_mobs` | Expressão feliz, bipe e uma frase mais forte |
| 13 | Faça um item que libera uma receita nova | Nenhuma fala |
| 14 | Abra a aba Settings | A chave `Chatter` em ON |
| 15 | Desligue a chave e repita os passos 4 e 11 | Nenhuma fala, e a conquista não faz o Cubit comemorar |
| 16 | Recolha e coloque o Cubit | A chave continua desligada |
| 17 | Com um zumbi no raio, ganhe uma conquista | A expressão continua em alerta, com uma frase de parabéns e um bipe |
| 18 | Ganhe outra conquista, na expressão normal | Um bipe só, sem repetir |
| 19 | Em sobrevivência, corra para longe do Cubit em linha reta | Por volta de 16 blocos, bipe e uma frase de ficar para trás, antes de ele aparecer perto de você |
| 20 | Corra de novo logo depois | Nenhuma fala, até passar 1 minuto |
| 21 | Rode `/tp @s ~100 ~ ~` | Ele aparece perto de você, sem essa fala |
| 22 | Saia e entre no mundo | Nenhuma fala de bioma ao entrar |
| 23 | Com `/locate biome minecraft:desert` e `/tp`, leve o Cubit a um deserto, vindo de outro tipo de bioma | Bipe e uma frase de deserto. O Cubit vem junto pelo teleporte dele |
| 24 | Logo depois, vá a uma floresta | Nada, até passarem 2 minutos da última fala de bioma |
| 25 | Atravesse um rio entre duas florestas, depois dos 2 minutos | Nenhuma fala nova |
| 26 | Mate vários porcos, vacas e galinhas, um por vez | De vez em quando, cerca de 1 em 3 e com ao menos 1 minuto entre as falas, bipe e uma frase daquele animal |
| 27 | Mate outro animal, como uma raposa | De vez em quando, uma frase genérica |
| 28 | Domestique um lobo e mate-o várias vezes, por exemplo com `/summon` e um osso | Nunca há fala |
| 29 | Deixe um zumbi matar o animal | Nenhuma fala |
| 30 | Em sobrevivência, minere um minério de redstone, sem mob por perto | Bipe e uma frase de redstone, como `Cubit: I love redstone!` |
| 31 | Minere outro logo depois | Nenhuma fala, até passar 1 minuto |
| 32 | Coloque redstone perto do Cubit, espere o `Cubit: REDSTONE!!!` e minere durante a surpresa | Nenhuma fala de redstone, porque a surpresa não é calma |
| 33 | Minere minério de ferro | Nenhuma fala |

## Jade

O `make client` já carrega o Jade. Mire no Cubit ou no Induction Dock para ver o tooltip no alto da tela.

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Coloque um Cubit e mire nele | Nome, vida, `Tier: Base` em cinza, `Battery: 25% (75,000 / 300,000 FE)` e `State: Active` |
| 2 | Deixe-o andar um pouco e mire de novo | A bateria cai |
| 3 | Zere a bateria com `/data merge entity @e[type=cubit:general_robot,limit=1,sort=nearest] {Energy:0L}` | `State: Out of battery` e `Battery: 0% (0 / 300,000 FE)` |
| 4 | Ligue outro Cubit, para desligar o primeiro, e mire no primeiro com carga | `State: Switched off` |
| 5 | Leve um Cubit ao Dock e mire nele | `State: Hibernating` |
| 6 | Mire no Dock vazio, sem energia | `Energy: 0 / 100,000 FE` e `Status: No power` (pergunta 685) |
| 7 | Coloque redstone no Dock e mire | O estoque sobe, e `Status: Idle` |
| 8 | Com o Cubit na base, mire no Dock | `Status: Charging` e, cheio, `Status: Complete` |
| 9 | Nas opções do Jade, em Plugins, desligue `Cubit` e `Induction Dock` | As linhas do mod somem |
| 10 | Rode `make server` | O servidor abre sem o Jade |

## Telas

Compare cada tela com as imagens [`tela-cubit.png`](../imagens/conceito/tela-cubit.png) e [`tela-induction-dock.png`](../imagens/conceito/tela-induction-dock.png). Os textos sem imagem estão em [`telas.md`](../funcionalidades/telas.md#textos-sem-imagem).

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Coloque um Cubit e clique nele | A tela do Cubit no estilo da imagem, sem a textura do baú: retrato piscando, `Cubit` com `BASE`, `● Following` em verde, vida e bateria em `75k / 300k FE`, slot `Redstone`, `Cubit's inventory` e `Inventory` |
| 2 | Ande de dia ao ar livre com a tela aberta | `Solar +5 FE/t` sob a bateria |
| 3 | Ponha redstone no slot com a bateria abaixo da capacidade | `Slot +500 FE/t` |
| 4 | Zere quase toda a bateria, até 5% | `● Critical battery` em vermelho, e a barra fica vermelha |
| 5 | Ligue outro Cubit e abra o primeiro, já religado com o clique | `● Active` em cinza |
| 6 | Com a tela aberta, ligue outro Cubit pelo clique, para desligar este | A tela fecha na hora, e o item no cursor volta ao inventário |
| 7 | Renomeie o Cubit para `Bolt` e leve-o ao Dock | `Bolt`, `● Hibernating · Dock` em azul, a expressão carregando, `Dock +500 FE/t` e `Bolt's inventory` |
| 8 | Clique na engrenagem | A aba Settings: `Battery` em porcentagem, `Lamp`, `Scanners` e `Chatter` com ON e OFF, o filtro, o status e o rodapé. Os slots somem |
| 9 | Clique nas três chaves | Cada uma troca entre ON e OFF. Na base, `Lamp` fica apagada e não muda |
| 10 | Escolha carvão no filtro e abra a aba Settings | O ícone do minério e `Looking for Coal Ore`. Sem filtro, `No filter · rare ores only` |
| 11 | Veja o status de dia, à noite, coberto e no Nether | `Charging · 5 FE/t`, `Night · not charging`, `No sky · not charging` e `No sun here · not charging` |
| 12 | Ande, ande na água e acenda a lanterna | `Energy use` lista as causas, como `16 FE/t · walking, lamp`. Parado, `0 FE/t · idle` |
| 13 | Volte à aba do baú | Os slots voltam, com os itens no lugar |
| 14 | Passe o mouse num texto cortado, se houver | O tooltip mostra o texto inteiro |
| 15 | Coloque um Dock sem energia e abra com Shift + clique | `● No power` em laranja, a torre com o raio cortado, `0 / 100k FE`, `No input`, `No Cubit docked`, `Needs energy before it can charge.`, `Empty`, o seu nome e o botão cinza |
| 16 | Ponha redstone no slot do Dock | `● Idle` em azul, a torre azul, `Ready to charge.`. Com o estoque cheio, `Full` |
| 17 | Aperte `Call the nearest Cubit (15 blocks)` com um Cubit perto | O Cubit vai ao Dock, mesmo com o botão cinza sem energia |
| 18 | Com o Cubit na base, veja a tela | `● Charging` em verde, o retrato carregando, `Cubit · Battery`, a vida e `+500 FE/t · ...`. Com a vida cheia, `health full` |
| 19 | Ligue um cabo de FE de outro mod, no Iron Meridian | `Input +... FE/t` com a média do último segundo |
| 20 | Chame um segundo Cubit com o Dock ocupado | `Queue` mostra `1 Cubit waiting` |
| 21 | Teste outro player no servidor | Ele não abre as telas do seu Cubit nem do seu Dock |
| 22 | Mude a escala da interface nas opções de vídeo | As torres, os ícones e os retratos continuam nítidos |

## Tradução

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | Em Opções, Idioma, escolha Português (Brasil) | Os itens viram `Cubit`, `Cubit Danificado`, `Caixa de Ferramentas` e `Base de Indução` |
| 2 | Abra as telas do Cubit e da base | Todos os textos em português, sem chave crua como `gui.cubit...`, e nada passa da borda |
| 3 | Espere uma fala e um aviso de minério | A fala em português e `Cubit: Minério de Diamante!!!` |
| 4 | Ainda em português, abra Mods e escolha o Cubit | A descrição em português, que começa com `Cubit é um robô companheiro fofo` |
| 5 | Volte ao inglês | Tudo em inglês de novo, com `Cubit: Diamond Ore!!!`, e a descrição na lista de mods em inglês |

## Servidor dedicado

1. Rode `make server` e aceite o EULA em `run/eula.txt`, se ainda não tiver aceitado.
2. Espere a mensagem `Done`, e confirme que o servidor abriu sem erro de classe de cliente.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-08 01:05 | Confere a descrição traduzida na lista de mods. |
| Marcelo Fabiano | 2026-11-08 00:11 | Usa o nome do bloco nos exemplos de minério. |
| Marcelo Fabiano | 2026-11-07 23:12 | Troca o painel lateral pelas chaves da aba Settings. |
| Marcelo Fabiano | 2026-11-07 23:11 | Adiciona as telas próprias e troca o estado do Dock no Jade. |
| Marcelo Fabiano | 2026-11-07 21:40 | Adiciona o Jade. |
| Marcelo Fabiano | 2026-11-07 21:05 | Troca a caixa do mob pela seta. |
| Marcelo Fabiano | 2026-11-07 20:51 | Adiciona as cores dos scanners. |
| Marcelo Fabiano | 2026-11-07 20:04 | Adiciona as falas da redstone. |
| Marcelo Fabiano | 2026-11-07 20:00 | Atualiza o raio do scanner de mobs nas caixas. |
| Marcelo Fabiano | 2026-11-07 19:21 | Adiciona as caixas dos scanners. |
| Marcelo Fabiano | 2026-11-07 19:06 | Atualiza o gasto esperado dos scanners. |
| Marcelo Fabiano | 2026-11-07 17:14 | Adiciona as falas de animais. |
| Marcelo Fabiano | 2026-11-07 17:11 | Adiciona as falas de bioma. |
| Marcelo Fabiano | 2026-11-07 17:07 | Adiciona a fala de ficar para trás. |
| Marcelo Fabiano | 2026-11-07 16:57 | Adiciona o bipe nas falas. |
| Marcelo Fabiano | 2026-11-07 16:54 | Adiciona as falas. |
| Marcelo Fabiano | 2026-11-07 16:19 | Adiciona os scanners e o dono que renasce. |
| Marcelo Fabiano | 2026-11-07 13:51 | Adiciona o Dock sem dono. |
| Marcelo Fabiano | 2026-11-06 15:58 | Adiciona a cor e o nome. |
| Marcelo Fabiano | 2026-11-06 15:16 | Tira o vidro do painel. |
| Marcelo Fabiano | 2026-11-06 15:05 | Adiciona o painel solar e o tier no corpo. |
| Marcelo Fabiano | 2026-11-06 13:57 | Adiciona o halo e o brilho dos olhos. |
| Marcelo Fabiano | 2026-11-06 13:52 | Adiciona a lanterna. |
| Marcelo Fabiano | 2026-11-06 10:36 | Adiciona os textos e os braços por 2 s, e ajusta as cargas para 5%. |
| Marcelo Fabiano | 2026-11-06 09:03 | Adiciona o colar e o indicador de bateria. |
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
