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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.hades.generator.components.*;
import com.hades.generator.ports.*;

public class HdsWriter {
	ComponentManager  compMan = ComponentManager.getInstance();
	ConnectionManager connMan = ConnectionManager.getInstance();
	ArrayList<IpinComponent> inputComps = null;
	ArrayList<OpinComponent> outputComps = null;
	HashMap<Integer, ArrayList<HdsComponent>> groups;
	HashMap<Integer, HashMap<Integer, Boolean> > downWireControl;
	int inputWireControl[];
	int maiorY = 0;
	
	public HdsWriter()
	{}
	
	/*
	 * Salva codigo do design no arquivo
	 */
	public void saveToFile(File file) throws IOException
	{
		if(file.exists())
			file.delete();
		file.createNewFile();
		FileWriter fw;
			
		organizeAll();
		setupPosition();
		setupWirePosition();
		//TODO Limpar codigo da classe
		//TODO Imprimir fios
		
		fw = new FileWriter(file);
		fw.write("# hades.models.Design file\r#  "+"\n");
		fw.write("[name] "+file.getName()+"\n[components]"+"\n");
		fw.write(compMan.getHdsCode());
		fw.write("[end components]\n[signals]"+"\n");
		fw.write(connMan.getHdsCode());		
		fw.write("[end signals]\n[end]"+"\n");	
		
		fw.flush();
		
	}
	
	/*
	 * Define as posicoes dos fios
	 */
	private void setupWirePosition() 
	{
		for(HdsComponent input: inputComps)
		{
			HdsPort iPort = input.getDefaultOutput();
			HdsWire wire = iPort.getWire();
			int lastX = input.X + iPort.getDisplacement()[0];
			int iY = input.Y + iPort.getDisplacement()[1];
			int toX, toY, temp;
			for(HdsPort port : wire.getPorts())
			{
				if(port instanceof InputPort)
				{
					HdsComponent compTo = port.getParent(); 
					toX = compTo.X + port.getDisplacement()[0] - ((inputWireControl[compTo.getOrder()]++)*150);
					wire.buildWire(lastX, iY, toX, iY);
					lastX = toX;
					wire.buildPoint(toX, iY);
					
					toY = compTo.Y + port.getDisplacement()[1];
					wire.buildWire(toX, iY, toX, toY);
					
					temp = compTo.X + port.getDisplacement()[0];
					wire.buildWire(toX, toY, temp, toY);
				}
			}
		}
		
		int i = 0;
		ArrayList<HdsComponent> comps;
		while((comps = groups.get(i)) != null)
		{
			int count = 1;
			for(HdsComponent comp : comps)
			{
				if(!(comp instanceof OpinComponent))
				{
					HdsPort portFrom = null;
					if(comp instanceof AndComponent)
						portFrom = ((AndComponent) comp).getOwnDefaultOutput();
					else if(comp instanceof OrComponent)
						portFrom = ((OrComponent) comp).getOwnDefaultOutput();
					else
						portFrom = comp.getDefaultOutput();
					if(portFrom == null)
						continue;
					HdsWire wire = portFrom.getWire();
					if(wire == null)
						continue;
					
					int lastX = comp.X + portFrom.getDisplacement()[0];
					int iY = comp.Y + portFrom.getDisplacement()[1];
					int toX, toY, tempX, tempY;
					for(HdsPort portTo : wire.getPorts())
					{
						if(portTo instanceof InputPort)
						{
							HdsComponent compTo = portTo.getParent();
							int orderPos = comp.getOrder();
							int pos = 1;
							if(comp instanceof NotComponent)
								toX = comp.X + portFrom.getDisplacement()[0] + ((count++)*150) + 1800;
							else
								toX = comp.X + portFrom.getDisplacement()[0] + ((count++)*150);
							wire.buildWire(lastX, iY, toX, iY);

							if(compTo.getOrder() - comp.getOrder() == -1)
							{
								tempY = compTo.Y + portTo.getDisplacement()[1];
								wire.buildWire(toX, iY, toX, tempY);
								tempX = compTo.X + portTo.getDisplacement()[0];
								wire.buildWire(toX, tempY, tempX, tempY);
							}
							else
							{
								for(pos = 1;downWireControl.get(orderPos).containsKey(pos) && downWireControl.get(orderPos).get(pos);pos++);
								for(int j = orderPos; j <= compTo.getOrder(); j++)
									downWireControl.get(j).put(pos, true);
								
								toY = maiorY + (150 * pos);
								wire.buildWire(toX, iY, toX, toY);

								tempX = compTo.X + portTo.getDisplacement()[0] - ((inputWireControl[compTo.getOrder()])*150);
								wire.buildWire(toX, toY, tempX, toY);
								
								tempY = compTo.Y + portTo.getDisplacement()[1];
								wire.buildWire(tempX, toY, tempX, tempY);
								
								toX = compTo.X + portTo.getDisplacement()[0];
								wire.buildWire(tempX, tempY, toX, tempY);
							}
							
						}
					}
				}
			}
			i++;
		}
	}
	
