package br.com.pentamc.core.engine.command.paramter.impl;

import br.com.pentamc.core.engine.command.paramter.Processor;
import br.com.pentamc.core.engine.command.utils.CommandReflection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class PlayerProcessor extends Processor<Player> {

    public Player process(Object sender, String supplied) {
        Player player = Bukkit.getPlayer(supplied);

        if(player == null) {
            CommandReflection.sendMessage(sender, "§cO jogador '" + supplied + "' não pôde ser encontrado.");
            return null;
        }

        return player;
    }

    public List<String> tabComplete(CommandSender sender, String supplied) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(supplied.toLowerCase()))
                .collect(Collectors.toList());
    }
}
