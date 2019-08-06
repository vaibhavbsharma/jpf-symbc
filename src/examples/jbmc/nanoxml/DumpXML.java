package jbmc.nanoxml;

import veritesting.nanoxml.IntReader;
import veritesting.nanoxml.StdXMLParser;
import veritesting.nanoxml.StdXMLReader;

import org.cprover.CProver;

public class DumpXML {

    public static void mainProcess(char i0, char i1, char i2, char i3, char i4, char i5, char i6, char i7, char i8){

        char[] str = new char[9];
        str[0] = CProver.nondetChar();
        str[1] = CProver.nondetChar();
        str[2] = CProver.nondetChar();
        str[3] = CProver.nondetChar();
        str[4] = CProver.nondetChar();
        str[5] = CProver.nondetChar();
        str[6] = CProver.nondetChar();
        str[7] = CProver.nondetChar();
        str[8] = i8;
        StdXMLParser parser = new StdXMLParser();

        IntReader intReader = new IntReader(str);
        StdXMLReader stdXMLReader =  new StdXMLReader(intReader);
        parser.setReader(stdXMLReader);

//		StdXMLBuilder builder = new StdXMLBuilder();
//		parser.setBuilder(builder);

//		NonValidator nonValidator = new NonValidator();
//		parser.setValidator(nonValidator);
        try{
            parser.parse();
            System.out.println("No output!");
        }
        catch(Exception e){
            e.printStackTrace();
        }

//		(new XMLWriter(System.out)).write(xml);
    }
    public static void main(String args[]){
        mainProcess('<','a', '>', ';', '<', '!', '?', '>', '/');
    }
}
