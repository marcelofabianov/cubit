# Fase 2: Versão mínima

## Pendentes

Não há perguntas pendentes nesta fase.

## Respondidas

| # | Pergunta | Resposta |
|---|---|---|
| 22 | O slot de energia existe no tier Base? | Sim, em todos os tiers |
| 29 | Quando dá para recolher o robô? | Sempre. Desligado, é a única ação, além de receber energia pelo slot (perguntas 21 e 45) |
| 30 | Como ele carrega, se sempre segue o player? | Substituída pela pergunta 47 |
| 31 | Quando ele religa? | Sozinho, com 5% de bateria |
| 32 | Ele consome energia parado? | 0 FE/t, configurável |
| 33 | Com quanta carga sai do craft? | 25%, configurável |
| 34 | O que acontece na morte, e como repara? | Vira item com inventário preservado e bateria zerada. A vida fica no item e se recupera na estação. O dono não o fere |
| 35 | O que conta como robô ativo? | Substituída pela pergunta 65 |
| 36 | Qual é o buffer e a entrada da estação? | 100 mil FE, configurável. A entrada foi alterada pela pergunta 61 |
| 37 | Qual é a fonte de FE no ambiente de dev? | Powah só em runtime local, com as dependências a confirmar, ou um comando de debug |
| 38 | Aprova a estrutura de pacotes do `AGENTS.md`? | Sim |
| 39 | Aprova as premissas da versão mínima? | Sim. Estão listadas abaixo |
| 43 | Quanta energia a redstone fornece, e quais itens valem? | 1 pó = 5.000 FE e 1 bloco = 45.000 FE, configuráveis |
| 44 | A Power Bank e a redstone usam o mesmo slot do Cubit? | Sim, um único slot de energia, que aceita um dos dois por vez |
| 45 | Com o Cubit desligado, clicar com redstone na mão funciona como com a Power Bank? | Sim. A pilha entra no slot, a recarga começa e, com 5%, o Cubit liga |
| 47 | Como o Cubit carrega no Dock? | "Base" é o Induction Dock, o bloco fixo, inspirado nas bases de robôs aspiradores. Na base, o Cubit fica em hibernação: diferente de desligado, com o painel solar desligado e o inventário e os slots exclusivos acessíveis. Ativo é ligado, e inativo é desligado, com o painel também desligado |
| 50 | Em que fase entram a redstone no Dock e no slot de energia do Cubit? | Fase 2. Assim, o carregamento já funciona sem gerador de outro mod |
| 54 | Sem o retorno automático da Fase 4, como o Cubit chega à base na versão mínima? | O player clica com o botão direito no Dock, e o Cubit por perto vai até ele. Colocar o item do Cubit em cima do Dock também funciona. A distância e o Cubit chamado foram definidos nas perguntas 59 e 81 |
| 55 | Como o Cubit sai da hibernação, já que interagir com ele abre o inventário? | Sai sozinho com a bateria cheia e volta a seguir. Antes disso, clicar no Dock o libera. O que ele faz ao sair foi ampliado pela pergunta 83 |
| 59 | A que distância clicar no Dock chama o Cubit "por perto"? | O raio do retorno automático do tier |
| 60 | O modelo final entra na Fase 2 ou é provisório nessa fase? | Geometria provisória na Fase 2 e modelo final na Fase 3, junto das expressões |
| 61 | O Dock recebe até 1.000 FE/t, mas recarrega Elite a 2.500 e Ultimate a 5.000 FE/t. Como resolver? | Entrada de 5.000 FE/t, com o estoque de 100 mil FE |
| 62 | Um Cubit sem bateria colocado no Dock entra em hibernação logo ou só com 5%? | Logo |
| 63 | Em que velocidade a vida se recupera na base? | Proporcional ao tier: todos levam o mesmo tempo para encher, cerca de 1 minuto, configurável |
| 64 | Como o Cubit extrai a redstone do slot? | Na velocidade de recarga do tier, consumindo um item por vez e só quando a energia couber |
| 65 | Como funciona o limite de robôs por player? | O player pode ter quantos robôs preferir, mas só um o segue. Pode haver um ou mais robôs configurados em áreas e ativos, e seguindo o player somente um |
| 71 | Já existe um Cubit seguindo o player. O que acontece ao colocar outro no mundo? | O novo fica parado e desligado. Ao ligá-lo, ele desliga o que estava ligado. A pergunta 76 limita isso ao que estava seguindo |
| 72 | Como o player escolhe qual Cubit o segue, na versão mínima, sem o controle remoto? | Ao ser colocado ou sair da hibernação, o Cubit segue se nenhum outro estiver seguindo. Para trocar, o player recolhe o atual ou o coloca na base |
| 73 | Clicar no Dock chama qual Cubit, se houver vários? E cabe mais de um na base? | Cada Dock recebe um Cubit por vez. Quem o clique chama foi alterado pela pergunta 81 |
| 74 | Com vários Cubits, o `/cubit reset` ainda é necessário, e há limite de quantidade? | O comando continua, para limpar o vínculo de quem segue, se ele se perder. Não há limite de quantidade |
| 75 | Sem o controle remoto, como ligar um Cubit parado e desligado, ainda com bateria? | O clique direito nele o liga, e ele passa a seguir o player |
| 76 | Ao ligar um Cubit, ele desliga também os que trabalham em áreas? | Não, só o que estava seguindo o player |
| 81 | A base serve a todos os Cubits ligados. Quem o clique no Dock chama? | O Cubit ligado mais próximo, dentro do raio, esteja seguindo ou em área. Também dá para colocar o Cubit na base pelo item |
| 84 | O que faz um Cubit ativo que volta à base, se já houver outro Cubit nela? | Para na frente da base e aguarda |
| 85 | Dois ou mais Cubits aguardam a mesma base. Quem entra quando ela libera? | O que chegou primeiro |
| 86 | A base está ocupada, mas há outra livre dentro do raio do Cubit. O que ele faz? | Vai para a base livre mais próxima, e só aguarda se todas no raio estiverem ocupadas |
| 95 | Qual ferramenta faz a formatação no commit? | Spotless com o google-java-format |
| 96 | Qual é a cobertura mínima de testes? | 85% |
| 97 | Um gate automático confere complexidade e tamanho de função e de classe? | Sim, com o Checkstyle no `check`, junto com o Spotless |
| 98 | Literais no código? | Sempre constantes em vez de string literal e número mágico |
| 99 | A regra de constantes também vale para os testes? | Sim. As constantes do teste ficam no próprio teste, com os valores dos documentos, sem compartilhar com o código de produção |
| 100 | A que distância do player o Cubit que segue para de andar? | 3 blocos, configurável, valor geral. Acima disso anda, e até 3 blocos para |
| 101 | O Cubit que segue, sem bateria, continua sendo o robô que segue? E um Cubit colocado sem bateria, sem outro seguindo, assume o lugar? | Sim, nos dois casos. Ele fica parado, com o vínculo, e com 5% religa e volta a seguir. Se o player ligar outro, o sem bateria vira desligado e deixa de religar sozinho |
| 102 | Um Cubit sai da base quando outro já assumiu o lugar de seguir. Em que estado ele fica? | Parado e desligado, igual à pergunta 71. O clique direito o liga, e ele passa a seguir, conforme a pergunta 75 |

