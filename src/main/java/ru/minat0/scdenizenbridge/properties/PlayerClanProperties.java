package ru.minat0.scdenizenbridge.properties;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import ru.minat0.scdenizenbridge.objects.ClanTag;

public class PlayerClanProperties implements Property {

    public static final String[] handledTags = new String[]{"clan"};
    private final PlayerTag player;

    public PlayerClanProperties(PlayerTag playerTag) {
        this.player = playerTag;
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static PlayerClanProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        } else {
            return new PlayerClanProperties((PlayerTag) object);
        }
    }

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlayerClan";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute.startsWith("clan")) {
            return ClanTag.valueOf(player.getPlayerEntity().getUniqueId().toString()).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

    }
}
