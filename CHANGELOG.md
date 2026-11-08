# Changelog

All notable changes to Cubit are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-11-08

The first release of Cubit, a cute battery-powered robot companion for Minecraft 1.21.1 on NeoForge. It ships with the Base tier only.

### Added

- **Cubit:** crafted from vanilla materials, placed from the item and picked back up with Shift + right click. The item keeps the tier, energy, health, inventory, color, name and settings, and picking it up, dying or repairing never deletes or duplicates anything.
- **Following:** walks to its owner and stops 3 blocks away, teleports back from about 20 blocks in the same dimension, climbs one block at a time, walks down drops of up to 5 blocks, avoids water and lava, and returns with an owner who respawns far away.
- **Many Cubits:** a player can own as many as they want, and only one follows at a time. Turning one on makes it follow and turns off the previous one.
- **`/cubit reset`:** clears your own follow link, without operator permission.
- **Dangers:** runs to water when on fire, tries to leave lava and gets off blocks that hurt, such as magma, cactus and campfires, including blocks from other mods. Damage caused by the owner never hurts it, except fire from flint and steel.
- **Death and repair:** a dead Cubit drops a Damaged Cubit that keeps the inventory, cannot burn or despawn, is rescued from the void and is repaired with a Toolbox.
- **Inventory:** 27 slots and an energy slot, in its own screen that only the owner opens.
- **Battery:** 300k FE, with drain per situation (walking, full inventory, water, climbing and danger), shutdown at zero and restart at 5%. It recharges with redstone in the energy slot or with the solar panel under open sky.
- **Induction Dock:** a 2-block-tall charging base powered by FE cables from any mod or by redstone. A Cubit hibernates there with its inventory open, recharges, heals, and waits in a queue when the Dock is busy. Clicking the Dock calls the nearest Cubit that is on.
- **Personality:** sounds for its tracks, power, charging and alerts, a face screen with ten expressions, animations, short messages above the hotbar, and chatter about the weather, biomes and your advancements, with a toggle to turn it off.
- **Looks:** an original model with a tier collar, a battery indicator on its back, a shell that changes color with any of the 16 dyes, and a custom name from a name tag or an anvil.
- **Lamp:** dynamic light through LambDynamicLights in dark places, with no invisible light blocks, and a toggle to turn it off.
- **Scanners:** an ore scanner based on the `c:ores` tag, so it finds ores from any mod, and a hostile mob scanner. They show a box around new ores, an arrow over hostile mobs and colored antennas, with a filter for one ore and a toggle to turn them off.
- **Jade:** optional tooltips for the Cubit and the Induction Dock.
- **Screens:** custom screens for the Cubit and the Induction Dock, with tabs, bars and tooltips.
- **Languages:** English and Brazilian Portuguese.
- **Config:** every balance number lives in `config/cubit-common.toml`, and an invalid value never crashes the game.
- **Recipes:** plain datapack JSON with vanilla materials only, so modpacks can override them with KubeJS.

[0.1.0]: https://github.com/marcelofabianov/cubit/releases/tag/v0.1.0
