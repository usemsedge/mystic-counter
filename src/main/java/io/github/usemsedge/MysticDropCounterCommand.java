package io.github.usemsedge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class MysticDropCounterCommand extends CommandBase {
    @Override
    public List getCommandAliases() {
        return new ArrayList<String>() {
            {
                add("myst");
            }
        };
    }

    @Override
    public String getCommandName()
    {
        return "mysticcounter";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/mysticcounter [subcommand]";
    }

    public void chat(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        if (ics instanceof EntityPlayer && MysticDropCounter.isInPit) {
            final EntityPlayer player = (EntityPlayer) ics;
            if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
                MysticDropCounter.toggled ^= true;
                chat(player, EnumChatFormatting.GREEN +
                        "Mystic Drop Counter has been toggled " +
                        EnumChatFormatting.DARK_GREEN +
                        (MysticDropCounter.toggled ? "on": "off"));

            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("align")) {
                MysticDropCounter.align = (args[1].equalsIgnoreCase("right")) ? "right": "left";
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("tips")) {
                chat(player, EnumChatFormatting.DARK_GREEN + "PIT TIPS FOR NONS (not unranked players, think skyblock terminology)");
                chat(player, EnumChatFormatting.GREEN + "To get an axe: You must be Prestige 2 and buy the Barbarian renown upgrade, then buy it in the perk shop");
                chat(player, EnumChatFormatting.GREEN + "Perks you should unlock in order: G-Head, Strength Chain, Vampire (replace G-Head), Gladiator OR Streaker");
                chat(player, EnumChatFormatting.GREEN + "Fresh: red, green, yellow, blue, orange fresh pants, worth about 15k");
                chat(player, EnumChatFormatting.GREEN + "Golden (enchanted) swords: mystic swords, can be enchanted to T1 and T2 when you have the Level 1 Mysticism upgrade, and can be T3 when you have the Level 9 Mysticism Upgrade ");
                chat(player, EnumChatFormatting.GREEN + "The Pit is a PVP game and expect to be killed.");
                chat(player, EnumChatFormatting.GREEN + "The Pit has a rabbit infestation that you can't do anything about.");
                chat(player, EnumChatFormatting.GREEN + "If you think diamond armor is unfair, buy diamond armor yourself.");
                chat(player, EnumChatFormatting.GREEN + "Permanent diamond armor is either an Archangel Chestplate or someone unlocking Autobuy.");
                chat(player, EnumChatFormatting.GREEN + "Before downloading a Pit Mod, be sure to make sure it does not contain any RATs");
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("count")) {
                chat(player, "Mystic Drops: " + (int)MysticDropCounter.mysticDrops);
                chat(player, "Kills: " + (int)MysticDropCounter.killCount);
                chat(player, "Since Last Drop: " + (int)MysticDropCounter.sinceLastMysticDrop);
            }

            else {
                chat(player, EnumChatFormatting.BLACK + "___________________________");

                chat(player, EnumChatFormatting.LIGHT_PURPLE + "/mysticcounter [subcommand] [arguments]");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "/myst [subcommand] [arguments]");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "1. toggle");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "2. align (right|left)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "3. tips");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "4. count");



            }
        }

        else {
            ics.addChatMessage(
                    new ChatComponentText(
                            EnumChatFormatting.RED + "Please join The Hypixel Pit to use this command."));
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}