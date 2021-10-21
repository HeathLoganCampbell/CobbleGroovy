# CobbleGroovy

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