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

## Lava

Use um balde de lava, com `/give @s minecraft:lava_bucket`. Ele tem 25 de vida e morre em cerca de 3 s na lava. Se morrer, vira um `Damaged Cubit`, que não queima.

| # | Faça | Espere |
|---|---|---|
| 1 | Coloque lava no bloco do robô ativo | Ele se move devagar rumo à borda, e pode morrer antes de sair |
| 2 | Coloque lava no bloco de um robô desligado | Ele fica parado e morre |

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
