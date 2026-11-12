# Decisões

Resumo do que já foi decidido no projeto. As perguntas, abertas e respondidas, ficam em [`perguntas/`](perguntas/README.md).

## Projeto

- **Nome do mod e do robô:** Cubit.
- **Modid:** `cubit`.
- **Pacote Java:** `io.github.marcelofabianov.cubit`.
- **Repositório:** `marcelofabianov/cubit`.
- **Licença:** MIT, para todos os recursos presentes no repositório, inclusive os sons. A exceção é o Gradle Wrapper, de terceiros, que não vai no jar.
- **Ícone do mod:** o mesmo do item do Cubit.
- **Primeira versão:** `0.1.0`, publicada só ao terminar todos os itens da Fase 3, com a `1.0.0` só com o mod completo.
- **Publicação:** no Modrinth e no CurseForge, com a mesma versão nos dois (pergunta 714). A `0.1.0` sai como release, e o jar é enviado à mão, pelo site, sem plugin do Gradle (perguntas 743 e 744). Ela saiu no Modrinth, e o CurseForge fica para depois (perguntas 785 e 787).
- **Aviso de IA:** no Modrinth, o aviso `Contains AI-generated content` fica marcado para os assets, por causa dos sons (pergunta 746).
- **Páginas nas plataformas:** slug `cubit`, com `cubit-robot` nas duas se o CurseForge recusar. O ícone é o do item, e a galeria só tem capturas do jogo. O desenvolvedor cria as contas e os projetos, e o agente prepara os textos (perguntas 747, 748 e 750).
- **Changelog por versão:** atualizado ao criar a `release/X.Y.Z` (pergunta 749).
- **Repositório público:** o desenvolvedor o torna público antes de enviar o jar da `0.1.0`, e as issues do GitHub são o canal de bugs (pergunta 751).
- **Interface de configuração:** a chave geral e a do painel solar entram na Fase 4, na aba Settings, e a `0.1.0` sai com as chaves de hoje (pergunta 717). A chave geral só desliga, por um botão `Power` no cabeçalho, e libera o lugar de quem segue. O painel solar ganha a quarta linha em Functions, começa ligado e fica no item. O rodapé da aba sai, e hibernar fora do Dock fica para a Fase 5 (perguntas 857 a 861).
- **Fases 4 e 5 divididas:** Fase 4 com os tiers, o choque com o Pacífico, os outros modos de defesa, o modo só o alvo e a interface de configuração, nessa ordem; 4.1 com o ímã, a coleta, a Power Bank e os slots exclusivos; 4.2 com o Cubit Remote, a mira do modo só o alvo, o modo área, a descarga, o retorno e o local de morte. Fase 5 com antenas, Servidor e storage wireless virtual; 5.1 com o carregamento de chunks; e 5.2 com os cards, a hibernação fora do Dock e a personalização por card (perguntas 722 a 726, com a Fase 4 reorganizada pelas 794 a 796).
- **Planejado nos documentos:** o que é de fase futura fica nos documentos, marcado com a fase em que entra, sem parecer pronto (pergunta 718).
- **Testes de instância:** os da regra 35 ficam para o fim da Fase 3, no teste da versão completa, na seção "Teste da versão" do roteiro manual, no Prism Launcher. O Iron Meridian é testado numa cópia da instância, com o jar em `mods/` (perguntas 767 a 769).
- **Exceção na `0.1.0`:** a versão sai com o teste no Iron Meridian, sem a instância só com o mod e sem as confirmações do item 7, que ficam para depois dela (pergunta 776). As confirmações e a instância só com o mod foram feitas depois, com o upgrade de tier e as receitas dos Modules (pergunta 820).
- **Release no GitHub:** cada versão tem a tag anotada e uma release no GitHub, com a seção do `CHANGELOG.md` e o jar, antes do envio aos sites (pergunta 778).
- **Outro player:** o teste com outro player não é feito no jogo. As GameTests com um player que não é o dono cobrem o Dock, o `/cubit reset`, a cor e o nome (perguntas 770 a 772).
- **Etapa atual:** as Fases 1, 2 e 3 foram encerradas, com a `0.1.0` publicada (perguntas 785 e 786). A Fase 4 só começa com pedido explícito.
- **Iron Meridian:** o Cubit entra como exceção à regra do pack contra funções repetidas, por causa do Robit do Mekanism. O registro está na issue `marcelofabianov/iron-meridian#2`.

## Idioma

