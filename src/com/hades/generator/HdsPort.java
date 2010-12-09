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
public class HdsPort {
	protected String portName;
	protected HdsWire wire = null;
	protected HdsComponent parent = null;
	protected int xDisplacement = 0;
	protected int yDisplacement = 0;
	
	public HdsPort(HdsComponent parent, String portName, int xDisplacement, int yDisplacement)
	{
		this.parent = parent;
		this.portName = portName;
		this.xDisplacement = xDisplacement;
		this.yDisplacement = yDisplacement;
	}
	
	/*
	 * 
	 */
	public void setDisplacement(int x, int y)
	{
		xDisplacement = x;
		yDisplacement = y;
	}
	
	/*
	 * Retorna o deslocamento relativo ao component
	 * Usado na geração dos fios.
	 */
	public int[] getDisplacement()
	{
		int[] displ = {xDisplacement, yDisplacement};
		return displ;
	}
	
	/*
	 * retorna o nome do componente associado 
	 */
	public String getParentName()
	{
		return this.parent.getName();
	}
	/*
	 * verifica se a porta esta conectada a algo
	 */
	public boolean hasConnection()
	{
		if(wire == null)
			return false;
		return !wire.isEmpty();
	}
	/*
	 * seta o fio a ser utilizado na porta
	 */
	public void setWire(HdsWire newWire)
	{
		wire = newWire;
	}
	/*
	 * Retorna o fio utilizado na porta
	 */
	public HdsWire getWire()
	{
		return wire;
	}
	
	/*
	 * Retorna o componente associado a porta
	 */
	public HdsComponent getParent()
	{
		return parent;
	}
}
