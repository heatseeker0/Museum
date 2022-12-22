package com.mcspacecraft.museum.timeismoney;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcspacecraft.museum.util.ChatMessages;

public class ATM implements Listener {
    public static String ATM_SIGN_THANK_YOU = "Thank you!";
    public static String ATM_SIGN_NEW_SOLD = "New sold : ";
    public static String ATM_SIGN_LINE0 = "Hi ";
    public static String ATM_SIGN_LINE1 = "Money earned:";
    public static String CURRENCY_SYMBOL = "sc";
    public static String ATM_SIGN_LINE3 = "<-Get money->";
    
    @EventHandler(ignoreCancelled =  true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();
            
            // 77 - stone button
            // 68 - wall sign
            // 70 - stone pressure plate            
            
            // Player clicked on any of the ATM buttons to get the money?
            if (isButton(b.getType()) && (
                    isWallSign(b.getRelative(0, 0, -1).getType()) && isButton(b.getRelative(0, 0, -2).getType()) &&  isPressurePlate(b.getRelative(-1, -1, -1).getType()) &&  isPressurePlate(b.getRelative(-2, -1, -1).getType()) ||                     
                    isWallSign(b.getRelative(0, 0, 1).getType()) && isButton(b.getRelative(0, 0, 2).getType()) && isPressurePlate(b.getRelative(1, -1, 1).getType()) &&  isPressurePlate(b.getRelative(2, -1, 1).getType()) || 
                    isWallSign(b.getRelative(1, 0, 0).getType()) && isButton(b.getRelative(2, 0, 0).getType()) && isPressurePlate(b.getRelative(1, -1, -1).getType()) &&  isPressurePlate(b.getRelative(1, -1, -2).getType()) || 
                    isWallSign(b.getRelative(-1, 0, 0).getType()) && isButton(b.getRelative(-2, 0, 0).getType()) && isPressurePlate(b.getRelative(-1, -1, 1).getType()) &&  isPressurePlate(b.getRelative(-1, -1, 2).getType()) || 
                    isWallSign(b.getRelative(0, 0, 1).getType()) && isButton(b.getRelative(0, 0, 2).getType()) && isPressurePlate(b.getRelative(-1, -1, 1).getType()) &&  isPressurePlate(b.getRelative(-2, -1, 1).getType()) || 
                    isWallSign(b.getRelative(0, 0, -1).getType()) && isButton(b.getRelative(0, 0, -2).getType()) && isPressurePlate(b.getRelative(1, -1, -1).getType()) && isPressurePlate(b.getRelative(2, -1, -1).getType()) || 
                    isWallSign(b.getRelative(-1, 0, 0).getType()) && isButton(b.getRelative(-2, 0, 0).getType()) && isPressurePlate(b.getRelative(-1, -1, -1).getType()) &&  isPressurePlate(b.getRelative(-1, -1, -2).getType()) || 
                    isWallSign(b.getRelative(1, 0, 0).getType()) && isButton(b.getRelative(2, 0, 0).getType()) && isPressurePlate(b.getRelative(1, -1, 1).getType()) && isPressurePlate(b.getRelative(1, -1, 2).getType()))) {
                
                int dx = 0;
                int dy = 0;
                int dz = 0;
                
                if (isWallSign(b.getRelative(1, 0, 0).getType())) {
                    dx = 1;
                    dy = 0;
                    dz = 0;
                } else if (isWallSign(b.getRelative(-1, 0, 0).getType())) {
                    dx = -1;
                    dy = 0;
                    dz = 0;
                } else if (isWallSign(b.getRelative(0, 0, 1).getType())) {
                    dx = 0;
                    dy = 0;
                    dz = 1;
                } else if (isWallSign(b.getRelative(0, 0, -1).getType())) {
                    dx = 0;
                    dy = 0;
                    dz = -1;
                }
                
                Sign sign = (Sign) b.getRelative(dx, dy, dz).getState();
                if (sign.getLine(3).length() > 2 && sign.getLine(3).substring(2).compareTo(ATM_SIGN_LINE3.substring(0, sign.getLine(3).substring(2).length())) == 0) {
                    sign.setLine(0, (new StringBuilder("\2471")).append(ATM_SIGN_THANK_YOU).toString());
                    sign.setLine(1, (new StringBuilder("\2471")).append(ATM_SIGN_NEW_SOLD).toString());
                    sign.setLine(2, (new StringBuilder("\247A")).append("0 ").append(CURRENCY_SYMBOL).toString());
                    sign.setLine(3, "");
                    sign.update();
                }

                player.sendMessage(ChatMessages.getMessage("atm.money.claimed", "amount", "0"));
            }
        } else if (event.getAction() == Action.PHYSICAL) {
            Block b = event.getClickedBlock();
            // Player steps on the 2nd pressure plate (away from the sign)?
            if (isPressurePlate(b.getType()) && (
                    isWallSign(b.getRelative(2, 1, 0).getType()) && isButton(b.getRelative(2, 1, -1).getType()) && isButton(b.getRelative(2, 1, 1).getType()) && isPressurePlate(b.getRelative(1, 0, 0).getType()) || 
                    isWallSign(b.getRelative(-2, 1, 0).getType()) && isButton(b.getRelative(-2, 1, -1).getType()) && isButton(b.getRelative(-2, 1, 1).getType()) && isPressurePlate(b.getRelative(-1, 0, 0).getType()) || 
                    isWallSign(b.getRelative(0, 1, 2).getType()) && isButton(b.getRelative(-1, 1, 2).getType()) && isButton(b.getRelative(1, 1, 2).getType()) && isPressurePlate(b.getRelative(0, 0, 1).getType()) || 
                    isWallSign(b.getRelative(0, 1, -2).getType()) && isButton(b.getRelative(-1, 1, -2).getType()) && isButton(b.getRelative(1, 1, -2).getType()) && isPressurePlate(b.getRelative(0, 0, -1).getType()))) {

                int dx = 0;
                int dy = 0;
                int dz = 0;

                if (isWallSign(b.getRelative(2, 1, 0).getType())) {
                    dx = 2;
                    dy = 1;
                    dz = 0;
                } else if (isWallSign(b.getRelative(-2, 1, 0).getType())) {
                    dx = -2;
                    dy = 1;
                    dz = 0;
                } else if (isWallSign(b.getRelative(0, 1, 2).getType())) {
                    dx = 0;
                    dy = 1;
                    dz = 2;
                } else if (isWallSign(b.getRelative(0, 1, -2).getType())) {
                    dx = 0;
                    dy = 1;
                    dz = -2;
                }

                Sign sign = (Sign) b.getRelative(dx, dy, dz).getState();
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.update();
            }

            // Player steps on the 1st pressure plate (next to the sign)?
            if (isPressurePlate(b.getType()) && (
                    isWallSign(b.getRelative(1, 1, 0).getType()) && isButton(b.getRelative(1, 1, -1).getType()) && isButton(b.getRelative(1, 1, 1).getType()) && isPressurePlate(b.getRelative(-1, 0, 0).getType()) || 
                    isWallSign(b.getRelative(-1, 1, 0).getType()) && isButton(b.getRelative(-1, 1, -1).getType()) && isButton(b.getRelative(-1, 1, 1).getType()) && isPressurePlate(b.getRelative(1, 0, 0).getType()) ||
                    isWallSign(b.getRelative(0, 1, 1).getType()) && isButton(b.getRelative(-1, 1, 1).getType()) && isButton(b.getRelative(1, 1, 1).getType()) && isPressurePlate(b.getRelative(0, 0, -1).getType()) ||
                    isWallSign(b.getRelative(0, 1, -1).getType()) && isButton(b.getRelative(-1, 1, -1).getType()) && isButton(b.getRelative(1, 1, -1).getType()) && isPressurePlate(b.getRelative(0, 0, 1).getType()))) {

                int dx = 0;
                int dy = 0;
                int dz = 0;

                if (isWallSign(b.getRelative(1, 1, 0).getType())) {
                    dx = 1;
                    dy = 1;
                    dz = 0;
                } else if (isWallSign(b.getRelative(-1, 1, 0).getType())) {
                    dx = -1;
                    dy = 1;
                    dz = 0;
                } else if (isWallSign(b.getRelative(0, 1, 1).getType())) {
                    dx = 0;
                    dy = 1;
                    dz = 1;
                } else if (isWallSign(b.getRelative(0, 1, -1).getType())) {
                    dx = 0;
                    dy = 1;
                    dz = -1;
                }
                Sign sign = (Sign) b.getRelative(dx, dy, dz).getState();
                sign.setLine(0, (new StringBuilder("\2471")).append(ATM_SIGN_LINE0).append(player.getName()).toString());
                sign.setLine(1, (new StringBuilder("\2471")).append(ATM_SIGN_LINE1).toString());
                sign.setLine(2, (new StringBuilder("\247A")).append("0").append(CURRENCY_SYMBOL).toString());
                sign.setLine(3, (new StringBuilder("\2471")).append(ATM_SIGN_LINE3).toString());
                sign.update();
            }
        }
    }

    private static List<Material> PRESSURE_PLATES = Arrays.asList(Material.ACACIA_PRESSURE_PLATE, Material.BAMBOO_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE,
        Material.DARK_OAK_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE,
        Material.OAK_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE);

    public static boolean isPressurePlate(Material m) {
        return PRESSURE_PLATES.contains(m);
    }

    private static List<Material> BUTTONS = Arrays.asList(Material.ACACIA_BUTTON, Material.BAMBOO_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON,
        Material.MANGROVE_BUTTON, Material.OAK_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON, Material.WARPED_BUTTON);

    public static boolean isButton(Material m) {
        return BUTTONS.contains(m);
    }

    private static List<Material> WALL_SIGNS = Arrays.asList(Material.ACACIA_WALL_SIGN, Material.BAMBOO_WALL_SIGN, Material.BIRCH_WALL_SIGN, Material.CRIMSON_WALL_SIGN, Material.DARK_OAK_WALL_SIGN,
        Material.JUNGLE_WALL_SIGN, Material.MANGROVE_WALL_SIGN, Material.OAK_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.WARPED_WALL_SIGN);

    public static boolean isWallSign(Material m) {
        return WALL_SIGNS.contains(m);
    }
}
