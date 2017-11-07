import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

public class Cliente {

	public static void main(String[] args) {
		try {
			Cliente cli = new Cliente();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			System.exit(0);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
			e.printStackTrace();
			System.exit(0);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Finalizando Cliente Pelo Exception");
			e.printStackTrace();
			System.exit(0);
		}
	}

	int rangeInicio;
	int rangeFim;
	String codigo = "1";

	boolean encontrado = false;

	Lock lock = new ReentrantLock();
	Condition liberaTrabalho = lock.newCondition();

	public Cliente() throws UnknownHostException, IOException, InterruptedException, NoSuchAlgorithmException {
		iniciaCliente();
	}

	public void iniciaCliente()
			throws UnknownHostException, IOException, InterruptedException, NoSuchAlgorithmException {
		Socket cliente = new Socket("127.0.0.1", 5000);

		ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());

		// cria trhead que vai ouvir msgs do servidor
		Thread escutaServer = iniciarEscutaServer(input);

		while (true) {
			out.writeObject("livre");
			out.flush();

			lock.lock();
			liberaTrabalho.await();
			if (codigo.equals("1")) {
				liberaTrabalho.await();
			}

			lock.unlock();
			long tempo = System.currentTimeMillis();
			int i = rangeInicio;

			System.out.println("Iniciando nova faixa -->Inicio: " + rangeInicio + "; Fim: " + rangeFim);
			
			while (i <= rangeFim && !encontrado) {
				// faz a conversao para numero com 7 digitos
				String numero = String.format("%07d", i);
				String md5 = md5(numero);

				if (md5.equals(codigo)) {

					out.writeObject("encontrado");
					out.writeObject(numero);
					out.flush();

					System.out.println("O código " + codigo + " é produzido pelo número " + numero);
					tempo = System.currentTimeMillis() - tempo;
					System.out.println("O programa levou " + tempo + "ms para encontrar esse número.");
				}
				i++; // incrementa numero a ser testado
			}
			System.out.println("Finalizando faixa -->Inicio: " + rangeInicio + "; Fim: " + rangeFim);
		}

		// area para finalizar treads
	}

	public static String md5(String entrada) throws NoSuchAlgorithmException {
		MessageDigest sha1 = MessageDigest.getInstance("MD5");
		byte[] saida = sha1.digest(entrada.getBytes());
		StringBuilder saidaStr = new StringBuilder();
		for (byte b : saida)
			saidaStr.append(String.format("%02x", b));
		return saidaStr.toString();
	}

	private Thread iniciarEscutaServer(ObjectInputStream input) {
		Thread retorno = new Thread() {
			public void run() {
				try {
					while (true) {
						String msg = (String) input.readObject();
						if (msg.equals("encontrado")) {
							lock.lock();

							System.out.println("Interrompido");
							encontrado = true;

							lock.unlock();
						} else {
							lock.lock();

							encontrado = false;
							String array[] = new String[3];
							array = msg.split(";");
							rangeInicio = Integer.parseInt(array[0]);
							rangeFim = Integer.parseInt(array[1]);
							codigo = array[2];

							lock.unlock();
						}
						lock.lock();
						liberaTrabalho.signal();
						lock.unlock();
					}
				} catch (Exception e) {
					System.out.println("Finalizando CLiente pelo Exception");
					// e.printStackTrace();
					System.exit(0);
				}
			}
		};
		retorno.start();
		return retorno;
	}
}
