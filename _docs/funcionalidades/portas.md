# Portas

O Cubit abre as portas, os alçapões e os portões de cerca que o dono marcou com o Tablet, emitindo energia, e fecha depois de passar (issue #223, perguntas 1359 a 1370). Os números estão em [`tiers.md`](../tiers.md#valores-gerais).

## Por quê

Sem isso, uma porta fechada separa o Cubit do dono e do trabalho: ele não segue o dono para dentro de casa nem chega aos baús de um cômodo fechado. Abrir toda porta seria inseguro num servidor, por isso só a porta marcada pelo dono conta (pergunta 1360).

## Marcar

- **App Portas:** um app novo do Tablet, que aparece para qualquer Cubit, sem card, e em todos os tiers (perguntas 1364 e 1367).
- **Clique direito** numa porta, num alçapão ou num portão de cerca marca o bloco. Num bloco já marcado, desmarca. Shift + clique desmarca o bloco clicado, ou, em outro lugar, a última porta marcada, como nos apps Descarga e Marcar.
- **Do dono:** as marcas valem para todos os Cubits do dono, e não para um Cubit só (pergunta 1364). Recolher, morrer ou trocar de Cubit não apaga as marcas.
- **Porta de duas partes:** marcar qualquer metade marca a porta inteira.
- **Limite:** 64 portas por dono, em todas as dimensões (pergunta 1369). Cheio, o Tablet avisa, e nada é marcado.
- **Setas:** com o Tablet na mão e o app Portas ativo, as portas marcadas ganham uma seta, como os baús marcados.
- **Outro bloco:** fora de uma porta, um alçapão ou um portão de cerca, o Tablet avisa, e nada é marcado.

## Blocos

Portas, alçapões e portões de cerca, de madeira e de ferro, pelas tags do jogo `minecraft:doors`, `minecraft:trapdoors` e `minecraft:fence_gates`, que também pegam os de outros mods (pergunta 1365).

## Abrir

- **Só para ele passar:** o Cubit abre a porta marcada que está no caminho dele, seguindo o dono, indo ao Dock, à área, a um baú ou trabalhando. O dono abre a própria porta como sempre (pergunta 1366).
- **Ferro também:** o Cubit faz o papel da redstone, com um pulso de energia, e abre a porta, o alçapão e o portão de ferro.
- **Custo:** 50 FE por abertura, igual em todos os tiers (pergunta 1368). A energia sai da bateria do Cubit, sem criar nem perder FE (regra 13).
- **Sem carga:** a porta fica fechada, e o caminho do Cubit não passa por ela, como hoje.
- **Desligado ou hibernando:** não abre nada.
- **Sinal:** ao abrir, uma faísca pequena no bloco e o som da porta do jogo.
- **Largura:** o Cubit tem 0,6 bloco de largura, como os mobs do jogo, e passa folgado pela porta aberta, que deixa 0,8125 livre (pergunta 1372).

## Fechar

- **Depois de passar:** o Cubit fecha a porta quando sai do bloco dela, como os aldeões (pergunta 1370).
- **Alguém na porta:** se o dono ou outra entidade estiver no bloco da porta, ele espera, e fecha quando o bloco fica livre.
- **Outro caminho:** se o Cubit abriu a porta e não passou por ela, fecha quando se afasta 2 blocos.
- **Fechar não custa energia.**
- **Porta aberta pelo player:** o Cubit só fecha a porta que ele mesmo abriu.

## Segurança e defesa

- **Dono:** o servidor confere o dono, o alcance e o chunk carregado em todo clique do Tablet (regra 20).
- **Marca inválida:** se o bloco marcado deixou de ser uma porta, a marca é ignorada e sai da lista na próxima vez que for lida.
- **Chunk descarregado:** o Cubit só lê portas em chunks carregados (regra 22).
- **Redstone do mundo:** o Cubit não emite sinal de redstone. Ele muda só a porta, sem acionar outros blocos.

## Contrato

| Onde | Nome | Conteúdo |
|---|---|---|
| Componente do Tablet | `cubit:tablet_app` | Ganha o valor `doors`, do app Portas |
| Dados do mundo | `cubit_door_marks` | As portas marcadas por dono: o UUID, a dimensão e a posição de cada uma. Ficam no mundo, e não no player, para o Cubit ler as marcas com o dono offline |
| Pacote do servidor | `cubit:door_marks` | As portas marcadas do player, para as setas do app Portas, a cada segundo com o Tablet na mão |
| Config | `door_open_cost` e `door_marks_limit`, em `general` | O custo de abrir, de 0 a 10.000 FE, com 50, e o limite de portas, de 1 a 256, com 64 |
| Tradução | `cubit.tablet.app.doors` e os textos do app | O nome do app e os avisos de marcar, desmarcar, limite cheio e bloco inválido |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-21 05:10 | Registra a largura do Cubit na porta (pergunta 1372). |
| Marcelo Fabiano | 2026-11-21 03:20 | Cria o conceito das portas (perguntas 1359 a 1370). |
