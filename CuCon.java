package Currency_Converter;

import java.util.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

import org.json.*;

public class CuCon {
    static Boolean latest;
    public static void main(String[] args) throws URISyntaxException, IOException {
        HashMap<Integer,String> currencyCodes=new HashMap<>();

        // Add Currency Codes
        currencyCodes.put(1,"USD");
        currencyCodes.put(2,"EUR");
        currencyCodes.put(3,"GBP");
        currencyCodes.put(4,"JPY");
        currencyCodes.put(5,"AUD");
        currencyCodes.put(6,"CAD");
        currencyCodes.put(7,"NZD");
        currencyCodes.put(8,"CHF");
        currencyCodes.put(9,"SEK");
        currencyCodes.put(10,"NOK");
        currencyCodes.put(11,"DKK");
        currencyCodes.put(12,"PLN");
        currencyCodes.put(13,"BRL");
        currencyCodes.put(14,"RUB");
        currencyCodes.put(15,"INR");
        currencyCodes.put(16,"HUF");

        int from, to;
        String fromCode, toCode;
        double amount;
        Boolean running = true;

        Scanner sc=new Scanner(System.in);

        System.out.println("Welcome to the Currency Converter!");
        
        do {
            System.out.println("Currency Converting FROM?");
            System.out.println("1:USD (US Dollar) \t 2:EUR (Euro) \t 3:GBP (British Pound) \t 4:JPY (Japanese Yen)\n5:AUD (Australian Dollar) \t 6:CAD (Canadian Dollar) \t 7:NZD (New Zealand Dollar) \t 8:CHF (Swiss Franc)\n9:SEK (Swedish Krona) \t 10:NOK (Norwegian Krone) \t 11:DKK (Danish Krone) \t 12:PLN (Polish Zloty)\n13:BRL (Brazilian Real) \t 14:RUB (Russian Ruble) \t 15:INR (Indian Rupee) \t 16:HUF (Hungarian Forint)");
            from=sc.nextInt();
            while(from<1 || from>16) {
                System.out.println("Invalid Input! Please enter a number between 1 and 16: ");
                from=sc.nextInt();
            }
            fromCode = currencyCodes.get(from);     // HashMap map a Key to a Value, Here user will give the Key as input to select currency code

            System.out.println("Currency Converting TO?");
            System.out.println("1:USD (US Dollar) \t 2:EUR (Euro) \t 3:GBP (British Pound) \t 4:JPY (Japanese Yen)\n5:AUD (Australian Dollar) \t 6:CAD (Canadian Dollar) \t 7:NZD (New Zealand Dollar) \t 8:CHF (Swiss Franc)\n9:SEK (Swedish Krona) \t 10:NOK (Norwegian Krone) \t 11:DKK (Danish Krone) \t 12:PLN (Polish Zloty)\n13:BRL (Brazilian Real) \t 14:RUB (Russian Ruble) \t 15:INR (Indian Rupee) \t 16:HUF (Hungarian Forint)");
            to=sc.nextInt();
            while(to<1 || to>16) {
                System.out.println("Invalid Input! Please enter a number between 1 and 16: ");
                to=sc.nextInt();
            }
            toCode = currencyCodes.get(to);        // HashMap map a Key to a Value, Here user will give the Key as input to select currency code

            System.out.println("Amount wish to Convert?");
            amount = sc.nextDouble();

            System.out.println("Do You want to Convert as per the Latest Exchange Rates? ");
            System.out.println("1: Yes \t Any Integer: No");
            latest = (sc.nextInt()==1)?true:false;

            sendHttpGETRequest(fromCode, toCode, amount);
            System.out.println("\n");
            System.out.println("Do you want to convert another pair of currencies? ");
            System.out.println("1: Yes \t Any Integer: No");
            if(sc.nextInt()!=1) {
                running = false;
            } else {
                System.out.println("\n\n");
            }
        } while(running);
        sc.close();
    }
    
