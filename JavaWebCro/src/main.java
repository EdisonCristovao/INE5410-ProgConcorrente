
public class main {

	public static void main(String[] args) {
		Site s1 = new Site("http://cco.inf.ufsc.br/");
		Thread t1 = new Thread(s1);
		t1.start();
		
	}

}
