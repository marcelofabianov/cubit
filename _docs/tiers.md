# Tiers

Esta é a página dos números de balanceamento. O `README.md` traz só um resumo, e o roteiro manual cita os valores padrão como resultado esperado. Os números são o ponto de partida para os testes.

Os tiers são Base, Avançado, Elite e Ultimate (no jogo: Base, Advanced, Elite e Ultimate).

## Regras dos tiers

- Os **upgrades são os próprios tiers**. A cada tier, o robô mantém as features do anterior, só que melhoradas.
- O robô Base já tem os recursos com capacidade menor, e os que ele não usa ficam **bloqueados** até o tier certo.
- O **upgrade de tier** é um item aplicado no robô e não pode pular etapas: Base → Avançado → Elite → Ultimate. Inventário, bateria, vida, nome, cor e configurações se mantêm, e a vida máxima sobe para a do novo tier.

## Identidade

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Cor do colar | Cinza | Azul | Roxo | Dourado |
| Vida | 25 (12,5 ❤) | 50 (25 ❤) | 100 (50 ❤) | 200 (100 ❤) |
| Velocidade | 1,0× | 1,25× | 1,5× | 2,0× |
| Lanterna | Luz 10 | Luz 12 | Luz 14 | Luz 15 |
| Expressões, nome e cor da carcaça | ✔ | ✔ | ✔ | ✔ |

## Energia

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Bateria | 300 mil FE | 600 mil FE | 3 mi FE | 8 mi FE |
| Power Bank (70% da bateria) | 210 mil FE | 420 mil FE | 2,1 mi FE | 5,6 mi FE |
| Autonomia aproximada, andando no plano, sem o painel e com a lanterna apagada | ~16,7 min | ~27,8 min | ~1,9 h | ~3,7 h |
| Autonomia com inventário cheio ou na água | ~12,5 min | ~20,8 min | ~1,4 h | ~2,8 h |
| Autonomia na água e com inventário cheio | ~10 min | ~16,7 min | ~1,2 h | ~2,2 h |
| Painel solar (de dia) | 5 FE/t | 10 FE/t | 12 FE/t | 16 FE/t |
| Painel solar (à noite) | — | — | 3 FE/t | 4 FE/t |
| Recarga na base e pelo slot de energia | 500 FE/t | 1.000 FE/t | 2.500 FE/t | 5.000 FE/t |
| Raio do retorno automático e da chamada pelo Dock, e bateria do retorno | 15 blocos, 10% | 24 blocos, 10% | 32 blocos, 10% | 48 blocos, 10% |

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
| Fundir na fornalha | Bloqueado | ~20 FE/t | ~20 FE/t | ~20 FE/t |
| Choque, por uso | Bloqueado | 2 mil FE | 2 mil FE | 1,5 mil FE |
| Extra com a lanterna acesa, mesmo parado | +1 FE/t | +1 FE/t | +2 FE/t | +2 FE/t |
| Ímã, scanners e colocar tochas | A definir | A definir | A definir | A definir |

