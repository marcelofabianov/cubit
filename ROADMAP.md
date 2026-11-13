# Roadmap

As fases do Cubit, do conceito aos trabalhos. O que está pronto em cada uma é marcado no [`checklist.md`](checklist.md), e as decisões ficam em [`_docs/perguntas/`](_docs/perguntas/README.md), que prevalecem sobre este arquivo. Os números de cada recurso ficam em [`_docs/tiers.md`](_docs/tiers.md).

Cada fase só começa com pedido explícito, e cada funcionalidade entra por issue, documentada em [`_docs/funcionalidades/`](_docs/funcionalidades/) antes do código.

## Fase 1: Conceito (encerrada)

Persona, visual, expressões, funcionalidades, itens, tiers, glossário, regras, pilares e fluxo de trabalho, em documentos.

## Fase 2: Versão mínima (encerrada)

- Crafting do Cubit, do Induction Dock e da Toolbox, com materiais do jogo puro.
- Seguir o player, com teleporte e parada, e vários Cubits com um seguindo.
- Inventário, bateria com consumo por situação e recarga com redstone.
- Induction Dock, com hibernação e fila.
- Reações a fogo, lava e blocos que ferem.
- Virar item, morte com o Damaged Cubit e conserto.
- O comando `/cubit reset` e o modelo novo.

## Fase 3: Sentidos (encerrada)

Todas as funcionalidades entraram:

- painel solar e lanterna;
- scanners de minérios e de mobs, com a caixa, a seta e as cores das antenas;
- os sons, os textos curtos e as falas;
- as animações, as expressões, o nome e a cor da carcaça;
- o Jade e as telas próprias do Cubit e do Induction Dock.

### Primeira versão: `0.1.0`

Saiu ao terminar a Fase 3, no Modrinth, com o mod funcionando só com ele e as dependências: GeckoLib e LambDynamicLights, obrigatórias, e Jade, opcional. O envio ao CurseForge, com a mesma versão, fica para depois.

## Fase 4: Upgrades e autonomia

Dividida em três partes, na ordem em que uma depende da outra.

### Fase 4: Tier e ataque (encerrada)

- Tiers Advanced, Elite e Ultimate, com os módulos de upgrade.
- Choque, com o modo Pacífico, o único do tier Base.
- Modos de defesa Contra-atacar e Agressivo.
- Modo de defesa só o alvo, com o último mob atacado.
- Interface de configuração na aba Settings, com a chave geral e a do painel solar.

### Fase 4.1: Itens, filtros, carregamento e mais (encerrada)

- Ímã e coleta de itens do chão, com o filtro em lista branca e lista negra.
- Power Bank, também no Dock e na grade de craft.
- Inventário do Induction Dock, numa aba nova, para a Toolbox e as Power Banks, que ele carrega.
- Slot exclusivo de tochas, do Advanced em diante, com o Cubit colocando tochas sozinho.

### Fase 4.2: Tablet, área e retorno

- Cubit Tablet, que substitui o Cubit Remote: tela de seleção e tela do Cubit, com os apps Open, Alvo e Dock.
- Modo área e descarga em baú marcado, com os apps Área e Descarga do Tablet e o botão Descarregar.
- Retorno automático e local de morte, com as linhas deles na aba Recursos do Tablet.

## Fase 5: Trabalhos (planejada)

Dividida em três partes.

### Fase 5: Antenas, Servidor e storage

- Antenas, com a ligação ao Servidor, o log e o Dock de referência.
- Robot Server, com o log dos Cubits e a viagem até o Dock.
- Storage wireless virtual.

### Fase 5.1: Carregamento de chunks

- Carregamento de chunks pelas antenas e pelo Servidor, com os limites do config.

### Fase 5.2: Cards de Aprendizado de Máquina

- Base comum dos trabalhos: estado de trabalho, card em slot e aba de configuração.
- Hibernação fora do Dock.
- Personalização do Cubit de acordo com o card recebido.
- Os trabalhos: Fazendeiro, Pescador, Organizador, Cozinheiro, Defensor, Minerador, Construtor e Pecuarista.

## Ainda sem fase

- Wiki e livro para o player.
