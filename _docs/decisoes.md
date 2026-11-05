# Decisões

Resumo do que já foi decidido no projeto. As perguntas, abertas e respondidas, ficam em [`perguntas/`](perguntas/README.md).

## Projeto

- **Nome do mod e do robô:** Cubit.
- **Modid:** `cubit`.
- **Pacote Java:** `io.github.marcelofabianov.cubit`.
- **Repositório:** `marcelofabianov/cubit`.
- **Licença:** MIT.
- **Ícone do mod:** o mesmo do item do Cubit.
- **Primeira versão:** `0.1.0`, com a `1.0.0` só com o mod completo.
- **Etapa atual:** Fase 2, versão mínima. A Fase 1, de conceito, foi encerrada.
- **Iron Meridian:** o Cubit entra como exceção à regra do pack contra funções repetidas, por causa do Robit do Mekanism. O registro está na issue `marcelofabianov/iron-meridian#2`.

## Idioma

- **Mod:** o que entra no jar, aparece no jogo ou acompanha o download fica em inglês, com um único `en_us.json`.
- **Programação:** arquivos como `.java` e `.sh` ficam em inglês.
- **Conteúdo:** arquivos como `.md` ficam em português, inclusive o nome do arquivo e da pasta.
- **Commits:** português, em Conventional Commits.

## Processo

