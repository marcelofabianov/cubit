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

## Fase 3: Sentidos (atual)

A ordem é a da numeração (perguntas 356 e 365), com a lanterna antes do painel solar (pergunta 458) e o item 17 antes do 16 (pergunta 504). Sons, intervalo, expressões, colar e textos entram em partes, e um item só é marcado quando estiver completo na Fase 3.

### Já existe

1. [x] Os onze arquivos de som em `.ogg`
2. [x] Decisões de sons, textos, expressões e painel solar
3. [x] Esteira, curto elétrico, ligar, desligar, recarga, colocar e bipe padrão tocando
4. [x] Oito expressões em uso, com piscada e olhar na normal
5. [x] Som de dano com `short_circuit.ogg` e barra de vida no inventário
6. [x] Cubit um pouco mais ágil (pergunta 392)

### Falta

7. [ ] Os dois alertas de som, que dependem dos scanners
8. [ ] Alerta e surpreso em uso, que dependem dos scanners, e as reações ao renomear e pintar
9. [x] Animações e modelo final
10. [x] Cor do colar e indicador de bateria variando
11. [x] Definir o intervalo mínimo entre repetições (perguntas 149, 433 e 444), junto dos textos
12. [ ] Textos curtos na barra acima da hotbar
13. [x] Painel solar, sem as chaves, que entram com a interface de configuração (perguntas 494 e 495)
14. [x] Lanterna, com a chave dela, o LambDynamicLights entrando no build e a claridade fraca da tela (perguntas 378 e 424)
15. [x] Feixe: saiu da Fase 3 e foi para o Minerador, na Fase 5 (pergunta 463)
16. [ ] Scanner de minérios e scanner de mobs, com a chave deles e os alertas
17. [ ] Nome personalizado e cor da carcaça
18. [ ] Jade
19. [ ] Telas próprias do Cubit e do Induction Dock, com janelas personalizadas, textura de metal de robô e tooltips, sem o padrão do Minecraft (pergunta 371)

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
7. [ ] Ímã e coleta, com as chaves deles e o filtro em lista branca e lista negra (perguntas 422 a 424)
8. [ ] Slots de tochas, fornalha e carregador de ferramentas
9. [ ] Definir o custo de ímã, scanners e tochas
10. [ ] Choque e modos de defesa
11. [ ] Cubit Remote: comandos, localizar e marcar área
12. [ ] Modo área, com a altura máxima a definir
13. [ ] Descarga em baú marcado, com a chave dela
14. [ ] Retorno automático, com a chave dele
15. [ ] Local de morte, com a chave dele

## Fase 5: Trabalhos (planejada)

### Já existe

1. [x] Conceito em [`trabalhos.md`](_docs/funcionalidades/trabalhos.md) e perguntas 190 a 256, com a 344

### Falta

2. [ ] Base comum: estado de trabalho, card em slot e aba de configuração
3. [ ] Fazendeiro e Pescador
4. [ ] Organizador, com o modo de abastecer, e fechar os detalhes dele (pergunta 208)
5. [ ] Cozinheiro
6. [ ] Defensor
7. [ ] Minerador, com os modos definidos na entrega e a lanterna de cabeça com feixe (pergunta 463)
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
