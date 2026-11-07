# Domínio

Regras do Cubit em Java puro, sem depender do jogo (regra 12 do `AGENTS.md`). Quem chama fornece os dados: o domínio não lê config, relógio nem o mundo.

## Linguagem

| Termo | Significado |
|---|---|
| `Energy` | Quantidade inteira de FE, nunca negativa |
| `Percentage` | Porcentagem inteira, de 0 a 100 |
| `Battery` | Capacidade e carga atual. É imutável, e cada operação devolve uma bateria nova |
| `PowerState` | Estado do robô |
| `PowerRules` | Regras que mudam o estado por causa da energia |
| `RobotColor` | A cor da carcaça, uma para cada corante do jogo. A `key` é o nome do corante, usado nos saves e nas texturas |
| `PaintRules` | Quando um corante muda o robô e quando ele reage |
| `RobotType` | Identifica o Cubit nos IDs e no config. O robô é único, e é um dado, identificado por um ID |
| `RobotTier` | Nível de evolução: Base, Advanced, Elite e Ultimate. A `key` é o nome usado no config e nos saves |
| `RobotState` | O que o robô guarda ao virar item: tier, energia, vida, a chave da lanterna, a cor, a chave dos scanners, o filtro de minérios e a chave das falas. `damaged` e `repaired` dão o estado ao morrer e ao consertar |
| `TierSpec` | Números de um tipo de robô num tier |
| `TierCatalog` | Entrega o `TierSpec` de um tipo de robô num tier. O domínio define a interface, e o config a implementa |
| `FollowAction` | O que o robô que segue faz: seguir, parar ou teleportar |
| `BurningAction` | O que o robô faz em chamas: nada, sair da lava, buscar água ou andar ao acaso |
| `FollowDecision` | Escolhe a `FollowAction` conforme o estado, a dimensão e a distância |
| `DamagingBlockReaction` | Diz se o robô sai de cima de um bloco que fere |
| `FollowingRobotPolicy` | Garante que só um robô siga o player |
| `RobotActivity` | O que o robô faz no tick: andar, peso, água, altura subida, condição negativa e lanterna acesa. Quem chama informa, e o domínio decide o gasto |
| `ConsumptionExtras` | Extras de energia de um `TierSpec`, por situação |
| `RobotExpression` | O que a tela do rosto mostra |
| `ExpressionRules` | Qual expressão vence e quando a troca toca o bipe |
| `RobotSituation` | O que vale agora para escolher a expressão |
| `ExpressionTimer` | Quanto tempo uma expressão temporária ainda dura |
| `BlinkFrame` e `BlinkCycle` | Os quadros da piscada e qual vale em cada tick |
| `GazeDirection` e `GazeTracker` | Para onde os olhos olham, com a subida e a descida de cada tick |
| `RobotPose` | A animação do corpo: desligado, andando ou parado, e se os braços ficam erguidos |
| `OwnerGaze` | Para onde os olhos olham por causa da altura do dono, e em que nível ele está |
| `BodyTilt` | Quanto o corpo inclina para o dono em outro nível |
| `TrackFrames` | Qual quadro da esteira vale em cada distância andada |
| `OwnerLook` | Quando o robô vira o corpo para o dono |
| `HealthLevel` | A vida mostrada numa barra, sem falso zero |
| `RobotNotice` | Os textos curtos que o robô mostra ao dono |
| `NoticeRules` | Em que estado o robô fala |
| `NoticeRepeat` | Quanto um aviso ainda espera para poder se repetir |
| `OreKind` | Um tipo de minério, pela tag: uma subtag, como `c:ores/diamond`, com nome em caixa alta, ou `c:ores`, sem nome |
| `OreFilter` | Os minérios que o dono pediu ao scanner, do mais antigo para o mais novo |
| `OreSignals` | Quais minérios avisam e qual deles o texto cita, a partir da lista dos raros |
| `ScannerSpec` | Números dos scanners de um tier: raios, intervalos, custos e vagas do filtro |
| `ScanRules` | Quando os scanners funcionam e o que conta como mob hostil |
| `ScanArea` | A esfera lida em volta do robô, com cada bloco do cubo que a contém numerado |
| `MarkLife` | Até quando uma caixa em volta do que um scanner achou continua desenhada |
| `MarkPulse` | A força do pulso suave das caixas dos scanners em cada momento |
| `FreshFinds` | O que um scanner já avisou, para só o que é novo avisar de novo |
| `ChatTopic` | O que o robô comenta e quantas frases tem para isso |
| `WeatherReading` | O que o robô lê do mundo para comentar: horário, céu, chuva, trovoada e frio |
| `ChatWatch` | O que mudou entre duas leituras e qual assunto isso dá |
| `ChatRules` | Quando o robô pode comentar algo |
| `BiomeWatch` | Quando o robô entra em outro grupo de biomas |
| `LeftBehindWatch` | Quando o robô reclama de o dono se afastar |
| `RespawnReturn` | Se o robô que segue volta com o dono que renasceu |
| `BatteryIndicator` | O que o indicador nas costas mostra: vazio, ou de 1 a 4 níveis, normal ou crítico |
| `LightLevel` | Nível de luz do jogo, de 0 a 15 |
| `LampPolicy` | Quando a lanterna acende e apaga, conforme a chave, o estado e a luz do ambiente |
| `RobotLight` | A luz final que o robô dá ao mundo: a tela e a lanterna |
| `LampPulse` | A força do halo das antenas em cada momento |
| `SolarOutput` | O que o painel dá por tick, de dia e à noite |
| `SolarPanel` | Quando o painel dá energia |
| `SolarPulse` | A força do brilho da célula em cada momento |
| `PowerSound` | Som tocado uma vez, ao ligar ou desligar |
| `ContinuousSound` | Som em loop enquanto a condição valer: esteira, curto elétrico e recarga |
| `RobotSoundRules` | Diz qual som cada situação toca |
| `SlotRecharge` | Reserva de FE do slot de energia e sua passagem para a bateria |
| `DockStock` | Estoque de energia de um Induction Dock e as transferências dele, sem criar nem perder FE |
| `DockStatus` | O que a tela de um Dock mostra |
| `DockStay` | Vida recuperada por tick e quando o robô sai da base |
| `DockQueue` | Robôs aguardando um Dock ocupado, por ordem de chegada |
| `DockTrip` | Um robô a caminho do Dock: o quanto chegou perto, há quanto tempo não avança e o que faz em cada tick |
| `DockRemoval` | Quem pode quebrar um Dock |
| `RobotClick` | O que o clique direito faz no robô |
| `InventoryFit` | Separa os itens que cabem nos slots do config dos que sobram |
| `Ownership` | Diz quem é dono de um robô ou de um Dock, e se os dois têm o mesmo dono |
| `OwnerProtection` | Diz se um dano é do dono e, por isso, não fere o robô |
| `VoidRescueTarget` | Para onde vai o robô danificado que caiu no vazio |
| `FollowingOutcome` | Resultado da política: estado do robô, quem segue e quem desligar |

