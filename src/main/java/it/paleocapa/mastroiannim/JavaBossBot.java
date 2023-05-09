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
		
		LinkedList<String> listaBere = new LinkedList<String>();
		listaBere.add("acquaNaturale");
		listaBere.add("cocaCola");
		listaBere.add("ThePesca");

		//boolean statoOrdinazione = false;

		LinkedList<String> listaOrdinazioni = new LinkedList<String>();
		listaOrdinazioni.add("ciao");
		if(messaggio.equals("/listaCibo")){
			invioMessaggio.setText(
				"listaCibo: \n paneCotoletta $2 \n focacciaProsciutto $2.50 \n panzerotto $1.50" 
			);
		}
		else if(messaggio.equals("/listaBere")){
			invioMessaggio.setText(
				"listaBere: \n acquaNaturale $1 \n cocaCola $2 \n ThePesca $1.50" 
			);
		}
		/* 
		else if(messaggio.equals("/Ordinazioni")){
			invioMessaggio.setText(
				"scrivi tutti i prodotti di cui hai bisogno..."
			);
			listaOrdinazioni.add(messaggio);
			
			Iterator<String> iteratoreCibo = listaCibo.iterator();
			Iterator<String> iteratoreBere = listaBere.iterator();
			while (iteratoreCibo.hasNext()) {
				String element = iteratoreCibo.next();
				if (element.equals(messaggio)) {
					listaOrdinazioni.add(messaggio);
				}
				
				else{
					invioMessaggio.setText("prodotto non disponibile...");
				}
				
			}
			while (iteratoreBere.hasNext()) {
				String element = iteratoreBere.next();
				if (element.equals(messaggio)) {
					listaOrdinazioni.add(messaggio);
				}
				/* 
				else{
					invioMessaggio.setText("prodotto non disponibile...");
				}
				
			}
			
		}
		*/
		else if(messaggio.equals("/stampaOrdinazioni")){
			if(listaOrdinazioni.size() <= 0){
				invioMessaggio.setText("lista vuota");
			}
			Iterator<String> iterator = listaCibo.iterator();
			StringBuilder stringBuilder = new StringBuilder();
			while (iterator.hasNext()) {
				String element = iterator.next();
				stringBuilder.append(element);
				if (iterator.hasNext()) {
					stringBuilder.append(", ");
				}
			}
			LOG.info(stringBuilder.toString());
		}
		
		

		try {
			execute(invioMessaggio);
		} catch (TelegramApiException e) {
			LOG.error(e.getMessage());
		}
	}
		
}

