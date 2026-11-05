# Domínio

Regras do Cubit em Java puro, sem depender do jogo (regra 12 do `AGENTS.md`). Quem chama fornece os dados: o domínio não lê config, relógio nem o mundo.

## Linguagem

| Termo | Significado |
|---|---|
| `Energy` | Quantidade inteira de FE, nunca negativa |
| `Percentage` | Porcentagem inteira, de 0 a 100 |
| `Battery` | Capacidade e carga atual. É imutável, e cada operação devolve uma bateria nova |
| `PowerState` | Estado do robô |
| `PowerRules` | Regras que mudam o estado por causa da energia |
| `RobotType` | Tipo de robô, como o de uso geral. É um dado, identificado por um ID |
| `RobotTier` | Nível de evolução: Base, Advanced, Elite e Ultimate. A `key` é o nome usado no config e nos saves |
| `RobotState` | O que o robô guarda ao virar item: tier, energia e vida |
| `TierSpec` | Números de um tipo de robô num tier |
| `TierCatalog` | Entrega o `TierSpec` de um tipo de robô num tier. O domínio define a interface, e o config a implementa |
| `FollowAction` | O que o robô que segue faz: seguir, parar ou teleportar |
| `BurningAction` | O que o robô faz em chamas: nada, sair da lava, buscar água ou andar ao acaso |
| `FollowDecision` | Escolhe a `FollowAction` conforme o estado, a dimensão e a distância |
| `FollowingRobotPolicy` | Garante que só um robô siga o player |
| `FollowingOutcome` | Resultado da política: estado do robô, quem segue e quem desligar |

## Regras e porquês

- **Energia inteira:** evita erro de arredondamento, e uma transferência nunca cria nem perde FE.
- **Operações informam o que moveu:** `recharge` devolve o que foi aceito, e `consume`, o que foi consumido. Assim, quem chama sabe o que sobrou e não perde energia.
- **Comparação de porcentagem sem divisão:** `hasAtLeast` multiplica os dois lados, para ser exata com os valores de `tiers.md`.

- **Tipo de robô é dado:** as regras recebem os números de um `TierSpec`, sem saber qual é o robô. Assim, um robô novo não muda as regras (regra 39 do `AGENTS.md`).
- **Upgrade:** as regras de evolução entre tiers entram na Fase 4, junto dos módulos.
- **Estado guardado se ajusta ao config:** o config pode mudar enquanto o robô está no item, então `batteryFor` e `healthFor` limitam os valores ao `TierSpec` atual. A vida nunca fica abaixo de 1, porque um robô colocado sem vida morreria na hora.

- **Só o ativo se move:** inativo ou em hibernação, o robô para e não teleporta. Em outra dimensão também para, porque o teleporte só vale na mesma dimensão.
- **Sair da lava vem antes de buscar água:** é o que mais fere. A rota nunca entra em lava, então a saída é direta, até o bloco seguro mais próximo (pergunta 139).
- **Em chamas, a reação vem antes de seguir:** só o robô ativo reage, porque é o único que se move. Parado, ele continua queimando (pergunta 117).
- **Distância inválida:** o robô para. É o estado mais restritivo, e o jogo não cai por isso (regra 17 do `AGENTS.md`).
- **Colocar e sair da base são a mesma regra:** o robô segue se nenhum outro estiver seguindo. Se houver outro, fica desligado, porque o clique direito só liga um robô desligado, e assim o player consegue fazê-lo seguir (perguntas 72, 75 e 102).
- **Sem bateria, o vínculo continua:** o robô para e, ao religar, volta a seguir. Ligar outro robô o desliga, e ele deixa de religar sozinho (pergunta 101).
- **Quem guarda o vínculo é quem chama:** a política recebe quem segue e devolve o resultado, sem ler os dados do player.
- **Outro robô assumiu o lugar:** um robô ativo que descobre isso se desliga. É o caso de quem estava em chunk descarregado na troca.
- **Vínculo apontando para o próprio robô:** não conta como outro robô seguindo. Assim, um vínculo antigo não deixa o robô desligado.

## Estados

Os documentos definem três estados: ativo, inativo e hibernação. No código, o inativo se divide em dois, porque a causa muda o comportamento:

| `PowerState` | Estado | Acesso ao inventário | Como sai |
|---|---|---|---|
| `ACTIVE` | Ativo | Sim | Bateria zerada ou troca de robô |
| `INACTIVE_EXHAUSTED` | Inativo, sem bateria | Não | Sozinho, ao atingir a carga mínima |
| `INACTIVE_SWITCHED_OFF` | Inativo, desligado com carga | Não | Clique direito, se houver carga |
| `HIBERNATING` | Hibernação, na base | Sim | Ao sair da base |

O desligamento pelo controle remoto (pergunta 56) entra na Fase 4, junto do controle.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 23:27 | Documenta a saída da lava. |
| Marcelo Fabiano | 2026-11-04 21:02 | Documenta a reação do robô em chamas. |
| Marcelo Fabiano | 2026-11-04 20:09 | Documenta o robô que descobre que outro assumiu o lugar. |
| Marcelo Fabiano | 2026-11-04 19:31 | Documenta o estado do robô e a chave do tier. |
| Marcelo Fabiano | 2026-11-04 19:12 | Documenta o catálogo de tiers. |
| Marcelo Fabiano | 2026-11-04 18:58 | Documenta o movimento e o robô que segue. |
| Marcelo Fabiano | 2026-11-04 18:18 | Documenta o tipo de robô e o tier. |
| Marcelo Fabiano | 2026-11-04 18:00 | Documenta a energia e os estados do domínio. |
