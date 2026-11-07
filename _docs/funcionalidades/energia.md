# Energia

Números: [`tiers.md`](../tiers.md#energia) e [valores gerais](../tiers.md#valores-gerais).

## Bateria

- Uma bateria interna alimenta o sistema.
- É um recurso a gerenciar: o robô precisa voltar para carregar.
- As barras do item e da tela mostram ao menos um risco enquanto houver carga, e só ficam vazias com 0 FE.

## Estados do Cubit

| Estado | Quando acontece | Painel solar | O que o player pode fazer |
|---|---|---|---|
| **Ativo** | Ligado, fora da base | Funciona | Todas as interações |
| **Inativo** | Desligado, por exemplo sem bateria | Desligado | Só recolher, colocar energia no slot exclusivo ou, com bateria, ligar com o clique direito, exceto se tiver sido desligado pelo controle |
| **Hibernação** | Na base (Induction Dock) | Desligado | Usar o inventário e os slots exclusivos, ou recolher |

- **Hibernação não é o mesmo que desligado:** o Cubit carrega e continua acessível. Fora da base, ela entra na Fase 5, conforme [`trabalhos.md`](trabalhos.md#hibernação-fora-do-dock).
- **Base:** é sempre o Induction Dock, o bloco fixo de recarga.
- **Religar:** sem bateria, com a carga mínima ele volta sozinho a ficar ativo.
- **Sem bateria na base:** entra em hibernação logo, sem esperar a carga mínima.
- **Desligado pelo controle remoto:** segue as regras do inativo, mesmo com bateria, e só volta a ficar ativo pelo controle.
- **Sons:** o Cubit tem um som ao ligar, um ao desligar e um enquanto é recarregado. Eles entram na Fase 3, e os detalhes estão em [`visual.md`](../conceito/visual.md#sons).

## Consumo

Os números estão em [`tiers.md`](../tiers.md#consumo-por-atividade), e o consumo só vale com o robô ativo.

- **Andar:** gasta o valor do tier a cada tick com a rota em andamento e deslocamento real, inclusive ao fugir do fogo e da lava. Ser empurrado não gasta, e rota sem avanço também não.
- **Inventário cheio:** com todos os slots ocupados, mesmo com pilhas incompletas, andar custa um extra, e a velocidade cai. Em chamas ou na lava, a velocidade não cai, porque fugir vem primeiro.
- **Água:** andar em contato com a água custa um extra, inclusive numa poça rasa e ao fugir do fogo.
- **Subir:** cada bloco de altura subido na própria rota custa um extra, proporcional à altura. Descer não devolve energia.
- **Condição negativa:** fogo, lava, [blocos que ferem](movimento.md#sobre-um-bloco-que-fere) e efeitos nocivos do jogo ou de outros mods custam um extra a cada tick, mesmo parado. Várias juntas cobram um extra só, e um golpe não conta. O extra pode zerar a bateria. Aí o robô desliga e, em chamas, queima parado.
- **Lanterna:** acesa, custa um extra a cada tick, mesmo parado, e pode zerar a bateria. A claridade da tela não gasta. Os detalhes estão em [`sensores.md`](sensores.md#lanterna).

## Como o Cubit carrega

Só existem quatro formas, e nenhuma outra:

1. **Induction Dock**
2. **Power Bank** no slot exclusivo
3. **Redstone** no slot exclusivo
4. **Painel solar**

## Painel solar

Funciona como um carregador solar real.

- **Quando carrega:** só com o Cubit ativo e durante o dia. Inativo ou em hibernação, o painel fica desligado.
- **À noite:** só carrega nos dois últimos tiers, Elite e Ultimate.
- **Blocos:** coberto por um bloco opaco, não carrega. Com o céu aberto ou sob vidro, carrega 100%.
- **Clima:** não tem efeito.
- **Dimensões:** no Nether e no End, não carrega.
- **Sinal:** enquanto o painel enche a bateria, a célula clareia e escurece devagar. Com a bateria cheia, o sinal para. O painel não toca o som de recarga, que é da base e do slot.
- **Chave:** ainda não existe. Entra com a interface de configuração (perguntas 494 e 495).

## Induction Dock

- É a base de recarga do Cubit, inspirada nas bases de robôs aspiradores. Na base, o Cubit fica em hibernação.
- **Formato:** o da [prancha de conceito](../imagens/conceito/induction-dock-prancha.png): torre atrás, com tela, e placa em U na frente, virada para o player que coloca. O Cubit sobe na placa, de costas para a torre. Ocupa 2 blocos de altura, como uma porta, e precisa do bloco de cima livre para ser colocado. Quebrar uma parte quebra as duas.
- **Tela:** desligada, sem energia e sem Cubit; ociosa, com energia e sem Cubit; carregando, com o Cubit recebendo; completa, com a bateria dele cheia; sem energia, com o Cubit na base e o estoque zerado.
- **Energia:** recebe por cabos de outros mods, em **FE**, em qualquer face, ou com redstone em um slot próprio, também por funil. Nada sai por cabo nem por funil.
- **Estoque:** tem um pequeno estoque de energia. Um item de redstone só vira energia quando couber inteiro no estoque. Sem FE, o Cubit fica hibernando.
- **Slot:** tem um único slot, que aceita redstone ou, na Fase 4, uma Power Bank, que recebe energia do Dock.
- **Vida:** recupera a vida do Cubit durante a hibernação, sem gastar FE, mesmo com o estoque zerado.
- **Retorno automático:** serve de base para ele, na Fase 4.

### Dono e tela

- **Dono:** quem coloca o Dock. Só o dono usa o Dock e abre a tela, e só os Cubits dele entram.
- **Clique direito:** chama o Cubit ou, com um na base, o libera. Com redstone na mão, a pilha entra no slot.
- **Shift + clique direito:** abre a tela, com o slot e uma barra de energia. A janela personalizada, com textura de metal de robô, entra na Fase 3 (pergunta 371).
- **Quebrar:** só o dono, ou um player no modo criativo com permissão de operador, para remover um Dock abandonado. Sem dono, como num save sem a chave ou colocado por algo que não é um player, só o operador o remove, e o log avisa ao carregar (pergunta 524). Explosões e pistões não o afetam. O item guarda a energia e empilha em 1, o slot cai no chão, e o Cubit sai como numa liberação. O dono do bloco novo é quem o coloca.

### Ir para a base e sair dela

- **Ir:** o player clica com o botão direito no Dock, e o Cubit ligado mais próximo, dentro do raio do retorno automático, vai até ele, esteja seguindo ou em área. Colocar o item do Cubit em cima do Dock também funciona. Num Dock ocupado ou de outro player, nada acontece, e o item continua na mão.
- **Sem conseguir chegar:** sem rota ou preso, teleporta para o Dock. Se o chunk estiver descarregado ou houver um bloqueio em cima, desiste e volta ao que fazia.
- **Fechar o jogo:** só a hibernação é salva. A caminho ou aguardando, o Cubit volta ao que fazia ao carregar de novo. Se o Dock sumiu, ele acorda como numa liberação.
- **Sair:** o Cubit sai sozinho cerca de 3 s depois de encher a bateria, com a tela no estado completa. Antes disso, clicar no Dock o libera. Ao sair, volta ao que fazia: a área ou seguir o player. Se outro assumiu o lugar de seguir, fica parado e desligado.
- **Dono offline:** o Cubit cheio continua na base e sai quando o dono entra. Se o Dock sumir nesse período, ele acorda desligado, e um clique o liga.

### Base ocupada

Cada Dock recebe um Cubit por vez. Se a base estiver ocupada:

1. O Cubit vai para a base livre mais próxima dentro do raio.
2. Se todas estiverem ocupadas, ele para na frente da base e aguarda, ainda no estado ativo.
3. Quando a base libera, entra o que chegou primeiro.

## Slot exclusivo de energia

O slot fica num painel ao lado da tela do inventário. O Cubit extrai a energia da Power Bank ou da redstone colocada no slot para a bateria interna, na velocidade de recarga do tier. Cada item extraído vira uma reserva, salva com o slot, para nada se perder se o robô for recolhido no meio da recarga. O slot existe em todos os tiers e aceita um dos dois por vez. A redstone é consumida um item por vez, e só quando a energia couber na bateria.

### Power Bank

- Bateria portátil que o player carrega no próprio inventário.
- Existe uma Power Bank para cada tier. A capacidade de cada uma equivale a uma porcentagem da bateria interna do tier correspondente.
- Entra na Fase 4. A redstone no slot já funciona na Fase 2.
- Funciona em um Cubit de qualquer tier, na velocidade de recarga do tier do Cubit.
- Não empilha: cada uma ocupa um slot.
- Sai vazia do craft e recarrega no Induction Dock ou em qualquer carregador de FE de outros mods, quando houver.

### Recarregar um Cubit desligado

1. Com o Cubit desligado e a Power Bank ou a redstone na mão, o player clica com o botão direito no Cubit.
2. O item entra sozinho no slot exclusivo, e a recarga começa.
3. Com a carga mínima, o Cubit liga.
4. A partir daí, o player pode acessá-lo, remover o item e fazer as outras interações.

Com redstone na mão, o clique num Cubit desligado põe a pilha no slot, mesmo com carga. Para ligá-lo, basta clicar sem redstone na mão.

### Recarregar o item pela grade de craft

Entra na Fase 4, junto da Power Bank.

O item com a bateria abaixo da carga de religar, como zerada, e com vida pode ser colocado no mundo, mas o robô fica desligado, com a interação limitada. Depois de recolhê-lo, o player pode usar esta receita.

- **Como:** combine o item do Cubit com uma Power Bank, em qualquer posição da grade do inventário ou da mesa de craft.
- **Quando:** só com a bateria do Cubit abaixo da carga de religar, e a Power Bank precisa ter o que falta. Sem isso, a receita não aparece. O `Damaged Cubit` não vale.
- **Quanto:** só o que falta para a carga de religar, sobre a bateria do Cubit. Assim, ele volta a funcionar ao ser colocado. A Power Bank perde exatamente essa quantidade, sem criar nem perder FE.
- **O que se mantém:** o Cubit não perde mais nada do estado, e a Power Bank continua na grade, com a carga restante.

## Retorno automático

Se o robô estiver dentro do raio da base e com a bateria baixa, volta sozinho para ela e entra em hibernação. O Cubit que segue também mostra o texto `I need to recharge`.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-07 13:51 | Define quem quebra o Dock sem dono. |
| Marcelo Fabiano | 2026-11-06 15:05 | Detalha o vidro, o sinal e a chave do painel solar. |
| Marcelo Fabiano | 2026-11-06 13:34 | Adiciona o consumo da lanterna. |
| Marcelo Fabiano | 2026-11-06 11:15 | Limita o texto do retorno ao Cubit que segue. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-05 23:49 | Cita a janela personalizada do Dock. |
| Marcelo Fabiano | 2026-11-05 21:30 | Define a saída da base com o dono offline. |
| Marcelo Fabiano | 2026-11-05 21:02 | Detalha a saída da base e o clique com redstone num Cubit desligado. |
| Marcelo Fabiano | 2026-11-05 16:57 | Adota o formato e os estados da prancha de conceito. |
| Marcelo Fabiano | 2026-11-05 16:09 | Detalha o Dock: formato, dono, tela, quebra, teleporte e o que é salvo. |
| Marcelo Fabiano | 2026-11-05 15:32 | Inclui os blocos que ferem na condição negativa. |
| Marcelo Fabiano | 2026-11-05 14:40 | Define o consumo por situação. |
| Marcelo Fabiano | 2026-11-05 13:57 | Define que as barras só ficam vazias com 0 FE. |
| Marcelo Fabiano | 2026-11-05 12:52 | Define o consumo e a reserva do slot de energia. |
| Marcelo Fabiano | 2026-11-05 12:08 | Aponta a hibernação fora da base para a Fase 5. |
| Marcelo Fabiano | 2026-11-05 02:00 | Amplia a recarga pela grade de craft para a bateria abaixo da carga de religar. |
| Marcelo Fabiano | 2026-11-05 01:58 | Detalha o uso da recarga pela grade de craft. |
| Marcelo Fabiano | 2026-11-05 01:57 | Adiciona a recarga do item pela grade de craft. |
| Marcelo Fabiano | 2026-11-05 00:12 | Cita os sons de ligar, desligar e recarregar. |
| Marcelo Fabiano | 2026-11-04 18:50 | Define o estado ao sair da base com outro seguindo. |
| Marcelo Fabiano | 2026-11-04 17:26 | Esclarece a exceção do robô desligado pelo controle. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:12 | Adiciona a espera na frente da base ocupada. |
| Marcelo Fabiano | 2026-11-04 17:11 | Atualiza a chamada ao Dock e o comportamento ao sair da base. |
| Marcelo Fabiano | 2026-11-04 17:08 | Adiciona a hibernação sem bateria, o ligar pelo clique e as regras da redstone. |
| Marcelo Fabiano | 2026-11-04 16:57 | Define os estados, as formas de recarga, o painel solar, o Dock e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
