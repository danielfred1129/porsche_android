package com.pos.porschetower.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.pos.porschetower.MainApplication;
import com.pos.porschetower.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CustomCall<T> implements Callback<T> {

    private Activity context;
    private ProgressDialog progressDlg;


    public CustomCall(Activity activity) {
        this.context = activity;
        progressDlg = new ProgressDialog(context);
        progressDlg.setCancelable(false);
        progressDlg.setCanceledOnTouchOutside(false);
        progressDlg.setMessage("Loading...");
        if (progressDlg != null)
            progressDlg.show();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        hideProgess();
        handleResponse(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        handleFailure(call, t);
    }

    public void hideProgess() {
        if (progressDlg != null && progressDlg.isShowing())
            progressDlg.dismiss();
    }

    public JSONObject convertResponseToJson(Response<ResponseBody> responsebody) {
        JSONObject response = null;
        try {
            response = new JSONObject(new String(responsebody.body().bytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    ;

    public abstract void handleResponse(Call<T> call, Response<T> responsebody);

    public void handleFailure(Call<T> call, Throwable t){
        hideProgess();
        Toast.makeText(context, context.getString(R.string.api_error), Toast.LENGTH_SHORT).show();
    };
}
