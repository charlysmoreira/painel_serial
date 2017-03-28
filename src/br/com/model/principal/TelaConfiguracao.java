package br.com.model.principal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import br.com.model.utils.ConfigSerial;
import br.com.model.utils.MensagenUtils;

public class TelaConfiguracao extends JInternalFrame {

    private static final long serialVersionUID = -462368900778612993L;

    private JPanel jPanel = null;

    private JButton jButtomlLed = null;
    
    private JLabel jLabelInformacao = null;

    private ConfigSerial configSerial = null;

    private JButton jButton = null;


    public TelaConfiguracao() {
        super("Painel digital", false, true, false, true);
        this.setFrameIcon(new ImageIcon(this.getClass().getResource("/img/ledInternal.png")));
        configSerial = new ConfigSerial();
        this.pack();
        this.getContentPane().add(getJPanel());
    }

    /**
     * This method initializes jPanel   
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            LineBorder border = new LineBorder(Color.BLUE, 1, true);
            TitledBorder tborder = new TitledBorder(border, "Configuração", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.ITALIC, 12), Color.BLUE);
            jPanel.setBorder(tborder);
            jPanel.setLayout(null);
            jPanel.setSize(new Dimension(395, 263));
            jPanel.add(getJButton(), null);
            jPanel.add(getJButtomLeds(), null);
            jPanel.add(getJLabelInformacao(), null);
        }
        return jPanel;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelInformacao() {
        if (jLabelInformacao == null) {
            jLabelInformacao = new JLabel();
            jLabelInformacao.setBounds(new Rectangle(20, 50, 350, 25));
            jLabelInformacao.setFont(new Font("Arial", Font.ITALIC, 15));
            jLabelInformacao.setForeground(Color.BLUE);
        }
        return jLabelInformacao;
    }

    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(8, 80, 165, 34));
            jButton.setText("Configura Data/Hora");
            jButton.setIcon(new ImageIcon(getClass().getResource("/img/confDataHora.png")));
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    configSerial.configuraDataHora();
                    if (configSerial.confEnvio) {
                        jLabelInformacao.setText(MensagenUtils.CONFIGURADO_SUCESSO);
                    } else {
                        jLabelInformacao.setText(MensagenUtils.FALHA_DATA_HORA);
                    }
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JButton getJButtomLeds() {
        if (jButtomlLed == null) {
            jButtomlLed = new JButton();
            jButtomlLed.setBounds(new Rectangle(180, 80, 120, 34));
            jButtomlLed.setText("Verificar led");
            jButtomlLed.setIcon(new ImageIcon(getClass().getResource("/img/testLed.png")));
            jButtomlLed.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    configSerial.verificaLed();
                    if (configSerial.confEnvio) {
                        jLabelInformacao.setText(MensagenUtils.VERIFICA_LED);
                    }else{
                        jLabelInformacao.setText(MensagenUtils.FALHA_VERIFICA);
                    }
                }
            });
        }
        return jButtomlLed;
    }


}
