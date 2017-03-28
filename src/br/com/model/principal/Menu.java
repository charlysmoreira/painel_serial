package br.com.model.principal;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import br.com.model.utils.ConfigSerial;
import br.com.model.utils.FileProperteis;
import br.com.model.utils.PropertiesLoaderImpl;

public class Menu extends JFrame implements ActionListener {

    /**
     * Menu Principal.
     */
    private static final long serialVersionUID = 2993599724118320253L;

    public static JDesktopPane desktop = new JDesktopPane();

    ImageIcon iconEnvio = null;

    JMenu menuConf = null;

    JMenuItem item = null;

    JMenuItem itemCadastro = null;

    JMenuBar menuBar = null;

    public javax.swing.Timer timer = new javax.swing.Timer(280, this);

    private static ConfigSerial configSerial = null;

    static Properties props;
    
    static Properties propsPorta;
    
    FileProperteis fileProperteis;

    int count = 0;

    JLabel label;

    /**
     * Costrutor
     */
    public Menu() {
        getContentPane().add(desktop);
        final JPopupMenu popupMenu = new JPopupMenu();
        this.setIconImage(new ImageIcon(getClass().getResource("/img/led.png")).getImage());
        // Create a submenu with items 
        JMenu submenu = new JMenu();
        // Add submenu to popup menu 
        popupMenu.add(submenu);
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Painel    ");
        menu.setFont(new Font("Arial", Font.PLAIN, 13));
        menuBar.add(menu);
        menuConf = new JMenu("Configuração  ");
        menuConf.setFont(new Font("Arial", Font.PLAIN, 13));
        menuBar.add(menuConf);
        JMenu menuAjuda = new JMenu("Ajuda  ");
        menuAjuda.setFont(new Font("Arial", Font.PLAIN, 13));
        menuBar.add(menuAjuda);
        iconEnvio = new ImageIcon(getClass().getResource("/img/envio.png"));
        ImageIcon iconConf = new ImageIcon(getClass().getResource("/img/conf.png"));
        ImageIcon iconAjuda = new ImageIcon(getClass().getResource("/img/ajuda.png"));
        ImageIcon iconCad = new ImageIcon(getClass().getResource("/img/mais.png"));
        item = new JMenuItem("Envio", iconEnvio);
        JMenuItem itemConf = new JMenuItem("Painel", iconConf);
        JMenuItem itemAjuda = new JMenuItem("Ajuda", iconAjuda);
        JMenuItem itemPanel = new JMenuItem("Sobre o Painel", iconAjuda);
        itemCadastro = new JMenuItem("Cadastrar painel", iconCad);
        item.setVisible(false);
        menu.add(item);
        menuConf.add(itemConf);
        menuAjuda.add(itemAjuda);
        menuAjuda.add(itemPanel);
        menu.add(itemCadastro);
        // Chama Tela/Classe envio
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                //Chama class TelaEnvio com JInternalFrame 
                JInternalFrame internalFrame = new TelaEnvio(PropertiesLoaderImpl.getValor("painel.conf.porta.serial"));
                internalFrame.setBounds(50, 70, 740, 450);
                desktop.add(internalFrame);
                try {
                    internalFrame.setSelected(true);
                } catch (PropertyVetoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        itemCadastro.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JInternalFrame internalFrame = new CadastroId();
                internalFrame.setBounds(50, 70, 540, 350);
                desktop.add(internalFrame);
                try {
                    internalFrame.setSelected(true);
                } catch (PropertyVetoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // Chama Tela/Classe configuracao
        itemConf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                //Chama class configuracao com JInternalFrame 
                JInternalFrame internalFrame = new TelaConfiguracao();
                internalFrame.setBounds(80, 70, 395, 263);
                internalFrame.setVisible(true);
                desktop.add(internalFrame);
                try {
                    internalFrame.setSelected(true);
                } catch (PropertyVetoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        // Chama Tela/Classe ajuda
        itemAjuda.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                //Chama class ajuda com JInternalFrame 
                JInternalFrame internalFrame = new MenuAjuda();
                internalFrame.setBounds(50, 50, 750, 450);
                internalFrame.setVisible(true);
                desktop.add(internalFrame);
                try {
                    internalFrame.setSelected(true);
                } catch (PropertyVetoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        // Chama Tela/Classe ajuda sobre o panel informação
        itemPanel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                //Chama class ajuda com JInternalFrame 
                ImageIcon i = new ImageIcon(getClass().getResource("/img/fundo_ajuda.png"));
                JLabel label = new JLabel(i);
                JOptionPane.showMessageDialog(null, label, "Sobre o Painel", JOptionPane.PLAIN_MESSAGE);
            }
        });
        // 
        menu.add(new JSeparator());
        ImageIcon iconSair = new ImageIcon(getClass().getResource("/img/sair.png"));
        JMenuItem item99 = new JMenuItem("Sair", iconSair);
        menu.add(item99);
        //Sair do Sistema 
        item99.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Object[] options = { "Sim", "Não" };
                int n = JOptionPane.showOptionDialog(null, "Deseja sair da aplicação?", "Menssagem",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {
                    System.exit(0);
                }
            }
        });

