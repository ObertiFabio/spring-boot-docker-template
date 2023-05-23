package it.paleocapa.mastroiannim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.*;
import java.util.*;

@Service
public class JavaBossBot extends TelegramLongPollingBot {

	private static final Logger LOG = LoggerFactory.getLogger(JavaBossBot.class);

	private String botUsername;
	private static String botToken;
	private static JavaBossBot instance;

	public static JavaBossBot getJavaBossBotInstance(String botUsername, String botToken){
		if(instance == null) {
			instance = new JavaBossBot();
			instance.botUsername = botUsername;
			JavaBossBot.botToken = botToken;
		}
		return instance;
	}

	private JavaBossBot(){
		super(botToken);
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
	
	@Override
	public String getBotUsername() {
		return botUsername;
	}

	LinkedList<String> listaOrdinazioni = new LinkedList<String>();
	
	@Override
	public void onUpdateReceived(Update update) {

		
		LOG.info("onUpdateReceived");
		String messaggio = update.getMessage().getText();
		String chatId = update.getMessage().getChatId().toString();
		SendMessage invioMessaggio = new SendMessage();
		invioMessaggio.setChatId(chatId);
	
		LinkedList<String> listaCibo = new LinkedList<String>();
		listaCibo.add("paneCotoletta");
		listaCibo.add("focacciaProsciutto");
		listaCibo.add("panzerotto");
		listaCibo.add("speck-brie");
		listaCibo.add("hamburger");
		listaCibo.add("calzone");


		LinkedList<String> listaBere = new LinkedList<String>();
		listaBere.add("acquaNaturale");
		listaBere.add("cocaCola");
		listaBere.add("ThePesca");

		if (messaggio.equals("/cibo")) {
			invioMessaggio.setText(
				"listaCibo: \n paneCotoletta $2 \n focacciaProsciutto $2.50 \n panzerotto $1.50 \n speck-brie $2.50 \n hamburger $2 \n calzone $2"
			);
		} else if (messaggio.equals("/bibite")) {
			invioMessaggio.setText(
				"listaBere: \n acquaNaturale $1 \n cocaCola $2 \n ThePesca $1.50"
			);
		} else if (messaggio.equals("/start")) {
			invioMessaggio.setText(
				"Buongiorno, come posso aiutarti?"
			);
		} else if (messaggio.equals("/ordinacibo")) {
			invioMessaggio.setText("scrivi tutti i prodotti di cui hai bisogno...");
		} else if (messaggio.equals("/pulisciordinazioni")) {
			listaOrdinazioni.clear();
			invioMessaggio.setText("lista pulita");
		}
		else if (messaggio.equals("/stampaordinazioni")) {
			if (listaOrdinazioni.size() <= 0) {
				invioMessaggio.setText("lista vuota");
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				for (String elemento : listaOrdinazioni) {
					stringBuilder.append(elemento).append(", ");
				}
				stringBuilder.setLength(stringBuilder.length() - 2); // Rimuove l'ultima virgola e lo spazio
				invioMessaggio.setText(stringBuilder.toString());
			}
		} else {
			boolean prodottoDisponibile = false;
	
			for (String elemento : listaCibo) {
				if (elemento.equals(messaggio)) {
					listaOrdinazioni.add(messaggio);
					invioMessaggio.setText("prodotto aggiunto");
					prodottoDisponibile = true;
					break;
				}
			}
	
			if (!prodottoDisponibile) {
				invioMessaggio.setText("prodotto non disponibile...");
			}
		}
	
		try {
			execute(invioMessaggio);
		} catch (TelegramApiException e) {
			LOG.error(e.getMessage());
		}
		
	}
		
}

