# Cubit

A cute, battery-powered robot companion for Minecraft 1.21.1 on NeoForge, built for the [Iron Meridian](https://github.com/marcelofabianov/iron-meridian) modpack. No magic: Cubit is pure technology, crafted from vanilla resources.

> **Status: design stage.** There is no code or release yet. All features and numbers below are planned, and the numbers are starting points for balancing.

## What Cubit does

Cubit follows you around and helps with simple tasks:

- **Carries items** in its own inventory and picks up nearby drops, with a pickup filter.
- **Runs on a battery** that you have to manage. When it runs out, Cubit shuts down and can only be picked up.
- **Recharges** at the Induction Dock, which accepts FE from any mod, through its solar charger, or from a portable Power Bank placed in its dedicated slot.
- **Turns into an item** that keeps its battery, inventory, name, color and tier.
- **Lights the way** and places torches on its own.
- **Warns you** about hostile mobs and scans for the ores you choose.
- **Defends itself** with a light electric shock, in a peaceful, retaliating or aggressive mode.
- **Works on its own**: it can stay in a marked area, unload into marked chests, smelt ores, charge your electric tools and guide you to where you died.
- **Has personality**: its screen face changes expression, and it makes track and alert sounds.

Each player can have only one active Cubit.

## Look

A rounded white body with a screen face and glowing eyes, two antennas, clawed arms and tracks instead of feet. It has a solar panel on top, a battery indicator on the back and an induction coil underneath. The band at its neck shows its tier, and the body color can be changed with dyes.

![Cubit concept sheet](_docs/assets/concept/robo-conceito-prancha.png)

## Tiers

Cubit keeps its features as it evolves, and each tier improves them. Advanced features stay locked until the right tier.

| | Base | Advanced | Elite | Ultimate |
|---|---|---|---|---|
| Band color | Gray | Blue | Purple | Gold |
| Health | 25 | 50 | 100 | 200 |
| Speed | 1.0× | 1.25× | 1.5× | 2.0× |
| Battery | 300k FE | 600k FE | 3M FE | 8M FE |
| Runtime while moving | ~25 min | ~42 min | ~2.8 h | ~5.6 h |
| Power Bank capacity | 210k FE | 420k FE | 2.1M FE | 5.6M FE |
| Inventory | 27 slots | 36 slots | 45 slots | 54 slots |
| Magnet radius | 1 block | 3 blocks | 5 blocks | 8 blocks |
| Scanner radius | 5 blocks | 10 blocks | 16 blocks | 24 blocks |
| Defense modes | Peaceful | + Retaliate | + Aggressive | All |
| Area mode | Locked | Up to 16×16 | Up to 32×32 | Up to 64×64 |

## Items and blocks

| Name | ID | Purpose |
|---|---|---|
| Cubit | `cubit:robot` | The companion, as a placeable item |
| Cubit Remote | `cubit:remote` | Sends commands and marks work areas |
| Advanced Module | `cubit:advanced_module` | Upgrades Base to Advanced |
| Elite Module | `cubit:elite_module` | Upgrades Advanced to Elite |
| Ultimate Module | `cubit:ultimate_module` | Upgrades Elite to Ultimate |
| Induction Dock | `cubit:induction_dock` | Recharges Cubit using FE |
| Base Power Bank | `cubit:base_power_bank` | Portable battery for the Base tier |
| Advanced Power Bank | `cubit:advanced_power_bank` | Portable battery for the Advanced tier |
| Elite Power Bank | `cubit:elite_power_bank` | Portable battery for the Elite tier |
| Ultimate Power Bank | `cubit:ultimate_power_bank` | Portable battery for the Ultimate tier |

Recipes will use vanilla items only and ship as datapack JSON, so modpacks can override them.

## Roadmap

1. **Concept (current):** persona, features and other definitions, written as documents.
2. **Minimum version:** crafting, following the player, inventory, battery, Induction Dock and turning into an item.
3. **Senses:** flashlight, enemy and ore sounds, and screen expressions.
4. **Upgrades and autonomy:** tiers, magnet, filters, dedicated slots, shock, defense modes, area mode, chest unloading, automatic return and death location.

## Tech stack

- Minecraft 1.21.1, NeoForge 21.1.x and Java 21
- [GeckoLib](https://modrinth.com/mod/geckolib) for the model and animations
- [LambDynamicLights](https://modrinth.com/mod/lambdynamiclights) as an optional dependency for dynamic light
- Forge Energy (FE) through NeoForge capabilities

## Documentation

All project documents are in Portuguese:

- [`robo-companheiro.md`](robo-companheiro.md): overview, phases and a map of the documents
- [`_docs/`](_docs/): persona, visual, features, items, tiers, decisions and glossary
- [`AGENTS.md`](AGENTS.md): project rules and context for developers and coding agents

## License

Not defined yet.
