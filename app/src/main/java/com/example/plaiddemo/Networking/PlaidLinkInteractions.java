package com.example.plaiddemo.Networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaidLinkInteractions {
    private final String URL_PREFIX = "https://cap-backe.herokuapp.com";
    private final InteractionResult interactionResult;
    private final Context context;

    public PlaidLinkInteractions(InteractionResult IR, Context context) {
        this.interactionResult = IR;
        this.context = context;
    }

    public void createLinkToken() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = URL_PREFIX + "/api/create_link_token";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject res = new JSONObject(response);
                        interactionResult.createdLinkToken(res.getString("link_token"));
                    } catch (Exception e) {
                        interactionResult.notifyError(e);
                    }
                },
                Throwable::printStackTrace
        );
        queue.add(request);
    }

    public void exchangePublicToken(String publicToken) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = URL_PREFIX + "/api/set_access_token";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject res = new JSONObject(response);
                        interactionResult.exchangedPublicToken(res.getString("access_token"), res.getString("item_id"));
                    } catch (Exception e) {
                        interactionResult.notifyError(e);
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("public_token", publicToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void getTransactions(String accessToken, String itemId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = URL_PREFIX + "/api/transactions";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject res = new JSONObject(response);
                        interactionResult.retrievedTransactions(res.getJSONArray("transactions"), itemId);
                    } catch (Exception e) {
                        interactionResult.notifyError(e);
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", accessToken);
                return params;
            }
        };
        queue.add(request);
    }
}
