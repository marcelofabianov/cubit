# Visual e sons

![Prancha de conceito do Cubit](../imagens/conceito/robo-conceito-prancha.png)

As imagens individuais estão em [`_docs/imagens/conceito/`](../imagens/conceito/), em PNG e SVG.

## Estilo

- **Moderno, fofo e doce:** um cubo branco arredondado com uma tela no rosto onde os olhos são luzes, antenas, braços com garras e **esteiras** no lugar dos pés.
- **Colar** no pescoço com a cor do tier. As cores estão em [`tiers.md`](../tiers.md).
- **Carcaça** com cor trocável por corantes.
- O conceito atual é a base. O modelo final tem **design próprio**, para o mod poder ser publicado. A geometria melhora já na Fase 2, mais próxima das imagens de conceito, e as animações e as expressões seguem na Fase 3.
- Modelo e animações com **GeckoLib** (já presente no pack): andar, virar a cabeça, mover os braços e trocar a expressão da tela.

## O que o conceito mostra

- **Vistas:** frente, costas, esquerda, direita, cima e baixo.
- **Corpo:** tela escura no rosto, olhos ciano e antenas com ponta laranja.
- **Detalhes:** painel solar em cima, indicador de bateria nas costas e bobina de indução embaixo. O painel carrega a bateria, conforme [`energia.md`](../funcionalidades/energia.md#painel-solar).
- **Desligado:** a tela apaga e as pontas das antenas ficam cinza.

## Expressões

São sete: normal, feliz, sono, alerta, surpreso, carregando e desligado. Os gatilhos estão em [`persona.md`](persona.md#reações).

## Sons

São oito sons diferentes, que entram na Fase 3 (perguntas 135 e 148):

| # | Quando toca | Som | Arquivo |
|---|---|---|---|
| 1 | Ao andar | Esteiras, como as de um tanque de guerra, em loop e acompanhando a velocidade | `tracks.ogg` |
| 2 | Ao encontrar um mob hostil | Alerta, com intervalo mínimo entre repetições | `hostile_mob_alert.ogg` |
| 3 | Ao encontrar um minério raro | Alerta, com intervalo mínimo entre repetições | `rare_ore_alert.ogg` |
| 4 | Ao trocar para uma expressão sem som exclusivo: normal, feliz ou sono | Bipe padrão | `expression_beep.ogg` |
| 5 | Enquanto pega fogo | Curto elétrico | `short_circuit.ogg` |
| 6 | Ao ligar | Próprio | `power_on.ogg` |
| 7 | Ao desligar | Próprio | `power_off.ogg` |
| 8 | Enquanto é recarregado | Próprio | `recharging.ogg` |

Os arquivos ficam em `assets/cubit/sounds/general_robot/`, em Ogg Vorbis, mono e 48 kHz, porque o jogo só aceita esse formato e só reduz o volume com a distância em sons mono. Ainda não tocam no jogo. O valor do intervalo mínimo será definido na Fase 3, num teste no jogo, e ficará no config.

## Identificação

- **Nome personalizado** com bigorna ou etiqueta, aparecendo no Jade.
- **Jade:** mostrar nome, tier, bateria e modo atual.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 00:12 | Limita o bipe padrão, antecipa a geometria e cita o intervalo mínimo. |
| Marcelo Fabiano | 2026-11-04 22:51 | Adiciona os arquivos dos sons. |
| Marcelo Fabiano | 2026-11-04 22:12 | Define os oito sons do Cubit. |
| Marcelo Fabiano | 2026-11-04 17:15 | Atualiza os caminhos após a renomeação para português. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o modelo final na Fase 3. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza as expressões, os sons e o painel solar. |
| Marcelo Fabiano | 2026-11-04 16:02 | Define a persona do Cubit. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona o rascunho da persona e o visual do Cubit. |
