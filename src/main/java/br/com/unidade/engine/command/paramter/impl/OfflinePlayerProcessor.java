package br.com.unidade.engine.command.paramter.impl;

import br.com.unidade.engine.command.paramter.Processor;
import br.com.unidade.engine.command.utils.CommandReflection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class OfflinePlayerProcessor extends Processor<OfflinePlayer> {

    public OfflinePlayer process(Object sender, String supplied) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(supplied);

        if(player == null) {
            CommandReflection.sendMessage(sender, "§cNenhum jogador com o nome '" + supplied + "' foi encontrado.");
            return null;
        }

        return player;
    }

    public List<String> tabComplete(Object sender, String supplied) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(name -> name.toLowerCase().startsWith(supplied.toLowerCase()))
                .limit(100)
                .collect(Collectors.toList());
    }
}
