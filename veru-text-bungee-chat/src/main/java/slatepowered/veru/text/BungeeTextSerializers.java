package slatepowered.veru.text;

public class BungeeTextSerializers {

    public static MinecraftLegacyTextSerializer legacyText() {
        return MinecraftLegacyTextSerializer.legacy();
    }

    public static BungeeComponentTextSerializer bungeeComponents() {
        return BungeeComponentTextSerializer.bungeeComponents();
    }

}
