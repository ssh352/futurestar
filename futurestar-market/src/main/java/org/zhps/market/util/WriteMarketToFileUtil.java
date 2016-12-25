package org.zhps.market.util;

import org.zhps.hjctp.api.MdApi;
import org.zhps.hjctp.spi.MdSpi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/12/16.
 */
public class WriteMarketToFileUtil {
    private static BufferedWriter bufWriter;
    private static MdSpi mdSpi;

    public static void registerMdSpi(MdSpi mdSpi){
        WriteMarketToFileUtil.mdSpi = mdSpi;
        initBufWriter();
    }

    public static void initBufWriter(){
        try {
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter("d:\\market\\market-20161226.txt", true));
            mdSpi.onResetBufferWriter(bufWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startTimer(final MdApi mdApi){
        Long interval = 24 * 60 * 60 * 1000l;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    bufWriter = new BufferedWriter(new FileWriter("d:\\market\\market-" + mdApi.getTradingDay() + ".txt", true));
                    mdSpi.onResetBufferWriter(bufWriter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        if(date.before(new Date())){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            date = cal.getTime();
        }
        Timer timer = new Timer();
        timer.schedule(timerTask, date, interval);
    }
}
