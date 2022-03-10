package models;

public class CliColumn {
    private final String databaseColumn;
    private final String hikeSuggesterCliColumn;
    private final String formatString;
    private final String formatRegex;

    public CliColumn(String databaseColumn, String hikeSuggesterCliColumn, String formatString, String formatRegex) {
        this.databaseColumn = databaseColumn;
        this.hikeSuggesterCliColumn = hikeSuggesterCliColumn;
        this.formatString = formatString;
        this.formatRegex = formatRegex;
    }

    public String getDatabaseColumn() {
        return databaseColumn;
    }

    public String getHikeSuggesterCliColumn() {
        return hikeSuggesterCliColumn;
    }

    public String getFormatString() {
        return formatString;
    }

    public String getFormatRegex() {
        return formatRegex;
    }
}

