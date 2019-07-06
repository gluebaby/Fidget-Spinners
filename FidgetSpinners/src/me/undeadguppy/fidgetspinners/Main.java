package me.undeadguppy.fidgetspinners;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private List<String> lore;
	private List<String> sounds;
	private String name;
	private ItemStack spinner;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		sounds = getConfig().getStringList("sounds");
		lore = getConfig().getStringList("lore");
		name = ChatColor.translateAlternateColorCodes('&', getConfig().getString("name"));
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		spinner = new ItemStack(Material.CLAY_BALL);
		ItemMeta spinmeta = spinner.getItemMeta();
		spinmeta.setDisplayName(name);
		lore = lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
		spinmeta.setLore(lore);
		spinmeta.spigot().setUnbreakable(true);
		spinner.setItemMeta(spinmeta);

		ShapedRecipe recipe = new ShapedRecipe(spinner);
		recipe.shape("#@#", "@#@");
		recipe.setIngredient('#', Material.AIR);
		recipe.setIngredient('@', Material.CLAY_BALL);
		Bukkit.getServer().addRecipe(recipe);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getPlayer().getInventory().getItemInHand() != null
				&& e.getPlayer().getInventory().getItemInHand().equals(spinner)) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
				if (e.getPlayer().hasPermission("fidgetspinners.use")) {
					int message = new Random().nextInt(sounds.size());
					String s = ChatColor.translateAlternateColorCodes('&', sounds.get(message));
					e.getPlayer().sendMessage(s);
					return;
				} else {
					e.getPlayer().sendMessage(
							ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-permission")));
					return;
				}

			}
		}
	}

}
