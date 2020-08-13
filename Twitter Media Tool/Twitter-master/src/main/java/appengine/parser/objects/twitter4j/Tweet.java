package appengine.parser.objects.twitter4j;

import appengine.parser.utils.StringUtils;
import twitter4j.*;

import java.util.ArrayList;

/**
 * Created by anand.kurapati on 18/06/17.
 */
public class Tweet {

    public Status status;

    public Tweet(Status status) {

        this.status = status;
    }

    public String getPreviewUrl() {

        if (status.getURLEntities().length > 0) {

            return status.getURLEntities()[0].getExpandedURL();
        }

        return "";
    }

    public String getImageUrl() {

        if (status.getMediaEntities().length > 0) {
            return status.getMediaEntities()[0].getMediaURL();
        }
        return "";

    }

    public String getFormattedTweet() {
        String formattedText = status.getText() + "\n";
        if (formattedText != null) {
            formattedText = formattedText.replace("&amp;", "&");
            formattedText = formattedText.replace("RT", "");
        }

        formattedText = formattedText.replaceAll("[@].*?[ ]", "");
        formattedText = formattedText.replaceAll("[http].*?[ ]", "");
        formattedText = formattedText.replaceAll("[http].*?[\n]", "");

        return formattedText;
    }

    public boolean isWithPreview() {
        if (StringUtils.isNonEmpty(getImageUrl()) || StringUtils.isNonEmpty(getPreviewUrl())) {
            return true;
        }
        return false;
    }

    public boolean isWithImage() {
        if (StringUtils.isNonEmpty(getImageUrl())) {
            return true;
        }
        return false;
    }


    public String getOEmbedUrl(Twitter twitter) {
        try {
            return twitter.getOEmbed(new OEmbedRequest(status.getId(), "")).getURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getEmbedScript() {
        return "<blockquote class=\"twitter-tweet\" data-lang=\"en\"><p lang=\"en\" dir=\"ltr\">" +
                status.getText() + getEmbedHyperLinkStrings(getImageUrl()) + getEmbedHyperLinkStrings(getPreviewUrl()) +
                "</p></blockquote>" +
                "<script async src=\"//platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";

    }

    private String getEmbedHyperLinkStrings(String string) {

        return "<a href =\"" + string + "\">" + string + "</a>";
    }

    public boolean similarTweetExists(ArrayList<Tweet> tweetedList) {

        double similarity = 0;
        for (Tweet tweeted : tweetedList) {

            double temp_similarity = StringUtils.similarity(getFormattedTweet(), tweeted.getFormattedTweet());
            System.out.println("Similarity : " + temp_similarity + " " + getFormattedTweet() +
                    " " + tweeted.getFormattedTweet());

            if (similarity < temp_similarity)
                similarity = temp_similarity;

            if(getImageUrl().equals(tweeted.getImageUrl())){
                return true;
            }

            if(similarity>80)
                return true;
        }

        if (similarity > 80)
            return true;
        else
            return false;
    }
}
