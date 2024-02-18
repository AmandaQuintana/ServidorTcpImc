/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amanda.imc.servidor;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import amanda.imc.vistas.VentanaPrincipal;



/**
 *
 * @author amanda
 */
public class ServidorTcp  extends Thread {

    private Boolean estado;
    public static Map<String, SubProcesoCliente> listaDeClientes;
    private Integer puerto = 9007;
    private ServerSocket servicio;
    private VentanaPrincipal ventana;

    public ServidorTcp(Integer puerto, VentanaPrincipal v) {
        if (puerto != null || puerto != 0) {
            this.puerto = puerto;
        }
        ventana = v;
        listaDeClientes = new HashMap<>();
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools |Templates.
        iniciarServicio();
    }

    public void iniciarServicio() {
        try {
            servicio = new ServerSocket(puerto);
            estado = true;
            ventana.getBtnIniciar().setText("DETENER");
            ventana.getTxtEstado().setText("ONLINE");
            ventana.getTxtEstado().setForeground(Color.green);
            ventana.getBtnIniciar().setForeground(Color.RED);
            String msg = log() + "Servidor disponible en el Puerto " + puerto;
            System.out.println(msg);
            ventana.getCajaLog().append(msg + "\n");
            while (estado) {
                Socket cliente = servicio.accept();
                String ip = cliente.getInetAddress().getHostAddress();
                msg = log() + "Cliente " + ip + " conectado";
                System.out.println(msg + "\n");
                ventana.getCajaLog().append(msg + "\n");
                SubProcesoCliente atencion = new SubProcesoCliente(cliente,
                        ventana);
                ServidorTcp.listaDeClientes.put(ip, atencion);
                atencion.start();
            }
        } catch (IOException ex) {
            String msg = log() + "ERROR al abrir el puerto " + puerto;
            System.out.println(msg);
            ventana.getCajaLog().append(msg + "\n");
            ventana.getBtnIniciar().setText("INICIAR");
            ventana.getTxtEstado().setText("OFF LINE");
        }
    }

    public void detenerServicio() {
        if (estado) {
            estado = false;
            ventana.getBtnIniciar().setText("INICIAR");
            ventana.getBtnIniciar().setForeground(Color.GREEN);
            ventana.getTxtEstado().setText("OFF LINE");
            ventana.getTxtEstado().setForeground(Color.RED);
            ServidorTcp.listaDeClientes.entrySet().stream().map(new Function<Map.Entry<String, SubProcesoCliente>, String>() {
                @Override
                public String apply(Map.Entry<String, SubProcesoCliente> elemento) {
                    String ip = elemento.getKey();
                    SubProcesoCliente cliente = elemento.getValue();
                    String msg = log() + "Desconectando cliente " + ip;
                    System.out.println(msg);
                    ventana.getCajaLog().append(msg + "\n");
                    try {
                        cliente.getCliente().close();
                        cliente = null;
                        ServidorTcp.listaDeClientes.remove(elemento);
                        msg = log() + "Cliente desconectado" + ip;
                        System.out.println(msg);
                        ventana.getCajaLog().append(msg + "\n");
                    } catch (IOException ex) {
                        cliente = null;
                        ServidorTcp.listaDeClientes.remove(elemento);
                        msg = log() + "Cliente desconectado" + ip;
                        System.out.println(msg);
                        ventana.getCajaLog().append(msg + "\n");
                    }
                    return ip;
                }
            }).forEachOrdered(ip -> {
                System.out.println("cliente " + ip + " Desconectado");
            });
            try {
                servicio.close();
            } catch (IOException ex) {
                System.out.println("ERRRO no se puede cerrar el Puerto "
                        + puerto);
                String msg = log() + "ERRRO no se puede cerrar el Puerto "
                        + puerto;
                System.out.println(msg);
                ventana.getCajaLog().append(msg + "\n");
            }
        }
    }

    public String log() {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        return f.format(new Date()) + " - ";
    }



}
