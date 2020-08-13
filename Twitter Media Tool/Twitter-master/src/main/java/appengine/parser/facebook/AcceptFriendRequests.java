package appengine.parser.facebook;

import appengine.parser.htmlunit.InviteFriends;
import appengine.parser.objects.UserCredentials;
import appengine.parser.repository.BaseRepository;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.*;

/**
 * Created by anand.kurapati on 27/06/17.
 */
public class AcceptFriendRequests {

    private int DEFAULT_ACCEPT_COUNT = 500;

    private int NTHREDS = 4;

    BaseRepository baseRepository;

    public AcceptFriendRequests(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    public void acceptFriendRequestsParallelly() {

        /*try {
            ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);

            for (UserCredentials userCredentials : ConstantsData.userCredentials) {

                Runnable worker = new FriendRequestRunnable(userCredentials);
                executor.execute(worker);
                // This will make the executor accept no new threads
                // and finish all existing threads in the queue
                executor.shutdown();
                // Wait until all threads are finish
                executor.awaitTermination(60, TimeUnit.MINUTES);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished all threads");*/
        for (UserCredentials userCredentials : baseRepository.getUserCredentials()) {
            loginAndAcceptFriendRequest(userCredentials);
        }


    }

    private class FriendRequestRunnable implements Runnable {

        UserCredentials userCredentials;

        FriendRequestRunnable(UserCredentials userCredentials) {
            this.userCredentials = userCredentials;
        }

        public void run() {

            loginAndAcceptFriendRequest(userCredentials);

        }
    }

    private void loginAndAcceptFriendRequest(UserCredentials userCredentials) {

        try {
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            createLogin(webClient, userCredentials);

            HtmlPage friendrequestPage = (HtmlPage) openFriendsPageAndInject(webClient);

            for (int i = 0; i < DEFAULT_ACCEPT_COUNT; i++)
                friendrequestPage = (HtmlPage) friendrequestPage.executeJavaScript(InviteFriends.invite_friends_script).getNewPage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Page openFriendsPageAndInject(WebClient webClient) {

        try {
            HtmlPage friendRequestsPage = webClient.getPage("https://m.facebook.com/friends/requests/?split=1&fcref=ft");
            // friendRequestsPage.executeJavaScript(InviteFriends.get_initial_Script());
            ScriptResult scriptResult = friendRequestsPage.executeJavaScript(InviteFriends.invite_friends_script);
            Page newpage = scriptResult.getNewPage();
            return newpage;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return loggedin_page;
    }


    public void createHtmlPreview(String content) {

        BufferedWriter bufferedWriter = null;
        try {
            String strContent = content;
            File myFile = new File("fb.html");
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
