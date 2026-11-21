# Tiers

Esta é a página dos números de balanceamento. O `README.md` traz só um resumo, e o roteiro manual cita os valores padrão como resultado esperado. Os números são o ponto de partida para os testes.

Os tiers são Base, Avançado, Elite e Ultimate (no jogo: Base, Advanced, Elite e Ultimate).

## Regras dos tiers

- Os **upgrades são os próprios tiers**. A cada tier, o robô mantém as features do anterior, só que melhoradas.
- O robô Base já tem os recursos com capacidade menor, e os que ele não usa ficam **bloqueados** até o tier certo.
- O **upgrade de tier** é um item aplicado no robô e não pode pular etapas: Base → Avançado → Elite → Ultimate. Inventário, bateria, vida, nome, cor e configurações se mantêm, e a vida máxima sobe para a do novo tier. O detalhe está em [`upgrade-de-tier.md`](funcionalidades/upgrade-de-tier.md).

## Identidade

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Cor do colar | Cinza | Azul | Roxo | Dourado |
| Vida | 25 (12,5 ❤) | 50 (25 ❤) | 100 (50 ❤) | 200 (100 ❤) |
| Velocidade (o Ultimate na pergunta 1258) | 1,0× | 1,25× | 1,5× | 1,75× |
| Lanterna | Luz 10 | Luz 12 | Luz 14 | Luz 15 |
| Expressões, nome e cor da carcaça | ✔ | ✔ | ✔ | ✔ |

## Energia

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Bateria | 300 mil FE | 600 mil FE | 3 mi FE | 8 mi FE |
| Power Bank (70% da bateria), em `power_bank_capacity` (pergunta 910) | 210 mil FE | 420 mil FE | 2,1 mi FE | 5,6 mi FE |
| Autonomia aproximada, andando no plano, sem o painel e com a lanterna apagada | ~16,7 min | ~27,8 min | ~1,9 h | ~3,7 h |
| Autonomia com inventário cheio ou na água | ~12,5 min | ~20,8 min | ~1,4 h | ~2,8 h |
| Autonomia na água e com inventário cheio | ~10 min | ~16,7 min | ~1,2 h | ~2,2 h |
| Painel solar (de dia) | 5 FE/t | 10 FE/t | 12 FE/t | 16 FE/t |
| Painel solar (à noite) | — | — | 3 FE/t | 4 FE/t |
| Recarga na base e pelo slot de energia | 500 FE/t | 1.000 FE/t | 2.500 FE/t | 5.000 FE/t |
| Raio do retorno automático e da chamada pelo Dock, e bateria do retorno (o retorno entra com o item 15 da Fase 4.2, pergunta 1066) | 15 blocos, 10% | 24 blocos, 10% | 32 blocos, 10% | 48 blocos, 10% |

## Consumo por atividade

Os extras somam ao andar. O consumo só vale com o robô ativo.

| Atividade | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Parado | 0 FE/t | 0 FE/t | 0 FE/t | 0 FE/t |
| Andar | 15 FE/t | 18 FE/t | 22 FE/t | 30 FE/t |
| Extra ao andar com o inventário cheio | +5 FE/t | +6 FE/t | +7 FE/t | +10 FE/t |
| Extra ao andar na água | +5 FE/t | +6 FE/t | +7 FE/t | +10 FE/t |
| Extra ao subir, por bloco de altura | +15 FE | +18 FE | +22 FE | +30 FE |
| Extra com condição negativa, mesmo parado | +25 FE/t | +50 FE/t | +125 FE/t | +250 FE/t |
| Choque, por uso | Bloqueado | 2 mil FE | 2 mil FE | 1,5 mil FE |
| Extra com a lanterna acesa, mesmo parado | +1 FE/t | +1 FE/t | +2 FE/t | +2 FE/t |
| Scanner de mobs, por varredura | 50 FE | 100 FE | 150 FE | 300 FE |
| Scanner de minérios, por varredura | 50 FE | 100 FE | 150 FE | 300 FE |
| Ímã, por item puxado (respostas 877 e 879) | Bloqueado | 20 FE | 30 FE | 60 FE |
| Colocar tochas, por tocha (pergunta 972) | Bloqueado | 20 FE | 20 FE | 20 FE |

