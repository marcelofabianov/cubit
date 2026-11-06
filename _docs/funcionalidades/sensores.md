# Sensores, alertas e lanterna

Números por tier: [`tiers.md`](../tiers.md#sensores) e [lanterna](../tiers.md#identidade).

## Scanner de minérios

- Identifica os minérios informados no filtro do scanner.
- Usa a tag `c:ores` e suas subtags em vez de uma lista fixa. Assim, reconhece os minérios de todos os mods e funciona com mods que unificam itens.
- **Minério raro:** definido por uma lista configurável de tags, começando em ferro.
- A varredura roda em intervalos, nunca a cada tick.

## Scanner de mobs

Localiza a posição de mobs hostis.

## Alertas

- **Inimigo por perto:** expressão de alerta, bipe de alerta e texto `Enemy nearby`.
- **Minério raro:** expressão de surpresa e bipe de alerta.
- **Minério do filtro do scanner:** texto `Ore found`.
- **Bateria crítica:** texto `Critical battery`.

Os detalhes dos sons estão em [`visual.md`](../conceito/visual.md#sons), e os textos em [`persona.md`](../conceito/persona.md#comunicação).

## Lanterna

- Ilumina o ambiente ao redor.
- Usa a API do **LambDynamicLights**, dependência obrigatória, para o robô emitir luz, em vez de colocar blocos de luz invisíveis.
- **Claridade da tela:** com a tela acesa, o robô emite uma luz fraca, sem gastar energia, que apaga com ela (pergunta 378).
- O nível de luz do Minecraft vai de 0 a 15, por isso os tiers mais altos evoluem com um feixe à frente. A viabilidade do feixe será confirmada na Fase 3 (pergunta 17).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-06 00:00 | Adiciona a claridade da tela. |
| Marcelo Fabiano | 2026-11-05 21:02 | Avisa que o feixe depende da Fase 3 e tira a citação de um mod específico. |
| Marcelo Fabiano | 2026-11-05 12:21 | Marca o LambDynamicLights como dependência obrigatória. |
| Marcelo Fabiano | 2026-11-05 00:12 | Usa o termo mobs hostis. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o texto de minério para o filtro do scanner. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza as tags, o minério raro e os alertas. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
