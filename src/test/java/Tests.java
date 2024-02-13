import sat.EnhancedDPLL;
import sat.ExhaustiveDPLL;
import org.junit.Test;
import sat.UnitPropDPLL;

import static org.junit.Assert.*;

public class Tests {


    @Test
    public void exhaustive3VarSatTest() {
        ExhaustiveDPLL exhaustiveDpll = new ExhaustiveDPLL("./data/sat3var.dimacs");
        assertFalse(exhaustiveDpll.unsat());
    }

    @Test
    public void exhaustive3VarUnsatTest(){
        ExhaustiveDPLL exhaustiveDpll = new ExhaustiveDPLL("./data/unsat3var.dimacs");
        assertTrue(exhaustiveDpll.unsat());
    }

    @Test
    public void unitProp3VarSatTest() {
        UnitPropDPLL unitpropdpll = new UnitPropDPLL("./data/sat3var.dimacs");
        assertFalse(unitpropdpll.unsat());

    }

    @Test
    public void unitProp3VarUnsatTest(){
        UnitPropDPLL unitpropdpll = new UnitPropDPLL("./data/unsat3var.dimacs");
        assertTrue(unitpropdpll.unsat());
    }

    @Test
    public void enhanced3VarSatTest() {
        EnhancedDPLL enhancedDpll = new EnhancedDPLL("./data/sat3var.dimacs");
        assertFalse(enhancedDpll.unsat());
    }

    @Test
    public void enhanced3VarUnsatTest(){
        EnhancedDPLL enhancedDpll = new EnhancedDPLL("./data/unsat3var.dimacs");
        assertTrue(enhancedDpll.unsat());
    }
}
