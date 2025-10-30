package velha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JogoDaVelha {
    private char[][] tabuleiro;
    private char jogadorAtual;
    private boolean jogoAtivo;
    private JButton[][] botoes;
    private JLabel statusLabel;
    private JFrame frame;

    public JogoDaVelha() {
        // Configurações da janela
        frame = new JFrame("Jogo da Velha");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        // Inicializa arrays
        tabuleiro = new char[3][3];
        botoes = new JButton[3][3];
        
        // Configura layout
        frame.setLayout(new BorderLayout());
        
        // Cria interface
        criarInterface();
        
        // Inicia jogo
        reiniciarJogo();
    }
    
    private void criarInterface() {
        // Painel do tabuleiro
        JPanel tabuleiroPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        tabuleiroPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Cria botões
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoes[i][j] = new JButton("");
                botoes[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                botoes[i][j].setFocusPainted(false);
                
                final int linha = i;
                final int coluna = j;
                
                botoes[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fazerJogada(linha, coluna);
                    }
                });
                
                tabuleiroPanel.add(botoes[i][j]);
            }
        }
        
        // Status e botão reiniciar
        statusLabel = new JLabel("Vez do: X", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton reiniciarBtn = new JButton("Reiniciar Jogo");
        reiniciarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reiniciarJogo();
            }
        });
        
        // Painel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(reiniciarBtn, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Adiciona à janela
        frame.add(tabuleiroPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void reiniciarJogo() {
        // Limpa tabuleiro
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = ' ';
                botoes[i][j].setText("");
                botoes[i][j].setEnabled(true);
                botoes[i][j].setBackground(Color.WHITE);
            }
        }
        
        jogadorAtual = 'X';
        jogoAtivo = true;
        statusLabel.setText("Vez do: X");
    }
    
    private void fazerJogada(int linha, int coluna) {
        if (!jogoAtivo || tabuleiro[linha][coluna] != ' ') {
            return;
        }
        
        // Faz jogada
        tabuleiro[linha][coluna] = jogadorAtual;
        botoes[linha][coluna].setText(String.valueOf(jogadorAtual));
        botoes[linha][coluna].setEnabled(false);
        
        // Verifica fim do jogo
        if (verificarVitoria()) {
            jogoAtivo = false;
            statusLabel.setText("Jogador " + jogadorAtual + " venceu!");
            JOptionPane.showMessageDialog(frame, "Jogador " + jogadorAtual + " venceu!");
        } else if (verificarEmpate()) {
            jogoAtivo = false;
            statusLabel.setText("Empate!");
            JOptionPane.showMessageDialog(frame, "Empate!");
        } else {
            // Próximo jogador
            jogadorAtual = (jogadorAtual == 'X') ? 'O' : 'X';
            statusLabel.setText("Vez do: " + jogadorAtual);
        }
    }
    
    private boolean verificarVitoria() {
        // Verifica linhas, colunas e diagonais
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] != ' ' && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                return true;
            }
            if (tabuleiro[0][i] != ' ' && tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
                return true;
            }
        }
        
        // Diagonais
        if (tabuleiro[0][0] != ' ' && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return true;
        }
        if (tabuleiro[0][2] != ' ' && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return true;
        }
        
        return false;
    }
    
    private boolean verificarEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        // Cria e mostra o jogo
        JogoDaVelha jogo = new JogoDaVelha();
        jogo.frame.setVisible(true);
    }
}