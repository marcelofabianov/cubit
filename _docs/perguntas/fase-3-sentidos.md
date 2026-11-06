# Fase 3: Sentidos

## Pendentes

Não há perguntas pendentes nesta fase.

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 48 | Onde os textos do Cubit aparecem? | Na barra acima da hotbar, só para o dono e com intervalo mínimo entre repetições |
| 49 | Os cinco textos ficam assim em inglês? | Sim: `Critical battery`, `Enemy nearby`, `Ore found`, `I need to recharge` e `Items unloaded` |
| 52 | Quais são os novos valores do solar, abaixo do consumo? | De dia: Base 5, Advanced 10, Elite 12 e Ultimate 16 FE/t. À noite, 25% do valor de dia: Elite 3 e Ultimate 4 FE/t |
| 53 | Como o solar se comporta com blocos, clima e outras dimensões? | Só blocos importam: coberto não carrega, e descoberto carrega 100%. O clima não tem efeito. No Nether e no End não carrega |
| 57 | Qual é a expressão em hibernação? | Sempre carregando |
| 66 | A partir de quanta bateria o Cubit mostra a expressão de sono? | 10%, igual à bateria crítica |
| 67 | O texto `Ore found` vale para qual minério? | Para minério do filtro do scanner. Bipe e surpresa, só para os raros |
| 92 | Qual é a expressão ao recarregar pelo slot, fora da base? | Inativo, tela apagada. Ativo, a expressão normal. A expressão carregando fica só para a base |
| 135 | Quais sons o Cubit tem? | Oito sons diferentes: esteiras ao andar, como as de um tanque de guerra; mob hostil encontrado; minério raro encontrado; bipe padrão ao trocar de expressão; curto elétrico enquanto pega fogo; ao ligar; ao desligar; e enquanto é recarregado. O detalhe está em [`visual.md`](../conceito/visual.md#sons). As perguntas 316 e 320 acrescentam o som do ataque e o de colocar no mundo |
| 136 | O bipe padrão vale para a troca de quais expressões? | Substituída pela pergunta 148, que limita às expressões sem som exclusivo. Esta resposta substituiu a pergunta 12 |
| 137 | Os sons são do desenvolvedor e podem ser publicados? | São dele, gerados com ferramentas de IA de estúdio. Os originais ficam em `tmp/wav/`, fora do repositório |
| 140 | Os sons convertidos estão bons? | Não, o desenvolvedor ouviu cortes e sensação de congelamento ou perda. A conversão precisa ser revista |
| 141 | Quais versões dos sons ficaram boas no VLC? | Os sons 3, 4 e 6 estão normais. Os sons 1, 2, 5, 7 e 8 têm cortes na reprodução |
| 142 | Os cortes nos sons vêm dos arquivos ou do reprodutor? | Do reprodutor: no VLC havia cortes, e no `pw-play` os sons tocaram sem problema. A conversão está certa. Desfaz a conclusão da pergunta 140 e explica a 141 |
| 148 | Ao trocar para uma expressão com som próprio, o bipe padrão toca junto? | Não. O bipe padrão toca nas trocas de expressão que não têm um som exclusivo. Substitui a pergunta 136 |
| 149 | Qual é o intervalo mínimo entre repetições dos textos e dos alertas sonoros? | O valor será definido na Fase 3, num teste no jogo, e ficará no config. Complementa a pergunta 48 |
| 157 | Quais são as reações do Cubit ao ligar e ao pegar fogo? | Ao ligar, toca o som de ligar. Ao pegar fogo, toca o som de curto elétrico. Confirma a pergunta 135 |
| 158 | Qual expressão o Cubit mostra ao ligar e ao pegar fogo? | Ao ligar, a normal. Ao pegar fogo, mantém a atual, sem troca |
| 264 | Quando entram o visual melhor e os tooltips do painel do slot de energia? | Na tela própria da Fase 3, a da [pergunta 186](fase-2-versao-minima.md). Na Fase 2, o painel continua simples |
| 316 | Qual é o som do ataque do Cubit? | O desenvolvedor forneceu `cubit_ataque_choque.wav`, convertido da mesma forma que os demais, para `shock_attack.ogg`. Passam a ser nove sons. O original fica em `tmp/wav/`. Amplia a pergunta 135 |
| 320 | O que fazer com o som novo, `robot_quando_colocado_no_mundo.wav`? | Converter para Ogg e colocar no diretório dos demais sons do projeto, para ser usado. Convertido da mesma forma que os demais, para `placed.ogg`. Passam a ser dez sons. O original fica em `tmp/wav/`. Amplia a pergunta 135. As perguntas 321 e 322 detalham o nome, o momento e a fase |
| 321 | O nome `placed.ogg` e o momento "ao ser colocado no mundo" estão certos? | Sim. Detalha a pergunta 320 |
| 322 | Em que fase o som de colocar no mundo passa a tocar? | Na Fase 3, junto dos outros oito. Detalha a pergunta 320 |
| 413 | Como foi o roteiro manual dos braços, dos olhos que acompanham o dono e da inclinação? | Concluído como esperado, executado pelo desenvolvedor, sem detalhar cada passo. O limite de 1 bloco ficou como está |
| 410 | Dá para o Cubit inclinar de leve para cima ou para baixo quando o dono está em outro nível, e de quanto? | Sim, cerca de 8°, com transição suave, numa constante. Como o modelo não tem cabeça separada, o osso `body` inclina inteiro. Os olhos mantêm o comportamento das perguntas 404 e 406 |
| 411 | Quando acontece a inclinação do corpo? | Só com o dono 1 bloco ou mais acima ou abaixo, com o robô ligado, na mesma dimensão e até 8 blocos. Um pulo move só os olhos. Usa um segundo dado sincronizado, fora do save |
| 412 | Em que entrega e com quais testes entra a inclinação? | Neste PR, na issue #89. JUnit: direção e ângulo por nível. GameTest: dado sincronizado mudando no servidor. Roteiro manual: ver a inclinação, junto dos braços e dos olhos, num só teste |
| 406 | A partir de quantos blocos de diferença de altura os olhos olham para o dono? | 1 bloco, entre os pés do dono e os do robô, enquanto a diferença durar, numa constante. A recomendação era 2, e o desenvolvedor escolheu 1. Detalha a pergunta 404 |
| 407 | Se o robô se move na vertical e o dono está em outra altura, qual movimento vence? | O do robô. Sem movimento próprio, os olhos seguem a altura do dono |
| 408 | Até que distância os olhos acompanham a altura do dono? | Até 8 blocos, com o robô ligado e na mesma dimensão, o mesmo limite de virar o corpo |
| 409 | Em que pontos ficam os testes dos braços e dos olhos? | JUnit: quando os braços sobem, a direção dos olhos por pulo e por nível, o tempo e a prioridade. GameTests: animação dos braços no arquivo e direção sincronizada mudando no servidor. Roteiro manual: ver os braços e os olhos |
| 402 | Por quanto tempo o Cubit fica com os braços erguidos ao levar dano? | Enquanto estiver triste, ou seja, enquanto durar a expressão triste ou de olhos em X, cerca de 5 s depois do último dano |
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
| 396 | O que "modelo final" muda no Cubit? | Mantém a geometria e a textura de 128×128 do corpo, e significa só os ossos que as animações precisarem. Detalha a [pergunta 60](fase-2-versao-minima.md) e mantém a 162 |
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
| 378 | Como fazer a pequena iluminação em volta do Cubit, que ilustra a claridade da tela? | Com luz real, no item da lanterna, pelo LambDynamicLights: uma luz fraca, de nível 3 a 4 como ponto de partida, sem gastar energia, que apaga quando a tela apaga |
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
| 331 | Quando o LambDynamicLights entra no build? | Com a lanterna, na Fase 3. A [pergunta 337](fase-2-versao-minima.md) muda a segunda parte: a `0.1.0` só sai com a Fase 3, então já o exige. Detalha a [pergunta 242](fase-5-robos-especializados.md) |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
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
