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
- **Luz final:** o maior valor entre a claridade da tela e a lanterna, e zero com a tela apagada ou o robô invisível. O servidor decide o nível e o envia ao cliente, porque o config não vai ao cliente.
- **Claridade da tela:** enquanto a tela brilha, inclusive hibernando, o robô emite uma luz fraca, sem gastar energia, que apaga com ela (perguntas 378, 465 e 471).
- **Quando acende:** só com o robô ativo e a chave ligada, no escuro. Acende e apaga em níveis diferentes, para não piscar perto do limite, e mantém o estado entre os dois (pergunta 470).
- **Luz lida:** o maior valor entre a luz de blocos e a do céu, com o céu escurecendo à noite e com chuva, em intervalos. A lanterna não conta, porque só existe no cliente (pergunta 472).
- **Consumo:** gasta só enquanto acesa, mesmo parado (pergunta 469).
- **Hibernando ou desligado:** apagada e sem consumo (pergunta 466).
- **Chave:** o botão `Lamp`, no painel lateral do inventário, começa ligado e é guardado no item, inclusive ao morrer e consertar. Fica desabilitado na hibernação, mantendo o valor. O Cubit Remote soma a chave na Fase 4 (perguntas 467, 468 e 473).
- **Opção do player:** se ele desligar a luz de entidades nas opções do LambDynamicLights, o Cubit não ilumina o mundo, e o resto continua igual.
- **Feixe:** é da lanterna de cabeça do Minerador, na Fase 5 (pergunta 463).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-06 13:34 | Define a lanterna e leva o feixe para o Minerador. |
| Marcelo Fabiano | 2026-11-06 00:00 | Adiciona a claridade da tela. |
| Marcelo Fabiano | 2026-11-05 21:02 | Avisa que o feixe depende da Fase 3 e tira a citação de um mod específico. |
| Marcelo Fabiano | 2026-11-05 12:21 | Marca o LambDynamicLights como dependência obrigatória. |
| Marcelo Fabiano | 2026-11-05 00:12 | Usa o termo mobs hostis. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o texto de minério para o filtro do scanner. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza as tags, o minério raro e os alertas. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
