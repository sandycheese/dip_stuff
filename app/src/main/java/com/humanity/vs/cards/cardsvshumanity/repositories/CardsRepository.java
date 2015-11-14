package com.humanity.vs.cards.cardsvshumanity.repositories;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonParse_AllCards;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonParse_Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robot on 11.11.15.
 */
// todo errors & nulls
public class CardsRepository {
    public static List<Card> getAllCards(Context context) {
        List<Card> cards = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(R.raw.json_cards);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        JsonParse_AllCards jsonParse_allCards = new Gson().fromJson(jsonString, JsonParse_AllCards.class);

        for (JsonParse_Card jsonParseCard : jsonParse_allCards.masterCards) {
            Card c = new Card();
            c.id = jsonParseCard.id;
            c.isBlackCard = jsonParseCard.cardType.equals("Q");
            c.answersCount = jsonParseCard.numAnswers;
            c.text = Html.fromHtml(jsonParseCard.text).toString();

            cards.add(c);
        }

        return cards;
    }
}
