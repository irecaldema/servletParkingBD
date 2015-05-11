//import com.zubiri.parking.ParkingVehiculos;
//import com.zubiri.parking.Vehiculo;
//import com.zubiri.parking.Coche;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GestiorParking
 */
@WebServlet(
		description = "gestion del parking", 
		urlPatterns = { "/Gestor" }				
		)
public class ParkingBD extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String USUARIO="root";
	private static final String CONTRA="zubiri";
	static final String URL_BD="jdbc:mysql://localhost/pruebas_java";
	/**
     * @see HttpServlet#HttpServlet()
     */

	public ParkingBD() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType( "text/html; charset=iso-8859-1" );
				
		Connection con = null;	
		Statement sentencia = null;
		try {
			System.out.println("En el try crear");
			
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);

			sentencia = con.createStatement();	        
			String sql;

			sql="CREATE TABLE IF NOT EXISTS coches (matricula VARCHAR(7),marca VARCHAR(20),motor BOOLEAN,automatico BOOLEAN,n_ruedas INTEGER(2),consumo INTEGER(3), PRIMARY KEY (matricula))";

			/*CREATE TABLE coches (
				matricula VARCHAR(7),
				marca VARCHAR(20),
				motor BOOLEAN,
				automatico BOOLEAN,
				n_ruedas INTEGER(2),
				consumo INTEGER(3),
				PRIMARY KEY (matricula)
			);*/

			sentencia.executeUpdate(sql);
		} catch(Exception e) {
			System.out.println("En el catch 1");
			System.err.println("Error "+ e);
		}

		String gestion=request.getParameter("gestion");
		System.out.println(gestion);
		if (gestion.equals("mostrar_vehiculos")) {
			System.out.println("Empieza mostrando");

			try {
				System.out.println("En el try mostrar");
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// Open a connection
				con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);

				sentencia = con.createStatement();

				String sql;		        
				sql="SELECT matricula, marca FROM coches";
				ResultSet mostrar = sentencia.executeQuery(sql);
				int cont=0;
				String [] matricula = new String[100];
				String [] marca = new String[100];
				System.out.println("Pre while");
				while (mostrar.next()) {	
					System.out.println("Matrícula:"+mostrar.getString("matricula"));
					matricula[cont] = mostrar.getString("matricula");
					System.out.println("Matrícula "+matricula[cont]);
					marca[cont] = mostrar.getString("marca");
					System.out.println("Marca "+marca[cont]);
					cont++;
				}
				System.out.println("Post while");

				con.close();    
				response(response,matricula,marca);

			} catch(Exception e) {
				System.out.println("En el catch 2");
				System.err.println("Error "+ e);
			}

		} else if (gestion.equals("buscar_matricula")) {
			System.out.println("Empieza buscando");
			String referencia=request.getParameter("matricula");
			
			try {
				
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// Open a connection
				con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);
				
				sentencia = con.createStatement();
				
				String sql;		    
				System.out.println("Referencia: "+referencia);		     
				//sql="SELECT matricula, marca FROM coches WHERE matricula='"+referencia+"'";
				sql="SELECT * FROM coches WHERE matricula=\""+referencia+"\"";
				
				ResultSet buscar = sentencia.executeQuery(sql);
				int cont = 0;
				String matricula = null;
				String marca = null;
				Boolean motor = false;
				Boolean automatico = false;
				Integer n_ruedas = 0;
				Integer consumo = 0;
				while (buscar.next()) {
					matricula = buscar.getString("matricula");
					marca = buscar.getString("marca");
					motor = buscar.getBoolean("motor");
					automatico = buscar.getBoolean("automatico");
					n_ruedas = buscar.getInt("n_ruedas");
					consumo = buscar.getInt("consumo");
					System.out.println("Matrícula: "+matricula);
					System.out.println("Marca: "+marca);
					//response(response,matricula,marca,motor,automatico,n_ruedas,consumo);
					cont++;
				}
				if (cont > 0) {
					response(response,matricula,marca,motor,automatico,n_ruedas,consumo);
				} else {
					response(response, "No se encontró el vehículo");
				}
				con.close();    
			
			} catch(ArrayIndexOutOfBoundsException e) {
				//response(response, "no se encontro el vehiculo");
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if (gestion.equals("anyadir_vehiculo")) {
			System.out.println("Empieza añadiendo");
			int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
			boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
			String marca = request.getParameter("marca");
			String matricula = request.getParameter("matricula");
			boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
			int consumo = Integer.parseInt(request.getParameter("consumo"));
			System.out.println("Nuevo coche");

			try {
				
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// Open a connection
				con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);
				
				sentencia = con.createStatement();
				
				String sql;
				//INSERT INTO coches VALUES ("0000AAA", "prueba1", true, true, 4, 100);
				System.out.println("INSERT INTO coches VALUES (\""+matricula+"\",\""+marca+"\","+motor+","+automatico+","+n_ruedas+","+consumo+")");
				sql="INSERT INTO coches VALUES (\""+matricula+"\",\""+marca+"\","+motor+","+automatico+","+n_ruedas+","+consumo+")";
				int crear = sentencia.executeUpdate(sql);
				System.out.println("Valor crear: "+crear);
				
				sql="SELECT * FROM coches WHERE matricula='"+matricula+"'";
				ResultSet buscar = sentencia.executeQuery(sql);
				while (buscar.next()) {
					String encontrado = buscar.getString("matricula");
					if (encontrado!=null) {			        	
						String matricula2 = buscar.getString("matricula");
						String marca2 = buscar.getString("marca");
						Boolean motor2 = buscar.getBoolean("motor");
						Boolean automatico2 = buscar.getBoolean("automatico");
						Integer n_ruedas2 = buscar.getInt("n_ruedas");
						Integer consumo2 = buscar.getInt("consumo");
						System.out.println("Matrícula: "+matricula2);
						System.out.println("Marca: "+marca2);
						response(response,matricula2,marca2,motor2,automatico2,n_ruedas2,consumo2);
					} else {
						response(response, "Error al añadir vehículo");
					}
				}
				con.close();    
				
			} catch(ArrayIndexOutOfBoundsException e) {
				//response(response, "no se encontro el vehiculo");
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(gestion.equals("borrar_vehiculo")) {
			System.out.println("Borrando");
			String sentenciado = request.getParameter("matricula");
			Boolean confirmacion = Boolean.parseBoolean(request.getParameter("confirmacion"));

			try {
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// Open a connection
				con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);
				
				sentencia = con.createStatement();
				
				String sql;		    
				System.out.println("Referencia: "+sentenciado);		     
				//sql="SELECT matricula, marca FROM coches WHERE matricula='"+referencia+"'";
				sql="SELECT * FROM coches WHERE matricula=\""+sentenciado+"\"";
				
				ResultSet buscar = sentencia.executeQuery(sql);
				int cont = 0;
				while (buscar.next()) {
					cont++;
				}
				if (cont > 0) {
					System.out.println("Contador: " + cont);
					if (confirmacion!=true) {
						confirmacion=false;
						response(response, "¿Seguro que quieres borrar el vehículo?", sentenciado);
					} else {
						try {					
							// Register JDBC driver
							Class.forName("com.mysql.jdbc.Driver");
							// Open a connection
							con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);			        
							sentencia = con.createStatement();
							
							//String sql;
							//INSERT INTO coches VALUES ("0000AAA", "prueba1", true, true, 4, 100);
							//DELETE FROM coches where matricula="0000AAA";
							System.out.println("DELETE FROM coches where matricula=\""+sentenciado+"\"");
							sql="DELETE FROM coches where matricula=\""+sentenciado+"\"";
							int borrar = sentencia.executeUpdate(sql);
							System.out.println("Valor borrar: "+borrar);
							if (borrar==1) {
								response(response, "Se ha borrado el vehículo");
							} else {
								response(response, "No se ha borrado el vehículo, compruebe la matrícula: "+sentenciado+".");
							}
							con.close();			    	
						} catch(ArrayIndexOutOfBoundsException e) {
							//response(response, "no se encontro el vehiculo");
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					response(response, "No se encontró el vehículo");
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				//response(response, "no se encontro el vehiculo");
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if (gestion.equals("modificar_vehiculo")) {
			System.out.println("Empieza modificando");

			Boolean confirmacion = Boolean.parseBoolean(request.getParameter("confirmacion"));
			String matriculavieja1 = request.getParameter("matriculavieja");
			
			try {
				
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// Open a connection
				con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);
				
				sentencia = con.createStatement();
				
				String sql;		    
				System.out.println("Referencia: "+matriculavieja1);		     
				//sql="SELECT matricula, marca FROM coches WHERE matricula='"+referencia+"'";
				sql="SELECT * FROM coches WHERE matricula=\""+matriculavieja1+"\"";
				
				ResultSet buscar = sentencia.executeQuery(sql);
				int cont = 0;
				while (buscar.next()) {
					cont++;
				}
				if (cont > 0) {
					System.out.println("Contador: " + cont);
					if (confirmacion!=true) {
						formulario_modificar(response,request.getParameter("matriculavieja"));
					} else {
						int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
						boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
						String marca = request.getParameter("marca");
						String matriculanueva = request.getParameter("matriculanueva");
						String matriculavieja = request.getParameter("matriculavieja");
						boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
						int consumo = Integer.parseInt(request.getParameter("consumo"));
						String cambios="";
						cambios="matricula = \""+matriculanueva+"\",";
						cambios+=" marca = \""+marca+"\",";
						cambios+=" motor = "+motor+",";
						cambios+=" automatico = "+automatico+",";
						cambios+=" n_ruedas = \""+n_ruedas+"\",";
						cambios+=" consumo = \""+consumo+"\"";
						
						try {
							// Register JDBC driver
							Class.forName("com.mysql.jdbc.Driver");
							// Open a connection
							con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);			        
							sentencia = con.createStatement();
							
							//String sql;
							//INSERT INTO coches VALUES ("0000AAA", "prueba1", true, true, 4, 100);
							//DELETE FROM coches where matricula="0000AAA";
							System.out.println("UPDATE coches SET "+cambios+" WHERE matricula=\""+matriculavieja+"\"");
							sql="UPDATE coches SET "+cambios+" WHERE matricula=\""+matriculavieja+"\"";
							int modificar = sentencia.executeUpdate(sql);
							System.out.println("Valor crear: " + modificar);
							if (modificar==1) {
								response(response, "Se ha modificado el vehículo " + matriculavieja + ".<br>" + cambios);
							} else {
								response(response, "¡Error! No se ha modificado el vehículo, compruebe las matrícula1: "+matriculavieja+" matricula2 "+matriculanueva );
							}
							con.close();
						} catch(ArrayIndexOutOfBoundsException e) {
							//response(response, "no se encontro el vehiculo");
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					response(response, "No se encontró el vehículo");
				}
				con.close();
			
			} catch(ArrayIndexOutOfBoundsException e) {
				//response(response, "no se encontro el vehiculo");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Fin");
	}

	// Mostrar
	private void response(HttpServletResponse response, String [] matricula, String [] marca) throws IOException {
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
			out.println("<title> Vehículos </title>");
			out.println("<link rel='stylesheet' type='text/css' href='stylebd.css'>");
		out.println("</head>");
		out.println("<body>");
		//out.println("<p>-------------------------------</p>");
		for (int i=0; i<matricula.length; i++) {	
			if (matricula[i] == null) {
				break;
			} else {
				out.println("<p>-------------------------------</p>");
				out.println("<p> <b>Matrícula:</b> " + matricula[i] + " | ");
				out.print(" <b>Marca:</b> " + marca[i] + "</p>");
				out.println("<p>-------------------------------</p>");
			}
		}
		out.println("<a href='index.html'> <button> Volver </button> </a>");
		out.println("</body>");
		out.println("</html>");
	}
	
	// Respuesta simple
	private void response(HttpServletResponse response,String msg) throws IOException {
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
			out.println("<title> Respuesta </title>");
			out.println("<link rel='stylesheet' type='text/css' href='stylebd.css'>");
		out.println("</head>");
		out.println("<body>");				
		out.println("<p>" + msg + "</p>");
		out.println("<a href='index.html'> <button> Volver </button> </a>");
		out.println("</body>");
		out.println("</html>");
	}

	// Buscar y Añadir
	private void response(HttpServletResponse response, String matricula, String marca,Boolean motor,Boolean automatico,Integer n_ruedas,Integer consumo) throws IOException {
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
			out.println("<title>  </title>");
			out.println("<link rel='stylesheet' type='text/css' href='stylebd.css'>");
		out.println("</head>");
		out.println("<body>");
		out.println("<table align=\"center\" border=5><tr>");
			out.println("<th>Matrícula</th>");
			out.println("<th>Marca</th>");
			out.println("<th>Motor</th>");
			out.println("<th>Automático</th>");
			out.println("<th>Número de ruedas</th>");
			out.println("<th>Consumo</th>");
		out.println("</tr><tr>");
			out.println("<td>" + matricula + "</td>");
			out.println("<td>" + marca + "</td>");
			if (motor) {
				out.println("<td>Sí</td>");
			} else {
				out.println("<td>No</td>");
			}			
			if (automatico) {
				out.println("<td>Sí</td>");
			} else {
				out.println("<td>No</td>");
			}			
			out.println("<td>" + n_ruedas + "</td>");
			out.println("<td>" + consumo + "</td>");
		out.println("</tr><tr>");
			out.println("<td colspan=6>");
				out.println("<center> <a href='index.html'> <button> Volver </button> </a> </center>");
			out.println("</td>");
		out.println("</tr></table>");
		out.println("</body>");
		out.println("</html>");
	}

	// Borrar
	private void response(HttpServletResponse response,String msg ,String matricula) throws IOException {
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
			out.println("<title> Borrar </title>");
			out.println("<link rel='stylesheet' type='text/css' href='stylebd.css'>");
		out.println("</head>");
		out.println("<body align='center'>");
		out.println("<p>" + msg + "</p>");
		out.println("<p>Matrícula: " + matricula + "</p>");
		out.println("<form name=\"borrar_vehiculo\" method=\"post\" action=\"GestorBD\" style='margin-right: auto;'>");
			out.println("<input name='gestion' hidden='true' type='text'  value='borrar_vehiculo'/>");
			out.println("<input name=\"matricula\" hidden=\"true\" type=\"text\"  value=" + matricula + "></input>");
			out.println("<input name=\"confirmacion\" hidden=\"true\" type=\"text\"  value='true'></input>");
			out.println("<p> <input type='submit' id='submit' value='Borrar'> </p>");
		out.println("</form>");
		out.println("<a href='index.html'> <button> Volver </button> </a>");
		out.println("</body>");
		out.println("</html>");
	}	
	
	// Modificar
	private void formulario_modificar(HttpServletResponse response, String referencia) throws IOException {
		response.setContentType( "text/html; charset=iso-8859-1" );
		System.out.println("Se está modificando el vehículo con matricula: " + referencia);

		Connection con = null;	
		Statement sentencia = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);
			
			sentencia = con.createStatement();
			
			String sql;		    
			System.out.println("Referencia: "+referencia);		     
			//sql="SELECT matricula, marca FROM coches WHERE matricula='"+referencia+"'";
			sql="SELECT * FROM coches WHERE matricula=\""+referencia+"\"";
			ResultSet buscar = sentencia.executeQuery(sql);
			String matricula = null;
			String marca = null;
			Boolean motor = false;
			Boolean automatico = false;
			Integer n_ruedas = 0;
			Integer consumo = 0; 
			while (buscar.next()) {
				matricula = buscar.getString("matricula");
				marca = buscar.getString("marca");
				motor = buscar.getBoolean("motor");
				automatico = buscar.getBoolean("automatico");
				n_ruedas = buscar.getInt("n_ruedas");
				consumo = buscar.getInt("consumo");
				System.out.println("Matrícula: " + matricula);
				System.out.println("Marca: " + marca);
			}

			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
				out.println("<title> Modificar </title>");
				out.println("<link rel='stylesheet' type='text/css' href='stylebd.css'>");
			out.println("</head>");
			out.println("<body>");
			out.println("<fieldset>	<legend> Modificar Vehículo " + referencia + "</legend>");
				out.println("<form name='modificar_vehiculo' method='post' onsubmit='return validacion_modificar_vehiculo_bd()' action='GestorBD'>");
					out.println("<input name='gestion' hidden='true' type='text' value='modificar_vehiculo'/>");
					out.println("<input name='matriculavieja' type='text' value='" + referencia + "' hidden='true'/> <br>");
					out.println("<label>Matricula a modificar: </label> <input type='text' value='" + referencia + "' disabled/> <br>");
					out.println("<label>Nueva matrícula: </label> <input name='matriculanueva' type='text' id='matricula' placeholder='4 numeros 3 letras'/> <br>");
					out.println("<label>Marca: </label> <input name='marca' type='text' id='marca' value='" + marca + "' /> <br>");
					out.println("<label>Número de ruedas: </label> <input name='numruedas' type='text' id='numruedas' value='"+n_ruedas+"' /> <br>");
					out.println("<label>¿Tiene motor? </label>");
					String motor_si = "";
					String motor_no = "";
					if (motor) {
						motor_si = "checked";
					} else {
						motor_no = "checked";
					}
					out.println("<input name='motor' type='radio' value='true' " + motor_si + " /> Sí");
					out.println("<input name='motor' type='radio' value='false' " + motor_no + "/> No <br>");
					out.println("<label>¿Es automático? </label>");
					String automatico_si = "";
					String automatico_no = "";
					if (automatico) {
						automatico_si = "checked";
					} else {
						automatico_no = "checked";
					}
					out.println("<input name='automatico' type='radio' value='true' " + automatico_si + " /> Sí");
					out.println("<input name='automatico' type='radio' value='false' " + automatico_no + "/> No <br>");
					out.println("<label>Consumo en 100km </label> <input name='consumo' type='text' id='consumo100km' value='" + consumo + "' /> <br>");
					out.println("<input name=\"confirmacion\" hidden=\"true\" type=\"text\" value='true'></input>");
					out.println("<input type='submit' id='submit' value='Modificar'>");
				out.println("</form>");
			out.println("</fieldset>");
			out.println("<br> <a href='index.html'> <button> Volver </button> </a>");
			out.println("<script type=\"text/javascript\">");
				out.println("function validacion_modificar_vehiculo_bd() { var a = document.forms[\"modificar_vehiculo\"][\"matriculanueva\"].value; if (validar_matricula_bd(a)) { return true; } else { return false; };}");
				out.println("function validar_matricula_bd(x) { if (x == null || x == \"\") { alert(\"Escribe la matrícula\"); console.log(\"Comprobación nula\"); return false; } else if(x.length!=7) { alert(\"No has introducido una matrícula válida (núm caracteres)\"); console.log(\"Error, número de caracteres matrícula\"); return false; } else { var expreg = /^[0-9]{4}[A-Z,a-z]{3}$/; if (expreg.test(x)) { return true; } else {	alert(\"La matrícula NO es correcta\");	console.log(\"Error en formato matrícula\"); return false; } } }");
			out.println("</script>");
			out.println("</body>");
			out.println("</html>");

			con.close();

		} catch(ArrayIndexOutOfBoundsException e) {
			//response(response, "no se encontro el vehiculo");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}