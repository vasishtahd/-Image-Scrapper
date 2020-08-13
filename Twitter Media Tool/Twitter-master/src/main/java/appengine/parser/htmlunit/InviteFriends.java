package appengine.parser.htmlunit;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by anand.kurapati on 20/06/17.
 */
public class InviteFriends {

    public static String invite_friends_script = "  var e=document.getElementsByName(\"confirm_button\");\n" +
            "var l = e.length;\n" +
            "var g = [];\n" +
            "g[0]=e[0];\n" +
            "/*for(var i=0;i<l;i++){\n" +
            " g[i] = e[i];\n" +
            "}*/\n" +
            "g.map(function(el){el.click();});";

    /*var e = document.getElementsByClassName("_42ft _4jy0 _4jy3 _4jy1 selected _51sy");
    var l = e.length;
    var g = [];
for(var i=0;i<l;i++){
        g[i] = e[i];
    }
g.map(function(el){el.click();});*/

    public static String invite_friends_wait_script = "  setInterval(function(){\n" +
            "        var e = document.getElementsByClassName(\"_42ft _4jy0 _4jy3 _4jy1 selected _51sy\");\n" +
            "        var g = [];\n" +
            "        g[0] = e[0];\n" +
            "        g.map(function(el){el.click();});\n" +
            "    },500);";

    public static String get_initial_Script() {

        String initial_script = "";
        try {
            initial_script = IOUtils.toString(new FileInputStream(new
                    File("initial_js_script.text")), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return initial_script;
    }

    public static final String remove_hidden_element = "var hiddenElHtml = document.getElementsByClassName('hidden_elem')[0]\n" +
            "    .innerHTML.replace('<!--', '').replace('-->', '');\n" +
            "\n" +
            "var divObj = document.createElement('div');\n" +
            "divObj.id='created_id';\n"+
            "divObj.innerHTML = hiddenElHtml;";

    public static void invite_friends_to_like_page() {
        //Invite friends to like page
            /*HtmlPage pageHomePage = webClient.getPage("https://www.facebook.com/4gchallenge/");
            HtmlButton htmlButton = (HtmlButton)pageHomePage.getFirstByXPath("//button[@class='_p _4jy0 _4jy4 _517h _51sy _42ft']");
            HtmlPage pageAfterOptionsCLick = htmlButton.click();
            pageAfterOptionsCLick.wait(5000);
            pageAfterOptionsCLick.getFirstByXPath("//span[@class='']");*/
    }

}
