# Itens e blocos

| Nome | Descrição |
|---|---|
| Cubit | Robô companheiro, colocado no mundo como item |
| Damaged Cubit | O que cai quando o Cubit morre. Guarda o estado dele e não pode ser colocado no mundo |
| Toolbox | Conserta o Damaged Cubit e é consumida a cada conserto |
| Cubit Remote | Envia comandos aos Cubits, marca áreas de trabalho e localiza os Cubits |
| Advanced Module | Leva o Cubit do tier Base para o Advanced |
| Elite Module | Leva o Cubit do tier Advanced para o Elite |
| Ultimate Module | Leva o Cubit do tier Elite para o Ultimate |
| Induction Dock | Base de recarga do Cubit, alimentada por cabos ou redstone |
| Base Power Bank | Bateria portátil com a capacidade do tier Base |
| Advanced Power Bank | Bateria portátil com a capacidade do tier Advanced |
| Elite Power Bank | Bateria portátil com a capacidade do tier Elite |
| Ultimate Power Bank | Bateria portátil com a capacidade do tier Ultimate |

## IDs e receitas

| Nome | ID | Receita |
|---|---|---|
| Cubit | `cubit:general_robot` | Abaixo |
| Damaged Cubit | `cubit:damaged_general_robot` | Não tem: cai quando o Cubit morre |
| Toolbox | `cubit:toolbox` | Provisória, abaixo |
| Cubit Remote | `cubit:remote` | A definir |
| Advanced Module | `cubit:advanced_module` | A definir |
| Elite Module | `cubit:elite_module` | A definir |
| Ultimate Module | `cubit:ultimate_module` | A definir |
| Induction Dock | `cubit:induction_dock` | Abaixo |
| Base Power Bank | `cubit:base_power_bank` | A definir |
| Advanced Power Bank | `cubit:advanced_power_bank` | A definir |
| Elite Power Bank | `cubit:elite_power_bank` | A definir |
| Ultimate Power Bank | `cubit:ultimate_power_bank` | A definir |

## Progressão das receitas

Receitas provisórias, só com recursos do jogo puro, a ajustar nos testes:

| Item | Materiais |
|---|---|
| Base | Ferro e redstone |
| Avançado | Ouro e quartzo |
| Elite | Diamante e ametista |
| Ultimate | Netherite e estrela do Nether |

### Cubit

Na mesa de craft, com uma forma que lembra o item. Vai além da progressão provisória acima (pergunta 348). A receita aparece no livro ao ter diamante.

| | Coluna 1 | Coluna 2 | Coluna 3 |
|---|---|---|---|
| Linha 1 | Redstone | Lâmpada de redstone | Redstone |
| Linha 2 | Lingote de ferro | Diamante | Lingote de ferro |
| Linha 3 | Obsidiana | Vazio | Obsidiana |

### Induction Dock

Na mesa de craft, em forma de L (pergunta 336). O desenho espelhado também vale, e a receita aparece no livro ao ter um Cubit.

| | Coluna 1 | Coluna 2 | Coluna 3 |
|---|---|---|---|
| Linha 1 | Redstone | Vazio | Vazio |
| Linha 2 | Lingote de ferro | Vazio | Vazio |
| Linha 3 | Baú | Redstone | Lingote de ferro |

### Toolbox

7 ferros, 1 redstone e 1 obsidiana, na mesa de craft: colunas laterais com 3 ferros cada, e coluna central com ferro em cima, redstone no meio e obsidiana embaixo.

## Virar item

- O player pode recolher o robô a qualquer momento, em qualquer estado.
- O item mantém bateria, vida, inventário, nome, cor, tier e configurações, nos *data components* do 1.21.1. As configurações são o modo de defesa, os filtros, os slots ativados ou desativados e a chave da lanterna. A área, os baús marcados e a seleção no controle se perdem ao recolher.
- O item empilha em 1 e mostra a bateria numa barra.
- O item não entra no inventário do robô nem em shulker boxes.
- O dono é quem coloca o item. O robô ganha um UUID novo a cada colocação.

## Morrer e consertar

