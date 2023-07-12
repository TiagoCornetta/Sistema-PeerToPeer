import java.io.File;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

//Classe responsável pelas implementações do metodos do servidor


public class MetServidor extends UnicastRemoteObject implements IntMetServidor{
	
	List <Servico> servicos = new ArrayList<>();
		
	
	protected MetServidor() throws RemoteException {
		super();
	}
	
	
	
	//Requisição Join ao Servidor
	@Override
	public String requisicaoJoin(String IP, int porta,List<String> lista ) throws RemoteException {
		System.out.print("PEER["+ IP + "]:" + porta + " adicionados com arquivos[ ");		
		for(int i = 0; i < lista.size(); i++) {
			System.out.print("'" +lista.get(i)+ "'" + " ");
			
			//Parta para armazenar os servicos no servidor
			Servico servico = new Servico(IP,lista.get(i),porta);
			servicos.add(servico);
		}
		System.out.print("]");
		System.out.println("");
			
		return "JOIN_OK";
	}


	//Requição Search no Servidor
	@Override
	public List<Servico> requisicaoSearch(String IP, int porta, String nomeServico) throws RemoteException {
		System.out.println("PEER["+ IP+"]:"+ porta + " solicitou o arquivo "+ nomeServico);
		
		List <Servico> servicoEncontrado = new ArrayList<>();
		for(int i = 0; i < servicos.size(); i++) {
			if(servicos.get(i).servico.equals(nomeServico) ) {
				servicoEncontrado.add(servicos.get(i));
			}
		}
		return servicoEncontrado;
	}



	@Override
	public String requisicaoUpdate(String IP, int porta, String nomeServico) throws RemoteException {
		
		//Ira adicionar o novo serviço desse peer na lista.
		Servico servico = new Servico(IP, nomeServico, porta);
		servicos.add(servico);
		String string = "UPDATE_OK";
		return string;
	}

}
