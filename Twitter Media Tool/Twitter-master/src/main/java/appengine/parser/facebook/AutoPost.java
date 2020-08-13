package appengine.parser.facebook;

import appengine.parser.htmlunit.InviteFriends;
import appengine.parser.objects.UserCredentials;
import appengine.parser.repository.BaseRepository;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.*;

/**
 * Created by anand.kurapati on 13/12/17.
 */
public class AutoPost {

    BaseRepository baseRepository;

    public AutoPost(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    public void loginAndPost() {
        for (UserCredentials userCredentials : baseRepository.getUserCredentials()) {
            loginAndPost(userCredentials);
            break;
        }
    }

    public void loginAndPost(UserCredentials userCredentials) {
        loginAndPostUtil(userCredentials);
    }

    private void loginAndPostUtil(UserCredentials userCredentials) {

        try {
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            HtmlPage loggedinPage = createLogin(webClient, userCredentials);

            createHtmlPreview(loggedinPage.asXml(), "beforescript");

            webClient.waitForBackgroundJavaScript(3000);

            ///y//HtmlDivision htmlDivision = (HtmlDivision) loggedinPage.getFirstByXPath("//div[@class='hidden_elem']");


            /*String wholetext = htmlDivision.asXml().replace("<!--","").replace("-->","");

            Logger.logMsg(Logger.INFO,"Anand" + wholetext);

            DomElement divElement = loggedinPage.createElement("div");
            divElement.setId("createdid");
            divElement.setNodeValue(wholetext);



            ScriptResult scriptResult = loggedinPage.executeJavaScript("" +
                    "var hiddenElHtml = document.getElementsByClassName('hidden_elem')[0].innerHTML.replace('<!--', '').replace('-->', '');"
                    + " var divObj = document.createElement('div');divObj.innerHTML = hiddenElHtml; divObj.id='createdscriptid';");
            */

            ScriptResult scriptResult = loggedinPage.executeJavaScript(InviteFriends.get_initial_Script());
            loggedinPage = (HtmlPage) scriptResult.getNewPage();

            createHtmlPreview(loggedinPage.asXml(), "afterscript");

            HtmlTextArea htmlTextInput = (HtmlTextArea) loggedinPage.getElementByName("xhpc_message");
            htmlTextInput.type("Hello world");

            createHtmlPreview(loggedinPage.asXml(), "complete");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HtmlPage createLogin(WebClient webClient, UserCredentials userCredentials) {

        HtmlPage loggedin_page = null;

        try {
            final HtmlPage loggedout_page = webClient.getPage("http://www.facebook.com");
            final HtmlForm form = (HtmlForm) loggedout_page.getElementById("login_form");
            final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputsByValue("Log In").get(0);
            final HtmlEmailInput textField = form.getInputByName("email");
            textField.setValueAttribute(userCredentials.userName);
            final HtmlPasswordInput textField2 = form.getInputByName("pass");
            textField2.setValueAttribute(userCredentials.password);
            loggedin_page = button.click();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loggedin_page;
    }

    public void createHtmlPreview(String content, String name) {

        BufferedWriter bufferedWriter = null;
        try {
            String strContent = content;
            File myFile = new File(name + "fb.html");
            // check if file exist, otherwise create the file before writing
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            Writer writer = new FileWriter(myFile);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(strContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (Exception ex) {

            }
        }

    }


}
