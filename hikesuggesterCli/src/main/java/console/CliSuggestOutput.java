package console;

import database.models.ImmutableMountainForecast;
import org.assertj.core.util.VisibleForTesting;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class CliSuggestOutput {

    @VisibleForTesting
    static ArrayList<Integer> checkPrecipProbabilityByHighConsequence(ArrayList<Integer> dayTimeTwelveHourBlocksByIndex, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int idx : dayTimeTwelveHourBlocksByIndex) {
            if (idx + 11 < sevenDayForecast.size() && checkPrecipProbabilityByHighConsequence(sevenDayForecast, idx, idx + 11)) {
                indexes.add(idx);
           }
       }
       return indexes;
    }

    @VisibleForTesting
    static ArrayList<Integer> checkPrecipProbabilityByLowConsequence(ArrayList<Integer> dayTimeSixHourBlocksByIndex, ArrayList<ImmutableMountainForecast> sevenDayForecast) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int idx : dayTimeSixHourBlocksByIndex) {
            if (idx + 5 < sevenDayForecast.size() && checkPrecipProbabilityByLowConsequence(sevenDayForecast, idx, idx + 5)) {
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
