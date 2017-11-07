package soluction;


import java.io.IOException;

public class ChatRunner
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        if (args.length != 1) {
            System.out.println("Use ChatRunner [server|client]");
            return;
        }

        if (args[0].equals("server"))
        {
            ChatServer server = new ChatServer();
            server.execute();
        }
        else
        {
            ChatClient client = new ChatClient();
            client.execute();
        }
    }
}
