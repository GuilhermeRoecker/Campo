package br.com.roecker.cm.modelo;

public class ResultadoEvento {

    private final boolean ganhou;


    public boolean isGanhou() {
        return this.ganhou;
    }
     

    public ResultadoEvento(boolean ganhou) {
        this.ganhou = ganhou;
    }

}
