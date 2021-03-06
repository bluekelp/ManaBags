package net.aesircraft.ManaBags.Keys;

import com.cypherx.xauth.xAuth;
import com.orange451.UltimateArena.main;
import net.aesircraft.ManaBags.Bags.ChestManager;
import net.aesircraft.ManaBags.Bags.PlayerBag;
import net.aesircraft.ManaBags.Bags.View;
import net.aesircraft.ManaBags.Bags.VirtualChest;
import net.aesircraft.ManaBags.Config.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Bag4Key implements BindingExecutionDelegate {

    @Override
    public void keyPressed(KeyBindingEvent kbe) {
    }

    @Override
    public void keyReleased(KeyBindingEvent kbe) {
	Player p = kbe.getPlayer();
	SpoutPlayer sp = SpoutManager.getPlayer(p);
	if (kbe.getScreenType() != ScreenType.GAME_SCREEN) {
	    return;
	}
	if (sp.getGameMode() == GameMode.CREATIVE && Config.getProtectCreative()) {
	    return;
	}
	if (Config.getUsexAuth()) {
	    xAuth x = (xAuth) Bukkit.getServer().getPluginManager().getPlugin("xAuth");
	    if (!x.getPlyrMngr().getPlayer(p).isAuthenticated()) {
		return;
	    }
	}
	if (Config.getUseUltimateArena()) {
	    main arena = (main) Bukkit.getServer().getPluginManager().getPlugin("UltimateArena");
	    if ((arena.isInArena(p))) {
		return;
	    }
	}
	if (PlayerBag.disabled.contains(p))
	    return;
	PlayerBag pb = new PlayerBag(p, 4);
	if (pb.getType() == 0) {
	    sp.sendNotification("§4Notice", "§eNo bag in Slot 4!", Material.CHEST);
	    return;
	}
	VirtualChest c;
	if (pb.getLarge()){
	    c=pb.getVirtualLargeChest();
	}
	else{
	    c=pb.getVirtualChest();
	}
	InventoryView gui=new View(p,c);
	InventoryOpenEvent ev=new InventoryOpenEvent(gui);
	Bukkit.getPluginManager().callEvent(ev);
	if (ev.isCancelled()){
	    return;
	}
	if (ChestManager.bags.containsKey(p)){
	    PlayerBag get = ChestManager.bags.get(p);
	    ChestManager.bags.remove(p);
	    get.close();
	}
	
	pb.open();
    }
}
