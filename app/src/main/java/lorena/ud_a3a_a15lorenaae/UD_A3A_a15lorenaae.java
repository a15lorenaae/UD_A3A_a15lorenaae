package lorena.ud_a3a_a15lorenaae;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;


public class UD_A3A_a15lorenaae extends Activity {
Button botonempezarThread;
    Button botonpararThread;
    private final int tempo_crono=20;
    private final Object signal=new Object();
    private volatile  boolean paused;
    TextView texto3;
    private static final int tempo_final = 20;
    private miñatarefa cronometro;
    TextView texto;
    private Button botonempezarAsync;


    static TextView texto2;
    private boolean timetoQuit=false;
public void setPaused(){
    paused=true;
}
    public void setUnPaused(){
        paused=false;
        synchronized (signal) {
            signal.notify();

        }
    }



    //INICIO DA CLASE HANDLER
    private static class ClassPonte extends Handler{
        private WeakReference<UD_A3A_a15lorenaae>mTarget=null;
        ClassPonte(UD_A3A_a15lorenaae target) {
            mTarget = new WeakReference<UD_A3A_a15lorenaae>(target);
        }
            public void handleMessage(Message msg){
                UD_A3A_a15lorenaae target=mTarget.get();
                texto2=(TextView)target.findViewById(R.id.texto2);
                if(msg.arg2==1) {
                    Toast.makeText(target.getApplicationContext(), "ACABOUSE O CRONO", Toast.LENGTH_LONG).show();
                    texto2.setText(String.valueOf(msg.arg1));
                }
                else{
                    texto2.setText(String.valueOf(msg.arg1));


            }
        }



    };
    private ClassPonte ponte=new ClassPonte(this);
    private class MeuFio extends Thread  {
        public void run() {

            for (int a = tempo_crono; a >= 0; a--) {
                try {


                   while(paused){
                        synchronized (signal){
                            signal.wait();

                            }

                    }
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.arg1 = a;
                    ponte.sendMessage(msg);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            Message msgFin = new Message();
            msgFin.arg2 = 1;
            ponte.sendMessage(msgFin);
        }
    };

        private Thread meufio=new Thread();

    private void XestionarEventos() {
        botonempezarThread.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {

                if ((meufio == null) || (!meufio.isAlive())) {
                    Toast.makeText(getApplicationContext(), "INICIANDO FIO", Toast.LENGTH_LONG).show();
                    meufio = new MeuFio();
                    meufio.start();
                    texto.setText("" + numerosaleatorios());
                } else {
                    texto.setText("" + numerosaleatorios());

                }
                if(paused){
                    setUnPaused();
                    Toast.makeText(getApplicationContext(), "RENAUDANDO FIO", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void xestionareventosparar() {
        botonpararThread.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!paused){
                   setPaused();
                   if(texto.getText().toString().equals(texto2.getText().toString()))
                       Toast.makeText(getApplicationContext(),"Coincide no valor "+texto2.getText().toString(),Toast.LENGTH_LONG).show();
                   else{
                       Toast.makeText(getApplicationContext(), "Non coinciden, porque o valor é:" + texto2.getText().toString(), Toast.LENGTH_LONG).show();
                   }

                  try {
                       meufio.sleep(1);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }


        });

    }
        public static int numerosaleatorios(){
        int numeroaleatorio= (int) (Math.random()*((10-5)+1))+5;
        return numeroaleatorio;

    }



    private class miñatarefa extends AsyncTask<Void, Integer, Boolean> implements lorena.ud_a3a_a15lorenaae.miñatarefa {
        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = tempo_final; i >=0; i--) {
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
                if (isCancelled())
                    break;
            }
            return true;
        }

        protected void onProgessUpdate(Integer... values){

        }

        protected void onPreExecute(){

        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(), "Tarefa finalizada", Toast.LENGTH_LONG).show();
            }

        }

        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Tarefa Cancelada!", Toast.LENGTH_LONG).show();
        }
    };

    private void XestionarEventosAsync() {
        botonempezarAsync= (Button) findViewById(R.id.botonempezarAsyncTask);
        botonempezarAsync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((cronometro == null) || (cronometro.getStatus() == AsyncTask.Status.FINISHED)) {
                    cronometro = new miñatarefa();
                    cronometro.execute();
                    Toast.makeText(getApplicationContext(),"Tarefa Iniciada",Toast.LENGTH_LONG).show();
                    texto3.setText("" + numerosaleatorios());


                } else {
                     texto3.setText(""+numerosaleatorios());
                }

            }
        });
        Button botoncancelar = (Button) findViewById(R.id.botonpararAsyncTask);
        botoncancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cronometro.getStatus() == AsyncTask.Status.RUNNING) {
                    cronometro.cancel(true);
                }
            }
        });
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud__a3_a_a15lorenaae);
        texto=(TextView)findViewById(R.id.texto);
        botonpararThread=(Button)findViewById(R.id.botonpararthread);
        botonempezarThread=(Button)findViewById(R.id.botonempezarthread);
        texto3=(TextView)findViewById(R.id.texto3);
        XestionarEventos();
        xestionareventosparar();
        XestionarEventosAsync();
        texto=(TextView)findViewById(R.id.texto);



    }
}
