import com.zubiri.parking.ParkingVehiculos;
import com.zubiri.parking.Vehiculo;
import com.zubiri.parking.Coche;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;

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
public class GestorParking extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestorParking() {
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
		if (ParkingVehiculos.getVehiculos().size()==0){
			//lectura del archivo
		ParkingVehiculos.leerVehiculos();
		//ParkingVehiculos.anyadirVehiculo(new Coche(4,true,"marca_prueba","0000AAA",true,50));
		//ParkingVehiculos.anyadirVehiculo(new Coche(4,true,"ferrari","0001ABA",true,100));
		//ParkingVehiculos.anyadirVehiculo(new Coche(4,true,"fiat","0002ACA",true,10));
		}
		String gestion=request.getParameter("gestion");
		System.out.println(gestion);
		if (gestion.equals("mostrar_vehiculos")){
			System.out.println("empieza mostrando");
			response(response,ParkingVehiculos.getVehiculos());
		}else if(gestion.equals("buscar_matricula")){
			System.out.println("empieza buscando");
			String matricula=request.getParameter("matricula");
			Vehiculo encontrado = new Coche();
			try{
				encontrado = ParkingVehiculos.buscarVehiculo(matricula);
				response(response, encontrado);
			}catch(ArrayIndexOutOfBoundsException e){
				response(response, "no se encontro el vehiculo");
			}			
		}else if(gestion.equals("anyadir_vehiculo")){
			System.out.println("empieza anyadiendo");
			int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
			boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
			String marca = request.getParameter("marca");
			String matricula = request.getParameter("matricula");
			boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
			int consumo = Integer.parseInt(request.getParameter("consumo"));	
			System.out.println("new coche");
			Coche nuevo = new Coche(n_ruedas,motor,marca,matricula,automatico,consumo);
			ParkingVehiculos.anyadirVehiculosFichero(nuevo);
			ParkingVehiculos.anyadirCoche(nuevo);
			if(ParkingVehiculos.buscarVehiculo(matricula)==nuevo){
				response(response, "vehiculo anyadido");
			}else{
				response(response, "error al anyadir vehiculo");
			}
		}else if(gestion.equals("borrar_vehiculo")){
			System.out.println("borrando");
			String matricula = request.getParameter("matricula");
			Boolean confirmacion = Boolean.parseBoolean(request.getParameter("confirmacion"));
			if (confirmacion!=true){
				confirmacion=false;
				Vehiculo sentenciado = new Coche();
				sentenciado=ParkingVehiculos.buscarVehiculo(matricula);
				response(response, "Seguro que quieres borrar el vehiculo?", sentenciado);
			}else{
				ParkingVehiculos.borrarVehiculosFichero(matricula);
				ParkingVehiculos.borrarVehiculo(matricula);
				response(response, "Se ha borrado el vehiculo");
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
	
	private void response(HttpServletResponse response, ArrayList<Vehiculo> vehiculos)
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
			out.println("<a href='index.html'><button/>volver</a>");
			out.println("</body>");
			out.println("</html>");
	}
	
	private void response(HttpServletResponse response,String msg)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");				
			out.println("<p>"+msg+"</p>");
			out.println("<a href='index.html'><button/>volver</a>");
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
			out.println("<a href='index.html'><button/>volver</a>");
			out.println("</body>");
			out.println("</html>");
	}
	
	private void response(HttpServletResponse response,String msg ,Vehiculo coche)
			throws IOException {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body align='center'>");
			out.println("<p>"+msg+"</p>");
			out.println("<p>matricula:"+coche.getMatricula()+" | marca del vehiculo: "+coche.getMarca()+"</p>");
			out.println("<form name=\"borrar_vehiculo\" method=\"post\" action=\"Gestor\">");
			out.println("<input name='gestion' hidden='true' type='text'  value='borrar_vehiculo'/>");
			out.println("<input name=\"matricula\" hidden=\"true\" type=\"text\"  value="+coche.getMatricula()+"></input>");
			out.println("<input name=\"confirmacion\" hidden=\"true\" type=\"text\"  value='true'></input>");
			out.println("<input type='submit' id='submit' value='borrar'>");
			out.println("</form>");
			out.println("<a href='index.html'><button/>volver</a>");
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
		out.println("<a href='index.html'><button/>Volver </a>");
		out.println("</body>");
		out.println("</html>");
		}
	}
	

}
