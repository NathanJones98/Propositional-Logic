package sat;

/** TODO: Your code here **/
public class EnhancedDPLL extends Solver{
    public EnhancedDPLL(String dimacs_file) {
        super(dimacs_file);
    }

    @Override
    public boolean unsat() {
        return false;
    }
}
