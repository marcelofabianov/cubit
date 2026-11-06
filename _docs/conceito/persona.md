# Persona do Cubit

O Cubit é um companheiro **tecnológico**, moderno, fofo e doce, que ajuda o player em tarefas simples.

## Temperamento

**Leal e protetor.** O Cubit acompanha o dono e cuida dele. No tier Base, que não tem choque, ele protege avisando sobre o perigo.

## Comunicação

**Bipes e expressões.** O Cubit não fala, e demonstra o que sente pela expressão na tela do rosto e pelos bipes.

Só em algumas situações ele mostra um texto simples e direto, começando com `Cubit: `, logo acima da hotbar e só para o dono, vindo só do Cubit ativo que segue, com o intervalo mínimo de [`tiers.md`](../tiers.md#valores-gerais) entre repetições:

| Situação | Texto no jogo |
|---|---|
| Bateria crítica | `Critical battery` |
| O Cubit leva dano | `That hurts!` |
| O dono leva dano, por perto | `Careful!` |
| Inimigo próximo | `Enemy nearby` |
| Minério encontrado | `Ore found` |
| Tenho que ir carregar | `I need to recharge` |
| Itens descarregados | `Items unloaded` |

## Reações

| Situação | Expressão | Som |
|---|---|---|
| Sem nada acontecendo | Normal | Bipe padrão, ao trocar |
| Player interage com o Cubit, de qualquer forma | Feliz | Bipe padrão, ao trocar |
| Renomeado | Feliz | Bipe padrão, ao trocar |
| Pintado | Feliz | Bipe padrão, ao trocar |
| Recebe um upgrade | Feliz | Bipe padrão, ao trocar |
| Bateria crítica | Sono | Bipe padrão, ao trocar |
| Em hibernação na base | Carregando | Som de recarga |
| Recarregando pelo slot, ligado | Normal | Som de recarga |
| Desligado, mesmo recarregando pelo slot | Tela apagada | Som de desligar e, se recarregando, som de recarga |
| Ao ligar | Normal | Som de ligar |
| Colocado no mundo, ativo | Feliz | Som de colocar |
| Colocado no mundo, desligado | Tela apagada | Som de colocar |
| Enquanto pega fogo | Mantém a atual | Curto elétrico |
| Ao atacar | Mantém a atual | Som de ataque |
| Ao levar dano | Triste, com lágrima, por cerca de 5 s | Curto elétrico |
| Ao levar dano com pouca vida | Olhos em X, por cerca de 3 s, e depois a lágrima | Curto elétrico |
| O dono leva dano, por perto, no robô que segue | Preocupado, por cerca de 3 s | Nenhum |
| Inimigo por perto | Alerta | Bipe de alerta de inimigo |
| Minério raro encontrado | Surpreso | Bipe de alerta de minério |

O bipe padrão toca só nas trocas de expressão sem som exclusivo. Ao ligar, toca só o som de ligar, na passagem de desligado a ligado. Ao ser colocado, toca só o som de colocar. Os sons estão em [`visual.md`](visual.md#sons).

O barulho das esteiras ao andar é um efeito do movimento, não uma forma de comunicação.

## Limites

O Cubit **nunca ataca o próprio dono nem mobs pacíficos**, em nenhum modo de defesa, e só ataca mobs neutros para defender o player. Na Fase 5, o card de Pecuarista tem uma exceção, só na área de trabalho (pergunta 205), e o Defensor também protege quem está na área. As consequências para o combate estão em [`combate.md`](../funcionalidades/combate.md#quem-o-cubit-nunca-ataca).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-06 10:15 | Adiciona os textos de dano. |
| Marcelo Fabiano | 2026-11-06 10:06 | Define o prefixo e o lugar dos textos. |
| Marcelo Fabiano | 2026-11-06 09:48 | Define de quem vêm os textos e o intervalo. |
| Marcelo Fabiano | 2026-11-06 00:28 | Define o tempo dos olhos em X. |
| Marcelo Fabiano | 2026-11-06 00:22 | Adiciona a reação ao dano do dono. |
| Marcelo Fabiano | 2026-11-06 00:17 | Aumenta o tempo da expressão triste. |
| Marcelo Fabiano | 2026-11-06 00:12 | Define o som ao levar dano. |
| Marcelo Fabiano | 2026-11-06 00:10 | Adiciona as reações ao dano e à vida baixa. |
| Marcelo Fabiano | 2026-11-05 21:36 | Define a expressão ao atacar. |
| Marcelo Fabiano | 2026-11-05 21:30 | Define a reação ao ser colocado no mundo. |
| Marcelo Fabiano | 2026-11-05 21:02 | Cita os sons de colocar no mundo e de atacar. |
| Marcelo Fabiano | 2026-11-05 12:08 | Cita as exceções dos cards da Fase 5. |
| Marcelo Fabiano | 2026-11-05 00:56 | Tira a menção a magia da descrição. |
| Marcelo Fabiano | 2026-11-05 00:17 | Define a expressão ao ligar e ao pegar fogo. |
| Marcelo Fabiano | 2026-11-05 00:12 | Preenche os sons das reações e cita o intervalo mínimo dos textos. |
| Marcelo Fabiano | 2026-11-04 17:28 | Define a expressão ao recarregar pelo slot. |
| Marcelo Fabiano | 2026-11-04 17:15 | Atualiza os caminhos após a renomeação para português. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o sono na bateria crítica e encerra as perguntas abertas. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza a comunicação, as reações e os limites conforme as respostas. |
| Marcelo Fabiano | 2026-11-04 16:02 | Define a persona do Cubit. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona o rascunho da persona e o visual do Cubit. |
