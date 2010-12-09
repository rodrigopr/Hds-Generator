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

public class OpinComponent extends HdsComponent {

	public OpinComponent() {
	}
	
	@Override
	public String print() {
		return String.format("hades.models.io.Opin %s %d %d @N 1001 5.0E-9", name, X, Y+600);
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
		portas.put("A", new InputPort(this, "A", 0, 600)); // O deslocamento real de Y é 0
	}
		
	@Override
	public HdsPort getDefaultOutput() {
		return null;
	}

}
