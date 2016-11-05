# Stock-Viewer-App
**Stock Search Android App with Facebook Post** 

 **1. Objectives**
 - Become familiar with Android Studio, Android App development and Facebook SDK for Android.
 - Build a good-looking Android app using the Android SDK. Ø Add social networking features using the Facebook SDK.

**2. Background**
 
 **2.1 Android Studio**
Android Studio is the official IDE for Android application development, based on IntelliJ IDEA (https://www.jetbrains.com/idea/). 

**2.2. Android**
Android is a mobile operating system consisting of Java applications running on a Java based object oriented application framework on top of Java core libraries running on the Dalvik virtual machine featuring JIT compilation.
The Official Android home page is located at:
http://www.android.com/
The Official Android Developer home page is located at:
http://developer.android.com/index.html

**2.3 Facebook**
Facebook provides developers with an application-programming interface, called the Facebook Platform.

**2.4 Markit on Demand**
Markit on Demand API provides detailed description about the stock information of company as well as historical stock values. You can refer to the API description on the following link:
http://dev.markitondemand.com/MODApis/

**2.5 Google App Engine (GAE)**
Google App Engine applications are easy to create, easy to maintain, and easy to scale as your traffic and data storage needs change. With App Engine, there are no servers to maintain. You simply upload your application and it’s ready to go. To learn more about GAE support for PHP visit the page: https://cloud.google.com/appengine/docs/php/

 **- Prerequisites**
This app requires the use of the following components:

 A. Download and install Android Studio. You may use any other IDE other than Android Studio such as Eclipse, but you will be on your own if problems spring up.
B. First you need to install Java on your local machine. You can download JDK 8 from -
http://www.oracle.com/technetwork/java/javase/downloads/index.html. For
windows users, after installing the JDK, you need to add environment variables for JDK.

• Properties -> Advanced -> Environment Variables -> System variables -> New Variable Name: JAVA_HOME, Variable Value: <Full path to the JDK>

• Typically, this full path looks something like C:\Program Files\Java\jdk1.8.0. Then modify the PATH variable as follows on Microsoft Windows: C:\WINDOWS\system32;C:\WINDOWS;C:\Program Files\Java\jdk1.8.0\bin
This path may vary depending on your installation

• Reboot your computer and type “java –version” in the terminal to see
whether your JDK has been installed correctly.

Set up the Android Studio environment so that you can run any sample android app on your phone/tablet/virtual device from it. Then you can start with this homework app. You will need to enable “Developer Options” and “USB debugging” if you are using an actual device. There are endless resources a simple search away on how to setup your Android Studio.
You also need to create a Facebook Developer application 
Follow the following steps to get started:
a. Download SDK: Download the latest Facebook Android SDK Link:
https://developers.facebook.com/docs/android
b. Instructions to import in Android Studio:
https://developers.facebook.com/docs/android/getting-started
c. Create a new app on Facebook developer:
https://developers.facebook.com/apps/
d. Specify App Info related to the HW9 android application you are developing.
e. Key Hashes: Specify Android key hash for the development environment using the commands mentioned.
f. Track App Installs and App Opens: Not required.
g. Next Steps: Utilize Login (optional) and Share tutorials to achieve the
functionality required for the exercise. Note: In your Facebook application
settings, you should go to the “Status & Review” section and choose “Yes” for the question “Do you want to make this app and all its live features available to the general public?”

 **- High Level Design**
This is a Android Mobile application, which has the following functionality:
An Auto-complete edit box is provided to enter the stock name or symbol. The user can then select a stock from the suggestions. This feature is developed using the Android's ‘AutoCompleteTextView’ component. 
Once the user has provided data and selected a result from the autocomplete list he will click on ‘Get Quote’, when validation is done to check that the entered data is valid.
Once the validation is successful, we get the stock details using the PHP script hosted on Google App Engine, which will return the result in JSON format. The app displays the stock details in a ListView component in the ‘Current’ tab. Furthermore, my PHP script will be responsible for rendering the HighCharts in the ‘Historical’ tab and also rending the news articles in the ‘News’ tab.
 
**- Implementation**<br>
**5.1 Search Form**
The interface consists of the following:
• An ‘AutoCompleteTextView’ component allowing the user to enter the company name or symbol.
• Two buttons for interaction in the Search Form.
• A button ‘CLEAR’ to clear the ‘AutoCompleteTextView’ component.
• A button ‘GET QUOITE’ to get the quote, after validation.
• The Favorite ListView showing the list of favorite stocks.
• The Favorite List starts with an empty favorite list.

The form has two buttons:
a) GET QUOTE: Validations are first performed, when the button is clicked. If the validations are successful, then the stock details will be fetched from the server (either hosted on Google App Engine or Amazon Web Services). However, if the validations are unsuccessful, appropriate messages are displayed and no further requests will be made to the server.
b) CLEAR: This button clears the ‘AutoCompleteTextView’ and also clear any validations error, if present.

**5.1.1 AutoComplete**
The user can enter the stock name or symbol in the text view to get the stock information from our PHP script. Based on the user input, the AutoComplete will display the all the matching companies and symbols (see Figure 2) by making a HTTP request. The requests will made only when the user enters a minimum of 3 characters to avoid unnecessary network calls. This needs to be implemented using AutoCompleteTextView.

