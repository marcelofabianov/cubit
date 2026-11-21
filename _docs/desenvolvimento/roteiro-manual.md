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
| 6 | Rode o comando num servidor, com um player que não é operador | Funciona da mesma forma, só no vínculo dele. Coberto pelas GameTests, sem teste no jogo (pergunta 771) |

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
| 9 | Num servidor com outro player, tente usar e quebrar o Dock | Ele não abre a tela e não quebra. No modo criativo com permissão de operador, quebra. Coberto pelas GameTests, sem teste no jogo (pergunta 771) |
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

Rode num mundo novo, em modo sobrevivência, com `/gamemode survival` e `/give @s minecraft:crafting_table`. Antes do passo 1, confira que as receitas do Cubit, do Dock e dos Modules não aparecem no livro.

| # | Faça | Espere |
|---|---|---|
| 1 | Pegue um diamante, com `/give @s minecraft:diamond` | As receitas do `Cubit Casing`, do `Cubit Visor`, das `Cubit Treads` e do `Cubit Neural Core` aparecem no livro. A do `Cubit` e a do `Induction Dock` ainda não |
| 2 | Dê os materiais: `/give @s minecraft:iron_ingot 16`, `/give @s minecraft:obsidian 3`, `/give @s minecraft:redstone_torch 2`, `/give @s minecraft:piston 2`, `/give @s minecraft:redstone 12`, `/give @s minecraft:diamond 5` e `/give @s minecraft:chest 2` | Os itens chegam |
| 3 | Faça as peças: tocha de redstone, ferro, tocha de redstone / ferro, vazio, ferro / obsidiana, ferro, obsidiana; diamante, obsidiana, diamante; ferro, ferro, ferro / pistão, vazio, pistão; vazio, redstone, vazio / redstone, diamante, redstone / vazio, redstone, vazio. Com o Neural Core, abra o livro, e depois junte as quatro peças em qualquer posição | Saem as quatro peças, a receita do `Cubit` aparece com o Neural Core, e sai um `Cubit` |
| 4 | Olhe o livro, com o `Cubit` no inventário | As receitas do `Induction Dock` e dos três Modules aparecem |
| 5 | Coloque o `Cubit` | Ele segue você, com a barra de 25% na tela |
| 6 | Na mesa, monte o L: diamante na coluna 1 da linha 1; baú na coluna 1 da linha 2; ferro, redstone e ferro na linha 3 | Sai um `Induction Dock` |
| 7 | Monte o L espelhado, trocando as colunas 1 e 3 | Também sai um `Induction Dock` |
| 8 | Coloque o `Dock` e abra a tela, com Shift + botão direito | A barra de energia está vazia, e a tela do bloco, apagada |
| 9 | Faça outro `Cubit Neural Core`, dê `/give @s minecraft:lapis_lazuli 2` e `/give @s minecraft:gold_ingot 2` e monte lápis em cima e embaixo, ouro nas laterais e o Neural Core no centro | Sai um `Advanced Module` |
| 10 | Dê `/give @s minecraft:purple_dye 2` e `/give @s minecraft:diamond 2` e monte corante roxo em cima e embaixo, diamante nas laterais e o Advanced Module no centro | Sai um `Elite Module` |
| 11 | Dê `/give @s minecraft:netherite_ingot 2` e `/give @s minecraft:blaze_rod 2` e monte netherite em cima e embaixo, vara de blaze nas laterais e o Elite Module no centro | Sai um `Ultimate Module` |

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

