# Decisões

Resumo do que já foi decidido no projeto. As perguntas, abertas e respondidas, ficam em [`perguntas/`](perguntas/README.md).

## Projeto

- **Nome do mod e do robô:** Cubit.
- **Modid:** `cubit`.
- **Pacote Java:** `io.github.marcelofabianov.cubit`.
- **Repositório:** `marcelofabianov/cubit`.
- **Licença:** MIT.
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
- **Regras de desenvolvimento:** no [`AGENTS.md`](../AGENTS.md#regras-de-desenvolvimento). Os gates de commit estão ativos, e os de Java e Gradle entram na Fase 2.
- **Proteção de branches:** o plano atual do GitHub não permite em repositório privado, então valem só os gates locais.
- **Fonte da verdade:** as respostas em `perguntas/` prevalecem sobre os outros documentos.
- **Pilares:** as decisões técnicas seguem a hierarquia de [`pilares.md`](desenvolvimento/pilares.md).
- **Registro de perguntas:** toda pergunta respondida pelo desenvolvedor entra em `perguntas/` antes do próximo passo.
- **Validação:** toda entrega é conferida contra os documentos `.md`, e artefatos complementam essa conferência.
- **Config e testes na Fase 2:** o config entra antes da entidade e cresce a cada etapa. As GameTests e o gate de datagen entram na etapa "Cubit no mundo".
- **Gates de push:** as GameTests também detectam classe de cliente carregada no servidor, sem exigir o EULA, então o `runServer` não é um gate. O datagen reprova se `src/generated` ficar diferente do Git.

## Mod

- **Energia:** o Cubit só carrega pelo Induction Dock, pela Power Bank, com redstone ou pelo painel solar.
- **Robôs especializados:** o robô atual, `cubit:general_robot`, é de uso geral. Robôs especializados são uma ideia para o futuro, fora das fases atuais, e nada pode ficar fixo de forma que comprometa essa flexibilidade.
- **Quantidade:** o player pode ter quantos robôs preferir, mas só um o segue. Os outros podem trabalhar em áreas. O controle remoto e a base atendem todos os robôs ligados.
- **Robô que segue sem bateria:** mantém o vínculo e volta a seguir ao religar. Se o player ligar outro, ele vira desligado.
- **Saída da base com outro seguindo:** o robô fica parado e desligado, como ao ser colocado.
- **Config:** `COMMON`, com chaves em `snake_case` por tipo de robô e tier, como `[general_robot.base]`.
- **Entidade:** a `RobotEntity` herda de `PathfinderMob`, com o dono num UUID próprio.
- **Estado como item:** um único componente, `cubit:robot_state`.
- **Dono:** quem coloca o robô. O item não guarda o dono. Nenhum dano atribuído a ele fere o robô.
- **Lava:** o robô ativo que está na lava vai para o bloco seguro mais próximo. A rota continua sem entrar em lava.
- **Fogo:** o robô ativo em chamas anda ao acaso a 1,25× da velocidade, evita perigos e busca água em até 8 blocos. Ao apagar, volta a seguir.
- **Morte:** o robô vira um item danificado, próprio por tipo, que não queima, não some com o tempo e não pode ser colocado no mundo. Conserta com uma caixa de ferramentas, consumida no craft, e sai com 25% da vida e a bateria zerada.
- **Movimento:** velocidade de referência de 0,3. Na água, o robô afunda e anda no fundo, sem se afogar. Na lava, leva dano igual ao do player. Não há dano de queda. O som de esteira entra na Fase 3.
- **Criativo:** aba própria, `cubit:main`, e colocar sempre gasta o item.
- **Estados:** ativo (ligado), inativo (desligado) e hibernação (na base, com o inventário e os slots acessíveis).
- **Painel solar:** só funciona com o Cubit ativo, de dia em todos os tiers e à noite só em Elite e Ultimate. Coberto por um bloco, não carrega. O clima não tem efeito, e no Nether e no End não funciona. Fica abaixo do consumo.
- **Induction Dock:** é a base de recarga, recebe energia por cabos ou redstone e tem um único slot, que também recarrega a Power Bank.
- **Slot de energia do Cubit:** existe em todos os tiers e aceita uma Power Bank ou redstone por vez. A redstone entra na Fase 2, e a Power Bank na Fase 4.
- **Comunicação:** bipes e expressões, com poucos textos simples e diretos.
- **Limites:** nunca ataca o dono nem mobs pacíficos. Só ataca mobs neutros para defender o player.
- **Scanner:** `c:ores/*` significa a tag-mãe `c:ores` e suas subtags, porque tags não aceitam curinga.
- **Estado do robô como item:** inclui as configurações, além de bateria, vida, inventário, tier, nome e cor.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
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
