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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;

import com.hades.generator.interpreter.Interpreter;

public class Principal {

	public static void main(String[] args) throws Exception{
		
		Interpreter inter = new Interpreter();
		/*
		inter.initOutput("PCin PCout MARin MDRin MDRout Yin Zin Zout RI_Ain RI_Bin OFFSET-OF-FIELD-IRout ADD SUB SHL SHR AND OR NOT END WMFC READ WRITE REGin REGout");
		inter.initInput("ADD SUB AND OR NOT JMP JZ JP JN MOV LOAD STORE CMP SHL SHR T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 IND DIR");

		inter.run("PCin", "T2 + T5 + T9.JMP + T9.JZ + T9.JP + T9.JN");
		inter.run("PCout", "T1 + T4 + T7.JMP + T7.JZ + T7.JP + T7.JN + T7.LOAD + T9.STORE");
		inter.run("MARin", "T1 + T4 + T7.ADD.IND + T7.SUB.IND + T7.MOV.IND + T9.LOAD + T10.STORE");
		*/
		String fileName = "saida.hds";
		boolean initInput = false, initOutput = false;
		int lineNum = 1;
		
		JFileChooser fch = new JFileChooser(new File(".//"));
		fch.setDialogTitle("Escolha o arquivo de input:");
	    if(fch.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	File f = fch.getSelectedFile();
	    	FileReader reader;
			reader = new FileReader(f);
	    	BufferedReader breader = new BufferedReader(reader);
	    	String line = breader.readLine();
	    	
	    	while(line!=null)
	    	{
	    		line = line.replaceAll(" :", ":").replaceAll("  ", " "); //Corrige possivel espaco no : e espacos duplos
	    		if(line.startsWith("#") || line.equals("") || line.equals(" ")) { }
	    		else if(line.startsWith("HDSNAME:"))
	    			fileName = line.replaceAll(" ", "").replace("HDSNAME:","");
	    		else if(line.startsWith("INPUTPORTS:"))
	    		{
	    			line = line.replace("INPUTPORTS:", "");
	    			inter.initInput(line);
	    			initInput = true;
	    		}
	    		else if(line.startsWith("OUTPUTPORTS:"))
	    		{
	    			line = line.replace("OUTPUTPORTS:", "");
	    			inter.initOutput(line);
	    			initOutput = true;
	    		}
	    		else if(line.startsWith("EXPRESSIONS:"))
	    		{
	    			if(!initInput || !initOutput)
	    				throw new Exception("Error! Expressoes devem vir apos a definicao. ");
	    		}
	    		else
	    		{
	    			String[] div = line.split("=");
	    			if(div.length != 2)
	    				throw new Exception(String.format("Erro, verifique a expressao da linha %d.", lineNum));
	    			inter.run(div[0].trim(), div[1]);	    				
	    		}
	    		line = breader.readLine();
	    		lineNum++;
	    	}	    	

	    	fileName = f.getParent() + "/" + fileName;
	        //Imprime Dados(para teste):
	        HdsWriter writer = new HdsWriter();
	        writer.saveToFile(new File(fileName));
	    }
	    
	}

}
