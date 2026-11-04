# Inventário e coleta

Números por tier: [`tiers.md`](../tiers.md#coleta-e-inventário) e [slots exclusivos](../tiers.md#slots-exclusivos).

## Interação

- **Clique direito:** abre o inventário, no estado ativo ou em hibernação. Desligado, valem as regras de [`energia.md`](energia.md#estados-do-cubit).
- **Shift + clique direito:** recolhe o robô. O acesso em cada estado está em [`energia.md`](energia.md#estados-do-cubit).

## Coleta

- **Inventário:** armazena itens.
- **Ímã:** puxa os itens do chão.
- **Filtro de coleta:** coleta só os itens definidos.

## Descarregar em baú

O player marca um baú. Quando o inventário do robô enche, ele vai sozinho descarregar os itens que não estão nos slots exclusivos e mostra o texto `Items unloaded`.

## Slots exclusivos

São quatro: tochas, fornalha, carregador de ferramentas e energia. O controle remoto ativa ou desativa tochas, fornalha e carregador de ferramentas. O slot de energia fica sempre ligado.

- **Tochas automáticas:** com tochas no slot exclusivo, o robô as coloca sozinho no chão.
- **Fornalha de minérios:** funde minérios usando a bateria em vez de combustível, então fundir gasta autonomia.
- **Carregador de ferramentas elétricas:** carrega as ferramentas elétricas do player.
- **Energia:** o robô extrai a energia da Power Bank ou da redstone para a bateria interna. Veja [`energia.md`](energia.md#slot-exclusivo-de-energia).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 17:26 | Limita a abertura do inventário aos estados ativo e hibernação. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as funções que o controle remoto desativa. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona as interações e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
