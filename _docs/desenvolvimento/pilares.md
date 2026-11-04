# Pilares de desenvolvimento

Os pilares estão em **ordem de prioridade**. Quando dois princípios entram em conflito, vence o de maior prioridade, e toda decisão técnica deve poder ser justificada por essa hierarquia.

```text
Segurança > Defesa > Fronteira de módulo > Contrato externo > Comportamento testado >
Design sólido > Clareza > Simplicidade > Sem repetição > Sem excesso > Domínio > Regra do Escoteiro
```

As regras numeradas citadas aqui são as do [`AGENTS.md`](../../AGENTS.md).

## 1. Segurança

**O que o Cubit protege é o mundo do player e a máquina dele.** Um mod roda com acesso total ao jogo. Um save corrompido ou itens perdidos custam horas de jogo.

| Risco | Como o projeto se protege |
|---|---|
| Controle de acesso | Toda ação confere o dono do robô. O servidor valida dono e distância de todo pacote recebido (regra 20) |
| Dados recebidos do cliente | O cliente só pede ações, e o servidor decide (regra 19) |
| Cadeia de dependências | Dependência nova é decisão bloqueante. As versões são fixas, e o wrapper tem o checksum conferido |
| Segredos | Nada de segredo no repositório, e o `gitleaks` roda em todo commit |
| Integridade do save | IDs, data components, NBT e chaves de config não mudam sem migração (regra 18) |
| Perda de itens | Recolher, morrer e colocar nunca apagam o inventário nem duplicam itens |
| Condições excepcionais | Dado inválido não derruba o jogo (regra 17) |

### Checklist

- [ ] A ação confere o dono do robô no servidor?
- [ ] O pacote recebido do cliente é validado?
- [ ] Nenhuma operação duplica nem apaga itens ou energia?
- [ ] A mudança preserva os saves existentes?

## 2. Defesa

**O código resiste a entrada inesperada, estado inválido e falha de dependência.**

- **Validar na fronteira:** NBT, data components, config e pacotes são validados antes de entrar no domínio.
- **Não confiar em dado de runtime:** entidade pode ter sumido, chunk pode estar descarregado, e o UUID salvo pode não apontar para nada (regra 22).
- **Tratamento único:** a falha é registrada ou propagada, nunca as duas coisas.
- **Falhar seguro:** diante de dado inválido, o mod assume o estado mais restritivo e registra no log (regra 17).
- **Falhar cedo ao iniciar:** um erro de programação derruba a inicialização, em vez de deixar o mod degradado.
- **Limites explícitos:** varreduras têm raio e intervalo definidos (regra 21).
- **Atomicidade:** uma transferência tira de um lado e põe no outro por completo. Energia e itens nunca ficam pela metade (regra 13).

### Checklist

- [ ] Todo dado externo é validado antes do domínio?
- [ ] O código trata entidade ausente e chunk descarregado?
- [ ] Uma falha no meio de uma transferência não cria nem perde energia ou itens?

## 3. Fronteira de módulo

**A fronteira é a decisão que o resto da arquitetura protege.**

1. `domain/` não importa o jogo (regra 12), e o ArchUnit reprova a violação.
2. As dependências apontam para dentro: as classes do jogo dependem do domínio, nunca o contrário.
3. Código comum nunca referencia `client/` (regra 23).
4. Dependências opcionais ficam isoladas, e o mod abre sem elas (regra 24).
5. Itens compartilhados reconhecem robôs por tag ou interface, nunca por uma classe concreta (regra 42).

### Checklist

- [ ] `domain/` continua sem importar o jogo?
- [ ] Nenhuma regra de negócio ficou em entidade, bloco ou tela?
- [ ] O servidor continua abrindo sem classes de cliente?

## 4. Contrato externo

**Quem consome nem sempre atualiza junto.** O Cubit tem quatro contratos:

| Contrato | Quem consome | Regra |
|---|---|---|
| Saves | Mundos já criados | IDs, data components e NBT mudam só com migração |
| Config | O pack, pelo packwiz | Chaves não mudam sem migração |
| Receitas e tags | KubeJS e outros mods | IDs e tags são estáveis, e as receitas são JSON |
| Energia | Mods de FE do pack | Capabilities padrão do NeoForge |

- O contrato é definido nos documentos antes do código (regra 11).
- Arquivos gerados são versionados e nunca editados à mão (regra 30).

### Checklist

- [ ] O contrato está documentado antes do código?
- [ ] A mudança é compatível ou tem migração?

## 5. Comportamento testado

**O teste define o comportamento esperado antes da implementação.**

- O ciclo é teste falhando, código mínimo e refatoração (regra 32).
- A cobertura mínima de `domain/` é de 85% (regra 34).
- Nenhum método público do domínio fica sem teste de comportamento.
- Comportamento de mundo se testa no mundo, sem mock de `Level` ou `Entity` (regra 33).
- Os testes não dependem de ordem, de relógio real nem de `sleep`.
- A correção de um bug começa pelo teste que o reproduz.

### Casos que sempre precisam de teste

- **Energia:** limites de zero e capacidade, e transferência que não cria nem perde FE.
- **Bateria:** desligar ao zerar e religar com a carga mínima.
- **Estados:** toda transição entre ativo, inativo e hibernação.
- **Robô que segue:** ligar um robô desliga só o que seguia.
- **Item:** recolher e colocar preservam todo o estado.
- **Base:** ordem de chegada, com a base ocupada.

