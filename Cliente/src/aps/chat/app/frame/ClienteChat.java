package aps.chat.app.frame;

import aps.chat.app.bean.ChatMessage;
import aps.chat.app.bean.ChatMessage.Action;
import aps.chat.app.service.ClienteService;
import aps.chat.app.service.DatabaseService;
import static aps.chat.app.service.DatabaseService.connection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

public class ClienteChat extends javax.swing.JFrame {

    private Socket socket;
    private ChatMessage message;
    private ClienteService service;
    List listaPalavroes = new ArrayList();
    List listaOK = new ArrayList();

    DatabaseService db = new DatabaseService();

    /**
     * Creates new form ClienteFrame
     */
    public ClienteChat() {
        initComponents();
        this.setLocationRelativeTo(null);

        try {
            db.conectaDB();
        } catch (Exception ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Statement st1 = connection.createStatement();
            Statement st2 = connection.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT * FROM Palavras");
            ResultSet rs2 = st2.executeQuery("SELECT * FROM PalavrasOK");

            while (rs1.next()) {
                listaPalavroes.add(rs1.getString("palavra"));
            }

            while (rs2.next()) {
                listaOK.add(rs2.getString("palavra"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {

            ChatMessage message = null;

            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();

                    switch (action) {
                        case CONNECT:
                            connected(message);
                            break;
                        case DISCONNECT:
                            disconnected();
                            socket.close();
                            break;
                        case SEND_ONE:
                            receive(message);
                            break;
                        case USERS_ONLINE:
                            refresh(message);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void connected(ChatMessage message) {
        if (message.getText().equals("NO")) {
            JOptionPane.showMessageDialog(this, "Usuário " + txtName.getText() + " já está conectado!\nTente novamente com outro nome de usuário");
            this.txtName.setText("");
            this.txtSenha.setText("");
            return;
        }

        this.message = message;

        this.txtName.setEditable(false);
        this.txtSenha.setText("");
        this.txtSenha.setEnabled(false);

        this.btConectar.setEnabled(false);
        this.btSair.setEnabled(true);
        this.txtSend.setEnabled(true);
        this.txtReceive.setEnabled(true);
        this.btEnviar.setEnabled(true);
        this.btLimpar.setEnabled(true);

        JOptionPane.showMessageDialog(this, "Conexão realizada com sucesso!\nAgora você já está conectado no chat.");
        System.out.println("Conectado ao chat com sucesso.");
    }

    private void disconnected() {

        this.txtName.setEditable(true);
        this.txtName.setText("");
        this.txtSenha.setEnabled(true);

        this.btConectar.setEnabled(true);
        this.btSair.setEnabled(false);
        this.txtSend.setEnabled(false);
        this.txtReceive.setEnabled(false);
        this.btEnviar.setEnabled(false);
        this.btLimpar.setEnabled(false);

        this.txtReceive.setText("");
        this.txtSend.setText("");

        JOptionPane.showMessageDialog(this, "Você saiu do chat!");
    }

    private void receive(ChatMessage message) {

        this.txtReceive.append(message.getName() + ": " + message.getText() + "\n");
    }

    private void refresh(ChatMessage message) {

        Set<String> names = message.getSetOnlines();
        names.remove(message.getName());
        String[] array = (String[]) names.toArray(new String[names.size()]);

        this.txtOnlineList.setListData(array);
        this.txtOnlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.txtOnlineList.setLayoutOrientation(JList.VERTICAL);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        btConectar = new javax.swing.JButton();
        btSair = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtOnlineList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtReceive = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSend = new javax.swing.JTextArea();
        btEnviar = new javax.swing.JButton();
        btLimpar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        arquivo = new javax.swing.JMenu();
        menuItemNovoUsuario = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItemSair = new javax.swing.JMenuItem();
        sobre = new javax.swing.JMenu();
        itemMenuSobre = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("APS - 5º Semestre - Chat");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        btConectar.setText("Conectar");
        btConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConectarActionPerformed(evt);
            }
        });

        btSair.setText("Sair");
        btSair.setEnabled(false);
        btSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSairActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Login:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Senha:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSenha))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addComponent(btConectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSair)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btConectar)
                            .addComponent(btSair))))
                .addGap(213, 213, 213))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Online"));

        jScrollPane3.setViewportView(txtOnlineList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtReceive.setEditable(false);
        txtReceive.setColumns(20);
        txtReceive.setRows(5);
        txtReceive.setEnabled(false);
        jScrollPane1.setViewportView(txtReceive);

        txtSend.setColumns(20);
        txtSend.setRows(5);
        txtSend.setEnabled(false);
        jScrollPane2.setViewportView(txtSend);

        btEnviar.setText("Enviar");
        btEnviar.setEnabled(false);
        btEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEnviarActionPerformed(evt);
            }
        });

        btLimpar.setText("Limpar");
        btLimpar.setEnabled(false);
        btLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEnviar)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btEnviar)
                    .addComponent(btLimpar))
                .addGap(23, 23, 23))
        );

        arquivo.setText("Arquivo");

        menuItemNovoUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuItemNovoUsuario.setText("Novo Usuário");
        menuItemNovoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNovoUsuarioActionPerformed(evt);
            }
        });
        arquivo.add(menuItemNovoUsuario);
        arquivo.add(jSeparator1);

        menuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSair.setText("Sair");
        menuItemSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSairActionPerformed(evt);
            }
        });
        arquivo.add(menuItemSair);

        jMenuBar1.add(arquivo);

        sobre.setText("Sobre");

        itemMenuSobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        itemMenuSobre.setText("Sobre");
        itemMenuSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuSobreActionPerformed(evt);
            }
        });
        sobre.add(itemMenuSobre);

        jMenuBar1.add(sobre);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConectarActionPerformed

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Usuario where username = '" + txtName.getText() + "'");
            rs.next();

            if (rs.getString("senha").equals(txtSenha.getText())) {
                String name = txtName.getText();

                this.message = new ChatMessage();
                this.message.setAction(Action.CONNECT);
                this.message.setName(name);

                this.service = new ClienteService();
                this.socket = this.service.connect();

                new Thread(new ListenerSocket(this.socket)).start();

                this.service.send(message);
            } else {
                JOptionPane.showMessageDialog(null, "Senha ou usuário inválidos.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Senha ou usuário inválidos.");
        }
    }//GEN-LAST:event_btConectarActionPerformed

    private void btSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSairActionPerformed
        // TODO add your handling code here:
        ChatMessage message = new ChatMessage();
        message.setName(this.message.getName());
        message.setAction(Action.DISCONNECT);
        this.service.send(message);
        disconnected();
    }//GEN-LAST:event_btSairActionPerformed

    private void btLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparActionPerformed
        // TODO add your handling code here:
        this.txtSend.setText("");
    }//GEN-LAST:event_btLimparActionPerformed

    private void btEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEnviarActionPerformed
        // TODO add your handling code here:

        String palavra = null;
        int contador = 0;

        //Detecta palavras impróprias
        for (int i = 0; i < listaPalavroes.size(); i++) {
            if (txtSend.getText().toLowerCase().contains((CharSequence) listaPalavroes.get(i))) {
                for (int j = 0; j < listaOK.size(); j++) {
                    if (txtSend.getText().toLowerCase().contains((CharSequence) listaOK.get(j))) {
                        break;
                    }
                    if (contador == listaOK.size() - 1) {
                        palavra = (String) listaPalavroes.get(i);
                        JOptionPane.showMessageDialog(this, "Hum...\nAlguma palavra que você digitou é considerada imprópria =(", "MENSAGEM NÃO ENVIADA", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                    contador++;
                }
                break;
            }
        }

        if (palavra == null) {

            String text = this.txtSend.getText();
            String name = this.message.getName();

            this.message = new ChatMessage();

            if (this.txtOnlineList.getSelectedIndex() > -1) {   //-1 indica que tem usuario da lista de onlines selecionado (lista começa no index [0])
                this.message.setNameReserved((String) this.txtOnlineList.getSelectedValue());
                this.message.setAction(Action.SEND_ONE);
                this.txtOnlineList.clearSelection();
            } else {
                this.message.setAction(Action.SEND_ALL);
            }

            if (!text.isEmpty()) {
                this.message.setName(name);
                this.message.setText(text);

                this.txtReceive.append("Você: " + text + "\n");

                this.service.send(this.message);
            }

            this.txtSend.setText("");
    }//GEN-LAST:event_btEnviarActionPerformed

    }
    private void menuItemNovoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNovoUsuarioActionPerformed
        // TODO add your handling code here:
        ClienteCadastro telaCadastro = new ClienteCadastro();
        telaCadastro.setVisible(true);
    }//GEN-LAST:event_menuItemNovoUsuarioActionPerformed

    private void menuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSairActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_menuItemSairActionPerformed

    private void itemMenuSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuSobreActionPerformed
        // TODO add your handling code here:
        About telaSobre = new About();
        telaSobre.setVisible(true);
    }//GEN-LAST:event_itemMenuSobreActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu arquivo;
    private javax.swing.JButton btConectar;
    private javax.swing.JButton btEnviar;
    private javax.swing.JButton btLimpar;
    private javax.swing.JButton btSair;
    private javax.swing.JMenuItem itemMenuSobre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem menuItemNovoUsuario;
    private javax.swing.JMenuItem menuItemSair;
    private javax.swing.JMenu sobre;
    private javax.swing.JTextField txtName;
    private javax.swing.JList<String> txtOnlineList;
    private javax.swing.JTextArea txtReceive;
    private javax.swing.JTextArea txtSend;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables
}
