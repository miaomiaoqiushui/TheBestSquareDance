package com.liu.dance.util;

/**
 * Created by 舞动的心 on 2018/3/8.
 */

public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}

