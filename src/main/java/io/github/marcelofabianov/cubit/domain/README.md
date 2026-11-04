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
| `RobotTier` | Nível de evolução: Base, Advanced, Elite e Ultimate |
| `TierSpec` | Números de um tipo de robô num tier |

## Regras e porquês

- **Energia inteira:** evita erro de arredondamento, e uma transferência nunca cria nem perde FE.
- **Operações informam o que moveu:** `recharge` devolve o que foi aceito, e `consume`, o que foi consumido. Assim, quem chama sabe o que sobrou e não perde energia.
- **Comparação de porcentagem sem divisão:** `hasAtLeast` multiplica os dois lados, para ser exata com os valores de `tiers.md`.

- **Tipo de robô é dado:** as regras recebem os números de um `TierSpec`, sem saber qual é o robô. Assim, um robô novo não muda as regras (regra 39 do `AGENTS.md`).
- **Upgrade:** as regras de evolução entre tiers entram na Fase 4, junto dos módulos.

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
| Marcelo Fabiano | 2026-11-04 18:18 | Documenta o tipo de robô e o tier. |
| Marcelo Fabiano | 2026-11-04 18:00 | Documenta a energia e os estados do domínio. |