    // Now we gonna use API to fetch real-time currency exchange rate
    // Firstly, you need an access key for this API (https://www.exchangerate-api.com/)
    // Then replace 'your_access_key' with your own access key in below URL and run it on browser
    // https://api.exchangerate-api.com/v4/latest/from_currency/to_currency?accessKey
    // It will give you a JSON response which contains all currencies rates
    // We are going to parse that JSON and get our required conversion result
    
    private static void sendHttpGETRequest(String fromCode,String toCode,double amount) throws URISyntaxException, IOException {

        DecimalFormat f=new DecimalFormat("00.00");
        String get_url="";
        int yyyy, mm, dd;
        String date="";
        if(latest) {
            get_url= "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_q4VN5qKjPKALOqXKwJu279FSyHwbyLZnTf3hb64q&base_currency="+fromCode+"&currencies="+toCode;
        }
        else {
            Scanner sn=new Scanner(System.in);
            System.out.println("Please Enter Specific Date from the Past within the Year 1999!");
            System.out.print("Enter Year in the format(YYYY) : ");
            yyyy=sn.nextInt();
            System.out.print("Enter Month in the Format(MM): ");
            mm=sn.nextInt();
            while(mm<1 || mm>12) {
                System.err.println("Invalid month inputted.");
                System.out.println("Enter Month in the Format(MM) within 1-12: ");
                mm=sn.nextInt();
            }
            System.out.print("Enter Day in the Format(DD): ");
            dd=sn.nextInt();
            while(dd<1 || dd>31) {
                System.err.println("Invalid day inputted.");
                System.out.println("Enter Day in the Format(DD) within 1-31: ");
                dd=sn.nextInt();
            }

            if(mm<10 && dd<10) {
                date=yyyy+"-"+"0"+mm+"-"+"0"+dd;
            }
            else if(mm<10) {
                date=yyyy+"-"+"0"+mm+"-"+dd;
            }
            else if(dd<10) {
                date=yyyy+"-"+mm+"-"+"0"+dd;
            }
            else {
                date=yyyy+"-"+mm+"-"+dd;
            }

            get_url= "https://api.freecurrencyapi.com/v1/historical?apikey=fca_live_q4VN5qKjPKALOqXKwJu279FSyHwbyLZnTf3hb64q&base_currency="+fromCode+"&currencies="+toCode+"&date="+date;
        }
        URL url=new URI(get_url).toURL();  // As constructor URL(string) is deprecated since version 20 java so we're using constructor of URI using the reference of URL
        HttpURLConnection httpconn=(HttpURLConnection) url.openConnection();  // Here we're setting up proper http connection
        httpconn.setRequestMethod("GET");  // Since we're making a GETRequest, we need to setup the request method is being get

        int responsecode=httpconn.getResponseCode();    // Here we're storing the response we get from URL
        if(responsecode== HttpURLConnection.HTTP_OK) {     // If response code is OK then proceed
            BufferedReader br= new BufferedReader(new InputStreamReader(httpconn.getInputStream()));   // Reading data from input stream
            String inputLine;
            StringBuffer response=new StringBuffer();

            while((inputLine=br.readLine())!=null) {    // If there is response from URL it will keep reading that
                response.append(inputLine);
            }
            br.close();

            JSONObject obj=new JSONObject(response.toString());   // Through API the data we're getting in JSON format, now we're reading this JSON format data
            Double exchangeRate;
            if(latest) {
                exchangeRate = obj.getJSONObject("data").getDouble(toCode);
            }
            else {
                exchangeRate = ((JSONObject) obj.getJSONObject("data").get(date)).getDouble(toCode);
            }
            System.out.println("\n"+obj.getJSONObject("data"));
            System.out.println();
            System.out.println(f.format(amount) + " " + fromCode + " = " + f.format(amount*exchangeRate) + " " + toCode);
        }
        else {
            System.out.println("GET request failed!");
        }
    }
}