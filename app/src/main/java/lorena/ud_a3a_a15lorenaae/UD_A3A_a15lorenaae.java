package lorena.ud_a3a_a15lorenaae;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;


public class UD_A3A_a15lorenaae extends Activity {
Button botonempezarThread;

    Button botonpararThread;
    private final int tempo_crono=20;
    private final Object signal=new Object();
    private volatile  boolean paused;
    private static final int tempo_final = 20;
    private miñatarefa cronometro;
    TextView texto;
    private Button botonempezarAsync;
    TextView texto2;
    Message msg;
    Message msgFin;



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
    private class ClassPonte extends Handler{
            private WeakReference<UD_A3A_a15lorenaae>mTarget=null;
            ClassPonte(UD_A3A_a15lorenaae target) {
                mTarget = new WeakReference<UD_A3A_a15lorenaae>(target);
            }
            public void handleMessage(Message msg){
                UD_A3A_a15lorenaae target=mTarget.get();
                texto2=(TextView)target.findViewById(R.id.textosegundo);

                if((msg.arg2==1)) {
                    //texto2.setText(String.valueOf(msg.arg1));

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
                    msg=new Message();
                    msg.arg1= a;
                    ponte.sendMessage(msg);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


           msgFin=new Message();
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
    private class miñatarefa extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            for (int a = tempo_final; a >=0; a--) {
                try {
                    Thread.sleep(1000);
                    msg = new Message();
                    msg.arg1 = a;
                    ponte.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isCancelled())
                    break;
                msgFin = new Message();
                msgFin.arg2 = 1;
                ponte.sendMessage(msgFin);
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
                   texto.setText("" + numerosaleatorios());


                } else {
                     texto.setText(""+numerosaleatorios());
                }

            }
        });
        Button botoncancelar = (Button) findViewById(R.id.botonpararAsyncTask);
        botoncancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((cronometro == null) || (cronometro.getStatus() == AsyncTask.Status.FINISHED)) {
                    Toast.makeText(getApplicationContext(), "Pulse boton Start", Toast.LENGTH_LONG).show();

                }

                else {
                    cronometro.cancel(true);


                    if (texto.getText().toString().equals(texto2.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Coincide no valor " + texto2.getText().toString(), Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getApplicationContext(), "Non coinciden, porque o valor é:" + texto2.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud__a3_a_a15lorenaae);
        texto=(TextView)findViewById(R.id.textoprimero);
        botonpararThread=(Button)findViewById(R.id.botonpararthread);
        botonempezarThread=(Button)findViewById(R.id.botonempezarthread);
        XestionarEventos();
        xestionareventosparar();
        XestionarEventosAsync();




    }
}
