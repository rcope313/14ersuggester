package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;
import models.RouteForecast;
import models.TimeScore;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webscraper.MountainForecastScraper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliSuggestOutput extends CliOutput {
    private final RoutesTrailheadsDao dao;
    private final MountainForecastScraper scraper;
    final private static Logger LOG = LoggerFactory.getLogger(CliSuggestOutput.class);
    private final static int LOW_CONSEQUENCE = 6;
    private final static int HIGH_CONSEQUENCE = 12;

    public CliSuggestOutput(RoutesTrailheadsDao dao, MountainForecastScraper scraper) {
        this.dao = dao;
        this.scraper = scraper;
    }

    public void buildCliTable(SearchQuery query) {
        List<ImmutableStoredRouteAndTrailhead> routes = dao.get(query);
        routes.forEach((route) -> {
            try {
                dao.update(route);
                LOG.info("Updated {}", route.getRouteUrl());
            } catch (Exception e) {
                LOG.warn("Unable to update {}", route.getRouteUrl());
            }
        });
        List<RouteForecast> forecasts = createListOfRouteForecasts(routes);
        List<TimeScore> bestTimes = getBestTimesOfAllRoutes(forecasts);
        if (bestTimes.size() > 4) {
            buildCliTableWeatherInputData(bestTimes.subList(0, 4));
        } else {
            buildCliTableWeatherInputData(bestTimes);
        }
    }

    @VisibleForTesting
    static void buildCliTableWeatherInputData(List<TimeScore> top5Times) {
        for (int idxRank = 0; idxRank < top5Times.size(); idxRank++) {
            cliTableWeatherDataFormatter(top5Times.get(idxRank), top5Times.get(idxRank).getTimeIndex(), idxRank);
        }
    }

    private static void cliTableWeatherDataFormatter(TimeScore timeScore, int idxTime, int idxRank) {
        idxRank++;
        System.out.print("Case " + idxRank + ":" + "\n");
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getDate() + "\n");
        System.out.print(timeScore.getRouteForecast().getRoute().getMountainName() + "\n");
        System.out.print(timeScore.getRouteForecast().getRoute().getRouteName() + "\n");
        showHourlyWeatherStatsTemperature(timeScore, idxTime);
        showHourlyWeatherStatsWindChill(timeScore, idxTime);
        showHourlyWeatherStatsWindSpeed(timeScore, idxTime);
        showHourlyWeatherStatsWindDirection(timeScore, idxTime);
        showHourlyWeatherStatsHumidity(timeScore, idxTime);
        showHourlyWeatherStatsCloudCover(timeScore, idxTime);
        showHourlyWeatherStatsPrecipProbability(timeScore, idxTime);
        showHourlyWeatherStatsPrecipAmount(timeScore, idxTime);
        System.out.print("\n");
    }

    private static void showHourlyWeatherStatsTemperature(TimeScore timeScore, int idxTime) {
        System.out.print("Temperature: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getTemperature() + ", ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getTemperature() + "\n");
    }

    private static void showHourlyWeatherStatsWindChill(TimeScore timeScore, int idxTime) {
        System.out.print("Wind Chill: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindChill() + ", ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindChill() + "\n");
    }

    private static void showHourlyWeatherStatsWindSpeed(TimeScore timeScore, int idxTime) {
        System.out.print("Wind Speed: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindSpeed() + ", ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindSpeed() + "\n");
    }

    private static void showHourlyWeatherStatsWindDirection(TimeScore timeScore, int idxTime) {
        System.out.print("Wind Direction: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindDirection() + ", ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindDirection() + "\n");
    }

    private static void showHourlyWeatherStatsHumidity(TimeScore timeScore, int idxTime) {
        System.out.print("Humidity: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getHumidity() + "%, ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getHumidity() + "%\n");
    }

    private static void showHourlyWeatherStatsCloudCover(TimeScore timeScore, int idxTime) {
        System.out.print("Cloud Cover: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getCloudCover() + "%, ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getCloudCover() + "%\n");
    }

    private static void showHourlyWeatherStatsPrecipProbability(TimeScore timeScore, int idxTime) {
        System.out.print("Precip Probability: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipProbability() + "%, ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipProbability()+ "%\n");
    }

    private static void showHourlyWeatherStatsPrecipAmount(TimeScore timeScore, int idxTime) {
        System.out.print("Precip Amount: ");
        int displayCount = timeScore.getRouteForecast().isHighConsequence() ? HIGH_CONSEQUENCE - 1 : LOW_CONSEQUENCE - 1;
        while (displayCount > 0) {
            System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipAmount() + ", ");
            idxTime++;
            displayCount--;
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipAmount() + "\n");
    }

    private List<RouteForecast> createListOfRouteForecasts(List<ImmutableStoredRouteAndTrailhead> routes) {
        List<RouteForecast> forecasts = new ArrayList<>();
        routes.forEach((route) -> {
            try {
                forecasts.add(new RouteForecast(route, scraper.buildMountainForecasts(route)));
                LOG.info("Created 7-day route forecast for route {}", route.getRouteUrl());
            } catch (Exception e) {
                LOG.warn("Unable to create 7-day route forecast for route {}", route.getRouteUrl());
            }
        });
        return forecasts;
    }

    @VisibleForTesting
    static List<TimeScore> getBestTimesOfAllRoutes(List<RouteForecast> routeForecasts) {
        List<TimeScore> bestTimes = new ArrayList<>();
        for (RouteForecast routeForecast : routeForecasts) {
            if (routeForecast.isHighConsequence()) {
                bestTimes.addAll(getBestThreeTimesOfOneRouteHighConsequence(routeForecast.getRoute(), routeForecast.getSevenDayForecast()));
            } else {
                bestTimes.addAll(getBestThreeTimesOfOneRouteLowConsequence(routeForecast.getRoute(), routeForecast.getSevenDayForecast()));
            }
        }
        bestTimes.sort((x,y) -> y.getScore() - x.getScore());
        return bestTimes;
    }

    @VisibleForTesting
    static List<TimeScore> getBestThreeTimesOfOneRouteLowConsequence(ImmutableStoredRouteAndTrailhead route, List<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> allSixHourBlockTimes = getAllDayTimeSixHourBlocks(sevenDayForecast.get(0).getDate());
        ArrayList<Integer> lowConsequenceIndexes = getListOfIndexesByLowConsequence(allSixHourBlockTimes, sevenDayForecast);
        ArrayList<Map.Entry<Integer, Integer>> sortedIndexes = sortLowConsequenceEntriesByWindSpeedAndWindChill(lowConsequenceIndexes, sevenDayForecast);
        List<TimeScore> listOfBestTimes = transformSortedEntriesToListOfBestTimes(sortedIndexes, sevenDayForecast, route);
        if (listOfBestTimes.size() < 3) {
            return listOfBestTimes;
        } else {
            return listOfBestTimes.subList(0,3);
        }
    }

    @VisibleForTesting
    static List<TimeScore> getBestThreeTimesOfOneRouteHighConsequence(ImmutableStoredRouteAndTrailhead route, List<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> allSixHourBlockTimes = getAllDayTimeTwelveHourBlocks(sevenDayForecast.get(0).getDate());
        ArrayList<Integer> highConsequenceIndexes = getListOfIndexesByHighConsequence(allSixHourBlockTimes, sevenDayForecast);
        ArrayList<Map.Entry<Integer, Integer>> sortedIndexes = sortHighConsequenceEntriesByWindSpeedAndWindChill(highConsequenceIndexes, sevenDayForecast);
        List<TimeScore> listOfBestTimes = transformSortedEntriesToListOfBestTimes(sortedIndexes, sevenDayForecast, route);
        if (listOfBestTimes.size() < 3) {
            return listOfBestTimes;
        } else {
            return listOfBestTimes.subList(0,3);
        }
    }

    @VisibleForTesting
    static List<TimeScore> transformSortedEntriesToListOfBestTimes(List<Map.Entry<Integer, Integer>> sortedIndexes, List<ImmutableMountainForecast> sevenDayForecast, ImmutableStoredRouteAndTrailhead route) {
        List<TimeScore> sortedTimeScores = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : sortedIndexes) {
            TimeScore currentTimeScore = new TimeScore(new RouteForecast(route, sevenDayForecast), entry.getKey(), entry.getValue());
            sortedTimeScores.add(currentTimeScore);
        }
        return sortedTimeScores;
    }

    @VisibleForTesting
    static ArrayList<Map.Entry<Integer, Integer>> sortLowConsequenceEntriesByWindSpeedAndWindChill(List<Integer> indexesByLowConsequence, List<ImmutableMountainForecast> sevenDayForecast) {
        HashMap<Integer,Integer> idxMap = new HashMap<>();

        for (int idx : indexesByLowConsequence) {
            int maxWindChill = getMaxWindChill(idx, idx + LOW_CONSEQUENCE, sevenDayForecast);
            int maxWindSpeed = getMaxWindSpeed(idx, idx + LOW_CONSEQUENCE, sevenDayForecast);
            idxMap.put(idx, maxWindChill - (maxWindSpeed * 2));
        }
        ArrayList<Map.Entry<Integer, Integer>> mapList = new ArrayList<>(idxMap.entrySet());
        mapList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return mapList;
    }

    @VisibleForTesting
    static ArrayList<Map.Entry<Integer, Integer>> sortHighConsequenceEntriesByWindSpeedAndWindChill(List<Integer> indexesByHighConsequence, List<ImmutableMountainForecast> sevenDayForecast) {
        HashMap<Integer,Integer> idxMap = new HashMap<>();

        for (int idx : indexesByHighConsequence) {
            int maxWindChill = getMaxWindChill(idx, idx + HIGH_CONSEQUENCE, sevenDayForecast);
            int maxWindSpeed = getMaxWindSpeed(idx, idx + HIGH_CONSEQUENCE, sevenDayForecast);
            idxMap.put(idx, maxWindChill - (maxWindSpeed * 2));
        }
        ArrayList<Map.Entry<Integer, Integer>> mapList = new ArrayList<>(idxMap.entrySet());
        mapList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return mapList;
    }

    @VisibleForTesting
    static int getMaxWindChill(int lowBound, int highBound, List<ImmutableMountainForecast> sevenDayForecast) {
        int highestWindChill = 0;
        while (lowBound < highBound) {
            int currentWindChill= Integer.parseInt(sevenDayForecast.get(lowBound).getWindChill());
            if (currentWindChill > highestWindChill) {
                highestWindChill = currentWindChill;
            }
            lowBound++;
        }
        return highestWindChill;
    }

    @VisibleForTesting
    static int getMaxWindSpeed(int lowBound, int highBound, List<ImmutableMountainForecast> sevenDayForecast) {
        int highestWindSpeed = 0;
        while (lowBound < highBound) {
            int currentWindSpeed = Integer.parseInt(sevenDayForecast.get(lowBound).getWindSpeed());
            if (currentWindSpeed > highestWindSpeed) {
                highestWindSpeed = currentWindSpeed;
            }
            lowBound++;
        }
        return highestWindSpeed;
    }

    @VisibleForTesting
    static ArrayList<Integer> getListOfIndexesByHighConsequence(List<Integer> dayTimeTwelveHourBlocksByIndex, List<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int idx : dayTimeTwelveHourBlocksByIndex) {
            if (idx + HIGH_CONSEQUENCE-1 < sevenDayForecast.size() && checkPrecipProbabilityByHighConsequence(sevenDayForecast, idx, idx + HIGH_CONSEQUENCE-1)) {
                indexes.add(idx);
           }
       }
       return indexes;
    }

    @VisibleForTesting
    static ArrayList<Integer> getListOfIndexesByLowConsequence(List<Integer> dayTimeSixHourBlocksByIndex, List<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int idx : dayTimeSixHourBlocksByIndex) {
            if (idx + LOW_CONSEQUENCE-1 < sevenDayForecast.size() && checkPrecipProbabilityByLowConsequence(sevenDayForecast, idx, idx + LOW_CONSEQUENCE-1)) {
                indexes.add(idx);
            }
        }
        return indexes;
    }

    @VisibleForTesting
    static boolean checkPrecipProbabilityByHighConsequence(List<ImmutableMountainForecast> sevenDayForecast, int lowerBound, int upperBound) {
        int idx = lowerBound;
        while (idx <= upperBound) {
            if (Integer.parseInt(sevenDayForecast.get(idx).getPrecipProbability()) != 0) {
                return false;
            }
            idx++;
        }
        return true;
    }

    @VisibleForTesting
    static boolean checkPrecipProbabilityByLowConsequence(List<ImmutableMountainForecast> sevenDayForecast, int lowerBound, int upperBound) {
        ArrayList<Integer> precipProbs = new ArrayList<>();
        int idx = lowerBound;
        while (idx <= upperBound) {
            int precipProb = Integer.parseInt(sevenDayForecast.get(idx).getPrecipProbability());
            if (precipProb > 15) {
                return false;
            }
            precipProbs.add(precipProb);
            idx++;
        }
        precipProbs.sort(Comparator.naturalOrder());
        if (precipProbs.size() % 2 == 0) {
            return (precipProbs.get(precipProbs.size()/2) + precipProbs.get(precipProbs.size()/2)-1) / 2 == 0;
        } else {
            return precipProbs.get(precipProbs.size() / 2) == 0;
        }
    }

    @VisibleForTesting
    static ArrayList<Integer> getAllDayTimeSixHourBlocks(String strTime){
        ArrayList<Integer> dayTimeSixHourBlocksByIndex = new ArrayList<>();
        int idx = 0;
        int days = 6;
        LocalDateTime startTime = parseStrToLocalDateTime(strTime);

        while (startTime.getHour() != 5) {
            idx++;
            startTime = startTime.plusHours(1);
        }

        while (days > 0) {
            dayTimeSixHourBlocksByIndex.add(idx);
            dayTimeSixHourBlocksByIndex.add(idx + 1);
            dayTimeSixHourBlocksByIndex.add(idx + 2);
            dayTimeSixHourBlocksByIndex.add(idx + 3);
            dayTimeSixHourBlocksByIndex.add(idx + 4);
            dayTimeSixHourBlocksByIndex.add(idx + 5);
            idx += 24;
            days--;
        }
        return dayTimeSixHourBlocksByIndex;
    }

    @VisibleForTesting
    static ArrayList<Integer> getAllDayTimeTwelveHourBlocks(String strTime){
        ArrayList<Integer> dayTimeTwelveHourBlocksByIndex = new ArrayList<>();
        int idx = 0;
        int days = 6;
        LocalDateTime startTime = parseStrToLocalDateTime(strTime);

        while (startTime.getHour() != 4) {
            idx++;
            startTime = startTime.plusHours(1);
        }

        while (days > 0) {
            dayTimeTwelveHourBlocksByIndex.add(idx);
            dayTimeTwelveHourBlocksByIndex.add(idx + 1);
            dayTimeTwelveHourBlocksByIndex.add(idx + 2);
            idx += 24;
            days--;
        }
        return dayTimeTwelveHourBlocksByIndex;
    }

    @VisibleForTesting
    static LocalDateTime parseStrToLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(str, formatter);
    }
}