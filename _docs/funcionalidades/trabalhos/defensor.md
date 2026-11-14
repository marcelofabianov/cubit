# Defensor

Conceito do trabalho de Defensor, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento está decidido nas [perguntas 207, 211, 214, 244 e 245](../../perguntas/fase-5-trabalhos.md) e 1150, que também sobe o card para o Elite. Os detalhes em aberto são respondidos no refinamento do card (pergunta 1153).

## A ideia

Um Cubit que guarda uma área: protege quem está nela e ataca os mobs que ameaçam. É diferente do comportamento de defender o player ao seguir, que existe desde a Fase 4.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Defender | `cubit:ml_defender` | Card de Aprendizado de Máquina do trabalho de Defensor |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Elite.

## Como ele trabalha

Definido nas perguntas 207, 211, 214, 244 e 245:

- **Protege:** o dono, outros players, robôs, animais e aldeões dentro da área.
- **Limite:** nunca sai da área.
- **Drops:** guarda no inventário enquanto couberem, e o resto cai no chão. Não hiberna por inventário cheio e só para sem bateria. Com baú marcado, descarrega quando não houver ameaça.

### Reação pelo modo

Com o card a partir do Elite, todo Defensor já tem o modo Agressivo liberado. Por isso, o que decide a reação é o modo escolhido na aba Defense, e não o tier.

| Modo | Reação na área |
|---|---|
| Pacífico | Não fica disponível com este card. O Cubit é migrado para Contra-atacar (pergunta 244) |
| Contra-atacar | Ataca o mob que feriu o próprio Cubit ou qualquer protegido da área, hostil ou neutro. Não ataca por iniciativa |
| Agressivo | Faz o mesmo que Contra-atacar e também ataca por iniciativa qualquer mob hostil na área ou no alcance do choque |

### Ataque de longe

Decidido na pergunta 1150, detalhando as perguntas 207 e 214. Muda a regra de recuar sempre que um mob ataca de fora.

- **Agressor fora da área, no alcance:** se o mob ataca de fora da área, mas o choque do Cubit o alcança de dentro dela, o Defensor ataca sem sair da área. O alcance é o do choque do tier, com linha de visão.
- **Agressor fora do alcance:** se o choque não alcança o mob, como um esqueleto atirando de longe, o Defensor adota o comportamento defensivo: recua e procura abrigo dentro da área.
- **Fim do abrigo:** o Defensor fica abrigado até o agressor entrar na área ou no alcance do choque, e então ataca. Se o mob se afastar de novo, volta a se abrigar.

## Evolução por tier

Os modos são os mesmos nos dois tiers. O tier melhora o choque, com os valores que o Cubit já tem em `tiers.md`, mais o dano próprio do card (pergunta 211). Com o card a partir do Elite, a linha "Defensor: modos" de `tiers.md` perde a coluna do Avançado.

| Recurso | Elite | Ultimate |
|---|---|---|
| Modos | Contra-atacar e Agressivo | Contra-atacar e Agressivo |
| Alcance do choque | 5 blocos | 8 blocos |
| Intervalo do choque | 2 s | 1 s |
| Alvos por choque | 1, o mais próximo | Todos no alcance (pergunta 826) |
| Dano | Base do card | Maior |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **Modo Só o alvo.** Ele dá choque no último mob que o dono feriu, o que depende do dono estar perto. Recomendação: não fica disponível com este card, como o Pacífico, e o Cubit é migrado para Contra-atacar. A linha "Todos, sem Pacífico" do Ultimate em `tiers.md` vira "Contra-atacar e Agressivo".
2. **Chegar à borda para alcançar.** Antes de recuar, o Defensor pode andar até a borda da área para trazer o agressor para dentro do alcance. Recomendação: sim, se a borda deixar o agressor no alcance, e ele recua só quando nem da borda o choque alcança.
3. **Onde ele se esconde.** Recomendação: atrás de um bloco sólido dentro da área, fora da linha de visão do atirador. Sem esconderijo, fica no ponto mais longe do mob.
4. **Creeper.** Recomendação: ataca o creeper de longe, ou recua se ele estiver prestes a explodir, para a explosão não ferir o que está na área.
5. **Vários Defensores.** Recomendação: cada um reserva um mob como alvo, e os outros escolhem outro.
6. **Prioridade de alvo.** Recomendação: primeiro o mob que está agredindo alguém da área, depois o mais próximo de um protegido.
7. **Mecânico na mesma área.** O Mecânico só repara quem não recebeu dano nos últimos segundos (item 7 em aberto do [`mecanico.md`](mecanico.md)). Recomendação: manter, para o Defensor não ser curado no meio da luta.
8. **Visual.** Recomendação: um capacete ou um escudo no braço, pela personalização por card da Fase 5.2.
9. **Números para `tiers.md`.** O dano do Defensor já está em "A definir". Entra também o custo por ataque.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 12:02 | Troca a reação pelo tier pela reação pelo modo e mostra a evolução pelo choque do tier. |
| Marcelo Fabiano | 2026-11-13 11:59 | Define o ataque a mobs hostis na área e o alcance do choque contra agressores de fora, com o abrigo só fora do alcance. |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Defensor, com o comportamento das perguntas 207 a 245 e a mudança para o Elite. |
