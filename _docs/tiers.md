# Tiers

Esta é a única página com os números de balanceamento. Eles são o ponto de partida para os testes e valem para o robô de uso geral.

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
| Lanterna | Luz 10 | Luz 15 | Luz 15 + feixe de 25 blocos | Luz 15 + feixe de 30 blocos |
| Expressões, nome e cor da carcaça | ✔ | ✔ | ✔ | ✔ |

## Energia

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Bateria | 300 mil FE | 600 mil FE | 3 mi FE | 8 mi FE |
| Power Bank (70% da bateria) | 210 mil FE | 420 mil FE | 2,1 mi FE | 5,6 mi FE |
| Consumo em movimento | 10 FE/t | 12 FE/t | 15 FE/t | 20 FE/t |
| Autonomia aproximada, sem o painel | ~25 min | ~42 min | ~2,8 h | ~5,6 h |
| Painel solar (de dia) | 5 FE/t | 10 FE/t | 12 FE/t | 16 FE/t |
| Painel solar (à noite) | — | — | 3 FE/t | 4 FE/t |
| Recarga na base e pelo slot de energia | 500 FE/t | 1.000 FE/t | 2.500 FE/t | 5.000 FE/t |
| Raio do retorno automático e da chamada pelo Dock, e bateria do retorno | 15 blocos, 10% | 24 blocos, 10% | 32 blocos, 10% | 48 blocos, 10% |

## Valores gerais

Valem para todos os tiers e ficam no config.

| Recurso | Valor |
|---|---|
| Consumo parado | 0 FE/t |
| Carga do Cubit ao sair do craft | 25% |
| Carga da Power Bank ao sair do craft | 0% |
| Velocidade 1,0× (atributo de movimento) | 0,3 |
| Intervalo para recalcular a rota | 10 ticks |
| Distância para parar perto do player | 3 blocos |
| Distância para teleportar até o player | ~20 blocos |
| Religar | 5% de bateria |
| Bateria crítica, com expressão de sono | 10% |
| Estoque do Induction Dock | 100 mil FE |
| Entrada do Induction Dock | 5.000 FE/t |
| Tempo para recuperar toda a vida na base | ~1 min |
| Alcance do controle remoto ao mirar num Cubit | 32 blocos |
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
| Modo área (controle remoto) | Bloqueado | Até 16×16 | Até 32×32 | Até 64×64 |
| Local de morte | Bloqueado | Marca a coordenada | Marca e guia até lá | Marca e guia até lá |

## Slots exclusivos

| Recurso | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Tochas (coloca sozinho) | Bloqueado | 1 slot, luz < 7 | 1 slot, luz < 7 | 2 slots, nível configurável |
| Carregador de ferramentas elétricas | Bloqueado | 1 slot, 250 FE/t | 2 slots, 500 FE/t | 4 slots, 1.000 FE/t |
| Fornalha de minérios | Bloqueado | 1 slot, 0,5× | 1 slot, 1× | 1 slot, 2× |
| Energia (Power Bank ou redstone) | 1 slot | 1 slot | 1 slot | 1 slot |

A fornalha gasta cerca de 20 FE por tick enquanto funde.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 20:09 | Adiciona a velocidade de referência e o intervalo da rota. |
| Marcelo Fabiano | 2026-11-04 18:50 | Adiciona a distância para parar perto do player. |
| Marcelo Fabiano | 2026-11-04 17:28 | Atualiza o upgrade de tier e adiciona o alcance do controle. |
| Marcelo Fabiano | 2026-11-04 17:26 | Atualiza os nomes das linhas de recarga e de raio. |
| Marcelo Fabiano | 2026-11-04 17:16 | Indica que os números valem para o robô de uso geral. |
| Marcelo Fabiano | 2026-11-04 17:15 | Atualiza os caminhos após a renomeação para português. |
| Marcelo Fabiano | 2026-11-04 17:08 | Atualiza a entrada do Dock, a expressão de sono e o tempo de recuperar a vida. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona o painel solar, os valores gerais e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona os números de balanceamento por tier. |
