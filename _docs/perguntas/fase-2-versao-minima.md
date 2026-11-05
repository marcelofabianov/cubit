# Fase 2: Versão mínima

## Pendentes

Não há perguntas pendentes nesta fase.

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 22 | O slot de energia existe no tier Base? | Sim, em todos os tiers |
| 29 | Quando dá para recolher o robô? | Sempre. Desligado, é a única ação, além de receber energia pelo slot (perguntas 21 e 45) |
| 30 | Como ele carrega, se sempre segue o player? | Substituída pela pergunta 47 |
| 31 | Quando ele religa? | Sozinho, com 5% de bateria |
| 32 | Ele consome energia parado? | 0 FE/t, configurável. A pergunta 277 abre uma exceção para condições negativas |
| 33 | Com quanta carga sai do craft? | 25%, configurável |
| 34 | O que acontece na morte, e como repara? | Vira item com inventário preservado e bateria zerada. A vida fica no item e se recupera na estação. O dono não o fere. As perguntas 118 e 119 detalham o item e o dano, e as perguntas 127 a 130, o item danificado e o conserto |
| 35 | O que conta como robô ativo? | Substituída pela pergunta 65 |
| 36 | Qual é o buffer e a entrada da estação? | 100 mil FE, configurável. A entrada foi alterada pela pergunta 61 |
| 37 | Qual é a fonte de FE no ambiente de dev? | Powah só em runtime local, com as dependências a confirmar, ou um comando de debug |
| 38 | Aprova a estrutura de pacotes do `AGENTS.md`? | Sim |
| 39 | Aprova as premissas da versão mínima? | Sim. Estão listadas abaixo |
| 43 | Quanta energia a redstone fornece, e quais itens valem? | 1 pó = 5.000 FE e 1 bloco = 45.000 FE, configuráveis |
| 44 | A Power Bank e a redstone usam o mesmo slot do Cubit? | Sim, um único slot de energia, que aceita um dos dois por vez |
| 45 | Com o Cubit desligado, clicar com redstone na mão funciona como com a Power Bank? | Sim. A pilha entra no slot, a recarga começa e, com 5%, o Cubit liga |
| 47 | Como o Cubit carrega no Dock? | "Base" é o Induction Dock, o bloco fixo, inspirado nas bases de robôs aspiradores. Na base, o Cubit fica em hibernação: diferente de desligado, com o painel solar desligado e o inventário e os slots exclusivos acessíveis. Ativo é ligado, e inativo é desligado, com o painel também desligado. A [pergunta 198](fase-5-robos-especializados.md) amplia a hibernação para fora da base, na Fase 5 |
| 50 | Em que fase entram a redstone no Dock e no slot de energia do Cubit? | Fase 2. Assim, o carregamento já funciona sem gerador de outro mod |
| 54 | Sem o retorno automático da Fase 4, como o Cubit chega à base na versão mínima? | O player clica com o botão direito no Dock, e o Cubit por perto vai até ele. Colocar o item do Cubit em cima do Dock também funciona. A distância e o Cubit chamado foram definidos nas perguntas 59 e 81 |
| 55 | Como o Cubit sai da hibernação, já que interagir com ele abre o inventário? | Sai sozinho com a bateria cheia e volta a seguir. Antes disso, clicar no Dock o libera. O que ele faz ao sair foi ampliado pela pergunta 83 |
| 59 | A que distância clicar no Dock chama o Cubit "por perto"? | O raio do retorno automático do tier |
| 60 | O modelo final entra na Fase 2 ou é provisório nessa fase? | Geometria provisória na Fase 2 e modelo final na Fase 3, junto das expressões. A pergunta 134 antecipa parte disso |
| 61 | O Dock recebe até 1.000 FE/t, mas recarrega Elite a 2.500 e Ultimate a 5.000 FE/t. Como resolver? | Entrada de 5.000 FE/t, com o estoque de 100 mil FE |
| 62 | Um Cubit sem bateria colocado no Dock entra em hibernação logo ou só com 5%? | Logo |
| 63 | Em que velocidade a vida se recupera na base? | Proporcional ao tier: todos levam o mesmo tempo para encher, cerca de 1 minuto, configurável |
| 64 | Como o Cubit extrai a redstone do slot? | Na velocidade de recarga do tier, consumindo um item por vez e só quando a energia couber |
| 65 | Como funciona o limite de robôs por player? | O player pode ter quantos robôs preferir, mas só um o segue. Pode haver um ou mais robôs configurados em áreas e ativos, e seguindo o player somente um |
| 71 | Já existe um Cubit seguindo o player. O que acontece ao colocar outro no mundo? | O novo fica parado e desligado. Ao ligá-lo, ele desliga o que estava ligado. A pergunta 76 limita isso ao que estava seguindo |
| 72 | Como o player escolhe qual Cubit o segue, na versão mínima, sem o controle remoto? | Ao ser colocado ou sair da hibernação, o Cubit segue se nenhum outro estiver seguindo. Para trocar, o player recolhe o atual ou o coloca na base |
| 73 | Clicar no Dock chama qual Cubit, se houver vários? E cabe mais de um na base? | Cada Dock recebe um Cubit por vez. Quem o clique chama foi alterado pela pergunta 81 |
| 74 | Com vários Cubits, o `/cubit reset` ainda é necessário, e há limite de quantidade? | O comando continua, para limpar o vínculo de quem segue, se ele se perder. Não há limite de quantidade |
| 75 | Sem o controle remoto, como ligar um Cubit parado e desligado, ainda com bateria? | O clique direito nele o liga, e ele passa a seguir o player |
| 76 | Ao ligar um Cubit, ele desliga também os que trabalham em áreas? | Não, só o que estava seguindo o player |
| 81 | A base serve a todos os Cubits ligados. Quem o clique no Dock chama? | O Cubit ligado mais próximo, dentro do raio, esteja seguindo ou em área. Também dá para colocar o Cubit na base pelo item |
| 84 | O que faz um Cubit ativo que volta à base, se já houver outro Cubit nela? | Para na frente da base e aguarda |
| 85 | Dois ou mais Cubits aguardam a mesma base. Quem entra quando ela libera? | O que chegou primeiro |
| 86 | A base está ocupada, mas há outra livre dentro do raio do Cubit. O que ele faz? | Vai para a base livre mais próxima, e só aguarda se todas no raio estiverem ocupadas |
| 95 | Qual ferramenta faz a formatação no commit? | Spotless com o google-java-format |
| 96 | Qual é a cobertura mínima de testes? | 85% |
| 97 | Um gate automático confere complexidade e tamanho de função e de classe? | Sim, com o Checkstyle no `check`, junto com o Spotless |
| 98 | Literais no código? | Sempre constantes em vez de string literal e número mágico |
| 99 | A regra de constantes também vale para os testes? | Sim. As constantes do teste ficam no próprio teste, com os valores dos documentos, sem compartilhar com o código de produção |
| 100 | A que distância do player o Cubit que segue para de andar? | 3 blocos, configurável, valor geral. Acima disso anda, e até 3 blocos para |
| 101 | O Cubit que segue, sem bateria, continua sendo o robô que segue? E um Cubit colocado sem bateria, sem outro seguindo, assume o lugar? | Sim, nos dois casos. Ele fica parado, com o vínculo, e com 5% religa e volta a seguir. Se o player ligar outro, o sem bateria vira desligado e deixa de religar sozinho |
| 102 | Um Cubit sai da base quando outro já assumiu o lugar de seguir. Em que estado ele fica? | Parado e desligado, igual à pergunta 71. O clique direito o liga, e ele passa a seguir, conforme a pergunta 75 |
| 103 | Quando entram o config e o `TierCatalog`, já que colocar o Cubit no mundo exige números da regra 3? | Agora, num PR próprio, antes da entidade e só com os valores do Base. Cada etapa seguinte acrescenta as chaves que passar a ler |
| 104 | Qual é o formato das chaves do config? | Por tipo e tier, em `snake_case`: `[general_robot.base]`, com os valores gerais em `[general]`. Os detalhes estão em [`config.md`](../desenvolvimento/config.md) |
| 105 | De qual classe do Minecraft a `RobotEntity` herda? | `PathfinderMob`, com o dono num UUID próprio |
| 106 | Quando entram as GameTests e os gates de datagen e `runServer`? | Já na etapa "Cubit no mundo", e a primeira GameTest prova que uma reprovação faz o `make gametest` falhar |
| 108 | Como salvar o estado do Cubit como item? | Num componente único, `cubit:robot_state`, com tier, energia e vida. Os próximos dados entram como campos opcionais, exceto o inventário, pela pergunta 185. O tipo do robô vem do próprio item |
| 109 | Quem é o dono de um Cubit? | Quem o coloca. O item não guarda o dono, e o robô ganha um UUID novo a cada colocação. A pergunta 172 abre uma exceção para o item danificado |
| 110 | Em que aba do criativo o Cubit aparece, e colocar no criativo gasta o item? | Numa aba própria, `cubit:main`. Colocar sempre gasta o item, porque ele carrega o estado do robô |
| 111 | Qual gate detecta classe de cliente carregada no servidor, já que o `runServer` exige aceitar o EULA? | As GameTests no `pre-push`. Elas carregam o mod com o mesmo alvo de servidor dedicado, sem EULA, e a regra 37 do `AGENTS.md` foi ajustada |
| 112 | Quando roda a conferência do datagen? | No `pre-push`, por meio de `scripts/development/check-generated.sh` |
| 113 | Quanto vale a velocidade 1,0× no jogo? | 0,3 no atributo de movimento, como o lobo. O valor e o multiplicador de cada tier ficam no config |
| 114 | O que acontece quando o Cubit cai na água? | Afunda e anda no fundo, sem nadar e sem se afogar, e o teleporte o resgata |
| 115 | Quando entra o som de esteira? | Na Fase 3, com os bipes. A Fase 2 fica sem esse som |
| 116 | O Cubit leva dano na lava? | Sim, igual ao player sem armadura, e continua pegando fogo ao sair. Na etapa de morte, o item resultante não queima, para cumprir a pergunta 34 |
| 117 | Como o Cubit se comporta pegando fogo? | Ativo, para de seguir e de teleportar, anda ao acaso evitando caminhos perigosos e busca água por perto para se apagar. É uma exceção à regra de a rota evitar água, só enquanto queima. Inativo ou na base, fica parado |
| 118 | Como proteger o item que cai quando o Cubit morre? | Ele não queima nem some com o tempo. O caso de cair no vazio foi decidido nas perguntas 147 e 172, e a pergunta 173 amplia a proteção |
| 119 | O que a proteção do dono cobre, na pergunta 34? | Qualquer dano que o jogo atribua ao dono. O fogo de um isqueiro continua ferindo, porque o jogo não guarda quem o acendeu |
| 120 | Em que ordem seguem o movimento, o fogo e a morte? | Primeiro o PR do movimento. Depois, um PR para o fogo e outro para a morte, antes do inventário |
| 122 | Como foi o roteiro manual do movimento? | Ok, executado pelo desenvolvedor |
| 123 | Os commits das decisões de movimento e das decisões de fogo e morte ficam separados? | Sim, separados |
| 124 | Em que velocidade o Cubit anda enquanto queima? | 1,25× a velocidade do tier, configurável |
| 125 | Até que distância o Cubit em chamas procura água? | 8 blocos, configurável, só em chunks carregados. A pergunta 131 detalha a altura |
| 126 | O que o Cubit faz quando o fogo apaga? | Volta a seguir na hora, pelas regras normais de movimento |
| 127 | Como existe o Cubit danificado, o item que cai quando ele morre? | Um item próprio por tipo de robô: `cubit:damaged_general_robot`, chamado `Damaged Cubit`. Guarda o estado, não pode ser colocado no mundo, não queima, não some com o tempo e não entra em shulker box. Conserta com a caixa de ferramentas, na grade do inventário ou na mesa de craft |
| 128 | O que acontece com a caixa de ferramentas ao consertar? | É consumida a cada conserto. Empilha até 16 |
| 129 | Com quanta vida o Cubit sai do conserto? | 25% da vida, configurável, com a bateria zerada. O Induction Dock recupera o resto |
| 130 | Qual é a receita provisória da caixa de ferramentas? | 7 ferros, 1 redstone e 1 obsidiana, na mesa de craft. Colunas laterais com 3 ferros cada, e coluna central com ferro em cima, redstone no meio e obsidiana embaixo |
| 131 | A busca de água do Cubit em chamas considera blocos de alturas variadas, como degraus? | Sim. Vale em qualquer direção dentro do raio da pergunta 125. A água precisa ter o topo livre e rota até ela. Sem isso, ele tenta outra água ou anda ao acaso |
| 132 | Quais arquivos `.md` têm histórico de modificações? | Só os que estão em `_docs/`. Os outros, como o `README.md`, não têm |
| 133 | O Cubit em chamas pode ficar parado? | Não. Ativo e em chamas, ele nunca para enquanto houver para onde ir. A pergunta 276 limita isso a enquanto houver bateria |
| 134 | Dá para melhorar o modelo do Cubit agora, mais próximo das imagens de conceito? | Sim, depois de terminar o tema do fogo. Antecipa parte da pergunta 60: a geometria melhora já na Fase 2, e animações e expressões seguem na Fase 3 |
| 138 | Como foi o roteiro manual do Cubit em chamas? | Ok, executado pelo desenvolvedor |
| 139 | O que o Cubit faz quando colocam lava no bloco dele? | Ativo, tenta sair pela saída mais próxima, conforme a pergunta 133, mesmo que ainda possa morrer |
| 143 | Como ficou o comportamento na lava, que costuma matar o tier Base antes de sair? | Aprovado. Quanto menor o tier, maior a chance de morrer. O teste no jogo com balde ficou de fora, e a etapa seguiu. Ele foi feito depois, conforme a pergunta 153 |
| 144 | Em que ordem seguem as próximas tarefas? | Histórico só em `_docs/`, revisão dos `.md`, modelo e morte com o item danificado |
| 145 | Qual é o nome no jogo e o ID da caixa de ferramentas? | `Toolbox` e `cubit:toolbox`, um só item para todos os tipos de robô (regra 42) |
| 146 | O `Damaged Cubit` pode entrar no inventário do robô? | Não, como o item normal |
| 147 | O `Damaged Cubit` que cai no vazio se perde? | Não. O item não é destruído e reaparece na última posição segura do robô. Decide o caso deixado em aberto na pergunta 118, e a pergunta 172 detalha quando a posição não pode ser usada |
| 150 | O roteiro manual pode citar números como resultado esperado? | Sim, como exceção, com o aviso de que os valores são os padrões de `tiers.md`, que prevalece |
| 151 | A seção de lava do roteiro manual, com o teste de balde, continua pendente? | O desenvolvedor vai testar agora, e o resultado entra como uma nova resposta, a pergunta 153. Complementa a pergunta 143 |
| 152 | Quem corrige documento desatualizado? | O agente sempre pergunta antes, mesmo quando a correção parece clara. Com a resposta, atualiza no mesmo PR |
| 153 | Como foi o teste de lava com balde do roteiro manual? | Saiu como esperado, executado pelo desenvolvedor, e o caso está resolvido |
| 154 | O template de PR deve pedir também `make check-generated`? | Sim, porque o `pre-push` já roda esse gate |
| 155 | Onde documentar a fonte de FE no ambiente de dev, da pergunta 37? | Na etapa de energia, porque as dependências do Powah ainda não foram conferidas |
| 156 | O `robo-companheiro.md` continua? | Não. O mapa dos documentos vai para `_docs/README.md`, as fases ficam no Roadmap do `README.md`, e o arquivo é removido, porque o resto já existia em outros documentos |
| 159 | O `Damaged Cubit` empilha? | Empilha em 1, como o item normal |
| 160 | Como produzir o novo modelo do Cubit, sem Blockbench na máquina? | O agente gera o `geo.json` e o PNG com um script temporário em `tmp/`. Só os arquivos finais entram no Git |
| 161 | O modelo novo muda a hitbox do Cubit? | Não. Braços e antenas passam um pouco da hitbox, só no visual |
| 162 | Qual é a resolução da textura do corpo? | 128×128, com 2 pixels por unidade do modelo |
| 163 | O que entra no modelo na Fase 2? | Os detalhes das seis vistas e o ícone do item, tudo fixo: colar cinza, olhos normais e bateria sem variação. Detalha a pergunta 134 |
| 164 | Como ficou o novo modelo do Cubit? | Aprovado pelo desenvolvedor, que gostou da modelagem. O resultado de cada passo do roteiro não foi detalhado |
| 165 | Qual é o ícone oficial do mod? | O ícone do item do Cubit. A pergunta 166 ajusta o tamanho na tela de Mods |
| 166 | Como ficou o ícone na tela de Mods? | Bom, mas um pouco grande, e pode ser menor. Fica em arquivo próprio, com o mesmo desenho e margem transparente |
| 167 | Onde o projeto cita que o mod não tem magia? | Só na regra 2 do `AGENTS.md`, que continua valendo. A menção sai da descrição do mod, do `README.md`, do `persona.md` e da abertura do `AGENTS.md` |
| 168 | O texto mais completo da descrição do mod está bom? | Sim. Ele descreve a versão mínima como ficará na `0.1.0` |
| 169 | Como ficou o ícone menor na tela de Mods? | Ok, conferido pelo desenvolvedor |
| 170 | A regra 4 vale para o conserto, que combina itens do mod? | Tem exceção: os materiais continuam só do jogo puro, e uma receita pode combinar itens do próprio mod. A regra 4 do `AGENTS.md` foi ajustada |
| 171 | Como fica o JSON do conserto? | Num tipo de receita do mod, `cubit:robot_repair`, com campos para o item danificado, a `Toolbox` e o resultado, porque o 1.21.1 não tem receita que copie os dados do item |
| 172 | O que acontece se a posição segura da pergunta 147 não puder ser usada? | O item vai para o inventário do dono. Por isso, o item danificado guarda o dono, o que refina a pergunta 109 só para ele |
| 173 | O `Damaged Cubit` resiste a quais danos? | A todos, como explosão e cacto, além do fogo. Amplia as perguntas 118 e 127 |
| 174 | E se o dono estiver offline, ou o item danificado não tiver dono salvo, no resgate do vazio? | O item vai ao spawn do mundo. Complementa a pergunta 172 |
| 175 | Como liberar o vínculo de quem segue, se o robô morre com o dono offline? | O mundo anota o robô morto num `SavedData`, e o vínculo é limpo quando o dono entra |
| 176 | Onde aparecem o `Damaged Cubit` e a `Toolbox`, e como ficam as texturas? | Os dois entram na aba `cubit:main`, com ícones de 16×16 desenhados pelo agente, para avaliação no jogo |
| 177 | Em que pontos ficam os testes da morte? | JUnit no domínio: estado danificado, estado consertado e proteção do dono. GameTests: morte, vínculo, dono, dano, vazio e receita. Roteiro manual: grade, mesa e ícones |
| 178 | Como foi o roteiro manual da morte e do conserto, e os ícones? | Ok, executado pelo desenvolvedor, conforme idealizado. O resultado de cada passo não foi detalhado |
| 179 | O que conta como posição segura, na pergunta 147? | A última posição do robô no chão, fora de fogo, lava e água. A definição foi usada no roteiro aprovado na pergunta 178, sem resposta separada |
| 184 | Em que ordem segue o que falta na Fase 2? | Inventário, energia em uso, Induction Dock, `/cubit reset` e fechamento, com receitas e teste no Iron Meridian |
| 185 | Onde o conteúdo do inventário fica guardado no item? | Num componente novo, `cubit:robot_inventory`, no formato `ItemContainerContents` do jogo, fora do domínio. Refina a pergunta 108 |
| 186 | Como fica a tela do inventário nesta etapa? | Um menu próprio, `RobotMenu`, com o visual do baú do jogo e sem slot extra. O slot de energia entra na etapa de energia em uso, e a tela própria, na Fase 3 |
| 187 | O que acontece com o excedente, se o config reduzir os slots do inventário? | Ao colocar ou carregar o robô, o que não couber cai no chão, ao lado dele. Nada é apagado |
| 188 | Em que pontos ficam os testes do inventário? | JUnit no domínio: regra do excedente. GameTests: quem abre, o que não entra, preservar sem perder nem duplicar, e excedente. Roteiro manual: tela e cliques |
| 189 | Como foi o roteiro manual do inventário? | Ok, executado pelo desenvolvedor, conforme esperado. O resultado de cada passo não foi detalhado. O robô consertado precisou de um comando para ter carga, porque a recarga entra na etapa seguinte |
| 257 | O que conta como andando, para gastar bateria? | Rota em andamento e deslocamento real no tick, inclusive ao fugir do fogo e da lava. Ser empurrado não gasta, e rota sem avanço também não |
| 258 | Onde fica o slot de energia na tela? | Num pequeno painel desenhado pelo mod ao lado da tela do baú, com espaço para os próximos slots exclusivos e a barra de bateria. Não muda os slots do inventário nem a textura do jogo. Complementa a pergunta 186. Em avaliação, sem decisão: o slot de fornalha ficar só para o card de Minerador |
| 259 | Onde salvar o conteúdo do slot de energia? | Em `cubit:energy_slot` no item e `EnergySlot` na entidade, separado do inventário. Atravessa recolher, morrer e consertar. O comportamento automático de esvaziar o inventário não coloca nem tira itens dos slots exclusivos. Automatizar isso, por exemplo pela base ou pelo Servidor, é uma ideia para o futuro |
| 260 | Em que pontos ficam os testes da energia? | JUnit: consumo, extração de redstone sem perder energia e religar. GameTests: gastar andando, não gastar parado ou empurrado, desligar ao zerar mantendo o vínculo, religar sozinho, slot com redstone, clique com redstone desligado e preservar no recolher, morrer, consertar e salvar. Roteiro manual: tela e barra |
| 261 | Como foi o roteiro manual da energia? | O robô sem energia ainda seguiu o player, e os demais passos ficaram ok, executado pelo desenvolvedor, sem detalhar cada passo. O erro está em investigação e bloqueia os commits da etapa. A pergunta 262 corrige a causa provada, e a 263 traz o que falta conferir |
| 262 | Como corrigir o robô parado que volta a andar, porque o `stop()` do jogo não cancela um recálculo de rota pendente? | Com uma navegação própria, `RobotNavigation`, cujo `stop()` também cancela o recálculo. Corrige a causa em todas as paradas, em vez de parar a navegação a cada tick |
| 263 | Como a bateria foi zerada no teste da pergunta 261? | Com um comando. O comando exato não foi informado |
| 265 | Com redstone na mão, o clique em qualquer robô desligado coloca a pilha no slot em vez de ligá-lo, mesmo com carga. Está certo? | Sim, como na pergunta 45. Para ligar um robô desligado com carga, basta clicar sem redstone na mão |
| 266 | O robô para ao zerar a bateria andando, sem nada no slot? | Sim, o robô parou, conforme visto pelo desenvolvedor, depois de seguir com a barra parecendo zerada. O save mostrou 4.500 FE e estado `ACTIVE` antes da parada, numa gravação de hora incerta. Os 0 FE e o estado ao parar não foram conferidos. Complementa a pergunta 261 |
| 267 | Como foi o restante do teste no jogo? | "Acho que está ok", executado pelo desenvolvedor, sem detalhar cada passo. Ao fechar, o save mostrou 299.810 FE, `ACTIVE`, slot vazio e reserva 0, e o log não trouxe erro |
| 268 | O erro da pergunta 261 fica fechado, sem os 0 FE medidos no jogo? | Sim. A GameTest cobre a parada com 0 FE, e a confusão no jogo vinha da barra |
| 269 | As barras de bateria podem mostrar zero com carga restante? | Não. As duas mostram ao menos um risco enquanto houver carga, e só ficam vazias com 0 FE |
| 270 | Como fica o passo 5 da seção Energia do roteiro manual? | Com o slot vazio e o comando que zera a bateria, porque andar até zerar leva minutos, e a recarga pelo slot impede o robô de parar |
| 271 | Quanto o Cubit gasta ao andar, com peso e na água? | Andar: Base 15, Avançado 18, Elite 22 e Ultimate 30 FE/t. Inventário cheio e água somam, cada um: +5, +6, +7 e +10 FE/t. No Base, cheio dá 20, na água dá 20, e os dois juntos dão 25. Entra em issue própria, depois da #57. Muda o consumo de `tiers.md` |
| 272 | O que conta como inventário cheio, para o consumo extra? | Todos os slots ocupados, mesmo com pilhas incompletas |
| 273 | A velocidade muda com o inventário cheio? | Sim, para 0,75×, configurável. Em chamas, a velocidade do fogo continua valendo |
| 274 | O que conta como na água, para o consumo extra? | O robô em contato com a água, pelo `isInWater` do jogo, inclusive numa poça rasa e ao fugir do fogo |
| 275 | Quais condições negativas fazem o Cubit perder FE extra? | Fogo, lava e os efeitos que o jogo marca como nocivos (`HARMFUL`), inclusive de outros mods, sem lista fixa. Vale só com o robô ativo. Várias juntas cobram um extra só, e um golpe não conta. Entra na issue da pergunta 271 |
| 276 | O dreno por condição negativa pode zerar a bateria? | Sim. Ao zerar, o robô desliga e, em chamas, queima parado. Limita a pergunta 133, que passa a valer enquanto houver bateria |
| 277 | Quanto FE extra por tick custa uma condição negativa ativa? | Base 25, Avançado 50, Elite 125 e Ultimate 250 FE/t, também com o robô parado, e somados ao andar. Exceção à pergunta 32 |
| 278 | Quanto custa subir? | 1,2× o custo do plano, ou seja, +20% sobre andar um bloco: Base 15, Avançado 18, Elite 22 e Ultimate 30 FE por bloco subido. É proporcional à altura, vale só na própria rota, e descer não devolve energia. Entra na issue da pergunta 271 |
| 279 | Como foi o passo 5a do roteiro manual, das barras de bateria? | Ok, como esperado, executado pelo desenvolvedor: a barra agora está precisa. O resultado de cada passo não foi detalhado |

