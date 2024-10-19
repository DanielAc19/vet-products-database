import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/veterinaria_vidas_peludas";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void insertarProducto(String codigo, String nombre, double precio, int cantidad, String fecha) {
        String query = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?,?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigo);
            pst.setString(2, nombre);
            pst.setDouble(3, precio);
            pst.setInt(4, cantidad);
            pst.setDate(5, java.sql.Date.valueOf(fecha));
            pst.executeUpdate();
            System.out.println("Producto insertado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarProducto(String codigoProducto, String nombre, double precio) {
        String query = "UPDATE producto SET nombreProducto = ?, precioUnitario = ? WHERE codigoProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nombre);
            pst.setDouble(2, precio);
            pst.setString(3, codigoProducto);
            int filasActualizadas = pst.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Producto actualizado correctamente");
            } else {
                System.out.println("No se encontró ningún producto con ese código");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void eliminarProducto(String codigoProducto) {
        String query = "DELETE FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            int filasEliminadas = pst.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Producto eliminado correctamente");
            } else {
                System.out.println("No se encontró ningún producto con ese código");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void buscarProducto(String codigoProducto) {
        String query = "SELECT * FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                System.out.println("Código: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: " + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
            } else {
                System.out.println("No se encontró ningún producto con ese código");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarProductos() {
        String query = "SELECT * FROM producto";
        try (Connection con = ConexionBD.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.println("Código: " + rs.getString("codigoProducto"));
                System.out.println("Nombre: " + rs.getString("nombreProducto"));
                System.out.println("Precio: " + rs.getDouble("precioUnitario"));
                System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
                System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
                System.out.println("");
            }
            if (!hayResultados) {
                System.out.println("No hay productos disponibles.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("-------Menú-------");
            System.out.println("1. Insertar Producto");
            System.out.println("2. Actualizar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Buscar Producto");
            System.out.println("5. Mostrar Productos");
            System.out.println("6. Salir");
            System.out.print("Elija una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese código del producto: ");
                    String codigo = scanner.nextLine();
                    System.out.print("Ingrese nombre del producto: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese precio del producto: ");
                    double precio = scanner.nextDouble();
                    System.out.print("Ingrese cantidad del producto: ");
                    int cantidad = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("Ingrese fecha de vencimiento (YYYY-MM-DD): ");
                    String fecha = scanner.nextLine();
                    insertarProducto(codigo, nombre, precio, cantidad, fecha);
                    break;

                case 2:
                    System.out.print("Ingrese código del producto a actualizar: ");
                    String codigoActualizar = scanner.nextLine();
                    System.out.print("Ingrese nuevo nombre del producto: ");
                    String nombreNuevo = scanner.nextLine();
                    System.out.print("Ingrese nuevo precio del producto: ");
                    double precioNuevo = scanner.nextDouble();
                    actualizarProducto(codigoActualizar, nombreNuevo, precioNuevo);
                    break;

                case 3:
                    System.out.print("Ingrese código del producto a eliminar: ");
                    String codigoEliminar = scanner.nextLine();
                    eliminarProducto(codigoEliminar);
                    break;

                case 4:
                    System.out.print("Ingrese código del producto a buscar: ");
                    String codigoBuscar = scanner.nextLine();
                    buscarProducto(codigoBuscar);
                    break;

                case 5:
                    listarProductos();
                    break;

                case 6:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida");
            }

            System.out.println();

        } while (opcion != 6);

        scanner.close();
    }

    public static void main(String[] args) {
        menu();
    }
}



