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

import com.hades.generator.HdsComponent;
import com.hades.generator.HdsPort;
import com.hades.generator.ports.*;

public class NotComponent extends HdsComponent {

	public NotComponent() {
	}
	
	@Override
	public String print() {
		return String.format("hades.models.gates.InvSmall %s %d %d @N 1001 5.0E-9", name, X, Y);
	}
	
	@Override
	public void initComponent(String name) {
		super.initComponent(name);
	}
	
	@Override
	public int getHeight() {
		return 1500;
	}
	
	@Override 
	public void initPorts() {		
		portas.put("Y", new OutputPort(this, "Y", 1800, 600));
		
		portas.put("A", new InputPort(this, "A", 0, 600));
	}
	
	@Override
	public HdsPort getDefaultOutput() {
		return portas.get("Y");
	}

}
