# Image Scrapper Tools
Scripts to download collect all urls of images from web-page and download them.

## Google Image Scraper

Find this tool in the `Google Images Tool` folder.


### To Run

- Make the required google Image search.  

- Scroll Down to a point till where you are satisfied with the number of images in the search result

- Right click on the web result → Click on Inspect → Go to the console tab

- In the console tab → Copy paste the complete contents from the file ```image_google_downloader.js``` 

- All the urls of the images in the search results are collected and written to a file called ```urls.txt``` and is downloaded to the default downloads directory of your system

- Move the “urls.txt” file to the root directory of our project on Google Drive

- Set right path as a value to the variable ```args["urls"]``` in our Python / Google Collab File ```deep_image_scraper_classifier.ipynb```

- Set right path as a value to the variable ```args["info_output"]``` in our Python / Google Collab File to set the path of the output directory

- Use the ```image_download_from_urls(args)``` method run in the ```Fetch Image from Google Images``` section 



## Twitter Image Scraper

Find this tool in the `Twitter Media Tool` folder.

### To Run

- Open this project in Intellij editor or any spring supported editor to download all dependencies

- Run the project using the run button

- Access the url the following url on a web browser to download all the related images 

> `http://localhost:8080/twitter/search/<tag_name>`  

>>>  ------------------------ OR -------------------------

- You can directly run the Twitter.getTweets() function from Java


