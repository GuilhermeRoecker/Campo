package br.com.roecker.cm.visao;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;

import br.com.roecker.cm.modelo.Tabuleiro;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        tabuleiro.paraCada(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {

            SwingUtilities.invokeLater(() -> {
                if (e.isGanhou()) {
                    JOptionPane.showMessageDialog(null, "Você Ganhou!!! :)");
                } else {
                    JOptionPane.showMessageDialog(null, "Você Perdeu!!! :(");
                }

                tabuleiro.reiniciar();
            });
        });
    }
}
