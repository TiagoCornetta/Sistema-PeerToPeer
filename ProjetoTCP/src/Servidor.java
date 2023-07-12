import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

	public static void main(String[] args) throws Exception{
	
	//Criando objeto com os metodos que o servidor poderá fazer	
	IntMetServidor	serv = new MetServidor();
	
	//Criando registry
	LocateRegistry.createRegistry(1099);
	
	//Criando acesso ao Registry
	Registry reg = LocateRegistry.getRegistry();
	
	reg.bind("rmi://127.0.0.1/servicoServ", serv);	
	System.out.println("Servidor no Ar");
		
	}
	
	
}
