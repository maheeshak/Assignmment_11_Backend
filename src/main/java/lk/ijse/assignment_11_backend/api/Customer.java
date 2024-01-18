package lk.ijse.assignment_11_backend.api;

import jakarta.json.Json;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.assignment_11_backend.db.DBProcess;
import lk.ijse.assignment_11_backend.dto.CustomerDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name ="customer" ,urlPatterns = "/customer",
initParams = {
        //create the path for the database connection...
        @WebInitParam(name = "db-user",value = "root"),
        @WebInitParam(name = "db-pw",value = "1234"),
        @WebInitParam(name = "db-url",value = "jdbc:mysql://localhost:3306/pos?createDatabaseIfNotExist=true"),
        @WebInitParam(name = "db-class",value = "com.mysql.cj.jdbc.Driver")

})
public class Customer extends HttpServlet {
    Connection connection;

    @Override
    public void init() throws ServletException {
        //Get connection from the connection pool...
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource= (DataSource) initialContext.lookup("java:comp/env/jdbc/pos");
            System.out.println(dataSource);
            this.connection=dataSource.getConnection();
            System.out.println(connection);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else{
            Jsonb jsonb = JsonbBuilder.create();
            var customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            var dbProcess = new DBProcess();
           String result =  dbProcess.saveCustomer(customerDTO,connection);
            System.out.println(result);

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            var writer = resp.getWriter();
            resp.setContentType("text/html");
            var data = new DBProcess();
            List <CustomerDTO>  getData = data.getCustomerData(connection);
            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(getData);
            writer.write(json);
            writer.close();

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else{
            Jsonb jsonb = JsonbBuilder.create();
            var customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            var dbProcess = new DBProcess();
            String result =  dbProcess.updateCustomer(customerDTO,connection);
            System.out.println(result);

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else{
            Jsonb jsonb = JsonbBuilder.create();
            var customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            var dbProcess = new DBProcess();
            String result =  dbProcess.deleteCustomer(customerDTO,connection);
            System.out.println(result);

        }
    }
}

