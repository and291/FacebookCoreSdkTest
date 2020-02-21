# FacebookSdkCoreTest

Incorrect network request handling example.

Preconditions:
- Mobile app is configured to use facebook-core sdk;
- Mobile phone is not signed in to network; (or bad network connection?);
- Mobile operator redirects any http(https?) request to it's own site.

Observed behavior:
- Facebook core sdk starts initializing (and occupies entire async task pool?);
- Any other async task (executed on the same pool?) will never be triggered.

Thread state from `traces.txt` (https://gist.github.com/and291/86d7c619204900aabc6df1369b72efa1)

> "AsyncTask #1" prio=5 tid=12 Native
  | group="main" sCount=1 dsCount=0 obj=0x12c76820 self=0xb4304e00
  | sysTid=25273 nice=0 cgrp=default sched=0/0 handle=0x9d100920
  | state=S schedstat=( 15845314 10992917 12 ) utm=1 stm=0 core=3 HZ=100
  | stack=0x9cffe000-0x9d000000 stackSize=1038KB
  | held mutexes=
  native: #00 pc 00048ed4  /system/lib/libc.so (__ppoll+20)
  native: #01 pc 0001ccc7  /system/lib/libc.so (poll+46)
  native: #02 pc 00012baf  /system/lib/libjavacrypto.so (???)
  native: #03 pc 0000e93b  /system/lib/libjavacrypto.so (???)
  native: #04 pc 0051343f  /data/dalvik-cache/arm/system@framework@boot.oat (Java_com_android_org_conscrypt_NativeCrypto_SSL_1do_1handshake__JLjava_io_FileDescriptor_2Lcom_android_org_conscrypt_NativeCrypto_00024SSLHandshakeCallbacks_2IZ_3B_3B+210)
  at com.android.org.conscrypt.NativeCrypto.SSL_do_handshake(Native method)
  at com.android.org.conscrypt.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:357)
  at com.android.okhttp.Connection.connectTls(Connection.java:235)
  at com.android.okhttp.Connection.connectSocket(Connection.java:199)
  at com.android.okhttp.Connection.connect(Connection.java:172)
  at com.android.okhttp.Connection.connectAndSetOwner(Connection.java:367)
  at com.android.okhttp.OkHttpClient$1.connectAndSetOwner(OkHttpClient.java:130)
  at com.android.okhttp.internal.http.HttpEngine.connect(HttpEngine.java:330)
  at com.android.okhttp.internal.http.HttpEngine.sendRequest(HttpEngine.java:247)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.execute(HttpURLConnectionImpl.java:457)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getResponse(HttpURLConnectionImpl.java:405)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getResponseCode(HttpURLConnectionImpl.java:521)
  at com.android.okhttp.internal.huc.DelegatingHttpsURLConnection.getResponseCode(DelegatingHttpsURLConnection.java:105)
  at com.android.okhttp.internal.huc.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:-1)
  at com.facebook.GraphResponse.fromHttpConnection(GraphResponse.java:264)
  at com.facebook.GraphRequest.executeConnectionAndWait(GraphRequest.java:1285)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1183)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1149)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1133)
  at com.facebook.GraphRequest.executeAndWait(GraphRequest.java:1108)
  at com.facebook.GraphRequest.executeAndWait(GraphRequest.java:1002)
  at com.facebook.internal.FetchedAppSettingsManager.getAppSettingsQueryResponse(FetchedAppSettingsManager.java:380)
  at com.facebook.internal.FetchedAppSettingsManager.queryAppSettings(FetchedAppSettingsManager.java:280)
  at com.facebook.UserSettingsManager$1.run(UserSettingsManager.java:155)
  at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1133)
  at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:607)
  at java.lang.Thread.run(Thread.java:761)

