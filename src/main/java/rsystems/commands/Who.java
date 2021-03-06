package rsystems.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import rsystems.HiveBot;
import rsystems.adapters.RoleCheck;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static rsystems.HiveBot.LOGGER;

public class Who extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if(event.getAuthor().isBot()){
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if((HiveBot.commands.get(3).checkCommand(event.getMessage().getContentRaw())) && (args.length < 2)){
            LOGGER.info(HiveBot.commands.get(3).getCommand() + " called by " + event.getAuthor().getAsTag());
            event.getMessage().addReaction("\uD83D\uDC1D ").queue(); // Bee Emoji
            event.getMessage().addReaction("\uD83D\uDC4B ").queue(); // Waving hand emoji
            try {
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                long uptime = runtimeMXBean.getUptime();
                long uptimeinSeconds = uptime/1000;
                long uptimeHours = uptimeinSeconds / (60*60);
                long uptimeMinutes = (uptimeinSeconds/60) - (uptimeHours * 60);
                long uptimeSeconds = uptimeinSeconds % 60;

                String prefix = HiveBot.prefix;

                EmbedBuilder info = new EmbedBuilder();
                if(HiveBot.prefix.equals("~")){
                    prefix = "~ (tilde)";
                }
                info.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
                info.setDescription("I am HIVE! A buzzy little bot that is here to help!\nWanna see a list of commands just type: `" + HiveBot.prefix + "info`  \n\nIf you run into any issues please contact my creator: "+ event.getGuild().getMemberById("313832264792539142").getAsMention() + "\uD83E\uDDD9\u200D️ ");
                info.addField("Prefix:",prefix,true);
                info.addField("Build Info","Version: " + HiveBot.version,true);
                info.addField("Uptime:",uptimeHours + " Hours " + uptimeMinutes + " Minutes " + uptimeSeconds + " Seconds",false);
                info.setColor(Color.ORANGE);
                event.getChannel().sendMessage(info.build()).queue();
                info.clear();
            } catch(PermissionException e){
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " I am HIVE! A buzzy little bot that is here to help!\nWanna see a list of commands just type: `" + HiveBot.prefix + "help`  \n\nIf you run into any issues please contact my creator: Blade2021#8727" + "\uD83E\uDDD9\u200D♂️ ").queue();
            }

        }

        //Admin Who command
        if((args.length > 1) && (HiveBot.commands.get(22).checkCommand(event.getMessage().getContentRaw()))){
            try{
                if (RoleCheck.checkRank(event.getMessage(),event.getMember(),HiveBot.commands.get(22))){
                    try {
                        event.getMessage().delete().queue();
                    } catch(PermissionException e){
                        // No delete permissions
                    }
                    if(event.getMessage().getMentionedMembers().size() > 0) {
                        List<Member> mentions = event.getMessage().getMentionedMembers();
                        EmbedBuilder info = new EmbedBuilder();
                        for (Member m : mentions) {
                            info.setTitle("User Information");
                            info.addField("User: ", m.getAsMention(), true);
                            info.setColor(Color.CYAN);
                            info.addField("Tag", m.getUser().getAsTag(), true);
                            info.addField("UserID", m.getId(), true);
                            info.addField("Joined", m.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE), true);
                            info.addField("Created", m.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE), true);
                            info.addField("Karma:", String.valueOf(HiveBot.karmaSQLHandler.getKarma(m.getId())), true);
                            info.setFooter("Called by " + event.getMessage().getAuthor().getName(), event.getMember().getUser().getAvatarUrl());
                            info.setThumbnail(m.getUser().getEffectiveAvatarUrl());

                            info.setColor(Color.CYAN);
                            info.setFooter("Called by " + event.getMessage().getAuthor().getName(), event.getMember().getUser().getAvatarUrl());
                            event.getChannel().sendMessage(info.build()).queue();
                            info.clear();
                        }
                    } else {
                        try{
                            for(Member m:event.getGuild().getMembers()){
                                if(m.getId().equalsIgnoreCase(args[1])){
                                    EmbedBuilder info = new EmbedBuilder();
                                    info.setTitle("User Information");
                                    info.setColor(Color.CYAN);
                                    info.addField("User:", m.getEffectiveName(), true);
                                    info.addField("Tag: ", m.getUser().getAsTag(), true);
                                    info.addField("UserID", m.getId(), true);
                                    info.addField("Joined", m.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE), true);
                                    info.addField("Created", m.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE), true);
                                    info.addField("Karma:", String.valueOf(HiveBot.karmaSQLHandler.getKarma(m.getId())), true);
                                    info.setFooter("Called by " + event.getMessage().getAuthor().getName(), event.getMember().getUser().getAvatarUrl());
                                    info.setThumbnail(m.getUser().getEffectiveAvatarUrl());

                                    event.getChannel().sendMessage(info.build()).queue();
                                    info.clear();
                                }
                            }
                        } catch (NullPointerException e){}
                    }
                }
            }
            catch(NullPointerException ignored){
            }
        }

    }
}
