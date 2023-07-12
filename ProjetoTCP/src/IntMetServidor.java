import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


//interface responsável para demonstrar os metodos que o servidor possui
public interface IntMetServidor extends Remote {

	
	//Requisição Join
	String requisicaoJoin(String IP, int porta,List<String> lista ) throws RemoteException;
	
	//Requisição Search
	List <Servico> requisicaoSearch(String IP, int porta, String nomeServico) throws RemoteException;
	
	//Requisição Update
	String  requisicaoUpdate(String IP, int porta, String  nomeServico) throws RemoteException;
}
