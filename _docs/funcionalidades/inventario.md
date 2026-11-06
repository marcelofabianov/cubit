# Inventário e coleta

Números por tier: [`tiers.md`](../tiers.md#coleta-e-inventário) e [slots exclusivos](../tiers.md#slots-exclusivos).

## Interação

- **Clique direito:** abre o inventário, no estado ativo ou em hibernação. Desligado, valem as regras de [`energia.md`](energia.md#estados-do-cubit).
- **Shift + clique direito:** recolhe o robô. O acesso em cada estado está em [`energia.md`](energia.md#estados-do-cubit).

## Tela

- **Quem abre:** só o dono, com o robô ativo ou em hibernação e por perto. O servidor confere os três, ao abrir e enquanto a tela estiver aberta.
- **Visual:** o do baú do jogo, com as linhas que o número de slots pedir. O slot de energia e a barra de bateria ficam num painel ao lado. A tela própria, com visual melhor e tooltips no painel, entra na Fase 3.
- **O que não entra:** o Cubit, o `Damaged Cubit` e os itens que o jogo não deixa entrar em containers, como shulker boxes.

## Conteúdo preservado

- Recolher, colocar, morrer, consertar e salvar o mundo nunca apagam nem duplicam itens, nem com a tela aberta.
- **Config reduzido:** ao colocar ou carregar o robô, o que não couber nos slots do config cai no chão, ao lado dele, com um aviso no log.

## Coleta

- **Inventário:** armazena itens.
- **Ímã:** puxa os itens do chão.
- **Filtro de coleta:** coleta só os itens definidos.

## Descarregar em baú

O player marca um baú. Quando o inventário do robô enche, ele vai sozinho descarregar os itens que não estão nos slots exclusivos e mostra o texto `Items unloaded`.

## Slots exclusivos

São quatro: tochas, fornalha, carregador de ferramentas e energia. Na Fase 5, o card de especialidade ocupa mais um. O controle remoto ativa ou desativa tochas, fornalha e carregador de ferramentas. O slot de energia fica sempre ligado.

- **Tochas automáticas:** com tochas no slot exclusivo, o robô as coloca sozinho no chão.
- **Fornalha de minérios:** funde minérios usando a bateria em vez de combustível, então fundir gasta autonomia.
- **Carregador de ferramentas elétricas:** carrega as ferramentas elétricas do player.
- **Esvaziar:** o comportamento automático de esvaziar o inventário não coloca nem tira itens dos slots exclusivos.
- **Energia:** o robô extrai a energia da Power Bank ou da redstone para a bateria interna. Veja [`energia.md`](energia.md#slot-exclusivo-de-energia).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 21:02 | Corrige quem abre a tela e o lugar do slot de energia, e cita o slot do card. |
| Marcelo Fabiano | 2026-11-05 13:30 | Deixa o visual e os tooltips do painel para a Fase 3. |
| Marcelo Fabiano | 2026-11-05 12:52 | Tira os slots exclusivos do esvaziar automático. |
| Marcelo Fabiano | 2026-11-05 02:11 | Define a tela e a preservação do conteúdo do inventário. |
| Marcelo Fabiano | 2026-11-04 17:26 | Limita a abertura do inventário aos estados ativo e hibernação. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as funções que o controle remoto desativa. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona as interações e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
