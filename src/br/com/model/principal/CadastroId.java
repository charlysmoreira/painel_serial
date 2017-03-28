package br.com.model.principal;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import br.com.model.utils.FileProperteis;

public class CadastroId extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTextField jTextField = null;

    private JLabel jLabelConf = null;

    private JPanel jPanel = null;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private JButton jButton = null;

    private JButton jButtonRemove = null;

    List<String> listaDeId = new ArrayList<String>();

    boolean alterar = false;

    static Properties props;

    FileProperteis fileProperteis;

    private DefaultTableModel modelo;

    int selRow;

    private String columnNames[] = { "Código" };

    public CadastroId() {
        super("Cadastrar painel para envio de dados", false, true, false, true);
        this.setFrameIcon(new ImageIcon(this.getClass().getResource("/img/ledInternal.png")));
        getContentPane().add(getJPanel());
        ListSelectionModel rowSM = getJTable().getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (arg0.getValueIsAdjusting())
                    return;
                ListSelectionModel lsm = (ListSelectionModel) arg0.getSource();
                if (!lsm.isSelectionEmpty()) {
                    selRow = lsm.getMinSelectionIndex();
                    Object valor = getJTable().getModel().getValueAt(selRow, 0);
                    alterar = true;
                    jTextField.setText(valor.toString());
                    jButton.setText("Alterar");
                    jButtonRemove.setVisible(true);
                }
            }
        });
        // Criando nossa classe
        fileProperteis = new FileProperteis();
        props = fileProperteis.carregarParametros();
        for (int i = 0; i < props.size(); i++) {
            listaDeId.add(props.getProperty("painel " + i));
            modelo.addRow(new Object[] { props.getProperty("painel " + i) });
        }
        setVisible(true);
    }

    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            LineBorder border = new LineBorder(Color.BLUE, 1, true);
            TitledBorder tborder = new TitledBorder(border, "Cadastro", TitledBorder.LEFT, TitledBorder.TOP, new Font(
                "Arial", Font.ITALIC, 13), Color.BLUE);
            jPanel.setBorder(tborder);
            jPanel.setLayout(null);
            jPanel.setSize(new Dimension(395, 263));
            jPanel.add(getJTextFild(), null);
            jPanel.add(getJLabelConf());
            jPanel.add(getJScrollPane());
            jPanel.add(getJButtom());
            jPanel.add(getJButtomRemove());

        }
        return jPanel;
    }

    private JLabel getJLabelConf() {
        if (jLabelConf == null) {
            jLabelConf = new JLabel();
            jLabelConf.setBounds(new Rectangle(8, 15, 291, 25));
            jLabelConf.setText("Código:");
            jLabelConf.setFont(new Font("Arial", Font.PLAIN, 13));
        }
        return jLabelConf;
    }

    private JTextField getJTextFild() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setBounds(new Rectangle(8, 35, 210, 24));
            jTextField.setDocument(new DocumentoLimitado(10));
        }
        return jTextField;
    }

    //Tabela de dados 

    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(6, 100, 510, 200));
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

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
            jTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        }
        return jTable;
    }


    public JButton getJButtom() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Adicionar");
            jButton.setBounds(new Rectangle(8, 70, 85, 24));
            jButton.setVisible(true);
            jButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if(!(tamanhoNovo < 10)){
                        if (alterar) {
                            modelo.removeRow(selRow);
                            listaDeId.remove(selRow);
                            modelo.insertRow(selRow, new Object[] { jTextField.getText() });
                            listaDeId.add(jTextField.getText());
                            jTextField.setText("");
                            alterar = false;
                        } else {
                            modelo.addRow(new Object[] { jTextField.getText() });
                            listaDeId.add(jTextField.getText());
                            jTextField.setText("");
                            Object[] options = { "Sim", "Não" };
                            int n = JOptionPane.showOptionDialog(null, "Deseja cadastrar outro serial?", "Menssagem",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (n != 0) {
                                dispose();
                            }
                        }
                        fileProperteis.salvarParametros(listaDeId);
                        jButton.setText("Adicionar");
                        jButtonRemove.setVisible(false);
                    }else{
                        JOptionPane.showMessageDialog(null, "Deve ter no mínimo 10 caracteres!!", "Atenção",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }
        return jButton;
    }

    public JButton getJButtomRemove() {
        if (jButtonRemove == null) {
            jButtonRemove = new JButton();
            jButtonRemove.setText("Remover");
            jButtonRemove.setBounds(new Rectangle(100, 70, 85, 24));
            jButtonRemove.setVisible(false);
            jButtonRemove.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    modelo.removeRow(selRow);
                    listaDeId.remove(selRow);
                    jTextField.setText("");
                    jButtonRemove.setVisible(false);
                    jButton.setText("Adicionar");
                    alterar = false;

                }
            });
        }
        return jButtonRemove;
    }
    int tamanhoNovo;
    public class DocumentoLimitado extends PlainDocument {

        private static final String _CARACTER_PAINEL = "1234567890" + "ABCDEF" + "abcdef";

        private int tamanhoMax = 5;

        public DocumentoLimitado(int tamanhoMax) {
            this.tamanhoMax = tamanhoMax;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

            str = str.toUpperCase();
            if (str == null)
                return;

            if(!(str.length() > 1 &&  alterar)){
                if (_CARACTER_PAINEL.indexOf(str) == -1) {
                    JOptionPane.showMessageDialog(null, "Este caractere não é aceito no painel!!", "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                    super.remove(offset, 1);
                }  
            }

            String stringAntiga = getText(0, getLength());
            tamanhoNovo = stringAntiga.length() + str.length();

            if (tamanhoNovo <= tamanhoMax) {
                super.insertString(offset, str, attr);
            } else {
                super.insertString(offset, "", attr);
                JOptionPane.showMessageDialog(null, "Quantidade de caracteres limitado!!", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