### Premissas aprovadas

- Clique direito abre o inventário, e Shift + clique direito recolhe.
- GeckoLib já na versão mínima, com geometria provisória.
- O item empilha em 1 e mostra a bateria numa barra.
- O item não entra no inventário do robô nem em shulker boxes.
- A rota do robô evita água e lava, e ele não leva dano de queda.
- O config só tem os valores do Base por enquanto, com estrutura por tipo de robô e tier (pergunta 91).
- Controle remoto, solar, Jade, nome e cor ficam fora da versão mínima.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 18:50 | Registra as respostas 100, 101 e 102. |
| Marcelo Fabiano | 2026-11-04 18:24 | Registra a resposta 99. |
| Marcelo Fabiano | 2026-11-04 18:20 | Registra a resposta 98 e a pergunta 99. |
| Marcelo Fabiano | 2026-11-04 17:52 | Registra a resposta 97. |
| Marcelo Fabiano | 2026-11-04 17:48 | Registra a resposta 96. |
| Marcelo Fabiano | 2026-11-04 17:48 | Registra a resposta 95. |
| Marcelo Fabiano | 2026-11-04 17:26 | Corrige a seção de pendentes e aponta as respostas refinadas por outras. |
| Marcelo Fabiano | 2026-11-04 17:16 | Ajusta a premissa do config para tipo de robô e tier. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:12 | Registra as respostas 84, 85 e 86. |
| Marcelo Fabiano | 2026-11-04 17:11 | Registra a resposta 81 e atualiza a 73. |
| Marcelo Fabiano | 2026-11-04 17:08 | Registra as respostas 59 a 65 e 71 a 76. |
| Marcelo Fabiano | 2026-11-04 16:57 | Cria o arquivo com as perguntas e respostas da fase. |
