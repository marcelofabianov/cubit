# Fase 5: Robôs especializados

Conceito para depois da Fase 4. Registrar aqui não autoriza implementar.

## Pendentes

Não há perguntas pendentes nesta fase.

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 190 | Onde registrar as ideias de robôs especializados? | Numa nova Fase 5, neste arquivo, com a fase no roadmap como planejada. Nada é implementado antes de a Fase 4 terminar |
| 191 | Como o card funciona no Cubit? | Fica num slot exclusivo e sai quando o player quiser, sem perda. Sem card, o Cubit é generalista. Morrer e recolher preservam o card |
| 192 | Quantas especialidades um Cubit tem ao mesmo tempo? | Uma, e o tier melhora o que o card faz |
| 193 | O que faz um Cubit com card quando está seguindo o player? | Age como generalista. O card só age em área marcada. Exceções podem ser decididas card a card |
| 194 | Como definir a altura da área de trabalho? | O player marca dois cantos em 3D, e a altura vem deles. A altura máxima fica no config, por tier |
| 195 | O que o Cubit nunca pode quebrar ou alterar? | Blocos com dados, como baús, máquinas, Dock, antena e Servidor, e a tag de blocos protegidos, que o pack pode editar. Cada card só mexe no seu tipo de bloco |
| 196 | O que o Cubit faz com o inventário cheio durante o trabalho? | Descarrega no baú o que conseguir. Quando não conseguir mais, entra em hibernação e só volta quando o player o religar. O termo é detalhado na pergunta 198 |
| 197 | Com que o Minerador cava? | Sem item de picareta, com a capacidade do tier. Base equivale a picareta de ferro, Avançado a de diamante, e Elite e Ultimate também a de diamante. A pergunta 210 muda isso: o Base não usa card, e o Avançado equivale a picareta de ferro. Os dois primeiros tiers não têm opções. Elite e Ultimate ativam uma ou mais opções equivalentes a encantamentos, como fortuna, eficiência e toque suave. Elite tem lista e nível limitados, e Ultimate abre todos os encantamentos possíveis de picareta, no último nível |
| 198 | Como fica a hibernação do Cubit parado por falta de onde descarregar? | É um estado semelhante ao desligado, mas com o player podendo usar o inventário e as configurações. O Cubit fica imóvel e sem executar tarefas, como dormindo (ZzzZZz). Fora do Dock, só volta quando o player o religar. Amplia a pergunta 47 |
| 199 | Quais opções equivalentes a encantamentos entram? | Um meio-termo criativo, sem ser literal: Inquebrável e Remendo, por exemplo, fazem a mineração gastar menos energia. A lista final é detalhada na pergunta 202 |
| 200 | Quais limites valem no tier Elite? | Eficiência III e Fortuna II. Toque Suave fica só no Ultimate, que tem Eficiência V, Fortuna III e Toque Suave |
| 201 | O Minerador pode sair da área marcada? | Não, sempre fica na área marcada em 3D. Um conjunto de antenas pode definir áreas maiores que 1 chunk |
| 202 | Como ficam as opções do Minerador no meio-termo? | Eficiência, Fortuna e Toque Suave agem como no jogo. Inquebrável reduz o custo de FE por bloco, e Remendo dá um desconto extra nos blocos em que encontra minério. Nenhum dos dois cria energia, e os percentuais ficam no config. Inquebrável e Remendo entram no Ultimate. Detalha a pergunta 199 |
| 203 | Fortuna e Toque Suave podem ficar ativos juntos? | Não. O player alterna entre os dois na tela do Cubit |
| 204 | O que o Fazendeiro faz na área? | Colhe o que estiver maduro, pelas tags comuns de plantações, e replanta com as sementes do inventário. Não ara terra nova. Sem semente, deixa o bloco vazio e segue |
| 205 | O que o Pecuarista faz na área? | Alimenta com itens do inventário, tosquia, recolhe ovos e abate só acima de um limite de animais por espécie, no config, poupando filhotes. A regra de nunca atacar mobs pacíficos ganha uma exceção só para este card e só na área |
| 206 | O documentado pode ser alterado? | Sim, conforme a regra 48 do `AGENTS.md`: um impedimento técnico pode alterar ou desfazer uma decisão. Ele é mostrado com evidência, o desenvolvedor decide, e a nova resposta aponta para a antiga. O publicado segue a regra 18 |
| 207 | O que o Defensor protege e onde? | É diferente do comportamento de defender o player ao seguir. Defende contra qualquer mob agressivo, ou neutro que esteja agredindo algo dentro da área. Ataca e guarda os drops no inventário, e nunca sai da área. Se um mob ataca de fora da área, como um esqueleto com arco, o Cubit recua e procura proteção. Quando o mob entra na área, ele ataca. Se o mob sair, ele volta a se esconder |
| 208 | O que o Organizador de storage faz? | Os detalhes ainda não estão fechados. Inicialmente, pega itens das saídas de fornalhas e máquinas, ou do chão, e procura baús que já têm o mesmo item. Com mais de um, a prioridade é o que possui a maior quantidade do item carregado |
| 209 | Como o player configura baús e filtros dos cards? | O Cubit Remote marca no mundo, e a tela do Cubit tem uma aba do card, com modo e filtros. As opções do Minerador ficam nela |
| 210 | O tier Base usa cards de especialidade? | Não, continuam bloqueados no Base. O primeiro tier que aceita cards é o Avançado, e nele o Minerador começa equivalente a picareta de ferro. Muda a pergunta 197 |
| 211 | Como o Defensor ataca nos tiers? | Cada tier que aceita o card ataca com mais dano. O Base não atua como Defensor |
| 212 | O que o Organizador faz quando nenhum baú tem o mesmo item? | Coloca no primeiro baú com espaço |
| 213 | O que o Construtor faz? | Inicialmente, é simples: retira blocos de uma área para aplainar, ou preenche espaços vazios ou com líquidos usando os blocos do inventário |
| 214 | O que o Defensor protege dentro da área? | O dono, outros players, robôs, animais e aldeões. Atacado, ele contra-ataca. Um mob hostil que entra na área só é atacado por iniciativa dele se o tier tiver o modo agressivo. Detalha a pergunta 207 |
| 215 | Em quais baús e máquinas o Organizador mexe? | Só nos baús marcados com o Cubit Remote, e "primeiro" é o primeiro marcado com espaço. Só tira o que a máquina oferece como saída, pela capability do NeoForge, sem tocar em entradas e combustível. A leitura roda em intervalos. Detalha as perguntas 208 e 212 |
| 216 | Como ficam os itens de card? | Um item por especialidade, como `cubit:miner_card` e `cubit:farmer_card`, cada um com receita própria em JSON e materiais do jogo puro. Eles empilham, porque o item não guarda dados |
| 217 | Como funcionam os modos do Construtor? | Dentro da área 3D. Aplainar retira os blocos permitidos, de cima para baixo. Preencher usa o bloco do filtro em vazios e líquidos, de baixo para cima. Sem o bloco ou com o inventário cheio, ele hiberna. Vale a proteção da pergunta 195. Detalha a pergunta 213 |
| 218 | Como a antena se relaciona com o Servidor? | Existem Servidores por tier, e cada tier tem uma quantidade fixa de antenas: o primeiro, por exemplo, até 2, e os últimos, mais. A receita de cada Servidor tem a dificuldade do Cubit do mesmo tier. O tier do Servidor não influencia a antena nem o Cubit diretamente: só limita a quantidade de antenas, e tiers variados podem conviver |
| 219 | Como a antena gasta a bateria? | Por tempo, em FE por tick enquanto ligada, mais em tiers maiores. Tem botão de ligar e desligar. Perto de zerar, avisa o dono. Ao zerar, solta os chunks e guarda o estado, para voltar quando recarregada |
| 220 | Como a área de trabalho se relaciona com a área carregada? | A área carregada sempre tem prioridade sobre o tier do Cubit. Se o tier permitir uma área maior que a carregada, ele só atua na parte carregada |
| 221 | O que acontece se a antena for quebrada, e quantas podem existir? | Ao quebrar, ela solta os chunks e vira item com a bateria guardada. A lista de antenas fica salva no mundo, e as que não existem mais são limpas ao abrir o jogo. O config tem um teto de chunks no mundo, e o player vê o motivo se passar dele |
| 222 | O que acontece com a antena sem Servidor, ou quando ele quebra? | O player clica no Servidor com a antena, e ela fica ligada. Sem ligação, a antena não carrega chunks. Com o Servidor quebrado, as antenas ligadas soltam os chunks, sem perder a ligação nem a carga, e voltam quando ele volta. O Servidor não aceita mais que o limite do tier |
| 223 | Como o Servidor gasta energia, e o que acontece sem ela? | Gasta FE por tick para manter o próprio chunk, se essa opção estiver ligada. Sem energia, solta o próprio chunk e avisa, mas as antenas continuam com a bateria delas. O Cubit só não consegue voltar enquanto o chunk estiver descarregado |
| 224 | Como o player passa para um tier maior do Servidor? | Com um item de upgrade de tier único no mod, aplicado com o botão direito no alvo e consumido, como nas máquinas do Mekanism. O mesmo item serve em vários alvos, como o Cubit e o Servidor |
| 225 | A antena tem tiers próprios? | Sim, cada tier aumenta a área carregada, e novos tiers do Servidor aumentam a quantidade de antenas que ele gerencia. O Servidor tem limite de número de antenas e de número de chunks carregados, para não haver desequilíbrio |
| 226 | Como o item de upgrade convive com os Modules? | Mantém um Module por tier, Advanced, Elite e Ultimate, sem pular etapas. Cada um serve no Cubit, no Servidor e na antena, com clique direito, e é consumido. Os alvos são reconhecidos por interface. Os números ficam em `tiers.md`. Detalha a pergunta 224 |
| 227 | Como o carregamento de chunks fica opcional? | Uma chave `chunk_loading_enabled` no config, ligada por padrão. Desligada, os blocos continuam existindo e guardando dados, mas ninguém carrega chunks. A opção do Servidor para o próprio chunk é outro controle, no jogo. Além disso, o Servidor mostra o log do que acontece numa antena, como o que o Cubit está fazendo e os inventários, o que justifica antena e Servidor mesmo com o carregamento desligado |
| 228 | A regra 22 ganha exceção? | Sim, para o Servidor e as antenas da Fase 5, por ação do player e com limites no config. O `AGENTS.md` foi ajustado |
| 229 | Como fica a viagem até o Dock? | O Dock fica obrigatoriamente no chunk do Servidor, ligado pelo controle. Com bateria baixa, o Cubit prefere um Dock livre na área. Sem ele, anda até a borda do carregado, teleporta para a borda do chunk do Servidor e caminha até o Dock, sem custo de FE. Se esse chunk estiver descarregado, espera na borda e tenta de novo a cada intervalo. Se a bateria acabar, desliga sem perder nada. Preso sem progresso, teleporta. Sem ponto seguro, aparece na frente do Dock |
| 230 | Como funciona o log do Servidor? | A tela mostra, por antena, robôs, tarefa, bateria, inventário e os últimos eventos, com limite no config. Os dados são enviados enquanto o chunk está carregado. Descarregado, mostra a última foto, com a hora. O controle continua só localizando. Detalha a pergunta 227 |
| 231 | Como o storage do Cubit se liga à base? | Por um inventário virtual, salvo no mundo, por dono e canal. Um Baú Wireless na base e o upgrade do Cubit acessam o mesmo canal, de qualquer lugar e dimensão, sem carregar chunks. Um Cubit por baú, ou vários no mesmo |
| 232 | Como escolher o canal? | Numa tela, com número e nome, como `Minérios` e `Fazenda`. A cor do corante pode colorir o bloco só como identificação |
| 233 | Quais limites e custos o storage tem? | É endgame: só no Ultimate, como upgrade caro. Transferir custa FE por item. Cada canal tem tamanho fixo, como um baú duplo, tudo no config. Toda alteração passa pelo servidor, num ponto único, com teste de duplicação. Só o dono acessa |
| 234 | Como os cards gastam energia? | Mantém o consumo ao andar e soma um custo por ação, por card, no config. O Minerador paga conforme a dureza do bloco. Sem ação, não gasta |
| 235 | Como evitar repetir funções do Iron Meridian? | O mod é construído para atender o player, não o modpack. Precisa dar suporte, por exemplo, a quem usa só o mod e suas dependências num Minecraft puro. A regra do projeto foi detalhada na pergunta 238 |
| 236 | Em que ordem a Fase 5 seria entregue? | Base, cards e infraestrutura: estado de trabalho, card em slot e aba de configuração; Fazendeiro; Organizador e Defensor; Minerador; Construtor; Pecuarista; antena e chunks; Servidor, log e viagem; storage wireless. A pergunta 251 inclui os novos cards |
| 237 | Como proteger o desempenho e evitar conflitos entre robôs? | Solução híbrida: um número fixo de Cubits por área, por exemplo até 3, cada um reservando o alvo antes de ir. Varreduras e ações têm limite por tick no config, e os robôs esperam a vez. Sem limite de quantidade no mundo (resposta 74). Testes com 20 a 30 robôs antes de fechar cada card |
| 238 | Qual é a regra de independência do mod? | O mod prioriza funcionar numa instância de Minecraft só com ele e suas dependências. As bibliotecas e dependências colocadas no mod são exceções: GeckoLib, LambDynamicLights e Jade. A pergunta 242 detalha quais são obrigatórias |
| 239 | Como fica o Iron Meridian no projeto? | Vira um alvo testado, não o motivo do mod. O texto diz que o mod é para o player. Antes da `main`, testa-se numa instância só com o mod e, depois, no Iron Meridian. As versões continuam compatíveis com ele. A revisão é mostrada antes de aplicar |
| 240 | De onde vem a energia do Dock, da antena e do Servidor, sem outros mods? | Todos aceitam redstone por slot ou funil, que vira FE interno. Cabos e mods são um extra, e nada exige gerador. O consumo é balanceado para funcionar assim |
| 241 | Como fechar o registro? | Antes de consolidar, fechar as contradições e avaliar novas especialidades: cozinheiro, pescador e fabricador, este com receitas configuradas e abastecimento de lugares definidos, com pedidos de quantidade fixa de itens para cada destino |
| 242 | Quais dependências são obrigatórias? | GeckoLib e LambDynamicLights são obrigatórias, e Jade é opcional. Detalha a pergunta 238 e muda as regras 6 e 24 do `AGENTS.md` |
| 243 | Onde os Docks podem ficar? | Todos ficam na área do chunk do Servidor ou próximos a uma antena. Mesmo com o carregamento de chunks desativado, o Dock deve estar próximo, até um número de blocos, do Servidor ou de uma antena. Muda a pergunta 229. A pergunta 246 trata do efeito antes da Fase 5 |
| 244 | O que vale no Defensor com os modos de defesa? | Com o card de Defensor, a opção Pacífico não fica disponível, e o Cubit é migrado para o modo seguinte. Muda, só para esse card, a pergunta 68 |
| 245 | E o Defensor com o inventário cheio? | Não hiberna por isso. Guarda os drops enquanto couberem, e o resto cai no chão, como no jogo. Só para sem bateria. Com baú marcado, descarrega quando não houver ameaça. Exceção à pergunta 196 |
| 246 | A exigência de Dock perto do Servidor ou da antena vale para quem? | Só para um Cubit com card que trabalha em área. Um Cubit sem card usa qualquer Dock, como nas Fases 2 a 4, sem Servidor. Limita a pergunta 243 |
| 247 | O Cozinheiro entra? | Sim, simples. Na área, leva alimentos crus de baús marcados a fumeiros e fornalhas marcados, abastece combustível do inventário e leva a comida pronta a um baú marcado. Usa blocos do jogo, sem máquina nova |
| 248 | O Pescador entra? | Sim. Na área com água aberta, pesca pela tabela de pesca do jogo, em intervalos do config e gastando FE, sem vara e sem boia. Com o inventário cheio, descarrega ou hiberna |
| 249 | O Fabricador entra? | Só para abastecer, sem craft. Leva uma quantidade fixa de um baú a cada destino, por pedidos. A pergunta 250 o torna um modo do Organizador |
| 250 | O que acontece com o card de Fabricador, sem craft? | Não existe card próprio: abastecer é um modo do Organizador |
| 251 | Em que ordem a Fase 5 seria entregue, com os novos cards? | Base comum; Fazendeiro e Pescador; Organizador, com o modo de abastecer; Cozinheiro; Defensor; Minerador; Construtor; Pecuarista; antena e chunks; Servidor, log e viagem; storage. Muda a pergunta 236 |
| 252 | Como seguir para consolidar? | Reavaliar se há furos. Se não houver, consolidar |
| 253 | Como o Cubit volta ao trabalho depois de hibernar fora do Dock? | Por um botão `Retomar` na tela e pelo Cubit Remote. O clique direito continua abrindo a tela, e Shift + clique continua recolhendo. Se o motivo não sumiu, ele hiberna de novo e explica. Detalha a pergunta 198 |
| 254 | Como um Cubit passa a trabalhar e como volta? | Com o Cubit Remote, o player define a área, e o processo só termina ao selecionar o Cubit no controle. O controle também manda esvaziar o inventário ou voltar à base. Se o player recolher o Cubit e colocá-lo de novo, precisa refazer a configuração |
| 255 | A antena gasta bateria com o carregamento desligado? | Um valor menor, no config, só para o log. Sem carga, deixa de enviar log, mas continua valendo como referência de Dock |
| 256 | Quais são os nomes no jogo? | Robot Server (`cubit:robot_server`), Signal Antenna (`cubit:signal_antenna`), Wireless Chest (`cubit:wireless_chest`), Wireless Storage Upgrade, e os cards Miner, Farmer, Rancher, Defender, Organizer, Builder, Cook e Fisher. Viram contrato só quando implementados |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 12:07 | Registra as respostas 253, 254, 255 e 256. |
| Marcelo Fabiano | 2026-11-05 12:03 | Registra as respostas 250, 251 e 252 e aponta as 236 e 249 para elas. |
| Marcelo Fabiano | 2026-11-05 11:57 | Registra as respostas 246, 247, 248 e 249. |
| Marcelo Fabiano | 2026-11-05 11:52 | Registra as respostas 242, 243, 244 e 245. |
| Marcelo Fabiano | 2026-11-05 11:47 | Registra as respostas 238, 239, 240 e 241. |
| Marcelo Fabiano | 2026-11-05 11:41 | Registra as respostas 234, 235, 236 e 237. |
| Marcelo Fabiano | 2026-11-05 11:36 | Registra as respostas 230, 231, 232 e 233. |
| Marcelo Fabiano | 2026-11-05 11:31 | Registra as respostas 226, 227, 228 e 229. |
| Marcelo Fabiano | 2026-11-05 11:25 | Registra as respostas 222, 223, 224 e 225. |
| Marcelo Fabiano | 2026-11-05 11:14 | Registra as respostas 218, 219, 220 e 221. |
| Marcelo Fabiano | 2026-11-05 11:06 | Registra as respostas 214, 215, 216 e 217. |
| Marcelo Fabiano | 2026-11-05 11:02 | Registra as respostas 210, 211, 212 e 213 e aponta a 197 para a 210. |
| Marcelo Fabiano | 2026-11-05 10:55 | Registra as respostas 206, 207, 208 e 209. |
| Marcelo Fabiano | 2026-11-05 10:46 | Registra as respostas 202, 203, 204 e 205. |
| Marcelo Fabiano | 2026-11-05 10:44 | Registra as respostas 198, 199, 200 e 201. |
| Marcelo Fabiano | 2026-11-05 10:38 | Registra as respostas 194, 195, 196 e 197. |
| Marcelo Fabiano | 2026-11-05 10:28 | Cria o arquivo e registra as respostas 190, 191, 192 e 193. |
