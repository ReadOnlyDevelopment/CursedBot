Since the EmbedData values in `config.json` are easily distinguished as "default", or in our case meaning fallbacks. This will display each value as shown in `overrides/list.json`

**Default Values after Generating**
```json
{
  "overrides" : [ {
    "id" : 359766,
    "channel" : "default",
    "role" : "default",
    "fileLink" : "DEFAULT",
    "description" : "default",
    "discordFormat" : "default"
  } ]
}
```
  - As stated, any of these values left as "default" will fallback to whatever values you set in `config.json` or its default value if not changed
  
**What Each Value Changes**
---

-> `channel` is pretty self explanatory. It's the channel the Update Post for this project will be sent too


-> `role` if a valid role ID is provided will ping this role on this particular update

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/role-ping.png)