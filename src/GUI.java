
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.Vector;

public class GUI
{
    public JPanel window;
    private JPanel top;
    private JPanel buttons;
    private JPanel result;
    private JPanel info;
    private JPanel cmd;
    private JLabel dInfo;
    private JLabel sqlCMD;
    private JLabel driver;
    private JLabel url;
    private JLabel user;
    private JLabel pass;
    private JComboBox drivers;
    private JComboBox urls;
    private JTextField passText;
    private JTextField userText;
    private JTextArea sql;
    private JTextField status;
    private JButton connect;
    private JButton clear;
    private JButton Execute;
    private JLabel resultWindow;
    private JTable results;
    private JButton clearR;
    private MysqlDataSource source;
    private boolean connected = false;
    private Connection connection;

    public GUI()
    {
        this.source = new MysqlDataSource();

        this.clear.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sql.setText("");
            }
        });

        connect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                source.setURL(Objects.requireNonNull(urls.getSelectedItem()).toString());
                source.setUser(userText.getText());
                source.setPassword(passText.getText());
                try {
                    Class.forName(Objects.requireNonNull(drivers.getSelectedItem()).toString());
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
                try
                {
                    connection = source.getConnection();
                    connected = true;
                    status.setText("Connected to " + Objects.requireNonNull(urls.getSelectedItem()).toString());
                }
                catch (Exception exception)
                {
                    JOptionPane.showMessageDialog(window, exception.toString(), null, JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        Execute.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!connected)
                {
                    JOptionPane.showMessageDialog(window, "Database not connected", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (sql.getText().startsWith("select"))
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        ResultSet result = statement.executeQuery(sql.getText());
                        ResultSetMetaData data = result.getMetaData();
                        int j = data.getColumnCount();
                        Vector<String> names = new Vector<>();
                        Vector<Vector<String>> rows = new Vector<>();
                        for (int i = 1; i <= j; i++)
                        {
                            names.add(data.getColumnName(i));
                        }
                        while (result.next())
                        {
                            Vector<String> row = new Vector<>();
                            for (int i = 1; i <=j; i++)
                            {
                                row.add(result.getString(i));
                            }
                            rows.add(row);
                        }
                        results.setModel(new DefaultTableModel(rows, names));
                    }
                    catch (SQLException throwables)
                    {
                        JOptionPane.showMessageDialog(window, throwables.toString(), null, JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    try
                    {
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sql.getText());
                        results.setModel(new DefaultTableModel(new String[]{"Updated"},0));
                    }
                    catch (SQLException throwables)
                    {
                        JOptionPane.showMessageDialog(window, throwables.toString(), null, JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        clearR.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                results.setModel(new DefaultTableModel());
            }
        });
    }

    public void close() throws Exception
    {
        this.connection.close();
    }
}