## Regras e porquês

- **Falas pelo que muda:** o robô comenta quando algo muda, para cada fala ter um motivo, e o sorteio só escolhe a frase. O que ele não pôde comentar se perde, para não anunciar uma tempestade que já passou (perguntas 572 a 583).
- **Redstone, sempre que calmo:** o Cubit ama redstone, então fala quando o dono a minera, sem chance. Só com a expressão calma, para nunca cobrir o aviso do scanner (perguntas 619 e 620).
- **Animal abatido, às vezes:** a chance evita uma fala a cada animal numa fazenda, e um animal de estimação nunca vira piada (pergunta 587).
- **Bioma por grupo:** só uma mudança de grupo conta, e um bioma sem grupo, como um rio, não muda nada, para o robô não repetir o comentário (pergunta 586).
- **Reclamar uma vez:** ao se afastar, o dono só ouve de novo depois de o robô chegar perto, para não repetir a cada passo. Um salto além do teleporte não conta, porque não é o dono correndo (pergunta 585).
- **Falas só com a expressão calma:** qualquer outra expressão indica algo mais importante, como um mob ou um dano.
- **Volta com o dono:** um robô longe, ou em outra dimensão, ficaria para trás ao descarregar o chunk. Perto, ele anda como sempre, e a caminho do Dock continua indo, porque o dono o chamou (pergunta 567).
- **Só o que é novo avisa:** o mesmo mob ou bloco não avisa de novo enquanto continuar no raio, porque o intervalo mínimo sozinho repetiria o mesmo minério a cada varredura. O que sai do raio é esquecido (pergunta 561).
- **Esfera, não cubo:** o canto de um cubo fica a cerca de 1,7 vez o raio, então só conta o que está a até o raio em linha reta. Os dois scanners usam a mesma regra (perguntas 604 e 606).
- **Varredura por número:** a área numera os blocos, então uma varredura em fatias só guarda um número entre os ticks, sem alocar nada (regra 21).
- **Bloco pela subtag:** um bloco está em `c:ores` e nas subtags, e só elas dizem o que ele é. `c:ores` só vale para quem não tem subtag, então um filtro nele não pega todo minério (perguntas 546 e 566).
- **Raro vem antes do filtro:** o texto cita o raro mais raro e, só sem raros, um minério do filtro (pergunta 562).
- **Filtro cheio troca o mais antigo:** assim, um clique sempre funciona, mesmo com só uma vaga (pergunta 548).
- **Mesma cor não gasta:** pintar da cor atual não faz nada, para não desperdiçar o corante (pergunta 509).
- **Só o ativo reage:** desligado, a tela está apagada, e hibernando ela sempre mostra carregando (perguntas 57 e 510).
- **Energia inteira:** evita erro de arredondamento, e uma transferência nunca cria nem perde FE.
- **Operações informam o que moveu:** `recharge` devolve o que foi aceito, e `consume`, o que foi consumido. Assim, quem chama sabe o que sobrou e não perde energia.
- **Expressões:** o estado vem primeiro, porque tela apagada e carregando não dependem do resto. Depois vêm vida baixa e dano, os avisos e o sono, e a feliz cede a eles (perguntas 368 e 379). O bipe não toca ao sair de uma tela apagada ou carregando, porque ligar e ser colocado têm som próprio e carregar o mundo não é uma troca (pergunta 369).
- **Pose pela expressão:** o cliente já recebe a expressão, e tela apagada e carregando só existem com o robô desligado ou na base, então a pose não precisa de um dado novo. Empurrado, ele continua na pose de desligado (pergunta 394).
- **Braços junto do dano:** ficam erguidos por um tempo menor que o da expressão triste ou de olhos em X, sem parar o balanço do corpo (perguntas 403 e 445).
- **Olhar para o dono:** um pulo ou uma queda rápida movem os olhos por um tempo, e estar em outro nível os mantém enquanto durar. O limite da queda fica acima da velocidade de pousar de um pulo, para o pulo não terminar com os olhos para baixo. O movimento do próprio robô vence (perguntas 404, 406 e 407).
- **Inclinação só por nível:** o corpo inclina de leve com o dono em outro nível, mas não num pulo, que move só os olhos, para ele não balançar a cada salto (perguntas 410 e 411).
- **Números das animações em constantes:** são só visuais, como os da piscada (pergunta 398).
- **Olhar sem tremer:** a direção fica um tempo depois do movimento, porque um degrau dura poucos ticks (perguntas 375 e 376).
- **Sons:** entrar na base não toca o som de desligar, porque a recarga já toca, e a recarga só vale com a carga subindo de fato, para não tocar com a bateria cheia nem com o Dock sem energia (perguntas 358 e 359).
- **Barra sem falso zero:** `level` só devolve 0 com a bateria vazia. Com qualquer carga, devolve ao menos 1, para o player não achar que o robô ativo está sem energia (pergunta 269).
- **Indicador crítico em todos os níveis:** o limite crítico vem do config e pode cobrir mais de um nível, então o vermelho não pode reduzir o preenchimento. O vazio vence o crítico, porque só vale com 0 FE (perguntas 415 e 416).
- **Brilho junto da tela:** a tela e o indicador brilham pela mesma regra, `RobotExpression.glows`, para nunca discordarem (pergunta 417).
- **Aviso só do ativo:** um robô desligado ou na base não fala (perguntas 429 e 443). Quem chama garante que é o robô que segue (pergunta 430).
- **Intervalo corre sempre:** a espera continua mesmo sem a condição, para um robô que oscila em volta do limite não repetir o aviso antes da hora.
- **Comparação de porcentagem sem divisão:** `hasAtLeast` multiplica os dois lados, para ser exata com os valores de `tiers.md`.

