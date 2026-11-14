# Alquimista

Conceito do trabalho de Alquimista, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o Alquimista entra como trabalho do Ultimate (pergunta 1146), mas o conceito abaixo ainda é uma proposta. Quase tudo está em [Em aberto](#em-aberto), respondido no refinamento do card (pergunta 1153).

## A ideia

Um Cubit que prepara poções sozinho. O player escolhe a poção que quer, e o Alquimista monta a cadeia de preparo nos suportes de poções marcados: enche as garrafas, põe os ingredientes na ordem certa, abastece o combustível e guarda as poções prontas. Ele segue o molde do Cozinheiro, que leva itens entre baús e blocos do jogo, mas com uma cadeia de várias etapas, o que justifica o Ultimate.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Alchemist | `cubit:ml_alchemist` | Card de Aprendizado de Máquina do trabalho de Alquimista |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada.

## Blocos que ele usa

Só blocos do jogo, sem máquina nova.

- **Suportes de poções marcados:** onde ele prepara, marcados com o app do Tablet que o primeiro card que precisar dele decide (pergunta 1155). Mexe neles pela capability de itens do NeoForge, como o Cozinheiro faz com as fornalhas.
- **Baú de entrada marcado:** de onde tira garrafas de vidro, verruga do Nether, ingredientes e pó de blaze.
- **Baú de saída marcado:** onde guarda as poções prontas.
- **Água da área:** uma fonte de água ou um caldeirão com água, para encher as garrafas.

## Como ele trabalha

### Pedido

Na aba Trabalho, o player escolhe a poção final e a quantidade. Por exemplo: Força II de arremesso, 6 unidades.

### Cadeia de preparo

O Alquimista calcula as etapas pela própria receita do jogo. No exemplo:

1. Garrafa de vidro → garrafa de água
2. Garrafa de água + verruga do Nether → poção estranha
3. Poção estranha + pó de blaze → Força
4. Força + pó de glowstone → Força II
5. Força II + pólvora → Força II de arremesso

Como ele lê as receitas do jogo, as poções que outros mods registrarem também entram.

### O ciclo

1. Confere se o baú de entrada tem tudo para o pedido.
2. Enche as garrafas na água da área.
3. Coloca até 3 garrafas num suporte livre, com o ingrediente da etapa e o pó de blaze como combustível.
4. Espera o preparo terminar, retira as poções e segue para a próxima etapa, no mesmo suporte ou em outro.
5. Na última etapa, guarda as poções no baú de saída.

Com vários suportes marcados, ele prepara lotes em paralelo.

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

### Pedido

1. **Pedido por poção final ou por sequência de ingredientes.** Recomendação: por poção final, com a cadeia calculada sozinha. Uma sequência montada à mão é mais flexível, mas mais difícil de usar.
2. **Um pedido ou uma fila.** Recomendação: uma fila curta de pedidos, atendidos em ordem, com 3 a 5 pedidos.
3. **Quantidade.** Recomendação: sempre em múltiplos de 3, que é uma carga do suporte. Um pedido de 4 vira 6.
4. **Modo contínuo.** Recomendação: uma opção para manter um estoque mínimo da poção no baú de saída, preparando de novo quando baixar.

### Materiais

5. **Água.** Recomendação: aceitar fonte de água e caldeirão. A fonte é infinita. O caldeirão gasta um nível por garrafa, e ele não reabastece o caldeirão.
6. **Garrafas de vidro.** Recomendação: só do baú de entrada. Ele não fabrica garrafas.
7. **Falta de material.** Recomendação: não começa um pedido sem ter tudo para a cadeia inteira. Se faltar no meio, termina a etapa em andamento, guarda o que está pronto num baú e avisa.
8. **Etapas intermediárias.** Recomendação: aceitar no baú de entrada poções já prontas de uma etapa, como poção estranha, e pular as etapas que elas cobrem.

### Suportes

9. **Itens deixados pelo player no suporte.** Recomendação: o Alquimista só usa suporte vazio, ou com o que ele mesmo colocou, e nunca retira o que o player deixou.
10. **Vários Alquimistas.** Recomendação: cada um reserva o suporte antes de usar, como as outras reservas de alvo.
11. **Combustível.** Recomendação: mantém o pó de blaze no slot de combustível de todo suporte marcado, sem esperar zerar.

### Saída e parada

12. **Baú de saída cheio.** Recomendação: fica com as poções no inventário e avisa. Sem espaço no inventário, hiberna.
13. **Nada a fazer.** Recomendação: fica parado perto dos suportes, sem gastar.

### Regras herdadas a confirmar

14. **Regras de área.** Recomendação: nunca sai da área, reserva o alvo antes de ir, tem limite de ações por tick e paga FE por ação.
15. **Combate.** Recomendação: não ataca. Atacado, foge.
16. **Log no Servidor.** Recomendação: registra pedidos concluídos, a falta de material e o baú de saída cheio.

### Visual e números

17. **Visual.** Recomendação: um frasco de vidro preso nas costas ou na garra, pela personalização por card da Fase 5.2.
18. **Números para `tiers.md`.** Entram em "A definir": o custo por ação do Alquimista, o tamanho da fila e o limite de suportes por Alquimista.

## Riscos

- **Cadeia pela receita do jogo:** calcular o caminho até a poção final exige uma busca nas receitas registradas. É a parte mais complexa do card e precisa de teste com as poções de outros mods do Iron Meridian.
- **Itens com dados:** as poções guardam o efeito nos data components. Mover e empilhar poções precisa preservar esses dados, sem duplicar nem perder itens.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:35 | Cria a proposta de conceito do Alquimista, com o pedido, a cadeia de preparo e os blocos do jogo. |
