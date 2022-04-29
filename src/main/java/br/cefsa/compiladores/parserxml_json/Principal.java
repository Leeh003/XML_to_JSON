/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.cefsa.compiladores.parserxml_json;

import java.io.IOException;

/**
 *
 * @author PC Novo
 */
public class Principal {
    
   public static void main(String[] args) throws IOException 
	{
		String enderecoTxtLeitura = "C:/Users/USUARIO/Documents/Leeh's/FTT/6° Bi/Compiladores/ParserXML_JSON/ParserXML_JSON/arquivoTXT/xml.txt";//"arquivoTXT\\xml.txt";
		String enderecoTxtGravacao = "C:/Users/USUARIO/Documents/Leeh's/FTT/6° Bi/Compiladores/ParserXML_JSON/ParserXML_JSON/arquivoTXT/json.txt";//"arquivoTXT\\json.txt";
		
		String xml = Utilitarios.lerArquivoTxt(enderecoTxtLeitura);
		
                String json = Utilitarios.converteXMLParaJSON(xml);

		Utilitarios.gravarArquivoTxt(enderecoTxtGravacao, json);
		
		System.out.println("Fim das execuções");

	}
    
}
