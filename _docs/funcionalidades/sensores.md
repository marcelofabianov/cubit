# Sensores, alertas e lanterna

Números por tier: [`tiers.md`](../tiers.md#sensores) e [lanterna](../tiers.md#identidade).

## Quando os scanners funcionam

- **Quem varre:** só o Cubit ativo que segue, com a chave ligada. Desligado ou hibernando, ele não varre nem gasta (pergunta 554).
- **Chave:** a chave `Scanners`, na aba Settings da [tela do Cubit](telas.md#aba-settings), vale para os dois, começa ligado e é guardado no item, inclusive ao morrer e consertar (pergunta 549).
- **Área:** uma esfera em volta do Cubit, com um raio para cada scanner, maior no de mobs. Só conta o que está a até o raio em linha reta, em qualquer direção (perguntas 604 a 606 e 622).
- **Custo:** cada varredura gasta uma quantia fixa de FE, a mesma nos dois scanners, parado ou andando, mesmo sem achar nada. Com menos carga que o custo, gasta o que resta (perguntas 550, 563 e 610).
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
| Minério raro novo | `Cubit: Diamond Ore!!!`, com o mais raro | `rare_ore_alert.ogg` | Surpresa, por cerca de 3 s |
| Minério só do filtro, novo | `Cubit: Coal Ore!!!` | Nenhum | Nenhuma |
| Bateria crítica | `Cubit: Critical battery` | Bipe padrão, ao trocar | Sono |

- **Nomes:** o do mob vem do próprio jogo e segue o idioma dele. O do minério é o nome do primeiro bloco da subtag, no idioma do jogo, como `Diamond Ore` ou `Minério de Diamante`. Uma tag sem bloco mostra o nome da subtag, e um minério que só tem a tag `c:ores` mostra `Ore found` (perguntas 546, 566 e 734).
- **Resposta ao clique do filtro:** `Cubit: Looking for Diamond Ore` ao definir e `Cubit: Filter cleared` ao limpar, com o bipe padrão, sem o intervalo mínimo (pergunta 565).
- **Repetição:** cada nome espera o próprio intervalo mínimo, então `Diamond Ore!!!` pode aparecer logo depois de `IRON!!!` (pergunta 547).
- **Sinais visuais:** a cor das antenas, o halo em volta do corpo, a caixa em volta do minério e a seta sobre o mob, descritos em "Cor e caixas" (perguntas 555 a 559, 590 e 615).

Os detalhes dos sons estão em [`visual.md`](../conceito/visual.md#sons), e os textos em [`persona.md`](../conceito/persona.md#comunicação).

## Cor e caixas

Definidos, em entregas próprias depois da detecção:

- **Cor:** vermelho para mobs e azul para minérios, nas pontas e nos halos das antenas e num halo suave em volta do corpo, inclusive de dia e com a lanterna apagada. É só visual, sem custo e sem mudar a luz real, que continua branca. Todos os players por perto veem (pergunta 558).
- **Quando acende:** o vermelho, enquanto houver mob hostil no raio, mesmo com o Cubit ferido. O azul, por cerca de 3 s depois de achar um minério, raro ou do filtro, o mesmo tempo da surpresa. Com os dois, o vermelho vence. Depois, as antenas voltam ao estado anterior: brancas com a lanterna acesa, apagadas sem ela (perguntas 638 a 640).
- **Halo do corpo:** um brilho translúcido atrás do Cubit, sempre virado para a câmera, maior que ele (pergunta 641).
- **Tom:** o mesmo azul claro e vermelho claro das caixas (pergunta 642).
- **Sinal:** o servidor decide e envia a todos por um dado sincronizado, fora do save (pergunta 638).
- **Caixas e seta:** em volta do minério, uma caixa de linhas azul. Sobre a cabeça do mob, uma seta vermelha para baixo, ▼, preenchida, virada para a câmera e com um leve balanço. As duas ficam só no cliente do dono, sem mixin e sem mexer no mob, visíveis através dos blocos, num tom claro e translúcido, com um pulso suave (perguntas 555 a 557, 600, 614 e 646 a 649).
- **Que minério ganha caixa:** todos os minérios novos que a varredura achou dentro da esfera, os raros e os do filtro, mesmo quando o texto espera o intervalo de 25 s. Um minério já visto não ganha caixa de novo (perguntas 593, 597 e 608).
- **Quanto dura:** a caixa do minério, cerca de 10 s ou até ele deixar de ser minério, e a seta do mob, enquanto ele estiver no raio do Cubit (perguntas 556 e 599).
- **Como chega ao cliente:** um pacote só do servidor para o dono, com as posições dos minérios e os IDs dos mobs. O cliente só desenha e apaga a caixa quando o tempo acaba ou o bloco muda, e a seta quando o mob sai do raio (perguntas 613 e 646).

## Lanterna

- Ilumina o ambiente ao redor.
- Usa a API do **LambDynamicLights**, dependência obrigatória, para o robô emitir luz, em vez de colocar blocos de luz invisíveis.
- **Luz final:** o maior valor entre a claridade da tela e a lanterna, e zero com a tela apagada ou o robô invisível. O servidor decide o nível e o envia ao cliente, porque o config não vai ao cliente.
- **Claridade da tela:** enquanto a tela brilha, inclusive hibernando, o robô emite uma luz fraca, sem gastar energia, que apaga com ela (perguntas 378, 465 e 471).
- **Quando acende:** só com o robô ativo e a chave ligada, no escuro. Acende e apaga em níveis diferentes, para não piscar perto do limite, e mantém o estado entre os dois (pergunta 470).
- **Luz lida:** o maior valor entre a luz de blocos e a do céu, com o céu escurecendo à noite e com chuva, em intervalos. A lanterna não conta, porque só existe no cliente (pergunta 472).
- **Consumo:** gasta só enquanto acesa, mesmo parado (pergunta 469).
- **Hibernando ou desligado:** apagada e sem consumo (pergunta 466).
- **Chave:** a chave `Lamp`, na aba Settings da [tela do Cubit](telas.md#aba-settings), começa ligado e é guardado no item, inclusive ao morrer e consertar. Fica desabilitado na hibernação, mantendo o valor. O Cubit Remote soma a chave na Fase 4 (perguntas 467, 468 e 473).
- **Opção do player:** se ele desligar a luz de entidades nas opções do LambDynamicLights, o Cubit não ilumina o mundo, e o resto continua igual.
- **Feixe:** é da lanterna de cabeça do Minerador, na Fase 5 (pergunta 463).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-08 00:11 | Nomeia o minério pelo bloco, no idioma do jogo. |
| Marcelo Fabiano | 2026-11-07 23:42 | Troca a caixa do mob pela seta nos sinais e no pacote. |
| Marcelo Fabiano | 2026-11-07 23:11 | Aponta as chaves para a aba Settings. |
| Marcelo Fabiano | 2026-11-07 21:05 | Troca a caixa do mob por uma seta sobre a cabeça. |
| Marcelo Fabiano | 2026-11-07 20:51 | Detalha as cores das antenas e o halo do corpo. |
| Marcelo Fabiano | 2026-11-07 20:00 | Separa o raio dos dois scanners. |
| Marcelo Fabiano | 2026-11-07 19:16 | Detalha as caixas: o que ganha caixa, a duração, o pulso e o pacote. |
| Marcelo Fabiano | 2026-11-07 19:06 | Define a área em esfera e o mesmo custo nos dois scanners. |
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
