package control;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import model.Jogador;
import model.Pecinha;
import model.Posicao;
import model.Rainha;
import model.Tabuleiro;
import rede.AtorNetGames;
import rede.Movimento;
import view.AtorJogador;

public class Jogo {
	
	private Boolean partidaEmAndamento;
	
	private Jogador jogador1;
	private Jogador jogador2;
	
	private Jogador jogadorDaVez;
	
	private AtorNetGames atorNetGames;
	private AtorJogador atorJogador;
	

	public void criarParticipante(String nome) {
		if (jogador1 == null) {
			jogador1 = new Jogador(nome, "preto");
			jogador1.tomaVez();
		} else if (jogador2 == null) {
			jogador2 = new Jogador(nome, "vermelho");
			jogador2.passaVez();
		}	
	}

	public void movePecinha(Posicao deBtn, Posicao paBtn) {
		
		if (jogador1.getVez()) {
			Pecinha pecinha = deBtn.removePecinha();
			paBtn.setPecinha(pecinha);
		} else if (jogador2.getVez()) {
			Pecinha pecinha = deBtn.removePecinha();
			paBtn.setPecinha(pecinha);
		}
		
		
	}
	
	public void inserePecinha(Pecinha pecinha, Posicao paBtn) {
		if (jogador1.getVez()) {
			paBtn.setPecinha(pecinha);
		} else if (jogador2.getVez()) {
			paBtn.setPecinha(pecinha);
		}
		
	}

	public Pecinha getGuerreiroDoJogador() {
		if (jogador1.getVez()) {
			return jogador1.getUmGuerreiro();
		} else if (jogador2.getVez()) {
			return jogador2.getUmGuerreiro();
		}
		return null;
	}

	public Pecinha getRainhaDoJogador() {
		if (jogador1.getVez()) {
			return jogador1.getUmRainha();
		} else if (jogador2.getVez()) {
			return jogador2.getUmRainha();
		}
		return null;
	}
	
	public void tratarMovimento(Movimento movimento) {
		if (movimento.getDeBtn() == null) {
			inserePecinha(movimento.getPecinha(), movimento.getPaBtn());
			passaVez();
		} else if (movimento.getPecinha() == null) {
			movePecinha(movimento.getDeBtn(), movimento.getPaBtn());
			passaVez();
		}
	}
	
	
	private void passaVez() {
		if (jogador1.getVez()) {
			jogador1.passaVez();
			jogador2.tomaVez();
		} else if (jogador2.getVez()) {
			jogador1.passaVez();
			jogador1.tomaVez();
		}
	}

	// get and set 
	
	public Jogador getJogador1() {
		return jogador1;
	}

	public Jogador getJogador2() {
		return jogador2;
	}

	public void tratarRecebeMovimento(Movimento movimento) {
		Pecinha p1 = jogador1.getUmGuerreiro();
		
		atorJogador.getTabuleiro().getPosicoes().get(1).setPecinha(p1);
		
		System.out.println("setou");
	}

	

	
	

}
