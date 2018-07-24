package com.an9elkiss.api.manager.wechart.service;

import com.an9elkiss.api.manager.wechart.model.WeChartMessage;

import net.sf.json.JSONObject;

public interface SendTextMessageService{

    JSONObject sendMessage(WeChartMessage wechartMessage);
}
