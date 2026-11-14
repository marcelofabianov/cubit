# Minerador

Conceito do trabalho de Minerador, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o comportamento está decidido nas [perguntas 197 a 203, 210, 234 e 344](../../perguntas/fase-5-trabalhos.md) e na 1149, que sobe o card para o Elite, tira Inquebrável e Remendo e define a fornalha elétrica. Os detalhes em aberto são respondidos no refinamento do card (pergunta 1153).

## A ideia

Um Cubit que minera sozinho dentro de uma área, sem picareta, com a força do tier. Escava, abre túneis ou busca minérios, e guarda o que tirar.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Miner | `cubit:ml_miner` | Card de Aprendizado de Máquina do trabalho de Minerador |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada. Disponível a partir do Elite.

## Como ele trabalha

Definido nas perguntas 197 a 203, 210, 234 e 344:

- **Sem ferramenta:** minera sem item de picareta, com a capacidade do tier.
- **Modos:** escava a área, abre túnel reto ou em degraus, ou busca minérios do filtro. É um modo por vez, sempre dentro da área. É uma ideia inicial, com os detalhes na entrega do card.
- **Opções:** Eficiência, Fortuna e Toque Suave agem como no jogo. Fortuna e Toque Suave não ficam ativos juntos, e o player alterna entre eles na tela.
- **Custo:** paga conforme a dureza do bloco.
- **Fornalha de minérios:** o slot exclusivo de fornalha é só do Minerador. A fornalha é elétrica: não usa combustível e gasta FE da bateria só enquanto está ativa, com a velocidade do tier em `tiers.md`. Ativa, todo minério que ele minera vai para a fornalha, e só depois de fundido o resultado é enviado ao inventário do Cubit. O player a liga e desliga pela chave na tela do Cubit, na seção Functions, junto das outras chaves de recursos, como lanterna e painel solar. Como as outras chaves, ela também aparece na aba Recursos do Tablet (pergunta 1037). Sem o card, o Cubit não tem fornalha. Responde a pergunta 339 (pergunta 1149).
- **Lanterna de cabeça:** com um feixe à frente, como na prancha do conceito. A viabilidade e o alcance ficam para a entrega do card.

## Evolução por tier

Com o card a partir do Elite, a coluna do Avançado sai de `tiers.md`. O resto continua igual.

| Recurso | Elite | Ultimate |
|---|---|---|
| Equivale a picareta de | Diamante | Diamante |
| Eficiência | III | V |
| Fortuna | II | III |
| Toque Suave | — | ✔ |
| Fornalha de minérios | 1 slot, 1× | 1 slot, 2× |

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

1. **Força no Elite.** Recomendação: diamante, como já está no Elite em `tiers.md`. O Minerador perde só a etapa de ferro do Avançado.
2. **Netherite.** Recomendação: nenhum tier passa de diamante, porque o jogo não exige netherite para minerar.
3. **Blocos protegidos.** Recomendação: além da regra de toda área, ele nunca quebra o bloco embaixo de si nem os que deixariam líquido vazar para fora da área.
4. **Líquidos.** Recomendação: ao encontrar lava ou água, tampa com um bloco do inventário, se tiver, ou desvia e avisa.
5. **Iluminação.** Recomendação: põe tochas pelo slot exclusivo de tochas da Fase 4.1, para não nascer mob no túnel.
6. **Buscar minérios.** Recomendação: usa o scanner de minérios do tier para achar o alvo e cava só o caminho até ele.
7. **O que conta como minério.** Recomendação: tudo o que tem receita de fornalha e vem de um bloco da tag comum de minérios, como o ferro bruto e, com Toque Suave, o próprio bloco de minério. Pedra, terra e o resto vão direto para o inventário.
8. **Fila da fornalha.** Recomendação: o minério que chega enquanto outro funde espera no próprio slot da fornalha, na mesma pilha. Um minério diferente, ou a pilha cheia, espera no inventário e entra quando o slot liberar. Com o inventário cheio, o resultado fica na fornalha e ela para até ter espaço.
9. **Chave sem o card.** Recomendação: a chave da fornalha só aparece na seção Functions com o card do Minerador no slot. Sem o card, ela some, e o estado escolhido fica guardado para quando o card voltar.
10. **Fornalha sem bateria para fundir.** Recomendação: perto da bateria crítica, ele para de fundir e guarda o minério cru, para a energia ir para a volta ao Dock.
11. **Visual.** Recomendação: o capacete com a lanterna de cabeça, já previsto na pergunta 463.
12. **Números para `tiers.md`.** O feixe da lanterna já está em "A definir". Entra também o custo por dureza.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 12:14 | Põe a chave da fornalha na seção Functions da tela do Cubit. |
| Marcelo Fabiano | 2026-11-13 12:13 | Define a fornalha como elétrica, com gasto de FE só ativa, todo minério passando por ela e o resultado indo ao inventário. |
| Marcelo Fabiano | 2026-11-13 12:11 | Torna a fornalha de minérios exclusiva do Minerador, respondendo a pergunta 339. |
| Marcelo Fabiano | 2026-11-13 12:09 | Remove Inquebrável e Remendo do card. |
| Marcelo Fabiano | 2026-11-13 11:43 | Cria o conceito do Minerador, com o comportamento das perguntas 197 a 344 e a mudança para o Elite. |
