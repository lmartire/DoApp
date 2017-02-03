![Alt text](https://github.com/lmartire/DoApp/blob/master/icon_complete.png)
<br>
DoApp is an Android standalone application that makes it possible perform a deep test of a target application.
Analysing the manifest of the target application, DoApp is able to stress each component (Activities, Services and BroadcastReceivers)
of the application. 
<br>Through fuzzing and an ad-hoc heuristic, DoApp generates a set of malformed inputs in order to test if the application
is crash-proof. Once the test is completed, DoApp produces a report that allows to individuate cause of fault in the target application.
