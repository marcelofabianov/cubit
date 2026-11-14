# Mecânico

Conceito do trabalho de Mecânico, da Fase 5.2. Nada aqui está implementado, e o documentado pode mudar se aparecer um impedimento técnico (regra 48 do `AGENTS.md`). Ele complementa [`trabalhos.md`](../trabalhos.md), cujas regras de toda área valem aqui.

**Situação:** o Mecânico entra como trabalho do Ultimate (perguntas 1146, 1147 e 1152), com o nome do card Machine Learning: Mechanic. O conceito abaixo ainda é uma proposta, e os detalhes em aberto são respondidos no refinamento do card (pergunta 1153).

## A ideia

Um Cubit que cuida dos outros Cubits da área. Ele não produz nada: recarrega, conserta, repara a vida e destrava os robôs da frota, para eles não precisarem sair do trabalho. Só faz sentido com muitos Cubits trabalhando, por isso é exclusivo do Ultimate.

## Card

| Nome | ID | Descrição |
|---|---|---|
| Machine Learning: Mechanic | `cubit:ml_mechanic` | Card de Aprendizado de Máquina do trabalho de Mecânico |

Segue as regras dos outros cards: slot exclusivo, um por Cubit, sai sem perda e só age em área marcada.

## Inventários

### Estoque do Dock

