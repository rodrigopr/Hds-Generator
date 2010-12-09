/**
 * Criado por Rodrigo Pereira Ribeiro
 * http://dev.rodrigo-ribeiro.net/
 * 
 *
 * $Rev$
 * $Author$
 * $Date$
 * $Id$
 */

package com.hades.generator;
import java.util.ArrayList;


/*
 * Contem informações de todas as Portas instanciadas pelo programa.
 */
public class ConnectionManager {
	private ConnectionManager()
	{
		
	}
	protected ArrayList<HdsWire> connections = new ArrayList<HdsWire>();
	
	/**
	 * Registra o fio
	 * @param wire
	 */
	public void registerWire(HdsWire wire)
	{
		if(!connections.contains(wire))
			connections.add(wire);
	}
	
	/*
	 * Conecta os componentes buscando pelo nome
	 */
	public void createConnection(String sender, String receiver)
	{
		ComponentManager cm = ComponentManager.getInstance();
		HdsComponent compSender = cm.getComponent(sender), compReceiver = cm.getComponent(receiver);
		if(compReceiver!= null && compSender != null)
			createConnection(compSender.getDefaultOutput(), compReceiver.getInput());
	}
	
	/*
	 * Conecta duas portas dadas
	 */
	public void createConnection(HdsPort sender, HdsPort receiver)
	{
		if(sender.getWire() == null)
		{
			HdsWire newWire = new HdsWire("w"+connections.size());
			newWire.insertPort(sender);
			newWire.insertPort(receiver);
			sender.setWire(newWire);
			receiver.setWire(newWire);
		}else
		{
			sender.getWire().insertPort(receiver);
			receiver.setWire(sender.getWire());
		}
	}
	
	/**
	 * Singleton instance
	 */
	private static ConnectionManager _instance = null;
	
	/**
	 * Singleton method - garante que apenas uma instancia da classe sera criada.
	 * @return Instancia unica
	 */
	public static ConnectionManager getInstance()
	{
		if(_instance == null)
			_instance = new ConnectionManager();
		return _instance;
	}

	public String getHdsCode() {
		StringBuilder sb = new StringBuilder();
		for(HdsWire wire : connections)
		{
			sb.append(wire.printWire());
			sb.append("\n");
		}
		return sb.toString();
	}

	public void preBuild() {
				
	}
	
}
