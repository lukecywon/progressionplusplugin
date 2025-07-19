# <p style="text-align:center;">Progression Plus</p>

A comprehensive Minecraft Paper plugin designed to revolutionize gameplay progression by implementing strategic gating systems, custom items, and enhanced mechanics for Minecraft 1.21.7.

## 🎯 Overview

ProgressionPlus transforms the traditional Minecraft experience by slowing down progression through carefully designed mechanics:

- **Dimensional Gating**: Lock access to the Nether and End behind specific items and time gates
- **Recipe Progression**: Diamond crafting locked until major milestones are achieved
- **Custom Item System**: 50+ unique items with special abilities across multiple rarity tiers
- **Enhanced Recipes**: Modified Eye of Ender and other key recipes for increased challenge

## ✨ Core Features

### 🔒 Progression Gating System

**Nether Access Control**
- Requires the **Nether Eye** item to unlock Nether access
- Time-based unlocking system (configurable)
- Portal creation and travel are blocked until requirements are met

**Diamond Recipe Locking**
- All diamond tool and armor recipes are locked by default
- Unlocked only after defeating the Ender Dragon
- Automatic recipe discovery for all online players upon unlock

**End Access Management**
- Time-based access control to the End dimension
- Configurable unlock times and conditions
- Prevents premature progression to end-game content

### 🗡️ Custom Items System

**Rarity Tiers**
- **Common**: Basic utility items and tools
- **Uncommon**: Enhanced tools with minor abilities  
- **Rare**: Specialized weapons with unique mechanics
- **Epic**: Powerful items with significant abilities
- **Legendary**: End-game items with transformative powers
- **Component**: Crafting materials for advanced items
- **Progression**: Key items for unlocking game phases

**Featured Legendary Weapons**
- **Void Reaper**: Teleport-slash attacks with soul collection mechanics
- **Fames Auri**: Multi-tier legendary sword with evolving abilities
- **Echo Gun**: Ranged weapon with special projectile mechanics
- **Gravity Maul**: Hammer with gravitational effects

**Progression Items**
- **Nether Eye**: Unlocks Nether access (crafted with rare components)
- **Refined Eye**: Enhanced Eye of Ender with harder recipe
- **Custom End Crystal**: Modified end crystal with custom properties
- **Custom Ender Chest**: Enhanced ender chest functionality

**Component Materials**
Essential crafting ingredients for high-tier items:
- **Tide Crystal**: Ocean-forged prismarine essence
- **Warden's Heart**: Pulsating remnant from the Deep Dark
- **Echo Core**: Resonant core material
- **Twisted Root**: Corrupted plant matter
- **Sunscorched Ember**: Intense fire-forged material
- **Enderite Ingot**: Advanced alloy material
- **Infernal Shard**: Hellish crystalline fragment
- **Steel Ingot**: Refined metallic component
- **Aether Core**: Celestial essence material

### 🔧 Game Mechanics

**Recipe Management**
- Dynamic recipe addition/removal system
- Advancement-based recipe unlocking
- Custom shaped recipes for all custom items

**Time-Based Systems**
- Configurable unlock times for dimensions (ISO 8601 format)
- Real-time progression tracking with server timezone support
- Default timezone: Asia/Kuala_Lumpur (configurable)
- Use `/endtime` command to check current status and unlock times

**Item Abilities**
- Cooldown-based special abilities
- Right-click and left-click activation modes
- Visual and audio feedback systems
- Particle effects for enhanced gameplay

## 🚀 Installation

### Requirements
- Minecraft Server 1.21.7
- Paper server software
- Java 21 or higher

### Setup Steps

1. **Download the Plugin**
   ```bash
   # Clone the repository
   git clone https://github.com/lukecywon/ProgressionPlus.git
   cd ProgressionPlus
   ```

2. **Build the Plugin**
   ```bash
   # Make gradlew executable and build
   chmod +x gradlew
   ./gradlew build
   ```

3. **Install on Server**
   ```bash
   # Copy the generated JAR to your server's plugins folder
   cp build/libs/ProgressionPlus-1.0.jar /path/to/your/server/plugins/
   ```

4. **Start Your Server**
   - The plugin will generate default configuration files on first run
   - Configure settings in `plugins/ProgressionPlus/config.yml`

## ⚙️ Configuration

### Default Configuration (`config.yml`)
```yaml
# Dimension unlock status
nether_unlocked: false
diamond_unlocked: false
nether_opened: false

# Time-based unlocks (ISO 8601 format)
nether_unlock_time: "2025-07-06T20:00"
end_unlock_time: "2025-07-10T20:00"
```

### Key Configuration Options

- `nether_unlocked`: Whether Nether access is enabled
- `diamond_unlocked`: Whether diamond recipes are available
- `nether_unlock_time`: Scheduled time for Nether unlock
- `end_unlock_time`: Scheduled time for End access

## 🎮 Commands

