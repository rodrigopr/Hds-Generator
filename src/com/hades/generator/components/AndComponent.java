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

package com.hades.generator.components;

import java.io.ObjectInputStream.GetField;

import com.hades.generator.*;
import com.hades.generator.ports.*;
public class AndComponent extends HdsComponent {
	protected int usedConnections = 0;
	protected AndComponent sister = null;
	public AndComponent() {
		
	}
	
	@Override
	public String print() {
		if(usedConnections == 4)
			return String.format("hades.models.gates.And4 %s %d %d @N 1001 1.0E-8", name, X, Y);
		else if(usedConnections == 3)
			return String.format("hades.models.gates.And3 %s %d %d @N 1001 1.0E-8", name, X, Y);
		else
			return String.format("hades.models.gates.And2 %s %d %d @N 1001 1.0E-8", name, X, Y);
	}
	
	@Override
	public void initComponent(String name) {
		super.initComponent(name);
	}
	
	@Override
	public int getHeight() {
		return 2700;
	}
	
	@Override 
	public void initPorts() {
		portas.put("Y", new OutputPort(this, "Y", 3600, 1200));
		
		portas.put("A", new InputPort(this, "A", 0, 600));
		portas.put("B", new InputPort(this, "B", 0, 1800));
		portas.put("C", new InputPort(this, "C", 0, 1800));
		portas.put("D", new InputPort(this, "D", 0, 2100));
	}
	
	@Override
	public HdsPort getDefaultOutput() {
		if(sister != null)
			return sister.getDefaultOutput();
		return portas.get("Y");
	}
	
	/*
	 * Retorna propria saida padrao independente do numero de entradas
	 */
	public HdsPort getOwnDefaultOutput() {
		return portas.get("Y");
	}
	
	@Override
	public HdsPort getInput() {
		HdsPort A = portas.get("A");
		HdsPort B = portas.get("B");
		HdsPort C = portas.get("C");
		HdsPort D = portas.get("D");
		
		switch (usedConnections) {
		case 0:
			usedConnections++;
			return A;
		case 1:
			usedConnections++;
			return B;
		case 2:
			usedConnections++;
			updatePorts();
			return C;
		case 3:
			usedConnections++;
			updatePorts();
			return D;
			
		default:
			if(sister==null)
			{
				try 
				{
					split();
				}
				catch (InstantiationException e) {} 
				catch (IllegalAccessException e) {}
			}
			return sister.getInput();
		}
	}

	/**
	 * Divide o And em dois
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void split() throws InstantiationException, IllegalAccessException {
		sister = (AndComponent) ComponentManager.getInstance().createComponent(AndComponent.class, this.name + "s");	
		HdsPort sY = sister.getDefaultOutput(), tY = portas.get("Y");
		if(tY != null && tY.hasConnection())
		{
			sY.setWire(tY.getWire());
			sY.getWire().replaceElem(tY, sY);
	
			tY.setWire(null);
			
		}
		ConnectionManager.getInstance().createConnection(tY, sister.getInput());
	}

	/**
	 * Atualiza as posicoes das portas, dependento do tipo do componente
	 */
	private void updatePorts() {
		if(usedConnections == 3)
		{
			portas.get("B").setDisplacement(0, 1200);			
		}
		else // usedConnections == 4
		{
			portas.get("A").setDisplacement(0, 300);	
			portas.get("B").setDisplacement(0, 900);	
			portas.get("C").setDisplacement(0, 1500);			
		}		
	}

}