- **Claridade da tela:** não gasta energia.
- **Cards:** o custo por ação está em [trabalhos](#trabalhos).

## Valores gerais

Valem para todos os tiers e ficam no config. Os marcados como planejados entram no config junto do recurso, na fase indicada (pergunta 718).

| Recurso | Valor |
|---|---|
| Consumo parado | 0 FE/t |
| Carga do Cubit ao sair do craft | 25% |
| Carga da Power Bank ao sair do craft | 0% |
| Recarga da Power Bank no inventário do Dock | A do Base, uma por vez, sem Cubit na base ou com o Cubit da base de bateria cheia (perguntas 949 e 952) |
| Descarga da Power Bank no slot de entrada do Dock | O limite de entrada do Dock, junto dos cabos (pergunta 920) |
| Velocidade 1,0× (atributo de movimento) | 0,33 |
| Intervalo para recalcular a rota | 10 ticks |
| Distância para parar perto do player | 3 blocos |
| Altura máxima que a rota desce | 5 blocos |
| Velocidade em chamas, sobre a do tier | 1,25× |
| Velocidade com o inventário cheio, sobre a do tier | 0,75× |
| Controle de velocidade, sobre a do tier: mínimo, máximo e passo (pergunta 1267) | 50%, 100% e 10%. O padrão do Cubit é 100% |
| Busca de água em chamas, em qualquer direção | 8 blocos |
| Alcance ao andar ao acaso em chamas e ao sair da lava ou de um bloco que fere | 5 blocos |
| Distância para teleportar até o player | ~20 blocos |
| Religar | 5% de bateria |
| Bateria crítica, com expressão de sono, indicador vermelho e texto | 5% |
| Tempo com a expressão feliz | ~3 s |
| Tempo com a expressão triste, depois de um dano | ~5 s |
| Vida para a expressão de quase morto | 10% |
| Tempo com a expressão de quase morto, depois de um dano | ~3 s |
| Tempo com a expressão de preocupado | ~3 s |
| Distância para ficar preocupado com o dono | 16 blocos |
| Estoque do Induction Dock | 100 mil FE |
| Entrada do Induction Dock | 5.000 FE/t |
| Tempo sem avançar antes de teleportar ao Dock | 5 s |
| Tempo com a tela completa antes de sair da base | 3 s |
| Tempo para recuperar toda a vida na base | ~1 min |
| Vida ao sair do conserto | 25% |
| Intervalo para tentar de novo o resgate no vazio | 20 ticks |
| Alcance do Tablet | Qualquer distância com o chunk do Cubit carregado. Descarregado, só leitura (pergunta 1018, que muda a 94) |
| Intervalo mínimo entre repetições de textos e alertas | ~25 s |
| Tempo em que o Contra-atacar lembra de quem feriu o Cubit ou o dono | ~30 s (pergunta 832) |
| Distância do dono em que o alvo do Focus deixa de ser alvo | 32 blocos (pergunta 846) |
| Alcance da mira do app Alvo do Tablet | 32 blocos (pergunta 1043) |
| Intervalo mínimo entre falas sobre o tempo | 5 min |
| Intervalo mínimo entre falas de bioma | 2 min |
| Intervalo mínimo entre falas de animais | 1 min |
| Chance de falar ao abater um animal | 33% |
| Intervalo mínimo entre falas da redstone | 1 min |
| Distância para reclamar de ficar para trás | 16 blocos |
| Intervalo mínimo entre falas de ficar para trás | 1 min |
| Intervalo para ler o tempo e o horário das falas | 20 ticks |
| Tempo com a expressão de surpresa | ~3 s |
| Tempo da caixa em volta de um minério | ~10 s |
| Limite de blocos lidos por tick numa varredura | 2.000 |
| Minérios raros, do mais raro para o menos raro | Sucata de netherita, esmeralda, diamante, ouro, lápis, redstone e ferro |
| Luz da claridade da tela | 4 |
| A lanterna acende abaixo da luz | 8 |
| A lanterna apaga a partir da luz | 10 |
| Intervalo para ler a luz do ambiente | 20 ticks |
| Intervalo para ler se o sol chega ao painel | 20 ticks |
| Energia de 1 pó de redstone | 5.000 FE |
| Energia de 1 bloco de redstone | 45.000 FE |
| Intervalo da coleta por contato | 5 ticks (resposta 887) |
| Mortes no histórico do player, `death_log_size` | 10 (pergunta 1115) |
| Distância de chegada da guia ao local de morte, `death_guide_arrival_blocks` | 3 blocos (pergunta 1126) |
| Quanto o Cubit anda à frente do dono na guia, `death_guide_lead_blocks` | 8 blocos (pergunta 1126) |
| Tempo sem avançar antes de cancelar a guia, `death_guide_stuck_seconds` | 10 s (pergunta 1126) |
| Intervalo do ímã | 10 ticks (resposta 887) |
| Custo de abrir uma porta marcada, `door_open_cost` | 50 FE (pergunta 1368) |
| Portas marcadas por dono, `door_marks_limit` | 64 (pergunta 1369) |

## Coleta e inventário

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Inventário | 27 slots (baú simples) | 36 slots | 45 slots | 54 slots (baú duplo) |
| Coleta por contato | ✔ | ✔ | ✔ | ✔ |
| Ímã (raio), do Advanced em diante (resposta 879) | Bloqueado | 3 blocos | 5 blocos | 8 blocos |
| Busca dos itens do chão (raio em volta do dono, respostas 936 e 937) | 4 blocos | 6 blocos | 8 blocos | 12 blocos |
| Filtro de coleta, nos dois modos (resposta 423) | 3 itens | 9 itens | 18 itens + tags | 27 itens + tags |
| Baús marcados para descarregar (`unload_chests`, pergunta 1094) | 1 | 1 | 2 | 3 |
| Distância máxima até o baú (`unload_reach_blocks`) | 16 blocos | 32 blocos | 48 blocos | 64 blocos |

## Sensores

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Scanner de minérios (raio) | 5 blocos | 8 blocos | 12 blocos | 20 blocos |
| Minérios no filtro do scanner | 1 | 3 | 6 | 64, o limite do config (pergunta 810) |
| Scanner de mobs hostis (raio) | 8 blocos | 14 blocos | 20 blocos | 25 blocos |
| Intervalo de varredura de minérios | 35 s | 20 s | 15 s | 5 s |
| Intervalo de varredura de mobs | 10 s | 8 s | 6 s | 4 s |

O raio dos dois scanners é o de uma esfera em volta do Cubit: só conta o que está a até esse número de blocos em linha reta, em qualquer direção (perguntas 602 a 606 e 622).

## Comportamento e combate

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Modos de defesa | Pacífico | + Contra-atacar e Focus | + Agressivo | Todos |
| Choque (dano) | Bloqueado | 2 (1 ❤) | 4 (2 ❤) | 6 (3 ❤), em área |
| Choque (alcance) | — | 3 blocos | 5 blocos | 8 blocos |
| Choque (intervalo) | — | 3 s | 2 s | 1 s |
| Custo do choque | — | 2 mil FE | 2 mil FE | 1,5 mil FE |
| Alvos por choque | — | 1, o mais próximo | 1, o mais próximo | Todos no alcance (pergunta 826) |

## Autonomia

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Seguir o player | ✔ | ✔ | ✔ | ✔ |
| Modo área (app Área do Tablet), na base | Bloqueado | Até 16×16 | Até 32×32 | Até 64×64 |
| Altura máxima da área (pergunta 1050) | — | 8 blocos | 16 blocos | 32 blocos |
| Local de morte, `death_point`, com `locked`, `mark` ou `guide` (perguntas 1117, 1118 e 1129), com o histórico em todos os tiers | Bloqueado | Marca a coordenada, com o texto ao renascer | Marca e guia até lá, no Overworld | Marca e guia até lá, no Overworld |

## Slots exclusivos

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Tochas (coloca sozinho, luz de bloco mínima que garante, perguntas 973 e 975) | Bloqueado | 1 slot, luz 1 | 1 slot, luz 1 | 1 slot, luz 1 |
| Energia (Power Bank ou redstone) | 1 slot | 1 slot | 1 slot | 1 slot |

A fornalha de minérios foi para o Minerador, na Fase 5, e o carregador de ferramentas saiu do mod (pergunta 930).

## Trabalhos

Planejado para a Fase 5. O tier Base não aceita cards, e cada card tem o seu tier mínimo. Os números de cada card são definidos no refinamento dele (pergunta 1144).

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Cards de Aprendizado de Máquina, `allowed_jobs` no config (perguntas 1145 e 1163) | Bloqueado | Pescador, Fazendeiro e Pecuarista | + Organizador, Cozinheiro, Defensor e Minerador | + Construtor, Mecânico e Alquimista |
| Minerador equivale a picareta de (pergunta 1149) | — | — | Diamante | Diamante |
| Minerador: Eficiência | — | — | III | V |
| Minerador: Fortuna | — | — | II | III |
| Minerador: Toque Suave | — | — | — | ✔ |
| Minerador: fornalha de minérios elétrica, em ~20 FE/t enquanto funde (perguntas 930 e 1149) | — | — | 1× | 2× |
| Defensor: modos, pela reação escolhida (pergunta 1150) | — | — | Contra-atacar e Agressivo | Contra-atacar e Agressivo. O Focus fica para o card do Defensor |
| Pescador: Isca, `fisher_lure_level` (pergunta 1185) | — | — | II | III |
| Pescador: Sorte do Mar, `fisher_luck_level` (pergunta 1185) | — | — | II | III |
| Pescador: custo por peixe pescado, `fisher_catch_cost` (pergunta 1190) | — | 300 FE | 300 FE | 300 FE |
| Fazendeiro: custo por planta colhida ou bloco arado, `farmer_action_cost` (perguntas 1226 e 1235) | — | 50 FE | 75 FE | 100 FE |
| Fazendeiro: pausa entre ações, `farmer_action_pause_ticks` (pergunta 1226) | — | 20 ticks | 12 ticks | 6 ticks |
| Fazendeiro: plantas de colheita especial, `farmer_special_crops` (pergunta 1219) | — | — | ✔ | ✔ |
| Fazendeiro: slot de fertilizante, `farmer_bone_meal` (perguntas 1244 e 1248) | — | ✔ | ✔ | ✔ |
| Fazendeiro: Fortuna, `farmer_fortune_level` (pergunta 1221) | — | — | — | II |
| Organizador: baús marcados, `organizer_chests` (pergunta 1300) | — | — | 9 | 18 |
| Organizador: máquinas ou destinos marcados, `organizer_marks` (pergunta 1300) | — | — | 8 | 16 |
| Organizador: custo por pilha movida, `organizer_action_cost` (pergunta 1300) | — | — | 50 FE | 50 FE |
| Organizador: pausa entre ações, `organizer_action_pause_ticks` (pergunta 1300) | — | — | 12 ticks | 6 ticks |
| Organizador: intervalo da varredura, `organizer_scan_interval_ticks` (pergunta 1300) | — | — | 100 ticks | 40 ticks |
| Cozinheiro: fornos marcados, `cook_ovens` (pergunta 1326) | — | — | 4 | 8 |
| Cozinheiro: custo por pilha movida, `cook_action_cost` (pergunta 1326) | — | — | 50 FE | 50 FE |
| Cozinheiro: pausa entre ações, `cook_action_pause_ticks` (pergunta 1326) | — | — | 12 ticks | 6 ticks |
| Cozinheiro: intervalo da varredura, `cook_scan_interval_ticks` (pergunta 1326) | — | — | 100 ticks | 40 ticks |
| Storage wireless | — | — | — | ✔ |

| Valor geral | Valor |
|---|---|
| Cubits por área | Até 3 |
| Teste antes de fechar um card | 20 a 30 robôs |
| Pescador: espera antes da mordida, como no jogo (pergunta 1184) | 100 a 600 ticks |
| Pescador: mordida, como no jogo | 20 a 80 ticks |
| Pescador: espera a menos por nível de Isca, como no jogo | 100 ticks |
| Pescador: alcance do lançamento, da margem ao ponto da água (pergunta 1208) | 6 blocos |
| Fazendeiro: intervalo da varredura da plantação, `farmer_scan_interval_ticks` (pergunta 1237) | 40 ticks |
| Intervalo de descarga na área, padrão e máximo, `unload_interval_default_minutes` e `unload_interval_max_minutes` (perguntas 1252 e 1254) | 5 min e 30 min |
| Fazendeiro: água que mantém a terra arada, como no jogo (pergunta 1232) | Até 4 blocos |
| Organizador: quantidade de um pedido, com − e + de 1 em 1 e, com Shift, de 8 em 8 (pergunta 1300) | 1 a 64 |

**A definir:**

- antenas e chunks por tier do Servidor, área e consumo por tier da antena e teto de chunks no mundo;
- consumo do Servidor e da antena com o carregamento desligado;
- custo por ação dos outros cards e dano do Defensor;
- limites por tick e distância do Dock ao Servidor ou à antena;
- limite de animais do Pecuarista e custo do storage e tamanho dos canais;
- alcance e viabilidade do feixe da lanterna de cabeça do Minerador.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-21 03:25 | Adiciona o custo e o limite das portas (perguntas 1368 e 1369). |
| Marcelo Fabiano | 2026-11-19 23:04 | Adiciona os números do Cozinheiro (pergunta 1326). |
| Marcelo Fabiano | 2026-11-19 21:34 | Adiciona os números do Organizador (pergunta 1300). |
| Marcelo Fabiano | 2026-11-19 20:18 | Adiciona os números do controle de velocidade (pergunta 1267). |
| Marcelo Fabiano | 2026-11-15 14:30 | Reduz a velocidade do Ultimate de 2,0× para 1,75× (pergunta 1258). |
| Marcelo Fabiano | 2026-11-15 14:05 | Acrescenta o intervalo de descarga (perguntas 1252 e 1254). |
| Marcelo Fabiano | 2026-11-15 13:45 | Libera o fertilizante desde o Avançado (pergunta 1248). |
| Marcelo Fabiano | 2026-11-15 13:25 | O `farmer_bone_meal` libera o slot de fertilizante (pergunta 1244). |
| Marcelo Fabiano | 2026-11-15 10:40 | Adiciona os números do Fazendeiro (perguntas 1219, 1221, 1226, 1232, 1235 e 1237). |
| Marcelo Fabiano | 2026-11-14 23:10 | Adiciona os números do Pescador (perguntas 1184, 1185, 1190 e 1208). |
| Marcelo Fabiano | 2026-11-14 20:52 | Liga os cards por tier à chave `allowed_jobs` (pergunta 1163). |
| Marcelo Fabiano | 2026-11-14 20:27 | Atualiza os trabalhos por tier, o Minerador e o Defensor (perguntas 1144, 1145, 1149 e 1150). |
| Marcelo Fabiano | 2026-11-14 18:18 | Cita a chave `death_point` por tier (pergunta 1129). |
| Marcelo Fabiano | 2026-11-14 18:15 | Define o local de morte por tier e os números do histórico e da guia (perguntas 1115 a 1118 e 1126). |
| Marcelo Fabiano | 2026-11-14 13:55 | Liga os limites da descarga ao config (pergunta 1094). |
| Marcelo Fabiano | 2026-11-14 10:00 | Liga a bateria do retorno ao item 15 (pergunta 1066). |
| Marcelo Fabiano | 2026-11-14 09:00 | Define a altura máxima da área (pergunta 1050). |
| Marcelo Fabiano | 2026-11-13 17:15 | Adiciona o alcance da mira do app Alvo. |
| Marcelo Fabiano | 2026-11-13 16:40 | Troca o alcance do controle remoto pelo do Tablet. |
| Marcelo Fabiano | 2026-11-13 13:45 | Define o custo, o slot e a luz das tochas. |
| Marcelo Fabiano | 2026-11-13 11:30 | Passa a recarga da Power Bank para o inventário do Dock e as tochas para o item 12. |
| Marcelo Fabiano | 2026-11-13 09:50 | Adiciona o raio da busca dos itens do chão. |
| Marcelo Fabiano | 2026-11-13 09:05 | Leva a fornalha para o Minerador e tira o carregador de ferramentas. |
| Marcelo Fabiano | 2026-11-12 23:10 | Acrescenta a descarga da Power Bank no Dock. |
| Marcelo Fabiano | 2026-11-12 22:30 | Tira a marca de planejada da Power Bank e acrescenta a recarga dela no Dock. |
| Marcelo Fabiano | 2026-11-12 21:45 | Define o ímã do Advanced em diante, o custo dele e os intervalos da coleta. |
| Marcelo Fabiano | 2026-11-12 15:10 | Adiciona o modo Focus e a distância do alvo. |
| Marcelo Fabiano | 2026-11-12 10:30 | Adiciona os alvos por choque e a memória do Contra-atacar. |
| Marcelo Fabiano | 2026-11-11 22:01 | Aponta para o upgrade de tier, junta a tabela dos sensores e troca o filtro ilimitado por 64. |
| Marcelo Fabiano | 2026-11-07 23:41 | Arruma a tabela dos sensores e marca os valores planejados. |
| Marcelo Fabiano | 2026-11-07 20:04 | Adiciona o intervalo das falas da redstone. |
| Marcelo Fabiano | 2026-11-07 20:00 | Muda o raio do scanner de mobs. |
| Marcelo Fabiano | 2026-11-07 19:06 | Muda os raios e os custos dos scanners e define a área como esfera. |
| Marcelo Fabiano | 2026-11-07 17:14 | Adiciona os números das falas de animais. |
| Marcelo Fabiano | 2026-11-07 17:11 | Adiciona o intervalo das falas de bioma. |
| Marcelo Fabiano | 2026-11-07 17:07 | Adiciona os números de ficar para trás. |
| Marcelo Fabiano | 2026-11-07 16:54 | Adiciona os intervalos das falas. |
| Marcelo Fabiano | 2026-11-07 16:20 | Adiciona a altura máxima que a rota desce. |
| Marcelo Fabiano | 2026-11-07 15:57 | Define os intervalos e o custo dos scanners, a surpresa, a caixa, o limite por tick e os minérios raros. |
| Marcelo Fabiano | 2026-11-06 15:05 | Adiciona o intervalo do painel solar. |
| Marcelo Fabiano | 2026-11-06 13:34 | Define a luz e o consumo da lanterna, os valores gerais dela e leva o feixe para o Minerador. |
| Marcelo Fabiano | 2026-11-06 11:10 | Tira a citação ao robô de uso geral. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-06 09:48 | Reduz a bateria crítica para 5% e define o intervalo mínimo. |
| Marcelo Fabiano | 2026-11-06 00:40 | Aumenta a velocidade 1,0×. |
| Marcelo Fabiano | 2026-11-06 00:28 | Adiciona o tempo dos olhos em X. |
| Marcelo Fabiano | 2026-11-06 00:22 | Adiciona os valores da expressão de preocupado. |
| Marcelo Fabiano | 2026-11-06 00:17 | Aumenta o tempo da expressão triste. |
| Marcelo Fabiano | 2026-11-06 00:10 | Adiciona os valores das expressões triste e quase morto. |
| Marcelo Fabiano | 2026-11-05 23:36 | Adiciona o tempo com a expressão feliz. |
| Marcelo Fabiano | 2026-11-05 21:34 | Adiciona a altura máxima da área, antecipada para a Fase 4. |
| Marcelo Fabiano | 2026-11-05 21:02 | Avisa da viabilidade do feixe, adiciona o intervalo dos alertas e amplia o nome do alcance. |
| Marcelo Fabiano | 2026-11-05 17:15 | Adiciona o tempo com a tela completa. |
| Marcelo Fabiano | 2026-11-05 16:09 | Adiciona o tempo sem avançar até o Dock. |
| Marcelo Fabiano | 2026-11-05 14:40 | Adiciona o consumo por atividade e os novos valores de autonomia. |
| Marcelo Fabiano | 2026-11-05 12:08 | Adiciona os números previstos para os robôs especializados. |
| Marcelo Fabiano | 2026-11-05 01:15 | Adiciona o intervalo do resgate no vazio. |
| Marcelo Fabiano | 2026-11-05 00:12 | Adiciona a vida após o conserto e cita a exceção do roteiro. |
| Marcelo Fabiano | 2026-11-04 21:29 | Indica que a busca de água vale em qualquer direção. |
| Marcelo Fabiano | 2026-11-04 21:02 | Adiciona os valores do comportamento em chamas. |
| Marcelo Fabiano | 2026-11-04 20:09 | Adiciona a velocidade de referência e o intervalo da rota. |
| Marcelo Fabiano | 2026-11-04 18:50 | Adiciona a distância para parar perto do player. |
| Marcelo Fabiano | 2026-11-04 17:28 | Atualiza o upgrade de tier e adiciona o alcance do controle. |
| Marcelo Fabiano | 2026-11-04 17:26 | Atualiza os nomes das linhas de recarga e de raio. |
| Marcelo Fabiano | 2026-11-04 17:16 | Indica que os números valem para o robô de uso geral. |
| Marcelo Fabiano | 2026-11-04 17:15 | Atualiza os caminhos após a renomeação para português. |
| Marcelo Fabiano | 2026-11-04 17:08 | Atualiza a entrada do Dock, a expressão de sono e o tempo de recuperar a vida. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona o painel solar, os valores gerais e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona os números de balanceamento por tier. |
