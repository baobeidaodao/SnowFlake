package com.baobeidaodao.algorithm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DaoDao
 */
public class SnowFlakeUtil {

    public static Map<String, Object> decrypt(Long id) {
        Map<String, Object> map = new HashMap<>(1 << 4);
        long millisecond = id >> (SnowFlake.DATA_CENTER_ID_BITS + SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS);
        long dataCenterId = (id - (millisecond << (SnowFlake.DATA_CENTER_ID_BITS + SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS))) >> (SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS);
        long workerId = (id - (millisecond << (SnowFlake.DATA_CENTER_ID_BITS + SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS)) - (dataCenterId << (SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS))) >> SnowFlake.SEQUENCE_BITS;
        long sequence = (id - (millisecond << (SnowFlake.DATA_CENTER_ID_BITS + SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS)) - (dataCenterId << (SnowFlake.WORKER_ID_BITS + SnowFlake.SEQUENCE_BITS)) - (workerId << SnowFlake.SEQUENCE_BITS));
        long timestamp = SnowFlake.START_TIME_STAMP + millisecond;
        Date date = new Date();
        date.setTime(timestamp);
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String dateTime = dateTimeFormatter.format(localDateTime);
        map.put("id", id);
        map.put("startTimestamp", SnowFlake.START_TIME_STAMP);
        map.put("millisecond", millisecond);
        map.put("dataCenterId", dataCenterId);
        map.put("workerId", workerId);
        map.put("sequence", sequence);
        map.put("timestamp", timestamp);
        map.put("date", date);
        map.put("localDateTime", localDateTime);
        map.put("dateTime", dateTime);
        return map;
    }

}
