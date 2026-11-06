# Checklist

O que está pronto e o que falta em cada fase do projeto. As fases estão no [`README.md`](README.md#roadmap), e as respostas em [`_docs/perguntas/`](_docs/perguntas/README.md) prevalecem sobre este arquivo.

**Quando atualizar:** ao encerrar um PR depois do merge, avalie se algum item deve ser marcado. A marcação entra num novo commit, no PR seguinte, nunca direto na `develop`.

## Fase 1: Conceito (encerrada)

- [x] Persona, visual e expressões
- [x] Funcionalidades, itens, tiers e glossário
- [x] Regras, pilares e fluxo de trabalho
- [x] Auditoria dos `.md`, com os blocos A, B e C

## Fase 2: Versão mínima (atual)

### Funcionalidades

- [x] Projeto Gradle, gates de commit e de push
- [x] Config por tipo de robô e tier
- [x] Colocar e recolher, com o estado no item
- [x] Seguir o player, com teleporte e parada
- [x] Vários robôs, com um seguindo
- [x] Reações a fogo, lava e blocos que ferem
- [x] Modelo novo, ícone do item e ícone do mod
- [x] Morte, `Damaged Cubit`, `Toolbox` e conserto
- [x] Inventário e tela do Cubit
- [x] Bateria, consumo por situação e barras
- [x] Slot de energia com redstone
- [x] Induction Dock, hibernação e fila
- [x] `/cubit reset`
- [x] Receitas do Cubit, do Dock e da `Toolbox`

### Fechamento

- [ ] Decidir a licença dos sons (pergunta 338)
- [x] Atualizar o "Próximos" no `AGENTS.md`
- [ ] Testar numa instância só com o mod e o GeckoLib (regra 35)
- [ ] Testar no Iron Meridian, com cabos reais de FE (regra 35)
- [ ] Testar com outro player num servidor: passo 9 do Dock e passo 6 do `/cubit reset`
- [ ] Confirmar no jogo quando as receitas aparecem no livro e o `/cubit reset` com chunk descarregado
- [ ] Decidir o momento e onde publicar a `0.1.0` (pergunta 337)
- [ ] Conferir a descrição do mod
- [ ] Publicar: `homolog`, `release/0.1.0`, `main` e tag

## Fase 3: Sentidos

### Já existe

- [x] Os dez arquivos de som em `.ogg`, sem tocar no jogo
- [x] Decisões de sons, textos, expressões e painel solar

### Falta

- [ ] `sounds.json` e os nove sons tocando (o de ataque fica para o choque)
- [ ] Definir o intervalo mínimo entre repetições (pergunta 149)
- [ ] Sete expressões na tela e as reações da persona
- [ ] Animações e modelo final
- [ ] Cor do colar e indicador de bateria variando
- [ ] Textos curtos na barra acima da hotbar
- [ ] Painel solar
- [ ] Lanterna, com o LambDynamicLights entrando no build
- [ ] Confirmar a viabilidade do feixe (pergunta 17)
- [ ] Scanner de minérios e scanner de mobs, com os alertas
- [ ] Nome personalizado e cor da carcaça
- [ ] Jade
- [ ] Tela própria do inventário, com visual melhor e tooltips

## Fase 4: Upgrades e autonomia

### Já existe

- [x] Slot de energia, por enquanto só com redstone
- [x] Decisões do modo área, do "parar" e do que o item guarda (perguntas 340 a 343)
- [x] Som e expressão do ataque definidos (perguntas 316 e 347)

### Falta

- [ ] Tiers Advanced, Elite e Ultimate, com config e módulos
- [ ] Definir as receitas dos módulos, da Power Bank e do controle
- [ ] Power Bank, inclusive no Dock e na grade de craft (issue #51)
- [ ] Ímã e filtro de coleta
- [ ] Slots de tochas, fornalha e carregador de ferramentas
- [ ] Definir o custo de lanterna, ímã, scanners e tochas
- [ ] Choque e modos de defesa
- [ ] Cubit Remote: comandos, localizar e marcar área
- [ ] Modo área, com a altura máxima a definir
- [ ] Descarga em baú marcado
- [ ] Retorno automático
- [ ] Local de morte

## Fase 5: Robôs especializados (planejada)

### Já existe

- [x] Conceito em [`robos-especializados.md`](_docs/funcionalidades/robos-especializados.md) e perguntas 190 a 256, com a 344

### Falta

- [ ] Base comum: estado de trabalho, card em slot e aba de configuração
- [ ] Fazendeiro e Pescador
- [ ] Organizador, com o modo de abastecer, e fechar os detalhes dele (pergunta 208)
- [ ] Cozinheiro
- [ ] Defensor
- [ ] Minerador, com os modos definidos na entrega
- [ ] Construtor
- [ ] Pecuarista
- [ ] Antena e carregamento de chunks
- [ ] Robot Server, log e viagem até o Dock
- [ ] Storage wireless
- [ ] Definir os números "A definir" de `tiers.md`
- [ ] Decidir o slot de fornalha (pergunta 339)
- [ ] Testar cada card com 20 a 30 robôs

## Ainda sem fase

- [ ] Wiki e livro para o player (pergunta 283), e conferir se o plano permite GitHub Pages
