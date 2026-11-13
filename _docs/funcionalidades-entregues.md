# Funcionalidades entregues

O que o Cubit já faz, do primeiro commit até o merge do PR #126. As regras completas ficam nas páginas de [`funcionalidades/`](funcionalidades/), e as respostas em [`perguntas/`](perguntas/README.md) prevalecem. Os números são os padrões do tier Base, o único no config até a Fase 4, e ficam em [`tiers.md`](tiers.md).

## Visão geral

| Área | O que entrou |
|---|---|
| Cubit no mundo | Craft, colocar, recolher, estado guardado no item, dono |
| Movimento | Seguir, parar, teleportar, subir, descer, água, voltar com o dono |
| Vários Cubits | Quantos quiser, um segue, `/cubit reset` |
| Perigos | Fogo, lava, blocos que ferem, proteção do dono |
| Vida e morte | Damaged Cubit, resgate no vazio, Toolbox |
| Inventário | 27 slots, tela própria, barras de bateria e vida |
| Energia | Bateria, consumo por situação, slot de energia, painel solar |
| Induction Dock | Base de recarga, hibernação, fila, chamada |
| Personalidade | Sons, expressões, animações, textos curtos, falas |
| Visual | Modelo, colar do tier, indicador de bateria, cor da carcaça, nome |
| Lanterna | Luz dinâmica, halo nas antenas, brilho nos olhos |
| Scanners | Minérios e mobs, filtro, alertas, caixas, seta, cores |
| Integrações | Jade, opcional |
| Técnica | Config, receitas em JSON, FE, servidor dedicado, gates |

## Cubit no mundo

- **Craft:** na mesa, com redstone, lâmpada de redstone, lingotes de ferro, diamante e obsidiana. Sai com 25% de bateria e a vida cheia, e a receita aparece no livro ao ter um diamante.
- **Colocar:** com o item na mão. Toca `placed.ogg` e, ativo, mostra a expressão feliz. O dono é quem coloca.
- **Recolher:** Shift + clique direito, em qualquer estado.
- **Estado no item:** tier, energia, vida, inventário, slot de energia, cor, nome e as chaves `Lamp`, `Scanners` e `Chatter`, em data components (`cubit:robot_state` e outros). Recolher, morrer e consertar nunca apagam nem duplicam nada.
- **Aba no criativo:** `cubit:main`, com o Cubit, o Damaged Cubit, a Toolbox e o Induction Dock.

## Movimento

- **Seguir:** anda até o dono e para a 3 blocos.
- **Teleporte:** longe demais, a cerca de 20 blocos, aparece perto do dono, só na mesma dimensão.
- **Terreno:** sobe um bloco por vez, porque tem esteiras, e a rota desce de até 5 blocos, sem dano de queda.
- **Água e lava:** a rota evita as duas. Na água, afunda e anda no fundo, sem se afogar.
- **Volta com o dono:** quando o dono morre e renasce longe, o Cubit que segue volta com ele, em qualquer dimensão, se ainda estiver carregado.
- **Só o ativo se move:** desligado ou hibernando, fica parado e não teleporta.

## Vários Cubits

- **Sem limite:** o player tem quantos Cubits quiser, e só um o segue por vez.
- **Ligar:** com carga, o clique direito num Cubit desligado o liga, e ele passa a seguir, desligando o que seguia antes.
- **Colocar com outro seguindo:** o novo fica parado e desligado.
- **`/cubit reset`:** limpa o vínculo de quem segue, sem permissão de operador, só no próprio vínculo. Responde `Follow link cleared.` ou `No Cubit is following you.`

## Perigos

- **Fogo:** ativo e em chamas, corre a 1,25× para a água mais próxima, a até 8 blocos, ou anda ao acaso, nunca parado. Toca o curto elétrico em loop.
- **Lava:** leva dano como um player sem armadura e tenta sair pela saída mais próxima. No Base, costuma morrer antes.
- **Blocos que ferem:** magma, fogueira acesa, cacto, arbusto de frutas, rosa do wither, estalactite e blocos de outros mods com a mesma classificação. O Cubit sai de cima e volta a seguir.
- **Proteção do dono:** nenhum dano que o jogo atribua ao dono fere o Cubit. O fogo de um isqueiro fere, porque não tem autor.

