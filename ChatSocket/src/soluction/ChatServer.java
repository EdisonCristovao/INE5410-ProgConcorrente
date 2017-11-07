package soluction;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 24/10/2017.
 */
public class ChatServer
{
    private List<ObjectOutputStream> clients;

    public void execute() throws IOException
    {
        clients = new ArrayList<>();
        System.out.println("Aguardando conexões...");

        //Cria um socket para receber conexões na porta 5000
        ServerSocket server = new ServerSocket(5000, 10);
        while (true)
        {
            //Recebe uma nova conexão
            Socket s = server.accept();
            System.out.println("Nova conexão realizada");

            //Inicia uma thread pra ficar ouvindo as mensagens do cliente
            serveClient(s);
        }
    }

    private void serveClient(Socket socket) throws IOException
    {
        //Cria os streams para ler e escrever no socket.
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        //Thread que ficará ouvindo as mensagens desse cliente
        Thread socketThread = new Thread() {
            public void run() {
                String nome = null;

                try
                {
                    while (true)
                    {
                        //Recebe uma mensagem...
                        String mensagem = (String) input.readObject();

                        //Se o nome está vazio, esta é a primeira mensagem. Na primeira mensagem é
                        //esperado que venha apenas o nome.
                        if (nome == null)
                        {
                            nome = mensagem;
                            mensagem = "Conectou-se";

                            //Adiciona o cliente à lista. Assim as próximas mensagens recebidas serão
                            //enviadas para ele também.
                            synchronized (clients)
                            {
                                clients.add(out);
                            }
                        }

                        //Formata e imprime no console a mensagem recebida
                        mensagem = nome + ": " + mensagem;
                        System.out.println(mensagem);

                        //Passa por todos os clientes conectados e envia a mensagem para eles também
                        //(exceto para o próprio cliente).
                        synchronized (clients)
                        {
                            for (ObjectOutputStream stream : clients)
                            {
                                if (stream != out)
                                {
                                    try
                                    {
                                        //Envia a mensagem
                                        stream.writeObject(mensagem);
                                        //Flush esvazia o buffer (envia de fato a mensagem)
                                        stream.flush();
                                    }
                                    catch (IOException e)
                                    {
                                        System.out.println("Não foi possível enviar mensagem...");
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                System.out.println("Encerrando conexão...");

                //Remove da lista de clientes
                synchronized (clients)
                {
                    if (clients.contains(out))
                        clients.remove(out);
                }
                //Fecha os streams e socket
                try { input.close(); } catch (IOException e) { e.printStackTrace(); }
                try { out.close(); } catch (IOException e) { e.printStackTrace(); }
                try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        };

        socketThread.start();
    }
}
