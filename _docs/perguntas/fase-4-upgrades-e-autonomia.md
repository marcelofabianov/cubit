# Fase 4: Upgrades e autonomia

## Pendentes

| # | Pergunta | Recomendação |
|---|---|---|
| 425 | Onde o player aciona as chaves que faltam da pergunta 424, qual é o estado inicial delas, e o que "Hibernar" da chave geral faz fora do Dock? | Já respondido: a lanterna (467 e 468), os scanners (549) e as falas (582), na aba Settings da tela do Cubit (683), e a interface de configuração entra na Fase 4 (717). Falta: a chave geral, com ligar, desligar e hibernar, e as chaves dos recursos das Fases 4 e 5, como o painel solar, o ímã, a coleta, a descarga, o retorno e o local de morte. Recomendação: na aba Settings, e também no Cubit Remote, todas começando ligadas e guardadas no item, como na pergunta 343. Hibernar fora do Dock segue a pergunta 198, prevista para a Fase 5, então precisa de decisão antes de antecipar. Veio da Fase 3 pela pergunta 721 |
| 423 | Qual é o modo padrão do filtro de coleta, o que vale com a lista vazia, e o limite de itens de `tiers.md` vale para os dois modos? | Lista negra por padrão, em que a lista vazia pega qualquer item. Na lista branca, a lista vazia não pega nada. O mesmo limite vale para os dois modos. Vem da pergunta 422 |
| 577 | Entra um modo de defesa "só o alvo", em que o Cubit ignora os outros inimigos e ataca só o último mob que o dono atacou, trocando de alvo a cada novo ataque, ou o mob em que o dono mira e clica à distância? | A ideia entra, conforme a pergunta 578, e os detalhes ficam para refinar na Fase 4. Recomendação: um quarto modo, a partir do tier Advanced, com a mira pelo Cubit Remote, porque um clique de mão vazia no ar não chega ao servidor. A regra de nunca atacar mobs pacíficos, players e o dono continua valendo |

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 840 | Como foi o teste manual da rolagem do inventário? | "testes feitos, tudo ok", executados pelo desenvolvedor no `make client`, na branch `feature/148-shock-and-defense-modes`, com um Cubit Ultimate, sem detalhar cada passo. Cobre o passo 9 da seção "Upgrade de tier" do roteiro manual: a tela cabe inteira, a roda, o clique e o arrasto passam pelas 6 linhas, e o Shift + clique guarda numa linha de baixo |
| 839 | Onde entra a correção da tela que corta nos tiers altos? | Dentro do PR #149, como mais commits na branch do choque, ainda aberta. A recomendação era uma issue e um PR próprios |
| 838 | Como mostrar 36, 45 e 54 slots sem a tela do Cubit passar da altura da tela, se no Ultimate ela chega a 279 px e corta? | Com rolagem: a janela fica sempre do tamanho da do Base, com 3 linhas visíveis e uma barra de rolagem ao lado do inventário do Cubit quando há mais de 27 slots, pela roda do mouse ou arrastando. O servidor acompanha a rolagem, para os itens nunca trocarem de lugar errado |
| 837 | Como foi o teste manual do choque e dos modos de defesa? | "testes feitos, tudo ok", executados pelo desenvolvedor no `make client`, na branch `feature/148-shock-and-defense-modes`, com o Jade junto, sem detalhar cada passo. Cobre a seção "Choque e modos de defesa" do roteiro manual, com a aba Defense. O teste com o jar numa instância só com o mod continua pendente para antes da `main` (regra 35) |
| 836 | A estrutura de pacotes do `AGENTS.md` pode citar o que entrou na Fase 4? | Sim, autorizado pelo desenvolvedor: `DefenseMode`, `TierUpgrade`, `ShockSpec`, as regras do choque, `CubitDamageTypes`, `RobotUpgrade`, `RobotDefense`, `RobotShock`, `ModuleItem`, `TierLook`, `DefenseControl`, `DefenseOpening` e as três abas da tela, com a Power Bank na Fase 4.1 |
| 835 | Como ficam os textos da aba Defense e do choque que a 833 não cobria? | Como propostos: a lista se chama `Modes` / `Modos`; a seção `Shock` / `Choque` tem `Damage` / `Dano`, `Range` / `Alcance` com `5 blocks` / `5 blocos`, `Interval` / `Intervalo` com `Every 2 s` / `A cada 2 s`, `Cost` / `Custo` com `2000 FE per shock` / `2000 FE por choque` e `Targets` / `Alvos` com `The nearest` / `O mais próximo` ou `All in range` / `Todos no alcance`. As legendas de som são `Cubit shocks` / `Cubit dá um choque` e `Cubit turns aggressive` / `Cubit fica agressivo`. Detalha a 833 |
| 834 | Quais chaves do config o choque e os modos de defesa ganham? | Em cada `[general_robot.{tier}]`: `defense_modes`, uma lista com `peaceful`, `retaliate` e `aggressive`; `shock_damage`; `shock_range_blocks`; `shock_interval_ticks`; `shock_cost`; e `shock_hits_all_in_range`, que só é `true` no Ultimate. O Base vem só com `peaceful` e o choque zerado. Em `[general]`: `retaliate_memory_ticks`, com 600, os 30 s da 832 |
| 833 | Quais textos a aba Defense usa? | Peaceful / Pacífico: `Never attacks` / `Nunca ataca`. Retaliate / Contra-atacar: `Shocks whoever hurts you or the Cubit` / `Dá choque em quem ferir você ou o Cubit`. Aggressive / Agressivo: `Shocks hostile mobs in range` / `Dá choque em mobs hostis no alcance`. Bloqueado: `Unlocks at Advanced` / `Libera no Avançado`. Morte: `X was zapped by Cubit` / `X levou um choque de Cubit`. Os nomes dos modos seguem o glossário |
| 832 | Por quanto tempo o Contra-atacar lembra de quem atacou o Cubit ou o dono? | O último mob que feriu o Cubit ou o dono vira alvo por cerca de 30 s, no config, ou até morrer ou mudar de dimensão. Um novo ataque renova o tempo, e ele só leva o choque quando estiver no alcance |
| 831 | Quando um choque mata um mob, quem fica com o abate e o que diz a mensagem de morte? | Como num lobo domesticado: o abate conta como do dono, e o mob solta XP e os itens que só caem por player. Um tipo de dano próprio, `cubit:shock`, dá a mensagem `X was zapped by Cubit` |
| 830 | Qual é o modo de defesa inicial, e o que o item guarda? | Cubit novo e saves antigos começam em Pacífico. O modo fica num campo opcional de `cubit:robot_state`, e recolher, morrer, consertar e o upgrade mantêm a escolha, como a chave da lanterna |
| 829 | O que a aba de defesa mostra? | Um cabeçalho como o da Settings, com `Defense`, o tier e a bateria. Uma lista com Peaceful, Retaliate e Aggressive, cada um com uma descrição curta e o atual marcado, e os bloqueados apagados com `Unlocks at Advanced`. Embaixo, os números do choque do tier: dano, alcance, intervalo e custo |
| 828 | Qual ícone a aba de defesa usa? | Um escudo, e a aba se chama Defense. A recomendação era um raio. Detalha a 824 |
| 827 | Como o choque aparece no mundo, além do som `shock_attack.ogg`? | Com faíscas elétricas do próprio jogo numa linha do Cubit até cada alvo, por um instante, vistas por todos por perto |
| 826 | O que significa o choque "em área" do Ultimate? | Um choque atinge de uma vez todos os alvos válidos do modo dentro dos 8 blocos, com o custo de um choque só. Os outros tiers atingem um alvo, o mais próximo |
| 825 | Quando e como o Cubit ataca? | Só ativo, seguindo ou parado, nunca desligado nem hibernando. Ele continua seguindo, sem perseguir, e dá o choque em quem estiver no alcance do tier, com linha de visão, no intervalo do tier. Sem carga para o custo, não dá o choque |
| 824 | Como o dono escolhe o modo de defesa na tela do Cubit? | "novo icone de aba e tela": uma terceira aba, com ícone e tela próprios, em vez de uma seção na aba Settings. A recomendação era uma seção com três botões na Settings. Os detalhes da aba seguem na rodada |
| 823 | O alinhamento da entrega do choque com os modos de defesa tem algo a acrescentar? | Nada a acrescentar |
| 822 | Como dividir os itens 5 e 6 da Fase 4, se só com o Pacífico o Cubit nunca dispara o choque? | Juntar os dois numa entrega: o choque, o modo de defesa guardado no item e os três modos, Pacífico no Base, Contra-atacar a partir do Advanced e Agressivo a partir do Elite, escolhidos na aba Settings. Os itens 5 e 6 do `checklist.md` são marcados juntos. Detalha a 796 |
| 821 | O "Estado atual" do `AGENTS.md` pode ser atualizado, com a Fase 4 começada e as confirmações da `0.1.0` feitas? | Sim, autorizado pelo desenvolvedor: a etapa atual cita o item 4 nos PRs #145 e #147 e a resposta 820, e os próximos são o item 5, o choque com o modo Pacífico, os itens 6 a 8 e o envio ao CurseForge, ainda pendente |
| 820 | Como foram os testes manuais na instância só com o mod? | "testes feitos, tudo ok", executados pelo desenvolvedor no Prism, na instância `Cubit - Teste develop (d8afab5)`, com o jar da `develop` em `d8afab5`, o GeckoLib e o LambDynamicLights, e o Jade depois, sem detalhar cada passo. Cobre a seção "Upgrade de tier", as receitas, inclusive as dos Modules, e as confirmações que saíram da `0.1.0` pela pergunta [776](fase-3-sentidos.md): as receitas no livro, o Dock em L espelhado, o `/cubit reset` com chunk descarregado e a luz de entidades do LambDynamicLights no padrão, cujo valor não foi anotado |
| 819 | O alinhamento do PR das receitas tem algo a acrescentar? As três receitas em datagen, aparecendo no livro ao ter um Cubit, com uma GameTest de cada uma e o roteiro manual atualizado | Nada a acrescentar. O item 4 do `checklist.md` é marcado no PR seguinte ao merge, conforme a regra do próprio checklist |
| 818 | Como os itens dos Modules ficam na grade 3×3? | O material principal nos 4 cantos, o secundário nos 4 lados e o núcleo no centro, no Advanced e no Elite. No Ultimate: diamantes nos cantos, netherite à esquerda e à direita, a estrela do Nether no centro, o bloco de redstone em cima e o meio de baixo vazio, como os conectores do cartucho |
| 817 | Qual item roxo entra no Elite Module no lugar da ametista? | O corante roxo |
| 807 | Quais são os materiais das receitas dos três Modules? | Advanced: 4 lingotes de ouro, 4 quartzos e um comparador. Elite: 4 diamantes, 4 corantes roxos e um bloco de redstone, porque a ametista deu lugar a outro item que lembra a cor do tier (pergunta 817). Ultimate: 2 lingotes de netherite, 1 estrela do Nether, 4 diamantes e um bloco de redstone. A grade está na 818. Fecha a 799 |
| 816 | O push da #144 espera o GitHub listar o `id_rsa` como chave de assinatura? | Não: o push segue, e os commits podem aparecer como Unverified até a chave de assinatura estar cadastrada |
| 815 | O remote `git@github.com:`, fora do padrão do `AGENTS.md`, pode ser trocado antes do push? | Sim, para `git@github.com-personal:marcelofabianov/cubit.git`, só neste repositório |
| 814 | Qual chave SSH assina os commits, se a ed25519 dos commits anteriores não está acessível nesta máquina? | O `~/.ssh/id_rsa`, a mesma chave do remote pessoal, configurado só neste repositório. O desenvolvedor a cadastra como chave de assinatura no GitHub, para os commits aparecerem como Verified. Detalha a 813 |
| 813 | Como assinar os commits da #144, que saíram com a chave GPG, se o `AGENTS.md` pede SSH? | Configurar a assinatura do repositório para seguir o padrão do projeto, com SSH, e reassinar os commits antes do push |
| 812 | As receitas dos Modules entram no PR da #144? | Não: ficam para o próximo PR, com os materiais da pendente 807. O item 4 do `checklist.md` só é marcado depois delas |
| 811 | Como ficam os Modules e os textos do upgrade em português? | Itens: `Módulo Avançado`, `Módulo Elite` e `Módulo Supremo`. Textos: `Cubit: Evoluí para Elite!`, `Cubit: Preciso ser Avançado antes` e `Cubit: Já sou Supremo`. Dica: `Exige um Cubit Avançado`. Segue os tiers já traduzidos no `pt_br` e a pergunta [733](fase-3-sentidos.md). Detalha a 808 |
| 810 | Como representar o filtro "Ilimitado" do scanner no Ultimate, se a chave `ore_filter_slots` aceita de 1 a 64? | Com o padrão 64, o limite da chave, que na prática cobre todos os minérios das tags `c:ores`. O `tiers.md` passa a dizer 64, e o contrato do config não muda |
| 809 | O que acontece quando quem não é dono aplica um Module, já que o aviso para outro player citado na 802 não existe? | Nada acontece, como na pintura e na etiqueta: sem texto, e o Module não é consumido. Corrige a [802](#respondidas) nesse ponto, pela regra 48 |
| 808 | Quais textos curtos o upgrade mostra? | Upgrade: `Cubit: Upgraded to Elite!`. Etapa pulada: `Cubit: I need to be Advanced first`. Mesmo tier ou acima: `Cubit: I'm already Ultimate`. Dica do Module: `Requires an Advanced Cubit`. O nome do tier vem traduzido no `pt_br` |
| 806 | O visual dos Modules gerado pela ferramenta do desenvolvedor fica como o definitivo? | Sim: um cartucho de circuito escuro, com conectores dourados embaixo e um núcleo na cor do colar do tier. O Elite ganha uma trilha a mais, e o Ultimate, duas trilhas e uma moldura clara em volta do núcleo. Os ícones têm 16×16, e as prévias ficam em `_docs/imagens/conceito/png/` |
| 805 | Quando as receitas dos Modules aparecem no livro de receitas? | As três aparecem quando o player tem um Cubit no inventário, como a do Induction Dock |
| 804 | Como o Module aparece no inventário? | Empilha até 64, e o nome aparece na cor do colar do tier: azul, roxo ou dourado |
| 803 | Como o colar ganha a cor do tier no ícone do Cubit e do Damaged Cubit? | Com uma segunda camada no ícone, só com o colar em tons claros, que o cliente tinge com a cor do tier. É 1 textura, gerada a partir do ícone atual, sem multiplicar os 16 modelos de cor. Fecha a pergunta [414](fase-3-sentidos.md) |
| 802 | O que acontece quando o Module não serve, porque pula uma etapa, o Cubit já está nesse tier ou acima, ou quem aplica não é o dono? | O Module não é consumido, e um texto curto na barra acima da hotbar explica o motivo: o tier que ele exige, ou que o Cubit já é Ultimate. Quem não é dono recebe o aviso que já existe para outro player. A dica do Module mostra o tier que ele exige. A [809](#respondidas) corrige o caso de quem não é dono: nada acontece |
| 801 | Como o Cubit reage ao receber um upgrade? | Expressão feliz e bipe, como ao receber um nome, e um texto curto na barra acima da hotbar com o novo tier. Com a tela apagada, só o texto aparece. Fecha o upgrade previsto na pergunta [455](fase-3-sentidos.md) |
| 800 | Em quais estados o dono aplica um Module no Cubit? | Em qualquer estado: ativo, desligado, sem bateria ou hibernando no Dock, como na pintura (pergunta [510](fase-3-sentidos.md)), porque o upgrade não gasta energia. Só o dono aplica (regra 20), e o Damaged Cubit nunca recebe |
| 799 | As receitas dos três Modules são independentes ou encadeadas, com o Module anterior no centro? | Independentes: nenhuma leva o Module anterior. Os materiais de cada uma o desenvolvedor envia depois, e a recomendação do agente não vale como decidida. Seguem só materiais do jogo puro (regra 4) |
| 798 | O alinhamento do item 4 da Fase 4 tem algo a acrescentar? Domínio dos tiers Advanced, Elite e Ultimate com o upgrade em sequência, números no config, os três Modules com receitas, aplicados só pelo dono, e o colar no ícone do Cubit e do Damaged Cubit seguindo o tier | Nada a acrescentar. Os ícones dos três Modules, gerados pelo desenvolvedor, ficam em `textures/item/`, e as prévias, em `_docs/imagens/conceito/png/` |
| 797 | Por onde começa o trabalho depois da Fase 3: o que ficou da `0.1.0` ou a Fase 4? | Pela Fase 4, no item 4 do `checklist.md`: os tiers |
| 796 | Em que ordem entram os itens da Fase 4, já que os modos de defesa atacam com o choque, e o choque é bloqueado no Base? | Tiers, choque com o Pacífico, Contra-atacar e Agressivo, só o alvo com o último mob atacado e, por fim, a aba Settings. Detalha a 794 |
| 795 | Onde entra a mira pelo controle do modo só o alvo, que na sequência nova ficaria na Fase 4, antes do Cubit Remote? | "a mira pelo controle do modo de defesa alvo pode entrar junto do controle": na Fase 4.2. O modo, com o último mob atacado, fica na Fase 4. Muda a 726 nesse ponto |
| 794 | A Fase 4 pode seguir a sequência proposta pelo desenvolvedor? | Sim, depois da avaliação do agente, que não achou impacto em saves nem contratos, porque o item já guarda o tier. Fase 4, Tier e ataque: tiers Advanced, Elite e Ultimate, com os módulos, os modos de defesa, o choque com o Pacífico e a aba Settings, com a chave geral e a do painel solar. Fase 4.1, Itens, filtros, carregamento e mais: ímã e coleta com o filtro, Power Bank, também no Dock e na grade de craft, e os slots de tochas, fornalha e carregador. Fase 4.2, Remote, área e retorno: Cubit Remote, modo área com a descarga, retorno automático e local de morte. Muda as 722 a 724 só na Fase 4, e as 795 e 796 ajustam o modo só o alvo e a ordem |
| 578 | Como fica a proposta do modo só o alvo? | "Gostei", e vai para a documentação, para ser refinada na Fase 4. Está em `funcionalidades/combate.md` como proposta. O desenvolvedor não respondeu separadamente sobre o nome e o tier, então seguem abertos na pendência 577 |
| 21 | Um robô desligado aceita a Power Bank? | Sim. Com o Cubit desligado e a Power Bank na mão, o clique direito coloca a Power Bank no slot exclusivo e inicia a recarga. Com 5% ele liga, e o player pode acessá-lo, remover a Power Bank e fazer as outras interações |
| 23 | Uma Power Bank funciona em robô de outro tier? | Sim, em qualquer tier |
| 24 | Qual é a velocidade de extração da Power Bank? | A mesma da recarga na base, conforme o tier do Cubit, configurável |
| 25 | Onde a energia entra? | O Induction Dock é a base de recarga, inspirada nas bases de robôs aspiradores. Ele recebe energia por cabos de diversos mods ou com redstone em um slot, e tem um pequeno estoque de energia. O Cubit também carrega a bateria interna com uma Power Bank ou redstone no slot. O Cubit só carrega pelo Induction Dock, pela Power Bank ou com redstone, além do painel solar, definido depois na [pergunta 51](fase-1-conceito.md) |
| 26 | A Power Bank sai do craft com carga? | Não, vazia |
| 27 | Em que fase a Power Bank entra? | Fase 4 |
| 28 | Os 70% prejudicam a necessidade de voltar à base? | Manter os 70% e avaliar nos testes, com o custo da receita |
| 42 | Onde a Power Bank recarrega? | No Induction Dock e em qualquer carregador de FE do pack |
| 56 | Desligado pelo controle remoto, ainda com bateria, o Cubit se comporta como inativo? | Sim, e só volta a ficar ativo pelo controle. O religamento sozinho vale apenas para quem ficou sem bateria |
| 58 | Como a Power Bank recarrega no Dock? | No mesmo slot da redstone. O slot aceita redstone, que vira energia para o Dock, ou uma Power Bank, que recebe energia do Dock |
| 68 | O que cada modo de defesa faz? | Peaceful nunca ataca. Retaliate ataca só quem atacou o Cubit ou o dono. Aggressive ataca os mobs hostis no alcance. Com mobs neutros, vale a pergunta 13. A [pergunta 244](fase-5-trabalhos.md) muda isso, só para o Aprendizado de Máquina: Defensor |
| 69 | O controle remoto também desativa o slot de energia? | Não. O controle desativa tochas, fornalha e carregador de ferramentas, e o slot de energia fica sempre ligado |
| 70 | A Power Bank empilha? | Não, cada uma ocupa um slot |
| 79 | Quais Cubits o controle remoto atende? | Todos os Cubits do player, desde que estejam ligados. O controle também localiza os Cubits e mostra as coordenadas |
| 80 | Como religar um Cubit desligado pelo controle, se o controle só age em ligados? | O controle também liga, como exceção |
| 82 | O que a função de localizar mostra? | Todos os Cubits do player, com nome, coordenadas, dimensão e estado. Em chunk descarregado, mostra a última posição conhecida, inclusive dos desligados |
| 83 | Um Cubit que trabalhava em área vai à base. O que ele faz ao sair dela? | Volta ao que fazia. Quem estava seguindo volta a seguir, se nenhum outro assumiu; se outro assumiu, fica parado. O estado foi definido na [pergunta 102](fase-2-versao-minima.md) |
| 93 | O upgrade de tier mantém a vida e as configurações? | Sim. A vida atual se mantém, e o máximo sobe para o do novo tier |
| 94 | Qual é o alcance do controle remoto ao mirar num Cubit? | 32 blocos, configurável. Localizar não tem limite de distância |
| 180 | O item do Cubit com a bateria zerada pode ser recarregado combinando-o com uma Power Bank na grade de craft? | Sim, na grade do inventário ou na mesa, na Fase 4, junto da Power Bank. Só com a bateria zerada, e o Cubit não perde mais nada. A pergunta 183 amplia para qualquer carga abaixo da de religar |
| 181 | Quanta energia essa receita transfere? | A carga de religar: a porcentagem de religar sobre a bateria do Cubit, ou só o que falta, pela pergunta 183. A Power Bank perde exatamente isso e precisa ter essa carga, porque 5% dela só daria 3,5% da bateria do mesmo tier, e o Cubit não religaria |
| 182 | Como essa receita se encaixa no uso do Cubit zerado? | O item com a bateria zerada e com vida pode ser colocado, mas fica desligado e com interação limitada. O player o recolhe e combina com a Power Bank, e ele recebe o suficiente para religar, se a Power Bank tiver a quantidade necessária. Detalha a pergunta 181 |
| 183 | Um Cubit com a bateria abaixo da carga de religar, mas sem estar zerada, pode usar essa receita? | Sim, recebendo só o que falta para religar. Amplia as perguntas 180 e 181 |
| 340 | O que um Cubit sem card faz numa área? | Coleta os itens do chão dentro da área, respeitando o filtro de coleta, e descarrega no baú marcado. Complementa a [pergunta 193](fase-5-trabalhos.md) |
| 341 | O que significa "mandar parar" no controle? | O Cubit continua ativo e com o vínculo, fica parado e não teleporta, e esse estado é salvo. "Voltar a seguir" retoma. Parado, ele não gasta energia, exceto com a lanterna acesa, pela [pergunta 469](fase-3-sentidos.md). Como mantém o vínculo, a regra da [pergunta 326](fase-2-versao-minima.md) não o desliga |
| 342 | A área da Fase 4 é em 2D ou em 3D? | Em 3D, por dois cantos. Os limites de `tiers.md`, como 16×16, valem para a base, e a altura máxima fica no config. Assim, o formato salvo não precisa de migração na Fase 5. Antecipa a [pergunta 194](fase-5-trabalhos.md) |
| 362 | Quando toca o som de modo agressivo? | Ao trocar para o modo agressivo, e só nesse momento. O desenvolvedor forneceu `cubit_modo_agresivo.wav`, convertido da mesma forma que os demais, para `aggressive_mode.ogg`. Passam a ser onze sons. Entra junto dos modos de defesa. Amplia a [pergunta 135](fase-3-sentidos.md) |
| 347 | Qual é a expressão do Cubit ao atacar? | Mantém a atual, sem troca, como ao pegar fogo. Com um inimigo perto, em geral ela já será a de alerta. Segue a [pergunta 158](fase-3-sentidos.md) |
| 422 | O filtro de coleta coleta só os itens definidos, ou também tem outro modo? | Tem os dois modos: lista branca, que coleta só os itens definidos, e lista negra, que coleta tudo menos os itens definidos. Detalhes na pendência 423 |
| 343 | O que o item guarda das configurações? | Modo de defesa, filtros e os slots ativados ou desativados. Perde a área, os baús marcados e a seleção no controle, porque dependem do lugar onde o robô estava. Detalha a [pergunta 254](fase-5-trabalhos.md). A [pergunta 468](fase-3-sentidos.md) acrescenta a chave da lanterna. As perguntas 514, 548, 549 e 582 acrescentam a cor, o filtro do scanner e as chaves dos scanners e das falas |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-12 14:10 | Registra a resposta 840. |
| Marcelo Fabiano | 2026-11-12 12:40 | Registra as respostas 837 a 839. |
| Marcelo Fabiano | 2026-11-12 11:40 | Registra as respostas 835 e 836. |
| Marcelo Fabiano | 2026-11-12 09:40 | Registra as respostas 820 a 834. |
| Marcelo Fabiano | 2026-11-12 08:30 | Responde a pendente 807 e registra as respostas 817 a 819. |
| Marcelo Fabiano | 2026-11-12 00:10 | Registra as respostas 815 e 816. |
| Marcelo Fabiano | 2026-11-11 23:55 | Registra as respostas 812 a 814. |
| Marcelo Fabiano | 2026-11-11 23:10 | Registra a resposta 811. |
| Marcelo Fabiano | 2026-11-11 22:01 | Registra as respostas 797 a 806 e 808 a 810, e a pendente 807. |
| Marcelo Fabiano | 2026-11-10 10:17 | Registra as respostas 794 a 796. |
| Marcelo Fabiano | 2026-11-07 23:42 | Recebe a pendente 425 da Fase 3 e acrescenta um ponteiro à 343. |
| Marcelo Fabiano | 2026-11-07 16:51 | Registra a resposta 578. |
| Marcelo Fabiano | 2026-11-07 16:48 | Registra a pendência 577. |
| Marcelo Fabiano | 2026-11-06 13:34 | Aponta as respostas 341 e 343 para as 469 e 468. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-06 09:32 | Registra a resposta 422 e a pendência 423. |
| Marcelo Fabiano | 2026-11-05 23:16 | Registra a resposta 362. |
| Marcelo Fabiano | 2026-11-05 21:36 | Registra a resposta 347. |
| Marcelo Fabiano | 2026-11-05 21:34 | Registra as respostas 340, 341, 342 e 343. |
| Marcelo Fabiano | 2026-11-05 21:02 | Aponta a resposta 68 para a 244. |
| Marcelo Fabiano | 2026-11-05 02:00 | Registra a resposta 183 e aponta as 180 e 181 para ela. |
| Marcelo Fabiano | 2026-11-05 01:58 | Registra a resposta 182. |
| Marcelo Fabiano | 2026-11-05 01:57 | Registra as respostas 180 e 181. |
| Marcelo Fabiano | 2026-11-04 18:50 | Aponta a resposta 83 para a pergunta 102. |
| Marcelo Fabiano | 2026-11-04 17:28 | Registra as respostas 93 e 94. |
| Marcelo Fabiano | 2026-11-04 17:26 | Adiciona as perguntas 93 e 94. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Registra as respostas 79, 80, 82 e 83. |
| Marcelo Fabiano | 2026-11-04 17:08 | Registra as respostas 68, 69 e 70. |
| Marcelo Fabiano | 2026-11-04 16:57 | Cria o arquivo com as perguntas e respostas da fase. |
