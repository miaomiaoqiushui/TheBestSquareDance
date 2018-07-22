package com.liu.dance.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by 舞动的心 on 2018/3/8.
 */

public class ProgressRequestBody extends RequestBody {
    public static final int UPDATE = 0x01;
    private RequestBody requestBody;
    private ProgressListener mListener;
    private BufferedSink bufferedSink;
    private MyHandler myHandler;
    public ProgressRequestBody(RequestBody body, ProgressListener listener) {
        requestBody = body;
        mListener = listener;
        if (myHandler==null){
            myHandler = new MyHandler();
        }
    }

    class MyHandler extends Handler {

        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    if (mListener!=null)mListener.onProgress(progressModel.getCurrentBytes(),progressModel.getContentLength(),progressModel.isDone());
                    break;

            }
        }


    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        if (bufferedSink==null){
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //刷新
        bufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {

        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength==0){
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                //回调
                Message msg = Message.obtain();
                msg.what = UPDATE;
                msg.obj =  new ProgressModel(bytesWritten,contentLength,bytesWritten==contentLength);
                myHandler.sendMessage(msg);
            }
        };
    }


}
