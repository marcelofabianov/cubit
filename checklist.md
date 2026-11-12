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

## Fase 3: Sentidos (encerrada)

A ordem é a da numeração (perguntas 356 e 365), com a lanterna antes do painel solar (pergunta 458) e o item 17 antes do 16 (pergunta 504). Sons, intervalo, expressões, colar e textos entram em partes, e um item só é marcado quando estiver completo na Fase 3.

### Já existe

1. [x] Os onze arquivos de som em `.ogg`
2. [x] Decisões de sons, textos, expressões e painel solar
3. [x] Esteira, curto elétrico, ligar, desligar, recarga, colocar e bipe padrão tocando
4. [x] Oito expressões em uso, com piscada e olhar na normal
5. [x] Som de dano com `short_circuit.ogg` e barra de vida no inventário
6. [x] Cubit um pouco mais ágil (pergunta 392)

### Falta

7. [x] Os dois alertas de som, que dependem dos scanners
8. [x] Alerta e surpreso em uso, que dependem dos scanners, e as reações ao renomear e pintar
9. [x] Animações e modelo final
10. [x] Cor do colar e indicador de bateria variando
11. [x] Definir o intervalo mínimo entre repetições (perguntas 149, 433 e 444), junto dos textos
12. [x] Textos curtos na barra acima da hotbar
13. [x] Painel solar, sem as chaves, que entram com a interface de configuração (perguntas 494 e 495)
14. [x] Lanterna, com a chave dela, o LambDynamicLights entrando no build e a claridade fraca da tela (perguntas 378 e 424)
15. [x] Feixe: saiu da Fase 3 e foi para o Minerador, na Fase 5 (pergunta 463)
16. [x] Scanner de minérios e scanner de mobs, com a chave deles e os alertas
17. [x] Nome personalizado e cor da carcaça
18. [x] Jade
19. [x] Telas próprias do Cubit e do Induction Dock, com janelas personalizadas, textura de metal de robô e tooltips, sem o padrão do Minecraft (pergunta 371)
20. [x] Falas do Cubit sobre o tempo e as conquistas, na mesma entrega dos scanners (perguntas 572 a 583)

## Primeira versão: `0.1.0`

Sai só ao terminar todos os itens da Fase 3 (pergunta 337).

1. [x] Decidir o momento da publicação
2. [x] Decidir a licença dos sons (pergunta 338)
3. [x] Decidir onde publicar: Modrinth e CurseForge (pergunta 714)
4. [x] Rever a descrição do mod, no jogo e nas plataformas (PR #134)
5. [x] Testar a versão completa no Iron Meridian, inclusive os cabos reais de FE no Dock. A instância só com o mod ficou de fora nesta versão, como exceção à regra 35 (perguntas 774 e 776)
6. [x] Outro player: o passo 9 do Dock, o 6 do `/cubit reset` e o 13 da cor e do nome ficam cobertos pelas GameTests, sem teste no jogo (perguntas 770 a 772)
7. [x] Confirmações no jogo: saíram da `0.1.0` e ficam para depois dela, com a instância só com o mod: quando as receitas aparecem no livro, a receita do Dock em L espelhado (pergunta 351), o `/cubit reset` com chunk descarregado e o padrão da opção de luz de entidades do LambDynamicLights (perguntas 461, 719 e 776)
8. [x] Conferir os termos das ferramentas de IA usadas nos sons antes de publicar (perguntas 338, 719 e 777)
9. [x] Publicar: `homolog`, `release/0.1.0`, `main`, tag, release no GitHub e envio ao Modrinth. O envio ao CurseForge fica para depois da `0.1.0` (perguntas 744, 778, 785 e 787)

## Fase 4: Upgrades e autonomia

Dividida em três partes, na ordem em que dependem uma da outra (perguntas 722 a 726, reorganizadas pelas 794 a 796). "Fase 4" nos documentos vale para as três.

### Já existe

1. [x] Slot de energia, por enquanto só com redstone
2. [x] Decisões do modo área, do "parar" e do que o item guarda (perguntas 340 a 343)
3. [x] Som e expressão do ataque definidos (perguntas 316 e 347)

### Fase 4: Tier e ataque

4. [x] Tiers Advanced, Elite e Ultimate, com config, módulos e as receitas deles (PRs #145 e #147)
5. [x] Choque e modo Pacífico, o único do tier Base (perguntas 722 e 796), na mesma entrega do item 6 (pergunta 822, PR #149)
6. [x] Modos Contra-atacar e Agressivo (PR #149)
7. [x] Modo só o alvo, com o último mob atacado (perguntas 577, 578 e 795), como o modo Focus (PR #151)
8. [x] Interface de configuração na aba Settings: chave geral e chave do painel solar (perguntas 717 e 425, PR #153)

### Fase 4.1: Itens, filtros, carregamento e mais

9. [ ] Ímã e coleta, com as chaves deles e o filtro em lista branca e lista negra (perguntas 422 a 424), e o custo do ímã
10. [ ] Power Bank, inclusive no Dock e na grade de craft (issue #51), com a receita dela
11. [ ] Slots de tochas, fornalha e carregador de ferramentas, com o Cubit colocando tochas e o custo delas (pergunta 724)

### Fase 4.2: Remote, área e retorno

12. [ ] Cubit Remote: comandos, localizar e marcar área, com a receita dele e a mira do modo só o alvo (perguntas 726 e 795)
13. [ ] Modo área, com a altura máxima a definir
14. [ ] Descarga em baú marcado, com a chave dela
15. [ ] Retorno automático, com a chave dele
16. [ ] Local de morte, com a chave dele

## Fase 5: Trabalhos (planejada)

Dividida em três partes (perguntas 722 e 725). "Fase 5" nos documentos vale para as três.

### Já existe

1. [x] Conceito em [`trabalhos.md`](_docs/funcionalidades/trabalhos.md) e perguntas 190 a 256, com a 344

### Fase 5: Antenas, Servidor e storage

2. [ ] Antenas: ligação ao Servidor, log e Dock de referência
3. [ ] Robot Server, log e viagem até o Dock
4. [ ] Storage wireless virtual

### Fase 5.1: Carregamento de chunks

5. [ ] Carregamento de chunks pelas antenas e pelo Servidor, com os limites do config

### Fase 5.2: Cards de Aprendizado de Máquina

6. [ ] Base comum: estado de trabalho, card em slot e aba de configuração
7. [ ] Hibernação fora do Dock
8. [ ] Conceito e entrega da personalização do Cubit de acordo com o card (pergunta 725)
9. [ ] Fazendeiro e Pescador
10. [ ] Organizador, com o modo de abastecer, e fechar os detalhes dele (pergunta 208)
11. [ ] Cozinheiro
12. [ ] Defensor
13. [ ] Minerador, com os modos definidos na entrega e a lanterna de cabeça com feixe (pergunta 463)
14. [ ] Construtor
15. [ ] Pecuarista
16. [ ] Definir os números "A definir" de `tiers.md`
17. [ ] Decidir o slot de fornalha (pergunta 339)
18. [ ] Testar cada card com 20 a 30 robôs

## Ainda sem fase

1. [ ] Wiki e livro para o player (pergunta 283), e conferir se o plano permite GitHub Pages
