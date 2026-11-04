# Sensores, alertas e lanterna

Números por tier: [`tiers.md`](../tiers.md#sensores) e [lanterna](../tiers.md#identidade).

## Scanner de minérios

- Identifica os minérios informados no filtro do scanner.
- Usa as tags `c:ores/*` em vez de uma lista fixa. Assim, reconhece os minérios de todos os mods e respeita a unificação do Almost Unified.
- A varredura roda em intervalos, nunca a cada tick.

## Scanner de mobs

Localiza a posição de mobs agressivos.

## Alertas

Emite um som quando está perto de inimigos e outro quando encontra minérios raros (de ferro para cima). Os detalhes estão em [`visual.md`](../concept/visual.md#sons).

## Lanterna

- Ilumina o ambiente ao redor.
- Usa a API do **LambDynamicLights** (já no pack) para o robô emitir luz, em vez de colocar blocos de luz invisíveis.
- O nível de luz do Minecraft vai de 0 a 15, por isso os tiers mais altos evoluem com um feixe à frente.