## Vida e morte

- **Vida:** 25 pontos, recuperada no Induction Dock.
- **Morte:** o Cubit vira um `Damaged Cubit`, com o inventário preservado e a bateria zerada. O item não queima, não some, não sofre dano de nada e não pode ser colocado no mundo.
- **Resgate no vazio:** o item volta à última posição segura, ao inventário do dono ou ao spawn do mundo.
- **Conserto:** `Damaged Cubit` + `Toolbox`, na grade do inventário ou na mesa. Sai com 25% da vida e a bateria zerada. A `Toolbox` é feita com 7 ferros, 1 redstone e 1 obsidiana e empilha até 16.
- **Dono offline:** o mundo anota o Cubit morto e limpa o vínculo quando o dono entra.

## Inventário

- **27 slots:** clique direito abre a tela, só para o dono.
- **Tela:** a tela própria do Cubit, descrita em [Telas](#telas), com o slot de energia, as barras e as chaves.
- **Excedente:** se o config reduzir os slots, o que sobra cai no chão. Nada é apagado.

## Energia

- **Bateria:** 300 mil FE, sempre inteira, sem criar nem perder FE nas transferências.
- **Consumo:** andar gasta 15 FE/t, e parado não gasta. O inventário cheio e a água somam 5 FE/t cada um, e subir soma 15 FE por bloco. Uma condição negativa (fogo, lava, efeito nocivo ou bloco que fere) soma 25 FE/t, mesmo parado.
- **Gastos parado:** a lanterna acesa, 1 FE/t, e as varreduras dos scanners, 50 FE cada.
- **Sem carga:** desliga ao zerar e religa sozinho com 5%.
- **Bateria crítica:** com 5%, mostra a expressão de sono, o indicador vermelho e o texto `Cubit: Critical battery`.
- **Slot de energia:** aceita redstone, 5.000 FE por pó e 45.000 FE por bloco, a 500 FE/t, inclusive com o Cubit desligado e o clique com redstone na mão.
- **Painel solar:** com o Cubit ativo, de dia e com o céu à vista, dá 5 FE/t. Coberto, no Nether ou no End, não carrega. A célula pulsa enquanto carrega.

## Induction Dock

- **Bloco:** 2 de altura, como uma porta, com torre e placa em U. A receita é em L, com redstone, lingotes de ferro e um baú.
- **Dono:** quem o coloca. Só o dono usa e quebra, e explosões e pistões não o afetam. Um operador em criativo pode remover um Dock sem dono.
- **Energia:** estoque de 100 mil FE, entrada de até 5.000 FE/t por cabos de qualquer mod ou redstone no slot, também por funil.
- **Chamar:** o clique no Dock chama o Cubit ligado mais próximo, dentro de 15 blocos. Colocar o item do Cubit em cima também funciona.
- **Na base:** o Cubit hiberna, com o inventário acessível, recarrega a 500 FE/t e recupera toda a vida em cerca de 1 minuto. Cheio, fica cerca de 3 s com a tela completa e sai.
- **Fila:** cada Dock recebe um Cubit por vez, e os outros esperam na ordem de chegada ou vão a outro Dock livre.
- **Preso:** sem avançar por 5 s, teleporta ao Dock.
- **Tela do Dock:** desligada, ociosa, carregando, completa ou sem energia, com LEDs que acendem no escuro.

## Sons

Ouvidos por todos os players por perto, no controle "Criaturas amigáveis":

- **Contínuos:** esteiras ao andar, curto elétrico em chamas e recarga.
- **Uma vez:** ligar, desligar, ser colocado, bipe padrão ao trocar de expressão e nas falas, alerta de mob hostil e alerta de minério raro.
- **Dano:** `short_circuit.ogg` substitui o som de dano do jogo.

Os arquivos do choque e do modo agressivo já existem e tocam a partir da Fase 4.

## Expressões

A tela do rosto mostra: normal, com piscada e olhar que acompanha a subida e o dono, feliz, sono, carregando, desligada, triste, quase morto, preocupado, alerta e surpreso. Os olhos brilham no escuro, e a prioridade segue `perguntas/fase-3-sentidos.md`.

## Animações

- **Corpo:** andar, parado, olhar para o dono, desligado e hibernando.
- **Braços:** sobem por cerca de 2 s ao levar dano.
- **Olhar e corpo:** os olhos acompanham um pulo ou um nível diferente do dono, e o corpo inclina de leve para ele.
- **Esteiras:** animadas pela distância andada.

## Visual

- **Modelo próprio:** corpo branco e arredondado, tela no rosto, duas antenas, braços com garras, esteiras, painel solar em relevo, indicador de bateria nas costas e bobina embaixo.
- **Colar do tier:** com a fita e as faixas nos braços, em cinza no Base. Azul, roxo e dourado já existem para os outros tiers.
- **Indicador de bateria:** 4 níveis, verde e vermelho na bateria crítica.
- **Cor da carcaça:** um corante nas 16 cores do jogo, só do dono e gastando 1. O ícone do item muda junto.
- **Nome:** com etiqueta ou bigorna, sempre visível acima do Cubit e no título do inventário.

## Textos curtos

Uma camada própria, logo acima da hotbar, só para o dono, a 75% da fonte do jogo e com o prefixo `Cubit: `. Cada texto espera 25 s para se repetir. Inclui `Critical battery`, `That hurts!`, `Careful!` e os avisos dos scanners.

## Lanterna

- **Luz dinâmica:** pelo LambDynamicLights, sem blocos de luz invisíveis. O nível é 10, e a luz real é branca.
- **Quando acende:** ativo, com a chave `Lamp` ligada, abaixo da luz 8, e apaga a partir de 10. Gasta 1 FE/t.
- **Claridade da tela:** uma luz fraca, de nível 4, enquanto a tela brilha, sem gasto.
- **Efeitos:** pontas das antenas acesas, halo suave em cada ponta e brilho nos olhos.

## Scanners

- **Quem varre:** só o Cubit ativo que segue, com a chave `Scanners` ligada.
- **Área:** uma esfera em volta do Cubit, de 5 blocos para minérios e 8 para mobs, só em chunks carregados e em fatias de 2.000 blocos por tick.
- **Minérios:** a cada 35 s, por 50 FE. Usa a tag `c:ores`, então reconhece minérios de qualquer mod.
- **Minérios raros:** sucata de netherita, esmeralda, diamante, ouro, lápis, redstone e ferro, numa lista do config.
- **Mobs:** a cada 10 s, por 50 FE, tudo o que o jogo marca como inimigo, inclusive de outros mods e atrás de paredes, e o mob neutro que ataca o dono.
- **Filtro:** clique no Cubit com um minério na mão, sem gastar o item. Responde `Cubit: Looking for Coal Ore` e `Cubit: Filter cleared`.
- **Só o novo avisa:** o mesmo mob ou bloco não avisa de novo enquanto continuar no raio.
- **Avisos:** `Cubit: Diamond Ore!!!`, com som e surpresa, para minério raro. `Cubit: Coal Ore!!!`, sem som, para minério do filtro. `Cubit: Careful, Zombie!`, com som e a expressão de alerta, para mob.
- **Caixas:** só o dono vê, através dos blocos, uma caixa azul clara e discreta em volta de cada minério novo, por cerca de 10 s ou até minerar.
- **Seta:** uma seta vermelha para baixo sobre a cabeça do mob, enquanto ele estiver no raio, com pulso e um leve balanço.
- **Cores:** as antenas e um halo atrás do corpo ficam vermelhos com mob e azuis por cerca de 3 s com minério, de dia ou de noite, e todos veem. O vermelho vence.

## Falas

Frases com o bipe padrão, só do Cubit ativo que segue e com a expressão calma, desligáveis pelo botão `Chatter`. A lista completa, em inglês e português, está em [`conceito/falas.md`](conceito/falas.md).

- **Tempo e horário:** amanhecer, dia lindo, entardecer, chuva, tempestade e frio, a cada 5 minutos no máximo, com céu à vista.
- **Biomas:** ao mudar de grupo de biomas, a cada 2 minutos no máximo.
- **Ficar para trás:** quando o dono se afasta correndo, a 16 blocos.
- **Conquistas:** parabéns, com a expressão feliz, nas conquistas e nos desafios visíveis.
- **Animais:** quando o dono abate um animal, com chance de 1 em 3, nunca um animal domesticado.
- **Redstone:** `I love redstone!` e outras duas, quando o dono minera redstone, a cada 1 minuto no máximo.

## Jade

Opcional: o mod funciona sem ele.

- **Cubit:** o tier na cor do colar, a bateria em porcentagem e FE e o estado. O Jade já mostra o nome e a vida.
- **Induction Dock:** o estoque de FE e o estado da tela, com os nomes das telas, como `No power` (pergunta 685).

## Telas

No estilo das imagens de conceito, sem a textura do baú do jogo.

- **Cubit:** abas do inventário e de configurações. O inventário mostra o retrato com a expressão, o nome, o tier, o estado, a vida, a bateria, a recarga, o slot de redstone e os inventários. As configurações têm as chaves `Lamp`, `Scanners` e `Chatter`, o filtro do scanner e o status do painel solar, do consumo, da vida e de quem ele segue.
- **Induction Dock:** a torre com a luz do estado, o estoque, a entrada média de FE, o slot de redstone, o Cubit na base, a fila, o dono e o botão de chamar o Cubit mais próximo.
- **Regras:** só o dono abre, e a tela do Cubit fecha se ele desligar. Os números são abreviados, como `214k / 300k FE`.

## Técnica

- **Config:** `config/cubit-common.toml`, do tipo `COMMON`, para o packwiz distribuir. Todo número de balanceamento fica nele, e valor inválido nunca derruba o jogo.
- **Receitas:** em JSON de datapack, que o KubeJS pode sobrescrever, só com materiais do jogo puro.
- **Energia:** FE pelas capabilities do NeoForge, aceitando cabos de qualquer mod, e redstone para funcionar sem outros mods.
- **Dependências:** GeckoLib e LambDynamicLights, obrigatórias, e Jade, opcional.
- **Servidor dedicado:** abre sem classes do cliente, conferido pelas GameTests.
- **Idioma:** inglês e português, no `en_us.json` e no `pt_br.json`, conforme o idioma do jogo, com as mesmas chaves conferidas por um teste.
- **Qualidade:** testes JUnit no domínio, com cobertura acima de 85%, GameTests no mundo e um roteiro manual. Os gates de commit e de push rodam formatação, Checkstyle, ArchUnit, testes e datagen.

## Ainda não entrou

- **Fase 3:** a primeira versão, `0.1.0`.
- **Fase 4:** tiers Advanced, Elite e Ultimate, Power Bank, ímã e coleta, choque e modos de defesa, Tablet, modo área, descarga em baú, retorno automático e local de morte.
- **Fase 5:** trabalhos com os cards de Aprendizado de Máquina, antenas, Servidor e storage wireless.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-13 16:40 | Troca o Cubit Remote pelo Tablet. |
| Marcelo Fabiano | 2026-11-08 00:11 | Cita a tradução para o português. |
| Marcelo Fabiano | 2026-11-07 23:42 | Atualiza o marco e acrescenta a animação de hibernando. |
| Marcelo Fabiano | 2026-11-07 23:12 | Adiciona as telas próprias. |
| Marcelo Fabiano | 2026-11-07 23:11 | Adiciona as telas próprias. |
| Marcelo Fabiano | 2026-11-07 21:55 | Lista as funcionalidades entregues até o PR #122. |
