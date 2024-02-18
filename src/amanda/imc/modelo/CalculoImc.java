/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amanda.imc.modelo;

import java.io.Serializable;

/**
 *
 * @author amanda
 */
public class CalculoImc implements Serializable {

    private float peso;
    private float altura;

    public static class Imc {

        public float resultado;
        public String mensaje;
    }
    private Imc imc;

    public CalculoImc() {
    }

    public CalculoImc(float peso, float altura) {
        this.peso = peso;
        this.altura = altura;
    }

    public Imc getImc() {
        imc = new Imc();
        if (peso <= 0 || altura <= 0) {
            imc.mensaje = "ERROR: El peso y la altura deben ser mayores que 0";
            return imc;
        } else {
            imc.resultado = peso / (altura * altura);
            if (imc.resultado < 18.5) {
                imc.mensaje = "Debes consultar un Medico, tu peso es muy bajo";
            } else if (imc.resultado >= 18.5 && imc.resultado <= 24.9) {
                imc.mensaje = "Estas bien de peso";
            } else if (imc.resultado > 24.9 && imc.resultado <= 29.9) {
                imc.mensaje = "Debes bajar un poco de peso";
            } else {
                imc.mensaje = "Debes consultar un Medico, tu peso es muy alto";
            }
            return imc;
        }
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

}
