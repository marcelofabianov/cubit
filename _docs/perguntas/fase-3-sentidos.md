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
| 334 | O que acontece ao colocar um Cubit no mundo? | Toca só `placed.ogg`, em qualquer estado, e `power_on.ogg` fica para a passagem de desligado a ligado. A expressão é feliz, se ele nascer ativo, e tela apagada, se nascer desligado. Detalha as perguntas 158 e 321 |
| 331 | Quando o LambDynamicLights entra no build? | Com a lanterna, na Fase 3. A `0.1.0` sai sem exigi-lo, porque nada nela usa luz. Detalha a [pergunta 242](fase-5-robos-especializados.md) |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
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
