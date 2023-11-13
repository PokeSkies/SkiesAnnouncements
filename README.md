# SkiesAnnouncements
<img height="50" src="https://camo.githubusercontent.com/a94064bebbf15dfed1fddf70437ea2ac3521ce55ac85650e35137db9de12979d/68747470733a2f2f692e696d6775722e636f6d2f6331444839564c2e706e67" alt="Requires Fabric Kotlin"/>

A Fabric (1.20.1) server-sided automated announcements mod! Send pre-defined announcements in a variety of forms with various requirements to online players.

More information on configuration can be found on the [Wiki](https://github.com/PokeSkies/SkiesAnnouncements/wiki)!

## Features
- Group announcements in files for easy configuring
- Message, title, and action messages all supported
- Multi-line support when possible
- MiniMessage formatting everywhere
- Add sound effects to your announcements
- Group formatting to automatically apply a standard format to messages
- Two different order modes, RANDOM and SEQUENTIAL
- 2 requirement types *(for now)*
- Placeholder support (Impactor and PlaceholderAPI)
- Optionally send certain announcements through Discord Webhooks

## Installation
1. Download the latest version of the mod from [Modrinth](https://modrinth.com/mod/skiesannouncements).
2. Download all required dependencies:
   - [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) 
   - [Fabric Permissions API](https://github.com/PokeSkies/fabric-permissions-api)
3. Download any optional dependencies:
   - [Impactor](https://modrinth.com/mod/impactor) **_(Placeholders)_**
   - [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders) **_(Placeholders)_**
   - [PlaceholderAPI]() **_(Placeholders)_**
4. Install the mod and dependencies into your server's `mods` folder.
5. Configure your Announcement Groups in the `./config/skiesannouncements/groups/` folder.

## Commands/Permissions
| Command                                   | Description                                        | Permission                          |
|-------------------------------------------|----------------------------------------------------|-------------------------------------|
| /announcements reload                     | Reload the Mod                                     | skiesannouncements.command.reload   |
| /announcements debug                      | Toggle the debug mode for more insight into errors | skiesannouncements.command.debug    |
| /announcements list                       | List the available announcement groups             | skiesannouncements.command.list     |
| /announcements announce <group> [id]      | Send an announcement group to all players          | skiesannouncements.command.announce |
| /announcements send <player> <group> [id] | Send an announcement group to specific players     | skiesannouncements.command.send     |


## Planned Features
- Better/more debugging and error handling
- More Requirement Types
    - **Please submit suggestions!**
- In-game GUI editor

## Support
A community support Discord has been opened up for all Skies Development related projects! Feel free to join and ask questions or leave suggestions :)

<a class="discord-widget" href="https://discord.gg/cgBww275Fg" title="Join us on Discord"><img src="https://discordapp.com/api/guilds/1158447623989116980/embed.png?style=banner2"></a>
