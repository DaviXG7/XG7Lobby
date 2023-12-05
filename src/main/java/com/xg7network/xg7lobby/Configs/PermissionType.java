package com.xg7network.xg7lobby.Configs;

public enum PermissionType {

    ADMIN("xg7lobby.admin"),

    BUILD("xg7lobby.build"),
    SETLOBBY_COMMAND("xg7lobby.command.setlobby"),
    RELOAD_COMMAND("xg7lobby.command.reload"),
    FLY_COMMAND("xg7lobby.command.fly"),
    FLY_OTHER("xg7lobby.command.flyother"),
    GUI_COMMAND("xg7lobby.command.gui"),
    MUTE_COMMAND("xg7lobby.command.mute"),
    BAN_COMMAND("xg7lobby.command.ban"),
    KICK_COMMAND("xg7lobby.command.kick"),
    WARN_COMMAND("xg7lobby.command.warn"),

    INV("xg7lobby.inv"),

    HELP_COMMAND("xg7lobby.command.help"),

    LOCK_CHAT("xg7lobby.command.lockchat"),

    VANISH_COMMAND("xg7lobby.command.vanish"),
    ITENS_JOGAR("xg7lobby.items.drop"),
    ITENS_PEGAR("xg7lobby.items.pickup"),
    BLOCOS_INTERAGIR("xg7lobby.blocks.interact"),
    BLOCOS_QUEBRAR("xg7lobby.blocks.break"),
    BLOCOS_COLOCAR("xg7lobby.blocks.place"),
    ATACAR("xg7lobby.attack"),
    DOUBLE_JUMP("xg7lobby.doublejump"),

    CHAT_PALAVRAS("xg7lobby.chat.badwords"),
    CHAT_COMANDOS("xg7lobby.chat.commands"),

    GAMEMODE_CREATIVE("xg7lobby.gamemode.creative"),
    GAMEMODE_SURVIVAL("xg7lobby.gamemode.survival"),
    GAMEMODE_ADVENTURE("xg7lobby.gamemode.adventure"),
    GAMEMODE_SPECTATOR("xg7lobby.gamemode.spectator"),
    GAMEMODE_OTHERS("xg7lobby.gamemode.others");



    private String perm;

    PermissionType(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return this.perm;
    }

}
