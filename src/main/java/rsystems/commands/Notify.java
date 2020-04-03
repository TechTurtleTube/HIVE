package rsystems.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import rsystems.HiveBot;

public class Notify extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //Don't accept messages from BOT Accounts
        if(event.getMessage().getAuthor().isBot()){
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(args[0].equalsIgnoreCase((HiveBot.prefix + "notify"))){
            try {
                if(!(event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) && !(event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES))){
                    event.getMessage().addReaction("\uD83D\uDEAB").queue();
                    event.getChannel().sendMessage("Missing permissions | Error 3X95Z").queue();
                    return;  //no point in continuing
                }

                //Check to see if user has notifications role
                if(event.getMember().getRoles().toString().contains("Notify")){
                    //User already has role

                    if((args.length > 1) && (args[1].equalsIgnoreCase("?"))){
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you have the notify role.").queue();
                        return;
                    }

                    event.getGuild().modifyMemberRoles(event.getMember(),null,event.getGuild().getRolesByName("Notify", false)).queue();
                    event.getChannel().sendMessage("Hello " + event.getMessage().getAuthor().getAsMention() + ", I have removed the notify role from you.").queue();

                } else {

                    if((args.length > 1) && (args[1].equalsIgnoreCase("?"))){
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you do not have the notify role.").queue();
                        return;
                    }
                    //User does not have role
                    event.getGuild().modifyMemberRoles(event.getMember(),event.getGuild().getRolesByName("Notify", false),null).queue();
                    event.getChannel().sendMessage("Hello " + event.getMessage().getAuthor().getAsMention() + ", I have added the notify role to you.").queue();
                }
            }

            catch(NullPointerException e){
                System.out.println("Found null for roles");
            }

            catch(InsufficientPermissionException e){
                System.out.println("Notify attempted call without access");
            }

            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