### Checklist

- [ ] O teste veio antes do código?
- [ ] A cobertura está em 85% ou mais?
- [ ] Sucesso, erro e bordas estão cobertos?

## 6. Design sólido

| Princípio | Aplicação |
|---|---|
| Responsabilidade única | Uma classe tem um motivo para mudar. Entidade adapta, e o domínio decide |
| Aberto e fechado | Um novo tipo de robô entra por dados e composição, sem mudar as regras existentes (regras 39 e 40) |
| Substituição de Liskov | Toda implementação de interface respeita o contrato dela |
| Segregação de interfaces | Interfaces são pequenas e definidas por quem consome (regra 15) |
| Inversão de dependência | O domínio define a interface, e o código do jogo a implementa, como `TierCatalog` |

### Checklist

- [ ] Cada classe tem uma responsabilidade clara?
- [ ] O domínio depende só de interfaces próprias?

## 7. Clareza

**Código é lido mais vezes do que é escrito.**

- O nome revela a intenção: `Battery`, não `EnergyManager`. Sem `data`, `info` ou `manager`.
- Funções têm no máximo 30 linhas (regra 27) e complexidade ciclomática de no máximo 10.
- Classes acima de cerca de 400 linhas indicam uma divisão ruim.
- Entidades e blocos adaptam e delegam. Regra de negócio neles é violação.
- Prefira retorno antecipado a `else` aninhado.
- Sem número mágico (regra 26), sem código morto e com comentários só para o porquê (regra 29).

### Checklist

- [ ] Os nomes revelam a intenção?
- [ ] Cada função faz uma coisa só?
- [ ] O código passa nos gates sem supressões injustificadas?

## 8. Simplicidade

**A solução mais simples que atende o requisito atual é a correta.**

- A abstração precisa de um motivo real. As fronteiras das regras 39 a 42 são a exceção decidida, porque mudá-las depois exigiria migração.
- Prefira a biblioteca padrão e a API do NeoForge a uma dependência nova.
- Cache e otimização precisam de um problema medido.

### Checklist

- [ ] É a solução mais simples que resolve corretamente?
- [ ] A complexidade é proporcional ao problema?

## 9. Sem repetição

**Conhecimento tem uma única representação no sistema.**

- A validação fica no value object, e os números ficam no config.
- A regra vale para conhecimento, não para linhas: dois trechos parecidos com motivos diferentes para mudar continuam separados.

### Checklist

- [ ] A validação ficou num lugar só?
- [ ] A repetição encontrada é de conhecimento ou é acidental?

## 10. Sem excesso

**Não implemente o que não é necessário agora.**

- Nada de recurso "por precaução", nem de código de robôs que ainda não existem (regra 43).
- Uma opção de config entra junto com o código que a lê.
- Questão em aberto é bloqueante: pergunte antes de escolher.

### Checklist

- [ ] Todo código é necessário para a tarefa atual?
- [ ] Nenhuma questão em aberto foi resolvida sem consulta?

## 11. Domínio

**Se o domínio aceitou o dado, o dado é válido. Se o dado é inválido, o domínio o rejeitou.**

- **Linguagem única:** os nomes do domínio são os mesmos no código, nos documentos e no [`glossario.md`](../glossario.md).
- **Value objects:** conceitos como energia e tier não são primitivos soltos. São `record` imutáveis que validam no construtor (regra 14).
- **Mudança por comportamento:** o estado muda por métodos que guardam as regras, nunca por setters.
- **Sem dependências ocultas:** o domínio não lê relógio, config nem o jogo. Quem chama fornece os dados.
- **README:** `domain/` documenta a linguagem, as regras e os porquês (regra 16).

### Checklist

- [ ] Todo conceito do domínio usa um value object?
- [ ] O construtor rejeita valores inválidos?
- [ ] O `README.md` do domínio está atualizado?

## 12. Regra do Escoteiro

**Deixe o código melhor do que o encontrou.**

| Situação | O que fazer |
|---|---|
| Um gate reprova em arquivo que você não alterou | Corrigir no mesmo PR |
| Um teste de outra área quebra | Investigar e corrigir; um bug real ganha issue própria |
| Documento desatualizado em arquivo que você leu | Atualizar no mesmo PR |

Isso não autoriza refatorar um arquivo inteiro nem mudar o comportamento de outra funcionalidade. Comportamento novo é uma issue nova.

## Matriz de decisão

| Situação | Quem vence | Decisão |
|---|---|---|
| Confiar na distância informada pelo cliente simplificaria o código | Segurança vence Simplicidade | O servidor calcula a distância |
| Assumir que o robô salvo ainda existe deixaria o método mais curto | Defesa vence Clareza | O código trata a entidade ausente |
| Colocar a regra de bateria na entidade evitaria uma classe | Fronteira vence Simplicidade | A regra fica em `domain/` |
| Renomear uma chave de config deixaria o nome melhor | Contrato vence Clareza | O nome só muda com migração |
| Um teste quebrou em outra área depois da sua mudança | Comportamento testado e Escoteiro | Você corrige antes do PR |

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-04 17:51 | Adota os pilares de desenvolvimento, adaptados ao mod. |
