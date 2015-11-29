package com.reputasi.library.manager;

import android.os.AsyncTask;

/**
 * Created by vikraa on 6/27/2015.
 */
abstract class BaseManager<T1, T2> {

    private T2 t2;

    /*private AsyncTask<Integer, T1, T2> mAsyncTask = new AsyncTask<Integer, T1, T2>() {

        @Override
        protected void onPreExecute() {
            onPreBackgroundTask();
        }

        @Override
        protected T2 doInBackground(Integer... params) {
            return onBackgroundTask(params);
        }

        @Override
        protected void onPostExecute(T2 t2) {
            onPostBackgroundTask(t2);
        }

    };*/

    protected T2 onBackgroundTask(Integer... params) {
        return t2;
    };

    protected void onPreBackgroundTask() {

    }

    protected void onPostBackgroundTask(T2 t2) {

    }

    protected boolean startBackgroundTask(int id) {

        new AsyncTask<Integer, T1, T2>() {
            @Override
            protected void onPreExecute() {
                onPreBackgroundTask();
            }

            @Override
            protected T2 doInBackground(Integer... params) {
                return onBackgroundTask(params);
            }

            @Override
            protected void onPostExecute(T2 t2) {
                onPostBackgroundTask(t2);
            }
        }.execute(id);

        /*if (asyncTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            return false;
        }

        asyncTask.execute(id);*/
        return true;
    }
}
