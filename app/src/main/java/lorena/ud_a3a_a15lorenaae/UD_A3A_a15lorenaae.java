package lorena.ud_a3a_a15lorenaae;

import android.app.Activity;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.Objects;


public class UD_A3A_a15lorenaae extends Activity  {
Button botonempezarThread;
    Button botonpararThread;
    private final int tempo_crono=20;
    private final Object signal=new Object();
    private volatile  boolean paused;
    TextView texto;
    static TextView texto2;
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

                }
                else{

                   texto2.setText(String.valueOf(msg.arg1));


            }
        }



    };
    private ClassPonte ponte=new ClassPonte(this);
    private class MeuFio extends Thread {
        public void run() {

            for (int a = tempo_crono; a >= 0; a--) {
                try {
                    while(paused){
                        synchronized (signal){
                            signal.wait();

                            }

                    }
                    Thread.sleep(1500);
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
        private Thread meufio;

    private void XestionarEventos() {
        botonempezarThread.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                if ((meufio == null) || (!meufio.isAlive())) {
                    Toast.makeText(getApplicationContext(), "INICIANDO FIO", Toast.LENGTH_LONG).show();
                    meufio = new MeuFio();
                    meufio.start();
                } else {
                    texto.setText(""+numerosaleatorios());

                }
            }
        });
    }

    public void xestionareventosparar() {
        botonpararThread.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(paused){
                   setUnPaused();
               }
                else{
                    setPaused();
                   if(texto.getText().toString().equals(texto2.getText().toString()))
                       Toast.makeText(getApplicationContext(),"Coincide no valor "+texto2.getText().toString(),Toast.LENGTH_LONG).show();
                   else{
                       Toast.makeText(getApplicationContext(),"C")
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud__a3_a_a15lorenaae);
        texto=(TextView)findViewById(R.id.texto);
        botonpararThread=(Button)findViewById(R.id.botonpararthread);
        botonempezarThread=(Button)findViewById(R.id.botonempezarthread);
        XestionarEventos();
        xestionareventosparar();



    }
}
