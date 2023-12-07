package mx.paradigmmasters.bastafakeserver;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Listener extends Thread{
    private ObjectInputStream entrada;
    public Listener(ObjectInputStream entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                String respuestas = (String) entrada.readObject();
                System.out.println(respuestas);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
