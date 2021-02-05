package io.github.usemsedge;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class MysticDropEventHandler {
    private boolean firstJoin = true;
    private FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
    private boolean tempSuspend = false;
    private int tick = 0;


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onChatMessageRecieved(ClientChatReceivedEvent e) {
        String msg = e.message.getUnformattedText();
        long d = System.currentTimeMillis();

        if (msg.contains("MYSTIC ITEM!") && !msg.contains(":")) {
            MysticDropCounter.mysticDrops++;
            MysticDropCounter.sinceLastMysticDrop = 0;
        }

        else if (msg.contains("KILL!") && !msg.contains(":")) {
            MysticDropCounter.killCount += 0.5;
            MysticDropCounter.sinceLastMysticDrop += 0.5;

        }



    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            tick++;
            if (tick > 9 && Minecraft.getMinecraft() != null
                    && Minecraft.getMinecraft().thePlayer != null) {
                if (Minecraft.getMinecraft().getCurrentServerData() != null
                        && Minecraft.getMinecraft().getCurrentServerData().serverIP != null
                        && Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1)
                        != null) {
                    MysticDropCounter.isInPit = (stripString(StringUtils.stripControlCodes(
                            Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1)
                                    .getDisplayName())).contains("THE HYPIXEL PIT") && Minecraft.getMinecraft()
                            .getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net"));
                }

                tick = 0;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoinevent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        MysticDropCounter.loggedIn = true;
        new ScheduledThreadPoolExecutor(1).schedule(() -> {
            Minecraft.getMinecraft().thePlayer
                    .addChatMessage(new ChatComponentText(EnumChatFormatting.RED +
                            "Downloads not from github.com/usemsedge/mystic-counter are RATs.\n" + EnumChatFormatting.GREEN + "Type /myst to get a list of commands."));
        }, 3, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public void OnPlayerLeaveEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        MysticDropCounter.loggedIn = false;
    }

    @SubscribeEvent
    public void renderLabymodOverlay(RenderGameOverlayEvent event) {
        if (event.type == null && isUsingLabymod() && MysticDropCounter.isInPit) {
            renderStats();
        }
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if ((event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE
                || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR) && MysticDropCounter.isInPit
                && !isUsingLabymod()) {
            renderStats();
        }
    }

    private String stripString(String s) {
        char[] nonValidatedString = StringUtils.stripControlCodes(s).toCharArray();
        StringBuilder validated = new StringBuilder();
        for (char a : nonValidatedString) {
            if ((int) a < 127 && (int) a > 20) {
                validated.append(a);
            }
        }
        return validated.toString();
    }

    private boolean isUsingLabymod() {
        return MysticDropCounter.usingLabyMod;
    }

    private void renderStats() {
        if (MysticDropCounter.toggled) {
            String killsPerMystic =
                    "Kills/Mystic: " + ((MysticDropCounter.mysticDrops == 0) ? MysticDropCounter.mysticDrops
                            : new DecimalFormat("#.##")
                            .format(MysticDropCounter.killCount / (MysticDropCounter.mysticDrops * 1.0d)));
            String kills = "Kills: " + (int)MysticDropCounter.killCount;
            String mystics = "Mystic Drops: " + (int)MysticDropCounter.mysticDrops;
            String lastMystic = "Kills since last Mystic Drop: " + (int)MysticDropCounter.sinceLastMysticDrop;
            String longest = lastMystic;
            //lastMystic is the longest string, because you are not getting a 20 digit number of kills
            if (MysticDropCounter.align.equals("right")) {
                renderer.drawString(mystics, MysticDropCounter.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(mystics),
                        MysticDropCounter.guiLocation[1], MysticDropCounter.color, true);
                renderer.drawString(kills, MysticDropCounter.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(kills),
                        MysticDropCounter.guiLocation[1] + renderer.FONT_HEIGHT, MysticDropCounter.color, true);
                renderer.drawString(killsPerMystic, MysticDropCounter.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(killsPerMystic),
                        MysticDropCounter.guiLocation[1] + renderer.FONT_HEIGHT * 2, MysticDropCounter.color, true);
                renderer.drawString(lastMystic, MysticDropCounter.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(lastMystic),
                        MysticDropCounter.guiLocation[1] + renderer.FONT_HEIGHT * 3, MysticDropCounter.color, true);
            }
            else {
                renderer.drawString(mystics, MysticDropCounter.guiLocation[0],
                        MysticDropCounter.guiLocation[1], MysticDropCounter.color, true);
                renderer.drawString(kills, MysticDropCounter.guiLocation[0],
                        MysticDropCounter.guiLocation[1] + renderer.FONT_HEIGHT, MysticDropCounter.color, true);
                renderer.drawString(killsPerMystic, MysticDropCounter.guiLocation[0],
                        MysticDropCounter.guiLocation[1] + renderer.FONT_HEIGHT * 2, MysticDropCounter.color, true);
                renderer.drawString(lastMystic, MysticDropCounter.guiLocation[0],
                        MysticDropCounter.guiLocation[1] + renderer.FONT_HEIGHT * 3, MysticDropCounter.color, true);
            }
        }
    }

}