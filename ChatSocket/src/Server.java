import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) {
		Server s1 = new Server();
		try {
			s1.iniciarServidor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	List<ObjectOutputStream> clientes;

	private void iniciarServidor() throws IOException {
		clientes = new ArrayList<>();

		ServerSocket server = new ServerSocket(5000);
		while (true) {
			Socket socket = server.accept();

			iniciarThread(socket);
		}
	}

	private void iniciarThread(Socket socket) throws IOException {

		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

		Thread socketThread = new Thread() {

			public void run() {
				boolean primeiraVez = true;
				System.out.println("aff");
				while (true) {
					try {
						String msg = (String) input.readObject();
						System.out.println(msg);

						out.writeObject("Recebi sua msg mano");
						out.flush();
						try {
							Thread.sleep(6000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (primeiraVez) {
							primeiraVez = false;
							synchronized (clientes) {
								clientes.add(out);
							}
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		socketThread.start();
	}
}