Compare com `tier-base.png`, `tier-avancado.png`, `tier-elite.png`, `tier-ultimate.png` e `vista-costas.png`, em `_docs/imagens/conceito/png/`. Os comandos abaixo trocam o tier sem Module, só para ver o colar. O upgrade de verdade está em [Upgrade de tier](#upgrade-de-tier). A carga vai de 0 a 300.000 FE, a bateria do Base.

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
| 13 | Olhe o item do Cubit e o `Damaged Cubit` de um robô Base | Os ícones têm o colar cinza, como antes do upgrade de tier |
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
| 13 | Com outro player no servidor, use corante e etiqueta no Cubit | Nada muda, e nada é gasto. Coberto pelas GameTests, sem teste no jogo (pergunta 771) |

## Upgrade de tier

Compare os ícones com `modulo-avancado.png`, `modulo-elite.png` e `modulo-ultimate.png`, em `_docs/imagens/conceito/png/`. Os Modules saem das receitas da seção [Receitas](#receitas) ou da aba do criativo. Os números são os de [`tiers.md`](../tiers.md).

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba do Cubit no criativo, ou faça os Modules pelas receitas | Os três Modules aparecem, com os nomes em azul, roxo e dourado |
| 2 | Passe o mouse em cada Module | `Requires a Base Cubit`, `Requires an Advanced Cubit` e `Requires an Elite Cubit`, em cinza |
| 3 | Em sobrevivência, com um Cubit Base ativo, clique com o Elite Module | Nada muda, o Module fica na mão, e aparece `Cubit: I need to be Advanced first` |
| 4 | Clique com o Advanced Module | O Module é gasto, ele fica feliz com o bipe, aparece `Cubit: Upgraded to Advanced!`, e o colar e as faixas ficam azuis |
| 5 | Abra o inventário dele | A janela tem a mesma altura da do Base e fica mais larga, com os 36 slots em 3 fileiras de 12, sem rolagem, e os itens nos mesmos lugares. A vida atual é a mesma, a vida máxima é 50, e a bateria tem a mesma carga sobre 600 mil FE |
| 6 | Clique de novo com o Advanced Module | Nada é gasto, e aparece `Cubit: I'm already Advanced` |
| 7 | Ande com ele | Mais rápido que o Base |
| 8 | Aplique o Elite Module num Cubit desligado e o Ultimate num Cubit no Dock | Os dois sobem de tier. Com a tela apagada, só o texto aparece, e no Dock ele continua carregando |
| 9 | Abra o Cubit Ultimate numa janela pequena do jogo, com a escala automática | A tela cabe inteira. A roda do mouse sobre o inventário, um clique na barra e arrastar o marcador passam pelas 6 linhas. Shift + clique de um item do player, com as 3 linhas de cima cheias, guarda o item numa linha de baixo |
| 10 | Recolha o Cubit Ultimate | O ícone tem o colar dourado, também pintado de outra cor |
| 11 | Mate esse Cubit | O ícone do `Damaged Cubit` tem o colar dourado, e o conserto mantém o tier |
| 12 | No criativo, aplique um Module | O Module não é gasto |
| 13 | Com o jogo em português, repita os passos 2 a 6 | `Módulo Avançado`, `Exige um Cubit Base`, `Cubit: Preciso ser Avançado antes`, `Cubit: Evoluí para Avançado!` e `Cubit: Já sou Avançado` |
| 14 | Com outro player no servidor, use um Module no Cubit | Nada acontece, sem texto, e o Module não é gasto. Coberto pelas GameTests, sem teste no jogo (pergunta 809) |

## Choque e modos de defesa

As regras estão em [`combate.md`](../funcionalidades/combate.md), e os números, em [`tiers.md`](../tiers.md#comportamento-e-combate). Use os Modules da aba do criativo para chegar a cada tier, em sobrevivência.

| # | Faça | Espere |
|---|---|---|
| 1 | Com um Cubit Base, abra a tela | Três abas: baú, engrenagem e escudo. Na Defense, `Peaceful` marcado, `Retaliate` e `Aggressive` apagados, com `Unlocks at Advanced` e `Unlocks at Elite`, e a seção `Shock` com `Unlocks at Advanced` |
| 2 | Suba para o Advanced e abra a aba Defense | `Retaliate` liberado, e a seção `Shock` com dano 2, 3 blocos, a cada 3 s, 2000 FE por choque e o mais próximo |
| 3 | Escolha `Retaliate`, chegue perto de um zumbi e deixe ele te bater | O Cubit dá choque nele, com faíscas do Cubit até o zumbi e o som de choque. Um zumbi que não atacou ninguém não leva choque |
| 4 | Suba para o Elite e escolha `Aggressive` | Toca o som do modo agressivo, só nessa troca |
| 5 | Ande perto de mobs hostis, de animais e de um piglin zumbificado calmo | Só os hostis levam choque. Animais e o piglin calmo, não |
| 6 | Bata no piglin zumbificado | Ele fica agressivo contra você e passa a levar choque |
| 7 | Deixe um mob morrer pelo choque | Ele solta XP, e, se tiver nome, a mensagem é `X was zapped by Cubit` |
| 8 | Suba para o Ultimate, com dois mobs hostis no alcance | Os dois levam choque de uma vez, a cada 1 s |
| 9 | Ponha um bloco entre o Cubit e o mob | Sem choque através do bloco |
| 9a | Com o Cubit no Advanced, escolha `Focus` e bata num zumbi, com outros zumbis por perto | Aparece `Cubit: Target: Zombie`, o zumbi ganha uma seta amarela maior, e só ele leva choque. As setas vermelhas do scanner continuam nos outros |
| 9b | Atire uma flecha em outro zumbi | Ele vira o alvo, com o texto e a seta amarela |
| 9c | Bata numa vaca | Ela não vira alvo, e o alvo continua o mesmo |
| 9d | Afaste-se mais de 32 blocos do alvo, ou deixe ele morrer | A seta amarela some. Sem alvo, o Cubit volta a defender quem te ferir |
| 9e | Fique parado com o Cubit no modo Agressivo e um zumbi no alcance | A cabeça e o corpo dele viram para o zumbi e seguem o zumbi enquanto ele vive, mesmo entre os choques. Quando o zumbi morre, ele fica olhando o ponto da morte por cerca de 3 s e depois volta a olhar para você |
| 9f | Ande com o Cubit seguindo e um zumbi ao lado do caminho | A cabeça acompanha o zumbi, e o corpo continua apontando para onde ele anda, sem largar você |
| 9g | Leve um zumbi atingido para mais de 32 blocos de você | O Cubit deixa de seguir o zumbi com o olhar e, cerca de 3 s depois, volta a olhar para você |
| 10 | Desligue o Cubit ou deixe ele no Dock, com mobs por perto | Sem choque |
| 11 | Recolha e coloque o Cubit, e saia e entre no mundo | O modo escolhido continua |
| 12 | Com o jogo em português, abra a aba | `Defesa`, `Modos`, `Pacífico`, `Contra-atacar`, `Agressivo`, `Libera no Avançado` e a seção `Choque` |
| 13 | Com outro player no servidor, tente mudar o modo pela tela | Ele não abre a tela do Cubit. Coberto pelas GameTests, sem teste no jogo |

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

## Chaves da aba Settings

As regras estão em [`telas.md`](../funcionalidades/telas.md#aba-settings) e em [`energia.md`](../funcionalidades/energia.md).

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Settings de um Cubit ativo e passe o mouse nas chaves | Cinco chaves em Functions, em linhas baixas, com `Torches` na última, apagada no Base. Na dica de `Solar panel`, `Charges in the sun · 5 FE/t` no Base, e tudo cabe na janela, sem o rodapé antigo. No cabeçalho, o botão `Power` ao lado de `Battery` |
| 2 | Passe o mouse no `Power` | `Power` e `Turns the Cubit off` |
| 3 | De dia, ao céu aberto, desligue o `Solar panel` | A célula para de pulsar, a bateria não sobe pelo sol, e o Status mostra `Solar panel` `Off` |
| 4 | Recolha e coloque o Cubit, e saia e entre no mundo | A chave continua desligada |
| 5 | Clique no `Power` | O Cubit desliga, com o som de desligar, e a tela fecha |
| 6 | Coloque outro Cubit | Ele passa a seguir você, porque o primeiro liberou o lugar |
| 7 | Clique com o botão direito no primeiro | Ele liga, e fica desligado de novo se o outro estiver seguindo, pela regra que já existe |
| 8 | Abra a tela de um Cubit hibernando no Dock | O `Power` aparece apagado e não faz nada |
| 9 | Com o jogo em português, abra a aba e passe o mouse nas chaves | `Painel solar`, com `Carrega no sol · 5 FE/t` na dica, `Tochas`, e no `Power`, `Energia` e `Desliga o Cubit` |

## Coleta e ímã

As regras estão em [`inventario.md`](../funcionalidades/inventario.md#coleta) e em [`telas.md`](../funcionalidades/telas.md#aba-behavior). Use `/give` para os Modules e os itens.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Behavior de um Cubit Base ativo e passe o mouse nas chaves | `Pickup` e `Fetch` ligadas, `Magnet` apagada, com `Unlocks at Advanced` na dica, as três chaves em linhas baixas, o botão `Blacklist`, 3 slots fantasma, e o seu inventário e a hotbar visíveis |
| 2 | Jogue terra no chão e ande até o Cubit passar por ela | Ele pega a terra quando encosta nela, e a bateria não cai pela coleta |
| 3 | Jogue um item e fique perto do Cubit | Ele não pega o item na hora, pelo atraso de coleta do jogo |
| 4 | Clique num slot fantasma com terra no cursor | A terra aparece no slot, e a terra do cursor continua com você |
| 5 | Jogue terra e pedra perto do Cubit | Ele pega a pedra e deixa a terra |
| 6 | Troque para `Whitelist` | Ele pega a terra e deixa a pedra |
| 7 | Limpe o slot com o cursor vazio, ainda na `Whitelist` | Ele não pega nada |
| 8 | Volte para `Blacklist`, aplique o Advanced Module e abra a aba | `Magnet` ligada, com `Pulls items within 3 blocks · 20 FE per item` na dica, e 9 slots |
| 9 | Jogue um item a 3 blocos do Cubit parado | O item desliza até ele em menos de meio segundo, e a bateria cai 20 FE |
| 10 | Desligue `Pickup` | `Magnet` fica apagada, e ele não pega nem puxa nada |
| 11 | Encha o inventário do Cubit, deixando espaço para meia pilha de terra, e jogue uma pilha inteira | Ele pega o que cabe, e o resto fica no chão |
| 12 | Jogue um Cubit recolhido perto dele | Ele não pega nem puxa o item |
| 13 | No Elite, clique com o botão direito num slot com tora de carvalho | O slot ganha a marca, e a dica mostra `Tag: #minecraft:logs`. Outras toras passam a valer |
| 14 | Recolha e coloque o Cubit, e saia e entre no mundo | As chaves, o modo e o filtro continuam iguais |
| 15 | Ponha o Cubit para hibernar no Dock e jogue itens nele | Ele não pega nada |
| 16 | Com o jogo em português, abra a aba | `Comportamento`, `Coleta`, `Ímã`, `Buscar`, `Filtro de coleta` e `Lista negra` |

### Busca dos itens do chão

As regras estão em [`inventario.md`](../funcionalidades/inventario.md#busca). Use um Cubit Base, seguindo você.

| # | Faça | Espere |
|---|---|---|
| 1 | Passe o mouse em `Fetch` | `Fetches items within 4 blocks of you` |
| 2 | Jogue terra com Q e fique parado | Depois do atraso de coleta, o Cubit anda até a terra, pega e volta a seguir |
| 3 | Jogue terra e ande para longe, a mais de 4 blocos dela | Ele larga a terra e volta a seguir |
| 4 | Jogue terra do outro lado de um muro, sem caminho | Em cerca de 5 s ele desiste, volta a seguir e só tenta de novo depois de 10 s |
| 5 | Desligue `Fetch` e jogue terra | Ele não vai buscar, e a coleta por contato continua |
| 6 | Ligue `Fetch`, desligue `Pickup` e jogue terra | `Fetch` fica apagada, e ele não busca |
| 7 | Encha o inventário do Cubit e jogue terra | Ele não vai buscar |
| 8 | Ponha terra na `Blacklist` e jogue terra | Ele não vai buscar |
| 9 | Com um zumbi perto, no modo Contra-atacar, deixe o zumbi ferir você e jogue terra | Ele dá o choque primeiro e só depois busca |
| 10 | Aplique o Ultimate Module e jogue terra a 10 blocos de você | Ele vai buscar, e a dica mostra 12 blocos |
| 11 | Recolha e coloque o Cubit | A chave `Fetch` continua como estava |
| 12 | Com o jogo em português, passe o mouse em `Buscar` | `Busca itens a até 4 blocos de você` |

## Power Bank

As regras estão em [`energia.md`](../funcionalidades/energia.md#power-bank) e as receitas, em [`itens.md`](../itens.md#power-banks). Use `/give` para os materiais.

| # | Faça | Espere |
|---|---|---|
| 1 | Pegue um Cubit e abra o livro de receitas | As receitas da `Power Bank Casing`, da `Base Power Cell` e da Base Power Bank aparecem |
| 2 | Faça a carcaça (quartzo, ouro, quartzo / quartzo, vazio, quartzo / quartzo, obsidiana, quartzo), a célula Base (vazio, cobre, vazio / redstone, bloco de redstone, redstone / vazio, cobre, vazio) e junte as duas em qualquer posição | Sai a Base Power Bank vazia, não empilha, com o nome cinza, a barra verde vazia e a dica `0 / 210k FE` e `Charges at the Induction Dock` |
| 3 | Coloque-a na aba `Storage` de um Induction Dock com energia e sem Cubit | A carga da Power Bank sobe e o estoque do Dock desce na mesma quantidade |
| 3a | Ponha uma Power Bank carregada no slot `Redstone` de um Dock com pouco estoque | A Power Bank descarrega no estoque, a linha mostra `Input +X FE/t`, e vazia ela fica no slot |
| 3b | Tente pôr redstone na aba `Storage` | Não entra |
| 4 | Com um Cubit hibernando no mesmo Dock | A Power Bank só volta a receber quando a bateria do Cubit enche |
| 5 | Tente carregar a Power Bank num carregador de FE de outro mod, se houver | Não carrega (pergunta 905) |
| 6 | Ponha a Power Bank carregada no slot de energia de um Cubit com pouca bateria | A bateria sobe na recarga do tier, a linha mostra `Slot +500 FE/t` no Base, e a Power Bank desce o mesmo tanto |
| 7 | Deixe-a esvaziar | Ela continua no slot, e a linha mostra `Not charging` |
| 8 | Com a Base Power Bank no inventário, abra o livro | As receitas da `Advanced Power Cell` e da Advanced Power Bank aparecem |
| 9 | Monte lápis-lazúli em cima e embaixo, ouro nas laterais e uma Base carregada no centro | A nova sai com a mesma carga, o nome azul e 2 LEDs no ícone |
| 10 | Recolha um Cubit sem bateria e combine-o com uma Power Bank na grade | Sai o Cubit com a carga de religar, e a Power Bank fica na grade com o resto |
| 11 | Tente com uma Power Bank quase vazia, ou com um Damaged Cubit | A receita não aparece |
| 12 | Com o jogo em português | `Power Bank Base`, `Power Bank Avançada`, `Power Bank Elite`, `Power Bank Suprema` e `Carrega no Induction Dock` |

## Portas

As regras estão em [`portas.md`](../funcionalidades/portas.md). Use um Cubit Base com carga, o Tablet e uma parede de pedra com uma porta de ferro, uma porta de madeira, um alçapão de madeira na parede e um portão de cerca.

| # | Faça | Espere |
|---|---|---|
| 1 | Com o Tablet na mão, troque de app com Shift e a roda até `Portas` | A faixa do Tablet fica âmbar, e a barra mostra `Cubit Tablet · Doors` |
| 2 | Clique na metade de cima da porta de ferro | `Door marked for your Cubits · up to 64`, e uma seta âmbar aparece sobre a porta |
| 3 | Clique numa pedra | `Doors marks a door, a trapdoor or a fence gate`, e nada é marcado |
| 4 | Fique do outro lado da parede, com o Cubit seguindo você | O Cubit chega à porta, abre com uma faísca e o som da porta, passa e fecha depois de sair do bloco |
| 5 | Veja a bateria na tela do Cubit antes e depois | Cai 50 FE a mais na passagem |
| 6 | Repita com a porta de madeira sem marca | A porta fica fechada, e o Cubit não passa |
| 7 | Marque o portão de cerca e o alçapão e passe por eles | O Cubit abre e fecha os dois |
| 8 | Fique parado na porta aberta enquanto o Cubit passa | Ele espera você sair do bloco para fechar |
| 9 | Marque a porta de madeira, abra-a com a mão e deixe o Cubit passar | Ele não fecha a porta que você abriu |
| 10 | Shift + clique na porta marcada | `Door unmarked`, e a seta some |
| 11 | Com o jogo em português | O app se chama `Portas`, e os avisos aparecem em português |

## Tiers do Dock

As regras estão em [`energia.md`](../funcionalidades/energia.md#tiers-do-dock), e os números, em [`tiers.md`](../tiers.md#induction-dock). Use um Induction Dock novo, um Cubit Base com pouca carga e os três Modules.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque o Dock e olhe a torre | O topo da torre fica cinza. O Jade mostra `Tier: Base` |
| 2 | Shift + clique direito e abra a aba `Storage` | O título mostra `Induction Dock · Base`, e o inventário tem 3 linhas |
| 3 | Clique no Dock com o `Elite Module` | `The Induction Dock needs to be Advanced first`, e o Module fica na mão |
| 4 | Clique com o `Advanced Module` | `Induction Dock upgraded to Advanced!`, o Module é gasto, a faixa fica azul, o inventário passa a 4 linhas e o estoque, a 1,2 mi FE |
| 5 | Repita com o Elite e o Ultimate | Faixa roxa e depois dourada. No Ultimate, o corpo fica em netherite, o inventário tem 6 linhas e o estoque, 16 mi FE |
| 6 | Clique de novo com um Module do mesmo tier | `The Induction Dock is already Ultimate`, e nada é gasto |
| 7 | Ponha o Cubit Base com pouca carga no Dock Ultimate e olhe a linha de recarga na tela do Cubit | `+5.000 FE/t`, a taxa do Dock, e não a do Cubit |
| 8 | Guarde uma Power Bank vazia no inventário do Dock, sem Cubit na base | Ela carrega na taxa do Dock |
| 9 | Pela tela, ponha um card, uma peça e um Module no inventário | Os três entram. Um item de fora do mod não entra |
| 10 | Quebre o Dock Ultimate e olhe o item | O ícone mostra a faixa dourada e o corpo de netherite. Colocado de novo, ele volta Ultimate, com a energia e o inventário |
| 11 | Com outro player, clique com um Module num Dock seu | Nada acontece |
| 12 | Com o jogo em português | `Induction Dock evoluiu para Avançado!` e os outros avisos em português |

## Dock Solar

As regras estão em [`energia.md`](../funcionalidades/energia.md#dock-solar). Use um Induction Dock com algum tier, energia e itens no inventário, e os materiais do painel: vidro, 2 sensores de luz do dia, 1 bloco de redstone e 3 lingotes de ferro.

| # | Faça | Espere |
|---|---|---|
| 1 | Com um Induction Dock no inventário, abra o livro | A receita do `Dock Solar Panel` aparece |
| 2 | Faça o painel: vidro, vidro, vidro / sensor, bloco de redstone, sensor / ferro, ferro, ferro | Sai o `Dock Solar Panel`, com a textura dele |
| 3 | Junte o painel e o Dock na grade | Sai o `Solar Induction Dock`, com o painel no ícone, e a energia, o tier e o inventário do Dock |
| 4 | Coloque-o ao ar livre, de dia e sem chuva | O painel fica num mastro sobre a torre, com o topo na cor do tier, e a face do painel pulsa devagar |
| 5 | Abra a tela, vá à aba `Solar`, com o ícone do painel, e olhe o Jade | `Status: Generating`, `Now 25 FE/t` e `Tier max 25 FE/t` no Base. A aba principal fica igual à do Dock comum. O Jade mostra `Solar +25 FE/t`, e o estoque sobe sem cabos |
| 6 | Ponha um bloco de pedra sobre o painel | Na aba `Solar`, `Stopped` e `Why: Sky covered`, o pulso para e o estoque não sobe mais. Com vidro no lugar da pedra, ele volta a gerar |
| 7 | Use `/time set night`, e depois `/time set day` com `/weather rain` | `Why: Night` e depois `Why: Rain`, sem geração e sem pulso |
| 8 | Suba o tier com um Module | A geração passa a 100, 500 e 1.400 FE/t |
| 9 | Ponha um Cubit para carregar nele | Ele carrega como em qualquer Dock |
| 10 | Quebre o Dock Solar | Volta o `Solar Induction Dock`, com tudo, sem separar o painel |
| 11 | Olhe um Induction Dock comum | O topo da torre na cor do tier, e a faixa de antes, acima da base, sumiu |
| 12 | Com o jogo em português | `Induction Dock Solar`, `Painel Solar do Dock` e `Solar: sem sol` |

## Recharge Splash

As regras estão em [`energia.md`](../funcionalidades/energia.md#recharge-splash). Use dois Cubits Base seus, com metade da bateria e com vida perdida, um Cubit de outro player, se houver, e os materiais: garrafa de vidro, bloco de redstone, lingote de ferro, pólvora e lápis-lazúli.

| # | Faça | Espere |
|---|---|---|
| 1 | Faça a mistura na bancada, sem forma, com os cinco materiais | Sai 1 `Recharge Splash`, com a textura do frasco, e a pilha vai até 16 |
| 2 | Passe o mouse sobre o item | `Heals and recharges your Cubits over 3 s, one at a time` |
| 3 | Arremesse a mistura mirando no chão ao lado de um Cubit seu, fora dele | O frasco quebra com o respingo vermelho e as faíscas azuis, o Cubit solta faíscas, toca o som de recarga e fica com o rosto feliz |
| 4 | Olhe a tela do Cubit enquanto ele recebe | A bateria sobe aos poucos, por 3 s, até 45 mil FE a mais, e a vida sobe 5 |
| 5 | Arremesse outra antes dos 3 s acabarem | Ele não recebe nada, e a barra de ação mostra `Cubit is still recharging: wait 2 s`, ou os segundos que faltam |
| 6 | Arremesse com os dois Cubits a até 4 blocos do impacto | Os dois recebem |
| 7 | Arremesse a mais de 4 blocos de um Cubit, perto de um mob ou do Cubit de outro player | Nenhum deles recebe nada |
| 8 | Arremesse num Cubit sem carga | Ele religa ao passar de 5% |
| 9 | Desligue um Cubit pelo `Power` e arremesse nele | Ele recebe e continua desligado |
| 10 | Arremesse num Cubit hibernando no Dock | Ele recebe e continua no Dock |
| 11 | Recolha um Cubit no meio dos 3 s e coloque de novo | Ele fica só com o que já tinha recebido |
| 12 | Ponha a mistura num dispenser e ative | O dispenser só solta o item, sem arremessar |
| 13 | Com o jogo em português | `Mistura de Recarga` e `O Cubit ainda está recarregando: aguarde 2 s` |

## Tochas

As regras estão em [`inventario.md`](../funcionalidades/inventario.md#tochas). Use `/give` para as tochas, o Advanced Module e os blocos.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Inventory de um Cubit Base | O título `Exclusive slots` e o slot de tochas apagado, com `Unlocks at Advanced` na dica, acima do inventário do Cubit. A janela ficou mais alta |
| 2 | Tente pôr uma tocha no slot do Base | Não entra |
| 3 | Aplique o Advanced Module e ponha 64 tochas no slot | Entram, e a dica do slot vazio mostrava `Torches` |
| 4 | Tente pôr uma tocha de alma ou terra no slot | Não entram |
| 5 | Entre numa caverna escura com o Cubit seguindo | Ele põe tochas no chão ou na parede, a até 4 blocos dele, uma a cada 2 s no máximo, e a bateria cai 20 FE por tocha |
| 6 | Pare num corredor já iluminado | Ele não põe nenhuma tocha |
| 7 | Saia para um campo aberto de dia | Ele não põe tochas |
| 8 | Fique no campo até anoitecer | Quando a lanterna acende, ele passa a pôr tochas nos lugares escuros |
| 9 | Use F3 num lugar onde ele pôs tochas e ande em volta | Nenhum bloco de chão no raio tem `Light: 0 (0 sky, 0 block)` na parte de bloco |
| 10 | Desligue `Torches` na aba Settings | Ele para de pôr tochas |
| 11 | Recolha e coloque o Cubit | As tochas do slot e a chave continuam iguais |
| 12 | Encha o inventário do Cubit e jogue tochas no chão | A coleta não põe tochas no slot de tochas |
| 13 | Com o jogo em português, abra a aba Inventory | `Slots exclusivos` e `Tochas` na dica |

## Inventário do Dock

As regras estão em [`energia.md`](../funcionalidades/energia.md#inventário-do-dock) e em [`telas.md`](../funcionalidades/telas.md#aba-storage). Use `/give` para as Power Banks, a Toolbox e um funil.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a tela de um Dock com Shift + clique direito | As abas `Dock` e `Storage`, com os ícones novos. A aba `Dock` mostra o slot `Redstone`, sem o slot `Charge` |
| 2 | Abra a aba `Storage` | `Storage`, 27 slots vazios e o seu inventário abaixo |
| 3 | Tente pôr terra num slot | Não entra |
| 4 | Ponha duas Power Banks vazias e uma pilha de Toolbox, com o Dock com energia e sem Cubit | Só a primeira Power Bank carrega, e a segunda começa quando a primeira enche |
| 5 | Chame um Cubit com pouca bateria para o Dock | As Power Banks param de carregar, e voltam quando a bateria do Cubit enche |
| 6 | Clique no Dock com uma Power Bank na mão, e depois com uma Toolbox | Os dois vão para o inventário, e o Cubit não é chamado |
| 7 | Ponha um funil em cima do Dock com uma Power Bank e uma Toolbox | Os dois entram no inventário |
| 8 | Ponha um funil embaixo do Dock | A Toolbox sai na hora, e a Power Bank só sai cheia |
| 9 | Quebre o Dock e coloque-o de novo | O inventário volta igual, e o item do slot `Redstone` caiu no chão |
| 10 | Abra um mundo salvo antes, com uma Power Bank no slot `Charge` | A Power Bank aparece no primeiro slot da aba `Storage` |
| 11 | Com o jogo em português, abra a tela | As abas `Dock` e `Armazenamento` |
| 12 | Guarde um Cubit recolhido e um Damaged Cubit no Storage, pela tela | Os dois entram, um por slot, e continuam com o mesmo estado ao tirá-los |
| 13 | Clique no Dock com um Damaged Cubit na mão, e depois com um Cubit | O Damaged Cubit vai para o Storage, e o Cubit é posto na base |
| 14 | Com um funil embaixo do Dock, guarde um Cubit e um Damaged Cubit | Os dois saem pelo funil |
| 15 | Ponha no Dock um Cubit ferido e de bateria quase cheia | A bateria enche primeiro, a tela fica em `Complete` com o tempo que falta para a vida, e ele só sai cerca de 3 s depois de a vida encher |
| 16 | Repita o passo 15 e clique no Dock antes de a vida encher | O Cubit sai na hora |

## Retorno automático

As regras estão em [`energia.md`](../funcionalidades/energia.md#retorno-automático). Use um Cubit Base com cerca de 8% de bateria, entre o crítico de 5% e o retorno de 10%, e `/give` para os Docks e o Tablet.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Settings de um Cubit | A chave `Auto return`, ligada, com `Goes to a Dock at 10% battery` na dica |
| 2 | Coloque um Dock com energia e, a até 15 blocos dele, um Cubit com 8% que siga você | Ele mostra `Cubit: I need to recharge` uma vez, vai andando até o Dock e hiberna |
| 3 | Espere a bateria e a vida encherem | Ele sai do Dock e volta a seguir você |
| 4 | Desligue `Auto return` e repita o passo 2 | Ele continua seguindo, sem ir ao Dock |
| 5 | Ligue de novo e repita o passo 2 sem nenhum Dock por perto, e então coloque um Dock a até 15 blocos | Ele segue você até o Dock aparecer e então vai até ele, em poucos segundos |
| 6 | Com dois Docks no raio, marque o mais longe com o app Dock do Tablet e repita o passo 2 | Ele vai ao Dock marcado |
| 7 | Desligue `Auto return`, recolha e coloque o Cubit, e saia e entre no mundo | A chave continua desligada |
| 8 | Abra a aba Recursos do Tablet | A linha `Auto return`, com a mesma chave e a mesma dica |
| 9 | Com o jogo em português | `Retorno automático`, `Vai a um Dock com 10% de bateria` e `Cubit: Preciso recarregar` |

## Follow do Tablet

As regras estão em [`tablet.md`](../funcionalidades/tablet.md#cabeçalho). Use dois Cubits com carga, um Dock e o Tablet.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque dois Cubits. Abra o Tablet, escolha o que ficou desligado e veja o cabeçalho | Os quatro botões na mesma fileira, alinhados, com `Follow` por último, habilitado, com `Follows you · the one following now switches off` na dica |
| 2 | Clique em `Follow` | Ele liga e passa a seguir você, e o que seguia desliga onde está |
| 3 | Abra a tela do Cubit que segue agora | `Follow` desabilitado, com `Already following you` |
| 4 | Chame um dos Cubits para o Dock e, com ele hibernando, use `Follow` nele pelo Tablet | Ele sai do Dock na hora e segue você, e o outro desliga |
| 5 | Afaste-se mais de 20 blocos de um Cubit desligado e use `Follow` nele | Ele liga e teleporta para perto de você, como hoje ao ficar para trás |
| 6 | Escolha um Cubit sem carga | `Follow` desabilitado, com `Without charge to follow you` |
| 7 | Escolha um Cubit de chunk descarregado | O botão aparece desabilitado na tela da foto |
| 8 | Com o jogo em português | `Seguir`, `Segue você · o que seguia antes desliga` e `Já está seguindo você` |

## Modo área

As regras estão em [`modo-area.md`](../funcionalidades/modo-area.md). Use um Cubit Advanced com carga, um Dock com energia, o Tablet e alguns itens para jogar no chão.

| # | Faça | Espere |
|---|---|---|
| 1 | Com o Tablet na mão, troque para o app Área com Shift + roda | `Tablet · Area` na barra de ação |
| 2 | Clique com o botão direito num Cubit Base | `... has no area mode in its tier`, e nada muda |
| 3 | Clique no Cubit Advanced | Ele vira o Cubit escolhido, e a barra de ação pede o primeiro canto |
| 4 | Clique num bloco do chão | `First corner x y z · click the other one`. Uma caixa vai do canto até o bloco na mira, e a barra de ação mostra o tamanho, como `10 × 1 × 8` |
| 5 | Mire num bloco a mais de 16 blocos do primeiro canto | A caixa e o tamanho ficam vermelhos |
| 6 | Clique nesse bloco | `... is too big`, e o primeiro canto continua marcado |
| 7 | Clique num bloco dentro do limite | `... works in 10 × 1 × 8`. O Cubit para de seguir e vai andando até a área |
| 8 | Na área, abra a tela do Cubit pelo Tablet | Estado `In area`, em roxo, e, na aba Recursos, a linha `Area` com o tamanho e os cantos |
| 9 | Com a área vazia de itens | Ele dá voltas pelas bordas, de canto a canto |
| 10 | Desligue `Fetch` na aba Behavior e jogue itens dentro da área | Ele vai buscá-los, mesmo com `Fetch` desligada |
| 11 | Jogue um item fora da área, perto da borda | Ele não sai da área para buscar. O ímã ainda puxa o que estiver no raio |
| 12 | Encha o inventário dele | Estado `Inventory full`, em laranja, e ele para de buscar |
| 13 | Afaste-se mais de 20 blocos | Ele fica na área, sem teleportar até você |
| 14 | Com o Tablet na mão no app Área, sem marcar nada | A caixa roxa mostra a área do Cubit escolhido |
| 15 | Clique num Cubit, num canto e então Shift + clique direito | `Area marking cancelled`, e a caixa da marcação some |
| 16 | Use `Follow` na tela do Cubit | Ele sai da área e volta a seguir você, e a linha `Area` mostra `None` |
| 17 | Marque a área de novo, com um Dock no raio, e deixe a bateria dele cair a 10% | Ele vai ao Dock, carrega e, cheio, volta para a área, sem seguir você |
| 18 | Saia e entre no mundo | A área continua, e ele volta a trabalhar nela |
| 19 | Recolha o Cubit e coloque-o de novo | A área se perdeu, e ele segue você |
| 20 | Com o jogo em português | `Tablet · Área`, `Na área`, `Inventário cheio` e `Área` na aba Recursos |

## Cores dos apps do Tablet

As regras estão em [`tablet.md`](../funcionalidades/tablet.md#apps).

| # | Faça | Espere |
|---|---|---|
| 1 | Com o Tablet na mão, troque de app com Shift + roda | Uma linha só acima da hotbar, como `Cubit Tablet · Dock`, em negrito e em vermelho |
| 2 | Passe pelos apps | Open em azul, Alvo em verde, Dock em vermelho e Área em roxo |
| 3 | Olhe o Tablet na hotbar e no inventário | Uma faixa fina com a cor do app ativo, na parte de baixo do ícone |
| 4 | Passe o mouse sobre o Tablet no inventário | O nome é só `Cubit Tablet` |

## Inventário inteiro

As regras estão em [`telas.md`](../funcionalidades/telas.md#aba-inventory).

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a tela de um Cubit Base | 3 fileiras de 9, como antes |
| 2 | Abra a de um Advanced, um Elite e um Ultimate | 3 fileiras de 12, 15 e 18, sem rolagem, e a janela mais larga, com a mesma altura |
| 3 | Encha um Ultimate inteiro com o mesmo item | Todos os 54 slots mostram os itens |
| 4 | Olhe o seu inventário embaixo | Continua com 9 colunas, à esquerda |
| 5 | Shift + clique num item seu com o Cubit quase cheio | O item vai para o primeiro slot livre, mesmo no fim do inventário |
| 6 | Abra o Ultimate pela aba Inventário do Tablet | As 18 colunas cabem, e a janela do Tablet fica mais larga |
| 7 | Abra as abas Settings, Defense e Behavior de um Advanced e de um Ultimate | As linhas e as chaves vão até a borda da janela, e cada chave mostra a descrição na linha, em texto menor |
| 8 | Abra a aba Settings de um Base | A descrição fica só na dica, como antes |
| 9 | Abra a aba Recursos do Tablet | Cada chave mostra a descrição na linha. Uma linha bloqueada mostra só `Unlocks at Advanced` |
| 10 | Abra as abas Defesa e Filtros do Tablet com um Ultimate | Elas vão até a borda da janela mais larga |
| 11 | Abra a aba Behavior de um Advanced e de um Ultimate | O filtro fica à esquerda, em 1 fileira de 12 no Advanced e em 2 fileiras de 18 no Ultimate, e o botão `Blacklist` na borda direita. O seu inventário embaixo também fica à esquerda |
| 12 | Abra a aba Filtros do Tablet | O filtro à esquerda, com pelo menos 15 colunas, e o seu inventário à esquerda |
| 13 | Abra a aba Inventário do Tablet | O slot de energia é o primeiro da fileira `Slots exclusivos`, antes das tochas, sem o rótulo `Redstone`. Vazio, ele diz `Redstone` na dica |

## Descarga em baú

As regras estão em [`inventario.md`](../funcionalidades/inventario.md#descarregar-em-baú). Use um Cubit Base e um Advanced com carga, baús, um barril e o Tablet.

| # | Faça | Espere |
|---|---|---|
| 1 | Troque para o app Descarga com Shift + roda | `Cubit Tablet · Unload` em laranja, e a faixa do ícone laranja |
| 2 | Com o Cubit Base escolhido, clique num bloco de pedra | `Unload marks a block with an inventory` |
| 3 | Clique num baú | `Chest marked for Cubit · up to 1 · Shift to unmark`. Com o Tablet na mão, uma seta laranja sobre o baú, com `Cubit` e `Inventory 0 / 27` |
| 4 | Clique num barril | `Cubit has 1 chests · Shift and a click unmarks`, e o barril não é marcado |
| 4a | Ponha itens no Cubit e clique de novo no baú marcado | `Cubit is unloading in this chest`, e ele vai descarregar só nele |
| 4b | Segure Shift e clique no baú marcado | `Chest unmarked for Cubit`, e a seta some |
| 4c | Clique no baú de novo | Ele volta a ser marcado |
| 4d | Afaste-se do baú e, mirando num bloco qualquer, segure Shift e clique | `Chest unmarked for Cubit`, e a seta do baú some |
| 4e | Clique no barril | Ele é marcado no lugar do baú |
| 4f | Shift + clique de novo, sem baú marcado | `Cubit has no marked chest` |
| 5 | Troque para o app Open | A seta continua sobre o baú. Guarde o Tablet, e a seta some |
| 6 | Ponha itens no Cubit que segue e abra a tela dele pelo Tablet | O botão `Unload` na fileira, habilitado |
| 7 | Clique em `Unload` | Ele vai ao baú, descarrega tudo menos as tochas e a Power Bank, mostra `Cubit: Items unloaded` e volta a seguir. A seta mostra os slots usados |
| 8 | Encha o inventário dele e espere | O Cubit que segue não descarrega sozinho |
| 9 | Encha o baú e tente `Unload` | O botão fica desabilitado, com `No marked chest with room within reach` |
| 10 | Com o inventário vazio | `Unload` desabilitado, com `Nothing to unload` |
| 11 | Com o Cubit Advanced, marque dois baús e uma área a até 32 blocos deles | As duas setas, com o nome dele |
| 12 | Encha o inventário dele na área | Ele vai ao primeiro baú, depois ao segundo, e volta para a área. Sem `Items unloaded` |
| 13 | Encha os dois baús e o inventário dele | Ele fica na área com `Inventory full` e não sai do lugar |
| 14 | Desligue `Unload` na aba Settings e esvazie um baú | Ele continua parado com o inventário cheio. O botão `Unload` ainda funciona |
| 15 | Saia e entre no mundo | Os baús continuam marcados |
| 16 | Recolha e coloque o Cubit | Os baús se perderam, e as setas somem |
| 17 | Com o jogo em português | `Descarga`, `Descarregar`, `Baú marcado para Cubit` e `Inventário 0 / 27` |

## Local de morte

As regras estão em [`movimento.md`](../funcionalidades/movimento.md#local-de-morte) e a aba, em [`tablet.md`](../funcionalidades/tablet.md#aba-mortes). Use um Cubit Base, um Advanced e um Elite com carga, os Modules por `/give` e o Tablet. Para morrer, use `/kill @s` ou caia de um lugar alto, em modo sobrevivência.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Settings de um Cubit Base | A linha `Death point`, apagada, com `Unlocks at Advanced` |
| 2 | Com um Cubit Advanced seguindo você, morra no Overworld e renasça | O Cubit volta com você e mostra `Cubit: You fell at x, y, z · Overworld`, com a coordenada da morte. Ele não sai guiando |
| 3 | Abra a tela do Cubit pelo Tablet, na aba Mortes, com o ícone próprio | Uma linha com `Day N, hh:mm`, `Overworld`, a coordenada e a causa, e os botões `Guide` e `Delete` |
| 4 | Passe o mouse em `Guide` | Botão cinza, com `Unlocks at Elite` na dica |
| 5 | Morra mais duas vezes e abra a aba | Três linhas, da mais recente para a mais antiga |
| 6 | Clique em `Delete` na linha do meio | Só ela some |
| 7 | Troque para um Cubit Elite que siga você e clique em `Guide` numa morte no Overworld | O botão vira `Cancel`, e o Cubit anda na sua frente até o local, esperando quando você fica a mais de 8 blocos |
| 8 | Fique parado longe e depois ande até o local | O Cubit espera você e segue andando. A 3 blocos do local, mostra `Cubit: Here is where you fell` e volta a seguir. O botão volta a `Guide` |
| 9 | Peça `Guide` de novo e clique em `Cancel` | O Cubit para de guiar e volta a seguir |
| 10 | Peça `Guide` e use `Go to Dock` no Tablet | A guia é cancelada, e ele vai ao Dock |
| 11 | Morra no Nether e abra a aba no Overworld | A linha do Nether com `Guide` cinza e `The Cubit only guides in the Overworld` na dica |
| 12 | Peça `Guide` para um local cercado, sem caminho | Depois de cerca de 10 s, `Cubit: I can't find a way there`, e ele volta a seguir |
| 13 | Desligue `Death point` na aba Settings, morra e renasça | Nenhum texto. A morte entra na aba mesmo assim, e `Guide` mostra `The Death point switch is off` |
| 14 | Recolha e coloque o Cubit, e saia e entre no mundo | A chave continua desligada, e as mortes continuam na aba |
| 15 | Com o jogo em português | `Local de morte`, `Mortes`, `Dia N, hh:mm`, `Superfície`, `Guiar`, `Cancelar`, `Excluir` e `Cubit: Você morreu em x, y, z · Superfície` |

## Base do trabalho

As regras estão em [`trabalhos.md`](../funcionalidades/trabalhos.md#base-do-trabalho). Use um Cubit Base, um Advanced com carga, o card por `/give @s cubit:ml_fisher`, o Tablet e um baú.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Inventory de um Cubit Base | O slot do card, depois do de tochas, apagado, com `Unlocks at Advanced` |
| 2 | Num Cubit Advanced, ponha o card no slot | Ele entra, e a aba Work mostra o card, `Fisher` / `Pescador` e o estado |
| 3 | Tente pôr um segundo card | Não entra, porque o slot leva um |
| 4 | Com o Cubit seguindo você, ande | Ele segue como sempre, porque o card só age na área |
| 5 | Marque uma área com o app Área do Tablet | Ele vai para a área, com `Working · Fisher` no topo da tela e na lista do Tablet, e coleta e faz a ronda como sem card |
| 6 | Encha o inventário dele, sem baú marcado | Ele para, com a expressão de sono, `Hibernating · Inventory full` e um texto curto, uma vez |
| 7 | Bata nele e chegue perto com mobs | Ele não reage, não dá choque e não sai do lugar |
| 8 | Clique em `Resume` na aba Work, ainda cheio | Ele volta a hibernar e explica |
| 9 | Marque um baú com o app Descarga e clique em `Resume` | Ele descarrega no baú e volta a trabalhar |
| 10 | Faça ele hibernar de novo e use `Follow` no Tablet | Ele acorda e volta a seguir você |
| 11 | Faça ele hibernar e veja o `Unload` do Tablet | Desabilitado, com a dica de usar o `Resume` |
| 12 | Faça ele hibernar e tire o card do slot | A hibernação acaba, e ele volta ao comportamento sem card na área |
| 13 | Recolha o Cubit com o card e coloque de novo | O card continua no slot |
| 14 | Faça ele morrer com o card e conserte o Damaged Cubit | O card continua no slot |
| 15 | Com o jogo em português | `Trabalho`, `Trabalhando · Pescador`, `Hibernando · Inventário cheio` e `Retomar` |

## Pescador

As regras estão em [`pescador.md`](../funcionalidades/trabalhos/pescador.md). Use um Cubit Advanced e um Ultimate com carga, o card feito pela receita, o Tablet, um baú, um lago com mais de 5×5 blocos de água-fonte e 2 blocos de profundidade, e uma poça de 2×2 blocos.

| # | Faça | Espere |
|---|---|---|
| 1 | Faça o card na bancada: papel, comparador, papel / redstone, vara de pesca, redstone / papel, redstone, papel | Sai o `Machine Learning: Fisher` |
| 2 | Ponha o card num Cubit Advanced, seguindo você | Aparecem o chapéu, cobrindo o painel solar, a mochila com a vara e o peixe pequeno no canto esquerdo da tela. Ele continua seguindo, sem pescar |
| 3 | Apague a tela dele com o `Power` | O peixe some junto com a tela |
| 4 | Ligue de novo e marque, com o app Área, uma área com a borda do lago | Ele vai até a borda, fica de frente para a água, a vara passa para a garra e ele ergue o braço |
| 5 | Espere algumas pescas | A cada peixe, respingo e o som de recolher a linha no ponto da água, sem linha, sem boia e sem orbes de experiência. Os itens vão para o inventário, em cerca de 20 s cada um |
| 6 | Marque um baú com o app Descarga e encha o inventário dele | Ele descarrega no baú e volta a pescar |
| 7 | Desligue `Keep junk` na aba Work e espere várias pescas | Nada de lixo entra no inventário nem cai no chão |
| 8 | Faça a mesma área só com a poça de 2×2 | Ele pesca só peixe e lixo, sem tesouro, porque a água não é aberta |
| 9 | Marque uma área sem água | Ele hiberna com `Hibernating · No water in the area` e `I can't find water to fish here`, uma vez |
| 10 | Clique em `Resume`, ainda sem água | Ele volta a hibernar e repete o texto |
| 11 | Marque de novo a área do lago e clique em `Resume` | Ele acorda e volta a pescar |
| 12 | Coloque um bloco no ponto onde ele está pescando | Na pesca seguinte, ele procura outro ponto e vai até ele |
| 13 | Ponha 3 Pescadores na mesma área do lago | Cada um fica num ponto próprio da borda |
| 14 | Num lago só com espaço para 2, ponha 3 Pescadores | O terceiro hiberna sem água |
| 15 | Com chuva, compare o tempo das pescas | Elas saem mais rápido |
| 16 | Num Cubit Ultimate, pesque por alguns minutos | As pescas saem em cerca de 7 s, com mais tesouro e menos lixo |
| 17 | Recolha o Cubit e coloque de novo | O card e o `Keep junk` desligado continuam; a área e o baú precisam ser marcados de novo |
| 18 | Tire o card do slot | O chapéu, a mochila e o peixe somem, e ele volta ao comportamento sem card |
| 19 | Com o jogo em português | `Guardar lixo`, `Hibernando · Sem água na área` e `Não acho água para pescar aqui` |

## Fazendeiro

As regras estão em [`fazendeiro.md`](../funcionalidades/trabalhos/fazendeiro.md). Use um Cubit Advanced, um Elite e um Ultimate com carga, o card feito pela receita, o Tablet, um baú com sementes de trigo e cenouras, e um terreno de grama com água no meio.

| # | Faça | Espere |
|---|---|---|
| 1 | Faça o card na bancada: papel, comparador, papel / redstone, enxada de ferro, redstone / papel, redstone, papel | Sai o `Machine Learning: Farmer` |
| 2 | Ponha o card num Cubit Advanced, seguindo você | Aparecem o chapéu de palha cobrindo o painel solar, a mochila com a enxada e o trigo no canto esquerdo da tela |
| 3 | Marque com o app Área o terreno de grama com a água, e com o app Descarga o baú dentro dele | Ele ara só a grama a até 4 blocos da água, com a enxada na garra, e não ara longe dela |
| 4 | Espere | Ele busca as sementes no baú e planta, formando canteiros pela planta vizinha |
| 5 | Plante trigo maduro e cenoura madura na área, ou espere crescer | Ele colhe o maduro, guarda a colheita e replanta a mesma planta |
| 6 | Esvazie as sementes do baú e do inventário dele | A terra arada fica vazia, e ele segue sem hibernar |
| 7 | Ande pela terra arada com ele, pule nela com outro Cubit sem card | O Fazendeiro nunca a desfaz, e o Cubit sem card a desfaz como no jogo |
| 8 | Ponha uma muda e bambu na área | Ele não os colhe, pela tag `cubit:farmer_ignored` |
| 9 | Num Elite, ponha cana, cacto, abóbora e melancia na área | Ele colhe acima do bloco de baixo na cana e no cacto, e o fruto da abóbora e da melancia, deixando o caule |
| 10 | Num Advanced, ponha farinha de osso no slot de fertilizante, depois do slot do card | Ele usa a farinha nas plantas que acabou de plantar e replantar, com as partículas verdes. Sem o card do Fazendeiro, o slot some |
| 11 | Esvazie o slot e ponha farinha de osso no baú da Descarga dentro da área | Ele busca a farinha no baú e enche o slot |
| 12 | Encha o inventário dele, sem baú com espaço | Ele hiberna com `Hibernating · Inventory full` |
| 13 | Ponha 3 Fazendeiros na mesma área | Cada um vai a uma planta diferente |
| 14 | Recolha o Cubit e coloque de novo | O card e o fertilizante do slot continuam; a área e o baú precisam ser marcados de novo |
| 15 | Com o jogo em português | `Fertilizante` na dica do slot e `Aprendizado de Máquina: Fazendeiro` |

## Controle de velocidade

As regras estão em [`movimento.md`](../funcionalidades/movimento.md#controle-de-velocidade). Use um Cubit Advanced com carga, seguindo você, o Tablet e um bloco de magma perto.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra a aba Settings da tela do Cubit | A última linha é `Speed`, com o velocímetro, −, `100%` e +, e a dica `Walking speed, 50% to 100% of the tier · slower costs more FE per trip` |
| 2 | Clique em + | Continua em `100%` |
| 3 | Clique em − cinco vezes e mais uma | Desce de 10 em 10 até `50%` e para nele |
| 4 | Ande com ele seguindo você | Ele anda visivelmente mais devagar, e o teleporte, quando fica para trás, continua igual |
| 5 | Abra a aba Recursos do Tablet com ele escolhido | A última linha mostra `50%`. Mude para `80%` ali, e a aba Settings também mostra `80%` |
| 6 | Ponha fogo nele, ou deixe-o sobre o magma | Ele foge na velocidade cheia e volta aos 80% ao sair do perigo |
| 7 | Encha o inventário dele | Ele fica ainda mais lento, com os dois redutores juntos |
| 8 | Marque uma área e o baú da descarga, e mande-o ao Dock pelo Tablet | Na área, até o baú e até o Dock, ele anda a 80% |
| 9 | Recolha o Cubit e coloque de novo | Continua em `80%` |
| 10 | Suba de tier com um Module | Continua em `80%`, agora da velocidade do tier novo |
| 11 | Com outro player, tente mudar pelo Tablet ou pela tela | Nada muda, porque só o dono muda |
| 12 | Com o jogo em português | `Velocidade` e `Velocidade ao andar, de 50% a 100% do tier · mais devagar gasta mais FE por caminho` |

## Organizador

As regras estão em [`organizador.md`](../funcionalidades/trabalhos/organizador.md). Use um Cubit Elite e um Ultimate com carga, o card feito pela receita, o Tablet, três baús, uma fornalha com minério e carvão, e uma área com tudo dentro.

| # | Faça | Espere |
|---|---|---|
| 1 | Faça o card na bancada: papel, comparador, papel / redstone, funil, redstone / papel, redstone, papel | Sai o `Machine Learning: Organizer` |
| 2 | Tente pôr o card num Cubit Advanced | Ele não entra, e a dica diz `Elite` |
| 3 | Ponha o card num Cubit Elite | Aparecem o boné laranja cobrindo o painel solar, a mochila com o livro e pena e a caixa no canto da tela, e o livro e pena vai para a garra quando ele tira ou põe um item |
| 4 | Marque a área com o app Área e os três baús com o app Descarga, com pedras no segundo baú | O Elite aceita até 9 baús |
| 5 | Com o app Marcar, marque a fornalha | Aparece a seta sobre ela, e o Elite aceita até 8 marcas |
| 6 | Espere a fornalha fundir e jogue pedras e terra no chão da área | Ele tira o resultado e o balde vazio por baixo, sem mexer no minério nem no carvão, pega os itens do chão e guarda as pedras no segundo baú e o resto no primeiro com espaço |
| 7 | Jogue uma picareta encantada e uma comum | As duas não se misturam pela regra do baú com mais do mesmo item |
| 8 | Encha os três baús | Ele fica com o que não cabe, avisa `Cubit: No room for some items` e, cheio, hiberna com `Hibernating · Inventory full` |
| 9 | Na aba Trabalho, troque `Mode` para `Supply` e, com o app Marcar, marque a face de cima de uma fornalha vazia e a face do lado de outra | Aparece a aba `Supply`, com o card como ícone, o título `Supply · 2 of 8` e duas linhas, com ↑ e →, o slot fantasma, `64` e − e + |
| 10 | Na aba `Supply`, clique no slot do primeiro pedido com minério de ferro e deixe 16, e no segundo com carvão e deixe 8, com os itens nos baús | Ele completa a entrada com 16 minérios e o combustível com 8 carvões, sem passar disso, e repõe quando a fornalha consome. A mão vazia no slot limpa o pedido |
| 11 | Use Shift no + | A quantidade anda de 8 em 8, até 64 |
| 12 | Com outro player, tente marcar ou mudar um pedido | Nada muda, porque só o dono pode |
| 13 | Recolha o Cubit e coloque de novo | O card e o modo continuam, e a área, os baús e as marcas precisam ser marcados de novo |
| 14 | Num Ultimate, repita os passos 4 e 5 | Ele aceita até 18 baús e 16 marcas, e trabalha mais rápido |
| 15 | Ponha 3 Organizadores na mesma área | Cada um pega um item diferente, sem duplicar nem perder itens |
| 16 | Com o jogo em português | `Aprendizado de Máquina: Organizador`, `Modo`, `Organizar` e `Abastecer` |

## Cozinheiro

As regras estão em [`cozinheiro.md`](../funcionalidades/trabalhos/cozinheiro.md). Use um Cubit Elite e um Ultimate com carga, o card feito pela receita, o Tablet, dois baús, um fumeiro, uma fornalha, carne crua, carvão, tábuas de madeira e minério de ferro, e uma área com tudo dentro.

| # | Faça | Espere |
|---|---|---|
| 1 | Faça o card na bancada: papel, comparador, papel / redstone, fumeiro, redstone / papel, redstone, papel | Sai o `Machine Learning: Cook` |
| 2 | Ponha o card num Cubit Elite | Aparecem a touca de chef cobrindo o painel solar, a mochila com a concha de pau e a tigela no canto da tela |
| 3 | Marque a área, os dois baús com o app Descarga e, com o app Marcar, o fumeiro e a fornalha | O app Marcar aceita o fumeiro e a fornalha, e num baú comum avisa que só marca fornos |
| 4 | Ponha carne crua, carvão, tábuas e minério de ferro nos baús | Ele enche primeiro o fumeiro com a carne e o carvão, pela entrada e pelo combustível, e só depois a fornalha |
| 5 | Espere | Ele tira a carne assada pela saída e a guarda nos baús, sem nunca levar o minério nem queimar as tábuas |
| 6 | Deixe uma batata crua na entrada da fornalha | Ele não a tira e só completa com batata |
| 7 | Tire a carne assada da fornalha com a mão | A experiência do forno vem junto |
| 8 | Esvazie o carvão dos baús | Os fornos ficam sem combustível novo, e ele espera sem hibernar |
| 9 | Encha o inventário dele, sem baú com espaço | Ele hiberna com `Hibernating · Inventory full` |
| 10 | Ponha 3 Cozinheiros na mesma área | Cada um vai a um forno diferente |
| 11 | Num Ultimate, marque até 8 fornos | Ele aceita 8 e trabalha mais rápido |
| 12 | Recolha o Cubit e coloque de novo | O card continua, e a área, os baús e os fornos precisam ser marcados de novo |
| 13 | Com o jogo em português | `Aprendizado de Máquina: Cozinheiro` |

## Engineering Desk

As regras estão em [`mesa-de-engenharia.md`](../funcionalidades/mesa-de-engenharia.md). Esta seção cobre o PR 1: o bloco, a energia, os inventários, o pedido, as receitas na mesa, os lados, o modelo sem animações e a tela funcional. Use um mundo novo em sobrevivência, ferro, vidro, baús, uma mesa de craft, redstone, obsidiana, um funil, um baú, um comparador e um Advanced Module, e, para o passo 17, um segundo player.

| # | Faça | Espere |
|---|---|---|
| 1 | Abra o livro de receitas da mesa de craft | Aparece a `Engineering Desk`, desde o início |
| 2 | Faça a mesa: painel de vidro, redstone, painel de vidro / baú, mesa de craft, baú / três ferros | Sai a `Engineering Desk`, com o modelo 3D no inventário |
| 3 | Coloque a mesa | Ocupa 2 blocos, com a outra metade à sua direita, o laptop de frente para você e o texto da primeira mesa na barra de ação |
| 4 | Coloque uma segunda mesa | O texto não aparece de novo |
| 5 | Tente colocar com um bloco à direita | Nada é colocado, e o item continua na mão |
| 6 | Tente fazer a `Toolbox` na mesa de craft do jogo | Nenhuma receita do mod aparece nem sai na mesa de craft |
| 7 | Abra a mesa | Abas Projeto e Estoque, catálogo com todas as receitas do mod: o Cubit, as 4 peças e a Toolbox liberados; as outras escurecidas e acinzentadas, com `Locked` e sem os materiais no tooltip |
| 8 | Clique na `Toolbox` no catálogo | A grade mostra as marcações e o pedido fica `0 / 1` |
| 9 | Use + e, com Shift, + de novo | O pedido sobe de 1 e depois de 8, até 8 no Base |
| 10 | Na aba Estoque, ponha 7 ferros, 1 redstone e 1 obsidiana na entrada; ponha terra | Os materiais entram, e a terra não |
| 11 | Sem energia | `No energy`, e nada sai da entrada |
| 12 | Ponha redstone no slot de energia | A barra sobe 5k por pó, e começa `Crafting` |
| 13 | Espere 5 s | Sai uma `Toolbox` na saída, e a energia cai 2.500 FE |
| 14 | Esvazie a entrada no meio do pedido | `Missing materials`, e o progresso continua quando os materiais voltam |
| 15 | Ponha um bloco de redstone ao lado | `Redstone signal`, e a mesa pausa |
| 16 | Ligue um comparador | O sinal acompanha o que falta do pedido, de 15 a 0 |
| 17 | Com o segundo player, use a mesa, aplique um Module e tente quebrá-la | Ele usa a mesa, mas não sobe o tier nem a quebra |
| 18 | Clique com o Advanced Module | Sobe para Advanced, a faixa do tampo fica azul e os inventários passam a 18 slots |
| 19 | Ponha um funil à esquerda, um à direita, um em cima e um baú embaixo | O da esquerda põe na entrada, o da direita tira da saída, o de cima não faz nada |
| 20 | Quebre a metade direita | Cai uma mesa só, com o tooltip do tier, da energia, do pedido e dos itens guardados |
| 21 | Tente pôr essa mesa num shulker box e no Dock | Não entra. Vazia, entra |
| 22 | Coloque a mesa de novo | Volta com o tier, a energia, os inventários e o pedido |
| 23 | Conserte um Damaged Cubit e religue um Cubit com uma Power Bank na mesa | Saem como antes, e a Power Bank volta para a saída com a carga que sobrou |
| 24 | Faça as 4 peças do Cubit, deixe-as na saída, ponha o Casing na entrada e escolha o Cubit | A mesa puxa o Visor, o Treads e o Neural Core da saída para a entrada e começa o Cubit |
| 25 | Peça 2 Cubits, com as peças de só um na saída | Ela faz um Cubit, puxa as peças e pausa em `Missing materials` até as peças do segundo chegarem |
| 26 | Com o jogo em português | `Mesa de Engenharia`, `Projeto`, `Estoque` e os estados em português |

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

## Teste da versão

Antes da `main`, a versão é testada com o jar, e não com o `make client`, numa instância só com o mod e suas dependências e, depois, numa do Iron Meridian (regra 35 e perguntas 767 a 769). As instâncias ficam no Prism Launcher. Cada parte testada vira uma resposta em [`perguntas/`](../perguntas/README.md), e um problema vira uma issue própria.

O teste com outro player não é feito no jogo: as GameTests com um player que não é o dono cobrem o passo 9 do Induction Dock, o 6 do `/cubit reset` e o 13 da cor e do nome (perguntas 770 a 772).

### Preparar o jar

1. Feche o jogo e rode `make build`, na branch da versão. O jar fica em `build/libs/cubit-X.Y.Z.jar`.
2. Baixe do Modrinth as dependências nas versões do `gradle.properties`: GeckoLib, LambDynamicLights e Jade, as mesmas do Iron Meridian.

### Instância só com o mod

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | No Prism, crie uma instância em **Adicionar instância → Personalizado**, com o Minecraft 1.21.1 e o NeoForge 21.1.265 | A instância abre sem mods |
| 2 | Em **Pasta → `minecraft/mods`**, coloque o jar do Cubit, o GeckoLib e o LambDynamicLights, sem o Jade, e abra | O jogo abre sem erro, e a tela de Mods mostra o Cubit, com o ícone e a descrição |
| 3 | Num mundo novo, rode [Colocar e recolher](#colocar-e-recolher), [Inventário](#inventário), [Induction Dock](#induction-dock), [Cubit na base](#cubit-na-base), [Lanterna](#lanterna), [Scanners](#scanners) e [Falas](#falas) | Como em cada seção, agora a partir do jar |
| 4 | Rode [Telas](#telas) e [Tradução](#tradução), ainda não feitas no jogo | Como em cada seção |
| 5 | Num mundo novo, em sobrevivência, rode [Receitas](#receitas), anotando os passos 1, 4 e 7 | A receita do Cubit aparece com o diamante, a do Dock com o Cubit, e o L espelhado também faz o Dock (pergunta 351) |
| 6 | Rode o passo 5 do [`/cubit reset`](#comando-cubit-reset), com o chunk descarregado | Como no passo |
| 7 | Em **Opções → Vídeo**, abra as opções do LambDynamicLights e anote o valor padrão da luz de entidades, sem mudar | Com o padrão, a lanterna do Cubit ilumina o chão no escuro (perguntas 461 e 719) |
| 8 | Saia, coloque o jar do Jade em `mods/` e rode [Jade](#jade) | Como na seção |

### Iron Meridian

| # | Passo | Resultado esperado |
|---|---|---|
| 1 | No Prism, clique com o botão direito na instância do Iron Meridian → **Copiar** | Uma cópia separada, sem mexer na instância nem nos mundos de sempre |
| 2 | Na cópia, em **Editar instância → Configurações → Comandos personalizados**, tire o comando de pré-lançamento do packwiz | A cópia abre sem sincronizar com o pack |
| 3 | Em **Pasta → `minecraft/mods`** da cópia, coloque só o jar do Cubit, porque o GeckoLib, o LambDynamicLights e o Jade já estão no pack, e abra | O jogo abre sem erro, e a tela de Mods mostra o Cubit |
| 4 | Num mundo novo, coloque um Cubit, ande com ele e abra as telas do Cubit e do Dock | Segue, aparece com a textura, toca os sons, e as telas abrem |
| 5 | Ligue um gerador de FE de um mod do pack ao Dock, com um cabo do pack | O estoque do Dock sobe, e a tela mostra a entrada média de FE, até 5.000 FE/t |
| 6 | À noite, com os shaders do pack, deixe a lanterna acender | O chão clareia em volta do Cubit |
| 7 | Mire no Cubit e no Dock | O Jade mostra o tier, a bateria e o estado do Cubit e o estoque e o estado do Dock |

### Capturas da galeria

Na instância só com o mod, tire capturas com F2 do Cubit seguindo, da tela do inventário, da aba Settings, do Dock com um Cubit na base, da caixa de um minério e das antenas coloridas. Elas ficam na pasta `screenshots` da instância, e só elas vão para a galeria das plataformas (pergunta 747).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Zennd | 2026-11-21 14:00 | Adiciona a cascata ao roteiro da Engineering Desk (perguntas 1500 a 1503). |
| Zennd | 2026-11-21 13:30 | Adiciona o roteiro da Engineering Desk, PR 1 (perguntas 1424 a 1494). |
| Marcelo Fabiano | 2026-11-21 12:00 | Adiciona o roteiro da Recharge Splash (perguntas 1399 a 1423). |
| Marcelo Fabiano | 2026-11-21 09:10 | Adiciona o roteiro do Dock Solar e leva a faixa para o topo (perguntas 1384 a 1392). |
| Marcelo Fabiano | 2026-11-21 07:30 | Adiciona o roteiro dos tiers do Dock (perguntas 1373 a 1382). |
| Marcelo Fabiano | 2026-11-21 04:30 | Adiciona o roteiro das portas (perguntas 1359 a 1371). |
| Marcelo Fabiano | 2026-11-21 02:35 | Troca as receitas dos Modules pela cruz encadeada (perguntas 1357 e 1358). |
| Marcelo Fabiano | 2026-11-21 02:05 | Troca o Visor para diamante, obsidiana, diamante (pergunta 1356). |
| Marcelo Fabiano | 2026-11-21 01:45 | Troca a célula Base e o upgrade pela cruz (perguntas 1354 e 1355). |
| Marcelo Fabiano | 2026-11-21 01:30 | Troca as grades da Carcaça e do Visor (pergunta 1353). |
| Marcelo Fabiano | 2026-11-21 01:05 | Troca as receitas do Cubit e das Power Banks pelas peças de montagem (perguntas 1343 a 1350). |
| Marcelo Fabiano | 2026-11-20 23:55 | Troca as receitas do Cubit, do Dock e dos Modules (perguntas 1331 e 1332). |
| Marcelo Fabiano | 2026-11-19 23:04 | Adiciona o roteiro do Cozinheiro. |
| Marcelo Fabiano | 2026-11-19 22:36 | Troca o visual do Organizador no roteiro (perguntas 1307 e 1308). |
| Marcelo Fabiano | 2026-11-19 22:13 | Leva o roteiro do Organizador à aba `Supply` (perguntas 1303 a 1305). |
| Marcelo Fabiano | 2026-11-19 21:35 | Adiciona o roteiro do Organizador. |
| Marcelo Fabiano | 2026-11-19 20:19 | Adiciona o roteiro do controle de velocidade. |
| Marcelo Fabiano | 2026-11-15 10:40 | Adiciona o roteiro do Fazendeiro. |
| Marcelo Fabiano | 2026-11-14 23:10 | Adiciona o roteiro do Pescador. |
| Marcelo Fabiano | 2026-11-14 20:53 | Adiciona o roteiro da base do trabalho. |
| Marcelo Fabiano | 2026-11-14 18:47 | Adiciona o roteiro do local de morte. |
| Marcelo Fabiano | 2026-11-14 16:33 | Deixa o filtro e o inventário do player à esquerda. |
| Marcelo Fabiano | 2026-11-14 16:18 | Adiciona o filtro alinhado e o slot de energia na fileira do Tablet. |
| Marcelo Fabiano | 2026-11-14 16:00 | Adiciona as abas que acompanham a janela. |
| Marcelo Fabiano | 2026-11-14 15:36 | Troca a rolagem do inventário pelo inventário inteiro em mais colunas. |
| Marcelo Fabiano | 2026-11-14 15:23 | Adiciona o Shift + clique de longe na descarga. |
| Marcelo Fabiano | 2026-11-14 14:40 | Acrescenta o clique no baú marcado e o Shift para desmarcar. |
| Marcelo Fabiano | 2026-11-14 14:15 | Adiciona a descarga em baú. |
| Marcelo Fabiano | 2026-11-14 13:20 | Adiciona as cores dos apps do Tablet. |
| Marcelo Fabiano | 2026-11-14 12:40 | Adiciona o modo área. |
| Marcelo Fabiano | 2026-11-14 11:20 | Adiciona o `Follow` do Tablet. |
| Marcelo Fabiano | 2026-11-14 10:30 | Adiciona o retorno automático. |
| Marcelo Fabiano | 2026-11-13 15:10 | Mantém o olhar no alvo até ele ser eliminado. |
| Marcelo Fabiano | 2026-11-13 14:50 | Adiciona o olhar para o alvo do choque. |
| Marcelo Fabiano | 2026-11-13 13:45 | Adiciona as tochas e as dicas das chaves da aba Settings. |
| Marcelo Fabiano | 2026-11-13 13:00 | Adiciona a saída do Dock com a bateria e a vida cheias. |
| Marcelo Fabiano | 2026-11-13 12:50 | Adiciona o Cubit e o Damaged Cubit ao roteiro do inventário do Dock. |
| Marcelo Fabiano | 2026-11-13 11:30 | Adiciona o inventário do Dock e troca o slot de carga nos passos da Power Bank. |
| Marcelo Fabiano | 2026-11-13 09:50 | Adiciona a busca dos itens do chão e as dicas das chaves da aba Behavior. |
| Marcelo Fabiano | 2026-11-12 23:20 | Atualiza o tempo do ímã no roteiro. |
| Marcelo Fabiano | 2026-11-12 23:10 | Acrescenta os dois slots do Dock ao roteiro da Power Bank. |
| Marcelo Fabiano | 2026-11-12 22:50 | Adiciona o roteiro da Power Bank. |
| Marcelo Fabiano | 2026-11-12 21:45 | Adiciona o roteiro da coleta e do ímã. |
| Marcelo Fabiano | 2026-11-12 18:10 | Adiciona as chaves da aba Settings. |
| Marcelo Fabiano | 2026-11-12 15:50 | Adiciona o modo Focus. |
| Marcelo Fabiano | 2026-11-12 13:40 | Adiciona a rolagem do inventário ao upgrade de tier. |
| Marcelo Fabiano | 2026-11-12 11:40 | Adiciona o choque e os modos de defesa. |
| Marcelo Fabiano | 2026-11-12 08:50 | Adiciona as receitas dos Modules. |
| Marcelo Fabiano | 2026-11-11 23:40 | Adiciona o upgrade de tier e atualiza o colar no ícone. |
| Marcelo Fabiano | 2026-11-08 01:17 | Adiciona o teste da versão, com as instâncias no Prism, e troca o teste com outro player pelas GameTests. |
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
