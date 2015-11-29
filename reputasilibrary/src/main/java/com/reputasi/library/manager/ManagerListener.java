package com.reputasi.library.manager;

/**
 * Created by vikraa on 6/27/2015.
 */
public interface ManagerListener {
    void onPreBackgroundTask(int id);
    void onPostBackgroundTask(Object ob, int id);
    void onEvent(Object ob, int id);
    void onSuccess(Object ob, int id);
    void onFailed(Object ob, int id);
}
