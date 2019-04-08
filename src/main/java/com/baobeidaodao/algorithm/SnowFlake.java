package com.baobeidaodao.algorithm;

/**
 * 雪花算法
 * 参考 https://github.com/twitter-archive/snowflake
 * 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
 * 64 位二进制，刚好可以使用 long 型。转为 十进制，19 位
 * 第 0 位，表示 long 数字符号位
 * 第 1-41 位，表示时间
 * 第 42-46 位，表示数据中心
 * 第 47-51 位，表示机器
 * 第 52-63 位，表示序列
 *
 * @author DaoDao
 */
public class SnowFlake {

    /**
     * 起始的时间戳
     * 2019-01-01 00:00:00
     */
    public final static long START_TIME_STAMP = 1546272000000L;

    /**
     * 位数
     */
    public final static long BITS = 64L;
    public final static long SYMBOLIC_BITS = 1L;
    public final static long TIMESTAMP_BITS = 41L;
    public final static long DATA_CENTER_ID_BITS = 5L;
    public final static long WORKER_ID_BITS = 5L;
    public final static long SEQUENCE_BITS = 12L;

    /**
     * 最大值
     */
    private final static long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    /**
     * 位移
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIME_STAMP_LEFT_SHIFT = DATA_CENTER_ID_SHIFT + DATA_CENTER_ID_BITS;

    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 数据中心
     */
    private long dataCenterId;
    /**
     * 机器标识
     */
    private long workerId;
    /**
     * 序列号
     */
    private long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造方法
     *
     * @param dataCenterId 数据中心 id
     * @param workerId     机器标识 id
     */
    public SnowFlake(long dataCenterId, long workerId) {
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0L) {
            throw new IllegalArgumentException("data center Id can't be greater than " + MAX_DATA_CENTER_ID + " or less than 0");
        }
        if (workerId > MAX_WORKER_ID || workerId < 0L) {
            throw new IllegalArgumentException("worker Id can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 产生下一个ID
     *
     * @return id
     */
    public synchronized long nextId() {
        long timeStamp = timeGen();
        if (timeStamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timeStamp) + " milliseconds");
        }
        if (timeStamp == lastTimestamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1L) & SEQUENCE_MASK;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                timeStamp = tilNextMillis();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimestamp = timeStamp;

        return (timeStamp - START_TIME_STAMP) << TIME_STAMP_LEFT_SHIFT
                | dataCenterId << DATA_CENTER_ID_SHIFT
                | workerId << WORKER_ID_SHIFT
                | sequence;
    }

    private long tilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
