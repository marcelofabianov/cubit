# Descrição das plataformas

Os textos do Cubit para a lista de mods do jogo e para as páginas do Modrinth e do CurseForge (perguntas 745 e 758 a 760). Os blocos em inglês estão prontos para copiar, e o [guia de publicação](publicacao.md) diz onde cada um entra. A cada versão, revise os textos junto do [`CHANGELOG.md`](../../CHANGELOG.md).

## Descrição curta

Aparece na lista de mods do jogo. O NeoForge lê primeiro a chave `fml.menu.mods.info.description.cubit` do arquivo de idioma e, sem ela, usa o `description` do `neoforge.mods.toml` (pergunta 759). Por isso o texto em inglês fica igual nos dois lugares, e o português só existe no `pt_br.json`. Os dois são gerados pelo datagen, nas classes `EnglishGameTexts` e `PortugueseGameTexts`.

Inglês, no `neoforge.mods.toml` e no `en_us.json`:

```text
Cubit is a cute battery-powered robot companion that follows you, carries your items and lights the way. It scans for ores and hostile mobs, talks with beeps and short messages, and recharges at the Induction Dock with FE from any mod, with redstone or with its solar panel. Pick it back up as an item whenever you want, with everything inside it kept safe.
```

Português, no `pt_br.json`:

```text
Cubit é um robô companheiro fofo, movido a bateria, que segue você, carrega seus itens e ilumina o caminho. Ele procura minérios e mobs hostis, conversa com bipes e textos curtos e recarrega na Base de Indução com FE de qualquer mod, com redstone ou com o painel solar. Recolha-o como item quando quiser, com tudo o que ele guarda a salvo.
```

## Resumo

Uma frase, sem repetir o nome, no campo de resumo das duas plataformas:

```text
A cute battery-powered robot companion that follows you, carries your items and scans for ores and mobs.
```

## Descrição longa

Em Markdown, no campo de descrição das duas plataformas. Segue as regras do Modrinth, que pedem dizer o que o mod adiciona, por que baixá-lo e o que for crítico, sem usar títulos como texto. Termina com uma linha curta sobre o que vem depois, sem datas (pergunta 760).

```markdown
Cubit is a cute, battery-powered robot companion. It follows you around, carries your items, lights the way in the dark and warns you about ores and hostile mobs nearby. It is crafted from vanilla materials and works on its own, with no other content mod required.

## What Cubit does

- **Follows you:** walks behind you, climbs one block at a time, walks down drops of up to 5 blocks and teleports back when it falls too far behind.
- **Carries your items:** 27 slots, in its own screen that only you can open.
- **Runs on a battery:** walking, scanning and the lamp use energy, and it shuts down when the battery runs out. Recharge it with redstone, with its solar panel under open sky, or at the Induction Dock, a charging base powered by redstone or by FE cables from any mod.
- **Lights the way:** its lamp turns on in dark places, through LambDynamicLights, with no invisible light blocks.
- **Scans around you:** finds ores from any mod through the `c:ores` tag and spots hostile mobs, even behind walls. New ores get a box only you can see, and mobs get an arrow above their heads.
- **Has a personality:** a face screen with expressions, animations, beeps, short messages above the hotbar and chatter about the weather, biomes and your advancements, with a toggle to turn it off.
- **Keeps your things safe:** pick it back up as an item at any time, with its inventory, battery and settings inside. When it dies, it drops a Damaged Cubit that keeps everything and is repaired with a Toolbox.
- **Avoids danger:** runs to water when on fire and gets out of lava and harmful blocks. You can own as many Cubits as you want, and one follows you at a time.

## Good to know

- **Tier:** this version has the Base tier only.
- **Requirements:** NeoForge for Minecraft 1.21.1, GeckoLib and, on the client, LambDynamicLights. Jade is optional and adds tooltips.
- **Server:** install Cubit on both the server and the client.
- **Config:** every balance number is in `config/cubit-common.toml`.
- **Recipes:** plain datapack JSON with vanilla materials, so modpacks can change them.
- **Languages:** English and Brazilian Portuguese.
- **Bugs and ideas:** open an issue on [GitHub](https://github.com/marcelofabianov/cubit/issues).

Coming next: upgrade tiers, a Power Bank, an item magnet, a defense mode and a remote control.
```

## Histórico de modificações

| Pessoa | Data e hora | Modificação |
|---|---|---|
| Marcelo Fabiano | 2026-11-08 00:58 | Cria a descrição curta, o resumo e a descrição longa das plataformas. |
