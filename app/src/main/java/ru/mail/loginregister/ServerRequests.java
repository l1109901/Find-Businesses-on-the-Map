package ru.mail.loginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ru.mail.loginregister.siniflar.Education;
import ru.mail.loginregister.siniflar.Firma;
import ru.mail.loginregister.siniflar.Randevu;
import ru.mail.loginregister.siniflar.User;

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT=1000*15;
    public static final String SERVER_ADDRESS="http://ec2-54-210-140-139.compute-1.amazonaws.com/project1/";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("İşlem sürüyor");
        progressDialog.setMessage("Bekleyiniz...");
    }

    public void storeUserDataInBackground(User user,GetConnectionCallBack connectionCallBack){
        progressDialog.show();
        new StoreUserDataAsyncTask(user,connectionCallBack).execute();
    }

    public void fetchUserDataInBackground(User user,GetUserCallback callback){
        progressDialog.show();
        new FetchUserDataAsyncTask(user,callback).execute();
    }

    public void storeEducationDataInBackground(Education education,long tcno){
        progressDialog.show();
        new StoreEducationDataAsyncTask(education,tcno).execute();
    }

    public void storeRandevuDataInBackground(long tcno1,long tcno2,String firma_adi,String tarih,String saat){
        progressDialog.show();
        new StoreRandevuDataAsyncTask(tcno1,tcno2,firma_adi,tarih,saat).execute();
    }

    public void storeFirmaDataInBackground(Firma firma){
        progressDialog.show();
        new StoreFirmaDataAsyncTask(firma).execute();
    }

    public void fetchLocationDataInBackground(GetFirmCallBack callBack){
        progressDialog.show();
        new FetchLocationDataAsyncTask(callBack).execute();
    }

    public void fetchOnaylananLocationDataInBackground(long tcno,GetFirmCallBack callBack){
        progressDialog.show();
        new FetchOnaylananLocationDataAsyncTask(tcno,callBack).execute();
    }

    public void fetchAlanInBackground(Double lat,Double lon,GetAlanCallBack callback){
        progressDialog.show();
        new FetchAlanDataAsyncTask(lat,lon,callback).execute();
    }

    public void fetchRandevuInBackground(long tcno,GetRandevularCallBack callback){
        progressDialog.show();
        new FetchRandevuDataAsyncTask(tcno,callback).execute();
    }

    public void fetchRandevuCalisanInBackground(long tcno,GetRandevularCallBack callback){
        progressDialog.show();
        new FetchRandevuCalisanDataAsyncTask(tcno,callback).execute();
    }

    public void fetchEgitimBilgisiCalisanInBackground(long tcno,GetEducationCallBack callback){
        progressDialog.show();
        new FetchEgitimBilgisiCalisanDataAsyncTask(tcno,callback).execute();
    }

    public void deleteFromRandevuInBackground(int id, GetConnectionCallBack connectionCallBack){
        progressDialog.show();
        new DeleteRandevuDataAsyncTask(id,connectionCallBack).execute();
    }

    public void onaylaInBackground(int id, GetConnectionCallBack connectionCallBack){
        progressDialog.show();
        new onaylaDataAsyncTask(id,connectionCallBack).execute();
    }

    public class onaylaDataAsyncTask extends AsyncTask<String,Void,String>{

        int id;
        GetConnectionCallBack connectionCallBack;

        public onaylaDataAsyncTask(int id,GetConnectionCallBack connectionCallBack){
            this.id=id;
            this.connectionCallBack=connectionCallBack;
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id",id+""));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"onaylaRandevu.php");

            String jsonResult = "";
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response=client.execute(post);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            connectionCallBack.done(result);
        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }

    public class DeleteRandevuDataAsyncTask extends AsyncTask<String,Void,String>{

        int id;
        GetConnectionCallBack connectionCallBack;

        public DeleteRandevuDataAsyncTask(int id,GetConnectionCallBack connectionCallBack){
            this.id=id;
            this.connectionCallBack=connectionCallBack;
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id",id+""));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"deleteRandevu.php");

            String jsonResult = "";
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response=client.execute(post);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            connectionCallBack.done(result);
        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }

    public class StoreUserDataAsyncTask extends AsyncTask<String,Void,String>{

        User user;
        GetConnectionCallBack connectionCallBack;

        public StoreUserDataAsyncTask(User user,GetConnectionCallBack connectionCallBack){
            this.user=user;
            this.connectionCallBack=connectionCallBack;
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("ad",user.getAd()));
            dataToSend.add(new BasicNameValuePair("soyad",user.getSoyad()));
            dataToSend.add(new BasicNameValuePair("tc_no",user.getTc_no()+""));
            dataToSend.add(new BasicNameValuePair("email",user.getEmail()));
            dataToSend.add(new BasicNameValuePair("tel",user.getTel()));
            dataToSend.add(new BasicNameValuePair("kim",user.getId()+""));
            dataToSend.add(new BasicNameValuePair("kullanici_adi", user.getKullaniciAdi()));
            dataToSend.add(new BasicNameValuePair("sifre", user.getParola1()));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Register.php");

            String jsonResult = "";
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse response=client.execute(post);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            connectionCallBack.done(result);
        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }


    public class FetchUserDataAsyncTask extends AsyncTask<Void,Void,User>{

        User user;
        GetUserCallback userCallback;

        public FetchUserDataAsyncTask(User user,GetUserCallback userCallback){
            this.user=user;
            this.userCallback=userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("kullanici_adi",user.getKullaniciAdi()));
            dataToSend.add(new BasicNameValuePair("sifre", user.getParola1()));

            System.out.println(user.getKullaniciAdi());
            System.out.println(user.getParola1());

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"FetchUserData.php");

            User returnedUser=null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                System.out.println(result);
                JSONObject jsonObject=new JSONObject(result);

                if(jsonObject.length()==0){
                    returnedUser=null;
                }else{
                    String ad=jsonObject.getString("ad");
                    String soyad=jsonObject.getString("soyad");
                    long tc_no=jsonObject.getLong("tc_no");
                    String email=jsonObject.getString("email");
                    String tel=jsonObject.getString("tel");
                    int id=jsonObject.getInt("kim");

                    returnedUser=new User(ad,soyad,tc_no,email,tel,id,user.getKullaniciAdi(),user.getParola1());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    public class FetchRandevuDataAsyncTask extends AsyncTask<Void,Void,ArrayList<Randevu>>{

        long tcno;
        GetRandevularCallBack callback;

        public FetchRandevuDataAsyncTask(long tcno,GetRandevularCallBack callback){
            this.tcno=tcno;
            this.callback=callback;
        }

        @Override
        protected ArrayList<Randevu> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("tcno", tcno + ""));//alici tcno

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"FetchRandevuData.php");

            ArrayList<Randevu> msj=null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                System.out.println(result);
                msj=new ArrayList<Randevu>();
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id=jsonObject.getInt("id");
                    String firma_adi=jsonObject.getString("firma_adi");
                    long tc_no=jsonObject.getLong("tcno");
                    String isim=jsonObject.getString("ad");
                    String soyad=jsonObject.getString("soyad");
                    String tarih=jsonObject.getString("tarih");
                    String saat=jsonObject.getString("saat");
                    int durum=jsonObject.getInt("durum");
                    Randevu rv=new Randevu(id,firma_adi,tc_no,isim,soyad,tarih,saat,durum);
                    msj.add(rv);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return msj;
        }

        @Override
        protected void onPostExecute(ArrayList<Randevu> msj){
            progressDialog.dismiss();
            callback.done(msj);
            super.onPostExecute(msj);
        }
    }

    public class FetchRandevuCalisanDataAsyncTask extends AsyncTask<Void,Void,ArrayList<Randevu>>{

        long tcno;
        GetRandevularCallBack callback;

        public FetchRandevuCalisanDataAsyncTask(long tcno,GetRandevularCallBack callback){
            this.tcno=tcno;
            this.callback=callback;
        }

        @Override
        protected ArrayList<Randevu> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("tcno", tcno + ""));//alici tcno

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"FetchRandevuCalisan.php");

            ArrayList<Randevu> msj=null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                System.out.println(result);
                msj=new ArrayList<Randevu>();
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id=jsonObject.getInt("id");
                    String firma_adi=jsonObject.getString("firma_adi");
                    long tc_no=jsonObject.getLong("tcno");
                    String isim=jsonObject.getString("ad");
                    String soyad=jsonObject.getString("soyad");
                    String tarih=jsonObject.getString("tarih");
                    String saat=jsonObject.getString("saat");
                    int durum=jsonObject.getInt("durum");
                    Randevu rv=new Randevu(id,firma_adi,tc_no,isim,soyad,tarih,saat,durum);
                    msj.add(rv);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return msj;
        }

        @Override
        protected void onPostExecute(ArrayList<Randevu> msj){
            progressDialog.dismiss();
            callback.done(msj);
            super.onPostExecute(msj);
        }
    }

    public class FetchEgitimBilgisiCalisanDataAsyncTask extends AsyncTask<Void,Void,ArrayList<Education>>{

        long tcno;
        GetEducationCallBack callback;

        public FetchEgitimBilgisiCalisanDataAsyncTask(long tcno,GetEducationCallBack callback){
            this.tcno=tcno;
            this.callback=callback;
        }

        @Override
        protected ArrayList<Education> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("tcno", tcno + ""));//alici tcno

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"FetchEgitimBilgisiCalisan.php");

            ArrayList<Education> edus=null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                System.out.println(result);
                edus=new ArrayList<Education>();
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String okul_adi=jsonObject.getString("okul_adi");
                    String bolum=jsonObject.getString("bolum");
                    int yil=jsonObject.getInt("mezuniyet_yili");
                    Education edu=new Education(okul_adi,bolum,yil);
                    edus.add(edu);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return edus;
        }

        @Override
        protected void onPostExecute(ArrayList<Education> edus){
            progressDialog.dismiss();
            callback.done(edus);
            super.onPostExecute(edus);
        }
    }

    public class StoreEducationDataAsyncTask extends AsyncTask<Void,Void,Void> {

        long tcno;
        Education education;

        public StoreEducationDataAsyncTask(Education education,long tcno){
            this.education=education;
            this.tcno=tcno;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("okul_adi",education.getOkul_adi()));
            dataToSend.add(new BasicNameValuePair("bolum", education.getBolum()));
            dataToSend.add(new BasicNameValuePair("mezuniyet_yili", (education.getMezuniyet_yili() + "")));
            dataToSend.add(new BasicNameValuePair("tc_no", (tcno + "")));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Education.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    public class StoreRandevuDataAsyncTask extends AsyncTask<Void,Void,Void> {

        long tcno1,tcno2;
        String firma_adi,tarih,saat;

        public StoreRandevuDataAsyncTask(long tcno1,long tcno2,String firma_adi,String tarih,String saat){
            this.tcno1=tcno1;
            this.tcno2=tcno2;
            this.firma_adi=firma_adi;
            this.tarih=tarih;
            this.saat=saat;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("tcno1",tcno1+""));
            dataToSend.add(new BasicNameValuePair("tcno2", tcno2+""));
            dataToSend.add(new BasicNameValuePair("firma_adi",firma_adi));
            dataToSend.add(new BasicNameValuePair("tarih", tarih));
            dataToSend.add(new BasicNameValuePair("saat", saat));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Randevu.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    public class StoreFirmaDataAsyncTask extends AsyncTask<Void,Void,Void> {

        Firma firma;

        public StoreFirmaDataAsyncTask(Firma firma){
            this.firma=firma;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("firma_adi",firma.getFirmaAdi()));
            dataToSend.add(new BasicNameValuePair("il",firma.getIl()));
            dataToSend.add(new BasicNameValuePair("ilce",firma.getIlce()));
            dataToSend.add(new BasicNameValuePair("alan",firma.getAlan()));
            dataToSend.add(new BasicNameValuePair("latitude", firma.getLatitude()+""));
            dataToSend.add(new BasicNameValuePair("longtitude", firma.getLongtitude()+""));
            dataToSend.add(new BasicNameValuePair("tc_no", firma.getTcno()+""));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Firma.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    public class FetchLocationDataAsyncTask extends AsyncTask<Void,Void,ArrayList<Firma>> {

        GetFirmCallBack callback;

        public FetchLocationDataAsyncTask(GetFirmCallBack callback) {
            this.callback = callback;
        }

        @Override
        protected ArrayList<Firma> doInBackground(Void... params) {

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SelectLatLon.php");

            ArrayList<Firma> firmas=null;
            try {
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result;
                result = EntityUtils.toString(entity);
                System.out.println(result);
                firmas=new ArrayList<Firma>();
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Long tc_no=jsonObject.getLong("tc_no");
                    String firma_adi=jsonObject.getString("firma_adi");
                    String il=jsonObject.getString("il");
                    String ilce=jsonObject.getString("ilce");
                    String alan=jsonObject.getString("alan");
                    Double lat=jsonObject.getDouble("lat");
                    Double lon=jsonObject.getDouble("lon");
                    Firma frm=new Firma(tc_no,firma_adi,il,ilce,alan,lat,lon);
                    firmas.add(frm);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return firmas;
        }

        @Override
        protected void onPostExecute(ArrayList<Firma> firmas){
            progressDialog.dismiss();
            callback.done(firmas);
            super.onPostExecute(firmas);
        }
    }

    public class FetchOnaylananLocationDataAsyncTask extends AsyncTask<Void,Void,ArrayList<Firma>> {

        GetFirmCallBack callback;
        long tcno;

        public FetchOnaylananLocationDataAsyncTask(long tcno,GetFirmCallBack callback) {
            this.callback = callback;
            this.tcno=tcno;
        }

        @Override
        protected ArrayList<Firma> doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("tcno", tcno + ""));//alici tcno

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "selectOnaylanan.php");

            ArrayList<Firma> firmas=null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result;
                result = EntityUtils.toString(entity);
                System.out.println(result);
                firmas=new ArrayList<Firma>();
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Long tc_no=jsonObject.getLong("tc_no");
                    String firma_adi=jsonObject.getString("firma_adi");
                    String il=jsonObject.getString("il");
                    String ilce=jsonObject.getString("ilce");
                    String alan=jsonObject.getString("alan");
                    Double lat=jsonObject.getDouble("lat");
                    Double lon=jsonObject.getDouble("lon");
                    String tarih=jsonObject.getString("tarih");
                    String saat=jsonObject.getString("saat");
                    Firma frm=new Firma(tc_no,firma_adi,il,ilce,alan,lat,lon,tarih,saat);
                    firmas.add(frm);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return firmas;
        }

        @Override
        protected void onPostExecute(ArrayList<Firma> firmas){
            progressDialog.dismiss();
            callback.done(firmas);
            super.onPostExecute(firmas);
        }
    }

    public class FetchAlanDataAsyncTask extends AsyncTask<Void,Void,Firma>{

        Double lat,lon;

        GetAlanCallBack callBack;

        public FetchAlanDataAsyncTask(Double lat,Double lon,GetAlanCallBack callback){
            this.lat=lat;
            this.lon=lon;
            this.callBack=callback;
        }

        @Override
        protected Firma doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("lat", lat + ""));
            dataToSend.add(new BasicNameValuePair("lon", lon + ""));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"FetchAlanData.php");

            Firma firma=null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                System.out.println(result);

                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                long tcno=jsonObject.getLong("tc_no");
                String alan=jsonObject.getString("alan");
                firma=new Firma(tcno,alan);
            }catch(Exception e){
                e.printStackTrace();
            }
            return firma;
        }

        @Override
        protected void onPostExecute(Firma firma) {
            progressDialog.dismiss();
            callBack.done(firma);
            super.onPostExecute(firma);
        }
    }
}