package com.queerlab.chat.tencent.model.impl.room.impl;

import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.queerlab.chat.tencent.model.impl.base.TRTCLogger;
import com.queerlab.chat.tencent.model.impl.base.TXInviteData;
import com.queerlab.chat.tencent.model.impl.base.TXRoomInfo;
import com.queerlab.chat.tencent.model.impl.base.TXSeatInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IMProtocol {
    private static final String TAG = IMProtocol.class.getName();


    public static class Define {
        public static final String KEY_ATTR_VERSION   = "version";
        public static final String VALUE_ATTR_VERSION = "1.0";
        public static final String KEY_ROOM_INFO      = "roomInfo";
        public static final String KEY_SEAT           = "seat";

        public static final String KEY_CMD_VERSION   = "version";
        public static final String VALUE_CMD_VERSION = "1.0";
        public static final String KEY_CMD_ACTION    = "action";

        public static final String KEY_INVITATION_VERSION   = "version";
        public static final String VALUE_INVITATION_VERSION = "1.0";
        public static final String KEY_INVITATION_CMD       = "command";
        public static final String KEY_INVITATION_CONTENT   = "content";


        public static final int CODE_UNKNOWN      = 0;
        public static final int CODE_ROOM_DESTROY = 200;

        public static final int CODE_ROOM_CUSTOM_MSG = 301;
    }

    public static HashMap<String, String> getInitRoomMap(TXRoomInfo TXRoomInfo, List<TXSeatInfo> TXSeatInfoList) {
        Gson                    gson    = new Gson();
        HashMap<String, String> jsonMap = new HashMap<>();
        jsonMap.put(Define.KEY_ATTR_VERSION, Define.VALUE_ATTR_VERSION);
        jsonMap.put(Define.KEY_ROOM_INFO, gson.toJson(TXRoomInfo));
        for (int i = 0; i < TXSeatInfoList.size(); i++) {
            String json = gson.toJson(TXSeatInfoList.get(i), TXSeatInfo.class);
            jsonMap.put(Define.KEY_SEAT + i, json);
        }
        return jsonMap;
    }

    public static HashMap<String, String> getSeatInfoListJsonStr(List<TXSeatInfo> TXSeatInfoList) {
        Gson                    gson    = new Gson();
        HashMap<String, String> jsonMap = new HashMap<>();
        for (int i = 0; i < TXSeatInfoList.size(); i++) {
            String json = gson.toJson(TXSeatInfoList.get(i), TXSeatInfo.class);
            jsonMap.put(Define.KEY_SEAT + i, json);
        }
        return jsonMap;
    }

    public static HashMap<String, String> getSeatInfoJsonStr(int index, TXSeatInfo info) {
        Gson                    gson = new Gson();
        String                  json = gson.toJson(info, TXSeatInfo.class);
        HashMap<String, String> map  = new HashMap<>();
        map.put(Define.KEY_SEAT + index, json);
        return map;
    }

    public static TXRoomInfo getRoomInfoFromAttr(Map<String, String> map) {
        TXRoomInfo TXRoomInfo;
        Gson       gson = new Gson();
        String     json = map.get(Define.KEY_ROOM_INFO);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            TXRoomInfo = gson.fromJson(json, TXRoomInfo.class);
        } catch (Exception e) {
            TRTCLogger.e(TAG, "parse room info json error! " + json);
            TXRoomInfo = null;
        }
        return TXRoomInfo;
    }

    public static List<TXSeatInfo> getSeatListFromAttr(Map<String, String> map, int seatSize) {
        Gson             gson        = new Gson();
        List<TXSeatInfo> txSeatInfoList = new ArrayList<>();
        for (int i = 0; i < seatSize; i++) {
            String     json = map.get(Define.KEY_SEAT + i);
            TXSeatInfo txSeatInfo;
            if (TextUtils.isEmpty(json)) {
                txSeatInfo = new TXSeatInfo();
            } else {
                try {
                    txSeatInfo = gson.fromJson(json, TXSeatInfo.class);
                } catch (Exception e) {
                    TRTCLogger.e(TAG, "parse seat info json error! " + json);
                    txSeatInfo = new TXSeatInfo();
                }
            }
            txSeatInfoList.add(txSeatInfo);
        }
        return txSeatInfoList;
    }

    public static String getInvitationMsg(String roomId, String cmd, String content) {
        Gson         gson = new Gson();
        TXInviteData data = new TXInviteData();
        data.roomId = roomId;
        data.command = cmd;
        data.message = content;
        return gson.toJson(data, TXInviteData.class);
    }

    public static TXInviteData parseInvitationMsg(String json) {
        Gson         gson = new Gson();
        TXInviteData data;
        try {
            data = gson.fromJson(json, TXInviteData.class);
        } catch (Exception e) {
            return null;
        }
        return data;
    }

    public static String getRoomDestroyMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Define.KEY_CMD_VERSION, Define.VALUE_CMD_VERSION);
            jsonObject.put(Define.KEY_CMD_ACTION, Define.CODE_ROOM_DESTROY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getCusMsgJsonStr(String cmd, String msg) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Define.KEY_ATTR_VERSION, Define.VALUE_ATTR_VERSION);
            jsonObject.put(Define.KEY_CMD_ACTION, Define.CODE_ROOM_CUSTOM_MSG);
            jsonObject.put("command", cmd);
            jsonObject.put("message", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static Pair<String, String> parseCusMsg(JSONObject jsonObject) {
        String cmd     = jsonObject.optString("command");
        String message = jsonObject.optString("message");
        return new Pair<>(cmd, message);
    }
}
