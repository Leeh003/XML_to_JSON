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
    }
    {<employee>
        {<firstName>Anna} {<lastName>Smith}
    }
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
            
            for(int i = 0; i < xmlEmLinhas.length; i++)
            {
                
                for(int k = 0; k < xmlEmLinhas[i].length(); k++)
                {
                    if(xmlEmLinhas[i].charAt(k) == '<')
                    {
                        charAt = "";
                        indexInicio = k;
                        tagAberto = true;
                        charAt += xmlEmLinhas[i].charAt(k);
                        validaTag.add(charAt);
                    }
                    else if (xmlEmLinhas[i].charAt(k) == '>')
                    {
                        indexFim = k;
                        
                        if(indexInicio != -1 && indexFim != -1)
                            tagAtual = xmlEmLinhas[i].substring(indexInicio, indexFim);
                        
                        tags.add(tagAtual);
                        indexInicio = -1;
                        indexFim = -1;
                    }
                    else if (xmlEmLinhas[i].charAt(k) == '/')
                    {
                        charAt = "";
                        charAt += xmlEmLinhas[i].charAt(k);
                        validaTag.add(charAt);
                        
                        tags.pop();
                        
                    }
                    
                    
                }
                
                
            }
            

            
            return json;
        }
	


}
