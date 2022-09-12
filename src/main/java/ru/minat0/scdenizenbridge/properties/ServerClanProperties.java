package ru.minat0.scdenizenbridge.properties;

import com.denizenscript.denizen.tags.core.ServerTagBase;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import ru.minat0.scdenizenbridge.SCDenizenBridge;
import ru.minat0.scdenizenbridge.objects.ClanTag;

import java.util.Collection;
import java.util.stream.Collectors;

public class ServerClanProperties implements Property {

    public static final String[] handledTags = new String[]{"clans"};
    private final ServerTagBase server;

    public ServerClanProperties(ServerTagBase serverTag) {
        this.server = serverTag;
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof ServerTagBase;
    }

    public static ServerClanProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        } else {
            return new ServerClanProperties((ServerTagBase) object);
        }
    }

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ServerClan";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute.startsWith("clans")) {
            return new ListTag((Collection<? extends ObjectTag>) SCDenizenBridge.getSCPlugin().getClanManager().getClans().
                    stream().map(ClanTag::new).collect(Collectors.toSet())).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

    }
}
