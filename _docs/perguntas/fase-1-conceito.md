# Fase 1: Conceito

## Pendentes

Não há perguntas pendentes nesta fase.

## Respondidas

### Projeto e nomes

| # | Pergunta | Resposta |
|---|---|---|
| 1 | Os upgrades se chamam `Module` ou `Upgrade`? | `Module` |
| 2 | Os modos de defesa são Peaceful, Retaliate e Aggressive? | Sim |
| 3 | Os itens se chamam Base, Advanced, Elite e Ultimate Power Bank? | Sim |
| 4 | O Cubit entra como exceção à regra do pack contra funções repetidas, já que o Robit do Mekanism é parecido? | Sim, com registro no repositório do pack |
| 5 | Qual é a licença? | MIT |
| 6 | Qual é a primeira versão? | `0.1.0`, com a `1.0.0` só com o mod completo |
| 7 | Protegemos `main`, `homolog` e `develop` no GitHub? | Sim, se o plano permitir em repositório privado. O plano atual não permite, então valem só os gates locais |
| 8 | Trocamos para inglês as legendas da prancha do README? | Substituída pela pergunta 88 |
| 9 | Criamos um `CLAUDE.md` apontando para o `AGENTS.md`? | Sim |

### Persona

| # | Pergunta | Resposta |
|---|---|---|
| 10 | Como o Cubit reage a ficar sem bateria? | Sono com a bateria baixa e tela apagada ao desligar |
| 11 | "Só bipes" vale também para telas e avisos? | Só bipes, mas o Cubit pode mostrar textos simples e diretos em algumas situações: bateria crítica (10%), inimigo próximo, minério encontrado, tenho que ir carregar e itens descarregados |
| 12 | As expressões feliz e sono têm bipe? | Substituída pela [pergunta 136](fase-3-sentidos.md): o bipe padrão vale ao trocar para qualquer expressão |
| 13 | Como o Cubit trata mobs neutros? | Só ataca para defender o player, quando os neutros ficam agressivos contra o player |
| 14 | Animais de outros players contam como mobs pacíficos? | Sim |
| 15 | As expressões normal, carregando e desligado entram no mod? | Sim |

### Balanceamento e visual

| # | Pergunta | Resposta |
|---|---|---|
| 16 | Elite e Ultimate ganham mais energia do sol do que gastam de dia. Isso muda? | Sim, o solar fica abaixo do consumo. Os valores estão na [pergunta 52](fase-3-sentidos.md) |
| 17 | O feixe de luz resolve Elite e Ultimate, com o limite de luz 15? | Sim, com a viabilidade confirmada na Fase 3 |
| 18 | O raio do retorno automático cresce por tier? | Sim |
| 19 | O conceito atual já é o design próprio ou ainda muda? | É a base, e o modelo final foi definido na pergunta 60 |
| 20 | Quais são as receitas? | Provisórias, seguindo a progressão de [`itens.md`](../itens.md), e ajustadas nos testes |
| 46 | O que acontece com o painel que aparece no topo do conceito? | O painel solar continua no modelo |
| 51 | O painel solar é só decorativo ou também carrega a bateria? | Carrega a bateria interna quando o Cubit está ativo e durante o dia. À noite, só carrega nos dois últimos tiers. Quando o Cubit está na base, o painel fica desligado. Funciona como um carregador solar real: blocos que bloqueiam o sol atrapalham o carregamento |

### Organização

| # | Pergunta | Resposta |
|---|---|---|
| 40 | Em que fase entram os itens sem fase? | Solar, scanners, nome, cor e Jade na Fase 3; controle remoto na Fase 4 |
| 41 | Como definir "minério raro, de ferro para cima"? | Uma lista configurável de tags, começando em ferro |
| 77 | Onde fica a lista de itens, com nome e uma linha de descrição? | Na tabela principal de `itens.md`, e ID e receita numa segunda tabela do mesmo arquivo |
| 78 | Um gate garante a atualização do histórico de modificações? | Sim, no `pre-commit`, para os arquivos de `_docs/`, conforme a [pergunta 132](fase-2-versao-minima.md) |
| 87 | Os nomes dos arquivos e pastas de documentação ficam em português? | Sim. `README.md`, `AGENTS.md`, `CLAUDE.md` e `LICENSE` mantêm o nome |
| 88 | As legendas da prancha de conceito ficam em português? | Sim, porque ela não entra no jar |
| 89 | Os scripts e hooks ficam em inglês? | Arquivos de programação, como `.sh`, ficam em inglês. Ficam em português os arquivos de conteúdo |
| 90 | Com robôs especializados no futuro, qual é o ID do robô atual? | `cubit:general_robot`. Cubit continua como nome do mod e da família |
| 91 | Como preparar o projeto para outros robôs? | Nada pode ficar fixo de forma que comprometa a flexibilidade do projeto. As regras 39 a 43 do `AGENTS.md` detalham isso |
| 121 | Quais perguntas ficam registradas em `.md`? | Todas as que o desenvolvedor responder, conforme a regra 47 do `AGENTS.md` |
| 107 | O template de pull request segue a regra de nome em português? | Não. `pull_request_template.md` mantém o nome, porque o GitHub só reconhece esse nome. O histórico dele foi removido pela [pergunta 132](fase-2-versao-minima.md) |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 23:45 | Aponta as respostas 78 e 107 para a pergunta 132. |
| Marcelo Fabiano | 2026-11-04 22:56 | Aponta a resposta 12 para a pergunta 136. |
| Marcelo Fabiano | 2026-11-04 20:41 | Registra a resposta 121. |
| Marcelo Fabiano | 2026-11-04 19:14 | Registra a resposta 107. |
| Marcelo Fabiano | 2026-11-04 17:26 | Reordena a resposta 89. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra as respostas 90 e 91. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e registra as respostas 87, 88 e 89. |
| Marcelo Fabiano | 2026-11-04 17:08 | Adiciona as respostas 77 e 78 e atualiza a 19. |
| Marcelo Fabiano | 2026-11-04 16:57 | Cria o arquivo com as perguntas e respostas da fase. |
