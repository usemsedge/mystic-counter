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
                if (args[1].equalsIgnoreCase("right") || args[1].equalsIgnoreCase("left")) {
                    MysticDropCounter.align = (args[1].equalsIgnoreCase("right")) ? "right": "left";
                }
                else {
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Aligns the text of the mystic drop window to the right or left");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /myst align (right|left)");
                }

            }

            else if (args.length == 1 && args[0].equalsIgnoreCase("count")) {
                chat(player, "Mystic Drops: " + MysticDropCounter.mysticDrops);
                chat(player, "Kills: " + MysticDropCounter.killCount);
                chat(player, "Since Last Drop: " + MysticDropCounter.sinceLastMysticDrop);
            }

            else if (args.length == 2 && args[0].equalsIgnoreCase("color")) {
                char[] c = args[1].toCharArray();

                //last six chars of the input string
                // #ffffff will give "ffffff", so will 0xffffff
                char[] x = Arrays.copyOfRange(c, c.length - 6, c.length);
                String number = String.copyValueOf(x);

                if (!MysticDropCounter.isInteger(number, 16)) {
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Changes the color of the display");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /myst color (color)");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "(color) should be substituted for a 6-character hex value like 00ffff");
                    return;
                }
                MysticDropCounter.color = Integer.decode("0x" + number);
            }

            else if (args.length == 3 && args[0].equalsIgnoreCase("pos")) {

                if (MysticDropCounter.isInteger(args[1]) &&
                    MysticDropCounter.isInteger(args[2])) {

                    MysticDropCounter.guiLocation[0] = Integer.parseInt(args[1]);
                    MysticDropCounter.guiLocation[1] = Integer.parseInt(args[2]);
                }
                else {
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Changes the location of the display");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /myst pos (x) (y)");
                }
            }

            else if(args.length == 2 && args[0].equalsIgnoreCase("l"))
            {
                MysticDropCounter.autoL ^= true;
                chat(player, EnumChatFormatting.GREEN +
                        "Auto L has been toggled " +
                        EnumChatFormatting.DARK_GREEN +
                        (MysticDropCounter.autoL ? "on": "off"));
            }

            else {
                chat(player, EnumChatFormatting.BLACK + "___________________________");

                chat(player, EnumChatFormatting.LIGHT_PURPLE + "Mystic Drop Counter tracks how many kills and mystic drops you get.");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "/mysticcounter [subcommand] [arguments]");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "/myst [subcommand] [arguments]");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "1. /myst toggle");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "2. /myst align (right|left)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "3. /myst count");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "4. /myst color (color)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "5. /myst pos (x) (y)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "6. /myst L toggle");



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