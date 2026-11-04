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
- **Validação:** toda entrega é conferida contra os documentos `.md`, e artefatos complementam essa conferência.

## Mod

- **Energia:** o Cubit só carrega pelo Induction Dock, pela Power Bank, com redstone ou pelo painel solar.
- **Robôs especializados:** o robô atual, `cubit:general_robot`, é de uso geral. Robôs especializados são uma ideia para o futuro, fora das fases atuais, e nada pode ficar fixo de forma que comprometa essa flexibilidade.
- **Quantidade:** o player pode ter quantos robôs preferir, mas só um o segue. Os outros podem trabalhar em áreas. O controle remoto e a base atendem todos os robôs ligados.
- **Robô que segue sem bateria:** mantém o vínculo e volta a seguir ao religar. Se o player ligar outro, ele vira desligado.
- **Saída da base com outro seguindo:** o robô fica parado e desligado, como ao ser colocado.
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
