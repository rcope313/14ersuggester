package tester;

import utility.Utils;

public class TestUtils {

    String s0, s1, s2, s3;

    void initData() {
        s0 = "";
        s1 = "Longs Peak";
        s2 = "Long's Peak";
        s3 = "Long''s Peak";
    }

    void testInsertApostrophe (Tester t) {
        initData();
        t.checkExpect(Utils.insertApostrophe(s0), s0);
        t.checkExpect(Utils.insertApostrophe(s1), s1);
        t.checkExpect(Utils.insertApostrophe(s2), s3);


    }
}
