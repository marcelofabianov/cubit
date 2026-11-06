# Trabalhos

Conceito da Fase 5, planejada para depois da Fase 4. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). As decisões ficam em [`fase-5-trabalhos.md`](../perguntas/fase-5-trabalhos.md), nas perguntas 190 a 256.

Números: [`tiers.md`](../tiers.md#trabalhos). Nomes e IDs previstos: [`itens.md`](../itens.md#previstos-na-fase-5).

## A ideia

Um mundo com vários Cubits trabalhando sozinhos, cada um com um trabalho. É sempre o mesmo robô: o card define o que ele sabe fazer, a área define onde, e as antenas mantêm esse lugar funcionando.

O mod funciona numa instância só com ele e suas dependências. Tudo aceita redstone, e cabos e outros mods são um extra (perguntas 238 e 240).

## Cards de Aprendizado de Máquina

- **Slot:** o card fica num slot exclusivo e sai quando o player quiser, sem perda. Sem card, o Cubit é generalista.
- **Quantidade:** um por Cubit, e o tier melhora o que o card faz.
- **Tier:** o Base não aceita cards. O primeiro tier que aceita é o Avançado.
- **Estado:** morrer e recolher preservam o card.
- **Seguindo:** age como generalista. O card só age em área marcada.
- **Item:** um por trabalho, com receita própria em JSON e materiais do jogo puro. Os cards empilham.
- **Custo:** gasta o consumo ao andar e mais um valor por ação, por card, sem gastar se não houver ação.

## Como o Cubit começa a trabalhar

1. Com o Cubit Remote, o player marca dois cantos, que definem uma área em 3D.
2. O processo só termina ao selecionar o Cubit no controle. Ele deixa de seguir e passa a trabalhar.
3. O controle também manda esvaziar o inventário ou voltar à base.

Baús são marcados com o controle, e a tela do Cubit tem uma aba Trabalho, com modo e filtros. Se o player recolher o Cubit e colocá-lo de novo, precisa refazer a configuração.

## Regras de toda área

- **Limite:** o Cubit nunca sai da área marcada.
- **Área carregada:** ela tem prioridade sobre o tier. Se o tier permitir uma área maior que a carregada, ele só atua na parte carregada.
- **Proteção:** o Cubit nunca mexe em blocos com dados, como baús, máquinas, Dock, antena e Servidor, nem na tag de blocos protegidos, que o pack pode editar. Cada card só mexe no seu tipo de bloco.
- **Vários Cubits:** há um número fixo por área, e cada um reserva o alvo antes de ir. Varreduras e ações têm limite por tick, e os robôs esperam a vez.
- **Inventário cheio:** descarrega no baú marcado o que conseguir. Quando não conseguir mais, hiberna.

## Hibernação fora do Dock

Estado semelhante ao desligado, mas com o inventário e as configurações acessíveis. O Cubit fica imóvel e sem tarefas, como dormindo. Ele volta pelo botão `Retomar`, na tela, ou pelo controle. Se o motivo não sumiu, hiberna de novo e explica. A hibernação no Dock continua saindo sozinha (pergunta 55).

## Trabalhos do Cubit

### Minerador

- **Sem ferramenta:** minera sem item de picareta, com a capacidade do tier.
- **Modos:** escava a área, abre túnel reto ou em degraus, ou busca minérios do filtro. É um modo por vez, sempre dentro da área. É uma ideia inicial, com os detalhes na entrega do card (pergunta 344).
- **Opções:** Eficiência, Fortuna e Toque Suave agem como no jogo. Fortuna e Toque Suave não ficam ativos juntos, e o player alterna entre eles na tela.
- **Economia:** Inquebrável reduz o custo de FE por bloco, e Remendo dá um desconto extra nos blocos em que encontra minério. Nenhum dos dois cria energia.

### Fazendeiro

Colhe o que estiver maduro, pelas tags comuns de plantações, e replanta com as sementes do inventário. Não ara terra nova. Sem semente, deixa o bloco vazio e segue.

### Pescador

Na área com água aberta, pesca pela tabela de pesca do jogo, em intervalos e gastando FE, sem vara e sem boia.

### Pecuarista

Alimenta com itens do inventário, tosquia, recolhe ovos e abate só acima de um limite de animais por espécie, poupando filhotes. A regra de nunca atacar mobs pacíficos tem exceção só para este card e só na área.

### Defensor

É diferente do comportamento de defender o player ao seguir.

- **Protege:** o dono, outros players, robôs, animais e aldeões dentro da área.
- **Reação:** atacado, contra-ataca. Um mob hostil que entra na área só é atacado por iniciativa dele se o tier tiver o modo agressivo. Um mob neutro é atacado quando agride algo na área.
- **Modos:** com este card, a opção Pacífico não fica disponível, e o Cubit é migrado para o modo seguinte.
- **Dano:** cada tier que aceita o card ataca com mais dano.
- **Ataque de fora da área:** se um mob ataca de fora, como um esqueleto com arco, o Cubit recua e procura proteção. Quando o mob entra na área, ele ataca. Se o mob sair, volta a se esconder.
- **Drops:** guarda no inventário enquanto couberem, e o resto cai no chão. Ele não hiberna por inventário cheio e só para sem bateria. Com baú marcado, descarrega quando não houver ameaça.

### Organizador

Os detalhes ainda não estão fechados.

- **Recolher:** pega itens do chão e do que as máquinas e fornalhas oferecem como saída, pela capability do NeoForge, sem tocar em entradas e combustível.
- **Guardar:** usa só baús marcados. A prioridade é o que tem mais do mesmo item. Se nenhum tiver, usa o primeiro marcado com espaço. A leitura roda em intervalos.
- **Modo de abastecer:** leva uma quantidade fixa de um baú a cada destino, por pedidos, sem craft.

### Cozinheiro

Na área, leva alimentos crus de baús marcados a fumeiros e fornalhas marcados, abastece combustível do inventário e leva a comida pronta a um baú marcado. Usa blocos do jogo, sem máquina nova.

### Construtor

Inicialmente simples, dentro da área 3D:

- **Aplainar:** retira os blocos permitidos, de cima para baixo.
- **Preencher:** usa o bloco do filtro em vazios e líquidos, de baixo para cima.

Sem o bloco ou com o inventário cheio, ele hiberna.

## Robot Server

- **Lugar:** um bloco fixo na base.
- **Energia:** gasta FE para manter o próprio chunk, se essa opção estiver ligada, e aceita redstone por slot ou funil. Sem energia, solta o próprio chunk e avisa, mas as antenas continuam com a bateria delas.
- **Tier:** limita o número de antenas e de chunks carregados, sem influenciar a antena nem o Cubit diretamente. A receita de cada tier tem a dificuldade do Cubit do mesmo tier.
- **Upgrade:** o mesmo Module de cada tier serve no Cubit, no Servidor e na antena, com clique direito, e é consumido, sem pular etapas.

### Log

A tela do Servidor mostra, por antena, robôs, tarefa, bateria, inventário e os últimos eventos. Os dados são enviados enquanto o chunk está carregado. Descarregado, mostra a última foto, com a hora.

## Signal Antenna

- **Ligação:** o player clica no Servidor com a antena. Sem ligação, ela não carrega chunks. Com o Servidor quebrado, as antenas ligadas soltam os chunks e voltam quando ele volta, sem perder a ligação nem a carga.
- **Área:** cada tier aumenta a área carregada, e um conjunto de antenas pode cobrir áreas maiores que 1 chunk.
- **Bateria:** gasta FE por tick enquanto ligada, mais em tiers maiores, e tem botão de ligar e desligar. Recarrega com redstone por funil, cabos ou outros mods.
- **Sem bateria:** perto de zerar, avisa o dono. Ao zerar, solta os chunks e guarda o estado.
- **Quebrar:** vira item com a bateria guardada, e a lista salva no mundo limpa o que não existir mais.

## Carregamento de chunks

- **Opcional:** a chave `chunk_loading_enabled` fica ligada por padrão. Desligada, os blocos continuam existindo e guardando dados, mas ninguém carrega chunks.
- **Antena com o carregamento desligado:** gasta um valor menor, só para o log. Sem carga, deixa de enviar log, mas continua valendo como referência de Dock.
- **Limites:** o Servidor limita antenas e chunks, e o config tem um teto de chunks no mundo. O player vê o motivo se passar dele.
- **Exceção:** é a única exceção à regra 22 do `AGENTS.md`.
- **Jogo fechado:** em singleplayer, nada roda com o jogo fechado. Manter carregado só vale para o player longe da área.

## Viagem até o Dock

Com um Cubit com trabalho trabalhando em área, todo Dock precisa estar no chunk do Servidor ou próximo a ele ou a uma antena, mesmo com o carregamento desligado. Um Cubit sem card usa qualquer Dock, como nas Fases 2 a 4.

1. Com bateria baixa, o Cubit prefere um Dock livre na área.
2. Sem ele, anda até a borda do carregado, rumo ao Dock ligado ao Servidor pelo controle.
3. Teleporta para a borda do chunk do Servidor, sem custo de FE, e caminha até o Dock. Preso sem progresso, teleporta também. Sem ponto seguro, aparece na frente do Dock.
4. Se o chunk de destino estiver descarregado, espera na borda e tenta de novo a cada intervalo. Se a bateria acabar, desliga sem perder nada.

## Storage wireless

- **Inventário virtual:** salvo no mundo, por dono e canal. A `Wireless Chest` na base e o upgrade do Cubit acessam o mesmo canal, de qualquer lugar e dimensão, sem carregar chunks.
- **Canal:** escolhido numa tela, com número e nome, como `Minérios` e `Fazenda`. A cor do corante pode colorir o bloco só como identificação. Um Cubit por baú, ou vários no mesmo.
- **Endgame:** só no Ultimate, como upgrade caro. Transferir custa FE por item, e cada canal tem tamanho fixo.
- **Segurança:** toda alteração passa pelo servidor, num ponto único, com teste de duplicação. Só o dono acessa.

## Ordem de entrega

1. Base comum: estado de trabalho, card em slot e aba de configuração
2. Fazendeiro e Pescador
3. Organizador, com o modo de abastecer
4. Cozinheiro
5. Defensor
6. Minerador
7. Construtor
8. Pecuarista
9. Antena e chunks
10. Servidor, log e viagem
11. Storage wireless

Antes de fechar cada card, testa-se com 20 a 30 robôs.

## Pendente

- Os números marcados como "A definir" em [`tiers.md`](../tiers.md#trabalhos).
- Os detalhes do Organizador (pergunta 208).
- O formato da tela do Cubit com a aba Trabalho e o botão `Retomar`.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-05 21:36 | Marca os modos do Minerador como ideia inicial. |
| Marcelo Fabiano | 2026-11-05 12:08 | Consolida o conceito dos robôs especializados. |
