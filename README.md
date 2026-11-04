# Cubit

<p align="center">
  <img src="_docs/imagens/conceito/png/expressao-feliz.png" alt="Cubit com a expressão feliz" width="240">
</p>

Um robô companheiro fofo, movido a bateria, para Minecraft 1.21.1 com NeoForge, feito para o modpack [Iron Meridian](https://github.com/marcelofabianov/iron-meridian). Sem magia: o Cubit é pura tecnologia, craftado com recursos do jogo puro.

> **Status: fase de conceito.** Ainda não há código nem versão publicada. Tudo abaixo está planejado, e os números são o ponto de partida para o balanceamento.

## O que o Cubit faz

O Cubit segue você e ajuda em tarefas simples:

- **Carrega itens** no próprio inventário e coleta o que está no chão, com um filtro de coleta.
- **Funciona com bateria**, que você precisa gerenciar. Sem carga, ele desliga, e só dá para recolhê-lo ou colocar energia nele.
- **Recarrega** no Induction Dock, uma base fixa alimentada por FE de qualquer mod ou por redstone, onde ele hiberna com o inventário acessível. Também recarrega com uma Power Bank ou redstone no slot exclusivo, ou pelo painel solar, com o céu aberto.
- **Vira item**, mantendo bateria, vida, inventário, nome, cor, tier e configurações.
- **Ilumina o caminho** e coloca tochas sozinho.
- **Avisa você** sobre mobs hostis e procura os minérios que você escolher.
- **Protege você** com um choque leve, nos modos pacífico, contra-atacar e agressivo. Nunca ataca o dono nem mobs pacíficos.
- **Trabalha sozinho**: atua numa área marcada, descarrega em baús marcados, funde minérios, carrega suas ferramentas elétricas e guia você até o local da sua morte.
- **Tem personalidade**: a tela do rosto muda de expressão, e ele se comunica com bipes e poucos textos curtos.

Você pode ter quantos Cubits quiser trabalhando em áreas marcadas, mas só um segue você por vez.

## Visual

Um corpo branco e arredondado, com uma tela no rosto e olhos que brilham, duas antenas, braços com garras e esteiras no lugar dos pés. Tem um painel solar em cima, um indicador de bateria nas costas e uma bobina de indução embaixo. O colar no pescoço mostra o tier, e a cor da carcaça muda com corantes.

![Prancha de conceito do Cubit](_docs/imagens/conceito/robo-conceito-prancha.png)

## Tiers

O Cubit mantém os recursos ao evoluir, e cada tier os melhora. Os recursos avançados ficam bloqueados até o tier certo.

| | Base | Avançado | Elite | Ultimate |
|---|---|---|---|---|
| Cor do colar | Cinza | Azul | Roxo | Dourado |
| Vida | 25 | 50 | 100 | 200 |
| Velocidade | 1,0× | 1,25× | 1,5× | 2,0× |
| Bateria | 300 mil FE | 600 mil FE | 3 mi FE | 8 mi FE |
| Autonomia em movimento, sem o painel | ~25 min | ~42 min | ~2,8 h | ~5,6 h |
| Capacidade da Power Bank | 210 mil FE | 420 mil FE | 2,1 mi FE | 5,6 mi FE |
| Inventário | 27 slots | 36 slots | 45 slots | 54 slots |
| Raio do ímã | 1 bloco | 3 blocos | 5 blocos | 8 blocos |
| Raio do scanner | 5 blocos | 10 blocos | 16 blocos | 24 blocos |
| Modos de defesa | Pacífico | + Contra-atacar | + Agressivo | Todos |
| Modo área | Bloqueado | Até 16×16 | Até 32×32 | Até 64×64 |

## Itens e blocos

Os nomes estão em inglês porque são os que aparecem no jogo.

| Nome | Descrição |
|---|---|
| Cubit | Robô companheiro, colocado no mundo como item |
| Cubit Remote | Envia comandos aos Cubits, marca áreas de trabalho e localiza os Cubits |
| Advanced Module | Leva o Cubit do tier Base para o Advanced |
| Elite Module | Leva o Cubit do tier Advanced para o Elite |
| Ultimate Module | Leva o Cubit do tier Elite para o Ultimate |
| Induction Dock | Base de recarga do Cubit, alimentada por cabos ou redstone |
| Base Power Bank | Bateria portátil com a capacidade do tier Base |
| Advanced Power Bank | Bateria portátil com a capacidade do tier Advanced |
| Elite Power Bank | Bateria portátil com a capacidade do tier Elite |
| Ultimate Power Bank | Bateria portátil com a capacidade do tier Ultimate |

As receitas usarão só itens do jogo puro e virão em datapack JSON, para os modpacks poderem sobrescrevê-las.

## Roadmap

1. **Conceito (atual):** persona, features e demais definições, em documentos.
2. **Versão mínima:** crafting, seguir o player, inventário, bateria, vários Cubits com um seguindo, Induction Dock com hibernação, recarga com redstone e virar item.
3. **Sentidos:** painel solar, lanterna, scanners, bipes e textos curtos, expressões na tela, nome, cor e Jade.
4. **Upgrades e autonomia:** tiers, Power Bank, ímã, filtros, demais slots exclusivos, choque, modos de defesa, Cubit Remote, modo área, descarga em baú, retorno automático e local de morte.

## Stack

- Minecraft 1.21.1, NeoForge 21.1.x e Java 21
- [GeckoLib](https://modrinth.com/mod/geckolib) para o modelo e as animações
- [LambDynamicLights](https://modrinth.com/mod/lambdynamiclights) como dependência opcional, para a luz dinâmica
- Forge Energy (FE), pelas capabilities do NeoForge

## Documentação

- [`robo-companheiro.md`](robo-companheiro.md): visão geral, fases e mapa dos documentos
- [`_docs/`](_docs/): persona, visual, funcionalidades, itens, tiers, decisões, perguntas por fase e glossário
- [`AGENTS.md`](AGENTS.md): regras e contexto do projeto para desenvolvedores e agentes

## Licença

[MIT](LICENSE)

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 17:26 | Atualiza o roadmap e padroniza o termo colar. |
| Marcelo Fabiano | 2026-11-04 17:15 | Traduz o README para português, conforme a regra de idioma. |
| Marcelo Fabiano | 2026-11-04 17:11 | Adiciona a função de localizar ao Cubit Remote. |
| Marcelo Fabiano | 2026-11-04 17:08 | Permite vários Cubits com um seguindo e atualiza o texto da Power Bank e do Dock. |
| Marcelo Fabiano | 2026-11-04 16:57 | Atualiza a recarga, o painel solar, a hibernação, a Power Bank, o roadmap e a licença. |
| Marcelo Fabiano | 2026-11-04 16:02 | Adiciona a imagem do Cubit feliz ao README. |
| Marcelo Fabiano | 2026-11-04 15:23 | Adiciona a visão geral, o README e as regras do projeto. |
