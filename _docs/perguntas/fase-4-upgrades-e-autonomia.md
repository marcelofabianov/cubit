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
