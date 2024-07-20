package br.com.unidade.engine.command.bukkit;

import br.com.unidade.engine.command.help.HelpNode;
import br.com.unidade.engine.command.node.ArgumentNode;
import br.com.unidade.engine.command.node.CommandNode;
import br.com.unidade.engine.command.paramter.ParamProcessor;
import br.com.unidade.engine.command.CommandHandler;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class BukkitCommand extends Command {

    @Getter private static final HashMap<String, BukkitCommand> commands = new HashMap<>();

    @SneakyThrows
    public BukkitCommand(String root) {
        super(root);
        commands.put(root.toLowerCase(), this);

        Field commandMap = ((Plugin) CommandHandler.getPlugin()).getServer().getClass().getDeclaredField("commandMap");
        commandMap.setAccessible(true);
        ((org.bukkit.command.CommandMap) commandMap.get(CommandHandler.getPlugin())).register(CommandHandler.getPluginName(), this);
    }

    @SneakyThrows
    public boolean execute(CommandSender sender, String label, String[] args) {
        List<CommandNode> sortedNodes = CommandNode.getNodes().stream()
                .sorted(Comparator.comparingInt(node -> node.getMatchProbability(sender, label, args, false)))
                .collect(Collectors.toList());

        CommandNode node = sortedNodes.get(sortedNodes.size() - 1);
        if(node.getMatchProbability(sender, label, args, false) < 90) {
            if(node.getHelpNodes().isEmpty()) {
                node.sendUsageMessage(sender);
                return false;
            }

            HelpNode helpNode = node.getHelpNodes().get(0);

            if(!helpNode.getPermission().isEmpty() && !sender.hasPermission(helpNode.getPermission())) {
                sender.sendMessage("§cVocê não possui permissão para executar este comando.");
                return false;
            }

            helpNode.getMethod().invoke(helpNode.getParentClass(), sender);
            return false;
        }

        node.execute(sender, args);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        try {
            List<CommandNode> sortedNodes = CommandNode.getNodes().stream()
                    .sorted(Comparator.comparingInt(node -> node.getMatchProbability(sender, label, args, true)))
                    .collect(Collectors.toList());

            CommandNode node = sortedNodes.get(sortedNodes.size() - 1);
            if(node.getMatchProbability(sender, label, args, true) >= 50) {

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
                            .filter(splitName -> splitName[0].equalsIgnoreCase(label))
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