	/*
	 * Retorna o espaco nescessario entre as colunas
	 */
	private int getSpace(Integer i)
	{
		Integer x = 1;
		if(groups.get(i-1) != null)
		{
			for(HdsComponent comp : groups.get(i-1))
				x += comp.getPortsOfType(InputPort.class).size();
			if(groups.get(i)!= null)
				for(HdsComponent comp : groups.get(i))
					x += comp.getPortsOfType(OutputPort.class).size();
		}
		return x * 150;
	}
	
	/*
	 * Posiciona todos os elementos de forma organizada
	 */
	private void setupPosition() {
		ArrayList<HdsComponent> comps = null;
		int i = 0, lastX=0, lastY=0;
		comps = groups.get(i);
		while (comps != null) {
			for(HdsComponent comp: comps)
			{
				comp.X = lastX;
				comp.Y = lastY;
				lastY += comp.getHeight();
			}
			i++;
			comps = groups.get(i);
			lastX -= 3750 + getSpace(i+1);
			if(lastY > maiorY)
				maiorY = lastY;
			lastY = 0;
		}
		lastY = -750 * inputComps.size();
		int diffX1 = lastX - 3150;
		int diffX2 = lastX;
		for(IpinComponent ipin : inputComps)
		{
			if(lastX == diffX1)
				lastX = diffX2;
			else
				lastX = diffX1;
			ipin.X = lastX;
			ipin.Y = lastY;
			lastY += ipin.getHeight()/2;
		}
	}
	
	/*
	 * Organiza os elemtendos em ordem de precedencia 
	 */
	private void organizeAll() 
	{
		groups = new HashMap<Integer, ArrayList<HdsComponent>>();
		inputComps = compMan.getAllFromClass(IpinComponent.class);
		outputComps =  compMan.getAllFromClass(OpinComponent.class);
		downWireControl = new HashMap<Integer, HashMap<Integer,Boolean>>();
		for(OpinComponent opin : outputComps)
			updateOrderns(opin, groups.size());	
		inputWireControl = new int[groups.size()];
		for(int i = 0; i <groups.size(); i++)
			inputWireControl[i] = 1;
	}
	
	/*
	 * Funcao define qual coluna o componente deve se posicionar
	 */
	private void updateOrderns(HdsComponent comp, int ordem) {
		downWireControl.put(ordem, new HashMap<Integer,Boolean>());
		if(comp.getClass() == IpinComponent.class)
			return;
		if(comp.getOrder() != -1 && comp.getOrder() < ordem)
			groups.get(comp.getOrder()).remove(comp);
		if(groups.get(ordem) == null)
			groups.put(ordem, new ArrayList<HdsComponent>());
		groups.get(ordem).add(comp);
		comp.setOrder(ordem);
		for(HdsWire wire : comp.getInputWires())
				for(HdsPort porta : wire.getPorts())
					if(porta.getClass() == OutputPort.class)
						updateOrderns(porta.parent, ordem+1);
		
	}
}
