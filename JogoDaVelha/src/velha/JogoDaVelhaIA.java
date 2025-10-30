package velha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class JogoDaVelhaIA {
    private char[][] tabuleiro;
    private char jogadorAtual;
    private boolean jogoAtivo;
    private boolean vsIA;
    private JButton[][] botoes;
    private JLabel statusLabel;
    private JFrame frame;
    private Random random;
    private JToggleButton modoButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JogoDaVelhaIA().iniciar();
        });
    }

    public JogoDaVelhaIA() {
        tabuleiro = new char[3][3];
        botoes = new JButton[3][3];
        random = new Random();
        vsIA = true; // Modo IA ativado por padrão
    }

    public void iniciar() {
        // Configura janela principal
        frame = new JFrame("🎮 Jogo da Velha com IA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        // Aplica tema escuro
        aplicarTema();
        
        criarInterface();
        reiniciarJogo();
        frame.setVisible(true);
    }

    private void aplicarTema() {
        // Cores modernas
        Color corFundo = new Color(45, 45, 65);
        Color corBotao = new Color(60, 60, 80);
        Color corTexto = new Color(220, 220, 220);
        Color corDestaque = new Color(86, 98, 246);
        
        frame.getContentPane().setBackground(corFundo);
        
        // Define cores padrão para componentes
        UIManager.put("Button.background", corBotao);
        UIManager.put("Button.foreground", corTexto);
        UIManager.put("Label.foreground", corTexto);
        UIManager.put("Panel.background", corFundo);
    }

    private void criarInterface() {
        // Cabeçalho
        JPanel headerPanel = criarHeader();
        
        // Tabuleiro
        JPanel panelTabuleiro = criarTabuleiro();
        
        // Painel de controle
        JPanel controlPanel = criarPainelControle();
        
        // Adiciona componentes à janela
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(panelTabuleiro, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(new Color(35, 35, 55));
        
        JLabel titulo = new JLabel("🎮 JOGO DA VELHA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        
        statusLabel = new JLabel("Sua vez (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(86, 98, 246));
        
        headerPanel.add(titulo, BorderLayout.NORTH);
        headerPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }

    private JPanel criarTabuleiro() {
        JPanel panelTabuleiro = new JPanel(new GridLayout(3, 3, 8, 8));
        panelTabuleiro.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panelTabuleiro.setBackground(new Color(45, 45, 65));

        Color corBotao = new Color(60, 60, 80);
        Color corTexto = Color.WHITE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoes[i][j] = new JButton("");
                botoes[i][j].setFont(new Font("Arial", Font.BOLD, 48));
                botoes[i][j].setBackground(corBotao);
                botoes[i][j].setForeground(corTexto);
                botoes[i][j].setFocusPainted(false);
                botoes[i][j].setBorder(BorderFactory.createLineBorder(new Color(86, 98, 246), 2));
                
                // Efeito hover
                botoes[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        JButton btn = (JButton) evt.getSource();
                        if (btn.isEnabled()) {
                            btn.setBackground(new Color(86, 98, 246));
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        JButton btn = (JButton) evt.getSource();
                        if (btn.isEnabled()) {
                            btn.setBackground(corBotao);
                        }
                    }
                });

                final int linha = i, coluna = j;
                botoes[i][j].addActionListener(e -> {
                    fazerJogadaHumano(linha, coluna);
                    if (jogoAtivo && vsIA && jogadorAtual == 'O') {
                        // IA joga depois do humano
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Thread.sleep(500); // Pequeno delay para parecer mais natural
                                fazerJogadaIA();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                });
                
                panelTabuleiro.add(botoes[i][j]);
            }
        }
        return panelTabuleiro;
    }

    private JPanel criarPainelControle() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        controlPanel.setBackground(new Color(45, 45, 65));

        // Botão Modo de Jogo
        modoButton = new JToggleButton("🔮 Modo: VS IA", vsIA);
        modoButton.setFont(new Font("Arial", Font.BOLD, 14));
        modoButton.setBackground(vsIA ? new Color(76, 175, 80) : new Color(244, 67, 54));
        modoButton.setForeground(Color.WHITE);
        modoButton.setFocusPainted(false);
        
        modoButton.addActionListener(e -> {
            vsIA = modoButton.isSelected();
            modoButton.setText(vsIA ? "🔮 Modo: VS IA" : "👥 Modo: 2 Jogadores");
            modoButton.setBackground(vsIA ? new Color(76, 175, 80) : new Color(244, 67, 54));
            reiniciarJogo();
        });

        // Botão Reiniciar
        JButton btnReiniciar = new JButton("🔄 Novo Jogo");
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnReiniciar.setBackground(new Color(33, 150, 243));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.addActionListener(e -> reiniciarJogo());

        // Botão Sair
        JButton btnSair = new JButton("🚪 Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.setBackground(new Color(158, 158, 158));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> System.exit(0));

        controlPanel.add(modoButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnReiniciar);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnSair);

        return controlPanel;
    }

    private void reiniciarJogo() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = ' ';
                botoes[i][j].setText("");
                botoes[i][j].setEnabled(true);
                botoes[i][j].setBackground(new Color(60, 60, 80));
            }
        }
        
        jogadorAtual = 'X';
        jogoAtivo = true;
        
        String modo = vsIA ? "VS IA" : "2 Jogadores";
        statusLabel.setText("Sua vez (X) - " + modo);
        
        // Se for modo IA e a IA começar (O)
        if (vsIA && jogadorAtual == 'O') {
            fazerJogadaIA();
        }
    }

    private void fazerJogadaHumano(int linha, int coluna) {
        if (!jogoAtivo || tabuleiro[linha][coluna] != ' ') return;

        executarJogada(linha, coluna);
    }

    private void fazerJogadaIA() {
        if (!jogoAtivo) return;

        // IA inteligente - tenta ganhar, depois bloqueia, depois joga aleatório
        int[] jogada = encontrarMelhorJogada();
        
        if (jogada != null) {
            // Pequeno delay para parecer que a IA está "pensando"
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executarJogada(jogada[0], jogada[1]);
        }
    }

    private int[] encontrarMelhorJogada() {
        // 1. Tenta vencer
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') {
                    tabuleiro[i][j] = 'O';
                    if (verificarVitoria()) {
                        tabuleiro[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    tabuleiro[i][j] = ' ';
                }
            }
        }

        // 2. Tenta bloquear o jogador
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') {
                    tabuleiro[i][j] = 'X';
                    if (verificarVitoria()) {
                        tabuleiro[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    tabuleiro[i][j] = ' ';
                }
            }
        }

        // 3. Joga no centro se disponível
        if (tabuleiro[1][1] == ' ') {
            return new int[]{1, 1};
        }

        // 4. Joga nos cantos
        int[][] cantos = {{0,0}, {0,2}, {2,0}, {2,2}};
        for (int[] canto : cantos) {
            if (tabuleiro[canto[0]][canto[1]] == ' ') {
                return canto;
            }
        }

        // 5. Joga em qualquer posição livre
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') {
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }

    private void executarJogada(int linha, int coluna) {
        tabuleiro[linha][coluna] = jogadorAtual;
        
        // Animação no botão
        botoes[linha][coluna].setText(String.valueOf(jogadorAtual));
        botoes[linha][coluna].setEnabled(false);
        
        // Cores diferentes para X e O
        if (jogadorAtual == 'X') {
            botoes[linha][coluna].setBackground(new Color(76, 175, 80)); // Verde para X
            botoes[linha][coluna].setForeground(Color.WHITE);
        } else {
            botoes[linha][coluna].setBackground(new Color(244, 67, 54)); // Vermelho para O
            botoes[linha][coluna].setForeground(Color.WHITE);
        }

        verificarFimDeJogo();
    }

    private void verificarFimDeJogo() {
        if (verificarVitoria()) {
            jogoAtivo = false;
            String vencedor = (jogadorAtual == 'X') ? "Você" : (vsIA ? "IA" : "Jogador O");
            statusLabel.setText(vencedor + " venceu! 🎉");
            
            // Mensagem personalizada
            String mensagem = vsIA && jogadorAtual == 'O' ? 
                "A IA venceu! Tente novamente! 🤖" : "Parabéns! Você venceu! 🏆";
            
            JOptionPane.showMessageDialog(frame, mensagem, "Fim de Jogo", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } else if (verificarEmpate()) {
            jogoAtivo = false;
            statusLabel.setText("Empate! 🤝");
            JOptionPane.showMessageDialog(frame, "Empate! Jogo interessante!", 
                "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Alterna jogador
            jogadorAtual = (jogadorAtual == 'X') ? 'O' : 'X';
            
            String textoStatus = "Sua vez (" + jogadorAtual + ")";
            if (vsIA && jogadorAtual == 'O') {
                textoStatus = "IA pensando...";
            }
            statusLabel.setText(textoStatus);
        }
    }

    private boolean verificarVitoria() {
        // Verifica linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] != ' ' && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) 
                return true;
        }
        // Verifica colunas
        for (int j = 0; j < 3; j++) {
            if (tabuleiro[0][j] != ' ' && tabuleiro[0][j] == tabuleiro[1][j] && tabuleiro[1][j] == tabuleiro[2][j]) 
                return true;
        }
        // Verifica diagonais
        if (tabuleiro[0][0] != ' ' && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) 
            return true;
        if (tabuleiro[0][2] != ' ' && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) 
            return true;
        
        return false;
    }

    private boolean verificarEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == ' ') return false;
            }
        }
        return true;
    }
}