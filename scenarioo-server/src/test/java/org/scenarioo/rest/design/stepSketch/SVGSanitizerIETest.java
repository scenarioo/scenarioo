package org.scenarioo.rest.design.stepSketch;

import static org.junit.Assert.*;

import org.junit.Test;

public class SVGSanitizerIETest {
	
	@Test
	public void sanitize_withTwoXmlns_RemovesSuperfluousXmlns(){
		String expectedSVG = "<svg height=\"1000\" width=\"1583\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" encoding=\"utf-8\" style=\"overflow: visible;\" id=\"SvgjsSvg1068\"><image xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=";
		String inputSVG    = "<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"1000\" width=\"1583\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" encoding=\"utf-8\" style=\"overflow: visible;\" id=\"SvgjsSvg1068\"><image xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=";
		assertEquals(expectedSVG, SVGSanitizerIE.sanitize(inputSVG));
	}
	
	@Test
	public void sanitize_withSingleXmlns_DoesNotModifySVG(){
		String expectedSVG = "<svg height=\"1000\" width=\"1583\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" encoding=\"utf-8\" style=\"overflow: visible;\" id=\"SvgjsSvg1068\"><image xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=";
		String inputSVG    = expectedSVG;
		assertEquals(expectedSVG, SVGSanitizerIE.sanitize(inputSVG));
	}
	
	@Test
	public void sanitize_withNS1_RemovesNS1(){
		String expectedSVG = "<svg id=\"SvgjsSvg1106\" style=\"overflow: visible;\" width=\"1369\" height=\"1637\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" encoding=\"utf-8\"><image id=\"SvgjsImage1104\" draggable=\"false\"";
		String inputSVG    = "<svg id=\"SvgjsSvg1106\" style=\"overflow: visible;\" width=\"1369\" height=\"1637\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" xmlns:NS1=\"\" NS1:xmlns:xlink=\"http://www.w3.org/1999/xlink\" encoding=\"utf-8\"><image id=\"SvgjsImage1104\" draggable=\"false\"";
		assertEquals(expectedSVG, SVGSanitizerIE.sanitize(inputSVG));
	}
	
	@Test
	public void sanitize_withoutNS1_DoesNotModifySVG(){
		String expectedSVG = "<svg id=\"SvgjsSvg1106\" style=\"overflow: visible;\" width=\"1369\" height=\"1637\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" encoding=\"utf-8\"><image id=\"SvgjsImage1104\" draggable=\"false\"";
		String inputSVG	   = "<svg id=\"SvgjsSvg1106\" style=\"overflow: visible;\" width=\"1369\" height=\"1637\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" encoding=\"utf-8\"><image id=\"SvgjsImage1104\" draggable=\"false\"";
		assertEquals(expectedSVG, SVGSanitizerIE.sanitize(inputSVG));
	}
}
