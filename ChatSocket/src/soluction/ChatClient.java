package soluction;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient
{
    public void execute() throws IOException
    {
        //Lê o nome do cliente
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite seu nome:\n");
        String nome = scanner.nextLine();

        System.out.println("Conectando-se ao servidor");

        //Conecta-se ao servidor (127.0.0.1 é o IP local, porta 5000)
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 5000);
        //Após conectar-se, cria os streams de input e output.
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        //Cria a thread que vai ficar ouvindo as mensagens do servidor.
        Thread readThred = startReadThread(input);

        String mensagem = "";

        System.out.println("Conectado!\nDigite sua mensagem abaixo e pressione <Enter> para enviar");

        //O servidor espera que a primeira mensagem enviada seja o nome do cliente
        out.writeObject(nome);
        //Flush para enviar a mensagem
        out.flush();

        //Enquanto não for pra sair...
        while (!mensagem.equals("sair"))
        {
            //Lê uma mensagem do console
            mensagem = scanner.nextLine();
            //Envia para o servidor
            out.writeObject(mensagem);
            out.flush();
        }

        System.out.println("Saindo...");
        //Fecha as conexões e dá join na thread
        try { input.close(); } catch (IOException e) { e.printStackTrace(); }
        try { out.close(); } catch (IOException e) { e.printStackTrace(); }
        try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        try { readThred.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private Thread startReadThread(ObjectInputStream input)
    {
        //Cria a thread de recepção de mensagens do servidor
        Thread readThread = new Thread() {
            public void run() {
                try
                {
                    //Esta thread basicamente fica ouvindo mensagens do servidor
                    //e imprimindo no console.
                    //Ela será encerrada quando for executado o input.close() no
                    //método start(). Isso resultará numa exceção, que forçará a saída
                    //do while.
                    while (true)
                    {
                        String mensagem = (String) input.readObject();
                        System.out.println(mensagem);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                System.out.println("Encerrando thread de leitura...");
            }
        };

        //Inicia e retorna a thread.
        readThread.start();
        return readThread;
    }
}
