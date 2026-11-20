# Movimento e robô que segue

Números: [`tiers.md`](../tiers.md#autonomia) e [valores gerais](../tiers.md#valores-gerais).

## Seguir ou atuar numa área

O player escolhe entre o robô seguir ou atuar numa área. A marcação da área está em [`modo-area.md`](modo-area.md).

## Movimento

- Por causa das esteiras, ele não pula; só sobe um bloco por vez.
- **Descer:** a rota desce de uma altura de até o limite de [`tiers.md`](../tiers.md#valores-gerais), sem dano de queda (pergunta 569).
- A rota evita água e lava, e ele não leva dano de queda.
- **Na água:** se cair nela, afunda e anda no fundo, sem nadar e sem se afogar. O teleporte o resgata quando o player se afasta.
- **Na lava:** leva dano igual ao player sem armadura e continua pegando fogo ao sair. Ativo, se colocarem lava no bloco dele, tenta ir para o bloco seguro mais próximo, mas a lava o deixa muito lento. Quanto menor o tier, maior a chance de morrer antes de sair, e no Base isso costuma acontecer. Inativo, fica parado.
- O robô que segue anda até o player e para quando chega perto.
- O robô que segue, se ficar longe demais do player, teleporta para perto, só na mesma dimensão.
- **Quando o dono renasce:** o robô ativo que segue volta com ele, em qualquer dimensão, se ainda estiver carregado nesse momento e longe demais. Um robô já descarregado continua onde está, porque nenhum chunk é carregado à força. Um robô a caminho do Dock continua indo, e passar por um portal não o leva (pergunta 567).
- Só se move no estado ativo. Inativo ou em hibernação, fica parado e não teleporta.
- Faz barulho de esteira ao andar. O som entra na Fase 3.

## Controle de velocidade

Entra com o card #212 da Fase 5 (perguntas 1258 a 1261 e 1266 a 1271). O player escolhe quão rápido o Cubit anda, sem passar da velocidade do tier.

- **Valor:** uma porcentagem da velocidade do tier, do mínimo ao máximo de [`tiers.md`](../tiers.md#valores-gerais), 50% a 100% por padrão, em passos de 10%. Como é uma porcentagem, o mesmo valor vale em qualquer tier e acompanha o upgrade (pergunta 1259).
- **Padrão:** 100%, fixo. Um Cubit que já existia, sem o valor salvo, anda a 100% (perguntas 1259 e 1266).
- **Onde muda:** nos botões − e + da linha `Speed` / `Velocidade`, na aba Settings da tela do Cubit e na aba Recursos do Tablet, descritas em [`telas.md`](telas.md#aba-settings) e [`tablet.md`](tablet.md) (perguntas 1260 e 1269). Só o dono muda, e o servidor confere o dono e o passo (regra 20).
- **Quando vale:** em toda caminhada: seguir o player, trabalhar na área, ir ao Dock, ir ao baú da descarga e buscar os itens do chão (pergunta 1268).
- **Perigo:** em chamas, na lava ou sobre um bloco que fere, o Cubit volta à velocidade cheia do tier, porque fugir vem primeiro, como com o inventário cheio (pergunta 1268).
- **Com o inventário cheio:** os dois redutores se multiplicam. A 80%, com o inventário cheio, ele anda a 80% de 0,75× da velocidade do tier (pergunta 1268).
- **Teleporte:** o teleporte do Cubit que fica para trás continua como está (pergunta 1260).
- **Energia:** andar mais devagar gasta mais energia no mesmo caminho, porque o consumo de andar é por tick, descrito em [`energia.md`](energia.md#consumo) (pergunta 1259).
- **Save:** fica no item e no NBT. Se o pack mudar o mínimo ou o passo, o valor salvo é ajustado para o degrau válido mais próximo, sem perder o save (pergunta 1266).

### Contrato

| Tipo | Nome | O que guarda |
|---|---|---|
| Componente do item | campo `speed_percent` do `cubit:robot_state` | A porcentagem escolhida, um inteiro, opcional, com 100 quando falta |
| NBT da entidade | `SpeedPercent` | A mesma porcentagem |
| Config, `general` | `speed_control_min_percent` | O menor valor dos botões, com 50, de 10 a 100 |
| Config, `general` | `speed_control_step_percent` | O passo dos botões, com 10, de 1 a 50 |
| Tradução | `Speed` / `Velocidade` e a dica | O nome e a descrição da linha (pergunta 1271) |

## Em chamas

Números: [valores gerais](../tiers.md#valores-gerais).

- **Ativo:** para de seguir e de teleportar, e anda mais rápido. Nunca fica parado enquanto houver para onde ir.
- **Com água por perto:** vai até ela para se apagar, em qualquer direção, inclusive acima ou abaixo por degraus. A busca vale só em chunks carregados. A rota só passa pela água nesse caso, ou se o robô já estiver nela.
- **Água sem rota:** água coberta por um bloco, ou sem caminho até ela, como atrás de uma parede, não serve. Ele tenta outra água ou anda ao acaso.
- **Sem água por perto:** anda ao acaso, evitando lava e fogo.
- **Inativo ou em hibernação:** fica parado, porque só o robô ativo se move.
- **Ao apagar:** volta a seguir na hora, pelas regras normais de movimento.
- **Som:** faz um curto elétrico enquanto pega fogo. O som entra na Fase 3.

## Sobre um bloco que fere

Vale para magma, fogueira acesa, cacto, arbusto de frutas, rosa do wither, estalactite e blocos de outros mods com a mesma classificação de rota no jogo.

- **Quando reage:** nas mesmas condições em que o jogo fere o player, com o bloco dentro da caixa de colisão do robô ou sendo pisado por ele. Um bloco só vizinho não conta.
- **Ativo:** para de seguir e vai ao bloco seguro mais próximo, na velocidade normal do tier, dentro do [alcance ao andar em chamas](../tiers.md#valores-gerais). Ao sair, volta a seguir. Sem bloco seguro no alcance, fica e tenta de novo.
- **Em chamas ou na lava:** essas reações vêm primeiro.
- **Inativo ou em hibernação:** fica parado e leva dano.
- **Energia:** custa o extra de [condição negativa](energia.md#consumo).

## Vários robôs, um seguindo

- O player pode ter quantos robôs preferir.
- **Só um robô segue o player** de cada vez.
- Pode haver um ou mais robôs ativos trabalhando em áreas ao mesmo tempo.
- O UUID do robô que segue fica guardado nos dados do player.
- **Colocar:** o robô segue se nenhum outro estiver seguindo. Se já houver um, o novo fica parado e desligado.
- **Sair da base:** vale a mesma regra de colocar.
- **Sem bateria:** o robô que segue fica parado, mantém o vínculo e volta a seguir ao religar. Colocado sem bateria, sem outro seguindo, assume o vínculo da mesma forma.
- **Ligar:** com bateria, o clique direito no robô desligado, sem redstone na mão, o liga, e ele passa a seguir o player. A exceção é o robô desligado pelo controle, que só volta pelo controle.
- **Chunk descarregado:** se o robô que seguia não estiver carregado na troca, ele se desliga quando voltar a carregar, ao ver que outro assumiu o lugar.
- **Trocar:** ao ligar um robô, ele desliga o que estava seguindo, mesmo que esteja sem bateria. Os que trabalham em áreas continuam ativos. Também dá para recolher o atual ou colocá-lo na base.
- **Base:** todos os robôs ligados podem usar a base, e cada Dock recebe um por vez. A chamada está em [`energia.md`](energia.md#ir-para-a-base-e-sair-dela).
- **Sem limite:** não há limite de quantidade.
- **Sem vínculo:** um robô ativo que não é o que segue, nem trabalha em área, fica parado e desligado. O clique direito o liga, e ele passa a seguir o player.

### Comando `/cubit reset`

Limpa o vínculo de quem segue, se ele se perder.

- **Quem usa:** qualquer player, sem permissão de operador, e só no próprio vínculo.
- **Robô que seguia:** vale a regra de ficar sem vínculo, logo depois do comando ou quando o chunk dele carregar. A carga é mantida.
- **Resposta:** `Follow link cleared.` ou, sem vínculo para limpar, `No Cubit is following you.`

## Vida e morte

- Ao morrer, o robô vira um `Damaged Cubit`, com o inventário preservado e a bateria zerada. Nenhum dano destrói o item, que não queima, mesmo na lava, e não some com o tempo. Se cair no vazio, ele é resgatado. As regras do item e o resgate estão em [`itens.md`](../itens.md#morrer-e-consertar).
- **Conserto:** com a `Toolbox`, na grade do inventário ou na mesa de craft. O robô sai com parte da vida, conforme [`tiers.md`](../tiers.md#valores-gerais), e a bateria zerada.
- **Vida:** se recupera na base, que também recupera o resto depois do conserto.
- **Dono:** nenhum dano que o jogo atribua ao dono fere o robô. O fogo de um isqueiro continua ferindo, porque o jogo não guarda quem o acendeu.

## Local de morte

Entra com o item 16 da Fase 4.2 (perguntas 1109 a 1126). Marca onde o player morreu e, se o tier permitir, o leva até lá.

### Histórico de mortes

- **O que entra:** toda morte do player, com ou sem Cubit, porque o histórico é dele (perguntas 1118 e 1124).
- **O que guarda:** o dia e a hora do jogo, como `Day 42, 18:30`, a partir do tempo do mundo, a coordenada, a dimensão e a causa, com o mesmo texto de morte que o jogo mostra no chat (perguntas 1111, 1114 e 1116).
- **Onde fica:** num dado próprio do mod no player, que sobrevive à morte. O jogo guarda só a última morte, sem a hora e sem a causa, e por isso não basta (pergunta 1114, que muda a 1110).
- **Limite:** até `death_log_size` mortes, 10 por padrão. Ao passar do limite, a mais antiga sai (pergunta 1115).
- **Excluir:** só o dono, pelo botão da linha na aba `Deaths` do Tablet, descrita em [`tablet.md`](tablet.md#aba-mortes).

### Por tier

| Tier | O que o Cubit faz |
|---|---|
| Base | Nada: a chave fica bloqueada. O histórico continua sendo registrado |
| Advanced | Marca a coordenada: o texto ao renascer |
| Elite e Ultimate | Marca e guia até lá, pelo botão do Tablet |

### Texto ao renascer

Do Advanced em diante, com a chave ligada, quando o dono renasce, o Cubit que segue mostra um texto curto com a coordenada e a dimensão da última morte, como `You fell at 120, 64, -340 · Overworld` (pergunta 1111). O Cubit não sai guiando sozinho: ele aguarda a ordem (pergunta 1113).

### Guia

- **Ordem:** pelo botão de guiar da linha da morte na aba `Deaths` do Tablet, que vira o botão de cancelar enquanto a guia dura (perguntas 1111 e 1113).
- **Quem guia:** só o Cubit escolhido no Tablet que segue o dono e está ativo, do Elite em diante, com a chave ligada (perguntas 1117, 1118 e 1120). Um Cubit desligado continua no vínculo, mas não anda, então não guia (pergunta 1130).
- **Onde:** só uma morte no Overworld, com o Cubit no Overworld (pergunta 1117).
- **Como:** o Cubit anda até o local, à frente do dono, até `death_guide_lead_blocks` blocos, 8 por padrão. Além disso, para e espera o dono. Assim ele nunca sai dos chunks carregados em volta do dono (perguntas 1112 e 1126, e regra 22).
- **Energia:** só o consumo de andar, sem extra (pergunta 1122).
- **Chegada:** a `death_guide_arrival_blocks` blocos do local, 3 por padrão, o Cubit para, diz `Here is where you fell` e volta a seguir. A linha da morte continua na aba até o dono excluí-la (perguntas 1121 e 1126).
- **Cancelar:** o botão de cancelar devolve o Cubit ao seguir (pergunta 1121).
- **Sem caminho:** sem avançar por `death_guide_stuck_seconds` segundos, 10 por padrão, o Cubit diz `I can't find a way there` e volta a seguir (perguntas 1123 e 1126).
- **Viagem ao Dock:** se a bateria chegar à do retorno automático, o retorno vence, e a guia é cancelada (pergunta 1121). O `Ir para o Dock` do Tablet também cancela a guia, porque é a mesma viagem (pergunta 1130).
- **Perigo:** fogo, lava ou um bloco que fere só pausam a guia. Ela continua quando o Cubit sai do perigo (pergunta 1130).
- **Ordem não salva:** recolher o Cubit, o dono sair do mundo ou o Cubit descarregar cancela a guia. O dono pede de novo pela aba (pergunta 1123).

### Chave

`Death point` / `Local de morte`, do Advanced em diante, ligada por padrão e guardada no item, na aba Settings da tela do Cubit e na aba Recursos do Tablet, com um ícone feito por prompt (perguntas 1118 e 1125). Ela liga o texto ao renascer e a guia. O histórico é registrado mesmo com ela desligada.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-19 20:18 | Define o controle de velocidade (perguntas 1258 a 1261 e 1266 a 1271). |
| Marcelo Fabiano | 2026-11-14 18:46 | Detalha quem guia, a viagem ao Dock e a pausa por perigo. |
| Marcelo Fabiano | 2026-11-14 18:14 | Define o local de morte, com o histórico, o texto ao renascer e a guia (perguntas 1109 a 1126). |
| Marcelo Fabiano | 2026-11-13 16:40 | Aponta a marcação da área para o modo área. |
| Marcelo Fabiano | 2026-11-07 16:20 | Define a altura que o robô desce. |
| Marcelo Fabiano | 2026-11-07 16:17 | Define a volta do robô com o dono que renasce. |
| Marcelo Fabiano | 2026-11-05 21:02 | Cita o clique sem redstone para ligar o robô. |
| Marcelo Fabiano | 2026-11-05 20:42 | Detalha o comando `/cubit reset` e o robô ativo sem vínculo. |
| Marcelo Fabiano | 2026-11-05 15:32 | Adiciona a reação aos blocos que ferem. |
| Marcelo Fabiano | 2026-11-05 01:15 | Aponta a proteção e o resgate do item para itens.md. |
| Marcelo Fabiano | 2026-11-05 00:12 | Detalha a morte, o conserto, a proteção do dono e a busca de água. |
| Marcelo Fabiano | 2026-11-04 23:27 | Define a saída da lava. |
| Marcelo Fabiano | 2026-11-04 22:01 | Define que o robô em chamas nunca fica parado. |
| Marcelo Fabiano | 2026-11-04 21:29 | Define a busca de água em qualquer altura e o caso sem rota. |
| Marcelo Fabiano | 2026-11-04 21:02 | Adiciona o comportamento em chamas. |
| Marcelo Fabiano | 2026-11-04 20:31 | Define o dano na lava e que o item do robô não queima. |
| Marcelo Fabiano | 2026-11-04 20:09 | Define o comportamento na água, o som e o robô em chunk descarregado. |
| Marcelo Fabiano | 2026-11-04 18:50 | Define a parada perto do player, a saída da base e o robô que segue sem bateria. |
| Marcelo Fabiano | 2026-11-04 17:26 | Limita o teleporte ao robô que segue e esclarece a exceção do controle. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Atualiza o uso da base por todos os robôs ligados. |
| Marcelo Fabiano | 2026-11-04 17:08 | Troca o limite de um robô por vários robôs com um seguindo. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona as regras do robô único, de vida e morte e os limites de movimento. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
