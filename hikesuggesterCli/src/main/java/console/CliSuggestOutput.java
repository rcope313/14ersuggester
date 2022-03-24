package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableMountainForecast;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRouteAndTrailhead;
import models.RouteForecast;
import models.TimeScore;
import org.assertj.core.util.VisibleForTesting;
import webscraper.MountainForecastScraper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliSuggestOutput {
    private final static int LOW_CONSEQUENCE = 6;
    private final static int HIGH_CONSEQUENCE = 12;

    public static void buildCliTableWeather(ImmutableSearchQuery query) {
        List<ImmutableStoredRouteAndTrailhead> routes = RoutesTrailheadsDao.get(query);
        List<RouteForecast> forecasts = createListOfRouteForecasts(routes);
        List<TimeScore> bestTimes = getBestTimesOfAllRoutes(forecasts);
        buildCliTableWeatherInputData(bestTimes.subList(0,4));
    }

    static void buildCliTableWeatherInputData(List<TimeScore> top5Times) {
        for (int idxRank = 0; idxRank < top5Times.size(); idxRank++) {
            cliTableWeatherDataFormatter(top5Times.get(idxRank), top5Times.get(idxRank).getTimeIndex(), idxRank);
        }
    }

    static void cliTableWeatherDataFormatter(TimeScore timeScore, int idxTime, int idxRank) {
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
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getTemperature());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getTemperature() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getTemperature() + "\n");
    }

    private static void showHourlyWeatherStatsWindChill(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindChill());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindChill() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindChill() + "\n");
    }

    private static void showHourlyWeatherStatsWindSpeed(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindSpeed());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindSpeed() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindSpeed() + "\n");
    }

    private static void showHourlyWeatherStatsWindDirection(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindDirection());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindDirection() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getWindDirection() + "\n");
    }

    private static void showHourlyWeatherStatsHumidity(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getHumidity());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getHumidity() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getHumidity() + "\n");
    }

    private static void showHourlyWeatherStatsCloudCover(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getCloudCover());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getCloudCover() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getCloudCover() + "\n");
    }

    private static void showHourlyWeatherStatsPrecipProbability(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipProbability());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipProbability() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipProbability() + "\n");
    }

    private static void showHourlyWeatherStatsPrecipAmount(TimeScore timeScore, int idxTime) {
        if (timeScore.getRouteForecast().isHighConsequence()) {
            while (idxTime < HIGH_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipAmount());
                idxTime++;
            }
        } else {
            while (idxTime < LOW_CONSEQUENCE + idxTime - 1) {
                System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipAmount() + ", ");
                idxTime++;
            }
        }
        System.out.print(timeScore.getRouteForecast().getSevenDayForecast().get(idxTime).getPrecipAmount() + "\n");
    }

    private static List<RouteForecast> createListOfRouteForecasts(List<ImmutableStoredRouteAndTrailhead> routes) {
        List<RouteForecast> forecasts = new ArrayList<>();
        routes.forEach((route) -> {
            try {
                forecasts.add(new RouteForecast(route, MountainForecastScraper.buildMountainForecasts(route)));
            } catch (Exception e) {
                e.printStackTrace();
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