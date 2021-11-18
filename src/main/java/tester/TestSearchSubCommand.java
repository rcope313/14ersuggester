//package tester;
//
//import mysql.query.MySqlSearchQuery;
//import picocli.CommandLine;
//import subcommands.SearchSubCommand;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class TestSearchSubCommand {
//
//    int exitCode0, exitCode1, exitCode2, exitCode3, exitCode4, exitCode5, exitCode6, exitCode7, exitCode8, exitCode9;
//    MySqlSearchQuery q0, q1, q2, q3, q4, q5, q6, q7, q8, q9;
//    SearchSubCommand app;
//    CommandLine cmd0, cmd1, cmd2, cmd3, cmd4, cmd5, cmd6, cmd7, cmd8, cmd9;
//    StringWriter sw0, sw1, sw2, sw3, sw4, sw5, sw6, sw7, sw8, sw9;
//
//    void initData() {
//
//        app = new SearchSubCommand();

//        cmd0 = new CommandLine(app);
//        sw0 = new StringWriter();
//        cmd0.setOut(new PrintWriter(sw0));
//        exitCode0 = cmd0.execute();
//        q0 = new MySqlSearchQuery();
//
//        cmd1 = new CommandLine(app);
//        sw1 = new StringWriter();
//        cmd1.setOut(new PrintWriter(sw1));
//        exitCode1 = cmd1.execute("--m", "Mt. Elbert", "--m", "Longs Peak", "--m", "Mt. Antero");
//        q1 = new MySqlSearchQuery();
//            q1.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Elbert", "Longs Peak", "Mt. Antero")));

//        cmd2 = new CommandLine(app);
//        sw2 = new StringWriter();
//        cmd2.setOut(new PrintWriter(sw2));
//        exitCode2 = cmd2.execute("--m", "Mt. Elbert", "--m", "Longs Peak", "--m", "Mt. Antero", "--st");
//        q2 = new MySqlSearchQuery();
//            q2.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Elbert", "Longs Peak", "Mt. Antero")));
//            q2.setStandardRoute(true);
//
//        cmd3 = new CommandLine(app);
//        sw3 = new StringWriter();
//        cmd3.setOut(new PrintWriter(sw3));
//        exitCode3 = cmd3.execute("-v", "--m", "Mt. Elbert", "--m", "Longs Peak", "--m", "Mt. Antero", "--tg", "5000", "--rl", "4.5");
//        q3 = new MySqlSearchQuery();
//            q3.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Elbert", "Longs Peak", "Mt. Antero")));
//            q3.setTotalGain(5000);
//            q3.setRouteLength(4.5);
//            q3.setVerbose(true);
//
//        cmd4 = new CommandLine(app);
//        sw4 = new StringWriter();
//        cmd4.setOut(new PrintWriter(sw4));
//        exitCode4 = cmd4.execute("--sn", "--r", "Box Creek Couloirs", "--r", "South Face");
//        q4 = new MySqlSearchQuery();
//            q4.setRouteNames (new ArrayList<>(Arrays.asList("Box Creek Couloirs", "South Face")));
//            q4.setSnowRoute(true);
//
//        cmd5 = new CommandLine(app);
//        sw5 = new StringWriter();
//        cmd5.setOut(new PrintWriter(sw5));
//        exitCode5 = cmd5.execute("--ste", "8000", "--sue", "14000");
//        q5 = new MySqlSearchQuery();
//            q5.setStartElevation(8000);
//            q5.setSummitElevation(14000);
//
//        cmd6 = new CommandLine(app);
//        sw6 = new StringWriter();
//        cmd6.setOut(new PrintWriter(sw6));
//        exitCode6 = cmd6.execute("-exposure", "low", "-routefinding", "low", "-rockfallpotential", "low", "-commitment", "low");
//        q6 = new MySqlSearchQuery();
//            q6.setExposure("low");
//            q6.setRouteFinding("low");
//            q6.setRockfallPotential("low");
//            q6.setCommitment("low");
//
//
//        cmd7 = new CommandLine(app);
//        sw7 = new StringWriter();
//        cmd7.setOut(new PrintWriter(sw7));
//        exitCode7 = cmd7.execute("--g", "1", "--g", "2", "--g", "3", "--gq", "Easy", "--gq", "Difficult");
//        q7 = new MySqlSearchQuery();
//            q7.setGrades(new ArrayList<>(Arrays.asList(1,2,3)));
//            q7.setGradeQualities(new ArrayList<>(Arrays.asList("Easy", "Difficult")));
//
//        cmd8 = new CommandLine(app);
//        sw8 = new StringWriter();
//        cmd8.setOut(new PrintWriter(sw8));
//        exitCode8 = cmd8.execute("-coordinates", "39.15177, -106.41918 ", "-coordinates", "39.06788, -106.50515 ", "-roaddifficulties", "1", "-roaddifficulties", "2", "-roaddifficulties", "3" );
//        q8 = new MySqlSearchQuery();
//            q8.setTrailheadCoordinates(new ArrayList<>(Arrays.asList("39.15177, -106.41918 ", "39.06788, -106.50515 ")));
//            q8.setRoadDifficulties(new ArrayList<>(Arrays.asList(1,2,3)));
//
//
//        cmd9 = new CommandLine(app);
//        sw9 = new StringWriter();
//        cmd9.setOut(new PrintWriter(sw9));
//        exitCode9 = cmd9.execute("-routeurls", "https://www.14ers.com/route.php?route=elbe1", "-routeurls", "https://www.14ers.com/route.php?route=elbe2", "-trailheadurls", "/trailheadsview.php?thparm=sw01", "-trailheadurls", "/trailheadsview.php?thparm=sw03");
//        q9 = new MySqlSearchQuery();
//            q9.setTrailheadUrls(new ArrayList<>(Arrays.asList("/trailheadsview.php?thparm=sw01", "/trailheadsview.php?thparm=sw03")));
//            q9.setRouteUrls(new ArrayList<>(Arrays.asList("https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=elbe2")));

//    }
//
//    void testSearchSubcommand (Tester t) {
//        initData();
////        t.checkExpect(sw0.toString(), q0.createSearchQuery()); this works
//        t.checkExpect(sw1.toString(), q1.createSearchQuery());
////        t.checkExpect(sw2.toString(), q2.createSearchQuery()); this works
////        t.checkExpect(sw3.toString(), q3.createSearchQuery()); this works
////        t.checkExpect(sw4.toString(), q4.createSearchQuery()); this works
////        t.checkExpect(sw5.toString(), q5.createSearchQuery()); this works
////        t.checkExpect(sw6.toString(), q6.createSearchQuery()); this works
////        t.checkExpect(sw7.toString(), q7.createSearchQuery()); this works
////        t.checkExpect(sw8.toString(), q8.createSearchQuery()); this works
////        t.checkExpect(sw9.toString(), q9.createSearchQuery()); this works
//
//
//
//    }
//
//
//
//}
