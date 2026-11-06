# Config

O config do Cubit é um contrato com o pack (pilar Contrato externo): as chaves abaixo não mudam sem migração (regra 18 do `AGENTS.md`).

- **Arquivo:** `config/cubit-common.toml`, do tipo `COMMON`, para o packwiz poder distribuí-lo.
- **Valores padrão:** são os de [`tiers.md`](../tiers.md), a única página com os números de balanceamento.
- **Crescimento:** uma chave entra junto com o código que a lê. Por enquanto, só existem os valores do tier Base (pergunta 39).

## Chaves

As chaves ficam em `snake_case`. As de um robô ficam em `[{tipo}.{tier}]`, como `[general_robot.base]`, porque um robô novo não pode exigir migração (regra 41 do `AGENTS.md`).

| Seção | Chave | Linha em `tiers.md` | Limites |
|---|---|---|---|
| `general` | `initial_charge_percent` | Carga do Cubit ao sair do craft | 0 a 100 |
| `general` | `wake_charge_percent` | Religar | 0 a 100 |
| `general` | `base_movement_speed` | Velocidade 1,0× | 0,01 a 1 |
| `general` | `follow_repath_ticks` | Intervalo para recalcular a rota | 1 a 200 |
| `general` | `stop_distance_blocks` | Distância para parar perto do player | 1 a 16 |
| `general` | `teleport_distance_blocks` | Distância para teleportar até o player | 17 a 128 |
| `general` | `burning_speed_multiplier` | Velocidade em chamas, sobre a do tier | 1 a 4 |
| `general` | `full_inventory_speed_multiplier` | Velocidade com o inventário cheio, sobre a do tier | 0,1 a 1 |
| `general` | `water_search_radius_blocks` | Busca de água em chamas | 1 a 16 |
| `general` | `burning_wander_radius_blocks` | Alcance ao andar ao acaso em chamas e ao sair da lava ou de um bloco que fere | 1 a 16 |
| `general` | `dock_energy_capacity` | Estoque do Induction Dock | 1 a 2147483647 |
| `general` | `dock_input_per_tick` | Entrada do Induction Dock | 1 a 2147483647 |
| `general` | `dock_full_heal_ticks` | Tempo para recuperar toda a vida na base | 1 a 72000 |
| `general` | `dock_stuck_ticks` | Tempo sem avançar antes de teleportar ao Dock | 1 a 1200 |
| `general` | `dock_complete_hold_ticks` | Tempo com a tela completa antes de sair da base | 1 a 1200 |
| `general` | `redstone_dust_energy` | Energia de 1 pó de redstone | 1 a 2147483647 |
| `general` | `redstone_block_energy` | Energia de 1 bloco de redstone | 1 a 2147483647 |
| `general` | `repair_health_percent` | Vida ao sair do conserto | 1 a 100 |
| `general` | `void_rescue_retry_ticks` | Intervalo para tentar de novo o resgate no vazio | 1 a 1200 |
| `general_robot.base` | `battery_capacity` | Bateria | 1 a 2147483647 |
| `general_robot.base` | `moving_consumption_per_tick` | Andar | 0 a 2147483647 |
| `general_robot.base` | `full_inventory_extra_per_tick` | Extra ao andar com o inventário cheio | 0 a 2147483647 |
| `general_robot.base` | `water_extra_per_tick` | Extra ao andar na água | 0 a 2147483647 |
| `general_robot.base` | `negative_condition_extra_per_tick` | Extra com condição negativa | 0 a 2147483647 |
| `general_robot.base` | `climb_extra_per_block` | Extra ao subir, por bloco de altura | 0 a 2147483647 |
| `general_robot.base` | `recharge_per_tick` | Recarga na base e pelo slot de energia | 1 a 2147483647 |
| `general_robot.base` | `inventory_slots` | Inventário | 1 a 54 |
| `general_robot.base` | `max_health` | Vida | 1 a 1024 |
| `general_robot.base` | `call_radius_blocks` | Raio do retorno automático e da chamada pelo Dock | 1 a 128 |
| `general_robot.base` | `speed_multiplier` | Velocidade | 0,1 a 4 |

## Por que esses limites

- **Energia até 2147483647:** é o maior valor de um `int`, o tipo usado pelas capabilities de FE do NeoForge.
- **Inventário até 54:** é o tamanho do baú duplo, o maior que a tela padrão do jogo mostra.
- **Vida até 1024:** é o máximo do atributo de vida do jogo.
- **Raio até 128:** mantém a busca limitada (regra 21 do `AGENTS.md`).
- **Parada e teleporte sem sobreposição:** os limites não se cruzam, então o config nunca faz o robô parar além da distância em que teleportaria.

## Valor inválido

O mod não cai por causa do config (regra 17 do `AGENTS.md`):

| Situação | O que acontece |
|---|---|
| Número fora dos limites | O NeoForge usa o limite mais próximo e registra no log |
| Chave ausente ou valor que não é número | O NeoForge volta ao padrão da chave e registra no log |
| Tier sem seção no config | O `TierCatalog` usa o tier Base do robô e registra no log uma vez |
| Tipo de robô sem seção | Erro de programação, então o `TierCatalog` lança uma exceção |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 21:02 | Acompanha o nome ampliado do alcance em `tiers.md`. |
| Marcelo Fabiano | 2026-11-05 17:15 | Adiciona o tempo com a tela completa. |
| Marcelo Fabiano | 2026-11-05 16:09 | Adiciona as chaves do Induction Dock. |
| Marcelo Fabiano | 2026-11-05 14:40 | Adiciona as chaves do consumo por situação. |
| Marcelo Fabiano | 2026-11-05 12:52 | Adiciona as chaves da redstone. |
| Marcelo Fabiano | 2026-11-05 01:15 | Adiciona as chaves do conserto e do resgate no vazio. |
| Marcelo Fabiano | 2026-11-04 21:02 | Adiciona as chaves do comportamento em chamas. |
| Marcelo Fabiano | 2026-11-04 20:09 | Adiciona as chaves de movimento. |
| Marcelo Fabiano | 2026-11-04 19:24 | Adiciona a carga para religar. |
| Marcelo Fabiano | 2026-11-04 19:08 | Documenta as chaves, os limites e o tratamento de valor inválido. |
