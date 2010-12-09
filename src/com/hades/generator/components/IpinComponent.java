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

public class IpinComponent extends HdsComponent {

	public IpinComponent() {
	}
	
	@Override
	public String print() {
		return String.format("hades.models.io.Ipin %s %d %d @N 1001 null 0", name, X+1650, Y+600);
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
		portas.put("Y", new OutputPort(this, "Y", 1650, 600)); // Deslocamento real é 0,0
	}
	
	@Override
	public HdsPort getDefaultOutput() {
		return portas.get("Y");
	}

}
