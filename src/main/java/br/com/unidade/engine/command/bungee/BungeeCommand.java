package br.com.unidade.engine.command.bungee;

import br.com.unidade.engine.command.CommandHandler;
import br.com.unidade.engine.command.help.HelpNode;
import br.com.unidade.engine.command.node.ArgumentNode;
import br.com.unidade.engine.command.node.CommandNode;
import br.com.unidade.engine.command.paramter.ParamProcessor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class BungeeCommand extends Command implements TabExecutor {

    @Getter
    private static final HashMap<String, BungeeCommand> commands = new HashMap<>();

    @SneakyThrows
    public BungeeCommand(String root) {
        super(root);
        commands.put(root.toLowerCase(), this);

        ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) CommandHandler.getPlugin(), this);
    }

    @SneakyThrows
    public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
        List<CommandNode> sortedNodes = CommandNode.getNodes().stream()
                .sorted(Comparator.comparingInt(node -> node.getMatchProbability(sender, getName(), args, false)))
                .collect(Collectors.toList());

        CommandNode node = sortedNodes.get(sortedNodes.size() - 1);
        if(node.getMatchProbability(sender, getName(), args, false) < 90) {
            if(node.getHelpNodes().size() == 0) {
                node.sendUsageMessage(sender);
                return;
            }

            HelpNode helpNode = node.getHelpNodes().get(0);

            if(!helpNode.getPermission().isEmpty() && !sender.hasPermission(helpNode.getPermission())) {
                sender.sendMessage("§cVocê não possui permissão para executar este comando.");
                return;
            }

            helpNode.getMethod().invoke(helpNode.getParentClass(), sender);
            return;
        }

        node.execute(sender, args);
    }

    @Override
    public Iterable<String> onTabComplete(net.md_5.bungee.api.CommandSender sender, String[] args) {
        try {
            List<CommandNode> sortedNodes = CommandNode.getNodes().stream()
                    .sorted(Comparator.comparingInt(node -> node.getMatchProbability(sender, getName(), args, true)))
                    .collect(Collectors.toList());

            CommandNode node = sortedNodes.get(sortedNodes.size() - 1);
            if(node.getMatchProbability(sender, getName(), args, true) >= 50) {

                int extraLength = node.getNames().get(0).split(" ").length - 1;
                int arg = (args.length - extraLength) - 1;

                if(arg < 0 || node.getParameters().size() < arg + 1)
                    return new ArrayList<>();

                ArgumentNode argumentNode = node.getParameters().get(arg);
                return new ParamProcessor(argumentNode, args[args.length - 1], sender).getTabComplete();
            }

            return sortedNodes.stream()
                    .filter(sortedNode -> sortedNode.getPermission().isEmpty() || sender.hasPermission(sortedNode.getPermission()))
                    .map(sortedNode -> sortedNode.getNames().stream()
                            .map(name -> name.split(" "))
                            .filter(splitName -> splitName[0].equalsIgnoreCase(getName()))
                            .filter(splitName -> splitName.length > args.length)
                            .map(splitName -> splitName[args.length])
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .filter(name -> name.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                    .collect(Collectors.toList());
        } catch(Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }
}
