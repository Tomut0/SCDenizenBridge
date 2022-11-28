package ru.minat0.scdenizenbridge.objects;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import net.sacredlabyrinth.phaed.simpleclans.ui.InventoryDrawer;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import ru.minat0.scdenizenbridge.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public record FrameTag(SCFrame frame) implements ObjectTag {

    public static ObjectTagProcessor<FrameTag> tagProcessor = new ObjectTagProcessor<>();
    private static String prefix = "frame";

    public static FrameTag valueOf(String string) {
        return valueOf(string, null);
    }

    // Called in Denizen internally
    @SuppressWarnings("unused")
    public static boolean matches(String arg) {
        return valueOf(arg) != null;
    }

    @Fetchable("f")
    public static FrameTag valueOf(String str, TagContext context) {
        if (str == null) return null;

        int hash = Integer.parseInt(str.replace(prefix + "@", ""));

        HashMap<UUID, SCFrame> opening = ReflectionUtils.getStaticFieldValue(InventoryDrawer.class, "OPENING", new HashMap<>());
        Optional<SCFrame> frameOptional = opening.values().stream().filter(scFrame -> scFrame.hashCode() == hash).findAny();

        return frameOptional.map(FrameTag::new).orElse(null);
    }

    // Called in Denizen internally
    @SuppressWarnings("unused")
    public static void registerTags() {
        tagProcessor.registerTag(ElementTag.class, "title", (attribute, frameTag) -> new ElementTag(frameTag.frame.getTitle(), true));
        tagProcessor.registerTag(ElementTag.class, "size", (attribute, frameTag) -> new ElementTag(frameTag.frame.getSize()));
        tagProcessor.registerTag(PlayerTag.class, "viewer", (attribute, frameTag) -> new PlayerTag(frameTag.frame.getViewer()));
        tagProcessor.registerTag(FrameTag.class, "parent", (attribute, frameTag) -> new FrameTag(frameTag.frame.getParent()));
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        if (frame == null) {
            return "null";
        }

        return prefix + "@" + frame.hashCode();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identifySimple();
    }

    @Override
    public Object getJavaObject() {
        return frame();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public ObjectTag setPrefix(String s) {
        prefix = s;
        return this;
    }
}
