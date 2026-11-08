# Changelog

## 1.2

The new crafts, the spear head, and recharging use **Roleplay Materials**.

### Circular Saw and Chainsaw Sword
Both tools now run on a charge. The blade texture stays still until the tool is switched on, then it animates. A blue bar above the durability bar shows the charge. A newly crafted tool starts full.

- Charge capacity is **150**.
- While the tool is running it spends **1.5 charge per second**.
- **Circular Saw** — hold right-click to spin. Let go, or run out of charge, and it stops.
- **Chainsaw Sword** — press right-click to turn the blade on or off. Off, it deals **4** attack damage. On, it deals **8**, and it keeps spending charge while you carry it.

Recharge with a **battery** from Roleplay Materials. One battery restores **100** charge. A battery used on a full tool is not consumed.

- In the inventory, right-click the battery onto the tool, or the tool onto the battery.
- In the world, hold the battery in the other hand and right-click.

Sounds: the start plays on the click that turns the tool on, then a loop plays while it runs. The circular saw also plays a stop sound when the blade spins down. If you turn the tool off or on again before a clip finishes, that clip cuts off.

### Taser
Right-click fires an electrode. It flies out of the taser, hooks onto the mob it hits, and two wires run from the taser to that hook. The wires stay for the stun, as long as you remain in range.

A hooked mob is stunned for **5 seconds**. It cannot attack or use items, it moves slowly, and it shakes. If the shot hits you, the view shakes a little as well.

- Durability is **100**. A Roleplay Materials battery refills the whole bar, so the taser can be charged again after it wears down.
- After every shot a yellow bar above durability fills for **18 seconds**. The taser cannot fire again until that bar is full.

### Wooden Shield
Crafted in the same shape as a vanilla shield. Use **planks** from Roleplay Materials, and one piece of **scrap** where the iron ingot goes:

```
plank  scrap  plank
plank  plank  plank
       plank
```

It blocks like a vanilla shield. Durability is **168**, half of a vanilla shield. Repair it with Roleplay Materials planks.

### Forged Spear
The recipe takes one **steel spear head** from Roleplay Materials, plus tape and two sticks:

```
       head
stick  tape
stick
```

### Throwing other weapons
The config button in the mods list opens the same file as `config/cold_weapons-common.toml`. Under `throwableWeapons`, list item ids you want to throw, for example `minecraft:iron_sword`.

A listed weapon throws like the kitchen knife: hold sneak, hold right-click, then release. A longer hold, up to about a second, throws harder. Right-click without sneak does what that item normally does. Knives from this mod already throw, so leave them off the list.

## 1.1
- New weapons: stone axe, stone spear, knife spear, machete, forged spear, forged mace, chainsaw sword, circular saw
- Circular saw: hold right-click to spin — continuous hitbox damage, sparks and grind sound on contact
- Crafting recipes for several weapons (including Roleplay Materials variants where needed)
- Replaced the rusty pipe model

## 1.0
- Initial release
- Apocalypse-themed melee weapons
