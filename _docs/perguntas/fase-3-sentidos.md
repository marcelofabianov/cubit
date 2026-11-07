# Fase 3: Sentidos

## Pendentes

| # | Pergunta | Recomendação |
|---|---|---|
| 425 | Onde o player aciona as chaves da pergunta 424, qual é o estado inicial de cada uma, e o que "Hibernar" da chave geral faz fora do Dock? | Na tela do Cubit, e também no Cubit Remote a partir da Fase 4. Todas começam ligadas e ficam nas configurações que o item guarda, como na [pergunta 343](fase-4-upgrades-e-autonomia.md). Hibernar fora do Dock segue a [pergunta 198](fase-5-trabalhos.md), hoje prevista só para a Fase 5, então precisa de decisão antes de antecipar. As perguntas 467 e 468 respondem a parte da lanterna, e a 475 deixa o resto para o painel solar |

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 48 | Onde os textos do Cubit aparecem? | Na barra acima da hotbar, só para o dono e com intervalo mínimo entre repetições. A pergunta 436 troca a barra do jogo por uma camada própria, no mesmo lugar |
| 49 | Os cinco textos ficam assim em inglês? | Sim: `Critical battery`, `Enemy nearby`, `Ore found`, `I need to recharge` e `Items unloaded`. A pergunta 435 acrescenta o prefixo `Cubit: `, e as perguntas 440 e 441, dois textos |
| 52 | Quais são os novos valores do solar, abaixo do consumo? | De dia: Base 5, Advanced 10, Elite 12 e Ultimate 16 FE/t. À noite, 25% do valor de dia: Elite 3 e Ultimate 4 FE/t |
| 53 | Como o solar se comporta com blocos, clima e outras dimensões? | Só blocos importam: coberto não carrega, e descoberto carrega 100%. O clima não tem efeito. No Nether e no End não carrega |
| 57 | Qual é a expressão em hibernação? | Sempre carregando |
| 66 | A partir de quanta bateria o Cubit mostra a expressão de sono? | 10%, igual à bateria crítica. A pergunta 432 muda para 5% |
| 67 | O texto `Ore found` vale para qual minério? | Para minério do filtro do scanner. Bipe e surpresa, só para os raros |
| 92 | Qual é a expressão ao recarregar pelo slot, fora da base? | Inativo, tela apagada. Ativo, a expressão normal. A expressão carregando fica só para a base |
| 135 | Quais sons o Cubit tem? | Oito sons diferentes: esteiras ao andar, como as de um tanque de guerra; mob hostil encontrado; minério raro encontrado; bipe padrão ao trocar de expressão; curto elétrico enquanto pega fogo; ao ligar; ao desligar; e enquanto é recarregado. O detalhe está em [`visual.md`](../conceito/visual.md#sons). As perguntas 316 e 320 acrescentam o som do ataque e o de colocar no mundo |
| 136 | O bipe padrão vale para a troca de quais expressões? | Substituída pela pergunta 148, que limita às expressões sem som exclusivo. Esta resposta substituiu a pergunta 12 |
| 137 | Os sons são do desenvolvedor e podem ser publicados? | São dele, gerados com ferramentas de IA de estúdio. Os originais ficam em `tmp/wav/`, fora do repositório |
| 140 | Os sons convertidos estão bons? | Não, o desenvolvedor ouviu cortes e sensação de congelamento ou perda. A conversão precisa ser revista |
| 141 | Quais versões dos sons ficaram boas no VLC? | Os sons 3, 4 e 6 estão normais. Os sons 1, 2, 5, 7 e 8 têm cortes na reprodução |
| 142 | Os cortes nos sons vêm dos arquivos ou do reprodutor? | Do reprodutor: no VLC havia cortes, e no `pw-play` os sons tocaram sem problema. A conversão está certa. Desfaz a conclusão da pergunta 140 e explica a 141 |
| 148 | Ao trocar para uma expressão com som próprio, o bipe padrão toca junto? | Não. O bipe padrão toca nas trocas de expressão que não têm um som exclusivo. Substitui a pergunta 136 |
| 149 | Qual é o intervalo mínimo entre repetições dos textos e dos alertas sonoros? | O valor será definido na Fase 3, num teste no jogo, e ficará no config. Complementa a pergunta 48. As perguntas 433 e 444 definem o valor |
| 157 | Quais são as reações do Cubit ao ligar e ao pegar fogo? | Ao ligar, toca o som de ligar. Ao pegar fogo, toca o som de curto elétrico. Confirma a pergunta 135 |
| 158 | Qual expressão o Cubit mostra ao ligar e ao pegar fogo? | Ao ligar, a normal. Ao pegar fogo, mantém a atual, sem troca |
| 264 | Quando entram o visual melhor e os tooltips do painel do slot de energia? | Na tela própria da Fase 3, a da [pergunta 186](fase-2-versao-minima.md). Na Fase 2, o painel continua simples |
| 316 | Qual é o som do ataque do Cubit? | O desenvolvedor forneceu `cubit_ataque_choque.wav`, convertido da mesma forma que os demais, para `shock_attack.ogg`. Passam a ser nove sons. O original fica em `tmp/wav/`. Amplia a pergunta 135 |
| 320 | O que fazer com o som novo, `robot_quando_colocado_no_mundo.wav`? | Converter para Ogg e colocar no diretório dos demais sons do projeto, para ser usado. Convertido da mesma forma que os demais, para `placed.ogg`. Passam a ser dez sons. O original fica em `tmp/wav/`. Amplia a pergunta 135. As perguntas 321 e 322 detalham o nome, o momento e a fase |
| 321 | O nome `placed.ogg` e o momento "ao ser colocado no mundo" estão certos? | Sim. Detalha a pergunta 320 |
| 322 | Em que fase o som de colocar no mundo passa a tocar? | Na Fase 3, junto dos outros oito. Detalha a pergunta 320 |
| 421 | Como foi o roteiro manual do colar, do indicador de bateria e do tronco central com +0,5? | Teste finalizado, tudo ok, executado pelo desenvolvedor, sem detalhar cada passo. Ele não pediu +1, então o tronco fica em 2 unidades. O `make server` e o olhar de longe não foram citados em separado |
| 532 | Como foi o teste no jogo da issue #106? | "Tudo certo no teste", executado pelo desenvolvedor, sem detalhar cada passo: dono normal, Dock sem dono, operador e aviso no log. O agente não entendeu a resposta como autorização de commits |
| 531 | Há algo a acrescentar à issue #106? | Não: "continue". O agente entendeu como autorização para criar a branch e começar, não para commits |
| 530 | Como foi o teste rápido no jogo da refatoração da issue #105? | "Testes feito tudo ok", executado pelo desenvolvedor, sem detalhar cada passo: energia, Dock, vazio e config. Na mesma mensagem, ele autorizou os sete commits do plano. O push e o PR não foram autorizados |
| 529 | O jogo já foi fechado? | Sim. Os gates podem rodar no PR da issue #105 |
| 528 | Onde entram a margem de 4 blocos e o raio de 128 repetidos em constantes e a energia negativa corrigida sem aviso no log? | Na issue de segurança, a #106 |
| 527 | O que fazer com a cobertura, que mede só instruções do domínio? | O gate também exige 85% de ramos no domínio. Além disso, o agente testa se o JaCoCo consegue medir as GameTests, só como relatório, sem reprovar nada, e traz o resultado antes de adotar |
| 526 | Cinco records do domínio aceitam valores inválidos no construtor: `ExpressionTimer`, `NoticeRepeat`, `GazeTracker`, `OwnerGaze` e `BodyTilt`. O que fazer? | Validar os cinco no construtor, com JUnit, como pede a regra 14. Antes, o agente confere se algum config pode chegar a eles com um valor que passaria a lançar erro. Os outros nove records sem construtor próprio não mudam |
| 525 | O que reforçar nos gates? | Só o ArchUnit, sem dependência nova: domínio isolado dos outros pacotes, código comum sem `client`, `block` sem `entity` (regra 42) e sem `get*` no domínio. O CI fica para depois |
| 524 | Quem pode quebrar um Induction Dock sem dono? | Só um operador em criativo, com aviso no log ao carregar, para falhar seguro. Num mundo sem comandos, esse Dock raro fica no mundo. Muda o comportamento atual, em que qualquer um o quebra. Detalha a [pergunta 302](fase-2-versao-minima.md) |
| 523 | O que fazer com as GameTests e o datagen, que vão no jar publicado? | Excluir do jar, junto das estruturas de teste, antes da `0.1.0`. O código continua em `src/main`, então os métodos públicos usados só por testes continuam públicos |
| 522 | Como organizar o que a auditoria de qualidade encontrou? | Sim, como proposto: no PR #105, só o que muda a forma (Javadoc duplicado, parâmetro sem uso e `insert` repetido); uma issue de segurança, com o checksum do wrapper, a regra única de dono e os testes de permissão do Dock; uma issue de domínio, com as regras de decisão que estão fora de `domain/`, antes dos scanners; e uma rodada de decisões para o resto. A resposta não define o lugar da margem repetida e do log de energia negativa, e o agente os leva à rodada |
| 521 | O agente corrige o `visual.md`, que cita a expressão feliz só ao colocar o robô e abrir o inventário, sem pintar e receber etiqueta? | "Pode fazer isso depois". Não entra no PR da refatoração, que só muda a forma, e fica para um PR seguinte, conforme a [pergunta 152](fase-2-versao-minima.md) |
| 520 | As "armadilhas" da pergunta 517 incluem as da máquina, como os scripts de `tmp/modelo/` que desfazem o modelo? | Não: "tmp não faz parte de refatoração, são arquivos temporários para discussões". Os scripts continuam lá, sem apagar. O aviso no `Makefile` ao compilar com o jogo aberto, perguntado junto, não foi citado, e o agente o deixou de fora, porque seria uma ferramenta nova |
| 519 | Qual é o escopo da refatoração? | "Segue o prompt", em vez da sugestão do agente, de fazer só `RobotEntity` e `DockGameTests`. O agente entendeu como os quatro arquivos do prompt de continuidade: `RobotEntity`, `DockGameTests`, `CustomizationGameTests`, dividido em cor e nome, e `TierSection`, com as chaves de consumo e da lanterna em classes aninhadas. `LampGameTests` só fica em observação. Os métodos de `DockableRobot` continuam na entidade, pela regra 42. Só muda a forma, como na resposta 460 |
| 518 | O jogo está aberto? | Sim. Enquanto estiver, o agente não compila nem roda os gates, e a branch remota da personalização espera, porque o `pre-push` compila também ao apagar |
| 517 | A refatoração técnica vem antes dos scanners? | Sim, a pedido do desenvolvedor, em 2026-11-06: "coloque no prompt a refatoracao para nao ficarmos nas armadilhas". O agente entendeu "armadilhas" como os limites de tamanho do Checkstyle. Muda a ordem do item 16 do checklist. A pergunta 519 define o escopo, e a 520 esclarece o que fica de fora |
| 516 | As decisões tomadas ao implementar a personalização ficam? | Sim: shift + clique com corante continua recolhendo o Cubit, e hibernando ele é pintado, mas a tela continua em carregando, sem bipe, conforme a pergunta 57 |
| 515 | Como foi o roteiro manual da cor e do nome? | "Testes feito tudo ok", executado pelo desenvolvedor, sem detalhar cada passo. O passo 13, com outro player, fica para os testes de instância no fim da Fase 3, conforme a resposta 354. O agente não entendeu a resposta como autorização de commits |
| 514 | O contrato e os testes da cor ficam como propostos? | Sim. No save: `Color` na entidade e `color`, opcional, em `cubit:robot_state`, com os nomes dos corantes do jogo, e `white` como padrão, inclusive em saves antigos e valor inválido. Domínio: `RobotColor`. Nome: `CustomName` da entidade e `minecraft:custom_name` do item. JUnit: regras de pintar e estado. GameTests: dono, não dono, gasto, sincronia, recolher, morrer, consertar, save antigo e arquivos presentes. Roteiro manual: ver as 16 cores e os ícones |
| 513 | Os ícones dos itens mudam com a cor da carcaça? | Só o do Cubit. O Damaged Cubit mantém o ícone atual e guarda a cor, que volta ao consertar. A recomendação era mudar os dois. O colar no ícone continua cinza até a Fase 4, conforme a pergunta 414 |
| 512 | Como os nomes são aplicados no Cubit? | Com etiqueta e bigorna. Etiqueta: só do dono, com o robô em qualquer estado, gasta 1, menos no criativo, e faz a reação feliz com bipe se a tela estiver acesa. Bigorna: renomeia o Cubit ou o Damaged Cubit como item, e o nome vale ao colocar. Recolher, morrer e consertar mantêm o nome |
| 511 | Como o nome personalizado aparece no mundo? | Sempre visível acima do Cubit, dentro do alcance normal do jogo. A recomendação era só ao mirar nele |
| 510 | Em quais estados o dono pode pintar o Cubit? | Em qualquer estado: ativo, desligado, sem bateria ou hibernando no Dock, porque pintar não gasta energia. Com a tela apagada, a cor muda sem expressão e sem bipe. Quem não é dono nunca pinta (regra 20) |
| 509 | Como funciona usar um corante no Cubit? | Com clique direito do dono: gasta 1 corante, menos no criativo. Na mesma cor, nada acontece e nada é gasto. Volta ao visual original com o corante branco. Sem novo item nem chave no config |
| 508 | O que a cor do corante pinta no Cubit? | Como a imagem de conceito: carcaça, ombros, braços, hastes das antenas e tronco. A fita e as faixas dos antebraços continuam na cor do tier. Tela, indicador de bateria, painel solar, garras, esteiras e pontas das antenas não mudam |
| 507 | Como aplicar as 16 cores no modelo do Cubit? | Com uma textura do corpo por cor, escolhida pelo renderer, como o colar faz por tier na pergunta 418. O branco é o visual atual, e a referência é `cubit-personalizacao.png` |
| 506 | Há algo a acrescentar sobre a personalização, além da imagem de conceito? | Não: "podemos avançar". O agente entendeu como autorização para seguir à rodada de decisões, não para commits |
| 505 | O jogo está aberto? | Não. Os gates podem rodar |
| 504 | A personalização pode vir antes dos scanners? | Sim, a pedido do desenvolvedor: o agente conferiu que nome e cor da carcaça, item 17, não dependem dos scanners, item 16, nem o contrário. Ele já tem a imagem de conceito, `tmp/conceito/cubit-personalizacao.png`, e quer segui-la. Muda a ordem das perguntas 356 e 365 só nesses dois itens. As decisões da personalização ainda vão para a rodada |
| 503 | Os scanners começam nesta sessão? | Não: o agente deixa o prompt de continuidade, e eles seguem numa sessão nova. A pergunta se há algo a acrescentar sobre os scanners ficou sem resposta |
| 502 | Como foi o teste no jogo depois de tirar o vidro? | "Ótimo, fiz os testes, podemos avançar", executado pelo desenvolvedor, sem detalhar cada passo. O agente entendeu a resposta também como autorização do plano de commits apresentado, inclusive da mudança no `AGENTS.md` |
| 501 | Como ficaram no jogo o painel, o sinal, a fita e as faixas? | O vidro não ficou legal e deve sair. O restante ficou bom, executado pelo desenvolvedor, sem detalhar cada passo: relevo, recarga, pulso, fita e faixas. Muda a pergunta 498 |
| 500 | Quando entra a interface de configuração, em relação ao item 19 e às Fases 4 e 5? | O desenvolvedor vai falar sobre isso depois. Até lá, o checklist continua como está nesse ponto |
| 499 | Onde fica o painel solar no topo? | Centralizado em relação ao topo da cabeça do Cubit |
| 498 | Dá para criar a ideia de uma camada fina de vidro protegendo o painel solar? | Sim, pedido do desenvolvedor: uma película fina e translúcida sobre a célula, com um reflexo discreto desenhado. Só visual. O sinal continua sendo o pulso suave da pergunta 490, confirmado. A pergunta 501 tira o vidro |
| 497 | Como entram as mudanças de modelo do item 13, e onde ficam os testes? | No mesmo PR do painel solar, em commit próprio. JUnit: dia e noite, tiers, soma sem criar FE e sinal. GameTests: céu aberto, coberto, outras dimensões e os testes de consumo sem depender do céu. Roteiro manual: ver a placa, o pulso, a fita e as faixas. As chaves e o save da recomendação saem, pelas perguntas 494 e 495 |
| 496 | Um bloco transparente acima do Cubit impede a recarga solar? | Não: vale a luz do céu que chega ao Cubit, então vidro deixa carregar, e blocos opacos bloqueiam. Detalha a pergunta 53 |
| 495 | O que entra da chave geral do Cubit nesta entrega? | Nada: "não entra agora". Fica para quando a interface de configuração entrar. Muda o item 13 do checklist |
| 494 | Onde ficam os botões do painel solar até a tela própria? | Nenhum entra agora. O desenvolvedor pediu só a elevação do painel, para parecer uma peça colocada, e o sinal luminoso. Toda a interface de configuração fica para depois, "ao final da fase 3 entre a fase 4 e 5", momento ainda a esclarecer. Por isso, a pendência 425 continua aberta nessa parte |
| 493 | Afinal, como fica a fita do tier na cintura? | É uma fita que o robô veste, então não aumenta o tamanho dele: só cobre mais o tronco. O agente definiu 1,25 unidade de altura, em vez de 0,75, com os mesmos 0,25 saindo do tronco, como ponto de partida a avaliar no jogo. Fecha a pergunta 491 e muda a [pergunta 396](fase-3-sentidos.md) nessa peça |
| 492 | Como mostrar o tier nos braços? | Com uma faixa colorida em volta de cada antebraço, em vez das caixas da ideia original. Muda de cor junto com o colar, sem brilhar, e acompanha o braço. O ícone do item continua cinza até a Fase 4, conforme a pergunta 414 |
| 491 | Como deixar o colar do tier mais grosso? | Nenhuma das opções apresentadas, de altura 1,25 ou 1,5 e saliência 0,25 ou 0,5. O desenvolvedor vai explicar o que prefere. Hoje o colar tem 0,75 de altura e sai 0,25 do tronco |
| 490 | Que sinal luminoso mostra que o painel está recebendo luz? | Um pulso suave na célula, que clareia e escurece devagar, só enquanto o painel estiver enchendo a bateria: Cubit ativo, de dia, céu aberto e bateria abaixo do máximo. A recomendação era o reflexo que passa |
| 489 | Como dar relevo ao painel solar? | Com uma placa de 0,5 unidade sobre a tampa do topo, com moldura escura e a célula azul em cima, cerca de 6 x 5 unidades. A hitbox não muda, e as antenas continuam livres. Muda a [pergunta 396](fase-3-sentidos.md) nessa peça |
| 488 | Como devem ser as rodadas de perguntas com opções? | Nunca sem a opção "Nenhuma das opções". É uma regra nova, a 51 do `AGENTS.md`, pedida pelo desenvolvedor |
| 487 | Há algo a acrescentar antes da rodada de decisões do painel solar? | O desenvolvedor disse que não tem nada a acrescentar e que tem ideias sobre o painel, a apresentar antes da rodada |
| 486 | Como ficaram os braços abertos e o balanço ao andar? | "Fechou, acho que era isso mesmo": a abertura de 5° fica, e o balanço de 12° continua como estava. O agente entendeu a mensagem como o fim dos ajustes visuais. Não foram detalhados os braços erguidos e a pose de desligado, nem o botão `Lamp`, a chave depois de recolher, as tochas, a bateria zerada e o Dock. A claridade da tela continua no nível 4 da pergunta 471 |
| 485 | Em que entrega entra a abertura dos braços? | No PR da lanterna, issue #99, num commit próprio, porque os testes no jogo estão sendo feitos juntos |
| 484 | Quanto abrir os braços, que estão quase colados na carcaça? | Cerca de 5°, girando cada braço a partir do ombro, com a garra cerca de 0,6 unidade mais afastada. A hitbox e o tamanho não mudam. É o ponto de partida, a avaliar no jogo. Muda a pergunta 396 nessa pose |
| 483 | Como ficaram a iluminação, as antenas e os olhos nos testes? | Ótimos, executado pelo desenvolvedor. O halo, os olhos, o tamanho das antenas e a luz ficam como estão. Os passos do roteiro não foram detalhados um a um |
| 482 | As antenas podem ser um pouco menores? | Sim. O agente encurtou a haste de 2,5 para 2 unidades, cerca de 12% da altura da antena, e a ponta desce junto, sem mudar a espessura, a hitbox nem as animações. É o ponto de partida, a avaliar no jogo. Muda a [pergunta 396](fase-3-sentidos.md) nessa medida |
| 481 | A intensidade da luz das antenas pode ser um pouco menor? | Sim. O agente entendeu como o brilho do halo, e o reduziu em cerca de um terço, numa constante, sem mudar o tamanho, o pulso, a ponta nem a luz real nos blocos, que continua nos números da pergunta 462. Ajusta a pergunta 480 |
| 480 | Como ficaram os efeitos da lanterna no jogo? | O halo das antenas ficou como o desenvolvedor queria. O brilho dos olhos ficou exagerado e deve ser um pouco mais sutil. Os demais passos do roteiro não foram citados |
| 479 | O brilho dos olhos depende da lanterna? | Sim: o desenvolvedor pediu que ele acompanhe as regras da lanterna das antenas, então só aparece com a lanterna acesa e não tem custo próprio. O agente entendeu que continua valendo em toda expressão e sem pulso, porque a mensagem não tratou disso. Muda a pergunta 478 |
| 478 | Como dar um brilho aos olhos azuis do Cubit? | Em toda expressão: um brilho suave em volta do que acende na tela, com a cor de cada uma, como azul nos olhos normais e âmbar no alerta. Acompanha a piscada e o olhar, só com a tela acesa, sem custo de energia e sem chave nova no config. A luz real no chão não muda de cor, porque o LambDynamicLights só controla a intensidade. Complementa a pergunta 366. A pergunta 479 o liga à lanterna |
| 477 | Que efeito visual acrescentar à lanterna acesa, depois de ver no jogo só a luz em volta do Cubit e as pontas brancas? | Um halo suave em cada ponta, sempre virado para a câmera e sem borda definida, com um pulso leve, só no cliente, em constantes, sem chave nova no config. Muda a pergunta 464, cujo motivo só vale para um disco rígido no modelo |
| 476 | Em que pontos ficam os testes da lanterna? | JUnit: acender e apagar nas bordas de 7 a 10, estados, luz final, consumo e config inválido. GameTests: sala escura, céu aberto com a hora fixada, chave, recolher, morrer, consertar, save antigo e botão de quem não é dono. Os testes de consumo existentes desligam a chave, para não dependerem da luz. Roteiro manual: ver a luz e as pontas |
| 475 | Quando decidir o resto da pendência 425? | Junto do painel solar, no item 13. A 425 fica pendente só na chave geral, no "Hibernar" fora do Dock e nas chaves dos outros recursos |
| 474 | Os nomes e os padrões da lanterna ficam como propostos? | Sim. Em `[general_robot.base]`: `lamp_light_level` (10) e `lamp_consumption_per_tick` (1). Em `[general]`: `lamp_on_below_light` (8), `lamp_off_from_light` (10), `screen_light_level` (4) e `lamp_check_ticks` (20). No save: `LampEnabled` na entidade e `lamp_enabled` em `cubit:robot_state`. No jogo: `Lamp`. Se `lamp_off_from_light` vier menor que `lamp_on_below_light`, o mod usa o maior dos dois para apagar e registra no log. A leitura da luz roda em intervalos, e o estado e a chave valem na hora |
| 473 | O que o botão da lanterna faz com o Cubit hibernando no Dock? | Fica desabilitado, mantendo o valor, que volta a valer quando ele sair da base |
| 472 | Qual luz do ambiente o servidor lê para decidir se está escuro? | O maior valor entre a luz de blocos e a do céu, com o céu escurecendo à noite e com chuva, como o jogo faz para os mobs. A lanterna acende à noite e em tempestade, a céu aberto |
| 471 | Qual é o nível padrão da claridade da tela? | 4, a avaliar no jogo, numa chave do config. A recomendação era 3. Fecha o ponto de partida da pergunta 378 |
| 470 | Em que níveis de luz a lanterna acende e apaga? | Acende abaixo de 8 e apaga a partir de 10, nos dois valores no config. Entre 8 e 9, mantém o estado em que estava |
| 469 | Quanto a lanterna gasta enquanto está acesa? | Base 1, Advanced 1, Elite 2 e Ultimate 2 FE/t, mesmo parado, somados ao andar. É uma nova exceção às perguntas [32](fase-2-versao-minima.md) e [341](fase-4-upgrades-e-autonomia.md). Fecha o custo da lanterna, previsto no item 9 da Fase 4 do checklist |
| 468 | Como fica a chave da lanterna no primeiro uso e ao virar item? | Começa ligada, inclusive em robôs e itens de saves antigos. Recolher, morrer e consertar mantêm a escolha, num campo opcional de `cubit:robot_state`, como na [pergunta 108](fase-2-versao-minima.md). Amplia a [pergunta 343](fase-4-upgrades-e-autonomia.md), porque a chave não é um slot. Responde a pendência 425 só para a lanterna |
| 467 | Como o player liga e desliga a chave da lanterna? | Por um botão no painel lateral do inventário, com o clique de botão de menu do próprio jogo, que confere dono e distância pelo `RobotMenu`, sem pacote próprio. O Cubit Remote soma a chave na Fase 4, e a tela própria a reconstrói no item 19. Responde a pendência 425 só para a lanterna |
| 466 | A lanterna acende com o Cubit hibernando no Dock? | Não: fica apagada e sem consumo, porque ele está imóvel e carregando. Ao sair da base, volta a seguir a chave e o escuro |
| 465 | A claridade fraca da tela fica acesa com o Cubit hibernando no Dock? | Sim, como nos documentos: enquanto a tela brilha, há a claridade fraca, conforme as perguntas 57 e 378. O pedido original dizia apagada. Desligado, com a tela apagada, não há luz |
| 464 | Como as antenas aparecem com a lanterna acesa? | Só as pontas acendem, em branco quente, junto da luz real nos blocos em volta, sem o disco da prancha, que num modelo 3D pareceria um objeto sólido. Prévia em imagem antes do jogo. A pergunta 477 acrescenta um halo suave |
| 463 | De quem é o feixe de luz? | Do Aprendizado de Máquina: Minerador, com a lanterna de cabeça, na Fase 5. Sai dos tiers e da Fase 3, então o item 15 do checklist deixa de existir na fase. Muda a [pergunta 17](fase-1-conceito.md) |
| 462 | Qual é o nível de luz da lanterna por tier? | Base 10, Advanced 12, Elite 14 e Ultimate 15. Nesta entrega, só o Base entra no config, conforme a [pergunta 39](fase-2-versao-minima.md), e os demais ficam nos documentos e no JUnit até a Fase 4 |
| 461 | Como foi o teste no jogo da prova do LambDynamicLights, com a luz fixa? | Como esperado, executado pelo desenvolvedor, sem detalhar cada passo. O passo opcional, de conferir a opção de luz de entidades, não foi citado, então o valor padrão dela continua sem confirmação |
| 460 | Como organizar a prova técnica do LambDynamicLights e as issues da lanterna? | Com uma issue e um PR técnicos, só para separar a `RobotEntity` e adicionar a dependência. A prova roda nessa branch, e a luz provisória não é commitada. A rodada de decisões vem depois da prova e antes da issue da lanterna |
| 459 | Como seguir com o plano da lanterna, depois da conferência contra os documentos e o código? | Incorporar ao plano os dez pontos encontrados e a leitura da licença do LambDynamicLights antes da etapa 1, e só então começar. O desenvolvedor não citou acréscimos ao responder ao alinhamento. Os pontos não fecham as decisões da lanterna, que seguem para a rodada |
| 458 | O painel solar é o próximo item? | Não. A lanterna, item 14, vem antes, e o desenvolvedor tem ideias a apresentar. Muda a ordem das perguntas 356 e 365 só nesses dois itens |
| 455 | Quando o Cubit fica feliz por interação? | Como está no código: ao ser colocado e ao abrir o inventário. Só o `persona.md` estava diferente, ao dizer "de qualquer forma". Renomear, pintar e receber upgrade continuam previstos |
| 456 | O item 11 do checklist fica pronto? | Sim, porque o valor e a chave já existem, e os alertas dos scanners vão usar a mesma chave. Muda a pergunta 428 só no item 11. O item 12 continua aberto |
| 457 | `I need to recharge` e `Items unloaded` também valem para Cubits em área? | Não: vêm só do Cubit que segue. Fecha o que a pergunta 430 deixou para reavaliar. Um Cubit em área não mostra esses textos |
| 454 | Os usos gerais de "trabalho", como "área de trabalho" e "Cubits trabalhando em áreas", mudam? | Não, ficam como estão, porque o sentido é natural |
| 448 | Como ficam os termos da Fase 5? | "Especialidade" virou "trabalho" (Job, no jogo). "Card de especialidade" virou "card de Aprendizado de Máquina" (Machine Learning card). Cada card se chama, por exemplo, "Aprendizado de Máquina: Minerador" (Machine Learning: Miner), com ID `cubit:ml_miner`. A aba passa a ser a aba Trabalho (Job tab). "Robô especializado" virou "Cubit com trabalho". "Generalista" não muda. Só o termo muda, sem alterar o sentido das decisões |
| 449 | O `pt_br.json` citado na troca de termos é criado? | Não. A regra 1 continua valendo, e os nomes em português ficam só nos documentos |
| 450 | Os arquivos e a âncora com o termo antigo são renomeados? | Sim: `funcionalidades/trabalhos.md`, `perguntas/fase-5-trabalhos.md` e a âncora `#trabalhos` de `tiers.md` |
| 451 | O projeto terá outros robôs? | Não. O robô é único, o Cubit, e os itens dão a ele o conhecimento para atuar num trabalho. É a ideia das [perguntas 191 e 192](fase-5-trabalhos.md). Muda as [perguntas 90 e 91](fase-1-conceito.md), que previam outros tipos de robô. `general_robot` e `RobotType` continuam no código, sem mudar nesta entrega |
| 452 | O milestone da Fase 5 é renomeado? | Sim, para `Fase 5: Trabalhos` |
| 453 | O que fazer com as incoerências dos documentos? | Revisar todos os `.md`, corrigir o que estiver desatualizado e seguir a ideia correta do projeto, aplicando os termos novos |
| 447 | O que acontece ao iniciar uma feature? | O agente descreve em poucas linhas o que vamos fazer e pergunta ao desenvolvedor se tem algo mais para acrescentar. Vira regra e passo do fluxo de trabalho |
| 446 | Como foi o roteiro manual dos textos, do intervalo de 25 s e dos braços por 2 s? | Teste finalizado, tudo ok, executado pelo desenvolvedor, sem detalhar cada passo. O `make server` e o texto mais recente substituindo o anterior não foram citados em separado |
| 445 | Quanto tempo os braços ficam erguidos depois de levar dano? | Cerca de 2 s depois do último dano, numa constante, com um dado sincronizado novo, fora do save. A lágrima continua nos 5 s. Muda a pergunta 402 |
| 444 | Qual é o intervalo dos textos? | 25 s, ou 500 ticks. O desenvolvedor pediu ao perguntar pelas frases novas, e o agente aplicou aos três textos, porque a chave é a mesma pela pergunta 442. Muda a pergunta 433 |
| 440 | Qual é o texto quando o Cubit leva dano? | `Cubit: That hurts!`. Amplia a pergunta 49 |
| 441 | Qual é o texto quando o dono leva dano? | `Cubit: Careful!`. Amplia a pergunta 49 |
| 442 | De quanto em quanto os textos de dano podem se repetir? | Com o mesmo intervalo dos outros, `notice_repeat_ticks`, contado por texto, sem chave nova |
| 443 | Quando valem os textos de dano, e em que entrega entram? | Só do Cubit ativo que segue o dono, conforme a pergunta 430. Dor: qualquer dano que dispara a expressão triste, inclusive fogo. Dono: o mesmo gatilho da expressão de preocupado, na mesma dimensão e a até 16 blocos. Entram na issue #93. JUnit nas regras, GameTests no servidor e nas traduções, e roteiro manual |
| 439 | O tamanho e a posição dos textos ficam como estão? | Sim: 75% da fonte do jogo, logo acima das barras, aprovados pelo desenvolvedor no jogo. Os demais passos do teste não foram detalhados |
| 435 | Como começam os textos do Cubit? | Toda frase começa com `Cubit: `, como `Cubit: Critical battery`. Muda a pergunta 49 nesse prefixo. O desenvolvedor também achou que a fonte poderia ser um pouco menor e o texto um pouco mais próximo da hotbar, o que as perguntas 436 a 438 definem |
| 436 | Como deixar os textos menores e mais perto da hotbar? | Com uma camada própria na tela, só do cliente, e o primeiro pacote de rede próprio do mod, só do servidor para o cliente. Tamanho e distância ficam em constantes, porque são só visuais, como na pergunta 398. Detalha a pergunta 48 |
| 437 | Qual é o tamanho inicial da fonte dos textos? | 75% do tamanho da fonte do jogo, a avaliar no jogo |
| 438 | A que distância da hotbar ficam os textos? | Logo acima do que estiver em cima da hotbar, cerca de 3 pixels da interface acima da altura que o jogo informa para corações e armadura, subindo sozinho quando eles aparecem |
| 432 | Os 5% do texto `Critical battery` valem só para ele ou para toda a bateria crítica? | Para tudo: `critical_charge_percent` passa de 10% para 5%, com a expressão de sono, o indicador vermelho e o texto, sem chave nova. Muda as perguntas [11](fase-1-conceito.md), 66 e 416 nesse número |
| 433 | Qual é o valor inicial do intervalo mínimo? | 60 s, ou 1.200 ticks, numa chave nova do config, a ajustar no teste no jogo. A recomendação era 30 s. Complementa a pergunta 149. A pergunta 444 muda para 25 s |
| 434 | Onde ficam os rascunhos e os logs? | Todo `.md` de rascunho de PR fica em `tmp/pr/`, os arquivos de issue, em `tmp/issue/`, e os logs, em `tmp/log/` |
| 428 | Quais dos cinco textos entram agora? | Só `Critical battery`, que já tem gatilho. `Enemy nearby` e `Ore found` entram com os scanners, e `I need to recharge` e `Items unloaded`, na Fase 4. Os itens 11 e 12 ficam abertos até os scanners, conforme a pergunta 356. A pergunta 456 fecha o item 11 |
| 429 | Quando o texto `Critical battery` aparece? | Ao chegar em 5% ou menos, repetindo a cada intervalo mínimo enquanto continuar crítico. Só com o robô ativo: desligado, hibernando ou recarregando acima de 5%, não avisa. A recomendação citava 10%, e o desenvolvedor escreveu 5%. A pergunta 432 leva os 5% a toda a bateria crítica |
| 430 | Com vários Cubits, de quem vêm as mensagens? | Somente do Cubit ativo que segue o player, que é um só, então o intervalo não precisa ser por dono. Complementa a pergunta 48. O valor inicial está na pergunta 433. A pergunta 457 confirma isso para `Items unloaded` e `I need to recharge` |
| 431 | Em que pontos ficam os testes do intervalo e do texto? | JUnit: quando o aviso vale, a entrada, a repetição e o estado. GameTests: tradução e chave do config presentes e aviso calculado no servidor. Roteiro manual: ver o texto na barra, só para o dono, e escolher o valor do intervalo |
| 426 | O agente corrige o status do `README.md`, que não cita o que já entrou na Fase 3? | Sim, no próximo PR, conforme a [pergunta 152](fase-2-versao-minima.md) |
| 427 | Os itens 11 e 12, do intervalo mínimo e dos textos, podem começar depois do merge do PR #92? | Sim |
| 424 | Quais recursos do Cubit têm chaveamento? | Lista do desenvolvedor, com ligado e desligado: 0. chave geral do Cubit, com ligar, desligar e hibernar; 1. painel solar; 2. lanterna; 3. scanners; 4. ímã; 5. pegar itens dropados; 6. descarga em baú; 7. retorno automático; 8. mostrar o local de morte do player. Cada chave entra com o recurso dela, na fase dele. Detalhes na pendência 425 |
| 420 | Qual parte do corpo aumenta, e quanto? | O tronco central, a coluna clara entre as esteiras e a carcaça, onde fica o colar. Para conferir no jogo, começa com +0,5 unidade, de 1,5 para 2, e o desenvolvedor considera +1. A carcaça, a tela e as texturas não mudam, e a hitbox continua a mesma. Muda a pergunta 396 só nessa medida |
| 418 | Como trocar a cor do colar no modelo? | Com uma camada transparente por tier, com as duas tonalidades do conceito, escolhida pela chave do tier. As cores ficam nos arquivos, sem número no código |
| 419 | Em que pontos ficam os testes do colar e do indicador? | JUnit: qual desenho do indicador vale em cada carga, com as bordas de 0 FE, carga mínima, crítica e cheia, e quando brilha. GameTests: tier e indicador sincronizados mudando no servidor e todos os arquivos presentes. Roteiro manual: ver as quatro cores por comando, cada nível, o vermelho, o brilho no escuro e o estado desligado |
| 414 | Até onde vai a cor do colar na Fase 3, já que só o tier Base existe até a Fase 4? | O robô no mundo já usa as quatro cores conforme o tier, testáveis por comando. O ícone do item e o do `Damaged Cubit` ficam cinza e ganham cor junto dos módulos, na Fase 4 |
| 415 | Como desenhar o indicador de bateria nas costas? | Numa camada de alta resolução, 8× a do corpo, como a tela do rosto, com cantos arredondados e fiel ao conceito. São 4 níveis, de 25% em 25%, além do vazio |
| 416 | Qual é a cor do preenchimento do indicador? | Verde, como no conceito, e vermelho com a bateria crítica, reutilizando `critical_charge_percent`, sem chave nova |
| 417 | Como o indicador se comporta com o Cubit desligado ou hibernando? | Mostra a carga real em qualquer estado. Brilha no escuro enquanto ligado ou hibernando, e fica sem brilho com a tela apagada |
| 413 | Como foi o roteiro manual dos braços, dos olhos que acompanham o dono e da inclinação? | Concluído como esperado, executado pelo desenvolvedor, sem detalhar cada passo. O limite de 1 bloco ficou como está |
| 410 | Dá para o Cubit inclinar de leve para cima ou para baixo quando o dono está em outro nível, e de quanto? | Sim, cerca de 8°, com transição suave, numa constante. Como o modelo não tem cabeça separada, o osso `body` inclina inteiro. Os olhos mantêm o comportamento das perguntas 404 e 406 |
| 411 | Quando acontece a inclinação do corpo? | Só com o dono 1 bloco ou mais acima ou abaixo, com o robô ligado, na mesma dimensão e até 8 blocos. Um pulo move só os olhos. Usa um segundo dado sincronizado, fora do save |
| 412 | Em que entrega e com quais testes entra a inclinação? | Neste PR, na issue #89. JUnit: direção e ângulo por nível. GameTest: dado sincronizado mudando no servidor. Roteiro manual: ver a inclinação, junto dos braços e dos olhos, num só teste |
| 406 | A partir de quantos blocos de diferença de altura os olhos olham para o dono? | 1 bloco, entre os pés do dono e os do robô, enquanto a diferença durar, numa constante. A recomendação era 2, e o desenvolvedor escolheu 1. Detalha a pergunta 404 |
| 407 | Se o robô se move na vertical e o dono está em outra altura, qual movimento vence? | O do robô. Sem movimento próprio, os olhos seguem a altura do dono |
| 408 | Até que distância os olhos acompanham a altura do dono? | Até 8 blocos, com o robô ligado e na mesma dimensão, o mesmo limite de virar o corpo |
| 409 | Em que pontos ficam os testes dos braços e dos olhos? | JUnit: quando os braços sobem, a direção dos olhos por pulo e por nível, o tempo e a prioridade. GameTests: animação dos braços no arquivo e direção sincronizada mudando no servidor. Roteiro manual: ver os braços e os olhos |
| 402 | Por quanto tempo o Cubit fica com os braços erguidos ao levar dano? | Enquanto estiver triste, ou seja, enquanto durar a expressão triste ou de olhos em X, cerca de 5 s depois do último dano. A pergunta 445 muda para cerca de 2 s |
| 403 | E se ele estiver andando com os braços erguidos? | Mantém o balanço do corpo e das antenas, e só os braços ficam erguidos, com um segundo controlador da GeckoLib |
| 404 | Como os olhos da expressão normal acompanham o player? | Uma mudança rápida na altura do dono, como um pulo, move os olhos para cima ou para baixo por cerca de 0,5 s, e eles voltam ao centro. Usa as texturas atuais e mantém a reação à subida do robô. O desenvolvedor acrescentou que também seria legal quando o player está em blocos ou níveis acima ou abaixo do Cubit. Complementa a pergunta 375 |
| 405 | Em que entrega entram os braços erguidos e os olhos que acompanham o player? | Neste PR, na issue #89, com um novo teste no jogo antes dos commits |
| 401 | Como foi o roteiro manual das animações? | Testes aprovados, tudo ok, executado pelo desenvolvedor, sem detalhar cada passo |
| 397 | Para quem o Cubit vira o corpo na animação de olhar? | Só para o dono, com o robô ligado e o dono por perto, inclusive parado |
| 398 | Onde ficam os números visuais das animações? | Tempos e poses no arquivo de animação, e o resto em constantes no código, como na pergunta 374, sem chave nova no config |
| 399 | Em que pontos ficam os testes das animações? | JUnit: qual animação vale em cada estado e situação, e qual quadro da esteira vale em cada distância. GameTests: arquivos de animação e quadros presentes, e servidor abrindo. Roteiro manual: ver cada animação e as esteiras |
| 400 | O agente corrige os documentos desatualizados no PR da issue #89? | Sim, num commit separado: o status e o roadmap no `README.md`, a etapa atual em `decisoes.md` e o bipe padrão em `visual.md`, conforme a [pergunta 152](fase-2-versao-minima.md) |
| 394 | Quais animações do Cubit entram no item 9? | Andar, olhar para o player, parado, e desligado e hibernando. Como o modelo não tem cabeça separada, olhar gira o osso `body` sobre o pescoço, com as esteiras paradas |
| 395 | Como animar as esteiras? | Com textura por quadros, escolhidos pela distância andada, sem mudar a geometria |
| 396 | O que "modelo final" muda no Cubit? | Mantém a geometria e a textura de 128×128 do corpo, e significa só os ossos que as animações precisarem. Detalha a [pergunta 60](fase-2-versao-minima.md) e mantém a 162. A pergunta 420 aumenta o tronco central, a 482 encurta as antenas, e a 484 abre os braços |
| 393 | Como foi o teste da velocidade nova? | "Ok", sem detalhe. A resposta veio antes de o config local ser recriado, então o valor novo pode não ter sido sentido no jogo |
| 392 | Quanto o Cubit fica mais ágil? | O atributo da velocidade 1,0× passa de 0,30 para 0,33, cerca de 10%, sem mudar os multiplicadores nem o consumo por tick. Muda a [pergunta 113](fase-2-versao-minima.md) |
| 391 | Como foi o roteiro manual das expressões, da barra de vida e do som de dano? | Fechou o teste e ficou ótimo, executado pelo desenvolvedor, sem detalhar cada passo. O crash anterior, atribuído à compilação simultânea, não foi citado de novo |
| 389 | Quanto duram os olhos em X? | Cerca de 3 s depois do último dano, com a vida em 10% ou menos e numa chave do config, em vez de ficarem enquanto a vida estiver baixa. Muda a pergunta 380 |
| 390 | O que aparece entre o fim dos olhos em X e o fim da lágrima? | A lágrima até os 5 s, e depois a expressão normal. Cada dano novo reinicia tudo |
| 386 | Em que entrega entra a expressão de preocupado? | Junto das outras, na issue #85 |
| 387 | Qual Cubit fica preocupado quando o dono leva dano? | Só o que segue, ligado, na mesma dimensão e a até 16 blocos, numa chave do config |
| 388 | Quanto dura a expressão de preocupado, e onde fica na ordem? | Cerca de 3 s, numa chave do config. Ordem: tela apagada, carregando, quase morto, triste, preocupado, alerta, surpreso, sono, feliz e normal. Muda a pergunta 379. Sem som e sem bipe. Como não há imagem de conceito, o agente a desenhou no mesmo estilo |
| 384 | Em que entrega entra a barra de vida no painel do inventário? | Junto das expressões, na issue #85 |
| 385 | Como fica a barra de vida? | Vermelha, ao lado da verde da bateria, com o mesmo tamanho e formato, mostrando ao menos um risco enquanto houver vida. O tooltip fica para as telas próprias. Complementa a pergunta 264 |
| 383 | Quanto dura a expressão triste? | Cerca de 5 s depois do último dano. Muda a pergunta 379, que definia cerca de 2 s |
| 379 | Quando aparece a expressão triste, e qual é a ordem agora? | Por cerca de 2 s depois de qualquer dano, com cada golpe reiniciando o tempo, numa chave do config. Ordem: tela apagada, carregando, quase morto, triste, alerta, surpreso, sono, feliz e normal. Muda a pergunta 368 |
| 380 | Quando aparecem os olhos em X, de quase morto? | Com a vida em 10% ou menos, numa chave do config |
| 381 | As expressões triste e quase morto tocam algum som? | Ao levar dano, toca `short_circuit.ogg`, que substitui o som padrão de dano do jogo, sem bipe. Em chamas, o loop de curto elétrico já está tocando, então os golpes do fogo não repetem o som. Quase morto não tem som próprio |
| 382 | Em que entrega entram essas duas expressões? | Junto das outras, na issue #85. Como não há imagem de conceito, o agente as desenhou no mesmo estilo: `expressao-triste.png` e `expressao-quase-morto.png` |
| 378 | Como fazer a pequena iluminação em volta do Cubit, que ilustra a claridade da tela? | Com luz real, no item da lanterna, pelo LambDynamicLights: uma luz fraca, de nível 3 a 4 como ponto de partida, sem gastar energia, que apaga quando a tela apaga. A pergunta 471 define o nível 4 |
| 372 | Como é a piscada na expressão normal? | Fica 5 s de olhos abertos e pisca em cerca de 0,25 s, com 3 quadros: meio fechado, fechado e meio fechado. Sem bipe, porque não troca a expressão |
| 373 | Em que entrega a piscada entra? | Junto das expressões, na issue #85 |
| 374 | Onde fica o tempo da piscada? | Em constante no código, porque é só visual e não é balanceamento, como na pergunta 335 |
| 375 | Em que expressões os olhos acompanham a subida e a descida? | Só na normal, olhando para cima ao subir e para baixo ao descer, um ou mais blocos. A pergunta 404 complementa com a altura do dono |
| 376 | Quanto os olhos se deslocam? | Cerca de 1/6 da tela, ou 14 de 88 pixels |
| 377 | Em que entrega o olhar entra? | Junto da piscada, na issue #85 |
| 371 | Como serão as telas do Cubit e do Induction Dock? | Reconstruídas, com janelas personalizadas e textura que lembre o metal de um robô, sem aparecer o padrão do Minecraft. Entra no item da tela própria. Amplia as perguntas 186, 264 e 308 |
| 367 | Qual é a resolução da camada da tela? | 8×, com 144×88 pixels na tela, porque 18×11 não comporta o conceito. Detalha a pergunta 366 |
| 368 | Qual expressão vence, e quanto dura a feliz? | A ordem é tela apagada, carregando, alerta, surpreso, sono, feliz e normal. A feliz dura cerca de 3 s, numa chave do config |
| 369 | Quando toca o bipe padrão? | Ao trocar para normal, feliz ou sono, exceto ao ligar, ao ser colocado e ao carregar o mundo. Detalha a pergunta 148 |
| 370 | Em que pontos ficam os testes das expressões? | JUnit: prioridade, duração e bipe. GameTests: expressão sincronizada e arquivos presentes. Roteiro manual: ver cada expressão, o brilho no escuro e ouvir o bipe |
| 366 | Como desenhar as expressões no modelo? | Numa camada só para a tela, com um arquivo por expressão, os olhos brilhando no escuro e o corpo sem mudar, o mais fiel possível aos modelos do conceito plano |
| 365 | Em que ordem segue a Fase 3, depois dos sons? | Expressões; animações e modelo final; colar e bateria; intervalo mínimo junto dos textos; e os demais, na ordem do checklist. Muda a pergunta 356, que criava a chave do intervalo antes de haver quem a lesse |
| 364 | Como foi o roteiro manual dos sons? | Como esperado, executado pelo desenvolvedor, sem detalhar cada passo |
| 361 | Qual versão do som de colocar no mundo fica? | A de `cubit_quando_colocado_no_mundo.wav`, que substitui a anterior em `placed.ogg`. Muda o arquivo da pergunta 320 |
| 363 | O áudio `cubit_desligando_por_falta_de_energia.wav` entra no projeto? | Não, foi removido. Desligar por falta de energia continua com `power_off.ogg`, como na pergunta 358 |
| 357 | Quem ouve os sons, e onde se regula o volume? | Todos os players por perto, com o volume caindo até cerca de 16 blocos, no controle "Criaturas amigáveis" do jogo. Volume e alcance ficam no `sounds.json`, onde um pacote de recursos pode mudá-los, sem chave no config |
| 358 | Quando tocam os sons de ligar e desligar? | Ligar: de inativo para ativo, pelo clique, ao religar sozinho ou ao sair da base ligado. Desligar: de ativo para inativo, por bateria zerada, troca de robô ou `/cubit reset`. Entrar na base e ser colocado não tocam esses sons |
| 359 | Como funcionam os sons contínuos? | Em loop, no cliente. Esteira: enquanto anda, com volume e altura acompanhando a velocidade. Curto elétrico: enquanto pega fogo, ativo ou não, junto da esteira. Recarga: enquanto a bateria sobe de fato, na base ou pelo slot, e para com a bateria cheia ou com o Dock sem energia |
| 360 | Em que pontos ficam os testes dos sons? | JUnit: qual som cada mudança de estado toca e quando cada som contínuo vale. GameTests: sons registrados, `sounds.json` carregando e estado sincronizado mudando no servidor. Roteiro manual: ouvir cada som, a distância, o controle de volume e o servidor dedicado abrindo |
| 356 | Em que ordem segue a Fase 3? | A do `checklist.md`: sons; intervalo mínimo; expressões; animações e modelo final; colar e bateria; textos; painel solar; lanterna; feixe; scanners; nome e cor; Jade; e tela própria. Sons, intervalo, expressões, colar, textos e feixe entram em partes: a parte possível na vez de cada um, e os scanners ligam o resto. Um item só é marcado quando estiver completo na Fase 3. A pergunta 365 muda a ordem |
| 334 | O que acontece ao colocar um Cubit no mundo? | Toca só `placed.ogg`, em qualquer estado, e `power_on.ogg` fica para a passagem de desligado a ligado. A expressão é feliz, se ele nascer ativo, e tela apagada, se nascer desligado. Detalha as perguntas 158 e 321 |
| 331 | Quando o LambDynamicLights entra no build? | Com a lanterna, na Fase 3. A [pergunta 337](fase-2-versao-minima.md) muda a segunda parte: a `0.1.0` só sai com a Fase 3, então já o exige. Detalha a [pergunta 242](fase-5-trabalhos.md) |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-07 13:57 | Registra a resposta 532. |
| Marcelo Fabiano | 2026-11-07 13:46 | Registra a resposta 531. |
| Marcelo Fabiano | 2026-11-07 13:38 | Registra a resposta 530. |
| Marcelo Fabiano | 2026-11-07 13:35 | Registra as respostas 527 a 529. |
| Marcelo Fabiano | 2026-11-07 13:34 | Registra as respostas 523 a 526. |
| Marcelo Fabiano | 2026-11-07 13:32 | Registra a resposta 522. |
| Marcelo Fabiano | 2026-11-07 13:21 | Registra as respostas 517 a 521. |
| Marcelo Fabiano | 2026-11-06 16:08 | Registra a resposta 516. |
| Marcelo Fabiano | 2026-11-06 16:04 | Registra a resposta 515. |
| Marcelo Fabiano | 2026-11-06 15:48 | Registra as respostas 511 a 514. |
| Marcelo Fabiano | 2026-11-06 15:46 | Registra as respostas 507 a 510. |
| Marcelo Fabiano | 2026-11-06 15:44 | Registra as respostas 505 e 506. |
| Marcelo Fabiano | 2026-11-06 15:40 | Registra a resposta 504. |
| Marcelo Fabiano | 2026-11-06 15:31 | Registra a resposta 503. |
| Marcelo Fabiano | 2026-11-06 15:25 | Registra a resposta 502. |
| Marcelo Fabiano | 2026-11-06 15:16 | Registra a resposta 501 e aponta a 498 para ela. |
| Marcelo Fabiano | 2026-11-06 15:00 | Registra as respostas 498 a 500. |
| Marcelo Fabiano | 2026-11-06 14:54 | Registra as respostas 493 a 497. |
| Marcelo Fabiano | 2026-11-06 14:49 | Registra as respostas 489 a 492. |
| Marcelo Fabiano | 2026-11-06 14:47 | Registra a resposta 488. |
| Marcelo Fabiano | 2026-11-06 14:38 | Registra a resposta 487. |
| Marcelo Fabiano | 2026-11-06 14:20 | Registra a resposta 486. |
| Marcelo Fabiano | 2026-11-06 14:15 | Registra as respostas 483 a 485 e aponta a 396 para a 484. |
| Marcelo Fabiano | 2026-11-06 14:05 | Registra a resposta 482 e aponta a 396 para ela. |
| Marcelo Fabiano | 2026-11-06 14:05 | Registra a resposta 481. |
| Marcelo Fabiano | 2026-11-06 14:03 | Registra a resposta 480. |
| Marcelo Fabiano | 2026-11-06 13:57 | Registra as respostas 477 a 479 e aponta a 464 para a 477. |
| Marcelo Fabiano | 2026-11-06 13:34 | Aponta a 378 para a 471 e a pendência 425 para as 467, 468 e 475. |
| Marcelo Fabiano | 2026-11-06 13:26 | Registra as respostas 462 a 476. |
| Marcelo Fabiano | 2026-11-06 13:10 | Registra a resposta 461. |
| Marcelo Fabiano | 2026-11-06 12:07 | Registra as respostas 459 e 460. |
| Marcelo Fabiano | 2026-11-06 11:24 | Registra a resposta 458. |
| Marcelo Fabiano | 2026-11-06 11:10 | Registra as respostas 448 a 457 e aponta a 428 e a 430 para elas. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-06 10:56 | Registra a resposta 447. |
| Marcelo Fabiano | 2026-11-06 10:49 | Registra as respostas 444, 445 e 446 e aponta a 433 e a 402 para elas. |
| Marcelo Fabiano | 2026-11-06 10:15 | Registra as respostas 439 a 443 e aponta a 49 para as 440 e 441. |
| Marcelo Fabiano | 2026-11-06 10:02 | Registra as respostas 435 a 438 e aponta a 48 e a 49 para elas. |
| Marcelo Fabiano | 2026-11-06 09:48 | Registra as respostas 432, 433 e 434 e aponta a 66 para a 432. |
| Marcelo Fabiano | 2026-11-06 09:45 | Registra as respostas 428 a 431. |
| Marcelo Fabiano | 2026-11-06 09:40 | Registra as respostas 426 e 427. |
| Marcelo Fabiano | 2026-11-06 09:36 | Registra a resposta 424 e a pendência 425. |
| Marcelo Fabiano | 2026-11-06 09:23 | Registra as respostas 420 e 421 e aponta a 396 para a 420. |
| Marcelo Fabiano | 2026-11-06 08:58 | Registra as respostas 414 a 419. |
| Marcelo Fabiano | 2026-11-06 01:12 | Registra as respostas 401 a 413. |
| Marcelo Fabiano | 2026-11-06 00:58 | Registra as respostas 394 a 400. |
| Marcelo Fabiano | 2026-11-06 00:44 | Registra a resposta 393. |
| Marcelo Fabiano | 2026-11-06 00:40 | Registra a resposta 392. |
| Marcelo Fabiano | 2026-11-06 00:34 | Registra a resposta 391. |
| Marcelo Fabiano | 2026-11-06 00:28 | Registra as respostas 389 e 390. |
| Marcelo Fabiano | 2026-11-06 00:22 | Registra as respostas 386, 387 e 388. |
| Marcelo Fabiano | 2026-11-06 00:20 | Registra as respostas 384 e 385. |
| Marcelo Fabiano | 2026-11-06 00:17 | Registra a resposta 383. |
| Marcelo Fabiano | 2026-11-06 00:12 | Corrige a resposta 381 com o som indicado. |
| Marcelo Fabiano | 2026-11-06 00:10 | Registra as respostas 379 a 382. |
| Marcelo Fabiano | 2026-11-06 00:00 | Registra a resposta 378. |
| Marcelo Fabiano | 2026-11-05 23:56 | Registra as respostas 372 a 377. |
| Marcelo Fabiano | 2026-11-05 23:49 | Registra a resposta 371. |
| Marcelo Fabiano | 2026-11-05 23:33 | Registra as respostas 367, 368, 369 e 370. |
| Marcelo Fabiano | 2026-11-05 23:31 | Registra a resposta 366. |
| Marcelo Fabiano | 2026-11-05 23:30 | Registra a resposta 365 e aponta a 356 para ela. |
| Marcelo Fabiano | 2026-11-05 23:20 | Registra a resposta 364. |
| Marcelo Fabiano | 2026-11-05 23:16 | Registra as respostas 361 e 363. |
| Marcelo Fabiano | 2026-11-05 22:39 | Registra as respostas 357, 358, 359 e 360. |
| Marcelo Fabiano | 2026-11-05 22:37 | Registra a resposta 356. |
| Marcelo Fabiano | 2026-11-05 22:21 | Aponta a resposta 331 para a 337. |
| Marcelo Fabiano | 2026-11-05 21:30 | Registra a resposta 334. |
| Marcelo Fabiano | 2026-11-05 21:15 | Registra a resposta 331. |
| Marcelo Fabiano | 2026-11-05 20:36 | Registra as respostas 321 e 322 e aponta a 320 para elas. |
| Marcelo Fabiano | 2026-11-05 20:26 | Registra a resposta 320 e aponta a 135 para ela. |
| Marcelo Fabiano | 2026-11-05 17:27 | Registra a resposta 316 e aponta a 135 para ela. |
| Marcelo Fabiano | 2026-11-05 13:30 | Registra a resposta 264. |
| Marcelo Fabiano | 2026-11-05 00:17 | Registra a resposta 158. |
| Marcelo Fabiano | 2026-11-05 00:16 | Registra a resposta 157. |
| Marcelo Fabiano | 2026-11-05 00:12 | Aponta a resposta 142 para a 140 e a 141. |
| Marcelo Fabiano | 2026-11-05 00:05 | Registra a resposta 149. |
| Marcelo Fabiano | 2026-11-05 00:02 | Registra a resposta 148 e aponta a 136 para ela. |
| Marcelo Fabiano | 2026-11-04 23:20 | Registra a resposta 142. |
| Marcelo Fabiano | 2026-11-04 23:16 | Registra a resposta 141. |
| Marcelo Fabiano | 2026-11-04 23:11 | Registra a resposta 140. |
| Marcelo Fabiano | 2026-11-04 22:56 | Registra as respostas 136 e 137. |
| Marcelo Fabiano | 2026-11-04 22:12 | Registra a resposta 135. |
| Marcelo Fabiano | 2026-11-04 17:28 | Registra a resposta 92. |
| Marcelo Fabiano | 2026-11-04 17:26 | Adiciona a pergunta 92. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Registra as respostas 66 e 67. |
| Marcelo Fabiano | 2026-11-04 16:57 | Cria o arquivo com as perguntas e respostas da fase. |
