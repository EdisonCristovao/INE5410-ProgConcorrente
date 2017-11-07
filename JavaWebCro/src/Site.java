import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Site implements Runnable {
	String url;

	public Site(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		List<String> urls = pegaLinks();
		String nomeArq = this.url.replaceAll("http://", "");
		try {
			escreveEmArquivo(nomeArq, urls);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String downloadHtml() {
		String html = "";
		try {
			html = Jsoup.connect("http://cco.inf.ufsc.br/").get().html();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}

	public List<String> pegaLinks() {
		String html = downloadHtml();
		List<String> result = new ArrayList<String>();
		Document docs = Jsoup.parse(html);
		Elements links = docs.select("a[href]");
		for (Element link : links) {
			String url = link.attr("abs:href");
			if (url.length() > 0 && url.startsWith("http")) {
				result.add(url);
				criaThread(url);
			}
		}
		return result;

	}

	public void criaThread(String url) {
		Site s1 = new Site(url);
		Thread t1 = new Thread(s1);
		t1.start();
	}

	public void escreveEmArquivo(String nomeArq, List<String> urls) throws IOException {
		FileWriter arq = new FileWriter("/home/joker/Modelos/workspace/java/JavaWebCro/sites/" + nomeArq);

		PrintWriter gravarArq = new PrintWriter(arq);
		gravarArq.printf(url + " >> " + urls.size() + "\n");
		for (int i = 0; i < urls.size(); i++) {
			gravarArq.printf(urls.get(i) + "\n");
		}
		arq.close();

	}

}
