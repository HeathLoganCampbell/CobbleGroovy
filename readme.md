# CobbleGroovy
Compile and execute programs on the fly with groovy scripts
ideally this will also pull from a git repo but currently 
it listens to all changes plugins/CobbleGroovy/scripts

Pre set up Script Environment:
https://github.com/HeathLoganCampbell/CobbleGroovy-Scripts

## Example of working Groovy Script
```groovy
package scripts.global

import dev.cobblesword.cobblegroovy.tools.CC
import me.lucko.helper.Commands
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.hologram.BaseHologram
import me.lucko.helper.hologram.BukkitHologramFactory
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent

Commands.create().assertPermission("example.command.yeet")
                .assertPlayer()
                 .handler(sender -> {
                     sender.sender().sendMessage(ChatColor.RED.toString() + "Weeee")
                     BaseHologram hologram = new BukkitHologramFactory()
                             .newHologram(Position.of(sender.sender().getLocation()),
                                     [
                                             CC.red + "Hello world",
                                             "I Am Iron Man"
                                     ])
                     hologram.spawn()

                     BossBar bossBar = Bukkit.createBossBar("yeet world", BarColor.WHITE, BarStyle.SOLID, BarFlag.DARKEN_SKY)
                     bossBar.addPlayer(sender.sender())
                 }).registerAndBind(registry, "yeet")


Schedulers.async().runRepeating(() -> {
    Bukkit.broadcastMessage(CC.red + "Example")
}, 20, 20).bindWith(registry)

Events.subscribe(BlockPlaceEvent.class).handler{
    Block block = it.getBlock()
    block.setType(Material.GOLD_BLOCK)
}.bindWith(registry)

Events.subscribe(AsyncPlayerChatEvent.class).handler{
    it.setFormat(CC.red + "%s: %s")
    it.getPlayer().sendMessage("ABCCC")
}.bindWith(registry)

Schedulers.async().runRepeating(() -> {
    for (Player player : Bukkit.getOnlinePlayers())
    {
        player.setPlayerListName(" My Name Jeff ")
        player.setPlayerListHeaderFooter("Header", "fotters")
    }
}, 20, 20).bindWith(registry)
```


## Commands
```groovy
Commands.create().handler { command -> 
    command.sender().sendMessage("Test")
}.register("Example")
```

## Events
```groovy 
Events.subscribe(InventoryCloseEvent.class).handler { event ->
    def player = event.getPlayer()
    player.sendMessage("Hello world")
}
```


## Database
```groovy
Database.executeQuery("SELECT * FROM table WHERE id = ?", 
    { statement -> statement.setInt(1, 1) }, 
    { result -> })

Database.execute("DELETE FROM table", { statement -> })
```

## CustomItems

```groovy
package scripts.global

import dev.cobblesword.cobblegroovy.tools.CC
import dev.cobblesword.cobblegroovy.tools.item.CustomItem
import dev.cobblesword.cobblegroovy.tools.item.Items
import me.lucko.helper.Commands
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

// create new item, cheese that when eld leaves a trail of sponges
CustomItem cheese = Items.create("CHEESE", Material.SPONGE)
        .displayName(CC.bYellow + "Cheese")
        .subscribe(PlayerMoveEvent.class, (e, item, customItem) -> {
            Player player = e.getPlayer();
            Location location = player.getLocation();
            Block block = location.getBlock();
            Block standingBlock = block.getRelative(BlockFace.DOWN);

            if (standingBlock.getType() != Material.AIR) {
                standingBlock.setType(Material.SPONGE);
            }
        })
cheese.bindWith(registry)

// Give new custom item
Commands.create().assertPlayer()
        .handler{cmd ->
            var sender = cmd.sender()
            sender.getInventory().addItem(cheese.getItemStack())
        }.registerAndBind(registry, "cheese")
```

For more info on the api, look at lucko's helper repository's wiki
