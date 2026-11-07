# Combate

Números por tier: [`tiers.md`](../tiers.md#comportamento-e-combate).

## Choque

Aplica um choque leve em mobs hostis próximos, com o som de ataque descrito em [`visual.md`](../conceito/visual.md#sons).

## Modos de defesa

Definido pelo player:

- **Pacífico:** nunca ataca.
- **Contra-atacar:** ataca só quem atacou o Cubit ou o dono. Com mobs neutros, vale a regra abaixo.
- **Agressivo:** ataca os mobs hostis no alcance. Ao trocar para este modo, toca o som descrito em [`sons.md`](../conceito/sons.md).

## Modo só o alvo (proposta)

Ideia aprovada para a Fase 4, onde será refinada. Nada aqui está decidido em detalhe, e os números ainda não estão em [`tiers.md`](../tiers.md) (perguntas 577 e 578).

O Cubit ignora os inimigos por perto, mesmo os que atacam o dono, e só ataca o alvo que o dono escolheu.

| Ponto | Proposta |
|---|---|
| Nome no jogo | `Focus` |
| Tier | A partir do Advanced, porque o Base não tem choque |
| Quem vira alvo | O último mob que o dono feriu, inclusive com arco, ou o que ele marcar com o controle. O mais recente vale |
| Marcar com o controle | Mirar no mob e clicar com o Cubit Remote, até o alcance do controle. O servidor refaz a mira e confere dono e distância, porque um clique de mão vazia no ar não chega a ele |
| Quem nunca vira alvo | O dono, outros players, mobs pacíficos e outros Cubits, mesmo se o dono os atacar |
| Mob neutro | Vira alvo se o dono o atacar, porque ele passa a revidar |
| Sem alvo | Só segue o dono e não ataca ninguém |
| Fim do alvo | O alvo morre, muda de dimensão ou fica muito longe do dono |
| Perseguição | Vai atrás do alvo até um limite de distância do dono, no config, e depois volta |
| Sinais | A caixa vermelha dos scanners, só no alvo, e `Cubit: Target: Zombie` |
| Risco a avaliar | Sem alvo, o Cubit fica parado enquanto um mob ataca o dono. O texto ao trocar de modo pode deixar isso claro |

## Quem o Cubit nunca ataca

O Cubit nunca ataca o próprio dono nem mobs pacíficos, em nenhum modo de defesa. Isso inclui o choque em área do tier Ultimate, que precisa poupá-los. Animais de outros players contam como mobs pacíficos.

## Mobs neutros

O Cubit só ataca um mob neutro para defender o player, quando esse mob fica agressivo contra o player.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-07 16:51 | Adiciona a proposta do modo só o alvo. |
| Marcelo Fabiano | 2026-11-05 23:16 | Cita o som de modo agressivo. |
| Marcelo Fabiano | 2026-11-05 21:02 | Cita o som do ataque. |
| Marcelo Fabiano | 2026-11-05 00:12 | Usa os termos modos de defesa e mobs hostis. |
| Marcelo Fabiano | 2026-11-04 17:26 | Esclarece a regra de mobs neutros no modo contra-atacar. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o que cada modo de defesa faz. |
| Marcelo Fabiano | 2026-11-04 16:57 | Define os limites de ataque e a regra de mobs neutros. |
| Marcelo Fabiano | 2026-11-04 16:02 | Define a persona do Cubit. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
