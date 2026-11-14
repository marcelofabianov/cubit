# Inventário e coleta

Números por tier: [`tiers.md`](../tiers.md#coleta-e-inventário) e [slots exclusivos](../tiers.md#slots-exclusivos).

## Interação

- **Clique direito:** abre o inventário, no estado ativo ou em hibernação. Desligado, valem as regras de [`energia.md`](energia.md#estados-do-cubit).
- **Shift + clique direito:** recolhe o robô. O acesso em cada estado está em [`energia.md`](energia.md#estados-do-cubit).

## Tela

- **Quem abre:** só o dono, com o robô ativo ou em hibernação e por perto. O servidor confere os três, ao abrir e enquanto a tela estiver aberta. Por perto é o alcance do player mais uma margem de 4 blocos, a mesma dos baús do jogo.
- **Visual:** a tela própria, descrita em [`telas.md`](telas.md#tela-do-cubit), com as linhas que o número de slots pedir (perguntas 371 e 676).
- **O que não entra:** o Cubit, o `Damaged Cubit` e os itens que o jogo não deixa entrar em containers, como shulker boxes.

## Conteúdo preservado

- Recolher, colocar, morrer, consertar e salvar o mundo nunca apagam nem duplicam itens, nem com a tela aberta.
- **Config reduzido:** ao colocar ou carregar o robô, o que não couber nos slots do config cai no chão, ao lado dele, com um aviso no log.
- **Excedente e coleta:** com a coleta ligada, o Cubit pode recolher do excedente o que couber numa pilha que ele já tem. Nada se perde nem duplica, e nenhum slot passa do config (pergunta 894).

## Coleta

Entra no item 9 da Fase 4.1 (respostas 422, 423 e 876 a 889). A coleta, o ímã e a busca são peças separadas, cada uma com a sua chave na [aba Behavior](telas.md#aba-behavior).

- **Inventário:** armazena itens.
- **Coleta:** desde o Base, o Cubit pega o item que encosta nele, com a caixa dele aumentada em 1 bloco, como o player. Não gasta energia.
- **Ímã:** do Advanced em diante, puxa até o Cubit os itens no raio do tier, com o movimento visível e sem teleportar. O item já pago é guiado a cada tick e coletado assim que encosta, e a 3 blocos chega em cerca de 0,3 s (pergunta 928). Gasta energia por item puxado, uma pilha no chão. No Base, fica bloqueado. Com a coleta desligada, o ímã fica inativo, porque puxar sem pegar só amontoaria itens.
- **Busca:** desde o Base, o Cubit ativo e seguindo anda até os itens no chão num raio em volta do **dono**, do tier, pega e volta a seguir (issue #165, respostas 927 e 936 a 943). Não tem custo próprio, além do consumo de andar. Com a coleta desligada, a busca fica inativa, como o ímã. Detalhes em [Busca](#busca).
- **Quando coleta:** só ativo, seguindo ou parado. Hibernando, desligado ou sem bateria, não coleta.
- **Inventário cheio:** pega o que cabe, juntando nas pilhas e nos slots livres, e o resto fica no chão. O ímã só puxa, e só cobra, os itens que cabem pelo menos em parte. Vale só o inventário principal, nunca os slots exclusivos.
- **Sem energia para o ímã:** ele não puxa, e a coleta por contato continua.
- **O que nunca pega:** o item ainda no atraso de coleta do jogo, como o que o player acabou de jogar, o Cubit e o `Damaged Cubit`, para o player não perder o rastro de um robô.
- **Varredura:** a coleta a cada 5 ticks e o ímã a cada 10 ticks, no config, só em chunks carregados (regras 21 e 22).
- **Estado inicial:** as duas chaves começam ligadas e ficam no item (pergunta 343).

### Busca

- **Quando busca:** só ativo e seguindo, sem estar indo ao Dock, sem fogo ou bloco que fere e sem alvo do choque. Fica abaixo dessas reações e acima de seguir (resposta 939).
- **O que busca:** os mesmos itens do ímã: os que o filtro de coleta aceita e que cabem pelo menos em parte no inventário principal, respeitando o atraso de coleta do jogo, e nunca o item de um Cubit nem um `Damaged Cubit`. Com o inventário cheio, não busca nada (resposta 941).
- **Um item por vez:** o mais próximo do dono, dentro do raio do tier em volta dele.
- **Como chega:** andando, pelo caminho do jogo, sem teleportar até o item. Pega ao encostar, pela coleta por contato.
- **Quando desiste:** se o dono sair do raio da busca, larga o item e volta a seguir, e o teleporte de seguir continua valendo. Sem chegar em 5 s, ou sem caminho, desiste e ignora aquele item por 10 s (resposta 942).
- **Varredura:** a cada 20 ticks, só em chunks carregados. Os três tempos ficam no config (regras 21 e 22).
- **Estado inicial:** a chave começa ligada, também em saves antigos, e fica no item (resposta 938).

### Filtro de coleta

- **Modos:** na lista branca, coleta só os itens definidos. Na lista negra, coleta tudo menos os itens definidos. O padrão é a lista negra.
- **Lista vazia:** na lista negra, pega qualquer item. Na lista branca, não pega nada.
- **Limite:** os slots do tier, iguais nos dois modos.
- **Comparação:** só o tipo do item, pelo ID. Uma espada encantada no filtro vale para qualquer espada de ferro.
- **Tags:** no Elite e no Ultimate, um slot pode valer por uma tag do item, como `#c:ores`, dentro do mesmo limite.
- **Edição:** por slots fantasma, na aba Behavior. O item não é consumido.
- **Vale também para o ímã:** o ímã só puxa o que a coleta pegaria.

## Descarregar em baú

Entra com o item 14 da Fase 4.2, numa entrega só (perguntas 1086 a 1095).

- **Marcar:** com o app Descarga do [Tablet](tablet.md#apps), o clique num bloco com inventário (baú, barril, shulker ou de outro mod, pela capability) marca o baú do Cubit escolhido, de qualquer tier, até o limite dele. O clique num baú já marcado manda o Cubit descarregar ali agora, como o botão `Descarregar`, mas só naquele baú. Shift + clique direito num baú marcado desmarca só ele, e em qualquer outro bloco, ou no ar, desmarca o último marcado, de qualquer distância, para dar lugar a um novo. Sem Shift, fora de um bloco com inventário, o app só avisa (perguntas 1086, 1098 e 1099).
- **Limites:** `unload_chests` e `unload_reach_blocks`, por tier, com os números de [`tiers.md`](../tiers.md#coleta-e-inventário) (pergunta 1094).
- **Sozinho, só na área:** com a chave `Unload` ligada e o inventário cheio, o Cubit numa área vai ao baú marcado, descarrega tudo menos os slots exclusivos e volta para a área. O Cubit que segue não descarrega sozinho (pergunta 1087).
- **Pelo botão:** o botão `Descarregar` do Tablet manda o Cubit escolhido descarregar já, mesmo sem o inventário cheio, e voltar ao que fazia, mesmo com a chave desligada (perguntas 1089 e 1093).
- **Ordem:** o primeiro baú marcado com espaço, depois o seguinte, até esvaziar ou não caber mais (pergunta 1088).
- **Sem onde descarregar:** sem espaço, fora do alcance ou com o chunk descarregado, ele não sai do lugar. O que segue avisa `No room in the marked chests`, e o da área fica com `Inventory full` e tenta de novo a cada intervalo (pergunta 1088).
- **Texto:** `Cubit: Items unloaded` sai depois de descarregar, só do Cubit que segue, ou seja, pelo botão (perguntas 457 e 1094).
- **Seta:** com o Tablet na mão, em qualquer app, cada baú marcado de um Cubit do player, carregado e na dimensão dele, tem uma seta em cima, como a do Cubit escolhido. Sob a seta, a primeira linha diz o nome do Cubit, e a segunda, os slots usados e o total do baú, como `Inventory 12 / 27`, que o servidor manda a cada segundo enquanto o player segura o Tablet (perguntas 1086 e 1090 a 1092).
- **Chave:** `Unload` / `Descarga`, desde o Base, ligada por padrão e guardada no item, na aba Settings e na aba Recursos do Tablet. Liga e desliga só a descarga automática da área (pergunta 1093).
- **Contrato:** os baús ficam no NBT `UnloadChests` e se perdem ao recolher o Cubit (pergunta 343).

## Slots exclusivos

São dois: tochas e energia. Na Fase 5, o card de Aprendizado de Máquina ocupa mais um. O Tablet ativa ou desativa as tochas, na aba Recursos, e o slot de energia fica sempre ligado. A fornalha de minérios foi para o [Minerador](trabalhos.md#minerador), na Fase 5, e o carregador de ferramentas saiu do mod (pergunta 930).

- **Fileira:** os slots exclusivos ficam numa fileira acima do inventário do Cubit, com o título `Exclusive slots` / `Slots exclusivos`. O slot de energia continua ao lado das barras (perguntas 980 e 984).
- **Tochas automáticas:** do Advanced em diante, com tochas no slot, o Cubit as coloca sozinho onde mobs podem nascer. Veja [Tochas](#tochas).
- **Esvaziar:** o comportamento automático de esvaziar o inventário não coloca nem tira itens dos slots exclusivos.
- **Energia:** o robô extrai a energia da Power Bank ou da redstone para a bateria interna. Veja [`energia.md`](energia.md#slot-exclusivo-de-energia).

### Tochas

Entra no item 12 da Fase 4.1 (issue #170, perguntas 971 a 984).

- **Slot:** um só, com uma pilha de até 64 tochas comuns, `minecraft:torch`. Não aceita outra tocha. No Base, aparece apagado, com a dica `Unlocks at Advanced`, e não aceita nada (perguntas 973 e 983).
- **Fora da coleta:** a coleta, o ímã, a busca e o esvaziar automático nunca põem nem tiram tochas desse slot.
- **Quando analisa:** a cada 2 s, só ativo, seguindo ou parado, com a chave `Torches` ligada e tochas no slot (perguntas 974 e 976).
- **Lugar perigoso:** um bloco em que um mob hostil pode nascer: o chão aceita mob, há 2 blocos livres acima, e a luz de bloco, a das tochas e das outras fontes, fica abaixo do nível do tier, que é 1 por padrão. Isso é o que impede o nascimento no 1.21, porque a luz do céu não protege à noite (perguntas 971 e 975).
- **Ao ar livre:** um bloco que vê o céu só conta quando escurece, com a mesma leitura e o mesmo limite da lanterna, a luz do ambiente abaixo de `lamp_on_below_light`. Nas cavernas e construções, conta a qualquer hora (perguntas 978 e 979).
- **Onde põe:** num raio de 8 blocos em volta do Cubit, só em chunks carregados, ele procura os lugares perigosos. Se achar algum, testa os lugares a até 4 blocos dele em que o jogo deixaria um player pôr uma tocha, no chão ou na parede, num bloco livre, e simula a luz, que começa em 14, cai 1 por bloco e não atravessa parede. A tocha vai no lugar que deixa seguros mais blocos perigosos. É uma tocha por varredura, sem sair do caminho de seguir (perguntas 976 e 977).
- **Sem desperdício:** sem lugar perigoso no raio, ele não põe nada, e uma tocha que não deixaria nenhum bloco seguro também não é posta.
- **Custo:** 20 FE por tocha, do Advanced ao Ultimate. Sem carga para o custo, não põe. A tocha do slot é consumida (pergunta 972).
- **Estado no item:** as tochas do slot e a chave ficam no item ao recolher, morrer e consertar (pergunta 981).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 15:23 | Deixa Shift + clique desmarcar de qualquer lugar (pergunta 1099). |
| Marcelo Fabiano | 2026-11-14 14:40 | Faz o clique num baú marcado descarregar ali (pergunta 1098). |
| Marcelo Fabiano | 2026-11-14 13:55 | Define a descarga em baú marcado (perguntas 1086 a 1095). |
| Marcelo Fabiano | 2026-11-13 16:40 | Troca o controle remoto pelo Tablet. |
| Marcelo Fabiano | 2026-11-13 13:45 | Detalha as tochas automáticas e a fileira dos slots exclusivos. |
| Marcelo Fabiano | 2026-11-13 09:50 | Adiciona a busca dos itens do chão. |
| Marcelo Fabiano | 2026-11-13 09:05 | Deixa só as tochas e a energia nos slots exclusivos. |
| Marcelo Fabiano | 2026-11-12 23:20 | Acelera o item puxado pelo ímã. |
| Marcelo Fabiano | 2026-11-12 21:45 | Detalha a coleta, o ímã, o filtro de coleta e o excedente recolhido. |
| Marcelo Fabiano | 2026-11-07 23:11 | Aponta a tela do inventário para as telas próprias. |
| Marcelo Fabiano | 2026-11-06 11:15 | Limita o texto da descarga ao Cubit que segue. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-06 09:32 | Define os dois modos do filtro de coleta. |
| Marcelo Fabiano | 2026-11-06 00:20 | Adiciona a barra de vida. |
| Marcelo Fabiano | 2026-11-05 23:49 | Detalha a tela própria. |
| Marcelo Fabiano | 2026-11-05 21:30 | Define a margem para abrir a tela. |
| Marcelo Fabiano | 2026-11-05 21:02 | Corrige quem abre a tela e o lugar do slot de energia, e cita o slot do card. |
| Marcelo Fabiano | 2026-11-05 13:30 | Deixa o visual e os tooltips do painel para a Fase 3. |
| Marcelo Fabiano | 2026-11-05 12:52 | Tira os slots exclusivos do esvaziar automático. |
| Marcelo Fabiano | 2026-11-05 02:11 | Define a tela e a preservação do conteúdo do inventário. |
| Marcelo Fabiano | 2026-11-04 17:26 | Limita a abertura do inventário aos estados ativo e hibernação. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as funções que o controle remoto desativa. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona as interações e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
