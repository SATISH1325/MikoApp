package com.professional.mikoapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.professional.mikoapp.model.DataModel;

import java.util.List;

public class MikoRepository {

    private MikoDao mikoDao;

    private MikoAppDatabase mikoAppDatabase;

    public MikoRepository(Application application) {
        mikoAppDatabase = MikoAppDatabase.getInstance(application);
        mikoDao = mikoAppDatabase.mikoDao();
    }

    public void insert(Miko miko) {
        new MikoAsyncTask(mikoDao, "insert").execute(miko);
    }

    public void update(Miko miko) {
        new MikoAsyncTask(mikoDao, "update").execute(miko);
    }

    public void delete(Miko miko) {
        new MikoAsyncTask(mikoDao, "delete").execute(miko);
    }

    public LiveData<List<Miko>> getAllRecords() {
        return mikoDao.getRecords();
    }

    public void isRowIsExist(List<DataModel.Record> recordList) {
        new IsRecordExistsAsyncTask(mikoDao).execute(recordList);
    }

    private static class MikoAsyncTask extends AsyncTask<Miko, String, Void> {

        private MikoDao mikoDao;
        private String typeOfAction;

        private MikoAsyncTask(MikoDao mikoDao, String typeOfAction) {
            this.mikoDao = mikoDao;
            this.typeOfAction = typeOfAction;
        }

        @Override
        protected Void doInBackground(Miko... mikos) {

            switch (typeOfAction) {
                case "insert": {
                    mikoDao.insert(mikos[0]);
                    break;
                }

                case "update": {
                    mikoDao.update(mikos[0]);
                    break;
                }

                case "delete": {
                    mikoDao.delete(mikos[0]);
                    break;
                }

            }

            return null;
        }
    }

    private static class IsRecordExistsAsyncTask extends AsyncTask<List<DataModel.Record>, Void, Void> {

        private MikoDao mikoDao;

        private IsRecordExistsAsyncTask(MikoDao mikoDao) {
            this.mikoDao = mikoDao;
        }

        @Override
        protected Void doInBackground(List<DataModel.Record>... recordList) {
            if (recordList[0] != null && recordList[0].size() > 0) {
                for (int i = 0; i < recordList[0].size(); i++) {

                    Miko miko = new Miko();
                    miko.setTitle(recordList[0].get(i).title);
                    miko.setShortDescription(recordList[0].get(i).shortDescription);
                    miko.setCollectedValue(recordList[0].get(i).collectedValue);
                    miko.setTotalValue(recordList[0].get(i).totalValue);
                    miko.setStartDate(recordList[0].get(i).startDate);
                    miko.setEndDate(recordList[0].get(i).endDate);
                    miko.setMainImageURL(recordList[0].get(i).mainImageURL);

                    if (mikoDao.isRowIsExist(recordList[0].get(i).id)) {
                        mikoDao.update(miko);
                    } else {
                        mikoDao.insert(miko);
                    }

                }
            }
            return null;
        }
    }
}
