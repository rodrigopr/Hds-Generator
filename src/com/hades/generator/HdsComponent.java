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
import java.util.HashMap;

import com.hades.generator.ports.InputPort;

public class HdsComponent {
	protected HashMap<String, HdsPort> portas = new HashMap<String, HdsPort>();
	protected String name = "";
	protected int order = -1;	
	public int X = 0, Y = 0;
	
	public HdsComponent()
	{
	}

	/*
	 * Retorna string com definicao do componente no Hades
	 */
	public String print()
	{
		return "";
	}
	
	public void initComponent(String name)
	{
		this.name = name;
	}
	
	/**
	 * É nescessario iniciar todas as portas no momento da inicializacao.
	 */
	public void initPorts()
	{
		
	}
	
	/**
	 * Retorna a ordem do componente
	 * @return
	 */
	public int getOrder(){
		return order;
	}
	
	/**
	 * Retorna a ordem do componente
	 * @return
	 */
	public int getHeight(){
		return 0;
	}
	
	/**
	 * Retorna a ordem do componente
	 * @return
	 */
	public void setOrder(int order){
		this.order = order;
	}
	
	/**
	 * Retorna a classe usada pelo hades para o componente
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Retorna a porta de saida padrao
	 * @return HdsPort instance
	 */
	public HdsPort getDefaultOutput()
	{
		return null;
	}
	
	/**
	 * Retorna uma porta de entrada que nao esteja sendo utilizada, 
	 * ou retorna null caso todas estejam sendo utilizada.
	 * @return HdsPort instance
	 */
	public HdsPort getInput()
	{
		HdsPort pReturn = null;
		for (HdsPort porta : portas.values()) {
			if(porta.getClass() == InputPort.class && !porta.hasConnection())
			{
				pReturn = porta;
				break;
			}			
		}
		return pReturn;
	}
	
	/*
	 * Retorna todos fios ligados as entradas desse componente
	 */
	public ArrayList<HdsWire> getInputWires()
	{
		ArrayList<HdsWire> array = new ArrayList<HdsWire>();
		for(HdsPort porta :portas.values())
			if(porta.hasConnection() && porta.getClass() == InputPort.class)
				array.add(porta.getWire());
		return array;
	}
	
	/*
	 * Retorna todas as portas da classe dada
	 */
	public ArrayList<HdsPort> getPortsOfType(Class T)
	{
		ArrayList<HdsPort> array = new ArrayList<HdsPort>();
		for(HdsPort porta :portas.values())
			if(porta.hasConnection() && porta.getClass() == T)
				array.add(porta);
		return array;
	}
}
