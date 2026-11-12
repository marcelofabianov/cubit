# Combate

Números por tier: [`tiers.md`](../tiers.md#comportamento-e-combate).

O choque e os três modos de defesa entram juntos, nos itens 5 e 6 da Fase 4 (pergunta 822).

## Choque

Um choque leve, a única forma de o Cubit atacar. O som é o `shock_attack.ogg`, e a expressão não muda (perguntas 316 e 347).

- **Quando:** só com o Cubit ativo, seguindo ou parado, e nunca desligado nem hibernando (pergunta 825).
- **Sem perseguir:** ele continua seguindo o dono e dá o choque em quem estiver no alcance do tier, com linha de visão, uma vez a cada intervalo do tier.
- **Energia:** cada choque gasta o custo do tier. Sem carga para o custo, não dá o choque.
- **Quantos alvos:** um, o mais próximo. No Ultimate, todos os alvos válidos do modo no alcance de uma vez, pelo custo de um choque só (pergunta 826).
- **Visual:** faíscas elétricas do próprio jogo numa linha do Cubit até cada alvo, por um instante, vistas por todos por perto (pergunta 827).
- **Abate:** conta como do dono, como num lobo domesticado, e o mob solta XP e os itens que só caem por player. O tipo de dano próprio, `cubit:shock`, dá a mensagem `X was zapped by Cubit` (pergunta 831).

## Modos de defesa

O dono escolhe na aba Defense da tela do Cubit, entre os modos que o tier libera (perguntas 824 e 829). Os modos de cada tier ficam no config, na chave `defense_modes` (pergunta 834).

| Modo | No jogo | Tier | O que faz |
|---|---|---|---|
| Pacífico | `Peaceful` | Todos | Nunca ataca |
| Contra-atacar | `Retaliate` | A partir do Advanced | Dá choque no último mob que feriu o Cubit ou o dono, por cerca de 30 s, no config, ou até ele morrer ou mudar de dimensão. Um novo ataque renova o tempo (pergunta 832) |
| Agressivo | `Aggressive` | A partir do Elite | Dá choque nos mobs hostis no alcance, os que o jogo marca como `Enemy`, como nos scanners, e nos neutros que ficaram agressivos contra o dono. Ao trocar para este modo, toca o `aggressive_mode.ogg` (pergunta 362) |

- **Modo inicial:** Pacífico, também em saves antigos (pergunta 830).
- **O que guarda:** o modo fica no item, e recolher, morrer, consertar e o upgrade mantêm a escolha.

## Modo só o alvo (proposta)

Ideia aprovada para a Fase 4, onde será refinada. O modo, com o último mob atacado, entra na Fase 4, e a mira pelo controle, na 4.2, junto do Cubit Remote (pergunta 795). Nada aqui está decidido em detalhe, e os números ainda não estão em [`tiers.md`](../tiers.md) (perguntas 577 e 578).

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
| Sinais | A seta vermelha dos scanners, só sobre o alvo (pergunta 646), e `Cubit: Target: Zombie` |
| Risco a avaliar | Sem alvo, o Cubit fica parado enquanto um mob ataca o dono. O texto ao trocar de modo pode deixar isso claro |

## Quem o Cubit nunca ataca

O Cubit nunca ataca o próprio dono, outros players, outros Cubits nem mobs pacíficos, em nenhum modo de defesa. Isso inclui o choque em área do tier Ultimate, que precisa poupá-los. Animais de outros players contam como mobs pacíficos.

## Mobs neutros

O Cubit só ataca um mob neutro para defender o player, quando esse mob fica agressivo contra o player.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-12 10:30 | Define o choque e os três modos de defesa, com a aba Defense. |
| Marcelo Fabiano | 2026-11-10 10:17 | Separa a mira pelo controle do modo só o alvo para a Fase 4.2. |
| Marcelo Fabiano | 2026-11-07 23:42 | Troca a caixa do alvo pela seta. |
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
