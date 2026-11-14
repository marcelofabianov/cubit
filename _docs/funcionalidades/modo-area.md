# Modo área

Números: [`tiers.md`](../tiers.md#autonomia) e [valores gerais](../tiers.md#valores-gerais).

Os comandos e o localizar do antigo controle remoto passaram para o [Tablet](tablet.md), que substitui o Cubit Remote (perguntas 998 a 1002).

## Modo área

O modo área entra com o item 13 da Fase 4.2, numa entrega só, depois do retorno automático do item 15 (perguntas 1057 e 1069). A descarga no baú marcado, o app Descarga e o botão `Descarregar` entram com o item 14, em [`inventario.md`](inventario.md#descarregar-em-baú).

### Marcar a área

A área é marcada com o app Área do [Tablet](tablet.md#apps), em três cliques direitos (perguntas 1051 e 1054):

1. **O Cubit:** o clique num Cubit do player, no mundo, faz dele o Cubit escolhido, e a barra de ação pede o primeiro canto. Com um Cubit Base, que não tem modo área, ou de outro player, a barra de ação diz o motivo. Clicar num bloco antes de escolher o Cubit só avisa.
2. **O primeiro canto:** o clique num bloco.
3. **O segundo canto:** o clique em outro bloco fecha um volume em 3D, e o Cubit vai para a área.

- **Limites:** os de [`tiers.md`](../tiers.md#autonomia), 16×16, 32×32 e 64×64, valem para a base, e a altura máxima, 8, 16 e 32 blocos, fica no config, por tier (perguntas 342 e 1050). Um segundo canto que passa do limite é recusado com aviso.
- **Alcance:** a área fica na mesma dimensão do Cubit, com o centro dentro do raio de chamada do Dock do tier, `call_radius_blocks`. Fora disso, a barra de ação diz o motivo (pergunta 1058).
- **Caixa:** com o Tablet na mão e o app Área, uma caixa vai do primeiro canto até o bloco na mira, com o tamanho, verde se cabe no tier e vermelha se passa. Depois, a caixa mostra a área do Cubit escolhido (pergunta 1056).
- **Marcação pela metade:** o Cubit e o primeiro canto ficam só na memória do servidor, por player, e somem ao trocar de app, ao sair do jogo ou com Shift + clique direito, que cancela. Uma nova área completa substitui a anterior do Cubit (pergunta 1060).

### O Cubit na área

- **Chegada:** ele deixa de seguir e vai andando. Preso sem progresso, teleporta para dentro da área, como na volta ao Dock (pergunta 1058).
- **Limite:** ele nunca sai da área, exceto para ir ao Dock.
- **Sem card:** com a chave `Pickup` ligada, busca os itens do chão dentro da área, respeitando o filtro de coleta. A chave `Fetch` vale só para quem segue o dono, e, com `Pickup` desligada, o Cubit só faz a ronda (perguntas 340 e 1080). O ímã continua puxando pelo raio do tier, mesmo de fora dela (pergunta 1064).
- **Ronda:** sem item para buscar, ele dá voltas contínuas pelas bordas da área, de canto a canto, na altura em que anda, dentro do volume, gastando com andar. Um canto que ele não alcança é pulado. Um item para buscar interrompe a ronda (perguntas 1059 e 1062).
- **Volume:** a área é um volume em 3D entre os dois cantos marcados. Conta como dentro dela também o bloco acima do topo, onde os itens ficam e o Cubit fica em pé sobre os blocos marcados, então dois cantos no chão já fazem uma área plana que funciona.
- **Defesa, tochas e lanterna:** seguem como hoje (pergunta 1059).
- **Inventário cheio:** com a chave `Unload` ligada e um baú marcado com espaço, vai descarregar e volta para a área ([descarga](inventario.md#descarregar-em-baú)). Sem isso, para de coletar e fica na área, e o estado mostra o inventário cheio, tentando descarregar de novo a cada intervalo (perguntas 1052, 1087 e 1088).
- **Bateria baixa:** pelo [retorno automático](energia.md#retorno-automático), que entra antes, vai ao Dock preferido, ou ao livre mais próximo no raio do tier, e com a carga cheia volta à área. Sem Dock, segue na área até o crítico e desliga, como seguindo (perguntas 1053, 1065 e 1068).
- **Dono longe:** o Cubit nunca teleporta até o dono e não é desligado pela perda de vínculo (pergunta 326). Com o chunk descarregado, para como o jogo para tudo e continua quando o chunk carregar, sem forçar o carregamento (pergunta 1061).
- **Estado:** `In area` (`Na área`), com cor própria, no topo da tela e na lista do Tablet. A bateria crítica e o inventário cheio aparecem antes dele (pergunta 1063).
- **Tela:** a aba Recursos do Tablet mostra a área, com os cantos e o tamanho.
- **Voltar a seguir:** o botão `Follow`, no cabeçalho da tela do Cubit no Tablet, que já entra com o retorno automático, também apaga a área e faz o Cubit voltar a seguir (perguntas 1055 e 1072).
- **Ao recolher:** a área, os baús marcados e o Dock preferido se perdem (perguntas 343 e 1008).
- **Com trabalho, na Fase 5:** vale [`trabalhos.md`](trabalhos.md).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 13:55 | Liga o inventário cheio à descarga (perguntas 1087 e 1088). |
| Marcelo Fabiano | 2026-11-14 12:10 | Define as chaves da busca na área (pergunta 1080). |
| Marcelo Fabiano | 2026-11-14 11:20 | Liga o `Follow` da área ao botão do #181 (pergunta 1072). |
| Marcelo Fabiano | 2026-11-14 10:00 | Liga a bateria baixa ao retorno automático (perguntas 1065 e 1069). |
| Marcelo Fabiano | 2026-11-14 09:00 | Define a marcação, a caixa e o Cubit na área (perguntas 1049 a 1064). |
| Marcelo Fabiano | 2026-11-13 16:40 | Vira o modo área, e os comandos e o localizar passam para o Tablet. |
| Marcelo Fabiano | 2026-11-13 09:05 | Deixa só as tochas entre as funções que o controle desativa. |
| Marcelo Fabiano | 2026-11-07 23:42 | Acrescenta os scanners ao consumo do Cubit parado. |
| Marcelo Fabiano | 2026-11-07 16:51 | Cita a marcação de um mob no modo só o alvo. |
| Marcelo Fabiano | 2026-11-06 13:34 | Adiciona a chave e o consumo da lanterna. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-05 21:34 | Define o parar, a área em 3D e o que o Cubit sem card faz nela. |
| Marcelo Fabiano | 2026-11-05 00:12 | Usa o termo modo de defesa. |
| Marcelo Fabiano | 2026-11-04 17:28 | Define o alcance do controle e da função de localizar. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Adiciona a função de localizar e o alcance do controle. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as funções que o controle remoto desativa. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona o comportamento ao desligar pelo controle. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
