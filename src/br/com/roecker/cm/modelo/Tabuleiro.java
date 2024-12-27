package br.com.roecker.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObeservador {

    private final int linhas;
    private final int colunas;
    private final  int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }

    private void notificarObservadores(Boolean resultado) {
        observadores.forEach(o -> o.accept( new ResultadoEvento(resultado)));
    }


    public void abrir(int linha, int coluna){

            campos.parallelStream()
            .filter(c -> c.getLinha() == linha &&
             c.getColuna() == coluna)
            .findFirst()
            .ifPresent(c -> c.abrir());

        }

    public void alternarMarcacao(int linha, int coluna){
        campos.parallelStream()
        .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
        .findFirst()
        .ifPresent(c -> c.alternarMarcacao());
    }

    private void gerarCampos() {
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                Campo campo = new Campo(l, c);
                campo.registrarObservador(this);
                campos.add(campo);
            }
            
        }
    }

    private void associarVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado();

        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();   
        } while (minasArmadas < minas);
    }

    public boolean objetivoAlcançado(){
        return campos.stream().allMatch(c ->  c.objetivoAlcançado());
    }

    public void reiniciar(){
        campos.stream().forEach(c -> c.reiniciar());
        sortearMinas();
    }


    @Override
    public void eventoOcoreu(Campo campo, CampoEvento evento) {
        if(evento == CampoEvento.EXPLODIR){
            mostarMinas();
            notificarObservadores(false);
        }else if(objetivoAlcançado()){
            notificarObservadores(true);
        }
        
    }

    private void mostarMinas(){
        campos.stream()
        .filter(c -> c.isMinado())
        .filter(c -> !c.isMarcado())
        .forEach(c -> c.setAberto(true));
    }

    public int getLinhas() {
        return this.linhas;
    }


    public int getColunas() {
        return this.colunas;
    }
    
    public void paraCada(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

}
