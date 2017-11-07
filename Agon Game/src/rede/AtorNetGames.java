package rede;

import javax.swing.JOptionPane;

import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;
import control.Jogo;
import view.AtorJogador;

public class AtorNetGames implements OuvidorProxy {

	private Proxy proxy;
	private boolean ehMinhaVez = false;
	private AtorJogador atorJogador;
	
	public AtorNetGames(AtorJogador atorJogador) {
		super();
		proxy = proxy.getInstance();
		proxy.addOuvinte(this);
		this.atorJogador = atorJogador;
	}

	@Override
	public void iniciarNovaPartida(Integer posicao) {
		
		if (posicao == 1){
			System.out.println(posicao);
			ehMinhaVez = true;
		}else if (posicao == 2){
			System.out.println(posicao);
			ehMinhaVez = false;
		}
		
		atorJogador.iniciarPartidaRede(ehMinhaVez);

	}

	@Override
	public void finalizarPartidaComErro(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receberMensagem(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receberJogada(Jogada jogada) {
		
		Movimento movimento = (Movimento) jogada;
		atorJogador.receberMovimentoRede(movimento);
		
	}

	@Override
	public void tratarConexaoPerdida() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {
		// TODO Auto-generated method stub

	}

	public void conectar(String nome, String ip) {
		try {
			proxy.conectar(ip, nome);
		} catch (JahConectadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NaoPossivelConectarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArquivoMultiplayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void desconectar() {
		try {
			proxy.desconectar();
		} catch (NaoConectadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void iniciarPartidaRede() {
		try {
			proxy.iniciarPartida(2);
		} catch (NaoConectadoException e) {
			JOptionPane.showMessageDialog(atorJogador.getFrmAgon(), e.getMessage());
			e.printStackTrace();
		}
		
	}

	public String obterNomeAdversario() {
		String nome = "";
		if (ehMinhaVez) {
			nome = proxy.obterNomeAdversario(2);
		} else {
			nome = proxy.obterNomeAdversario(1);
		}
		return nome;
	}

	public void enviarJogada(Movimento movimento) {
		try {
			System.out.println("Enviar Jogada");
			proxy.enviaJogada(movimento);
			ehMinhaVez = false;
		} catch (NaoJogandoException e) {
			JOptionPane.showMessageDialog(atorJogador.getFrmAgon(), e.getMessage());
			System.out.println("Erro no enviar jogada");
		}
	}

}
