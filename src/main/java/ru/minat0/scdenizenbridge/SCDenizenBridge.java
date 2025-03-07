package ru.minat0.scdenizenbridge;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.Depenizen;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.logging.Level;

public final class SCDenizenBridge extends JavaPlugin {

    private static SimpleClans scPlugin;
    private static SCDenizenBridge instance;

    public static SimpleClans getSCPlugin() {
        return scPlugin;
    }

    public static SCDenizenBridge getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        scPlugin = SimpleClans.getInstance();
        Depenizen.instance.registerBridge("SimpleClans", getSupplier());

        String DepVersion = Depenizen.instance.getDescription().getVersion();
        String[] semVer = DepVersion.split("\\.");

        if (semVer.length > 1 && semVer.length <= 3) {
            int major = Integer.parseInt(semVer[0]);
            int minor = Integer.parseInt(semVer[1]);
            if (major == 2 && minor >= 1) {
                Depenizen.instance.loadBridge("SimpleClans", new Depenizen.BridgeData("ru.minat0.scdenizenbridge.SCDenizenBridge", getSupplier()));
            } else {
                loadOldDepenizen();
            }
        } else {
            instance.getLogger().severe("Unrecognized version: " + DepVersion);
        }
    }

    private static void loadOldDepenizen() {
        try {
            // Get the loadBridge method
            Method loadBridge = Depenizen.class.getDeclaredMethod("loadBridge", String.class, Supplier.class);
            loadBridge.setAccessible(true);
            // Invoke the method on the Depenizen instance
            loadBridge.invoke(Depenizen.instance, "SimpleClans", getSupplier());
        } catch (ReflectiveOperationException ex) {
            instance.getLogger().log(Level.SEVERE, "Failed to load Depenizen <= 2.0.0", ex);
        }
    }

    private static Supplier<Bridge> getSupplier() {
        return SCBridge::new;
    }
}
