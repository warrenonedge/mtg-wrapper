package io.nomad.mtg.mtgwrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.api.SetAPI;
import io.magicthegathering.javasdk.resource.Card;

/**
 * Hello world!
 *
 */
public class MTGWrapper {

	private static final Logger logger = LogManager.getLogger(MTGWrapper.class);
	private static final String NAME_OPT = "-name";
	private static final String SET_OPT = "-set";

	public static void main(String[] args) {
		List<String> argsList = new ArrayList<String>();
		Map<String, String> optsMap = new HashMap<String, String>();
		List<String> doubleOptsList = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
			case '-':
				if (args[i].length() < 2)
					throw new IllegalArgumentException("Not a valid argument: " + args[i]);
				if (args[i].charAt(1) == '-') {
					if (args[i].length() < 3)
						throw new IllegalArgumentException("Not a valid argument: " + args[i]);
					// --opt
					doubleOptsList.add(args[i].substring(2, args[i].length()));
				} else {
					if (args.length - 1 == i)
						throw new IllegalArgumentException("Expected arg after: " + args[i]);
					// -opt
					optsMap.put(args[i], args[i + 1]);
					i++;
				}
				break;
			default:
				// arg
				argsList.add(args[i]);
				break;
			}
		}

		logger.debug("optsMap: " + optsMap);

		if (!optsMap.containsKey(NAME_OPT)) {
			throw new IllegalArgumentException("Name of Card Required to obtain information");
		}

		ArrayList<String> filter = new ArrayList<String>();
		filter.add("name=\"" + optsMap.get(NAME_OPT) + "\"");
		if (optsMap.containsKey(SET_OPT)) {
			String setName = SetAPI.getSet(optsMap.get(SET_OPT)).getName();

			logger.debug("setName: " + setName);
			filter.add("set=" + optsMap.get(SET_OPT));
		}

		List<Card> cardsWithName = CardAPI.getAllCards(filter);

		Gson gson = new Gson();
		String jsonInString = gson.toJson(cardsWithName);

		System.out.println(jsonInString);
	}
}
