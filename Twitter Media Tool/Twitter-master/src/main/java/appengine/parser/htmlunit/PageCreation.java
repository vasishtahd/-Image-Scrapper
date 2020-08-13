package appengine.parser.htmlunit;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

import java.io.IOException;

/**
 * Created by anand.kurapati on 20/06/17.
 */
public class PageCreation {

    public static HtmlPage createPageMobile(WebClient webClient) {


        HtmlPage createPagePage = null;
        try {
            createPagePage = webClient.getPage("https://m.parser.facebook.com/pages/creation_flow/?step=name&cat_ref_page_id=0&ref_type=bookmark");
            //return createPagePage;
            webClient.waitForBackgroundJavaScriptStartingBefore(2000);
            for (int i = 0; i < 5; i++) {

                synchronized (createPagePage) {
                    createPagePage.wait(500);
                }
            }

            HtmlInput pageInput = (HtmlInput) createPagePage.getFirstByXPath("//input[@class='_56bg _55ws _rtz']");
            pageInput.click();
            pageInput.focus();
            System.out.print(createPagePage.executeJavaScript("document.getElementsByClassName(\"_56bg _55ws _rtz\")[0].value=\"hey\""));
            Keyboard keyboard = new Keyboard();
            keyboard.press(32);
            keyboard.type('A');
            for (int i = 0; i < 1; i++) {

                synchronized (createPagePage) {
                    createPagePage.wait(500);
                }
            }
            //createPagePage.executeJavaScript("document.getElementsByClassName(\"_54k8 _56bs _56b_ _56bx _56bu\")[0].click()");

            /*System.out.print(createPagePage.executeJavaScript("document.getElementsByClassName(\"_54k8 _56bs _56b_ _56bx _56bu\")[0].click()")
                    );*/

            HtmlButtonInput button = (HtmlButtonInput) createPagePage.getFirstByXPath("//button[@class='_54k8 _56bs _56b_ _56bx _56bu']");
            System.out.println(button.asXml());
            button.mouseOver();
            //System.out.print(button.dblClick());
            HtmlPage selectCategoryPage = button.click();


            //HtmlPage selectCategoryPage = webClient.getPage(createPagePage.executeJavaScript("document.getElementsByClassName(\"_54k8 _56bs _56b_ _56bx _56bu\")[0].click()").
            //       getNewPage().getUrl());
            //createPagePage.wait(2000);
            //System.out.print(createPagePage.querySelectorAll().asXml());
            //HtmlInput pageInput = (HtmlInput) createPagePage.getFirstByXPath("//input[@class='_56bg _55ws _rtz']");
            //pageInput.focus();
            // pageInput.setValueAttribute("Hello Auto");
            // HtmlButton button = (HtmlButton) createPagePage.getFirstByXPath("//button[@class='_54k8 _56bs _56b_ _56bx _56bu']");
            //  System.out.println(button.asXml());
            //  button.mouseOver();
            //System.out.print(button.dblClick());
            // HtmlPage selectCategoryPage = button.click();

            return  selectCategoryPage;
            //return selectCategoryPage;
            /*while (manager.getJobCount() > 0) {
                Thread.sleep(1000);
                System.out.print("Sleeping");
            }*/
            // createPagePage.wait(5000);
            //webClient.waitForBackgroundJavaScript(3000);
           /* ScriptResult result = createPagePage.executeJavaScript("PagesCreateLegacySlider.slideUp(\"celebrity\")");

            createPagePage = createPagePage.getElementById("u_0_1a").click();
            //createPagePage.executeJavaScript("document.getElementById(\"u_0_b\").click()");
            createPagePage.wait(3000);
            //webClient.waitForBackgroundJavaScript(3000);

            createPagePage = createPagePage.getElementsByName("_54nh").get(0).click();

            createPagePage.getElementById("celebrity_form_page_name").setNodeValue("Testing User");

            createPagePage = (HtmlPage) ((HtmlForm) createPagePage.getElementById("celebrity_form")).fireEvent(Event.TYPE_SUBMIT).getNewPage();
           */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createPagePage;

    }


    public static HtmlPage createPage(WebClient webClient) {


        HtmlPage createPagePage = null;
        try {
            createPagePage = webClient.getPage("https://www.parser.facebook.com/pages/create/?ref_type=logout_gear");
            JavaScriptJobManager manager = createPagePage.getEnclosingWindow().getJobManager();
            while (manager.getJobCount() > 0) {
                Thread.sleep(1000);
            }
            // createPagePage.wait(5000);
            //webClient.waitForBackgroundJavaScript(3000);
           /* ScriptResult result = createPagePage.executeJavaScript("PagesCreateLegacySlider.slideUp(\"celebrity\")");

            createPagePage = createPagePage.getElementById("u_0_1a").click();
            //createPagePage.executeJavaScript("document.getElementById(\"u_0_b\").click()");
            createPagePage.wait(3000);
            //webClient.waitForBackgroundJavaScript(3000);

            createPagePage = createPagePage.getElementsByName("_54nh").get(0).click();

            createPagePage.getElementById("celebrity_form_page_name").setNodeValue("Testing User");

            createPagePage = (HtmlPage) ((HtmlForm) createPagePage.getElementById("celebrity_form")).fireEvent(Event.TYPE_SUBMIT).getNewPage();
           */
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return createPagePage;
    }

    public static HtmlPage createPageViaHomeClick(WebClient webClient) {

        HtmlPage createPagePage = null;
        try {
            createPagePage = webClient.getPage("https://www.parser.facebook.com/pages/create/?ref_type=logout_gear");
            // createPagePage.wait(5000);
            //webClient.waitForBackgroundJavaScript(3000);
            ScriptResult result = createPagePage.executeJavaScript("PagesCreateLegacySlider.slideUp(\"celebrity\")");

            createPagePage = createPagePage.getElementById("u_0_1a").click();
            //createPagePage.executeJavaScript("document.getElementById(\"u_0_b\").click()");
            createPagePage.wait(3000);
            //webClient.waitForBackgroundJavaScript(3000);

            createPagePage = createPagePage.getElementsByName("_54nh").get(0).click();

            createPagePage.getElementById("celebrity_form_page_name").setNodeValue("Testing User");

            createPagePage = (HtmlPage) ((HtmlForm) createPagePage.getElementById("celebrity_form")).fireEvent(Event.TYPE_SUBMIT).getNewPage();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return createPagePage;
    }
}
