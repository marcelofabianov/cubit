# Construtor

Conceito do trabalho de Construtor, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento base está decidido nas [perguntas 213 e 217](../../perguntas/fase-5-trabalhos.md). O tier mínimo, o Ultimate, está decidido na pergunta 1145. Os novos modos abaixo são proposta, e os detalhes em aberto são respondidos no refinamento do card (perguntas 1153 e 1154).

## A ideia

Um Cubit que prepara terreno: aplaina uma área tirando os blocos ou preenche os vazios com um bloco escolhido. É o primeiro passo antes de uma construção. Talvez evolua para o Arquiteto, que copia estruturas.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Builder | `cubit:ml_builder` | Card de Aprendizado de Máquina do trabalho de Construtor |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível só no Ultimate.

## Como ele trabalha

Definido nas perguntas 213 e 217, dentro da área 3D:

- **Aplainar:** retira os blocos permitidos, de cima para baixo.
- **Preencher:** usa o bloco do filtro em vazios e líquidos, de baixo para cima.
- **Parada:** sem o bloco ou com o inventário cheio, hiberna.
- **Proteção:** vale a regra de toda área, sem mexer em blocos com dados nem na tag de blocos protegidos.

## Novos modos (proposta)

No Ultimate, o Construtor é o trabalho mais simples do tier. Para ficar à altura dos outros, a proposta são dois modos novos, ainda simples e dentro da área:

| Modo | O que faz |
|---|---|
| Aplainar | Como hoje |
| Preencher | Como hoje |
| Piso | Troca só a camada de cima por um bloco do filtro, sem mexer no resto |
| Paredes | Ergue paredes com o bloco do filtro no contorno da área, na altura dela |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **Novos modos.** Recomendação: Piso e Paredes, como na tabela acima, e nada mais complexo até decidir o Arquiteto.
2. **Ficar no Ultimate.** Recomendação: fica, com os novos modos. Sem eles, vale reavaliar se ele desce para o Elite.
3. **O que aplainar retira.** Recomendação: tudo o que não for protegido, inclusive plantas e neve. Os itens vão para o inventário e para o baú marcado.
4. **Blocos do inventário ou do baú.** Recomendação: tira os blocos de um baú marcado quando acabam os do inventário, antes de hibernar.
5. **Líquidos no aplainar.** Recomendação: tampa a fonte com o bloco do filtro, se tiver, antes de seguir, para a água ou a lava não se espalhar.
6. **Queda.** Recomendação: nunca tira o bloco embaixo de si, como o Minerador.
7. **Visual.** Recomendação: um capacete de obra, pela personalização por card da Fase 5.2.
8. **Números para `tiers.md`.** Entram em "A definir": o custo por bloco tirado e por bloco colocado.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Construtor, com os modos das perguntas 213 e 217 e a proposta de Piso e Paredes no Ultimate. |
