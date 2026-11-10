# Fase 3: Sentidos

## Pendentes

Nenhuma. A pendente 425 foi para a Fase 4 (pergunta 721).

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 48 | Onde os textos do Cubit aparecem? | Na barra acima da hotbar, só para o dono e com intervalo mínimo entre repetições. A pergunta 436 troca a barra do jogo por uma camada própria, no mesmo lugar |
| 49 | Os cinco textos ficam assim em inglês? | Sim: `Critical battery`, `Enemy nearby`, `Ore found`, `I need to recharge` e `Items unloaded`. A pergunta 435 acrescenta o prefixo `Cubit: `, as perguntas 440 e 441, dois textos, e a 546 troca `Enemy nearby` e `Ore found` pelo nome do que foi achado |
| 52 | Quais são os novos valores do solar, abaixo do consumo? | De dia: Base 5, Advanced 10, Elite 12 e Ultimate 16 FE/t. À noite, 25% do valor de dia: Elite 3 e Ultimate 4 FE/t |
| 53 | Como o solar se comporta com blocos, clima e outras dimensões? | Só blocos importam: coberto não carrega, e descoberto carrega 100%. O clima não tem efeito. No Nether e no End não carrega |
| 57 | Qual é a expressão em hibernação? | Sempre carregando |
| 66 | A partir de quanta bateria o Cubit mostra a expressão de sono? | 10%, igual à bateria crítica. A pergunta 432 muda para 5% |
| 67 | O texto `Ore found` vale para qual minério? | Para minério do filtro do scanner. Bipe e surpresa, só para os raros. A pergunta 546 troca o texto, e a 562 mantém a regra |
| 92 | Qual é a expressão ao recarregar pelo slot, fora da base? | Inativo, tela apagada. Ativo, a expressão normal. A expressão carregando fica só para a base |
| 135 | Quais sons o Cubit tem? | Oito sons diferentes: esteiras ao andar, como as de um tanque de guerra; mob hostil encontrado; minério raro encontrado; bipe padrão ao trocar de expressão; curto elétrico enquanto pega fogo; ao ligar; ao desligar; e enquanto é recarregado. O detalhe está em [`visual.md`](../conceito/visual.md#sons). As perguntas 316 e 320 acrescentam o som do ataque e o de colocar no mundo |
| 136 | O bipe padrão vale para a troca de quais expressões? | Substituída pela pergunta 148, que limita às expressões sem som exclusivo. Esta resposta substituiu a pergunta 12 |
| 137 | Os sons são do desenvolvedor e podem ser publicados? | São dele, gerados com ferramentas de IA de estúdio. Os originais ficam em `tmp/wav/`, fora do repositório |
| 140 | Os sons convertidos estão bons? | Não, o desenvolvedor ouviu cortes e sensação de congelamento ou perda. A conversão precisa ser revista |
| 141 | Quais versões dos sons ficaram boas no VLC? | Os sons 3, 4 e 6 estão normais. Os sons 1, 2, 5, 7 e 8 têm cortes na reprodução |
| 142 | Os cortes nos sons vêm dos arquivos ou do reprodutor? | Do reprodutor: no VLC havia cortes, e no `pw-play` os sons tocaram sem problema. A conversão está certa. Desfaz a conclusão da pergunta 140 e explica a 141 |
| 148 | Ao trocar para uma expressão com som próprio, o bipe padrão toca junto? | Não. O bipe padrão toca nas trocas de expressão que não têm um som exclusivo. Substitui a pergunta 136. As perguntas 565 e 584 acrescentam o bipe na resposta do filtro e nas falas |
| 149 | Qual é o intervalo mínimo entre repetições dos textos e dos alertas sonoros? | O valor será definido na Fase 3, num teste no jogo, e ficará no config. Complementa a pergunta 48. As perguntas 433 e 444 definem o valor |
| 157 | Quais são as reações do Cubit ao ligar e ao pegar fogo? | Ao ligar, toca o som de ligar. Ao pegar fogo, toca o som de curto elétrico. Confirma a pergunta 135 |
| 158 | Qual expressão o Cubit mostra ao ligar e ao pegar fogo? | Ao ligar, a normal. Ao pegar fogo, mantém a atual, sem troca |
| 264 | Quando entram o visual melhor e os tooltips do painel do slot de energia? | Na tela própria da Fase 3, a da [pergunta 186](fase-2-versao-minima.md). Na Fase 2, o painel continua simples |
| 316 | Qual é o som do ataque do Cubit? | O desenvolvedor forneceu `cubit_ataque_choque.wav`, convertido da mesma forma que os demais, para `shock_attack.ogg`. Passam a ser nove sons. O original fica em `tmp/wav/`. Amplia a pergunta 135 |
| 320 | O que fazer com o som novo, `robot_quando_colocado_no_mundo.wav`? | Converter para Ogg e colocar no diretório dos demais sons do projeto, para ser usado. Convertido da mesma forma que os demais, para `placed.ogg`. Passam a ser dez sons. O original fica em `tmp/wav/`. Amplia a pergunta 135. As perguntas 321 e 322 detalham o nome, o momento e a fase |
| 321 | O nome `placed.ogg` e o momento "ao ser colocado no mundo" estão certos? | Sim. Detalha a pergunta 320 |
| 322 | Em que fase o som de colocar no mundo passa a tocar? | Na Fase 3, junto dos outros oito. Detalha a pergunta 320 |
| 421 | Como foi o roteiro manual do colar, do indicador de bateria e do tronco central com +0,5? | Teste finalizado, tudo ok, executado pelo desenvolvedor, sem detalhar cada passo. Ele não pediu +1, então o tronco fica em 2 unidades. O `make server` e o olhar de longe não foram citados em separado |
| 791 | O `README.md` ganha o link da página do Cubit no Modrinth? | Não, por enquanto. Os links das plataformas entram juntos, quando o CurseForge entrar (resposta 787) |
| 790 | O `ROADMAP.md` é atualizado no encerramento da Fase 3? | Sim: a Fase 3 passa a encerrada, a lista "Falta a reta final do lançamento" sai, e a seção da `0.1.0` diz que ela saiu no Modrinth, com o CurseForge depois (resposta 787). Amplia a 789 |
| 789 | O que o encerramento da Fase 3 atualiza? | O `checklist.md`, com o item 9 da `0.1.0` e a Fase 3 encerrada; o roadmap do `README.md`, de onde o desenvolvedor já tinha tirado o status (`96ed561`); o "Estado atual" do `AGENTS.md`, com a autorização do desenvolvedor; e estas respostas, com o resumo em `decisoes.md` |
| 788 | Por qual issue entra o encerramento da Fase 3? | Pela #137, ainda aberta, na branch `feature/137-close-phase-3`, e o PR fecha a issue |
| 787 | Como fica o CurseForge, que a pergunta 714 previa junto do Modrinth? | Fica para depois, com a mesma versão, sem bloquear o encerramento. Muda a 714 só nesse ponto |
| 786 | A publicação da `0.1.0` e a Fase 3 estão encerradas? | Sim: "esta parte de publicacao e fase 3 podemos definir como encerrado" |
| 785 | O envio da `0.1.0` aos sites foi feito? | Ao Modrinth, sim, pelo desenvolvedor. Antes, o agente conferiu os merges do PR #139, da `release/0.1.0` para a `main` (`1480b53`), e do #140, de volta para a `develop` (`a75ca09`), a tag `v0.1.0` no remote, a release no GitHub com o `cubit-0.1.0.jar` e o repositório público |
| 784 | O jogo está fechado antes do push da `release/0.1.0`? | Sim |
| 783 | O desenvolvedor fez o merge do PR #138, da `develop` para a `homolog`? | Sim. O agente conferiu o merge (`7a0bd90`) e criou a `release/0.1.0` a partir da `homolog`. As respostas 780 a 782 tinham sumido do disco às 01:46, durante o `pre-push` da remoção da branch remota da #135, sem causa achada nos hooks, e foram registradas de novo |
| 782 | Como a publicação da `0.1.0` entra no fluxo de issues? | Por uma issue "Publicação da 0.1.0" (#137), no milestone da Fase 3. Os PRs da `develop` para a `homolog`, da `release/0.1.0` para a `main` e de volta para a `develop` citam a issue, que fecha no fim |
| 781 | O jogo está fechado antes de apagar a branch remota da #135? | Sim. O agente, com a autorização, apagou a branch local e a remota |
| 780 | O desenvolvedor fez o merge do PR #136? | Sim. O agente conferiu o merge (`e927bdd`) e a issue #135 fechada. Os itens 4 a 8 da `0.1.0` já entraram marcados no próprio PR |
| 779 | O jogo está fechado antes do push da #135? | Sim |
| 778 | A publicação também tem release no GitHub? | Sim: "vamos criar a release e tags no github". Depois do merge na `main`, a tag anotada `v0.1.0` e uma release no GitHub com a seção da versão do `CHANGELOG.md` e o jar, antes do envio aos sites. Entra no `fluxo-de-trabalho.md` e no `publicacao.md` |
| 777 | Os termos das ferramentas de IA que geraram os sons foram conferidos (item 8 da `0.1.0`, resposta 338)? | Sim: o desenvolvedor conferiu, e eles permitem publicar os sons sob a licença MIT |
| 776 | O que fica antes de publicar a `0.1.0`, com o teste só com o mod ainda não feito com o jar? | Publicar sem esse teste: o teste no Iron Meridian (resposta 774) vale para o item 5, como exceção à regra 35 nesta versão, e as confirmações do item 7 ficam para depois da `0.1.0`. Muda a 337 só nesse ponto |
| 775 | O que fazer depois do teste no Iron Meridian? | "ja queria publicar nos sites": o desenvolvedor quer publicar a `0.1.0` no Modrinth e no CurseForge, sem a instância só com o mod antes. A 776 decide o que fica antes da publicação |
| 774 | Como foi o teste no Iron Meridian, os passos 4 a 7 da parte "Iron Meridian" do teste da versão? | Tudo ok nos quatro passos: seguir e telas, cabos reais de FE no Dock, lanterna com os shaders e Jade. O agente criou a cópia `IronMeridian2-CubitTest` no Prism, sem os mundos e sem o packwiz, com o jar de `190b6b6`, e o log não teve erro do Cubit. O aviso `defineId called for ... RobotSyncedData` aparece 12 vezes na carga |
| 773 | O jogo está fechado antes do `make build` do teste da versão, na #135? | Sim |
| 772 | Correção do agente sobre a 771: falta mesmo a GameTest do corante de outro player? | Não. A `onlyTheOwnerPaints`, em `ColorGameTests`, já confere que o corante de outro player não pinta nem é gasto, e a primeira busca do agente não a achou. Nenhuma GameTest nova entra, e o item 6 fica coberto pelas GameTests de hoje. Muda a 771 |
| 771 | O item 6 da `0.1.0` sai do teste no jogo, coberto pelas GameTests com um player que não é o dono? | Sim, com uma GameTest nova do corante usado por outro player, a única que faltava: ele não pinta o Cubit nem é gasto. O passo 9 do Dock, o 6 do `/cubit reset` e o 13 da cor e do nome ficam cobertos pelas GameTests. Detalha a 770 e muda a 515 |
| 770 | Qual caminho para o teste com outro player, o item 6 da `0.1.0`? | "nao precisamos testar a LAN": o teste com outro player não é feito no jogo. A 771 detalha |
| 769 | Como o jar entra no Iron Meridian para o teste, antes de estar publicado? | Numa cópia da instância no Prism, sem o comando do packwiz só na cópia, com o jar em `mods/` e num mundo novo. A instância e os mundos do desenvolvedor ficam intocados |
| 768 | Em que launcher ficam as instâncias dos testes? | No Prism Launcher, instalado na máquina e usado pelo `INSTALL.md` do Iron Meridian |
| 767 | Onde fica o roteiro dos testes de instância? | Numa seção "Teste da versão" no fim do `roteiro-manual.md`, que monta as instâncias com o jar e aponta para as seções existentes, sem repetir os passos. Vale também para as próximas versões |
| 766 | Há algo a acrescentar no alinhamento dos testes de instância, itens 5 a 7 da `0.1.0`? | "nao": o plano segue como apresentado, e as decisões vão para a rodada |
| 765 | Há ajuste no roteiro depois do PR #134? | Não: seguir o plano, com os itens 5 a 7 da `0.1.0`, os testes de instância, numa issue em que o agente prepara um roteiro único e o desenvolvedor joga, e depois os itens 8 e 9 |
| 764 | O jogo está fechado antes de apagar a branch remota da #133? | Sim. O agente, com a autorização, apagou a branch local e a remota |
| 763 | O desenvolvedor fez o merge do PR #134? | Sim. O agente conferiu o merge (`190b6b6`) e a issue #133 fechada. O item 4 da `0.1.0` no `checklist.md` fica marcado num commit do próximo PR |
| 762 | O jogo está fechado antes do push da #133? | Sim. Fica sem commit até o próximo PR |
| 761 | O jogo está fechado antes de formatar, gerar os idiomas e rodar o `make check` da #133? | Sim. O `./gradlew test` do teste novo já tinha rodado antes da pergunta, uma falha do agente, que passou a perguntar antes de cada compilação |
| 760 | A descrição longa das plataformas mostra o que está planejado para as próximas fases? | Só uma linha curta no fim, sem datas. O texto descreve o que a versão faz e diz que só o tier Base existe |
| 759 | A descrição curta na lista de mods ganha tradução? | Sim: a chave `fml.menu.mods.info.description.cubit`, que o NeoForge 21.1.265 lê antes do `neoforge.mods.toml` (`ModListScreen.java:426`), entra no `en_us.json` e no `pt_br.json` pelo datagen. O `neoforge.mods.toml` leva o mesmo texto em inglês |
| 758 | Onde fica guardada a descrição longa das plataformas? | Em `_docs/desenvolvimento/descricao-das-plataformas.md`, em português, com o resumo e a descrição em inglês em blocos prontos para copiar. O guia de publicação aponta para ele |
| 757 | Há algo a acrescentar no alinhamento do item 4 da `0.1.0`, a revisão da descrição do mod? | "continue": nada a acrescentar, e as decisões vão para a rodada |
| 756 | Há ajuste no roteiro depois do PR #132? | Não: seguir o plano, com o item 4 da `0.1.0`, a revisão da descrição do mod, e depois os itens 5 a 9 |
| 755 | O jogo está fechado antes de apagar a branch remota da #131? | Sim. O agente, com a autorização, apagou a branch local e a remota |
| 754 | O desenvolvedor fez o merge do PR #132? | Sim. O agente conferiu o merge (`4a54d74`) e a issue #131 fechada. Nenhum item do `checklist.md` fica marcado, porque os itens 4 a 9 da `0.1.0` continuam abertos |
| 753 | O jogo está fechado antes do push da #131? | Sim. Fica sem commit até o próximo PR |
| 752 | O texto da exceção do `CHANGELOG.md` na regra 25 do `AGENTS.md` está aprovado? | Sim: "O `CHANGELOG.md` fica em inglês, no nome e no conteúdo, porque vai para as páginas de versão das plataformas (pergunta 713)." Aplica a resposta 713 |
| 751 | Como tratar os links do código e das issues, com o repositório privado? | O desenvolvedor torna o repositório público antes de enviar o jar da `0.1.0`, depois de conferir que nada privado está nele. O guia e o `CHANGELOG.md` usam os links do GitHub, e as issues são o canal de bugs |
| 750 | Qual slug o Cubit usa nas duas plataformas? | `cubit`, livre no Modrinth (conferido pela API pública em 2026-11-08). Se o CurseForge recusar, os dois usam `cubit-robot`, para ficarem iguais |
| 749 | Em que momento o `CHANGELOG.md` é atualizado, a partir da `0.1.0`? | Ao criar a `release/X.Y.Z`, junto do ajuste da versão, com o que entrou desde a versão anterior. Os PRs de feature não mudam o changelog. Detalha a resposta 713 |
| 748 | Quem cria as contas e os projetos nas plataformas? | O desenvolvedor cria as contas e os projetos e envia o jar, pelo guia. O agente prepara os textos, o changelog e a lista do que preencher |
| 747 | Que imagens vão para o ícone e a galeria nas plataformas? | Só o ícone do item e capturas de tela feitas no jogo, sem as pranchas de conceito, porque o Modrinth proíbe imagem criada ou derivada de IA na página do projeto. As capturas são feitas no teste de instância |
| 746 | O que fazer com a regra do Modrinth que exige o aviso de conteúdo gerado por IA, já que os sons foram gerados com ferramentas de IA (resposta 137)? | Marcar o aviso `Contains AI-generated content` na aba Disclosures do Modrinth, só para os assets, citando os sons. O guia de publicação registra isso, e o item 8 da `0.1.0` continua conferindo os termos das ferramentas. O CurseForge só pede aviso em imagem de vitrine que engane |
| 745 | Em que idioma ficam o resumo e a descrição longa nas páginas do Modrinth e do CurseForge? | Em inglês, pela regra 1, citando que o mod também tem a tradução para o português |
| 744 | Como o jar é enviado ao Modrinth e ao CurseForge a cada versão? | Manual, pelo site, sem plugin do Gradle e sem token na máquina. O guia descreve o passo a passo |
| 743 | Com que tipo a `0.1.0` sai nas plataformas: alpha, beta ou release? | Release. O agente tinha recomendado alpha |
| 742 | Há algo a acrescentar no alinhamento da issue do `CHANGELOG.md` e do guia de publicação? | "continue, nada a acrescentar": o plano segue como apresentado, e as decisões prováveis vão para a rodada |
| 741 | Há ajuste no roteiro depois do PR #130? | Não: seguir o plano, com a issue do `CHANGELOG.md` e do guia de publicação, e depois os itens 4 a 9 da `0.1.0` |
| 740 | O desenvolvedor fez o merge do PR #130? | Sim. O agente conferiu o merge (`d522098`) e a issue #129 fechada e, com a autorização, apagou a branch local e a remota |
| 739 | O jogo está fechado antes do push da #129? | Sim. Fica sem commit até o próximo PR |
| 738 | O jogo está fechado antes de compilar o nome do minério pelo bloco e rodar as GameTests, na #129? | Sim |
| 737 | O jogo está fechado antes de gerar o `pt_br.json`, na #129? | Sim |
| 736 | A nova regra 1 do `AGENTS.md` fica com o inglês como base e os arquivos de tradução em outros idiomas, com as mesmas chaves conferidas por um teste? | Sim, o desenvolvedor aprovou o texto. Aplica as respostas 712 e 732 |
| 735 | Como organizar os textos em português no datagen? | Um provedor por idioma: o `CubitLanguageProvider` recebe o idioma, e cada classe de textos ganha a versão em português ao lado da inglesa. Um teste JUnit confere que as duas línguas têm as mesmas chaves |
| 734 | O que fazer com os nomes de minério nos avisos e no filtro, que vêm da tag em inglês? | Usar o nome do bloco: o texto mostra o nome traduzido do primeiro bloco da tag, como `Minério de Diamante` em português, e segue o idioma do jogo, como já acontece com os mobs. Muda as respostas 546 e 566 |
| 733 | Como ficam os nomes dos itens em português? | Tudo traduzido, como `Base de Indução`, `Cubit Danificado` e `Caixa de Ferramentas`. O nome do robô, Cubit, continua |
| 732 | Há algo a acrescentar ao plano da tradução `pt_br`? | Sim: a regra 1 deixa claro que os arquivos de tradução podem ter textos em outros idiomas além do padrão. Detalha a resposta 712 |
| 731 | Como ficam os milestones com as fases divididas? | Criar os quatro que faltam: `Fase 4.1: Tiers`, `Fase 4.2: Controle e autonomia`, `Fase 5.1: Carregamento de chunks` e `Fase 5.2: Cards de Aprendizado de Máquina`. Os da Fase 4 e da Fase 5 continuam valendo para a primeira parte de cada uma |
| 730 | Há ajuste no roteiro depois do PR #128? | Não: seguir o plano, com a issue da tradução `pt_br`, depois a do `CHANGELOG.md` com o guia de publicação, e depois os itens 4 a 9 da `0.1.0` |
| 729 | O desenvolvedor fez o merge do PR #128? | Sim. O agente conferiu o merge (`3a36dce`) e a issue #127 fechada e, com a autorização, apagou a branch local e a remota |
| 728 | O jogo está fechado antes do push da #127? | Sim. Fica sem commit até o próximo PR |
| 727 | O roadmap ganha um arquivo próprio? | Sim: "crie um arquivo na raiz .md ROADMAP.md". O agente o escreveu em português, pela regra 25, com o nome em inglês como os outros arquivos da raiz, e o roteiro do `README.md` virou um resumo com link para ele. O `checklist.md` continua marcando o que está pronto |
| 726 | Em que fase entra o modo de defesa "atacar o alvo" (só o alvo, pendente 577)? | Na Fase 4.2, junto do Cubit Remote, com as duas formas de escolher o alvo: o último mob que o dono atacou e o mob em que ele mira com o controle. Confirma a 723 |
| 725 | Como fica a Fase 5 no roteiro dividido? | Como o desenvolvedor propôs, com ajustes. Fase 5: antenas, com a ligação ao Servidor, o log e o Dock de referência, o Servidor e o storage wireless virtual. Fase 5.1: carregamento de chunks. Fase 5.2: a base comum dos cards (estado de trabalho, card em slot e aba de configuração), os oito trabalhos, a hibernação fora do Dock e a personalização do Cubit de acordo com o card, ideia nova que ganha conceito em documento antes de entrar |
| 724 | Onde ficam os itens da Fase 4 que faltaram no roteiro dividido? | Pelo que dependem. Fase 4: coleta com o ímã, interface de configuração, receita da Power Bank e custo do ímã. Fase 4.1: módulos e receitas deles, e os slots exclusivos de tochas, fornalha e carregador de ferramentas, com o Cubit colocando tochas e o custo delas, porque fornalha e carregador são bloqueados no Base. Fase 4.2: receita do controle |
| 723 | Como resolver o modo área e a descarga, marcados com o Cubit Remote, que ficaria depois deles? | O modo área e a descarga em baú vão para a Fase 4.2, junto do Cubit Remote, do retorno automático e do local de morte. A Fase 4.1 fica com os tiers. O modo só o alvo, que mira pelo controle (pendente 577), entra na 4.2 |
| 722 | Como dividir a Fase 4 e a Fase 5, e como resolver os modos de defesa, que dependem do tier? | O desenvolvedor dividiu as fases: Fase 4 com Power Bank, ímã, filtros, choque e modos de defesa; 4.1 com tiers, modo área e descarga em baú; 4.2 com Cubit Remote, retorno automático e local de morte; Fase 5 com antenas, Servidor e storage wireless virtual; 5.1 com carregamento de chunks; e 5.2 com os cards de Aprendizado de Máquina e a personalização do Cubit de acordo com o card. Os modos: a Fase 4 entrega o choque e o Pacífico, o único do Base, e o Contra-atacar e o Agressivo entram na 4.1, com os tiers. As perguntas 723 a 725 ajustam o resto |
| 721 | O que fazer com a pendente 425, de recomendação desatualizada? | Atualizar a recomendação para citar só o que falta, ou seja, a chave geral, o Hibernar fora do Dock e as chaves das Fases 4 e 5, e levá-la ao arquivo da Fase 4, onde a resposta vai ser usada |
| 720 | O que fazer com a ordem misturada e as perguntas na fase errada do registro? | Deixar como está, porque mover dá muito diff e pouco ganho, e a busca pelo número funciona. Daqui em diante, a ordem é decrescente, com a mais nova no topo, e a regra entra no README das perguntas |
| 719 | Os termos das ferramentas de IA dos sons (resposta 338) e o padrão da opção de luz de entidades do LambDynamicLights (resposta 461) entram no checklist da `0.1.0`? | Sim, os dois: conferir os termos antes de publicar e confirmar a opção no teste de instância |
| 718 | Como tratar, nos documentos, o que é de fases futuras e ainda não existe, como o `PowerBankItem` e os valores da Power Bank, do controle remoto e do retorno? | Marcar como planejado, com a fase em que entra, sem remover. O plano continua visível, sem parecer pronto |
| 717 | Quando entra a interface de configuração, com a chave geral e a do painel solar (perguntas 494, 495 e 500)? | Fora da `0.1.0`, na Fase 4, junto do Cubit Remote e das chaves novas, na aba Settings que já existe. A `0.1.0` sai com as chaves de hoje. Responde a pergunta 500 |
| 716 | O agente aplica as correções diretas da auditoria e acrescenta os ponteiros às respostas antigas? | Sim: as correções A1 a A27 e os ponteiros na issue da auditoria, e a A28, da regra de idioma, na issue da tradução. O relatório fica em `tmp/auditoria/2026-11-08_auditoria-pre-lancamento.md` |
| 715 | Como organizar a reta final em issues e PRs? | Três issues, cada uma com a sua branch e o seu PR: a auditoria dos documentos, com o fechamento do item 19 e as respostas sem commit; a tradução `pt_br`, com a mudança da regra 1; e o `CHANGELOG.md` com o guia de publicação em `_docs/desenvolvimento/publicacao.md` |
| 714 | Onde publicar o Cubit? | No Modrinth e no CurseForge, com a mesma versão nos dois. Responde o item 3 da `0.1.0` no checklist. A pergunta 787 deixa o CurseForge para depois da `0.1.0` |
| 713 | Em que idioma fica o `CHANGELOG.md`? | Em inglês, no formato Keep a Changelog, na raiz, porque vai para as páginas de versão das plataformas. É uma exceção à regra 25, como o `README.md` e o `LICENSE`, e é atualizado a cada versão depois da `0.1.0` |
| 712 | Como fica a regra de idioma com a tradução para o português? | O inglês continua o idioma base: IDs, comandos, config, logs e o `en_us.json`. O `pt_br.json` é gerado pelo mesmo datagen, com um teste que confere que as duas línguas têm as mesmas chaves, e o player escolhe o idioma no jogo. Muda a regra 1 do `AGENTS.md` e substitui a resposta 449 |
| 711 | Há algo a acrescentar no alinhamento da reta final: estudo do estado, auditoria dos documentos, tradução para o português, `CHANGELOG.md` e guia de publicação? | "continue": nada a acrescentar, e as ideias antes da publicação (resposta 710) são essas. Os pontos em conflito com as regras, como o `pt_br.json` contra a regra 1, vão para a rodada de decisões |
| 710 | Há ajuste no roteiro das próximas tarefas depois do PR #126? | "Seguir o plano": primeiro o PR curto que marca o item 19 do checklist e o estado no `README.md` e no `AGENTS.md`, depois os itens 3 a 8 da `0.1.0`. Logo depois, o desenvolvedor acrescentou: "antes da publicacao tenho ideias", que entram antes dos itens de publicação |
| 709 | O desenvolvedor fez o merge do PR #126? | Sim. O agente conferiu o merge (`c5b4ebf`) e a issue #125 fechada e, com a autorização, apagou a branch local e a remota |
| 708 | O jogo está fechado antes do push da #125? | Sim. Fica sem commit até o próximo PR |
| 707 | Os quatro commits da tela do Cubit podem ser feitos, e a branch segue para o teste no jogo? | "top fechamos tudo ok pode fazer os commits push e abrir a PR": autoriza os commits, o push e o PR da #125. O agente entendeu "fechamos tudo ok" como o fim do trabalho da issue, sem o detalhe dos passos do roteiro "Telas" no jogo, que o PR informa como pendente de confirmação |
| 706 | O jogo está fechado antes de compilar os textos reduzidos, na #125? | Sim |
| 705 | Quanto diminuir todos os textos das telas do Cubit e do Dock, na mesma proporção? | Fator 0,75: títulos, rótulos e valores passam de 1,0 para 0,75, e os detalhes, de 0,75 para cerca de 0,56. O desenvolvedor escolheu mesmo com o aviso de que os detalhes podem ficar ilegíveis com a escala da interface em 2. Muda a escala dos detalhes das telas, que seguia a resposta 437 |
| 704 | O jogo está fechado antes de regenerar o `en_us.json` sem as chaves antigas, na #125? | Sim |
| 703 | O agente atualiza a lista de classes do domínio no `AGENTS.md` e remove as chaves de tradução sem uso do painel antigo (`gui.cubit.lamp.on/off`, `gui.cubit.scanners.on/off` e `gui.cubit.chatter.on/off`)? | Sim, as duas: "pode atualizar o AGENTS.md e remover as chaves antigas". A remoção das chaves é avisada no PR, como pede o contrato de traduções. O desenvolvedor também autorizou os cinco commits da tela do Cubit |
| 702 | O jogo está fechado antes de rodar as GameTests das telas, na #125? | Sim |
| 701 | O jogo está fechado antes de compilar o menu e a tela do Cubit, na #125? | Sim |
| 700 | O agente segue nesta sessão com o menu e a tela do Cubit, ou numa sessão nova, a partir do prompt de continuidade? | Nesta sessão: "pode seguir aqui com o menu e a tela do Cubit" |
| 699 | O jogo está fechado antes de compilar a leitura do último tick do Cubit, na #125? | Sim |
| 698 | Como o gasto, as causas, a origem da recarga e o estado do painel do último tick chegam ao menu do Cubit, se a `RobotEntity` está no limite de 400 linhas? | Por um data attachment do NeoForge no robô, sem serializador, que não vai para o save nem para a rede. O `RobotEnergyUse` e o `RobotSolar` gravam a leitura a cada tick, e o menu a lê no servidor. Segue o caminho da resposta 696 |
| 697 | O jogo está fechado antes de compilar a parte do Dock da #125? | Sim |
| 696 | Como a tela do Dock lê a bateria, a vida, a recarga e a expressão do Cubit na base, se a `RobotEntity` está no limite de 400 linhas e a regra 42 pede interface? | Por uma capability de entidade do NeoForge: o `block/` define uma interface curta de leitura, e o registro a liga ao tipo do Cubit, com um adaptador no pacote `entity/`. Não toca na `RobotEntity` nem nos saves |
| 695 | O jogo está fechado antes do segundo `make format` e `make test` da #125? | Sim |
| 694 | O jogo está fechado antes do primeiro `make format` e `make test` da #125? | Sim |
| 693 | Os textos sem imagem propostos em `telas.md` estão aprovados, e a issue e a branch das telas podem ser criadas? | Sim: "aprovo os textos propostos, pode criar a issue e a branch". Todos os textos da seção "Textos sem imagem" de [`telas.md`](../funcionalidades/telas.md) valem como estão, inclusive o slot vencendo o solar na linha de recarga, a entrada do Dock como a média do último segundo e o raio do botão de chamar do tier Base. Responde a pergunta 692 |
| 692 | Como definir os textos que as imagens não mostram, como o painel solar no Nether, no End ou hibernando, o consumo parado, a linha `Following` sem seguir ninguém e a seção `Docked Cubit` no estado Complete? | O agente propõe cada texto em `_docs/funcionalidades/telas.md`, marcado como proposta, e o desenvolvedor aprova ou corrige na revisão do documento, antes do código. Cada resposta é registrada |
| 691 | Como testar as telas? | JUnit no domínio, na escolha do estado do topo, no formato dos números e na divisão dos valores acima de 16 bits. GameTests de só o dono abrir, Shift + clique no Dock, a tela fechar com o Cubit desligado, o botão de chamar e as chaves, sem duplicar itens. Roteiro manual comparando cada tela com as imagens |
| 690 | Como desenhar a torre do Dock e os retratos do Cubit nas telas? | Em sprites 2D, fiéis às imagens. O retrato do Cubit monta a moldura com a tela do rosto, reaproveitando as texturas de expressão que já existem, com a piscada, e a cor do colar do tier. A torre do Dock vira um sprite próprio em `textures/gui/`, com a luz do estado |
| 689 | O que a linha `Owner` da tela do Dock mostra? | O nome do dono, que o servidor busca pelo UUID no cache de perfis do jogo e envia ao abrir a tela. Sem nome no cache, mostra `Unknown` e registra no log, sem derrubar o jogo (regra 17) |
| 688 | A seção `Scanner filter` da aba Settings é só leitura ou tem um botão para limpar? | Só leitura, como na imagem. O filtro continua mudando só pelo clique com o minério na mão |
| 687 | Em que formato as telas mostram os números de energia? | O das imagens: estoques e baterias abreviados com `k`, e com `M` acima de um milhão, para os tiers futuros, e taxas por tick inteiras com separador de milhar, como `+2,400 FE/t`. O Jade continua como está |
| 686 | Que estado o topo da tela do Cubit mostra com ele ativo e sem seguir ninguém? | `● Active`, em cinza, a mesma palavra do Jade. `Critical battery` continua valendo por cima de `Following` e de `Active` |
| 685 | Que texto cada estado do Dock mostra no topo da tela? | Os nomes das imagens: `OFF` e `NO_ENERGY` mostram `● No power`, em laranja; `IDLE`, `● Idle`, em azul; `CHARGING`, `● Charging`, em verde; e `COMPLETE`, `● Complete`, em verde. O Jade passa a usar os mesmos textos. O enum e o nome de contrato `status` (resposta 313) não mudam |
| 684 | O que a tela do Cubit faz se ele desligar com ela aberta? | Fecha na hora: o menu deixa de valer com o Cubit inativo, e o item no cursor volta ao inventário do player, sem perder nem duplicar. Detalha a resposta 677 |
| 683 | Como os botões novos das telas chegam ao servidor? | Pelo clique de botão de menu do próprio jogo (`clickMenuButton`), validado no servidor pelo menu, sem pacote próprio do cliente para o servidor. Vale para o botão de chamar do Dock e para as chaves da aba Settings. Responde a pergunta 680 e mantém a resposta 467 |
| 682 | Há algo a acrescentar no alinhamento das telas próprias (regra 50), antes da leitura e da rodada de decisões? | Não: "vamos trabalhar conforme o prompt de continuidade". O agente resumiu o que entendeu de `tmp/prompt/2026-11-07_22h14_continuidade_fase-3-telas.md`, e o desenvolvedor respondeu "pode seguir" para a leitura obrigatória e a rodada das 9 decisões em aberto |
| 681 | E a barra de bateria vermelha com o Cubit desligado? | "Vamos ajustar para ficar como imagem": as barras seguem os mockups. O agente entendeu que o vermelho continua só na bateria crítica (respostas 415 e 416), como nas imagens, em que o Cubit desligado tem a barra vazia |
| 680 | As telas criam pacotes próprios do cliente para o servidor? | "Depois falamos melhor sobre." Fica em aberto. Hoje as chaves usam `clickMenuButton`, sem pacote próprio (resposta 467) |
| 679 | O botão de chamar fica desabilitado com o Dock sem energia, como no mockup? | Não: mantém o comportamento atual. A chamada funciona, e o Cubit entra e hiberna (resposta 352), então o botão fica habilitado |
| 678 | Como a tela nova do Induction Dock abre? | Do mesmo jeito de hoje, com Shift + clique. O clique continua chamando ou liberando o Cubit (respostas 295 e 308) |
| 677 | O Cubit desligado dá acesso ao inventário, como no mockup "Off"? | Não: "o Cubit desligado não pode ser acesso". Vale a tabela de estados do domínio, e a redstone continua entrando pelo clique com ela na mão (resposta 45). O que a tela faz se o Cubit desligar com ela aberta fica para a rodada |
| 676 | Que estilo as telas próprias usam? | O das imagens de conceito, `tmp/conceito/cubit-v0.1.0.png` e `dock-v0.1.0.png`, e não o do Minecraft: "não quero usar estilo minecraft quero da imagem". O desenvolvedor pediu fidelidade máxima ao conceito, com as respostas 676 a 681 tendo prioridade sobre ele. Detalha a resposta 371 |
| 675 | O que entra no prompt da próxima sessão, sobre as telas? | O desenvolvedor criou dois mockups e um prompt em `tmp/conceito/` e pediu a avaliação do agente, que apontou contradições com os documentos. Com as respostas 676 a 681, o agente escreve o prompt de continuidade das telas |
| 674 | O jogo está fechado antes do push da #123? | Sim |
| 673 | O que o desenvolvedor pediu depois do PR #122? | Um `.md` com todas as funcionalidades do Cubit, do início ao último commit, que o agente criou em `_docs/funcionalidades-entregues.md`, e o commit dos arquivos sem commit, com push e PR. O agente entendeu como autorização desses commits, do push e do PR, numa issue e numa branch próprias (#123), porque o commit direto na `develop` é proibido. A marcação do item 18, Jade, entra junto. A pergunta sobre apagar a branch da #121 e a do roteiro do item 19 ficaram sem resposta |
| 672 | O jogo está fechado antes do push da #121? | Sim. O `pre-push` rodou, e o PR #122 foi aberto. Fica sem commit até o próximo PR |
| 671 | O agente pode fazer os 3 commits do plano do Jade? | Sim, os três, na branch `feature/121-jade`, sem push e sem PR. Vale só para esses commits |
| 670 | Como foi o teste no jogo do Jade? | "Perfeito, teste ok", executado pelo desenvolvedor, sem detalhar cada passo do roteiro. O agente não entendeu a resposta como autorização de commits e apresenta o plano |
| 669 | O jogo está fechado antes do `make data` e dos gates do Jade? | Sim |
| 668 | O jogo está fechado antes de baixar o Jade e compilar? | Sim. O SHA-512 do jar baixado do Modrinth bateu com o do `jade.pw.toml` do Iron Meridian |
| 667 | Onde fica o plugin do Jade? | Num pacote novo, `io.github.marcelofabianov.cubit.compat.jade`, só com as classes que importam o Jade, seguindo a recomendação. O Jade carrega o plugin pela anotação dele, então nada do mod o referencia. Uma regra de ArchUnit garante que só esse pacote importa o Jade, e a estrutura de pacotes do `AGENTS.md` ganha a linha `compat/` |
| 666 | Em que pontos ficam os testes do Jade? | JUnit: o texto da bateria em porcentagem e FE. GameTest: o mod abre e o Cubit funciona sem o Jade. Roteiro manual: ver o tooltip do Cubit e do Dock em cada estado. Segue a recomendação |
| 665 | Em quais execuções o Jade carrega? | Só no `make client`, seguindo a recomendação. O código compila contra o Jade, e o jar não o leva. As GameTests e o `make server` rodam sem ele, o que prova que o mod abre sem o Jade (regra 24) |
| 664 | De qual repositório Maven o Jade entra no build? | Do Maven do Modrinth, `api.modrinth.com/maven`, limitado ao grupo `maven.modrinth`, seguindo a recomendação. O agente conferiu na API do Modrinth que a versão `eYz2YBGT` é a `15.10.6+neoforge`, com o mesmo SHA-512 do `jade.pw.toml` do Iron Meridian |
| 663 | Qual versão do Jade entra no build? | A do Iron Meridian, conferida pelo agente no GitHub do pack, só lendo o arquivo pelo `gh`, sem baixar nada: `Jade-1.21.1-NeoForge-15.10.6.jar` |
| 662 | O Induction Dock aparece no Jade? | Sim, com o estoque de FE e o estado da tela: desligada, ociosa, carregando, completa ou sem energia, seguindo a recomendação. A pergunta 685 junta desligada e sem energia em `No power` |
| 661 | Como o Jade mostra a bateria exata, que só existe no servidor? | Pelo provedor de dados de servidor do Jade, que envia a carga só enquanto alguém mira no Cubit, sem pacote próprio do mod, seguindo a recomendação |
| 660 | O que o tooltip do Jade mostra ao mirar no Cubit? | O tier, a bateria, em porcentagem e FE, e o estado: ativo, desligado, sem bateria ou hibernando. O nome e a vida o Jade já mostra sozinho |
| 659 | Há algo a acrescentar sobre o Jade? | Não: "Nada a acrescentar", depois do alinhamento: nome, tier, bateria e estado do Cubit, sem o "modo atual", que é da Fase 4 (regra 43), e o Jade opcional e isolado. O agente entendeu como autorização para seguir à rodada de decisões, não para a issue, a branch ou commits |
| 658 | O desenvolvedor quer algum ajuste no roteiro, depois do PR #120? | Não: "seguir assim". Segue o Jade (item 18), depois as telas próprias (item 19, que depende da resposta 500) e a `0.1.0`. As branches da #119 foram apagadas, com autorização |
| 657 | Onde entram a marcação do item 16, o estado do projeto e as respostas sem commit? | No PR do Jade, num commit próprio no início da branch, seguindo a recomendação, como os itens 7, 8, 12 e 20 entraram na #117 |
| 656 | O jogo está fechado antes de apagar a branch remota da #119? | Sim. O `pre-push` rodou ao apagar a remota |
| 655 | O jogo está fechado antes do push da #119? | Sim. O `pre-push` rodou, e o PR #120 foi aberto. Fica sem commit até o próximo PR |
| 654 | Quando o estado do projeto no `README.md` e no `AGENTS.md` é atualizado com as cores? | No PR seguinte, junto da marcação do item 16 do checklist, depois do merge da #119. Fecha a resposta 653 |
| 653 | O agente prepara o commit 4, com o estado do projeto no `README.md` e no `AGENTS.md`? | "Nenhuma das opções", sem explicação. O agente não mexe nos dois arquivos e pergunta o que o desenvolvedor prefere |
| 652 | O agente pode fazer os 3 commits do plano das cores? | Sim, os três, na branch `feature/119-scanner-colors`, sem push e sem PR. Vale só para esses commits |
| 651 | Como foi o teste no jogo das cores das antenas, do halo do corpo e da seta do mob? | "Top, concluído o teste, tudo ok", executado pelo desenvolvedor, sem detalhar cada passo. Os tamanhos da seta e do halo ficam como estão. O agente não entendeu a resposta como autorização de commits e apresenta o plano |
| 650 | O jogo está fechado antes dos gates da seta? | Sim. Os gates podem rodar |
| 649 | Como o marcador do mob é desenhado? | Preenchido e translúcido, seguindo a recomendação: vermelho claro, como o das caixas, com cerca de 50% de opacidade, o pulso suave e um leve balanço para cima e para baixo |
| 648 | Qual forma tem o marcador acima da cabeça do mob? | Uma seta para baixo, ▼, seguindo a recomendação, porque aponta para o mob e se entende de longe qual é ele, mesmo num grupo |
| 647 | Em que entrega entra o marcador do mob? | Na issue #119, num commit próprio, junto das cores, seguindo a recomendação, porque os dois são sinais visuais do scanner de mobs |
| 646 | Como o scanner identifica o mob hostil, no lugar da caixa de linhas retas? | Ideia do desenvolvedor, que viu a caixa no jogo: "será que conseguimos fazer identificação diferente?". Fica um marcador acima da cabeça do mob, vermelho, virado para a câmera e visível através das paredes, só no cliente do dono, sem mixin. A recomendação era a silhueta vermelha, pelo `requestOutlineEffect` da NeoForge, com prova técnica. Muda as respostas 557 e 613 no desenho do mob, e a caixa de minério continua. A forma e o desenho vão para a rodada |
| 645 | O jogo está fechado antes dos gates das cores? | Sim. O agente já tinha compilado uma vez sem perguntar, depois de conferir que nenhum jogo estava aberto, e avisou o desenvolvedor do erro |
| 644 | Como entra a entrega das cores? | Pelo fluxo normal, seguindo a recomendação: issue nova no milestone da Fase 3, branch `feature/{issue}-scanner-colors` a partir da `develop` e um PR que fecha o item 16 |
| 643 | Em que pontos ficam os testes das cores? | JUnit: qual cor vence, quanto dura o azul e quando volta ao estado anterior. GameTests: o dado sincronizado com mob, com minério, com os dois, ao levar dano e com a chave desligada. Roteiro manual: ver as cores de dia, de noite e com a lanterna apagada. Segue a recomendação |
| 642 | Qual tom as cores das antenas e do halo usam? | O mesmo das caixas, azul claro e vermelho claro, seguindo a recomendação, para cada cor ter um só significado (resposta 555). As pontas ficam sólidas, e os halos, translúcidos |
| 641 | Como é o halo suave em volta do corpo? | Um brilho atrás do corpo, seguindo a recomendação: um disco translúcido e sem borda, sempre virado para a câmera, maior que o Cubit e centrado nele, como o halo das antenas, sem mixin. Detalha a resposta 558 |
| 640 | Minério só do filtro deixa as antenas e o halo azuis? | Sim, por cerca de 3 s, seguindo a recomendação, o mesmo tempo da surpresa de um raro, em `surprised_expression_ticks`, sem chave nova. O azul quer dizer minério achado, como na caixa. Muda a resposta 556, que ligava o azul à surpresa |
| 639 | Com um mob no raio e um minério achado ao mesmo tempo, qual cor vence? | O vermelho, seguindo a recomendação, porque o perigo vem primeiro, como na expressão. O azul volta se ainda houver tempo dele |
| 638 | Como o cliente sabe quando acender as cores dos scanners? | Por um dado sincronizado novo, fora do save, com nenhum, mob ou minério, decidido no servidor pelo scanner, seguindo a recomendação. O vermelho continua mesmo se o Cubit levar dano com o mob no raio |
| 637 | Há algo a acrescentar sobre a cor das antenas e o halo dos scanners? | Não: "Nada a acrescentar". O agente entendeu como autorização para seguir à rodada de decisões, não para a issue, a branch ou commits |
| 636 | O desenvolvedor quer algum ajuste no roteiro, depois do PR #118? | Não: "seguir assim". Segue a cor das antenas e o halo dos scanners (resposta 558), que fecha o item 16, depois o Jade, as telas próprias e a `0.1.0`. As branches da #117 foram apagadas, com autorização |
| 635 | O jogo está fechado antes de apagar a branch remota? | Sim. O `pre-push` rodou ao apagar a remota |
| 634 | O jogo está fechado antes do push? | Sim. O `pre-push` rodou, e o PR #118 foi aberto. Fica sem commit até o próximo PR |
| 633 | O agente segue com o commit 7 e prepara o rascunho do PR? | "Sim". O agente entendeu como autorização do commit 7, com as respostas 631 a 633, e do rascunho em `tmp/pr/`, não do push nem da abertura do PR |
| 632 | O jogo está fechado? | Sim. Os gates podem rodar no estado final da branch |
| 631 | O agente pode fazer o commit 6, com o status do `README.md` e do `AGENTS.md`? | Sim, com o diff apresentado, sem push e sem PR. Vale só para esse commit |
| 630 | A chave das falas da redstone fica `chatter_redstone_repeat_ticks`, com 1.200 ticks, em `[general]`? | Sim, seguindo a recomendação, no padrão de `chatter_animal_repeat_ticks` |
| 629 | O status do `README.md` e do `AGENTS.md` muda nesta entrega? | Sim, num sexto commit, seguindo a recomendação: o status do `README.md` e, no `AGENTS.md`, "Já entrou", "Próximos" e a estrutura de pacotes. O agente mostra o diff antes do commit |
| 628 | O agente pode fazer os commits do plano? | Sim, os cinco, na branch `feature/117-scanner-marks`, sem push e sem PR. Vale só para esses commits |
| 627 | Como foi o teste no jogo das caixas, do raio dos mobs e da fala da redstone? | "Podemos avançar, está ok", executado pelo desenvolvedor, sem detalhar cada passo do roteiro. O agente não entendeu a resposta como autorização de commits e apresenta o plano |
| 626 | O jogo está fechado? | Sim. O `make data` e os gates podem rodar |
| 625 | O que o desenvolvedor pediu no meio da mudança do raio? | "Também faça a frase que combinamos" e "atualize os documentos .md". O agente entendeu como implementar a fala da redstone das respostas 619 a 621 e atualizar os `.md` dela e das caixas: `falas.md`, `tiers.md`, `config.md`, o roteiro manual, o README do domínio e `decisoes.md`. A chave nova, `chatter_redstone_repeat_ticks` (1.200), segue o padrão das falas de animais e ainda pode mudar antes do commit |
| 624 | O jogo está fechado? | Sim. Os gates podem rodar |
| 623 | O que fazer com os configs locais, que guardam o raio antigo do scanner de mobs? | Apagar os dois de novo, para o jogo recriá-los com os padrões novos. A recomendação era trocar só a linha do raio. Vale só para esta vez, como a resposta 611 |
| 622 | Como fica o raio do scanner de mobs hostis por tier? | 8, 14, 20 e 25 blocos, a pedido do desenvolvedor, depois de ver a tabela de tiers. O agente entendeu que o raio dos minérios continua 5, 8, 12 e 20, porque a mensagem só trata dos mobs, então os dois raios deixam de ser iguais. Muda as respostas 605 e 609 só nesse ponto. A esfera e o custo igual continuam. No config, o Base passa a 8 em `mob_scan_radius_blocks` |
| 621 | Em que entrega entra a fala da redstone? | Na issue #117, num commit próprio, como as falas entraram na #115 (resposta 580). A recomendação era uma issue própria, depois da #117 |
| 620 | Quais frases da redstone ficam? | As três: `I love redstone!`, `Redstone! My favorite snack!` e `Redstone runs in my circuits!`, com o prefixo `Cubit: `, e o Cubit sorteia uma |
| 619 | Quando o Cubit diz que ama redstone? | Quando o dono minera um minério de redstone, como uma fala, seguindo a recomendação: bipe, a chave `Chatter` e 1 minuto entre falas, sem brigar com o `Cubit: REDSTONE!!!` do scanner. O agente entendeu que vale o mesmo das falas de animais: só do Cubit ativo que segue e com a expressão calma, sem a chance de 1 em 3, que não foi citada |
| 618 | Entra uma fala do Cubit sobre a redstone? | Sim, ideia do desenvolvedor: "quando cubit ver o player um minerio de redstone ele fala para o player que ele ama redstone". O gatilho, a frase em inglês e a entrega vão para a rodada, porque o scanner já mostra `Cubit: REDSTONE!!!` para esse minério raro |
| 617 | O jogo está fechado? | Sim. Os gates podem rodar |
| 616 | Como ficou a caixa azul no jogo? | "Poderia deixar pouco mais discreto a marcacao azul". O agente entendeu como baixar só a opacidade do azul, de cerca de 50% para cerca de 30%, sem mudar a linha, o pulso nem o vermelho dos mobs. Detalha a resposta 600. A mensagem não diz como foram os outros passos do roteiro |
| 615 | O item 16 do checklist fecha com as caixas ou depois das cores? | Depois das cores das antenas e do halo, seguindo a recomendação, porque as cores também são sinais dos scanners. Muda a resposta 559 nesse ponto |
| 614 | A caixa pisca ou fica fixa? | Pulso suave, que clareia e escurece devagar, como o do painel solar. A recomendação era fixa. Detalha a resposta 600 |
| 613 | Como o servidor avisa o cliente do dono sobre as caixas? | Por posições e IDs, seguindo a recomendação, num pacote só do servidor para o cliente do dono. Minério: as posições novas da varredura, e o cliente apaga a caixa depois de ~10 s ou quando o bloco deixa de ser minério. Mob: os IDs a cada varredura, e o cliente segue o mob e apaga a caixa quando ele sai do raio do Cubit |
| 612 | O jogo está fechado? | Sim. Os gates podem rodar |
| 611 | O que fazer com os configs locais, que guardam os custos antigos dos scanners? | Apagar os dois, `run/config/cubit-common.toml` e `run/gametest/config/cubit-common.toml`, para o jogo recriá-los com os padrões novos. Os valores editados à mão se perdem. A recomendação era trocar só os dois valores. Vale só para esta vez |
| 610 | Quanto cada scanner gasta por varredura? | "Vamos manter 50 para base e os demais 100, 150 e 300". O agente entendeu como FE por varredura, igual nos dois scanners: Base 50, Advanced 100, Elite 150 e Ultimate 300. No Base, mobs gastam 50 FE a cada 10 s e minérios, 50 FE a cada 35 s, somando cerca de 0,32 FE/t. A recomendação era manter 100 e 350. Muda a resposta 553, e só o Base entra no config agora |
| 609 | O código faz o que foi discutido, e como fica o gasto dos dois scanners? | O desenvolvedor pediu a revisão do código contra as decisões e escreveu "Ambos scanner de mobs deve gastar a mesma energia e ter o mesmo raio". O agente entendeu "ambos" como os dois scanners, de mobs e de minérios. O raio igual já é a resposta 605. Hoje os dois gastam 0,5 FE/t cada, mas com valores diferentes por varredura: 100 FE a cada 10 s e 350 FE a cada 35 s. Qual "mesma energia" vale vai para a rodada |
| 608 | Com vários minérios novos numa varredura, quais ganham caixa? | Todos os novos, seguindo a recomendação, por cerca de 10 s. Mantém a resposta 562 e fecha a 598 |
| 607 | Onde entram a esfera e os raios novos? | Na issue #117, num commit próprio, com JUnit e GameTests, seguindo a recomendação |
| 606 | O scanner de mobs também vira uma esfera? | Sim, seguindo a recomendação: só conta o mob a até o raio em linha reta, medido a partir do Cubit. Amplia a resposta 604 |
| 605 | O raio do scanner de mobs muda junto? | Sim, para 5, 8, 12 e 20, igual aos minérios, seguindo a recomendação. Muda `tiers.md`, e o Base continua em 5 |
| 604 | A área do scanner de minérios continua um cubo? | Não: "deve ser uma esfera em volta do cubit sendo ele o centro para ser possivel pegar minerios 5 para frente, 5 atras e as demais regioes". Só conta o bloco a até o raio em linha reta, a partir do bloco do Cubit. A recomendação era a esfera. O agente entendeu a resposta para os minérios, que eram o assunto da pergunta, e leva os mobs à rodada |
| 603 | O que o raio 5 do scanner de minérios significa? | 5 blocos para cada lado do Cubit, seguindo a recomendação. Os outros tiers ficam 8, 12 e 20 para cada lado. Detalha a resposta 602 |
| 602 | Como fica o raio do scanner de minérios por tier? | O desenvolvedor pediu 5, 8, 12 e 20 blocos, em vez de 5, 10, 16 e 24, depois de entender que o Base lia 11×11×11. O agente explicou que raio 5 são 5 blocos para cada lado, o que dá 11 de largura. O que o "5" significa, se a forma continua um cubo e se os mobs mudam junto vão para a rodada. Muda a tabela de sensores de `tiers.md`, e só o Base entra no config agora |
| 601 | O jogo está fechado? | Sim. A prova pode compilar |
| 600 | Como deixar o azul da caixa mais sutil? | Mais claro e translúcido, seguindo a recomendação: azul claro com cerca de 50% de opacidade e a linha de 2 px, com o ajuste fino no teste no jogo. Detalha a resposta 594 |
| 599 | Depois de quanto tempo a caixa de um minério some, se ele não for minerado antes? | Cerca de 10 s, em `scan_mark_ticks` (200), seguindo a recomendação. Confirma a resposta 556 e fecha o que a 597 deixou em aberto |
| 598 | Com vários minérios novos numa varredura, quais ganham caixa? | Sem escolha: o desenvolvedor pediu para ver a tabela de tiers do scanner, para revisá-la. A pergunta volta depois da revisão |
| 597 | Um minério novo achado dentro do intervalo de 25 s do mesmo nome, sem texto, ganha caixa? | "Sim pode marcar apenas os minerios encontrados dentro do raio de blocos do cubit quando é disparado o scanner. depois o internavo deve desaparecer". O agente entendeu que todo minério novo daquela varredura ganha caixa, mesmo com o texto esperando, e que a caixa some depois de um tempo. A recomendação era não marcar sem texto. Ficou em aberto qual intervalo: os ~10 s de `scan_mark_ticks` (resposta 556) ou outro |
| 596 | Minério só do filtro, com texto e sem surpresa, ganha caixa? | Sim, com o texto, seguindo a recomendação. Mantém a resposta 562 nesse ponto |
| 595 | Com vários minérios novos numa varredura, quais ganham caixa? | Sem escolha: o desenvolvedor perguntou se os minérios encontrados estão dentro do raio do Cubit quando o scanner dispara e pediu para conferir o código. O agente conferiu: a varredura lê um cubo de 11×11×11 em volta do bloco do Cubit no momento em que dispara, com raio 5, e só guarda os raros e os do filtro. A pergunta volta para a rodada |
| 594 | Como foi a prova técnica das caixas? | "Não gostei do resultado": o desenvolvedor quer um azul mais sutil e viu "todos os minérios por onde eu passo" marcados. O agente explicou que a prova marcava de propósito todo minério em volta do player, sem seguir o Cubit. A mensagem não diz se a caixa apareceu através dos blocos em cada modo gráfico, mas o desenvolvedor viu as caixas no jogo. Repete o pedido da resposta 593 |
| 593 | Quais minérios ganham caixa? | A pedido do desenvolvedor: só os que o Cubit identifica dentro do raio, só quando ele faz a animação e o texto, e só aquele que encontrou. O agente entendeu que a caixa de minério aparece junto do aviso, não a cada varredura, e que a prova, que marca todo minério em volta do player, não mostra o comportamento final. A mensagem não trata dos mobs, então continuam as respostas 556 e 557. Vários juntos, minério só do filtro e aviso dentro do intervalo de 25 s seguem para a rodada, porque podem mudar a resposta 562. A mensagem também não informa o resultado do roteiro da prova. As perguntas 597 e 608 ampliam: ganham caixa todos os minérios novos da varredura |
| 592 | O jogo está aberto? | Não. Os gates podem rodar |
| 591 | Há algo a acrescentar sobre as caixas dos scanners, como um efeito ao aparecer ou uma ideia visual? | Não: "Nada a acrescentar". O agente entendeu como autorização para seguir à prova técnica e à rodada de decisões, não para commits |
| 590 | O que vem depois do PR #116? | As caixas antes das cores, seguindo a recomendação, porque hoje o aviso não diz onde está o que foi achado. Muda a ordem da pergunta 559. O desenvolvedor autorizou apagar a branch da #115 e disse "pode fazer as marcações", que o agente entendeu como as do checklist, sem autorizar o commit |
| 589 | `Cubit: COAL!!!`, `Cubit: Careful!` e as repetições de `Cubit: Critical battery` também tocam o bipe? | Não: "deixar o bipe" como está, seguindo a recomendação. Na mesma mensagem, ele autorizou os commits do plano e a atualização do `AGENTS.md` e do `README.md`. O push e o PR não foram autorizados |
| 588 | Como foi o teste no jogo dos scanners, da volta com o dono, da descida, dos sons e das falas? | "Teste fechou ok", executado pelo desenvolvedor, sem detalhar cada passo. O agente não entendeu a resposta como autorização de commits. Ficaram sem resposta se o `AGENTS.md` e o `README.md` mudam neste PR e se `Cubit: COAL!!!`, `Cubit: Careful!` e as repetições de `Cubit: Critical battery` tocam o bipe |
| 587 | As falas ao abater animais ficam? | Sim: "gostei de todas, vamos usar todas as frases". São 26 frases, em `conceito/falas.md`. O agente entendeu que vale também o proposto: só quando o próprio dono mata, com chance de 1 em 3 e 1 minuto entre falas, e sem frase para animal domesticado, porque a pergunta sobre esses números e a trava não teve resposta separada |
| 586 | As falas de bioma ficam? | Sim: "gostei, vamos usar elas". São 48 frases em 12 grupos, em `conceito/falas.md`. O agente entendeu que vale também o proposto: falar ao mudar de grupo, intervalo próprio de 2 minutos, bioma com neve usando as falas de frio, e nada ao entrar no mundo. Um bioma sem grupo, como um rio, não conta como mudança |
| 585 | O Cubit fala quando o dono se afasta correndo? | Sim, ideia do desenvolvedor, e todas as 16 frases propostas ficam, em `conceito/falas.md`. Dispara ao passar de 16 blocos, antes do teleporte de 20, e só volta a valer depois de ele chegar perto. O intervalo é de 1 minuto, separado dos 5 minutos das falas do tempo. Segue o bipe, a chave `Chatter` e a expressão calma |
| 584 | As falas têm som? | Sim: "quando ele fala deve ter o som padrão". Toda fala do tempo e todo parabéns tocam uma vez o bipe padrão, mesmo sem trocar de expressão. O agente entendeu como as falas de `conceito/falas.md`, então os avisos com regra própria, como os dos scanners, não mudaram |
| 583 | Quando a situação "dia lindo" dispara? | Cerca de 3 minutos depois do amanhecer, sem chuva, ou quando a chuva para de dia, com céu visível. O intervalo de 5 minutos continua valendo |
| 582 | Como o player desliga as falas? | Com o botão `Chatter`, no painel lateral, como `Lamp` e `Scanners`. Começa ligado e fica guardado no item. Vale para as falas do tempo e para os parabéns por conquista. As perguntas 683 e 703 levam a chave para a aba Settings da tela do Cubit |
| 581 | Qual é o intervalo mínimo entre duas falas sobre o tempo? | 5 minutos, numa chave do config. Os avisos dos scanners e os parabéns por conquista não entram nele |
| 580 | Como organizar as falas, se a #115 ainda não tem commit? | Tudo na branch da #115, no mesmo PR, em commits separados. A recomendação era commits locais e uma branch própria em cima. Muda a previsão de uma issue própria, da pergunta 572 |
| 579 | Quando as falas são feitas? | Agora, antes de continuar o que falta dos scanners: "sobre as frases já pode fazer agora e depois voltamos para continuar o scanner o que falta". Muda a pergunta 576 e a previsão de vir depois das caixas e das cores |
| 576 | Quando decidir as regras de disparo das falas? | Ao iniciar o trabalho das falas: "coloque para quando iniciarmos o trabalho". O agente entendeu como acrescentar o item 20 ao checklist da Fase 3, com as regras de `conceito/falas.md` ainda como proposta |
| 575 | O Cubit cita o nome da conquista, e todas as frases de parabéns ficam no arquivo? | Todas as frases do quadro ficam, em `conceito/falas.md`, e o Cubit não cita o nome da conquista: "não precisa". A recomendação era incluir o nome como mais uma opção do sorteio. Fecha o que a pergunta 574 deixou em aberto nesse ponto |
| 574 | As frases de parabéns por uma conquista ficam? | Sim: "ficaram ótimas". São 8 para conquista normal e 6 para desafio, em `conceito/falas.md`, e o agente entendeu como permissão de acrescentá-las ao arquivo. A resposta não diz se o Cubit também cita o nome da conquista, nem fecha as regras de disparo, que continuam como proposta |
| 573 | Quais das frases adicionais do Cubit ficam? | Todas: 36 novas, além das 12 da pergunta 572. O desenvolvedor pediu um arquivo com a frase em inglês, em português e o gatilho, que é `conceito/falas.md`. Os gatilhos continuam como proposta |
| 572 | As falas do Cubit sobre o tempo agradam? | Sim: "gostei das sugestões", e as frases propostas estão aprovadas: `The sun is coming up!`, `Good morning!`, `What a beautiful day!`, `Nice weather today!`, `It's getting dark...`, `The sun is going down.`, `Looks like rain.`, `My circuits don't like water.`, `What a storm!`, `That thunder is loud!`, `Brrr, it's cold!` e `My battery hates this cold.` Ele pediu mais frases, para um grupo maior. O agente entendeu como aprovação das frases e da ideia, não das regras de disparo, que ainda vão para uma rodada, nem como início do trabalho, previsto para uma issue própria, depois das caixas e das cores |
| 571 | Qual versão mais alta dos sons de alerta fica? | "Ótimo", depois da recomendação de minério +24 dB e mob +12 dB, então o agente entendeu como aprovação dela, sem citar os arquivos, e não ficou claro se ele ouviu todas as versões. `rare_ore_alert.ogg` e `hostile_mob_alert.ogg` foram gerados a partir dos originais de `tmp/wav/`, com um limitador suave nos picos. Ainda falta confirmar no jogo |
| 570 | Dá para deixar os sons do scanner mais altos? | Sim, a pedido do desenvolvedor, que depois pediu "um pouco mais" sobre as primeiras versões. O jogo limita o volume a 100%, então muda o próprio arquivo, sem mudar o alcance nem o `sounds.json`. O agente entendeu "som do scanner" como os dois alertas |
| 569 | De que altura o Cubit desce? | De até 5 blocos, a pedido do desenvolvedor, em vez dos 3 do padrão do jogo, numa chave do config. Continua sem levar dano de queda e subindo só 1 bloco. O agente entendeu "descer blocos de até 5 níveis" como a rota poder descer de uma altura de até 5 blocos, e colocou na issue #115, seguindo a resposta 568, por ser outra correção de movimento vista no mesmo teste |
| 568 | Em que entrega entra a volta do Cubit com o dono que renasce? | Dentro da issue #115, a pedido do desenvolvedor. A recomendação era uma issue própria, depois dela |
| 567 | Quando o dono morre e renasce longe, o que faz o Cubit ativo que segue? | Volta com ele, em qualquer dimensão, se ainda estiver carregado ao renascer. Sem forçar chunk, conforme a regra 22, um robô já descarregado continua onde está. Passar por um portal continua sem levá-lo. O desenvolvedor viu no jogo que ele ficava para trás. É uma exceção à regra de `movimento.md` de teleportar só na mesma dimensão |
| 566 | O que fazer com `Enemy nearby`, já que todo mob tem nome no jogo? | Sai dos textos: não entra no código nem na tradução, porque nunca apareceria. `Ore found` continua para um minério que só tem a tag `c:ores`. Muda as perguntas 49 e 546 nesse texto. A pergunta 734 troca o nome do minério pelo nome traduzido do bloco |
| 565 | Como o dono sabe o que aconteceu ao clicar no Cubit com um minério na mão? | Por um texto e o bipe padrão: `Cubit: Looking for DIAMOND` ao definir e `Cubit: Filter cleared` ao limpar, em qualquer estado e sem o intervalo de 25 s, porque responde a um clique. Detalha a pergunta 548 |
| 564 | Em que pontos ficam os testes da detecção, o PR 1 dos scanners? | JUnit: quando varre, custo, filtro, raros e prioridade, novidade e repetição por nome. GameTests: achar minério e mob, chave, filtro por clique, quem não é dono, chunk não carregado, recolher, morrer, consertar e save antigo. Roteiro manual: ver os textos, ouvir os alertas e ver as expressões |
| 563 | O que acontece com uma varredura se a bateria tiver menos que o custo dela? | Gasta o que resta, como os outros gastos: varre, consome até zerar e desliga, se chegar a 0 FE |
| 562 | Quais sinais vale um minério que só está no filtro, e o que acontece com vários juntos? | Minério só do filtro, como carvão: `Cubit: COAL!!!` e caixa, sem som e sem surpresa, conforme a pergunta 67. Minério raro: todos os sinais, mesmo sem estar no filtro. Com vários juntos, todos ganham caixa, e o texto cita o mais raro |
| 561 | O que faz o scanner avisar de novo sobre algo que já viu? | Só o que é novo avisa: o mesmo mob ou bloco não avisa de novo enquanto continuar no raio. Se sair do raio e voltar, avisa como novo. A lembrança não entra no save, então ao carregar o mundo ele pode avisar uma vez |
| 560 | Os nomes e padrões do contrato dos scanners ficam como propostos? | Sim. Em `[general_robot.base]`: `ore_scan_radius_blocks` (5), `mob_scan_radius_blocks` (5), `ore_scan_interval_ticks` (700), `mob_scan_interval_ticks` (200), `ore_scan_cost` (350), `mob_scan_cost` (100) e `ore_filter_slots` (1). Em `[general]`: `rare_ore_tags`, `surprised_expression_ticks` (60), `scan_mark_ticks` (200) e `scan_blocks_per_tick` (2000). No save: `ScannersEnabled` e `OreFilter` na entidade, e `scanners_enabled` e `ore_filter`, opcionais, em `cubit:robot_state`. No jogo: `Scanners`. As perguntas 610 e 622 mudam os custos para 50 FE e o raio dos mobs para 8 blocos |
| 559 | Em que entregas o trabalho dos scanners entra? | Em três PRs. 1: varreduras, filtro, chave, textos, sons e expressões, que fecha os itens 7, 8 e 12 do checklist. 2: cores das antenas e halo. 3: prova técnica do desenho através de blocos e caixas, que fecha o item 16 |
| 558 | Como ficam as antenas e o halo durante um alerta? | As pontas e os halos das antenas acendem em vermelho ou azul, inclusive de dia e com a lanterna apagada, e depois voltam ao estado anterior. Um halo suave em volta do corpo acompanha a cor. É só visual, sem custo e sem mudar o nível da luz, e todos os players por perto veem. Detalha a pergunta 542 e não muda a 478 |
| 557 | Como desenhar o contorno do mob? | Com uma caixa de linhas, a mesma do minério, só no cliente do dono, sem mixin e sem mexer no mob ou no time dele. Detalha a pergunta 542 |
| 556 | Por quanto tempo os sinais de um minério aparecem? | Surpresa, antenas e halo azuis por cerca de 3 s. A caixa fica cerca de 10 s ou até o minério ser minerado. Os dois tempos ficam no config. Os sinais de um mob duram enquanto ele estiver no raio |
| 555 | Qual é a cor da caixa que marca o que o Cubit achou? | Vermelha para mob e azul para minério, as mesmas cores das antenas e do halo, para cada cor ter um só significado. Muda a cor do contorno do mob sugerida na pergunta 542 |
| 554 | Em quais estados os scanners funcionam, e quando o dono pode definir o filtro? | Só o Cubit ativo que segue varre e gasta. Desligado ou hibernando, não varre, mas o dono ainda pode definir o filtro com o clique do minério, como ao pintar (pergunta 510). Detalha a pergunta 548 |
| 553 | Quanto cada varredura gasta no tier Base? | Mobs 100 FE e minérios 350 FE, somando cerca de 1 FE/t, igual à lanterna. Os outros tiers ficam para a Fase 4, e o valor entra no config. Detalha a pergunta 550 |
| 552 | O que conta como mob hostil para o scanner? | O que o jogo marca como `Enemy`, inclusive de outros mods, sem lista fixa e inclusive atrás de paredes. Um mob neutro só conta se for confirmado no fonte que está agredindo o dono, seguindo a [pergunta 13](fase-1-conceito.md) |
| 551 | Quais minérios contam como raros? | Sete tags, numa lista no config, da mais rara para a menos rara: sucata de netherita, esmeralda, diamante, ouro, lápis, redstone e ferro. Ficam de fora carvão, cobre e quartzo. Detalha a [pergunta 41](fase-1-conceito.md) |
| 550 | Os scanners gastam energia? | Sim, um custo fixo por varredura, com o robô parado ou andando. A recomendação era não gastar na Fase 3. É uma nova exceção às perguntas [32](fase-2-versao-minima.md) e [341](fase-4-upgrades-e-autonomia.md), e antecipa essa parte do item 9 da Fase 4 do checklist. O valor ainda vai para a rodada |
| 549 | Como o player liga e desliga os scanners? | Com uma chave única para os dois, o botão `Scanners`, no painel lateral do inventário, como o `Lamp`. Começa ligada e fica guardada no item, inclusive em saves antigos. Responde a pendência 425 só para os scanners. As perguntas 683 e 703 levam a chave para a aba Settings da tela do Cubit |
| 548 | Como o player escolhe o minério do filtro do scanner, sem a interface de configuração? | O dono clica no Cubit com um minério na mão. O item não é gasto, e clicar com o mesmo minério limpa o filtro. Fica guardado no item. Em quais estados do robô isso vale ainda vai para a rodada |
| 547 | Como conta o intervalo de 25 s entre textos repetidos, agora que os textos têm nomes? | Por nome: `DIAMOND!!!` pode aparecer logo depois de `IRON!!!`, e cada nome espera os próprios 25 s, com a mesma chave, `notice_repeat_ticks`. Detalha a pergunta 442 |
| 546 | Como ficam os textos do que o Cubit achou? | `Cubit: DIAMOND!!!` para minérios e `Cubit: Careful, Zombie!` para mobs. Substituem `Ore found` e `Enemy nearby`, que só ficam quando não há nome para mostrar. O nome do mob segue o idioma do jogo do player. Muda as perguntas 49 e 67 no texto, e a 542 no idioma. A pergunta 734 troca o nome do minério pelo nome traduzido do bloco |
| 545 | Qual é o intervalo do scanner de mobs por tier? | Base 10 s, Advanced 8 s, Elite 6 s e Ultimate 4 s. A recomendação era manter 5, 4, 3 e 2 s de `tiers.md`, porque um mob pode chegar e atacar entre duas leituras. Muda, só para mobs, o intervalo de varredura de `tiers.md` |
| 544 | Como fica o intervalo do scanner de minérios em cada tier? | Base 35 s, Advanced 20 s, Elite 15 s e Ultimate 5 s, escritos pelo desenvolvedor como "base 35, 20, 15, 5", depois de ele sugerir 45 s, caindo 10 s por tier, e em seguida 20 s no Base. A recomendação do agente era 20, 15, 10 e 5 s. Muda, só para minérios, o intervalo de varredura de `tiers.md`. A resposta não decide o intervalo do scanner de mobs, que vai para a rodada. Só o Base entra no config agora, conforme a [pergunta 39](fase-2-versao-minima.md) |
| 543 | A descrição do scanner apresentada pelo agente pode ser a base da rodada de decisões? | Sim. Cada ponto ainda vira uma pergunta na rodada, então a descrição não decide nada e não autoriza commit |
| 542 | Há algo a acrescentar sobre os scanners? | Sim, como ideias do desenvolvedor, a decidir na rodada: o texto diz o que o Cubit achou, como zumbi, esqueleto, creeper, diamante ou ouro, em caixa alta para minério, como "DIAMANT!!!", e como "cuidado Zumbi" para mob; o Cubit localiza o que achou e cria uma identificação visual, como um contorno azul no mob ou no diamante; a antena fica vermelha para mobs hostis e azul para minérios, assim como a luz em volta do Cubit. O radar a cada 45 s foi trocado pela resposta 544. O agente apontou que o texto no jogo precisa ficar em inglês, pela regra 1, e que a luz real não muda de cor, pela resposta 478 |
| 541 | O jogo está aberto? | Não. Os gates podem rodar |
| 540 | O desenvolvedor quer algum ajuste no roteiro, depois do PR #114? | Não: "ok" e "vamos fazer uma nova sessão". Os scanners começam numa sessão nova |
| 539 | O desenvolvedor quer algum ajuste no roteiro, depois do PR #112? | Não: "ok" e "continue". Segue a correção do `visual.md`, os scanners, o Jade, as telas próprias e a `0.1.0` |
| 538 | O JaCoCo mede as GameTests. O agente adota essa medição? | Sim, com um comando só de relatório, `make gametest-coverage`, que não reprova nada e não entra no `pre-push`. A prova deu 92% das linhas (1.954 de 2.117) e 78% dos ramos, fora de `domain/`, `client/`, `gametest/` e `datagen/`. Os mais baixos foram `RobotAnimations` (0%, só roda no cliente), os dois menus (cerca de 50%) e a rede (cerca de 65%). Fecha a pergunta 527 |
| 537 | O desenvolvedor quer algum ajuste no roteiro das próximas tarefas, depois do PR #111? | Não: "ok" e "continue". Segue a #108, depois a correção do `visual.md`, os scanners, o Jade, as telas próprias e a `0.1.0`. O item 19 continua dependendo da resposta 500 |
| 536 | O que o agente faz depois do merge de um PR? | É uma regra nova, a 52 do `AGENTS.md`, pedida pelo desenvolvedor: pede autorização para apagar a branch local e a remota, que não são mais necessárias, apresenta no que vai atuar em seguida e pergunta se ele quer algum ajuste no roteiro. O agente entendeu "PR 1" como o primeiro passo da lista, e "roteiro" como a sequência das próximas tarefas, não o roteiro manual de testes |
| 535 | O agente atualiza a estrutura de pacotes do `AGENTS.md`, que não cita `DockTrip`, `DockRemoval` e `Ownership`? | Sim, nas linhas `dock/` e `ownership/`, num commit próprio |
| 534 | Como foi o teste rápido no jogo da issue #107, e o agente pode fazer os commits? | "Sim". O agente entendeu como teste sem problemas e autorização dos commits do plano, porque as duas coisas foram perguntadas juntas. O resultado de cada passo não foi detalhado, então o teste pode não ter sido feito em todos eles |
| 533 | Há algo a acrescentar à issue #107? | Não: "pode avançar", depois de ver a lista das próximas tarefas. O agente entendeu como autorização para criar a branch e começar pela #107, na ordem sugerida, não para commits |
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
| 515 | Como foi o roteiro manual da cor e do nome? | "Testes feito tudo ok", executado pelo desenvolvedor, sem detalhar cada passo. O passo 13, com outro player, fica para os testes de instância no fim da Fase 3, conforme a resposta 354. O agente não entendeu a resposta como autorização de commits. A 771 troca o passo 13 pelas GameTests |
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
| 500 | Quando entra a interface de configuração, em relação ao item 19 e às Fases 4 e 5? | O desenvolvedor vai falar sobre isso depois. Até lá, o checklist continua como está nesse ponto. A pergunta 717 responde: a interface entra na Fase 4 |
| 499 | Onde fica o painel solar no topo? | Centralizado em relação ao topo da cabeça do Cubit |
| 498 | Dá para criar a ideia de uma camada fina de vidro protegendo o painel solar? | Sim, pedido do desenvolvedor: uma película fina e translúcida sobre a célula, com um reflexo discreto desenhado. Só visual. O sinal continua sendo o pulso suave da pergunta 490, confirmado. A pergunta 501 tira o vidro |
| 497 | Como entram as mudanças de modelo do item 13, e onde ficam os testes? | No mesmo PR do painel solar, em commit próprio. JUnit: dia e noite, tiers, soma sem criar FE e sinal. GameTests: céu aberto, coberto, outras dimensões e os testes de consumo sem depender do céu. Roteiro manual: ver a placa, o pulso, a fita e as faixas. As chaves e o save da recomendação saem, pelas perguntas 494 e 495 |
| 496 | Um bloco transparente acima do Cubit impede a recarga solar? | Não: vale a luz do céu que chega ao Cubit, então vidro deixa carregar, e blocos opacos bloqueiam. Detalha a pergunta 53 |
| 495 | O que entra da chave geral do Cubit nesta entrega? | Nada: "não entra agora". Fica para quando a interface de configuração entrar. Muda o item 13 do checklist |
| 494 | Onde ficam os botões do painel solar até a tela própria? | Nenhum entra agora. O desenvolvedor pediu só a elevação do painel, para parecer uma peça colocada, e o sinal luminoso. Toda a interface de configuração fica para depois, "ao final da fase 3 entre a fase 4 e 5", momento ainda a esclarecer. Por isso, a pendência 425 continua aberta nessa parte. A pergunta 717 leva a interface de configuração para a Fase 4 |
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
| 449 | O `pt_br.json` citado na troca de termos é criado? | Não. A regra 1 continua valendo, e os nomes em português ficam só nos documentos. A pergunta 712 substitui: o `pt_br.json` é criado |
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
| 369 | Quando toca o bipe padrão? | Ao trocar para normal, feliz ou sono, exceto ao ligar, ao ser colocado e ao carregar o mundo. Detalha a pergunta 148. As perguntas 565 e 584 acrescentam o bipe na resposta do filtro e nas falas |
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
| Marcelo Fabiano | 2026-11-10 09:53 | Registra as respostas 785 a 791 e o ponteiro na 714. |
| Marcelo Fabiano | 2026-11-08 01:48 | Registra as respostas 780 a 784. |
| Marcelo Fabiano | 2026-11-08 01:06 | Registra as respostas 762 a 779 e o ponteiro na 515. |
| Marcelo Fabiano | 2026-11-08 00:50 | Registra as respostas 753 a 761. |
| Marcelo Fabiano | 2026-11-08 00:33 | Registra as respostas 739 a 752. |
| Marcelo Fabiano | 2026-11-07 23:48 | Registra as respostas 728 a 738. |
| Marcelo Fabiano | 2026-11-07 23:45 | Registra as respostas 716 a 727. |
| Marcelo Fabiano | 2026-11-07 23:42 | Acrescenta os ponteiros da auditoria e leva a pendente 425 à Fase 4. |
| Marcelo Fabiano | 2026-11-07 23:40 | Registra as respostas 720 e 721. |
| Marcelo Fabiano | 2026-11-07 23:39 | Registra as respostas 716 a 719. |
| Marcelo Fabiano | 2026-11-07 23:34 | Registra as respostas 712 a 715. |
| Marcelo Fabiano | 2026-11-07 23:32 | Registra a resposta 711. |
| Marcelo Fabiano | 2026-11-07 23:28 | Registra as respostas 709 e 710. |
| Marcelo Fabiano | 2026-11-07 23:25 | Registra a resposta 708. |
| Marcelo Fabiano | 2026-11-07 23:23 | Registra a resposta 707. |
| Marcelo Fabiano | 2026-11-07 23:20 | Registra a resposta 706. |
| Marcelo Fabiano | 2026-11-07 23:19 | Registra a resposta 705. |
| Marcelo Fabiano | 2026-11-07 23:13 | Registra a resposta 704. |
| Marcelo Fabiano | 2026-11-07 23:13 | Registra a resposta 703. |
| Marcelo Fabiano | 2026-11-07 23:10 | Registra a resposta 702. |
| Marcelo Fabiano | 2026-11-07 23:08 | Registra a resposta 701. |
| Marcelo Fabiano | 2026-11-07 23:02 | Registra a resposta 700. |
| Marcelo Fabiano | 2026-11-07 22:59 | Registra a resposta 699. |
| Marcelo Fabiano | 2026-11-07 22:57 | Registra a resposta 698. |
| Marcelo Fabiano | 2026-11-07 22:53 | Registra a resposta 697. |
| Marcelo Fabiano | 2026-11-07 22:44 | Registra a resposta 696. |
| Marcelo Fabiano | 2026-11-07 22:39 | Registra a resposta 695. |
| Marcelo Fabiano | 2026-11-07 22:37 | Registra a resposta 694. |
| Marcelo Fabiano | 2026-11-07 22:34 | Registra a resposta 693. |
| Marcelo Fabiano | 2026-11-07 22:29 | Registra as respostas 691 e 692. |
| Marcelo Fabiano | 2026-11-07 22:27 | Registra as respostas 687 a 690. |
| Marcelo Fabiano | 2026-11-07 22:24 | Registra as respostas 683 a 686. |
| Marcelo Fabiano | 2026-11-07 22:20 | Registra a resposta 682. |
| Marcelo Fabiano | 2026-11-07 22:14 | Registra as respostas 675 a 681. |
| Marcelo Fabiano | 2026-11-07 22:00 | Registra as respostas 673 e 674. |
| Marcelo Fabiano | 2026-11-07 21:49 | Registra a resposta 672. |
| Marcelo Fabiano | 2026-11-07 21:46 | Registra a resposta 671. |
| Marcelo Fabiano | 2026-11-07 21:45 | Registra a resposta 670. |
| Marcelo Fabiano | 2026-11-07 21:41 | Registra a resposta 669. |
| Marcelo Fabiano | 2026-11-07 21:36 | Registra as respostas 667 e 668. |
| Marcelo Fabiano | 2026-11-07 21:32 | Registra as respostas 664, 665 e 666. |
| Marcelo Fabiano | 2026-11-07 21:29 | Registra as respostas 660, 661, 662 e 663. |
| Marcelo Fabiano | 2026-11-07 21:27 | Registra a resposta 659. |
| Marcelo Fabiano | 2026-11-07 21:25 | Registra as respostas 656, 657 e 658. |
| Marcelo Fabiano | 2026-11-07 21:18 | Registra a resposta 655. |
| Marcelo Fabiano | 2026-11-07 21:11 | Registra a resposta 654. |
| Marcelo Fabiano | 2026-11-07 21:10 | Registra as respostas 652 e 653. |
| Marcelo Fabiano | 2026-11-07 21:08 | Registra a resposta 651. |
| Marcelo Fabiano | 2026-11-07 21:06 | Registra a resposta 650. |
| Marcelo Fabiano | 2026-11-07 21:03 | Registra as respostas 648 e 649. |
| Marcelo Fabiano | 2026-11-07 21:01 | Registra as respostas 646 e 647. |
| Marcelo Fabiano | 2026-11-07 20:53 | Registra a resposta 645. |
| Marcelo Fabiano | 2026-11-07 20:46 | Registra as respostas 642, 643 e 644. |
| Marcelo Fabiano | 2026-11-07 20:44 | Registra as respostas 638, 639, 640 e 641. |
| Marcelo Fabiano | 2026-11-07 20:40 | Registra a resposta 637. |
| Marcelo Fabiano | 2026-11-07 20:39 | Registra as respostas 635 e 636. |
| Marcelo Fabiano | 2026-11-07 20:33 | Registra a resposta 634. |
| Marcelo Fabiano | 2026-11-07 20:21 | Registra a resposta 633. |
| Marcelo Fabiano | 2026-11-07 20:18 | Registra as respostas 631 e 632. |
| Marcelo Fabiano | 2026-11-07 20:12 | Registra as respostas 628, 629 e 630. |
| Marcelo Fabiano | 2026-11-07 20:09 | Registra a resposta 627. |
| Marcelo Fabiano | 2026-11-07 20:06 | Registra as respostas 625 e 626. |
| Marcelo Fabiano | 2026-11-07 20:02 | Registra as respostas 623 e 624. |
| Marcelo Fabiano | 2026-11-07 20:00 | Registra a resposta 622. |
| Marcelo Fabiano | 2026-11-07 19:56 | Registra as respostas 619, 620 e 621. |
| Marcelo Fabiano | 2026-11-07 19:53 | Registra a resposta 618. |
| Marcelo Fabiano | 2026-11-07 19:49 | Registra a resposta 617. |
| Marcelo Fabiano | 2026-11-07 19:48 | Registra a resposta 616. |
| Marcelo Fabiano | 2026-11-07 19:16 | Registra as respostas 613, 614 e 615. |
| Marcelo Fabiano | 2026-11-07 19:07 | Registra as respostas 611 e 612. |
| Marcelo Fabiano | 2026-11-07 19:04 | Registra a resposta 610. |
| Marcelo Fabiano | 2026-11-07 19:02 | Registra as respostas 606, 607, 608 e 609. |
| Marcelo Fabiano | 2026-11-07 19:01 | Registra as respostas 603, 604 e 605. |
| Marcelo Fabiano | 2026-11-07 18:58 | Registra a resposta 602. |
| Marcelo Fabiano | 2026-11-07 18:56 | Registra as respostas 598, 599, 600 e 601. |
| Marcelo Fabiano | 2026-11-07 18:55 | Registra as respostas 594, 595, 596 e 597. |
| Marcelo Fabiano | 2026-11-07 17:49 | Registra a resposta 593. |
| Marcelo Fabiano | 2026-11-07 17:41 | Registra as respostas 591 e 592. |
| Marcelo Fabiano | 2026-11-07 17:32 | Registra a resposta 590. |
| Marcelo Fabiano | 2026-11-07 17:23 | Registra a resposta 589. |
| Marcelo Fabiano | 2026-11-07 17:21 | Registra a resposta 588. |
| Marcelo Fabiano | 2026-11-07 17:14 | Registra a resposta 587. |
| Marcelo Fabiano | 2026-11-07 17:11 | Registra a resposta 586. |
| Marcelo Fabiano | 2026-11-07 17:07 | Registra a resposta 585. |
| Marcelo Fabiano | 2026-11-07 16:57 | Registra a resposta 584. |
| Marcelo Fabiano | 2026-11-07 16:54 | Registra as respostas 579 a 583. |
| Marcelo Fabiano | 2026-11-07 16:48 | Registra a resposta 576. |
| Marcelo Fabiano | 2026-11-07 16:44 | Registra a resposta 575. |
| Marcelo Fabiano | 2026-11-07 16:43 | Registra a resposta 574. |
| Marcelo Fabiano | 2026-11-07 16:41 | Registra a resposta 573. |
| Marcelo Fabiano | 2026-11-07 16:40 | Registra a resposta 572. |
| Marcelo Fabiano | 2026-11-07 16:27 | Registra as respostas 570 e 571. |
| Marcelo Fabiano | 2026-11-07 16:20 | Registra a resposta 569. |
| Marcelo Fabiano | 2026-11-07 16:17 | Registra as respostas 567 e 568. |
| Marcelo Fabiano | 2026-11-07 16:01 | Registra as respostas 565 e 566. |
| Marcelo Fabiano | 2026-11-07 15:57 | Aponta as respostas 49 e 67 para a 546. |
| Marcelo Fabiano | 2026-11-07 15:56 | Registra as respostas 561 a 564. |
| Marcelo Fabiano | 2026-11-07 15:54 | Registra as respostas 557 a 560. |
| Marcelo Fabiano | 2026-11-07 15:53 | Registra as respostas 553 a 556. |
| Marcelo Fabiano | 2026-11-07 15:52 | Registra as respostas 549 a 552. |
| Marcelo Fabiano | 2026-11-07 15:50 | Registra as respostas 545 a 548. |
| Marcelo Fabiano | 2026-11-07 15:47 | Registra as respostas 540 a 544. |
| Marcelo Fabiano | 2026-11-07 14:49 | Registra a resposta 539. |
| Marcelo Fabiano | 2026-11-07 14:38 | Registra a resposta 538. |
| Marcelo Fabiano | 2026-11-07 14:28 | Registra a resposta 537. |
| Marcelo Fabiano | 2026-11-07 14:25 | Registra a resposta 536. |
| Marcelo Fabiano | 2026-11-07 14:18 | Registra as respostas 534 e 535. |
| Marcelo Fabiano | 2026-11-07 14:10 | Registra a resposta 533. |
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
