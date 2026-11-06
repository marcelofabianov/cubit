# Visual e sons

![Prancha de conceito do Cubit](../imagens/conceito/robo-conceito-prancha.png)

As imagens individuais estão em [`_docs/imagens/conceito/`](../imagens/conceito/), em PNG e SVG.

## Estilo

- **Moderno, fofo e doce:** um cubo branco arredondado com uma tela no rosto onde os olhos são luzes, antenas, braços com garras e **esteiras** no lugar dos pés.
- **Colar** no pescoço com a cor do tier. As cores estão em [`tiers.md`](../tiers.md).
- **Carcaça** com cor trocável por corantes.
- O conceito atual é a base. O modelo final tem **design próprio**, para o mod poder ser publicado. A geometria melhora já na Fase 2, mais próxima das imagens de conceito, e as animações e as expressões seguem na Fase 3.
- Modelo e animações com **GeckoLib**, dependência obrigatória: andar, virar a cabeça, mover os braços e trocar a expressão da tela.
- **Modelo na Fase 2:** traz os detalhes das seis vistas e o ícone do item, com tudo fixo: colar cinza, olhos normais e bateria sem variação. A textura tem 128×128, com 2 pixels por unidade, e a hitbox não muda, então braços e antenas passam um pouco dela.

## O que o conceito mostra

- **Vistas:** frente, costas, esquerda, direita, cima e baixo.
- **Corpo:** tela escura no rosto, olhos ciano e antenas com ponta laranja.
- **Detalhes:** painel solar em cima, indicador de bateria nas costas e bobina de indução embaixo. O painel carrega a bateria, conforme [`energia.md`](../funcionalidades/energia.md#painel-solar).
- **Desligado:** a tela apaga e as pontas das antenas ficam cinza.

## Expressões

São sete: normal, feliz, sono, alerta, surpreso, carregando e desligado. Os gatilhos estão em [`persona.md`](persona.md#reações).

## Sons

São onze sons diferentes (perguntas 135, 148, 316, 320, 322 e 362). Nove entram na Fase 3, o do ataque, junto do choque, e o de modo agressivo, junto dos modos de defesa. A lista, com o arquivo e o uso de cada um, está em [`sons.md`](sons.md).

Os arquivos ficam em `assets/cubit/sounds/general_robot/`, em Ogg Vorbis, mono e 48 kHz, porque o jogo só aceita esse formato e só reduz o volume com a distância em sons mono. O valor do intervalo mínimo será definido na Fase 3, num teste no jogo, e ficará no config.

Tocam todos, menos o bipe padrão, que entra com as expressões, os alertas, que entram com os scanners, e o ataque e o modo agressivo, que entram na Fase 4.

- **IDs:** `cubit:general_robot.{nome}`, com o nome do arquivo sem a extensão. Não mudam sem aviso.
- **Quem ouve:** todos os players por perto, com o volume caindo até cerca de 16 blocos, no controle "Criaturas amigáveis" do jogo. Volume e alcance ficam no `sounds.json`, onde um pacote de recursos pode mudá-los.
- **Ligar:** de inativo para ativo, pelo clique, ao religar sozinho ou ao sair da base ligado.
- **Desligar:** de ativo para inativo, por bateria zerada, troca de robô ou `/cubit reset`.
- **Sem esses sons:** entrar na base e ser colocado no mundo.
- **Esteira:** em loop, enquanto anda, com volume e altura acompanhando a velocidade.
- **Curto elétrico:** em loop, enquanto pega fogo, ativo ou não, junto da esteira.
- **Recarga:** em loop, enquanto a bateria sobe de fato, na base ou pelo slot. Para com a bateria cheia ou com o Dock sem energia.

## Identificação

- **Ícone do mod:** tem o mesmo desenho do ícone do item, em `textures/gui/mod_logo.png`, sem borrar os pixels. O arquivo é separado porque a tela de Mods sempre encaixa o logo numa altura de 50 pixels, então o tamanho vem da margem transparente, e 25×25 dá a escala exata de 2×. Ao mudar o ícone do item, atualize também este arquivo.
- **Nome personalizado** com bigorna ou etiqueta, aparecendo no Jade.
- **Jade:** mostrar nome, tier, bateria e modo atual.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 23:16 | Adiciona o som de modo agressivo. |
| Marcelo Fabiano | 2026-11-05 22:47 | Passa a lista dos sons para `sons.md`. |
| Marcelo Fabiano | 2026-11-05 22:39 | Define quando cada som toca e quem ouve. |
| Marcelo Fabiano | 2026-11-05 21:30 | Separa o som de ligar do som de colocar. |
| Marcelo Fabiano | 2026-11-05 21:02 | Trata a GeckoLib como dependência do mod. |
| Marcelo Fabiano | 2026-11-05 20:36 | Define a fase do som de colocar no mundo. |
| Marcelo Fabiano | 2026-11-05 20:26 | Adiciona o som de colocar no mundo. |
| Marcelo Fabiano | 2026-11-05 17:27 | Adiciona o som do ataque. |
| Marcelo Fabiano | 2026-11-05 00:55 | Reduz o ícone do mod na tela de Mods. |
| Marcelo Fabiano | 2026-11-05 00:35 | Define o ícone do mod. |
| Marcelo Fabiano | 2026-11-05 00:31 | Define o modelo da Fase 2. |
| Marcelo Fabiano | 2026-11-05 00:12 | Limita o bipe padrão, antecipa a geometria e cita o intervalo mínimo. |
| Marcelo Fabiano | 2026-11-04 22:51 | Adiciona os arquivos dos sons. |
| Marcelo Fabiano | 2026-11-04 22:12 | Define os oito sons do Cubit. |
| Marcelo Fabiano | 2026-11-04 17:15 | Atualiza os caminhos após a renomeação para português. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define o modelo final na Fase 3. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza as expressões, os sons e o painel solar. |
| Marcelo Fabiano | 2026-11-04 16:02 | Define a persona do Cubit. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona o rascunho da persona e o visual do Cubit. |
