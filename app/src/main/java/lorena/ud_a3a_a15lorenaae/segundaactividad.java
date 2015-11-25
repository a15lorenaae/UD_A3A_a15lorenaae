package lorena.ud_a3a_a15lorenaae;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Created by Lorena on 23/11/2015.
 */
public class segundaactividad extends Activity {
    private static final int tempo_final = 20;
    private miñatarefa cronometro;
    private ProgressBar barraprogreso;
    private Button botonempezarAsync;


    private class miñatarefa extends AsyncTask<Void, Integer, Boolean> implements lorena.ud_a3a_a15lorenaae.miñatarefa {
        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 0; i <= tempo_final; i++) {
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
            int progreso=values[0].intValue();
            barraprogreso.setProgress(progreso);
        }


        protected Integer doInBackground(String... params) {
            return null;
        }

        protected void onPreExecute(){
           barraprogreso.setProgress(0);
            barraprogreso.setMax(20);
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
        private void XestionarEventos() {
            botonempezarAsync= (Button) findViewById(R.id.botonempezarAsyncTask);
            botonempezarAsync.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((cronometro == null) || (cronometro.getStatus() == AsyncTask.Status.FINISHED)) {
                        cronometro = new miñatarefa();
                        cronometro.execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "A tarefa non acabou!!!", Toast.LENGTH_LONG).show();
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
        barraprogreso=(ProgressBar)findViewById(R.id.progressbar);
        XestionarEventos();


    }
    }



