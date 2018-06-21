package com.github.liangyunfeng.leakcanary.customizing;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;

/**
 * Created by yunfeng.l on 2018/6/21.
 */

public class LeakUploadService extends DisplayLeakService {
    @Override protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        if (!result.leakFound || result.excludedLeak) {
            return;
        }
        /**
         * upload file to server here.
          */
        //myServer.uploadLeakBlocking(heapDump.heapDumpFile, leakInfo);
    }
}
