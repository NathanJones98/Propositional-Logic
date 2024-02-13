package sat;

/** TODO: Your code here **/
public class UnitPropDPLL extends Solver{
    public UnitPropDPLL(String dimacs_file) {
        super(dimacs_file);
    }

    @Override
    public boolean unsat() {
        return false;
    }
}