- **Fluxo de Git:** Gitflow, Semantic Versioning e Conventional Commits, com `homolog` para a release em teste e `release/X.Y.Z` para a release fechada. O fluxo completo está no [`AGENTS.md`](../AGENTS.md#fluxo-de-branches).
- **Regras de desenvolvimento:** no [`AGENTS.md`](../AGENTS.md#regras-de-desenvolvimento). Os gates de commit e de push estão ativos, inclusive os de Java e Gradle.
- **Proteção de branches:** o plano atual do GitHub não permite em repositório privado, então valem só os gates locais.
- **Fonte da verdade:** as respostas em `perguntas/` prevalecem sobre os outros documentos.
- **Mapa dos documentos:** fica em [`README.md`](README.md), e as fases, no Roadmap do `README.md` da raiz.
- **Pilares:** as decisões técnicas seguem a hierarquia de [`pilares.md`](desenvolvimento/pilares.md).
- **Registro de perguntas:** toda pergunta respondida pelo desenvolvedor entra em `perguntas/` antes do próximo passo.
- **Validação:** toda entrega é conferida contra os documentos `.md`, e artefatos complementam essa conferência.
- **Config e testes na Fase 2:** o config entra antes da entidade e cresce a cada etapa. As GameTests e o gate de datagen entram na etapa "Cubit no mundo".
- **Histórico de modificações:** só os `.md` de `_docs/` têm a tabela.
- **Ordem da Fase 2:** movimento, fogo, lava e arquivos de som já entraram. Seguem a revisão dos `.md`, o modelo e a morte com o item danificado, antes do inventário.
- **Documento desatualizado:** o agente sempre pergunta antes de corrigir, mesmo quando a correção parece clara.
- **Números no roteiro manual:** ele pode citar os valores padrão como resultado esperado, e `tiers.md` prevalece.
- **Gates de push:** as GameTests também detectam classe de cliente carregada no servidor, sem exigir o EULA, então o `runServer` não é um gate. O datagen reprova se `src/generated` ficar diferente do Git.

## Mod

- **Energia:** o Cubit só carrega pelo Induction Dock, pela Power Bank, com redstone ou pelo painel solar.
- **Robôs especializados:** o robô atual, `cubit:general_robot`, é de uso geral. Robôs especializados são uma ideia para o futuro, fora das fases atuais, e nada pode ficar fixo de forma que comprometa essa flexibilidade.
- **Quantidade:** o player pode ter quantos robôs preferir, mas só um o segue. Os outros podem trabalhar em áreas. O controle remoto e a base atendem todos os robôs ligados.
- **Robô que segue sem bateria:** mantém o vínculo e volta a seguir ao religar. Se o player ligar outro, ele vira desligado.
- **Saída da base com outro seguindo:** o robô fica parado e desligado, como ao ser colocado.
- **Config:** `COMMON`, com chaves em `snake_case` por tipo de robô e tier, como `[general_robot.base]`.
- **Entidade:** a `RobotEntity` herda de `PathfinderMob`, com o dono num UUID próprio.
- **Estado como item:** o componente `cubit:robot_state`, e o inventário em `cubit:robot_inventory`, fora do domínio.
- **Inventário:** menu próprio, com o visual do baú do jogo. Com o config reduzido, o que não couber cai no chão.
- **Dono:** quem coloca o robô. O item não guarda o dono. Nenhum dano atribuído a ele fere o robô. O fogo de um isqueiro continua ferindo, porque o jogo não guarda quem o acendeu.
- **Lava:** o robô ativo que está na lava vai para o bloco seguro mais próximo. A rota continua sem entrar em lava.
- **Fogo:** o robô ativo em chamas anda ao acaso, mais rápido, evita perigos e nunca para enquanto houver para onde ir. Busca água só em chunks carregados, e ela precisa ter o topo livre e rota até ela. Inativo, fica parado. Ao apagar, volta a seguir. Os números estão em [`tiers.md`](tiers.md#valores-gerais).
- **Morte:** o robô vira um item danificado, próprio por tipo, que nenhum dano destrói, não some com o tempo, não entra em shulker box nem no inventário do robô e não pode ser colocado no mundo. Se cair no vazio, volta à última posição segura, ao dono online ou ao spawn do mundo, sem carregar chunk à força. A morte libera o vínculo, e com o dono offline isso acontece quando ele entra.
- **Conserto:** usa a `Toolbox` (`cubit:toolbox`), na grade do inventário ou na mesa de craft, num tipo de receita do mod, `cubit:robot_repair`, com os itens no JSON. Ela é consumida a cada conserto e empilha até 16. O robô sai com parte da vida, conforme [`tiers.md`](tiers.md#valores-gerais), e a bateria zerada.
- **Movimento:** a velocidade de referência está em [`tiers.md`](tiers.md#valores-gerais). Na água, o robô afunda e anda no fundo, sem se afogar. Na lava, leva dano igual ao do player. Não há dano de queda. O som de esteira entra na Fase 3.
- **Criativo:** aba própria, `cubit:main`, e colocar sempre gasta o item.
- **Estados:** ativo (ligado), inativo (desligado) e hibernação (na base, com o inventário e os slots acessíveis).
- **Painel solar:** só funciona com o Cubit ativo, de dia em todos os tiers e à noite só em Elite e Ultimate. Coberto por um bloco, não carrega. O clima não tem efeito, e no Nether e no End não funciona. Fica abaixo do consumo.
- **Induction Dock:** é a base de recarga, recebe energia por cabos ou redstone e tem um único slot, que também recarrega a Power Bank.
- **Recarga pela grade de craft:** o item do Cubit abaixo da carga de religar, combinado com uma Power Bank, recebe só o que falta para religar, na Fase 4.
- **Consumo:** gasta ao andar de verdade, sem gastar parado nem empurrado.
- **Parada do robô:** uma navegação própria cancela o recálculo de rota pendente, para o robô parado não voltar a andar.
- **Painel do slot de energia:** visual melhor e tooltips ficam para a tela própria da Fase 3.
- **Barras de bateria:** mostram ao menos um risco enquanto houver carga e só ficam vazias com 0 FE.
- **Consumo por situação (decidido, ainda não implementado):** andar custa mais, e inventário cheio, água, subida e condições negativas somam um extra. Com o inventário cheio, a velocidade cai. Os números entram em `tiers.md` junto do código.
- **Slot de energia do Cubit:** existe em todos os tiers e aceita uma Power Bank ou redstone por vez. A redstone entra na Fase 2, e a Power Bank na Fase 4.
- **Comunicação:** bipes e expressões, com poucos textos simples e diretos.
- **Sons:** oito sons, que entram na Fase 3. O bipe padrão toca nas trocas de expressão sem som exclusivo. O intervalo mínimo entre repetições será definido na Fase 3, e os sons são do desenvolvedor.
- **Modelo:** a geometria melhora já na Fase 2, com os detalhes das seis vistas, tudo fixo e sem mudar a hitbox. As animações e as expressões seguem na Fase 3.
- **Robôs especializados (Fase 5, planejada):** um único Cubit, com um card por vez, age só em área marcada e nunca sai dela. Antena e Servidor mantêm os chunks carregados, de forma opcional. Sem chunk de destino, ele espera. O detalhe está em [`robos-especializados.md`](funcionalidades/robos-especializados.md).
- **Limites:** nunca ataca o dono nem mobs pacíficos. Só ataca mobs neutros para defender o player.
- **Scanner:** `c:ores/*` significa a tag-mãe `c:ores` e suas subtags, porque tags não aceitam curinga.
- **Estado do robô como item:** inclui as configurações, além de bateria, vida, inventário, tier, nome e cor.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-05 14:13 | Registra o consumo por situação. |
| Marcelo Fabiano | 2026-11-05 13:54 | Registra o mínimo visível das barras de bateria. |
| Marcelo Fabiano | 2026-11-05 13:30 | Registra a parada do robô e o painel do slot de energia. |
| Marcelo Fabiano | 2026-11-05 12:52 | Registra o consumo ao andar. |
| Marcelo Fabiano | 2026-11-05 12:08 | Registra os robôs especializados. |
| Marcelo Fabiano | 2026-11-05 02:11 | Registra o inventário. |
| Marcelo Fabiano | 2026-11-05 02:00 | Amplia a recarga pela grade de craft. |
| Marcelo Fabiano | 2026-11-05 01:57 | Registra a recarga pela grade de craft. |
| Marcelo Fabiano | 2026-11-05 01:15 | Detalha a morte, o resgate no vazio e a receita do conserto. |
| Marcelo Fabiano | 2026-11-05 00:35 | Registra o ícone do mod. |
| Marcelo Fabiano | 2026-11-05 00:31 | Detalha o modelo da Fase 2. |
| Marcelo Fabiano | 2026-11-05 00:15 | Registra o novo lugar do mapa dos documentos. |
| Marcelo Fabiano | 2026-11-05 00:12 | Atualiza os gates e aponta os números para tiers.md, e completa os resumos de fogo, morte, sons, modelo e processo. |
| Marcelo Fabiano | 2026-11-04 23:27 | Registra a saída da lava. |
| Marcelo Fabiano | 2026-11-04 21:19 | Registra o item danificado e o conserto. |
| Marcelo Fabiano | 2026-11-04 21:02 | Detalha o comportamento em chamas. |
| Marcelo Fabiano | 2026-11-04 20:41 | Registra o fogo, a morte e o registro de perguntas. |
| Marcelo Fabiano | 2026-11-04 20:31 | Registra o dano na lava. |
| Marcelo Fabiano | 2026-11-04 20:09 | Registra as decisões de movimento. |
| Marcelo Fabiano | 2026-11-04 19:56 | Registra os gates de GameTests e datagen no push. |
| Marcelo Fabiano | 2026-11-04 19:24 | Registra o estado como item, o dono e o criativo. |
| Marcelo Fabiano | 2026-11-04 19:05 | Registra o config, a entidade e o momento das GameTests. |
| Marcelo Fabiano | 2026-11-04 18:50 | Registra as regras do robô que segue sem bateria e ao sair da base. |
| Marcelo Fabiano | 2026-11-04 17:51 | Registra a adoção dos pilares. |
| Marcelo Fabiano | 2026-11-04 17:44 | Encerra a Fase 1 e registra a regra de validação. |
| Marcelo Fabiano | 2026-11-04 17:26 | Atualiza a exceção no pack e o estado do robô como item. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra a flexibilidade para robôs especializados. |
| Marcelo Fabiano | 2026-11-04 17:16 | Registra a ideia futura de robôs especializados. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e registra a regra de idioma. |
| Marcelo Fabiano | 2026-11-04 17:11 | Registra que o controle e a base atendem todos os robôs ligados. |
| Marcelo Fabiano | 2026-11-04 17:08 | Registra a regra de vários robôs com um seguindo. |
| Marcelo Fabiano | 2026-11-04 16:57 | Reorganiza como resumo e registra as decisões das perguntas. |
| Marcelo Fabiano | 2026-11-04 16:02 | Define a persona do Cubit. |
| Marcelo Fabiano | 2026-11-04 15:58 | Adota as regras de desenvolvimento e o fluxo de branches. |
| Marcelo Fabiano | 2026-11-04 15:23 | Registra as decisões e o glossário. |