> "AsyncTask #2" prio=5 tid=13 Native
  | group="main" sCount=1 dsCount=0 obj=0x12c76940 self=0xb4305300
  | sysTid=25275 nice=0 cgrp=default sched=0/0 handle=0x9cffb920
  | state=S schedstat=( 11275155 30256406 38 ) utm=0 stm=1 core=2 HZ=100
  | stack=0x9cef9000-0x9cefb000 stackSize=1038KB
  | held mutexes=
  native: #00 pc 00048ed4  /system/lib/libc.so (__ppoll+20)
  native: #01 pc 0001ccc7  /system/lib/libc.so (poll+46)
  native: #02 pc 00012baf  /system/lib/libjavacrypto.so (???)
  native: #03 pc 0000e93b  /system/lib/libjavacrypto.so (???)
  native: #04 pc 0051343f  /data/dalvik-cache/arm/system@framework@boot.oat (Java_com_android_org_conscrypt_NativeCrypto_SSL_1do_1handshake__JLjava_io_FileDescriptor_2Lcom_android_org_conscrypt_NativeCrypto_00024SSLHandshakeCallbacks_2IZ_3B_3B+210)
  at com.android.org.conscrypt.NativeCrypto.SSL_do_handshake(Native method)
  at com.android.org.conscrypt.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:357)
  at com.android.okhttp.Connection.connectTls(Connection.java:235)
  at com.android.okhttp.Connection.connectSocket(Connection.java:199)
  at com.android.okhttp.Connection.connect(Connection.java:172)
  at com.android.okhttp.Connection.connectAndSetOwner(Connection.java:367)
  at com.android.okhttp.OkHttpClient$1.connectAndSetOwner(OkHttpClient.java:130)
  at com.android.okhttp.internal.http.HttpEngine.connect(HttpEngine.java:330)
  at com.android.okhttp.internal.http.HttpEngine.sendRequest(HttpEngine.java:247)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.execute(HttpURLConnectionImpl.java:457)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getResponse(HttpURLConnectionImpl.java:405)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getResponseCode(HttpURLConnectionImpl.java:521)
  at com.android.okhttp.internal.huc.DelegatingHttpsURLConnection.getResponseCode(DelegatingHttpsURLConnection.java:105)
  at com.android.okhttp.internal.huc.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:-1)
  at com.facebook.GraphResponse.fromHttpConnection(GraphResponse.java:264)
  at com.facebook.GraphRequest.executeConnectionAndWait(GraphRequest.java:1285)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1183)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1149)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1133)
  at com.facebook.GraphRequest.executeAndWait(GraphRequest.java:1108)
  at com.facebook.GraphRequest.executeAndWait(GraphRequest.java:1002)
  at com.facebook.internal.FetchedAppGateKeepersManager.getAppGateKeepersQueryResponse(FetchedAppGateKeepersManager.java:219)
  at com.facebook.internal.FetchedAppGateKeepersManager.access$000(FetchedAppGateKeepersManager.java:51)
  at com.facebook.internal.FetchedAppGateKeepersManager$1.run(FetchedAppGateKeepersManager.java:127)
  at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1133)
  at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:607)
  at java.lang.Thread.run(Thread.java:761)

> "AsyncTask #3" prio=5 tid=14 Native
  | group="main" sCount=1 dsCount=0 obj=0x12c769d0 self=0xb4305800
  | sysTid=25276 nice=0 cgrp=default sched=0/0 handle=0x9cef6920
  | state=S schedstat=( 10158281 20155209 30 ) utm=1 stm=0 core=0 HZ=100
  | stack=0x9cdf4000-0x9cdf6000 stackSize=1038KB
  | held mutexes=
  native: #00 pc 00048ed4  /system/lib/libc.so (__ppoll+20)
  native: #01 pc 0001ccc7  /system/lib/libc.so (poll+46)
  native: #02 pc 00012baf  /system/lib/libjavacrypto.so (???)
  native: #03 pc 0000e93b  /system/lib/libjavacrypto.so (???)
  native: #04 pc 0051343f  /data/dalvik-cache/arm/system@framework@boot.oat (Java_com_android_org_conscrypt_NativeCrypto_SSL_1do_1handshake__JLjava_io_FileDescriptor_2Lcom_android_org_conscrypt_NativeCrypto_00024SSLHandshakeCallbacks_2IZ_3B_3B+210)
  at com.android.org.conscrypt.NativeCrypto.SSL_do_handshake(Native method)
  at com.android.org.conscrypt.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:357)
  at com.android.okhttp.Connection.connectTls(Connection.java:235)
  at com.android.okhttp.Connection.connectSocket(Connection.java:199)
  at com.android.okhttp.Connection.connect(Connection.java:172)
  at com.android.okhttp.Connection.connectAndSetOwner(Connection.java:367)
  at com.android.okhttp.OkHttpClient$1.connectAndSetOwner(OkHttpClient.java:130)
  at com.android.okhttp.internal.http.HttpEngine.connect(HttpEngine.java:330)
  at com.android.okhttp.internal.http.HttpEngine.sendRequest(HttpEngine.java:247)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.execute(HttpURLConnectionImpl.java:457)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getResponse(HttpURLConnectionImpl.java:405)
  at com.android.okhttp.internal.huc.HttpURLConnectionImpl.getResponseCode(HttpURLConnectionImpl.java:521)
  at com.android.okhttp.internal.huc.DelegatingHttpsURLConnection.getResponseCode(DelegatingHttpsURLConnection.java:105)
  at com.android.okhttp.internal.huc.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:-1)
  at com.facebook.GraphResponse.fromHttpConnection(GraphResponse.java:264)
  at com.facebook.GraphRequest.executeConnectionAndWait(GraphRequest.java:1285)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1183)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1149)
  at com.facebook.GraphRequest.executeBatchAndWait(GraphRequest.java:1133)
  at com.facebook.GraphRequest.executeAndWait(GraphRequest.java:1108)
  at com.facebook.GraphRequest.executeAndWait(GraphRequest.java:1002)
  at com.facebook.internal.FetchedAppSettingsManager.getAppSettingsQueryResponse(FetchedAppSettingsManager.java:380)
  at com.facebook.internal.FetchedAppSettingsManager.access$100(FetchedAppSettingsManager.java:63)
  at com.facebook.internal.FetchedAppSettingsManager$1.run(FetchedAppSettingsManager.java:178)
  at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1133)
  at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:607)
  at java.lang.Thread.run(Thread.java:761)

![screenshot](https://github.com/and291/FacebookSdkCoreTest/blob/master/Screenshot_20200221-202129.png)
