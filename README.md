# CursedBot
Initally a fork of [ErdbeerbaerLP's Curseforge-Bot](https://github.com/ErdbeerbaerLP/Curseforge-Bot) but eneded up as a complete re-write while keeping the same message style as Curseforge-Bot. 

A major change from the original is the Update thread is now ran as a Asyncronous ScheduledExecutor task with each thread start delayed 2 seconds

**Preface**
---
  - Definition: EmbedData
    - EmbedData is a collection of values that determine certain values in the Update Post
      - Posting Channel ID
        - Must be changed from its default value in the config.json AT THE LEAST
        - The channel ID where the update embed will be sent
      - Mention Role ID
        - [Valid Values] *none | role ID* 
        - if an ID is provided each update Post will ping this role
      - FileLink
        - [Valid Values] *default | direct | curseforge*
        - **NOTE**: Changing this value can be done without regarding Case Sensitivity. But will be set .ToUpperCase() on next run
      - Discord Syntax
        - Will give the Update Post changelog section a syntax coloring
      - Embed Description
        - A short sentence right under the Update Post title
        
**To See what each EmbedData value changes please check out [EmbedData Values](https://github.com/ReadOnlyDevelopment/CursedBot/edit/master/EmbedData.md)**

  - IMPORTANT NOTE
    - For now changing any values in `config.json` or `overrides/list.json` will require a restart.

**Changes**
---
  - Bot Configuration File
    - Is now a json file
    - Name: `config.json`
      - defining Projects is now by Project ID only
        - The following values are now strickly fallback values
          - Posting Channel
          - Mention Role
          - Discord Syntax
          - Embed Description
  - Update Threads
    - Are now ran as Asyncronous ScheduledExecutor tasks
    - Each Thread is delayed from starting by 2 seconds to ensure continuity
  - Cache File
    - is now a json file
    - Name: `cache/cache.json`
     - On inital run is generated with empty array. Creating a `cacheLock` file (Don't Delete This File)
    
**Whats New?**
---
  - Per Project Customizble EmbedData json
  - name: `overrides/list.json`
    - Each project defined in `config.json` will have section in this file. Where EmbedData can be changed.
      - All values are by default set to "default" which falls back to the values defined in `config.json`
        - prior to JDA starting checks are ran to ensure that any role/channel ID is valid (numerical and 17-20 chars)
          - if value is "default" then this is ignored and the check falls back on the supplied value in `config.json`
        - after JDA is connected more checks are ran to check that:
          - any provided Channel ID is an actual TextChannel and that the bot can see
          - any provided Role ID is an actual Role
        - If any checks fail the bot will shutdown and provide console output detailing what failed and what to fix

**So How It Works**
---
 - On Inital Run a default `config.json` file is generated, then the bot is shutdown to allow you to:
   - Required Changes
     - Set projects to check for
     - Set a valid Posting Channel ID
   - Optional Changes
     - Set a Discord Syntax value
     - Set a Embed Description
     - Set if a direct download link, URL link to CurseForge page, or no link will be included in the Update Post
---
__Default Configuation___
```json
{
  "token" : "EXAMPLE-TOKENTUzNDEwMDQ4.W_e4Eg.EpqOhrNmry3gmCPmjVFai5hFlIM",
  "debug" : false,
  "defaultChannel" : "SET ID",
  "defulatRole" : "none",
  "updateFileLink" : "default",
  "defaultDescription" : "New File Detected For CurseForge Project",
  "discordFormat" : "css",
  "projects" : [
    123456,
    987654
  ]
}
```
__Updated Configuation___
```json
{
  "token" : "EXAMPLE-TOKENTUzNDEwMDQ4.W_e4Eg.EpqOhrNmry3gmCPmjVFai5hFlIM",
  "debug" : false,
  "defaultChannel" : "808852131594502164",
  "defulatRole" : "695532548419354720",
  "updateFileLink" : "default",
  "defaultDescription" : "New File Detected For CurseForge Project",
  "discordFormat" : "css",
  "projects" : [
    395397,
    359766,
    358968
  ]
}
```
 - After the required and/or optional changes to `config.json` files, on second run, as long as project ID's are valid:
   - The projects customizble EmbedData json file `overrides/list.json` is generated
     - If any changes are wanted to these a restart after changing is required
   - The inital latest-file id is cached for each project