- **Item próprio:** o `Damaged Cubit`.
- **Regras do item:** guarda o estado do robô, com o inventário preservado e a bateria zerada, e o dono, só para o resgate no vazio. Empilha em 1. Não pode ser colocado no mundo, não some com o tempo e não entra no inventário do robô nem em shulker boxes.
- **Dano:** nenhum dano o destrói, como fogo, explosão ou cacto. Só o comando `/kill` o remove.
- **Vazio:** cair no vazio não destrói o item, que volta para o primeiro destino possível:
  1. a última posição segura do robô, no chão e fora de fogo, lava e água, se estiver na mesma dimensão e num chunk carregado;
  2. o inventário do dono online, ou os pés dele, com o inventário cheio;
  3. o spawn do mundo, se o chunk estiver carregado;
  4. nenhum destino possível: fica parado logo abaixo do mundo e tenta de novo depois de um intervalo, sem carregar chunk à força.
- **Vínculo:** a morte libera o vínculo de quem segue. Com o dono offline, o mundo anota o robô morto, e o vínculo é limpo quando o dono entra.
- **Criativo:** o `Damaged Cubit` e a `Toolbox` ficam na aba `cubit:main`.
- **Conserto:** combine o `Damaged Cubit` com a `Toolbox`, na grade do inventário ou na mesa de craft. O robô sai com parte da vida, conforme [`tiers.md`](tiers.md#valores-gerais), e a bateria zerada. O Induction Dock recupera o resto.
- **Toolbox:** consumida a cada conserto. Empilha até 16.

## Colocar

- O clique direito num bloco coloca o robô em cima da face clicada, de frente para o player. Sem espaço, nada acontece, e o item continua na mão.
- Colocar sempre gasta o item, também no modo criativo.
- O item sem estado salvo, como o da aba do criativo, vale como um robô novo: tier Base, carga inicial e vida cheia.
- A aba do criativo do mod é `cubit:main`.

## Previstos na Fase 5

Nomes aprovados na pergunta 256. Viram contrato só quando implementados.

| Nome | ID | Descrição |
|---|---|---|
| Robot Server | `cubit:robot_server` | Bloco fixo na base, com tiers, que gerencia antenas |
| Signal Antenna | `cubit:signal_antenna` | Bloco com bateria, levado ao local de trabalho, que carrega chunks |
| Wireless Chest | `cubit:wireless_chest` | Acessa um canal do storage wireless |
| Wireless Storage Upgrade | A definir | Liga o Cubit Ultimate a um canal |
| Machine Learning: Miner | `cubit:ml_miner` | Card de Aprendizado de Máquina do trabalho de Minerador |
| Machine Learning: Farmer | `cubit:ml_farmer` | Do trabalho de Fazendeiro |
| Machine Learning: Fisher | `cubit:ml_fisher` | Do trabalho de Pescador |
| Machine Learning: Rancher | `cubit:ml_rancher` | Do trabalho de Pecuarista |
| Machine Learning: Defender | `cubit:ml_defender` | Do trabalho de Defensor |
| Machine Learning: Organizer | `cubit:ml_organizer` | Do trabalho de Organizador |
| Machine Learning: Cook | `cubit:ml_cook` | Do trabalho de Cozinheiro |
| Machine Learning: Builder | `cubit:ml_builder` | Do trabalho de Construtor |

## Contratos de save

Estes nomes não mudam sem migração (regra 18 do `AGENTS.md`).

| Onde | Nome | Conteúdo |
|---|---|---|
| Componente do item | `cubit:robot_state` | `tier`, `energy`, `health`, `lamp_enabled`, opcional, com o padrão ligado, e `color`, opcional, com o padrão `white`, também no item danificado |
| Componente do item | `cubit:energy_slot` | Item do slot de energia e reserva de FE, também no item danificado |
| Componente do item do Dock | `cubit:dock_energy` | Energia do estoque |
| Componente do item | `cubit:robot_inventory` | Conteúdo do inventário, também no item danificado |
| Componente do item danificado | `cubit:last_safe_position` | Dimensão e posição seguras do robô |
| Componente do item danificado | `cubit:robot_owner` | UUID do dono |
| Tipo de receita | `cubit:robot_repair` | Campos `damaged_robot`, `toolbox` e `result` |
| Dados do mundo | `cubit_dead_robots` | Robôs mortos com o dono offline |
| NBT da entidade | `Owner`, `Tier`, `Energy`, `PowerState`, `LastSafePosition`, `Inventory` e `EnergySlot` | Dono, tier, carga da bateria, estado, última posição segura, inventário, com `Slot` em cada item, e slot de energia, com `Item` e `Reserve` |
| NBT do bloco do Dock | `Owner`, `Energy` e `Slot` | Dono, estoque e item do slot |
| Estado do bloco do Dock | `facing`, `half` e `status` | Direção, parte de baixo ou de cima e estado da tela: `off`, `idle`, `charging`, `complete` ou `no_energy` |
| NBT da entidade na base | `DockPosition` | Posição do Dock em que hiberna |
| NBT da entidade | `LampEnabled` | Chave da lanterna. Sem a chave no save, vale ligada |
| NBT da entidade | `Color` | Cor da carcaça, com o nome do corante, como `light_blue`. Sem a chave ou com valor inválido, vale `white` |
| Componente do jogo no item | `minecraft:custom_name` | Nome do Cubit, inclusive no item danificado |
| NBT do jogo na entidade | `CustomName` | Nome do Cubit |
| Dados do player | `cubit:following_robot` | `robot`: o UUID do robô que segue, ou nada |

O estado salvo é validado ao entrar no jogo, sem derrubá-lo (regra 17 do `AGENTS.md`):

| Dado | Se for inválido |
|---|---|
| Tier | Usa o Base |
| Energia acima da capacidade | Usa a capacidade |
| Energia negativa, no robô, no slot de energia, no Dock ou no item | Usa zero e registra no log |
| Dock sem dono | Registra no log, ninguém o usa, e só um operador em criativo o remove |
| Vida fora dos limites | Fica entre 1 e o máximo do tier. Sem vida salva, o robô colocado morreria na hora |
| Estado da entidade | Fica desligado (`INACTIVE_SWITCHED_OFF`), o mais restritivo |
| Dono ausente | Ninguém consegue interagir com o robô |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-07 13:51 | Adiciona a energia negativa e o Dock sem dono à validação do save. |
| Marcelo Fabiano | 2026-11-06 15:58 | Adiciona a cor e o nome ao contrato do save. |
| Marcelo Fabiano | 2026-11-06 13:34 | Adiciona o contrato da chave da lanterna. |
| Marcelo Fabiano | 2026-11-06 11:10 | Atualiza os nomes e os IDs previstos dos cards e tira o tipo de robô. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-05 21:53 | Define quando as receitas aparecem no livro. |
| Marcelo Fabiano | 2026-11-05 21:50 | Define a receita do Induction Dock. |
| Marcelo Fabiano | 2026-11-05 21:47 | Define a receita do Cubit. |
| Marcelo Fabiano | 2026-11-05 21:34 | Detalha o que o item guarda das configurações. |
| Marcelo Fabiano | 2026-11-05 21:02 | Detalha o contrato do slot de energia na entidade. |
| Marcelo Fabiano | 2026-11-05 16:57 | Adiciona os estados do bloco do Dock. |
| Marcelo Fabiano | 2026-11-05 16:09 | Adiciona os contratos do Induction Dock. |
| Marcelo Fabiano | 2026-11-05 12:52 | Adiciona os contratos do slot de energia. |
| Marcelo Fabiano | 2026-11-05 12:08 | Adiciona os itens previstos na Fase 5. |
| Marcelo Fabiano | 2026-11-05 02:11 | Adiciona os contratos do inventário. |
| Marcelo Fabiano | 2026-11-05 01:15 | Detalha a proteção do item danificado, o resgate no vazio e os contratos novos. |
| Marcelo Fabiano | 2026-11-05 00:17 | Define que o item danificado empilha em 1. |
| Marcelo Fabiano | 2026-11-05 00:12 | Adiciona o item danificado, a Toolbox e o conserto. |
| Marcelo Fabiano | 2026-11-04 19:24 | Define como colocar o robô e os contratos de save. |
| Marcelo Fabiano | 2026-11-04 17:16 | Troca o ID do robô para cubit:general_robot. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Adiciona a função de localizar ao Cubit Remote. |
| Marcelo Fabiano | 2026-11-04 17:08 | Separa a lista de nomes e descrições da tabela de IDs e receitas. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza a Power Bank, o Dock e as regras de virar item. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona os itens e blocos do mod. |
