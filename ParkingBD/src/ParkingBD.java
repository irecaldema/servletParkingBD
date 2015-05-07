import com.zubiri.parking.ParkingVehiculos;
import com.zubiri.parking.Vehiculo;
import com.zubiri.parking.Coche;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
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
    //private static final String bd = "pruebas_java";
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				
		Connection con = null;	
		Statement sentencia = null;
		try{
			System.out.println("en el try crear");
			
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
		}catch(Exception e){
			System.out.println("en el catch 1");
			System.err.println("error "+ e);
		}
	        
		/*if (ParkingVehiculos.getVehiculos().size()==0){
			//lectura del archivo
			ParkingVehiculos.leerVehiculos();			
		}*/
		String gestion=request.getParameter("gestion");
		System.out.println(gestion);
		if (gestion.equals("mostrar_vehiculos")){
			System.out.println("empieza mostrando");
			//response(response,ParkingVehiculos.getVehiculos());			
			try{
				System.out.println("en el try mostrar");
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
		        System.out.println("pre while");
		        while(mostrar.next()){	
		        	System.out.println("matricula:"+mostrar.getString("matricula"));
		        	matricula[cont] = mostrar.getString("matricula");
		        	System.out.println("matricula "+matricula[cont]);
		        	marca[cont] = mostrar.getString("marca");
		        	System.out.println("marca "+marca[cont]);
		        	cont++;
		        }
		        System.out.println("post while");
		        
		    con.close();    
		    response(response,matricula,marca);
		    
			}catch(Exception e){
				System.out.println("en el catch 2");
				System.err.println("error "+ e);
			}
			
		}else if(gestion.equals("buscar_matricula")){
			System.out.println("empieza buscando");
			String referencia=request.getParameter("matricula");
			//Vehiculo encontrado = new Coche();
			try{
				//encontrado = ParkingVehiculos.buscarVehiculo(matricula);
				//response(response, encontrado);
				
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

		        // Open a connection
		        con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);
		        
		        sentencia = con.createStatement();
		        
		        String sql;		    
		        System.out.println("referencia: "+referencia);		     
		        //sql="SELECT matricula, marca FROM coches WHERE matricula='"+referencia+"'";
		        sql="SELECT * FROM coches WHERE matricula=\""+referencia+"\"";
        	    //matricula VARCHAR(7), marca VARCHAR(20), motor BOOLEAN,  automatico BOOLEAN,n_ruedas INTEGER(2),consumo INTEGER(3),
		        ResultSet buscar = sentencia.executeQuery(sql);
		        while(buscar.next()){
			        String matricula = buscar.getString("matricula");
			        String marca = buscar.getString("marca");
			        Boolean motor = buscar.getBoolean("motor");
			        Boolean automatico = buscar.getBoolean("automatico");
			        Integer n_ruedas = buscar.getInt("n_ruedas");
			        Integer consumo = buscar.getInt("consumo");
			        System.out.println("matricula: "+matricula);
			        System.out.println("marca: "+marca);
			        response(response,matricula,marca,motor,automatico,n_ruedas,consumo);
		        }
		        con.close();    
		    	
			}catch(ArrayIndexOutOfBoundsException e){
				//response(response, "no se encontro el vehiculo");
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(gestion.equals("anyadir_vehiculo")){
			System.out.println("empieza anyadiendo");
			int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
			System.out.println(request.getParameter("motor"));
			boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
			System.out.println(motor);
			String marca = request.getParameter("marca");
			String matricula = request.getParameter("matricula");
			boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
			int consumo = Integer.parseInt(request.getParameter("consumo"));	
			System.out.println("new coche");
					//Coche nuevo = new Coche(n_ruedas,motor,marca,matricula,automatico,consumo);
					//ParkingVehiculos.anyadirVehiculosFichero(nuevo);
					//ParkingVehiculos.anyadirCoche(nuevo);
			try{
				
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
		        System.out.println("valor crear: "+crear);
        	    //matricula VARCHAR(7), marca VARCHAR(20), motor BOOLEAN,  automatico BOOLEAN,n_ruedas INTEGER(2),consumo INTEGER(3),
		        sql="SELECT * FROM coches WHERE matricula='"+matricula+"'";
		        ResultSet buscar = sentencia.executeQuery(sql);
		        while(buscar.next()){
			        String encontrado = buscar.getString("matricula");
			        if(encontrado!=null){			        	
				        String matricula2 = buscar.getString("matricula");
				        String marca2 = buscar.getString("marca");
				        Boolean motor2 = buscar.getBoolean("motor");
				        Boolean automatico2 = buscar.getBoolean("automatico");
				        Integer n_ruedas2 = buscar.getInt("n_ruedas");
				        Integer consumo2 = buscar.getInt("consumo");
				        System.out.println("matricula: "+matricula2);
				        System.out.println("marca: "+marca2);
				        response(response,matricula2,marca2,motor2,automatico2,n_ruedas2,consumo2);
					}else{
						response(response, "error al anyadir vehiculo");
					}
		        }
		        con.close();    
		    	
			}catch(ArrayIndexOutOfBoundsException e){
				//response(response, "no se encontro el vehiculo");
			}catch(Exception e){
				e.printStackTrace();
			}
			/*if(ParkingVehiculos.buscarVehiculo(matricula)==nuevo){
				response(response, "vehiculo anyadido");
			}else{
				response(response, "error al anyadir vehiculo");
			}*/
		}else if(gestion.equals("borrar_vehiculo")){
			System.out.println("borrando");
			String sentenciado = request.getParameter("matricula");
			Boolean confirmacion = Boolean.parseBoolean(request.getParameter("confirmacion"));
			if (confirmacion!=true){
				confirmacion=false;
				//Vehiculo sentenciado = new Coche();
				//sentenciado=ParkingVehiculos.buscarVehiculo(matricula);
				response(response, "Seguro que quieres borrar el vehiculo?", sentenciado);
			}else{
				//ParkingVehiculos.borrarVehiculosFichero(sentenciado);
				//ParkingVehiculos.borrarVehiculo(sentenciado);
				
				try{					
					// Register JDBC driver
					Class.forName("com.mysql.jdbc.Driver");
			        // Open a connection
			        con = DriverManager.getConnection(URL_BD,USUARIO,CONTRA);			        
			        sentencia = con.createStatement();
			        
			        String sql;
			        //INSERT INTO coches VALUES ("0000AAA", "prueba1", true, true, 4, 100);
			        //DELETE FROM coches where matricula="0000AAA";
			        System.out.println("DELETE FROM coches where matricula=\""+sentenciado+"\"");
			        sql="DELETE FROM coches where matricula=\""+sentenciado+"\"";
			        int borrar = sentencia.executeUpdate(sql);
			        System.out.println("valor crear: "+borrar);
			        if(borrar==1){
			        	response(response, "Se ha borrado el vehiculo");
			        }
			        con.close();			    	
				}catch(ArrayIndexOutOfBoundsException e){
					//response(response, "no se encontro el vehiculo");
				}catch(Exception e){
					e.printStackTrace();
				}
			}

		}else if (gestion.equals("modificar_vehiculo")) {			
			System.out.println("Empieza modificando");
			Boolean confirmacion = Boolean.parseBoolean(request.getParameter("confirmacion"));
			if(confirmacion!=true){
				formulario_modificar(response,request.getParameter("matriculavieja"));
			}else{
				int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
				//System.out.println("prueba1 "+n_ruedas);
				boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
				String marca = request.getParameter("marca");
				String matriculanueva = request.getParameter("matriculanueva");
				//System.out.println("prueba2 "+matriculanueva);
				String matriculavieja = request.getParameter("matriculavieja");
				//System.out.println("prueba3 "+matriculavieja);
				boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
				int consumo = Integer.parseInt(request.getParameter("consumo"));
				
				try {
					if (ParkingVehiculos.buscarVehiculo(matriculavieja) != null) {
						try {
							if (ParkingVehiculos.buscarVehiculo(matriculanueva) != null) {
								response(response, "La matricula introducida ya existe");
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("matricula: "+matriculavieja+" matri nueva: "+matriculanueva+" marca: "+marca+" ruedas: "+n_ruedas+" motor: "+motor+" automa: "+automatico+" consu: "+consumo);
							ParkingVehiculos.modificarVehiculosFicheroServlet(matriculavieja, matriculanueva, marca, n_ruedas, motor, automatico, consumo);
							ParkingVehiculos.modificarVehiculoServlet(matriculavieja, matriculanueva, marca, n_ruedas, motor, automatico, consumo);
							System.out.println("Vehículo modificado");
							response(response, "Vehículo modificado");
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					response(response, "No se encontró el vehículo");
				}
			}
		}
		//ParkingVehiculos pv = new ParkingVehiculos();
		//response(response,"prueba");
		System.out.println("fin");		
	}
	
	/*private void response(HttpServletResponse response, ArrayList<Vehiculo> vehiculos)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<p>-------------------------------</p>");
			for (int i=0;i<vehiculos.size();i++){				
				out.println("<b>matricula:</b> "+vehiculos.get(i).getMatricula()+" | ");
				out.print("<b>marca:</b> "+vehiculos.get(i).getMarca()+"");
				out.println("<p>-------------------------------</p>");
			}
			out.println("<a href='index.html'><button>volver</button></a>");
			out.println("</body>");
			out.println("</html>");
	}*/
	
	private void response(HttpServletResponse response, String [] matricula, String [] marca)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<p>-------------------------------</p>");
			for (int i=0;i<matricula.length;i++){	
				if(matricula[i]==null){
					break;
				}else{
				out.println("<b>matricula:</b> "+matricula[i]+" | ");
				out.print("<b>marca:</b> "+marca[i]+"");
				out.println("<p>-------------------------------</p>");
				}
			}
			out.println("<a href='index.html'><button>volver</button></a>");
			out.println("</body>");
			out.println("</html>");
	}
	
	private void response(HttpServletResponse response,String msg)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");				
			out.println("<p>"+msg+"</p>");
			out.println("<a href='index.html'><button>volver</button></a>");
			out.println("</body>");
			out.println("</html>");
	}
	
	private void response(HttpServletResponse response, Vehiculo coche)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<p>"+coche.getMarca()+"</p>");
			out.println("<p>"+coche.getMatricula()+"</p>");
			out.println("<a href='index.html'><button>volver</button></a>");
			out.println("</body>");
			out.println("</html>");
	}
	
	private void response(HttpServletResponse response, String matricula, String marca,Boolean motor,Boolean automatico,Integer n_ruedas,Integer consumo)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<table align=\"center\" border=5><tr>");
			out.println("<td>matricula</td>");
			out.println("<td>marca</td>");
			out.println("<td>motor</td>");
			out.println("<td>automatico</td>");
			out.println("<td>numero de ruedas</td>");
			out.println("<td>consumo</td>");
			out.println("</tr><tr>");
			out.println("<td>"+matricula+"</td>");
			out.println("<td>"+marca+"</td>");
			if(motor) {
				out.println("<td>si</td>");
			}else{
				out.println("<td>no</td>");
			}			
			if(automatico){
				out.println("<td>si</td>");
			}else{
				out.println("<td>no</td>");
			}			
			out.println("<td>"+n_ruedas+"</td>");
			out.println("<td>"+consumo+"</td>");
			out.println("</tr><tr>");
			out.println("<td colspan=6>");
			out.println("<center><a href='index.html'><button>volver</button></a></center>");
			out.println("</td>");
			out.println("</tr></table>");
			out.println("</body>");
			out.println("</html>");
	}
	
	/*private void response(HttpServletResponse response,String msg ,Vehiculo coche)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body align='center'>");
			out.println("<p>"+msg+"</p>");
			out.println("<p>matricula:"+coche.getMatricula()+" | marca del vehiculo: "+coche.getMarca()+"</p>");
			out.println("<form name=\"borrar_vehiculo\" method=\"post\" action=\"GestorBD\">");
			out.println("<input name='gestion' hidden='true' type='text'  value='borrar_vehiculo'/>");
			out.println("<input name=\"matricula\" hidden=\"true\" type=\"text\"  value="+coche.getMatricula()+"></input>");
			out.println("<input name=\"confirmacion\" hidden=\"true\" type=\"text\"  value='true'></input>");
			out.println("<input type='submit' id='submit' value='borrar'>");
			out.println("</form>");
			out.println("<a href='index.html'><button>volver</button></a>");
			out.println("</body>");
			out.println("</html>");
	}*/
	
	private void response(HttpServletResponse response,String msg ,String matricula)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body align='center'>");
			out.println("<p>"+msg+"</p>");
			out.println("<p>matricula:"+matricula+" | marca del vehiculo: "+/*coche.getMarca()+*/"</p>");
			out.println("<form name=\"borrar_vehiculo\" method=\"post\" action=\"GestorBD\">");
			out.println("<input name='gestion' hidden='true' type='text'  value='borrar_vehiculo'/>");
			out.println("<input name=\"matricula\" hidden=\"true\" type=\"text\"  value="+matricula+"></input>");
			out.println("<input name=\"confirmacion\" hidden=\"true\" type=\"text\"  value='true'></input>");
			out.println("<input type='submit' id='submit' value='borrar'>");
			out.println("</form>");
			out.println("<a href='index.html'><button>volver</button></a>");
			out.println("</body>");
			out.println("</html>");
	}	
	
	private void formulario_modificar(HttpServletResponse response,String matricula)
			throws IOException {
		System.out.println("se esta modificando el vehiculo con matricula: "+matricula);
		Vehiculo vehiculo_viejo = new Coche();
		vehiculo_viejo=ParkingVehiculos.buscarVehiculo(matricula);
		
		if (vehiculo_viejo instanceof Coche){
			Coche coche_viejo = ( Coche ) vehiculo_viejo;
			//coche_viejo.getConsumo100km();
			System.out.println("prueba get matricula: "+coche_viejo.getMatricula());
		
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<form name='modificar_vehiculo' method='post' action='Gestor'>");
			out.println("<input name='gestion' hidden='true' type='text' value='modificar_vehiculo'/>");
			out.println("<input name='matriculavieja' type='text' value='"+coche_viejo.getMatricula()+"' hidden='true'/> <br>");
			out.println("Matricula a modificar: <input type='text' value='"+coche_viejo.getMatricula()+"' disabled/> <br>");
			out.println("Nueva matrícula: <input name='matriculanueva' type='text' id='matricula' value='matricula (4 numeros 3 letras)'/> <br>");
			out.println("Marca: <input name='marca' type='text' id='marca' value='"+coche_viejo.getMarca()+"' /> <br>");
			out.println("Número de ruedas: <input name='numruedas' type='text' id='numruedas' value='"+coche_viejo.getNumRuedas()+"' /> <br>");
			out.println("¿Tiene motor?");
			String motor_si="";
			String motor_no="";
			if (coche_viejo.isMotor()) {
				motor_si="checked";
			} else {
				motor_no="checked";
			}
			out.println("<input name='motor' type='radio' value='true' "+motor_si+" /> Sí");
			out.println("<input name='motor' type='radio' value='false' "+motor_no+"/> No <br>");
			out.println("¿Es automático?");
			String automatico_si="";
			String automatico_no="";
			if (coche_viejo.isAutomatico()) {
				automatico_si="checked";
			} else {
				automatico_no="checked";
			}
			out.println("<input name='automatico' type='radio' value='true' "+automatico_si+" /> Sí");
			out.println("<input name='automatico' type='radio' value='false' "+automatico_no+"/> No <br>");
			out.println("Consumo en 100km <input name='consumo' type='text' id='consumo100km' value='"+coche_viejo.getConsumo100km()+"' /> <br>");
			out.println("<input name=\"confirmacion\" hidden=\"true\" type=\"text\"  value='true'></input>");
			out.println("<input type='submit' id='submit' value='Modificar'>");
		out.println("</form>");
		out.println("<a href='index.html'><button>volver</button></a>");
		out.println("</body>");
		out.println("</html>");
		}
	}
	

}