- **Claridade da tela:** não gasta energia.
- **Carregador de ferramentas:** transfere energia para outro item, então não conta como gasto. A velocidade está em [slots exclusivos](#slots-exclusivos).
- **Cards:** o custo por ação está em [trabalhos](#trabalhos).

## Valores gerais

Valem para todos os tiers e ficam no config.

| Recurso | Valor |
|---|---|
| Consumo parado | 0 FE/t |
| Carga do Cubit ao sair do craft | 25% |
| Carga da Power Bank ao sair do craft | 0% |
| Velocidade 1,0× (atributo de movimento) | 0,33 |
| Intervalo para recalcular a rota | 10 ticks |
| Distância para parar perto do player | 3 blocos |
| Velocidade em chamas, sobre a do tier | 1,25× |
| Velocidade com o inventário cheio, sobre a do tier | 0,75× |
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
| Alcance do controle remoto ao mirar num Cubit | 32 blocos |
| Intervalo mínimo entre repetições de textos e alertas | ~25 s |
| Luz da claridade da tela | 4 |
| A lanterna acende abaixo da luz | 8 |
| A lanterna apaga a partir da luz | 10 |
| Intervalo para ler a luz do ambiente | 20 ticks |
| Energia de 1 pó de redstone | 5.000 FE |
| Energia de 1 bloco de redstone | 45.000 FE |

## Coleta e inventário

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Inventário | 27 slots (baú simples) | 36 slots | 45 slots | 54 slots (baú duplo) |
| Ímã (raio) | 1 bloco | 3 blocos | 5 blocos | 8 blocos |
| Filtro de coleta | 3 itens | 9 itens | 18 itens + tags | 27 itens + tags |
| Baús marcados para descarregar | 1 | 1 | 2 | 3 |
| Distância máxima até o baú | 16 blocos | 32 blocos | 48 blocos | 64 blocos |

## Sensores

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Scanner de minérios (raio) | 5 blocos | 10 blocos | 16 blocos | 24 blocos |
| Minérios no filtro do scanner | 1 | 3 | 6 | Ilimitado |
| Scanner de mobs hostis (raio) | 5 blocos | 10 blocos | 16 blocos | 24 blocos |
| Intervalo de varredura | 5 s | 4 s | 3 s | 2 s |

## Comportamento e combate

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Modos de defesa | Pacífico | + Contra-atacar | + Agressivo | Todos |
| Choque (dano) | Bloqueado | 2 (1 ❤) | 4 (2 ❤) | 6 (3 ❤), em área |
| Choque (alcance) | — | 3 blocos | 5 blocos | 8 blocos |
| Choque (intervalo) | — | 3 s | 2 s | 1 s |
| Custo do choque | — | 2 mil FE | 2 mil FE | 1,5 mil FE |

## Autonomia

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Seguir o player | ✔ | ✔ | ✔ | ✔ |
| Modo área (controle remoto), na base | Bloqueado | Até 16×16 | Até 32×32 | Até 64×64 |
| Altura máxima da área | — | A definir | A definir | A definir |
| Local de morte | Bloqueado | Marca a coordenada | Marca e guia até lá | Marca e guia até lá |

## Slots exclusivos

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Tochas (coloca sozinho) | Bloqueado | 1 slot, luz < 7 | 1 slot, luz < 7 | 2 slots, nível configurável |
| Carregador de ferramentas elétricas | Bloqueado | 1 slot, 250 FE/t | 2 slots, 500 FE/t | 4 slots, 1.000 FE/t |
| Fornalha de minérios | Bloqueado | 1 slot, 0,5× | 1 slot, 1× | 1 slot, 2× |
| Energia (Power Bank ou redstone) | 1 slot | 1 slot | 1 slot | 1 slot |

A fornalha gasta cerca de 20 FE por tick enquanto funde.

## Trabalhos

Planejado para a Fase 5. O tier Base não aceita cards.

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Card de Aprendizado de Máquina | Bloqueado | ✔ | ✔ | ✔ |
| Minerador equivale a picareta de | — | Ferro | Diamante | Diamante |
| Minerador: Eficiência | — | — | III | V |
| Minerador: Fortuna | — | — | II | III |
| Minerador: Toque Suave | — | — | — | ✔ |
| Minerador: Inquebrável e Remendo | — | — | — | ✔ |
| Defensor: modos | — | Contra-atacar | + Agressivo | Todos, sem Pacífico |
| Storage wireless | — | — | — | ✔ |

| Valor geral | Valor |
|---|---|
| Cubits por área | Até 3 |
| Teste antes de fechar um card | 20 a 30 robôs |

**A definir:**

- antenas e chunks por tier do Servidor, área e consumo por tier da antena e teto de chunks no mundo;
- consumo do Servidor e da antena com o carregamento desligado;
- custo por ação de cada card, percentuais de Inquebrável e Remendo e dano do Defensor;
- limites por tick e distância do Dock ao Servidor ou à antena;
- limite de animais do Pecuarista, intervalo do Pescador e custo do storage e tamanho dos canais;
- alcance e viabilidade do feixe da lanterna de cabeça do Minerador.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
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
