package com.bushbungalo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.bushbungalo.model.CityData;

/**
 * @author Paul O. Patterson
 * <br />
 * <b style="margin-left:-40px">Date Created:</b>
 * <br />
 * 11/21/17
 * <br />
 */

public class XMLHelper
{
	public static final String PREVIOUSLY_FOUND_CITIES_XML = "res/storage/previous_cities.xml";

	public XMLHelper()
	{
	}// default constructor
	
	public static boolean exportToXML( CityData cityData )
    {    
	    try 
        {
	    	File xmlFile = new File( PREVIOUSLY_FOUND_CITIES_XML );
	    	Element worldCities = null; 
	    	Document doc = null; 
	    	Element city = new Element( "City" );
	    	
        	if( !xmlFile.exists() ) 
        	{	
        		worldCities = new Element( "WorldCities" ); 
    	    	doc = new Document( worldCities );        		     		
        	}// end of if block
        	else 
        	{
        		FileInputStream fis = new FileInputStream( xmlFile );
    	        SAXBuilder builder = new SAXBuilder();
    	        doc = builder.build( fis );
    	        worldCities = doc.getRootElement();
    	        fis.close();
        	}// end of else block
        	
        	city.addContent( new Element( "CityName" ).setText( cityData.getCityName() ) );
    		city.addContent( new Element( "CountryName" ).setText( cityData.getCountryName() ) );
    		city.addContent( new Element( "CountryCode" ).setText( cityData.getCountryCode() ) );
    		city.addContent( new Element( "RegionName" ).setText( cityData.getRegionName() ) );
    		city.addContent( new Element( "RegionCode" ).setText( cityData.getRegionCode() ) );
    		city.addContent( new Element( "Latitude" ).setText( String.valueOf( cityData.getLatitude() ) ) );
    		city.addContent( new Element( "Longitude" ).setText( String.valueOf( cityData.getLongitude() ) ) ); 
        	
        	doc.getRootElement().addContent( city );
    		
    		// new XMLOutputter().output(doc, System.out);
    		XMLOutputter xmlOutput = new XMLOutputter();
    		
    		// display nice nice
    		xmlOutput.setFormat( Format.getPrettyFormat() );
    		xmlOutput.output( doc, new FileWriter( PREVIOUSLY_FOUND_CITIES_XML ) );  
	    				
			return true;
		}// end of try block
        catch ( FileNotFoundException e )
        {
        	UtilityMethod.logMessage( "severe", e.getMessage(), "XMLHelper::exportToXML" );
		}// end of catch block
        catch ( IOException e )
        {
        	UtilityMethod.logMessage( "severe", e.getMessage(), "XMLHelper::exportToXML" );
		}// end of catch block
	    catch ( JDOMException e )
	    {
	    	UtilityMethod.logMessage( "severe", e.getMessage(), "XMLHelper::exportToXML" );
			e.printStackTrace();
		}// end of catch block
		 
		 return false;
    }// end of method  exportToXML
	
	public static List< CityData > importFromXML()
    {    	
    	SAXBuilder builder = new SAXBuilder();
    	List< CityData > cd = new ArrayList< CityData >();
    	
    	try 
    	{
    		// just in case the document contains unnecessary white spaces
    		builder.setIgnoringElementContentWhitespace( true );
    		
    		// download the document from the URL and build it
    		Document document = builder.build( PREVIOUSLY_FOUND_CITIES_XML );
    		
    		// get the root node of the XML document
    		Element rootNode = document.getRootElement();
    		
    		List< Element > list = rootNode.getChildren( "City" );
    		
    		for ( int i = 0; i < list.size(); i++ )
    		{
    			Element node = ( Element ) list.get( i );    			
    			
    			cd.add( new CityData( node.getChildText( "CityName" ), node.getChildText( "CountryCode" ),
    					              node.getChildText( "RegionName" ), node.getChildText( "RegionCode" ),
    					              node.getChildText( "CountryName" ), Float.parseFloat( node.getChildText( "Latitude" ) ),
        				              Float.parseFloat( node.getChildText( "Longitude") ) ) 
    			      );
    			
    		}// end of for loop    		 		
    		
    	}// end of try block 
    	catch ( IOException io )
    	{
    		cd = null;
    		UtilityMethod.logMessage( "severe", io.getMessage(), "XMLHelper::importFromXML" );
    	}// end of catch block 
    	catch ( JDOMException jdomex )
    	{
    		cd = null;
    		UtilityMethod.logMessage( "severe", jdomex.getMessage(), "XMLHelper::importFromXML" );
    	}// end of catch block
		
       return cd;
    }// end of method importFromXML

}// end of class XMLHelper
