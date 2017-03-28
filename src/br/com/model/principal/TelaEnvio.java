package br.com.model.principal;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import br.com.model.model.ConfigTO;
import br.com.model.model.Menssagem;
import br.com.model.utils.ConfigSerial;
import br.com.model.utils.MensagenUtils;
import br.com.model.utils.PropertiesLoaderImpl;

/**
 * @author Charlys
 *
 */
public class TelaEnvio extends JInternalFrame {

    /**
     * Responsavel pela apresentacao do sistema.
     */
    private static final long serialVersionUID = -462368900778612993L;

    private JPanel jPanelEnvio = null;

    private JLabel jLabel = null;

    private JLabel jLabelEnviado = null;

    private JLabel jLabelErro = null;

    private JTextField jTextField = null;

    private JTable jTable = null;

    private JButton jButton = null;

    private JButton jButtonAdicionar = null;

    private JButton jButtonLimpar = null;

    private JButton jButtonDelete = null;

    private JScrollPane jScrollPane = null;

    protected String[] portas;

    private Integer count = 1;

    private JComboBox comboBoxTipo = null;

    private JComboBox comboBoxEfeito = null;

    private JComboBox comboBoxVelocidade = null;

    private JLabel jLabelTemp = null;

    private JLabel jLabelData = null;

    private JLabel jLabelHora = null;

    private JLabel jLabelConf = null;

    private JLabel imageLabel = null;

    private JLabel jLabelInformacao = null;

    private JCheckBox jCheckBoxTemp = null;

    private JCheckBox jCheckBoxData = null;

    private JCheckBox jCheckBoxHora = null;

    private JCheckBox jCheckBoxConf = null;

    CheckBoxListener myListener = null;

    private String[] tipo = { "Selecione Tipo", "Texto", "Data", "Temperatura", "Hora" };

    private String[] efeitos = { "Selecione Efeito", "Direita para esquerda", "Baixo para cima", "Padrão" };

    private String[] velocidade = { "Selecione Velocidade", "1x", "2x", "3x", "4x", "5x", "6x", "7x", "8x", "9x", "10x" };

    static List<Menssagem> listaFrases = new ArrayList<Menssagem>();

    int contadorFrase;

    ConfigTO configTO;

    Menssagem menssagem;

    protected Enumeration<?> listaDePortas;

    private ConfigSerial configSerial = null;

    private DefaultTableModel modelo;

    int selRow;

    int tamanhoMax;

    boolean alterar = false;

    boolean config = true;
    
    private String columnNames[] = { "Tipo de Dado", "Menssagem", "Efeito", "Velocidade" };

