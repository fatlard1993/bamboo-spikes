# Bamboo Spikes

A Fabric mod that adds bamboo spikes - a trap block crafted from bamboo that damages anything that touches it.

## Features

- **Bamboo Spikes Block**: A trap that deals cactus damage (2 points, one heart) to players and mobs that touch it. Villagers, iron, snow and copper golems, armor stands and mannequins are left alone: the game files them under its miscellaneous category with boats and item frames, which the spikes skip
- **Increased Fall Damage**: Falling onto spikes deals 5x normal fall damage
- **Directional Placement**: Can be placed on any surface (floor, ceiling, walls)
- **No Collision**: Entities walk through the spikes while taking damage
- **Waterloggable**: Can be placed underwater
- **Craftable**: Five bamboo in an X (corners and center) make one
- **Axe-Mineable**: Breaks fastest with an axe

## Screenshots

![Bamboo Spikes](img.png)
![Bamboo Spikes on Bamboo](img2.png)

## Pandorical

Bamboo Spikes uses Pandorical to register its block and item models and sync them (and the mod's other assets) to clients for correct rendering. Pandorical is required on both the server and every client; there is no vanilla-client fallback.

## Development

Installing is in [DEVELOPMENT.md](DEVELOPMENT.md).

## License

MIT, see [LICENSE](LICENSE).
