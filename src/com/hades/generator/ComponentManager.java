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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class ComponentManager {
	protected HashMap<String,HdsComponent> components = new HashMap<String,HdsComponent>();
	protected ArrayList<String> ordem = new ArrayList<String>();
	private ComponentManager()
	{
		
	}
		
	/**
	 * 
	 * @param Nome base do componente
	 * @return 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public String getNotUsedName(String name)
	{
		String newName = name;
		int i = 1;
		while(components.containsKey(newName))
		{
			newName = name + i;
			i++;
		}
		return newName;
	}
	
	/**
	 * 
	 * @param Classe derivada de HdsComponent a ser instanciada
	 * @param Nome do componente
	 * @return 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public HdsComponent createComponent(Class componentClass, String name)
	{
		HdsComponent cpm = null;
		if(!components.containsKey(name))
		{
			try {
				cpm = (HdsComponent) componentClass.newInstance();
			} 
			catch (InstantiationException e) {} 
			catch (IllegalAccessException e) {}
			cpm.initComponent(name);
			cpm.initPorts();
			components.put(name, cpm);
			ordem.add(name);
		}
		return cpm;
	}
	
	/*
	 * Retorna todos os componentes da classe dada
	 */
	public <T extends HdsComponent> ArrayList<T> getAllFromClass(Class T)
	{
		ArrayList<T> array = new ArrayList<T>();
		HdsComponent comp = null;
		for(String name: ordem)
		{
			comp = components.get(name);
			if(T==comp.getClass())
				array.add((T) comp);	
		}
		return array;
	}
	
	/**
	 * Singleton instance
	 */	
	private static ComponentManager _instance = null;
	
	/**
	 * Singleton method - garante que apenas uma instancia da classe sera criada.
	 * @return Instancia unica
	 */
	public static ComponentManager getInstance()
	{
		if(_instance == null)
			_instance = new ComponentManager();
		return _instance;
	}

	/*
	 * Retorna o componente de nome dado
	 */
	public HdsComponent getComponent(String name) {
		return components.get(name);
	}

	/*
	 * retorna o codigo do hades para a definicao dos componentes
	 */
	public String getHdsCode() 
	{
		StringBuilder sb = new StringBuilder();
		for(HdsComponent component : components.values())
		{
			sb.append(component.print());
			sb.append("\n");
		}
		return sb.toString();
	}
}
