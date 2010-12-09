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

public class HdsWire {
	protected ArrayList<HdsPort> ports = new ArrayList<HdsPort>();
	protected ArrayList<int[]> wireSegments = new ArrayList<int[]>();
	protected ArrayList<int[]> wirePoints = new ArrayList<int[]>();	
	String name = null;
	public HdsWire(String name)
	{
		ConnectionManager.getInstance().registerWire(this);
		this.name = name;
	}
	
	/**
	 * Conecta uma nova porta ao fio
	 * @param port
	 */
	public void insertPort(HdsPort port)
	{
		if(!ports.contains(port))
			ports.add(port);
	}
	
	/*
	 * Retorna o codigo do hades para descrever o fio
	 */
	public String printWire()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("hades.signals.SignalStdLogic1164 %s %d", name, ports.size()));
		for(HdsPort port : ports)
		{
			sb.append(String.format(" %s %s", port.getParentName(), port.portName));
		}
		sb.append(String.format(" %d", wireSegments.size()));
		for(int[] segments: wireSegments)
		{
			sb.append(String.format(" 2 %d %d %d %d", segments[0], segments[1], segments[2], segments[3]));
		}
		sb.append(String.format(" %d", wirePoints.size()));
		for(int[] points: wirePoints)
		{
			sb.append(String.format(" %d %d", points[0], points[1]));
		}
		
		return sb.toString();
	}
	
	/**
	 * Retorna true se o fio nao foi utlizado.
	 * @return
	 */
	public boolean isEmpty()
	{
		return ports.isEmpty();
	}

	/*
	 * Retorna array com portas conectadas ao fio
	 */
	public ArrayList<HdsPort> getPorts() { return ports; }

	/*
	 * Substitui um porta no fio
	 */
	public void replaceElem(HdsPort port1, HdsPort port2) {
		this.ports.remove(port1);
		insertPort(port2);
	}

	/*
	 * Define segmento para a reta dada
	 */
	public void buildWire(int xFrom, int yFrom, int xTo, int yTo)
	{
		wireSegments.add(new int[] {xFrom, yFrom, xTo, yTo});
	}
	
	/*
	 * Define Ponto no fio
	 */
	public void buildPoint(int x, int y)
	{
		wirePoints.add(new int[] {x, y});
	}
}