O Mecânico usa o inventário do Dock da Fase 4.1 como ele é: a aba Storage, com 27 slots para Power Banks, a Toolbox, o Cubit e o Damaged Cubit, e com a automação da resposta 967 (pergunta 1151). As regras estão em [`energia.md`](../energia.md#inventário-do-dock).

- **Uso:** o Mecânico devolve ali as Power Banks vazias, pega as cheias, busca Toolboxes e guarda Damaged Cubits.

### Fileira de suprimentos do Mecânico

- **Tamanho:** uma fileira extra de 9 slots, além do inventário normal do Ultimate.
- **O que aceita:** só Power Banks de qualquer tier e Toolboxes.

## O que ele faz

### 1. Recarga

O Mecânico procura um Cubit da área com a bateria abaixo do limite, vai até ele e passa FE das Power Banks da fileira. A transferência tira de um lado e põe no outro por completo, sem criar nem perder FE.

### 2. Conserto

1. Recolhe o Damaged Cubit caído na área.
2. **Com Toolbox:** procura primeiro na própria fileira e depois no estoque do Dock. Conserta o robô, coloca de volta no mundo e repara a vida e a bateria dele. Cada conserto consome uma Toolbox, como no conserto manual.
3. **Sem Toolbox nos dois lugares:** guarda o Damaged Cubit no estoque do Dock.
4. **Quando uma Toolbox aparecer no estoque:** retira o Damaged Cubit guardado e faz o conserto normal.

### 3. Reparo de vida

O Mecânico procura um Cubit da área com a vida abaixo do limite, vai até ele e converte FE da própria bateria em vida para ele.

- **Fonte:** a bateria própria do Mecânico.
- **Diferença do Dock:** no Dock, a vida volta sem gastar FE. Com o Mecânico, custa FE, em troca de o robô não sair do trabalho.
- **Depois do conserto:** o robô sai do conserto com 25% da vida, e o Mecânico emenda o reparo ali mesmo.

### 4. Destravar

Quando um Cubit da área hiberna por inventário cheio:

1. O Mecânico passa todos os itens desse Cubit para o próprio inventário.
2. Descarrega os itens num baú marcado.
3. Se não couber tudo de uma vez, faz quantas viagens forem precisas até o inventário do outro Cubit ficar completamente vazio.
4. Só com o inventário do outro vazio, dá o `Retomar`.

### Reabastecimento

- **Power Bank vazia:** o Mecânico leva ao estoque do Dock e pega uma cheia no lugar.
- **Toolbox:** quando acabam as da fileira, busca no estoque do Dock.

## Em aberto

Cada item traz a recomendação atual, ainda sem resposta.

### Ordem e limites

1. **Ordem de prioridade.** Recomendação: recarga crítica, reparo de vida crítico, conserto, recarga preventiva, reparo preventivo, destravar e, por último, reabastecimento. Sem Power Bank com carga ou Toolbox para a tarefa da vez, reabastecer passa para a frente.
2. **Limites configuráveis.** Recomendação: na aba Trabalho, bateria em 20%, vida crítica em 25% e vida preventiva em 75%.
3. **Reserva própria.** Recomendação: o Mecânico nunca desce abaixo da bateria que garante a volta ao Dock.

### Recarga e reparo

4. **Bateria própria na recarga dos outros.** Recomendação: sem Power Bank com carga, usa a própria bateria, só acima da reserva.
5. **Recarregar a si mesmo pelas Power Banks.** Recomendação: sim, para evitar viagens ao Dock.
6. **Power Banks no reparo de vida.** Recomendação: o reparo usa só a bateria própria, e as Power Banks ficam para a recarga.
7. **Reparo em combate.** Recomendação: só repara quem não recebeu dano nos últimos segundos, para o Defensor não ficar curado no meio da luta.
8. **Reparar a si mesmo.** Recomendação: não. O Mecânico usa o Dock, ou outro Mecânico o repara.

### Conserto

9. **Robô consertado volta a trabalhar.** Recomendação: volta, se a área dele for a mesma do Mecânico. Fora disso, fica parado e desligado, e o dono é avisado.
10. **Item Cubit no estoque.** Recomendação: serve de reserva do player e de saída do conserto quando o Mecânico não pode colocar o robô de volta. O Mecânico nunca tira um Cubit do estoque para colocar no mundo.
11. **Ordem do conserto quando chega Toolbox.** Recomendação: primeiro os Damaged Cubits caídos na área e depois os guardados no Dock.

### Estoque, fileira e dono

12. **Fileira sem o card.** Recomendação: a fileira continua visível, mas só para tirar itens.
13. **Dono.** Recomendação: o Mecânico só atende Cubits e Damaged Cubits do mesmo dono, e só usa Dock do mesmo dono.
14. **Tooltip no estoque.** Recomendação: Cubits e Damaged Cubits mostram o nome e o tier.

### Quando ele para

15. **Comportamento de parada.** Recomendação:
    - **Estoque do Dock sem Power Bank cheia ou sem Toolbox:** avisa e segue com o que dá para fazer.
    - **Estoque do Dock cheio:** fica com as Power Banks vazias na fileira e com os Damaged Cubits no inventário normal, e avisa.
    - **Baú marcado cheio no destravar:** fica com os itens e avisa. O alvo continua hibernando.
    - **Bateria própria na reserva:** vai ao Dock.
    - **Nada a fazer:** fica parado perto do Dock, sem gastar.

### Regras herdadas a confirmar

16. **Regras de área.** Recomendação: nunca sai da área, reserva o alvo antes de ir, inclusive durante todas as viagens do destravar, tem limite de ações por tick e paga FE por ação.
17. **Combate.** Recomendação: não ataca. Atacado, foge.
18. **Mecânicos entre si.** Recomendação: dois Mecânicos recarregam, reparam e consertam um ao outro.

### Servidor, visual e números

19. **Log no Servidor.** Recomendação: registra recargas, reparos, consertos, destravamentos e a falta de Power Bank ou Toolbox.
20. **Visual.** Recomendação: uma chave inglesa na garra ou um cinto de ferramentas, pela personalização por card da Fase 5.2.
21. **Números para `tiers.md`.** Entram em "A definir": o custo de FE por ponto de vida, a velocidade do reparo, o tempo sem dano para reparar e o custo por ação do Mecânico.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 20:25 | Leva o conceito de `tmp/fase5` para `_docs`, com as decisões da consolidação (perguntas 1145 a 1156). |
| Marcelo Fabiano | 2026-11-13 11:13 | Cria o conceito do Suporte, com o estoque do Dock, a fileira de suprimentos, a recarga, o conserto, o reparo de vida e o destravar. |
