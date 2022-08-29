package minat0.scdenizenbridge;

import com.denizenscript.depenizen.bukkit.Depenizen;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.plugin.java.JavaPlugin;

public final class SCDenizenBridge extends JavaPlugin {

    private static SimpleClans ScPlugin;

    public static SimpleClans getSCPlugin() {
        return ScPlugin;
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public void onEnable() {
        // Plugin startup logic
        ScPlugin = SimpleClans.getInstance();
        Depenizen.instance.registerBridge("SimpleClans", () -> new SCBridge());
        Depenizen.instance.loadBridge("SimpleClans", () -> new SCBridge());
    }
}
