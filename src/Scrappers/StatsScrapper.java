/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scrappers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StatsScrapper {

	static ArrayList<String> timeToWin;
	public String[] stats = new String [13];
	
	public static void main(String[] args) 
	{
	// String y=	scrapeFighterProfile("http://www.ufcstats.com/fighter-details/3d6749c4267da18f","20, 2015","Jessica Penne");
	//	String x=scrapeFighterProfile("http://www.ufcstats.com/fighter-details/33a331684283900f","27, 2020","Dan Hooker");
	//	System.out.println("dustins stats: "+ x);
	//	String y = eventScraper("http://www.ufcstats.com/event-details/0b5b6876c2a4723f");
		System.out.println(eventsScraper(1,"http://www.ufcstats.com/statistics/events/completed?page=13"));
	//	System.out.println(y);
	}	//11 10 9 8 7 6 5 4 3 2 1
	public static String scrapeFighterProfile(String url, String date,String opponent)
	{
		timeToWin = new ArrayList<String>();
		Document doc = null;
		try
		{
		   doc = Jsoup.connect(url).get(); // URL shortened!
		   //System.out.println(doc.outerHtml());
		   int KOW=0;
		   int KOL=0;
		   int UDW=0;//just decsion, not unanonymous
		   int UDL=0;
		   int SUBW=0;
		   int SUBL=0;
		   int numberOfUFCFights=0;
		   int perfFOTN =0;
		//   String nPasses="";
		   Float wRounds;
		   Float wSeconds;
		   Float wTotalTime=0.00000000F;
		   String wTime="";
		   int wCounter=0;
		   Integer wMins;
		   
		   Float rounds;
		   Float seconds;
		   Float totalTime=0.0000000F;
		   String time="";
		   int counter=0;
		   Integer mins;
		   Double atotalTime=0.0d;
		   
		   Float heightCM=0.0f;
		   String reach="";
		   int stance=0;
		   String DOB="";
		   String StkLd="";
		   String StkAcc="";//%
		   String averageNumPasses="";
		   String strkDef="";
		   String TDavg="";
		   String TDacc="";
		   String TDdef="";
		   String SubAvg="";
		   String stkAbs="";
		   double strikes = 0;
		   double takeDowns = 0.0d;
		   double subAttempts = 0.0d;
		   double passes = 0.0d;
		   int recentLosses=0;
		   int recentWins=0;
		   Float recentWinRatio=0.0f;//from previous 3 matches		   
		  
		  
		   int j=0;//needed to get height //because i assumed you can only iterate throught ELEMENTS with a for each loop;
		   for(Element li :doc.select("ul.b-list__box-list li"))//gets fighter stats
		   {
			   String currentStat = li.text();
			   if(j==0)//gets height
			   {  
				   currentStat = li.text().replaceAll("([a-zA-Z|\\-|\\s|\"|'|:])+","");		   
				   if(currentStat.length()>2)
				   {
					   heightCM =(Float.parseFloat(currentStat.substring(0,1))*12*2.54f)+(Float.parseFloat(currentStat.substring(1,3))*2.54f);
		//			   System.out.println("height is "+heightCM);
				   }
				   else
				   {					  
					   heightCM =(Float.parseFloat(currentStat.substring(0,1))*12*2.54f)+(Float.parseFloat(currentStat.substring(1,2))*2.54f);						  
				   }
				   j++;
				   continue;
			   }
			   else if(j==1)
			   {
				   j++;
				   continue;
			   }
			   else if(j==2)
			   {
				   reach = currentStat.substring(7,9);		
				   j++;
				   continue;
			   }
			   else if(j==3)
			   {				
				   stance= getStance(currentStat);	
				   j++;
				   continue;
			   }
			   else if(j==4)
			   {
				   DOB=currentStat.substring(13,17);	
				   j++;
				   continue;
			   }
			   else if(j==5)
			   {
				//   StkLd=currentStat.substring(6,currentStat.length());	
				   j++;
				   continue;
			   }
			   else if(j==6)
			   {
				   StkAcc=currentStat.substring(11,currentStat.length()-1);	
				   j++;
				   continue;
			   }
			   else if(j==7)
			   {
				   stkAbs=currentStat.substring(6,currentStat.length());	
				   j++;
				   continue;
			   }
			   else if(j==8)
			   {
				   strkDef=currentStat.substring(10,currentStat.length()-1);	
				   j++;
				   continue;
			   }
			   else if(j==9)
			   {
				   j++;
				   continue;
			   }
			   else if(j==10)
			   {
				//   TDavg=currentStat.substring(9,currentStat.length());	
				   j++;
				   continue;
			   }
			   else if(j==11)
			   {
				   TDacc=currentStat.substring(9,currentStat.length()-1);	
				   j++;
				   continue;
			   }
			   else if(j==12)
			   {
				   TDdef=currentStat.substring(9,currentStat.length()-1);
				   j++;
				   continue;
			   }
			   else if(j==13)
			   {
			//	   SubAvg=currentStat.substring(11,currentStat.length());
				   j++;
				   continue;
			   }			   
			   
		   }	
		   
		   boolean startScraping = false;
		   
		   for(Element row : doc.select(
				   "table.b-fight-details__table.b-fight-details__table_style_margin-top.b-fight-details__table_type_event-details tr"))
		   {			  
			   
			   String namesColumn=row.select("td.l-page_align_left.b-fight-details__table-col:nth-of-type(2)").text();
			   String dateColumn=row.select("td.l-page_align_left.b-fight-details__table-col:nth-of-type(7)").text();
			  
			   if(startScraping)
			   {
				   wSeconds=0.0f;
				   seconds=0.0f;
				   numberOfUFCFights++;
				   //method of victory
				   String method=row.select("td.b-fight-details__table-col:nth-of-type(8)").text();
				   //bonuses
				   String bonuses = row.select("td.b-fight-details__table-col:nth-of-type(8)").outerHtml();
				   
				   //in fight stats
				   strikes+=Integer.parseInt(row.select(" td.b-fight-details__table-col:nth-of-type(3)").select("p.b-fight-details__table-text:nth-of-type(1)").text());
				   takeDowns+=Integer.parseInt(row.select(" td.b-fight-details__table-col:nth-of-type(4)").select("p.b-fight-details__table-text:nth-of-type(1)").text());
				   subAttempts+=Integer.parseInt(row.select(" td.b-fight-details__table-col:nth-of-type(5)").select("p.b-fight-details__table-text:nth-of-type(1)").text());
				   passes+=Integer.parseInt(row.select(" td.b-fight-details__table-col:nth-of-type(6)").select("p.b-fight-details__table-text:nth-of-type(1)").text());
				   
				//   System.out.println("stikes:   "+strikes);
				//   System.out.println("TD:   "+takeDowns);
				//   System.out.println("SUB ATT:   "+subAttempts);
				 //  System.out.println("PASSEES:   "+passes);
				   
				   
				   if(bonuses.contains("http://1e49bc5171d173577ecd-1323f4090557a33db01577564f60846c.r80.cf1.rackcdn.com/perf.png")|bonuses.contains("http://1e49bc5171d173577ecd-1323f4090557a33db01577564f60846c.r80.cf1.rackcdn.com/fight.png"))
				   {
					   perfFOTN++;
				   }
				   if(row.select("td.b-fight-details__table-col:nth-of-type(1)").text().equalsIgnoreCase("WIN"))//if fighter won
				   {
					   UDW+=method(method)[0];
					   KOW+=method(method)[1];
					   SUBW+=method(method)[2];
					   
					  					   
							   
					 //time calculations
					   String Wsecs=row.select("td.b-fight-details__table-col:nth-of-type(10)").text();
					   wMins=Integer.parseInt(row.select("td.b-fight-details__table-col:nth-of-type(10)").text().substring(0,1));
					   wRounds= 5.00f * (Float.parseFloat(row.select("td.b-fight-details__table-col:nth-of-type(9)").text())-1);
					   wSeconds+= Float.parseFloat(wMins.toString()+"."+row.select("td.b-fight-details__table-col:nth-of-type(10)").text().substring(Wsecs.length()-2,Wsecs.length()));
					   wTotalTime+=(wRounds+wSeconds);		
					   wCounter++;		
					   if(numberOfUFCFights<5)
					   {
						   recentWins++;
					   }
					   
				   }
				   
				   else if(row.select("td.b-fight-details__table-col:nth-of-type(1)").text().equalsIgnoreCase("Loss"))
				   {
					   UDL+=method(method)[0];
					   KOL+=method(method)[1];
					   SUBL+=method(method)[2];					   
					   String secs=row.select("td.b-fight-details__table-col:nth-of-type(10)").text();
					   mins=Integer.parseInt(row.select("td.b-fight-details__table-col:nth-of-type(10)").text().substring(0,1));
					   rounds= 5.00f * (Float.parseFloat(row.select("td.b-fight-details__table-col:nth-of-type(9)").text())-1);
					   seconds+= Float.parseFloat(mins.toString()+"."+row.select("td.b-fight-details__table-col:nth-of-type(10)").text().substring(secs.length()-2,secs.length()));					   
					   totalTime=rounds+seconds;
					   atotalTime+=(double)totalTime;		
					   counter++;
					   if(numberOfUFCFights<5)
					   {
						   recentLosses++;
					   }
					   
				   }			   
				   
			   }
			   if(dateColumn.contains(date) && namesColumn.contains(opponent))
			   {				
				//   System.out.println(row.select(" td.l-page_align_left.b-fight-details__table-col:nth-of-type(7)").text());
				   startScraping=true;				
			   }			 
		   }
		   double totalFightTime =  (double) (wTotalTime+atotalTime);
		   
		   //code below puts the avg time in good string format
		   wTotalTime= wTotalTime/wCounter;
		   wTime = wTotalTime.toString();
		   wTime = timeFormat(wTime);
		   
		   Double SLpM = (double)((double)strikes/(double)totalFightTime);
		   Double avgTD =  ((double)takeDowns/(double)numberOfUFCFights);
		   Double avgSubAttempt = (double) (subAttempts/(double)numberOfUFCFights);
		   Double avgPass =  (passes/(double)numberOfUFCFights);
		   StkLd = (SLpM.toString()+"00").substring(0,4);
		   TDavg = (avgTD.toString()+"00").substring(0,3);
		   SubAvg = (avgSubAttempt.toString()+"00").substring(0,3);
		 //  System.out.println(avgPass);
		   averageNumPasses = (avgPass.toString()+"00").substring(0,3);
		   
		   atotalTime= atotalTime/counter;
		   time= atotalTime.toString();
		   time= timeFormat(time);
		   if(takeDowns<1.0d)
		   {
			   TDavg="0.0";
		   }
		   if(Double.isNaN(avgPass))
		   {
			   averageNumPasses="0.0";
		   }
		   if(subAttempts<1.00d)
		   {
			   SubAvg="0.0";
		   }
		   if(counter==0)//if losses was 0
		   {
			   time="?";
		   }
		   if(wCounter==0)
		   {
			   wTime="?";
		   }
		//   System.out.println("avpass "+averageNumPasses);
		   if(wCounter==0 && counter==0)
		   {
			   StkLd="?";
			   averageNumPasses="?";
			   strkDef="?";
			   TDavg="?";
			   SubAvg="?";			  
		   }
	//	   System.out.println(strikes);
		   return heightCM.toString()+","+ reach+","+ stance +","+ DOB +","+ StkLd + "," + StkAcc+ ","+","+stkAbs+"," +  averageNumPasses+ "," + strkDef+ "," + TDavg+ "," + TDacc+ "," + TDdef+ "," + SubAvg
				   + "," + wTime+ "," + time+ "," + UDW+ "," + UDL+ "," + KOW+ "," + KOL+ "," + SUBW+ "," + SUBL+ "," + numberOfUFCFights + "," + recentWins+","+recentLosses+","+ perfFOTN+ ",";
		} 
		catch (IOException ioe)
		{
		   ioe.printStackTrace();
		}
		return "";
	}
	
	public static String eventScraper(String url)
	{	
		String Master ="";
		int numOfFightsToScrapeFromEvent=12; //IMPORTANT!!!!!!!!!!
		ArrayList<String> fighters = new ArrayList<String>();
		ArrayList<String> fighterUrls= new ArrayList<String>();
		String isWin="1";
		String isLoss="0";
		Document eventPage = null;
	//	Document domDoc = Jsoup.parse(url);
		try
		{
		   eventPage = Jsoup.connect(url).get(); // URL shortened!
		   String urldate=eventPage.select("li.b-list__box-list-item:nth-of-type(1)").text();
		   String date=eventPage.select("li.b-list__box-list-item:nth-of-type(1)").text().substring(urldate.length()-8,urldate.length());
		//   System.out.println(date);
		   Elements names = eventPage.getElementsByClass("b-link b-link_style_black");
		   for(Element fighter: names)
		   {
			   String x = fighter.attr("href");
			   fighterUrls.add(x);
			   String y= fighter.text();
			 //  y = y.replaceAll("'"," ");
			   fighters.add(y);
			   
			 //  System.out.println(x);
		   }
		   
		   //for(int i =0 ;i<fighters.add(e))
		   
		   
		   int inverseCounter = 0;
		   int inverseCounter2 = 0;
		   String EventData="% UFC EVENT : "+urldate+"\n";
		   String fightInstance = "";
		   String fightName;
		   for(int i=0; i<numOfFightsToScrapeFromEvent;i+=4)
		   { 
			  // inverseCounter++;
			//   if(inverseCounter%2==0)
			 //  {
				   fightInstance="'"+fighters.get(i).replaceAll("'"," ")+"'"+","+scrapeFighterProfile(fighterUrls.get(i),date,fighters.get(i+1));
				   fightInstance+="'"+fighters.get(i+1).replaceAll("'"," ")+"'"+","+scrapeFighterProfile(fighterUrls.get(i+1),date,fighters.get(i));
				   fightInstance +=isWin;
				   fightName = "% "+ fighters.get(i) +" vs "+ fighters.get(i+1)+" \n";
				   fightInstance = fightName + fightInstance;
				   EventData+=fightInstance+"\n";
			//   }   
			    
		   }
		   for(int i=3; i<numOfFightsToScrapeFromEvent;i+=4)
		   {	
			  //  inverseCounter2++;
			//	if(inverseCounter%2!=0)
			//	{
					fightInstance="'"+fighters.get(i).replaceAll("'"," ")+"'"+","+scrapeFighterProfile(fighterUrls.get(i),date,fighters.get(i-1));
					fightInstance+="'"+fighters.get(i-1).replaceAll("'"," ") +"'"+","+scrapeFighterProfile(fighterUrls.get(i-1),date,fighters.get(i));
					fightInstance +=isLoss;
					fightName = "% "+ fighters.get(i) +" vs "+ fighters.get(i-1)+" \n";
					fightInstance = fightName + fightInstance;
				   EventData+=fightInstance +"\n";
			//	}
		   }
		//   System.out.println(EventData);
		   return EventData;
		   //scrapeFighterProfile(url,date,x);
		}
		catch (IOException ioe)
		{
		   ioe.printStackTrace();
		}	
		return " ";
	}	
	
	public static String eventsScraper(int nPages,String manUrl)
	{
		
		//tble b-statistics__table-events
		String MasterData ="";
		Document eventsPage = null;		
	
			try
			{	
				FileWriter out = new FileWriter("UFCDATAnewAttributes.bin",true);
				if(nPages!=1)
				{
					 for (int pages=nPages ; pages >1; pages--)
					   {	
						   Queue<String> events = new LinkedList<String>();
						   String url ="http://www.ufcstats.com/statistics/events/completed?page="+pages;
						   eventsPage = Jsoup.connect(url).get(); // URL shortened!	
						   Elements names = eventsPage.getElementsByClass("b-link b-link_style_black");
						   for(Element event: names)
						   {							   
							   String x = event.attr("href");
							   events.add(x);							   							   
						   }
						   for(int k=0;k<events.size();k++)
						   {
							   String x = events.poll();
							   String sss= eventScraper(x);
							   
						//	   out.write(sss);							   
							   MasterData +=sss;
						//	   out.flush();
						   }
						   MasterData+="\n "+ "%New Page :" + pages;
					   }   		
				}
				else
				{
					   eventsPage = Jsoup.connect(manUrl).get();  
					   Elements names = eventsPage.getElementsByClass("b-link b-link_style_black");
					/*   for(Element event: names)
					   {
						   String x = event.attr("href");
						   String sss= eventScraper(x);
						   out.write(sss);
						   MasterData += sss+ "\n";						  
					   }
					   MasterData += "\n"+ "%newPage";
					   */
					   Stack<String> events = new Stack<String>();					  
					   for(Element event: names)
					   {							   
						   String x = event.attr("href");
						//   System.out.println(x);
						   events.add(x);							   							   
					   }
					   int numEvents=events.size();
					   for(int k=0;k<numEvents;k++)
					   {
						   String x = events.pop();
						   String sss= eventScraper(x);
					//	   System.out.println(sss);
						  // System.out.println(k);
						  // System.out.println("size: "+events.size());
						   out.write(sss);	
						   out.flush();						   
						  MasterData +=sss;
					   }
					   MasterData+="\n "+ "%New Page";
				}
				//out.write(MasterData);
				
				out.close();
			  		    
			}
			catch (IOException ioe)
			{
			   ioe.printStackTrace();
			}
			
		return MasterData;
		//.b-link_style_black.b-link
	}
	public static String decimals(String s)
	{
		return s.substring(0,4);
	}
	
	//to get the method of victory/loss
	
	public static int[] method(String s)
	{
		int [] method = new int[]{0,0,0};
		if(s.contains("DEC"))
		{
			method[0]=1;
		}
		else if(s.contains("KO"))
		{
			method[1]=1;
		}
		else if(s.contains("SUB"))
		{
			method[2]=1;
		}
		return method;
	}
	
	public static int getStance(String s)
	{
		if(s.contains("Southpaw"))
		{
			return 2;
		}
		else if(s.contains("Othordox"))
		{
			return 1;
		}
		else if(s.contains("Switch"))
		{
			return 0;
		}
		else return 1;	
	}
	
	public static String timeFormat(String s)
	{
		String x = s.substring(0,s.length())+"0000";
		x=x.substring(0,5);
		return x;
	}
	
}