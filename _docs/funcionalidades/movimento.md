# Movimento e robô que segue

Números: [`tiers.md`](../tiers.md#autonomia) e [valores gerais](../tiers.md#valores-gerais).

## Seguir ou atuar numa área

O player escolhe entre o robô seguir ou atuar numa área. A marcação da área está em [`controle-remoto.md`](controle-remoto.md).

## Movimento

- Por causa das esteiras, ele não pula; só sobe um bloco por vez.
- A rota evita água e lava, e ele não leva dano de queda.
- **Na água:** se cair nela, afunda e anda no fundo, sem nadar e sem se afogar. O teleporte o resgata quando o player se afasta.
- **Na lava:** leva dano igual ao player sem armadura e continua pegando fogo ao sair. Ativo, se colocarem lava no bloco dele, tenta ir para o bloco seguro mais próximo, mas a lava o deixa muito lento, e no tier Base ele costuma morrer antes de sair. Inativo, fica parado.
- O robô que segue anda até o player e para quando chega perto.
- O robô que segue, se ficar longe demais do player, teleporta para perto, só na mesma dimensão.
- Só se move no estado ativo. Inativo ou em hibernação, fica parado e não teleporta.
- Faz barulho de esteira ao andar. O som entra na Fase 3.

## Em chamas

Números: [valores gerais](../tiers.md#valores-gerais).

- **Ativo:** para de seguir e de teleportar, e anda mais rápido. Nunca fica parado enquanto houver para onde ir.
- **Com água por perto:** vai até ela para se apagar, em qualquer direção, inclusive acima ou abaixo por degraus. A rota só passa pela água nesse caso, ou se o robô já estiver nela.
- **Água sem rota:** água coberta por um bloco, ou sem caminho até ela, como atrás de uma parede, não serve. Ele tenta outra água ou anda ao acaso.
- **Sem água por perto:** anda ao acaso, evitando lava e fogo.
- **Inativo ou em hibernação:** fica parado, porque só o robô ativo se move.
- **Ao apagar:** volta a seguir na hora, pelas regras normais de movimento.

## Vários robôs, um seguindo

- O player pode ter quantos robôs preferir.
- **Só um robô segue o player** de cada vez.
- Pode haver um ou mais robôs ativos trabalhando em áreas ao mesmo tempo.
- O UUID do robô que segue fica guardado nos dados do player.
- **Colocar:** o robô segue se nenhum outro estiver seguindo. Se já houver um, o novo fica parado e desligado.
- **Sair da base:** vale a mesma regra de colocar.
- **Sem bateria:** o robô que segue fica parado, mantém o vínculo e volta a seguir ao religar. Colocado sem bateria, sem outro seguindo, assume o vínculo da mesma forma.
- **Ligar:** com bateria, o clique direito no robô desligado o liga, e ele passa a seguir o player. A exceção é o robô desligado pelo controle, que só volta pelo controle.
- **Chunk descarregado:** se o robô que seguia não estiver carregado na troca, ele se desliga quando voltar a carregar, ao ver que outro assumiu o lugar.
- **Trocar:** ao ligar um robô, ele desliga o que estava seguindo, mesmo que esteja sem bateria. Os que trabalham em áreas continuam ativos. Também dá para recolher o atual ou colocá-lo na base.
- **Base:** todos os robôs ligados podem usar a base, e cada Dock recebe um por vez. A chamada está em [`energia.md`](energia.md#ir-para-a-base-e-sair-dela).
- **Sem limite:** não há limite de quantidade, e o comando `/cubit reset` limpa o vínculo de quem segue, se ele se perder.

## Vida e morte

- Ao morrer, o robô vira item, com o inventário preservado e a bateria zerada. O item não queima, mesmo que o robô morra na lava.
- A vida fica guardada no item e se recupera na base.
- O dono não consegue ferir o próprio robô.

## Local de morte

Marca onde o player morreu e o leva até lá, se o tier permitir.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
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
