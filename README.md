# UnanimousSleep Plugin
## Overview
UnanimousSleep is a plugin written to override Minecraft's built-in `playerSleepingPercentage` gamerule, to instead skip the night if just 1 person is sleeping, as long as no online players cancel the sleep.

## Notes
Sleeping is PER WORLD, and as such, so is this plugin. If your server has multiple worlds where players are able to sleep, this will operate independently in those worlds.
When any player enters a bed, all online players in that world will be notified via Action Bar, and will have 5 seconds to run the `/cancelsleep` command. If the command is not run, night will skip. The player who entered the bed can also cancel the sleeping by simply exiting the bed.

## Permissions
There is one permission node for players that allows players to cancel sleep.
| Permission | Description |
| ---------- | ----------- |
| `unanimoussleep.cancelsleep` | Allows players to cancel sleep via the `/cancelsleep` command. |

## Installation
1. Clone this repo
2. run `mvn clean package`
4. move the `unanimoussleep.jar` file to your server's `plugins` folder
5. Restart your server

## Contributors
- [Stuffy](github.com/stuffyerface)
