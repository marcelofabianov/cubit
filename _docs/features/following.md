# Movimento e robô ativo

Números por tier: [`tiers.md`](../tiers.md#autonomia).

## Seguir ou atuar numa área

O player escolhe entre o robô seguir ou atuar numa área. A marcação da área está em [`remote.md`](remote.md).

## Movimento

- Por causa das esteiras, ele não pula; só sobe um bloco por vez.
- Se ficar a mais de uns 20 blocos do player, teleporta para perto.
- Faz barulho de esteira ao andar.

## Um robô ativo por player

- O player só pode ter **1 robô ativo** seguindo. Não é possível ter mais de um.
- O UUID do robô ativo fica guardado nos dados do player.

## Local de morte

Marca onde o player morreu e o leva até lá, se o tier permitir.
