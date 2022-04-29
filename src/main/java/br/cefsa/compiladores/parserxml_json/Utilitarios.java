/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.cefsa.compiladores.parserxml_json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/*
Planejamento da Sequencia do algoritmo implementado

 1 Passo: Todo < deve ser precedido de { e todo </ deve conter } após final tag

        Entrada:
        <employees>
          <employee>
            <firstName>John</firstName> <lastName>Doe</lastName>
          </employee>
          <employee>
            <firstName>Anna</firstName> <lastName>Smith</lastName>
          </employee>
          <employee>
            <firstName>Peter</firstName> <lastName>Jones</lastName>
          </employee>
        </employees>

        Saída:
        {<employees>
         {<employee>
                {<firstName>John</firstName>} {<lastName>Doe</lastName>}
          </employee>}
            {<employee>
                {<firstName>Anna</firstName>} {<lastName>Smith</lastName>}
          </employee>}
            {<employee>
                {<firstName>Peter</firstName>} {<lastName>Jones</lastName>}
          </employee>}
        </employees>}

2 Passo: Remover todos as tags de fechamento do XML.
            Exemplo:

            Entrada:
            {<employees>
             {<employee>
                    {<firstName>John</firstName>} {<lastName>Doe</lastName>}
              </employee>}
                {<employee>
                    {<firstName>Anna</firstName>} {<lastName>Smith</lastName>}
              </employee>}
                {<employee>
                    {<firstName>Peter</firstName>} {<lastName>Jones</lastName>}
              </employee>}
            </employees>}

            Saída:
            {<employees>
                {<employee>
                    {<firstName>John} {<lastName>Doe}
                }
                {<employee>
                    {<firstName>Anna} {<lastName>Smith}
                }
                {<employee>
                    {<firstName>Peter} {<lastName>Jones}
                }
            }

3 Passo: Identificar se existe algum array. Para isso, sera verificado as
        seguintes condições. Tags repetem mais de uma vez, e em sequencia vem outra Tag
        Se sim, então é Array

        {<employees>
           [{<employee>
                {<firstName>John} {<lastName>Doe}
            <employee>}
            {<employee>
                {<firstName>Anna} {<lastName>Smith}
            <employee>}
            {<employee>
                {<firstName>Peter} {<lastName>Jones}
            }]
        }

4 Passo: Tags que são array devem ser apagados:

Saída:
{<employees>
   [{
        {<firstName>John} {<lastName>Doe}
    }
    {
        {<firstName>Anna} {<lastName>Smith}
    }
    {
        {<firstName>Peter} {<lastName>Jones}
    }]
}

5 Passo: 
Substituir  < e > por " 
Substituir }{ por ,
adicionar : após >

{"employees":
   [{
        {"firstName":John,"lastName":Doe},
    
    
        {"firstName":Anna,"lastName":Smith},
    
    
        {"firstName":Peter,"lastName":Jones}
    }]
}


7 Passo: Eliminar redundancias. 
Se [{ sobra [.
Se {{ sobra {
Se {[ sobra [
Se }] sobra ]
Se }} sobra }

{"employees":
   [
        {"firstName":John,"lastName":Doe},
    
        {"firstName":Anna,"lastName":Smith},
    
        {"firstName":Peter,"lastName":Jones}
    ]
}



*/


public class Utilitarios 
{
	public static String lerArquivoTxt(String endereco)
	{
		Scanner ler = new Scanner(System.in);
		String conteudo = "";
		
		try {
		      FileReader arq = new FileReader(endereco);
		      BufferedReader lerArq = new BufferedReader(arq);

		      String linha = lerArq.readLine();
		      conteudo += linha + "\n";

		      while (linha != null) {

		        linha = lerArq.readLine();
		        
		        if(linha != null)
		        	conteudo += linha + "\n";
		      }
		      arq.close();
		    } 
		catch (IOException e) 
		{
		        System.err.printf("Erro na abertura do arquivo: %s.\n",
		          e.getMessage());
		}
		return conteudo;
	}
	
	public static void gravarArquivoTxt(String endereco, String conteudo) throws IOException
	{
		 FileWriter arq = new FileWriter(endereco);
		 PrintWriter gravarArq = new PrintWriter(arq);
		 
		 gravarArq.printf(conteudo);
		 
		 arq.close();
		 
	}
        
        /*
       
        */
        public static String[] primeiroPasso (String[] xml)
        {
            String passoUm = "";
            boolean fechou = false;
            
            for(int i = 0; i < xml.length; i++)
            {
                for(int j = 0; j < xml[i].length(); j++)
                {
                    if(xml[i].charAt(j) == '<')
                    {
                        passoUm += '{';
                        passoUm += xml[i].charAt(j);
                    }
                        
                    else
                        passoUm += xml[i].charAt(j);
                    
                    if(xml[i].charAt(j) == '/')
                        fechou = true;
                    
                    if(fechou && xml[i].charAt(j) == '>')
                    {
                        passoUm += '}';
                    }
                        
                }
                passoUm += '\n';
            }
            
            String[] passoUmLinhas = passoUm.split("\n");
            
            return passoUmLinhas;
        }
        
