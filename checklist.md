# Checklist

O que está pronto e o que falta em cada fase do projeto. As fases estão no [`README.md`](README.md#roadmap), e as respostas em [`_docs/perguntas/`](_docs/perguntas/README.md) prevalecem sobre este arquivo.

**Quando atualizar:** ao encerrar um PR depois do merge, avalie se algum item deve ser marcado. A marcação entra num novo commit, no PR seguinte, nunca direto na `develop`.

## Fase 1: Conceito (encerrada)

1. [x] Persona, visual e expressões
2. [x] Funcionalidades, itens, tiers e glossário
3. [x] Regras, pilares e fluxo de trabalho
4. [x] Auditoria dos `.md`, com os blocos A, B e C

## Fase 2: Versão mínima (encerrada)

### Funcionalidades

1. [x] Projeto Gradle, gates de commit e de push
2. [x] Config por tipo de robô e tier
3. [x] Colocar e recolher, com o estado no item
4. [x] Seguir o player, com teleporte e parada
5. [x] Vários robôs, com um seguindo
6. [x] Reações a fogo, lava e blocos que ferem
7. [x] Modelo novo, ícone do item e ícone do mod
8. [x] Morte, `Damaged Cubit`, `Toolbox` e conserto
9. [x] Inventário e tela do Cubit
10. [x] Bateria, consumo por situação e barras
11. [x] Slot de energia com redstone
12. [x] Induction Dock, hibernação e fila
13. [x] `/cubit reset`
14. [x] Receitas do Cubit, do Dock e da `Toolbox`

### Fechamento

15. [x] Atualizar o "Próximos" no `AGENTS.md`

## Fase 3: Sentidos (próxima)

A ordem é a da numeração (pergunta 356). Sons, intervalo, expressões, colar, textos e feixe entram em partes, e um item só é marcado quando estiver completo na Fase 3.

### Já existe

1. [x] Os onze arquivos de som em `.ogg`
2. [x] Decisões de sons, textos, expressões e painel solar

### Falta

3. [ ] `sounds.json` e os nove sons tocando (o de ataque fica para o choque)
4. [ ] Definir o intervalo mínimo entre repetições (pergunta 149)
5. [ ] Sete expressões na tela e as reações da persona
6. [ ] Animações e modelo final
7. [ ] Cor do colar e indicador de bateria variando
8. [ ] Textos curtos na barra acima da hotbar
9. [ ] Painel solar
10. [ ] Lanterna, com o LambDynamicLights entrando no build
11. [ ] Confirmar a viabilidade do feixe (pergunta 17)
12. [ ] Scanner de minérios e scanner de mobs, com os alertas
13. [ ] Nome personalizado e cor da carcaça
14. [ ] Jade
15. [ ] Tela própria do inventário, com visual melhor e tooltips

## Primeira versão: `0.1.0`

Sai só ao terminar todos os itens da Fase 3 (pergunta 337).

1. [x] Decidir o momento da publicação
2. [x] Decidir a licença dos sons (pergunta 338)
3. [ ] Decidir onde publicar: Modrinth, CurseForge ou os dois
4. [ ] Rever a descrição do mod, que hoje descreve só a Fase 2
5. [ ] Testar a versão completa numa instância só com o mod e as dependências e no Iron Meridian (regra 35), inclusive os cabos reais de FE no Dock
6. [ ] Testar com outro player num servidor: passo 9 do Dock e passo 6 do `/cubit reset`
7. [ ] Confirmar no jogo quando as receitas aparecem no livro e o `/cubit reset` com chunk descarregado
8. [ ] Publicar: `homolog`, `release/0.1.0`, `main` e tag

## Fase 4: Upgrades e autonomia

### Já existe

1. [x] Slot de energia, por enquanto só com redstone
2. [x] Decisões do modo área, do "parar" e do que o item guarda (perguntas 340 a 343)
3. [x] Som e expressão do ataque definidos (perguntas 316 e 347)

### Falta

4. [ ] Tiers Advanced, Elite e Ultimate, com config e módulos
5. [ ] Definir as receitas dos módulos, da Power Bank e do controle
6. [ ] Power Bank, inclusive no Dock e na grade de craft (issue #51)
7. [ ] Ímã e filtro de coleta
8. [ ] Slots de tochas, fornalha e carregador de ferramentas
9. [ ] Definir o custo de lanterna, ímã, scanners e tochas
10. [ ] Choque e modos de defesa
11. [ ] Cubit Remote: comandos, localizar e marcar área
12. [ ] Modo área, com a altura máxima a definir
13. [ ] Descarga em baú marcado
14. [ ] Retorno automático
15. [ ] Local de morte

## Fase 5: Robôs especializados (planejada)

### Já existe

1. [x] Conceito em [`robos-especializados.md`](_docs/funcionalidades/robos-especializados.md) e perguntas 190 a 256, com a 344

### Falta

2. [ ] Base comum: estado de trabalho, card em slot e aba de configuração
3. [ ] Fazendeiro e Pescador
4. [ ] Organizador, com o modo de abastecer, e fechar os detalhes dele (pergunta 208)
5. [ ] Cozinheiro
6. [ ] Defensor
7. [ ] Minerador, com os modos definidos na entrega
8. [ ] Construtor
9. [ ] Pecuarista
10. [ ] Antena e carregamento de chunks
11. [ ] Robot Server, log e viagem até o Dock
12. [ ] Storage wireless
13. [ ] Definir os números "A definir" de `tiers.md`
14. [ ] Decidir o slot de fornalha (pergunta 339)
15. [ ] Testar cada card com 20 a 30 robôs

## Ainda sem fase

1. [ ] Wiki e livro para o player (pergunta 283), e conferir se o plano permite GitHub Pages
