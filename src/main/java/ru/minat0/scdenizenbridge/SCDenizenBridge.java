package ru.minat0.scdenizenbridge;

import com.denizenscript.depenizen.bukkit.Depenizen;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.plugin.java.JavaPlugin;

public final class SCDenizenBridge extends JavaPlugin {

    private static SimpleClans scPlugin;
    private static SCDenizenBridge instance;

    public static SimpleClans getSCPlugin() {
        return scPlugin;
    }

    public static SCDenizenBridge getInstance() {
        return instance;
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        scPlugin = SimpleClans.getInstance();
        Depenizen.instance.registerBridge("SimpleClans", () -> new SCBridge());
        Depenizen.instance.loadBridge("SimpleClans", () -> new SCBridge());
    }
}
