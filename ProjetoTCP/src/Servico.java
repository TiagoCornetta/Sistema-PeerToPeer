import java.io.Serializable;

public class Servico implements Serializable{
	public String IP;
	public String servico;
	public int porta;
	
	public Servico(String IP, String Servico, int porta) {
		this.IP = IP;
		this.servico = Servico;
		this.porta = porta;
	}
	
	
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getServico() {
		return servico;
	}
	public void setServico(String servico) {
		this.servico = servico;
	}
	public int getPorta() {
		return porta;
	}
	public void setPorta(int porta) {
		this.porta = porta;
	}
	
	
	
}
