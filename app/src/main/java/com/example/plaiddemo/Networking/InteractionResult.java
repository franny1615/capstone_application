package com.example.plaiddemo.Networking;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface InteractionResult {
    void createdLinkToken(String publicToken);
    void exchangedPublicToken(String accessToken, String itemId);
    void retrievedTransactions(JSONArray response, String itemId);
    void notifyError(Exception error);
}
