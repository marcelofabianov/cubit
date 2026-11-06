# Controle remoto e modo área

Números: [`tiers.md`](../tiers.md#autonomia) e [valores gerais](../tiers.md#valores-gerais).

## Comandos

O controle atende todos os Cubits do player, desde que estejam ligados. Mirando no robô e clicando com o botão direito, dá para:

- ligar e desligar;
- mandar parar e voltar a seguir;
- trocar o modo de defesa;
- ativar ou desativar tochas, fornalha e carregador de ferramentas.

O controle é o mesmo em todos os tiers. As opções bloqueadas aparecem desabilitadas.

Parado pelo controle, o Cubit continua ativo e com o vínculo, não anda nem teleporta, e esse estado é salvo. Voltar a seguir retoma. Parado, ele não gasta energia.

Desligado pelo controle, o Cubit segue as regras do estado inativo, mesmo com bateria, e só volta a ficar ativo pelo controle. Ligar é a única ação do controle sobre um Cubit desligado.

## Localizar

O controle lista todos os Cubits do player, sem limite de distância, com nome, coordenadas, dimensão e estado. Em chunk descarregado, mostra a última posição conhecida, inclusive dos desligados.

## Modo área

A área é marcada com o controle, clicando com o botão direito em dois blocos, os cantos de um volume em 3D. Os limites de [`tiers.md`](../tiers.md#autonomia), como 16×16, valem para a base, e a altura máxima fica no config.

- **Sem card:** o Cubit coleta os itens do chão dentro da área, respeitando o filtro de coleta, e descarrega no baú marcado.
- **Com trabalho, na Fase 5:** vale [`trabalhos.md`](trabalhos.md).
- **Ao recolher:** a área, os baús marcados e a seleção no controle se perdem.

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-06 11:10 | Troca 'especialidade' por 'trabalho' e 'card de especialidade' por 'card de Aprendizado de Máquina'. |
| Marcelo Fabiano | 2026-11-05 21:34 | Define o parar, a área em 3D e o que o Cubit sem card faz nela. |
| Marcelo Fabiano | 2026-11-05 00:12 | Usa o termo modo de defesa. |
| Marcelo Fabiano | 2026-11-04 17:28 | Define o alcance do controle e da função de localizar. |
| Marcelo Fabiano | 2026-11-04 17:15 | Renomeia para português e atualiza os caminhos. |
| Marcelo Fabiano | 2026-11-04 17:11 | Adiciona a função de localizar e o alcance do controle. |
| Marcelo Fabiano | 2026-11-04 17:08 | Define as funções que o controle remoto desativa. |
| Marcelo Fabiano | 2026-11-04 16:57 | Adiciona o comportamento ao desligar pelo controle. |
| Marcelo Fabiano | 2026-11-04 15:23 | Descreve as features do Cubit. |