### Player Commands
```
/encyclopedia          - Get the Item Encyclopedia
/endtime              - View current time and End unlock time
/trade <accept|reject> - Handle trade requests
/wormhole <accept|reject> - Handle wormhole teleport requests
```

### Admin Commands
```
/artifact <subcommand>     - Main command for artifact items
/config du <true|false>    - Toggle diamond unlocked status
/cooldown clear <player>   - Clear item cooldowns for a player
/addsouls [amount]         - Add souls to Void Reaper
/fixme                     - Fix player invulnerability issues
```

### Permission Nodes
- `progressionplus.artifact` - Access to admin commands
- Commands without permissions are available to all players

## 🎒 Custom Items Guide

### How to Get Custom Items
1. **Crafting**: Most items have custom recipes (use `/encyclopedia`)
2. **Loot Tables**: Some items are found in specific loot chests
3. **Admin Commands**: Use `/artifact` commands for testing

### Item Mechanics
- **Enchantability**: Some items can be enchanted, others cannot
- **Stackability**: Most custom items are non-stackable
- **Durability**: Custom weapons have modified durability systems
- **Abilities**: Right-click or left-click to activate special abilities

### Notable Item Recipes

**Nether Eye** (Unlocks Nether)
```
[Ender Pearl] [Tide Crystal]    [Ender Pearl]
[Echo Core]   [Warden's Heart]  [Twisted Root]
[Ender Pearl] [Sunscorched Ember] [Ender Pearl]
```

**Refined Eye** (Enhanced Eye of Ender)
```
[Ender Pearl]    [Netherite Scrap] [Ender Pearl]
[Blaze Powder]   [Nether Star]     [Blaze Powder]
[Ender Pearl]    [Netherite Scrap] [Ender Pearl]
```

## 🛠️ Development

### Building from Source
```bash
# Clone and build
git clone https://github.com/lukecywon/ProgressionPlus.git
cd ProgressionPlus
./gradlew build
```

### Project Structure
```
src/main/kotlin/com/lukecywon/progressionPlus/
├── ProgressionPlus.kt         # Main plugin class
├── commands/                  # Command implementations
├── events/                    # Custom event definitions
├── gui/                       # GUI implementations
├── items/                     # Custom item definitions
│   ├── armor/                 # Custom armor pieces
│   ├── progression/           # Progression-gating items
│   ├── utility/               # Utility items
│   └── weapons/               # Custom weapons by rarity
├── listeners/                 # Event listeners
├── mechanics/                 # Core game mechanics
├── recipes/                   # Recipe management
├── setup/                     # Plugin initialization
└── utils/                     # Utility classes
```

### Technologies Used
- **Kotlin**: Primary programming language
- **Paper API**: Minecraft server API
- **Gradle**: Build system with Kotlin DSL
- **Adventure API**: Modern text and chat components
- **Triumph GUI**: Enhanced inventory GUI framework
- **Reflections**: Runtime classpath scanning

### Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📋 Plugin Architecture

### Core Systems

**Manager Pattern**
- `DiamondRecipeManager`: Handles diamond recipe locking/unlocking
- `DayNightManager`: Manages day/night cycle events
- `LegendaryManager`: Coordinates legendary item mechanics
- `TeleportRequestManager`: Handles teleportation requests

**Event-Driven Architecture**
- Comprehensive listener system for all game interactions
- Separate listeners for different item categories
- Mechanics-specific listeners for progression control

**Annotation-Based Setup**
- `@RunOnEnable` annotation for automatic initialization
- Reflection-based component discovery
- Priority-based loading system

## 🐛 Troubleshooting

### Common Issues

**Build Fails with "Permission Denied"**
```bash
chmod +x gradlew
./gradlew build
```

**Items Not Working**
- Ensure server is running Paper (not Spigot/Bukkit)
- Check console for any error messages
- Verify Java version compatibility (21+)

**Recipe Issues**
- Use `/encyclopedia` to view correct recipes
- Check if progression requirements are met
- Verify all required components are available

**Time-Based Unlocks Not Working**
- Check server timezone configuration (default: Asia/Kuala_Lumpur)
- Verify time format in config.yml (ISO 8601: YYYY-MM-DDTHH:MM)
- Use `/endtime` to check current time vs unlock time
- Ensure system clock is synchronized

## 📄 License

This project's license terms should be determined by the project owner. Please contact the repository owner for licensing information.

## 🙏 Credits

**Plugin Development**
- **lukecywon** - Developer
- **ANARCHY2319** - Developer
- **lukecywon** - Developer

**Dependencies**
- [Paper](https://papermc.io/) - Modern Minecraft server software
- [Adventure](https://docs.adventure.kyori.net/) - Text and chat components
- [Triumph GUI](https://github.com/TriumphTeam/triumph-gui) - Enhanced inventory GUIs

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/lukecywon/ProgressionPlus/issues)
- **Discussions**: [GitHub Discussions](https://github.com/lukecywon/ProgressionPlus/discussions)

---

*Experience Minecraft like never before with carefully crafted progression systems and unique custom items. Every milestone earned, every dimension unlocked.*
test
