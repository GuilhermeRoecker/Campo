package br.com.roecker.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    private final int linha;
    private final int coluna;

    private boolean mindado = false;
    private boolean aberto = false;
    private boolean marcado = false;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObeservador> obeservadores = new ArrayList<>();
    // private List<BiConsumer<Campo, CampoEvento >> obeservadores2 = new
    // ArrayList<>(); Pode ser usado no lugar da interface

    public Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObeservador obeservador) {
        obeservadores.add(obeservador);
    }

    private void notificarObservadores(CampoEvento evento) {
        obeservadores.forEach(o -> o.eventoOcoreu(this, evento));
    }

    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        }
        if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    public void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;

            if (marcado) {
                notificarObservadores(CampoEvento.MARCAR);
            } else {
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir() {
        if (!aberto && !marcado) {

            if (mindado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);

            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean vizinhancaSegura() {
        return vizinhos.stream().noneMatch(v -> v.mindado);
    }

    void minar() {
        mindado = true;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public Boolean isAberto() {
        return aberto;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;

        if (aberto) {
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    public Boolean isFechado() {
        return !aberto;
    }

    public Boolean isMinado() {
        return mindado;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    boolean objetivoAlcançado() {
        boolean desvendado = !mindado && aberto;
        boolean protejido = mindado && marcado;
        return desvendado || protejido;
    }

    public int minasNaVizinhança() {
        return (int) vizinhos.stream().filter(v -> v.mindado).count();
    }

    void reiniciar() {
        aberto = false;
        mindado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }
}
