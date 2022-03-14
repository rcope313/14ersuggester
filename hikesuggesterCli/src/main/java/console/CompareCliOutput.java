package console;

import database.query.Query;
import models.CliColumn;
import models.CliColumnDesign;

import java.util.ArrayList;

public class CompareCliOutput extends CliOutput{

    @Override
    public void buildCliTable(Query query) {
        ArrayList<CliColumn> cliColumnFields = designateCliColumnFields();
        String querySyntax = query.createQuerySyntax();
        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(querySyntax, cliColumnFields);
        createDifferenceString(); 
    }

    ArrayList<CliColumn> designateAllCliColumnFields() {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);
        cliColumnFields.add(CliColumnDesign.GRADE);
        cliColumnFields.add(CliColumnDesign.TOTAL_GAIN);
        cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH);
        cliColumnFields.add(CliColumnDesign.ROUTE_URL);
        return cliColumnFields;
    }

    private void createDifferenceString() {
    }


}
