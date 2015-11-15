package com.humanity.vs.cards.cardsvshumanity.logic.repositories;

import android.content.Context;
import android.os.RecoverySystem;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonParse_AllCards;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonParse_Card;

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
    // // FIXME: 15.11.15 too long loading time. serialize and save after first parse?
    public static List<Card> getAllCards(Context context, @Nullable RecoverySystem.ProgressListener progressCallback) {

        long startTime = System.currentTimeMillis();

        List<Card> cards = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(R.raw.json_cards);

        double allBytes = -1;
        double loadedBytes = -1;

        Writer writer = new StringWriter();
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        try {
            allBytes = is.available();

            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);

                loadedBytes += bufferSize;

                if (progressCallback != null) {
                    // first half of work. factor 100 * (1/2)
                    double progress = (loadedBytes / allBytes) * 50;
                    progressCallback.onProgress((int) progress);
                }
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

        double allCards = jsonParse_allCards.masterCards.length;
        double parsedCards = 0;
        for (JsonParse_Card jsonParseCard : jsonParse_allCards.masterCards) {
            Card c = new Card();
            c.id = jsonParseCard.id;
            c.isBlackCard = jsonParseCard.cardType.equals("Q");
            c.answersCount = jsonParseCard.numAnswers;
            c.text = Html.fromHtml(jsonParseCard.text).toString();

            cards.add(c);

            parsedCards++;
            if (progressCallback != null) {
                // second half of work
                double progress = 50 + (parsedCards / allCards) * 50;
                progressCallback.onProgress((int) progress);
            }
        }

        long endTime = System.currentTimeMillis();

        Log.d(App.TAG, "cards loading time (ms): " + String.valueOf(endTime - startTime));

        return cards;
    }
}
