import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) {
		try {
			Server s1 = new Server();
		} catch (IOException | InterruptedException e) {
			System.out.println("IOException | InterruptedException");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static final int MAX_NUMERO = 9999999;
	public static final int FREQ = 200000;
	public int inicio = 0;

	List<String> codigos;
	List<ObjectOutputStream> outs = new ArrayList<>();
	File file = new File("numeros.txt");
	PrintWriter escreve = new PrintWriter(file);

	int posCodigo = 0;

	public Server() throws IOException, InterruptedException {
		codigos = Files.readAllLines(Paths.get("hashes.txt"));
		iniciarServer();
	}

	public void iniciarServer() throws IOException, InterruptedException {
		ServerSocket server = new ServerSocket(5000);
		boolean fim = false;
		while (!fim) {
			System.out.println("Aguardando Conexões");
			Socket cliente = server.accept();

			serverClient(cliente);

			if (posCodigo == codigos.size()) {
				fim = true;
			}
		}
	}

	private void serverClient(Socket cliente) throws IOException, InterruptedException {
		ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());

		outs.add(out);

		Thread escutaThread = new Thread() {
			public void run() {
				try {
					while (posCodigo < codigos.size()) {
						String msg = (String) input.readObject();
						synchronized (outs) {
							if (msg.equals("livre")) {
								mandarNumeros(out);
							} else if (msg.equals("encontrado")) {
								escreverArquivo();
								interromperClientes(out);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}

			private void escreverArquivo() throws ClassNotFoundException, IOException {
				String numero = (String) input.readObject();
				escreve.println(numero);
				escreve.flush();
			}

			private void interromperClientes(ObjectOutputStream out) throws IOException {
				inicio = 0;
				for (ObjectOutputStream stream : outs) {
					stream.writeObject("encontrado");
					stream.flush();
				}
				posCodigo++;
			}

			private void mandarNumeros(ObjectOutputStream out) throws IOException {
				System.out.println(
						"Inicio: " + inicio + "; Fim: " + (inicio + FREQ - 1) + "; Codigo: " + codigos.get(posCodigo));
				out.writeObject(inicio + ";" + (inicio + FREQ - 1) + ";" + codigos.get(posCodigo));
				inicio = inicio + FREQ;
				out.flush();
			}
		};
		escutaThread.start();
	}

}