        label = new JLabel("Por favor aguarde, buscando painel para se conectar.");
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.ITALIC, 35));
        getContentPane().add(label, BorderLayout.BEFORE_FIRST_LINE);
        timer.start();

        menuBar.setVisible(false);
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 850;
        int height = 600;
        this.setTitle("Envio de dados para painel digital - " + PropertiesLoaderImpl.getValor("painel.version"));
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        fileProperteis = new FileProperteis();
        
        props = fileProperteis.carregarParametros();
        
        abilitarCadastroId();
        
        iniciaThreadConect();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Não foi possível alterar a L&F");
        }
        new Menu();
    }


    private boolean verificaFileProp() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < props.size(); i++) {
            if (props.getProperty("painel " + i) != null || !"".equals(props.getProperty("painel " + i))) {
                return false;
            }
        }
        return true;
    }


    private void abilitarCadastroId() {
        if (verificaFileProp()) {
            label.setText("");
            hasCadastroId();
        }

    }

    private void hasCadastroId() {
        Object[] options = { "Sim", "Não" };
        int n = JOptionPane.showOptionDialog(null, "Painel não cadastrado, deseja cadastrar agora?", "Atenção",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == 0) {
            timer.stop();
            menuBar.setVisible(true);
            label.setVisible(false);
            JInternalFrame internalFrame = new CadastroId();
            internalFrame.setBounds(50, 70, 740, 450);
            desktop.add(internalFrame);
            try {
                internalFrame.setSelected(true);
            } catch (PropertyVetoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            itemCadastro.setVisible(true);
            menuConf.setVisible(false);
        } else {
            System.exit(0);
        }
    }

    String porta = null;
    
    private void iniciaThreadConect() {
        configSerial = new ConfigSerial();
        Thread thread = new Thread() {

            public void run() {
                menuBar.setVisible(false);
                //Pegas todas as portas e seleciona a informada.
                configSerial.listarPortas();
                porta = configSerial.getPortas()[count];
                if (abrirApp && porta != null) {
                    PropertiesLoaderImpl.incluirPropriedade("painel.conf.porta.serial", porta);
                    abrirApp = false;
                    boolean validaPainel = true;
                    boolean hasConect = true;
                    while(validaPainel && hasConect){
                        try {
                            if(configSerial.validaPainel(0xab, porta)){
                                timer.stop();
                                JInternalFrame internalFrame = new TelaEnvio(porta);
                                internalFrame.setBounds(50, 70, 740, 450);
                                menuBar.setVisible(true);
                                item.setVisible(true);
                                menuConf.setVisible(true);
                                desktop.add(internalFrame);
                                label.setVisible(false);
                                validaPainel = false;
                            }else{
                                hasCadastroId();
                                validaPainel = false;
                            }
                        } catch (PortInUseException e) {
                            JOptionPane.showMessageDialog(null, "Conecte o cabo usb!! Feche o software e abra novamente.", "Erro",
                                JOptionPane.ERROR_MESSAGE);
                            hasConect = false;
                            e.printStackTrace();
                        } catch (UnsupportedCommOperationException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    porta = null;
                }

            };
        };
        thread.start();
    }

    boolean abrirApp = true;

    boolean abrirCadaId = true;

    boolean confiSerial = true;

    boolean confPorta = true;

    @Override
    public void actionPerformed(ActionEvent arg0) {
        String oldText = label.getText();
        String newText = oldText.substring(1) + oldText.substring(0, 1);
        label.setText(newText);
    }
}
