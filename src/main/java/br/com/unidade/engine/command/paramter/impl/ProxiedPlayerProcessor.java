package br.com.unidade.engine.command.paramter.impl;

import br.com.unidade.engine.command.paramter.Processor;
import br.com.unidade.engine.command.utils.CommandReflection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class ProxiedPlayerProcessor extends Processor<ProxiedPlayer> {

    public ProxiedPlayer process(Object sender, String supplied) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(supplied);

        if(player == null) {
            CommandReflection.sendMessage(sender, "§cO jogador '" + supplied + "' não pôde ser encontrado.");
            return null;
        }

        return player;
    }

    public List<String> tabComplete(CommandSender sender, String supplied) {
        return ProxyServer.getInstance().getPlayers().stream()
                .map(ProxiedPlayer::getName)
                .filter(name -> name.toLowerCase().startsWith(supplied.toLowerCase()))
                .collect(Collectors.toList());
    }
}
