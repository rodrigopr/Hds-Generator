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

package com.hades.generator.interpreter;

import java.util.ArrayList;
import java.util.Stack;

import com.hades.generator.*;
import com.hades.generator.components.*;

interface Expression {
    public HdsComponent interpret();
}
 
class Input implements Expression {
    private String varName;
    public Input(String name)       { this.varName = name; }
    public HdsComponent interpret()  { return ComponentManager.getInstance().getComponent(varName); }
}
 
class Not implements Expression {
    Expression expression = null;
    public Not() { 
    }
    
    public void setExpr(Expression expr) { 
    	expression = expr;
    }
 
    public HdsComponent interpret()  { 
    	ComponentManager cm = ComponentManager.getInstance();
       	HdsComponent not = cm.createComponent(NotComponent.class, cm.getNotUsedName("dNot"));
       	HdsComponent fatherComp = expression.interpret();
       	ConnectionManager.getInstance().createConnection(fatherComp.getName(),not.getName());
       	return not;
    }
}
 
interface insertableOperator extends Expression
{
	 public void insertElement(Expression elem);
}

class And implements insertableOperator {
    ArrayList<Expression> elements = new ArrayList<Expression>();
    public And() { 
    }
    public void insertElement(Expression elem)
    {
    	elements.add(elem);
    }
 
    public HdsComponent interpret()  {
    	ComponentManager cm = ComponentManager.getInstance();
    	ConnectionManager conn = ConnectionManager.getInstance();
    	HdsComponent and = cm.createComponent(AndComponent.class, cm.getNotUsedName("dAnd"));
    	for(Expression expr: elements)
    	{
    		HdsComponent newComp = expr.interpret();
    		conn.createConnection(newComp.getName(), and.getName());
    	}
    	return and;
    }
}

class Or implements insertableOperator {
    ArrayList<Expression> elements = new ArrayList<Expression>();
    public Or() { 
    }
    public void insertElement(Expression elem)
    {
    	elements.add(elem);
    }
 
    public HdsComponent interpret()  {
    	ComponentManager cm = ComponentManager.getInstance();
    	ConnectionManager conn = ConnectionManager.getInstance();
    	HdsComponent or = cm.createComponent(OrComponent.class, cm.getNotUsedName("dOr"));
    	for(Expression expr: elements)
    	{
    		HdsComponent newComp = expr.interpret();
    		conn.createConnection(newComp.getName(), or.getName());
    	}
    	return or;
    }
}
 
public class Interpreter {
	String[] elems;
	int index;
	public void run(String output, String expr)
	{
		elems = splitExpr(expr);
		index = 0;
		Expression ex = interpret();
		HdsComponent lastComp = ex.interpret();
		ConnectionManager.getInstance().createConnection(lastComp.getName(), output);
	}
	
	public Expression interpret()
	{
		Stack<Expression> pilha = new Stack<Expression>();
		insertableOperator ex = null;
		while(index<elems.length && !elems[index].equals(")"))
		{
			if(elems[index].equals("!"))
				pilha.push(new Not());
			else if(elems[index].equals("("))
			{
				index++;
				Expression e = interpret();
				if(!pilha.isEmpty())
					if(pilha.peek().getClass() == Not.class)
					{
						((Not) pilha.peek()).setExpr(e);
						e =  pilha.pop();
					}
				if(ex==null)
					pilha.push(e);
				else
					ex.insertElement(e);
			}
			else if(elems[index].equals("."))
			{

				if(ex==null)
				{
					ex = new And();
					if(!pilha.isEmpty())
						ex.insertElement(pilha.pop());
				}
			}
			else if(elems[index].equals("+"))
			{
				if(ex!=null)
				{
					//if(!pilha.isEmpty())
					//	ex.insertElement(pilha.pop());
					pilha.push(ex);
					ex = null;
				}
			}
			else
			{
				Expression e = new Input(elems[index]);
				if(!pilha.isEmpty())
					if(pilha.peek().getClass() == Not.class)
					{
						((Not) pilha.peek()).setExpr(e);
						e =  pilha.pop();
					}
				if(ex==null)
					pilha.push(e);
				else
					ex.insertElement(e);
			}
			index++;
		}
		if(pilha.isEmpty())
			return ex;
		if(ex!=null)
			pilha.push(ex);
		if(pilha.size() == 1)
			return pilha.pop();
		
		ex = new Or();
		while(!pilha.isEmpty())
		{
			ex.insertElement(pilha.pop());
		}
		return ex;
	}

	private String[] splitExpr(String expr) {
		expr = expr.trim()
		           .replaceAll("  ", " ")
		   		   .replaceAll("~", "!")
		   		   .replaceAll("\\!", " ! ")
				   .replaceAll("\\+", " + ")
				   .replaceAll("\\*", ".")
				   .replaceAll("\\.", " . ")
				   .replaceAll("\\(", "( ")
				   .replaceAll("\\)", " )")
				   .replaceAll("  ", " ")
				   .trim();
		return expr.split(" ");
	}

	public void initInput(String inputs) {
		String[] list = inputs.trim().replaceAll("  ", " ").split(" ");
		ComponentManager cm = ComponentManager.getInstance();
		for (String it : list)
			cm.createComponent(IpinComponent.class, it);
	}
	
	public void initOutput(String inputs) {
		String[] list = inputs.trim().replaceAll("  ", " ").split(" ");
		ComponentManager cm = ComponentManager.getInstance();
		for (String it : list)
			cm.createComponent(OpinComponent.class, it);
	}

}