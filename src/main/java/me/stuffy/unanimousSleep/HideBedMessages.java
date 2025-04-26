package me.stuffy.unanimousSleep;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HideBedMessages {
    private final UnanimousSleepPlugin plugin;

    public HideBedMessages(UnanimousSleepPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SYSTEM_CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {

                boolean overlay = event.getPacket().getBooleans().read(0);
                if (!overlay) {
                    return;
                }

                WrappedChatComponent systemChat = event.getPacket().getChatComponents().read(0);
                String json = systemChat.getJson();
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                if(jsonObject.has("translate")){
                    String translateKey = jsonObject.get("translate").getAsString();
                    if (translateKey.equals("sleep.skipping_night") || translateKey.equals("sleep.not_possible") || translateKey.equals("sleep.players_sleeping")) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}
