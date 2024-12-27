package br.com.roecker.cm.visao;

import javax.swing.JFrame;

import br.com.roecker.cm.modelo.Tabuleiro;

public class TelaPrincipal extends JFrame {
    

    public TelaPrincipal() {
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);

        add(new PainelTabuleiro(tabuleiro));

        setTitle("Campo Minado");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


    public static void main(String[] args) {
        
        new TelaPrincipal();
    }
}
