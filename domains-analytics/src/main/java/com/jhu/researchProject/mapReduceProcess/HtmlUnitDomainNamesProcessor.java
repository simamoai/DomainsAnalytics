package com.jhu.researchProject.mapReduceProcess;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitDomainNamesProcessor {
	private WebClient webClient;

	public HtmlUnitDomainNamesProcessor() {
		webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(false);
		webClient.getCookieManager().setCookiesEnabled(false);
	}

	public String getWebPageContent(String webUrl) throws Exception {
		System.out.println("Connecting to: " + webUrl);
		HtmlPage page = webClient.getPage(webUrl);
		HtmlBody pageBody = (HtmlBody) page.getBody();
		// System.out.println(pageBody.asText());
		webClient.closeAllWindows();
		return pageBody.asText();
	}

	public static void main(String[] args) throws Exception {
		String testDomainName = "NS2.SAXOPHONE";
		HtmlUnitDomainNamesProcessor processor = new HtmlUnitDomainNamesProcessor();
		System.out.println(processor.getWebPageContent("http://"
				+ testDomainName.substring(testDomainName.indexOf(".") + 1)
				+ ".com"));
	}
}