- **Números por dado:** as regras recebem os números de um `TierSpec`, sem constantes no código (regra 39 do `AGENTS.md`).
- **Upgrade:** as regras de evolução entre tiers entram na Fase 4, junto dos módulos.
- **Estado guardado se ajusta ao config:** o config pode mudar enquanto o robô está no item, então `batteryFor` e `healthFor` limitam os valores ao `TierSpec` atual. A vida nunca fica abaixo de 1, porque um robô colocado sem vida morreria na hora.

- **Só o ativo se move:** inativo ou em hibernação, o robô para e não teleporta. Em outra dimensão também para, porque o teleporte só vale na mesma dimensão.
- **Sair da lava vem antes de buscar água:** é o que mais fere. A rota nunca entra em lava, então a saída é direta, até o bloco seguro mais próximo (pergunta 139).
- **Bloco que fere:** só o ativo sai de cima, porque é o único que se move, e fogo e lava vêm antes, porque ferem mais (perguntas 284 e 285).
- **Em chamas, a reação vem antes de seguir:** só o robô ativo reage, porque é o único que se move. Parado, ele continua queimando (pergunta 117).
- **Distância inválida:** o robô para. É o estado mais restritivo, e o jogo não cai por isso (regra 17 do `AGENTS.md`).
- **Colocar e sair da base são a mesma regra:** o robô segue se nenhum outro estiver seguindo. Se houver outro, fica desligado, porque o clique direito só liga um robô desligado, e assim o player consegue fazê-lo seguir (perguntas 72, 75 e 102).
- **Sem bateria, o vínculo continua:** o robô para e, ao religar, volta a seguir. Ligar outro robô o desliga, e ele deixa de religar sozinho (pergunta 101).
- **Quem guarda o vínculo é quem chama:** a política recebe quem segue e devolve o resultado, sem ler os dados do player.
- **Ativo sem o lugar de seguir:** um robô ativo que descobre que não é o que segue se desliga, porque só um robô desligado volta a seguir com o clique. É o caso de quem estava em chunk descarregado na troca e do robô que perdeu o vínculo pelo `/cubit reset` (pergunta 326).
- **Morte e conserto zeram a bateria:** ao morrer, o robô fica sem vida e sem energia. O conserto devolve parte da vida, e o Induction Dock recupera o resto (perguntas 34 e 129).
- **Sem dono, a resposta é não:** dois `Optional` vazios são iguais em Java, então a regra exige um dono presente, para um robô sem dono não combinar com um Dock sem dono (regra 20 do `AGENTS.md`).
- **Dano do dono:** só conta o que o jogo atribui a ele. O fogo de um isqueiro não tem autor, então fere (pergunta 119).
- **Resgate no vazio:** a ordem é a posição segura, o dono online e o spawn do mundo. Sem destino possível, o item espera, porque nunca é destruído nem força um chunk a carregar (perguntas 147, 172 e 174).
- **Consumo por deslocamento:** quem chama informa se o robô andou, e o domínio só gasta nesse caso (pergunta 257).
- **Consumo por situação:** peso, água e subida só somam ao andar. A condição negativa soma mesmo parado, e só uma vez, ainda que haja várias. A lanterna acesa também soma parado (pergunta 469). O robô inativo não gasta (perguntas 271 a 278).
- **Dois limites na lanterna:** ela acende abaixo de um e apaga a partir do outro, para não piscar perto do limite (pergunta 470). `fitting` existe porque o config pode trazer os dois invertidos, e o construtor rejeita isso.
- **Dia pelo relógio:** o painel olha o horário do mundo, e não a luz do céu, porque o clima não tem efeito (pergunta 53).
- **Só a luz espera:** a chave e o estado valem na hora, e só a leitura do mundo roda em intervalos, para a lanterna nunca ficar acesa num robô desligado (pergunta 474).
- **Luz junto da tela:** `RobotLight` dá zero com a tela apagada, pela mesma regra do brilho, `RobotExpression.glows` (perguntas 378 e 465).
- **Lanterna na hibernação:** apagada e sem consumo, porque o robô está imóvel e carregando. A tela continua dando a claridade (perguntas 465 e 466).
- **Subida em FE inteiro:** o extra é arredondado, porque energia é inteira (regra 13). Descer não devolve nada, porque criaria FE.
- **Preso a caminho do Dock:** um movimento pequeno não conta como avanço, para um robô contra uma parede desistir. Preso com o Dock ocupado, ele aguarda onde está, porque teleportar não o deixaria entrar. Perto do Dock, o tempo não conta, porque ele pode estar esperando a vez (perguntas 84 e 301).
- **Ordem do clique:** o Shift sempre recolhe, inclusive com corante na mão, e o corante vem antes de abrir o inventário, para pintar em qualquer estado (perguntas 510 e 516).
- **Estado interno também valida:** os records que só guardam tempo ou ângulo rejeitam valor inválido, como os demais value objects (pergunta 526).
- **Fila sem remoção:** um robô se informa a cada tick, então o que saiu do mundo perde o lugar sem ninguém precisar tirá-lo (perguntas 85 e 86).
- **Cheio espera um momento:** o robô cheio fica cerca de 3 s antes de sair, para o player ver a tela completa (pergunta 312).
- **Vida igual em todos os tiers:** a vida por tick segue a vida máxima, então todos levam o mesmo tempo para encher (pergunta 63).
- **Reserva do slot:** um item só é consumido quando a energia dele cabe, e a reserva nunca some, para a transferência não criar nem perder FE (regra 13 e pergunta 64).
- **Excedente:** se o config reduzir os slots, o que não couber sobra, e quem chama o larga no chão. Nada é apagado (pergunta 187).
- **Vínculo apontando para o próprio robô:** não conta como outro robô seguindo. Assim, um vínculo antigo não deixa o robô desligado.

## Estados

Os documentos definem três estados: ativo, inativo e hibernação. No código, o inativo se divide em dois, porque a causa muda o comportamento:

| `PowerState` | Estado | Acesso ao inventário | Como sai |
|---|---|---|---|
| `ACTIVE` | Ativo | Sim | Bateria zerada, troca de robô, perda do lugar de seguir ou entrada na base |
| `INACTIVE_EXHAUSTED` | Inativo, sem bateria | Não | Sozinho, ao atingir a carga mínima |
| `INACTIVE_SWITCHED_OFF` | Inativo, desligado com carga | Não | Clique direito, se houver carga |
| `HIBERNATING` | Hibernação, na base | Sim | Ao sair da base |

O desligamento pelo controle remoto (pergunta 56) entra na Fase 4, junto do controle.
