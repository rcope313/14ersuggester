package models;

public class CliColumn {
    public String databaseColumn;
    public String hikeSuggesterCliColumn;
    public String formatString;
    public String formatRegex;


    public CliColumn(String databaseColumn, String hikeSuggesterCliColumn, String formatString, String formatRegex) {
        this.databaseColumn = databaseColumn;
        this.hikeSuggesterCliColumn = hikeSuggesterCliColumn;
        this.formatString = formatString;
        this.formatRegex = formatRegex;


    }
}
