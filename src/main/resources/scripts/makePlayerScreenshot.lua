local JSON = require("JSON")
photos = {}
net.screenshot_request(%victimId%)
net.screenshot_request(%killerId%)
temp=net.get_player_info(%victimId%,"screens")
photos["victim"] = temp[table.maxn(temp)]
temp=net.get_player_info(%killerId%,"screens")
photos["killer"] = temp[table.maxn(temp)]
return JSON:encode(photos)