**5.1.2 Validations**
The following validations are implemented:
• Empty Entry: If the user does not enter anything in the AutoComplete
component, an appropriate message is displayed to indicate the same.
• Invalid Selection: If the user enters an invalid entry which does not correspond to any valid stock symbol, an appropriate message is displayed to indicate the same.
Once if the user input is successfully validated, the API calls should be made, otherwise appropriate messages should be displayed.

**5.1.3 Get Quote Execution**
Once the validation is successful, you should execute an HTTP request transaction to the PHP script which is located in the Google App Engine.
The PHP script on Google App Engine is used to retrieve data from Markit on Demand. You should pass the company symbol as a parameter of the transaction when calling the PHP script.
For example, if your Google App Engine service is located at
http://example.appspot.com
and the user enters ‘AAPL’ as the company symbol, then a query of the following type needs to be generated:
http://example.appspot.com/?symbol=AAPL
The PHP script running on the Google App Engine will extract the stock details of the company symbol, perform an API request to Markit on Demand, and returns the data in JSON data.
After obtaining the query results from the callback of the AJAX request, it displays the results in a drop-down suggestion list.

**5.2 Favorite List**
The Favorite stocks will be displayed in a list as per Figure 4. The data for the favorite list should be loaded from ‘SharedPreferences’. For more about shared preferences please refer to the appendix.
Every entry in the favorite list should contains the following:
<table>
<tr><td>Field</td><td> Description</td></tr>
<tr><td>Symbol </td><td>Displays the symbol of the company</td></tr>
<tr><td>Company Name</td><td> Displays the name of the company</td></tr>
<tr><td>Stock Price</td><td> Displays the current stock price of the company</td></tr>
<tr><td>Change Percentage</td><td> Displays the percentage change of the price of the company. It is rounded to 2 decimal places and the background indicates an increase or decrease in the change. For example, the background should be green if the change is positive and red if it is negative. E.g. +1.50% in green background.</td></tr>
<tr><td>Market Cap</td><td> Displays the market cap of the current stock. Possible suffixes are {Billions, Millions, None}. </td></tr>
</table>

Additionally, there are few important features:
• Automatic Refresh – A switch: when it is on it refreshes the price and change percentage column every 10 seconds.
• Refresh button –refreshes only the price and change column fields and not the rest of the table.
• Stock Details – The stock details activity loads when the user clicks on any entry of the listView.

**5.3 Stock Details**
The Stock Details section  is implemented using the ViewPager and TabLayout to render the tabs – ‘Current’, ‘Historical’ and ‘News’.

The stock detail section has 3 tabs:
• Current Stock
• Historical Charts
• News Feeds

The back button in the header navigates back to the Search Form.
The Stock Details will be starting with the ‘Current’ tab as loaded by default. Furthermore, the stock details have a list showing all the stock values. The list of the stock details is implemented using a ListView. The following stock will be displayed:

<table>
<tr><td>Field</td><td> Description</td></tr>
<tr><td>Company Name </td><td>Displays the name of the company</td></tr>
<tr><td>Symbol </td><td>Displays the symbol of the company</td></tr>
<tr><td>Stock Price </td><td>Displays the current stock price of the company</td></tr>
<tr><td>Change (Change
Percentage) </td><td>Displays the change and percentage change of the price of the
company. It is rounded to 2 decimal places, followed by
an indicator(image) which indicates whether the change is
positive or negative.</td></tr>
<tr><td>TimeStamp</td><td> Displays the timestamp of the last updated price.</td></tr>
<tr><td>Market Cap</td><td> Displays the market cap of the current stock. Possible suffixes
are {Billions, Millions, None}. </td></tr>
<tr><td>Volume</td><td> Displays the volume of the current stock</td></tr>
<tr><td>Change YTD (Change
YTD Percentage)</td><td>Displays the Change of the stock from the beginning of the current year till date. It is rounded to 2 decimal places,
followed by an indicator(image) which indicates whether the
change is positive or negative. </td></tr>
<tr><td>High </td><td>Displays the highest value of the stock’s price for the current date.</td></tr>
<tr><td>Low</td><td> Displays the lowest value of the stock’s price for the current date.</td></tr>
<tr><td>Open </td><td>Displays the opening value of the stock’s price for the current date.</td></tr>
</table>
This is implemented using an ImageView. The image of the current daily chart of the stock of the company is retrieved via Yahoo charts API

**5.4 Historical Charts**
This tab represents the historical charts showing the value of the stock in the past. This is implemented using a WebView using HighCharts API. It has the following details:
• It has the following zoom levels: 1 week, 1 month, 3 months, 6 months, 1 year, YTD and All.
• It shows 1 week worth of stock data by default.
• The title of the chart is the company symbol stock value.
• The X Axis should be date time.
• The Y Axis should be Stock Value

**5.5 News Feed**
This tab represents 4 news articles related to the current stock. The news articles need to implemented in a ListView. I have used the  Bing Search API for this. Each of the entry in the list is display the following details:

<table>
<tr><td>Field</td><td> Value</td></tr>
<tr><td>Article Title </td><td>Represents the title of the news article</td></tr>
<tr><td>Article Content </td><td>Represents the content of the news article</td></tr>
<tr><td>Publisher</td><td> Represents the publisher of the news article</td></tr>
<tr><td>Date</td><td> Represents the date of the news article</td></tr>
</table>
