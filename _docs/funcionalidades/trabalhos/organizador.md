# Organizador

Conceito do trabalho de Organizador, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento base está decidido nas [perguntas 208, 212, 215, 249 e 250](../../perguntas/fase-5-trabalhos.md), mas a pergunta 208 deixa os detalhes ainda não fechados. O tier mínimo, o Elite, está decidido na pergunta 1145. A evolução por tier é proposta, e os detalhes em aberto são respondidos no refinamento do card (perguntas 1153 e 1154).

## A ideia

Um Cubit que mantém a base arrumada: recolhe o que as máquinas produzem e o que está no chão e guarda junto dos itens iguais. Também abastece lugares com quantidades fixas, o que antes seria o Fabricador.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Organizer | `cubit:ml_organizer` | Card de Aprendizado de Máquina do trabalho de Organizador |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Elite.

## Como ele trabalha

Definido nas perguntas 208, 212, 215, 249 e 250.

### Modo organizar

- **Recolher:** pega itens do chão e do que as máquinas e fornalhas oferecem como saída, pela capability do NeoForge, sem tocar em entradas e combustível.
- **Guardar:** usa só baús marcados. A prioridade é o baú que tem mais do mesmo item. Se nenhum tiver, usa o primeiro marcado com espaço.
- **Leitura:** roda em intervalos.

### Modo abastecer

Leva uma quantidade fixa de um baú a cada destino, por pedidos, sem craft. Não existe card de Fabricador: abastecer é um modo do Organizador.

## Evolução por tier (proposta)

O tier melhora o que o card faz (pergunta 192).

| Recurso | Elite | Ultimate |
|---|---|---|
| Modo organizar e modo abastecer | ✔ | ✔ |
| Baús marcados por Organizador | Base do config | O dobro |
| Intervalo de leitura | Base do config | Menor |
| Pedidos de abastecer | Poucos | Mais |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta. Os itens 1 a 5 fecham a pergunta 208.

1. **Um modo por vez.** Recomendação: o player escolhe organizar ou abastecer na aba Trabalho, um de cada vez, como o Minerador.
2. **"Primeiro baú marcado".** Recomendação: a ordem em que o player marcou com o Tablet, mostrada na aba Trabalho e reordenável ali.
3. **Itens sem lugar.** Recomendação: com todos os baús cheios, fica com o item no inventário, avisa e, sem espaço, hiberna.
4. **Máquinas de outros mods.** Recomendação: entram se oferecerem saída pela capability. Ele só tira o que a capability marca como saída.
5. **Pedido de abastecer.** Recomendação: cada pedido tem item, quantidade, baú de origem e destino. Ele completa o destino até a quantidade, sem passar dela, e repete a cada intervalo.
6. **Itens com dados.** Recomendação: ferramentas encantadas, poções e itens com nome contam como itens diferentes do mesmo item comum na regra do baú com mais.
7. **Visual.** Recomendação: uma prancheta ou uma caixa nas costas, pela personalização por card da Fase 5.2.
8. **Números para `tiers.md`.** Entram em "A definir": o custo por ação, o número de baús marcados, o intervalo de leitura e o número de pedidos por tier.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Organizador, com os modos das perguntas 208 a 250 e a proposta para fechar a pergunta 208. |
