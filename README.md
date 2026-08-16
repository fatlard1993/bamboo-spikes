# Bamboo Spikes

A Fabric mod that adds bamboo spikes - a trap block crafted from bamboo that damages anything that touches it.

## Features

- **Bamboo Spikes Block**: A trap that damages any living entity (including players) that touches it
- **Increased Fall Damage**: Falling onto spikes deals 5x normal fall damage
- **Directional Placement**: Can be placed on any surface (floor, ceiling, walls)
- **No Collision**: Entities walk through the spikes while taking damage
- **Waterloggable**: Can be placed underwater
- **Craftable**: Made from bamboo

## Screenshots

![Bamboo Spikes](img.png)
![Bamboo Spikes on Bamboo](img2.png)

## Pandorical

Bamboo Spikes uses Pandorical to register its block and item models and sync them (and the mod's other assets) to clients for correct rendering. Pandorical is declared as a hard dependency in `fabric.mod.json`, so it must be installed on both server and client for this mod to load at all: there is no vanilla-client fallback.

## Installation

Install server-side alongside its declared dependencies (see `fabric.mod.json`); connecting clients need only Pandorical. Version targets live in `gradle.properties` (Minecraft, loader, Fabric API) and `fabric.mod.json` (Java).

## License

MIT, see [LICENSE](LICENSE).
