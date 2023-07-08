package aps.chat.app.userdata;

import static aps.chat.app.service.DatabaseService.connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertUser {
    
    Long id;
    String nome;
    String data_nascimento;
    String username;
    String senha;
    
    public void addUser(User user) {
        
        String sql = "INSERT INTO Usuario(nome,data_nascimento,username,senha) VALUES(?,?,?,?)";
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, user.getNome());
            st.setString(2, user.getData_nascimento());
            st.setString(3, user.getUsername());
            st.setString(4, user.getSenha());
            st.execute();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(InsertUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
