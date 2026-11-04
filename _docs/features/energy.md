# Energia

Números por tier: [`tiers.md`](../tiers.md#energia).

## Bateria

- Uma bateria interna alimenta o sistema.
- Quando acaba, o robô desliga, e o player só pode recolhê-lo para o inventário.
- É um recurso a gerenciar: o robô precisa voltar para carregar.

## Carregador solar

Recarrega a bateria com a luz do sol. Só o tier Ultimate também recarrega à noite.

## Power Bank

- Bateria portátil que o player carrega no próprio inventário.
- Quando colocada no slot exclusivo do robô, ele extrai a energia dela para a bateria interna.
- Existe uma Power Bank para cada tier. A capacidade de cada uma equivale a uma porcentagem da bateria interna do tier correspondente.
- As regras ainda em aberto estão em [`decisions.md`](../decisions.md#power-bank).

## Estação de carga

- O bloco é o Induction Dock, descrito em [`items.md`](../items.md).
- Recarrega o robô e serve de base para o retorno automático.
- Usa **FE**, o padrão do NeoForge, e aceita energia de qualquer gerador do pack (Mekanism, Powah, Solar Flux e outros).

## Retorno automático

Se o robô estiver dentro do raio da estação e com a bateria baixa, volta sozinho para ela.
