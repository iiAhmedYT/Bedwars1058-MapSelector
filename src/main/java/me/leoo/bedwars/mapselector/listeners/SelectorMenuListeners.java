/*
 *
 */

package me.leoo.bedwars.mapselector.listeners;

import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.Misc;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class SelectorMenuListeners implements Listener {

    private final MapSelector plugin = MapSelector.getPlugin();

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (item.getType().equals(Material.AIR)) return;
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getDisplayName() == null) return;

        Player player = (Player) event.getWhoClicked();

        String tag1 = Misc.getTag(item, "n1");
        String tag2 = Misc.getTag(item, "n2");
        String tag3 = Misc.getTag(item, "n3");
        String tag4 = Misc.getTag(item, "n4");
        String tag5 = Misc.getTag(item, "n5");

        //first gui
        if (event.getView().getTitle().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.title"))) {
            if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.join-random.name").replace("{groupName}", tag2 == null ? "" : tag2))) {
                if (plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.join-random.command").equals("default-action")) {
                    Misc.joinRandomGroup(player, tag1, false, false);
                    player.closeInventory();
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.join-random.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.map-selector.name").replace("{groupName}", tag2 == null ? "" : tag2))) {
                if (plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.map-selector.command").equals("default-action")) {
                    if (tag1 != null) Misc.openSecondGui(player, tag1, 0);
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.map-selector.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.close.name"))) {
                if (plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.close.command").equals("default-action")) {
                    player.closeInventory();
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.close.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.rejoin.name"))) {
                if (plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.rejoin.command").equals("default-action")) {
                    player.performCommand("bw rejoin");
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items.rejoin.command"));
                }
            }
            for (String extra : plugin.getMainConfig().getYml().getConfigurationSection("map-selector.menus.bedwars-menu.items").getKeys(false)) {
                if (plugin.getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items." + extra + ".extra") &&
                        item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items." + extra + ".name"))) {
                    if (plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items." + extra + ".command").equals("default-action")) {
                        if (player.isOp() || player.hasPermission("bwselector.reload")) {
                            String name = plugin.getBedwarsMode().getName();
                            player.sendMessage("§c§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/" + name + "/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (path: map-selector.menus.maps-menu.items." + extra + ".command)§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (path: map-selector.menus.maps-menu.items." + extra + ".enabled)§7.");
                        }
                    } else {
                        player.performCommand(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.items." + extra + ".command"));
                    }
                }
            }

            event.setCancelled(true);
        }

        //second gui
        if (event.getView().getTitle().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.title"))) {
            if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map.name").replace("{mapName}", tag3 == null ? "" : tag3))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map.command").equals("default-action")) {
                    if (event.isRightClick()) {
                        Yaml.setFavorite(player, tag1);
                        if (tag4 != null && tag5 != null)
                            Misc.openSecondGui(player, tag4, Integer.parseInt(tag5));
                    } else {
                        if (Misc.getSelectionsType(player).equals(plugin.getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                            Misc.joinArena(player, tag1, tag2, true);
                            player.closeInventory();
                        } else if (!Misc.getSelectionsType(player).equals(plugin.getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                            if (plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Misc.getSelectionsType(player))) {
                                Misc.joinArena(player, tag1, tag2, false);
                                player.closeInventory();
                            } else {
                                player.closeInventory();
                                player.sendMessage(plugin.getMainConfig().getString("map-selector.messages.limit-reached"));
                            }
                        }
                    }
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.name").replace("{mapName}", tag3 == null ? "" : tag3))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.command").equals("default-action")) {
                    if (event.isRightClick()) {
                        Yaml.unsetFavorite(player, tag1);
                        if (tag4 != null && tag5 != null)
                            Misc.openSecondGui(player, tag4, Integer.parseInt(tag5));
                    } else {
                        if (Misc.getSelectionsType(player).equals(plugin.getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                            Misc.joinArena(player, tag1, tag2, true);
                            player.closeInventory();
                        } else if (!Misc.getSelectionsType(player).equals(plugin.getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                            if (plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Misc.getSelectionsType(player))) {
                                Misc.joinArena(player, tag1, tag2, false);
                                player.closeInventory();
                            } else {
                                player.closeInventory();
                                player.sendMessage(plugin.getMainConfig().getString("map-selector.messages.limit-reached"));
                            }
                        }
                    }
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.name").replace("{mapName}", tag3 == null ? "" : tag3))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.command").equals("default-action")) {
                    player.closeInventory();
                    player.sendMessage(plugin.getMainConfig().getString("map-selector.messages.limit-reached"));
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.random-map.name").replace("{groupName}", tag2 == null ? "" : tag2))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.random-map.command").equals("default-action")) {
                    Misc.joinRandomGroup(player, tag1, false, false);
                    player.closeInventory();
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.random-map.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.random-favourite.name").replace("{groupName}", tag2 == null ? "" : tag2))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.random-favourite.command").equals("default-action")) {
                    if (Misc.getSelectionsType(player).equals(plugin.getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                        Misc.joinRandomGroup(player, tag1, true, true);
                    } else if (!Misc.getSelectionsType(player).equals(plugin.getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                        if (plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Misc.getSelectionsType(player))) {
                            Misc.joinRandomGroup(player, tag1, false, true);
                            player.closeInventory();
                        } else {
                            player.closeInventory();
                            player.sendMessage(plugin.getMainConfig().getString("map-selector.messages.limit-reached"));
                        }
                    }
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.random-favourite.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.back.name"))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.back.command").equals("default-action")) {
                    Misc.openFirstGui(player, tag1);
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.back.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.next-page.name"))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.next-page.command").equals("default-action")) {
                    if (tag1 != null && tag2 != null)
                        Misc.openSecondGui(player, tag1, Integer.parseInt(tag2));
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.next-page.command"));
                }
            } else if (item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.previous-page.name"))) {
                if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.previous-page.command").equals("default-action")) {
                    if (tag1 != null && tag2 != null)
                        Misc.openSecondGui(player, tag1, Integer.parseInt(tag2));
                } else {
                    player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items.previous-page.command"));
                }
            }
            for (String extra : plugin.getMainConfig().getYml().getConfigurationSection("map-selector.menus.maps-menu.items").getKeys(false)) {
                if (plugin.getMainConfig().getBoolean("map-selector.menus.maps-menu.items." + extra + ".extra") &&
                        item.getItemMeta().getDisplayName().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items." + extra + ".name"))) {
                    if (plugin.getMainConfig().getString("map-selector.menus.maps-menu.items." + extra + ".command").equals("default-action")) {
                        if (player.isOp() || player.hasPermission("bwselector.reload")) {
                            String name = plugin.getBedwarsMode().getName();
                            player.sendMessage("§c§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/" + name + "/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (path: map-selector.menus.maps-menu.items." + extra + ".command)§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (path: map-selector.menus.maps-menu.items." + extra + ".enabled)§7.");
                        }
                    } else {
                        player.performCommand(plugin.getMainConfig().getString("map-selector.menus.maps-menu.items." + extra + ".command"));
                    }
                }
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryDropEvent(PlayerDropItemEvent event) {
        if (event.getPlayer().getOpenInventory().getTitle().equals(plugin.getMainConfig().getString("map-selector.menus.bedwars-menu.title"))
                || event.getPlayer().getOpenInventory().getTitle().equals(plugin.getMainConfig().getString("map-selector.menus.maps-menu.title"))) {
            event.setCancelled(true);
        }
    }

}
