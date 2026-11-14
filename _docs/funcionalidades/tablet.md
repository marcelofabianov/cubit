# Tablet

O Tablet substitui o controle remoto, o Cubit Remote, no item 12 da Fase 4.2 (perguntas 998 a 1026). É um item que o player segura e que, com o clique direito, abre uma tela grande para cuidar dos seus Cubits de longe. A tela segue a identidade visual das outras telas ([`telas.md`](telas.md#princípios)) e usa as mesmas regras e os mesmos painéis delas (pergunta 1023).

O conceito de origem está em `tmp/tablet.md`. O modo área ficou em [`modo-area.md`](modo-area.md).

## Entregas

O Tablet 1 traz só o que já existe no mod (pergunta 999) e se divide em três entregas, cada uma com o seu teste manual (pergunta 1001):

1. O item, a receita, o app Open, a tela de seleção com a foto do Cubit, o Cubit escolhido e a seta.
2. A tela do Cubit, com o cabeçalho, a chave geral, `Rename` e as abas Recursos, Defesa, Filtros e Inventário.
3. Os apps Alvo e Dock e o botão `Ir para o Dock`.

**Depois do Tablet 1:**
- os apps Área e Descarga, o botão `Descarregar` e as linhas de retorno e local de morte entram com os itens 13 a 16 da Fase 4.2 (pergunta 1000);
- o app Marcar e a aba Trabalho entram na Fase 5, com os cards (regra 43).

## Item

| Nome | ID | Descrição |
|---|---|---|
| Cubit Tablet | `cubit:tablet` | Abre a tela de gerenciamento dos Cubits do player |

- **Sem dono:** o item não guarda dono, e qualquer Tablet mostra só os Cubits de quem o segura (pergunta 1024).
- **Não empilha.**
- **Receita:** com forma, de materiais do jogo puro: 2 painéis de vidro e 1 redstone na linha de cima, 4 lingotes de ferro nas laterais das duas linhas de baixo, 1 diamante no centro e 1 lingote de ouro embaixo, no meio, como na [tabela de `itens.md`](../itens.md#cubit-tablet). Aparece no livro de receitas quando o player tem um Cubit, como a do Dock (perguntas 1025, 1030 e 1031).
- **Textura:** 16×16, feita por prompt em `tmp/arte/tablet/` (pergunta 1025).
- **Troca de ID:** o `cubit:remote` estava só planejado, sem save nem receita, então o `cubit:tablet` não precisa de migração.

## Apps

O Tablet funciona por apps, um ativo por vez, e o clique direito faz o que o app ativo faz. O nome "app" evita a confusão com os Modules de upgrade de tier.

| App | Clique direito | Quando aparece | Entrega |
|---|---|---|---|
| Open | Num Cubit, abre a tela dele. Em qualquer outro lugar, abre a tela de seleção | Sempre | 1 |
| Alvo | Marca o mob na mira, a até 32 blocos, como alvo do modo Focus do Cubit escolhido | Com o Cubit escolhido carregado e no modo Focus | 3 |
| Dock | Num Dock do player, faz dele o Dock preferido do Cubit escolhido. No Dock que já é o preferido, tira a preferência | Sempre | 3 |
| Área | Num Cubit seu, escolhe o Cubit. Depois, em dois blocos, marca os cantos da área, e o Cubit vai para ela ([`modo-area.md`](modo-area.md#marcar-a-área)) | Sempre | Item 13 |
| Descarga | Marca os baús de descarga | Com o item 14 da Fase 4.2 | Depois |
| Marcar | Marca os blocos do trabalho do card | Na Fase 5 | Depois |

- **Trocar de app:** com o Tablet na mão, Shift + roda do mouse troca o app. No macOS, o Shift faz a roda de um mouse rolar para o lado, e essa rolagem também troca o app, como a hotbar do jogo faz. O app atual aparece na barra de ação, como `Tablet · Alvo`, e no nome do item. Só entram na troca os apps que fazem sentido para o Cubit escolhido. O app ativo fica no item, no componente `cubit:tablet_app` (perguntas 1006 e 1026).
- **Prioridade do Tablet:** no app Open, clicar num bloco ou numa entidade com ação própria, como um baú, uma porta, um aldeão ou um Dock, abre a tela de seleção e não aciona o bloco nem a entidade (pergunta 1007).
- **App inválido:** se o app ativo deixa de fazer sentido, como o Alvo com o Cubit escolhido fora do Focus, o próximo clique faz o que o Open faz, o item volta a Open e a barra de ação avisa (pergunta 1044).
- **Alvo:** o mob na mira, a até `tablet_target_reach_blocks` blocos, 32 por padrão, a distância em que o Focus esquece o alvo. Valem as regras do Focus: só mob hostil ou neutro. Sem o Cubit escolhido carregado e no Focus, a barra de ação diz o motivo. Sem mob na mira, a barra de ação avisa, e nada abre. No mob que já é o alvo, nada muda (perguntas 1043 e 1047).
- **Dock, fora de um Dock do player:** num outro bloco, num Dock de outro player ou numa entidade, a barra de ação diz que o app marca um Dock seu, e nada abre (pergunta 1046).
- **Dock preferido:** o segundo clique no mesmo Dock tira a preferência, com aviso na barra de ação (pergunta 1045). Recolher o Cubit apaga o Dock preferido, como a área e os baús marcados (pergunta 343), e o Cubit volta a usar o Dock livre mais próximo (pergunta 1008).

## Cubit escolhido

O Cubit que o player escolhe na tela de seleção, ou em que clica no app Open, vira o Cubit escolhido. É ele que a tela mostra e que os apps marcam, sem o player precisar mirar nele (pergunta 254).

- **Onde fica:** no attachment `cubit:chosen_robot` do player, e não no item, para valer em qualquer Tablet que ele segure. A escolha some se o Cubit for recolhido ou deixar de existir (perguntas 1009 e 1026).
- **Seta:** enquanto o player segura o Tablet, aparece uma seta em cima do Cubit escolhido. Ela tem a cor do colar do tier e um leve balanço para cima e para baixo, para não se confundir com a seta do scanner nem com a seta amarela do Focus, e aparece através de blocos (perguntas 1012 e 1013).
- **Quem vê:** só o player que segura o Tablet, porque a seta é desenhada só no cliente dele (pergunta 1010).
- **Fora da vista:** longe do alcance de renderização ou em outra dimensão, a seta some, e o player consulta o lugar na tela de seleção (pergunta 1014).
- **Sem o Tablet na mão, ou sem Cubit escolhido:** nenhuma indicação aparece.

## Tela de seleção

É a tela inicial do Tablet. Lista os Cubits de que o player é dono, sem limite de distância, e substitui a função de localizar do controle (pergunta 82). A janela ocupa quase toda a tela, com até 480 px de largura, e mostra quantas linhas couberem na altura (pergunta 1029).

| Coluna | O que mostra |
|---|---|
| Retrato | A expressão atual, ou a última conhecida, com a cor da carcaça e a cor do colar do tier |
| Nome e tier | O nome do Cubit, ou `Cubit`, com a etiqueta do tier |
| Estado | Seguindo, ativo, parado, hibernando, desligado ou bateria crítica, com a cor da tela do Cubit |
| Battery | A porcentagem da bateria |
| Lugar | Coordenadas, dimensão e distância até o player |

- **Quem entra:** só os Cubits que existem como entidade, inclusive os que hibernam no Dock. Os guardados como item, num baú ou no inventário do Dock, e os Damaged Cubits não aparecem, e recolher um Cubit o tira da lista (pergunta 1004).
- **Ordem:** primeiro o Cubit que segue o player, depois os demais pela distância, com os de outras dimensões e os de chunk descarregado no fim (pergunta 1003).
- **Muitos Cubits:** a lista rola, como o inventário nos tiers altos, e ganha um campo de busca pelo nome quando passar do que cabe numa tela (pergunta 1005).
- **Chunk descarregado:** mostra a última foto, com a posição, o estado e a aparência, e a hora dela. A foto fica nos dados do mundo, em `cubit_robot_snapshots`, por dono, e é atualizada enquanto o Cubit está carregado (pergunta 1026).
- **Selecionar:** o clique num Cubit faz dele o Cubit escolhido e abre a tela dele, na aba Recursos.

## Tela do Cubit

Abre depois da seleção, ou mirando num Cubit com o app Open. A janela é grande, com os textos, as fontes e os números das outras telas.

### Cabeçalho

Fica fixo em todas as abas. `Voltar` fica no canto superior esquerdo, e os botões ficam numa fileira de botões planos sob as barras, como o de chamar o Cubit no Dock (pergunta 1035):

| Parte | O que mostra |
|---|---|
| Voltar | Botão que volta para a tela de seleção |
| Retrato | A expressão atual, na moldura, com a cor da carcaça e a cor do colar do tier (pergunta 1011) |
| Nome e tier | O nome do Cubit, ou `Cubit`, com a etiqueta do tier |
| Rename | Botão que abre uma janela pequena com o campo de texto, Confirmar e Cancelar (pergunta 1016) |
| Estado | O estado do topo, como `● Following`, com a cor da tela do Cubit |
| Health | Barra e `18 / 25` |
| Battery | Barra e `214k / 300k FE` |
| Recarga | A linha de recarga, como `Solar +5 FE/t` |
| Lugar | Coordenadas e dimensão |
| Power | A chave geral, que liga e desliga o Cubit (perguntas 1021 e 1033) |
| Ir para o Dock | Manda o Cubit ir para o Dock e hibernar, na entrega 3 (pergunta 1020) |
| Follow | Faz o Cubit escolhido seguir o player, no lugar do que seguia, e, com o item 13, o tira da área (perguntas 1055 e 1072) |

- **Rename:** nomeia e renomeia o Cubit sem gastar etiqueta nem XP, com as regras da etiqueta do jogo. O nome tem até 50 caracteres, e o servidor confere o dono e o tamanho e corta os espaços nas pontas. Um nome vazio tira o nome. Com o Cubit ativo, ele faz a expressão feliz com o bipe, como ao receber a etiqueta (perguntas 1015 a 1017).
- **Rename, a janela:** pequena, no centro, com o campo de até 50 caracteres já preenchido com o nome atual, Confirmar e Cancelar. Enter confirma, e Esc cancela. Um Cubit sem nome abre o campo vazio, para Confirmar não chamá-lo de `Cubit` (pergunta 1035).
- **Power:** o botão mostra `Switch off` com o Cubit ativo e `Turn on` com ele desligado e com carga. Sem carga, fica desabilitado, com a dica do motivo. Desliga como o botão `Power` da aba Settings, e o clique direito no Cubit também o religa, sem estado salvo novo. Também liga um Cubit desligado com carga, como a exceção da pergunta 80 (pergunta 1033, que muda a 1021).
- **Parar e seguir:** o `Stop` saiu do Tablet por agora e espera um refinamento, em "Ainda sem fase" no `ROADMAP.md` (pergunta 1036).
- **Follow:** fica na fileira de botões, depois do `Ir para o Dock`. A fileira começa na margem, sob o retrato, para os quatro botões caberem, e um texto que não cabe no botão encolhe. Faz o Cubit escolhido seguir o player, desligado com carga, hibernando no Dock, que ele deixa na hora, ou numa área, que se apaga com o item 13. O que seguia antes desliga onde está, como no `Turn on`. Vale de qualquer distância, na mesma dimensão, e o Cubit segue pela regra de hoje, andando ou teleportando. Já seguindo, sem carga, em outra dimensão ou com o chunk descarregado, fica desabilitado, com a dica do motivo (perguntas 1055 e 1072 a 1075).
- **Ir para o Dock:** vai para o Dock preferido, se ele estiver na mesma dimensão, carregado e no raio de chamada do tier. Senão, vai para o Dock livre mais próximo nesse raio, como o clique no Dock (pergunta 1041). Com o preferido ocupado, vai até ele e espera na fila, procurando outro livre enquanto espera. Um preferido destruído é apagado ao ser lido (pergunta 1042). Sem Dock ao alcance, desligado, hibernando ou com o chunk descarregado, o botão fica desabilitado, com a dica do motivo. Ao sair do Dock, o Cubit volta ao que fazia.

### Abas

| Aba | Ícone | Conteúdo |
|---|---|---|
| Recursos | Engrenagem | A aba inicial: cada recurso numa linha, com o nome, a descrição na dica e a chave ON e OFF |
| Defesa | Escudo | Os modos de defesa, como na aba Defense da tela do Cubit |
| Filtros | Funil | Só o filtro de coleta, com o botão da lista e a grade de slots fantasma da aba Behavior, e o inventário e a hotbar do player embaixo (perguntas 1022 e 1037) |
| Inventário | Baú | O inventário do Cubit, só para consulta |

- **Aba Recursos:** um painel próprio, com as oito chaves numa lista só, que usam os mesmos controles do servidor da tela do Cubit (pergunta 1037). Uma linha bloqueada pelo tier fica apagada, com a chave desativada e o tier que libera, como `Unlocks at Advanced`. Entram os recursos que já existem:

| Recurso | Libera |
|---|---|
| Lanterna | Base |
| Scanners | Base |
| Falas | Base |
| Painel solar | Base |
| Coleta | Base |
| Ímã | Advanced |
| Busca | Base |
| Tochas | Advanced |
| Retorno automático | Base |

  Com um Cubit numa área, a aba também mostra a área, com os cantos e o tamanho (item 13). O retorno automático entra com o item 15, antes do modo área (pergunta 1069), e a descarga em baú e o local de morte, com os itens 14 e 16 da Fase 4.2.
- **Aba Inventário:** mostra o inventário do Cubit com todas as linhas do tier e rolagem nos tiers altos, mas não permite tirar, pôr nem mover itens. A dica de cada item funciona.
- **Inventário do player:** aparece só na aba Filtros.

## Distância

- **Cubit descarregado:** a tela abre só com o cabeçalho da foto, sem abas, e um aviso como `Out of reach · seen 5 min ago`. Os botões ficam desabilitados com a dica do motivo, e `Voltar` continua funcionando (pergunta 1034).

- **Chunk carregado:** o Tablet lê e muda tudo, a qualquer distância e em qualquer dimensão (pergunta 1018, que muda a 94).
- **Chunk descarregado:** o Tablet só lê a última foto, com a hora. As chaves, os botões e o `Rename` ficam desabilitados, com a dica do motivo.

## Segurança

- **Dono:** o servidor confere o dono, o alcance e o chunk em toda ação recebida do cliente (regra 20 e pergunta 1024).
- **Duas telas:** com o Tablet e a tela do Cubit abertos ao mesmo tempo, as mudanças chegam às duas, porque usam os mesmos controles do servidor (pergunta 1023).

## Contrato

| Onde | Nome | Conteúdo |
|---|---|---|
| Item | `cubit:tablet` | O Tablet |
| Receita | `cubit:tablet` | A receita com forma |
| Componente do item | `cubit:tablet_app` | O app ativo: `open`, `target`, `dock` ou `area`. Sem o componente, vale `open` |
| Attachment do player | `cubit:chosen_robot` | O UUID do Cubit escolhido |
| NBT da entidade | `PreferredDock` | O Dock preferido: `Block`, a posição num `long`, e `Dimension`. Sem a chave, o Cubit não tem Dock preferido |
| Menu | `cubit:tablet_robot` | A tela do Cubit no Tablet, com a cor, o lugar e a chave geral nos valores do menu |
| Pacotes do cliente | `cubit:choose_robot`, `cubit:robot_list_request`, `cubit:rename_robot` e `cubit:switch_tablet_app` | Escolher e abrir, voltar para a seleção, renomear e trocar de app, todos conferidos no servidor |
| Config | `tablet_target_reach_blocks`, em `general` | O alcance da mira do app Alvo, de 1 a 128, com 32 |
| NBT da entidade | `WorkArea` | A área do Cubit: `Min` e `Max`, os cantos em `long`, e `Dimension`. Sem a chave, o Cubit não tem área |
| Config | `area_size_blocks` e `area_height_blocks`, por tier | O lado da base e a altura máxima da área, com 0 para bloqueado |
| Dados do mundo | `cubit_robot_snapshots` | A foto de cada Cubit por dono: posição, dimensão, estado, bateria, vida, tier, cor, nome e a hora |

Os textos em inglês e português, como `Cubit Tablet` / `Tablet do Cubit`, `Tablet · Open` / `Tablet · Abrir`, `Go to Dock` / `Ir para o Dock` e `Rename` / `Renomear`, ficam no datagen (pergunta 1026).

## Riscos

- **Tela sem o Cubit por perto:** as telas de hoje são menus presos à entidade, e o jogo fecha um menu longe dela. A tela do Tablet precisa de um menu próprio, que lê o Cubit pelo UUID no servidor, e de pacotes do cliente para o servidor, que o `AGENTS.md` já previa para o controle.
- **Chunk descarregado:** o Cubit não está na memória, então a seleção depende da foto salva no mundo.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-14 11:20 | Estende o `Follow` a qualquer Cubit (perguntas 1072 a 1076). |
| Marcelo Fabiano | 2026-11-14 10:45 | Aceita a rolagem para o lado do macOS na troca de app. |
| Marcelo Fabiano | 2026-11-14 10:00 | Adiciona o retorno automático à aba Recursos (pergunta 1066). |
| Marcelo Fabiano | 2026-11-14 09:00 | Define o app Área, o botão `Follow` e a área na aba Recursos (perguntas 1049 a 1064). |
| Marcelo Fabiano | 2026-11-13 17:35 | Detalha o NBT `PreferredDock`, o pacote da troca de app e o config do app Alvo. |
| Marcelo Fabiano | 2026-11-13 17:15 | Define os apps Alvo e Dock e o `Ir para o Dock` da entrega 3, com as perguntas 1041 a 1047. |
| Marcelo Fabiano | 2026-11-13 16:30 | Descreve o botão Power, a janela de Rename sem nome, o menu e os pacotes da entrega 2. |
| Marcelo Fabiano | 2026-11-13 19:00 | Põe as oito chaves na aba Recursos e só o filtro na aba Filtros. |
| Marcelo Fabiano | 2026-11-13 18:50 | Tira o Stop e o Follow, faz a chave geral igual ao Power e define a barra de botões e o Cubit descarregado. |
| Marcelo Fabiano | 2026-11-13 18:00 | Muda a receita, com o diamante no centro. |
| Marcelo Fabiano | 2026-11-13 17:50 | Corrige a grade da receita. |
| Marcelo Fabiano | 2026-11-13 17:45 | Faz a tela de seleção ocupar quase toda a tela. |
| Marcelo Fabiano | 2026-11-13 16:40 | Cria o Tablet a partir do conceito, com as decisões das perguntas 998 a 1026. |
