package lk.ijse.assignment_11_backend.db;

import lk.ijse.assignment_11_backend.dto.CustomerDTO;
import lk.ijse.assignment_11_backend.dto.ItemDTO;
import lk.ijse.assignment_11_backend.dto.OrderDTO;
import lk.ijse.assignment_11_backend.dto.OrderDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBProcess {
    private static final String SAVE_DATA = "INSERT INTO customer(CUSTOMER_ID,NAME,ADDRESS,CONTACT) VALUES (?,?,?,?)";
    private static final String SAVE_ITEM_DATA = "INSERT INTO item(ITEM_ID, DESCR, PRICE, QTY) VALUES (?,?,?,?)";

    private static final String GET_ALL_CUSTOMER_DATA = "SELECT * FROM customer";
    private static final String UPDATE_CUSTOMER = "UPDATE customer SET name=? ,address=?, contact=? WHERE customer_id=?";
    private static final String DELETE_CUSTOMER = "DELETE FROM customer WHERE customer_id=?;";
    private static final String GET_ALL_ITEM_DATA = "SELECT * FROM item";
    private static final String UPDATE_ITEM = "UPDATE item SET descr=? ,price=?, qty=? WHERE item_id=?";

    private static final String DELETE_ITEM = "DELETE FROM item WHERE item_id=?;";

    private static final String SAVE_ORDER = "INSERT INTO order_details(order_id,customer_id,date,total) VALUES(?,?,?,?);";

    private static final String SAVE_ORDER_DETAILS = "INSERT INTO order_item(order_id,item_id,qty) VALUES(?,?,?);";

    private static final String GET_ALL_ORDERS = "SELECT * FROM order_details;";


    final static Logger logger = LoggerFactory.getLogger(DBProcess.class);

    public String saveCustomer(CustomerDTO customerDTO, Connection connection) {
        // save / manipulate data
        try {
            var ps = connection.prepareStatement(SAVE_DATA);
            ps.setString(1, customerDTO.getCustomer_id());
            ps.setString(2, customerDTO.getName());
            ps.setString(3, customerDTO.getAddress());
            ps.setString(4, customerDTO.getContact());

            if (ps.executeUpdate() != 0) {

                return "Data saved";
            } else {

                return "Failed to save data";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public String saveItem(ItemDTO itemDTO, Connection connection) {
        // save / manipulate data
        try (PreparedStatement ps = connection.prepareStatement(SAVE_ITEM_DATA)) {
            ps.setString(1, itemDTO.getItem_id());
            ps.setString(2, itemDTO.getDescr());
            ps.setDouble(3, itemDTO.getPrice());
            ps.setInt(4, itemDTO.getQty());

            if (ps.executeUpdate() != 0) {
                return "Data saved";
            } else {
                return "Failed to save data";
            }
        } catch (SQLException e) {
            logger.error("Error saving item data", e);
            throw new RuntimeException("Failed to save item data", e);
        }
    }


    public List<CustomerDTO> getCustomerData(Connection connection){
        //get data
        List<CustomerDTO> selectedCustomer = new ArrayList<>();
        try {
            var ps = connection.prepareStatement(GET_ALL_CUSTOMER_DATA);
            var rs = ps.executeQuery();

            while (rs.next()){
                selectedCustomer.add(new CustomerDTO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
            }
            return selectedCustomer;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateCustomer(CustomerDTO customerDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(UPDATE_CUSTOMER);

            ps.setString(1, customerDTO.getName());
            ps.setString(2, customerDTO.getAddress());
            ps.setString(3, customerDTO.getContact());
            ps.setString(4, customerDTO.getCustomer_id());

            if (ps.executeUpdate() != 0) {

                return "Data Updates Successfully";
            } else {

                return "Failed to update data";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public String deleteCustomer(CustomerDTO customerDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(DELETE_CUSTOMER);
            ps.setString(1, customerDTO.getCustomer_id());
            if (ps.executeUpdate() != 0) {

                return "Data Deleted Successfully";
            } else {

                return "Failed to delete data";
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public List<ItemDTO> getItemData(Connection connection) {

        //get data
        List<ItemDTO> selectedItem = new ArrayList<>();
        try {
            var ps = connection.prepareStatement(GET_ALL_ITEM_DATA);
            var rs = ps.executeQuery();

            while (rs.next()){
                selectedItem.add(new ItemDTO(rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getInt(4)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedItem;

    }

    public String updateItem(ItemDTO itemDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(UPDATE_ITEM);

            ps.setString(1, itemDTO.getDescr());
            ps.setDouble(2, itemDTO.getPrice());
            ps.setInt(3, itemDTO.getQty());
            ps.setString(4, itemDTO.getItem_id());

            if (ps.executeUpdate() != 0) {

                return "Data Updates Successfully";
            } else {

                return "Failed to update data";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String deleteItem(ItemDTO itemDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(DELETE_ITEM);
            ps.setString(1, itemDTO.getItem_id());
            if (ps.executeUpdate() != 0) {

                return "Data Deleted Successfully";
            } else {

                return "Failed to delete data";
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean saveOrder(OrderDTO orderDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(SAVE_ORDER);
            ps.setString(1, orderDTO.getOrder_id());
            ps.setString(2, orderDTO.getCustomer_id());
            ps.setString(3, orderDTO.getDate());
            ps.setString(4, orderDTO.getTotal());

            if (ps.executeUpdate() != 0) {
                logger.info("Order saved successfully");
                System.out.println("Data saved");
                return true;
            } else {
                logger.info("Order saving failed");
                System.out.println("Failed to save");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean saveOrderDetails(OrderDTO orderDTO, Connection connection){
        try {
            var ps = connection.prepareStatement(SAVE_ORDER_DETAILS);
            for (ItemDTO itemDTO : orderDTO.getItems()) {
                ps.setString(1, orderDTO.getOrder_id());
                ps.setString(2, itemDTO.getItem_id());
                ps.setString(3, String.valueOf(itemDTO.getQty()));

                if (ps.executeUpdate() == 0) {
                    logger.info("Order details saving failed");
                    System.out.println("Failed to save");
                    return false;
                }
            }
            logger.info("Order details saved successfully");
            System.out.println("Data saved");
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDetailsDTO> getAllOrders(Connection connection) {
        List<OrderDetailsDTO> orderDTOS = new ArrayList<>();

        try {
            var ps = connection.prepareStatement(GET_ALL_ORDERS);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                orderDTOS.add(new OrderDetailsDTO(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderDTOS;
    }

}

