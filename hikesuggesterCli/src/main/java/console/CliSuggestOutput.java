package console;

import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRoute;
import models.TimeScore;
import org.assertj.core.util.VisibleForTesting;
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

    public List<TimeScore> getBestThreeTimesOfOneRouteLowConsequence(ImmutableStoredRoute route, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> allSixHourBlockTimes = getAllDayTimeSixHourBlocks(sevenDayForecast.get(0).getDate());
        ArrayList<Integer> lowConsequenceIndexes = getListOfIndexesByLowConsequence(allSixHourBlockTimes, sevenDayForecast);
        ArrayList<Map.Entry<Integer, Integer>> sortedIndexes = sortLowConsequenceEntriesByWindSpeedAndWindChill(lowConsequenceIndexes, sevenDayForecast);
        List<TimeScore> listOfBestTimes = transformSortedEntriesToListOfBestTimes(sortedIndexes, route);
        return listOfBestTimes.subList(0,3);
    }

    public List<TimeScore> getBestThreeTimesOfOneRouteHighConsequence(ImmutableStoredRoute route, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> allSixHourBlockTimes = getAllDayTimeSixHourBlocks(sevenDayForecast.get(0).getDate());
        ArrayList<Integer> highConsequenceIndexes = getListOfIndexesByHighConsequence(allSixHourBlockTimes, sevenDayForecast);
        ArrayList<Map.Entry<Integer, Integer>> sortedIndexes = sortHighConsequenceEntriesByWindSpeedAndWindChill(highConsequenceIndexes, sevenDayForecast);
        List<TimeScore> listOfBestTimes = transformSortedEntriesToListOfBestTimes(sortedIndexes, route);
        return listOfBestTimes.subList(0,3);
    }

    @VisibleForTesting
    static List<TimeScore> transformSortedEntriesToListOfBestTimes(ArrayList<Map.Entry<Integer, Integer>> sortedIndexes, ImmutableStoredRoute route) {
        List<TimeScore> sortedTimeScores = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : sortedIndexes) {
            TimeScore currentTimeScore = new TimeScore(route, entry.getKey(), entry.getValue());
            sortedTimeScores.add(currentTimeScore);
        }
        return sortedTimeScores;
    }
    @VisibleForTesting
    static ArrayList<Map.Entry<Integer, Integer>> sortLowConsequenceEntriesByWindSpeedAndWindChill(ArrayList<Integer> indexesByLowConsequence, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
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
    static ArrayList<Map.Entry<Integer, Integer>> sortHighConsequenceEntriesByWindSpeedAndWindChill(ArrayList<Integer> indexesByHighConsequence, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
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
    static int getMaxWindChill(int lowBound, int highBound, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
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
    static int getMaxWindSpeed(int lowBound, int highBound, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
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
    static ArrayList<Integer> getListOfIndexesByHighConsequence(ArrayList<Integer> dayTimeTwelveHourBlocksByIndex, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int idx : dayTimeTwelveHourBlocksByIndex) {
            if (idx + HIGH_CONSEQUENCE-1 < sevenDayForecast.size() && checkPrecipProbabilityByHighConsequence(sevenDayForecast, idx, idx + HIGH_CONSEQUENCE-1)) {
                indexes.add(idx);
           }
       }
       return indexes;
    }

    @VisibleForTesting
    static ArrayList<Integer> getListOfIndexesByLowConsequence(ArrayList<Integer> dayTimeSixHourBlocksByIndex, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int idx : dayTimeSixHourBlocksByIndex) {
            if (idx + LOW_CONSEQUENCE-1 < sevenDayForecast.size() && checkPrecipProbabilityByLowConsequence(sevenDayForecast, idx, idx + LOW_CONSEQUENCE-1)) {
                indexes.add(idx);
            }
        }
        return indexes;
    }

    @VisibleForTesting
    static boolean checkPrecipProbabilityByHighConsequence(ArrayList<ImmutableMountainForecast> sevenDayForecast, int lowerBound, int upperBound) {
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
    static boolean checkPrecipProbabilityByLowConsequence(ArrayList<ImmutableMountainForecast> sevenDayForecast, int lowerBound, int upperBound) {
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
