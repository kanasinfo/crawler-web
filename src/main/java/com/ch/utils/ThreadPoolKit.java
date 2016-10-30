package com.ch.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Devid on 2016/10/30.
 */
public final class ThreadPoolKit {
    private static ExecutorService fetchContentThread;

    /**
     * 创建下载内容线程池
     */
    public synchronized static ExecutorService getFetchContentThread(int threadCount) {
        if (fetchContentThread == null)
            fetchContentThread = Executors.newFixedThreadPool(threadCount);
        return fetchContentThread;
    }
}
