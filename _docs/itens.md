# Itens e blocos

| Nome | Descrição |
|---|---|
| Cubit | Robô companheiro, colocado no mundo como item |
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
| Cubit | `cubit:general_robot` | A definir |
| Cubit Remote | `cubit:remote` | A definir |
| Advanced Module | `cubit:advanced_module` | A definir |
| Elite Module | `cubit:elite_module` | A definir |
| Ultimate Module | `cubit:ultimate_module` | A definir |
| Induction Dock | `cubit:induction_dock` | A definir |
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

## Virar item

- O player pode recolher o robô a qualquer momento, em qualquer estado.
- O item mantém bateria, vida, inventário, nome, cor, tier e configurações, nos *data components* do 1.21.1.
- O item empilha em 1 e mostra a bateria numa barra.
- O item não entra no inventário do robô nem em shulker boxes.
- O tipo do robô vem do próprio item, e o dono é quem o coloca. O robô ganha um UUID novo a cada colocação.

## Colocar

- O clique direito num bloco coloca o robô em cima da face clicada, de frente para o player. Sem espaço, nada acontece, e o item continua na mão.
- Colocar sempre gasta o item, também no modo criativo.
- O item sem estado salvo, como o da aba do criativo, vale como um robô novo: tier Base, carga inicial e vida cheia.
- A aba do criativo do mod é `cubit:main`.

## Contratos de save

Estes nomes não mudam sem migração (regra 18 do `AGENTS.md`).

| Onde | Nome | Conteúdo |
|---|---|---|
| Componente do item | `cubit:robot_state` | `tier`, `energy` e `health` |
| NBT da entidade | `Owner`, `Tier`, `Energy` e `PowerState` | Dono, tier, carga da bateria e estado |
| Dados do player | `cubit:following_robot` | `robot`: o UUID do robô que segue, ou nada |

O estado salvo é validado ao entrar no jogo, sem derrubá-lo (regra 17 do `AGENTS.md`):

| Dado | Se for inválido |
|---|---|
| Tier | Usa o Base |
| Energia acima da capacidade | Usa a capacidade |
| Vida fora dos limites | Fica entre 1 e o máximo do tier. Sem vida salva, o robô colocado morreria na hora |
| Estado da entidade | Fica desligado (`INACTIVE_SWITCHED_OFF`), o mais restritivo |
| Dono ausente | Ninguém consegue interagir com o robô |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 19:24 | Define como colocar o robô e os contratos de save. |
| Marcelo Fabiano | 2026-11-04 17:16 | Troca o ID do robô para cubit:general_robot. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Adiciona a função de localizar ao Cubit Remote. |
| Marcelo Fabiano | 2026-11-04 17:08 | Separa a lista de nomes e descrições da tabela de IDs e receitas. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza a Power Bank, o Dock e as regras de virar item. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona os itens e blocos do mod. |
