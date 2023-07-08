package aps.chat.app.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseService {
    
    public Statement stm;
    public ResultSet rs;
    public static Connection connection;
    String url = "jdbc:sqlserver://192.168.0.2:1433;databaseName=dbChat";   //alterar endereço IP do servidor de Banco de Dados se necessário
    
    public void conectaDB() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connection = DriverManager.getConnection(url, "tdnt2", "12345");    //alterar os dados da Instância do SQL Server se necessário
        System.out.println("Conectado ao banco de dados.");
    }
}
