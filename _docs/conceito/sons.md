# Sons

Os arquivos ficam em `src/main/resources/assets/cubit/sounds/general_robot/`. Quem ouve, os gatilhos e o formato estão em [`visual.md`](visual.md#sons).

| Arquivo | Item | Para que é usado |
|---|---|---|
| `tracks.ogg` | Cubit | Esteiras ao andar, em loop, acompanhando a velocidade |
| `hostile_mob_alert.ogg` | Cubit | Alerta ao encontrar um mob hostil, mais alto que o original (pergunta 571) |
| `rare_ore_alert.ogg` | Cubit | Alerta ao encontrar um minério raro, mais alto que o original (pergunta 571) |
| `expression_beep.ogg` | Cubit | Bipe ao trocar para uma expressão sem som próprio |
| `short_circuit.ogg` | Cubit | Curto elétrico, em loop, enquanto pega fogo, e uma vez ao levar dano |
| `power_on.ogg` | Cubit | Ao passar de desligado a ligado |
| `power_off.ogg` | Cubit | Ao passar de ligado a desligado |
| `recharging.ogg` | Cubit | Em loop, enquanto a bateria sobe, na base ou pelo slot de energia |
| `shock_attack.ogg` | Cubit | Choque ao atacar |
| `placed.ogg` | Cubit | Ao ser colocado no mundo |
| `aggressive_mode.ogg` | Cubit | Ao trocar para o modo agressivo |

## Engineering Desk

Os arquivos ficam em `src/main/resources/assets/cubit/sounds/engineering_desk/`, a partir dos `.wav` fornecidos pelo desenvolvedor, convertidos para Ogg, com os originais em `tmp/wav/` (perguntas 1451, 1452 e 1477). A mesa é o primeiro contato do player com o mod, e os sons seguem o mesmo cuidado do visual (pergunta 1463).

| Arquivo | Para que é usado | Duração e clima |
|---|---|---|
| `crafting.ogg` | Em loop, enquanto a mesa cria um item. Para na pausa e no fim do pedido | 2 a 4 s, sem emenda audível no loop. Baixo e agradável: um zumbido eletrônico suave, com cliques leves do braço mecânico e de teclas, que não cansa depois de minutos |
| `done.ogg` | Quando cada item fica pronto | Menos de 1 s. Um "plim" eletrônico curto e satisfatório, com um leve estalo de faísca e o clique da gaveta fechando |
| `startup.ogg` | Na animação de ligação, ao colocar a mesa | 1 a 2 s. Um bipe de ligação simpático, da família dos bipes do Cubit, subindo de tom, como um computador fofo acordando |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Zennd | 2026-11-21 12:30 | Descreve os sons da Engineering Desk (perguntas 1451, 1452 e 1477). |
| Marcelo Fabiano | 2026-11-07 16:27 | Registra o volume maior dos alertas. |
| Marcelo Fabiano | 2026-11-06 00:12 | Adiciona o uso do curto elétrico ao levar dano. |
| Marcelo Fabiano | 2026-11-05 23:16 | Adiciona o som de modo agressivo. |
| Marcelo Fabiano | 2026-11-05 22:47 | Cria a lista dos sons. |