- **Mod:** o que entra no jar, aparece no jogo ou acompanha o download fica em inglês, o idioma base. O jogo também tem o `pt_br.json`, gerado pelo mesmo datagen, para o player escolher o português (pergunta 712, que substitui a 449).
- **Tradução:** os nomes são todos traduzidos, como `Base de Indução`, e o minério nos avisos e no filtro é o nome do bloco da tag, no idioma do jogo (perguntas 733 e 734).
- **Changelog:** o `CHANGELOG.md`, na raiz, fica em inglês, no formato Keep a Changelog, como exceção da regra 25 (perguntas 713 e 752).
- **Páginas das plataformas:** o resumo e a descrição longa ficam em inglês e citam a tradução para o português (pergunta 745). O texto fica em `desenvolvimento/descricao-das-plataformas.md` e termina com uma linha curta sobre o que vem depois, sem datas (perguntas 758 e 760).
- **Descrição na lista de mods:** traduzida pela chave `fml.menu.mods.info.description.cubit`, gerada pelo datagen, com o mesmo texto em inglês no `neoforge.mods.toml` (pergunta 759).
- **Programação:** arquivos como `.java` e `.sh` ficam em inglês.
- **Conteúdo:** arquivos como `.md` ficam em português, inclusive o nome do arquivo e da pasta.
- **Commits:** português, em Conventional Commits.

## Processo

