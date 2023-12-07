package mx.paradigmmasters.bastafakeserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Servidor {
    private static final int PUERTO = 5555;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor esperando conexiones en el puerto 5555...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado.");

                ClienteHandler clienteHandler = new ClienteHandler(clienteSocket);
                new Thread(clienteHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClienteHandler implements Runnable {

        private Socket clienteSocket;
        private ObjectInputStream entrada;
        private ObjectOutputStream salida;

        public ClienteHandler(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
            try {
                this.salida = new ObjectOutputStream(clienteSocket.getOutputStream());
                this.entrada = new ObjectInputStream(clienteSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String nombreUsuario = (String) entrada.readObject();
                System.out.println("Usuario '" + nombreUsuario + "' conectado.");

                int opt;


                do {
                    new Listener(this.entrada);
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Selecciona una opción");
                    System.out.println("1. Enviar estado LISTO");
                    System.out.println("2. Enviar estado LETRA");
                    System.out.println("3. Enviar estado FINALIZADO");
                    System.out.println("4. Enviar puntos aleatorios");
                    System.out.println("5. Enviar estado CARGANDO");
                    System.out.println("6. Salir");
                    opt = sc.nextInt();
                    switch (opt){
                        case 1:
                            System.out.println("Enviando estado LISTO...");
                            salida.writeObject(enviarListo());
                            System.out.println("Enviado");
                            break;
                        case 2:
                            System.out.println("Enviando estado LETRA...");
                            salida.writeObject(enviarLetra());
                            System.out.println("Enviado");
                            break;
                        case 3:
                            System.out.println("Enviando estado FINALIZADO...");
                            salida.writeObject(enviarFinalizado());
                            System.out.println("Enviado");
                            break;
                        case 4:
                            System.out.println("Enviando puntos...");
                            salida.writeObject(enviarPuntos());
                            System.out.println("Enviado");
                            break;
                        case 5:
                            System.out.println("Enviando estado CARGANDO...");
                            salida.writeObject(enviarCargando());
                            System.out.println("Enviado");
                            break;
                    }
                } while(opt != 6);



            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private String enviarLetra() {
            Random random = new Random();
            String json = "{Letra:{" +
                            "\"letra\": \"" + (char)(random.nextInt(26) + 'A') + "\"" +
                            "}}";
            System.out.println(json);
            return json;
        }

        private String enviarListo(){
            Random random = new Random();
            String json = "{Estado:{" +
                    "\"estado\": 2," +
                    "\"puntos\": " + random.nextInt(500)+100 +
                    "}}";
            System.out.println(json);
            return json;
        }

        private String enviarFinalizado(){
            Random random = new Random();
            String json = "{Estado:{" +
                    "\"estado\": 3," +
                    "\"puntos\": " + random.nextInt(500)+100 +
                    "}}";
            System.out.println(json);
            return json;
        }

        private String enviarCargando(){
            String json = "{Estado:{" +
                    "\"estado\": 1," +
                    "\"puntos\": " + 0 +
                    "}}";
            System.out.println(json);
            return json;
        }

        private String enviarPuntos(){
            int[] puntosPosibles = {0,25,50,100};
            Random random = new Random();
            String json = "{Calificacion:{" +
                    "\"nombre\": " + puntosPosibles[random.nextInt(4)] + "," +
                    "\"florFruto\": " + puntosPosibles[random.nextInt(4)] + "," +
                    "\"pais\": " + puntosPosibles[random.nextInt(4)] + "," +
                    "\"animal\": " + puntosPosibles[random.nextInt(4)] + "," +
                    "\"color\": " + puntosPosibles[random.nextInt(4)] +
                    "}}";
            System.out.println(json);
            return json;
        }
    }
}