### Premissas aprovadas

- Clique direito abre o inventário, e Shift + clique direito recolhe.
- GeckoLib já na versão mínima, com geometria provisória.
- O item empilha em 1 e mostra a bateria numa barra.
- O item não entra no inventário do robô nem em shulker boxes.
- A rota do robô evita água e lava, e ele não leva dano de queda.
- O config só tem os valores do Base por enquanto, com estrutura por tipo de robô e tier (pergunta 91).
- Controle remoto, solar, Jade, nome e cor ficam fora da versão mínima.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 14:23 | Registra a resposta 279. |
| Marcelo Fabiano | 2026-11-05 14:13 | Registra a resposta 278. |
| Marcelo Fabiano | 2026-11-05 14:12 | Registra as respostas 275, 276 e 277 e aponta as 32 e 133 para elas. |
| Marcelo Fabiano | 2026-11-05 14:10 | Registra as respostas 271, 272, 273 e 274. |
| Marcelo Fabiano | 2026-11-05 13:54 | Registra as respostas 268, 269 e 270. |
| Marcelo Fabiano | 2026-11-05 13:53 | Registra a resposta 267. |
| Marcelo Fabiano | 2026-11-05 13:51 | Registra a resposta 266. |
| Marcelo Fabiano | 2026-11-05 13:30 | Registra as respostas 262, 263 e 265 e aponta a 261 para elas. |
| Marcelo Fabiano | 2026-11-05 13:13 | Registra a resposta 261. |
| Marcelo Fabiano | 2026-11-05 12:52 | Registra as respostas 257, 258, 259 e 260. |
| Marcelo Fabiano | 2026-11-05 10:44 | Aponta a resposta 47 para a pergunta 198. |
| Marcelo Fabiano | 2026-11-05 02:29 | Registra a resposta 189. |
| Marcelo Fabiano | 2026-11-05 02:11 | Registra as respostas 185, 186, 187 e 188 e aponta a 108 para a 185. |
| Marcelo Fabiano | 2026-11-05 02:07 | Registra a resposta 184. |
| Marcelo Fabiano | 2026-11-05 01:41 | Registra as respostas 178 e 179. |
| Marcelo Fabiano | 2026-11-05 01:14 | Registra as respostas 174, 175, 176 e 177. |
| Marcelo Fabiano | 2026-11-05 01:12 | Registra as respostas 170, 171, 172 e 173 e aponta as 109, 118 e 147 para elas. |
| Marcelo Fabiano | 2026-11-05 01:01 | Registra as respostas 168 e 169. |
| Marcelo Fabiano | 2026-11-05 00:56 | Registra a resposta 167. |
| Marcelo Fabiano | 2026-11-05 00:55 | Registra a resposta 166 e aponta a 165 para ela. |
| Marcelo Fabiano | 2026-11-05 00:35 | Registra a resposta 165. |
| Marcelo Fabiano | 2026-11-05 00:34 | Registra a resposta 164. |
| Marcelo Fabiano | 2026-11-05 00:31 | Registra as respostas 160, 161, 162 e 163. |
| Marcelo Fabiano | 2026-11-05 00:17 | Registra a resposta 159. |
| Marcelo Fabiano | 2026-11-05 00:15 | Registra a resposta 156. |
| Marcelo Fabiano | 2026-11-05 00:09 | Registra as respostas 154 e 155. |
| Marcelo Fabiano | 2026-11-05 00:07 | Registra a resposta 153 e aponta a 143 e a 151 para ela. |
| Marcelo Fabiano | 2026-11-05 00:05 | Registra as respostas 150, 151 e 152. |
| Marcelo Fabiano | 2026-11-05 00:02 | Registra as respostas 145, 146 e 147 e aponta a 118 para a 147. |
| Marcelo Fabiano | 2026-11-04 23:40 | Registra as respostas 143 e 144. |
| Marcelo Fabiano | 2026-11-04 23:11 | Registra a resposta 139. |
| Marcelo Fabiano | 2026-11-04 22:56 | Registra a resposta 138. |
| Marcelo Fabiano | 2026-11-04 22:05 | Registra a resposta 134. |
| Marcelo Fabiano | 2026-11-04 22:01 | Registra a resposta 133. |
| Marcelo Fabiano | 2026-11-04 21:51 | Registra a resposta 132. |
| Marcelo Fabiano | 2026-11-04 21:49 | Registra a resposta 131. |
| Marcelo Fabiano | 2026-11-04 21:19 | Registra as respostas 127, 128, 129 e 130. |
| Marcelo Fabiano | 2026-11-04 21:02 | Registra as respostas 124, 125 e 126. |
| Marcelo Fabiano | 2026-11-04 20:54 | Registra as respostas 122 e 123. |
| Marcelo Fabiano | 2026-11-04 20:41 | Registra as respostas 117, 118, 119 e 120. |
| Marcelo Fabiano | 2026-11-04 20:31 | Registra a resposta 116 e tira a lava da resposta 114. |
| Marcelo Fabiano | 2026-11-04 20:09 | Registra as respostas 113, 114 e 115. |
| Marcelo Fabiano | 2026-11-04 19:56 | Registra as respostas 111 e 112. |
| Marcelo Fabiano | 2026-11-04 19:24 | Registra as respostas 108, 109 e 110. |
| Marcelo Fabiano | 2026-11-04 19:05 | Registra as respostas 103, 104, 105 e 106. |
| Marcelo Fabiano | 2026-11-04 18:50 | Registra as respostas 100, 101 e 102. |
| Marcelo Fabiano | 2026-11-04 18:24 | Registra a resposta 99. |
| Marcelo Fabiano | 2026-11-04 18:20 | Registra a resposta 98 e a pergunta 99. |
| Marcelo Fabiano | 2026-11-04 17:52 | Registra a resposta 97. |
| Marcelo Fabiano | 2026-11-04 17:48 | Registra a resposta 96. |
| Marcelo Fabiano | 2026-11-04 17:48 | Registra a resposta 95. |
| Marcelo Fabiano | 2026-11-04 17:26 | Corrige a seção de pendentes e aponta as respostas refinadas por outras. |
| Marcelo Fabiano | 2026-11-04 17:16 | Ajusta a premissa do config para tipo de robô e tier. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:12 | Registra as respostas 84, 85 e 86. |
| Marcelo Fabiano | 2026-11-04 17:11 | Registra a resposta 81 e atualiza a 73. |
| Marcelo Fabiano | 2026-11-04 17:08 | Registra as respostas 59 a 65 e 71 a 76. |
| Marcelo Fabiano | 2026-11-04 16:57 | Cria o arquivo com as perguntas e respostas da fase. |
