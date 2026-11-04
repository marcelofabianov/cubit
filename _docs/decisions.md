# Decisões

## Decididas

- **Nome do mod e do robô:** Cubit.
- **Modid:** `cubit`.
- **Pacote Java:** `io.github.marcelofabianov.cubit`.
- **Idioma do mod:** tudo só em inglês, com um único `en_us.json`.
- **Repositório:** `marcelofabianov/cubit`.
- **Etapa atual:** a Fase 1 é o conceito, feito em arquivos `.md`, sem código.
- **Idioma dos commits:** português, em Conventional Commits.
- **Fluxo de Git:** Gitflow, Semantic Versioning e Conventional Commits, com `homolog` para a release em teste e `release/X.Y.Z` para a release fechada. O fluxo completo está no [`AGENTS.md`](../AGENTS.md#fluxo-de-branches).
- **Regras de desenvolvimento:** no [`AGENTS.md`](../AGENTS.md#regras-de-desenvolvimento). Os gates de commit estão ativos, e os de Java e Gradle entram na Fase 2.

## Em aberto

- **Nome dos upgrades:** `Module` (usado por enquanto) ou `Upgrade`.
- **Receitas** de todos os itens de [`items.md`](items.md).
- **Balanceamento do solar:** pelos números de [`tiers.md`](tiers.md#energia), o Ultimate e o Elite ganham mais energia do que gastam de dia. Se a ideia é que ele sempre precise voltar à estação, reduzir o solar ou aumentar o consumo.
- **Lanterna:** confirmar a solução do feixe para os tiers Elite e Ultimate, por causa do limite de luz 15.
- **Raio da estação de carga:** confirmar se o raio do retorno automático deve mesmo crescer por tier.
- **Modelo próprio:** criar o design final a partir das referências, sem copiar um robô existente.
- **Expressões:** confirmar normal, carregando e desligado, que só aparecem no conceito.
- **Persona:** perguntas em [`persona.md`](concept/persona.md).
- **Sobreposição com o Mekanism:** até onde sei, o Robit do Mekanism também segue o player, carrega itens e recarrega numa estação. A regra 3 do pack proíbe mod que repete a função de outro, então é preciso confirmar se o Cubit entra como exceção.
- **Licença:** ainda não definida.
- **Primeira versão:** recomendação ainda não aprovada de começar em `0.1.0` e só lançar a `1.0.0` com o mod completo.
- **Proteção de branches:** recomendação ainda não aprovada de proteger `main`, `homolog` e `develop` no GitHub, se o plano da conta permitir em repositório privado.

## Power Bank

Definido: uma Power Bank por tier, com 70% da bateria interna do tier correspondente, carregada no inventário do player e extraída pelo robô num slot exclusivo.

Recomendações **ainda não aprovadas**:

| Tema | Lacuna | Recomendação |
|---|---|---|
| Robô desligado | Com a bateria zerada, o robô só aceita ser recolhido, então o player não consegue colocar a Power Bank | Abrir uma exceção: desligado, ele também aceita a Power Bank, que é o melhor uso dela |
| Slot no Base | Os outros slots exclusivos ficam bloqueados no Base, mas existe uma Power Bank Base | Liberar o slot em todos os tiers |
| Compatibilidade | Não está definido se uma Power Bank funciona em robô de outro tier | Qualquer Power Bank funciona em qualquer tier, porque a diferença já está na capacidade |
| Velocidade | A taxa de extração não está definida | Igual à recarga na estação do tier do robô, configurável |
| Como recarregar | Não está definido onde a Power Bank recarrega | No Induction Dock e em qualquer carregador de FE do pack, pela capability de energia do item |
| Carga inicial | Não está definido se sai do craft com energia | Vazia |
| Fase | Não está definido quando entra | Fase 4, junto dos slots exclusivos |
| Equilíbrio | A Power Bank reduz a necessidade de voltar à estação, que é parte da proposta da bateria | Manter os 70%, e avaliar nos testes o limite de uma por slot e o custo da receita |

## Lacunas para a Fase 2 (versão mínima)

Cada item traz uma recomendação, **ainda não aprovada**.

| Tema | Lacuna | Recomendação |
|---|---|---|
| Recolher o robô | O pedido inicial diz que só pode ser recolhido desligado; as features permitem a qualquer momento | Sempre permitir; desligado, é a única ação |
| Como carregar | Na versão mínima o robô sempre segue o player, sem comando de parar | Carregar dentro de um raio de indução configurável, de 4 blocos, separado do raio de retorno automático |
| Religar | Não está definido quando volta a funcionar | Religar sozinho com 5% de bateria, para não ficar ligando e desligando |
| Consumo parado | A tabela só define o consumo em movimento | 0 FE/t, configurável |
| Carga inicial | Sem carga e sem gerador, o robô recém-craftado fica inútil | 25% ao sair do craft, configurável |
| Vida e morte | Morte, reparo e vida preservada no item não estão definidos | Ao morrer, vira item caído com inventário preservado e bateria zerada; a vida fica no item e se recupera na estação; o dono não fere o robô |
| Robô ativo | Não está definido se conta desligado, em chunk descarregado ou em outra dimensão | Conta da colocação até ser recolhido ou morrer; um segundo robô é recusado com a última posição conhecida; teleporte só na mesma dimensão; `/cubit reset` limpa o vínculo |
| Estação | Buffer e taxa de entrada não estão definidos | 100 mil FE e 1.000 FE/t, configuráveis |
| FE em desenvolvimento | O cliente de dev não tem gerador | Powah como dependência só de runtime local (dependências a confirmar); alternativa: comando de debug |

### Assumidos, salvo objeção

- Clique direito abre o inventário, e Shift + clique direito recolhe.
- GeckoLib já na versão mínima, com geometria provisória.
- O item empilha em 1 e mostra a bateria numa barra.
- O item não entra no inventário do robô nem em shulker boxes.
- A rota do robô evita água e lava, e ele não leva dano de queda.
- O config só tem os valores do Base por enquanto, com estrutura por tier.
- Controle remoto, solar, Jade, nome e cor ficam fora da versão mínima.

## Ajustes pendentes nas features

- **Fases:** nenhuma inclui explicitamente solar, scanners, controle remoto, nome, cor ou Jade, e os sons de minérios da Fase 3 dependem do scanner.
- **Minérios raros:** "de ferro para cima" não sai das tags e vai exigir uma lista configurável.
- **`c:ores/*`:** tags não aceitam curinga, então a regra significa a tag-mãe `c:ores` e suas subtags.
- **Estado preservado:** a regra de virar item não cita as configurações, mas os data components sim. Valem os data components.
