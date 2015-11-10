package com.humanity.vs.cards.cardsvshumanity.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.managers.GameManager;

/**
 * Created by robot on 08.11.15.
 */
public class GameManagerHelper {

    public static JsonGameStage1Data getStage1DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage1Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage2Data getStage2DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage2Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage3Data getStage3DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage3Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage4Data getStage4DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage4Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage1Data getStage1Data(GameManager gameManager) {
        return null;
    }

    public static JsonGameStage1Data getStage2Data(GameManager gameManager) {
        return null;
    }

    public static JsonGameStage1Data getStage3Data(GameManager gameManager) {
        return null;
    }

    public static JsonGameStage1Data getStage4Data(GameManager gameManager) {
        return null;
    }
}