        /*
        
        */
        
        public static String[] segundoPasso (String[] xml)
        {
            String passoDois = "";
            
            for(int i = 0; i < xml.length; i++)
            {
                for(int j = 0; j < xml[i].length(); j++)
                {
                    if(xml[i].charAt(j) != '/')
                        passoDois += xml[i].charAt(j);
                }
                passoDois += '\n';
            }
            
            String[] passoDoisLinhas = passoDois.split("\n");
            return passoDoisLinhas;
            
        }
        
        /*
           {<employees>
                [{<employee>
                     {<firstName>John} {<lastName>Doe}
                 <employee>}
                 {<employee>
                     {<firstName>Anna} {<lastName>Smith}
                 <employee>}
                 {<employee>
                     {<firstName>Peter} {<lastName>Jones}
            <employees>}]
        }
        */
        
        public static String[] terceiroPasso (String[] xml)
        {
            String tagAnterior = "";
            String tagAtual = "";
            String valorAtual = "";
            String passoTres = "";
            List<XmlObjeto> tags = new ArrayList<XmlObjeto>();
            boolean tag = false;
            boolean isArray = false;
            Stack<String> tagsAbertas = new Stack<>();

            
            for(int i = 0; i < xml.length; i++)
            {
                for(int j = 0; j < xml[i].length(); j++)
                {
                    XmlObjeto x = new XmlObjeto();
                    passoTres += xml[i].charAt(j);
                    
                    if(xml[i].charAt(j) == '<')
                    {
                        tag = true;
                        x.setValor(valorAtual);
                        valorAtual = "";
                    }
                        
                    if(xml[i].charAt(j) == '>')
                    {
                        tag = false;
                        tagAtual += '>';
                        x.setChave(tagAtual);
                        String temp = x.getChave();
                        if (!tagsAbertas.empty() && tagsAbertas.lastElement() == temp){
                            tagAnterior = tagsAbertas.pop();
                        }
                        else{
                            
                            tagsAbertas.add(temp);
                            if (temp == tagAnterior && !isArray){
                                isArray = true;
                                xml[i] = '[' + xml[i];
                                xml[i].replace(temp, "");
                            }
                            else if (temp == tagAnterior){
                                xml[i].replace(temp, "");
                            }
                            else if (temp != tagAnterior && isArray){
                                isArray = false;
                                xml[i] += ']';

                            }
                            else{isArray = false;}
                        }
                        tagAtual = "";
                        tags.add(x);
                    }
                        
                    if(tag)
                        tagAtual += xml[i].charAt(j);
                    else
                    {
                        if(xml[i].charAt(j) != '{' &&
                                xml[i].charAt(j) != '<' &&
                                xml[i].charAt(j) != '>' &&
                                xml[i].charAt(j) != '}')
                            valorAtual += xml[i].charAt(j);
                    }


                }
                passoTres += "\n";
            }
            String[] passoTresV = passoTres.split("\n");
            System.out.println(tagsAbertas);
            
            return passoTresV;
        }

        public static String[] ultimoPasso(String[] xmlEmLinhas){
            for(int i = 0; i < xmlEmLinhas.length; i++)
            {
                xmlEmLinhas[i].replace("[{", "[");
                xmlEmLinhas[i].replace("{[", "[");
                xmlEmLinhas[i].replace("}]", "]");
                xmlEmLinhas[i].replace("{{", "{");
                xmlEmLinhas[i].replace("}}", "}");
                xmlEmLinhas[i].replace('<', '"');
                if (xmlEmLinhas[i].contains(">")){
                    xmlEmLinhas[i].replace('>', '"'); 
                    //colocar : dps do >    
                }
                xmlEmLinhas[i].replace('}', ',');
            }

            return xmlEmLinhas;
        }
        
        public static String converteXMLParaJSON (String xmlString)
        {
            String json = "";
            String tagAtual = "";
            String charAt = "";
            Stack<String> tags = new Stack<String>();
            Stack<String> validaTag = new Stack<String>();
            int indexInicio = -1;
            int indexFim = -1;
            boolean tagAberto = false;
            boolean contemArray = false;
            
            String[] xmlEmLinhas = xmlString.split("\n");
            
            for(int i = 0; i < xmlEmLinhas.length; i++)
            {
                xmlEmLinhas[i] = xmlEmLinhas[i].trim();
            }
            
            String[] passoUm = primeiroPasso(xmlEmLinhas);
            String[] passoDois = segundoPasso(passoUm);
            String[] passoTres = terceiroPasso(passoDois);
            String[] ultimoPasso = ultimoPasso(passoTres);
            for (String string : ultimoPasso) {
                System.out.println(string);
            }
            //json = Arrays.toString(ultimoPasso);
            
            return json;
        }
	


}
