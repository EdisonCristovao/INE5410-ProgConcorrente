import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Cliente {

	public static void main(String[] args) {
		Cliente cli = new Cliente();
		try {
			try {
				cli.iniciaCliente();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void iniciaCliente() throws UnknownHostException, IOException, InterruptedException {
		Socket cliente = new Socket("127.0.0.1", 5000);
		
		ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
		out.flush();
		ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());

		Thread threadLeito = startThreadLeitor(input);

		while (true) {
			System.out.println("mandar msg");
			out.writeObject("Tao servidor tudo certo ?");
			out.flush();
			Thread.sleep(10000);
		}
	}

	private Thread startThreadLeitor(ObjectInputStream input) {
		
		Thread threadLeitor = new Thread() {
			public void run() {
				while(true) {
					try {
						String msg = (String) input.readObject();
						System.out.println(msg);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
		
		threadLeitor.start();
		return null;
	}
}
