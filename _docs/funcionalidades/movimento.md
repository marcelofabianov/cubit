# Movimento e robô que segue

Números: [`tiers.md`](../tiers.md#autonomia) e [valores gerais](../tiers.md#valores-gerais).

## Seguir ou atuar numa área

O player escolhe entre o robô seguir ou atuar numa área. A marcação da área está em [`controle-remoto.md`](controle-remoto.md).

## Movimento

- Por causa das esteiras, ele não pula; só sobe um bloco por vez.
- A rota evita água e lava, e ele não leva dano de queda.
- O robô que segue anda até o player e para quando chega perto.
- O robô que segue, se ficar longe demais do player, teleporta para perto, só na mesma dimensão.
- Só se move no estado ativo. Inativo ou em hibernação, fica parado e não teleporta.
- Faz barulho de esteira ao andar.

## Vários robôs, um seguindo

- O player pode ter quantos robôs preferir.
- **Só um robô segue o player** de cada vez.
- Pode haver um ou mais robôs ativos trabalhando em áreas ao mesmo tempo.
- O UUID do robô que segue fica guardado nos dados do player.
- **Colocar:** o robô segue se nenhum outro estiver seguindo. Se já houver um, o novo fica parado e desligado.
- **Sair da base:** vale a mesma regra de colocar.
- **Sem bateria:** o robô que segue fica parado, mantém o vínculo e volta a seguir ao religar. Colocado sem bateria, sem outro seguindo, assume o vínculo da mesma forma.
- **Ligar:** com bateria, o clique direito no robô desligado o liga, e ele passa a seguir o player. A exceção é o robô desligado pelo controle, que só volta pelo controle.
- **Trocar:** ao ligar um robô, ele desliga o que estava seguindo, mesmo que esteja sem bateria. Os que trabalham em áreas continuam ativos. Também dá para recolher o atual ou colocá-lo na base.
- **Base:** todos os robôs ligados podem usar a base, e cada Dock recebe um por vez. A chamada está em [`energia.md`](energia.md#ir-para-a-base-e-sair-dela).
- **Sem limite:** não há limite de quantidade, e o comando `/cubit reset` limpa o vínculo de quem segue, se ele se perder.

## Vida e morte

- Ao morrer, o robô vira item, com o inventário preservado e a bateria zerada.
- A vida fica guardada no item e se recupera na base.
- O dono não consegue ferir o próprio robô.

## Local de morte

Marca onde o player morreu e o leva até lá, se o tier permitir.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 18:50 | Define a parada perto do player, a saída da base e o robô que segue sem bateria. |
| Marcelo Fabiano | 2026-11-04 17:26 | Limita o teleporte ao robô que segue e esclarece a exceção do controle. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Atualiza o uso da base por todos os robôs ligados. |
| Marcelo Fabiano | 2026-11-04 17:08 | Troca o limite de um robô por vários robôs com um seguindo. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona as regras do robô único, de vida e morte e os limites de movimento. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
