# Sensores, alertas e lanterna

Números por tier: [`tiers.md`](../tiers.md#sensores) e [lanterna](../tiers.md#identidade).

## Quando os scanners funcionam

- **Quem varre:** só o Cubit ativo que segue, com a chave ligada. Desligado ou hibernando, ele não varre nem gasta (pergunta 554).
- **Chave:** o botão `Scanners`, no painel lateral do inventário, vale para os dois, começa ligado e é guardado no item, inclusive ao morrer e consertar (pergunta 549).
- **Custo:** cada varredura gasta uma quantia fixa de FE, parado ou andando, mesmo sem achar nada. Com menos carga que o custo, gasta o que resta (perguntas 550, 553 e 563).
- **Avisa só o que é novo:** o mesmo mob ou bloco não avisa de novo enquanto continuar no raio. Se sair e voltar, avisa como novo. A lembrança não entra no save, então ao carregar o mundo ele pode avisar uma vez (pergunta 561).

## Scanner de minérios

- Usa a tag `c:ores` e suas subtags em vez de uma lista fixa. Assim, reconhece os minérios de todos os mods e funciona com mods que unificam itens.
- **Minério raro:** definido por uma lista configurável de tags, da mais rara para a menos rara: sucata de netherita, esmeralda, diamante, ouro, lápis, redstone e ferro. Carvão, cobre e quartzo ficam de fora (pergunta 551).
- **Filtro:** o dono clica no Cubit com um minério na mão, em qualquer estado, sem gastar o item. Clicar com o mesmo minério limpa. O filtro é guardado no item (perguntas 548 e 554).
- **Varredura:** roda em intervalos, nunca a cada tick, em fatias limitadas por tick e só em chunks carregados.

## Scanner de mobs

Localiza os mobs que o jogo marca como `Enemy`, inclusive de outros mods e atrás de paredes, sem lista fixa (pergunta 552).

## Alertas

| Achado | Texto | Som | Expressão |
|---|---|---|---|
| Mob hostil novo | `Cubit: Careful, Zombie!`, com o mob mais próximo | `hostile_mob_alert.ogg` | Alerta, enquanto houver mob no raio |
| Minério raro novo | `Cubit: DIAMOND!!!`, com o mais raro | `rare_ore_alert.ogg` | Surpresa, por cerca de 3 s |
| Minério só do filtro, novo | `Cubit: COAL!!!` | Nenhum | Nenhuma |
| Bateria crítica | `Cubit: Critical battery` | Bipe padrão, ao trocar | Sono |

- **Nomes:** o do mob vem do próprio jogo e segue o idioma dele. O do minério vem da subtag, em inglês e em caixa alta. Um minério que só tem a tag `c:ores` mostra `Ore found` (perguntas 546 e 566).
- **Resposta ao clique do filtro:** `Cubit: Looking for DIAMOND` ao definir e `Cubit: Filter cleared` ao limpar, com o bipe padrão, sem o intervalo mínimo (pergunta 565).
- **Repetição:** cada nome espera o próprio intervalo mínimo, então `DIAMOND!!!` pode aparecer logo depois de `IRON!!!` (pergunta 547).
- **Sinais visuais:** a cor das antenas e o halo em volta do corpo, e as caixas em volta do que foi achado, entram nas duas entregas seguintes (perguntas 555 a 559).

Os detalhes dos sons estão em [`visual.md`](../conceito/visual.md#sons), e os textos em [`persona.md`](../conceito/persona.md#comunicação).

## Cor e caixas

Definidos, em entregas próprias depois da detecção:

- **Cor:** vermelho para mobs e azul para minérios, nas pontas e nos halos das antenas e num halo suave em volta do corpo, inclusive de dia e com a lanterna apagada. É só visual, sem custo e sem mudar a luz real, que continua branca. Todos os players por perto veem (pergunta 558).
- **Caixas:** caixa de linhas na mesma cor, só no cliente do dono, sem mixin e sem mexer no mob. A do minério dura cerca de 10 s ou até ele ser minerado, e a do mob, enquanto ele estiver no raio (perguntas 555 a 557). Desenhar através de blocos depende de uma prova técnica.

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
| Marcelo Fabiano | 2026-11-07 15:57 | Define os scanners, os alertas, a cor e as caixas. |
| Marcelo Fabiano | 2026-11-06 13:34 | Define a lanterna e leva o feixe para o Minerador. |
| Marcelo Fabiano | 2026-11-06 00:00 | Adiciona a claridade da tela. |
| Marcelo Fabiano | 2026-11-05 21:02 | Avisa que o feixe depende da Fase 3 e tira a citação de um mod específico. |
| Marcelo Fabiano | 2026-11-05 12:21 | Marca o LambDynamicLights como dependência obrigatória. |
| Marcelo Fabiano | 2026-11-05 00:12 | Usa o termo mobs hostis. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o texto de minério para o filtro do scanner. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza as tags, o minério raro e os alertas. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
