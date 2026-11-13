# Inventário e coleta

Números por tier: [`tiers.md`](../tiers.md#coleta-e-inventário) e [slots exclusivos](../tiers.md#slots-exclusivos).

## Interação

- **Clique direito:** abre o inventário, no estado ativo ou em hibernação. Desligado, valem as regras de [`energia.md`](energia.md#estados-do-cubit).
- **Shift + clique direito:** recolhe o robô. O acesso em cada estado está em [`energia.md`](energia.md#estados-do-cubit).

## Tela

- **Quem abre:** só o dono, com o robô ativo ou em hibernação e por perto. O servidor confere os três, ao abrir e enquanto a tela estiver aberta. Por perto é o alcance do player mais uma margem de 4 blocos, a mesma dos baús do jogo.
- **Visual:** a tela própria, descrita em [`telas.md`](telas.md#tela-do-cubit), com as linhas que o número de slots pedir (perguntas 371 e 676).
- **O que não entra:** o Cubit, o `Damaged Cubit` e os itens que o jogo não deixa entrar em containers, como shulker boxes.

## Conteúdo preservado

- Recolher, colocar, morrer, consertar e salvar o mundo nunca apagam nem duplicam itens, nem com a tela aberta.
- **Config reduzido:** ao colocar ou carregar o robô, o que não couber nos slots do config cai no chão, ao lado dele, com um aviso no log.
- **Excedente e coleta:** com a coleta ligada, o Cubit pode recolher do excedente o que couber numa pilha que ele já tem. Nada se perde nem duplica, e nenhum slot passa do config (pergunta 894).

## Coleta

Entra no item 9 da Fase 4.1 (respostas 422, 423 e 876 a 889). A coleta e o ímã são peças separadas, cada uma com a sua chave na [aba Behavior](telas.md#aba-behavior).

- **Inventário:** armazena itens.
- **Coleta:** desde o Base, o Cubit pega o item que encosta nele, com a caixa dele aumentada em 1 bloco, como o player. Não gasta energia.
- **Ímã:** do Advanced em diante, puxa até o Cubit os itens no raio do tier, com o movimento visível e sem teleportar. O item já pago é guiado a cada tick e coletado assim que encosta, e a 3 blocos chega em cerca de 0,3 s (pergunta 928). Gasta energia por item puxado, uma pilha no chão. No Base, fica bloqueado. Com a coleta desligada, o ímã fica inativo, porque puxar sem pegar só amontoaria itens.
- **Quando coleta:** só ativo, seguindo ou parado. Hibernando, desligado ou sem bateria, não coleta.
- **Inventário cheio:** pega o que cabe, juntando nas pilhas e nos slots livres, e o resto fica no chão. O ímã só puxa, e só cobra, os itens que cabem pelo menos em parte. Vale só o inventário principal, nunca os slots exclusivos.
- **Sem energia para o ímã:** ele não puxa, e a coleta por contato continua.
- **O que nunca pega:** o item ainda no atraso de coleta do jogo, como o que o player acabou de jogar, o Cubit e o `Damaged Cubit`, para o player não perder o rastro de um robô.
- **Varredura:** a coleta a cada 5 ticks e o ímã a cada 10 ticks, no config, só em chunks carregados (regras 21 e 22).
- **Estado inicial:** as duas chaves começam ligadas e ficam no item (pergunta 343).

### Filtro de coleta

- **Modos:** na lista branca, coleta só os itens definidos. Na lista negra, coleta tudo menos os itens definidos. O padrão é a lista negra.
- **Lista vazia:** na lista negra, pega qualquer item. Na lista branca, não pega nada.
- **Limite:** os slots do tier, iguais nos dois modos.
- **Comparação:** só o tipo do item, pelo ID. Uma espada encantada no filtro vale para qualquer espada de ferro.
- **Tags:** no Elite e no Ultimate, um slot pode valer por uma tag do item, como `#c:ores`, dentro do mesmo limite.
- **Edição:** por slots fantasma, na aba Behavior. O item não é consumido.
- **Vale também para o ímã:** o ímã só puxa o que a coleta pegaria.

## Descarregar em baú

O player marca um baú. Quando o inventário do robô enche, ele vai sozinho descarregar os itens que não estão nos slots exclusivos. O Cubit que segue também mostra o texto `Items unloaded`.

## Slots exclusivos

São dois: tochas e energia. Na Fase 5, o card de Aprendizado de Máquina ocupa mais um. O controle remoto ativa ou desativa as tochas, e o slot de energia fica sempre ligado. A fornalha de minérios foi para o [Minerador](trabalhos.md#minerador), na Fase 5, e o carregador de ferramentas saiu do mod (pergunta 930).

- **Tochas automáticas:** do Advanced em diante, com tochas no slot exclusivo, o robô as coloca sozinho no chão. No Base, o slot fica bloqueado (respostas 879 e 930).
- **Esvaziar:** o comportamento automático de esvaziar o inventário não coloca nem tira itens dos slots exclusivos.
- **Energia:** o robô extrai a energia da Power Bank ou da redstone para a bateria interna. Veja [`energia.md`](energia.md#slot-exclusivo-de-energia).

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-13 09:05 | Deixa só as tochas e a energia nos slots exclusivos. |
| Marcelo Fabiano | 2026-11-12 23:20 | Acelera o item puxado pelo ímã. |
| Marcelo Fabiano | 2026-11-12 21:45 | Detalha a coleta, o ímã, o filtro de coleta e o excedente recolhido. |
| Marcelo Fabiano | 2026-11-07 23:11 | Aponta a tela do inventário para as telas próprias. |
| Marcelo Fabiano | 2026-11-06 11:15 | Limita o texto da descarga ao Cubit que segue. |
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-06 09:32 | Define os dois modos do filtro de coleta. |
| Marcelo Fabiano | 2026-11-06 00:20 | Adiciona a barra de vida. |
| Marcelo Fabiano | 2026-11-05 23:49 | Detalha a tela própria. |
| Marcelo Fabiano | 2026-11-05 21:30 | Define a margem para abrir a tela. |
| Marcelo Fabiano | 2026-11-05 21:02 | Corrige quem abre a tela e o lugar do slot de energia, e cita o slot do card. |
| Marcelo Fabiano | 2026-11-05 13:30 | Deixa o visual e os tooltips do painel para a Fase 3. |
| Marcelo Fabiano | 2026-11-05 12:52 | Tira os slots exclusivos do esvaziar automático. |
| Marcelo Fabiano | 2026-11-05 02:11 | Define a tela e a preservação do conteúdo do inventário. |
| Marcelo Fabiano | 2026-11-04 17:26 | Limita a abertura do inventário aos estados ativo e hibernação. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as funções que o controle remoto desativa. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona as interações e o slot de energia. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
