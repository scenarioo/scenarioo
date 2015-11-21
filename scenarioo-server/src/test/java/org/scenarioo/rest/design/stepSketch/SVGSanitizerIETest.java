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
}
