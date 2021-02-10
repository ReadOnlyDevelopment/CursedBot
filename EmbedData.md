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

-> `"channel"` is pretty self explanatory. It's the channel the Update Post for this project will be sent too


-> `"role"` if a valid role ID is provided will ping this role on this particular update

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/role-ping.png)

---

-> `"fileLink"` This can be 1 of 3 values `default | curseforge | direct`


**if project is a Mod**

Example: `curseforge`

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/filelink-curseforge.png)

Example: `direct`

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/filelink-direct.png)


**if project is a Modpack**

The values stil produce the same result. However with modpacks, if the update contains a `server file` download, this is included
and is always a direct download link regardless of `fileLink` value

Example:

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/modpack-serverfiles.png)

---

-> `"description"` This can override the default embed descrption set in `config.json`

Example: `"description" : "This overrides the default embed description",`

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/desc.png)

---

-> `"discordFormat"` This gives the changelog content discord syntax highlighting

NOTE: The highlighting is only applied if the changelog content is properly formatted for the provided language

Example: `"discordFormat" : "yml"`

![](https://github.com/ReadOnlyDevelopment/CursedBot/blob/master/assets/yml.png)