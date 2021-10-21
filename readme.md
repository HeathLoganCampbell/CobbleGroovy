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
CustomItems.register(Material.STONE, "ORB_OF_EXAMPLE")
           .displayName("Orb of Example")
           .lore("Slay girllll", "Rule the world")
           .withNBT("Yeet")
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

## Runnable
```groovy
Sync.create().delay(5).interval(10).run {
    
}.run();
```

```groovy
Async.create().delay(5).interval(10).run {
    
}.run();
```

## Menu
```groovy
def menu = Menu.create(27, 'Hello world')
menu.set(0, new ItemBuilder(Material.STONE).setDisplayName("Example").build(), { player -> player.sendMessage("Yeet") })
menu.set(1, new ItemBuilder(Material.STONE).setDisplayName("Example").build())
menu.open(player)
```

## Holograms
```groovy
Holograms.create(location)
         .addLine("Hello world")
         .spawn()
```

## Database
```groovy
Database.executeQuery("SELECT * FROM table WHERE id = ?", 
    { statement -> statement.setInt(1, 1) }, 
    { result -> })

Database.execute("DELETE FROM table", { statement -> })
```

## CustomItems