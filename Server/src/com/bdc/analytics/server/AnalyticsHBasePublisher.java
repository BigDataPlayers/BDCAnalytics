package com.bdc.analytics.server;

import com.bdc.container.webservice.ResultProcessor;
import rcaller.RCaller;
import rcaller.RCode;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/20/12
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnalyticsHBasePublisher implements ResultProcessor {

    String rootDir = "./htmls/";
    String csvDir = "./csv/";
    String tempDir = "/opt/snaplogic/data/";

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public File getFile(String aid) {
        System.out.println("In process file" + aid);
        final File file = getFileFromR(aid);
        System.out.println(file.getName());
        return file;
    }

    private File getFileFromR(String chartType)

    {
        System.out.println("Chart type requested is " + chartType);
      //  return new File(rootDir + chartType + ".html");

            try {

                if (chartType.equals("bubbleChart"))
                {
                   // System.out.println("In bubble chart");
                    String[] columnNames= {"First_1","Last_1","State_1"};
                    ExtractHBaseData processor = new ExtractHBaseData(columnNames);
                    processor.generateFile();

                   // System.out.println("After generate file");

                    getBubbleChart();
//                    ChartBuilder chart = (ChartBuilder) Server.getInstance().getContext().getBean("bubbleChart");
//                    chart.createChart();
                } else if  (chartType.equals("columnChart"))
                {
                    getColumnChart();
                   // return new File("/columnChart.html") ;
                }   else if  (chartType.equals("barChart"))
                {
                    getBarChart();
                  //  return new File("/home/gasawa/barChart.html") ;
                }    else if  (chartType.equals("areaChart"))
                {
                    getAreaChart();
                   // return new File("/home/gasawa/areaChart.html") ;
                }else if  (chartType.equals("pieChart"))
                {
                    getPieChart();
                  //  return new File("/home/gasawa/pieChart.html") ;
                } else if  (chartType.equals("dashBoard"))
                {
                    getDashBoard();
                   // return new File("/home/gasawa/DashBoard.html") ;
                }    else if  (chartType.equals("mapChart"))
                {
                    getMapChart();
                   // return new File("/home/gasawa/mapChart.html") ;
                }     else if  (chartType.equals("geoChart"))
                {
                    return getGeoChart();
                    // return new File("/home/gasawa/mapChart.html") ;
                }   else if  (chartType.equals("candleChart"))
                {
                    getCandleChart();
                   // return new File("/home/gasawa/candleChart.html") ;
                } else if  (chartType.equals("treeMap"))
                {
//                    The file is hardcoded for now
                }   else if  (chartType.equals("motionChart"))
                {
                    getMotionChart();
                    // return new File("/home/gasawa/candleChart.html") ;
                }   else if  (chartType.startsWith("cloudChart"))
                {
                    String [] input = chartType.split("%20");
                    return getCloudChart(input[1], input[2]);
                   // return new File("/home/ubuntu/Strata2013Cloud.png") ;
                }



                return new File(rootDir + chartType + ".html");
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }


    }

    private File getCloudChart(String k1 , String k2)


    {

        File file = null;

        try {


            RCaller caller = new RCaller();


            caller.setRscriptExecutable("/usr/bin/Rscript");


            RCode code= new RCode();


            code.clear();

            file = code.startPlot();


            code.addRCode("library(twitteR)");


            code.addRCode("library(tm)");


            code.addRCode("library(wordcloud)");


            code.addRCode("library(RColorBrewer)");


//            code.addRCode( "mach_tweets = searchTwitter(\"strata 2013\", n=500, lang=\"en\", encoding=\"utf-8\" ) ");
          code.addRCode( "mach_tweets = searchTwitter(\"" + k1 + " " + k2 + "\", n=500, lang=\"en\", encoding=\"utf-8\" ) ");


            code.addRCode("mach_text = sapply(mach_tweets, function(x) x$getText() )");


            code.addRCode("mach_corpus = Corpus(VectorSource(mach_text), readerControl = list(language =\"english\"))");


            code.addRCode("abc = tm_map(mach_corpus, function(x) iconv(enc2utf8(x), sub = \"byte\"))");


    //        code.addRCode("tdm = TermDocumentMatrix(abc,control = list(removePunctuation = TRUE,stopwords = c(stopwords(\"english\"),\"strata\", \"2013\"),removeNumbers = TRUE,tolower=TRUE))");
             code.addRCode("tdm = TermDocumentMatrix(abc,control = list(removePunctuation = TRUE,stopwords = c(stopwords(\"english\"),\"" + k1 + "\", \"" + k2 + "\"),removeNumbers = TRUE,tolower=TRUE))");


            code.addRCode("m = as.matrix(tdm) ");


            code.addRCode("word_freqs = sort(rowSums(m), decreasing=TRUE)");


            code.addRCode("dm = data.frame(word=names(word_freqs), freq=word_freqs)");


            //  code.addRCode("png(\"/home/ubuntu/Strata2013Cloud.png\", width=12, height=8, units=\"in\", res=300)");


            code.addRCode("wordcloud(dm$word, dm$freq,random.order=FALSE,colors=brewer.pal(8, \"Dark2\"))");

            code.endPlot();


            //      code.addRCode("dev.off()");

            System.out.println("Plot will be saved to : " + file);


            caller.setRCode(code);


            caller.runOnly();


            code.clear();

            return file;


        } catch (Throwable e) {


            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.

            return file;


        }


    }



    public String getResult(String input) {
        return "";
//        if (input.startsWith("truncate"))
//        {
//            System.out.println(input);
//            return getTruncate(input);
//        }
//        if (input.startsWith("snap"))
//        {
//            System.out.println(input);
//            return getResultForSnap(input);
//        }
//        if (input.startsWith("ip"))
//        {
//            return getResultForIp(input)  ;
//        }
//        else  if (input.startsWith("aid"))
//        {
//            return getResultForAid(input)  ;
//        }
//        else
//        {
//            return getResultForPid(input)  ;
//
//        }
//
    }

    private void getMotionChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");


            code.addRCode("temp1 <- read.csv(\"" + tempDir+ "RChartData.csv\", header=TRUE,sep=\",\")");
            code.addRCode( "dateVar <- as.Date(temp1$CreatedAt, format=\"%Y-%m-%d\") ");

            code.addRCode("motionChart <- cbind(dateVar,temp1)");



            code.addRCode(" M =gvisMotionChart(data=motionChart, idvar=\"TweetID\", timevar=\"dateVar\", chartid=\"MotionChart\")");
            code.addRCode("plot(M)");

            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "motionChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }



    private void getBubbleChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");


            code.addRCode("bubbleChart <- read.csv(\"" + csvDir+ "bubbleChart.csv\", header=TRUE,sep=\",\")");
         //  code.addRCode("bubbleChart <- read.csv(\"/var/bdc/csv/bubbleChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("M =gvisBubbleChart(data=bubbleChart, idvar = \"ID\", xvar = \"Age\", yvar = \"Click.Rate\",sizevar=\"Click.Rate\",  colorvar = \"ID\",list(title=\"Correlation between Age,Click Rate and Clicks of some states\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:24}\",vAxis= \"{title: 'Clicks Rate',titleTextStyle: {color:'blue',fontSize: '20'}}\",hAxis= \"{title: 'Age',titleTextStyle: {color:'blue',fontSize: '20'}}\"),chartid=\"bubbleChart\")");
            code.addRCode("plot(M)");
            //code.addRCode("cat(unlist(M$html), file=\"/var/bdc/htmls/bubbleChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "bubbleChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private void getPieChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
            //code.addRCode("pieChart <- read.csv(\"/home/gasawa/pieChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("pieChart <- read.csv(\"" + csvDir + "pieChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("M =gvisPieChart(data=pieChart, labelvar = \"Age\", numvar = \"PercentClicks\",  options=list(title=\"Clicks by Age Group\",titleTextStyle=\"{fontName:'Courier',fontSize:24}\"),chartid=\"pieChart\")");
            code.addRCode("plot(M)");
         //   code.addRCode("cat(unlist(M$html), file=\"/home/gasawa/pieChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "pieChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }


    private void getColumnChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
         //   code.addRCode("columnChart <- read.csv(\"/home/gasawa/columnChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("columnChart <- read.csv(\"" + csvDir + "columnChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("columnChart$Mean=rowMeans(columnChart[,-1], na.rm = TRUE)");
            code.addRCode("M = gvisColumnChart(data=columnChart, yvar=c(\"Mean\",\"A\",\"J\",\"V\",\"X1\"),xvar=\"Year\",  options=list(seriesType=\"bars\",series='{0: {type:\"line\"}}',title=\"Sends by Affiliates\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:24}\",vAxis= \"{title: 'Sends(Millions)',viewWindowMode:'maximized',titleTextStyle: {color:'blue',fontSize: '20'}}\",hAxis= \"{title: 'Year',titleTextStyle: {color:'blue',fontSize: '20'}}\",curveType='function',width=800,height=500,legend = \"{position: 'right', textStyle: {color: 'blue', fontSize: 12}}\"), chartid=\"columnChart\")");
            code.addRCode("plot(M)");
           // code.addRCode("cat(unlist(M$html), file=\"/home/gasawa/columnChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "columnChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
            /*
            columnChart <- read.csv("/home/gasawa/columnChart.csv", header=TRUE,sep=",")
columnChart$Mean=rowMeans(columnChart[,-1], na.rm = TRUE)
 M = gvisComboChart(data=columnChart, yvar=c("Mean","American.Express","JCPenny","Victoria.s.Secret","X1800Flower"),xvar="Year",  options=list(seriesType="bars",series='{0: {type:"line"}}',title="Sends by Affiliates",titleTextStyle="{color:'black',fontName:'Courier',fontSize:24}",vAxis= "{title: 'Sends(Millions)',viewWindowMode:'maximized',titleTextStyle: {color:'blue',fontSize: '20'}}",hAxis= "{title: 'Year',titleTextStyle: {color:'blue',fontSize: '20'}}",curveType='function',width=800,height=800,legend = "{position: 'right', textStyle: {color: 'blue', fontSize: 12}}"), chartid="columnChart")
             */

        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private void getBarChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
           // code.addRCode("barChart <- read.csv(\"/home/gasawa/barChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("barChart <- read.csv(\"" + csvDir + "barChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode(" M = gvisBarChart(data=barChart, yvar=c(\"Save..10.on.3.Can.t.Miss.Valentine.s.Bestsellers\",\"SAVE.40..on.Valentine.s.Bestsellers..While.Supplies.Last.\",\"X.Valentine.s.Free.Shipping.No.Service.Charge...Hurry..Ends.Today.\"),xvar=\"Hour\",  options=list(title=\"Clicks - Campaigns/Hour\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:24}\",vAxis= \"{title: 'Hour',viewWindowMode:'maximized',titleTextStyle: {color:'blue',fontSize: '20'}}\",hAxis= \"{title: 'Clicks',titleTextStyle: {color:'blue',fontSize: '20'}}\",curveType='function',width=800,height=700,legend = \"{position: 'right', textStyle: {color: 'blue', fontSize: 12}}\"), chartid=\"barChart\") ");
            code.addRCode("plot(M)");
           // code.addRCode("cat(unlist(M$html), file=\"/home/gasawa/barChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "barChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private void getCandleChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
          //  code.addRCode("candleChart <- read.csv(\"/home/gasawa/candleChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("candleChart <- read.csv(\"" + csvDir + "candleChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode(" M = gvisCandlestickChart(candleChart, xvar = \"Campaign\", low = \"Low\", open = \"Open\",close = \"Close\", \"High\",options = list(title=\"Campaign Clicks - Variations\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:20}\",legend='none'), chartid=\"candleChart\") ");
            code.addRCode("plot(M)");
          // code.addRCode("cat(unlist(M$html), file=\"/home/gasawa/candleChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "candleChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private void getAreaChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
        //    code.addRCode("areaChart <- read.csv(\"/home/gasawa/areaChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("areaChart <- read.csv(\"" + csvDir + "areaChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("tableChart <- read.csv(\"" + csvDir + "tableChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("  M1 = gvisAreaChart(data=areaChart, yvar=c(\"Last.Chance..Earn.25.miles.per..1.on.Valentine.s.Day.Flowers...Gifts\", \"Save..10.on.3.Can.t.Miss.Valentine.s.Bestsellers\",\"SAVE.40..on.Valentine.s.Bestsellers..While.Supplies.Last.\",\"Thanks.for.letting.us.help.you.make.someone.smile\",\"X.Valentine.s.Free.Shipping.No.Service.Charge...Hurry..Ends.Today.\"),xvar=\"Day\",  options=list(title=\"Clicks - Campaigns/Day\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:24}\",vAxis= \"{title: '%Clicks',titleTextStyle: {color:'blue',fontSize: '20'}}\",hAxis= \"{title: 'Day',titleTextStyle: {color:'blue',fontSize: '20'}}\",legend = \"{position: 'right', textStyle: {color: 'blue', fontSize: 12}}\"), chartid=\"areaChart\")");
            code.addRCode("M2 <- gvisTable(tableChart, options=list(page='enable', height=300)) ");
            code.addRCode("M=gvisMerge(M1, M2, horizontal = TRUE, chartid=\"AreaChartTable\")");
            code.addRCode("plot(M)");
            //code.addRCode("cat(unlist(M$html), file=\"/home/gasawa/areaChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "areaChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();

        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private void getMapChart()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
           // code.addRCode("geoChart <- read.csv(\"/home/gasawa/clicks_hbase.txt\", header=TRUE,sep=\",\")");
            code.addRCode("geoChart <- read.csv(\"" + csvDir + "clicks_hbase.txt\", header=TRUE,sep=\",\")");
            code.addRCode("M <- gvisGeoMap(geoChart, \"name\", \"clicks\",options=list(region=\"US\", dataMode=\"regions\",width=600, height=400))");
            code.addRCode("plot(M)");
           // code.addRCode("cat(unlist(M$html), file=\"/home/gasawa/mapChart.html\")");
            code.addRCode("cat(unlist(M$html), file=\"" + rootDir + "mapChart.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private File getGeoChart()

    {
        File file = null;
        try {

            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");


            RCode code = new RCode();
            code.clear();
            file = code.startPlot();
            code.addRCode("library(maps)");
            code.addRCode("library(RColorBrewer)");
            code.addRCode("library(psych)");
            code.addRCode("library(car)");
            code.addRCode("library(classInt)");
            code.addRCode("library(spdep)");
            code.addRCode("world <- map(\"world\", plot = F)");
            code.addRCode("states <- map(\"state\", plot = F)");
            code.addRCode("library(maptools)");
            code.addRCode("world <- map2SpatialLines(world, proj4string = CRS(\"+proj=longlat\"))");
            code.addRCode("states <- map2SpatialLines(states, proj4string = CRS(\"+proj=longlat\"))");
            code.addRCode("map.states <- map(\"state\", plot = F, fill = T)");
            code.addRCode("list.names.states <- strsplit(map.states$names,\":\")");
            code.addRCode("map.IDs <- sapply(list.names.states, function(x) x[1])");
            code.addRCode("states <- map2SpatialPolygons(map.states, IDs = map.IDs,proj4string = CRS(\"+proj=longlat\"))");
            code.addRCode("sp.IDs <- sapply(slot(states, \"polygons\"), function(x) slot(x,\"ID\"))");

           // code.addRCode("click <- read.csv(\"/home/gasawa/clicks_hbase.txt\", stringsAsFactors = F,row.names = 1)");
            code.addRCode("click <- read.csv(\"" + csvDir + "clicks_hbase.txt\", stringsAsFactors = F,row.names = 1)");
            code.addRCode("states.click <- SpatialPolygonsDataFrame(states,click)");
            code.addRCode("nclr <- 5");
            code.addRCode("plotvar <- states.click$clicks");
            code.addRCode("plotclr <- brewer.pal(nclr, \"Set1\")");
            code.addRCode("class <- classIntervals(plotvar, nclr, style = \"quantile\")");
            code.addRCode("colcode <- findColours(class, plotclr, digits = 3)");
            code.addRCode("plot(states.click, col = colcode, border = \"grey\",axes = T)");
            code.addRCode("title(main = \"Clicks for Mailing\")");
            code.addRCode("legend(\"bottomleft\", legend = names(attr(colcode,\"table\")), fill = attr(colcode, \"palette\"))");
            System.out.println("Plot will be saved to : " + file);
            // code.endPlot();

            caller.setRCode(code);
            caller.runOnly();
            // code.showPlot(file);
            return file;
        } catch (Exception e) {
            System.out.println(e.toString());
            return file;
        }

    }

    private void getDashBoard()

    {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable("/usr/bin/Rscript");

            RCode code = new RCode();
            code.clear();
            code.addRCode("library(googleVis)");
          //  code.addRCode("pieChart <- read.csv(\"/home/gasawa/pieChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("pieChart <- read.csv(\"" + csvDir + "pieChart.csv\", header=TRUE,sep=\",\")");
          //  code.addRCode("bubbleChart <- read.csv(\"/home/gasawa/bubbleChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("bubbleChart <- read.csv(\"" + csvDir + "bubbleChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("M1 =gvisBubbleChart(data=bubbleChart, idvar = \"ID\", xvar = \"Age\", yvar = \"Click.Rate\",sizevar=\"Click.Rate\",  colorvar = \"ID\",list(title=\"Correlation between Age,Click Rate and Clicks of some states\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:24}\",vAxis= \"{title: 'Clicks Rate',titleTextStyle: {color:'blue',fontSize: '20'}}\",hAxis= \"{title: 'Age',titleTextStyle: {color:'blue',fontSize: '20'}}\"),chartid=\"bubbleChart\")");
            code.addRCode("M2 =gvisPieChart(data=pieChart, labelvar = \"Age\", numvar = \"PercentClicks\",  options=list(title=\"Clicks by Age Group\",titleTextStyle=\"{fontName:'Courier',fontSize:24}\"),chartid=\"pieChart\")");
            code.addRCode("M3=gvisMerge(M1, M2, horizontal = TRUE, chartid=\"DashBoard\")");
            code.addRCode("plot(M3)");
            //code.addRCode("areaChart <- read.csv(\"/home/gasawa/areaChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("areaChart <- read.csv(\"" + csvDir + "areaChart.csv\", header=TRUE,sep=\",\")");
            //code.addRCode("tableChart <- read.csv(\"/home/gasawa/tableChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("tableChart <- read.csv(\"" + csvDir + "tableChart.csv\", header=TRUE,sep=\",\")");
            code.addRCode("  M4 = gvisAreaChart(data=areaChart, yvar=c(\"Last.Chance..Earn.25.miles.per..1.on.Valentine.s.Day.Flowers...Gifts\", \"Save..10.on.3.Can.t.Miss.Valentine.s.Bestsellers\",\"SAVE.40..on.Valentine.s.Bestsellers..While.Supplies.Last.\",\"Thanks.for.letting.us.help.you.make.someone.smile\",\"X.Valentine.s.Free.Shipping.No.Service.Charge...Hurry..Ends.Today.\"),xvar=\"Day\",  options=list(title=\"Clicks - Campaigns/Day\",titleTextStyle=\"{color:'black',fontName:'Courier',fontSize:24}\",vAxis= \"{title: '%Clicks',titleTextStyle: {color:'blue',fontSize: '20'}}\",hAxis= \"{title: 'Day',titleTextStyle: {color:'blue',fontSize: '20'}}\",legend = \"{position: 'right', textStyle: {color: 'blue', fontSize: 12}}\"), chartid=\"areaChart\")");
            code.addRCode("M5 <- gvisTable(tableChart, options=list(page='enable', height=300)) ");
            code.addRCode("M6 =gvisMerge(M4, M5, horizontal = TRUE, chartid=\"AreaChartTable\")");
            code.addRCode("M7 =gvisMerge(M3, M6, horizontal = FALSE, chartid=\"Dashboard\")");

           // code.addRCode("cat(unlist(M7$html), file=\"/home/gasawa/DashBoard.html\")");
            code.addRCode("cat(unlist(M7$html), file=\"" + rootDir + "DashBoard.html\")");
            caller.setRCode(code);
            caller.runOnly();
            code.clear();


        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }


    }

    private void getTreeMap (){

    }
}