    private ImageIcon ii = null;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    public TelaEnvio(String porta) {
        super("Painel digital - " + PropertiesLoaderImpl.getValor("painel.version"), false, true, false, true);
        this.setFrameIcon(new ImageIcon(this.getClass().getResource("/img/ledInternal.png")));
        configSerial = new ConfigSerial();
        menssagem = new Menssagem();
        listaFrases = new ArrayList<Menssagem>();
        myListener = new CheckBoxListener();
        ii = new ImageIcon(this.getClass().getResource("/img/atencao.png"));
        ListSelectionModel rowSM = getJTable().getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    selRow = lsm.getMinSelectionIndex();
                    Object valorTipo = getJTable().getModel().getValueAt(selRow, 0);
                    Object valorTexto = getJTable().getModel().getValueAt(selRow, 1);
                    Object valorEfeito = getJTable().getModel().getValueAt(selRow, 2);
                    Object valorVeleoc = getJTable().getModel().getValueAt(selRow, 3);
                    menssagem = new Menssagem();
                    menssagem.setTexto(valorTexto.toString());
                    alterar = true;
                    if (valorEfeito != null) {
                        if (valorEfeito.toString().equals("Direita para esquerda")) {
                            menssagem.setEfeito(1);
                            comboBoxEfeito.setSelectedIndex(1);
                        } else if (valorEfeito.toString().equals("Baixo para cima")) {
                            menssagem.setEfeito(2);
                            comboBoxEfeito.setSelectedIndex(2);
                        } else if (valorEfeito.toString().equals("Padrão")) {
                            menssagem.setEfeito(3);
                            comboBoxEfeito.setSelectedIndex(3);
                        }
                    }
                    if (valorVeleoc != null) {
                        if (valorVeleoc.toString().equals("1x")) {
                            menssagem.setVelocidade(1);
                            comboBoxVelocidade.setSelectedIndex(1);
                        } else if (valorVeleoc.toString().equals("2x")) {
                            menssagem.setVelocidade(2);
                            comboBoxVelocidade.setSelectedIndex(2);
                        } else if (valorVeleoc.toString().equals("3x")) {
                            menssagem.setVelocidade(3);
                            comboBoxVelocidade.setSelectedIndex(3);
                        } else if (valorVeleoc.toString().equals("4x")) {
                            menssagem.setVelocidade(4);
                            comboBoxVelocidade.setSelectedIndex(4);
                        } else if (valorVeleoc.toString().equals("5x")) {
                            menssagem.setVelocidade(5);
                            comboBoxVelocidade.setSelectedIndex(5);
                        } else if (valorVeleoc.toString().equals("6x")) {
                            menssagem.setVelocidade(6);
                            comboBoxVelocidade.setSelectedIndex(6);
                        } else if (valorVeleoc.toString().equals("7x")) {
                            menssagem.setVelocidade(7);
                            comboBoxVelocidade.setSelectedIndex(7);
                        } else if (valorVeleoc.toString().equals("8x")) {
                            menssagem.setVelocidade(8);
                            comboBoxVelocidade.setSelectedIndex(8);
                        } else if (valorVeleoc.toString().equals("9x")) {
                            menssagem.setVelocidade(9);
                            comboBoxVelocidade.setSelectedIndex(9);
                        } else if (valorVeleoc.toString().equals("10x")) {
                            menssagem.setVelocidade(10);
                            comboBoxVelocidade.setSelectedIndex(10);
                        }
                    }
                    if (valorTipo != null) {
                        if (valorTipo.toString().equals("Texto")) {
                            menssagem.setTipoTexto(1);
                            comboBoxTipo.setSelectedIndex(1);
                        } else if (valorTipo.toString().equals("Data")) {
                            menssagem.setTipoTexto(2);
                            comboBoxTipo.setSelectedIndex(2);
                        } else if (valorTipo.toString().equals("Temperatura")) {
                            menssagem.setTipoTexto(3);
                            comboBoxTipo.setSelectedIndex(3);
                        } else if (valorTipo.toString().equals("Hora")) {
                            menssagem.setTipoTexto(4);
                            comboBoxTipo.setSelectedIndex(4);
                        }
                    }
                    alterar = true;
                    jLabelInformacao.setText(MensagenUtils.ACOES);
                    jLabelInformacao.setForeground(Color.BLUE);
                    jTextField.setText(valorTexto.toString());
                    jLabel.setText("Editar texto : " + (selRow + 1));
                    jButtonAdicionar.setText("Alterar");
                    jButtonDelete.setVisible(true);
                    jButtonAdicionar.setEnabled(true);
                    jTextField.setEditable(true);
                }
            }
        });
        this.setVisible(true);
        this.pack();
        this.getContentPane().add(getJPanelEnvio());
    }

    /**
     * This method initializes jPanel   
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelEnvio() {
        if (jPanelEnvio == null) {
            jPanelEnvio = new JPanel();
            jPanelEnvio.setLayout(null);
            jPanelEnvio.setSize(new Dimension(710, 263));
            jPanelEnvio.add(getJScrollPane(), null);
            jPanelEnvio.add(getJButton(), null);
            jPanelEnvio.add(getJButtonLimpar(), null);
            jPanelEnvio.add(getJButtonDelete(), null);
            jPanelEnvio.add(getJLabel(), null);
            jPanelEnvio.add(getJTextField(), null);
            jPanelEnvio.add(getJButtonAdicionar(), null);
            jPanelEnvio.add(getJComboBoxTipo(), null);
            jPanelEnvio.add(getJComboBoxEfeito(), null);
            jPanelEnvio.add(getJComboBoxVelocidade(), null);
            jPanelEnvio.add(getJLabelEnviado(), null);
            jPanelEnvio.add(getJLabelErro(), null);
            //            jPanelEnvio.add(getJCheckBoxTemp(), null);
            //            jPanelEnvio.add(getJCheckBoxData(), null);
            //            jPanelEnvio.add(getJCheckBoxHora(), null);
            //            jPanelEnvio.add(getJLabelTemp(), null);
            //            jPanelEnvio.add(getJLabelData(), null);
            //            jPanelEnvio.add(getJLabelHora(), null);
            jPanelEnvio.add(getJLabelImg(), null);
            jPanelEnvio.add(getJLabelInformacao(), null);
        }
        return jPanelEnvio;
    }

    /**
     * This method initializes jScrollPane  
     *  
     * @return javax.swing.JScrollPane  
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            LineBorder border = new LineBorder(Color.BLUE, 1, true);
            TitledBorder tborder = new TitledBorder(border, "Texto para envio", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.ITALIC, 12), Color.BLUE);
            jScrollPane.setBorder(tborder);
            jScrollPane.setBounds(new Rectangle(6, 100, 710, 219));
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTextArea    
     *  
     * @return javax.swing.JTextArea    
     */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.addKeyListener(new KeyListener() {
                
                @Override
                public void keyTyped(KeyEvent paramKeyEvent) {
                    if(alterar){
                        quantCaractCurrent = jTextField.getText().length();
                    }
                    if(paramKeyEvent.getKeyChar() == KeyEvent.VK_BACK_SPACE){
                        if(quantCaractCurrent != 0 && !"".equals(jTextField.getText())) {
                            if(quantCaractCurrent > jTextField.getText().length()){
                                quantMaxCaracter -= quantCaractCurrent - jTextField.getText().length();
                            }else{
                                quantMaxCaracter--;
                            }
                         }else if(jTextField.getText().length() == 0){
                             quantMaxCaracter -= quantCaractCurrent;
                         }
                        quantCaractCurrent = jTextField.getText().length();
                        hasShow = true;
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent paramKeyEvent) {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public void keyPressed(KeyEvent paramKeyEvent) {
                    // TODO Auto-generated method stub
                    
                }
            });
            LineBorder border = new LineBorder(Color.BLUE, 1, true);
            jTextField.setBorder(border);
            jTextField.setBounds(new Rectangle(6, 40, 710, 20));
            jTextField.setEditable(false);
        }
        return jTextField;
    }

    /**
     * This method initializes jTextArea    
     *  
     * @return javax.swing.JTextArea    
     */
    @SuppressWarnings("serial")
    private JTable getJTable() {
        if (jTable == null) {
            modelo = new DefaultTableModel();
            jTable = new JTable(modelo) {

                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            modelo.setColumnIdentifiers(columnNames);
            jTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        }
        return jTable;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelEnviado() {
        if (jLabelEnviado == null) {
            jLabelEnviado = new JLabel("Enviado com sucesso!!");
            jLabelEnviado.setFont(new Font("Arial", Font.ITALIC, 12));
            jLabelEnviado.setForeground(Color.black);
            jLabelEnviado.setBounds(new Rectangle(530, 68, 710, 30));
            jLabelEnviado.setIcon(new ImageIcon(getClass().getResource("/img/sucesso.png")));
            jLabelEnviado.setVisible(false);
        }
        return jLabelEnviado;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelErro() {
        if (jLabelErro == null) {
            jLabelErro = new JLabel("Erro no envio dos dados!!");
            jLabelErro.setFont(new Font("Arial", Font.ITALIC, 12));
            jLabelErro.setForeground(Color.black);
            jLabelErro.setBounds(new Rectangle(530, 68, 710, 30));
            jLabelErro.setIcon(new ImageIcon(getClass().getResource("/img/erro.png")));
            jLabelErro.setVisible(false);
        }
        return jLabelErro;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new JLabel("Texto número: " + count.intValue());
            jLabel.setBounds(new Rectangle(6, 20, 710, 20));
            jLabel.setFont(new Font("Arial", Font.BOLD, 12));
            jLabel.setForeground(Color.BLUE);
        }
        return jLabel;
    }

    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(8, 350, 85, 24));
            jButton.setText("Enviar");
            jButton.setEnabled(false);
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jLabelEnviado.setVisible(false);
                    jButton.repaint();
                    getJLabelEnviado();
                    if (!(listaFrases.isEmpty())) {
                        getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        imageLabel.setVisible(true);
                        getJLabelImg();
                        jLabelEnviado.setVisible(false);
                        jLabelErro.setVisible(false);
                        try {
                            if (configSerial.validaPainel(0xab, PropertiesLoaderImpl.getValor("painel.conf.porta.serial"))) {
                                configSerial.prepararEnvio(listaFrases);
                                if (configSerial.isEnviado()) {
                                    imageLabel.setVisible(false);
                                    getJLabelImg();
                                    jLabelEnviado.setVisible(true);
                                    getJLabelEnviado();
                                } else {
                                    imageLabel.setVisible(false);
                                    getJLabelImg();
                                    jLabelErro.setVisible(true);
                                    getJLabelErro();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Painel não reconhecido para envio", "Verifique",
                                    JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (HeadlessException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (PortInUseException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (UnsupportedCommOperationException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    configSerial.close();
                    getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }
        return jButton;
    }

    /**
     * Lendo o arquivo das configuraçoes de portas.
     */
    private boolean lendoConfSerializada() {
        if (PropertiesLoaderImpl.getValor("painel.conf.porta.serial").equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Remove o arquivo que foi serializado para obter as portas.
     * @param file
     */
    public static void delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();
    }

    /**
     * This method initializes jButtonLimpar 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonLimpar() {
        if (jButtonLimpar == null) {
            jButtonLimpar = new JButton();
            jButtonLimpar.setBounds(new Rectangle(97, 350, 90, 24));
            jButtonLimpar.setText("Limpar");
            jButtonLimpar.setEnabled(false);
            jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (alterar == true) {
                        alterar = false;
                        jLabel.setText("Texto número: " + count.intValue());
                        jButtonAdicionar.setText("Adiciona");
                        jButtonDelete.setVisible(false);
                    } else {
                        Object[] options = { "Sim", "Não" };
                        int n = JOptionPane.showOptionDialog(null, "Deseja apagar todos os textos?", "Menssagem",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                        if (n == 0) {
                            modelo.setNumRows(0);
                            listaFrases = new ArrayList<Menssagem>();
                            count = 1;
                            jLabel.setText("Texto número: " + count.intValue());
                            quantMaxCaracter = 2;
                        }else{
                            if(menssagem.getTipoTexto().equals(new Integer(1))){
                                quantMaxCaracter -= jTextField.getText().length();
                                quantMaxCaracter -= 3;
                            }else if(!menssagem.getTipoTexto().equals(new Integer(0))){
                                quantMaxCaracter -= 1;
                            }
                        }
                    }
                    menssagem = new Menssagem();
                    jTextField.setText("");
                    comboBoxTipo.setSelectedIndex(0);
                    comboBoxVelocidade.setSelectedIndex(0);
                    comboBoxEfeito.setSelectedIndex(0);
                    if(quantMaxCaracter < _MAX_CARACTER){
                        jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
                        jLabelInformacao.setForeground(Color.BLUE);
                    }
                    imageLabel.setVisible(false);
                    getJLabelEnviado();
                }
            });
        }
        return jButtonLimpar;
    }

    /**
     * This method initializes jButtonDelete
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonDelete() {
        if (jButtonDelete == null) {
            jButtonDelete = new JButton();
            jButtonDelete.setBounds(new Rectangle(191, 350, 90, 24));
            jButtonDelete.setText("Remover");
            jButtonDelete.setVisible(false);
            jButtonDelete.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    modelo.removeRow(selRow);
                    listaFrases.remove(selRow);
                    alterar = false;
                    count = count - 1;
                    if(menssagem.getTipoTexto().equals(new Integer(1))){
                        quantMaxCaracter -= jTextField.getText().length();
                        quantMaxCaracter -= 3;
                    }else{
                        quantMaxCaracter -= 1;
                    }
                    jTextField.setText("");
                    comboBoxTipo.setSelectedIndex(0);
                    comboBoxVelocidade.setSelectedIndex(0);
                    comboBoxEfeito.setSelectedIndex(0);
                    if(quantMaxCaracter < _MAX_CARACTER){
                        jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
                        jLabelInformacao.setForeground(Color.BLUE);
                    }
                    jLabel.setText("Texto número: " + count.intValue());
                    jButtonAdicionar.setText("Adiciona");
                    menssagem = new Menssagem();
                    jButtonDelete.setVisible(false);
                    hasShow = true;
                }
            });
        }
        return jButtonDelete;
    }

    /**
     * This method initializes jButtonAdicionar 
     *  
     * @return javax.swing.jButtonAdicionar  
     */
    private JButton getJButtonAdicionar() {
        if (jButtonAdicionar == null) {
            jButtonAdicionar = new JButton();
            jButtonAdicionar.setBounds(new Rectangle(10, 70, 100, 24));
            jButtonAdicionar.setVisible(true);
            jButtonAdicionar.setText("Adicionar");
            jButtonAdicionar.setEnabled(false);
            jButtonAdicionar.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
            jButtonAdicionar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (contadorFrase != 255) {
                        if (validaCampo(jTextField.getText(), menssagem.getTipoTexto())) {
                            if (menssagem.getTipoTexto() != null && !(menssagem.getTipoTexto().equals(new Integer(0)))) {
                                if (alterar == true && menssagem != null) {
                                    modelo.removeRow(selRow);
                                    if (menssagem.isData() && menssagem.getTipoTexto().equals(new Integer(2))) {
                                        modelo.insertRow(selRow, new Object[] { tipo[menssagem.getTipoTexto()],
                                                "------", "--------", "-------------" });
                                    } else if (menssagem.isHora() && menssagem.getTipoTexto().equals(new Integer(4))) {
                                        modelo.insertRow(selRow, new Object[] { tipo[menssagem.getTipoTexto()],
                                                "--------", "-------------------", "--------------" });
                                    } else if (menssagem.isTemp() && menssagem.getTipoTexto().equals(new Integer(3))) {
                                        modelo.insertRow(selRow, new Object[] { tipo[menssagem.getTipoTexto()],
                                                "-------------------", "--------------------", "-----------------" });
                                    } else {
                                        menssagem.setTexto(jTextField.getText().replaceAll("[ ]+", " ").trim());
                                        modelo.insertRow(selRow, new Object[] { tipo[menssagem.getTipoTexto()],
                                                menssagem.getTexto(), efeitos[menssagem.getEfeito()],
                                                velocidade[menssagem.getVelocidade()] });
                                    }
                                    int valorT = menssagem.getTipoTexto();
                                    int valorE = menssagem.getEfeito();
                                    int valorV = menssagem.getVelocidade();
                                    
                                    listaFrases.set(selRow, menssagem);
                                    alterar = false;
                                    jButtonAdicionar.setText("Adiciona");
                                    
                                    comboBoxTipo.setSelectedIndex(0);
                                    comboBoxVelocidade.setSelectedIndex(0);
                                    comboBoxEfeito.setSelectedIndex(0);
                                    
                                    menssagem.setTipoTexto(valorT);
                                    menssagem.setEfeito(valorE);
                                    menssagem.setVelocidade(valorV);
                                    
                                    if(quantMaxCaracter < _MAX_CARACTER){
                                        jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
                                    }
                                } else {
                                    if (menssagem.isData() && menssagem.getTipoTexto().equals(new Integer(2))) {
                                        modelo.addRow(new Object[] { tipo[menssagem.getTipoTexto()], "------",
                                                "--------", "-------------" });
                                    } else if (menssagem.isHora() && menssagem.getTipoTexto().equals(new Integer(4))) {
                                        modelo.addRow(new Object[] { tipo[menssagem.getTipoTexto()], "--------",
                                                "-------------------", "--------------" });
                                    } else if (menssagem.isTemp() && menssagem.getTipoTexto().equals(new Integer(3))) {
                                        modelo.addRow(new Object[] { tipo[menssagem.getTipoTexto()],
                                                "-------------------", "--------------------", "-----------------" });
                                    } else {
                                        menssagem.setTexto(jTextField.getText());
                                        modelo.addRow(new Object[] { tipo[menssagem.getTipoTexto()],
                                                menssagem.getTexto(), efeitos[menssagem.getEfeito()],
                                                velocidade[menssagem.getVelocidade()] });
                                    }
                                    count++;
                                    listaFrases.add(menssagem);
                                    jLabel.setText("Texto número: " + count.intValue());
                                    menssagem = new Menssagem();
                                    comboBoxTipo.setSelectedIndex(0);
                                    comboBoxVelocidade.setSelectedIndex(0);
                                    comboBoxEfeito.setSelectedIndex(0);
                                    if(quantMaxCaracter < _MAX_CARACTER){
                                        jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
                                    }
                                    contadorFrase++;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Selecione o Tipo de Dado!!", "Atenção",
                                    JOptionPane.WARNING_MESSAGE);
                            }
                            jTextField.setText("");
                            jButtonDelete.setVisible(false);

                            comboBoxVelocidade.setEnabled(false);
                            comboBoxEfeito.setEnabled(false);
                            jTextField.setEditable(false);

                            jButtonLimpar.setEnabled(true);
                            jButton.setEnabled(true);
                            jButtonAdicionar.setEnabled(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Digite um texto!!", "Atenção",
                                JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "A quantidade de frases está limatada à 255!!", "Atenção",
                            JOptionPane.WARNING_MESSAGE);
                    }
                     flagText = false;
                     flagTemp = false;
                     flagDate = false;
                     flagHora = false;
                     quantCaractCurrent = 0;
                }
            });
        }
        return jButtonAdicionar;
    }

    //Valida se o texto foi digitado;
    private boolean validaCampo(String texto, Integer tipoDeDado) {
        if (!"".equals(texto.trim()) && tipoDeDado.equals(1)) {
            return true;
        } else if (!tipoDeDado.equals(1)) {
            return true;
        }
        jTextField.setText("");
        return false;
    }
    
    boolean hasShow = true;

    /**
     * This method initializes JComboBox 
     *  
     * @return javax.swing.JComboBox  
     */
    private JComboBox getJComboBoxTipo() {
        if (comboBoxTipo == null) {
            comboBoxTipo = new JComboBox(tipo);
            comboBoxTipo.setBounds(new Rectangle(130, 70, 100, 24));
            comboBoxTipo.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    menssagem.setTipoTexto(((JComboBox) e.getSource()).getSelectedIndex());
                    if (menssagem.getTipoTexto().equals(new Integer(2))) {//Data 
                        menssagem.setData(true);
                        menssagem.setTemp(false);
                        menssagem.setHora(false);
                        comboBoxEfeito.setEnabled(false);
                        comboBoxEfeito.setSelectedIndex(0);
                        comboBoxVelocidade.setSelectedIndex(0);
                        comboBoxVelocidade.setEnabled(false);
                        jTextField.setEditable(false);
                         
                        jTextField.setText("");
                        if(!hasShow){
                            menssagenLimetCaracter();
                            jButtonAdicionar.setEnabled(false);
                            
                        }else{
                            if(quantMaxCaracter < _MAX_CARACTER){
                                jLabelInformacao.setText(MensagenUtils.ADICIONAR_INFORMAÇÃO);
                            }
                            jLabelInformacao.setForeground(Color.BLUE);
                            jButtonAdicionar.setEnabled(true);
                        }
                        if(!flagDate && !alterar){
                            quantMaxCaracter += 1;
                            if(flagTemp){
                                quantMaxCaracter -= 1;
                                flagTemp = false;
                            } else if(flagHora){
                                quantMaxCaracter -= 1;
                                flagHora = false;
                            } else if(flagText){
                                quantMaxCaracter -= 3;
                                flagText = false;
                            }
                            flagDate = true;
                        }
                    } else if (menssagem.getTipoTexto().equals(new Integer(3))) {//Temperatua
                        menssagem.setTemp(true);
                        menssagem.setData(false);
                        menssagem.setHora(false);
                        comboBoxEfeito.setEnabled(false);
                        comboBoxVelocidade.setEnabled(false);
                        jTextField.setEditable(false);
                        comboBoxEfeito.setSelectedIndex(0);
                        comboBoxVelocidade.setSelectedIndex(0);
                         
                        jTextField.setText("");
                        if(!hasShow){
                            menssagenLimetCaracter();
                            jButtonAdicionar.setEnabled(false);
                        }else{
                            if(quantMaxCaracter < _MAX_CARACTER){
                                jLabelInformacao.setText(MensagenUtils.ADICIONAR_INFORMAÇÃO);
                            }
                            jLabelInformacao.setForeground(Color.BLUE);
                            jButtonAdicionar.setEnabled(true);
                        }
                        if(!flagTemp && !alterar){
                            quantMaxCaracter += 1;
                            if(flagDate){
                                quantMaxCaracter -= 1;
                                flagDate = false;
                            }else if(flagHora){
                                quantMaxCaracter -= 1;
                                flagHora = false;
                            } else if(flagText){
                                quantMaxCaracter -= 3;
                                flagText = false;
                            }
                            flagTemp = true;
                        }
                    } else if (menssagem.getTipoTexto().equals(new Integer(4))) {//Hora
                        menssagem.setHora(true);
                        menssagem.setTemp(false);
                        menssagem.setData(false);
                        comboBoxEfeito.setEnabled(false);
                        comboBoxVelocidade.setEnabled(false);
                        comboBoxEfeito.setSelectedIndex(0);
                        comboBoxVelocidade.setSelectedIndex(0);
                        jTextField.setEditable(false);
                         
                        jTextField.setText("");
                        if(!hasShow){
                            menssagenLimetCaracter();
                            jButtonAdicionar.setEnabled(false);
                        }else{
                            if(quantMaxCaracter < _MAX_CARACTER){
                                jLabelInformacao.setText(MensagenUtils.ADICIONAR_INFORMAÇÃO);
                            }
                            jLabelInformacao.setForeground(Color.BLUE);
                            jButtonAdicionar.setEnabled(true);
                        }
                        if(!flagHora && !alterar){
                            quantMaxCaracter += 1;
                            if(flagDate){
                                quantMaxCaracter -= 1;
                                flagDate = false;
                            }else if(flagTemp){
                                quantMaxCaracter -= 1;
                                flagTemp = false;
                            } else if(flagText){
                                quantMaxCaracter -= 3;
                                flagText = false;
                            }
                            flagHora = true;
                        }
                    } else if (menssagem.getTipoTexto().equals(new Integer(1))) {//Texto
                        menssagem.setHora(false);
                        menssagem.setTemp(false);
                        menssagem.setData(false);
                        comboBoxEfeito.setEnabled(true);
                        jButtonAdicionar.setEnabled(false);
                        if(!hasShow){
                            menssagenLimetCaracter();
                        }else{
                            if(quantMaxCaracter < _MAX_CARACTER){
                                jLabelInformacao.setText(MensagenUtils.EFEITO_TEXTO);
                            }
                            jLabelInformacao.setForeground(Color.BLUE);
                        }
                        if(!flagText && !alterar){
                            quantMaxCaracter += 3;
                            if(flagDate){
                                quantMaxCaracter -= 1;
                                flagDate = false;
                            }else if(flagTemp){
                                quantMaxCaracter -= 1;
                                flagTemp = false;
                            } else if(flagHora){
                                quantMaxCaracter -= 1;
                                flagHora = false;
                            }
                            flagText = true;
                        }
                    } else {
                        menssagem.setHora(false);
                        menssagem.setTemp(false);
                        menssagem.setData(false);
                        comboBoxEfeito.setEnabled(false);
                        jButtonAdicionar.setEnabled(false);
                        jTextField.setEditable(false);
                        if(!hasShow){
                            menssagenLimetCaracter();
                        }else{
                            if(quantMaxCaracter < _MAX_CARACTER){
                                jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
                            }
                            jLabelInformacao.setForeground(Color.BLUE);
                        }
                    }

                    if (!alterar && quantMaxCaracter >= _MAX_CARACTER) {
                        comboBoxTipo.setSelectedIndex(0);
                        comboBoxEfeito.setSelectedIndex(0);
                        comboBoxEfeito.setEnabled(false);
                        comboBoxVelocidade.setSelectedIndex(0);
                        comboBoxVelocidade.setEnabled(false);
                        jTextField.setText("");
                    }
                }
            });
        }
        return comboBoxTipo;
    }
    
    private void menssagenLimetCaracter() {
        jLabelInformacao.setText(MensagenUtils.LIMITED_CARACTER);
        jLabelInformacao.setForeground(Color.RED);
    }

    int quantMaxCaracter = 0;
    
    int quantCaractCurrent;
    //TODO TROCAR VALOR DE 20 PARA 2034
    private static final int _MAX_CARACTER = 20;

    public class DocumentoLimitado extends PlainDocument {

        private static final String _CARACTER_PAINEL = "1234567890QWERTYUIOPASDFGHJKLÇZXCVBNM"
                + "qwertyuiopasdfghjklçzxcvbnm" + ",/[{]}.:;!@#$%¨&*()_+-ªºàáé=ãâ ";

        private int tamanhoMax = 5;

        public DocumentoLimitado(int tamanhoMax) {
            this.tamanhoMax = tamanhoMax;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

            if (str == null)
                return;
            
            if (quantMaxCaracter >= _MAX_CARACTER) {
                if(hasShow){
                    JOptionPane.showMessageDialog(null, "Quantidade de caracteres limitado!!", "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                    menssagenLimetCaracter();
                    hasShow = false;
                }
                if(str.length() == 1){
                    super.remove(offset, 1);
                }
            }

            if (!(str.length() > 1)) {
                if (_CARACTER_PAINEL.indexOf(str) == -1) {
                    JOptionPane.showMessageDialog(null, "Este caractere não é aceito no painel!!", "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                    super.remove(offset, 1);
                }
            }


            String stringAntiga = getText(0, getLength());
            int tamanhoNovo = stringAntiga.length() + str.length();


            if (tamanhoNovo <= tamanhoMax) {
                super.insertString(offset, str, attr);
            } else if(!str.startsWith("-----")){
                super.insertString(offset, "", attr);
                JOptionPane.showMessageDialog(null, "Quantidade de caracteres limitado para este efeito!!", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
                if(str.length() == 1){
                    super.remove(offset, 1);
                }
                
            }

            if(!alterar || str.length() == 1){
                quantMaxCaracter++;
                quantCaractCurrent++;
            }
        }
    }


    /**
     * This method initializes JComboBox 
     *  
     * @return javax.swing.JComboBox  
     */
    private JComboBox getJComboBoxEfeito() {
        if (comboBoxEfeito == null) {
            comboBoxEfeito = new JComboBox(efeitos);
            comboBoxEfeito.setBounds(new Rectangle(238, 70, 130, 24));
            comboBoxEfeito.setEnabled(false);
            comboBoxEfeito.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    menssagem.setEfeito(((JComboBox) e.getSource()).getSelectedIndex());
                    if (!menssagem.getEfeito().equals(new Integer(0))) {
                        if (((JComboBox) e.getSource()).getSelectedIndex() == 2 ) {
                            String ultimoCaract = PropertiesLoaderImpl.getValor("painel.conf.id");
                            ultimoCaract = ultimoCaract.substring(8, 10);
                            tamanhoMax = Integer.parseInt(ultimoCaract, 16);
                            jTextField.setDocument(new DocumentoLimitado(tamanhoMax));
                            jLabelInformacao.setText("Atenção,a frase deve ter apenas "
                                    + ultimoCaract + " letras. " + "\r\n" + "Selecione a velocidade.");
                        } else {
                            jTextField.setDocument(new DocumentoLimitado(_MAX_CARACTER));
                            jLabelInformacao.setText(MensagenUtils.VELOCIDADE);
                        }
                        comboBoxVelocidade.setEnabled(true);
                    } else {
                        comboBoxVelocidade.setEnabled(false);
                        jTextField.setEditable(false);
                        jButtonAdicionar.setEnabled(false);
                    }
                }
            });
        }
        return comboBoxEfeito;
    }

    /**
     * This method initializes JComboBox 
     *  
     * @return javax.swing.JComboBox  
     */
    private JComboBox getJComboBoxVelocidade() {
        if (comboBoxVelocidade == null) {
            comboBoxVelocidade = new JComboBox(velocidade);
            comboBoxVelocidade.setBounds(new Rectangle(375, 70, 130, 24));
            comboBoxVelocidade.setEnabled(false);
            comboBoxVelocidade.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    menssagem.setVelocidade(((JComboBox) e.getSource()).getSelectedIndex());
                    if (!menssagem.getVelocidade().equals(new Integer(0))) {
                        jLabelInformacao.setText(MensagenUtils.DIGITE_TEXTO);
                        jTextField.setEditable(true);
                        jButtonAdicionar.setEnabled(true);
                    } else {
                        jTextField.setEditable(false);
                        jButtonAdicionar.setEnabled(false);
                    }
                }
            });
        }
        return comboBoxVelocidade;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelTemp() {
        if (jLabelTemp == null) {
            jLabelTemp = new JLabel();
            jLabelTemp.setBounds(new Rectangle(525, 70, 80, 25));
            jLabelTemp.setText("Temperatura");
        }
        return jLabelTemp;
    }

    /**
     * This method initializes JCheckBox    
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getJCheckBoxTemp() {
        if (jCheckBoxTemp == null) {
            jCheckBoxTemp = new JCheckBox();
            jCheckBoxTemp.setBounds(new Rectangle(500, 70, 25, 25));
            jCheckBoxTemp.setVisible(true);
            jCheckBoxTemp.addItemListener(myListener);
        }
        return jCheckBoxTemp;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelData() {
        if (jLabelData == null) {
            jLabelData = new JLabel();
            jLabelData.setBounds(new Rectangle(620, 70, 80, 25));
            jLabelData.setText("Data");
        }
        return jLabelData;
    }

    /**
     * This method initializes JCheckBox    
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getJCheckBoxData() {
        if (jCheckBoxData == null) {
            jCheckBoxData = new JCheckBox();
            jCheckBoxData.setBounds(new Rectangle(595, 70, 25, 25));
            jCheckBoxData.setVisible(true);
            jCheckBoxData.addItemListener(myListener);
        }
        return jCheckBoxData;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelHora() {
        if (jLabelHora == null) {
            jLabelHora = new JLabel();
            jLabelHora.setBounds(new Rectangle(680, 70, 80, 25));
            jLabelHora.setText("Hora");
        }
        return jLabelHora;
    }

    /**
     * This method initializes JCheckBox    
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getJCheckBoxHora() {
        if (jCheckBoxHora == null) {
            jCheckBoxHora = new JCheckBox();
            jCheckBoxHora.setBounds(new Rectangle(655, 70, 25, 25));
            jCheckBoxHora.setVisible(true);
            jCheckBoxHora.addItemListener(myListener);
        }
        return jCheckBoxHora;
    }


    public void verificaChebox() {
        if (menssagem.isTemp() || menssagem.isData() || menssagem.isHora()) {
            jTextField.setEditable(false);
        } else {
            jTextField.setEditable(true);
        }
    }

    class CheckBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Object source = e.getSource();
            if (source == jCheckBoxConf) {
                if (config) {
                    jButton.setVisible(false);
                    jButtonLimpar.setVisible(false);
                    config = false;
                    jLabelInformacao.setText(MensagenUtils.CONFIGURAR);
                } else {
                    jButton.setVisible(true);
                    jButtonLimpar.setVisible(true);
                    config = true;
                    jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
                }
            }

            //            else if (source == jCheckBoxData) {
            //                if (!menssagem.isData()) {
            //                    menssagem.setData(true);
            //                    count++;
            //                } else {
            //                    menssagem.setData(false);
            //                    count--;
            //                }
            //                jLabel.setText("Texto número: " + count.intValue());
            //            } else if (source == jCheckBoxHora) {
            //                if (!menssagem.isHora()) {
            //                    menssagem.setHora(true);
            //                    count++;
            //                } else {
            //                    menssagem.setHora(false);
            //                    count--;
            //                }
            //                jLabel.setText("Texto número: " + count.intValue());
            //            }
            //            jTextField.setText("");
            //            verificaChebox();
        }
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelInformacao() {
        if (jLabelInformacao == null) {
            jLabelInformacao = new JLabel();
            jLabelInformacao.setBounds(new Rectangle(288, 350, 500, 25));
            jLabelInformacao.setFont(new Font("Arial", Font.ITALIC, 15));
            jLabelInformacao.setForeground(Color.BLUE);
            if(quantMaxCaracter < _MAX_CARACTER){
                jLabelInformacao.setText(MensagenUtils.SELECIONE_O_TIPO_DE_DADO);
            }
        }
        return jLabelInformacao;
    }

    /**
     * This method initializes JLabel    
     *  
     * @return javax.swing.JLabel    
     */
    private JLabel getJLabelImg() {
        if (imageLabel == null) {
            imageLabel = new JLabel();
            imageLabel.setBounds(new Rectangle(530, 68, 130, 25));
            imageLabel.setIcon(ii);
            imageLabel.setVisible(false);
        }
        return imageLabel;
    }
    
    boolean flagText;
    boolean flagTemp;
    boolean flagDate;
    boolean flagHora;
}