- **Fluxo de Git:** Gitflow, Semantic Versioning e Conventional Commits, com `homolog` para a release em teste e `release/X.Y.Z` para a release fechada. O fluxo completo está no [`AGENTS.md`](../AGENTS.md#fluxo-de-branches).
- **Regras de desenvolvimento:** no [`AGENTS.md`](../AGENTS.md#regras-de-desenvolvimento). Os gates de commit e de push estão ativos, inclusive os de Java e Gradle.
- **Proteção de branches:** o plano atual do GitHub não permite em repositório privado, então valem só os gates locais.
- **Fonte da verdade:** as respostas em `perguntas/` prevalecem sobre os outros documentos.
- **Mapa dos documentos:** fica em [`README.md`](README.md), e as fases, no Roadmap do `README.md` da raiz.
- **Pilares:** as decisões técnicas seguem a hierarquia de [`pilares.md`](desenvolvimento/pilares.md).
- **Registro de perguntas:** toda pergunta respondida pelo desenvolvedor entra em `perguntas/` antes do próximo passo.
- **Validação:** toda entrega é conferida contra os documentos `.md`, e artefatos complementam essa conferência.
- **Config e testes na Fase 2:** o config entra antes da entidade e cresce a cada etapa. As GameTests e o gate de datagen entram na etapa "Cubit no mundo".
- **Histórico de modificações:** só os `.md` de `_docs/` têm a tabela.
- **Fase 2:** encerrada. Os testes de instância ficaram para o fim da Fase 3.
- **Documento desatualizado:** o agente sempre pergunta antes de corrigir, mesmo quando a correção parece clara.
- **Merge:** é feito somente pelo desenvolvedor.
- **Depois do merge:** o agente pede autorização para apagar a branch local e a remota, apresenta o próximo trabalho e pergunta se há ajuste no roteiro (regra 52).
- **Checklist das fases:** fica em [`checklist.md`](../checklist.md). É avaliado ao encerrar cada PR, e a marcação entra num novo commit, no PR seguinte.
- **Contratos:** comandos e chaves de tradução entram como quinto contrato, e os nomes só mudam com aviso, sem exigir migração.
- **Números no roteiro manual:** ele pode citar os valores padrão como resultado esperado, e `tiers.md` prevalece.
- **Auditoria de qualidade (perguntas 522 a 528):** antes dos scanners, entram o checksum do wrapper, a regra única de dono, os testes de permissão do Dock, as regras de decisão no domínio, a validação dos cinco records de estado, novas regras de ArchUnit e o gate de ramos. O CI fica para depois.
- **Jar publicado:** sem as GameTests, o datagen e as estruturas de teste, antes da `0.1.0` (pergunta 523).
- **Gates de push:** as GameTests também detectam classe de cliente carregada no servidor, sem exigir o EULA, então o `runServer` não é um gate. O datagen reprova se `src/generated` ficar diferente do Git.

## Mod

- **Energia:** o Cubit só carrega pelo Induction Dock, pela Power Bank, com redstone ou pelo painel solar.
- **Robô único:** o Cubit, `cubit:general_robot`, é o único robô do mod. Um card de Aprendizado de Máquina dá a ele um trabalho, na Fase 5, planejada. Sem card, ele é generalista.
- **Quantidade:** o player pode ter quantos robôs preferir, mas só um o segue. Os outros podem trabalhar em áreas. O controle remoto e a base atendem todos os robôs ligados.
- **Robô que segue sem bateria:** mantém o vínculo e volta a seguir ao religar. Se o player ligar outro, ele vira desligado.
- **Saída da base com outro seguindo:** o robô fica parado e desligado, como ao ser colocado.
- **Base com o dono offline:** o robô cheio continua na base e sai quando o dono entra.
- **Comando `/cubit reset`:** limpa o vínculo de quem segue, só do próprio player e sem permissão. Um robô ativo sem vínculo fica parado e desligado, com a carga mantida, e um clique o liga de novo.
- **Config:** `COMMON`, com chaves em `snake_case` por tier, como `[general_robot.base]`.
- **Entidade:** a `RobotEntity` herda de `PathfinderMob`, com o dono num UUID próprio.
- **Estado como item:** o componente `cubit:robot_state`, e o inventário em `cubit:robot_inventory`, fora do domínio.
- **Inventário:** menu próprio, na tela própria do Cubit, no estilo das imagens de conceito (perguntas 371 e 676). Com o config reduzido, o que não couber cai no chão.
- **Dono:** quem coloca o robô. O item do robô não guarda o dono, e o item danificado guarda, só para o resgate no vazio. Nenhum dano atribuído a ele fere o robô. O fogo de um isqueiro continua ferindo, porque o jogo não guarda quem o acendeu.
- **Lava:** o robô ativo que está na lava vai para o bloco seguro mais próximo. A rota continua sem entrar em lava.
- **Blocos que ferem:** o robô ativo vai ao bloco seguro mais próximo, nas mesmas condições em que o jogo fere o player, e isso cobra o extra de condição negativa. Inativo, fica parado.
- **Fogo:** o robô ativo em chamas anda ao acaso, mais rápido, evita perigos e nunca para enquanto houver para onde ir. Busca água só em chunks carregados, e ela precisa ter o topo livre e rota até ela. Inativo, fica parado. Ao apagar, volta a seguir. Os números estão em [`tiers.md`](tiers.md#valores-gerais).
- **Morte:** o robô vira um item danificado, que nenhum dano destrói, não some com o tempo, não entra em shulker box nem no inventário do robô e não pode ser colocado no mundo. Se cair no vazio, volta à última posição segura, ao dono online ou ao spawn do mundo, sem carregar chunk à força. A morte libera o vínculo, e com o dono offline isso acontece quando ele entra.
- **Receita do Cubit:** lembra o item, com redstone, lâmpada de redstone, ferro, diamante e obsidiana, conforme [`itens.md`](itens.md#cubit).
- **Receita do Induction Dock:** em forma de L, com redstone, ferro e baú, conforme [`itens.md`](itens.md#induction-dock).
- **Conserto:** usa a `Toolbox` (`cubit:toolbox`), na grade do inventário ou na mesa de craft, num tipo de receita do mod, `cubit:robot_repair`, com os itens no JSON. Ela é consumida a cada conserto e empilha até 16. O robô sai com parte da vida, conforme [`tiers.md`](tiers.md#valores-gerais), e a bateria zerada.
- **Dono que renasce:** o robô ativo que segue volta com ele, em qualquer dimensão, se ainda estiver carregado. Nenhum chunk é carregado à força, e passar por um portal não o leva.
- **Descida:** a rota desce de até 5 blocos, sem dano de queda, e continua subindo só 1 por vez.
- **Movimento:** a velocidade de referência está em [`tiers.md`](tiers.md#valores-gerais). Na água, o robô afunda e anda no fundo, sem se afogar. Na lava, leva dano igual ao do player. Não há dano de queda. O som de esteira entra na Fase 3.
- **Criativo:** aba própria, `cubit:main`, e colocar sempre gasta o item.
- **Estados:** ativo (ligado), inativo (desligado) e hibernação (na base, com o inventário e os slots acessíveis).
- **Painel solar:** só funciona com o Cubit ativo, de dia em todos os tiers e à noite só em Elite e Ultimate. Coberto por um bloco opaco, não carrega, e vidro deixa carregar. A célula pulsa de leve enquanto enche a bateria, e a chave entra com a interface de configuração. O clima não tem efeito, e no Nether e no End não funciona. Fica abaixo do consumo.
- **Induction Dock:** é a base de recarga, com torre e placa em U, 2 blocos de altura e dono, recebe energia por cabos ou redstone, sem saída, e tem um único slot, que também recarrega a Power Bank. A energia fica no item ao quebrar, e só o dono o quebra, além de um operador no modo criativo. Sem conseguir chegar, o Cubit teleporta.
- **Dock sem dono:** só um operador em criativo o remove, com aviso no log ao carregar (pergunta 524). Entra na issue #106.
- **Recarga pela grade de craft:** o item do Cubit abaixo da carga de religar, combinado com uma Power Bank, recebe só o que falta para religar, na Fase 4.
- **Consumo:** gasta ao andar de verdade, sem gastar parado nem empurrado. As exceções são a condição negativa, a lanterna acesa e as varreduras dos scanners, que gastam mesmo parado.
- **Parada do robô:** uma navegação própria cancela o recálculo de rota pendente, para o robô parado não voltar a andar.
- **Slot de energia na tela:** fica na aba Inventory da tela própria do Cubit, ao lado das barras, e o texto cortado ganha um tooltip.
- **Barras de bateria:** mostram ao menos um risco enquanto houver carga e só ficam vazias com 0 FE.
- **Barra de vida:** vermelha, acima da bateria na aba Inventory da tela do Cubit, sem falso zero.
- **Documentação para o player (fase a definir):** uma wiki em site estático no GitHub e um livro entregue ao criar o mundo, que aponta para ela.
- **Consumo por situação:** inventário cheio, água, subida e condições negativas somam um extra ao gasto. Com o inventário cheio, a velocidade cai. Os números estão em [`tiers.md`](tiers.md#consumo-por-atividade).
- **Slot de energia do Cubit:** existe em todos os tiers e aceita uma Power Bank ou redstone por vez. A redstone entra na Fase 2, e a Power Bank na Fase 4.
- **LambDynamicLights:** obrigatório no cliente, e um servidor dedicado abre sem ele.
- **Lanterna:** luz real, de nível 10, 12, 14 e 15 por tier, com 1, 1, 2 e 2 FE/t enquanto acesa, mesmo parado. Acende abaixo da luz 8 e apaga a partir da 10, lendo blocos e céu. Apagada e sem consumo hibernando ou desligada. A chave `Lamp` fica na aba Settings da tela do Cubit, começa ligada e é guardada no item. As pontas das antenas acendem com um halo suave que pulsa, e os olhos ganham um brilho em volta, na cor de cada expressão.
- **Claridade da tela:** luz 4, sem gastar energia, enquanto a tela brilha, inclusive hibernando.
- **Feixe:** é da lanterna de cabeça do Minerador, na Fase 5.
- **Ao colocar no mundo:** toca só o som de colocar. O de ligar fica para a passagem de desligado a ligado.
- **Margem das telas:** os 4 blocos ficam em constante, fora do config, porque seguem o padrão do jogo.
- **Ao atacar:** mantém a expressão atual, com o som de ataque.
- **Ordem da Fase 3:** a do [`checklist.md`](../checklist.md), com a personalização (pergunta 504) e uma refatoração técnica (pergunta 517) antes dos scanners, com o intervalo mínimo junto dos textos e alguns itens entrando em partes, até os scanners ligarem o resto.
- **Expressões:** camada própria da tela, com 8× a resolução do corpo, recortada do conceito, e brilho no escuro. Ordem: tela apagada, carregando, quase morto, triste, preocupado, alerta, surpreso, sono, feliz e normal.
- **Piscada e olhar:** só na expressão normal, no cliente e sem bipe: pisca a cada 5 s, e os olhos acompanham a subida e a descida.
- **Comunicação:** bipes e expressões, com poucos textos simples e diretos.
- **Sons:** onze sons. Nove entram na Fase 3, o do ataque, junto do choque, e o de modo agressivo, junto dos modos de defesa. O bipe padrão toca nas trocas de expressão sem som exclusivo. O intervalo mínimo entre repetições está em [`tiers.md`](tiers.md#valores-gerais), e os sons são do desenvolvedor. Todos os players por perto ouvem, com volume e alcance no `sounds.json`.
- **Modelo:** a geometria melhora já na Fase 2, com os detalhes das seis vistas, tudo fixo e sem mudar a hitbox. As expressões seguem na Fase 3.
- **Animações:** andar, olhar para o player, parado, e desligado e hibernando, com as esteiras em textura por quadros. O modelo final mantém a geometria e a textura do corpo. Ao levar dano, os braços ficam erguidos por cerca de 2 s.
- **Olhar para o dono:** na expressão normal, os olhos reagem a pulo, queda rápida e dono em outro nível, até 8 blocos, e o movimento do próprio robô vence. Com o dono em outro nível, o corpo também inclina cerca de 8°, mas não num pulo.
- **Colar:** o robô no mundo usa a cor do tier, numa camada por tier. Nos ícones do Cubit e do Damaged Cubit, uma camada do colar tingida pelo cliente com a cor do tier, sem multiplicar os modelos de cor (pergunta 803).
- **Modules (perguntas 799 a 811, 817 e 818):** cartucho de circuito com núcleo na cor do tier, empilhando até 64 e com o nome nessa cor. Só o dono aplica, em qualquer estado do Cubit, inclusive no Dock. O Cubit fica feliz, bipa e mostra o novo tier num texto curto. O Module que não serve não é consumido, e um texto explica o motivo, menos para quem não é dono, em que nada acontece (pergunta 809). As receitas são independentes, sem o Module anterior, e aparecem no livro ao ter um Cubit. Os materiais e a grade estão em [`itens.md`](itens.md#modules) (perguntas 807, 817 e 818). O filtro do scanner do Ultimate é 64, o limite do config (pergunta 810).
- **Indicador de bateria:** mostra a carga real em qualquer estado, com 4 níveis em alta resolução, verde e vermelho na bateria crítica, e brilha só com a tela acesa.
- **Cor da carcaça:** 16 cores dos corantes, com uma textura por cor, em qualquer estado, só pelo dono, gastando 1 corante. O branco volta ao original. Fita e faixas seguem o tier, e só o ícone do Cubit muda de cor.
- **Nome:** com etiqueta, só do dono, ou na bigorna, e sempre visível acima do Cubit.
- **Tronco central:** a coluna entre as esteiras e a carcaça tem 2 unidades de altura, e a hitbox não muda.
- **Trabalhos (Fase 5, planejada):** um único Cubit, com um card por vez, age só em área marcada e nunca sai dela. Antena e Servidor mantêm os chunks carregados, de forma opcional. Sem chunk de destino, ele espera. O detalhe está em [`trabalhos.md`](funcionalidades/trabalhos.md).
- **Modo Focus (perguntas 843 a 853):** o modo só o alvo se chama `Focus` e libera no Advanced. Só dá o choque no último mob que o dono feriu, com qualquer dano, sem perseguir, e um neutro ferido também vira alvo. Sem alvo, age como o Contra-atacar. O alvo acaba ao morrer, mudar de dimensão ou ficar a mais de 32 blocos do dono. Ganha uma seta amarela e maior, e o texto `Cubit: Target: Zombie` ao mudar. A mira pelo controle fica na Fase 4.2 (pergunta 795).
- **Limites:** nunca ataca o dono, outros players, outros Cubits nem mobs pacíficos. Só ataca mobs neutros para defender o player.
- **Choque e modos de defesa (perguntas 822 a 834):** entram juntos. O Cubit ativo dá o choque sem perseguir, com linha de visão, no alcance e no intervalo do tier, num alvo, ou em todos no Ultimate, com faíscas e o abate do dono. Pacífico no Base, Contra-atacar a partir do Advanced, com 30 s de memória, e Agressivo a partir do Elite, escolhidos numa aba Defense própria, com o escudo. O modo começa Pacífico e fica no item, e os modos e números de cada tier ficam no config.
- **Scanner:** `c:ores/*` significa a tag-mãe `c:ores` e suas subtags, porque tags não aceitam curinga.
- **Falas (perguntas 572 a 587 e 618 a 621):** o Cubit comenta o tempo e o horário ao mudarem, com um intervalo mínimo, dá parabéns pelas conquistas visíveis e diz que ama redstone quando o dono a minera, com a chave `Chatter`. As frases e as regras estão em [`falas.md`](conceito/falas.md).
- **Caixas dos scanners (perguntas 593 a 616):** só o dono vê, através dos blocos, uma caixa azul clara e discreta em volta de cada minério novo que a varredura achou, por cerca de 10 s ou até minerar, e uma seta vermelha para baixo sobre a cabeça do mob hostil enquanto ele estiver no raio, as duas com um pulso suave (perguntas 646 a 649).
- **Telas próprias (perguntas 371 e 675 a 692):** a tela do Cubit e a do Induction Dock seguem as imagens de conceito `v0.1.0`, e não o estilo do Minecraft, com retratos e torre em sprites 2D e as respostas como prioridade sobre as imagens. O Cubit desligado não abre a tela, e ela fecha se ele desligar. Os botões usam o clique de botão de menu do jogo, sem pacote próprio. Os estados do Dock usam os nomes das imagens, também no Jade, e os números aparecem abreviados, como `214k / 300k FE`. O Dock lê o Cubit na base por uma capability de entidade (pergunta 696), e o menu do Cubit lê o último tick por um attachment sem save (pergunta 698). O detalhe está em [`telas.md`](funcionalidades/telas.md).
- **Jade (perguntas 659 a 667):** opcional, na versão do Iron Meridian, num pacote isolado, `compat.jade`, mostra o tier, a bateria e o estado do Cubit e o estoque e o estado do Induction Dock, com os dados do servidor só enquanto alguém mira.
- **Cores dos scanners (perguntas 558 e 638 a 643):** as antenas e um brilho atrás do corpo ficam vermelhos com mob no raio e azuis por cerca de 3 s com minério achado, no tom das caixas, com o vermelho vencendo. Todos veem, e a luz real continua branca.
- **Scanners (perguntas 544 a 564 e 602 a 610):** só o Cubit ativo que segue varre, com a chave `Scanners`, numa esfera em volta dele, por um custo fixo por varredura, igual nos dois scanners, em intervalos separados para minérios e mobs. Só o que é novo avisa, dizendo o nome: `Cubit: Diamond Ore!!!` e `Cubit: Careful, Zombie!`, com o intervalo mínimo por nome. O filtro é definido clicando com um minério na mão, e os raros são uma lista de tags, de ferro para cima. Mob hostil é o que o jogo marca como `Enemy`. A entrega teve três PRs: detecção, caixas e cores, com a seta sobre o mob entrando junto das cores (perguntas 590, 615 e 647).
- **Estado do robô como item:** inclui as configurações, além de bateria, vida, inventário, tier, nome e cor. Perde a área, os baús marcados e a seleção no controle.
- **Modo área:** por dois cantos em 3D, já na Fase 4. Sem card, o robô coleta os itens do chão na área e descarrega no baú marcado.
- **Parar pelo controle:** o robô continua ativo e com o vínculo, parado e sem teleportar, e o estado é salvo.
- **Bateria crítica e textos:** a bateria crítica é de 5%, com sono, indicador vermelho e `Critical battery`, que vem só do Cubit ativo que segue, repetindo a cada intervalo mínimo. `Cubit: That hurts!` e `Cubit: Careful!` avisam do dano no robô e no dono, com o mesmo intervalo. Todos os textos vêm só do Cubit que segue. Os textos começam com `Cubit: ` e são desenhados pelo mod, menores e logo acima das barras da hotbar, com um pacote de rede do servidor para o cliente.
- **Filtro de coleta:** tem lista branca e lista negra.
- **Chaveamento:** têm chave a chave geral do Cubit, com ligar, desligar e hibernar, o painel solar, a lanterna, os scanners, o ímã, pegar itens dropados, a descarga em baú, o retorno automático e mostrar o local de morte do player. Cada chave entra com o recurso dela.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-12 17:20 | Resume as chaves do item 8. |
| Marcelo Fabiano | 2026-11-12 15:10 | Fecha o modo Focus. |
| Marcelo Fabiano | 2026-11-12 10:30 | Resume o choque e os modos de defesa. |
| Marcelo Fabiano | 2026-11-12 09:40 | Registra as confirmações feitas depois da `0.1.0`. |
| Marcelo Fabiano | 2026-11-12 08:30 | Fecha as receitas dos Modules. |
| Marcelo Fabiano | 2026-11-11 22:01 | Registra as decisões dos Modules e do colar no ícone. |
| Marcelo Fabiano | 2026-11-10 10:17 | Registra a Fase 4 reorganizada e a mira do modo só o alvo na 4.2. |
| Marcelo Fabiano | 2026-11-10 09:53 | Encerra a Fase 3 e registra a `0.1.0` no Modrinth, com o CurseForge depois. |
| Marcelo Fabiano | 2026-11-08 01:41 | Registra a exceção da `0.1.0` e a release no GitHub. |
| Marcelo Fabiano | 2026-11-08 01:15 | Registra como ficam os testes de instância e o teste com outro player. |
| Marcelo Fabiano | 2026-11-08 00:55 | Registra onde fica a descrição das plataformas e a tradução da descrição na lista de mods. |
| Marcelo Fabiano | 2026-11-08 00:33 | Registra o tipo da `0.1.0`, o envio manual, o idioma das páginas, o aviso de IA no Modrinth, as imagens, as contas, o slug, o momento do changelog e o repositório público. |
| Marcelo Fabiano | 2026-11-08 00:11 | Usa o nome do bloco nos exemplos de minério. |
| Marcelo Fabiano | 2026-11-07 23:42 | Atualiza o inventário, a lanterna e os PRs dos scanners para as telas próprias e registra a interface na Fase 4, o planejado e a divisão das Fases 4 e 5. |
| Marcelo Fabiano | 2026-11-07 23:34 | Registra o pt_br, o changelog e as plataformas. |
| Marcelo Fabiano | 2026-11-07 22:57 | Registra a leitura do último tick. |
| Marcelo Fabiano | 2026-11-07 22:44 | Registra a leitura do Cubit pelo Dock. |
| Marcelo Fabiano | 2026-11-07 22:30 | Registra as telas próprias. |
| Marcelo Fabiano | 2026-11-07 21:40 | Registra o Jade. |
| Marcelo Fabiano | 2026-11-07 21:05 | Troca a caixa do mob pela seta. |
| Marcelo Fabiano | 2026-11-07 20:51 | Registra as cores dos scanners. |
| Marcelo Fabiano | 2026-11-07 20:04 | Registra as caixas dos scanners e as falas da redstone. |
| Marcelo Fabiano | 2026-11-07 19:06 | Registra a esfera e o custo igual dos scanners. |
| Marcelo Fabiano | 2026-11-07 16:54 | Registra as falas. |
| Marcelo Fabiano | 2026-11-07 16:51 | Registra a ideia do modo só o alvo. |
| Marcelo Fabiano | 2026-11-07 16:20 | Registra a altura que o robô desce. |
| Marcelo Fabiano | 2026-11-07 16:17 | Registra a volta do robô com o dono que renasce. |
| Marcelo Fabiano | 2026-11-07 15:57 | Registra os scanners. |
| Marcelo Fabiano | 2026-11-07 14:25 | Registra o encerramento depois do merge. |
| Marcelo Fabiano | 2026-11-07 13:35 | Registra as decisões da auditoria de qualidade. |
| Marcelo Fabiano | 2026-11-07 13:21 | Registra a refatoração antes dos scanners. |
| Marcelo Fabiano | 2026-11-06 15:58 | Registra a cor, o nome e a ordem da personalização. |
| Marcelo Fabiano | 2026-11-06 15:05 | Registra o vidro, o sinal e a chave do painel solar. |
| Marcelo Fabiano | 2026-11-06 13:57 | Registra o halo e o brilho dos olhos. |
| Marcelo Fabiano | 2026-11-06 13:34 | Registra a lanterna, a claridade da tela e o feixe. |
| Marcelo Fabiano | 2026-11-06 11:10 | Corrige o robô único, os braços, o intervalo e a Fase 2. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-06 10:06 | Registra a bateria crítica e os textos. |
| Marcelo Fabiano | 2026-11-06 09:36 | Registra os recursos com chaveamento. |
| Marcelo Fabiano | 2026-11-06 09:32 | Registra os dois modos do filtro de coleta. |
| Marcelo Fabiano | 2026-11-06 09:23 | Registra o colar, o indicador de bateria e o tronco central. |
| Marcelo Fabiano | 2026-11-06 01:00 | Corrige a etapa atual. |
| Marcelo Fabiano | 2026-11-06 00:58 | Registra as animações e o modelo final. |
| Marcelo Fabiano | 2026-11-06 00:22 | Atualiza a ordem das expressões. |
| Marcelo Fabiano | 2026-11-06 00:20 | Registra a barra de vida. |
| Marcelo Fabiano | 2026-11-06 00:10 | Atualiza a ordem das expressões. |
| Marcelo Fabiano | 2026-11-05 23:56 | Registra a piscada e o olhar. |
| Marcelo Fabiano | 2026-11-05 23:36 | Registra as expressões. |
| Marcelo Fabiano | 2026-11-05 23:30 | Atualiza a ordem da Fase 3. |
| Marcelo Fabiano | 2026-11-05 23:16 | Registra o som de modo agressivo. |
| Marcelo Fabiano | 2026-11-05 22:39 | Registra quem ouve os sons. |
| Marcelo Fabiano | 2026-11-05 22:37 | Registra a ordem da Fase 3. |
| Marcelo Fabiano | 2026-11-05 22:33 | Encerra a Fase 2. |
| Marcelo Fabiano | 2026-11-05 22:25 | Registra a exceção do Gradle Wrapper e o momento dos testes de instância. |
| Marcelo Fabiano | 2026-11-05 22:22 | Registra que a licença MIT vale para todos os recursos. |
| Marcelo Fabiano | 2026-11-05 22:21 | Registra que a primeira versão sai com a Fase 3. |
| Marcelo Fabiano | 2026-11-05 22:18 | Registra o checklist das fases. |
| Marcelo Fabiano | 2026-11-05 21:50 | Registra a receita do Induction Dock. |
| Marcelo Fabiano | 2026-11-05 21:47 | Registra a receita do Cubit. |
| Marcelo Fabiano | 2026-11-05 21:36 | Registra a terceira rodada da auditoria. |
| Marcelo Fabiano | 2026-11-05 21:34 | Registra a segunda rodada da auditoria. |
| Marcelo Fabiano | 2026-11-05 21:30 | Registra a primeira rodada da auditoria. |
| Marcelo Fabiano | 2026-11-05 21:15 | Registra quando o LambDynamicLights entra no build. |
| Marcelo Fabiano | 2026-11-05 21:02 | Corrige o resumo do Dock, dos robôs especializados, da ordem da Fase 2, do dono e do consumo. |
| Marcelo Fabiano | 2026-11-05 20:48 | Registra o comando `/cubit reset`. |
| Marcelo Fabiano | 2026-11-05 20:36 | Define a fase do som de colocar no mundo. |
| Marcelo Fabiano | 2026-11-05 20:26 | Registra o som de colocar no mundo. |
| Marcelo Fabiano | 2026-11-05 17:27 | Registra o som do ataque. |
| Marcelo Fabiano | 2026-11-05 16:09 | Detalha o Induction Dock. |
| Marcelo Fabiano | 2026-11-05 15:32 | Registra a reação aos blocos que ferem. |
| Marcelo Fabiano | 2026-11-05 14:55 | Anota a wiki e o livro para o player. |
| Marcelo Fabiano | 2026-11-05 14:40 | Aponta o consumo por situação para `tiers.md`. |
| Marcelo Fabiano | 2026-11-05 14:35 | Registra que o merge é somente do desenvolvedor. |
| Marcelo Fabiano | 2026-11-05 14:13 | Registra o consumo por situação. |
| Marcelo Fabiano | 2026-11-05 13:54 | Registra o mínimo visível das barras de bateria. |
| Marcelo Fabiano | 2026-11-05 13:30 | Registra a parada do robô e o painel do slot de energia. |
| Marcelo Fabiano | 2026-11-05 12:52 | Registra o consumo ao andar. |
| Marcelo Fabiano | 2026-11-05 12:08 | Registra os robôs especializados. |
| Marcelo Fabiano | 2026-11-05 02:11 | Registra o inventário. |
| Marcelo Fabiano | 2026-11-05 02:00 | Amplia a recarga pela grade de craft. |
| Marcelo Fabiano | 2026-11-05 01:57 | Registra a recarga pela grade de craft. |
| Marcelo Fabiano | 2026-11-05 01:15 | Detalha a morte, o resgate no vazio e a receita do conserto. |
| Marcelo Fabiano | 2026-11-05 00:35 | Registra o ícone do mod. |
| Marcelo Fabiano | 2026-11-05 00:31 | Detalha o modelo da Fase 2. |
| Marcelo Fabiano | 2026-11-05 00:15 | Registra o novo lugar do mapa dos documentos. |
| Marcelo Fabiano | 2026-11-05 00:12 | Atualiza os gates e aponta os números para tiers.md, e completa os resumos de fogo, morte, sons, modelo e processo. |
| Marcelo Fabiano | 2026-11-04 23:27 | Registra a saída da lava. |
| Marcelo Fabiano | 2026-11-04 21:19 | Registra o item danificado e o conserto. |
| Marcelo Fabiano | 2026-11-04 21:02 | Detalha o comportamento em chamas. |
| Marcelo Fabiano | 2026-11-04 20:41 | Registra o fogo, a morte e o registro de perguntas. |
| Marcelo Fabiano | 2026-11-04 20:31 | Registra o dano na lava. |
| Marcelo Fabiano | 2026-11-04 20:09 | Registra as decisões de movimento. |
| Marcelo Fabiano | 2026-11-04 19:56 | Registra os gates de GameTests e datagen no push. |
| Marcelo Fabiano | 2026-11-04 19:24 | Registra o estado como item, o dono e o criativo. |
| Marcelo Fabiano | 2026-11-04 19:05 | Registra o config, a entidade e o momento das GameTests. |
| Marcelo Fabiano | 2026-11-04 18:50 | Registra as regras do robô que segue sem bateria e ao sair da base. |
| Marcelo Fabiano | 2026-11-04 17:51 | Registra a adoção dos pilares. |
| Marcelo Fabiano | 2026-11-04 17:44 | Encerra a Fase 1 e registra a regra de validação. |
| Marcelo Fabiano | 2026-11-04 17:26 | Atualiza a exceção no pack e o estado do robô como item. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra a flexibilidade para robôs especializados. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra a ideia futura de robôs especializados. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e registra a regra de idioma. |
| Marcelo Fabiano | 2026-11-04 17:11 | Registra que o controle e a base atendem todos os robôs ligados. |
| Marcelo Fabiano | 2026-11-04 17:08 | Registra a regra de vários robôs com um seguindo. |
| Marcelo Fabiano | 2026-11-04 16:57 | Reorganiza como resumo e registra as decisões das perguntas. |
| Marcelo Fabiano | 2026-11-04 16:02 | Define a persona do Cubit. |
| Marcelo Fabiano | 2026-11-04 15:58 | Adota as regras de desenvolvimento e o fluxo de branches. |
| Marcelo Fabiano | 2026-11-04 15:23 | Registra as decisões e o glossário. |
