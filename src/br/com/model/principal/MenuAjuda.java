package br.com.model.principal;


import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MenuAjuda extends JInternalFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -5374755902903298777L;

    private JTree tree;

    private JTextArea fileDetailsTextArea = new JTextArea();

    public MenuAjuda() {
        super("Tela de Ajuda do Painel", false, true, true, true);
        this.setFrameIcon(new ImageIcon(this.getClass().getResource("/img/ledInternal.png")));
        fileDetailsTextArea.setEditable(false);
        //cria arvore principal
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Configuração e uso");
        tree = new JTree(root);
        createNodes(root);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent event) {
                String file = tree.getLastSelectedPathComponent().toString();
                fileDetailsTextArea.setText(getFileDetails(file));
            }
        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(tree),
            new JScrollPane(fileDetailsTextArea));
        getContentPane().add(splitPane);
        setVisible(true);
    }

    private String getFileDetails(String file) {
        if (file == null)
            return "";
        StringBuffer buffer = new StringBuffer();
        buffer.append("Name: " + file + "\n");
        return buffer.toString();
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;
        DefaultTreeCellRenderer renderer = null;
        ImageIcon imageIconPanel = new ImageIcon(MenuAjuda.class.getResource("/img/panel_tree.png"));
        ImageIcon imageIconConf = new ImageIcon(MenuAjuda.class.getResource("/img/conf_tree.png"));
        ImageIcon imageIconUser = new ImageIcon(MenuAjuda.class.getResource("/img/user_tree.png"));

        category = new DefaultMutableTreeNode("Configuração do Sistema");
        top.add(category);
        renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(imageIconPanel);
        renderer.setOpenIcon(imageIconConf);
        renderer.setLeafIcon(imageIconUser);
        tree.setCellRenderer(renderer);

        //configuração 
        book = new DefaultMutableTreeNode("Conectar painel");
        category.add(book);

        //Tutorial Continued
        book = new DefaultMutableTreeNode("Configuração");
        category.add(book);
        
        category = new DefaultMutableTreeNode("Como usar o sistema");
        top.add(category);

        //VM
        book = new DefaultMutableTreeNode("Tela de envio");
        category.add(book);

    }
}
